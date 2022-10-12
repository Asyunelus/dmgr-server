package com.dace.dmgr.combat.character.arkace.action;

import com.dace.dmgr.combat.Bullet;
import com.dace.dmgr.combat.Combat;
import com.dace.dmgr.combat.CombatUtil;
import com.dace.dmgr.combat.action.Reloadable;
import com.dace.dmgr.combat.action.Weapon;
import com.dace.dmgr.combat.action.WeaponController;
import com.dace.dmgr.combat.entity.CombatUser;
import com.dace.dmgr.combat.entity.ICombatEntity;
import com.dace.dmgr.gui.ItemBuilder;
import com.dace.dmgr.system.Cooldown;
import com.dace.dmgr.system.CooldownManager;
import com.dace.dmgr.system.TextIcon;
import com.dace.dmgr.system.task.TaskTimer;
import com.dace.dmgr.util.SoundPlayer;
import com.dace.dmgr.util.VectorUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class ArkaceWeapon extends Weapon implements Reloadable {
    public static final int DAMAGE = 75;
    public static final int DAMAGE_DISTANCE = 25;
    public static final long COOLDOWN = (long) (0.1 * 20);
    public static final int CAPACITY = 30;
    public static final long RELOAD_DURATION = (long) (1.4 * 20);
    public static final long RELOAD_DURATION_FULL = (long) (1.9 * 20);
    private static final ArkaceWeapon instance = new ArkaceWeapon();

    public ArkaceWeapon() {
        super("HLN-12", ItemBuilder.fromCSItem("HLN-12").setLore(
                "",
                "§f뛰어난 안정성을 가진 전자동 돌격소총입니다.",
                "§7사격§f하여 " + TextIcon.DAMAGE + " §c피해§f를 입힙니다.",
                "",
                "§f" + TextIcon.DAMAGE + "    " + DAMAGE + " (" + DAMAGE_DISTANCE + "m) - " + DAMAGE / 2 + " (" + DAMAGE_DISTANCE * 2 + "m)",
                "§f" + TextIcon.ATTACK_SPEED + "    0.1초",
                "§f" + TextIcon.CAPACITY + "    30+1발",
                "§f" + TextIcon.CAPACITY + TextIcon.COOLDOWN + "  1.4초 (1.9초)",
                "",
                "§7§l[우클릭] §f사용 §7§l[Q] §f재장전").build());
    }

    public static ArkaceWeapon getInstance() {
        return instance;
    }

    @Override
    public long getCooldown() {
        return COOLDOWN;
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    @Override
    public long getReloadDuration() {
        return RELOAD_DURATION;
    }

    @Override
    public long getReloadDurationFull() {
        return RELOAD_DURATION_FULL;
    }

    @Override
    public void use(CombatUser combatUser, WeaponController weaponController) {
        CooldownManager.setCooldown(combatUser, Cooldown.NO_SPRINT, 4);
        if (combatUser.getSkillController(ArkaceP1.getInstance()).isUsing())
            weaponController.setCooldown(3);
        if (!weaponController.isCooldownFinished())
            return;

        Location location = combatUser.getEntity().getLocation();
        SoundPlayer.play("random.gun2.scarlight_1", location, 3F, 1F);
        SoundPlayer.play("random.gun_reverb", location, 5F, 1.2F);
        CombatUtil.sendRecoil(combatUser, RECOIL.UP, RECOIL.SIDE, RECOIL.UP_SPREAD, RECOIL.SIDE_SPREAD, 2, 2F);
        CombatUtil.applyBulletSpread(combatUser, SPREAD.INCREMENT, SPREAD.RECOVERY, SPREAD.MAX);
        weaponController.consume(1);
        new Bullet(combatUser, 7) {
            @Override
            public void trail(Location location) {
                Location trailLoc = location.add(VectorUtil.getPitchAxis(location).multiply(-0.2)).add(0, -0.2, 0);
                location.getWorld().spawnParticle(Particle.CRIT, trailLoc, 1, 0, 0, 0, 0);
            }

            @Override
            public void onHitEntity(Location location, ICombatEntity target) {
                Combat.attack(combatUser, target, DAMAGE, "", false, false);
            }
        }.shoot(combatUser.getBulletSpread());
    }

    @Override
    public void onReload(CombatUser combatUser, WeaponController weaponController) {
        new TaskTimer(1, RELOAD_DURATION_FULL) {
            @Override
            public boolean run(int i) {
                if (!weaponController.isReloading())
                    return false;

                switch (i) {
                    case 4:
                        SoundPlayer.play(Sound.BLOCK_PISTON_CONTRACT, combatUser.getEntity().getLocation(), 0.6F, 1.6F);
                        break;
                    case 5:
                        SoundPlayer.play(Sound.ENTITY_VILLAGER_NO, combatUser.getEntity().getLocation(), 0.6F, 1.9F);
                        break;
                    case 22:
                        SoundPlayer.play(Sound.ENTITY_PLAYER_HURT, combatUser.getEntity().getLocation(), 0.6F, 0.5F);
                        break;
                    case 23:
                        SoundPlayer.play(Sound.ITEM_FLINTANDSTEEL_USE, combatUser.getEntity().getLocation(), 0.6F, 1F);
                        break;
                    case 24:
                        SoundPlayer.play(Sound.ENTITY_VILLAGER_YES, combatUser.getEntity().getLocation(), 0.6F, 1.8F);
                        break;
                    case 34:
                        SoundPlayer.play(Sound.ENTITY_WOLF_SHAKE, combatUser.getEntity().getLocation(), 0.6F, 1.7F);
                        break;
                    case 36:
                        SoundPlayer.play(Sound.BLOCK_IRON_DOOR_OPEN, combatUser.getEntity().getLocation(), 0.6F, 1.8F);
                        break;
                }

                return true;
            }
        };
    }

    public static class RECOIL {
        static final float UP = 0.6F;
        static final float SIDE = 0.04F;
        static final float UP_SPREAD = 0.1F;
        static final float SIDE_SPREAD = 0.06F;
    }

    public static class SPREAD {
        static final float INCREMENT = 0.15F;
        static final float RECOVERY = 1F;
        static final float MAX = 2.3F;
    }
}
