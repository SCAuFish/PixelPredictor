/**
 * This class includes the data for generating a Pixel at certain position.
 * Each Pixel is determined by the Pixel to the left, on the top and to the
 * upper left. Therefore, 3*3 = 9 weights are stored. Furthermore, the
 * coordinate of this Pixel is also a part of the data.
 *
 * @author Cheng Shen
 * @version 0.1
 */

import java.util.Random;
public class PixelGenerator{
    //Several constants
    //number of variables defining a color
    private final int COLOR_VAR_NUM = 3;
    //Instance Variable
    //weights should be values between 0 and 1 but during calculation
    //it might go out of bounds. That's okay as long as they maintain this
    //magnitude
    private double[][] leftWeights;
    private double[][] topWeights;
    private double[][] upLeftWeights;
    private int x, y;

    public PixelGenerator(int x, int y){
        this.x = x;
        this.y = y;
        this.leftWeights = new double[COLOR_VAR_NUM][COLOR_VAR_NUM];
        initializeWeights(leftWeights);
        this.topWeights = new double[COLOR_VAR_NUM][COLOR_VAR_NUM];
        initializeWeights(topWeights);
        this.upLeftWeights = new double[COLOR_VAR_NUM][COLOR_VAR_NUM];
        initializeWeights(upLeftWeights);
    }

    /**
     * This method gives out the predicted Pixel based on current weights
     * @param left the given Pixel to the left
     * @param top the given Pixel to the top
     * @param upLeft the given Pixel to the upperLeft
     * @return the Pixel predicted
     */
    public Pixel predictPixel(Pixel left, Pixel upLeft, Pixel top){
        double[] leftRGB = {left.getR(), left.getG(), left.getB()};
        double[] upLeftRGB = {upLeft.getR(), upLeft.getG(), upLeft.getB()};
        double[] topRGB = {top.getR(), top.getG(), top.getB()};
        
        double[] fromLeft = product(leftWeights, leftRGB);
        double[] fromUpLeft = product(upLeftWeights, upLeftRGB);
        double[] fromTop = product(topWeights, topRGB);

        int[] resultRGB =new int[COLOR_VAR_NUM];
        for(int i = 0; i < resultRGB.length; i++){
            resultRGB[i] = 
                (int)sigmoid((fromLeft[i]+fromUpLeft[i]+fromTop[i])/3);
        }

        int r = resultRGB[0];
        int g = resultRGB[1];
        int b = resultRGB[2];
        return new Pixel(x,y, r, g, b);
    }

    /**
     * This method trains current PixelGenerator based on a given "answer"
     * Pixel
     * It compares generated Pixel with "answer" pixel and adjust weights
     * accordingly using gradient descent method
     * @param p the picture passed in for training
     * @param weight how much weight current training is
     */
    public void trainGenerator(Picture p, double weight){
        Pixel left = new Pixel(this.x-1, this.y, p);
        Pixel upLeft = new Pixel(this.x-1, this.y-1, p);
        Pixel top = new Pixel(this.x, this.y-1, p);
        Pixel answer = new Pixel(this.x, this.y, p);

        Pixel predicted = predictPixel(left, upLeft, top);
        double rDiff = predicted.getR()-answer.getR();
        //System.out.println("predicted: "+predicted.getR());
        //System.out.println("answer: "+answer.getR());
        double rDiffRatio = rDiff/255.0;
        double gDiff = predicted.getG()-answer.getG();
        double gDiffRatio = gDiff/255.0;
        double bDiff = predicted.getB()-answer.getB();
        double bDiffRatio = bDiff/255.0;
        //System.out.println("r: "+rDiff+"g: "+gDiff+"b: "+bDiff);
    
        //Add weight to all the diffs
        rDiffRatio *= weight;
        gDiffRatio *= weight;
        bDiffRatio *= weight;

        double[] diffs = {rDiffRatio, gDiffRatio, bDiffRatio};
        //Adjust weights in leftWeights
        int[] leftRGB = {left.getR(), left.getG(), left.getB()};
        for(int i = 0; i < leftWeights.length; i++){
            for(int j = 0; j < leftWeights[i].length; j++){
                //The gradient of difference over weight is the RGB
                //value that is passed in. Furthermore, intuitively,
                //the higher the difference, the more the weight should
                //be adjusted.
                leftWeights[i][j]-=diffs[i]*leftRGB[j]/255.0;
            }
        }
        //Adjust weights in upLeftWeights
        int[] upLeftRGB = {upLeft.getR(), upLeft.getG(), upLeft.getB()};
        for(int i = 0; i < upLeftWeights.length; i++){
            for(int j = 0; j < upLeftWeights[i].length; j++){
                //The gradient of difference over weight is the RGB
                //value that is passed in. Furthermore, intuitively,
                //the higher the difference, the more the weight should
                //be adjusted.
                upLeftWeights[i][j]-=diffs[i]*upLeftRGB[j]/255.0;
            }
        }
        //Adjust weights in topWeights
        int[] topRGB = {top.getR(), top.getG(), top.getB()};
        for(int i = 0; i < topWeights.length; i++){
            for(int j = 0; j < topWeights[i].length; j++){
                //The gradient of difference over weight is the RGB
                //value that is passed in. Furthermore, intuitively,
                //the higher the difference, the more the weight should
                //be adjusted.
                topWeights[i][j]-=diffs[i]*topRGB[j]/255.0;
            }
        }
           
    }

    /**
     * Helper method for filling the weights array with random starting
     * values
     */
    private static void initializeWeights(double[][] weights){
        Random r = new Random();
        for(int i = 0; i < weights.length; i++){
            for(int j = 0; j < weights[i].length; j++){
                weights[i][j] = r.nextDouble();
            }
        }
    }

    /**
     * This helper method makes use of Sigmoid function to turn
     * values probably larger than 255 or smaller than 0 within this
     * range
     * @param x original value
     * @return z sigmoided x value
     */
    private double sigmoid(double x){
        double max = 245;
        double mid = max/2;
        double k = 0.02; //This makes the derivative at the midpoint 255

        //System.out.println("x: "+x);
        return max/(1+Math.exp(-k*(x-mid)))+10;
    }

    /**
     * Helper method to accomplish matrix operation
     * @param A the linear operation matrix
     * @param x the vector to be transformed
     * @return a column vector after x being transformed
     */
    private double[] product(double[][] A, double[] x) 
            throws IllegalArgumentException{
        if(A[0].length != x.length){
            throw new IllegalArgumentException();
        }

        double[] result = new double[x.length];
        for(int i = 0; i < result.length; i++){
            result[i] = dotProduct(A[i], x);
        }

        return result;
    }

    /**
     * Helper method on matrix operation for 1D vectors.
     * @param x a row vector
     * @param y a column vector
     * @return the dot product result
     */
    private double dotProduct(double[] x, double[] y) 
            throws IllegalArgumentException{
        if(x.length!=y.length){
            throw new IllegalArgumentException();
        }
        double product=0;
        for(int i =0; i < x.length; i++){
            product+=x[i]*y[i];
        }

        return product;
    }

    /**
     * This toString method actually acts as a debugging helper
     * @return String expression of a PixelGenerator
     */
    @Override
    public String toString(){
       String result = 
            "| "+upLeftWeights[0][0]+" "+upLeftWeights[0][1]+" "+upLeftWeights[0][2]+
            " | "+topWeights[0][0]+" "+topWeights[0][1]+" "+topWeights[0][2]+" |"+ "\n"+
            "| "+upLeftWeights[1][0]+" "+upLeftWeights[1][1]+" "+upLeftWeights[1][2]+
            " | "+topWeights[1][0]+" "+topWeights[1][1]+" "+topWeights[1][2]+" |"+ "\n"+
            "| "+upLeftWeights[2][0]+" "+upLeftWeights[2][1]+" "+upLeftWeights[2][2]+
            " | "+topWeights[2][0]+" "+topWeights[2][1]+" "+topWeights[2][2]+" |"+ "\n"+
            "| "+leftWeights[0][0]+" "+leftWeights[0][1]+" "+leftWeights[0][2]+" |"+ "\n"+
            "| "+leftWeights[1][0]+" "+leftWeights[1][1]+" "+leftWeights[1][2]+" |"+ "\n"+
            "| "+leftWeights[2][0]+" "+leftWeights[2][1]+" "+leftWeights[2][2]+" |"+ "\n";

       return result;
    }
}
