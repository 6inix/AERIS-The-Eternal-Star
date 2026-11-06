package com.ashenfall.game.engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class AssetManager {
    public static BufferedImage tilesImage;
    public static BufferedImage bgImage;
    public static BufferedImage heroImage;
    public static BufferedImage enemiesImage;

    public static BufferedImage[] heroRunFrames = new BufferedImage[6];
    public static BufferedImage[] heroIdleFrames = new BufferedImage[4];
    public static BufferedImage heroJumpFrame;
    public static BufferedImage heroCrouchFrame;
    public static BufferedImage heroAttackFrame;

    public static BufferedImage[] ashwalkerFrames = new BufferedImage[5];
    public static BufferedImage[] ashwalkerIdleFrames = new BufferedImage[5];
    public static BufferedImage[] ashwalkerRunFrames = new BufferedImage[8];
    public static BufferedImage[] ashwalkerAttackFrames = new BufferedImage[5];
    public static BufferedImage[] ashwalkerJumpFrames = new BufferedImage[5];
    public static BufferedImage[] ashwalkerDieFrames = new BufferedImage[3];

    public static BufferedImage[] wolfIdleFrames = new BufferedImage[2];
    public static BufferedImage[] wolfRunFrames = new BufferedImage[4];
    public static BufferedImage[] wolfAttackFrames = new BufferedImage[3];
    public static BufferedImage wolfHowlFrame;

    public static BufferedImage[] knightIdleFrames = new BufferedImage[1];
    public static BufferedImage[] knightWalkFrames = new BufferedImage[8];
    public static BufferedImage[] knightRunFrames = new BufferedImage[8];
    public static BufferedImage[] knightJumpFrames = new BufferedImage[4];
    public static BufferedImage[] knightAttackFrames = new BufferedImage[4];
    public static BufferedImage[] knightShieldRaiseFrames = new BufferedImage[3];
    public static BufferedImage[] knightShieldIdleFrames = new BufferedImage[1];
    public static BufferedImage[] knightShieldBlockFrames = new BufferedImage[4];
    public static BufferedImage[] knightShieldBashFrames = new BufferedImage[4];
    public static BufferedImage[] knightShieldRunFrames = new BufferedImage[8];
    public static BufferedImage[] knightShieldLowerFrames = new BufferedImage[3];
    public static BufferedImage[] knightHurtFrames = new BufferedImage[1];
    public static BufferedImage[] knightDeathFrames = new BufferedImage[2];

    public static BufferedImage[] blueHeroIdleFrames = new BufferedImage[1];
    public static BufferedImage[] blueHeroRunFrames = new BufferedImage[9];
    public static BufferedImage[] blueHeroJumpFrames = new BufferedImage[15];
    public static BufferedImage[] blueHeroCrouchFrames = new BufferedImage[8];

    public static BufferedImage[] fireWraithRunFrames = new BufferedImage[7];
    public static BufferedImage[] fireWraithAttackFrames = new BufferedImage[6];
    public static BufferedImage[] fireWraithDieFrames = new BufferedImage[5];

    public static BufferedImage[] hollowKnightIdleFrames = new BufferedImage[1];
    public static BufferedImage[] hollowKnightRunFrames = new BufferedImage[6];
    public static BufferedImage[] hollowKnightAttackFrames = new BufferedImage[5];
    public static BufferedImage[] hollowKnightJumpFrames = new BufferedImage[5];
    public static BufferedImage[] hollowKnightHurtFrames = new BufferedImage[4];
    public static BufferedImage[] hollowKnightSkillChargeFrames = new BufferedImage[3];
    public static BufferedImage[] hollowKnightDieFrames = new BufferedImage[4];
    public static BufferedImage[] hollowKnightDeathExplosionFrames = new BufferedImage[5];

    // UI Components
    public static BufferedImage uiPlay;
    public static BufferedImage uiPause;
    public static BufferedImage uiHome;
    public static BufferedImage uiMenu;
    public static BufferedImage uiRestart;
    public static BufferedImage uiClose;
    public static BufferedImage uiMusic;

    // Lava Pit, Platform & Ground Textures
    public static BufferedImage lavaImage;
    public static BufferedImage platformImage;
    public static BufferedImage groundImage;

    // Gothic Pause Menu Buttons
    public static BufferedImage gothicResumeNormal, gothicResumeSelected;
    public static BufferedImage gothicOptionsNormal, gothicOptionsSelected;
    public static BufferedImage gothicControlsNormal, gothicControlsSelected;
    public static BufferedImage gothicSaveNormal, gothicSaveSelected;
    public static BufferedImage gothicExitNormal, gothicExitSelected;

    // Stage 1, 2, 4, 7 Tiles & Props
    public static java.util.List<BufferedImage> stage1Tiles = new java.util.ArrayList<>();
    public static java.util.List<BufferedImage> stage2Tiles = new java.util.ArrayList<>();
    public static java.util.List<BufferedImage> stage4PlatformTiles = new java.util.ArrayList<>();
    public static java.util.List<BufferedImage> stage4Pillars = new java.util.ArrayList<>();
    public static java.util.List<BufferedImage> stage4Stairs = new java.util.ArrayList<>();
    public static java.util.List<BufferedImage> stage4Decorations = new java.util.ArrayList<>();
    public static java.util.List<BufferedImage> stage4Obstacles = new java.util.ArrayList<>();
    public static BufferedImage stage4SpikeGate;
    public static java.util.List<BufferedImage> stage7TerrainBlocks = new java.util.ArrayList<>();
    public static java.util.List<BufferedImage> stage7Platforms = new java.util.ArrayList<>();
    public static java.util.List<BufferedImage> stage7Pillars = new java.util.ArrayList<>();
    public static java.util.List<BufferedImage> stage7Hazards = new java.util.ArrayList<>();
    public static java.util.List<BufferedImage> stage7Decorations = new java.util.ArrayList<>();
    public static java.util.List<BufferedImage> stage7Banners = new java.util.ArrayList<>();

    // Items & Collectibles
    public static java.util.List<BufferedImage> itemCoins = new java.util.ArrayList<>();
    public static java.util.List<BufferedImage> itemHearts = new java.util.ArrayList<>();
    public static java.util.List<BufferedImage> itemGems = new java.util.ArrayList<>();
    public static java.util.List<BufferedImage> itemChests = new java.util.ArrayList<>();
    public static BufferedImage chestOpenImage;
    public static BufferedImage itemKey;
    public static BufferedImage itemScroll;
    public static BufferedImage itemCharm;

    // Checkpoints
    public static BufferedImage checkpointObelisk;
    public static BufferedImage checkpointLantern;
    public static BufferedImage checkpointGate;

    // Specific Renamed Stage 1 Tiles & Items
    public static BufferedImage stage1Block;
    public static BufferedImage stage1Platform;
    public static BufferedImage stage1Boulder;
    public static BufferedImage stage1Coin;
    public static BufferedImage stage1Heart;

    // Specific Renamed Stage 2 Tiles & Hazards
    public static BufferedImage stage2Platform;
    public static BufferedImage stage2Spike;
    public static BufferedImage stage2Ground1;
    public static BufferedImage stage2Ground2;
    public static BufferedImage stage2Rock1;
    public static BufferedImage stage2Rock2;

    public static boolean assetsLoaded = false;

    public static void loadAssets() {
        try {
            File tilesFile = new File("assets/tiles.png");
            File bgFile = new File("assets/pro_bg.png");
            if (!bgFile.exists()) bgFile = new File("assets/background.png");
            if (!bgFile.exists()) bgFile = new File("C:/Users/AMAL/Desktop/pro bg.png");
            File heroFile = new File("assets/hero.png");
            File enemiesFile = new File("assets/enemies.png");
            File lavaFile = new File("assets/lava.jpg");
            if (!lavaFile.exists()) lavaFile = new File("assets/lava.png");
            if (!lavaFile.exists()) lavaFile = new File("C:/Users/Admin/Desktop/LAVA.jpg");

            File platformFile = new File("assets/platform.png");
            if (!platformFile.exists()) platformFile = new File("C:/Users/Admin/.gemini/antigravity/brain/ae86cd92-6981-40a6-a536-0df4ac227972/.user_uploaded/media_1791140853948.png");

            File groundFile = new File("assets/ground.png");
            if (!groundFile.exists()) groundFile = new File("C:/Users/Admin/.gemini/antigravity/brain/ae86cd92-6981-40a6-a536-0df4ac227972/.user_uploaded/media_1791141227976.png");

            if (tilesFile.exists()) tilesImage = ImageIO.read(tilesFile);
            if (bgFile.exists()) bgImage = ImageIO.read(bgFile);
            if (heroFile.exists()) heroImage = ImageIO.read(heroFile);
            if (enemiesFile.exists()) enemiesImage = ImageIO.read(enemiesFile);
            if (lavaFile.exists()) lavaImage = ImageIO.read(lavaFile);
            if (platformFile.exists()) platformImage = ImageIO.read(platformFile);
            if (groundFile.exists()) groundImage = ImageIO.read(groundFile);

            if (heroImage != null) {
                // Slice hero frames from hero sheet (1024x576)
                int frameW = heroImage.getWidth() / 6;
                int frameH = heroImage.getHeight() / 2;

                for (int i = 0; i < 6; i++) {
                    heroRunFrames[i] = heroImage.getSubimage(i * frameW, 0, frameW, frameH);
                }
                for (int i = 0; i < 4; i++) {
                    heroIdleFrames[i] = heroImage.getSubimage(i * frameW, frameH, frameW, frameH);
                }
                heroJumpFrame = heroRunFrames[2];
                heroCrouchFrame = heroIdleFrames[0];
                heroAttackFrame = heroRunFrames[4];
            }

            // Load Ashwalker Animation Frames
            loadAshwalkerFolderFrames("ashwalker idle", ashwalkerIdleFrames);
            loadAshwalkerFolderFrames("ashwalker run", ashwalkerRunFrames);
            loadAshwalkerFolderFrames("ash walker attack", ashwalkerAttackFrames);
            loadAshwalkerFolderFrames("ash walker jump", ashwalkerJumpFrames);
            loadAshwalkerFolderFrames("ash walker die", ashwalkerDieFrames);

            // Backwards compatibility for ashwalkerFrames
            for (int i = 0; i < 5; i++) {
                if (ashwalkerIdleFrames[i] != null) {
                    ashwalkerFrames[i] = ashwalkerIdleFrames[i];
                }
            }
            if (ashwalkerFrames[0] == null) {
                for (int i = 1; i <= 5; i++) {
                    File f = new File("assets/ashwalker/F" + i + ".png");
                    if (!f.exists()) f = new File("JAVA/assets/ashwalker/F" + i + ".png");
                    if (!f.exists()) f = new File("C:/Users/Admin/Desktop/ashwalkeridle/F" + i + ".png");
                    if (f.exists()) ashwalkerFrames[i - 1] = ImageIO.read(f);
                }
            }

            // Load Burning Wolf Frames
            loadWolfFolderFrames("burinig wolf run", wolfRunFrames);
            if (wolfRunFrames[0] == null) {
                // Fallback to legacy RUN folder
                String[] runNames = {"burning_wolf_07.png", "burning_wolf_08.png", "burning_wolf_09.png", "burning_wolf_10.png"};
                for (int i = 0; i < runNames.length; i++) {
                    File f = new File("assets/burning_wolf/RUN/" + runNames[i]);
                    if (!f.exists()) f = new File("JAVA/assets/burning_wolf/RUN/" + runNames[i]);
                    if (!f.exists()) f = new File("C:/Users/Admin/Desktop/burning_wolf_frames/RUN/" + runNames[i]);
                    if (f.exists()) wolfRunFrames[i] = ImageIO.read(f);
                }
            }

            loadWolfFolderFrames("attack fire wolf", wolfAttackFrames);
            if (wolfAttackFrames[0] == null) {
                // Fallback to legacy ATTACK folder
                String[] attackNames = {"burning_wolf_16.png", "burning_wolf_17.png", "burning_wolf_18.png"};
                for (int i = 0; i < attackNames.length; i++) {
                    File f = new File("assets/burning_wolf/ATTACK/" + attackNames[i]);
                    if (!f.exists()) f = new File("JAVA/assets/burning_wolf/ATTACK/" + attackNames[i]);
                    if (!f.exists()) f = new File("C:/Users/Admin/Desktop/burning_wolf_frames/ATTACK/" + attackNames[i]);
                    if (f.exists()) wolfAttackFrames[i] = ImageIO.read(f);
                }
            }

            // Load Howl Frame
            File howlFile = new File("assets/burning_wolf/howling/howling_cropped.png");
            if (!howlFile.exists()) howlFile = new File("assets/burning_wolf/howling/howling.png");
            if (!howlFile.exists()) howlFile = new File("JAVA/assets/burning_wolf/howling/howling_cropped.png");
            if (!howlFile.exists()) howlFile = new File("JAVA/assets/burning_wolf/howling/howling.png");
            if (!howlFile.exists()) howlFile = new File("C:/Users/AMAL/Desktop/burning wolf/howling/howling.png");
            if (howlFile.exists()) {
                try {
                    wolfHowlFrame = ImageIO.read(howlFile);
                } catch (Exception ignored) {}
            }

            // Populate Idle frames
            if (wolfRunFrames[0] != null) wolfIdleFrames[0] = wolfRunFrames[0];
            if (wolfHowlFrame != null) {
                wolfIdleFrames[1] = wolfHowlFrame;
            } else if (wolfRunFrames[0] != null) {
                wolfIdleFrames[1] = wolfRunFrames[0];
            }

            // Load Cinder Knight Frames
            loadFolderFrames("Idle", knightIdleFrames);
            loadFolderFrames("Walk", knightWalkFrames);
            loadFolderFrames("Run", knightRunFrames);
            loadFolderFrames("Jump", knightJumpFrames);
            loadFolderFrames("Attack_Sword", knightAttackFrames);
            loadFolderFrames("Shield_Raise", knightShieldRaiseFrames);
            loadFolderFrames("Shield_Idle", knightShieldIdleFrames);
            loadFolderFrames("Shield_Block", knightShieldBlockFrames);
            loadFolderFrames("Shield_Bash", knightShieldBashFrames);
            loadFolderFrames("Shield_Run", knightShieldRunFrames);
            loadFolderFrames("Shield_Lower", knightShieldLowerFrames);
            loadFolderFrames("Hurt", knightHurtFrames);
            loadFolderFrames("Death", knightDeathFrames);

            // Load Blue Hero Frames
            loadHeroFolderFrames("Idle", blueHeroIdleFrames);
            loadHeroFolderFrames("Run", blueHeroRunFrames);
            loadHeroFolderFrames("Double_Jump", blueHeroJumpFrames);
            loadHeroFolderFrames("Shield_On_Head_Crouch", blueHeroCrouchFrames);

            // Load Fire Wraith Frames
            loadFireWraithFolderFrames("run", fireWraithRunFrames);
            loadFireWraithFolderFrames("attack", fireWraithAttackFrames);
            loadFireWraithFolderFrames("die", fireWraithDieFrames);

            // Load The Hollow Knight Frames
            loadHollowKnightFolderFrames("idle", hollowKnightIdleFrames);
            loadHollowKnightFolderFrames("run", hollowKnightRunFrames);
            loadHollowKnightFolderFrames("attack", hollowKnightAttackFrames);
            loadHollowKnightFolderFrames("jump", hollowKnightJumpFrames);
            loadHollowKnightFolderFrames("hurt", hollowKnightHurtFrames);
            loadHollowKnightFolderFrames("skill charge", hollowKnightSkillChargeFrames);
            loadHollowKnightFolderFrames("die", hollowKnightDieFrames);
            loadHollowKnightFolderFrames("death explossion", hollowKnightDeathExplosionFrames);

            // Load UI Component Images
            uiPlay = loadUIImage("play.png");
            uiPause = loadUIImage("pause.png");
            uiHome = loadUIImage("home.png");
            uiMenu = loadUIImage("menu.png");
            uiRestart = loadUIImage("restart.png");
            uiClose = loadUIImage("close.png");
            uiMusic = loadUIImage("music.png");

            // Load Gothic Menu Buttons
            gothicResumeNormal = loadGothicImage("resume_normal.png");
            gothicResumeSelected = loadGothicImage("resume_selected.png");
            gothicOptionsNormal = loadGothicImage("options_normal.png");
            gothicOptionsSelected = loadGothicImage("options_selected.png");
            gothicControlsNormal = loadGothicImage("controls_normal.png");
            gothicControlsSelected = loadGothicImage("controls_selected.png");
            gothicSaveNormal = loadGothicImage("save_game_normal.png");
            gothicSaveSelected = loadGothicImage("save_game_selected.png");
            gothicExitNormal = loadGothicImage("exit_to_main_menu_normal.png");
            gothicExitSelected = loadGothicImage("exit_to_main_menu_selected.png");

            // Load Stage 1, 2, 4, 7 Tiles, Checkpoints & Items
            loadStageAssets();

            assetsLoaded = true;
            System.out.println("AssetManager loaded game artwork successfully.");
        } catch (Exception e) {
            System.err.println("Error loading asset images: " + e.getMessage());
            assetsLoaded = false;
        }
    }

    private static void loadFolderFrames(String folderName, BufferedImage[] targetArray) {
        String[] variations = {
            folderName,
            folderName.toLowerCase(),
            folderName.replace("_", " "),
            folderName.replace(" ", "_"),
            folderName.replace("Attack_Sword", "attack"),
            folderName.replace("Attack_Sword", "Attack"),
            folderName.replace("Shield_Raise", "shield riser"),
            folderName.replace("Shield_Block", "shild block"),
            folderName.replace("Shield_Run", "cinder knight run"),
            folderName.replace("Walk", "cinder knight run"),
            folderName.replace("Run", "cinder knight run"),
            folderName.replace("Shield_Bash", "shield bash"),
            folderName.replace("Shield_Lower", "shield lower"),
            folderName.replace("Shield_Idle", "shield idle"),
            folderName.replace("Death", "death"),
            folderName.replace("Hurt", "hurt"),
            folderName.replace("Jump", "jump"),
            folderName.replace("Idle", "idle")
        };

        String[] baseDirs = {
            "assets/cinder_knight/",
            "JAVA/assets/cinder_knight/",
            "C:/Users/AMAL/Desktop/cinder knight/",
            "C:/Users/Admin/Desktop/Knight_Sprite_Frames/Knight_Sprite_Frames/"
        };

        File dir = null;
        for (String base : baseDirs) {
            for (String var : variations) {
                File f = new File(base + var);
                if (f.exists() && f.isDirectory()) {
                    dir = f;
                    break;
                }
            }
            if (dir != null) break;
        }

        if (dir != null && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".png"));
            if (files != null && files.length > 0) {
                java.util.Arrays.sort(files, (a, b) -> {
                    try {
                        int numA = Integer.parseInt(a.getName().replaceAll("\\D+", ""));
                        int numB = Integer.parseInt(b.getName().replaceAll("\\D+", ""));
                        return Integer.compare(numA, numB);
                    } catch (Exception e) {
                        return a.getName().compareTo(b.getName());
                    }
                });

                for (int i = 0; i < Math.min(files.length, targetArray.length); i++) {
                    try {
                        targetArray[i] = ImageIO.read(files[i]);
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    private static void loadHeroFolderFrames(String folderName, BufferedImage[] targetArray) {
        File dir = new File("assets/blue_hero/" + folderName);
        if (!dir.exists()) {
            dir = new File("C:/Users/Admin/Desktop/Blue_Hero_Animations/" + folderName);
        }
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".png"));
            if (files != null) {
                java.util.Arrays.sort(files, (a, b) -> a.getName().compareTo(b.getName()));
                for (int i = 0; i < Math.min(files.length, targetArray.length); i++) {
                    try {
                        targetArray[i] = ImageIO.read(files[i]);
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    private static void loadAshwalkerFolderFrames(String folderName, BufferedImage[] targetArray) {
        String[] folderVariations = {
            folderName,
            folderName.replace(" ", "_"),
            folderName.replace("_", " "),
            folderName.replace("ashwalker", "ash walker"),
            folderName.replace("ash walker", "ashwalker")
        };

        String[] basePaths = {
            "assets/ashwalker/",
            "JAVA/assets/ashwalker/",
            "C:/Users/AMAL/Desktop/ashwalker/",
            "C:/Users/Admin/Desktop/ashwalker/"
        };

        File dir = null;
        for (String base : basePaths) {
            for (String var : folderVariations) {
                File f = new File(base + var);
                if (f.exists() && f.isDirectory()) {
                    dir = f;
                    break;
                }
            }
            if (dir != null) break;
        }

        if (dir != null && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> {
                String lower = name.toLowerCase();
                return lower.endsWith(".png") && !lower.contains("wolf") && !lower.contains("html");
            });

            if (files != null && files.length > 0) {
                java.util.Arrays.sort(files, (a, b) -> {
                    try {
                        int numA = Integer.parseInt(a.getName().replaceAll("\\D+", ""));
                        int numB = Integer.parseInt(b.getName().replaceAll("\\D+", ""));
                        return Integer.compare(numA, numB);
                    } catch (Exception e) {
                        return a.getName().compareTo(b.getName());
                    }
                });

                for (int i = 0; i < Math.min(files.length, targetArray.length); i++) {
                    try {
                        targetArray[i] = ImageIO.read(files[i]);
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    private static void loadFireWraithFolderFrames(String folderName, BufferedImage[] targetArray) {
        String[] possiblePaths = {
            "assets/fire_wrath/" + folderName,
            "assets/fire wrath/" + folderName,
            "JAVA/assets/fire_wrath/" + folderName,
            "JAVA/assets/fire wrath/" + folderName,
            "C:/Users/AMAL/Desktop/fire wrath/" + folderName,
            "C:/Users/Admin/Desktop/fire wrath/" + folderName
        };

        File dir = null;
        for (String p : possiblePaths) {
            File f = new File(p);
            if (f.exists() && f.isDirectory()) {
                dir = f;
                break;
            }
        }

        if (dir != null && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".png"));
            if (files != null) {
                java.util.Arrays.sort(files, (a, b) -> {
                    try {
                        int numA = Integer.parseInt(a.getName().replaceAll("\\D+", ""));
                        int numB = Integer.parseInt(b.getName().replaceAll("\\D+", ""));
                        return Integer.compare(numA, numB);
                    } catch (Exception e) {
                        return a.getName().compareTo(b.getName());
                    }
                });
                for (int i = 0; i < Math.min(files.length, targetArray.length); i++) {
                    try {
                        targetArray[i] = ImageIO.read(files[i]);
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    private static void loadWolfFolderFrames(String folderName, BufferedImage[] targetArray) {
        String[] folderVariations = {
            folderName,
            folderName.replace(" ", "_"),
            folderName.replace("_", " "),
            folderName.replace("burinig", "burning"),
            folderName.replace("burning", "burinig")
        };

        String[] basePaths = {
            "assets/burning_wolf/",
            "JAVA/assets/burning_wolf/",
            "C:/Users/AMAL/Desktop/burning wolf/",
            "C:/Users/Admin/Desktop/burning_wolf_frames/"
        };

        File dir = null;
        for (String base : basePaths) {
            for (String var : folderVariations) {
                File f = new File(base + var);
                if (f.exists() && f.isDirectory()) {
                    dir = f;
                    break;
                }
            }
            if (dir != null) break;
        }

        if (dir != null && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> {
                String lower = name.toLowerCase();
                return lower.endsWith(".png") && !lower.contains("exact_crop");
            });

            if (files != null && files.length > 0) {
                java.util.Arrays.sort(files, (a, b) -> {
                    try {
                        int numA = Integer.parseInt(a.getName().replaceAll("\\D+", ""));
                        int numB = Integer.parseInt(b.getName().replaceAll("\\D+", ""));
                        return Integer.compare(numA, numB);
                    } catch (Exception e) {
                        return a.getName().compareTo(b.getName());
                    }
                });

                for (int i = 0; i < Math.min(files.length, targetArray.length); i++) {
                    try {
                        targetArray[i] = ImageIO.read(files[i]);
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    private static void loadHollowKnightFolderFrames(String folderName, BufferedImage[] targetArray) {
        String[] folderVariations = {
            folderName,
            folderName.toLowerCase(),
            folderName.replace(" ", "_"),
            folderName.replace("_", " "),
            folderName.replace("death explosion", "death explossion"),
            folderName.replace("death_explosion", "death explossion"),
            // Fallback aliases for cinder knight folder
            folderName.equals("idle") ? "shield idle" : "",
            folderName.equals("idle") ? "Shield_Idle" : "",
            folderName.equals("run") ? "cinder knight run" : "",
            folderName.equals("run") ? "Shield_Run" : "",
            folderName.equals("run") ? "Run" : "",
            folderName.equals("attack") ? "Attack" : "",
            folderName.equals("attack") ? "Attack_Sword" : "",
            folderName.equals("jump") ? "Jump" : "",
            folderName.equals("hurt") ? "Hurt" : "",
            folderName.equals("die") ? "death" : "",
            folderName.equals("die") ? "Death" : "",
            folderName.equals("death explossion") ? "death" : "",
            folderName.equals("skill charge") ? "shield bash" : "",
            folderName.equals("skill charge") ? "shield riser" : ""
        };

        String[] basePaths = {
            "assets/hollow_knight/",
            "JAVA/assets/hollow_knight/",
            "C:/Users/AMAL/Desktop/the hollow knight/",
            "C:/Users/AMAL/Desktop/hollow knight/",
            "assets/cinder_knight/",
            "JAVA/assets/cinder_knight/",
            "C:/Users/AMAL/Desktop/cinder knight/"
        };

        File dir = null;
        for (String base : basePaths) {
            for (String var : folderVariations) {
                File f = new File(base + var);
                if (f.exists() && f.isDirectory()) {
                    dir = f;
                    break;
                }
            }
            if (dir != null) break;
        }

        if (dir != null && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".png"));

            if (files != null && files.length > 0) {
                java.util.Arrays.sort(files, (a, b) -> {
                    try {
                        int numA = Integer.parseInt(a.getName().replaceAll("\\D+", ""));
                        int numB = Integer.parseInt(b.getName().replaceAll("\\D+", ""));
                        return Integer.compare(numA, numB);
                    } catch (Exception e) {
                        return a.getName().compareTo(b.getName());
                    }
                });

                for (int i = 0; i < Math.min(files.length, targetArray.length); i++) {
                    try {
                        targetArray[i] = ImageIO.read(files[i]);
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    private static BufferedImage loadUIImage(String filename) {
        File file = new File("assets/ui/" + filename);
        if (!file.exists()) {
            file = new File("C:/Users/Admin/Desktop/ui components/" + filename);
        }
        if (file.exists()) {
            try {
                return ImageIO.read(file);
            } catch (Exception ignored) {}
        }
        return null;
    }

    private static BufferedImage loadGothicImage(String filename) {
        File file = new File("assets/gothic_menu/" + filename);
        if (!file.exists()) {
            file = new File("C:/Users/Admin/Desktop/gothic_menu_buttons/" + filename);
        }
        if (file.exists()) {
            try {
                return ImageIO.read(file);
            } catch (Exception ignored) {}
        }
        return null;
    }

    private static void loadStageAssets() {
        stage1Tiles.clear();
        stage2Tiles.clear();
        stage4PlatformTiles.clear();
        stage4Pillars.clear();
        stage4Stairs.clear();
        stage4Decorations.clear();
        stage7TerrainBlocks.clear();
        stage7Platforms.clear();
        stage7Pillars.clear();
        stage7Hazards.clear();
        stage7Decorations.clear();
        stage7Banners.clear();
        itemCoins.clear();
        itemHearts.clear();
        itemGems.clear();
        itemChests.clear();

        // 1. Stage 1: Ashen Forest Tiles
        loadImagesFromStageDir("stage1_ashen_forest_tiles", stage1Tiles);
        stage1Block = loadSingleStageImage("stage1_ashen_forest_tiles/block.png");
        stage1Platform = loadSingleStageImage("stage1_ashen_forest_tiles/platform.png");
        stage1Boulder = loadSingleStageImage("stage1_ashen_forest_tiles/boulder.png");
        stage1Coin = loadSingleStageImage("stage1_ashen_forest_tiles/coin.png");
        stage1Heart = loadSingleStageImage("stage1_ashen_forest_tiles/heart.png");
        if (stage1Coin != null) itemCoins.add(0, stage1Coin);
        if (stage1Heart != null) itemHearts.add(0, stage1Heart);

        // 2. Stage 2: Lava Cavern Tiles
        loadImagesFromStageDir("stage2_lava_cavern_tiles", stage2Tiles);
        stage2Platform = loadSingleStageImage("stage2_lava_cavern_tiles/platform.png");
        stage2Spike = loadSingleStageImage("stage2_lava_cavern_tiles/spike.png");
        stage2Ground1 = loadSingleStageImage("stage2_lava_cavern_tiles/stage2_lava_cavern_sprite_026.png");
        stage2Ground2 = loadSingleStageImage("stage2_lava_cavern_tiles/stage2_lava_cavern_sprite_027.png");
        stage2Rock1 = loadSingleStageImage("stage2_lava_cavern_tiles/stage2_lava_cavern_sprite_061.png");
        stage2Rock2 = loadSingleStageImage("stage2_lava_cavern_tiles/stage2_lava_cavern_sprite_062.png");

        // 3. Stage 4: Hidden Shrine Assets
        loadImagesFromStageDir("stage4_hidden_shrine_separated_assets/platform_tiles", stage4PlatformTiles);
        loadImagesFromStageDir("stage4_hidden_shrine_separated_assets/pillars_columns", stage4Pillars);
        loadImagesFromStageDir("stage4_hidden_shrine_separated_assets/steps_stairs", stage4Stairs);
        loadImagesFromStageDir("stage4_hidden_shrine_separated_assets/background_decoration", stage4Decorations);
        loadImagesFromStageDir("stage4_hidden_shrine_separated_assets/obstacles", stage4Obstacles);
        stage4SpikeGate = loadSingleStageImage("stage4_hidden_shrine_separated_assets/obstacles/spike_gate.png");

        // Stage 4 Items
        loadImagesFromStageDir("stage4_hidden_shrine_separated_assets/coins", itemCoins);
        loadImagesFromStageDir("stage4_hidden_shrine_separated_assets/health_energy", itemHearts);
        loadImagesFromStageDir("stage4_hidden_shrine_separated_assets/bonus_items", itemChests);
        itemKey = loadSingleStageImage("stage4_hidden_shrine_separated_assets/key_special_items/key.png");
        itemScroll = loadSingleStageImage("stage4_hidden_shrine_separated_assets/key_special_items/scroll.png");
        BufferedImage blueCrystal = loadSingleStageImage("stage4_hidden_shrine_separated_assets/key_special_items/blue_crystal.png");
        if (blueCrystal != null) itemGems.add(blueCrystal);
        chestOpenImage = loadSingleStageImage("stage4_hidden_shrine_separated_assets/bonus_items/chest_open.png");

        // 4. Stage 7: Hollow Knight Arena Assets
        loadImagesFromStageDir("stage7_hollow_knight_arena_transparent_sprites/terrain_blocks", stage7TerrainBlocks);
        loadImagesFromStageDir("stage7_hollow_knight_arena_transparent_sprites/platforms", stage7Platforms);
        loadImagesFromStageDir("stage7_hollow_knight_arena_transparent_sprites/pillars", stage7Pillars);
        loadImagesFromStageDir("stage7_hollow_knight_arena_transparent_sprites/hazards", stage7Hazards);
        loadImagesFromStageDir("stage7_hollow_knight_arena_transparent_sprites/decorations", stage7Decorations);
        loadImagesFromStageDir("stage7_hollow_knight_arena_transparent_sprites/banners_and_props", stage7Banners);

        // Stage 7 Items & Collectibles
        loadImagesFromStageDir("stage7_hollow_knight_arena_transparent_sprites/collectibles", itemGems);
        itemCharm = loadSingleStageImage("stage7_hollow_knight_arena_transparent_sprites/collectibles/charm_01.png");
        BufferedImage blueGem = loadSingleStageImage("stage7_hollow_knight_arena_transparent_sprites/collectibles/blue_gem_01.png");
        if (blueGem != null) itemGems.add(blueGem);

        // Checkpoints
        checkpointObelisk = loadSingleStageImage("stage7_hollow_knight_arena_transparent_sprites/decorations/obelisk_01.png");
        if (checkpointObelisk == null) checkpointObelisk = loadSingleStageImage("stage7_hollow_knight_arena_transparent_sprites/decorations/obelisk_02.png");
        checkpointLantern = loadSingleStageImage("stage7_hollow_knight_arena_transparent_sprites/banners_and_props/lantern_01.png");
        checkpointGate = loadSingleStageImage("stage7_hollow_knight_arena_transparent_sprites/decorations/arena_gate_01.png");
    }

    private static void loadImagesFromStageDir(String relPath, java.util.List<BufferedImage> outList) {
        String[] bases = {
            "assets/stages/" + relPath,
            "JAVA/assets/stages/" + relPath,
            "C:/Users/AMAL/Desktop/" + relPath
        };
        File dir = null;
        for (String b : bases) {
            File f = new File(b);
            if (f.exists() && f.isDirectory()) {
                dir = f;
                break;
            }
        }
        if (dir != null) {
            File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".png"));
            if (files != null && files.length > 0) {
                java.util.Arrays.sort(files, (a, b) -> a.getName().compareTo(b.getName()));
                for (File f : files) {
                    try {
                        BufferedImage img = ImageIO.read(f);
                        if (img != null) outList.add(img);
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    private static BufferedImage loadSingleStageImage(String relPath) {
        String[] bases = {
            "assets/stages/" + relPath,
            "JAVA/assets/stages/" + relPath,
            "C:/Users/AMAL/Desktop/" + relPath
        };
        for (String b : bases) {
            File f = new File(b);
            if (f.exists() && f.isFile()) {
                try {
                    return ImageIO.read(f);
                } catch (Exception ignored) {}
            }
        }
        return null;
    }
}
