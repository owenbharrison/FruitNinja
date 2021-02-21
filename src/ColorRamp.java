import processing.core.*;

public class ColorRamp{
  public PImage rampImg;
  public int[] ramp;
  
  public ColorRamp(PImage img){
    rampImg = img;
    ramp = new int[rampImg.width];
    rampImg.loadPixels();
    for(int i=0;i<rampImg.width;i++){
      ramp[i] = rampImg.pixels[i];
    }
    rampImg.updatePixels();
  }
  
  int getAtPercent(float pc){
    int index = PApplet.floor(PApplet.map(pc, 0, 1, 0, ramp.length));
    return ramp[index];
  }
}
