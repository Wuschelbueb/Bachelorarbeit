/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;

/**
 *
 * @author wusch
 */
public class PictureTransformation {
    private File ogFile = null;
    private File newFile = null;
    private Image rawImage = null;
    private WritableImage croppedImage = null;
    private PixelReader pixelReader = null;

    /**
     * creates an image (with javaFX) of the choosen file.
     *
     * @param file
     * @throws FileNotFoundException
     */
    public PictureTransformation(File file) throws FileNotFoundException {
        this.ogFile = file;
        this.rawImage = new Image(new FileInputStream(getURL()));
    }

    private String getURL() {
        return ogFile.getAbsolutePath();
    }

    private String setURL() {
        String setURL = "C:\\Users\\David\\Desktop\\test\\halbe_bilder\\" + getName();
        return setURL;
    }

    private String getName() {
        return ogFile.getName();
    }

    /**
     * crops the original image to a new one. we do that with javaFX commands it
     * is important, that you use the starting parameters to set the rectangle.
     * because the rectangle defines the size of the new image.
     *
     * @param xStartingPostion
     * @param xDistance
     * @param yStartingPostion
     * @param yDistance
     * @throws FileNotFoundException
     */
    public void createCroppedImage(int xStartingPostion, int yStartingPostion, int xDistance, int yDistance) throws FileNotFoundException {
        //Reading color from the loaded image 
        this.pixelReader = rawImage.getPixelReader();

        //crops image
        this.croppedImage = new WritableImage(pixelReader, xStartingPostion, yStartingPostion, xDistance, yDistance);
        createBinaryPicture();
    }

    /**
     * creates a binary picture and saves it as png.
     *
     * @throws FileNotFoundException
     */
    private void createBinaryPicture() throws FileNotFoundException {

        pixelReader = croppedImage.getPixelReader();

        //This method returns a PixelWriter that provides access to write the pixels of the image.
        PixelWriter writer = croppedImage.getPixelWriter();

        //threshold farbe, decides if pixel are black or white
        Color threshold = Color.rgb(102, 102, 102);

        //checks every pixel in the image and compares them to the threshold
        for (int y = 0; y < croppedImage.getHeight(); y++) {
            for (int x = 0; x < croppedImage.getWidth(); x++) {
                Color color = pixelReader.getColor(x, y);
                if (color.getBlue() > threshold.getBlue() && color.getRed() > threshold.getRed() && color.getGreen() > threshold.getGreen()) {
                    writer.setColor(x, y, Color.WHITE);
                } else {
                    writer.setColor(x, y, Color.BLACK);
                }
            }
        }
        this.newFile = new File(setURL());
        if (checkForCroppedImage() == false) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(croppedImage, null), "png", newFile);
                System.out.println("snapshot saved: " + newFile.getAbsolutePath());
            } catch (IOException ex) {
            }
        }

    }

    /**
     * checks if such a file already exists.
     *
     * @return
     * @throws FileNotFoundException
     */
    private boolean checkForCroppedImage() throws FileNotFoundException {
        if (newFile.exists() && !newFile.isDirectory()) {
//            System.out.println(true);
            return true;
        } else {
            System.out.println(false);
            return false;
        }
    }
}
