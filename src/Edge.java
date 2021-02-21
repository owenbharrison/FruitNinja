import processing.core.*;

class Edge{
  PVector p0, p1;
  
  public Edge(PVector a, PVector b){
    p0 = a.copy();
    p1 = b.copy();
  }
  
  public PVector intersectPoint(PVector a, PVector b){
    PVector c = p0;
    PVector d = p1;
    float den = (a.x-b.x)*(c.y-d.y)-(a.y-b.y)*(c.x-d.x);
    float t = ((a.x-c.x)*(c.y-d.y)-(a.y-c.y)*(c.x-d.x))/den;
    float u = ((b.x-a.x)*(a.y-c.y)-(b.y-a.y)*(a.x-c.x))/den;
    if(t>=0.0&&t<=1.0&u>=0.0&&u<=1.0){
      return new PVector(a.x+t*(b.x-a.x), a.y+t*(b.y-a.y));
    }
    else return null;
  }
}