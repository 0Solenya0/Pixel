package db;

import db.exception.ConnectionException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Random;

public class ImageDB {
    private static final Logger logger = LogManager.getLogger(ImageDB.class);
    private static HashMap<String, Image> cache = new HashMap<>();
    private Config config = Config.getConfig("mainConfig");

    private int getLastId() {
        File path = new File(config.getProperty("IMAGE_PATH") + "/");
        path.mkdirs();
        return path.list().length;
    }

    public String save(BufferedImage image) throws ConnectionException {
        byte[] array = new byte[10];
        new Random().nextBytes(array);
        String randomId = new String(array, StandardCharsets.US_ASCII);

        File path = new File(config.getProperty("IMAGE_PATH") + "/" + randomId + ".jpg");
        if (!path.getParentFile().exists())
            path.getParentFile().mkdirs();
        try {
            ImageIO.write(image, "jpg", path);
        } catch (IOException e) {
            logger.error("Saving image failed - " + e.getMessage());
            throw new ConnectionException();
        }
        return randomId;
    }

    public File getImageFile(String id) {
        return new File(config.getProperty("IMAGE_PATH") + "/" + id + ".jpg");
    }

    private static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }

    public Image load(String id) throws ConnectionException {
        if (cache.containsKey(id))
            return cache.get(id);
        File path = getImageFile(id);
        try {
            Image image = convertToFxImage(ImageIO.read(path));
            cache.put(id, image);
            return image;
        } catch (IOException e) {
            logger.error("Failed reading image file - " + e.getMessage());
            throw new ConnectionException();
        }
    }
}
