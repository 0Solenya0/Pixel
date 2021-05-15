package controller;

import apps.auth.State;
import apps.auth.controller.SettingsController;
import db.dbSet.ImageDBSet;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageController extends Controller {
    private static final Logger logger = LogManager.getLogger(ImageController.class);

    public String saveImageToDB(File file) throws ConnectionException {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            ImageDBSet imageDBSet = new ImageDBSet();
            String id = imageDBSet.save(bufferedImage);
            return id;

        } catch (IOException e) {
            logger.error("Failed to read photo");
            return null;
        }
    }
}
