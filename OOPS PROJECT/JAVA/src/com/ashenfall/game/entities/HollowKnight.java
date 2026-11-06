package com.ashenfall.game.entities;

import com.ashenfall.game.engine.AssetManager;
import com.ashenfall.game.engine.SoundManager;
import com.ashenfall.game.entities.player.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

public class HollowKnight extends Enemy {
    // Animation States
    public static final int STATE_IDLE = 0;
    public static final int STATE_RUN = 1;
    public static final int STATE_ATTACK = 2;
    public static final int STATE_SKILL_CHARGE = 3;
    public static final int STATE_JUMP = 4;
    public static final int STATE_HURT = 5;
    public static final int STATE_DIE = 6;
    public static final int STATE_DEATH_EXPLOSION = 7;

    private int animState = STATE_IDLE;
    private int animFrame = 0;
    private int animTimer = 0;

    private int attackCooldown = 0;
    private int spellCooldown = 0;
    private int jumpCooldown = 0;
    private boolean dying = false;
    private boolean hasHitInAttack = false;
    private boolean hasCastSpell = false;

    public HollowKnight(float x, float y) {
        this(x, y, false);
    }

    public HollowKnight(float x, float y, boolean isMiniBoss) {
        super(x, y, 54, 75, isMiniBoss ? 180 : 75, isMiniBoss ? "THE HOLLOW KNIGHT (MINI-BOSS)" : "Hollow Knight", 2, isMiniBoss ? 1000 : 350);
        this.isMiniBoss = isMiniBoss;
    }

    @Override
    public void updateAI(Player player, List<Projectile> projectiles, List<Particle> particles) {
        if (dying) {
            vx = 0;
            animTimer++;

            // Phase 1: Die / Collapse
            if (animState == STATE_DIE) {
                if (animTimer >= 8) {
                    animTimer = 0;
                    animFrame++;
                    int dieLen = getFrameCount(AssetManager.hollowKnightDieFrames);
                    if (animFrame >= dieLen) {
                        // Transition to Phase 2: Void Death Explosion
                        animState = STATE_DEATH_EXPLOSION;
                        animFrame = 0;
                        animTimer = 0;
                        SoundManager.playSound("boss_spell");
                    }
                }
            }
            // Phase 2: Void Explosion
            else if (animState == STATE_DEATH_EXPLOSION) {
                // Burst of dark void and flame embers
                if (Math.random() < 0.6) {
                    particles.add(new Particle(
                        x + width / 2.0f + (float) (Math.random() * 20 - 10),
                        y + height / 2.0f + (float) (Math.random() * 20 - 10),
                        (float) (Math.random() * 6 - 3),
                        (float) (-Math.random() * 4 - 1),
                        20,
                        new Color(30, 20, 45, 230),
                        5
                    ));
                }

                if (animTimer >= 7) {
                    animTimer = 0;
                    animFrame++;
                    int explodeLen = getFrameCount(AssetManager.hollowKnightDeathExplosionFrames);
                    if (animFrame >= explodeLen) {
                        animFrame = explodeLen - 1;
                        alive = false; // Finalize death; gate opens and score awarded
                    }
                }
            }

            super.update();
            return;
        }

        if (!alive) return;

        if (hitStunTimer > 0) {
            super.update();
            return;
        }

        if (attackCooldown > 0) attackCooldown--;
        if (spellCooldown > 0) spellCooldown--;
        if (jumpCooldown > 0) jumpCooldown--;

        float distToPlayer = Math.abs(player.x - x);
        float distY = Math.abs(player.y - y);

        // 1. Hurt state
        if (animState == STATE_HURT) {
            animTimer++;
            if (animTimer >= 5) {
                animTimer = 0;
                animFrame++;
                if (animFrame >= getFrameCount(AssetManager.hollowKnightHurtFrames)) {
                    animState = STATE_IDLE;
                    animFrame = 0;
                }
            }
            super.update();
            return;
        }

        // 2. Attack state (Nail Combo)
        if (animState == STATE_ATTACK) {
            vx = 0;
            animTimer++;
            if (animTimer >= 5) {
                animTimer = 0;
                animFrame++;

                // Key nail strike frame
                if (animFrame == 2 && !hasHitInAttack) {
                    float reach = 75;
                    float reachX = facingRight ? x + width : x - reach;
                    Rectangle attackBox = new Rectangle((int) reachX, (int) y - 10, (int) reach, height + 20);

                    if (attackBox.intersects(player.getBounds())) {
                        player.takeDamage(damage, facingRight ? 7.0f : -7.0f);
                    }
                    SoundManager.playSound("slash");

                    // Spawn void crescent slash particles
                    float slashX = facingRight ? x + width + 10 : x - 10;
                    for (int i = 0; i < 6; i++) {
                        particles.add(new Particle(
                            slashX, y + height / 2.0f,
                            (float) (facingRight ? Math.random() * 3 + 1 : -Math.random() * 3 - 1),
                            (float) (Math.random() * 3 - 1.5),
                            14,
                            new Color(40, 30, 60),
                            4
                        ));
                    }
                    hasHitInAttack = true;
                }

                if (animFrame >= getFrameCount(AssetManager.hollowKnightAttackFrames)) {
                    animState = STATE_IDLE;
                    animFrame = 0;
                    hasHitInAttack = false;
                }
            }
            super.update();
            return;
        }

        // 3. Skill Charge / Spell Casting (Soul Pillar Eruption)
        if (animState == STATE_SKILL_CHARGE) {
            vx = 0;
            animTimer++;

            // Gather void & soul particles towards core
            if (Math.random() < 0.5) {
                particles.add(new Particle(
                    x + width / 2.0f + (float) (Math.random() * 40 - 20),
                    y + height / 2.0f + (float) (Math.random() * 40 - 20),
                    (float) (Math.random() * 2 - 1),
                    (float) (-Math.random() * 3),
                    14,
                    new Color(255, 120, 20),
                    3
                ));
            }

            if (animTimer >= 7) {
                animTimer = 0;
                animFrame++;

                // Apex frame: summon erupting fire pillars under player
                if (animFrame == 2 && !hasCastSpell) {
                    SoundManager.playSound("boss_spell");
                    projectiles.add(new Projectile(player.x - 15, y + height - 120, 0, 0, 36, 120, 2, true, true));
                    projectiles.add(new Projectile(player.x + 65, y + height - 120, 0, 0, 36, 120, 2, true, true));
                    hasCastSpell = true;
                }

                if (animFrame >= getFrameCount(AssetManager.hollowKnightSkillChargeFrames)) {
                    animState = STATE_IDLE;
                    animFrame = 0;
                    hasCastSpell = false;
                }
            }
            super.update();
            return;
        }

        // 4. Airborne / Jump State
        if (!onGround) {
            if (animState != STATE_JUMP) {
                animState = STATE_JUMP;
                animFrame = 0;
                animTimer = 0;
            }
            animTimer++;
            if (animTimer >= 5) {
                animTimer = 0;
                int jumpCount = getFrameCount(AssetManager.hollowKnightJumpFrames);
                if (animFrame < jumpCount - 1) animFrame++;
            }
            super.update();
            return;
        }

        // 5. Ground Combat & Decision AI
        facingRight = (player.x > x);

        if (distToPlayer < 450 && distY < 120) {
            // Close range: execute melee nail slash
            if (distToPlayer < 75 && attackCooldown <= 0) {
                animState = STATE_ATTACK;
                animFrame = 0;
                animTimer = 0;
                attackCooldown = 75;
                hasHitInAttack = false;
                vx = 0;
            }
            // Mid range leap/lunge
            else if (distToPlayer >= 80 && distToPlayer < 180 && jumpCooldown <= 0 && Math.random() < 0.25) {
                vy = -7.5f;
                vx = (facingRight ? 1 : -1) * 6.0f;
                jumpCooldown = 130;
                animState = STATE_JUMP;
                animFrame = 0;
                animTimer = 0;
                SoundManager.playSound("jump");
            }
            // Mid-to-long range spell cast (Skill charge)
            else if (distToPlayer >= 140 && distToPlayer < 420 && spellCooldown <= 0 && Math.random() < 0.3) {
                animState = STATE_SKILL_CHARGE;
                animFrame = 0;
                animTimer = 0;
                spellCooldown = 150;
                hasCastSpell = false;
                vx = 0;
            }
            // Advance towards player
            else {
                vx = facingRight ? 3.2f : -3.2f;
                animState = STATE_RUN;
                animTimer++;
                if (animTimer >= 4) {
                    animTimer = 0;
                    int runLen = getFrameCount(AssetManager.hollowKnightRunFrames);
                    animFrame = (animFrame + 1) % runLen;
                }
            }
        } else {
            // Idle stance
            vx = 0;
            animState = STATE_IDLE;
            animFrame = 0;
        }

        super.update();
    }

    @Override
    public void takeDamage(int amount, float knockbackX) {
        if (!alive || dying) return;

        super.takeDamage(amount, knockbackX);

        if (hp <= 0 && !dying) {
            hp = 0;
            dying = true;
            alive = true; // Stay alive while death sequence plays
            damage = 0;   // Inactive damage during death
            animState = STATE_DIE;
            animFrame = 0;
            animTimer = 0;
            SoundManager.playSound("hit");
        } else if (animState != STATE_ATTACK && animState != STATE_SKILL_CHARGE) {
            animState = STATE_HURT;
            animFrame = 0;
            animTimer = 0;
        }
    }

    @Override
    public Rectangle getBounds() {
        if (dying || !alive) {
            return new Rectangle(-9999, -9999, 0, 0);
        }
        return super.getBounds();
    }

    @Override
    public void draw(Graphics2D g2, float camX, float camY) {
        if (!alive) return;
        int renderX = (int) (x - camX);
        int renderY = (int) (y - camY);

        if (invincibilityTimer % 4 >= 2) return;

        BufferedImage currentFrame = getCurrentAnimationFrame();

        if (currentFrame != null) {
            int drawH = height + 35; // 75 + 35 = 110 px tall
            int drawW = (int) (drawH * ((float) currentFrame.getWidth() / currentFrame.getHeight()));
            int drawY = renderY + height - drawH + 4; // Feet planted on the ground

            if (animState == STATE_DIE || animState == STATE_DEATH_EXPLOSION) {
                int drawX = renderX + (width - drawW) / 2;
                if (facingRight) {
                    g2.drawImage(currentFrame, drawX, drawY, drawW, drawH, null);
                } else {
                    g2.drawImage(currentFrame, drawX + drawW, drawY, -drawW, drawH, null);
                }
            } else {
                if (facingRight) {
                    int drawX = renderX - 16;
                    g2.drawImage(currentFrame, drawX, drawY, drawW, drawH, null);
                } else {
                    int drawX = renderX + width + 16 - drawW;
                    g2.drawImage(currentFrame, drawX + drawW, drawY, -drawW, drawH, null);
                }
            }
        } else {
            // Procedural fallback
            g2.setColor(new Color(90, 20, 20));
            g2.fillRect(renderX + 4, renderY + 16, width - 8, height - 16);
            g2.setColor(new Color(255, 100, 0));
            g2.fillPolygon(
                new int[]{renderX + 6, renderX + 16, renderX + 26, renderX + 36, renderX + 46},
                new int[]{renderY, renderY + 16, renderY + 4, renderY + 16, renderY},
                5
            );
            g2.setColor(new Color(255, 220, 50));
            g2.fillOval(renderX + width / 2 - 10, renderY + 28, 20, 20);
        }

        if (isMiniBoss) {
            drawMiniBossHUD(g2);
        } else if (!dying) {
            drawHealthBar(g2, camX, camY);
        }
    }

    private int getFrameCount(BufferedImage[] frames) {
        if (frames == null || frames.length == 0) return 1;
        int count = 0;
        for (BufferedImage img : frames) {
            if (img != null) count++;
        }
        return count > 0 ? count : 1;
    }

    private BufferedImage getCurrentAnimationFrame() {
        BufferedImage[] frames = null;
        switch (animState) {
            case STATE_IDLE: frames = AssetManager.hollowKnightIdleFrames; break;
            case STATE_RUN: frames = AssetManager.hollowKnightRunFrames; break;
            case STATE_ATTACK: frames = AssetManager.hollowKnightAttackFrames; break;
            case STATE_SKILL_CHARGE: frames = AssetManager.hollowKnightSkillChargeFrames; break;
            case STATE_JUMP: frames = AssetManager.hollowKnightJumpFrames; break;
            case STATE_HURT: frames = AssetManager.hollowKnightHurtFrames; break;
            case STATE_DIE: frames = AssetManager.hollowKnightDieFrames; break;
            case STATE_DEATH_EXPLOSION: frames = AssetManager.hollowKnightDeathExplosionFrames; break;
        }

        if (frames != null && frames.length > 0) {
            int validCount = getFrameCount(frames);
            int idx = Math.min(animFrame, validCount - 1);
            if (frames[idx] != null) return frames[idx];
            if (frames[0] != null) return frames[0];
        }

        // Secondary fallback to knight frames
        switch (animState) {
            case STATE_IDLE: frames = AssetManager.knightIdleFrames; break;
            case STATE_RUN: frames = AssetManager.knightRunFrames; break;
            case STATE_ATTACK: frames = AssetManager.knightAttackFrames; break;
            case STATE_JUMP: frames = AssetManager.knightJumpFrames; break;
            case STATE_HURT: frames = AssetManager.knightHurtFrames; break;
            case STATE_DIE:
            case STATE_DEATH_EXPLOSION: frames = AssetManager.knightDeathFrames; break;
        }
        if (frames != null && frames.length > 0) {
            int validCount = getFrameCount(frames);
            int idx = Math.min(animFrame, validCount - 1);
            if (frames[idx] != null) return frames[idx];
            if (frames[0] != null) return frames[0];
        }

        if (AssetManager.hollowKnightIdleFrames != null && AssetManager.hollowKnightIdleFrames.length > 0 && AssetManager.hollowKnightIdleFrames[0] != null) {
            return AssetManager.hollowKnightIdleFrames[0];
        }
        if (AssetManager.knightIdleFrames != null && AssetManager.knightIdleFrames.length > 0 && AssetManager.knightIdleFrames[0] != null) {
            return AssetManager.knightIdleFrames[0];
        }

        return null;
    }

    private void drawMiniBossHUD(Graphics2D g2) {
        if (!alive && !dying) return;
        int barW = 400;
        int barH = 16;
        int barX = 440;
        int barY = 55;

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(barX - 10, barY - 22, barW + 20, barH + 30);
        g2.setColor(Color.ORANGE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString(name, barX, barY - 6);

        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(barX, barY, barW, barH);
        float hpPercent = (float) hp / maxHp;
        g2.setColor(new Color(255, 120, 0));
        g2.fillRect(barX, barY, (int) (barW * hpPercent), barH);
        g2.setColor(Color.WHITE);
        g2.drawRect(barX, barY, barW, barH);
    }
}
