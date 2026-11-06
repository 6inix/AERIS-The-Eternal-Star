package com.ashenfall.game.entities;

import com.ashenfall.game.engine.AssetManager;
import com.ashenfall.game.engine.SoundManager;
import com.ashenfall.game.entities.player.Player;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

public class Ashwalker extends Enemy {
    public static final int STATE_IDLE = 0;
    public static final int STATE_RUN = 1;
    public static final int STATE_JUMP = 2;
    public static final int STATE_ATTACK = 3;
    public static final int STATE_DIE = 4;

    private int animState = STATE_IDLE;
    private int animFrame = 0;
    private int animTimer = 0;

    private int lungeCooldown = 0;
    private int attackCooldown = 0;
    private boolean dying = false;
    private boolean hasDealtDamageThisAttack = false;

    public Ashwalker(float x, float y) {
        super(x, y, 40, 44, 25, "Ashwalker", 1, 100);
    }

    @Override
    public void updateAI(Player player, List<Projectile> projectiles, List<Particle> particles) {
        if (dying) {
            vx = 0;
            animTimer++;
            if (animTimer >= 7) {
                animTimer = 0;
                animFrame++;
                if (animFrame >= AssetManager.ashwalkerDieFrames.length) {
                    alive = false; // Finalize death; GamePanel removes enemy and awards score
                }
            }

            // Emit crumbling ash particles while dissolving
            if (Math.random() < 0.45) {
                particles.add(new Particle(
                    x + (float) (Math.random() * width),
                    y + height - 12 + (float) (Math.random() * 8),
                    (float) (Math.random() * 2 - 1),
                    (float) (-Math.random() * 1.8),
                    18,
                    new Color(90, 85, 85, 220),
                    4
                ));
            }

            super.update();
            return;
        }

        if (!alive) return;

        if (attackCooldown > 0) attackCooldown--;
        if (lungeCooldown > 0) lungeCooldown--;

        if (hitStunTimer > 0) {
            super.update();
            return;
        }

        float distToPlayer = Math.abs(player.x - x);
        float distY = Math.abs(player.y - y);

        // Handle active attack state
        if (animState == STATE_ATTACK) {
            vx = 0; // Stand firmly while executing slash
            animTimer++;
            if (animTimer >= 5) {
                animTimer = 0;
                animFrame++;

                // Slash hit frame: full forward claw/blade extension
                if (animFrame == 2 && !hasDealtDamageThisAttack) {
                    float attackReach = 65;
                    float forwardReachX = facingRight ? x + width : x - attackReach;
                    Rectangle attackBox = new Rectangle((int) forwardReachX, (int) y - 10, (int) attackReach, height + 20);

                    if (attackBox.intersects(player.getBounds())) {
                        player.takeDamage(damage, facingRight ? 6.0f : -6.0f);
                    }
                    SoundManager.playSound("slash");

                    // Spawn fiery ash slash particles
                    float slashX = facingRight ? x + width + 10 : x - 10;
                    for (int i = 0; i < 6; i++) {
                        particles.add(new Particle(
                            slashX, y + height / 2.0f,
                            (float) (facingRight ? Math.random() * 3 + 1 : -Math.random() * 3 - 1),
                            (float) (Math.random() * 3 - 1.5),
                            14,
                            new Color(230, 110, 30),
                            3
                        ));
                    }
                    hasDealtDamageThisAttack = true;
                }

                if (animFrame >= AssetManager.ashwalkerAttackFrames.length) {
                    animState = STATE_IDLE;
                    animFrame = 0;
                    animTimer = 0;
                }
            }
        } else if (!onGround) {
            // Jump / In-air state
            if (animState != STATE_JUMP) {
                animState = STATE_JUMP;
                animFrame = 0;
                animTimer = 0;
            }
            animTimer++;
            if (animTimer >= 6) {
                animTimer = 0;
                if (animFrame < AssetManager.ashwalkerJumpFrames.length - 1) {
                    animFrame++;
                }
            }
        } else {
            // On Ground Actions: Attack, Lunge, Chase, or Patrol
            if (distToPlayer < 240 && distY < 80) {
                facingRight = (player.x > x);
                float moveDir = facingRight ? 1.0f : -1.0f;

                // Close range: execute melee attack
                if (distToPlayer < 55 && attackCooldown <= 0) {
                    animState = STATE_ATTACK;
                    animFrame = 0;
                    animTimer = 0;
                    attackCooldown = 75;
                    hasDealtDamageThisAttack = false;
                    vx = 0;
                }
                // Mid range: leap/lunge towards player
                else if (distToPlayer < 160 && distToPlayer >= 55 && lungeCooldown <= 0) {
                    vy = -6.5f;
                    vx = moveDir * 6.5f;
                    lungeCooldown = 100;
                    animState = STATE_JUMP;
                    animFrame = 0;
                    animTimer = 0;
                    SoundManager.playSound("jump");
                }
                // Chase player
                else {
                    vx = moveDir * 3.6f;
                    setRunOrIdle();
                }
            } else {
                // Patrol between boundaries
                if (facingRight) {
                    vx = 2.0f;
                    if (x >= patrolMaxX) facingRight = false;
                } else {
                    vx = -2.0f;
                    if (x <= patrolMinX) facingRight = true;
                }
                setRunOrIdle();
            }
        }

        super.update();
    }

    private void setRunOrIdle() {
        if (Math.abs(vx) > 0.4f) {
            if (animState != STATE_RUN) {
                animState = STATE_RUN;
                animFrame = 0;
                animTimer = 0;
            }
            animTimer++;
            if (animTimer >= 4) {
                animTimer = 0;
                int runLen = (AssetManager.ashwalkerRunFrames != null && AssetManager.ashwalkerRunFrames.length > 0)
                        ? AssetManager.ashwalkerRunFrames.length : 8;
                animFrame = (animFrame + 1) % runLen;
            }
        } else {
            if (animState != STATE_IDLE) {
                animState = STATE_IDLE;
                animFrame = 0;
                animTimer = 0;
            }
            animTimer++;
            if (animTimer >= 6) {
                animTimer = 0;
                int idleLen = (AssetManager.ashwalkerIdleFrames != null && AssetManager.ashwalkerIdleFrames.length > 0)
                        ? AssetManager.ashwalkerIdleFrames.length : 5;
                animFrame = (animFrame + 1) % idleLen;
            }
        }
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
            animState = STATE_DIE;
            animFrame = 0;
            animTimer = 0;
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
            int drawY = renderY + height - drawH + 2;

            if (animState == STATE_DIE) {
                int drawX = renderX + (width - drawW) / 2;
                if (facingRight) {
                    g2.drawImage(currentFrame, drawX, drawY, drawW, drawH, null);
                } else {
                    g2.drawImage(currentFrame, drawX + drawW, drawY, -drawW, drawH, null);
                }
            } else {
                if (facingRight) {
                    int drawX = renderX - 4;
                    g2.drawImage(currentFrame, drawX, drawY, drawW, drawH, null);
                } else {
                    int drawX = renderX + width + 4 - drawW;
                    g2.drawImage(currentFrame, drawX + drawW, drawY, -drawW, drawH, null);
                }
            }
        } else {
            // Fallback visual design: Small ash creature with flaming hood
            g2.setColor(new Color(40, 30, 30));
            g2.fillRect(renderX + 4, renderY + 12, width - 8, height - 12);
            g2.setColor(new Color(255, 90, 0));
            g2.fillOval(renderX + 2, renderY, width - 4, 18);
            g2.setColor(new Color(255, 220, 0));
            int eyeX = facingRight ? renderX + width - 10 : renderX + 4;
            g2.fillRect(eyeX, renderY + 6, 6, 4);
        }

        if (!dying) {
            drawHealthBar(g2, camX, camY);
        }
    }

    private BufferedImage getCurrentFrame() {
        BufferedImage[] frames = null;
        switch (animState) {
            case STATE_ATTACK:
                frames = AssetManager.ashwalkerAttackFrames;
                break;
            case STATE_JUMP:
                frames = AssetManager.ashwalkerJumpFrames;
                break;
            case STATE_DIE:
                frames = AssetManager.ashwalkerDieFrames;
                break;
            case STATE_RUN:
                frames = AssetManager.ashwalkerRunFrames;
                break;
            case STATE_IDLE:
            default:
                frames = AssetManager.ashwalkerIdleFrames;
                if (frames == null || frames[0] == null) {
                    frames = AssetManager.ashwalkerFrames;
                }
                break;
        }

        if (frames != null && frames.length > 0) {
            int idx = Math.min(animFrame, frames.length - 1);
            if (frames[idx] != null) return frames[idx];
            if (frames[0] != null) return frames[0];
        }

        if (AssetManager.ashwalkerFrames != null && AssetManager.ashwalkerFrames[0] != null) {
            return AssetManager.ashwalkerFrames[0];
        }

        return null;
    }
}
