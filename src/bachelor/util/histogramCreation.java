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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author David
 */
public class histogramCreation {

    //kreiert eine ArrayList mit allen Files im angegebenen Verzeichnis
    File[] files = new File("C:\\Users\\David\\Desktop\\test\\halbe_bilder\\").listFiles();
    ArrayList<String> croppedPictures = directory.showFiles(files);

    /**
     * Zählt die Pixel des ausgeschnittenen Bildes. Generiert eine ArrayList,
     * welche man zu Erstellung eines horizontalen Histograms benötigt.
     *
     * @return
     * @throws FileNotFoundException
     */
    public List<List<Integer>> horizontalMatrix() throws FileNotFoundException {

        List<List<Integer>> AllHorizontalHistograms = new ArrayList<List<Integer>>();
        

        for (String i : croppedPictures) {

            Image croppedImage = new Image(new FileInputStream("C:\\Users\\David\\Desktop\\test\\halbe_bilder\\" + i));
            int counter = 0;
            int heightOfImage = (int) croppedImage.getHeight();
            int widthOfImage = (int) croppedImage.getWidth();            
            List<Integer> resultsOfAllHistograms = new ArrayList<>();

            for (int y = 0; y < heightOfImage; y++) {
                for (int x = 0; x < widthOfImage; x++) {
                    Color croppedImagePixelColor = croppedImage.getPixelReader().getColor(x, y);
                    if (croppedImagePixelColor.equals(Color.BLACK)) {
                        counter++;
                    }
                }
                resultsOfAllHistograms.add(counter);
                counter = 0;
            }
            AllHorizontalHistograms.add(resultsOfAllHistograms);
        }
        return AllHorizontalHistograms;
    }

    /**
     * Zählt die Pixel des ausgeschnittenen Bildes. Generiert eine ArrayList,
     * welche man zu Erstellung eines vertikalen Histograms benötigt. Ist das
     * normale Histogram.
     *
     * @return
     * @throws FileNotFoundException
     */
    public List<List<Integer>> verticalMatrix() throws FileNotFoundException {

        List<List<Integer>> AllVerticalHistograms = new ArrayList<List<Integer>>();

        for (String i : croppedPictures) {

            Image croppedImage = new Image(new FileInputStream("C:\\Users\\David\\Desktop\\test\\halbe_bilder\\" + i));
            int counter = 0;
            int heightOfImage = (int) croppedImage.getHeight();
            int widthOfImage = (int) croppedImage.getWidth();
            List<Integer> resultsOfAllHistograms = new ArrayList<>();

            for (int y = 0; y < widthOfImage; y++) {
                for (int x = 0; x < heightOfImage; x++) {
                    Color croppedImagePixelColor = croppedImage.getPixelReader().getColor(x, y);
                    if (croppedImagePixelColor.equals(Color.BLACK)) {
                        counter++;
                    }
                }
                resultsOfAllHistograms.add(counter);
                counter = 0;
            }
            AllVerticalHistograms.add(resultsOfAllHistograms);
        }
        return AllVerticalHistograms;
    }
}
