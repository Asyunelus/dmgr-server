package com.dace.dmgr.event.listener;

import com.dace.dmgr.combat.entity.CombatUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

import static com.dace.dmgr.system.HashMapList.combatUserHashMap;

public class OnPlayerItemHeld implements Listener {
    @EventHandler
    public static void event(PlayerItemHeldEvent event) {
        CombatUser combatUser = combatUserHashMap.get(event.getPlayer());

        if (combatUser != null) {
            event.setCancelled(true);
            combatUser.onItemHeld(event.getNewSlot());
        }
    }
}
