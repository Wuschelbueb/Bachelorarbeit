/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor.view;

import bachelor.MainClass;
import bachelor.util.MyImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 *
 * @author David
 */
public class ImageOverviewController {

    private File[] fileList = null;

    @FXML
    private ListView<String> imageList;

    @FXML
    private ImageView imgPane;

    private MainClass main;

    public ImageOverviewController() {

    }

    /**
     * based on:
     * http://www.crazyandcoding.com/blog/post/javafx-selecting-an-item-in-a-listview/
     * https://stackoverflow.com/questions/9722418/how-to-handle-listview-item-clicked-action
     */
    @FXML
    private void displayImage() {
        imageList.setOnMouseClicked((MouseEvent event) -> {
            for (File f : fileList) {
                if (f.getName().equals(imageList.getSelectionModel().getSelectedItem())) {
                    try {
//                        System.out.println(f.getName() + " and " + imageList.getSelectionModel().getSelectedItem());
                        Image showImg = new Image(new FileInputStream(f.getPath()));
                        imgPane.setImage(showImg);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(ImageOverviewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    @FXML
    private void initialize() {
    }

    /**
     * based on: https://docs.oracle.com/javafx/2/ui_controls/list-view.htm
     *
     * @param files
     */
    private void listFiles(File[] files) {
        ObservableList<String> imageList = FXCollections.observableArrayList();
        this.fileList = files;
        for (File f : files) {
            imageList.add(f.getName());

        }
        this.imageList.setItems(imageList);
    }

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
        listFiles(allFiles);
    }

    /**
     * based on:
     * http://o7planning.org/de/11129/die-anleitung-zu-javafx-filechooser-und-directorychooser
     * https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
     */
    @FXML
    private void handleOpenImage() {
        FileChooser file = new FileChooser();
        File refFile = main.getFile(file);
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param main
     */
    public void setMain(MainClass main) {
        this.main = main;
    }

}
