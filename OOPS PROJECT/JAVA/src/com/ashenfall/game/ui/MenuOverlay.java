package com.ashenfall.game.ui;

import com.ashenfall.game.engine.AssetManager;
import com.ashenfall.game.engine.InputHandler;
import com.ashenfall.game.engine.SoundManager;

import java.awt.*;

public class MenuOverlay {
    public static int selectedOption = 0;

    // Title Menu Logos
    public static final Rectangle menuPlayRect = new Rectangle(640 - 160, 340, 80, 80);
    public static final Rectangle menuControlsRect = new Rectangle(640 - 40, 340, 80, 80);
    public static final Rectangle menuQuitRect = new Rectangle(640 + 80, 340, 80, 80);

    // Gothic Pause Menu Buttons (Centered 5-button vertical stack)
    public static final Rectangle gothicResumeRect = new Rectangle(640 - 180, 230, 360, 54);
    public static final Rectangle gothicOptionsRect = new Rectangle(640 - 180, 296, 360, 54);
    public static final Rectangle gothicControlsRect = new Rectangle(640 - 180, 362, 360, 54);
    public static final Rectangle gothicSaveRect = new Rectangle(640 - 180, 428, 360, 54);
    public static final Rectangle gothicExitRect = new Rectangle(640 - 180, 494, 360, 54);

    public static final Rectangle[] gothicPauseRects = {
        gothicResumeRect, gothicOptionsRect, gothicControlsRect, gothicSaveRect, gothicExitRect
    };

    // Controls Screen Logo
    public static final Rectangle controlsBackRect = new Rectangle(640 - 40, 580, 80, 80);

    // Game Over Logos
    public static final Rectangle gameOverRestartRect = new Rectangle(640 - 90, 370, 80, 80);
    public static final Rectangle gameOverHomeRect = new Rectangle(640 + 10, 370, 80, 80);

    // Victory Logos
    public static final Rectangle victoryPlayRect = new Rectangle(640 - 90, 370, 80, 80);
    public static final Rectangle victoryHomeRect = new Rectangle(640 + 10, 370, 80, 80);

    // Music Logo
    public static final Rectangle musicBtnRect = new Rectangle(1200, 20, 48, 48);

    public static void drawMenu(Graphics2D g2, int width, int height, InputHandler input) {
        // Dark Atmospheric Menu Background
        g2.setColor(new Color(15, 10, 20));
        g2.fillRect(0, 0, width, height);

        // Header Title text
        g2.setColor(new Color(255, 60, 0));
        g2.setFont(new Font("Serif", Font.BOLD, 48));
        drawCenteredString(g2, "ASHEN FALL: THE BURNING KING", width, 160);

        g2.setColor(Color.ORANGE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        drawCenteredString(g2, "An Action Platformer Adventure", width, 200);

        // Top Right Music Logo Button
        boolean hoverMusic = input.mousePoint != null && musicBtnRect.contains(input.mousePoint);
        drawLogoButton(g2, musicBtnRect, AssetManager.uiMusic, "AUDIO TOGGLE", hoverMusic, !SoundManager.isSoundEnabled());

        // Render Title Navigation Logos
        boolean hoverPlay = input.mousePoint != null && menuPlayRect.contains(input.mousePoint);
        boolean hoverControls = input.mousePoint != null && menuControlsRect.contains(input.mousePoint);
        boolean hoverQuit = input.mousePoint != null && menuQuitRect.contains(input.mousePoint);

        drawLogoButton(g2, menuPlayRect, AssetManager.uiPlay, "START GAME", hoverPlay, false);
        drawLogoButton(g2, menuControlsRect, AssetManager.uiMenu, "CONTROLS", hoverControls, false);
        drawLogoButton(g2, menuQuitRect, AssetManager.uiClose, "QUIT GAME", hoverQuit, false);
    }

    public static void drawControls(Graphics2D g2, int width, int height, InputHandler input) {
        g2.setColor(new Color(15, 10, 20));
        g2.fillRect(0, 0, width, height);

        g2.setColor(new Color(255, 215, 0));
        g2.setFont(new Font("Serif", Font.BOLD, 36));
        drawCenteredString(g2, "CONTROLS & GAMEPLAY", width, 90);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        g2.setColor(Color.WHITE);

        String[] lines = {
            "MOVE LEFT / RIGHT :  A / D  or  LEFT / RIGHT ARROWS",
            "JUMP               :  SPACE  or  W / UP ARROW",
            "DOUBLE JUMP        :  Press JUMP again in mid-air (Uses Stamina)",
            "CROUCH             :  S  or  DOWN ARROW (Shield Head & Duck Under Hazards)",
            "ATTACK             :  J  or  Z  or  LEFT MOUSE CLICK (Melee Sword Slash)",
            "PAUSE GAME         :  P  or  ESC Key",
            "",
            "OBJECTIVE: Traverse the Ashen Forest, defeat 4 types of enemies,",
            "slay The Hollow Knight Mini-Boss to open the Gate, and defeat Varkan The Burning King!"
        };

        int y = 160;
        for (String line : lines) {
            drawCenteredString(g2, line, width, y);
            y += 36;
        }

        boolean hoverBack = input.mousePoint != null && controlsBackRect.contains(input.mousePoint);
        drawLogoButton(g2, controlsBackRect, AssetManager.uiClose, "BACK TO MENU", hoverBack, false);
    }

    public static void drawPause(Graphics2D g2, int width, int height, InputHandler input) {
        // Translucent dark backdrop
        g2.setColor(new Color(8, 6, 12, 215));
        g2.fillRect(0, 0, width, height);

        // Music Toggle Logo Button Top Right
        boolean hoverMusic = input.mousePoint != null && musicBtnRect.contains(input.mousePoint);
        drawLogoButton(g2, musicBtnRect, AssetManager.uiMusic, "AUDIO TOGGLE", hoverMusic, !SoundManager.isSoundEnabled());

        // Header Title: PAUSED with Gothic ornaments
        int titleY = 165;

        // Top ornament flourish
        g2.setColor(new Color(210, 185, 150, 220));
        g2.setFont(new Font("Serif", Font.PLAIN, 24));
        drawCenteredString(g2, "❖ ──────  ⚜  ────── ❖", width, titleY - 26);

        // Main Title
        g2.setColor(new Color(235, 225, 210));
        g2.setFont(new Font("Serif", Font.BOLD, 46));
        drawCenteredString(g2, "PAUSED", width, titleY);

        // Bottom ornament line
        g2.setColor(new Color(180, 150, 110, 200));
        g2.setFont(new Font("Serif", Font.PLAIN, 20));
        drawCenteredString(g2, "❖ ──────────────────── ❖", width, titleY + 24);

        // Gothic Menu Buttons
        Image[] normalImgs = {
            AssetManager.gothicResumeNormal,
            AssetManager.gothicOptionsNormal,
            AssetManager.gothicControlsNormal,
            AssetManager.gothicSaveNormal,
            AssetManager.gothicExitNormal
        };

        Image[] selectedImgs = {
            AssetManager.gothicResumeSelected,
            AssetManager.gothicOptionsSelected,
            AssetManager.gothicControlsSelected,
            AssetManager.gothicSaveSelected,
            AssetManager.gothicExitSelected
        };

        for (int i = 0; i < gothicPauseRects.length; i++) {
            Rectangle rect = gothicPauseRects[i];
            boolean hover = input.mousePoint != null && rect.contains(input.mousePoint);
            if (hover) {
                selectedOption = i;
            }

            boolean isSelected = (selectedOption == i);
            Image btnImg = isSelected ? selectedImgs[i] : normalImgs[i];

            if (btnImg != null) {
                g2.drawImage(btnImg, rect.x, rect.y, rect.width, rect.height, null);
            } else {
                // Fallback rendering if image is loading
                g2.setColor(isSelected ? new Color(180, 40, 40) : new Color(30, 25, 35));
                g2.fillRect(rect.x, rect.y, rect.width, rect.height);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Serif", Font.BOLD, 18));
                drawCenteredString(g2, "OPTION " + (i + 1), width, rect.y + 32);
            }
        }
    }

    public static void drawGameOver(Graphics2D g2, int width, int height, InputHandler input) {
        g2.setColor(new Color(40, 0, 0, 220));
        g2.fillRect(0, 0, width, height);

        g2.setColor(Color.RED);
        g2.setFont(new Font("Serif", Font.BOLD, 56));
        drawCenteredString(g2, "YOU DIED", width, 220);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        drawCenteredString(g2, "The Ash Consumed You...", width, 290);

        boolean hoverRestart = input.mousePoint != null && gameOverRestartRect.contains(input.mousePoint);
        boolean hoverHome = input.mousePoint != null && gameOverHomeRect.contains(input.mousePoint);

        drawLogoButton(g2, gameOverRestartRect, AssetManager.uiRestart, "RESPAWN / RESTART", hoverRestart, false);
        drawLogoButton(g2, gameOverHomeRect, AssetManager.uiHome, "MAIN MENU", hoverHome, false);
    }

    public static void drawVictory(Graphics2D g2, int width, int height, int score, int kills, InputHandler input) {
        g2.setColor(new Color(20, 45, 20, 230));
        g2.fillRect(0, 0, width, height);

        g2.setColor(new Color(255, 215, 0));
        g2.setFont(new Font("Serif", Font.BOLD, 50));
        drawCenteredString(g2, "VICTORY!", width, 180);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        drawCenteredString(g2, "VARKAN THE BURNING KING HAS BEEN VANQUISHED!", width, 240);

        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        drawCenteredString(g2, "FINAL SCORE: " + score + "   |   TOTAL KILLS: " + kills, width, 310);

        boolean hoverPlay = input.mousePoint != null && victoryPlayRect.contains(input.mousePoint);
        boolean hoverHome = input.mousePoint != null && victoryHomeRect.contains(input.mousePoint);

        drawLogoButton(g2, victoryPlayRect, AssetManager.uiPlay, "PLAY AGAIN", hoverPlay, false);
        drawLogoButton(g2, victoryHomeRect, AssetManager.uiHome, "MAIN MENU", hoverHome, false);
    }

    private static void drawLogoButton(Graphics2D g2, Rectangle rect, Image img, String label, boolean isHovered, boolean isMuted) {
        if (img == null) return;

        int pad = isHovered ? 2 : 6;
        int drawX = rect.x + pad;
        int drawY = rect.y + pad;
        int drawW = rect.width - (pad * 2);
        int drawH = rect.height - (pad * 2);

        // Hover Glow Effect
        if (isHovered) {
            g2.setColor(new Color(255, 140, 0, 160));
            g2.fillOval(rect.x - 6, rect.y - 6, rect.width + 12, rect.height + 12);
        }

        // Draw Raw UI Component Icon Logo
        g2.drawImage(img, drawX, drawY, drawW, drawH, null);

        // Audio Mute indicator line if muted
        if (isMuted) {
            g2.setColor(new Color(255, 40, 40, 230));
            g2.setStroke(new BasicStroke(3.5f));
            g2.drawLine(rect.x + 6, rect.y + rect.height - 6, rect.x + rect.width - 6, rect.y + 6);
            g2.setStroke(new BasicStroke(1.0f));
        }

        // Floating Tooltip Label on Hover
        if (isHovered && label != null) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            FontMetrics fm = g2.getFontMetrics();
            int lblW = fm.stringWidth(label) + 18;
            int lblX = rect.x + (rect.width - lblW) / 2;
            int lblY = rect.y + rect.height + 22;

            g2.setColor(new Color(15, 10, 25, 220));
            g2.fillRoundRect(lblX, lblY - 14, lblW, 22, 8, 8);
            g2.setColor(Color.ORANGE);
            g2.drawRoundRect(lblX, lblY - 14, lblW, 22, 8, 8);

            g2.setColor(Color.YELLOW);
            g2.drawString(label, lblX + 9, lblY + 2);
        }
    }

    private static void drawCenteredString(Graphics2D g2, String text, int width, int y) {
        FontMetrics fm = g2.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        g2.drawString(text, x, y);
    }
}
