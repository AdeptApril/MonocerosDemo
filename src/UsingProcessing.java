/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import processing.core.*;	
import ddf.minim.*;
import java.util.Random;

//TODO: Try/catch around object loading. the program should fail anyway, so it doesn't seem hugely important, but good style and all

/**
 *
 * @author Computer
 */
public class UsingProcessing extends PApplet{
    
    Minim minim;
    AudioPlayer player;
    
    Random rand = new Random();

    int framesIntoScene = 0;
    int timeIntoScene = 0; //Time in milliseconds.
    int startTimer; //startTimer = millis() at the beginning of any scene
    int y = 80;
    int secondLine = 88;
    int thirdLine = 96;
    String state = "LOADINGSTART";
    int textX = 400;
    int textY = 200;
    int tempNum = 0;
    int decay = 0;
    int translateX = 0;
    int translateY = 0;
    int translateZ = 0;
    
    int horseCount = 1;
    int narwhalCount = 1;
    
    int tempx = 0; //TODO: Delete anything that uses this, then delete this.
    int tempy = 0;
    int tempz = 0;
    
    boolean batch1Loaded = false;
    boolean batch2Loaded = false;
    boolean batch3Loaded = false;
    
    float angle;
    
    PImage img;
    PShape cup;
    PShape horseShape;
    PShape horseRainbowShape;
    PShape narwhalShape;
    PShape narwhalRainbowShape;
    PShape backgroundLandShape;
    PShape backgroundWaterShape;
    PShape babyUnicornShape;
    PShape backgroundLandSnowShape;
    PShape backgroundSnowShape;
    PShape backgroundSnowWaterShape;
    PShape birdShape;
    PShape cloudShape;
    PShape fishShape;
    PShape flowerShape;
    PShape iceShelfShape;
    PShape icebergShape;
    PShape poopShape;
    PShape treeShape;
    
    //TODO: Remove all the temporary showing frames-into-scenes text (or other debug text)
    
    public static void main(String[] args) {
        PApplet.main(new String[]{UsingProcessing.class.getName()}); 
    }
    
    public void resetVars() {
        framesIntoScene = 0;
        translateX = 0;
        translateY = 0;
        translateZ = 0;
        tempNum = 0;
        startTimer = millis();
        //y = 80;
        //secondLine = 88;
        //thirdLine = 96;
        //textX = 400;
        //textY = 200;
        //decay = 0;
    }
    
    
    public void setup() {
        
        frameRate(30);
        //Perhaps camera could be moved around, but this is assuming that the seen scene is screen sized
        camera(width / 2, height / 2, (height/2) / tan((float) (PI*30.0 / 180.0)), width / 2, height / 2, 0, 0, 1, 0);
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
            case "LOADINGSTART":
                if (framesIntoScene == 1) {
                    //Load picture
                    img = loadImage("loadingScreenTemp.jpg");
                    image(img, 0, 0);
                }
                if (framesIntoScene == 2) {
                    //Load models
                    loadBatch1();
                }
                if (framesIntoScene > 2) {
                    state = "NARWHAL";
                    resetVars();
                }
                break;
            case "NARWHAL": ////7700, *2, *3, 30733, 38333, 46100 (narwhal suck), 
                //start the music playing at the beginning, but otherwise only set cues when there's keyboard input
                if (framesIntoScene == 1 && narwhalCount == 1) {
                    player.cue(15400);
                    startTimer = millis();
                    player.play();
                }
                narwhalScene();
                if (millis() - startTimer > 7700){//framesIntoScene > 228) {
                    narwhalCount++;
                    //TODO: Make this have the correct progression of scenes
                    if (narwhalCount <= 2) {
                        state = "HORSE";
                    }
                    resetVars();
                }
                break;                
            case "HORSE":
                horseScene();
                if (millis() - startTimer > 7700){//framesIntoScene > 228) {
                    horseCount++;
                    if (horseCount < 2) {
                        state = "NARWHAL";
                    } else {
                        state = "NARWHALRAINBOWAPPROACH";
                    }
                    resetVars();
                }
                break;
            case "NARWHALRAINBOWAPPROACH":
                narwhalRainbowApproach();
                if (millis() - startTimer > 7666){//framesIntoScene > 226) {
                    state = "HORSERAINBOWAPPROACH";
                    resetVars();
                }
                break;
            case "HORSERAINBOWAPPROACH":
                horseRainbowApproach();
                if (millis() - startTimer > 6700){//framesIntoScene > 226) {
                    state = "NARWHALSUCK";
                    resetVars();
                }
                break;
            case "NARWHALSUCK":
                narwhalSuck();
                if (millis() - startTimer > 5500){//framesIntoScene > 226) {
                    player.pause(); //resyncing the demo during a pause into the next scene
                    state = "LOADINGMIDDLE";
                    resetVars();
                }
                break;
            case "LOADINGMIDDLE":
                if (framesIntoScene == 1)
                {
                    //Do loading
                    loadBatch2();
                }
                if (framesIntoScene == 2)
                {
                    state = "HORSESUCK";
                    resetVars();
                }
                break;
            case "HORSESUCK":
                if (framesIntoScene == 1) {
                    //resyncing the demo, since there's a pause in the music.
                    player.cue(51400);
                    //resetVars();
                    player.play();
                }
                horseSuck();
                if (millis() - startTimer > 5600){//framesIntoScene > 226) {
                    state = "LOADINGMIDDLE2";
                    resetVars();
                }
                break;
            case "LOADINGMIDDLE2":
                if (framesIntoScene == 1)
                {
                    //Do loading
                    loadBatch3();
                }
                if (framesIntoScene == 2)
                {
                    state = "NARWHALSEARCH";
                    resetVars();
                }
                break;
            case "NARWHALSEARCH":
                if (framesIntoScene == 1) {
                    //resyncing the demo, since there's a pause in the music.
                    player.cue(57200);
                    player.play();
                }
                narwhalSearch();
                if (millis() - startTimer > 4500){
                    state = "HORSESEARCH";
                    resetVars();
                }
                break;
            case "HORSESEARCH":
                horseSearch();
                if (millis() - startTimer > 3820){
                    state = "NARWHALSEARCH2";
                    resetVars();
                }
                break;
            case "NARWHALSEARCH2":
                narwhalSearch2();
                if (millis() - startTimer > 4320){
                    state = "HORSESEARCH2";
                    resetVars();
                }
                break;
            case "HORSESEARCH2":
                horseSearch2();
                if (millis() - startTimer > 3820){
                    state = "HORSENARWHALMEETING";
                    resetVars();
                }
                break;
            case "HORSENARWHALMEETING":
                horseNarwhalMeeting();
                if (millis() - startTimer > 8088){
                    state = "ICESHELFBREAK";
                    resetVars();
                }
                break; //These two scenes possibly should be combined.
            case "ICESHELFBREAK":
                iceShelfBreak();
                if (millis() - startTimer > 8088){
                    state = "SHADER";
                    resetVars();
                }
                break;
            case "WORRIEDHORSE":
                //worriedHorse();
                if (millis() - startTimer > 10088){
                    state = "SHADER";
                    resetVars();
                }
                break;
            case "NARWHALSAVE":
                //narwhalSave();
                if (millis() - startTimer > 10088){
                    state = "SHADER";
                    resetVars();
                }
                break;
            case "LOVEINAIR":
                //loveInAir();
                if (millis() - startTimer > 10088){
                    state = "SHADER";
                    resetVars();
                }
                break;
            case "SEXYTIMES":
                break;
            case "BABYUNICORN":
                break;
            case "CREDITS":
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
            case 'z':
                state = "ENDSCENE";
                resetVars();
                break;
            case 'a':
                if(!batch1Loaded) //TODO: Possibly replace all these if statements
                    //With a, "loadNeededFiles(char case)", passing along
                    // the curret point in the demo.
                    loadBatch1();
                state = "NARWHAL";
                narwhalCount = 0;
                player.cue(15360);
                resetVars();
                break;                
            case 'b':
                if(!batch1Loaded)
                    loadBatch1();
                state = "HORSE";
                horseCount = 0;
                player.cue(23040);
                resetVars();
                break;
            case 'c':
                if(!batch1Loaded)
                    loadBatch1();
                state = "NARWHALRAINBOWAPPROACH";
                player.cue(30720);
                resetVars();
                break;
            case 'd':
                if(!batch1Loaded)
                    loadBatch1();
                state = "HORSERAINBOWAPPROACH"; 
                player.cue(38400);
                resetVars();
                break;
            case 'e':
                if(!batch1Loaded)
                    loadBatch1();
                state = "NARWHALSUCK";
                player.cue(45120);
                resetVars();
                break;
            case 'f':
                if(!batch1Loaded)
                    loadBatch1();
                if(!batch2Loaded)
                    loadBatch2();
                state = "HORSESUCK";
                player.cue(50640);
                resetVars();
                break;
            case 'g':
                 if(!batch1Loaded)
                    loadBatch1();
                if(!batch2Loaded)
                    loadBatch2();
                if(!batch3Loaded)
                    loadBatch3();
                state="NARWHALSEARCH";
                player.cue(56400);
                resetVars();
                break;
            case 'h':
                 if(!batch1Loaded)
                    loadBatch1();
                if(!batch2Loaded)
                    loadBatch2();
                if(!batch3Loaded)
                    loadBatch3();
                state="HORSENARWHALMEETING";
                player.cue(72720);
                resetVars();
                break;
            case 'r':
                if(!batch1Loaded)
                    loadBatch1();
                if(!batch2Loaded)
                    loadBatch2();
                if(!batch3Loaded)
                    loadBatch3();
                state = "SCROLLER";
                resetVars();
                break;
            case 's':
                state = "SHADER";
                resetVars();
                break;
            default:
                break;
        }
    }
    
    public void loadBatch1() {
        horseShape = loadShape("horse_no_hair_w_color_2.obj");
        narwhalShape = loadShape("narwhal.obj");
        narwhalRainbowShape = loadShape("narwhal_rainbow.obj");
        backgroundLandShape = loadShape("background_grass_sky.obj");
        backgroundWaterShape = loadShape("background_water_sky.obj");
        birdShape = loadShape("bird.obj");
        cloudShape = loadShape("cloud.obj");
        fishShape = loadShape("fish.obj");
        flowerShape = loadShape("flower.obj");
        treeShape = loadShape("tree.obj");
        batch1Loaded = true;
//        horseRainbowShape = loadShape("horse_rainbow.obj");
//        batch2Loaded = true;
//        cup = loadShape("cup.obj");
//        babyUnicornShape = loadShape("baby_unicorn_final.obj");
//        backgroundLandSnowShape = loadShape("background_grass_snow.obj");
//        backgroundSnowShape = loadShape("background_snow.obj");
//        backgroundSnowWaterShape = loadShape("background_snow_water.obj");
//        iceShelfShape = loadShape("ice_shelf.obj");
//        icebergShape = loadShape("iceberg.obj");
//        poopShape = loadShape("poop.obj");
//        batch3Loaded = true;
    }
    
    public void loadBatch2() {
        horseRainbowShape = loadShape("horse_rainbow.obj");
        backgroundSnowShape = loadShape("background_snow.obj");
        backgroundSnowWaterShape = loadShape("background_snow_water.obj");
        iceShelfShape = loadShape("ice_shelf.obj");
        icebergShape = loadShape("iceberg.obj");
        batch2Loaded = true;
    }
    
    public void loadBatch3() {
        babyUnicornShape = loadShape("baby_unicorn_final.obj");
        backgroundLandSnowShape = loadShape("background_grass_snow.obj");
        poopShape = loadShape("poop.obj");
        batch3Loaded = true;
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

        pointLight(200, 200, 200, width / 2, height / 2, 200);

        translate(width / 2, height / 2);
        rotateY(angle);
        //rotateX(angle);
        rotateZ(angle);

        //coneOnCube.setFill(color(50 + decay, 50, 150));
        babyUnicornShape.setFill(color(50 + decay, 50, 150));
        scale(15);
        //shape(coneOnCube);
        shape(babyUnicornShape);

        angle += 0.01 + decay *0.001 ;
    }
    
    public void narwhalScene() {
//        int lowVolume = (int) (abs(player.left.get(0)) * 100);
//        if (lowVolume > 10 && decay < 100) //lowVolume only used once, so could just lookup here.
//        {
//            decay += 150;
//        }
//
//        decay = (int) (decay / 1.5);
        
        //background(0x54, 0xff, 0x9f); // sea green
        background(0);
        pushMatrix();
        scale(2);
        text(framesIntoScene, width/4, height/2); // show value of variable, if wanted
        //TODO: Remove all this displaying text stuff.
//        text("Number of vertices: " + narwhalShape.getVertexCount(), width/2, height/2); // How many vertices
//        text("Number of children: " + narwhalShape.getChildCount(), width/2, height/2+100);
//        int childVertixCount = 0;
//        for(int i =0; i < narwhalShape.getChildCount(); i++ )
//        {
//            if(narwhalShape.getChild(i).getVertexCount() > 1)
//                childVertixCount++;
//            if(narwhalShape.getChild(i).getName() != null && narwhalShape.getChild(i).getName().toLowerCase().contains("tail") )
//               text("Found it!", width/2, height/2+150); 
//        }
//        text("Number of children with 2 or more vertices: " + childVertixCount, width/2, height/2+200);
        //text(player.length(), width/2, height/2); //Show total length of song in ms. Last check was over 65k
        //text(width, width/2, height/2); //Show width

        //Light is supposed to be mostly from above, with a fair amount of scattered light
        lightFromAbove();
        
        popMatrix();
        pushMatrix();
        
        translate(width / 2, (float)(height*0.5), -50);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(64);
        shape(backgroundWaterShape);
        popMatrix(); //end of background water
        pushMatrix();

        //beginning of Narwhal
        translate(width, height * 6 / 8 );
        rotateY(radians(90));
        rotateX(radians(180));
        //rotateZ(radians(180));
        translate(translateX, translateY, translateZ);

        //narwhalShape.setFill(color(50 + decay, 50, 150));
        scale(64);
        shape(narwhalShape);
        popMatrix();

        translateZ += 5;
        
        pushMatrix(); // Begin background shapes 2 clouds, 3 fish
        
        translate(width / 4, height / 2);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(96);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width * 3 / 5, height / 3);
        rotateY(radians(180));
        rotateZ(radians(180));
        scale(86);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(translateZ*2+25, height -(sin((framesIntoScene + 90)/5) * 5)-50);
        rotateY(radians(270 + cos(framesIntoScene) * 5));
        rotateZ(radians(180));
        fishShape.setFill(color(72,119,160));
        scale(96);
        
        shape(fishShape);
        
        scale(16);
        
        popMatrix();
        
        pushMatrix();
        
        translate(translateZ*2-25, height -(sin(framesIntoScene/5) * 5)+50);
        rotateY(radians(270 + sin(framesIntoScene) * 5));
        rotateZ(radians(180));
        fishShape.setFill(color(26,73,114));
        scale(96);
        
        shape(fishShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(translateZ*2-50, height -(cos((framesIntoScene)/5) * 10));
        rotateY(radians(270 + cos(framesIntoScene) * 7));
        rotateZ(radians(180));
        fishShape.setFill(color(178,87,145));
        scale(96);
        
        shape(fishShape);
        
        scale(16);
        
        popMatrix(); // end background objects
    }    
    
    public void horseScene() {
        //background(0x87, 0xce, 0xff); //sky blue
        background(0);
        text(framesIntoScene, width/2, height/2); // show value of variable, if wanted
        
        pushMatrix();
        
        //Light is supposed to be mostly from above, with a fair amount of scattered light
        lightFromAbove();
        
        translate(width / 2, height);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(128);
        shape(backgroundLandShape);
        popMatrix();
        pushMatrix();
        
        translate(width / 2, height / 2);
        rotateY(radians(90));
        rotateZ(radians(180));
        translate(translateX, translateY, translateZ);
        //horseShape.setFill(color(205,133,63));
        //horseShape.setFill(color(255));
        scale(96);
        shape(horseShape);
        
        popMatrix();
        pushMatrix();
        scale(16);
        //fill(255);
        //scale((float)1/16);
        //angle += 0.01;
        translateZ += 5;
        translateY += sin(framesIntoScene) * 25;
        
        popMatrix();
        pushMatrix();
        
        translate(width-translateZ*2, height / 2);
        rotateY(radians(90 + sin(framesIntoScene) * 2));
        rotateZ(radians(180 + sin(framesIntoScene) * 5));
        birdShape.setFill(color(72,119,160));
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        
        pushMatrix();
        
        translate(width-translateZ*2-50, height / 2-45);
        rotateY(radians(90 + cos(framesIntoScene) * 1));
        rotateZ(radians(180 + cos(framesIntoScene) * 7));
        birdShape.setFill(color(26,73,114));
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(width-translateZ*2-65, height / 2+30);
        rotateY(radians(90 + cos(framesIntoScene) * rand.nextInt(10)));
        rotateZ(radians(180 + sin(framesIntoScene) * rand.nextInt(10)));
        birdShape.setFill(color(178,87,145)); //Pink bird
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(width / 3, height / 2);
        rotateY(radians(90));
        rotateZ(radians(180));
        scale(96);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width * 3 / 4, height / 3);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(86);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width / 3, height * 3 / 4);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(50);
        
        shape(flowerShape);
        
        popMatrix();
    }
    
    public void narwhalRainbowApproach()
    {
        background(0x54, 0xff, 0x9f); // sea green
        pushMatrix();
        //Light is supposed to be mostly from above, with a fair amount of scattered light
        lightFromAbove();
        popMatrix();
        pushMatrix();
        
        translate(width / 2, (float)(height*0.5), -50);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(64);
        shape(backgroundWaterShape);
        popMatrix(); //end of background water
        pushMatrix();
        
        translate(width / 4, height / 2);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(96);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width * 3 / 5, height / 3);
        rotateY(radians(180));
        rotateZ(radians(180));
        scale(86);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width-translateX*3, height -(sin(framesIntoScene/5) * 5));
        rotateY(radians(90)); // + (cos(framesIntoScene) * 5));
        rotateZ(radians(180));
        //rotateX(radians(180));
        //translate(width/2, height -(sin((framesIntoScene + 90)/5) * 5)-50);
        
        fishShape.setFill(color(72,119,160));
        scale(96);
        
        shape(fishShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(width-translateX*3+25, height -(sin(framesIntoScene/5) * 5)+50,-10);
        rotateY(radians(90));
        rotateZ(radians(180));
        fishShape.setFill(color(26,73,114));
        scale(96);
        
        shape(fishShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(width-translateX*3-25, height -(cos((framesIntoScene)/5) * 10)-50,10);
        rotateY(radians(90));
        rotateZ(radians(180));
        fishShape.setFill(color(178,87,145));
        scale(96);
        
        shape(fishShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        //Brighten the rainbow a bit
        //spotLight(v1, v2, v3, x, y, z, nx, ny, nz, angle, concentration)
        //So it's fairly far out to get a wider spotlight
        //TODO: Does this actually help anything? Perhaps delete.
        spotLight(51, 102, 126, 50, 50, 5000, 0, 0, -1, PI/16, 600); 
        translate((float)(width-translateX*3), (float)(height * 6 / 8));
        
        //Narwhal faces left
        rotateY(radians(90));
        rotateX(radians(-180));
        //rotateZ(radians(180));
        
        //narwhalShape.setFill(color(50 + decay, 50, 150));
        scale(64);
        shape(narwhalShape);
        
        //rotate and scale back to default
        popMatrix();
        
        pushMatrix();
        scale(2);
        fill(255);
        text(framesIntoScene, width/2, height/2); // show value of variable, if wanted
        popMatrix();
        scale(2);

        //Display the rainbow, slowly scrolling it onto the screen (and then stopping)
        //width*0.4 might be a bit early with translateX*3. Set the number a bit higher, maybe?
        rainbow(-(int)(width*0.9)+translateX*5,height/2);
        if(translateX < height/2 )
            translateX += 1;
    }
    
    public void horseRainbowApproach()
    {
        background(0x87, 0xce, 0xff); //sky blue
        pushMatrix();

        //Light is supposed to be mostly from above, with a fair amount of scattered light
        lightFromAbove();
        popMatrix();
        pushMatrix();
        
        translate(width / 2, height);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(128);
        shape(backgroundLandShape);
        popMatrix();
        
        pushMatrix();
        
        translate((float)(width * 0.1), height / 2);
        rotateY(radians(90));
        rotateZ(radians(180));
        if(framesIntoScene<150)
            translate(0, sin(framesIntoScene) * 5, framesIntoScene*5);
        else
            translate(0, 0, 150*5);

        scale(96);
        shape(horseShape);
        
        //rotate and scale back to default
        popMatrix();
        pushMatrix();
        
        translate(framesIntoScene*10, height / 2, 50);
        rotateY(radians(-90 + sin(framesIntoScene) * 2));
        rotateZ(radians(180 + sin(framesIntoScene) * 5));
        birdShape.setFill(color(72,119,160));
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        
        pushMatrix();
        
        translate(framesIntoScene*10-50, height / 2-45, 50);
        rotateY(radians(-90 + cos(framesIntoScene) * 1));
        rotateZ(radians(180 + cos(framesIntoScene) * 7));
        birdShape.setFill(color(26,73,114));
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(framesIntoScene*10-65, height / 2+30, 50);
        rotateY(radians(-90 + cos(framesIntoScene) * rand.nextInt(10)));
        rotateZ(radians(180 + sin(framesIntoScene) * rand.nextInt(10)));
        birdShape.setFill(color(178,87,145)); //Pink bird
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(width / 3, height / 2);
        rotateY(radians(90));
        rotateZ(radians(180));
        scale(96);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width * 3 / 4, height / 3);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(86);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width / 3, height * 3 / 4);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(50);
        
        shape(flowerShape);
        
        popMatrix();
        pushMatrix();
        scale(2);
        fill(255);
        text(framesIntoScene, 0, 275); // show value of variable, if wanted

        popMatrix();
        //Display the rainbow. This should be the stopping point for the rainbow in the narwhal scene
        scale(2);
        if(framesIntoScene < 100)
            rainbow(width-framesIntoScene*4, height/2);
        else
            rainbow(width-400,height/2);
        /*rainbow(-(int)(width*0.9)+translateX*5,height/2);
        if(translateX < height/2 )
            translateX += 1;*/
    }   

    public void rainbow(int centerX, int centerY) {
        //TODO: Make rainbow thicker, with speed multiplier working better, and center not being black.
        int speedMultiplier = 18; //how quickly to go through the rainbow
        int ellipseSizeMultiplier = 16; //I think this, combined with i < height/x, 
            //have to come out to height/2 (or whatever the size of the inner ellipse is. multiplier 8, height/16 works for height/2
            //And the bigger the multiplier, the faster the loading, but the less smooth the rainbow transition
        int rainbowState = 0;
        int r = 255;
        int rainbowG = 0;
        int b = 0;
        for (int i = 0; i < height/16; i++) {
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
            stroke(r, rainbowG, b, 5); //Stroke should follow the rainbow. Opacity number is a guess. Will likely change if lighting changes.
            ellipse(centerX, centerY, height*2 - i *ellipseSizeMultiplier, height*2 - i*ellipseSizeMultiplier);
        }
        //fill(0x87, 0xce, 0xff); //Sky blue center
        fill(0x33, 0xce, 0xee); //Sky blue center
        ellipse(centerX, centerY, height/2, height/2);
    }

    public void narwhalSuck()
    {
        background(0);
        pushMatrix();
        
        //Light is supposed to be mostly from above, with a fair amount of scattered light
        lightFromAbove();
        
        popMatrix();
        pushMatrix();
        
        //Display background
        translate(width / 2, (float)(height*0.5), -50);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(64);
        shape(backgroundWaterShape);
        popMatrix(); //end of background water
        pushMatrix();
        
        translate(width / 4, height / 2);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(96);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width * 3 / 5, height / 3);
        rotateY(radians(180));
        rotateZ(radians(180));
        scale(86);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width-translateX*3, height -(sin(framesIntoScene/5) * 5));
        rotateY(radians(90)); // + (cos(framesIntoScene) * 5));
        rotateZ(radians(180));
        //rotateX(radians(180));
        //translate(width/2, height -(sin((framesIntoScene + 90)/5) * 5)-50);
        
        fishShape.setFill(color(72,119,160));
        scale(96);
        
        shape(fishShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(width-translateX*3+25, height -(sin(framesIntoScene/5) * 5)+50,-10);
        rotateY(radians(90));
        rotateZ(radians(180));
        fishShape.setFill(color(26,73,114));
        scale(96);
        
        shape(fishShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(width-translateX*3-25, height -(cos((framesIntoScene)/5) * 10)-50,10);
        rotateY(radians(90));
        rotateZ(radians(180));
        fishShape.setFill(color(178,87,145));
        scale(96);
        
        shape(fishShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();

        //beginning of Narwhal
        translate(width, height * 6 / 8 );
        rotateY(radians(90));
        rotateX(radians(180));
        //rotateZ(radians(180));
        translateZ = 610;
        translate(translateX, translateY, translateZ);

        //narwhalShape.setFill(color(50 + decay, 50, 150));
        scale(64);
        if(framesIntoScene < 130)
            shape(narwhalShape);
        else
            shape(narwhalRainbowShape);
        
        popMatrix();
        rainbowTriangle(width/2, height/2+210, -1, 60);
    }
    
    public void horseSuck()
    {
        background(0);
        pushMatrix();
        
        //Light is supposed to be mostly from above, with a fair amount of scattered light
        lightFromAbove();
        
        translate(width / 2, height);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(128);
        shape(backgroundLandShape);
        popMatrix();
        pushMatrix();
        
        translate(width / 2, height / 2);
        rotateY(radians(90));
        rotateZ(radians(180));
        translate(translateX, translateY, translateZ);
        //horseShape.setFill(color(205,133,63));
        //horseShape.setFill(color(255));
        scale(96);
        if(framesIntoScene < 130)
            shape(horseShape);
        else
            shape(horseRainbowShape);
        //shape(horseShape);
        popMatrix();
        pushMatrix();
        
        translate(translateZ*2, height / 2);
        rotateY(radians(90 + sin(framesIntoScene) * 2));
        rotateZ(radians(180 + sin(framesIntoScene) * 5));
        birdShape.setFill(color(72,119,160));
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        
        pushMatrix();
        
        translate(translateZ*2-50, height / 2-45);
        rotateY(radians(90 + cos(framesIntoScene) * 1));
        rotateZ(radians(180 + cos(framesIntoScene) * 7));
        birdShape.setFill(color(26,73,114));
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(translateZ*2-65, height / 2+30);
        rotateY(radians(90 + cos(framesIntoScene) * rand.nextInt(10)));
        rotateZ(radians(180 + sin(framesIntoScene) * rand.nextInt(10)));
        birdShape.setFill(color(178,87,145)); //Pink bird
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(width / 3, height / 2);
        rotateY(radians(90));
        rotateZ(radians(180));
        scale(96);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width * 3 / 4, height / 3);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(86);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width / 3, height * 3 / 4);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(50);
        
        shape(flowerShape);
        
        popMatrix();
        pushMatrix();
        scale(2);
        fill(255);
        text(framesIntoScene, 0, 275); // show value of variable, if wanted

        popMatrix();
        rainbowTriangle(width/2, height/2, 1, 50);
    }
    
    public void narwhalSearch()
    {
        background(0);
        pushMatrix();
        scale(2);
        text(framesIntoScene, width/4, height/2); // show value of variable, if wanted
        //TODO: Remove all this displaying text stuff.
        //text(player.length(), width/2, height/2); //Show total length of song in ms. Last check was over 65k
        //text(width, width/2, height/2); //Show width

        //Light is supposed to be mostly from above, with a fair amount of scattered light
        lightFromAbove();
        
        popMatrix();
        pushMatrix();
        
        translate(width / 2, (float)(height*0.5), -50);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(64);
        shape(backgroundWaterShape);
        popMatrix(); //end of background water
        pushMatrix();

        //beginning of Narwhal
        translate(width, height * 6 / 8 );
        rotateY(radians(70));
        rotateX(radians(180));
        //rotateZ(radians(180));
        if(framesIntoScene < 30)
            translate(0, framesIntoScene, framesIntoScene * 10); //-(sin((framesIntoScene + 90)/5) * 5)
        else if(framesIntoScene >= 30 && framesIntoScene < 60 )
            translate(0, -framesIntoScene+60, framesIntoScene * 10);
        else if(framesIntoScene >= 60)
            translate(0, translateY, framesIntoScene * 10);
        
        //narwhalShape.setFill(color(50 + decay, 50, 150));
        scale(64);
        shape(narwhalRainbowShape);
        popMatrix();

        translateZ += 5;
        
        pushMatrix();
        
        translate(width / 4, height / 2);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(96);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width * 3 / 5, height / 3);
        rotateY(radians(180));
        rotateZ(radians(180));
        scale(86);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate((width * 1 / 5)-framesIntoScene, height/2 -(sin((framesIntoScene + 90)/5) * 5));
        rotateY(radians(160));
        rotateZ(radians(180));
        rotateX(radians(-20));
        scale(86);
        
        shape(icebergShape);
        
        popMatrix();
        pushMatrix();
        
        translate((width * 3 / 5)+framesIntoScene, height/2 -(cos((framesIntoScene + 90)/5) * 5));
        rotateY(radians(160));
        rotateZ(radians(180));
        rotateX(radians(-20));
        scale(60);
        
        shape(icebergShape);
        
        popMatrix();
        pushMatrix();
        
        translate(translateZ*2+25, height -(sin((framesIntoScene + 90)/5) * 5)-50);
        rotateY(radians(270 + cos(framesIntoScene) * 5));
        rotateZ(radians(180));
        fishShape.setFill(color(72,119,160));
        scale(96);
        
        shape(fishShape);
        
        scale(16);
        
        popMatrix();
        
        pushMatrix();
        
        translate(translateZ*2-25, height -(sin(framesIntoScene/5) * 5)+50);
        rotateY(radians(270 + sin(framesIntoScene) * 5));
        rotateZ(radians(180));
        fishShape.setFill(color(26,73,114));
        scale(96);
        
        shape(fishShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(translateZ*2-50, height -(cos((framesIntoScene)/5) * 10));
        rotateY(radians(270 + cos(framesIntoScene) * 7));
        rotateZ(radians(180));
        fishShape.setFill(color(178,87,145));
        scale(96);
        
        shape(fishShape);
        
        scale(16);
        
        popMatrix();
    }
    
    public void horseSearch()
    {
        //background(0x87, 0xce, 0xff); //sky blue
        background(0);
        text(framesIntoScene, width/2, height/2); // show value of variable, if wanted
        
        pushMatrix();
        
        //Light is supposed to be mostly from above, with a fair amount of scattered light
        lightFromAbove();
        
        translate(width / 2, height);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(128);
        shape(backgroundLandSnowShape);
        popMatrix();
        pushMatrix();
        
        translate(width / 2, height / 2);
        if(framesIntoScene<20)
            rotateY(radians(90));
        if(framesIntoScene>=20 && framesIntoScene< 55)
            rotateY(radians(90+framesIntoScene-19));
        if(framesIntoScene>=55)
            rotateY(radians(90+35));
        rotateZ(radians(180));
        //if(framesIntoScene<30)
        //    translate(translateX, translateY, framesIntoScene*10);
        if(framesIntoScene<30)
            translate(translateX, translateY, framesIntoScene*10);
        else
            translate(translateX, translateY + (framesIntoScene-30)/5, framesIntoScene*10);
        //horseShape.setFill(color(205,133,63));
        //horseShape.setFill(color(255));
        scale(64);
        shape(horseRainbowShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width-translateZ*2, height / 2);
        rotateY(radians(90 + sin(framesIntoScene) * 2));
        rotateZ(radians(180 + sin(framesIntoScene) * 5));
        birdShape.setFill(color(72,119,160));
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        
        pushMatrix();
        
        translate(width-translateZ*2-50, height / 2-45);
        rotateY(radians(90 + cos(framesIntoScene) * 1));
        rotateZ(radians(180 + cos(framesIntoScene) * 7));
        birdShape.setFill(color(26,73,114));
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(width-translateZ*2-65, height / 2+30);
        rotateY(radians(90 + cos(framesIntoScene) * rand.nextInt(10)));
        rotateZ(radians(180 + sin(framesIntoScene) * rand.nextInt(10)));
        birdShape.setFill(color(178,87,145)); //Pink bird
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(width / 3, height / 2);
        rotateY(radians(90));
        rotateZ(radians(180));
        scale(96);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width * 3 / 4, height / 3);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(86);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width / 3, height * 3 / 4);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(50);
        
        shape(flowerShape);
        
        popMatrix();
        pushMatrix();
        scale(16);
        //fill(255);
        //scale((float)1/16);
        //angle += 0.01;
        translateZ += 5;
        translateY += sin(framesIntoScene) * 25;
        
        popMatrix();
        pushMatrix();
        
        translate(width-translateZ*2, height / 2);
        rotateY(radians(90 + sin(framesIntoScene) * 2));
        rotateZ(radians(180 + sin(framesIntoScene) * 5));
        birdShape.setFill(color(72,119,160));
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        
        pushMatrix();
        
        translate(width-translateZ*2-50, height / 2-45);
        rotateY(radians(90 + cos(framesIntoScene) * 1));
        rotateZ(radians(180 + cos(framesIntoScene) * 7));
        birdShape.setFill(color(26,73,114));
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(width-translateZ*2-65, height / 2+30);
        rotateY(radians(90 + cos(framesIntoScene) * rand.nextInt(10)));
        rotateZ(radians(180 + sin(framesIntoScene) * rand.nextInt(10)));
        birdShape.setFill(color(178,87,145)); //Pink bird
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(width / 3, height / 2);
        rotateY(radians(90));
        rotateZ(radians(180));
        scale(96);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width * 3 / 4, height / 3);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(86);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width / 3, height * 3 / 4);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(50);
        
        shape(flowerShape);
        
        popMatrix();
    }
    
    public void narwhalSearch2()
    {
        background(0);
        pushMatrix();
        scale(2);
        text(framesIntoScene, width/4, height/2); // show value of variable, if wanted
        //TODO: Remove all this displaying text stuff.
        //text(player.length(), width/2, height/2); //Show total length of song in ms. Last check was over 65k
        //text(width, width/2, height/2); //Show width

        //Light is supposed to be mostly from above, with a fair amount of scattered light
        lightFromAbove();
        
        popMatrix();
        pushMatrix();
        
        translate(width / 2, (float)(height*0.5), -50);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(64);
        shape(backgroundWaterShape);
        popMatrix(); //end of background water
        pushMatrix();

        //beginning of Narwhal
        translate(width, height * 6 / 8 );
        rotateY(radians(110));
        rotateX(radians(180));
        //rotateZ(radians(180));
        if(framesIntoScene < 30)
            translate(0, framesIntoScene, framesIntoScene * 10); //-(sin((framesIntoScene + 90)/5) * 5)
        else if(framesIntoScene >= 30 && framesIntoScene < 60 )
            translate(0, -framesIntoScene+60, framesIntoScene * 10);
        else if(framesIntoScene >= 60)
            translate(0, translateY, framesIntoScene * 10);
        
        //narwhalShape.setFill(color(50 + decay, 50, 150));
        scale(64);
        shape(narwhalRainbowShape);
        popMatrix();

        translateZ += 5;
        
        pushMatrix();
        
        translate(width / 4, height / 2);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(96);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width * 3 / 5, height / 3);
        rotateY(radians(180));
        rotateZ(radians(180));
        scale(86);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate((width * 1 / 5)-framesIntoScene, height/2 -(sin((framesIntoScene + 90)/5) * 5));
        rotateY(radians(160));
        rotateZ(radians(180));
        rotateX(radians(-20));
        scale(86);
        
        shape(icebergShape);
        
        popMatrix();
        pushMatrix();
        
        translate((width * 3 / 5)+framesIntoScene, height/2 -(cos((framesIntoScene + 90)/5) * 5));
        rotateY(radians(160));
        rotateZ(radians(180));
        rotateX(radians(-20));
        scale(60);
        
        shape(icebergShape);
        
        popMatrix();
        pushMatrix();
        
        translate(translateZ*2+25, height -(sin((framesIntoScene + 90)/5) * 5)-50);
        rotateY(radians(270 + cos(framesIntoScene) * 5));
        rotateZ(radians(180));
        fishShape.setFill(color(72,119,160));
        scale(96);
        
        shape(fishShape);
        
        scale(16);
        
        popMatrix();
        
        pushMatrix();
        
        translate(translateZ*2-25, height -(sin(framesIntoScene/5) * 5)+50);
        rotateY(radians(270 + sin(framesIntoScene) * 5));
        rotateZ(radians(180));
        fishShape.setFill(color(26,73,114));
        scale(96);
        
        shape(fishShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(translateZ*2-50, height -(cos((framesIntoScene)/5) * 10));
        rotateY(radians(270 + cos(framesIntoScene) * 7));
        rotateZ(radians(180));
        fishShape.setFill(color(178,87,145));
        scale(96);
        
        shape(fishShape);
        
        scale(16);
        
        popMatrix();
    }
        
   public void horseSearch2()
    {
        //background(0x87, 0xce, 0xff); //sky blue
        background(0);
        text(framesIntoScene, width/2, height/2); // show value of variable, if wanted
        
        pushMatrix();
        
        //Light is supposed to be mostly from above, with a fair amount of scattered light
        lightFromAbove();
        
        translate(width / 2, height);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(128);
        shape(backgroundLandSnowShape);
        popMatrix();
        pushMatrix();
        
        translate(width / 2, height / 2);
        if(framesIntoScene<20)
            rotateY(radians(90));
        if(framesIntoScene>=20 && framesIntoScene < 55)
            rotateY(radians(90-framesIntoScene+19));
        if(framesIntoScene>= 55)
            rotateY(radians(90-35));
        rotateZ(radians(180));
        translate(translateX, translateY, framesIntoScene*10);
        //horseShape.setFill(color(205,133,63));
        //horseShape.setFill(color(255));
        scale(96);
        shape(horseRainbowShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width-translateZ*2, height / 2);
        rotateY(radians(90 + sin(framesIntoScene) * 2));
        rotateZ(radians(180 + sin(framesIntoScene) * 5));
        birdShape.setFill(color(72,119,160));
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        
        pushMatrix();
        
        translate(width-translateZ*2-50, height / 2-45);
        rotateY(radians(90 + cos(framesIntoScene) * 1));
        rotateZ(radians(180 + cos(framesIntoScene) * 7));
        birdShape.setFill(color(26,73,114));
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(width-translateZ*2-65, height / 2+30);
        rotateY(radians(90 + cos(framesIntoScene) * rand.nextInt(10)));
        rotateZ(radians(180 + sin(framesIntoScene) * rand.nextInt(10)));
        birdShape.setFill(color(178,87,145)); //Pink bird
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(width / 3, height / 2);
        rotateY(radians(90));
        rotateZ(radians(180));
        scale(96);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width * 3 / 4, height / 3);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(86);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width / 3, height * 3 / 4);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(50);
        
        shape(flowerShape);
        
        popMatrix();
        pushMatrix();
        scale(16);
        //fill(255);
        //scale((float)1/16);
        //angle += 0.01;
        translateZ += 5;
        translateY += sin(framesIntoScene) * 25;
        
        popMatrix();
        pushMatrix();
        
        translate(width-translateZ*2, height / 2);
        rotateY(radians(90 + sin(framesIntoScene) * 2));
        rotateZ(radians(180 + sin(framesIntoScene) * 5));
        birdShape.setFill(color(72,119,160));
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        
        pushMatrix();
        
        translate(width-translateZ*2-50, height / 2-45);
        rotateY(radians(90 + cos(framesIntoScene) * 1));
        rotateZ(radians(180 + cos(framesIntoScene) * 7));
        birdShape.setFill(color(26,73,114));
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(width-translateZ*2-65, height / 2+30);
        rotateY(radians(90 + cos(framesIntoScene) * rand.nextInt(10)));
        rotateZ(radians(180 + sin(framesIntoScene) * rand.nextInt(10)));
        birdShape.setFill(color(178,87,145)); //Pink bird
        scale(96);
        
        shape(birdShape);
        
        scale(16);
        
        popMatrix();
        pushMatrix();
        
        translate(width / 3, height / 2);
        rotateY(radians(90));
        rotateZ(radians(180));
        scale(96);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width * 3 / 4, height / 3);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(86);
        
        shape(cloudShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width / 3, height * 3 / 4);
        //rotateY(radians(90));
        rotateZ(radians(180));
        scale(50);
        
        shape(flowerShape);
        
        popMatrix();
    }
    
    public void horseNarwhalMeeting()
    {
        background(0);
        pushMatrix();
        
        //Light is supposed to be mostly from above, with a fair amount of scattered light
        lightFromAbove();
        
        //Background############################################################
        translate(width / 2, (float)(height*0.5), -50);
        //rotateY(radians(90));
        rotateX(radians(-10));
        rotateZ(radians(180));
        scale(32);
        shape(backgroundSnowWaterShape);
        popMatrix();
        pushMatrix();
        
        //Ice shelf#############################################################
        translate(width / 2, (float)(height*0.5), +50);
        rotateX(radians(-10));
        rotateZ(radians(180));
        scale(32);
        shape(iceShelfShape);
        
        popMatrix();
        pushMatrix();
        
        //Horse#################################################################
        //sin(framesIntoScene) * 25 is the hopping
        if(framesIntoScene * 5 < width/2)
            translate(width, height / 2 - 200 + (sin(framesIntoScene) * 15), 200);
        else
            translate(width, height / 2 - 200, 200);
        //if(framesIntoScene<20)
        rotateY(radians(-90));
        //if(framesIntoScene>=20 && framesIntoScene < 55)
        //    rotateY(radians(90-framesIntoScene+19));
        //if(framesIntoScene>= 55)
        //    rotateY(radians(90-35));
        rotateZ(radians(180));
        //The horse is travelling from width to width/2 in the above section. So probably 1920 to 960
        if(framesIntoScene * 5 < width/2)
            translate(translateX, translateY, framesIntoScene * 5);
        else
            translate(translateX, translateY, 960);
        //horseShape.setFill(color(205,133,63));
        //horseShape.setFill(color(255));
        scale(48);
        shape(horseRainbowShape);
        
        popMatrix();
        pushMatrix();
        
        //Narwhal###############################################################
        translate(0, height * 6 / 8 );
        rotateY(radians(-90));
        rotateX(radians(180));
        //rotateZ(radians(180));
        if(framesIntoScene < 30)
            translate(0, framesIntoScene, (framesIntoScene * 5)); //-(sin((framesIntoScene + 90)/5) * 5)
        else if(framesIntoScene >= 30 && framesIntoScene < 60 )
            translate(0, -framesIntoScene+60, framesIntoScene + 120);
        else if(framesIntoScene >= 60)
            translate(0, translateY, (framesIntoScene) + 150);
        
        //narwhalShape.setFill(color(50 + decay, 50, 150));
        scale(64);
        shape(narwhalRainbowShape);
        
        popMatrix();
        //pushMatrix();
    }
    
    public void iceShelfBreak()
    {
        background(0);
        pushMatrix();
        
        //Light is supposed to be mostly from above, with a fair amount of scattered light
        lightFromAbove();
        
        translate(width / 2, (float)(height*0.5), -50);
        //rotateY(radians(90));
        rotateX(radians(-10));
        rotateZ(radians(180));
        scale(32);
        shape(backgroundSnowWaterShape);
        popMatrix();
        pushMatrix();
        
        if(framesIntoScene<50)
            translate(width / 2, (float)(height*0.5), +50);
        else
            translate(width / 2 - framesIntoScene + 49, (float)(height*0.5), +50);
        rotateX(radians(-10));
        rotateZ(radians(180));
        scale(32);
        shape(iceShelfShape);
        
        popMatrix();
        pushMatrix();
        
        translate(width / 2, height / 2 - 200, 200);
        //if(framesIntoScene<20)
            rotateY(radians(-90));
        //if(framesIntoScene>=20 && framesIntoScene < 55)
        //    rotateY(radians(90-framesIntoScene+19));
        //if(framesIntoScene>= 55)
        //    rotateY(radians(90-35));
        rotateZ(radians(180));
        if(framesIntoScene<50)
            translate(0, 0, 0);
        else
            translate(0, 0, framesIntoScene-49);
        //horseShape.setFill(color(205,133,63));
        //horseShape.setFill(color(255));
        scale(48);
        shape(horseRainbowShape);
        
        popMatrix();
        pushMatrix();
        
        //beginning of Narwhal
        translate(0, height * 6 / 8 );
        rotateY(radians(-90));
        rotateX(radians(180));
        //rotateZ(radians(180));
        if(framesIntoScene < 30)
            translate(0, framesIntoScene, (framesIntoScene * 5)); //-(sin((framesIntoScene + 90)/5) * 5)
        else if(framesIntoScene >= 30 && framesIntoScene < 60 )
            translate(0, -framesIntoScene+60, framesIntoScene + 120);
        else if(framesIntoScene >= 60)
            translate(0, translateY, (framesIntoScene) + 150);
        
        //narwhalShape.setFill(color(50 + decay, 50, 150));
        scale(64);
        shape(narwhalRainbowShape);
        
        popMatrix();
        //pushMatrix();
    }
    
    public void sexyTimes()
    {
        background(0);
        pushMatrix();
        
        //Light is supposed to be mostly from above, with a fair amount of scattered light
        lightFromAbove();
        
        popMatrix();
        pushMatrix();
    }
    
    public void babyUnicorn()
    {
        background(0);
        pushMatrix();
        
        //Light is supposed to be mostly from above, with a fair amount of scattered light
        lightFromAbove();
        
        popMatrix();
        pushMatrix();
    }
    
    public void credits()
    {
        background(0);
        pushMatrix();
        
        //Light is supposed to be mostly from above, with a fair amount of scattered light
        lightFromAbove();
        
        popMatrix();
        pushMatrix();
    }
    
    /**
     * Creates a rainbow triangle that fades in, going to the passed start point.
     * @param startX
     * @param startY 
     */
    public void rainbowTriangle(int startX, int startY, int direction, int frameDelay)
    { //-1 makes it come in from the left. 1 from the right Well, theoretically would be, if that's done. So TODO.
        if(framesIntoScene < frameDelay)
            return;
        int speedMultiplier = millis() % 8 + 8;//16; //how quickly to go through the rainbow
        int rainbowState = 0;
        int r = 255;
        int rainbowG = 0;
        int b = 0;
        for (int i = 1000; i >= 0 ; i--) { //Not sure if height/16 makes any sense at all for an end
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
            stroke(r, rainbowG, b);
            if(100 - framesIntoScene < i/30)
            {
                //stroke(0);
                //line(x1, y1, x2, y2)
                line(startX+i*direction, startY-(i/4), startX+i*direction, startY-i);
                //line(startX+i, startY-(i/4), startX+i, startY-i);
            }
        }
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
    
    public void lightFromAbove() {
        //directionalLight(v1, v2, v3, nx, ny, nz)
        //Light is supposed to be mostly from above, with a fair amount of scattered light
        //pointLight(255, 255, 255, 0, -height, -200);
        //pointLight(255, 255, 255, 0, -height, 200);
        //directionalLight(255, 255, 255, 0, -height, 200);
        //ambientLight(255,255,255);
        lightFalloff((float)0.5, 0, 0);
        ambientLight(64, 64, 64);
        //pointLight(255, 255, 255, 0 + tempx, -1000 + tempy, 1000 + tempz);
        tempx += 0;
        tempy += 0;
        tempz += 0;
        
        //The following, with a tempz -= 5; leads to a solid sunset (well, dusk, anyway; no sun) effect
        //pointLight(255, 255, 255, 0 + tempx, -1000 + tempy, 1000 + tempz);
        pointLight(255, 255, 255, 0 + tempx, -1000 + tempy, 1000 + tempz);
        //default light values
        //ambientLight(128, 128, 128);
        directionalLight(200, 200, 200, 0, 0, -1); 
        //lightFalloff(1, 0, 0);
        lightSpecular(0, 0, 0);        
    }
    
}
