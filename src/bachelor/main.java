/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor;

import bachelor.util.DatabaseOperation;
import bachelor.util.DatabaseCreation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author David
 */
public class main extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
//        PrintStream out = new PrintStream(new FileOutputStream("correlation_delta.txt"));
//        System.setOut(out);
        File[] ogFiles = new File("C:\\Users\\David\\Desktop\\test\\ganze_bilder\\").listFiles();
        
        DatabaseCreation referentialPic = new DatabaseCreation(ogFiles[1]);
        referentialPic.createReferentialImage(50, 200, 2249, 150);
        
        List<DatabaseOperation> listOfPictures = new ArrayList<>();
        for (File f : ogFiles) {
            DatabaseCreation transformedPic = new DatabaseCreation(f);
            transformedPic.createComparableImage(50, 250, 2249, 150);
        }
        File[] newFiles = new File("C:\\Users\\David\\Desktop\\test\\halbe_bilder\\").listFiles();
        for (File f : newFiles) {
            DatabaseOperation newPic = new DatabaseOperation(f);
            listOfPictures.add(newPic);
        }
        System.out.println("Vergleich nr. X: [Vertikal, Horizontal]");
        System.out.println(" ");
        for (int i = 0; i < listOfPictures.size(); i++) {
//            results.add(listOfPictures.get(1).compareTo(listOfPictures.get(i)));
            System.out.println("Vergleich nr. " + (i+1) + ": " + listOfPictures.get(1).compareTo(listOfPictures.get(i)));
        }
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
