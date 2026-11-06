package com.ashenfall.game.entities.player;

import com.ashenfall.game.engine.InputHandler;
import com.ashenfall.game.engine.SoundManager;
import com.ashenfall.game.entities.Particle;

import java.awt.Color;
import java.util.List;

public class PlayerMovement {
    private Player player;
    public int jumpCount = 0;
    public boolean isCrouching = false;
    public boolean isMovingLeft = false;
    public boolean isMovingRight = false;

    private int normalHeight = 54;
    private int crouchHeight = 32;

    public PlayerMovement(Player player) {
        this.player = player;
    }

    public void handleInput(InputHandler input, List<Particle> particles, PlayerStats stats) {
        if (!player.alive) return;

        isMovingLeft = input.left;
        isMovingRight = input.right;

        // Crouch handling
        if (input.crouch && player.onGround) {
            if (!isCrouching) {
                isCrouching = true;
                player.y += (normalHeight - crouchHeight);
                player.height = crouchHeight;
            }
        } else {
            if (isCrouching) {
                isCrouching = false;
                player.y -= (normalHeight - crouchHeight);
                player.height = normalHeight;
            }
        }

        // Horizontal movement - disabled while crouching
        if (isCrouching) {
            player.vx = 0;
            isMovingLeft = false;
            isMovingRight = false;
        } else {
            float moveSpeed = 5.5f;
            if (isMovingLeft) {
                player.vx = -moveSpeed;
                player.facingRight = false;
            } else if (isMovingRight) {
                player.vx = moveSpeed;
                player.facingRight = true;
            } else {
                player.vx = 0; // Instant stop on key release
            }
        }

        // Jump & Double Jump handling
        if (input.consumeJump()) {
            if (player.onGround) {
                player.vy = -12.5f;
                player.onGround = false;
                jumpCount = 1;
                SoundManager.playSound("jump");
                spawnDust(particles);
            } else if (jumpCount < 2 && stats.getStamina() >= 20) {
                player.vy = -11.0f;
                jumpCount = 2;
                stats.consumeStamina(20);
                SoundManager.playSound("double_jump");
                spawnDoubleJumpFX(particles);
            }
        }
    }

    public void updatePhysics() {
        // Gravity physics - apply gravity only when in air or jumping
        if (!player.onGround || player.vy < 0) {
            player.vy += 0.55f;
            if (player.vy > 14.0f) player.vy = 14.0f;
        } else {
            player.vy = 0;
        }

        player.x += player.vx;
        player.y += player.vy;
    }

    private void spawnDust(List<Particle> particles) {
        for (int i = 0; i < 8; i++) {
            particles.add(new Particle(
                player.x + player.width / 2.0f, player.y + player.height,
                (float) (Math.random() * 4 - 2), (float) (-Math.random() * 2),
                15, new Color(200, 180, 150), 4
            ));
        }
    }

    private void spawnDoubleJumpFX(List<Particle> particles) {
        for (int i = 0; i < 16; i++) {
            double angle = Math.random() * Math.PI * 2;
            float speed = (float) (Math.random() * 3 + 2);
            particles.add(new Particle(
                player.x + player.width / 2.0f, player.y + player.height / 2.0f,
                (float) (Math.cos(angle) * speed), (float) (Math.sin(angle) * speed),
                20, new Color(80, 180, 255), 6
            ));
        }
    }
}
