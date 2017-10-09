/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor;

import bachelor.util.histogramCreation;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;

/**
 *
 * @author David
 */
public class bachelor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        PrintStream fileStream = new PrintStream("test.txt");
        System.setOut(fileStream);
        histogramCreation test = new histogramCreation();
        System.out.println(test.horizontalMatrix());
    }

}
