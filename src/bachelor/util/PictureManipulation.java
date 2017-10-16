/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor.util;

import static bachelor.util.directory.showFiles;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
public class PictureManipulation {

    /**
     * creates a list of all files in a directory.
     * @param files
     * @return 
     */
    public static ArrayList<String> createListOfFiles(File[] files) {
        ArrayList<String> listOfFiles = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                showFiles(files);
            } else {
                listOfFiles.add(file.getName());
            }
        }
        return listOfFiles;
    }
/**
 * creates an image (with javaFX) of the choosen file.
 * @param originalFile
 * @return
 * @throws FileNotFoundException 
 */
    public static Image createImage(File originalFile) throws FileNotFoundException {
        Image rawImage = new Image(new FileInputStream("C:\\Users\\David\\Desktop\\test\\ganze_bilder\\" + originalFile.getName()));
        return rawImage;
    }
/**
 * crops and saves the old image as a new file. we do that with javaFX commands
 * it is important, that you use the starting parameters to set the rectangle.
 * because the rectangle defines the size of the new image.
 * @param originalFile
 * @param xStartingPostion
 * @param xDistance
 * @param yStartingPostion
 * @param yDistance
 * @throws FileNotFoundException 
 */
    public static void cropAndSaveImage(File originalFile, int xStartingPostion, int xDistance, int yStartingPostion, int yDistance) throws FileNotFoundException {
        Image rawImage = createImage(originalFile);
        String NameOfFile = originalFile.getName();

        //Reading color from the loaded image 
        PixelReader pixelReader = rawImage.getPixelReader();

        //crops image
        WritableImage croppedImage = new WritableImage(pixelReader, xStartingPostion, yStartingPostion, xDistance, yDistance);

        //This method returns a PixelWriter that provides access to write the pixels of the image.
        PixelWriter writer = croppedImage.getPixelWriter();

        //threshold farbe, decides if pixel are black or white
        Color threshold = Color.rgb(102, 102, 102);

        //checks every pixel in the image and compares them to the threshold
        for (int y = 0; y < yDistance; y++) {
            for (int x = 0; x < xDistance; x++) {
                Color color = pixelReader.getColor(x, y + yStartingPostion);
                if (color.getBlue() > threshold.getBlue() && color.getRed() > threshold.getRed() && color.getGreen() > threshold.getGreen()) {
                    writer.setColor(x, y, Color.WHITE);
                } else {
                    writer.setColor(x, y, Color.BLACK);
                }
            }
        }
        File newImage = new File("C:\\Users\\David\\Desktop\\test\\halbe_bilder\\" + NameOfFile);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(croppedImage, null), "png", newImage);
            System.out.println("snapshot saved: " + newImage.getAbsolutePath());
        } catch (IOException ex) {
        }
    }

    /**
     * creates the horizontal matrix of the new Image.
     * @param newImage
     * @return
     * @throws FileNotFoundException 
     */
    public static List<Integer> horizontalMatrix(File newImage) throws FileNotFoundException {
        Image croppedImage = new Image(new FileInputStream("C:\\Users\\David\\Desktop\\test\\halbe_bilder\\" + newImage.getName()));
        List<Integer> horizontalMatrix = new ArrayList<>();
        int counter = 0;
        int heightOfImage = (int) croppedImage.getHeight();
        int widthOfImage = (int) croppedImage.getWidth();
        for (int y = 0; y < heightOfImage; y++) {
            for (int x = 0; x < widthOfImage; x++) {
                Color croppedImagePixelColor = croppedImage.getPixelReader().getColor(x, y);
                if (croppedImagePixelColor.equals(Color.BLACK)) {
                    counter++;
                }
            }
            horizontalMatrix.add(counter);
            counter = 0;
        }
        return horizontalMatrix;
    }

    /**
     * creates the vertical matrix of the new Image.
     * @param newImage
     * @return
     * @throws FileNotFoundException 
     */
    public static List<Integer> verticalMatrix(File newImage) throws FileNotFoundException {
        Image croppedImage = new Image(new FileInputStream("C:\\Users\\David\\Desktop\\test\\halbe_bilder\\" + newImage.getName()));
        List<Integer> verticalMatrix = new ArrayList<>();
        int counter = 0;
        int heightOfImage = (int) croppedImage.getHeight();
        int widthOfImage = (int) croppedImage.getWidth();
        for (int y = 0; y < widthOfImage; y++) {
            for (int x = 0; x < heightOfImage; x++) {
                Color croppedImagePixelColor = croppedImage.getPixelReader().getColor(x, y);
                if (croppedImagePixelColor.equals(Color.BLACK)) {
                    counter++;
                }
            }
            verticalMatrix.add(counter);
            counter = 0;
        }
        return verticalMatrix;
    }
/**
 * aligns two matrices with each other. we have an referential matrix, and a changing matrix.
 * the changing matrix gets aligned with the referential.
 * first we need to find the max value of both matrices and the position of this value.
 * as soon as we have found it, we can check if they are already aligned with each other.
 * if so, go to the compareMatrices() method, else align them properly.
 * @param refMatrix
 * @param changingMatrix 
 */
    public static void alignMatrices(List<Integer> refMatrix, List<Integer> changingMatrix) {
        int refSize = refMatrix.size();
        int chaSize = changingMatrix.size();
        int refMax, chaMax, refMaxPos, chaMaxPos;
        chaMax = refMax = Integer.MIN_VALUE;
        chaMaxPos = refMaxPos = -1;
        for (int i = 0; i < refSize; i++) {
            int value = refMatrix.get(i);
            if (value > refMax) {
                refMax = value;
                refMaxPos = i;
            }
        }
        for (int i = 0; i < chaSize; i++) {
            int value = changingMatrix.get(i);
            if (value > chaMax) {
                chaMax = value;
                chaMaxPos = i;
            }
        }
        if (refMaxPos == chaMaxPos) {
            compareMatrices(refMatrix, changingMatrix);
        } else if (refMaxPos > chaMaxPos) {
            int difference = refMaxPos - chaMaxPos;
            List<Integer> newChangingMatrix = new ArrayList<>();
            for (int i = 0; i < difference; i++) {
                newChangingMatrix.set(i, 0);
            }
            for (int i = 0; i < chaSize - difference; i++) {
                newChangingMatrix.set(i + difference, refMatrix.get(i));
            }
            compareMatrices(refMatrix, newChangingMatrix);
        } else {
            int difference = chaMaxPos - refMaxPos;
            List<Integer> newChangingMatrix = new ArrayList<>();
            for (int i = difference; i < chaSize - difference; i++) {
                newChangingMatrix.set(i - difference, refMatrix.get(i));
            }
            for (int i = chaSize-difference; i < chaSize; i++) {
                newChangingMatrix.set(i, 0);
            }
            compareMatrices(refMatrix, newChangingMatrix);
        }
    }

    private static void compareMatrices(List<Integer> refMatrix, List<Integer> changingMatrix) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
