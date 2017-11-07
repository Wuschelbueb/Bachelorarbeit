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
public class DatabaseCreation {

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
    public DatabaseCreation(File file) throws FileNotFoundException {
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
     * creates the images, which need to be compared. they will be 20% bigger
     * than the referential image. with the bigger size we make sure that the
     * title is on the image
     *
     * @param xStartingPostion
     * @param xDistance
     * @param yStartingPostion
     * @param yDistance
     * @throws FileNotFoundException
     */
    public void createComparableImage(int xStartingPostion, int yStartingPostion, int xDistance, int yDistance) throws FileNotFoundException {

        //Reading color from the loaded image 
        this.pixelReader = rawImage.getPixelReader();

        int ImageWidth = (int) rawImage.getWidth();
        int ImageHeight = (int) rawImage.getHeight();
        int newXDistance;
        int newYDistance;
        
        //used to scale the new picture.      
        if (xDistance <= 100) {
            newXDistance = (int) (xDistance * 3);
        } else if (xDistance <= 200) {
            newXDistance = (int) (xDistance * 1.5);
        } else if (xDistance <= 350) {
            newXDistance = (int) (xDistance * 0.9);
        } else if (xDistance <= 500) {
            newXDistance = (int) (xDistance * 0.7);
        } else if (xDistance <= 1000) {
            newXDistance = (int) (xDistance * 0.4);
        } else if (xDistance <= 2000) {
            newXDistance = (int) (xDistance * 0.2);
        } else {
            newXDistance = (int) (xDistance * 0.1);
        }

        if (yDistance <= 100) {
            newYDistance = (int) (yDistance * 3);
        } else if (yDistance <= 200) {
            newYDistance = (int) (yDistance * 1.5);
        } else if (yDistance <= 350) {
            newYDistance = (int) (yDistance * 0.9);
        } else if (yDistance <= 500) {
            newYDistance = (int) (yDistance * 0.7);
        } else if (yDistance <= 1000) {
            newYDistance = (int) (yDistance * 0.4);
        } else if (yDistance <= 2000) {
            newYDistance = (int) (yDistance * 0.2);
        } else {
            newYDistance = (int) (yDistance * 0.1);
        }

        //conditions to set the new startingPositions
        if (xStartingPostion > (newXDistance / 2) && yStartingPostion <= (newYDistance / 2)) {
            xStartingPostion -= (int) (newXDistance / 2);
            yStartingPostion = 0;
        } else if (xStartingPostion <= (newXDistance / 2) && yStartingPostion > (newYDistance / 2)) {
            xStartingPostion = 0;
            yStartingPostion -= (int) (newYDistance / 2);
        } else if (xStartingPostion <= (newXDistance / 2) && yStartingPostion <= (newYDistance / 2)) {
            xStartingPostion = 0;
            yStartingPostion = 0;
        } else if (xStartingPostion > (newXDistance / 2) && yStartingPostion > (newYDistance / 2)) {
            xStartingPostion -= (int) (newXDistance / 2);
            yStartingPostion -= (int) (newYDistance / 2);
        }

        //conditions to set the new distances. like that the picture gets bigger
        if ((xDistance + newXDistance) >= ImageWidth && (yDistance + newYDistance) < ImageHeight) {
            xDistance = ImageWidth;
            yDistance += newYDistance;
        } else if ((xDistance + newXDistance) < ImageWidth && (yDistance + newYDistance) >= ImageHeight) {
            xDistance += newXDistance;
            yDistance = ImageHeight;
        } else if ((xDistance + newXDistance) >= ImageWidth && (yDistance + newYDistance) >= ImageHeight) {
            xDistance = ImageWidth;
            yDistance = ImageHeight;
        } else {
            xDistance += newXDistance;
            yDistance += newYDistance;
        }
        //crops image
        this.croppedImage = new WritableImage(pixelReader, xStartingPostion, yStartingPostion, xDistance, yDistance);
        createBinaryPicture();
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
    public void createReferentialImage(int xStartingPostion, int yStartingPostion, int xDistance, int yDistance) throws FileNotFoundException {
        this.pixelReader = rawImage.getPixelReader();
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
            return true;
        } else {
            return false;
        }
    }
}
