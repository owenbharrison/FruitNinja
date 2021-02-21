import processing.core.*;

class Particle{
	PApplet papp;
  PVector pos, vel;
  int lifespan;
  int age = 0;
  int strokeCol;
  float radius;
  
  public Particle(PApplet pa, float x, float y, float vx, float vy, float r, int lf){
  	papp = pa;
    pos = new PVector(x, y);
    vel = new PVector(vx, vy);
    radius = r;
    lifespan = lf;
    strokeCol = papp.color(0);
  }
  
  void setStroke(int col){
    strokeCol = col;
  }
  
  void update(){
    pos.add(vel);
    vel.mult(Main.AIRFRICTION);
    vel.add(Main.GRAVITY);
    age++;
  }
  
  boolean isFinished(){
    return age>=lifespan;
  }
  
  void show(){
    papp.push();
    papp.stroke(strokeCol);
    papp.strokeWeight(radius);
    papp.point(pos.x, pos.y);
    papp.pop();
  }
}