package com.ashenfall.game.world;

import com.ashenfall.game.engine.AssetManager;
import com.ashenfall.game.entities.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TileMap {
    public static final int TILE_SIZE = 48;
    // Lengthy 4-Stage World (8160 px = 170 tiles)
    public static final int WORLD_WIDTH = 8160;
    public static final int WORLD_HEIGHT = 720;

    public List<Tile> tiles = new ArrayList<>();
    public List<Enemy> enemies = new ArrayList<>();
    public List<Item> items = new ArrayList<>();
    public List<Checkpoint> checkpoints = new ArrayList<>();

    public Checkpoint activeCheckpoint = null;
    public HollowKnight miniBoss;
    public CinderKnight mainBoss;

    public boolean gateLocked = true;
    public Tile bossGateTile;

    public String bannerText = null;
    public int bannerTimer = 0;

    public TileMap() {
        generateLevel();
    }

    public void generateLevel() {
        tiles.clear();
        enemies.clear();
        items.clear();
        checkpoints.clear();
        activeCheckpoint = null;
        gateLocked = true;

        int groundY = 600;

        // =========================================================================
        // STAGE 1: Ashen Forest (x: 0 to 2040) - Assets from stage1_ashen_forest_tiles
        // =========================================================================
        BufferedImage s1Block = AssetManager.stage1Block != null ? AssetManager.stage1Block : getTileSprite(AssetManager.stage1Tiles, 0);
        BufferedImage s1Plat = AssetManager.stage1Platform != null ? AssetManager.stage1Platform : getTileSprite(AssetManager.stage1Tiles, 12);
        BufferedImage s1Boulder = AssetManager.stage1Boulder != null ? AssetManager.stage1Boulder : s1Block;
        BufferedImage s1Coin = AssetManager.stage1Coin != null ? AssetManager.stage1Coin : getItemSprite(AssetManager.itemCoins, 0);
        BufferedImage s1Heart = AssetManager.stage1Heart != null ? AssetManager.stage1Heart : getItemSprite(AssetManager.itemHearts, 0);

        for (int x = 0; x < 2040; x += TILE_SIZE) {
            // Hazard Lava Pit - duplicated down till end of screen
            if (x >= 720 && x <= 864) {
                for (int ly = groundY + 20; ly < WORLD_HEIGHT + TILE_SIZE; ly += TILE_SIZE) {
                    tiles.add(new Tile(x, ly, TILE_SIZE, TILE_SIZE, false, true, 3, null, 1));
                }
                continue;
            }
            tiles.add(new Tile(x, groundY, TILE_SIZE, 120, true, false, 0, s1Block, 1));
        }

        // Stage 1 Ledges & Platforms using renamed platform.png
        tiles.add(new Tile(280, 480, 160, 24, true, false, 1, s1Plat, 1));
        tiles.add(new Tile(520, 390, 150, 24, true, false, 1, s1Plat, 1));
        tiles.add(new Tile(880, 440, 180, 24, true, false, 1, s1Plat, 1));
        tiles.add(new Tile(1200, 480, 180, 24, true, false, 1, s1Plat, 1));
        tiles.add(new Tile(1520, 410, 180, 24, true, false, 1, s1Plat, 1));
        tiles.add(new Tile(1800, 450, 160, 24, true, false, 1, s1Plat, 1));

        // Boulder obstacles using renamed boulder.png
        tiles.add(new Tile(650, groundY - 40, 44, 40, true, false, 2, s1Boulder, 1));
        tiles.add(new Tile(1400, groundY - 40, 44, 40, true, false, 2, s1Boulder, 1));

        // Stage 1 Items using renamed coin.png and heart.png
        spawnItem(350, 440, 24, 24, Item.TYPE_COIN, s1Coin, 1, "Forest Coin", 50, 0);
        spawnItem(580, 350, 24, 24, Item.TYPE_COIN, s1Coin, 1, "Forest Coin", 50, 0);
        spawnItem(940, 400, 26, 26, Item.TYPE_HEALTH, s1Heart, 1, "Heart Container", 50, 2);
        spawnItem(1260, 440, 24, 24, Item.TYPE_COIN, s1Coin, 1, "Forest Coin", 50, 0);
        spawnItem(1580, 370, 26, 26, Item.TYPE_SCROLL, AssetManager.itemScroll, 1, "Ancient Scroll", 150, 0);

        // Stage 1 Enemies (Ashwalkers and Burning Wolves only - No Hollow Knight, No Cinder Knight)
        enemies.add(new Ashwalker(400, groundY - 40));
        enemies.add(new BurningWolf(550, 350));
        enemies.add(new Ashwalker(900, 360));
        enemies.add(new Ashwalker(1350, groundY - 40));
        enemies.add(new BurningWolf(1700, groundY - 35));

        // Checkpoint 1 & 2 in Forest Section
        Checkpoint cp1 = new Checkpoint(1, "Stage 1: Ashen Forest", 1100, groundY - 85, 1100, groundY - 80, AssetManager.checkpointObelisk);
        checkpoints.add(cp1);
        Checkpoint cp2 = new Checkpoint(2, "Stage 2: Ancient Forest Shrine", 2000, groundY - 85, 2000, groundY - 80, AssetManager.checkpointLantern);
        checkpoints.add(cp2);

        // =========================================================================
        // STAGE 3: Hidden Shrine (x: 2040 to 4080) - Assets from stage4_hidden_shrine (Stage 3)
        // Immediately after Checkpoint 1 & 2 (x = 2000)
        // =========================================================================
        BufferedImage s3Pillar = getTileSprite(AssetManager.stage4Pillars, 0);
        BufferedImage s3Stairs = getTileSprite(AssetManager.stage4Stairs, 0);

        for (int x = 2040; x < 4080; x += TILE_SIZE) {
            // Hazard Temple Spikes / Lava Pit - duplicated down till end of screen
            if (x >= 2880 && x <= 3024) {
                for (int ly = groundY + 20; ly < WORLD_HEIGHT + TILE_SIZE; ly += TILE_SIZE) {
                    tiles.add(new Tile(x, ly, TILE_SIZE, TILE_SIZE, false, true, 3, AssetManager.stage4SpikeGate, 3));
                }
                continue;
            }
            BufferedImage groundSprite = getTileSprite(AssetManager.stage4PlatformTiles, (x / TILE_SIZE) % Math.max(1, AssetManager.stage4PlatformTiles.size()));
            tiles.add(new Tile(x, groundY, TILE_SIZE, 120, true, false, 0, groundSprite, 3));
        }

        // Stage 3 Ancient Temple Platforms & Pillars using Stage 3 (Hidden Shrine) assets
        tiles.add(new Tile(2200, 460, 180, 26, true, false, 1, getTileSprite(AssetManager.stage4PlatformTiles, 0), 3));
        tiles.add(new Tile(2500, 380, 180, 26, true, false, 1, getTileSprite(AssetManager.stage4PlatformTiles, 1), 3));
        tiles.add(new Tile(2840, 440, 180, 26, true, false, 1, getTileSprite(AssetManager.stage4PlatformTiles, 2), 3));
        tiles.add(new Tile(3180, 380, 180, 26, true, false, 1, getTileSprite(AssetManager.stage4PlatformTiles, 3), 3));
        tiles.add(new Tile(3520, 440, 180, 26, true, false, 1, getTileSprite(AssetManager.stage4PlatformTiles, 4), 3));
        tiles.add(new Tile(3820, 460, 180, 26, true, false, 1, getTileSprite(AssetManager.stage4PlatformTiles, 5), 3));

        // Ancient Carved Pillars from stage4Pillars
        tiles.add(new Tile(2380, groundY - 90, 36, 90, true, false, 2, s3Pillar, 3));
        tiles.add(new Tile(3380, groundY - 90, 36, 90, true, false, 2, s3Pillar, 3));

        // Temple Steps / Stairs from stage4Stairs
        tiles.add(new Tile(2680, groundY - 48, 64, 48, true, false, 1, s3Stairs, 3));
        tiles.add(new Tile(3680, groundY - 48, 64, 48, true, false, 1, s3Stairs, 3));

        // Stage 3 Items
        spawnItem(2260, 420, 24, 24, Item.TYPE_COIN, getItemSprite(AssetManager.itemCoins, 0), 3, "Shrine Coin", 100, 0);
        spawnItem(2560, 340, 26, 26, Item.TYPE_KEY, AssetManager.itemKey, 3, "Golden Shrine Key", 250, 0);
        spawnChest(2900, 400, 38, 34, getItemSprite(AssetManager.itemChests, 1), AssetManager.chestOpenImage, 3, "Sacred Blue Shrine Chest", 450);
        spawnItem(3240, 340, 26, 26, Item.TYPE_GEM, getItemSprite(AssetManager.itemGems, 1), 3, "Sapphire Relic", 300, 0);
        spawnItem(3580, 400, 26, 26, Item.TYPE_HEALTH, getItemSprite(AssetManager.itemHearts, 2), 3, "Divine Heart", 100, 3);
        spawnItem(3880, 350, 26, 26, Item.TYPE_CHARM, AssetManager.itemCharm, 3, "Soul Shield Charm", 350, 0);

        // Stage 3 Enemies (Wraiths, Wolves, Walkers only - No Hollow Knight, No Cinder Knight)
        enemies.add(new FireWraith(2320, 300));
        enemies.add(new FireWraith(2620, 300));
        enemies.add(new Ashwalker(3050, groundY - 40));
        enemies.add(new BurningWolf(3400, groundY - 35));
        enemies.add(new FireWraith(3750, 300));

        // Checkpoint 3: Hidden Shrine Sanctum (Boundary at x = 4040)
        Checkpoint cp3 = new Checkpoint(3, "Stage 3: Hidden Shrine Sanctum", 4040, groundY - 85, 4040, groundY - 80, AssetManager.checkpointLantern);
        checkpoints.add(cp3);

        // =========================================================================
        // STAGE 4 & 5: Lava Cavern & Molten Catacombs (x: 4080 to 6120) - Assets from stage2_lava_cavern_tiles
        // =========================================================================
        BufferedImage s2Plat = AssetManager.stage2Platform != null ? AssetManager.stage2Platform : getTileSprite(AssetManager.stage2Tiles, 8);
        BufferedImage s2Spike = AssetManager.stage2Spike != null ? AssetManager.stage2Spike : null;
        BufferedImage s2G1 = AssetManager.stage2Ground1 != null ? AssetManager.stage2Ground1 : getTileSprite(AssetManager.stage2Tiles, 0);
        BufferedImage s2G2 = AssetManager.stage2Ground2 != null ? AssetManager.stage2Ground2 : s2G1;
        BufferedImage s2Rock1 = AssetManager.stage2Rock1 != null ? AssetManager.stage2Rock1 : s2Plat;
        BufferedImage s2Rock2 = AssetManager.stage2Rock2 != null ? AssetManager.stage2Rock2 : s2Rock1;

        for (int x = 4080; x < 6120; x += TILE_SIZE) {
            // Hazard Spike & Lava Pits using renamed spike.png - duplicated down till end of screen
            if (x >= 4560 && x <= 4704) {
                for (int ly = groundY + 16; ly < WORLD_HEIGHT + TILE_SIZE; ly += TILE_SIZE) {
                    tiles.add(new Tile(x, ly, TILE_SIZE, TILE_SIZE, false, true, 3, s2Spike, 2));
                }
                continue;
            }
            if (x >= 5472 && x <= 5616) {
                for (int ly = groundY + 16; ly < WORLD_HEIGHT + TILE_SIZE; ly += TILE_SIZE) {
                    tiles.add(new Tile(x, ly, TILE_SIZE, TILE_SIZE, false, true, 3, s2Spike, 2));
                }
                continue;
            }
            BufferedImage groundSprite = ((x / TILE_SIZE) % 2 == 0) ? s2G1 : s2G2;
            tiles.add(new Tile(x, groundY, TILE_SIZE, 120, true, false, 0, groundSprite, 2));
        }

        // Stage 4 & 5 Platforms using renamed platform.png
        tiles.add(new Tile(4220, 460, 180, 28, true, false, 1, s2Plat, 2));
        tiles.add(new Tile(4520, 380, 180, 28, true, false, 1, s2Plat, 2));
        tiles.add(new Tile(4880, 440, 180, 28, true, false, 1, s2Plat, 2));
        tiles.add(new Tile(5200, 380, 180, 28, true, false, 1, s2Plat, 2));
        tiles.add(new Tile(5540, 440, 180, 28, true, false, 1, s2Plat, 2));
        tiles.add(new Tile(5840, 460, 180, 28, true, false, 1, s2Plat, 2));

        // Volcanic rocks & stalactites using sprite 61 & 62
        tiles.add(new Tile(4400, groundY - 45, 45, 45, true, false, 2, s2Rock1, 2));
        tiles.add(new Tile(5350, groundY - 45, 45, 45, true, false, 2, s2Rock2, 2));

        // Stage 4 & 5 Items
        spawnItem(4280, 420, 24, 24, Item.TYPE_COIN, getItemSprite(AssetManager.itemCoins, 3), 2, "Cavern Coin", 75, 0);
        spawnItem(4580, 340, 26, 26, Item.TYPE_HEALTH, getItemSprite(AssetManager.itemHearts, 1), 2, "Blue Heart", 75, 2);
        spawnChest(4940, 400, 36, 32, getItemSprite(AssetManager.itemChests, 0), AssetManager.chestOpenImage, 2, "Cavern Treasure Chest", 300);
        spawnItem(5260, 340, 24, 24, Item.TYPE_COIN, getItemSprite(AssetManager.itemCoins, 4), 2, "Cavern Coin", 75, 0);
        spawnItem(5600, 400, 26, 26, Item.TYPE_GEM, getItemSprite(AssetManager.itemGems, 0), 2, "Fire Crystal", 200, 0);

        // Stage 4 & 5 Enemies (No Hollow Knight, No Cinder Knight before stage 5!)
        enemies.add(new FireWraith(4350, 320));
        enemies.add(new BurningWolf(4680, groundY - 35));
        enemies.add(new FireWraith(5050, 300));
        enemies.add(new Ashwalker(5400, groundY - 40));
        enemies.add(new BurningWolf(5800, groundY - 35));

        // Checkpoint 4: Lava Cavern
        Checkpoint cp4 = new Checkpoint(4, "Stage 4: Lava Cavern", 5100, groundY - 85, 5100, groundY - 80, AssetManager.checkpointLantern);
        checkpoints.add(cp4);

        // Checkpoint 5: Molten Catacombs (Stage 5 Boundary at x = 6080)
        Checkpoint cp5 = new Checkpoint(5, "Stage 5: Molten Catacombs", 6080, groundY - 85, 6080, groundY - 80, AssetManager.checkpointObelisk);
        checkpoints.add(cp5);

        // =========================================================================
        // AFTER STAGE 5: Hollow Knight Domain & Last Stage (x: 6120 to 8160)
        // Hollow Knight ONLY comes after stage 5 and til stage end!
        // Cinder Knight comes at the last stage with BIG size like a final boss!
        // =========================================================================
        BufferedImage s7Tile = getTileSprite(AssetManager.stage7TerrainBlocks, 0);
        BufferedImage s7Plat = getTileSprite(AssetManager.stage7Platforms, 0);

        for (int x = 6120; x < WORLD_WIDTH; x += TILE_SIZE) {
            BufferedImage groundSprite = getTileSprite(AssetManager.stage7TerrainBlocks, (x / TILE_SIZE) % Math.max(1, AssetManager.stage7TerrainBlocks.size()));
            tiles.add(new Tile(x, groundY, TILE_SIZE, 120, true, false, 0, groundSprite, 7));
        }

        // Arena Battle Platforms
        tiles.add(new Tile(6250, 460, 200, 24, true, false, 1, s7Plat, 7));
        tiles.add(new Tile(6550, 390, 220, 24, true, false, 1, s7Plat, 7));
        tiles.add(new Tile(6900, 440, 200, 24, true, false, 1, s7Plat, 7));
        tiles.add(new Tile(7250, 400, 220, 24, true, false, 1, s7Plat, 7));
        tiles.add(new Tile(7600, 440, 240, 24, true, false, 1, s7Plat, 7));

        // Arena Collectibles & Powerups
        spawnItem(6300, 420, 26, 26, Item.TYPE_CHARM, AssetManager.itemCharm, 7, "Void Charm", 400, 0);
        spawnChest(6600, 350, 40, 36, getItemSprite(AssetManager.itemChests, 2), AssetManager.chestOpenImage, 7, "Grand Arena Chest", 500);
        spawnItem(6950, 400, 26, 26, Item.TYPE_GEM, getItemSprite(AssetManager.itemGems, 2), 7, "Abyss Gem", 350, 0);
        spawnItem(7300, 360, 26, 26, Item.TYPE_HEALTH, getItemSprite(AssetManager.itemHearts, 0), 7, "Grand Heart", 100, 4);

        // Checkpoint 6: Hollow Knight Domain (Immediately after Stage 5, at x = 6150)
        Checkpoint cp6 = new Checkpoint(6, "Stage 6: Hollow Knight Domain", 6150, groundY - 85, 6150, groundY - 80, AssetManager.checkpointGate);
        checkpoints.add(cp6);

        // Hollow Knight Sentinels (Only comes after stage 5 and til stage end!)
        enemies.add(new HollowKnight(6350, groundY - 80, false));
        enemies.add(new HollowKnight(6600, 360, false));
        enemies.add(new HollowKnight(6900, groundY - 80, false));

        // 1. The Hollow Knight Mini-Boss Arena at x = 7050
        miniBoss = new HollowKnight(7050, groundY - 80, true);
        enemies.add(miniBoss);

        // Boss Gate at x = 7300 (unlocked when Hollow Knight Mini-Boss is defeated)
        bossGateTile = new Tile(7300, groundY - 240, 48, 240, true, false, 2, null, 7);
        tiles.add(bossGateTile);

        // Checkpoint 7: Final Stage - The Burning Throne (x = 7350)
        Checkpoint cp7 = new Checkpoint(7, "Final Stage: The Burning Throne", 7350, groundY - 85, 7350, groundY - 80, AssetManager.checkpointGate);
        checkpoints.add(cp7);

        // Hollow Knight Royal Guard before the throne
        enemies.add(new HollowKnight(7480, 360, false));

        // 2. Main Final Boss: CINDER KNIGHT (COLOSSAL FINAL BOSS) at the Throne at x = 7700
        // Sized big like a final boss! (130x150 hitbox, 240px draw height, 500 HP, Epic Boss HUD)
        mainBoss = new CinderKnight(7700, groundY - 150, false, true);
        enemies.add(mainBoss);
    }

    private BufferedImage getTileSprite(List<BufferedImage> list, int idx) {
        if (list != null && !list.isEmpty()) {
            return list.get(Math.abs(idx) % list.size());
        }
        return null;
    }

    private BufferedImage getItemSprite(List<BufferedImage> list, int idx) {
        if (list != null && !list.isEmpty()) {
            return list.get(Math.abs(idx) % list.size());
        }
        return null;
    }

    private void spawnItem(float x, float y, int w, int h, int type, BufferedImage sprite, int stage, String name, int scoreVal, int heal) {
        items.add(new Item(x, y, w, h, type, sprite, stage, name, scoreVal, heal));
    }

    private void spawnChest(float x, float y, int w, int h, BufferedImage closed, BufferedImage open, int stage, String name, int scoreVal) {
        Item chest = new Item(x, y, w, h, Item.TYPE_CHEST, closed, stage, name, scoreVal, 3);
        chest.openSprite = open;
        items.add(chest);
    }

    public void update() {
        if (bannerTimer > 0) {
            bannerTimer--;
        }

        // Unlock Mini-Boss Gate when Hollow Knight mini-boss is defeated
        if (gateLocked && (miniBoss == null || !miniBoss.alive)) {
            gateLocked = false;
            tiles.remove(bossGateTile);
        }
    }

    public void drawBackground(Graphics2D g2, float camX) {
        if (AssetManager.assetsLoaded && AssetManager.bgImage != null) {
            BufferedImage bg = AssetManager.bgImage;

            // Zoom pro bg vertically to fill world height (720) and span lengthy world
            int bgH = WORLD_HEIGHT;
            int bgW = (int) (bg.getWidth() * ((float) bgH / bg.getHeight()));
            if (bgW < WORLD_WIDTH) {
                bgW = WORLD_WIDTH;
            }

            // Smooth parallax scrolling mapped precisely across the lengthy level
            float maxCamX = Math.max(1, WORLD_WIDTH - 1280);
            float maxBgScroll = Math.max(0, bgW - 1280);
            float scrollRatio = maxBgScroll / maxCamX;
            float bgX = -camX * scrollRatio;

            // Draw base panoramic artwork
            int startX = (int) bgX;
            while (startX > 0) startX -= bgW;
            for (int x = startX; x < 1280; x += bgW) {
                g2.drawImage(bg, x, 0, bgW, bgH, null);
            }

            // =========================================================================
            // Dynamic Background Changes where checkpoints exist:
            // Stage 1 & 2 (0-2040) -> Stage 3 (2040-4080) -> Stage 4 & 5 (4080-6080) -> Stage 6 & Final Stage (6080+)
            // =========================================================================
            float viewCenterX = camX + 640;

            if (viewCenterX < 2040) {
                // Stage 1 & 2: Ashen Forest - Twilight atmospheric tint
                g2.setColor(new Color(25, 30, 45, 35));
                g2.fillRect(0, 0, 1280, WORLD_HEIGHT);
            } else if (viewCenterX < 4080) {
                // Stage 3: Hidden Shrine (immediately after Checkpoint 1 & 2!) - Mystical sapphire & amethyst runic atmosphere
                float factor = Math.min(1.0f, (viewCenterX - 2040) / 300.0f);
                int alpha = (int) (50 * factor);
                g2.setColor(new Color(75, 40, 160, alpha));
                g2.fillRect(0, 0, 1280, WORLD_HEIGHT);

                // Temple ethereal moonlight band
                g2.setColor(new Color(120, 160, 255, 20));
                g2.fillRect(0, 0, 1280, 260);
            } else if (viewCenterX < 6080) {
                // Stage 4 & 5: Lava Cavern & Molten Catacombs - Fiery volcanic magma glow & heat ambiance
                float factor = Math.min(1.0f, (viewCenterX - 4080) / 300.0f);
                int alpha = (int) (55 * factor);
                g2.setColor(new Color(255, 60, 0, alpha));
                g2.fillRect(0, 0, 1280, WORLD_HEIGHT);

                // Lava heat haze horizontal line
                g2.setColor(new Color(255, 120, 20, 25));
                g2.fillRect(0, WORLD_HEIGHT - 180, 1280, 180);
            } else {
                // After Stage 5: Hollow Knight Domain & Final Stage - Abyssal onyx void darkness & soul mist
                float factor = Math.min(1.0f, (viewCenterX - 6080) / 300.0f);
                int alpha = (int) (70 * factor);
                g2.setColor(new Color(15, 10, 28, alpha));
                g2.fillRect(0, 0, 1280, WORLD_HEIGHT);

                // Arena spotlight beams
                g2.setColor(new Color(160, 100, 255, 22));
                g2.fillRect(200, 0, 180, WORLD_HEIGHT);
                g2.fillRect(800, 0, 220, WORLD_HEIGHT);
            }
        } else {
            // Procedural Sunset Sky gradient fallback
            for (int y = 0; y < WORLD_HEIGHT; y++) {
                float ratio = (float) y / WORLD_HEIGHT;
                int r = (int) (120 - ratio * 80);
                int g = (int) (30 - ratio * 20);
                int b = (int) (30 - ratio * 20);
                g2.setColor(new Color(r, g, b));
                g2.drawLine(0, y, 1280, y);
            }
        }
    }

    public void drawTiles(Graphics2D g2, float camX, float camY) {
        // 1. Draw tiles
        for (Tile tile : tiles) {
            tile.draw(g2, camX, camY, AssetManager.tilesImage);
        }

        // 2. Draw checkpoints
        for (Checkpoint cp : checkpoints) {
            cp.draw(g2, camX, camY);
        }

        // 3. Draw items
        for (Item item : items) {
            item.draw(g2, camX, camY);
        }
    }

    public void drawBanner(Graphics2D g2) {
        if (bannerTimer <= 0 || bannerText == null) return;

        float alpha = Math.min(1.0f, bannerTimer / 40.0f);
        int alphaInt = (int) (alpha * 240);

        int barW = 540;
        int barH = 50;
        int barX = (1280 - barW) / 2;
        int barY = 90;

        // Banner backing
        g2.setColor(new Color(15, 15, 25, alphaInt));
        g2.fillRect(barX, barY, barW, barH);
        g2.setColor(new Color(80, 220, 255, alphaInt));
        g2.drawRect(barX, barY, barW, barH);
        g2.drawRect(barX + 2, barY + 2, barW - 4, barH - 4);

        // Banner text
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.setColor(new Color(255, 255, 255, alphaInt));
        int strW = g2.getFontMetrics().stringWidth(bannerText);
        g2.drawString(bannerText, barX + (barW - strW) / 2, barY + 32);
    }
}
