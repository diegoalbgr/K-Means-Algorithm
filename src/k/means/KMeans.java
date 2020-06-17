package k.means;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import javax.swing.WindowConstants;

/**
 *
 * @author Gary
 */
public class KMeans {

    //Data universe (Salinity, PH) samples in water.
    
    static double[][] arrayValues = {{2, 2}, {4, 2}, {6, 5}, {8, 9}, {2, 4}, {6, 12}, {3, 2}, {1, 3}, {2, 2}, {4, 5},
        {1, 2}, {4, 9}, {2, 5}, {7, 5}, {3, 10}, {2, 10}, {10, 5}, {1, 2}, {2, 9}, {1, 3}, {1, 6}, {3, 5}, {3, 8}, {4, 3}, {2, 1},
        {4, 2}, {2, 9}, {7, 5}, {5, 5}, {4, 1}, {4, 4}, {2, 5}, {3, 2}, {8, 9}, {7, 3}, {6, 6}, {7, 5}, {8, 8}, {6, 3}, {2, 1}};

    //Centroids array
    
    static double[][] centroids;

    //Distances array
    
    static double[][] distancesMatrix;

    //Membership array
    
    static boolean[][] membershipMatrix;

    //Membership array
    
    static boolean[][] membershipMatrixTemp;
    
    //Centroids counter array
    
    static int cCounter = 0;

    public static void main(String[] args) {

        double maxXValue = GetXMaxValue();

        double maxYValue = GetYMaxValue();

        Scanner sc = new Scanner(System.in);
        
        System.out.println("How many centroids you want to manage?");

        cCounter = sc.nextInt();

        centroids = new double[cCounter][2];

        //Set all centroids values to random
        
        for (int i = 0; i < centroids.length; i++) {

            for (int j = 0; j < 2; j++) {

                centroids[i][j] = ((j == 0) ? (int) (Math.random() * maxXValue) + 1 : (int) (Math.random() * maxYValue) + 1);

            }

        }

        //Set length of distancesMatrix, membershipMatrix & membershipMatrixTemp.
        
        distancesMatrix = new double[centroids.length][arrayValues.length];

        membershipMatrix = new boolean[centroids.length][arrayValues.length];

        membershipMatrixTemp = new boolean[centroids.length][arrayValues.length];

        //Print initial centroids 
        
        PrintCentroids();

        //While true -> membershipmatrixTemp != membershipMatrix
        
        while(true){
            
            //Set distances matrix
            SetDistancesMatrixValues();

            
            //Set membership matrix values by distances matrix value
            SetMembershipMatrixValues();
            
            if(VerifyMembershipMatrixValues() == false){
            
                SetNewCentroids();
            
                membershipMatrixTemp = membershipMatrix;
                
            }else{
                
                //If VerifyMembershipMatrixValues() returns true (It means that membershipMatrixTemp != membershipMatrix)
                
                break;
                
            }
            
        }

        //Print finalMembershipMatrixValues
        
        PrintMembershipMatrixValues();
        
        //Print final centroids values
        
        PrintCentroids();
        
        //Graph K-Means result in scatterPlot --> JFreeChart
        
        ScatterPlot example = new ScatterPlot("K-Means Algorithm result", centroids, arrayValues, membershipMatrix);  
        example.setSize(800, 400);  
        example.setLocationRelativeTo(null);  
        example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  
        example.setVisible(true);  
        
    }

    //Function that returns the X max value of data universe.
    
    public static double GetXMaxValue() {

        double maxXValue = Integer.MIN_VALUE;

        for (int i = 0; i < arrayValues.length; i++) {

            maxXValue = ((arrayValues[i][0]) > maxXValue) ? arrayValues[i][0] : maxXValue;

        }

        return maxXValue;

    }

    //Function that returns the y max value of data universe.
    
    public static double GetYMaxValue() {

        double maxYValue = Integer.MIN_VALUE;

        for (int i = 0; i < arrayValues.length; i++) {

            maxYValue = ((arrayValues[i][1]) > maxYValue) ? arrayValues[i][0] : maxYValue;

        }

        return maxYValue;

    }

    /*---------------------- PRINT CENTROIDS -----------------------*/
    public static void PrintCentroids() {

        for (int i = 0; i < centroids.length; i++) {

            System.out.print("\n C" + i + " = (");

            for (int j = 0; j < 2; j++) {

                if (j == 0) {
                    System.out.print(centroids[i][j]);
                } else {
                    System.out.print(", " + centroids[i][j]);
                }

            }

            System.out.println(")");

        }

    }

    /*---------------------- PRINT DISTANCES -----------------------*/
    public static void PrintDistancesMatrix() {

        for (int i = 0; i < centroids.length; i++) {

            System.out.println("Centroide: " + i);

            for (int j = 0; j < arrayValues.length; j++) {

                System.out.println("(" + i + ", " + j + ")");

                System.out.println(distancesMatrix[i][j]);

            }

        }

    }

    /*---------------------- SET DISTANCES MATRIX VALUES -----------------------*/
    
    public static void SetDistancesMatrixValues(){
        
        double distance = 0.00;

        for (int i = 0; i < centroids.length; i++) {

            for (int j = 0; j < arrayValues.length; j++) {

                // Mathematical formula to get the distance of each point to each centroid.
                
                distance = Math.sqrt((Math.pow((arrayValues[j][0] - centroids[i][0]), 2)) + (Math.pow((arrayValues[j][1] - centroids[i][1]), 2)));

                distancesMatrix[i][j] = distance;

            }

        }

        
    }
    
    /*---------------------- SET MEMBERSHIP MATRIX VALUES -----------------------*/
    
    public static void SetMembershipMatrixValues() {
        
        int pointer = -1;
        
        double minValue = 0.00;
        
        for (int j = 0; j < arrayValues.length; j++) {
            
            minValue = Double.MAX_VALUE;

            for (int i = 0; i < centroids.length; i++) {
                
                // Set pointer to the least value column position in my distancesMatrix.
                
                pointer = (distancesMatrix[i][j] < minValue) ? i : pointer;
                
                minValue = (distancesMatrix[i][j] < minValue) ? distancesMatrix[i][j] : minValue;
                
                // Set all positions to false
                
                membershipMatrix[i][j] = false;
                
            }
            
            // At the end set only the [pointer][j] position to true (That is the position where my least value is).
            
            membershipMatrix[pointer][j] = true;
            
        }
        
    }

    /*---------------------- PRINT MEMBERSHIP MATRIX VALUES -----------------------*/
    
    public static void PrintMembershipMatrixValues() {

        for (int i = 0; i < centroids.length; i++) {

            for (int j = 0; j < arrayValues.length; j++) {

                System.out.println(membershipMatrix[i][j]);

            }

        }

    }

    /*---------------------- PRINT MEMBERSHIP TEMP MATRIX VALUES -----------------------*/
    
    public static void PrintMembershipTempMatrixValues() {

        for (int i = 0; i < centroids.length; i++) {

            for (int j = 0; j < arrayValues.length; j++) {

                System.out.println(membershipMatrixTemp[i][j]);

            }

        }

    }
    
    /*---------------------- PRINT MEMBERSHIP TEMP MATRIX VALUES -----------------------*/
    
    public static boolean VerifyMembershipMatrixValues() {

        boolean isEquals = true;
        
        for (int i = 0; i < centroids.length; i++) {

            for (int j = 0; j < arrayValues.length; j++) {

                if(membershipMatrix[i][j] != membershipMatrixTemp[i][j]){
                
                    return false;
                    
                }
                
            }

        }

        return isEquals;
        
    }
    
    /*---------------------- Set new centroids method -----------------------*/
    
    public static void SetNewCentroids() {

        double valueX = 0.00;
        
        double valueY = 0.00;
        
        double counter = 0.00;
        
        //Iterate until centroids.length - 1 (<)
        
        for (int i = 0; i < centroids.length; i++) {

            //Set xValue & yValue to 0.00 -> for each iterate of this for is a new X & Y value of a new centroid.
            
            valueX = 0.00;
            
            valueY = 0.00;
            
            for (int j = 0; j < 1; j++) {
                
                for (int k = 0; k < arrayValues.length; k++) {
                    
                    // arrayValues.length = membershipMatrix.length -> Iterate membership matrix to get their true or false of each position
                    
                    if(membershipMatrix[i][k]){
                    
                        //If membershipMatrix == true then counter++ and add the new values of X & Y.
                        
                        counter++;
                        
                        for (int l = 0; l < 1; l++) {

                            //arrayValues[k][0] position == membershipMatrix[i][k] position;
                            
                            valueX += arrayValues[k][0];
                            
                            valueY += arrayValues[k][1];
                                    
                        }
                        
                    }

                }

                //Set new centroids value/counter (if counter == 0 then it is divided by 1)
                
                centroids[i][0] = valueX/(( counter > 0 ) ? counter : 1.00);
                
                centroids[i][1] = valueY/((counter > 0) ? counter : 1.00);
                
            }

        }

    }

}
