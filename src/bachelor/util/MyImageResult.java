/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor.util;

/**
 *
 * @author David
 */
public class MyImageResult implements Comparable<MyImageResult>{
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
    
    @Override
    public int compareTo(MyImageResult o) {
        if(this.getSimilarity() == null || o.getSimilarity() == null){
            return Integer.MAX_VALUE;
        }
        //compare with the similarity
        if (this.getSimilarity() < o.getSimilarity()) {
            return -1;
        }

        if (this.getSimilarity() > o.getSimilarity()) {
            return 1;
        }
        return 0;

    }
    
    
}
