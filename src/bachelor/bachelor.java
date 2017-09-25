/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author David
 */
public class bachelor extends Application {

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        
        /**
         * Binärisierung des Bildes. Funktioniertgut mit 0x666666
         */

        //Creating an image 
        Image image = new Image(new FileInputStream("C:\\Users\\David\\Google Drive\\Unifr\\3. Jahr\\Bachelorarbeit\\Titelbilder\\slz-002_1896_041_0005.jpg"));
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        //Creating a writable image 
        WritableImage wImage = new WritableImage(width, height);

        //Reading color from the loaded image 
        PixelReader pixelReader = image.getPixelReader();

        //getting the pixel writer 
        PixelWriter writer = wImage.getPixelWriter();

        //threshold farbe, entscheidet ob pixel schwarz oder weiss werden soll
        Color threshold = Color.rgb(102, 102, 102);

        //geht jedes pixel im Bild durch und vergleicht es mit dem threshold (binärisierung)
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = pixelReader.getColor(x, y);
                if (color.getBlue() > threshold.getBlue() && color.getRed() > threshold.getRed() && color.getGreen() > threshold.getGreen()) {
                    writer.setColor(x, y, Color.WHITE);
                } else {
                    writer.setColor(x, y, Color.BLACK);
                }
            }
        }
        /**
         * bestimmt die Grösse des Bildes. Ratio des Bildes wird erhalten
         */
        ImageView imageView = new ImageView(wImage);
        imageView.setFitHeight(800);
        imageView.setPreserveRatio(true);

        //Creating a Group object  
        Group root = new Group(imageView);

        //Creating a scene object 
        Scene scene = new Scene(root, 500, 500);

        //Setting title to the Stage 
        stage.setTitle("Writing pixels ");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();

//        Color test = image.getPixelReader().getColor(x, y);
//        System.out.println(test);
    }
    //    @Override
    //    public void start(Stage primaryStage) {
    //        Button btn = new Button();
    //        btn.setText("Say 'Hello World'");
    //        btn.setOnAction(new EventHandler<ActionEvent>() {
    //            
    //            @Override
    //            public void handle(ActionEvent event) {
    //                System.out.println("Hello World!");
    //            }
    //        });
    //        
    //        StackPane root = new StackPane();
    //        root.getChildren().add(btn);
    //        
    //        Scene scene = new Scene(root, 300, 250);
    //        
    //        primaryStage.setTitle("Hello World!");
    //        primaryStage.setScene(scene);
    //        primaryStage.show();
    //    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }

}
