package com.ashenfall.game.entities.player;

public class PlayerStats {
    private int health = 6;
    private int maxHealth = 6;
    private float stamina = 100.0f;
    private float maxStamina = 100.0f;

    private float shield = 200.0f;
    private float maxShield = 200.0f;
    private boolean shieldBroken = false;

    public int getHealth() { return health; }
    public void setHealth(int h) { this.health = h; }
    public int getMaxHealth() { return maxHealth; }
    public float getStamina() { return stamina; }
    public void setStamina(float s) { this.stamina = s; }
    public float getMaxStamina() { return maxStamina; }

    public float getShield() { return shield; }
    public void setShield(float s) { this.shield = s; }
    public float getMaxShield() { return maxShield; }
    public boolean isShieldBroken() { return shieldBroken; }

    public void update(boolean isCrouching) {
        // Regenerate stamina
        if (stamina < maxStamina) {
            stamina += 0.4f;
            if (stamina > maxStamina) stamina = maxStamina;
        }

        // Regenerate shield when not crouching
        if (!isCrouching && shield < maxShield) {
            shield += 0.8f;
            if (shield >= maxShield) {
                shield = maxShield;
                shieldBroken = false;
            }
        }
    }

    public boolean consumeStamina(float amount) {
        if (stamina >= amount) {
            stamina -= amount;
            return true;
        }
        return false;
    }

    public boolean absorbShieldDamage(int amount, boolean isCrouching) {
        if (isCrouching && shield > 0 && !shieldBroken) {
            float shieldDmg = amount * 35.0f; // 35 shield points per HP hit
            shield -= shieldDmg;
            if (shield <= 0) {
                shield = 0;
                shieldBroken = true;
            }
            return true; // Damage absorbed by shield!
        }
        return false;
    }

    public void damage(int amount) {
        health -= amount;
        if (health < 0) health = 0;
    }

    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) health = maxHealth;
    }
}
