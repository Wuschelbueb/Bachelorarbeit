/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author David
 */
public class DatabaseOperation {

    private File ogFile = null;
    private Image transformedImage = null;
    private List<Double> horizontalMatrix = null;
    private List<Double> verticalMatrix = null;

    /**
     * creates an image (with javaFX) of the choosen file.
     *
     * @param file
     * @throws FileNotFoundException
     */
    public DatabaseOperation(File file) throws FileNotFoundException {
        this.ogFile = file;
        this.transformedImage = new Image(new FileInputStream(getURL()));
    }

    private String getURL() {
        return ogFile.getAbsolutePath();
    }

    /**
     * creates the horizontal matrix of the new Image.
     *
     */
    public void calculateVerticalMatrix() {
        List<Double> tempHorzMatrix = new ArrayList<>();
        double counter = 0;
        int heightOfImage = (int) transformedImage.getHeight();
        int widthOfImage = (int) transformedImage.getWidth();
        for (int y = 0; y < heightOfImage; y++) {
            for (int x = 0; x < widthOfImage; x++) {
                Color croppedImagePixelColor = transformedImage.getPixelReader().getColor(x, y);
                if (croppedImagePixelColor.equals(Color.BLACK)) {
                    counter++;
                }
            }
            tempHorzMatrix.add(counter);
            counter = 0;
        }
        horizontalMatrix = tempHorzMatrix;
    }

    public List<Double> getVerticalMatrix() {
        if (horizontalMatrix == null) {
            calculateVerticalMatrix();
        }
        return horizontalMatrix;
    }

    /**
     * creates the vertical matrix of the new Image.
     *
     */
    public void calculateHorizontalMatrix() {
        List<Double> tempVertMatrix = new ArrayList<>();
        double counter = 0;
        int heightOfImage = (int) transformedImage.getHeight();
        int widthOfImage = (int) transformedImage.getWidth();
        for (int y = 0; y < widthOfImage; y++) {
            for (int x = 0; x < heightOfImage; x++) {
                Color croppedImagePixelColor = transformedImage.getPixelReader().getColor(y, x);
                if (croppedImagePixelColor.equals(Color.BLACK)) {
                    counter++;
                }
            }
            tempVertMatrix.add(counter);
            counter = 0;
        }
        verticalMatrix = tempVertMatrix;
    }

    public List<Double> getHorzinontalMatrix() {
        if (verticalMatrix == null) {
            calculateHorizontalMatrix();
        }
        return verticalMatrix;
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

    public List<String> compareTo(DatabaseOperation theOtherPicture) throws FileNotFoundException {
        double vert = compare(getVerticalMatrix(), theOtherPicture.getVerticalMatrix());
        double horz = compare(getHorzinontalMatrix(), theOtherPicture.getHorzinontalMatrix());
        List<String> totalSimilarity = new ArrayList<>();
//        totalSimilarity.add(vert);
//        totalSimilarity.add(horz);
        double test = (vert+horz)/2;
        String tse = Double.toString(test);
        totalSimilarity.add(tse);
        totalSimilarity.add(theOtherPicture.getURL());
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
     * @return
     */
    public static double compare(List<Double> refMatrix, List<Double> changingMatrix) {
        int maxMoves = changingMatrix.size() - refMatrix.size();
        List<Double> normRefMatrix = normalizeMatrix(refMatrix);
        //10.0 für chi square und 0.0 für correlation
        double finalResult = 0.0;
        for (int i = 0; i <= maxMoves; i++) {
            List<Double> newChangingMatrix = new ArrayList<>();
            for (int j = i; j < refMatrix.size() + i; j++) {
                newChangingMatrix.add(changingMatrix.get(j));
            }
            List<Double> normChaMatrix = normalizeMatrix(newChangingMatrix);
            double tempResult = compareCorrelation(normRefMatrix, normChaMatrix);
            // >= für correlation <= für chi square methode
            if (tempResult >= finalResult) {
                finalResult = tempResult;
            }
        }
        return finalResult;
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
    private static double compareCorrelation(List<Double> refMatrix, List<Double> changingMatrix) {
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
     * chi square algorithm. needed to compare to matrices with each other.
     *
     * @param refMatrix
     * @param changingMatrix
     * @return
     */
    private static double chiSquareComparison(List<Double> refMatrix, List<Double> changingMatrix) {
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
