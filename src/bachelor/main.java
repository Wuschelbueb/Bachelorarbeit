/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor;

import bachelor.util.DatabaseOperation;
import bachelor.util.DatabaseCreation;
import bachelor.util.WrapperImageName;
import bachelor.util.WrapperSimilarityName;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author David
 */
public class main extends Application {

    private List<WrapperImageName> imageList = null;

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
//        PrintStream out = new PrintStream(new FileOutputStream("memoryoutput2.txt"));
//        System.setOut(out);

        File[] ogFiles = new File("C:\\Users\\wusch\\Desktop\\test\\ganze_bilder").listFiles();
        List<DatabaseOperation> listOfPictures = new ArrayList<>();
        List<WrapperImageName> imageList = new ArrayList<>();
        List<WrapperSimilarityName> listWithResults = new ArrayList<>();

        DatabaseCreation referentialPic = new DatabaseCreation(ogFiles[4]);
        imageList.add(referentialPic.createReferentialImage(50, 200, 2249, 150));

        for (File f : ogFiles) {
            if (!imageList.get(0).getName().equals(f.getName())) {
                DatabaseCreation transformedPic = new DatabaseCreation(f);
                imageList.add(transformedPic.createComparableImage(50, 200, 2249, 150));
//                System.out.println(f.getName() + "  ||  Total Memory: "
//                        + (Runtime.getRuntime().totalMemory() / 1024) + " MB  ||  Free Memory: "
//                        + (Runtime.getRuntime().freeMemory() / 1024) + " MB  ||  Used Memory: "
//                        + ((Runtime.getRuntime().totalMemory()
//                        - Runtime.getRuntime().freeMemory()) / 1024) + "MB");
            }
        }

        for (WrapperImageName img : imageList) {
            DatabaseOperation newPicture = new DatabaseOperation(img);
            listOfPictures.add(newPicture);
        }
        for (int i = 0; i < listOfPictures.size(); i++) {
            listWithResults.add(listOfPictures.get(0).compareTo(listOfPictures.get(i)));
        }

        //sorts and prints the values.
        Collections.sort(listWithResults, WrapperSimilarityName.simResultsComparison);
        for (WrapperSimilarityName str : listWithResults) {
            System.out.println(str);
        }

        System.out.println("hoi");
//        launch(args);
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
    public void start(Stage stage) throws Exception {
//        ImageView test = new ImageView(imageList.get(0).getImage());
//        test.setFitWidth(1000);
//        test.setPreserveRatio(true);
//        Group root = new Group(test);
//        Scene scene = new Scene(root);
//        scene.setFill(Color.BLACK);
//        HBox box = new HBox();
//        root.getChildren().add(box);
//
//        stage.setTitle("ImageView");
//        stage.setScene(scene);
//        stage.sizeToScene();
//        stage.show();

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
