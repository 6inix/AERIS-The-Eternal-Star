package com.ashenfall.game.entities.player;

public class PlayerEffects {
    private int invincibilityTimer = 0;
    private int hitStunTimer = 0;

    public void update() {
        if (invincibilityTimer > 0) invincibilityTimer--;
        if (hitStunTimer > 0) hitStunTimer--;
    }

    public void triggerDamage(int invincFrames, int hitStunFrames) {
        this.invincibilityTimer = invincFrames;
        this.hitStunTimer = hitStunFrames;
    }

    public void triggerInvincibility(int frames) {
        this.invincibilityTimer = frames;
    }

    public boolean isInvincible() { return invincibilityTimer > 0; }
    public boolean isHitStunned() { return hitStunTimer > 0; }
    public boolean isFlashing() { return invincibilityTimer % 4 >= 2; }
    public int getInvincibilityTimer() { return invincibilityTimer; }
    public int getHitStunTimer() { return hitStunTimer; }
}
