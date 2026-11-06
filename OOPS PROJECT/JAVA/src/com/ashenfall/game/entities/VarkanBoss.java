package com.ashenfall.game.entities;

import com.ashenfall.game.engine.SoundManager;
import com.ashenfall.game.entities.player.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

public class VarkanBoss extends Enemy {
    public int phase = 1; // 1 or 2
    private int actionTimer = 0;
    private int attackState = 0; // 0: Idle/Approach, 1: Sword Swing, 2: Firestorm Rain, 3: Teleport
    private int cooldown = 0;

    public VarkanBoss(float x, float y) {
        super(x, y, 64, 90, 350, "VARKAN, THE BURNING KING", 3, 5000);
        this.isBoss = true;
    }

    @Override
    public void updateAI(Player player, List<Projectile> projectiles, List<Particle> particles) {
        if (!alive || hitStunTimer > 0) return;

        // Check Phase Transition at 50% HP
        if (hp <= maxHp / 2 && phase == 1) {
            phase = 2;
            SoundManager.playSound("boss_spell");
            // Phase 2 Enrage burst particles
            for (int i = 0; i < 30; i++) {
                particles.add(new Particle(
                    x + width / 2.0f, y + height / 2.0f,
                    (float) (Math.random() * 8 - 4), (float) (Math.random() * 8 - 4),
                    25, new Color(255, 30, 0), 8
                ));
            }
        }

        if (cooldown > 0) cooldown--;

        float distToPlayer = Math.abs(player.x - x);
        facingRight = (player.x > x);

        if (attackState == 0) { // Idle/Tracking
            float speed = (phase == 2) ? 4.5f : 2.2f;
            vx = facingRight ? speed : -speed;

            if (cooldown <= 0 && distToPlayer < 500) {
                double rand = Math.random();
                if (rand < 0.45) { // Sword Swing
                    attackState = 1;
                    actionTimer = (phase == 2) ? 20 : 35;
                } else if (rand < 0.8) { // Firestorm Rain
                    attackState = 2;
                    actionTimer = 40;
                    SoundManager.playSound("boss_spell");
                    // Rain 5 fireballs
                    for (int i = 0; i < 5; i++) {
                        float fireX = player.x - 200 + (i * 100);
                        projectiles.add(new Projectile(fireX, y - 250, 0, 7.0f, 18, 18, 2, true, false));
                    }
                } else if (phase == 2) { // Phase 2 Teleport behind player
                    attackState = 3;
                    actionTimer = 20;
                    // Teleport FX
                    for (int i = 0; i < 20; i++) {
                        particles.add(new Particle(x + width / 2.0f, y + height / 2.0f, (float)(Math.random()*6-3), (float)(Math.random()*6-3), 20, Color.RED, 6));
                    }
                    x = facingRight ? player.x + 80 : player.x - 80;
                    SoundManager.playSound("boss_spell");
                }
                cooldown = (phase == 2) ? 60 : 100;
            }
        } else if (attackState == 1) { // Sword Swing execution
            actionTimer--;
            vx = facingRight ? 3.0f : -3.0f;

            // Attack Hitbox check
            if (actionTimer == 10) {
                SoundManager.playSound("slash");
                int swordW = 80;
                int swordX = facingRight ? (int) (x + width) : (int) (x - swordW);
                Rectangle swordBounds = new Rectangle(swordX, (int) y, swordW, height);
                if (swordBounds.intersects(player.getBounds())) {
                    player.takeDamage((phase == 2) ? 3 : 2, facingRight ? 10.0f : -10.0f);
                }
            }

            if (actionTimer <= 0) {
                attackState = 0;
            }
        } else if (attackState == 2 || attackState == 3) { // Spell/Teleport recovery
            actionTimer--;
            vx = 0;
            if (actionTimer <= 0) {
                attackState = 0;
            }
        }

        update();
    }

    @Override
    public void draw(Graphics2D g2, float camX, float camY) {
        if (!alive) return;
        int renderX = (int) (x - camX);
        int renderY = (int) (y - camY);

        if (invincibilityTimer % 4 >= 2) return;

        // Phase 2 Glowing Aura
        if (phase == 2) {
            g2.setColor(new Color(255, 50, 0, 100));
            g2.fillOval(renderX - 10, renderY - 10, width + 20, height + 20);
        }

        // Varkan Armor
        g2.setColor(new Color(30, 20, 30));
        g2.fillRect(renderX + 4, renderY + 20, width - 8, height - 20);
        // Fiery Cape
        g2.setColor(new Color(180, 30, 0));
        g2.fillRect(renderX + (facingRight ? -8 : width - 4), renderY + 18, 12, height - 20);

        // Giant Flaming Crown
        g2.setColor(new Color(255, 140, 0));
        g2.fillPolygon(
            new int[]{renderX + 4, renderX + 16, renderX + width / 2, renderX + width - 16, renderX + width - 4},
            new int[]{renderY, renderY + 20, renderY + 4, renderY + 20, renderY},
            5
        );

        // Giant Fiery Sword
        if (attackState == 1) {
            g2.setColor(new Color(255, 90, 0));
            int swordX = facingRight ? renderX + width : renderX - 60;
            g2.fillArc(swordX, renderY - 20, 60, height + 30, facingRight ? -60 : 120, 120);
        }

        // Draw Main Boss Health Bar at Top Center
        drawBossHUD(g2);
    }

    private void drawBossHUD(Graphics2D g2) {
        int barW = 600;
        int barH = 22;
        int barX = 340;
        int barY = 30;

        // Background frame
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(barX - 15, barY - 25, barW + 30, barH + 35);

        // Boss Title & Phase
        g2.setColor(phase == 2 ? Color.RED : Color.YELLOW);
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        String title = name + (phase == 2 ? " [ENRAGED PHASE 2]" : "");
        g2.drawString(title, barX, barY - 8);

        // Health fill
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(barX, barY, barW, barH);
        float hpPercent = (float) hp / maxHp;
        g2.setColor(phase == 2 ? new Color(255, 30, 0) : new Color(220, 80, 0));
        g2.fillRect(barX, barY, (int) (barW * hpPercent), barH);

        g2.setColor(new Color(255, 215, 0));
        g2.drawRect(barX, barY, barW, barH);
    }
}
