/**
 * Pixel class with RGB values and location
 * @author Cheng SHen
 * @version 0.1
 */

public class Pixel{
    //instance variable
    private int x, y;
    private int r, g, b;

    public Pixel(int x, int y, Picture p){
        this.x = x;
        this.y = y;
        this.r = (p.getRGB(x, y) >> 16) & 0xFF;
        this.g = (p.getRGB(x, y) >>  8) & 0xFF;
        this.b = (p.getRGB(x, y) >>  0) & 0xFF;
    }

    public Pixel(int x, int y, int r, int g, int b){
        this.x = x;
        this.y = y;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getR(){
        return this.r;
    }

    public int getG(){
        return this.g;
    }

    public int getB(){
        return this.b;
    }
    
    public void setR(int r) throws IllegalArgumentException{
        if(r < 0 || r > 255){
            throw new IllegalArgumentException(); 
        }

        this.r = r;
    }

    public void setG(int g) throws IllegalArgumentException{
        if(g < 0 || g > 255){
            throw new IllegalArgumentException(); 
        }

        this.g = g;
    }
    
    public void setB(int b) throws IllegalArgumentException{
        if(b < 0 || b > 255){
            throw new IllegalArgumentException(); 
        }

        this.b = b;
    }

    /**
     * Show the pixel on the given picture
     * @param the picture to show the pixel
     */
    public void show(Picture p){
        int rgb = (this.r<<16)+(this.g<<8)+(this.b<<0);
        p.setRGB(this.x, this.y, rgb);
    }
}
