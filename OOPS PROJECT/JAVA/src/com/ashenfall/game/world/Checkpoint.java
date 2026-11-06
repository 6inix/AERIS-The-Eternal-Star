package com.ashenfall.game.world;

import com.ashenfall.game.engine.SoundManager;
import com.ashenfall.game.entities.Particle;
import com.ashenfall.game.entities.player.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

public class Checkpoint {
    public int stage;
    public String stageName;
    public float x, y;
    public int width = 44;
    public int height = 85;
    public float respawnX, respawnY;
    public boolean activated = false;
    public BufferedImage sprite;

    private float pulseTimer = 0;

    public Checkpoint(int stage, String stageName, float x, float y, float respawnX, float respawnY, BufferedImage sprite) {
        this.stage = stage;
        this.stageName = stageName;
        this.x = x;
        this.y = y;
        this.respawnX = respawnX;
        this.respawnY = respawnY;
        this.sprite = sprite;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public void update(Player player, List<Particle> particles, TileMap tileMap) {
        pulseTimer += 0.05f;

        // Collision check with player
        if (!activated && player.alive && getBounds().intersects(player.getBounds())) {
            activated = true;
            tileMap.activeCheckpoint = this;
            tileMap.bannerText = "CHECKPOINT ACTIVATED - " + stageName.toUpperCase();
            tileMap.bannerTimer = 180; // 3 seconds
            SoundManager.playSound("checkpoint");

            // Burst of radiant soul / checkpoint particles
            for (int i = 0; i < 25; i++) {
                particles.add(new Particle(
                    x + width / 2.0f, y + 20,
                    (float) (Math.random() * 6 - 3),
                    (float) (-Math.random() * 5 - 1),
                    30,
                    new Color(80, 220, 255),
                    5
                ));
            }
        }

        // Active glowing soul flames
        if (activated && Math.random() < 0.15) {
            particles.add(new Particle(
                x + width / 2.0f + (float) (Math.random() * 16 - 8),
                y + 12 + (float) (Math.random() * 10),
                (float) (Math.random() * 1 - 0.5f),
                (float) (-Math.random() * 2 - 0.5f),
                20,
                new Color(120, 220, 255, 200),
                3
            ));
        }
    }

    public void draw(Graphics2D g2, float camX, float camY) {
        int renderX = (int) (x - camX);
        int renderY = (int) (y - camY);

        if (sprite != null) {
            g2.drawImage(sprite, renderX, renderY, width, height, null);
        } else {
            // Procedural Obelisk
            g2.setColor(new Color(40, 45, 60));
            g2.fillRect(renderX + 8, renderY + 20, width - 16, height - 20);
            g2.setColor(new Color(60, 70, 90));
            g2.drawRect(renderX + 8, renderY + 20, width - 16, height - 20);
        }

        // Checkpoint active state visual indicator
        if (activated) {
            // Pulsing soul flame orb at top of obelisk
            int pulse = (int) (Math.sin(pulseTimer) * 4);
            int orbSize = 18 + pulse;
            int orbX = renderX + (width - orbSize) / 2;
            int orbY = renderY - 6 - pulse / 2;

            g2.setColor(new Color(80, 220, 255, 120));
            g2.fillOval(orbX - 4, orbY - 4, orbSize + 8, orbSize + 8);
            g2.setColor(new Color(180, 255, 255, 220));
            g2.fillOval(orbX, orbY, orbSize, orbSize);
            g2.setColor(Color.WHITE);
            g2.fillOval(orbX + orbSize / 4, orbY + orbSize / 4, orbSize / 2, orbSize / 2);

            // Floating "CHECKPOINT" label
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            g2.setColor(new Color(100, 240, 255));
            g2.drawString("ACTIVE", renderX + 4, renderY - 14);
        } else {
            // Dormant marker
            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g2.setColor(new Color(180, 180, 180, 160));
            g2.drawString("SHRINE", renderX + 2, renderY - 6);
        }
    }
}
