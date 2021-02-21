import processing.core.*;

class RewardMessage{
	public PApplet papp;
	
  public String message = "";
  public int col;
  public int age = 0;//in ms
  public int lifespan;
  public float rot = 0;
  
  public RewardMessage(PApplet pa, String ms, int c, int lf){
  	papp = pa;
    message = ms;
    col = c;
    lifespan = lf;
  }
  
  public boolean isFinished(){
    return age>lifespan;
  }
  
  public void update(){
    age++;
    rot = PApplet.map(PApplet.sin(PApplet.map(age, 0, lifespan, 0, 2*PApplet.PI)), -1, 1, -PApplet.PI/32, PApplet.PI/32);
  }
  
  public void show(float x, float y, int a, int b){
    papp.push();
    papp.translate(x, y);
    papp.rotate(rot);
    papp.strokeWeight(4);
    papp.textAlign(a, b);
    
    papp.textSize(30);
    papp.fill(col, PApplet.map(age, 0, lifespan, 255, 120));
    papp.text(message, 0, 0);
    papp.pop();
  }
}