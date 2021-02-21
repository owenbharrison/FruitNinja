import processing.core.*;
import java.util.ArrayList;

class Polygon{
	public PApplet papp;
	
	public ArrayList<Edge> edges;
  public ArrayList<PVector> points;
  
  public Polygon(PApplet pa, ArrayList<PVector> pts){
  	papp = pa;
    edges = new ArrayList<Edge>();
    points = new ArrayList<PVector>(pts);
    PVector prev = points.get(points.size()-1);
    for(PVector p:points){
      edges.add(new Edge(prev, p));
      prev = p;
    }
  }
  
  public float getArea(){
    int n = points.size();
    float a = 0;
    for(int i=0;i<n-1;i++){
      a+=points.get(i).x*points.get(i+1).y-points.get(i+1).x*points.get(i).y;
    }
    return PApplet.abs(a+points.get(n-1).x*points.get(0).y-points.get(0).x*points.get(n-1).y)/2.0f;
  }
  
  public ArrayList<Edge> edgesToRealPos(PVector pos, float rot){
    ArrayList<Edge> newEdges = new ArrayList<Edge>();
    for(Edge e:edges){
      float a0 = e.p0.heading()+rot;
      float a1 = e.p1.heading()+rot;
      float l0 = e.p0.mag();
      float l1 = e.p1.mag();
      PVector v0 = PVector.fromAngle(a0).mult(l0).add(pos);
      PVector v1 = PVector.fromAngle(a1).mult(l1).add(pos);
      newEdges.add(new Edge(v0, v1));
    }
    return newEdges;
  }
  
  public Edge findNextEdge(Edge e){
    return edges.get((edges.indexOf(e)+1)%edges.size());
  }
  
  public Polygon[] splitByLine(PVector l0, PVector l1){
    Polygon[] result = new Polygon[2];
    Edge edge0 = null;
    Edge edge1 = null;
    PVector intersect0 = null;
    PVector intersect1 = null;
    for(Edge edge:edges){
      PVector intersectPoint = edge.intersectPoint(l0, l1);
      if(intersectPoint!=null){
        if(edge0==null){
          edge0 = edge;
          intersect0 = intersectPoint;
        }
        else if(edge1==null){
          edge1 = edge;
          intersect1 = intersectPoint;
        }
      }
    }
    ArrayList<PVector> pts0 = new ArrayList<PVector>();
    ArrayList<PVector> pts1 = new ArrayList<PVector>();
    if(edge0!=null&&edge1!=null){
      pts0.add(intersect0);
      pts0.add(edge0.p1);
      for(Edge e=findNextEdge(edge0);e!=edge1;e=findNextEdge(e)){
        pts0.add(points.get(edges.indexOf(e)));
      }
      pts0.add(intersect1);    
      pts1.add(intersect1);
      pts1.add(edge1.p1);
      for(Edge e=findNextEdge(edge1);e!=edge0;e=findNextEdge(e)){
        pts1.add(points.get(edges.indexOf(e)));
      }
      pts1.add(intersect0);
    }
    result[0] = new Polygon(papp, pts0);
    result[1] = new Polygon(papp, pts1);
    return result;
  }
  
  public void show(){
    papp.push();
    papp.noFill();
    papp.stroke(0);
    papp.strokeWeight(4);
    papp.beginShape();
    for(PVector p:points){
    	papp.vertex(p.x, p.y);
    }
    papp.endShape(PApplet.CLOSE);
    papp.pop();
  }
}