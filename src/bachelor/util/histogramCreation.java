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
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author David
 */
public class histogramCreation {

    File[] files = new File("C:\\Users\\David\\Desktop\\test\\halbe_bilder\\").listFiles();
    ArrayList<String> halbeBilder = directory.showFiles(files);

    public List<List<Integer>> horizontalMatrix() throws FileNotFoundException {

        List<List<Integer>> ulitmativerArray = new ArrayList<List<Integer>>();

        for (int i = 0; i < halbeBilder.size(); i++) {

            Image croppedImage = new Image(new FileInputStream("C:\\Users\\David\\Desktop\\test\\halbe_bilder\\" + halbeBilder.get(i)));

            /**
             * zählt die schwarzen pixel auf dem bildausschnitt
             *
             * @numberOfBlackPixelsPerRow wird später benötigt um verschiedene
             * bildausschnitte miteinander zu vergleichen
             */
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
            ulitmativerArray.add(resultsOfAllHistograms);
        }
        return ulitmativerArray;
    }

    public static int[] verticalMatrix() {
//        System.out.println("test");
        return new int[3];
    }

}
