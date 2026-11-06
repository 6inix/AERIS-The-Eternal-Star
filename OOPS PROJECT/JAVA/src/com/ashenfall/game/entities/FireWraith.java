package com.ashenfall.game.entities;

import com.ashenfall.game.engine.AssetManager;
import com.ashenfall.game.engine.SoundManager;
import com.ashenfall.game.entities.player.Player;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

public class FireWraith extends Enemy {
    public static final int STATE_RUN = 0;
    public static final int STATE_ATTACK = 1;
    public static final int STATE_DIE = 2;

    private int animState = STATE_RUN;
    private int animFrame = 0;
    private int animTimer = 0;
    private float floatAngle = 0;
    private int shootCooldown = 60;
    private boolean dying = false;
    private boolean hasFiredProjectileInAttack = false;

    public FireWraith(float x, float y) {
        super(x, y, 42, 48, 40, "Fire Wraith", 1, 250);
        this.floatAngle = (float) (Math.random() * Math.PI * 2);
    }

    @Override
    public void updateAI(Player player, List<Projectile> projectiles, List<Particle> particles) {
        if (dying) {
            vx = 0;
            vy = 0.2f;
            y += vy;

            animTimer++;
            // Flame embers and ash particles while dissolving
            if (Math.random() < 0.6) {
                particles.add(new Particle(
                    x + (float) (Math.random() * width),
                    y + (float) (Math.random() * height),
                    (float) (Math.random() * 3 - 1.5),
                    (float) (-Math.random() * 2.5 - 0.5),
                    18,
                    new Color(255, 60 + (int) (Math.random() * 120), 0),
                    4
                ));
            }

            if (animTimer >= 6) {
                animTimer = 0;
                animFrame++;
                if (animFrame >= AssetManager.fireWraithDieFrames.length) {
                    alive = false; // Finalize death; GamePanel will remove and score
                }
            }
            return;
        }

        if (!alive) return;

        if (invincibilityTimer > 0) invincibilityTimer--;
        if (hitStunTimer > 0) hitStunTimer--;
        if (shootCooldown > 0) shootCooldown--;

        floatAngle += 0.08f;
        vy = (float) Math.sin(floatAngle) * 1.5f;

        float dist = (float) Math.hypot(player.x - x, player.y - y);

        if (animState == STATE_ATTACK) {
            vx = 0; // Float stationary while casting fire blast
            animTimer++;
            if (animTimer >= 5) {
                animTimer = 0;
                animFrame++;

                // Key attack frame: unleash fireball projectile
                if (animFrame == 3 && !hasFiredProjectileInAttack) {
                    if (dist < 1) dist = 1;
                    float dirX = (player.x - (x + width / 2.0f)) / dist;
                    float dirY = (player.y - (y + height / 2.0f)) / dist;
                    float spawnX = facingRight ? x + width + 8 : x - 18;
                    float spawnY = y + height / 2.0f - 8;

                    projectiles.add(new Projectile(spawnX, spawnY, dirX * 6.5f, dirY * 6.5f, 16, 16, 1, true, false));
                    SoundManager.playSound("boss_spell");

                    for (int i = 0; i < 8; i++) {
                        particles.add(new Particle(
                            spawnX, spawnY,
                            (float) (Math.random() * 4 - 2 + (facingRight ? 3 : -3)),
                            (float) (Math.random() * 4 - 2),
                            16,
                            new Color(255, 100 + (int) (Math.random() * 120), 0),
                            4
                        ));
                    }
                    hasFiredProjectileInAttack = true;
                }

                if (animFrame >= AssetManager.fireWraithAttackFrames.length) {
                    animState = STATE_RUN;
                    animFrame = 0;
                    animTimer = 0;
                }
            }
        } else {
            // Hover / Run cycle
            animTimer++;
            if (animTimer >= 5) {
                animTimer = 0;
                animFrame = (animFrame + 1) % AssetManager.fireWraithRunFrames.length;
            }

            if (dist < 380) {
                facingRight = (player.x > x);
                vx = facingRight ? 1.5f : -1.5f;

                // Adjust vertical position to stay level with player
                if (Math.abs(y - player.y) > 35) {
                    vy += (player.y > y ? 0.4f : -0.4f);
                }

                if (shootCooldown <= 0) {
                    shootCooldown = 130;
                    animState = STATE_ATTACK;
                    animFrame = 0;
                    animTimer = 0;
                    hasFiredProjectileInAttack = false;
                }
            } else {
                // Patrol slowly within patrol area
                if (facingRight) {
                    vx = 1.0f;
                    if (x >= patrolMaxX) facingRight = false;
                } else {
                    vx = -1.0f;
                    if (x <= patrolMinX) facingRight = true;
                }
            }

            // Passive floating ember particles
            if (Math.random() < 0.25) {
                float tailX = facingRight ? x - 4 : x + width + 4;
                particles.add(new Particle(
                    tailX, y + height / 2.0f + (float) (Math.random() * 12 - 6),
                    (float) (facingRight ? -Math.random() * 1.5 : Math.random() * 1.5),
                    (float) (-Math.random() * 1.5),
                    16,
                    new Color(255, 120, 20, 220),
                    3
                ));
            }
        }

        x += vx;
        y += vy;
    }

    @Override
    public void takeDamage(int amount, float knockbackX) {
        if (!alive || dying) return;

        super.takeDamage(amount, knockbackX);

        if (hp <= 0 && !dying) {
            hp = 0;
            dying = true;
            alive = true; // Retain alive status so death animation renders
            damage = 0;   // Inactive damage while dying
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
            int drawH = height + 24;
            int drawW = (int) (drawH * ((float) currentFrame.getWidth() / currentFrame.getHeight()));
            int drawY = renderY + (height - drawH) / 2;

            if (animState == STATE_DIE) {
                int drawX = renderX + (width - drawW) / 2;
                if (facingRight) {
                    g2.drawImage(currentFrame, drawX, drawY, drawW, drawH, null);
                } else {
                    g2.drawImage(currentFrame, drawX + drawW, drawY, -drawW, drawH, null);
                }
            } else {
                if (facingRight) {
                    int drawX = renderX + width + 6 - drawW;
                    g2.drawImage(currentFrame, drawX, drawY, drawW, drawH, null);
                } else {
                    int drawX = renderX - 6;
                    g2.drawImage(currentFrame, drawX + drawW, drawY, -drawW, drawH, null);
                }
            }
        } else {
            // Fallback visual design: Floating spirit cloak & glowing core
            g2.setColor(new Color(220, 60, 40, 220));
            g2.fillOval(renderX, renderY, width, height - 10);
            g2.setColor(new Color(255, 140, 0, 200));
            g2.fillOval(renderX + 4, renderY + 4, width - 8, height - 16);

            g2.setColor(Color.YELLOW);
            g2.fillOval(renderX + 12, renderY + 10, 12, 12);
        }

        if (!dying) {
            drawHealthBar(g2, camX, camY);
        }
    }

    private BufferedImage getCurrentFrame() {
        BufferedImage[] frames = null;
        switch (animState) {
            case STATE_ATTACK:
                frames = AssetManager.fireWraithAttackFrames;
                break;
            case STATE_DIE:
                frames = AssetManager.fireWraithDieFrames;
                break;
            case STATE_RUN:
            default:
                frames = AssetManager.fireWraithRunFrames;
                break;
        }

        if (frames != null && frames.length > 0) {
            int idx = Math.min(animFrame, frames.length - 1);
            return frames[idx];
        }
        return null;
    }
}
