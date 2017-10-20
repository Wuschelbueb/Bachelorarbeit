/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor.util;

import java.io.FileNotFoundException;
import java.util.List;
import javafx.scene.chart.NumberAxis;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.ApplicationFrame;

/**
 *
 * @author David
 */
public class Graph extends ApplicationFrame {

    // double/int durch number (parent) ersetzt, ungünstig -> suche alternativen
    public Graph(String applicationTitle, String chartTitle, List<Number> histogramType) throws FileNotFoundException {
        super(applicationTitle);
//        for(List<Integer> k : histogramType){
            JFreeChart barChart = ChartFactory.createBarChart(
                chartTitle,
                "Row", "Number of Black Pixels",
                createDataset(histogramType),
                PlotOrientation.HORIZONTAL,
                true, true, false);
        
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        chartPanel.restoreAutoRangeBounds();
        setContentPane(chartPanel);
//        }
        
    }

    private DefaultCategoryDataset createDataset(List<Number> Matrix) throws FileNotFoundException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int j = 1;
        for (Number i : Matrix) {
            dataset.setValue(i, "Amount of Black Pixels", "" + j);
            j++;
        }
        return dataset;
    }
}
