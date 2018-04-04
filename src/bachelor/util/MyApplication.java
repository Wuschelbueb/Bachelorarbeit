/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

/**
 *
 * @author David
 */
public class MyApplication {

    private Integer xStart = null;
    private Integer yStart = null;
    private Integer width = null;
    private Integer height = null;
    private File[] ogFiles = null;
    private final Integer numberOfCores = 8;
    private List<MyImageResult> imageList = new ArrayList<>();
    private MyImage refImage = null;
    private String refImgName = null;

    public void setRefImage(String refImgName) {
        this.refImgName = refImgName;
    }

    //sets the distances needed to crop the ref Image
    public void setImageParameters(int xStart, int yStart,
            int width, int height) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.width = width;
        this.height = height;
    }

    public void setFiles(File[] files) {
        this.ogFiles = files;
    }

    public MyApplication() {
    }

    /**
     * start takes all files, converts them to myImages and calls the
     * calculateSmilarity. all this is based on multithreading.
     *
     * based on:
     * http://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/
     * http://winterbe.com/posts/2014/03/16/java-8-tutorial/
     * https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
     * https://blogs.oracle.com/corejavatechtips/using-callable-to-return-results-from-runnables
     * https://stackoverflow.com/questions/38924444/executorservice-for-do-while-loop-in-java
     *
     */
    public void start() {

        //create array of all files in a path
        try {
            imageList = new ArrayList<>();

            //choose refImage with properties file
            for (File f : ogFiles) {
                if (f.getName().equals(refImgName)) {
                    refImage = new MyImage(f);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        long start = new Date().getTime();
        refImage.createRefImageBW(xStart, yStart, width, height);
        //is needed to execute in multithreading
        ExecutorService executor = Executors.newFixedThreadPool(numberOfCores);
        //needed to temporarly save results
        List<Future> futureList = new ArrayList();

        System.out.println("Referential Image: " + refImage.getName());
        //create images and calculate the similarity
        for (File ogFile : ogFiles) {
            if (!refImage.getName().equals(ogFile.getName())) {
                //directly cerates the call()-method of the Interface Callable with lambdas
                Callable<MyImageResult> callable = () -> {
                    MyImage compImg = new MyImage(ogFile);
                    compImg.createCompImgBW(xStart, yStart, width, height);
                    compImg.calculateSimilarityBW(refImage);
                    return compImg.getResult();
                };

                Future<MyImageResult> future = executor.submit(callable);
                futureList.add(future);
            }
        }
        //stops the executorService
        executor.shutdown();

        //takes the futureList with the results and adds them to the imagelist
        for (Future e : futureList) {
            try {
                imageList.add((MyImageResult) e.get());
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(MyApplication.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        //sort images from imageList based on the similarity
        Collections.sort(imageList, Collections.reverseOrder(MyImageResult.Comparators.SIMILARITY));
        //output result
        imageList.forEach(System.out::println);
        long end = new Date().getTime();
        long duration = end - start;
        System.out.println("finished after: " + duration / 1000 + "s");
        
        //based on: http://code.makery.ch/blog/javafx-dialogs-official/
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Finished!");
        alert.setHeaderText("Calculations are complete!");
        alert.setContentText("finished after: " + duration / 1000 + "s");
        alert.showAndWait();
    }
    
    public List<MyImageResult> getResults(){
        return imageList;
    }
}
