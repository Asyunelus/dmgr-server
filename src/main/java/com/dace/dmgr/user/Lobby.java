package com.dace.dmgr.user;

import com.dace.dmgr.DMGR;
import com.dace.dmgr.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static com.dace.dmgr.system.EntityList.userList;

public class Lobby {
    public static Location lobby = new Location(Bukkit.getWorld("DMGR"), 72.5, 64, 39.5, 90, 0);

    public static void spawn(Player player) {
        User user = userList.get(player.getUniqueId());

        player.teleport(lobby);
        user.reset();
    }

    public static void lobbyTick(Player player) {
        User user = userList.get(player.getUniqueId());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (userList.get(player.getUniqueId()) == null)
                    cancel();

                if (user.getUserConfig().isNightVision())
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 0, false, false));
                else
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);

                int reqXp = user.getNextLevelXp();
                int reqRank = user.getNextTierScore();
                int curRank = user.getCurrentTierScore();
                user.lobbySidebar.setName("§b§n" + user.getName());
                user.lobbySidebar.setAll(
                        "§f",
                        "§e보유 중인 돈",
                        "§6" + String.format("%,d", user.getMoney()),
                        "§f§f",
                        "§f레벨 : " + user.getLevelPrefix(),
                        StringUtil.getBar(user.getXp(), reqXp, ChatColor.DARK_GREEN) + " §2[" + user.getXp() + "/" + reqXp + "]",
                        "§f§f§f",
                        "§f랭크 : " + user.getTierPrefix(),
                        StringUtil.getBar(user.getRank() - curRank, reqRank - curRank, ChatColor.DARK_AQUA) + " §3[" + user.getRank() + "/" + reqRank + "]"
                );
            }
        }.runTaskTimer(DMGR.getPlugin(), 0, 20);
    }
}
