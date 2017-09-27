/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor.util;

import java.io.File;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 *
 * @author David
 */
public class directory extends Application {

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        DirectoryChooser whole = new DirectoryChooser();
        whole.setTitle("Whole Pictures");
        File defaultDirectory = new File("C:\\Users\\David\\Desktop\\test\\ganze_bilder\\");
        whole.setInitialDirectory(defaultDirectory);
    }

}
