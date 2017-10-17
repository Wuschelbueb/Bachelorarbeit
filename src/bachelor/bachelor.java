/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor;

import bachelor.util.Graph;
import bachelor.util.PictureManipulation;
import bachelor.util.histogramCreation;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author David
 */
public class bachelor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
//        PrintStream fileStream = new PrintStream("test.txt");
//        System.setOut(fileStream);
//        histogramCreation test = new histogramCreation();
//        for(List<Integer> k: test.horizontalMatrix()){
//            Graph chart = new Graph(
//                "Picture",
//                "bah",k);
//        chart.pack();
//        RefineryUtilities.centerFrameOnScreen(chart);
//        chart.setVisible(true);
//        }
        List<Double> test = new ArrayList<>();
        test.add(0.0);
        test.add(2.0);
        test.add(8.0);
        test.add(5.0);
        test.add(3.0);
        test.add(0.0);
        List<Double> test1 = new ArrayList<>();
        test1.add(0.0);
        test1.add(2.0);
        test1.add(8.0);
        test1.add(3.0);
        test1.add(5.0);
        test1.add(0.0);
        System.out.println("refMatrix: "+test);
        System.out.println("chaMatrix: "+test1);
        PictureManipulation.alignMatrices(test, test1);

    }
}
