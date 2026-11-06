package com.ashenfall.game.engine;

import com.ashenfall.game.entities.*;
import com.ashenfall.game.entities.player.*;
import com.ashenfall.game.ui.HUD;
import com.ashenfall.game.ui.MenuOverlay;
import com.ashenfall.game.world.Tile;
import com.ashenfall.game.world.TileMap;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    private Thread gameThread;
    private boolean running = false;
    private static final int FPS = 60;

    public GameState gameState = GameState.MENU;
    public InputHandler input = new InputHandler();
    public Camera camera = new Camera();

    public Player player;
    public TileMap tileMap;
    public List<Projectile> projectiles = new ArrayList<>();
    public List<Particle> particles = new ArrayList<>();

    public int killCount = 0;
    public int score = 0;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);

        AssetManager.loadAssets();
        initGame();
    }

    public void initGame() {
        tileMap = new TileMap();
        player = new Player(100, 450);
        projectiles.clear();
        particles.clear();
        killCount = 0;
        score = 0;
    }

    public void respawnPlayer() {
        if (tileMap != null && tileMap.activeCheckpoint != null) {
            player.respawn(tileMap.activeCheckpoint.respawnX, tileMap.activeCheckpoint.respawnY);
        } else {
            player = new Player(100, 450);
        }
        projectiles.clear();
    }

    public void startThread() {
        if (!running) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException ignored) {}
        }
    }

    private void update() {
        if (gameState == GameState.MENU) {
            updateMenu();
        } else if (gameState == GameState.CONTROLS) {
            if (input.consumeClick()) {
                if (MenuOverlay.controlsBackRect.contains(input.mousePoint)) {
                    gameState = GameState.MENU;
                    SoundManager.playSound("button");
                    return;
                }
            }
            if (input.consumeJump() || input.up || input.consumePause()) {
                gameState = GameState.MENU;
                SoundManager.playSound("button");
            }
        } else if (gameState == GameState.PLAYING) {
            updatePlaying();
        } else if (gameState == GameState.PAUSED) {
            updatePause();
        } else if (gameState == GameState.GAME_OVER) {
            updateGameOver();
        } else if (gameState == GameState.VICTORY) {
            updateVictory();
        }
    }

    private void updateMenu() {
        if (input.consumeClick()) {
            if (MenuOverlay.musicBtnRect.contains(input.mousePoint)) {
                SoundManager.toggleSound();
                SoundManager.playSound("button");
                return;
            }
            if (MenuOverlay.menuPlayRect.contains(input.mousePoint)) {
                initGame();
                gameState = GameState.PLAYING;
                SoundManager.playSound("button");
            } else if (MenuOverlay.menuControlsRect.contains(input.mousePoint)) {
                gameState = GameState.CONTROLS;
                SoundManager.playSound("button");
            } else if (MenuOverlay.menuQuitRect.contains(input.mousePoint)) {
                System.exit(0);
            }
        }
    }

    private void updatePause() {
        if (input.consumePause()) {
            gameState = GameState.PLAYING;
            SoundManager.playSound("button");
            return;
        }

        // Keyboard Navigation (Up / Down / Enter)
        if (input.consumeUp()) {
            MenuOverlay.selectedOption = (MenuOverlay.selectedOption - 1 + 5) % 5;
            SoundManager.playSound("button");
        }
        if (input.consumeDown()) {
            MenuOverlay.selectedOption = (MenuOverlay.selectedOption + 1) % 5;
            SoundManager.playSound("button");
        }
        if (input.consumeEnter()) {
            triggerPauseOption(MenuOverlay.selectedOption);
            return;
        }

        // Mouse Click Navigation
        if (input.consumeClick()) {
            if (MenuOverlay.musicBtnRect.contains(input.mousePoint)) {
                SoundManager.toggleSound();
                SoundManager.playSound("button");
                return;
            }

            for (int i = 0; i < MenuOverlay.gothicPauseRects.length; i++) {
                if (MenuOverlay.gothicPauseRects[i].contains(input.mousePoint)) {
                    MenuOverlay.selectedOption = i;
                    triggerPauseOption(i);
                    return;
                }
            }
        }
    }

    private void triggerPauseOption(int option) {
        SoundManager.playSound("button");
        if (option == 0) { // RESUME
            gameState = GameState.PLAYING;
        } else if (option == 1) { // OPTIONS
            SoundManager.toggleSound();
        } else if (option == 2) { // CONTROLS
            gameState = GameState.CONTROLS;
        } else if (option == 3) { // SAVE GAME
            // Save Checkpoint Action
        } else if (option == 4) { // EXIT TO MAIN MENU
            gameState = GameState.MENU;
        }
    }

    private void updateGameOver() {
        if (input.consumeClick()) {
            if (MenuOverlay.gameOverRestartRect.contains(input.mousePoint)) {
                respawnPlayer();
                gameState = GameState.PLAYING;
                SoundManager.playSound("button");
                return;
            }
            if (MenuOverlay.gameOverHomeRect.contains(input.mousePoint)) {
                initGame();
                gameState = GameState.MENU;
                SoundManager.playSound("button");
                return;
            }
        }
        if (input.up || input.jumpPressed) {
            respawnPlayer();
            gameState = GameState.PLAYING;
        }
    }

    private void updateVictory() {
        if (input.consumeClick()) {
            if (MenuOverlay.victoryPlayRect.contains(input.mousePoint)) {
                initGame();
                gameState = GameState.PLAYING;
                SoundManager.playSound("button");
                return;
            }
            if (MenuOverlay.victoryHomeRect.contains(input.mousePoint)) {
                gameState = GameState.MENU;
                SoundManager.playSound("button");
                return;
            }
        }
        if (input.up || input.jumpPressed) {
            initGame();
            gameState = GameState.PLAYING;
        }
    }

    private void updatePlaying() {
        // Toggle Pause State
        if (input.consumePause()) {
            gameState = GameState.PAUSED;
            SoundManager.playSound("button");
            return;
        }

        // Check Top-Right HUD UI Buttons (Pause & Music)
        if (input.consumeClick()) {
            if (HUD.pauseBtnRect.contains(input.mousePoint)) {
                gameState = GameState.PAUSED;
                SoundManager.playSound("button");
                return;
            }
            if (HUD.musicBtnRect.contains(input.mousePoint)) {
                SoundManager.toggleSound();
                SoundManager.playSound("button");
                return;
            }
        }

        // Handle Player Input, Physics, Tile Collisions & Animations in synchronized order
        player.handleInput(input, particles);
        player.update(tileMap);

        // Check if player fell out of bounds
        if (player.y > TileMap.WORLD_HEIGHT + 100) {
            player.takeDamage(10, 0);
        }

        // Check Player Game Over
        if (!player.alive) {
            gameState = GameState.GAME_OVER;
            SoundManager.playSound("game_over");
            return;
        }

        // TileMap AI & Gate update
        tileMap.update();

        // Update Items and Checkpoints
        int[] scoreRef = new int[]{score};
        for (com.ashenfall.game.entities.Item item : tileMap.items) {
            item.update(player, particles, scoreRef);
        }
        score = scoreRef[0];

        for (com.ashenfall.game.world.Checkpoint cp : tileMap.checkpoints) {
            cp.update(player, particles, tileMap);
        }

        // Update Camera
        camera.update(player.x, WIDTH, TileMap.WORLD_WIDTH);

        // Update Enemies AI & Collisions
        Iterator<Enemy> enemyIter = tileMap.enemies.iterator();
        while (enemyIter.hasNext()) {
            Enemy enemy = enemyIter.next();
            if (!enemy.alive) {
                killCount++;
                score += enemy.scoreValue;
                // Death particles
                for (int i = 0; i < 15; i++) {
                    particles.add(new Particle(
                        enemy.x + enemy.width / 2.0f, enemy.y + enemy.height / 2.0f,
                        (float) (Math.random() * 6 - 3), (float) (Math.random() * 6 - 3),
                        20, Color.RED, 5
                    ));
                }

                // If Main Boss Varkan dies, Trigger Victory!
                if (enemy == tileMap.mainBoss) {
                    gameState = GameState.VICTORY;
                    SoundManager.playSound("victory");
                }

                enemyIter.remove();
                continue;
            }

            enemy.updateAI(player, projectiles, particles);

            // Enemy Tile Collisions
            for (Tile tile : tileMap.tiles) {
                if (tile.solid && enemy.getBounds().intersects(tile.getBounds())) {
                    if (enemy.vy > 0 && enemy.y + enemy.height - enemy.vy <= tile.y + 12) {
                        enemy.y = tile.y - enemy.height;
                        enemy.vy = 0;
                        enemy.onGround = true;
                    }
                }
            }

            // Player Sword Attack vs Enemy
            if (player.isAttacking && player.attackHitbox != null) {
                if (player.attackHitbox.intersects(enemy.getBounds())) {
                    float kb = player.facingRight ? 8.0f : -8.0f;
                    enemy.takeDamage(15, kb);
                    camera.shake(6.0f, 8);
                    SoundManager.playSound("hit");
                }
            }

            // Enemy Body vs Player
            if (player.getBounds().intersects(enemy.getBounds())) {
                float kb = enemy.x > player.x ? -6.0f : 6.0f;
                player.takeDamage(enemy.damage, kb);
                camera.shake(10.0f, 12);
                SoundManager.playSound("hit");
            }
        }

        // Update Projectiles
        Iterator<Projectile> projIter = projectiles.iterator();
        while (projIter.hasNext()) {
            Projectile proj = projIter.next();
            proj.update();
            if (!proj.active) {
                projIter.remove();
                continue;
            }

            // Projectile vs Player
            if (proj.isEnemy && proj.getBounds().intersects(player.getBounds())) {
                player.takeDamage(proj.damage, proj.vx > 0 ? 5.0f : -5.0f);
                camera.shake(8.0f, 10);
                SoundManager.playSound("hit");
                proj.active = false;
            }
        }

        // Update Particles
        Iterator<Particle> partIter = particles.iterator();
        while (partIter.hasNext()) {
            Particle part = partIter.next();
            part.update();
            if (!part.active) partIter.remove();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == GameState.MENU) {
            MenuOverlay.drawMenu(g2, WIDTH, HEIGHT, input);
        } else if (gameState == GameState.CONTROLS) {
            MenuOverlay.drawControls(g2, WIDTH, HEIGHT, input);
        } else {
            // Render World Gameplay
            float camX = camera.getX();
            float camY = camera.getOffsetY();

            // 1. Background Parallax
            tileMap.drawBackground(g2, camX);

            // 2. Level Tiles
            tileMap.drawTiles(g2, camX, camY);

            // 3. Enemies
            for (Enemy enemy : tileMap.enemies) {
                enemy.draw(g2, camX, camY);
            }

            // 4. Projectiles
            for (Projectile proj : projectiles) {
                proj.draw(g2, camX, camY);
            }

            // 5. Hero Character
            player.draw(g2, camX, camY);

            // 6. Particles
            for (Particle part : particles) {
                part.draw(g2, camX, camY);
            }

            // 7. HUD Layer
            HUD.draw(g2, player, killCount, score, camX, input);

            // Checkpoint Announcement Banner
            tileMap.drawBanner(g2);

            // 8. Overlays for Pause, Game Over, Victory
            if (gameState == GameState.PAUSED) {
                MenuOverlay.drawPause(g2, WIDTH, HEIGHT, input);
            } else if (gameState == GameState.GAME_OVER) {
                MenuOverlay.drawGameOver(g2, WIDTH, HEIGHT, input);
            } else if (gameState == GameState.VICTORY) {
                MenuOverlay.drawVictory(g2, WIDTH, HEIGHT, score, killCount, input);
            }
        }
    }
}
