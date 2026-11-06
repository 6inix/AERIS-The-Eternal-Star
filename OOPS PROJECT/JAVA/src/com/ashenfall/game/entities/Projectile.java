package com.ashenfall.game.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Projectile {
    public float x, y;
    public float vx, vy;
    public int width, height;
    public int damage;
    public boolean active = true;
    public boolean isEnemy;
    public boolean isFirePillar;
    public int lifeTimer = 180; // 3 seconds max

    public Projectile(float x, float y, float vx, float vy, int width, int height, int damage, boolean isEnemy, boolean isFirePillar) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.width = width;
        this.height = height;
        this.damage = damage;
        this.isEnemy = isEnemy;
        this.isFirePillar = isFirePillar;
        if (isFirePillar) {
            lifeTimer = 60; // 1 second eruption
        }
    }

    public void update() {
        if (!isFirePillar) {
            x += vx;
            y += vy;
        }
        lifeTimer--;
        if (lifeTimer <= 0) {
            active = false;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public void draw(Graphics2D g2, float camX, float camY) {
        if (!active) return;
        int renderX = (int) (x - camX);
        int renderY = (int) (y - camY);

        if (isFirePillar) {
            // Render glowing fire pillar eruption
            g2.setColor(new Color(255, 100, 0, 180));
            g2.fillRect(renderX, renderY, width, height);
            g2.setColor(new Color(255, 220, 50, 220));
            g2.fillRect(renderX + 4, renderY, width - 8, height);
            g2.setColor(Color.WHITE);
            g2.fillRect(renderX + 8, renderY, width - 16, height);
        } else {
            // Render fireball projectile
            g2.setColor(new Color(255, 80, 0));
            g2.fillOval(renderX, renderY, width, height);
            g2.setColor(new Color(255, 220, 0));
            g2.fillOval(renderX + 2, renderY + 2, width - 4, height - 4);
        }
    }
}
