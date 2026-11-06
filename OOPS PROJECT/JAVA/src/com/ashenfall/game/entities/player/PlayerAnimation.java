package com.ashenfall.game.entities.player;

import com.ashenfall.game.engine.AssetManager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PlayerAnimation {
    private Player player;
    private int animTimer = 0;
    private int idleIdx = 0;
    private int runIdx = 0;
    private int jumpIdx = 0;
    private int crouchIdx = 0;

    public PlayerAnimation(Player player) {
        this.player = player;
    }

    public PlayerState getCurrentState(PlayerMovement movement) {
        if (!player.alive) return PlayerState.DEAD;
        if (!player.onGround) return player.vy < 0 ? PlayerState.JUMPING : PlayerState.FALLING;
        if (movement.isCrouching) return PlayerState.CROUCHING;
        if (movement.isMovingLeft || movement.isMovingRight) return PlayerState.RUNNING;
        return PlayerState.IDLE;
    }

    private int crouchAnimTimer = 0;

    public void update(PlayerMovement movement) {
        PlayerState state = getCurrentState(movement);

        // Faster crouch animation (every 2 ticks)
        if (state == PlayerState.CROUCHING) {
            crouchAnimTimer++;
            if (crouchAnimTimer >= 2) {
                crouchAnimTimer = 0;
                if (AssetManager.blueHeroCrouchFrames != null && AssetManager.blueHeroCrouchFrames.length > 0) {
                    if (crouchIdx < AssetManager.blueHeroCrouchFrames.length - 1) {
                        crouchIdx++;
                    }
                }
            }
        } else {
            crouchIdx = 0;
            crouchAnimTimer = 0;
        }

        animTimer++;
        if (animTimer >= 5) {
            animTimer = 0;
            switch (state) {
                case RUNNING:
                    if (AssetManager.blueHeroRunFrames != null && AssetManager.blueHeroRunFrames.length > 0) {
                        runIdx = (runIdx + 1) % AssetManager.blueHeroRunFrames.length;
                    }
                    break;
                case JUMPING:
                case FALLING:
                    if (AssetManager.blueHeroJumpFrames != null && AssetManager.blueHeroJumpFrames.length > 0) {
                        jumpIdx = (jumpIdx + 1) % AssetManager.blueHeroJumpFrames.length;
                    }
                    break;
                case IDLE:
                default:
                    if (AssetManager.blueHeroIdleFrames != null && AssetManager.blueHeroIdleFrames.length > 0) {
                        idleIdx = (idleIdx + 1) % AssetManager.blueHeroIdleFrames.length;
                    }
                    break;
            }
        }
    }

    public void draw(Graphics2D g2, float camX, float camY, PlayerMovement movement, PlayerEffects effects) {
        int renderX = (int) (player.x - camX);
        int renderY = (int) (player.y - camY);

        if (effects.isFlashing()) return;

        PlayerState state = getCurrentState(movement);
        boolean drawnCustom = false;
        BufferedImage frame = null;

        if (AssetManager.assetsLoaded) {
            switch (state) {
                case JUMPING:
                case FALLING:
                    if (AssetManager.blueHeroJumpFrames != null && AssetManager.blueHeroJumpFrames[0] != null) {
                        frame = AssetManager.blueHeroJumpFrames[jumpIdx % AssetManager.blueHeroJumpFrames.length];
                    }
                    break;
                case CROUCHING:
                    if (AssetManager.blueHeroCrouchFrames != null && AssetManager.blueHeroCrouchFrames[0] != null) {
                        frame = AssetManager.blueHeroCrouchFrames[crouchIdx % AssetManager.blueHeroCrouchFrames.length];
                    }
                    break;
                case RUNNING:
                    if (AssetManager.blueHeroRunFrames != null && AssetManager.blueHeroRunFrames[0] != null) {
                        frame = AssetManager.blueHeroRunFrames[runIdx % AssetManager.blueHeroRunFrames.length];
                    }
                    break;
                case IDLE:
                default:
                    if (AssetManager.blueHeroIdleFrames != null && AssetManager.blueHeroIdleFrames[0] != null) {
                        frame = AssetManager.blueHeroIdleFrames[idleIdx % AssetManager.blueHeroIdleFrames.length];
                    }
                    break;
            }
        }

        if (frame != null) {
            int drawH = movement.isCrouching ? 52 : 68;
            int drawW = (int) (drawH * (128.0f / 160.0f));
            int drawX = (int) (renderX + (player.width / 2.0f) - (drawW / 2.0f));
            int feetOffsetY = (int) (drawH * (150.0f / 160.0f));
            int drawY = (int) (renderY + player.height - feetOffsetY);

            if (player.facingRight) {
                g2.drawImage(frame, drawX, drawY, drawW, drawH, null);
            } else {
                g2.drawImage(frame, drawX + drawW, drawY, -drawW, drawH, null);
            }
            drawnCustom = true;
        }

        if (!drawnCustom) {
            g2.setColor(new Color(25, 75, 180));
            if (movement.isCrouching) {
                g2.fillRect(renderX, renderY, player.width, player.height);
            } else {
                g2.fillRect(renderX + 4, renderY + 14, player.width - 8, player.height - 20);
                g2.setColor(new Color(230, 180, 140));
                g2.fillOval(renderX + 6, renderY, 24, 20);
            }
        }
    }
}
