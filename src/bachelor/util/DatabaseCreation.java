/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;

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

        this.croppedImage = createGrayScalePicutre();
        this.pixelReader = croppedImage.getPixelReader();
        WrapperImageName wrapper = new WrapperImageName();

        //This method returns a PixelWriter that provides access to write the pixels of the image.
        PixelWriter binaryPicture = croppedImage.getPixelWriter();

        int thresholdValue = otsuMethod();

        //threshold farbe, decides if pixel are black or white
        Color threshold = Color.rgb(thresholdValue, thresholdValue, thresholdValue);

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
    private WritableImage createGrayScalePicutre() {

        //creates a bufferedPicture of the croppedImage
        BufferedImage grayImg = new BufferedImage((int) croppedImage.getWidth(), (int) croppedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        //throws a grayscaling filter on it
        Graphics graph = grayImg.getGraphics();
        graph.drawImage(SwingFXUtils.fromFXImage(croppedImage, null), 0, 0, null);
        graph.dispose();
        //transform the Image back to a javaFX Image. 
        //replaces old croppedImage with new grayscale Image
        return (WritableImage) SwingFXUtils.toFXImage(grayImg, null);
    }

    private Integer otsuMethod() {
        List<Integer> histogram = new ArrayList<>(Collections.nCopies(256, 0));
        int threshold = 256;
        int bestThresholdValue = 0;
        double betweenMaxValue = 0.0;
        double maxPixel = (croppedImage.getHeight() * croppedImage.getWidth());

        //histogram creation
        for (int y = 0; y < croppedImage.getHeight(); y++) {
            for (int x = 0; x < croppedImage.getWidth(); x++) {
                histogram.set((int) (pixelReader.getColor(x, y).getBlue() * 255),
                        histogram.get((int) (pixelReader.getColor(x, y).getBlue() * 255)) + 1);
            }
        }

        //between class variance calculation
        for (int t = 0; t < threshold; t++) {
            int getBValue = 0;
            int getFValue = 0;

            double meanBNom = 0;
            double meanBDen = 0;
            double meanBackground = 0;

            double meanFNom = 0;
            double meanFDen = 0;
            double meanForeground = 0;

            double tempBetweenClassVar = 0;

            //calculate weightBackground
            for (int i = 0; i < t; i++) {
                getBValue += histogram.get(i);
            }
            double weightBackground = getBValue / maxPixel;

            //calculate weightForeground
            for (int i = t; i < threshold; i++) {
                getFValue += histogram.get(i);
            }
            double weightForeground = getFValue / maxPixel;

            //calculate meanBackground
            for (int j = 0; j < t; j++) {
                meanBNom += (histogram.get(j) * j);
                meanBDen += histogram.get(j);
            }
            if (meanBDen == 0) {
                meanBackground = 0;
            } else {
                meanBackground = meanBNom / meanBDen;
            }

            //calculate meanForeground
            for (int j = t; j < threshold; j++) {
                meanFNom += (histogram.get(j) * j);
                meanFDen += histogram.get(j);
            }
            if (meanFDen == 0) {
                meanForeground = 0;
            } else {
                meanForeground = meanFNom / meanFDen;
            }

            //calculate between class variance
            tempBetweenClassVar = weightBackground * weightForeground
                    * Math.pow(meanBackground - meanForeground, 2);

            if (betweenMaxValue < tempBetweenClassVar) {
                betweenMaxValue = tempBetweenClassVar;
                bestThresholdValue = t;
            }
        }
        return bestThresholdValue;
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
