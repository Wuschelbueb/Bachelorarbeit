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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 *
 * @author David
 */
public class MyImage implements Comparable<MyImage> {

    private File originalFile = null;
    private Image rawImage = null;
    private WritableImage croppedImage = null;
    private PixelReader pixelReader = null;
    private List<Double> horizontalMatrix = null;
    private List<Double> verticalMatrix = null;
    private Double similarity = null;
    private MyImage refImage = null;
    private Thread t;
    
    public MyImage(File file) throws FileNotFoundException {
        this.originalFile = file;
        this.rawImage = new Image(new FileInputStream(getURL()));
    }

    public MyImage(File file, MyImage refImage) throws FileNotFoundException {
        this(file);
        this.refImage = refImage;
    }

    public String getURL() {
        return originalFile.getPath();
    }

    public String getName() {
        return originalFile.getName();
    }

    public Image getBinaryImage() {
        return croppedImage;
    }

    public Double getSimilarity() {
        return similarity;
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
     */
    public void createReferentialImage(int xStartingPosition,
            int yStartingPosition, int xDistance, int yDistance) {
        pixelReader = rawImage.getPixelReader();
        croppedImage = new WritableImage(pixelReader, xStartingPosition,
                yStartingPosition, xDistance, yDistance);
        createBinaryImage();
        rawImage = null;

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
     */
    public void createComparableImage(int xStartingPosition,
            int yStartingPosition, int xDistance, int yDistance) {
        pixelReader = rawImage.getPixelReader();
        int ImageWidth = (int) rawImage.getWidth();
        int ImageHeight = (int) rawImage.getHeight();
        int newXDistance = (int) (xDistance + 400);
        int newYDistance = (int) (yDistance + 400);
        
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
            yDistance = newYDistance;
        } else if ((xDistance + newXDistance) < ImageWidth && (yDistance + newYDistance) >= ImageHeight) {
            xDistance = newXDistance;
            yDistance = ImageHeight;
        } else if ((xDistance + newXDistance) >= ImageWidth && (yDistance + newYDistance) >= ImageHeight) {
            xDistance = ImageWidth;
            yDistance = ImageHeight;
        } else {
            xDistance = newXDistance;
            yDistance = newYDistance;
        }
//crops image
        croppedImage = new WritableImage(pixelReader, xStartingPosition,
                yStartingPosition, xDistance, yDistance);
        createBinaryImage();
        rawImage = null;
    }

    /**
     * create binary image. need to transform the color image first to a
     * grayscale image. after that it gets transformed to a binary one.
     */
    private void createBinaryImage() {

        croppedImage = createGrayScaleImage();
        pixelReader = croppedImage.getPixelReader();
//This method returns a PixelWriter that provides access to write the pixels of the image.
        PixelWriter imageWriter = croppedImage.getPixelWriter();
//threshold farbe, decides if pixel are black or white
        int thresholdValue = otsuMethod();
        Color threshold = Color.rgb(thresholdValue, thresholdValue, thresholdValue);
//checks every pixel in the image and compares them to the threshold
        for (int y = 0; y < croppedImage.getHeight(); y++) {
            for (int x = 0; x < croppedImage.getWidth(); x++) {
                Color color = pixelReader.getColor(x, y);
                if (color.getBlue() > threshold.getBlue()
                        && color.getRed() > threshold.getRed()
                        && color.getGreen() > threshold.getGreen()) {
                    imageWriter.setColor(x, y, Color.WHITE);
                } else {
                    imageWriter.setColor(x, y, Color.BLACK);
                }
            }
        }
    }

    /**
     * creates a grayscaleImage of the original image. based on:
     * https://stackoverflow.com/questions/9131678/convert-a-rgb-image-to-grayscale-image-reducing-the-memory-in-java
     *
     *
     */
    private WritableImage createGrayScaleImage() {

//creates a bufferedPicture of the croppedImage
        BufferedImage grayImg = new BufferedImage((int) croppedImage.getWidth(),
                (int) croppedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
//throws a grayscaling filter on it
        Graphics graph = grayImg.getGraphics();
        graph.drawImage(SwingFXUtils.fromFXImage(croppedImage, null), 0, 0, null);
        graph.dispose();
//transform the Image back to a javaFX Image.         
//replaces old croppedImage with new grayscale Image
        return (WritableImage) SwingFXUtils.toFXImage(grayImg, null);
    }

    /**
     * calculates the best RGB-threshold to create a binary picture.
     * http://www.labbookpages.co.uk/software/imgProc/otsuThreshold.html
     *
     * @return
     */
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
            double meanBackground;

            double meanFNom = 0;
            double meanFDen = 0;
            double meanForeground;

            double tempBetweenClassVar;
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
     * calculate the vertical Matrix of the binaryImage.
     */
    public void calculateVerticalMatrix() {
        List<Double> tempVertMatrix = new ArrayList<>();
        double counter = 0;
        int heightOfImage = (int) croppedImage.getHeight();
        int widthOfImage = (int) croppedImage.getWidth();
        for (int y = 0; y < heightOfImage; y++) {
            for (int x = 0; x < widthOfImage; x++) {
                Color binaryImagePixelColor = croppedImage.getPixelReader().getColor(x, y);
                if (binaryImagePixelColor.equals(Color.BLACK)) {
                    counter++;
                }
            }
            tempVertMatrix.add(counter);
            counter = 0;
        }
        verticalMatrix = tempVertMatrix;
    }

    public List<Double> getVerticalMatrix() {
        if (verticalMatrix == null) {
            calculateVerticalMatrix();
        }
        return verticalMatrix;
    }

    /**
     * calculate the horizontal Matrix of the binaryImage.
     */
    public void calculateHorizontalMatrix() {
        List<Double> tempHorzMatrix = new ArrayList<>();
        double counter = 0;
        int heightOfImage = (int) croppedImage.getHeight();
        int widthOfImage = (int) croppedImage.getWidth();
        for (int y = 0; y < widthOfImage; y++) {
            for (int x = 0; x < heightOfImage; x++) {
                Color croppedImagePixelColor = croppedImage.getPixelReader().getColor(y, x);
                if (croppedImagePixelColor.equals(Color.BLACK)) {
                    counter++;
                }
            }
            tempHorzMatrix.add(counter);
            counter = 0;
        }
        horizontalMatrix = tempHorzMatrix;
    }

    public List<Double> getHorizontalMatrix() {
        if (horizontalMatrix == null) {
            calculateHorizontalMatrix();
        }
        return horizontalMatrix;
    }

    /**
     * normalizes matrix. easier to chiSquareMethod the graphs with each other
     * now. because the scale ends always at one. to normalize the matrix you
     * need divide each element of the list with the sum of all elements.
     *
     * @param Matrix
     * @return
     */
    private static List<Double> normalizeMatrix(List<Double> Matrix) {
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

    /**
     * calculates the similarity between to images.
     *
     * @param refImage
     */
    public void calculateSimilarity(MyImage refImage) {
        double vert = compareChiSquare(refImage.getVerticalMatrix(), getVerticalMatrix());
        double horz = compareChiSquare(refImage.getHorizontalMatrix(), getHorizontalMatrix());
//store result in so class variable
        similarity = (vert + horz) / 2;
    }

    private static double compareChiSquare(List<Double> refMatrix, List<Double> changingMatrix) {
        int maxMoves = changingMatrix.size() - refMatrix.size();
        List<Double> normRefMatrix = normalizeMatrix(refMatrix);
        double finalResult = 10.0;
        for (int i = 0; i <= maxMoves; i++) {
            List<Double> newChangingMatrix = new ArrayList<>();
            for (int j = i; j < refMatrix.size() + i; j++) {
                newChangingMatrix.add(changingMatrix.get(j));
            }
            List<Double> normChaMatrix = normalizeMatrix(newChangingMatrix);
            double tempResult = chiSquareMethod(normRefMatrix, normChaMatrix);
            if (tempResult <= finalResult) {
                finalResult = tempResult;
            }
        }
        return finalResult;
    }

    /**
     * chi square algorithm. needed to compare matrices with each other.
     *
     * @param refMatrix
     * @param changingMatrix
     * @return
     */
    private static double chiSquareMethod(List<Double> refMatrix, List<Double> changingMatrix) {
        double sizeOfMatrix = refMatrix.size();
        double chiSquare = 0;
        for (int i = 0; i < sizeOfMatrix; i++) {
            double numerator = Math.pow((refMatrix.get(i) - changingMatrix.get(i)), 2);
            double denominator = refMatrix.get(i) + changingMatrix.get(i);
            chiSquare += (denominator != 0) ? (numerator / denominator) : 0;
        }
        return chiSquare;
    }

    /**
     * used to compare two double with the comparator. based on:
     * https://stackoverflow.com/questions/4242023/comparator-with-double-type
     * https://beginnersbook.com/2013/12/java-arraylist-of-object-sort-example-comparable-and-comparator/
     */
    @Override
    public int compareTo(MyImage o) {
        //compare with the similarity
        if (this.getSimilarity() < o.getSimilarity()) {
            return -1;
        }

        if (this.getSimilarity() > o.getSimilarity()) {
            return 1;
        }
        return 0;

    }

    @Override
    public String toString() {
        return "File: " + getName() + " | Similarity: " + getSimilarity();
    }

    public MyImageResult getResult() {
        return new MyImageResult(getName(), getSimilarity());
    }
    

//    @Override
//    public void run() {
//        
//        long start = new Date().getTime();
//                    //System.out.println("Thread START" + getName());
//        createComparableImage();
//        calculateSimilarity(refImage);
//        try {
//            Thread.sleep(random.nextInt(500));
//        } catch (InterruptedException ex) {
//            Logger.getLogger(MyImage.class.getName()).log(Level.SEVERE, null, ex);
//        }
//                    long end = new Date().getTime();
//                    long duration = end-start;
//        System.out.println("Thread STOP" + getName() + " " +duration+"ms");
//    }

}
