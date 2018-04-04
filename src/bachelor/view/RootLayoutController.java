/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor.view;

import bachelor.Main;
import bachelor.util.MyApplication;
import bachelor.view.ImageOverviewController;
import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;

/**
 *
 * @author David
 */
public class RootLayoutController {
    
    private MyApplication myApp = new MyApplication();
    private Main main;
    private ImageOverviewController imageOverviewController;
    
    /**
     * based on:
     * http://java-buddy.blogspot.ch/2013/03/javafx-simple-example-of.html
     * https://docs.oracle.com/javase/8/javafx/api/javafx/stage/DirectoryChooser.html#getInitialDirectory--
     *
     */
    @FXML
    private void handleOpenDirectory() {
        DirectoryChooser directory = new DirectoryChooser();
        File[] allFiles = main.getDirectory(directory);
        myApp.setFiles(allFiles);
        imageOverviewController.listFiles(allFiles);
    }
}
