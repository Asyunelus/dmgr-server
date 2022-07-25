package com.dace.dmgr.gui.slot;

import org.bukkit.Material;

public enum DisplaySlot implements ISlotItem {
    EMPTY((short) 1),
    EMPTY_LEFT((short) 2),
    EMPTY_RIGHT((short) 3),
    EMPTY_UP((short) 4),
    EMPTY_DOWN((short) 5),
    DISABLED((short) 6),
    ENABLED((short) 7);

    public final static Material MATERIAL = Material.CARROT_STICK;
    private final short damage;

    DisplaySlot(short damage) {
        this.damage = damage;
    }

    @Override
    public String getName() {
        return "§f";
    }

    @Override
    public short getDamage() {
        return damage;
    }
}
