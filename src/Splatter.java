import processing.core.*;

class Splatter{
	public PApplet papp;
	
  public PImage sprite;
  public float rot = 0;
  public PVector pos;
  public int age = 0;
  public int lifespan = 0;
  public int size = 0;
  
  public Splatter(PApplet pa, float x, float y, int sz, int lf, PImage splatImg){
  	papp = pa;
    pos = new PVector(x, y);
    sprite = splatImg;
    rot = papp.random(-PApplet.PI/8, PApplet.PI/8);
    size = sz;
    lifespan = lf;
    sprite = splatImg;
  }
  
  public boolean isFinished(){
    return age>lifespan;
  }
  
  public void update(){
    age++;
  }
  
  public void show(){
    papp.push();
    papp.translate(pos.x, pos.y);
    papp.rotate(rot);
    papp.imageMode(PApplet.CENTER);
    papp.tint(255, PApplet.map(age, 0, lifespan, 255, 55));
    papp.image(sprite, 0, 0, size, size);
    papp.pop();
  }
}