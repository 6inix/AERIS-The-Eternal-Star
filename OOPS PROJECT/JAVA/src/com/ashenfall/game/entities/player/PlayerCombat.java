package com.ashenfall.game.entities.player;

import com.ashenfall.game.engine.InputHandler;
import com.ashenfall.game.engine.SoundManager;
import com.ashenfall.game.entities.Particle;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.List;

public class PlayerCombat {
    private Player player;
    public boolean isAttacking = false;
    public int attackTimer = 0;
    public Rectangle attackHitbox;

    public PlayerCombat(Player player) {
        this.player = player;
    }

    public void handleInput(InputHandler input, List<Particle> particles) {
        if (input.consumeAttack() && !isAttacking) {
            isAttacking = true;
            attackTimer = 15; // 15 frames duration
            SoundManager.playSound("slash");
            spawnAttackSlashFX(particles);
        }
    }

    public void update() {
        if (isAttacking) {
            attackTimer--;
            int attackW = 44;
            int attackH = player.height;
            int attackX = player.facingRight ? (int) (player.x + player.width) : (int) (player.x - attackW);
            attackHitbox = new Rectangle(attackX, (int) player.y, attackW, attackH);
            if (attackTimer <= 0) {
                isAttacking = false;
                attackHitbox = null;
            }
        }
    }

    private void spawnAttackSlashFX(List<Particle> particles) {
        float slashX = player.facingRight ? player.x + player.width + 10 : player.x - 20;
        for (int i = 0; i < 10; i++) {
            particles.add(new Particle(
                slashX, player.y + player.height / 2.0f + (float) (Math.random() * 20 - 10),
                player.facingRight ? (float) (Math.random() * 4 + 2) : (float) (-Math.random() * 4 - 2),
                (float) (Math.random() * 4 - 2),
                12, new Color(120, 210, 255), 5
            ));
        }
    }
}
