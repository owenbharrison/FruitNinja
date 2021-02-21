import processing.core.*;

class MouseTrail{
	public PApplet papp;
	
	public float maxSize, minSize;
	public PVector[] points;
  
  public MouseTrail(PApplet pa, int len, float max, float min){
  	papp = pa;
    points = new PVector[len];
    java.util.Arrays.fill(points, Main.mouseCoords.copy());
    maxSize = max;
    minSize = min;
  }
  
  public void show(){
    PVector prev = points[0];
    for(int i=1;i<points.length;i++){
      PVector c = points[i];
      papp.push();
      papp.stroke(240);
      papp.strokeWeight(PApplet.map(i, 0, points.length, maxSize, minSize));
      papp.line(prev.x, prev.y, c.x, c.y);
      papp.pop();
      prev = c;
    }
  }
  
  public void update(){
    PVector prev = points[0];
    points[0] = Main.mouseCoords.copy();
    for(int i=1;i<points.length;i++){
      PVector next = points[i];
      points[i] = prev;
      prev = next;
    }
  }
}