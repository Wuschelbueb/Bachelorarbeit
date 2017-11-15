/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import javax.swing.GrayFilter;

/**
 *
 * @author David
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
     * crops the original image to a new one. we do that with javaFX commands it
     * is important, that you use the starting parameters to set the rectangle.
     * because the rectangle defines the size of the new image.
     *
     * @param xStartingPosition
     * @param xDistance
     * @param yStartingPosition
     * @param yDistance
     * @return
     * @throws FileNotFoundException
     */
    public WrapperImageName createReferentialImage(int xStartingPosition, int yStartingPosition, int xDistance, int yDistance) throws FileNotFoundException {
        this.pixelReader = rawImage.getPixelReader();
        this.croppedImage = new WritableImage(pixelReader, xStartingPosition, yStartingPosition, xDistance, yDistance);
        return createBinaryPicture();
    }

    /**
     * creates the images, which need to be compared. they will be 20% bigger
     * than the referential image. with the bigger size we make sure that the
     * title is on the image
     *
     * @param xStartingPosition
     * @param xDistance
     * @param yStartingPosition
     * @param yDistance
     * @return
     * @throws FileNotFoundException
     */
    public WrapperImageName createComparableImage(int xStartingPosition, int yStartingPosition, int xDistance, int yDistance) throws FileNotFoundException {

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
        if (xStartingPosition > (newXDistance / 2) && yStartingPosition <= (newYDistance / 2)) {
            xStartingPosition -= (int) (newXDistance / 2);
            yStartingPosition = 0;
        } else if (xStartingPosition <= (newXDistance / 2) && yStartingPosition > (newYDistance / 2)) {
            xStartingPosition = 0;
            yStartingPosition -= (int) (newYDistance / 2);
        } else if (xStartingPosition <= (newXDistance / 2) && yStartingPosition <= (newYDistance / 2)) {
            xStartingPosition = 0;
            yStartingPosition = 0;
        } else if (xStartingPosition > (newXDistance / 2) && yStartingPosition > (newYDistance / 2)) {
            xStartingPosition -= (int) (newXDistance / 2);
            yStartingPosition -= (int) (newYDistance / 2);
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
        this.croppedImage = new WritableImage(pixelReader, xStartingPosition, yStartingPosition, xDistance, yDistance);
        return createBinaryPicture();
    }

    /**
     * creates a binary picture and saves it as png.
     *
     * @throws FileNotFoundException
     */
    private WrapperImageName createBinaryPicture() throws FileNotFoundException {

        createGrayScalePicutre();
        this.pixelReader = croppedImage.getPixelReader();
        WrapperImageName wrapper = new WrapperImageName();

        //This method returns a PixelWriter that provides access to write the pixels of the image.
        PixelWriter binaryPicture = croppedImage.getPixelWriter();

        //threshold farbe, decides if pixel are black or white
        Color threshold = Color.rgb(150, 150, 150);

        //checks every pixel in the image and compares them to the threshold
        for (int y = 0; y < croppedImage.getHeight(); y++) {
            for (int x = 0; x < croppedImage.getWidth(); x++) {
                Color color = pixelReader.getColor(x, y);
                if (color.getBlue() > threshold.getBlue() && color.getRed() > threshold.getRed() && color.getGreen() > threshold.getGreen()) {
                    binaryPicture.setColor(x, y, Color.WHITE);
                } else {
                    binaryPicture.setColor(x, y, Color.BLACK);
                }
            }
        }
//        used to create a file
//        this.newFile = new File(setURL());
//        if (checkForCroppedImage() == false) {
//            try {
//                ImageIO.write(SwingFXUtils.fromFXImage(croppedImage, null), "png", newFile);
//                System.out.println("snapshot saved: " + newFile.getAbsolutePath());
//            } catch (IOException ex) {
//            }
//        }
        wrapper.setFileName(getName());
        wrapper.setImage(croppedImage);
        return wrapper;
    }

    /**
     * creates a grayscaleImage of the original image. based on:
     * https://stackoverflow.com/questions/9131678/convert-a-rgb-image-to-grayscale-image-reducing-the-memory-in-java
     *
     *
     */
    private void createGrayScalePicutre() {

        //creates a bufferedPicture of the croppedImage
        BufferedImage buffImg = SwingFXUtils.fromFXImage(croppedImage, null);
        BufferedImage image = new BufferedImage((int) croppedImage.getWidth(), (int) croppedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        //throws a grayscaling filter on it
        Graphics g = image.getGraphics();
        g.drawImage(buffImg, 0, 0, null);
        g.dispose();
        //transform the Image back to a javaFX Image. 
        //replaces old croppedImage with new grayscale Image
        this.croppedImage = (WritableImage) SwingFXUtils.toFXImage(image, null);
//        image.flush();
    }

    private void otsuMethod(Image greyImage) {
        /*todo*/
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
