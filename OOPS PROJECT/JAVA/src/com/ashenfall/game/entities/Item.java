package com.ashenfall.game.entities;

import com.ashenfall.game.engine.AssetManager;
import com.ashenfall.game.engine.SoundManager;
import com.ashenfall.game.entities.player.Player;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

public class Item {
    public static final int TYPE_COIN = 0;
    public static final int TYPE_HEALTH = 1;
    public static final int TYPE_GEM = 2;
    public static final int TYPE_CHEST = 3;
    public static final int TYPE_CHARM = 4;
    public static final int TYPE_KEY = 5;
    public static final int TYPE_SCROLL = 6;

    public float x, y;
    public int width, height;
    public int itemType;
    public BufferedImage sprite;
    public BufferedImage openSprite;
    public boolean collected = false;
    public boolean isOpened = false;
    public int stage = 1;
    public String name = "Item";
    public int scoreValue = 100;
    public int healAmount = 0;

    private float bobTimer;
    private float floatOffset = 0;

    public Item(float x, float y, int width, int height, int itemType, BufferedImage sprite, int stage, String name, int scoreValue, int healAmount) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.itemType = itemType;
        this.sprite = sprite;
        this.stage = stage;
        this.name = name;
        this.scoreValue = scoreValue;
        this.healAmount = healAmount;
        this.bobTimer = (float) (Math.random() * 10);
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) (y + floatOffset), width, height);
    }

    public void update(Player player, List<Particle> particles, int[] scoreRef) {
        if (collected) return;

        bobTimer += 0.08f;
        // Subtle floating bob effect for floating items
        if (itemType != TYPE_CHEST) {
            floatOffset = (float) Math.sin(bobTimer) * 4.0f;
        } else {
            floatOffset = 0;
        }

        // Ambient shimmer particles
        if (Math.random() < 0.04) {
            Color sparkleColor = Color.YELLOW;
            if (itemType == TYPE_HEALTH) sparkleColor = new Color(255, 80, 100);
            else if (itemType == TYPE_GEM) sparkleColor = new Color(80, 220, 255);
            else if (itemType == TYPE_CHARM) sparkleColor = new Color(200, 100, 255);

            particles.add(new Particle(
                x + width / 2.0f + (float) (Math.random() * width - width / 2.0f),
                y + floatOffset + height / 2.0f + (float) (Math.random() * height - height / 2.0f),
                (float) (Math.random() * 1.5 - 0.75),
                (float) (-Math.random() * 1.5),
                16,
                sparkleColor,
                3
            ));
        }

        // Collision check with player
        if (player.alive && getBounds().intersects(player.getBounds())) {
            collect(player, particles, scoreRef);
        }
    }

    public void collect(Player player, List<Particle> particles, int[] scoreRef) {
        if (collected) return;

        if (itemType == TYPE_CHEST) {
            if (!isOpened) {
                isOpened = true;
                SoundManager.playSound("chest");
                if (openSprite != null) {
                    sprite = openSprite;
                } else if (AssetManager.chestOpenImage != null) {
                    sprite = AssetManager.chestOpenImage;
                }
                player.heal(3);
                scoreRef[0] += scoreValue;

                // Burst of treasure particles and coin sparkles
                for (int i = 0; i < 18; i++) {
                    particles.add(new Particle(
                        x + width / 2.0f, y + height / 2.0f,
                        (float) (Math.random() * 8 - 4),
                        (float) (-Math.random() * 6 - 2),
                        28,
                        new Color(255, 215, 0),
                        5
                    ));
                }
            }
            return;
        }

        collected = true;

        // Sound effect & actions
        switch (itemType) {
            case TYPE_COIN:
                SoundManager.playSound("coin");
                scoreRef[0] += scoreValue;
                spawnBurstParticles(particles, new Color(255, 215, 0), 8);
                break;
            case TYPE_HEALTH:
                SoundManager.playSound("heal");
                player.heal(healAmount > 0 ? healAmount : 2);
                scoreRef[0] += scoreValue;
                spawnBurstParticles(particles, new Color(255, 80, 120), 12);
                break;
            case TYPE_GEM:
                SoundManager.playSound("coin");
                scoreRef[0] += scoreValue;
                spawnBurstParticles(particles, new Color(80, 220, 255), 10);
                break;
            case TYPE_CHARM:
                SoundManager.playSound("heal");
                player.shield = player.maxShield; // Restore complete shield
                scoreRef[0] += scoreValue;
                spawnBurstParticles(particles, new Color(220, 100, 255), 15);
                break;
            case TYPE_KEY:
            case TYPE_SCROLL:
                SoundManager.playSound("coin");
                scoreRef[0] += scoreValue;
                spawnBurstParticles(particles, new Color(255, 240, 150), 10);
                break;
        }
    }

    private void spawnBurstParticles(List<Particle> particles, Color color, int count) {
        for (int i = 0; i < count; i++) {
            particles.add(new Particle(
                x + width / 2.0f, y + floatOffset + height / 2.0f,
                (float) (Math.random() * 5 - 2.5),
                (float) (-Math.random() * 4 - 1),
                22,
                color,
                4
            ));
        }
    }

    public void draw(Graphics2D g2, float camX, float camY) {
        if (collected) return;
        int renderX = (int) (x - camX);
        int renderY = (int) (y - camY + floatOffset);

        // Ground shadow for floating items
        if (itemType != TYPE_CHEST) {
            int shadowW = (int) (width * 0.8f);
            int shadowX = renderX + (width - shadowW) / 2;
            int shadowY = (int) (y - camY + height + 6);
            g2.setColor(new Color(0, 0, 0, 70));
            g2.fillOval(shadowX, shadowY, shadowW, 6);
        }

        if (sprite != null) {
            g2.drawImage(sprite, renderX, renderY, width, height, null);
        } else {
            // Procedural fallback
            if (itemType == TYPE_COIN) {
                g2.setColor(new Color(255, 215, 0));
                g2.fillOval(renderX, renderY, width, height);
                g2.setColor(Color.WHITE);
                g2.drawOval(renderX, renderY, width, height);
            } else if (itemType == TYPE_HEALTH) {
                g2.setColor(new Color(255, 50, 70));
                g2.fillOval(renderX, renderY, width, height);
            } else {
                g2.setColor(new Color(100, 200, 255));
                g2.fillRect(renderX, renderY, width, height);
            }
        }
    }
}
