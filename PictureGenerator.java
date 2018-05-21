/**
 * This class reads in a set of pictures to train itself.PictureGenerator
 * is consisted of PixelGenerators.They will be trained picture by picture.
 *
 * It also consists method to generate a picture with the upperLeft corner
 * assigned in a give picture.
 *
 * @author Cheng Shen
 * @version 0.1
 */

import java.io.File;
public class PictureGenerator{
    private PixelGenerator[][] generatorGrid;
    //default starting point is (1,1) to avoid index out of bounds
    private int startX=1;
    private int startY=1;
   
    /**
     * Main method for testing
     */
    public static void main(String[] args) throws InterruptedException{
        PictureGenerator pg = new PictureGenerator(716, 1023);
        for(int i = 0; i < 10; i++){
            System.out.println(i+"th loop");
            for(int j = 1; j <= 3; j++){
                Picture p = new Picture(new File("p"+j+".jpg"));
                System.out.println("Loading p"+j+" with width"+p.width()+
                        "and height"+p.height());
                pg.trainPictureGenerator(p, 1.0/(i+j));
            }
        }

        Picture toShow = new Picture(new File("p1.jpg"));
        pg.writePicture(toShow);
        toShow.show();
        System.out.println("Finish");

        toShow.save(new File("generated.jpg"));

        /*
        Picture p1 = new Picture(new File("original.jpg"));
        System.out.println("Loading file with width "+p1.width()
                +" and height "+p1.height());
        p1.show();
        System.out.println("Showing once");
        PictureGenerator pg1 = new PictureGenerator(299,299);
        System.out.println("Randomly created weights:(First point) ");
        System.out.print(pg1.generatorGrid[0][0]);
        for(int i=0;i<500; i++){
            pg1.trainPictureGenerator(p1);
            System.out.println("Training "+i);
            //System.out.print(pg1.generatorGrid[0][0]);
        }
        Thread.sleep(3000);
        System.out.println("Sleeping for next display.");
        pg1.writePicture(p1);
        p1.show();
        Picture p2 = new Picture(new File("p2.jpg"));
        pg1.writePicture(p2);
        p2.show();
        System.out.println("Final show");
        */
    }

    /**
     * constructor
     * @param width the width of picture to be processed
     * @param height the height of picture to be processed
     */
    public PictureGenerator(int width, int height){
        //Default
        generatorGrid = new PixelGenerator[height][width];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                //The position given is related to the part where
                //generatorGrid covers
                generatorGrid[j][i]=new PixelGenerator(startX+i,startY+j);
            }
        }
    }

    /**
     * Train the Picture generator with the picture passed in
     * @param p the picture used to train
     * @param weight how much weight current training is
     */
    public void trainPictureGenerator(Picture p, double weight){
       for(int j = 0; j < generatorGrid.length; j++){
           for(int i = 0; i < generatorGrid[j].length; i++){
               generatorGrid[j][i].trainGenerator(p, weight);
           }
       } 
    }

    /**
     * Use current picture generator to write in a picture.
     * currentPictureGnerator would use the passed-in picture as a starting
     * point
     * @param p the picture used as a start to predict and will be
     * overriden
     */
    public void writePicture(Picture p){
        //Generate Pixel 2D array
        Pixel[][] copiedP = new Pixel[p.height()][p.width()];
        for(int j = 0; j < copiedP.length; j++){
            for(int i = 0; i < copiedP[j].length; i++){
                copiedP[j][i] = new Pixel(i, j, p);
            }
        }
        //Modify the Pixels
        for(int j = 0; j < generatorGrid.length; j++){
            for(int i = 0; i < generatorGrid[j].length; i++){
                try{
               copiedP[startY+j][startX+i]=
                   generatorGrid[j][i].predictPixel(copiedP[startY+j][startX+i-1], 
                        copiedP[startY+j-1][startX+i-1],copiedP[startY+j-1][startX+i]); 
                }catch(ArrayIndexOutOfBoundsException e){
                    System.out.println("i: "+i+"; j: "+j);
                    System.out.println("startX: "+startX+"; startY: "+startY);
                }
            }
        }
        //Displayed the predicted pixels on the picture
        for(int j = 0; j < copiedP.length; j++){
            for(int i = 0; i < copiedP[j].length; i++){
                copiedP[j][i].show(p);
            }
        }
    }
}
