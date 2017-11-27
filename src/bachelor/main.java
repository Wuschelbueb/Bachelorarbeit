/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor;

import bachelor.util.MyApplication;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
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

//    private List<WrapperImgSimName> imageList = null;

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        PrintStream out = new PrintStream(new FileOutputStream("test.txt"));
        System.setOut(out);
        MyApplication myApplication = new MyApplication();
        myApplication.start();
//        launch(args);
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
