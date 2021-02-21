import processing.core.*;

class FruitChunk{
	public PApplet papp;
	
  public PVector pos, vel;
  public float rot = 0, rotVel;
  public PImage sprite;
  public Polygon poly;
  public float size = 100;
  
  public FruitChunk(PApplet pa, float x, float y, float vx, float vy, float r, float rv, float size_, PImage img, Polygon p){
  	papp = pa;
    pos = new PVector(x, y);
    vel = new PVector(vx, vy);
    sprite = img;
    poly = p;
    rot = r;
    rotVel = rv;
    size = size_;
  }
  
  public void update(){
    vel.add(Main.GRAVITY);
    pos.add(vel);
    rot += rotVel;
    rotVel *= 0.995;
  }
  
  public void show(){
  	papp.push();
  	papp.translate(pos.x, pos.y);
    papp.rotate(rot);
    papp.noStroke();
    papp.textureMode(PApplet.NORMAL);
    papp.beginShape();
    papp.texture(sprite);
    for(PVector p:poly.points){
    	papp.vertex(p.x, p.y, PApplet.map(p.x, -size, size, 0, 1), PApplet.map(p.y, -size, size, 0, 1));
    }
    papp.endShape();
    papp.pop();
  }
  
  public boolean isOffscreen(){
    return pos.x<-size*2||pos.x>papp.width+size*2||pos.y<-size*2||pos.y>papp.height+size*2;
  }
}