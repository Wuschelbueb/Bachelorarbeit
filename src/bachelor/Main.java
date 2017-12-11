/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor;

import bachelor.view.ImageOverviewController;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 *
 * @author David
 */
public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

//    private List<WrapperImgSimName> imageList = null;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            PrintStream out = new PrintStream(new FileOutputStream("test.txt"));
            System.setOut(out);
        } catch (IOException e) {
            System.out.println("Can't create text!");
        }
//
//        MyApplication myApplication = new MyApplication();
//        myApplication.start();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Historical Documents Comparison");

        initRootLayout();

        showImageOverview();
    }

    /**
     * Initializes the root layout.
     * based on: http://code.makery.ch/library/javafx-8-tutorial/part1/
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     * based on: http://code.makery.ch/library/javafx-8-tutorial/part1/
     */
    public void showImageOverview() {
        try {
            // Load image overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ImageOverview.fxml"));
            AnchorPane imageOverview = (AnchorPane) loader.load();
            
            // Set image overview into the center of root layout.
            rootLayout.setCenter(imageOverview);

            ImageOverviewController controller = loader.getController();
            controller.setMain(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the Main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public File[] getDirectory(DirectoryChooser directory) {
        File[] allFiles = null;
        try {
            File selectedDirectory = directory.showDialog(primaryStage);
            allFiles = new File(selectedDirectory.getAbsolutePath()).listFiles();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("No directory selected!");
            alert.setContentText("No directory is selected. Please select one.");
            alert.showAndWait();
        }
        return allFiles;
    }

//    @Override
//    public void start(Stage stage) throws Exception {
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
//        throw new UnsupportedOperationException(
//                "Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}
