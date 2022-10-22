package com.dace.dmgr.combat.entity;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import static com.dace.dmgr.system.HashMapList.temporalEntityMap;

public class TemporalEntity<T extends LivingEntity> extends CombatEntity<T> {
    protected TemporalEntity(EntityType entityType, String name, Location location, Hitbox hitbox) {
        super((T) location.getWorld().spawnEntity(location, entityType), name, hitbox);
        temporalEntityMap.put(getEntity(), this);
    }

    public void remove() {
        entity.setHealth(0);
        entity.remove();
    }
}
