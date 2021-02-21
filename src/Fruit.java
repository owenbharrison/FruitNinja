import processing.core.*;
import java.util.ArrayList;

class Fruit{
	public PApplet papp;
	
	public PVector pos, vel;
	public float rot = 0, rotVel;
  public int id;
  public String type;
  public PImage sprite;
  public Polygon mainPoly;
  public Polygon[] slicedPoly;
  public boolean sliced = false;
  public boolean isBomb = false;
  public float size = 50;
  
  public Fruit(PApplet pa, float x, float y, float vx, float vy, float rv){
  	papp = pa;
  	id = PApplet.floor(papp.random(Main.TYPES.length));
    pos = new PVector(x, y);
    vel = new PVector(vx, vy);
    type = Main.TYPES[id];
    if(type=="pineapple")size*=2;
    if(type=="banana")size*=1.5;
    if(type=="watermelon")size*=1.2;
    if(type=="tomato")size*=0.75;
    if(type=="dragonfruit")size*=1.5;
    if(type=="bomb"){size*=1.35;isBomb=true;}
    sprite = Main.SPRITES[id];
    ArrayList<PVector> mainPts = new ArrayList<PVector>();
    for(String s:Main.HITBOXES[id].split("\n")){
      String[] pos = s.split(" ");
      mainPts.add(new PVector(Float.parseFloat(pos[0])*size, Float.parseFloat(pos[1])*size));
    }
    mainPoly = new Polygon(papp, mainPts);
    slicedPoly = new Polygon[2];
    rotVel = rv;
  }
  
  void update(){
    vel.add(Main.GRAVITY);
    pos.add(vel);
    rot += rotVel;
    rotVel *= 0.995;
  }
  
  void show(){
    papp.push();
    papp.translate(pos.x, pos.y);
    papp.rotate(rot);
    papp.noStroke();
    papp.textureMode(PApplet.NORMAL);
    papp.beginShape();
    papp.texture(sprite);
    for(PVector p:mainPoly.points){
    	papp.vertex(p.x, p.y, PApplet.map(p.x, -size, size, 0, 1), PApplet.map(p.y, -size, size, 0, 1));
    }
    papp.endShape();
    papp.pop();
  }
  
  PImage getSplatterImg(){
    if(type=="apple"||type=="tomato"||type=="dragonfruit"||type=="pomegranate"||type=="watermelon")return Main.redSplatterImg;
    if(type=="orange"||type=="mango")return Main.orangeSplatterImg;
    if(type=="banana"||type=="lemon"||type=="starfruit"||type=="pineapple")return Main.yellowSplatterImg;
    if(type=="pear")return Main.greenSplatterImg;
    if(type=="coconut")return Main.whiteSplatterImg;
    else return sprite;
  }
  
  boolean isOffscreen(){
    return pos.x<-size||pos.x>papp.width+size||pos.y<-size||pos.y>papp.height+size;
  }
  
  public FruitChunk[] slice(PVector a, PVector b){
    FruitChunk[] fcs = new FruitChunk[2];
    Polygon[] slicedPoly = mainPoly.splitByLine(a, b);
    
    float angle = PVector.sub(a, b).heading();
    PVector front = PVector.fromAngle(angle+PApplet.PI/2).mult(4);
    PVector back = PVector.fromAngle(angle-PApplet.PI/2).mult(4);
    
    fcs[0] = new FruitChunk(papp, pos.x, pos.y, front.x, front.y, rot, 0.02f, size, sprite, slicedPoly[0]);
    fcs[1] = new FruitChunk(papp, pos.x, pos.y, back.y, back.y, rot, -0.02f, size, sprite, slicedPoly[1]);
    
    return fcs;
  }
  
  PVector[] checkSlice(MouseTrail trail){
    PVector[] line = new PVector[2];
    PVector prev = trail.points[trail.points.length-1];
    for(PVector p:trail.points){
      for(Edge e:mainPoly.edgesToRealPos(pos, rot)){
        PVector ip = e.intersectPoint(prev, p);
        if(ip!=null){
          PVector newPt = ip.sub(pos);
          float len = newPt.mag();
          newPt.normalize();
          newPt = PVector.fromAngle(newPt.heading()-rot).mult(len);
          if(line[0]==null)line[0] = newPt;
          else if(line[1]==null)line[1] = newPt;
          else return line;
        }
      }
      prev = p;
    }
    return null;
  }
}