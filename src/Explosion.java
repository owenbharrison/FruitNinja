import processing.core.*;
import java.util.ArrayList;

class Explosion{
	public PApplet papp;
	
	public PVector pos;
	public ArrayList<Particle> particles;
	
	public int duration;
	public int age = 0;
	public int intensity;
	public int lifespan;
  
  public Explosion(PApplet pa, float x, float y, int i, int lf, int du){
  	papp = pa;
    particles = new ArrayList<Particle>();
    pos = new PVector(x, y);
    intensity = i;
    lifespan = lf;
    duration = du;
  }
  
  public void update(){
    if(age<duration){
      explodeFromCenter();
    }
    for(int i=0;i<particles.size();i++){
      Particle p = particles.get(i);
      p.update();
      if(p.isFinished()){
        particles.remove(i);
        i--;
      }
    }
    age++;
  }
  
  public void explodeFromCenter(){
    for(int i=0;i<intensity;i++){
      PVector v = PVector.random2D().mult(papp.random(4.3f));
      float size = papp.random(3, 8);
      int lf = lifespan-PApplet.round(papp.random(1, 50));
      Particle p = new Particle(papp, pos.x, pos.y, v.x, v.y, size, lf);
      particles.add(p);
    }
  }
  
  public void updateParticleColors(){
    for(Particle p:particles){
      float percent = PApplet.map(p.age, 0, p.lifespan, 0, 1);
      p.setStroke(Main.explosionGradient.getAtPercent(percent));
    }
  }
  
  public boolean isFinished(){
    boolean res = true;
    for(Particle p:particles){
      if(!p.isFinished())res=false;
    }
    return res;
  }
  
  public void show(){
    for(Particle p:particles)p.show();
  }
}