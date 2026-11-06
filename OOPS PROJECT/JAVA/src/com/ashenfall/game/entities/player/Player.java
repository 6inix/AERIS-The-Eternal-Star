package com.ashenfall.game.entities.player;

import com.ashenfall.game.engine.InputHandler;
import com.ashenfall.game.entities.Entity;
import com.ashenfall.game.entities.Particle;
import com.ashenfall.game.world.TileMap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

public class Player extends Entity {
    private PlayerStats stats;
    private PlayerMovement movement;
    private PlayerCombat combat;
    private PlayerAnimation animation;
    private PlayerCollision collision;
    private PlayerEffects effects;

    // Direct public fields synced for backward compatibility with HUD & GamePanel
    public float stamina = 100;
    public float maxStamina = 100;
    public float shield = 200;
    public float maxShield = 200;
    public boolean isCrouching = false;
    public boolean isAttacking = false;
    public Rectangle attackHitbox = null;
    public int jumpCount = 0;

    public Player(float x, float y) {
        super(x, y, 36, 54, 6);
        this.stats = new PlayerStats();
        this.movement = new PlayerMovement(this);
        this.combat = new PlayerCombat(this);
        this.animation = new PlayerAnimation(this);
        this.collision = new PlayerCollision(this);
        this.effects = new PlayerEffects();
    }

    public void handleInput(InputHandler input, List<Particle> particles) {
        if (!alive) return;
        movement.handleInput(input, particles, stats);
        combat.handleInput(input, particles);
        syncFields();
    }

    public void update(TileMap tileMap) {
        if (!alive) return;
        effects.update();
        stats.update(movement.isCrouching);
        movement.updatePhysics();

        // Resolve tile collisions BEFORE animation state update so onGround is 100% accurate
        if (tileMap != null) {
            if (collision.resolveTileCollisions(tileMap)) {
                takeDamage(2, -8.0f);
            }
        }

        combat.update();
        animation.update(movement);
        syncFields();
    }

    @Override
    public void update() {
        update(null);
    }

    private void syncFields() {
        this.hp = stats.getHealth();
        this.maxHp = stats.getMaxHealth();
        this.stamina = stats.getStamina();
        this.maxStamina = stats.getMaxStamina();
        this.shield = stats.getShield();
        this.maxShield = stats.getMaxShield();
        this.isCrouching = movement.isCrouching;
        this.isAttacking = combat.isAttacking;
        this.attackHitbox = combat.attackHitbox;
        this.jumpCount = movement.jumpCount;
        this.invincibilityTimer = effects.getInvincibilityTimer();
        this.hitStunTimer = effects.getHitStunTimer();
    }

    public void heal(int amount) {
        if (!alive) return;
        stats.heal(amount);
        syncFields();
    }

    public void respawn(float rx, float ry) {
        this.x = rx;
        this.y = ry;
        this.vx = 0;
        this.vy = 0;
        this.alive = true;
        stats.setHealth(stats.getMaxHealth());
        stats.setShield(stats.getMaxShield());
        stats.setStamina(stats.getMaxStamina());
        effects.triggerInvincibility(60); // 1 second invincibility on respawn
        syncFields();
    }

    @Override
    public void takeDamage(int amount, float knockbackX) {
        if (effects.isInvincible() || !alive) return;

        boolean absorbed = stats.absorbShieldDamage(amount, movement.isCrouching);
        if (absorbed) {
            com.ashenfall.game.engine.SoundManager.playSound("hit");
            effects.triggerDamage(10, 4);
            vx += knockbackX * 0.3f;
        } else {
            stats.damage(amount);
            effects.triggerDamage(20, 10);
            vx += knockbackX;
            vy = -3.0f;
        }
        syncFields();
        if (stats.getHealth() <= 0) {
            hp = 0;
            alive = false;
        }
    }

    @Override
    public void draw(Graphics2D g2, float camX, float camY) {
        animation.draw(g2, camX, camY, movement, effects);

        // Render Sword Attack Slash visual arc
        if (combat.isAttacking) {
            int renderX = (int) (x - camX);
            int renderY = (int) (y - camY);
            g2.setColor(new Color(150, 220, 255, 200));
            int arcX = facingRight ? renderX + width : renderX - 34;
            g2.fillArc(arcX, renderY, 34, height, facingRight ? -60 : 120, 120);
            g2.setColor(Color.WHITE);
            g2.drawArc(arcX, renderY, 34, height, facingRight ? -60 : 120, 120);
        }
    }

    // Subsystem Getters
    public PlayerStats getStats() { return stats; }
    public PlayerMovement getMovement() { return movement; }
    public PlayerCombat getCombat() { return combat; }
    public PlayerAnimation getAnimation() { return animation; }
    public PlayerCollision getCollision() { return collision; }
    public PlayerEffects getEffects() { return effects; }
}
