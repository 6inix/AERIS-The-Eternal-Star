import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ProcessAssets {

    public static void main(String[] args) {
        // Target assets directory
        File assetsDir = new File("assets");
        if (!assetsDir.exists()) {
            File altAssetsDir = new File("JAVA/assets");
            if (altAssetsDir.exists()) {
                assetsDir = altAssetsDir;
            } else {
                assetsDir = new File("c:\\Users\\Admin\\Desktop\\JAVA\\assets");
            }
        }
        if (!assetsDir.exists()) {
            assetsDir.mkdirs();
        }

        // Source file paths as defined in original script
        String tilesSrc = "C:\\Users\\Admin\\.gemini\\antigravity\\brain\\ae86cd92-6981-40a6-a536-0df4ac227972\\.user_uploaded\\media_1789663122903.png";
        String bgSrc = "C:\\Users\\Admin\\.gemini\\antigravity\\brain\\ae86cd92-6981-40a6-a536-0df4ac227972\\.user_uploaded\\media_1789663122948.png";
        String heroSrc = "C:\\Users\\Admin\\.gemini\\antigravity\\brain\\ae86cd92-6981-40a6-a536-0df4ac227972\\.user_uploaded\\media_1789663137396.jpg";
        String enemiesSrc = "C:\\Users\\Admin\\.gemini\\antigravity\\brain\\ae86cd92-6981-40a6-a536-0df4ac227972\\.user_uploaded\\media_1789663137400.jpg";

        // Save as PNG in assets directory
        saveAsPng(tilesSrc, new File(assetsDir, "tiles.png"));
        saveAsPng(bgSrc, new File(assetsDir, "background.png"));
        saveAsPng(heroSrc, new File(assetsDir, "hero.png"));
        saveAsPng(enemiesSrc, new File(assetsDir, "enemies.png"));

        System.out.println("Assets copied and converted successfully.");
    }

    private static void saveAsPng(String srcPath, File destFile) {
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) {
            System.out.println("Source file does not exist, skipping: " + srcPath);
            return;
        }

        try {
            BufferedImage image = ImageIO.read(srcFile);
            if (image == null) {
                System.err.println("Could not decode image from: " + srcPath);
                return;
            }
            ImageIO.write(image, "PNG", destFile);
            System.out.println("Processed and saved: " + destFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error processing " + srcPath + ": " + e.getMessage());
        }
    }
}
