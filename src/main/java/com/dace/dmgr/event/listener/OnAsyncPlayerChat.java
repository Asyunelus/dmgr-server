package com.dace.dmgr.event.listener;

import com.dace.dmgr.DMGR;
import com.dace.dmgr.lobby.User;
import com.dace.dmgr.system.Cooldown;
import com.dace.dmgr.system.CooldownManager;
import com.dace.dmgr.util.SoundUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static com.dace.dmgr.system.HashMapList.userMap;

public class OnAsyncPlayerChat implements Listener {
    @EventHandler
    public static void event(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        Player player = event.getPlayer();
        User user = userMap.get(player);

        if (!player.isOp()) {
            if (CooldownManager.getCooldown(user, Cooldown.CHAT) > 0) {
                player.sendMessage(DMGR.PREFIX.CHAT_WARN + "채팅을 천천히 하십시오.");
                return;
            }
            CooldownManager.setCooldown(user, Cooldown.CHAT);
        }

        Bukkit.getServer().broadcastMessage(String.format("<%s> %s", player.getDisplayName(), event.getMessage()));
        Bukkit.getOnlinePlayers().forEach((Player player2) -> {
            User user2 = userMap.get(player2);
            SoundUtil.play(user2.getUserConfig().getChatSound().getSound(), 1000F, 1.414F, player2);
        });
    }
}
