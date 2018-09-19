package com.synchro.runnity.Models;

/**
 * Created by Shirco on 01/03/2016.
 */
public class Achievements {
    private Achievement[] achievements_done;
    private Achievement[] achievements_fail;
    private Achievement[] achievements;

    public Achievements() {
        this.achievements = new Achievement[]{};
        this.achievements_done = new Achievement[]{};
        this.achievements_fail = new Achievement[]{};
    }

    public Achievement[] getAchievements_done() {
        return achievements_done;
    }

    public void setAchievements_done(Achievement[] achievements_done) {
        this.achievements_done = achievements_done;
    }

    public Achievement[] getAchievements_fail() {
        return achievements_fail;
    }

    public void setAchievements_fail(Achievement[] achievements_fail) {
        this.achievements_fail = achievements_fail;
    }

    public Achievement[] getAchievements() {
        return achievements;
    }

    public void setAchievements(Achievement[] achievements) {
        this.achievements = achievements;
    }
}

