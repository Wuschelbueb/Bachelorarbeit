/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    private List<MyImageResult> imageList = new ArrayList<>();
    private MyImage refImage = null;
    
    
    public MyApplication() throws FileNotFoundException {

        ogFiles = new File("C:\\Users\\David\\Desktop\\test\\ganze_bilder").listFiles();
        imageList = new ArrayList<>();

        refImage = new MyImage(ogFiles[4]);

    }

    public void start() {
        long start = new Date().getTime();
        refImage.createReferentialImage(X_START, Y_START, X_DISTANCE, Y_DISTANCE);

//        List<Thread> threads = new ArrayList();
        //create images and calculate the similarity
        for (File ogFile : ogFiles) {
            try {
                if (!refImage.getName().equals(ogFile.getName())) {
                    MyImage compImage = new MyImage(ogFile, refImage);
                    compImage.createComparableImage(X_START, Y_START, X_DISTANCE, Y_DISTANCE);
                    compImage.calculateSimilarity(refImage);
                    imageList.add(compImage.getResult());

                    //run in multiple threads
//                    Thread t = new Thread(compImage, compImage.getName());
//                    threads.add(t);
//                    t.start();
                }
            } catch (Exception e) {
                System.out.println("error in " + ogFile.getName());
                imageList.add(new MyImageResult(ogFile.getName(), e));
            }
        }
//sort images similarities
        Collections.sort(imageList);
//output result
        imageList.forEach(System.out::println);
        long end = new Date().getTime();
        long duration = end - start;
        System.out.println("finished after: " + duration / 1000 + "s");
    }
}
