package k.means;

import java.awt.Color;
import javax.swing.JFrame;
import static k.means.KMeans.centroids;
import static k.means.KMeans.distancesMatrix;
import static k.means.KMeans.membershipMatrix;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Gary
 */
public class ScatterPlot extends JFrame {

    public ScatterPlot(String title, double[][] centroides, double[][] arrayValues, boolean[][] membershipMatrix) {
        super(title);

        // Create dataset  
        XYDataset dataset = createDataset(centroides, arrayValues, membershipMatrix);

        // Create chart  
        JFreeChart chart = ChartFactory.createScatterPlot(
                "K-Means Algorithm",
                "X-Axis", "Y-Axis", dataset);

        //Changes background color  
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(255, 228, 196));

        // Create Panel  
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    private XYDataset createDataset(double[][] centroids, double[][] arrayValues, boolean[][] membershipMatrix) {
        
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (int i = 0; i < centroids.length; i++) {

            XYSeries serie = new XYSeries("centroide" + i);
            
            for (int j = 0; j < arrayValues.length; j++) {

                if (membershipMatrix[i][j] == true) {

                    serie.add(arrayValues[j][0], arrayValues[j][1]);

                }

            }

            dataset.addSeries(serie);

        }

        return dataset;
    }

}
