/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import processing.core.*;
	

import processing.sound.*;
import ddf.minim.*;


/**
 *
 * @author Computer
 */
public class UsingProcessing extends PApplet{
    
    Minim minim;
    AudioPlayer player;

    int framesIntoScene = 0;
    int y = 80;
    int secondLine = 88;
    int thirdLine = 96;
    String state = "NARWHAL";
    int textX = 400;
    int textY = 200;
    int tempNum = 0;
    int decay = 0;
    int translateX = 0;
    int translateY = 0;
    int translateZ = 0;
    
    int horseCount = 0;
    int narwhalCount = 0;
    
    float angle;
    
    PShape coneOnCube;
    PShape cup;
    PShape horseShape;
    PShape narwhalShape;
    
    public static void main(String[] args) {
        PApplet.main(new String[]{UsingProcessing.class.getName()}); 
    }
    
    public void resetVars() {
        framesIntoScene = 0;
        translateX = 0;
        translateY = 0;
        translateZ = 0;
        tempNum = 0;
        //y = 80;
        //secondLine = 88;
        //thirdLine = 96;
        //textX = 400;
        //textY = 200;
        //decay = 0;
    }
    
    
    public void setup() {
        coneOnCube = loadShape("coneoncube.obj");
        cup = loadShape("cup.obj");
        horseShape = loadShape("horse_final_no_fur_1.obj");
        narwhalShape = loadShape("narwhal_test.obj");
        frameRate(30);
        // we pass this to Minim so that it can load files from the data directory
        minim = new Minim(this);
  
        // loadFile will look in all the same places as loadImage does.
        // this means you can find files that are in the data folder and the 
        // sketch folder. you can also pass an absolute path, or a URL.
        player = minim.loadFile("Monoceros.mp3");
    }
    
    public void settings() {
        fullScreen();
        size(1920, 1080, P3D);
    }       
    
    public void draw() {
        framesIntoScene++;
        stateMachine();
        if (keyPressed) {
            stateSwitch(key);
        }
    }
    
    /**
     * Gets hit each time through, and goes to a different place depending on
     * how the "state" variable is currently set.
     */
    public void stateMachine() {
        switch (state) {
            case "END":
                player.close();
                minim.stop();
                super.stop();
                exit();
                break;
            case "HORSE":
                horseScene();
                if (framesIntoScene > 228) {
                    horseCount++;
                    if (horseCount < 2) {
                        state = "NARWHAL";
                    } else {
                        state = "NARWHALRAINBOWAPPROACH";
                    }
                    resetVars();
                }
                break;
            case "NARWHAL":
                if (framesIntoScene == 1 && narwhalCount == 0) {
                    player.cue(0);
                    player.play();
                }
                narwhalScene();
                if (framesIntoScene > 226) {
                    narwhalCount++;
                    //TODO: Make this have the correct progression of scenes
                    if (narwhalCount <= 2) {
                        state = "HORSE";
                    }
                    resetVars();
                }
                break;
            case "HORSERAINBOWAPPROACH":
                horseRainbowApproach();
                if (framesIntoScene > 226) {
                    state = "SCROLLER";
                    resetVars();
                }
                break;
            case "NARWHALRAINBOWAPPROACH":
                narwhalRainbowApproach();
                if (framesIntoScene > 228) {
                    state = "HORSERAINBOWAPPROACH";
                    resetVars();
                }
                break;
            case "SCROLLER":
                scrollerScene();
                if (framesIntoScene > 300) {
                    state = "SHADER";
                    framesIntoScene = 0;
                }
                break;
            case "SHADER":
                shaderScene();
                if (framesIntoScene > 300) {
                    state = "ENDSCENE";
                    framesIntoScene = 0;
                }
                break;
            case "ENDSCENE":
                endScene();
                break;
            default:
                break;
        }
    }
    
    /**
     * If a key is hit, it changes the state. Well, as long as it's in the list.
     * @param key - which key was hit
     */
    public void stateSwitch(char key) {
        switch (key) {
            case ESC:
            case 'q':
                state = "END";
                resetVars();
                break;
            case 'e':
                state = "ENDSCENE";
                resetVars();
                break;
            case 'h':
                state = "HORSE";
                horseCount = 0;
                player.cue(7700);
                resetVars();
                break;
            case 'n':
                state = "NARWHAL";
                narwhalCount = 0;
                player.cue(0);
                resetVars();
                break;
            case 'o':
                state = "HORSERAINBOWAPPROACH";
                player.cue(38333);
                resetVars();
                break;
            case 'r':
                state = "SCROLLER";
                resetVars();
                break;
            case 's':
                state = "SHADER";
                resetVars();
                break;
            case 'w':
                state = "NARWHALRAINBOWAPPROACH";
                player.cue(30733);
                resetVars();
                break;
            default:
                break;
        }
    }
    
    public void scrollerScene()
    {
        drawLines();
        drawText("Turns out that it's not particularly hard to scroll using Processing.", 400, 200);
    }
    
    public void drawLines() {
        int yChange = 2;
        int secondChange = 4;
        int thirdChange = 8;
        stroke(255);
        background(0);   // Clear the screen with a black background
        y = y - yChange;
        if (y < 0) { y = height; }
        line(0, y, width, y);        
        
        stroke(125);
        secondLine = secondLine - secondChange;
        if (secondLine < 0) { secondLine = height; }
        line(0, secondLine, width, secondLine);        
        
        stroke(35);
        thirdLine = thirdLine - thirdChange;
        if (thirdLine < 0) { thirdLine = height; }
        line(0, thirdLine, width, thirdLine);        
        
        int randLine =  (int)(Math.random() * (height + 1));
        stroke(0, 255, 255, 200);
        line(0, randLine, width, randLine);
    }
    
    public void drawText(String inText, int inTextX, int inTextY) {
        text(framesIntoScene, 60, 60); // show value of variable, if wanted
        if(framesIntoScene < 2)
            textX = inTextX;
        String text = inText;
        PFont mono;
        mono = createFont("Monospaced", 32);
        textFont(mono);
        textSize(32); // Set text size to 32
        fill(255);
        for (int i=0; i<text.length(); i++) {
            char c = text.charAt(i);
            text(c, textX + i * 15 , inTextY + (int)((Math.sin(textX + i * 15))*(10+decay)) + decay );
        }
        //text("Turns out that it's not particularly hard to scroll using Processing.", textX, textY);
        //The following bit is meant to make the letters seem more like they're walking,
        //but it doesn't seem to do a whole lot; it might be fine to just subtract 5 each time
        if(framesIntoScene % 2 == 1)
            textX = textX - 9;
        else
            textX = textX - 1;

        //Make the text bounce when the beat is loud
        int lowVolume = (int) (abs(player.left.get(0)) * 100);
        if (lowVolume > 15 && decay < 100) //lowVolume only used once, so could just lookup here.
        {
            decay += 100;
        }

        decay = (int) (decay / 2);
    }
    
    public void shaderScene() {
        int lowVolume = (int) (abs(player.left.get(0)) * 100);
        if (lowVolume > 10 && decay < 100) //lowVolume only used once, so could just lookup here.
        {
            decay += 150;
        }

        decay = (int) (decay / 1.5);
        
        background(0);
        drawText("Now a shader!", 400, 100);
        camera(width / 2, height / 2, 300, width / 2, height / 2, 0, 0, 1, 0);

        pointLight(200, 200, 200, width / 2, height / 2, 200);

        translate(width / 2, height / 2);
        rotateY(angle);
        //rotateX(angle);
        rotateZ(angle);

        //coneOnCube.setFill(color(50 + decay, 50, 150));
        cup.setFill(color(50 + decay, 50, 150));
        scale(15);
        //shape(coneOnCube);
        shape(cup);

        angle += 0.01 + decay *0.001 ;
    }
    
    public void horseScene() {
//        int lowVolume = (int) (abs(player.left.get(0)) * 100);
//        if (lowVolume > 10 && decay < 100) //lowVolume only used once, so could just lookup here.
//        {
//            decay += 150;
//        }
//
//        decay = (int) (decay / 1.5);
//        
        background(0);
        text(framesIntoScene, width/2, height/2); // show value of variable, if wanted
        pushMatrix();
        fill(255);
        //camera(width / 2, height / 2, 150, width / 2, height / 2, 0, 0, 1, 0);
//default camera(width / 2, height / 2, (height/2) / tan((float) (PI*30.0 / 180.0)), height / 2, 0, 0, 1, 0);
        camera(width / 2, height / 2, (height/2) / tan((float) (PI*30.0 / 180.0)), width / 2, height / 2, 0, 0, 1, 0);
        pointLight(200, 200, 200, width / 2, height / 2, 200);
        

        translate(width / 2, height / 2);
        rotateY(radians(90));
        rotateZ(radians(180));
        translate(translateX, translateY, translateZ);
        //horseShape.setFill(color(205,133,63));
        horseShape.setFill(color(255));
        scale(96);
        shape(horseShape);
        
        popMatrix();
        scale(16);
        fill(255);
        //scale((float)1/16);
        //angle += 0.01;
        translateZ += 5;
        translateY += sin(framesIntoScene) * 25;
    }
    
    public void narwhalScene() {
        int lowVolume = (int) (abs(player.left.get(0)) * 100);
        if (lowVolume > 10 && decay < 100) //lowVolume only used once, so could just lookup here.
        {
            decay += 150;
        }

        decay = (int) (decay / 1.5);
        
        background(0);
        pushMatrix();
        text(framesIntoScene, width/4, height/2); // show value of variable, if wanted
        //text(player.length(), width/2, height/2); //Show total length of song in ms. Last check was over 65k
        //text(width, width/2, height/2); //Show width
        //camera(width / 2, height / 2, 150, width / 2, height / 2, 0, 0, 1, 0);
        camera(width / 2, height / 2, (height/2) / tan((float) (PI*30.0 / 180.0)), width / 2, height / 2, 0, 0, 1, 0);

        pointLight(200, 200, 200, width / 2, height / 2, 200);

        translate(width, height / 2);
        rotateY(radians(90));
        rotateX(radians(180));
        //rotateZ(radians(180));
        translate(translateX, translateY, translateZ);

        narwhalShape.setFill(color(50 + decay, 50, 150));
        scale(64);
        shape(narwhalShape);
        popMatrix();

        translateZ += 5;
    }    
    
    public void narwhalRainbowApproach()
    {
        background(0);
        pushMatrix();
        //camera(width / 2, height / 2, 150, width / 2, height / 2, 0, 0, 1, 0);
        pointLight(200, 200, 200, width / 2, height / 2, 200);
        translate((float)((width * 0.39)+translateX), (float)(height *0.52));
        rotateZ(radians(180));
        narwhalShape.setFill(color(50 + decay, 50, 150));
        scale(4);
        shape(narwhalShape);
        
        //rotate and scale back to default
        popMatrix();
        
        scale(2);
        fill(255);
        text(framesIntoScene, 425, 275); // show value of variable, if wanted

        //Display the rainbow, slowly scrolling it onto the screen (and then stopping)
        rainbow(width-translateX,375);
        if(translateX < 200 )
            translateX++;
    }
    
    public void horseRainbowApproach()
    {
        background(0);
        pushMatrix();
        camera(width / 2, height / 2, 150, width / 2, height / 2, 0, 0, 1, 0);
        pointLight(200, 200, 200, width / 2, height / 2, 200);
        translate((float)(width * 0.39), height / 2);
        rotateY(radians(90));
        rotateZ(radians(180));
        translate(0, translateY, translateZ);

        scale(16);
        shape(horseShape);
        
        //rotate and scale back to default
        popMatrix();
        
        scale(2);
        fill(255);
        text(framesIntoScene, 425, 275); // show value of variable, if wanted

        //Display the rainbow, slowly scrolling it onto the screen (and then stopping)
        rainbow(700,375);
        if(translateZ < 200 )
        {
            translateZ += 5;
            translateY += sin(framesIntoScene) * 5;
        }
    }   
    
    public void rainbow2(int centerX, int centerY) {
        fill(0); //black
        ellipse(centerX, centerY, 430, 430);

        fill(255, 0, 0); //red
        ellipse(centerX, centerY, 425, 425);

        fill(257, 127, 0); //orange
        ellipse(centerX, centerY, 412, 412);

        fill(255, 255, 0); //yellow
        ellipse(centerX, centerY, 400, 400);

        fill(0, 255, 0); //green
        ellipse(centerX, centerY, 387, 387);

        fill(0, 0, 255); //blue
        ellipse(centerX, centerY, 375, 375);

        fill(75, 0, 130); //indigo
        ellipse(centerX, centerY, 362, 362);

        fill(148, 0, 211); //violet
        ellipse(centerX, centerY, 350, 350);

        fill(0); //black
        ellipse(centerX, centerY, 337, 337);

    }
    
    public void rainbow(int centerX, int centerY) {
        int speedMultiplier = 14;
        int rainbowState = 0;
        int r = 255;
        int rainbowG = 0;
        int b = 0;
        for (int i = 0; i < 93; i++) { //93 is the difference between the outer ellipse and the black inner elipse (430-337)
            if (rainbowState == 0) {
                rainbowG += speedMultiplier;
                if (rainbowG >= 255) {
                    rainbowState = 1;
                }
            }
            if (rainbowState == 1) {
                r -= speedMultiplier;
                if (r <= 0) {
                    rainbowState = 2;
                }
            }
            if (rainbowState == 2) {
                b += speedMultiplier;
                if (b >= 255) {
                    rainbowState = 3;
                }
            }
            if (rainbowState == 3) {
                rainbowG -= speedMultiplier;
                if (rainbowG == 0) {
                    rainbowState = 4;
                }
            }
            if (rainbowState == 4) {
                r += speedMultiplier;
                if (r >= 255) {
                    rainbowState = 5;
                }
            }
            if (rainbowState == 5) {
                b -= speedMultiplier;
                if (b <= 0) {
                    rainbowState = 0;
                }
            }
            fill(r, rainbowG, b);
            stroke(r, rainbowG, b);
            ellipse(centerX, centerY, 430 - i, 430 - i);
        }
        fill(0); //black
        ellipse(centerX, centerY, 337, 337);
    }

    
    public void endScene() {
        background(0);
        textSize(200);
        fill(255);
        text("The End", 500, 500);
        if(framesIntoScene > 180 )
        {
            state = "END";
            framesIntoScene = 0;
        }
    }

    
}
