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
public class MyPicture {

    private File ogFile = null;
    private File newFile = null;
    private Image rawImage = null;
    private WritableImage croppedImage = null;
    private PixelReader pixelReader = null;
    private List<Double> horizontalMatrix = null;
    private List<Double> verticalMatrix = null;

    /**
     * creates an image (with javaFX) of the choosen file.
     *
     * @param file
     * @throws FileNotFoundException
     */
    public MyPicture(File file) throws FileNotFoundException {
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

    /**
     * creates the horizontal matrix of the new Image.
     *
     * @return
     * @throws FileNotFoundException
     */
    public void calculateHorizontalMatrix()  {
//        Image modifiedImage = new Image(new FileInputStream("C:\\Users\\David\\Desktop\\test\\halbe_bilder\\" + newImage));
        List<Double> tempHorzMatrix = new ArrayList<>();
        double counter = 0;
        int heightOfImage = (int) rawImage.getHeight();
        int widthOfImage = (int) rawImage.getWidth();
        for (int y = 0; y < heightOfImage; y++) {
            for (int x = 0; x < widthOfImage; x++) {
                Color croppedImagePixelColor = rawImage.getPixelReader().getColor(x, y);
                if (croppedImagePixelColor.equals(Color.BLACK)) {
                    counter++;
                }
            }
            tempHorzMatrix.add(counter);
            counter = 0;
        }
        horizontalMatrix = normalizeMatrix(tempHorzMatrix);
    }

    public List<Double> getHorizontalMatrix() throws FileNotFoundException {
//        Image modifiedImage = new Image(new FileInputStream("C:\\Users\\David\\Desktop\\test\\halbe_bilder\\" + newImage));
        if (horizontalMatrix == null) {
            calculateHorizontalMatrix();
        }
        return horizontalMatrix;
    }

    /**
     * creates the vertical matrix of the new Image.
     *
     * @return
     * @throws FileNotFoundException
     */
    public List<Double> getVerticalMatrix() throws FileNotFoundException {
//        Image modifiedImage = new Image(new FileInputStream("C:\\Users\\David\\Desktop\\test\\halbe_bilder\\" + newImage.getName()));
        List<Double> tempHorzMatrix = new ArrayList<>();
        double counter = 0;
        int heightOfImage = (int) rawImage.getHeight();
        int widthOfImage = (int) rawImage.getWidth();
        for (int y = 0; y < widthOfImage; y++) {
            for (int x = 0; x < heightOfImage; x++) {
                Color croppedImagePixelColor = rawImage.getPixelReader().getColor(y, x);
                if (croppedImagePixelColor.equals(Color.BLACK)) {
                    counter++;
                }
            }
            tempHorzMatrix.add(counter);
            counter = 0;
        }
        return normalizeMatrix(tempHorzMatrix);
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
    private static List<Double> normalizeMatrix(List<Double> Matrix)  {
        double sum = 0;
        List<Double> normalizedMatrix = new ArrayList<>();
        for (Double d : Matrix) {
            sum += d;
        }
        for (Double d : Matrix) {
            double relativValue = d / sum;
            normalizedMatrix.add(relativValue);
        }
        return normalizedMatrix;
    }

    public List<Double> compareTo(MyPicture theOtherPicture) throws FileNotFoundException {
        this.horizontalMatrix = getHorizontalMatrix();
        theOtherPicture.horizontalMatrix = theOtherPicture.getHorizontalMatrix();
        double vert = alignAndCompare(getVerticalMatrix(), theOtherPicture.getVerticalMatrix());
        double horz = alignAndCompare(horizontalMatrix, theOtherPicture.horizontalMatrix);
        List<Double> totalSimilarity = new ArrayList<>();
        totalSimilarity.add(vert);
        totalSimilarity.add(horz);
//        double totalSimilarity = (vert+horz)/2;
        return totalSimilarity;
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
    public static double alignAndCompare(List<Double> refMatrix, List<Double> changingMatrix) throws FileNotFoundException {
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
            return compareCorrelation(refMatrix, changingMatrix);
        } else if (refMaxPos > chaMaxPos) {
            int difference = refMaxPos - chaMaxPos;
            System.out.println("Anzahl PIxel zwischen Maxima (right): " +difference);
            List<Double> newChangingMatrix = new ArrayList<>();
            for (int i = 0; i < difference; i++) {
                newChangingMatrix.add(i, 0.0);
            }
            for (int i = 0; i < chaSize - difference; i++) {
                newChangingMatrix.add(i + difference, changingMatrix.get(i));
            }
//            System.out.println("right shit: "+newChangingMatrix);
            return compareCorrelation(refMatrix, newChangingMatrix);
        } else {
            int difference = chaMaxPos - refMaxPos;
            
            System.out.println("Anzahl PIxel zwischen Maxima (left): " +difference);
            List<Double> newChangingMatrix = new ArrayList<>();
            for (int i = difference; i < chaSize; i++) {
                newChangingMatrix.add(i - difference, changingMatrix.get(i));
            }
            for (int i = chaSize - difference; i < chaSize; i++) {
                newChangingMatrix.add(i, 0.0);
            }
//            System.out.println("left shift: "+newChangingMatrix);
            return compareCorrelation(refMatrix, newChangingMatrix);
        }
    }

    /**
     * compares to normed list/matrices with each other. the result is the
     * similarity of the two matrices. we use the Pearson correlation
     * coefficient to calculate the similarity
     *
     * @param refMatrix
     * @param changingMatrix
     * @return
     * @throws FileNotFoundException
     */
    private static double compareCorrelation(List<Double> refMatrix, List<Double> changingMatrix) throws FileNotFoundException {
        double sumOfRef = 0;
        double sumOfCha = 0;
        double sizeOfMatrix = refMatrix.size();
        double correlationNumerator = 0;
        for (Double d : refMatrix) {
            sumOfRef += d;
        }
        for (Double d : changingMatrix) {
            sumOfCha += d;
        }
        double averageRef = sumOfRef / sizeOfMatrix;
        double averageCha = sumOfCha / sizeOfMatrix;
        for (int i = 0; i < sizeOfMatrix; i++) {
            correlationNumerator += (refMatrix.get(i) - averageRef) * (changingMatrix.get(i) - averageCha);
        }
        double tempRef = 0;
        double tempCha = 0;
        for (Double d : refMatrix) {
            tempRef += Math.pow(d - averageRef, 2);
        }
        for (Double d : changingMatrix) {
            tempCha += Math.pow(d - averageCha, 2);
        }
        double correlationDenominator = Math.sqrt(tempRef * tempCha);
        double correlation = correlationNumerator / correlationDenominator;
        return correlation;
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
        for (Double d : vertHorzPctValues) {
            sumOfPctValues += d;
        }
        totalPercentage = sumOfPctValues / vertHorzPctValues.size();
        return totalPercentage;
    }

}
