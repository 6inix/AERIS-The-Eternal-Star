package com.ashenfall.game.entities;

import com.ashenfall.game.entities.player.Player;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

public abstract class Enemy extends Entity {
    public String name;
    public int damage;
    public int scoreValue;
    public float patrolMinX;
    public float patrolMaxX;
    public boolean isBoss = false;
    public boolean isMiniBoss = false;

    public Enemy(float x, float y, int width, int height, int hp, String name, int damage, int scoreValue) {
        super(x, y, width, height, hp);
        this.name = name;
        this.damage = damage;
        this.scoreValue = scoreValue;
        this.patrolMinX = x - 150;
        this.patrolMaxX = x + 150;
    }

    public abstract void updateAI(Player player, List<Projectile> projectiles, List<Particle> particles);

    @Override
    public void update() {
        if (invincibilityTimer > 0) invincibilityTimer--;
        if (hitStunTimer > 0) hitStunTimer--;

        vy += 0.55f; // Gravity
        if (vy > 14.0f) vy = 14.0f;

        x += vx;
        y += vy;
    }

    public void drawHealthBar(Graphics2D g2, float camX, float camY) {
        if (!alive || isBoss || isMiniBoss) return;
        int renderX = (int) (x - camX);
        int renderY = (int) (y - camY) - 10;
        int barW = width;
        int barH = 5;

        g2.setColor(Color.BLACK);
        g2.fillRect(renderX, renderY, barW, barH);
        float hpPercent = (float) hp / maxHp;
        g2.setColor(Color.RED);
        g2.fillRect(renderX, renderY, (int) (barW * hpPercent), barH);
    }
}
