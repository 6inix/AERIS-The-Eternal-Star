package com.ashenfall.game.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Tile {
    public int x, y, width, height;
    public boolean solid;
    public boolean isHazard;
    public boolean isBreakable;
    public int tileType; // 0: Ground, 1: Ledge/Platform, 2: Wall/Pillar, 3: Hazard Spikes/Lava
    public BufferedImage customSprite = null;
    public int stage = 1;

    public Tile(int x, int y, int width, int height, boolean solid, boolean isHazard, int tileType) {
        this(x, y, width, height, solid, isHazard, tileType, null, 1);
    }

    public Tile(int x, int y, int width, int height, boolean solid, boolean isHazard, int tileType, BufferedImage customSprite, int stage) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.solid = solid;
        this.isHazard = isHazard;
        this.tileType = tileType;
        this.customSprite = customSprite;
        this.stage = stage;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g2, float camX, float camY, BufferedImage tilesImage) {
        int renderX = (int) (x - camX);
        int renderY = (int) (y - camY);

        // 1. Hazard Tiles (Lava or Spike Trap)
        if (isHazard || tileType == 3) {
            if (customSprite != null) {
                drawTiledSprite(g2, customSprite, renderX, renderY, width, height);
                return;
            }
            if (com.ashenfall.game.engine.AssetManager.lavaImage != null) {
                g2.drawImage(com.ashenfall.game.engine.AssetManager.lavaImage, renderX, renderY, width, height, null);
                g2.setColor(new Color(255, 220, 80, 200));
                g2.fillRect(renderX, renderY, width, 3);
            } else {
                g2.setColor(new Color(255, 60, 0));
                g2.fillRect(renderX, renderY, width, height);
                g2.setColor(Color.YELLOW);
                for (int i = 0; i < width; i += 8) {
                    g2.fillPolygon(new int[]{renderX + i, renderX + i + 4, renderX + i + 8}, new int[]{renderY + height, renderY + height - 14, renderY + height}, 3);
                }
            }
            return;
        }

        // 2. Custom Stage Sprite (from Stage 1, 2, 4, 7)
        if (customSprite != null) {
            if (tileType == 1) { // Platform - stretch smoothly across platform bounds
                g2.drawImage(customSprite, renderX, renderY, width, height, null);
            } else {
                drawTiledSprite(g2, customSprite, renderX, renderY, width, height);
            }
        } else {
            // 3. Stage Fallbacks
            drawStageFallback(g2, renderX, renderY, width, height, stage, tileType, tilesImage);
        }

        // 4. Ground tiles fade to black as they go down
        if (tileType == 0 && height > 24) {
            java.awt.Paint oldPaint = g2.getPaint();
            int fadeStartY = renderY + 20; // Keep top 20px surface crystal clear
            int fadeH = height - 20;
            if (fadeH > 0) {
                java.awt.GradientPaint blackFade = new java.awt.GradientPaint(
                    0, fadeStartY, new Color(0, 0, 0, 0),
                    0, renderY + height, new Color(0, 0, 0, 255)
                );
                g2.setPaint(blackFade);
                g2.fillRect(renderX, fadeStartY, width, fadeH);
                g2.setPaint(oldPaint);
            }
        }
    }

    private void drawTiledSprite(Graphics2D g2, BufferedImage img, int rx, int ry, int w, int h) {
        int iw = img.getWidth();
        int ih = img.getHeight();
        if (w <= iw && h <= ih) {
            g2.drawImage(img, rx, ry, w, h, null);
            return;
        }
        for (int tx = 0; tx < w; tx += iw) {
            int dw = Math.min(iw, w - tx);
            for (int ty = 0; ty < h; ty += ih) {
                int dh = Math.min(ih, h - ty);
                g2.drawImage(img, rx + tx, ry + ty, rx + tx + dw, ry + ty + dh, 0, 0, dw, dh, null);
            }
        }
    }

    private void drawStageFallback(Graphics2D g2, int rx, int ry, int w, int h, int stageId, int type, BufferedImage tilesImage) {
        if (type == 0 && com.ashenfall.game.engine.AssetManager.groundImage != null && stageId == 1) {
            g2.drawImage(com.ashenfall.game.engine.AssetManager.groundImage, rx, ry, w, h, null);
            return;
        }
        if (type == 1 && com.ashenfall.game.engine.AssetManager.platformImage != null && stageId == 1) {
            int platH = Math.max(h + 60, (int) (w * 0.75f));
            g2.drawImage(com.ashenfall.game.engine.AssetManager.platformImage, rx, ry - 4, w, platH, null);
            return;
        }

        // Stage specific colors and atmospheric highlights
        switch (stageId) {
            case 1: // Ashen Forest
                if (type == 0) {
                    g2.setColor(new Color(45, 40, 42));
                    g2.fillRect(rx, ry, w, h);
                    g2.setColor(new Color(140, 65, 35));
                    g2.fillRect(rx, ry, w, 6);
                } else if (type == 1) {
                    g2.setColor(new Color(65, 52, 45));
                    g2.fillRect(rx, ry, w, h);
                    g2.setColor(new Color(180, 85, 30));
                    g2.fillRect(rx, ry, w, 4);
                } else {
                    g2.setColor(new Color(38, 32, 35));
                    g2.fillRect(rx, ry, w, h);
                }
                break;
            case 2: // Lava Cavern
                if (type == 0) {
                    g2.setColor(new Color(35, 20, 20));
                    g2.fillRect(rx, ry, w, h);
                    g2.setColor(new Color(255, 90, 10));
                    g2.fillRect(rx, ry, w, 5);
                } else if (type == 1) {
                    g2.setColor(new Color(50, 25, 22));
                    g2.fillRect(rx, ry, w, h);
                    g2.setColor(new Color(255, 140, 20));
                    g2.fillRect(rx, ry, w, 4);
                } else {
                    g2.setColor(new Color(30, 18, 18));
                    g2.fillRect(rx, ry, w, h);
                }
                break;
            case 3: // Hidden Shrine
            case 4: // Hidden Shrine
                if (type == 0) {
                    g2.setColor(new Color(30, 35, 50));
                    g2.fillRect(rx, ry, w, h);
                    g2.setColor(new Color(100, 120, 200));
                    g2.fillRect(rx, ry, w, 6);
                } else if (type == 1) {
                    g2.setColor(new Color(45, 50, 70));
                    g2.fillRect(rx, ry, w, h);
                    g2.setColor(new Color(140, 160, 240));
                    g2.fillRect(rx, ry, w, 4);
                } else {
                    g2.setColor(new Color(25, 30, 42));
                    g2.fillRect(rx, ry, w, h);
                }
                break;
            case 7: // Hollow Knight Arena
            default:
                if (type == 0) {
                    g2.setColor(new Color(20, 18, 28));
                    g2.fillRect(rx, ry, w, h);
                    g2.setColor(new Color(160, 90, 220));
                    g2.fillRect(rx, ry, w, 6);
                } else if (type == 1) {
                    g2.setColor(new Color(35, 30, 45));
                    g2.fillRect(rx, ry, w, h);
                    g2.setColor(new Color(200, 120, 255));
                    g2.fillRect(rx, ry, w, 4);
                } else {
                    g2.setColor(new Color(15, 12, 22));
                    g2.fillRect(rx, ry, w, h);
                }
                break;
        }
    }
}
