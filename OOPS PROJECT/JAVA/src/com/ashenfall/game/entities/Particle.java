package com.ashenfall.game.entities;

import java.awt.Color;
import java.awt.Graphics2D;

public class Particle {
    public float x, y;
    public float vx, vy;
    public int life;
    public int maxLife;
    public Color color;
    public float size;
    public boolean active = true;

    public Particle(float x, float y, float vx, float vy, int maxLife, Color color, float size) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.life = maxLife;
        this.maxLife = maxLife;
        this.color = color;
        this.size = size;
    }

    public void update() {
        x += vx;
        y += vy;
        life--;
        if (life <= 0) {
            active = false;
        }
    }

    public void draw(Graphics2D g2, float camX, float camY) {
        if (!active) return;
        float alpha = (float) life / maxLife;
        Color c = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255));
        g2.setColor(c);
        int currentSize = (int) (size * alpha);
        if (currentSize < 1) currentSize = 1;
        g2.fillRect((int) (x - camX), (int) (y - camY), currentSize, currentSize);
    }
}
