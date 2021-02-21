import processing.core.*;
import ddf.minim.*;
import java.util.ArrayList;

public class Main extends PApplet{
	public static final float AIRFRICTION = 0.998f;
	public static final PVector GRAVITY = new PVector(0, 0.2f);
	public static String[] TYPES = {
	  "apple",
	  "banana",
	  "coconut",
	  "dragonfruit",
	  "lemon",
	  "mango",
	  "orange",
	  "pear",
	  "pineapple",
	  "pomegranate",
	  "starfruit",
	  "tomato",
	  "watermelon",
	  "bomb",
	  "bomb",
	  "bomb",
	  "bomb",
	  "bomb",
	  "bomb"
	};
	public static PImage[] SPRITES;
	public static String[] HITBOXES;
	public static int LIVES = 3;
	public static int SCORE = 0;
	public static int FOULS = 0;

	public static ColorRamp explosionGradient;
	
	public ArrayList<Explosion> explosions;
	public ArrayList<Fruit> fruits;
	public ArrayList<FruitChunk> fruitChunks;
	public ArrayList<RewardMessage> rewardMessages;
	public ArrayList<Splatter> splatters;
	
	public static PImage redSplatterImg;
	public static PImage orangeSplatterImg;
	public static PImage yellowSplatterImg;
	public static PImage greenSplatterImg;
	public static PImage whiteSplatterImg;
	
	public MouseTrail mouseTrail;
	
	public RewardMessage scoreKeeper;
	public RewardMessage foulsKeeper;
	public RewardMessage livesKeeper;

	public Minim minim;
	public AudioPlayer slice1;
	public AudioPlayer slice2;
	public AudioPlayer throwFruit;
	public AudioPlayer bombExplode;
	public AudioPlayer gameOver;

	public PImage backgroundImg;
	public PImage gameOverImg;
	
	public static PVector mouseCoords = new PVector(0, 0);
	
	public static void main(String[] args){
		PApplet.main(new String[] {"Main"});
	}
	
	public void settings() {
		size(700, 800, P2D);
	}

	public void setup(){
	  backgroundImg = loadImage("./../res/backgrounds/background"+round(random(1, 4))+".jpg");
	  gameOverImg = loadImage("./../res/backgrounds/gameOver.png");
	  
	  SPRITES = new PImage[TYPES.length];
	  for(int i=0;i<TYPES.length;i++){
	    SPRITES[i] = loadImage("./../res/sprites/"+TYPES[i]+".png");
	  }
	  HITBOXES = new String[TYPES.length];
	  for(int i=0;i<TYPES.length;i++){
	    HITBOXES[i] = String.join("\n", loadStrings("./../res/hitboxes/"+TYPES[i]+".hitbox"));
	  }
	  
	  explosions = new ArrayList<Explosion>();
	  explosionGradient = new ColorRamp(loadImage("./../res/sprites/explosionRamp.png"));
	  fruits = new ArrayList<Fruit>();
	  fruitChunks = new ArrayList<FruitChunk>();
	  rewardMessages = new ArrayList<RewardMessage>();
	  splatters = new ArrayList<Splatter>();
	  mouseTrail = new MouseTrail(this, 10, 10, 3);
	  scoreKeeper = new RewardMessage(this, "Score: "+SCORE, color(255), round(Float.POSITIVE_INFINITY));
	  foulsKeeper = new RewardMessage(this, "Fouls: "+FOULS, color(255, 255, 0), round(Float.POSITIVE_INFINITY));
	  livesKeeper = new RewardMessage(this, "LIVES: "+LIVES, color(255, 0, 0), round(Float.POSITIVE_INFINITY));
	  
	  redSplatterImg = loadImage("./../res/sprites/redSplatter.png");
	  orangeSplatterImg = loadImage("./../res/sprites/orangeSplatter.png");
	  yellowSplatterImg = loadImage("./../res/sprites/yellowSplatter.png");
	  greenSplatterImg = loadImage("./../res/sprites/greenSplatter.png");
	  whiteSplatterImg = loadImage("./../res/sprites/whiteSplatter.png");
	  
	  minim = new Minim(this);
	  slice1 = minim.loadFile("./../res/sounds/slice1.wav", 1024);
	  slice2 = minim.loadFile("./../res/sounds/slice2.wav", 1024);
	  throwFruit = minim.loadFile("./../res/sounds/throwFruit.wav", 1024);
	  bombExplode = minim.loadFile("./../res/sounds/bombExplode.wav", 1024);
	  gameOver = minim.loadFile("./../res/sounds/gameOver.wav", 1024);
	}

	public void mousePressed(){
	  for(PVector p:mouseTrail.points){
	    p.x = mouseX;
	    p.y = mouseY;
	  }
	  noCursor();
	}

	public void mouseReleased(){
	  for(PVector p:mouseTrail.points){
	    p.x = width/2;
	    p.y = -50;
	  }
	  cursor();
	}

	public void draw(){
	  background(0);
	  image(backgroundImg, 0, 0, width, height);
	  
	  //show and update splatter and if done remove it
	  for(int i=0;i<splatters.size();i++){
	    Splatter s = splatters.get(i);
	    s.update();
	    s.show();
	    if(s.isFinished()){
	      splatters.remove(i);
	      i--;
	    }
	  }
	  
	  //show and update explosions and if done remove it
	  for(int i=0;i<explosions.size();i++){
	    Explosion e = explosions.get(i); 
	    e.update();
	    e.updateParticleColors();
	    e.show();
	    if(e.isFinished()){
	      explosions.remove(i);
	      i--;
	    }
	  }
	  
	  if(random(1)<0.01){
	    Fruit fruit = makeNewFruit();
	    if(fruits.size()>0){
	      for(Fruit f=fruit;f.type!=fruits.get(fruits.size()-1).type;f=makeNewFruit()){
	        fruit = f;
	      }
	    }
	    fruits.add(fruit);
	    throwFruit.rewind();
	    throwFruit.play();
	  }
	  
	  //loop through fruits to show and update and see if they need to be sliced
	  for(int i=0;i<fruits.size();i++){
	    Fruit f = fruits.get(i);
	    
	    f.update();
	    f.show();
	    
	    boolean toBeRemoved = false;
	    PVector[] sliceCheck = f.checkSlice(mouseTrail);
	    if(sliceCheck!=null){
	      if(f.isBomb){
	        LIVES--;
	        bombExplode.rewind();
	        bombExplode.play();
	        explosions.add(new Explosion(this, f.pos.x, f.pos.y, 500, 80, 5));
	      }
	      else{
	        PVector a = sliceCheck[0];
	        PVector b = sliceCheck[1];
	        PVector d = PVector.sub(b,a);
	        PVector o = d.mult(10/d.mag()/2);
	        a.sub(o);
	        b.add(o);
	        FruitChunk[] fc = f.slice(a, b);
	        fruitChunks.add(fc[0]);
	        fruitChunks.add(fc[1]);
	      
	        splatters.add(new Splatter(this, f.pos.x, f.pos.y, round(f.size*2), 60, f.getSplatterImg()));
	      
	        //add reward messages based on how close to 50:50 split it was
	        float a0 = fc[0].poly.getArea();
	        float a1 = fc[1].poly.getArea();
	        float sum = a0+a1;
	        float pDiff = abs((round(a0/sum*100)+1)-(round(a1/sum*100)+1));
	        String msg = "";
	        int col =  color(0);
	        if(pDiff>0&&pDiff<=20){
	          msg = "Nice! You're Insane!!";
	          col = color(10, 10, 255);
	          SCORE += 5;
	        }
	        else if(pDiff>20&&pDiff<=40){
	          msg = "Good Job!";
	          col = color(10, 255, 10);
	          SCORE += 4;
	        }
	        else if(pDiff>40&&pDiff<=60){
	          msg = "Not Bad!";
	          col = color(255, 255, 10);
	          SCORE += 3;
	        }
	        else if(pDiff>60&&pDiff<=80){
	          msg = "Better Luck Next Time.";
	          col = color(255, 102, 0);
	          SCORE += 2;
	        }
	        else if(pDiff>80&&pDiff<=100){
	          msg = "You're Awful.";
	          col = color(255, 20, 20);
	          SCORE += 1;
	        }
	        RewardMessage rw = new RewardMessage(this, msg, col, 75);
	        rewardMessages.add(rw);
	        if(rewardMessages.size()>5)rewardMessages.remove(0);
	        
	        //play random slice noise
	        if(random(1)>0.5){
	          slice1.rewind();
	          slice1.play();
	        }
	        else{
	          slice2.rewind();
	          slice2.play();
	        }
	      }
	      toBeRemoved=true;
	    }
	    if(f.isOffscreen()){
	      if(f.type!="bomb"){
	        FOULS++;
	      }
	      toBeRemoved=true;
	    }
	    if(toBeRemoved){
	      fruits.remove(i);
	      i--;
	    }
	  }
	  scoreKeeper = new RewardMessage(this, "Score: "+SCORE, color(255), round(Float.POSITIVE_INFINITY));
	  foulsKeeper = new RewardMessage(this, "Fouls: "+FOULS, color(255, 255, 0), round(Float.POSITIVE_INFINITY));
	  livesKeeper = new RewardMessage(this, "Lives: "+LIVES, color(255, 0, 0), round(Float.POSITIVE_INFINITY));
	  if(FOULS==3){
	    LIVES--;
	    FOULS = 0;
	  }
	  if(LIVES==0){
	    gameEnd();
	    noLoop();
	  }
	  
	  //loop through fruit chunks and show and update and see if they are offscreen
	  for(int i=0;i<fruitChunks.size();i++){
	    FruitChunk fc = fruitChunks.get(i);
	    fc.update();
	    fc.show();
	    
	    if(fc.isOffscreen()){
	      fruitChunks.remove(i);
	      i--;
	    }
	  }
	  
	  //show sketch name and fps
	  surface.setTitle("Fruit Ninja  FPS: "+round(frameRate));
	  if(mousePressed){
	    mouseTrail.update();
	    mouseTrail.show();
	  }
	  
	  //show reward messages
	  for(int i=0;i<rewardMessages.size();i++){
	    RewardMessage rw = rewardMessages.get(i);
	    rw.update();
	    rw.show(width/2, map(i, 0, 5, height, height-110), CENTER, BOTTOM);
	    
	    if(rw.isFinished()){
	      rewardMessages.remove(i);
	      i--;
	    }
	  }
	  scoreKeeper.update();
	  scoreKeeper.show(0, 0, LEFT, TOP);
	  foulsKeeper.update();
	  foulsKeeper.show(width, 25, RIGHT, TOP);
	  livesKeeper.update();
	  livesKeeper.show(width, 0, RIGHT, TOP);
	  mouseCoords.set(mouseX, mouseY);
	}

	public Fruit makeNewFruit(){
	  float xvel = random(-3, 3);
	  float yvel = random(-17, -11);
	  float rotVel = random(-0.2f, 0.2f);
	  return new Fruit(this, width/2+random(-75, 75), height, xvel, yvel, rotVel);
	}

	public void gameEnd(){
	  gameOver.play();
	  image(gameOverImg, 0, 0, width, height);
	}
}