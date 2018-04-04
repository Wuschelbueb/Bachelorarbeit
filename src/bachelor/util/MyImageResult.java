/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor.util;

import java.util.Comparator;

/**
 *
 * @author David
 */
public class MyImageResult {
    public String name;
    public Double similarity = null;
    public Exception error;
    
    public MyImageResult(String name, double similarity){
        this.name= name;
        this.similarity = similarity;
    }
    
    public MyImageResult(String name, Exception error){
        this.name= name;
        this.error = error;
    }
    
    public MyImageResult(String name, String error){
        this.name= name;
        this.error = new Exception(error);
    }

    public String getName() {
        return name;
    }

    public Double getSimilarity() {
        return similarity;
    }
    
    @Override
    public String toString() {
        return "File: " + getName() + " | Similarity: " + getSimilarity();
    }
    
    public static class Comparators {
        
        public static Comparator<MyImageResult> SIMILARITY = Comparator.comparing(MyImageResult::getSimilarity);
    }    
}
