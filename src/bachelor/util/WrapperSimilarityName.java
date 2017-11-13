/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor.util;

import java.util.Comparator;

/**
 * creates a class which holds the similarity value and the name of the picture.
 *
 * @author David
 */
public class WrapperSimilarityName {

    private Double similarityValue = null;
    private String fileName = null;

    public Double getSimilarityValue() {
        return similarityValue;
    }

    public void setSimilarityValue(Double similarityResults) {
        this.similarityValue = similarityResults;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    /**
     * used to compare two double with the comparator. based on:
     * https://stackoverflow.com/questions/4242023/comparator-with-double-type
     * https://beginnersbook.com/2013/12/java-arraylist-of-object-sort-example-comparable-and-comparator/
     */
    public static Comparator<WrapperSimilarityName> simResultsComparison = new Comparator<WrapperSimilarityName>() {

        @Override
        public int compare(WrapperSimilarityName l1, WrapperSimilarityName l2) {
            if (l1.getSimilarityValue() < l2.getSimilarityValue()) {
                return -1;
            }
            if (l1.getSimilarityValue() > l2.getSimilarityValue()) {
                return 1;
            }
            return 0;
        }
    };

    //if you print the whole class, that what gets printed.
    @Override
    public String toString() {
        return "File: " + fileName + " | Similarity: " + similarityValue;
    }

}
