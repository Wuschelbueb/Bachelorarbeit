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
import javafx.scene.image.Image;

/**
 *
 * @author David
 */
public class MyApplication {

    public static final int X_START = 50;
    public static final int Y_START = 200;
    public static final int X_DISTANCE = 2249;
    public static final int Y_DISTANCE = 150;
    private File[] ogFiles = null;
    private Integer numberOfCores = null;
    private List<MyImageResult> imageList = new ArrayList<>();
    private MyImage refImage = null;

    public MyApplication() {

        try {
            //read from config.properties file the properties
            //based on: https://www.mkyong.com/java/java-properties-file-examples/
            String configFile = "config.properties";
            InputStream configPath = getClass().getClassLoader().getResourceAsStream(configFile);
            Properties prop = new Properties();
            prop.load(configPath);

            //save the properties as a string
            String path = prop.getProperty("path");
            String refImgName = prop.getProperty("refImage");
            numberOfCores = Integer.parseInt(prop.getProperty("cores"));

            //create array of all files in a path
            ogFiles = new File(path).listFiles();
            imageList = new ArrayList<>();

            //choose refImage with properties file
            for (File f : ogFiles) {
                if (f.getName().equals(refImgName)) {
                    refImage = new MyImage(f);
                }
            }
        } catch (IOException e) {
            System.out.println("Can't find path to config file or files");
        }
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
        long start = new Date().getTime();
        refImage.createReferentialImage(X_START, Y_START, X_DISTANCE, Y_DISTANCE);
        //is needed to execute in multithreading
        ExecutorService executor = Executors.newFixedThreadPool(numberOfCores);

        //needed to temporarly save results
        List<Future> futureList = new ArrayList();

        //create images and calculate the similarity
        for (File ogFile : ogFiles) {
            if (!refImage.getName().equals(ogFile.getName())) {
                //directly cerates the call()-method of the Interface Callable with lambdas
                Callable<MyImageResult> callable = () -> {
                    MyImage compImg = new MyImage(ogFile);
                    compImg.createComparableImage(X_START, Y_START, X_DISTANCE, Y_DISTANCE);
                    compImg.calculateSimilarity(refImage);
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
        Collections.sort(imageList);
        //output result
        imageList.forEach(System.out::println);
        long end = new Date().getTime();
        long duration = end - start;
        System.out.println("finished after: " + duration / 1000 + "s");
    }
}
