/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor;

import bachelor.util.Graph;
import bachelor.util.histogramCreation;
import java.io.FileNotFoundException;
import java.io.PrintStream;
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
        histogramCreation test = new histogramCreation();
        for(List<Integer> k: test.horizontalMatrix()){
            Graph chart = new Graph(
                "Picture",
                ""+test,k);
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
        }
        
    }
}
