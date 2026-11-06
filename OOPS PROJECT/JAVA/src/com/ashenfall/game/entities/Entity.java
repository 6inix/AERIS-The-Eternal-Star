package com.ashenfall.game.entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Entity {
    public float x, y;
    public float vx, vy;
    public int width, height;
    public boolean onGround = false;
    public boolean facingRight = true;
    
    public int hp;
    public int maxHp;
    public int invincibilityTimer = 0;
    public int hitStunTimer = 0;
    public boolean alive = true;

    public Entity(float x, float y, int width, int height, int hp) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hp = hp;
        this.maxHp = hp;
    }

    public abstract void update();
    public abstract void draw(Graphics2D g2, float camX, float camY);

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public void takeDamage(int amount, float knockbackX) {
        if (invincibilityTimer > 0 || !alive) return;
        hp -= amount;
        invincibilityTimer = 20; // 20 frames invulnerability
        hitStunTimer = 10;
        vx += knockbackX;
        vy = -3.0f;
        if (hp <= 0) {
            hp = 0;
            alive = false;
        }
    }
}
