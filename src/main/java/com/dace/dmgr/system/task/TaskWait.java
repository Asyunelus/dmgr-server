package com.dace.dmgr.system.task;

import com.dace.dmgr.DMGR;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class TaskWait {
    protected final long delay;

    public TaskWait(long delay) {
        this.delay = delay;
        execute();
    }

    private void execute() {
        new BukkitRunnable() {
            @Override
            public void run() {
                TaskWait.this.run();

            }
        }.runTaskLater(DMGR.getPlugin(), delay);
    }

    public abstract void run();
}
