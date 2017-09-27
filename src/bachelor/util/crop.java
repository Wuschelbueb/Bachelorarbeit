/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author David
 */
public class crop extends Application {

    @Override
    public void start(Stage stage) throws FileNotFoundException {

        //speichert system.out.println in einem test.txt file
        PrintStream fileStream = new PrintStream("test.txt");
        System.setOut(fileStream);

        /**
         * Binärisierung des Bildes. Funktioniertgut mit 0x666666 Bild wird
         * zudem direkt zugeschnitten (manuell)
         */

        //Creating an image 
        Image image = new Image(new FileInputStream("C:\\Users\\David\\Desktop\\test\\ganze_bilder\\slz-002_1896_041_0005.jpg"));

        //Reading color from the loaded image 
        PixelReader pixelReader = image.getPixelReader();

        //y postition, welche das bild anfangen soll
        int y_start = 200;

        //distanz die es von der anfangsposition machen soll
        int y_distanz = 150;

        //schneidet Bild zu
        WritableImage croppedImage = new WritableImage(pixelReader, 0, y_start, (int) image.getWidth(), y_distanz);

        //This method returns a PixelWriter that provides access to write the pixels of the image.
        PixelWriter writer = croppedImage.getPixelWriter();

        //threshold farbe, entscheidet ob pixel schwarz oder weiss werden soll
        Color threshold = Color.rgb(102, 102, 102);

        //geht jedes pixel im  zugeschnittenen Bild durch und vergleicht es mit dem threshold (binärisierung)
        for (int y = 0; y < y_distanz; y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                // hier muss i hinzugefügt werden, dass die farbe am richtigen Ort entnommen werden kann
                Color color = pixelReader.getColor(x, y + y_start);
                if (color.getBlue() > threshold.getBlue() && color.getRed() > threshold.getRed() && color.getGreen() > threshold.getGreen()) {
                    writer.setColor(x, y, Color.WHITE);
                } else {
                    writer.setColor(x, y, Color.BLACK);
                }
            }
        }

        /**
         * zählt die schwarzen pixel auf dem bildausschnitt
         *
         * @numberOfBlackPixelsPerRow wird später benötigt um verschiedene
         * bildausschnitte miteinander zu vergleichen
         */
        int counter = 0;
        int[] numberOfBlackPixelsPerRow = new int[y_distanz];
        for (int y = 0; y < croppedImage.getHeight(); y++) {
            for (int x = 0; x < croppedImage.getWidth(); x++) {
                Color croppedImagePixelColor = croppedImage.getPixelReader().getColor(x, y);
                if (croppedImagePixelColor.equals(Color.BLACK)) {
                    counter++;
                }
            }
            numberOfBlackPixelsPerRow[y] = counter;
            counter = 0;
        }

        /**
         * bestimmt die Grösse des Bildes. Ratio des Bildes wird erhalten
         */
        ImageView imageView = new ImageView(croppedImage);
        imageView.setFitWidth(1000);
        imageView.setPreserveRatio(true);

        //Creating a Group object  
        Group root = new Group(imageView);

        //Creating a scene object 
        Scene scene = new Scene(root);

        //Setting title to the Stage (Fenster) 
        stage.setTitle("Cropped Image");

        //Adding scene to the stage 
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();

        //speicher bildausschnitt in neuem File
        File file = new File("C:\\Users\\David\\Desktop\\test\\test.png");

//        try {
//            ImageIO.write(SwingFXUtils.fromFXImage(croppedImage, null), "png", file);
//        } catch (Exception s) {
//        }
    }

}
