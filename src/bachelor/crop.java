/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 *
 * @author David
 */
public class crop extends Application {

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        Image image = new Image(new FileInputStream("C:\\Users\\David\\Google Drive\\Unifr\\3. Jahr\\Bachelorarbeit\\Titelbilder\\slz-002_1896_041_0005.jpg"));

        PixelReader reader = image.getPixelReader();
        WritableImage croppedImage = new WritableImage(reader, 0, 0, (int) image.getWidth(), 500);

        ImageView imageView = new ImageView(croppedImage);

        Group root = new Group(imageView);
        Scene scene = new Scene(root);
        stage.setTitle("Cropped");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();

    }

}
