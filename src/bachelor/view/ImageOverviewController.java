/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor.view;

import bachelor.MainClass;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import sun.plugin.javascript.navig.Anchor;

/**
 *
 * @author David
 */
public class ImageOverviewController {

    private File[] fileList = null;

    @FXML
    private ListView<String> imageList;

    @FXML
    private ImageView imageView;

    @FXML
    private Group imageLayer;
    
    @FXML
    private AnchorPane paneRight;
    
    ScrollPane scrollPane;

    private MainClass main;
    private RubberBandSelection rubberBandSelection;

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
                        Image image = new Image(new FileInputStream(f.getPath()));
                        imageView.setImage(image);
                        imageView.fitWidthProperty().bind(paneRight.widthProperty());

                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(ImageOverviewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        System.out.println("test");
        
        imageLayer.getChildren().add(imageView);
//        scrollPane.setContent(imageLayer);
        rubberBandSelection = new RubberBandSelection(imageLayer);
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

    @FXML
    private void handleCropImage() {
        Bounds selectionBounds = rubberBandSelection.getBounds();
        System.out.println("selected area: " + selectionBounds);
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

    private void crop(Bounds bounds) {

    }

    /**
     * Drag rectangle with mouse cursor in order to get selection bounds based
     * on:
     * https://stackoverflow.com/questions/30993681/how-to-make-a-javafx-image-crop-app
     *
     */
    private static class RubberBandSelection {

        final DragContext dragContext = new DragContext();
        Rectangle rect = new Rectangle();

        Group group;

        public Bounds getBounds() {
            return rect.getBoundsInParent();
        }

        public RubberBandSelection(Group group) {
            this.group = group;

            rect = new Rectangle(0, 0, 0, 0);
            rect.setStroke(Color.BLUE);
            rect.setStrokeWidth(1);
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

            group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        }

        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if (event.isSecondaryButtonDown()) {
                    return;
                }

                // remove old rect
                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().remove(rect);

                // prepare new drag operation
                dragContext.mouseAnchorX = event.getX();
                dragContext.mouseAnchorY = event.getY();

                rect.setX(dragContext.mouseAnchorX);
                rect.setY(dragContext.mouseAnchorY);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().add(rect);

            }
        };

        EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if (event.isSecondaryButtonDown()) {
                    return;
                }

                double offsetX = event.getX() - dragContext.mouseAnchorX;
                double offsetY = event.getY() - dragContext.mouseAnchorY;

                if (offsetX > 0) {
                    rect.setWidth(offsetX);
                } else {
                    rect.setX(event.getX());
                    rect.setWidth(dragContext.mouseAnchorX - rect.getX());
                }

                if (offsetY > 0) {
                    rect.setHeight(offsetY);
                } else {
                    rect.setY(event.getY());
                    rect.setHeight(dragContext.mouseAnchorY - rect.getY());
                }
            }
        };

        EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if (event.isSecondaryButtonDown()) {
                    return;
                }
            }
        };

        private static final class DragContext {

            public double mouseAnchorX;
            public double mouseAnchorY;

        }
    }
}
