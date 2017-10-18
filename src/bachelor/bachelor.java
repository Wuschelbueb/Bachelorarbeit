/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor;

import bachelor.util.PictureManipulation;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author David
 */
public class bachelor extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
//        PrintStream fileStream = new PrintStream("test.txt");
//        System.setOut(fileStream);
//        File oFile = new File("C:\\Users\\David\\Desktop\\test\\halbe_bilder\\");
//        PictureManipulation.cropAndSaveImage(oFile, 0, 0, 0, 0);
//        File newImage = new File("C:\\Users\\David\\Desktop\\test\\halbe_bilder\\");
//        List<Integer> ImageToMatrix = PictureManipulation.horizontalMatrix(newImage);
////        for(List<Integer> k: test){
//            Graph chart = new Graph(
//                "Picture",
//                "bah",ImageToMatrix);
//        chart.pack();
//        RefineryUtilities.centerFrameOnScreen(chart);
//        chart.setVisible(true);
////        }
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
        test1.add(6.0);
        test1.add(0.0);
        System.out.println("refMatrix: " + test);
        System.out.println("chaMatrix: " + test1);
        PictureManipulation.alignMatrices(test, test1);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}