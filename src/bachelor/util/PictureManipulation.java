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
import java.util.ArrayList;
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
     *
     * @param files
     * @return
     */
    public static ArrayList<String> createListOfFiles(File[] files) {
        ArrayList<String> listOfFiles = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                createListOfFiles(files);
            } else {
                listOfFiles.add(file.getName());
            }
        }
        return listOfFiles;
    }

    /**
     * creates an image (with javaFX) of the choosen file.
     *
     * @param originalFile
     * @return
     * @throws FileNotFoundException
     */
    public static Image createImage(File originalFile) throws FileNotFoundException {
        Image rawImage = new Image(new FileInputStream("C:\\Users\\David\\Desktop\\test\\ganze_bilder\\" + originalFile.getName()));
        return rawImage;
    }

    /**
     * crops and saves the old image as a new file. we do that with javaFX
     * commands it is important, that you use the starting parameters to set the
     * rectangle. because the rectangle defines the size of the new image.
     *
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
     *
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
     *
     * @param newImage
     * @return
     * @throws FileNotFoundException
     */
    public static List<Double> verticalMatrix(File newImage) throws FileNotFoundException {
        Image croppedImage = new Image(new FileInputStream("C:\\Users\\David\\Desktop\\test\\halbe_bilder\\" + newImage.getName()));
        List<Double> verticalMatrix = new ArrayList<>();
        double counter = 0;
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
     * aligns two matrices with each other. we have an referential matrix, and a
     * changing matrix. the changing matrix gets aligned with the referential.
     * first we need to find the max value of both matrices and the position of
     * this value. as soon as we have found it, we can check if they are already
     * aligned with each other. if so, go to the compareMatrices() method, else
     * align them properly.
     *
     * @param refMatrix
     * @param changingMatrix
     */
    public static void alignMatrices(List<Double> refMatrix, List<Double> changingMatrix) throws FileNotFoundException {
        int refSize = refMatrix.size();
        int chaSize = changingMatrix.size();
        double refMax, chaMax;
        int refMaxPos, chaMaxPos;
        chaMax = refMax = Integer.MIN_VALUE;
        chaMaxPos = refMaxPos = -1;
        for (int i = 0; i < refSize; i++) {
            double value = refMatrix.get(i);
            if (value > refMax) {
                refMax = value;
                refMaxPos = i;
            }
        }
        for (int i = 0; i < chaSize; i++) {
            double value = changingMatrix.get(i);
            if (value > chaMax) {
                chaMax = value;
                chaMaxPos = i;
            }
        }
        if (refMaxPos == chaMaxPos) {
//            System.out.println("same");
            compareMatrices(refMatrix, changingMatrix);
        } else if (refMaxPos > chaMaxPos) {
            int difference = refMaxPos - chaMaxPos;
            List<Double> newChangingMatrix = new ArrayList<>();
            for (int i = 0; i < difference; i++) {
                newChangingMatrix.add(i, 0.0);
            }
            for (int i = 0; i < chaSize - difference; i++) {
                newChangingMatrix.add(i + difference, changingMatrix.get(i));
            }
//            System.out.println("right shit: "+newChangingMatrix);
            compareMatrices(refMatrix, newChangingMatrix);
        } else {
            int difference = chaMaxPos - refMaxPos;
            List<Double> newChangingMatrix = new ArrayList<>();
            for (int i = difference; i < chaSize; i++) {
                newChangingMatrix.add(i - difference, changingMatrix.get(i));
            }
            for (int i = chaSize - difference; i < chaSize; i++) {
                newChangingMatrix.add(i, 0.0);
            }
//            System.out.println("left shift: "+newChangingMatrix);
            compareMatrices(refMatrix, newChangingMatrix);
        }
    }

    /**
     * normalizes matrix. easier to compare the graphs with each other now.
     * because the scale ends always at one. to normalize the matrix you need
     * divide each element of the list with the sum of all elements.
     *
     * @param Matrix
     * @return
     * @throws FileNotFoundException
     */
    public static List<Double> normalizeMatrix(List<Double> Matrix) throws FileNotFoundException {
        double sum = 0;
        List<Double> normalizedMatrix = new ArrayList<>();
        for (int i = 0; i < Matrix.size(); i++) {
            sum += Matrix.get(i);
        }
        for (int i = 0; i < Matrix.size(); i++) {
            double matrixValue = Matrix.get(i);
            double relativValue = matrixValue / sum;
            normalizedMatrix.add(i, relativValue);
        }
        return normalizedMatrix;
    }

    /**
     * compares to normed list/matrices with each other. the result is the
     * similarity of the two matrices (in percentage).
     *
     * @param refMatrix
     * @param changingMatrix
     * @return
     * @throws FileNotFoundException
     */
    private static double compareMatrices(List<Double> refMatrix, List<Double> changingMatrix) throws FileNotFoundException {
        List<Double> normRefMatrix = normalizeMatrix(refMatrix);
        List<Double> normChaMatrix = normalizeMatrix(changingMatrix);
        List<Double> percentageValues = new ArrayList<>();
        double sumOfpercentageValues = 0;
        double totalPercentage;
        for (int i = 0; i < normChaMatrix.size(); i++) {
            double x = normRefMatrix.get(i);
            double y = normChaMatrix.get(i);
            double percentage;
            if (x > y) {
                percentage = y / x;
                percentageValues.add(i, percentage);
            } else if (x < y) {
                double temp = y / x;
                percentage = Math.abs(temp - 2);
                percentageValues.add(i, percentage);
            } else {
                percentageValues.add(i, 1.0);
            }
        }
        for (int i = 0; i < percentageValues.size(); i++) {
            sumOfpercentageValues += percentageValues.get(i);
        }
        totalPercentage = (sumOfpercentageValues / percentageValues.size());
        System.out.println(totalPercentage);
        return totalPercentage;
    }

    /**
     * Takes the percentages of the horizontal and vertical Results of a picture
     * and calculates how well the new picture fits with the referential
     * picture.
     *
     * @param vertHorzPctValues
     * @return
     */
    public static double totalVertHorzPct(List<Double> vertHorzPctValues) {
        double sumOfPctValues = 0;
        double totalPercentage;
        for (int i = 0; i < vertHorzPctValues.size(); i++) {
            sumOfPctValues += vertHorzPctValues.get(i);
        }
        totalPercentage = sumOfPctValues / vertHorzPctValues.size();
        return totalPercentage;
    }

}
