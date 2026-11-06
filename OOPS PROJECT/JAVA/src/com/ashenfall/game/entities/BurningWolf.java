package com.ashenfall.game.entities;

import com.ashenfall.game.engine.AssetManager;
import com.ashenfall.game.engine.SoundManager;
import com.ashenfall.game.entities.player.Player;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

public class BurningWolf extends Enemy {
    public static final int STATE_PATROL = 0;
    public static final int STATE_HOWL = 1;
    public static final int STATE_RUN = 2;
    public static final int STATE_ATTACK = 3;
    public static final int STATE_DIE = 4;

    private int animState = STATE_PATROL;
    private int animFrame = 0;
    private int animTimer = 0;

    private boolean isCharging = false;
    private boolean isAttacking = false;
    private boolean hasHowled = false;
    private int howlTimer = 0;
    private int attackCooldown = 0;
    private boolean hasDealtDamageThisAttack = false;

    private boolean dying = false;
    private int deathTimer = 0;

    public BurningWolf(float x, float y) {
        super(x, y, 60, 36, 35, "Burning Wolf", 1, 150);
    }

    @Override
    public void updateAI(Player player, List<Projectile> projectiles, List<Particle> particles) {
        if (dying) {
            vx = 0;
            deathTimer--;
            if (deathTimer <= 0) {
                alive = false; // Finalize death; GamePanel removes enemy and awards score
            }

            // Burst of flame embers as it dissipates
            if (Math.random() < 0.6) {
                particles.add(new Particle(
                    x + (float) (Math.random() * width),
                    y + height - 8 + (float) (Math.random() * 8),
                    (float) (Math.random() * 3 - 1.5),
                    (float) (-Math.random() * 2.5 - 0.5),
                    18,
                    new Color(255, 80 + (int) (Math.random() * 120), 0),
                    4
                ));
            }

            super.update();
            return;
        }

        if (!alive) return;

        if (invincibilityTimer > 0) invincibilityTimer--;
        if (hitStunTimer > 0) {
            super.update();
            return;
        }

        if (attackCooldown > 0) attackCooldown--;

        float distToPlayer = Math.abs(player.x - x);
        float distY = Math.abs(player.y - y);

        // 1. Howl state
        if (animState == STATE_HOWL) {
            vx = 0;
            howlTimer--;

            // Intense fire particles erupting from neck/mane
            if (Math.random() < 0.5) {
                float maneX = facingRight ? x + width * 0.7f : x + width * 0.3f;
                particles.add(new Particle(
                    maneX, y + 4,
                    (float) (Math.random() * 2 - 1),
                    (float) (-Math.random() * 3 - 1),
                    16,
                    new Color(255, 120 + (int) (Math.random() * 100), 0),
                    4
                ));
            }

            if (howlTimer <= 0) {
                animState = STATE_RUN;
                isCharging = true;
                animFrame = 0;
                animTimer = 0;
            }
            super.update();
            return;
        }

        // 2. Attack state
        if (animState == STATE_ATTACK) {
            animTimer++;
            if (animTimer >= 5) {
                animTimer = 0;
                animFrame++;

                // Frame 1: Mid-air leaping bite
                if (animFrame == 1) {
                    vx = (facingRight ? 1.0f : -1.0f) * 7.5f;
                    vy = -2.5f;
                    SoundManager.playSound("slash");

                    // Fiery bite particles forward
                    float biteX = facingRight ? x + width + 8 : x - 8;
                    for (int i = 0; i < 6; i++) {
                        particles.add(new Particle(
                            biteX, y + height / 2.0f,
                            (float) (facingRight ? Math.random() * 3 + 1 : -Math.random() * 3 - 1),
                            (float) (Math.random() * 3 - 1.5),
                            15,
                            new Color(255, 100 + (int) (Math.random() * 120), 0),
                            4
                        ));
                    }

                    // Check player damage
                    float reach = 60;
                    float reachX = facingRight ? x + width - 10 : x - reach + 10;
                    Rectangle attackRect = new Rectangle((int) reachX, (int) y - 6, (int) reach, height + 12);
                    if (attackRect.intersects(player.getBounds()) && !hasDealtDamageThisAttack) {
                        player.takeDamage(damage, facingRight ? 6.5f : -6.5f);
                        hasDealtDamageThisAttack = true;
                    }
                } else if (animFrame == 2) {
                    vx = (facingRight ? 1.0f : -1.0f) * 2.0f;
                }

                int atkLen = (AssetManager.wolfAttackFrames != null && AssetManager.wolfAttackFrames.length > 0)
                        ? AssetManager.wolfAttackFrames.length : 3;
                if (animFrame >= atkLen) {
                    animState = isCharging ? STATE_RUN : STATE_PATROL;
                    animFrame = 0;
                    animTimer = 0;
                    isAttacking = false;
                }
            }
            super.update();
            return;
        }

        // 3. Combat & Movement AI
        if (distToPlayer < 360 && distY < 80) {
            facingRight = (player.x > x);

            // If spotting player for the first time, trigger a howl
            if (!hasHowled && AssetManager.wolfHowlFrame != null) {
                hasHowled = true;
                animState = STATE_HOWL;
                howlTimer = 22;
                SoundManager.playSound("boss_spell");
                vx = 0;
                super.update();
                return;
            }

            // Close range attack trigger
            if (distToPlayer < 75 && attackCooldown <= 0 && onGround) {
                animState = STATE_ATTACK;
                isAttacking = true;
                animFrame = 0;
                animTimer = 0;
                attackCooldown = 70;
                hasDealtDamageThisAttack = false;
                super.update();
                return;
            }

            // Charge at player
            isCharging = true;
            animState = STATE_RUN;
            vx = facingRight ? 5.8f : -5.8f;

            // Flaming trail particles
            if (Math.random() < 0.45) {
                float tailX = facingRight ? x + 4 : x + width - 4;
                particles.add(new Particle(
                    tailX, y + height - 6,
                    (float) (facingRight ? -Math.random() * 2.5 - 0.5 : Math.random() * 2.5 + 0.5),
                    (float) (-Math.random() * 2.0),
                    16,
                    new Color(255, 110 + (int) (Math.random() * 100), 0),
                    4
                ));
            }
        } else {
            // Calm down if player is away
            isCharging = false;
            hasHowled = false;
            animState = STATE_PATROL;

            // Patrol
            if (facingRight) {
                vx = 2.2f;
                if (x >= patrolMaxX) facingRight = false;
            } else {
                vx = -2.2f;
                if (x <= patrolMinX) facingRight = true;
            }
        }

        // Run / Patrol animation cycling
        animTimer++;
        if (animTimer >= (isCharging ? 4 : 6)) {
            animTimer = 0;
            int runLen = (AssetManager.wolfRunFrames != null && AssetManager.wolfRunFrames.length > 0)
                    ? AssetManager.wolfRunFrames.length : 4;
            animFrame = (animFrame + 1) % runLen;
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
            alive = true; // Retain alive status so death animation renders
            damage = 0;
            deathTimer = 25;
            animState = STATE_DIE;
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

        BufferedImage currentFrame = getCurrentFrame();

        if (currentFrame != null) {
            int drawH = height + 18;
            int drawW = (int) (drawH * ((float) currentFrame.getWidth() / currentFrame.getHeight()));
            int drawY = renderY + height - drawH + 4;

            if (facingRight) {
                int drawX = renderX - 4;
                g2.drawImage(currentFrame, drawX, drawY, drawW, drawH, null);
            } else {
                int drawX = renderX + width + 4 - drawW;
                g2.drawImage(currentFrame, drawX + drawW, drawY, -drawW, drawH, null);
            }
        } else {
            // Fallback Wolf Body
            g2.setColor(new Color(180, 50, 20));
            g2.fillRect(renderX + 6, renderY + 8, width - 12, height - 12);
            g2.setColor(new Color(255, 140, 0));
            g2.fillRect(renderX + (facingRight ? 2 : width - 10), renderY, 12, 14);
            g2.setColor(Color.YELLOW);
            int eyeX = facingRight ? renderX + width - 12 : renderX + 4;
            g2.fillRect(eyeX, renderY + 4, 6, 4);
        }

        if (!dying) {
            drawHealthBar(g2, camX, camY);
        }
    }

    private BufferedImage getCurrentFrame() {
        if (animState == STATE_HOWL) {
            if (AssetManager.wolfHowlFrame != null) return AssetManager.wolfHowlFrame;
        }

        if (animState == STATE_ATTACK) {
            if (AssetManager.wolfAttackFrames != null && AssetManager.wolfAttackFrames.length > 0) {
                int idx = Math.min(animFrame, AssetManager.wolfAttackFrames.length - 1);
                if (AssetManager.wolfAttackFrames[idx] != null) return AssetManager.wolfAttackFrames[idx];
            }
        }

        // Default or Run / Patrol
        if (AssetManager.wolfRunFrames != null && AssetManager.wolfRunFrames.length > 0) {
            int idx = Math.min(animFrame, AssetManager.wolfRunFrames.length - 1);
            if (AssetManager.wolfRunFrames[idx] != null) return AssetManager.wolfRunFrames[idx];
            if (AssetManager.wolfRunFrames[0] != null) return AssetManager.wolfRunFrames[0];
        }

        if (AssetManager.wolfIdleFrames != null && AssetManager.wolfIdleFrames.length > 0 && AssetManager.wolfIdleFrames[0] != null) {
            return AssetManager.wolfIdleFrames[0];
        }

        return null;
    }
}
