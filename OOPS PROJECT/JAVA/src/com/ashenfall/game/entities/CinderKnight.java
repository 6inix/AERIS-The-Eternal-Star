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

public class CinderKnight extends Enemy {
    public boolean isBlocking = false;
    private int attackCooldown = 0;

    // Animation States
    public static final int STATE_IDLE = 0;
    public static final int STATE_WALK = 1;
    public static final int STATE_RUN = 2;
    public static final int STATE_JUMP = 3;
    public static final int STATE_ATTACK = 4;
    public static final int STATE_SHIELD_RAISE = 5;
    public static final int STATE_SHIELD_IDLE = 6;
    public static final int STATE_SHIELD_RUN = 7;
    public static final int STATE_SHIELD_BLOCK = 8;
    public static final int STATE_SHIELD_BASH = 9;
    public static final int STATE_SHIELD_LOWER = 10;
    public static final int STATE_HURT = 11;
    public static final int STATE_DEATH = 12;

    private int animState = STATE_IDLE;
    private int animFrame = 0;
    private int animTimer = 0;
    private boolean dying = false;
    private boolean hasHitInAttack = false;

    public boolean isFinalBoss = false;

    public CinderKnight(float x, float y) {
        this(x, y, false, false);
    }

    public CinderKnight(float x, float y, boolean isMiniBoss) {
        this(x, y, isMiniBoss, false);
    }

    public CinderKnight(float x, float y, boolean isMiniBoss, boolean isFinalBoss) {
        super(x, y,
              isFinalBoss ? 130 : 64,
              isFinalBoss ? 150 : 75,
              isFinalBoss ? 500 : (isMiniBoss ? 180 : 70),
              isFinalBoss ? "CINDER KNIGHT - THE INFERNAL LORD (FINAL BOSS)" : (isMiniBoss ? "CINDER KNIGHT (MINI-BOSS)" : "Cinder Knight"),
              isFinalBoss ? 4 : 2,
              isFinalBoss ? 10000 : (isMiniBoss ? 1200 : 300));
        this.isMiniBoss = isMiniBoss;
        this.isBoss = isFinalBoss;
        this.isFinalBoss = isFinalBoss;
        if (isFinalBoss) {
            this.patrolMinX = x - 250;
            this.patrolMaxX = x + 250;
        }
    }

    @Override
    public void updateAI(Player player, List<Projectile> projectiles, List<Particle> particles) {
        if (dying) {
            vx = 0;
            // Dying animation update
            animTimer++;
            if (animTimer >= (isFinalBoss ? 14 : 10)) {
                animTimer = 0;
                animFrame++;
                if (animFrame >= getFrameCount(AssetManager.knightDeathFrames)) {
                    animFrame = getFrameCount(AssetManager.knightDeathFrames) - 1;
                    alive = false; // Finalize death; GamePanel removes enemy and awards score
                }
            }

            // Fiery cinder & ash dissolving particles
            int pCount = isFinalBoss ? 4 : 1;
            for (int i = 0; i < pCount; i++) {
                if (Math.random() < (isFinalBoss ? 0.9 : 0.5)) {
                    particles.add(new Particle(
                        x + (float) (Math.random() * width),
                        y + height - 12 + (float) (Math.random() * 20),
                        (float) (Math.random() * (isFinalBoss ? 6 : 2) - (isFinalBoss ? 3 : 1)),
                        (float) (-Math.random() * (isFinalBoss ? 5 : 2) - 0.5f),
                        25,
                        new Color(255, 90 + (int) (Math.random() * 120), 0),
                        isFinalBoss ? 7 : 4
                    ));
                }
            }

            super.update();
            return;
        }

        if (!alive) return;

        // Final boss phase 2 enraged flame aura
        if (isFinalBoss && hp <= maxHp / 2 && Math.random() < 0.45) {
            particles.add(new Particle(
                x + (float) (Math.random() * width),
                y + (float) (Math.random() * height),
                (float) (Math.random() * 3 - 1.5),
                (float) (-Math.random() * 3 - 1.0),
                22,
                new Color(255, 60, 0, 200),
                5
            ));
        }

        // Advance current state animation frame
        animTimer++;
        if (animTimer >= 5) {
            animTimer = 0;
            animFrame++;

            // Handle non-looping action animations
            if (animState == STATE_ATTACK) {
                // Spawn fiery sword slash particles on hit frame
                if (animFrame == 2 && !hasHitInAttack) {
                    float slashX = facingRight ? x + width + (isFinalBoss ? 24 : 10) : x - (isFinalBoss ? 24 : 10);
                    int slashParticles = isFinalBoss ? 16 : 6;
                    for (int i = 0; i < slashParticles; i++) {
                        particles.add(new Particle(
                            slashX, y + height / 2.0f + (float) (Math.random() * (isFinalBoss ? 60 : 20) - (isFinalBoss ? 30 : 10)),
                            (float) (facingRight ? Math.random() * (isFinalBoss ? 6 : 3) + 2 : -Math.random() * (isFinalBoss ? 6 : 3) - 2),
                            (float) (Math.random() * 4 - 2),
                            18,
                            new Color(255, 120, 30),
                            isFinalBoss ? 6 : 3
                        ));
                    }
                    if (isFinalBoss) {
                        // Ground shockwave
                        for (int i = 0; i < 10; i++) {
                            particles.add(new Particle(
                                slashX, y + height - 8,
                                (float) (Math.random() * 8 - 4),
                                -3.0f,
                                20,
                                new Color(255, 60, 0),
                                6
                            ));
                        }
                    }
                    hasHitInAttack = true;
                }
                if (animFrame >= getFrameCount(AssetManager.knightAttackFrames)) {
                    animState = isBlocking ? STATE_SHIELD_IDLE : STATE_IDLE;
                    animFrame = 0;
                    hasHitInAttack = false;
                }
            } else if (animState == STATE_SHIELD_BASH) {
                if (animFrame >= getFrameCount(AssetManager.knightShieldBashFrames)) {
                    animState = STATE_SHIELD_IDLE;
                    animFrame = 0;
                }
            } else if (animState == STATE_SHIELD_RAISE) {
                if (animFrame >= getFrameCount(AssetManager.knightShieldRaiseFrames)) {
                    animState = STATE_SHIELD_IDLE;
                    animFrame = 0;
                }
            } else if (animState == STATE_SHIELD_LOWER) {
                if (animFrame >= getFrameCount(AssetManager.knightShieldLowerFrames)) {
                    animState = STATE_IDLE;
                    animFrame = 0;
                }
            } else if (animState == STATE_HURT) {
                if (animFrame >= getFrameCount(AssetManager.knightHurtFrames)) {
                    animState = isBlocking ? STATE_SHIELD_IDLE : STATE_IDLE;
                    animFrame = 0;
                }
            } else if (animState == STATE_SHIELD_BLOCK) {
                if (animFrame >= getFrameCount(AssetManager.knightShieldBlockFrames)) {
                    animState = STATE_SHIELD_IDLE;
                    animFrame = 0;
                }
            }
        }

        if (hitStunTimer > 0) {
            super.update();
            return;
        }

        if (attackCooldown > 0) attackCooldown--;

        float distToPlayer = Math.abs(player.x - x);
        float aggroRange = isFinalBoss ? 680 : 320;
        float blockRange = isFinalBoss ? 240 : 180;
        float attackThreshold = isFinalBoss ? 135 : 65;
        int hitDmg = isFinalBoss ? (hp <= maxHp / 2 ? 5 : 4) : 2;
        float bashKb = isFinalBoss ? 14.0f : 9.0f;
        float slashKb = isFinalBoss ? 10.0f : 6.0f;

        // AI logic if performing a specific action
        if (animState != STATE_ATTACK && animState != STATE_SHIELD_BASH && animState != STATE_HURT && animState != STATE_DEATH) {
            if (!onGround) {
                setAnimState(STATE_JUMP);
                int jumpCount = getFrameCount(AssetManager.knightJumpFrames);
                if (animFrame >= jumpCount) animFrame = jumpCount - 1;
            } else if (distToPlayer < aggroRange) {
                facingRight = (player.x > x);

                // Block/Shield stance when player is close or facing knight
                isBlocking = (distToPlayer < blockRange);

                if (distToPlayer < attackThreshold && attackCooldown <= 0) {
                    attackCooldown = isFinalBoss ? (hp <= maxHp / 2 ? 50 : 65) : 75;
                    hasHitInAttack = false;
                    if (Math.random() < 0.5) {
                        setAnimState(STATE_SHIELD_BASH);
                        SoundManager.playSound("hit");
                        if (getBounds().intersects(player.getBounds())) {
                            player.takeDamage(hitDmg, facingRight ? bashKb : -bashKb);
                        }
                    } else {
                        setAnimState(STATE_ATTACK);
                        SoundManager.playSound("slash");
                        if (getBounds().intersects(player.getBounds())) {
                            player.takeDamage(hitDmg, facingRight ? slashKb : -slashKb);
                        }
                    }
                } else {
                    float moveSpeed = isBlocking ? (isFinalBoss ? 2.8f : 2.2f) : (isFinalBoss ? 4.2f : 3.5f);
                    vx = facingRight ? moveSpeed : -moveSpeed;
                    if (isBlocking) {
                        setAnimState(STATE_SHIELD_RUN);
                    } else {
                        setAnimState(STATE_RUN);
                    }
                    int runCount = getFrameCount(AssetManager.knightRunFrames);
                    if (animFrame >= runCount) animFrame = 0;
                }
            } else {
                isBlocking = false;
                if (facingRight) {
                    vx = isFinalBoss ? 2.4f : 1.8f;
                    setAnimState(STATE_WALK);
                    if (x >= patrolMaxX) facingRight = false;
                } else {
                    vx = isFinalBoss ? -2.4f : -1.8f;
                    setAnimState(STATE_WALK);
                    if (x <= patrolMinX) facingRight = true;
                }
                int walkCount = getFrameCount(AssetManager.knightWalkFrames);
                if (animFrame >= walkCount) animFrame = 0;
            }
        } else {
            vx = 0;
        }

        super.update();
    }

    private void setAnimState(int newState) {
        if (animState != newState) {
            animState = newState;
            animFrame = 0;
            animTimer = 0;
        }
    }

    @Override
    public void takeDamage(int amount, float knockbackX) {
        if (!alive || dying) return;

        float kb = isFinalBoss ? knockbackX * 0.15f : knockbackX;
        if (isBlocking) {
            setAnimState(STATE_SHIELD_BLOCK);
            super.takeDamage(1, kb * 0.2f);
        } else {
            setAnimState(STATE_HURT);
            super.takeDamage(amount, kb);
        }

        if (hp <= 0 && !dying) {
            hp = 0;
            dying = true;
            alive = true; // Stay alive during death animation
            damage = 0;   // Cannot hurt player while dying
            setAnimState(STATE_DEATH);
            SoundManager.playSound("hit");
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
            int drawH = isFinalBoss ? 240 : (height + 35);
            int drawW = (int) (drawH * ((float) currentFrame.getWidth() / currentFrame.getHeight()));
            int drawY = renderY + height - drawH + 4; // Boots firmly on the ground

            if (animState == STATE_DEATH) {
                int drawX = renderX + (width - drawW) / 2;
                if (facingRight) {
                    g2.drawImage(currentFrame, drawX, drawY, drawW, drawH, null);
                } else {
                    g2.drawImage(currentFrame, drawX + drawW, drawY, -drawW, drawH, null);
                }
            } else {
                int offset = isFinalBoss ? 45 : 16;
                if (facingRight) {
                    int drawX = renderX - offset;
                    g2.drawImage(currentFrame, drawX, drawY, drawW, drawH, null);
                } else {
                    int drawX = renderX + width + offset - drawW;
                    g2.drawImage(currentFrame, drawX + drawW, drawY, -drawW, drawH, null);
                }
            }
        } else {
            // Procedural fallback
            g2.setColor(new Color(60, 50, 60));
            g2.fillRect(renderX + 4, renderY + 12, width - 8, height - 12);
            g2.setColor(new Color(255, 60, 0));
            g2.fillRect(renderX + 12, renderY - 8, 16, 10);
        }

        if (isFinalBoss) {
            drawFinalBossHUD(g2);
        } else if (isMiniBoss) {
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
            case STATE_IDLE: frames = AssetManager.knightIdleFrames; break;
            case STATE_WALK: frames = AssetManager.knightWalkFrames; break;
            case STATE_RUN: frames = AssetManager.knightRunFrames; break;
            case STATE_JUMP: frames = AssetManager.knightJumpFrames; break;
            case STATE_ATTACK: frames = AssetManager.knightAttackFrames; break;
            case STATE_SHIELD_RAISE: frames = AssetManager.knightShieldRaiseFrames; break;
            case STATE_SHIELD_IDLE: frames = AssetManager.knightShieldIdleFrames; break;
            case STATE_SHIELD_RUN: frames = AssetManager.knightShieldRunFrames; break;
            case STATE_SHIELD_BLOCK: frames = AssetManager.knightShieldBlockFrames; break;
            case STATE_SHIELD_BASH: frames = AssetManager.knightShieldBashFrames; break;
            case STATE_SHIELD_LOWER: frames = AssetManager.knightShieldLowerFrames; break;
            case STATE_HURT: frames = AssetManager.knightHurtFrames; break;
            case STATE_DEATH: frames = AssetManager.knightDeathFrames; break;
        }

        if (frames != null && frames.length > 0) {
            int validCount = getFrameCount(frames);
            int idx = Math.min(animFrame, validCount - 1);
            if (frames[idx] != null) return frames[idx];
            if (frames[0] != null) return frames[0];
        }

        // Secondary fallback to Hollow Knight frames
        switch (animState) {
            case STATE_IDLE: frames = AssetManager.hollowKnightIdleFrames; break;
            case STATE_WALK:
            case STATE_RUN: frames = AssetManager.hollowKnightRunFrames; break;
            case STATE_JUMP: frames = AssetManager.hollowKnightJumpFrames; break;
            case STATE_ATTACK: frames = AssetManager.hollowKnightAttackFrames; break;
            case STATE_HURT: frames = AssetManager.hollowKnightHurtFrames; break;
            case STATE_DEATH: frames = AssetManager.hollowKnightDieFrames; break;
        }
        if (frames != null && frames.length > 0) {
            int validCount = getFrameCount(frames);
            int idx = Math.min(animFrame, validCount - 1);
            if (frames[idx] != null) return frames[idx];
            if (frames[0] != null) return frames[0];
        }

        if (AssetManager.knightIdleFrames != null && AssetManager.knightIdleFrames.length > 0 && AssetManager.knightIdleFrames[0] != null) {
            return AssetManager.knightIdleFrames[0];
        }
        if (AssetManager.hollowKnightIdleFrames != null && AssetManager.hollowKnightIdleFrames.length > 0 && AssetManager.hollowKnightIdleFrames[0] != null) {
            return AssetManager.hollowKnightIdleFrames[0];
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
        g2.setColor(new Color(255, 100, 0));
        g2.fillRect(barX, barY, (int) (barW * hpPercent), barH);
        g2.setColor(Color.WHITE);
        g2.drawRect(barX, barY, barW, barH);
    }

    private void drawFinalBossHUD(Graphics2D g2) {
        if (!alive && !dying) return;
        int barW = 640;
        int barH = 24;
        int barX = (1280 - barW) / 2;
        int barY = 32;

        // Gothic Shadow backing
        g2.setColor(new Color(0, 0, 0, 220));
        g2.fillRect(barX - 16, barY - 26, barW + 32, barH + 38);

        // Header Title
        boolean enraged = hp <= maxHp / 2;
        g2.setColor(enraged ? new Color(255, 60, 20) : new Color(255, 205, 50));
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        String title = name + (enraged ? " [ENRAGED PHASE 2]" : "");
        g2.drawString(title, barX, barY - 8);

        // HP background bar
        g2.setColor(new Color(40, 25, 25));
        g2.fillRect(barX, barY, barW, barH);

        // HP foreground fill with fiery gradient
        float hpPercent = Math.max(0.0f, Math.min(1.0f, (float) hp / maxHp));
        int fillW = (int) (barW * hpPercent);
        if (fillW > 0) {
            java.awt.Paint oldPaint = g2.getPaint();
            java.awt.GradientPaint hpGrad = new java.awt.GradientPaint(
                barX, barY, enraged ? new Color(255, 30, 0) : new Color(255, 120, 0),
                barX, barY + barH, new Color(180, 20, 0)
            );
            g2.setPaint(hpGrad);
            g2.fillRect(barX, barY, fillW, barH);
            g2.setPaint(oldPaint);
        }

        // Gold ornate borders
        g2.setColor(new Color(255, 215, 0));
        g2.drawRect(barX, barY, barW, barH);
        g2.drawRect(barX - 1, barY - 1, barW + 2, barH + 2);

        // HP numerical text
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.setColor(Color.WHITE);
        String hpStr = Math.max(0, hp) + " / " + maxHp;
        int hpStrW = g2.getFontMetrics().stringWidth(hpStr);
        g2.drawString(hpStr, barX + (barW - hpStrW) / 2, barY + 17);
    }
}
