/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor;

import bachelor.util.MyPicture;
import java.io.File;
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
//        MyPicture.cropAndSaveImage(oFile, 0, 0, 0, 0);
//        File newImage = new File("C:\\Users\\David\\Desktop\\test\\halbe_bilder\\");
//        List<Integer> ImageToMatrix = MyPicture.getHorizontalMatrix(newImage);
////        for(List<Integer> k: test){
//            Graph chart = new Graph(
//                "Picture",
//                "bah",ImageToMatrix);
//        chart.pack();
//        RefineryUtilities.centerFrameOnScreen(chart);
//        chart.setVisible(true);
////        }
//        List<Double> test = new ArrayList<>();
//        test.add(0.0);
//        test.add(2.0);
//        test.add(8.0);
//        test.add(5.0);
//        test.add(3.0);
//        test.add(0.0);
//        List<Double> test1 = new ArrayList<>();
//        test1.add(0.0);
//        test1.add(2.0);
//        test1.add(8.0);
//        test1.add(3.0);
//        test1.add(6.0);
//        test1.add(0.0);
//        System.out.println("refMatrix: " + test);
//        System.out.println("chaMatrix: " + test1);
        File[] ogFiles = new File("C:\\Users\\David\\Desktop\\test\\ganze_bilder\\").listFiles();

        List<MyPicture> listOfPictures = new ArrayList<MyPicture>();
        for (File f : ogFiles) {
            MyPicture newPicture = new MyPicture(f);
            newPicture.createCroppedImage(0, 200, 2599, 150);
//            System.out.println("blub");
        }
        File[] newFiles = new File("C:\\Users\\David\\Desktop\\test\\halbe_bilder\\").listFiles();
        for (File f : newFiles) {
            MyPicture newPicture = new MyPicture(f);
            listOfPictures.add(newPicture);
        }
//        for (int i = 0; i < listOfPictures.size(); i++) {
//            System.out.println("Vergleich nr. " + i + ": " + listOfPictures.get(1).compareTo(listOfPictures.get(3)));
//        }
        System.out.println("Vergleich nr. : " + listOfPictures.get(1).compareTo(listOfPictures.get(3)));

//    MyPicture newP = new MyPicture(files[1]);
//    List<Double> test = newP.getHorizontalMatrix();
    }

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

    @Override
    public void start(Stage primaryStage) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
