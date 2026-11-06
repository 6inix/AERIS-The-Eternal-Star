package com.ashenfall.game.ui;

import com.ashenfall.game.engine.AssetManager;
import com.ashenfall.game.engine.InputHandler;
import com.ashenfall.game.engine.SoundManager;
import com.ashenfall.game.entities.player.Player;
import com.ashenfall.game.world.TileMap;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class HUD {
    public static final Rectangle musicBtnRect = new Rectangle(1140, 15, 45, 45);
    public static final Rectangle pauseBtnRect = new Rectangle(1200, 15, 45, 45);

    public static void draw(Graphics2D g2, Player player, int killCount, int score, float camX, InputHandler input) {
        // Render Player HP Hearts
        int startX = 25;
        int startY = 25;
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.setColor(Color.WHITE);
        g2.drawString("HP:", startX, startY + 14);

        for (int i = 0; i < player.maxHp; i++) {
            int heartX = startX + 35 + (i * 24);
            if (i < player.hp) {
                g2.setColor(new Color(255, 40, 40));
                g2.fillOval(heartX, startY, 18, 18);
            } else {
                g2.setColor(Color.GRAY);
                g2.drawOval(heartX, startY, 18, 18);
            }
        }

        // Render Stamina Energy Bar
        int stamY = startY + 28;
        g2.setColor(Color.WHITE);
        g2.drawString("STAMINA:", startX, stamY + 12);
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(startX + 80, stamY, 150, 14);
        float stamPercent = player.stamina / player.maxStamina;
        g2.setColor(new Color(60, 160, 255));
        g2.fillRect(startX + 80, stamY, (int) (150 * stamPercent), 14);
        g2.setColor(Color.WHITE);
        g2.drawRect(startX + 80, stamY, 150, 14);

        // Render Shield Bar (200 Max)
        int shieldY = stamY + 22;
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.setColor(Color.WHITE);
        g2.drawString("SHIELD:", startX, shieldY + 12);
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(startX + 80, shieldY, 150, 14);
        float shieldPercent = player.getStats().getShield() / player.getStats().getMaxShield();
        g2.setColor(new Color(0, 220, 255)); // Bright Cyan Shield Bar
        g2.fillRect(startX + 80, shieldY, (int) (150 * shieldPercent), 14);
        g2.setColor(Color.WHITE);
        g2.drawRect(startX + 80, shieldY, 150, 14);
        g2.setFont(new Font("SansSerif", Font.BOLD, 10));
        g2.setColor(Color.WHITE);
        g2.drawString((int) player.getStats().getShield() + " / 200", startX + 130, shieldY + 11);

        // Kills & Score
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.setColor(Color.YELLOW);
        g2.drawString("KILLS: " + killCount, 280, startY + 14);
        g2.drawString("SCORE: " + score, 400, startY + 14);

        // Zone Location & Level Progress Indicator
        g2.setColor(Color.LIGHT_GRAY);
        int progressPercent = (int) ((player.x / TileMap.WORLD_WIDTH) * 100);
        g2.drawString("PROGRESS: " + progressPercent + "%", 550, startY + 14);

        // Render Top Right Interactive HUD UI Logos (Music & Pause)
        drawHudLogo(g2, musicBtnRect, AssetManager.uiMusic, "AUDIO TOGGLE", input, !SoundManager.isSoundEnabled());
        drawHudLogo(g2, pauseBtnRect, AssetManager.uiPause, "PAUSE GAME", input, false);

        // Controls hint (Bottom Right)
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2.setColor(new Color(220, 220, 220, 180));
        g2.drawString("[A/D] Move   [SPACE/W] Jump / Double Jump   [S] Crouch   [J/Left Click] Attack   [P/ESC] Pause", 580, 685);
    }

    private static void drawHudLogo(Graphics2D g2, Rectangle rect, java.awt.image.BufferedImage img, String label, InputHandler input, boolean isMuted) {
        if (img == null) return;
        boolean hover = input != null && input.mousePoint != null && rect.contains(input.mousePoint);

        int pad = hover ? 2 : 5;
        int drawX = rect.x + pad;
        int drawY = rect.y + pad;
        int drawW = rect.width - (pad * 2);
        int drawH = rect.height - (pad * 2);

        if (hover) {
            g2.setColor(new Color(255, 140, 0, 160));
            g2.fillOval(rect.x - 4, rect.y - 4, rect.width + 8, rect.height + 8);
        }

        g2.drawImage(img, drawX, drawY, drawW, drawH, null);

        if (isMuted) {
            g2.setColor(new Color(255, 40, 40, 230));
            g2.setStroke(new java.awt.BasicStroke(3.0f));
            g2.drawLine(rect.x + 6, rect.y + rect.height - 6, rect.x + rect.width - 6, rect.y + 6);
            g2.setStroke(new java.awt.BasicStroke(1.0f));
        }

        if (hover && label != null) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            java.awt.FontMetrics fm = g2.getFontMetrics();
            int lblW = fm.stringWidth(label) + 14;
            int lblX = rect.x + (rect.width - lblW) / 2;
            int lblY = rect.y + rect.height + 18;

            g2.setColor(new Color(15, 10, 25, 220));
            g2.fillRoundRect(lblX, lblY - 12, lblW, 18, 6, 6);
            g2.setColor(Color.ORANGE);
            g2.drawRoundRect(lblX, lblY - 12, lblW, 18, 6, 6);

            g2.setColor(Color.YELLOW);
            g2.drawString(label, lblX + 7, lblY + 1);
        }
    }
}
