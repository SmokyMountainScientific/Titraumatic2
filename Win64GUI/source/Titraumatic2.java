import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import org.gicentre.utils.stat.*; 
import controlP5.*; 
import processing.serial.*; 
import java.io.*; 
import java.util.Arrays; 
import java.util.*; 
import java.lang.Math.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Titraumatic2 extends PApplet {

/*  Titraumatic_Mar2017
    new dropdown for pump mode
    Made with parts from TitraumaticGUI_3
    Automated com port search and connection
    Compatable with Energia sketch Titraumatic_Engergia_09_15_15
    New buttons and corosponding functions for lading a calibration file or reading the raw data in the "Buttons" tab
    New textfield in the "Text_Fields" tab to show the loaded calibration file
    New varialbes below for to hold the calibration coeficents
    Adjusted the "read_serial" function to use calibration    
*/

  //////////////////// Imports ////////////////
       // needed for charts
                     // needed for text boxes
  
                       // needed for BufferedWriter
              // need for BufferedWriter?
  

               // for raising a number to a power
  ///////////////////// Declaring classes  ////////////////////
  XYChart lineChart, calChart, trendChart; 
       int chartX =75;
       int chartY =100;
  ControlP5 cp5, cp5Com, cp5Txt, cp5list, cp5knob;
  Serial serial;
  Serial serialPort;
  DropdownList mode;
  Button dirButton, runPump, valDir; // runValve;   // buttons for manual mode
  Button runButton, savRun, loadCal, readCal, setA, setB, setC, read, reCalc, pause, readPh;
  Textfield speed, volume, injDelay, diam, calA, calB, calC, syrCal, setVol, target;  //injections, 
  Knob valveKnob;
  
  String stringSpeed;
  String strDia;
  String stringVolume;
//  String stringInjections;
  String valveMode = "1";  // new feb, 2017, replaces injections
//  int valveModeSel = 0;
  String stringMode;
  String Modetorun;
  String stringInjDelay;
  String valDirTxt = "To Reservoir";
  Boolean valDirB = false;
  Boolean logFile = false;  // probably only want to log titration data, but ...
  Boolean paused = false;

  //////// chart stuff ////////
//char[] strtochar;  // dont know if this is used
 char cData;
 float pHRead;
 String[] file = {"pH,reading","","","",""};
 float calSlope = -0.00354574f;     // reported slope = -250 mV / pH unit, 
                       //    m = -3300 mV full scale / (4095 digits full scale * 250 mV / pH unit)
 float calInt =  14.86f;                // Reported value of 1.75 volts at pH 7
                       //  b = 3500 mV at zero * 4095 digits full scale / 
 float avPh;
 float avVal;
 float sumSq;
 float[] calVal = {0,1,2}; 
 float[] calPh = {0,1,2};
 String[] strPh= {"0","1","2"};
 float pHthreshold = 0.01f;
 float fTarget;
 String sTarget;
int overlay = 0;
int calChartX = 340;
int calChartY= 100;
int calXsize = 240;
int calYsize = 240;

float[] nullData = {0};
float[] nullY = {0};
float p1;
float p2;
String sData3; // 11/25
int LINE_FEED = 10; 
int command;     // read variable from LaunchPad for status info

//String tab ="\t";
float[] xData = {0};   
float[] yData = {0};
float xRead;   
float yRead;
String[] file1 = new String[4];
//String file1 = "logdata.txt";
String file2;                  // save file path
String[] sData = new String[3];  //String sData;
String sData2 = " ";
char cData2;
int i =0;
int p = 0;           //stop signal

 String pHTxt = "No input";   // not used?
// String curVol = "10.0";
 float fVol;                              //volume from text box converted to float 
 float inVol;                             // volume dispensed
 float initVol;                        // initial volume in syringe

  /////// font stuff ////////
PFont font16, font12;
// font = createFont("ArialMT-16.vlw", 16);

boolean runState = false;           // is the pump running?
boolean boolDir = false;              // false = inject, true = withdraw
boolean comState = false;               // is the com port connected?
    boolean addBase;                  // next 5 lines used in pause routine
    boolean passedTarget = false;
    boolean belowTarget;
    float delta0;
    float delta1;
int modeState = 2;                    // use int for more than two modes
String comStatTxt = "not connected";
//String modeTxt = "Mode; continuous";
String dirTxt = "Direction: Inject";
String runStateTxt = "Run";
String[] comList ;               //A string to hold the ports in.
boolean Comselected = false;
int valvewritepos;
//float[] calCoefsA = {0,1};
int bkgd = 0xff0289A2;
int box1Xpos = 10;
int box1Ypos = 100;
int box1Xsize = 310;
int box1Ysize = 100;
 int y2 = box1Ypos+box1Ysize+20;
 int off1 = 50;
 int off2 = 25;
       String runPtxt = "Run Pump";
  //     String runVtxt = "Run Valve";
  boolean pumpRunState = false;
//  boolean valveRunState = false;
  String pauseTxt = "Pause";
  
      int j;  // used durring communications with launchpad
      char b;
   boolean contRead = false;  // continuous read of pH  from read button
  boolean newData = false;
  /////////////////////// setup ///////////////////////////
  public void setup() {
    
    surface.setResizable(true);

 //   PImage[] run_imgs = {loadImage("run_button1.png"),loadImage("run_button2.png"),loadImage("run_button3.png")};
   // PImage[] stop_imgs = {loadImage("stop_button1.png"),loadImage("stop_button2.png"),loadImage("stop_button3.png")};
font16 = createFont("ArialMT-16.vlw", 16);
font12 = createFont("ArialMT-16.vlw", 12);
textFont(font16);
//    setupknob();
    String sFile[] = loadStrings("calFile.txt");

    for (int j=1; j<4; j++){
    String tokens[] = split(sFile[j],',');
    strPh[j-1] = tokens[0];
    calPh[j-1] = PApplet.parseFloat(tokens[0]);
     calVal[j-1] = PApplet.parseFloat(tokens[1]);
     println("int "+j+": "+calVal[j-1]);
    }
    String tokens2[] = split(sFile[4],',');
    strDia = tokens2[1];
        chartsSetup();
        reCalc();
        setupTxtFields();
    setupComPort();      // defined in Com_Port tab
    connect();           // defined in Com_Port tab
    setupButtons();      // defined in Buttons tab
    frameRate(100);
    dropdownSetup();
        setupScreen();


  }             /// end of setup /////
  
  ///////////////// start of draw loop ////////
  
  public void draw() {
     background(bkgd);  //#0289A2);    //#162181);
     stroke(255);
     noFill();
//     textFont(font);  // 1/3/17
     text(comStatTxt, 65,25);
//     text(dirTxt, 65,55);
//     text (runStateTxt, 65, 120);
     textSize(16);
       text("Current Vol:",120,55);
       text(fVol,230,55);

       text("Measured pH:", 120,80);
       if (yRead==0){
          text("No Read",230,80);
       }else{
       pHRead = calSlope*yRead + calInt;
       text(pHRead,230,80);
       }
 //      println("pH read: "+pHRead);
//         text("7.00", 210, 125);
         textSize(14);

     if (modeState == 0) {    // Manual
 /*    logFile = false;
     bkgd = #8B9593;
       surface.setSize(350, 210);*/

 rect(box1Xpos,box1Ypos,box1Xsize,box1Ysize);


       text("Pump", 15,box1Ypos+17);
       text(dirTxt, 95,box1Ypos+17);

       text(runPtxt, 70, box1Ypos+70);
       text("Valve", 15, box1Ypos+37);
       text(valDirTxt,95,box1Ypos+37);
     }

     
     if (modeState == 1) {  // calibrate
if(contRead == true){
  readVal();
//  println("main tab, line 219");
}
//println("main tab, line 220");
 rect(box1Xpos,box1Ypos,box1Xsize,box1Ysize);
      text("Syringe", 15,box1Ypos+17);
  rect(box1Xpos,y2,box1Xsize,box1Ysize+70);
        text("pH Meter", 15, y2+17);

            textSize (16);
            text("Digital Reading: "+ PApplet.parseInt(yRead), 100, 2*box1Ypos+75);
            textSize (14);
            for(int m = 0; m<3; m++){
 
             text(PApplet.parseInt(calVal[m]),85, 2*box1Ypos+130+m*25);
            }
            text ("pH    Cal Value", 45,y2+85);
 ////// draw calibration chart ////////////
 setChartLims();
        fill(0xffEADFC9);               // background color
       rect(calChartX,calChartY,calXsize,calYsize);    // chart background
       fill(250,250,250);             //Chart heading color
       textSize(14);
       calChart.draw(calChartX,calChartY,calXsize,calYsize);    //early lineChart
       int xShift = 56;
       int yShift = 12;
       int deltaX = 66;
       int deltaY = 55;
       trendChart.draw(calChartX+xShift,calChartY+yShift,calXsize-deltaX,calYsize-deltaY);
       stroke(0);
       noFill();
       rect(calChartX+xShift,calChartY+yShift,calXsize-deltaX,calYsize-deltaY);
       stroke(255);
       
  
      }
     
     if (modeState == 2 || modeState == 3) {  // pH adjust

       text (runStateTxt, 455, 90);
       text (pauseTxt,495,90);
       fill(0xffEADFC9);               // background color
       rect(chartX,chartY, 445, 420);    // chart background
       fill(250,250,250);             //Chart heading color
       textSize(14);
       lineChart.draw(chartX, chartY, 400, 400);    
       
   
     }
     

/////////////////////////////// originally at end of void draw loop //////////       


if (runState == true){
//     logData(file1, "", false);   // log data to file 1, do not append, start new file
                                 // added 4/15/15
       read_serial(); 
        //////////////////////// new stuff below /////////////      
        if (xData.length>1 && xData.length==yData.length)
        {
          float yMinVal = min(yData);
          if (yMinVal > 4) {
            yMinVal = 4;
            }
          float yMaxVal = max(yData);
           if (yMaxVal < 10) {
            yMaxVal = 10;
            }
         
          lineChart.setMaxX(max(xData));
          lineChart.setMaxY(yMaxVal);
          lineChart.setMinX(min(xData));
          lineChart.setMinY(yMinVal);
          lineChart.setData(xData, yData);

        }
       }


  }
  ////////////   end of draw loop ///////////
  
 
// Buttons tab
// Titraumatic_3 GUI

 public void setupButtons() {
    PImage[] run_imgs = {loadImage("run_button1.png"),loadImage("run_button2.png"),loadImage("run_button3.png")};
    PImage[] stop_imgs = {loadImage("stop_button1.png"),loadImage("stop_button2.png"),loadImage("stop_button3.png")};
    PImage[] togs = {loadImage("tog_button1.png"),loadImage("tog_button2.png"),loadImage("tog_button3.png")};
 cp5 = new ControlP5(this);  
 
    runButton = cp5.addButton("runButton")
     .setPosition(450,35)
     .setSize(30,30)
    .setImages(run_imgs)
   ;
   
  runPump = cp5.addButton("runPump")
     .setPosition(20,box1Ypos+50)
     .setSize(30,30)
    .setImages(run_imgs)
   ;
/*     runValve = cp5.addButton("runValve")
 //    .setValue(10)
     .setPosition(20,y2+off1)
     .setSize(30,30)
    .setImages(run_imgs)
   ;  */
   
    dirButton = cp5.addButton("dirButton")     //  dirrection button
 //    .setValue(1)
     .setPosition(70,box1Ypos+6)  //off2)  // y position was 50
     .setSize(30,20)
        .setImages(togs)   //stop_imgs)
    ;
    
   valDir = cp5.addButton("valDir")     //  valve dirrection button
 //    .setValue(1)
     .setPosition(70,box1Ypos+26)  // y position was 50
     .setSize(30,20)
        .setImages(togs)   //stop_imgs)
    ;
 
     savRun = cp5.addButton("saveRun")     //  was saveButton
          .setPosition(400, 10)
            .setSize(80, 20)
                .setLabel("Save Run")
    //              .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)  //.setLabel("Start_Run")
                    ;
                    
  loadCal  = cp5.addButton("loadCal")                          // program is at bottom of this tab
      .setColorBackground(0xffFFFEFC)//#FFFEFC 
        .setColorCaptionLabel(0xff030302) //#030302
          .setColorForeground(0xffAA8A16)
          .setColorActive(0xff06CB49)
            .setPosition(100, 2*box1Ypos+25)
              .setSize(50, 20)
     //           .setTriggerEvent(Bang.RELEASE)
                  .setLabel("Load File") //
      //              .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)  //.setLabel("Start_Run")
                      ;
    readCal =   cp5.addButton("writeCal")                          // program is at bottom of this tab
       .setColorBackground(0xffFFFEFC)//#FFFEFC 
        .setColorCaptionLabel(0xff030302) //#030302
          .setColorForeground(0xffAA8A16)
          .setColorActive(0xff06CB49)
            .setPosition(160, 2*box1Ypos+25)
              .setSize(50, 20)
        //        .setTriggerEvent(Bang.RELEASE)
                  .setLabel("Write File") //
      //              .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)  //.setLabel("Start_Run")
                      ;
   read =   cp5.addButton("read")                          // read pH for calibration
       .setColorBackground(0xffFFFEFC)//#FFFEFC 
        .setColorCaptionLabel(0xff030302) //#030302
          .setColorForeground(0xffAA8A16)
          .setColorActive(0xff06CB49)
            .setPosition(40, 2*box1Ypos+60)
              .setSize(50, 20)
        //        .setTriggerEvent(Bang.RELEASE)
                  .setLabel("Read") //
      //              .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)  //.setLabel("Start_Run")
                      ;
   
   
  int step = 115;                    
   setA = cp5.addButton("set_A")                          // program is at bottom of this tab
       .setColorBackground(0xffFFFEFC)//#FFFEFC 
        .setColorCaptionLabel(0xff030302) //#030302
          .setColorForeground(0xffAA8A16)
          .setColorActive(0xff06CB49)
            .setPosition(140, 2*box1Ypos+step)
              .setSize(50, 20)
        //        .setTriggerEvent(Bang.RELEASE)
                  .setLabel("Set A") ;
                  
     setB = cp5.addButton("set_B")                          // program is at bottom of this tab
       .setColorBackground(0xffFFFEFC)//#FFFEFC 
        .setColorCaptionLabel(0xff030302) //#030302
          .setColorForeground(0xffAA8A16)
          .setColorActive(0xff06CB49)
            .setPosition(140, 2*box1Ypos+25+step)
              .setSize(50, 20)
        //        .setTriggerEvent(Bang.RELEASE)
                  .setLabel("Set B"); 
                  
     setC = cp5.addButton("set_C")                          // program is at bottom of this tab
       .setColorBackground(0xffFFFEFC)//#FFFEFC 
        .setColorCaptionLabel(0xff030302) //#030302
          .setColorForeground(0xffAA8A16)
          .setColorActive(0xff06CB49)
            .setPosition(140, 2*box1Ypos+50+step)
              .setSize(50, 20)
        //        .setTriggerEvent(Bang.RELEASE)
                  .setLabel("Set C") ;
                  
      reCalc = cp5.addButton("reCalc")                          // program is at bottom of this tab
       .setColorBackground(0xffFFFEFC)//#FFFEFC 
        .setColorCaptionLabel(0xff030302) //#030302
          .setColorForeground(0xffAA8A16)
          .setColorActive(0xff06CB49)
            .setPosition(220, 2*box1Ypos+25)
              .setSize(50, 20)
        //        .setTriggerEvent(Bang.RELEASE)
                  .setLabel("Recalib.") ;            
                  
     readPh = cp5.addButton("readPh")                          // program is at bottom of this tab
       .setColorBackground(0xffFFFEFC)//#FFFEFC 
        .setColorCaptionLabel(0xff030302) //#030302
          .setColorForeground(0xffAA8A16)
          .setColorActive(0xff06CB49)
            .setPosition(50, 65)
              .setSize(50, 20)
              .setLabel("Read pH") ;

    pause = cp5.addButton("pause")                          // program is at bottom of this tab
       .setColorBackground(0xffFFFEFC)//#FFFEFC 
        .setColorCaptionLabel(0xff030302) //#030302
          .setColorForeground(0xffAA8A16)
          .setColorActive(0xff06CB49)
            .setPosition(500, 35)
              .setSize(30, 30);
        //        .setTriggerEvent(Bang.RELEASE)
   //               .setLabel("Run pH") ;  
               
 }


public void dirButton() {
 boolDir =! boolDir;
 if(boolDir == false) {
  println ("direction = inject");
  dirTxt = "Direction: Inject";
 }
else {
//  transInt[0] = 0;
 println ("direction = withdraw");
  dirTxt = "Direction: Withdraw";
} 
} 




public void valDir(){
   valDirB =! valDirB;
   if (valDirB == true){
      valDirTxt = "To Sample";
      valveMode= "0";

   } else {
      valDirTxt = "To Reservoir";
      valveMode= "1";
   }
       println("valveMode = "+valveMode);  
}


public void saveRun() {
  selectInput("Select a file to process:", "fileSelected");
  // set up file1
  file1[0] = ",,Titraumatic titration data";
  file1[1] = ",,Inject vol,"+stringVolume ;
  file1[2] = ",,Final delay,"+stringInjDelay;
  file1[3] = ",,Syr diam,"+strDia;
  int p = xData.length;
   println("data length: "+p);
  for (int j = 0; j<p; j++){
   String jData = str(xData[j])+","+str(yData[j]);
   file1 = append(file1,jData);
//   println("jData: "+jData);
  }
  int q = file1.length;
   println("file length: "+q);
  
}

public void fileSelected(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } 
  else {
    file2 = selection.getAbsolutePath();
    println("User selected " + file2);
   // myTextarea.setText(file2);
      ///////////////////////////////////////
  //  String file2 = "C:/Users/Ben/Documents/Voltammetry Stuff/log/data.txt";
  try{
    saveStrings(file2,file1);
  //saveStream(file2,file1);
      }
      catch(Exception e){}              
            
          }}
          
 public void loadCal() {                                       // load the calibration file
 selectInput("Select a calibration file:", "fileToLoad");
}

public void fileToLoad(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } 
  else {
    String[] file2str = loadStrings(selection.getAbsolutePath());
    for (int j = 0; j<3; j++){
       String[] tokens = split(file2str[j+1],',');
       strPh[j] = tokens[0];
       calPh[j] = PApplet.parseFloat(tokens[0]);
       calVal[j] = PApplet.parseFloat(tokens[1]);       
    }  
    String[] tokens2 = split(file2str[4],',');
    strDia = tokens2[1];
  }
  setupTxtFields();
  reCalc();
}
    
public void reCalc(){
   avPh = (calPh[0]+calPh[1]+calPh[2])/3;
   avVal = (calVal[0]+calVal[1]+calVal[2])/3;
//   println("average pH: "+avPh+", average value: "+avVal);
   
   // calculate sum of squares using existing slope and average values
   calcSumSq();
   float minSumSq = sumSq;
   // increment to min
   boolean upEnd = false;
   while(upEnd == false){
    calSlope += .000001f;
    calcSumSq();
    if(sumSq > minSumSq){
       upEnd = true;
    }else{
     minSumSq = sumSq;  
    }
   }
   boolean downEnd = false;
     minSumSq = sumSq;
   while(downEnd == false){
    calSlope -= .000001f;
    calcSumSq();
    if(sumSq > minSumSq){
       downEnd = true;
    }else{
     minSumSq = sumSq;  
    }
   }
   float[] delta = {0,0,0};
   for (int d = 0; d<3; d++){
      delta[d] = (calPh[d]-calInt)/calSlope-calVal[d];
   }
   println("Calibration complete");
   println("calSlope: "+calSlope+", calInt: "+calInt);
   println("delta values: A, "+delta[0]+ ", B; "+ delta[1] +", C; "+delta[2]);
   
}

public void calcSumSq(){
      calInt = avPh-(calSlope * avVal); // calculate intercept 
      sumSq = 0;
   for (int n = 0; n<3; n++){
    sumSq += sq((calPh[n]-calInt)/calSlope-calVal[n]);  
   }
   println("slope: "+calSlope+" intercept: "+calInt+" sum of squares: "+sumSq);
}



public void set_A(){
   calVal[0] = yRead;
   println("calibration Value 0: "+calVal[0]);
   String s = cp5Txt.get(Textfield.class, "calA").getText();
   calPh[0] = PApplet.parseFloat(s);
}

public void set_B(){
   calVal[1] = yRead;
   String s = cp5Txt.get(Textfield.class, "calB").getText();
   calPh[1] = PApplet.parseFloat(s);
}

public void set_C(){
   calVal[2] = yRead;
  String s = cp5Txt.get(Textfield.class, "calC").getText();
   calPh[2] = PApplet.parseFloat(s);
}

public void readPh(){
      println("Read pH from pH adjust screen");
   serialPort.write('&');
   runState = true;
   delay(10);

   serialPort.write("1,0,0,0,0,0,0");
   delay(250);
    p=1;

 //   println("value: "+value);
   String trash = serialPort.readStringUntil(LINE_FEED);                // need to get stuff out of buffer
   println("made it through trash");
   readVal();

   runState=false;
   serialPort.write('&');
   println("new pH value read");
   println("value: "+yRead);
  
}

public void read(){    // button initiates pH read for calibration screen
   println("Read pH in process");

  contRead =! contRead;
  runState = contRead;
  println("contRead: "+contRead);

   serialPort.write('&');
  println(" go signal");
   delay(10);
   if(runState == true){
   serialPort.write("1,0,0,0,0,0,0");
   delay(200);
   String trash = serialPort.readStringUntil(LINE_FEED);                // need to get stuff out of buffer
   println("made it through trash");
   p=1;
   readVal();
  
   }
 //  runState=! runState;
}

   public void readVal(){
   float value = 0;
   for(int j = 0; j<10; j++){
     yRead = 0;
     while(yRead == 0){
     readData();
   }
   println("line 357");
      value += yRead;
//      println("value read for j = "+j);
   }
     yRead = value/10;
     println("line 361 buttons tab");
   }
   

public void setupknob(){
cp5knob = new ControlP5(this);
PImage[] togs = {loadImage("tog_button1.png"),loadImage("tog_button2.png"),loadImage("tog_button3.png")};
/*valveKnob = cp5knob.addKnob("valvePos")     // display these channels
               .setPosition(40, 200)
               .setRange(0,1)
               .setValue(0)
               .setRadius(25)
               .setNumberOfTickMarks(2)
               .setTickMarkLength(2)
               .setTickMarkWeight(10)
               .snapToTickMarks(true)
               .setMax(1)
               .setMin(0)
               .setViewStyle(1)
               //.setSensitivity(0)
               //.setScrollSensitivity(100)
               .setConstrained(true)
               //.setDefaultValue(0)
               //.setStartAngle(0)
               .setMouseOver(false)
               ;  */
}
// Com_Port tab
// code on this tab sets up communications with the launchpad

public void setupComPort() {
  

 /////////  connect button ////////////
  cp5Com = new ControlP5(this); 
    cp5Com.addButton("connect")
     .setPosition(10,10)
     .setSize(45,20)
//   .setImages(imgs)
//    .updateSize()
     ;

}



/////////// connect button program //////////

public void connect() {
  println("connect button pressed");
  
  char cData = 'a';
  comList = null;
  comList = serial.list();  
  int n = comList.length;
  println("com list length = "+n);
  if (n == 0) { 
    comStatTxt = "No com ports detected";
  }
  else if (n==1) {
    comStatTxt = "Com port: "+comList[0];
     serialPort = new Serial(this, comList[0], 9600);
      // serial print character '&'
 //    serialPort.write('*');
     println("* character sent to LaunchPad");
 // listen for return character '&'
     delay(100);
     if (serialPort.available () <= 0) {}
     if (serialPort.available() > 0)
        {
        cData =  serialPort.readChar();
        if (cData == '&') {
          comStatTxt = "Connected on "+comList[0];
        }
        }
     
  }      ///////// end of else if n==1 loop
  else {
    int k = 9999;
    comStatTxt = "Multiple com ports detected";
    for (int m = 0; m <= n-1; m++) {
      try{
        serialPort = new Serial(this, comList[m], 9600);
             serialPort.write('*');         // should this be *?  was &
     // listen for return character '&'
         delay(100);
         if (serialPort.available () <= 0) {
         println (comList[m]+" not responsive");
         }
         if (serialPort.available() > 0)
            {
            cData =  serialPort.readChar();
            if (cData == '&') {
              println (comList[m]+" responsive");
              k = m;
            }
            serialPort.clear();
            serialPort.stop();
            }
      }
      catch(Exception e){}
    }  // end of itterative look at ports
    if (k == 9999) {
      comStatTxt = "No responsive port";
    }
    else{
    serialPort = new Serial(this, comList[k], 9600); 
    comStatTxt = "Connected on "+comList[k];
    }
  }

}
 // dropdown lists depricated, change to scrollableList
 
 public void dropdownSetup() {
   //////////////////////////////////////////Dropdownlist////////////////////////////////////////
 cp5list = new ControlP5(this); 
 List modes = Arrays.asList("Manual","Calibration", "Titration"); //"pH Adjust", "Titration");
//  mode = cp5list.addDropdownList("list-2", 250, 30, 80, 84);     //Nov. 1, 2013
  cp5list.addScrollableList("mode")
    .setPosition(250, 10)
    .setSize(80, 104)
    .setItemHeight(20)
    .setBarHeight(20)
    .addItems(modes)
    ;
    

 }
public void mode(int n){
  print("from ScrollableList, n = ");
  println(n, cp5list.get(ScrollableList.class,"mode").getItem(n));
  modeState = n;
  setupScreen();
}


public void keyPressed() {
   
 }
 
 public void setupScreen(){
       if (modeState == 0) {    // Manual
     logFile = false;
     bkgd = 0xff8B9593;
       surface.setSize(350, 210);
              volume.hide();
       speed.show();
   //    injections.hide();
       injDelay.hide();
       dirButton.show();
       runButton.hide();
       runPump.show();
       valDir.show();
       setVol.show();
   }
        else {
       dirButton.hide();
       runPump.hide(); 
  //     runValve.hide();
       valDir.hide();
      setVol.hide();
     }
  if (modeState == 1) {  // calibrate
          logFile = false;
          bkgd = 0xff7A8995;
      surface.setSize(600, 420);
      println("modeState 1, dropdownlist, line 56");
         //injections.hide();
       injDelay.hide();
       volume.hide();
       speed.show();
    //   injections.hide();
  //     syrCal.show();
  //     setVol.show();
  //     injDelay.show();
  //   dirButton.hide();
            runButton.hide();
            calA.show();
            calB.show();
            calC.show();
            loadCal.show();
            readCal.show();
            diam.show();
            setA.show();
            setB.show();
            setC.show();
            read.show();
            reCalc.show();   
  }else {
       calA.hide();
       calB.hide();
       calC.hide();
            setA.hide();
            setB.hide();
            setC.hide();
       loadCal.hide();
       readCal.hide();
       diam.hide();
 //      syrCal.hide();
//       setVol.hide();
       reCalc.hide();
       read.hide();
     }
          if (modeState == 2 || modeState == 3) {  // pH adjust
          logFile = true;   // may want to change this in the GUI later
     bkgd = 0xff529079;
      surface.setSize(650, 550);
//       frame.setSize(850, 550);
       target.show();
       speed.hide();
 //      injections.hide();
       readPh.show();
       volume.show();
       injDelay.show();
       runButton.show();  
       savRun.show();
            pause.show();
       
          }  else{
     savRun.hide();
     target.hide();
     pause.hide();
  }
  
 }
public void controlEvent(ControlEvent theEvent) {
//  print("coming from the Event controller:  ");
  //println(theEvent.getController().getName());
//////// new stuff //////////////////////
/*  if (theEvent.isGroup() ) {
    println("event from group: "+theEvent.getGroup().getValue()+" from "+theEvent.getGroup());
//    float Mod =theEvent.getGroup().getValue();
    modeState =(int)theEvent.getGroup().getValue();
   println("modeState "+modeState);

    
      if(theEvent.name().equals("mode")){
        if (modeState == 0) {
        Modetorun = "toSignal";
  //      Modesel = true;
        }
        if (modeState == 1) {
        Modetorun = "toVol";
  //      Modesel = true;
        }
              if (modeState == 2) {
        Modetorun = "Titrate";
  //      Modesel = true;
        }
              if (modeState == 3) {
        Modetorun = "pHAdjust";
   //     Modesel = true;
        }
        println(Modetorun);
      }
      
    } */

  if (theEvent.isController()) {
    println("event from controller : "+theEvent.getController().getValue()+" from "+theEvent.getController());
    //println("event from controller : "+theEvent.getController().getValue()+" from "+theEvent.getController()
    
    if(theEvent.isFrom(valveKnob)){
          
          if(serialPort!=null){
            println("<"+(int)theEvent.getController().getValue()+">");
            int knobpos = (int)theEvent.getController().getValue();
            if(valvewritepos != knobpos){
               valvewritepos = (int)theEvent.getController().getValue();
              if(valvewritepos == 1){
                serialPort.write("a");
              }
              if(valvewritepos == 2){
                serialPort.write("b");
              }
              if(valvewritepos == 3){
                serialPort.write("c");
              }
              println("new pos "+(int)theEvent.getController().getValue());
            }
          }
      }
  }
}
////////// Initiate Buttons tab ////
// runButton
// runPump
// pause
// writeParams

public void runButton() {  

  println("holy crap, sombody pressed run");

  serialPort.write('&');         // initiates run
//  println("line 12");
//  delay(100); 

  if (runState == false) {      // start run
 // println("line 16");
    if (modeState == 2  && paused == false) {  // only restart data file if paused is false
    println("line18");
      // create log file for pH adjust and titration
  //    logData(file1, "volume,pH,\n", false);   // log data to file 1, do not append, start new file
      // added 4/15/15
      xData = nullData;  /// Clear X and Y data to redraw chart
      yData = nullY;
      inVol = 0;
      p = 0;                        // reset counter
      runStateTxt = "Stop";
    }
    println("line28");
      getParams();
    println("line 30");
      delay(100);
    writeParams();
    println("line 29, did not get here");
  } // end of if runState  == false loop

  ///////////////////////

  else {                       // stop run at button press
    runState = false;
    paused = false;
    println("runState = false");
    //  delay(100);
    runStateTxt = "Run";
    pauseTxt="Pause";
  }
}


public void runPump(){
  
  serialPort.write('&'); 
 if(pumpRunState == true){  // if running, turn off
    runPtxt = "Run Pump";
    runState = false;
  }
  else{
    getParams();
    delay(100); 
    writeParams();
    runPtxt = "Stop Pump";
    runState = true;
  }
  pumpRunState =! pumpRunState;
  println("pump run state = "+pumpRunState);

}


public void pause(){
   println("pause button pressed");
   if(paused == true){        // resume titration
      runStateTxt = "Stop";
      pauseTxt = "Pause";
      runState = true;
      paused = false;
      //get new values for addition delay, target and addition volume
        stringInjDelay = cp5Txt.get(Textfield.class, "Delay_(s)").getText();
        int k = PApplet.parseInt(stringInjDelay);
        stringInjDelay = str(k);
        stringVolume = cp5Txt.get(Textfield.class, "volume").getText();
        sTarget = cp5Txt.get(Textfield.class, "target").getText();
        fTarget = PApplet.parseFloat(sTarget);
        serialPort.clear();                             // did not help
        serialPort.write('N');
        println("N sent to microcontroller");
        delay(100);
        String send = stringInjDelay+","+stringVolume;
        //String send = "2,"+stringVolume;
        serialPort.write(send);
        println(send+" sent to microcontoller");
        
    
   }else if (paused == false && runState == true){
     println("pause signal sent");
      runState = false;
      paused = true;
      runStateTxt = "Stop";
      pauseTxt = "Resume";     
      serialPort.write('P');
     } else {
     }

}

public void writeParams() {
  ///////////// write parameters to lauchpad ////////////////////
  serialPort.write(stringMode);    // transmit mode
  serialPort.write(',');               
  serialPort.write(strDia);          // transmit syringe diameter
  serialPort.write(',');               
  serialPort.write(stringVolume);  // transmit volume to inject /withdraw
  serialPort.write(',');
  ////////////////////////////// transmit direction ///////////////////                                  
  if (boolDir == false) {     // if direction = inject
    serialPort.write(",0,");    // specifies direction
    println("Direction = inject");
  }  // end of if direction = inject
  else {                        // if direction = withdraw
    serialPort.write(",1,");
    println("Direction = withdraw");
  }  
  ///////////////////// end of transmit direction //////////////////////
  serialPort.write(stringSpeed);       // speed
  serialPort.write(',');
  if(modeState == 0){
  serialPort.write(valveMode);
  }else{
    serialPort.write("0");
  }
  //  serialPort.write(stringInjections);  // number of injections
  serialPort.write(',');
  serialPort.write(stringInjDelay);    // delay between injections
  //////////////////// end of transmissions ////////////////////

  println("mode = "+stringMode);
  println("Volume = "+stringVolume);
  println("Speed = "+stringSpeed);   
  println("valve mode = "+valveMode);
  println("delay = "+stringInjDelay);

  //   Wait for LaunchPad to transmit '&' before starting data read
  boolean startRead = false;  
      char priorB = 'o';

  while (startRead == false) {
   b = serialPort.readChar();

    if(b != priorB){
      j = PApplet.parseInt(b);
      println("b value: "+j);
      priorB = b;
    }
    if (j == 65535){  //b == '&') {         // was b == '#' to end run
 //   if (b == '&') {         // was b == '#' to end run
      runState = true; 
      println("runState = true, line 82");
      runStateTxt = "Stop";
      startRead = true;
    }
    b='b';
  }  // end of while serialport available
}
public void chartsSetup(){
/*   // use the following to align charts
 float minY = 0.85*calVal[2];
 float maxY = 1.15*calVal[0];
 float minX = calPh[0]-1;
 float maxX = calPh[2]+1;
 float[] xVals = {minX, minX, maxX, maxX};
 float[] yVals = {minY, maxY, minX, maxX}; */
              ////////////////////////////////gicentre charts///
  lineChart = new XYChart(this);
  lineChart.setData(new float[] {1, 2, 3}, new float[] {1, 2, 3});
  lineChart.showXAxis(true); 
  lineChart.showYAxis(true);
  lineChart.setXAxisLabel("injected volume (mL)");
  lineChart.setYAxisLabel("pH");
  /////////////////// limits on chart ////

  //lineChart.setMinY(0);   
  lineChart.setYFormat("##.##");  
  lineChart.setXFormat("##.##");       
  // Symbol colours
  lineChart.setPointColour(color(234, 28, 28));
  lineChart.setPointSize(5);
  lineChart.setLineWidth(2);
  
                ////////////////////////////////Calibration charts///
  calChart = new XYChart(this);
  calChart.setData(calPh,calVal);
//  calChart.setData(xVals,yVals);    // use to align charts
  calChart.showXAxis(true); 
  calChart.showYAxis(true);
  calChart.setXAxisLabel("pH");
  calChart.setYAxisLabel("Read value");

/* float minY = 0.85*calVal[2];
 float maxY = 1.15*calVal[0];
  calChart.setMinY(minY);
  calChart.setMaxY(maxY);
  calChart.setMinX(calPh[0]-1);
  calChart.setMaxX(calPh[2]+1);  */
  calChart.setYFormat("##.##");  
  calChart.setXFormat("##.##");       
  // Symbol colours
  calChart.setPointColour(color(234, 28, 28));
  calChart.setPointSize(5);
//  calChart.setLineWidth(2);

 trendChart = new XYChart(this);
  trendChart.showXAxis(false); 
  trendChart.showYAxis(false);
  trendChart.setLineWidth(2);
  trendChart.setPointSize(1);
  setChartLims();
}/////////////////////////////////////////////////end charts_gic_setup///////////////////////////////////////////////

public void setChartLims(){
 float minY = 0.85f*calVal[2];
 float maxY = 1.15f*calVal[0];
  calChart.setMinY(minY);
  calChart.setMaxY(maxY);
  calChart.setMinX(calPh[0]-1);
  calChart.setMaxX(calPh[2]+1);
  trendChart.setMinY(minY);
  trendChart.setMaxY(maxY);
  trendChart.setMinX(calPh[0]-1);
  trendChart.setMaxX(calPh[2]+1);
 float[] trendX = {calPh[0],calPh[2]};
 float[] trendY = {(calPh[0]-calInt)/calSlope,(calPh[2]-calInt)/calSlope};
 trendChart.setData(trendX,trendY);


}
public void setupTxtFields() { 
 cp5Txt = new ControlP5(this);  
int step = 115;
//int box1Off = 25;
  speed = cp5Txt.addTextfield("speed")  // time based txt field
//  cp5Txt.addTextfield("speed")  // time based txt field
    .setColor(0xff030302) 
      .setColorBackground(0xffCEC6C6) 
        .setColorForeground(0xffAA8A16) 
          .setPosition(240, box1Ypos+off1) //box1Off)
            .setSize(45, 20)
   //           .setFont(font)// 1/3/17
                .setFocus(false)
                    .setText("300");
  /*                    controlP5.Label ril = speed.captionLabel(); 
                        ril.setFont(font);
                          ril.toUpperCase(false);
                            ril.setText("Speed (uL/s)");     */
 int textY1 = 35;
 
 volume = cp5Txt.addTextfield("volume")  // time based txt field
//  cp5Txt.addTextfield("speed")  // time based txt field
    .setColor(0xff030302) 
      .setColorBackground(0xffCEC6C6) 
        .setColorForeground(0xffAA8A16) 
          .setPosition(320,textY1)
            .setSize(45, 20)
   //           .setFont(font)  // 1/3/17
                .setFocus(false)
                    .setText("200");
   /*                   controlP5.Label vol = volume.captionLabel(); 
                        vol.setFont(font);
                          vol.toUpperCase(false);
                            vol.setText("Vol. (uL)");     */

/*  injections = cp5Txt.addTextfield("injections")  // time based txt field
//  cp5Txt.addTextfield("speed")  // time based txt field
    .setColor(#030302) 
      .setColorBackground(#CEC6C6) 
        .setColorForeground(#AA8A16) 
          .setPosition(20, 150)
            .setSize(45, 20)
   //           .setFont(font)  // 1/3/17
                .setFocus(false)
                    .setText("1");
    */ 
                            
injDelay = cp5Txt.addTextfield("Delay_(s)")  // time based txt field
//  cp5Txt.addTextfield("speed")  // time based txt field
    .setColor(0xff030302) 
      .setColorBackground(0xffCEC6C6) 
        .setColorForeground(0xffAA8A16) 
          .setPosition(380, textY1)

            .setSize(45, 20)
              .setFont(font12)
                .setFocus(false)
                    .setText("2.0");
    /*                  controlP5.Label inDl = injDelay.captionLabel(); 
                        inDl.setFont(font);
                          inDl.toUpperCase(false);
                            inDl.setText("Delay (s)");     */
                            
 target = cp5Txt.addTextfield("target")  // status and com port text area
    .setPosition(320, textY1+25)
      .setSize(40, 20)   //was 30
      .setFont(font12) //(font)
         //.setLineHeight(14)
            .setColor(0xff030302)
              .setColorBackground(0xffCEC6C6)
                .setColorForeground(0xffAA8A16)//#CEC6C6
                .setText("14.00");
                    
diam = cp5Txt.addTextfield("diam")  
    .setColor(0xff030302) 
      .setColorBackground(0xffCEC6C6) 
        .setColorForeground(0xffAA8A16) 
          .setPosition(160, box1Ypos+off1)  //box1Off)
            .setSize(45, 20)
              .setFont(font12)
                .setFocus(false)
                    .setText(strDia);
  
                            

                    
setVol  = cp5Txt.addTextfield("volume_(mL)")  
    .setColor(0xff030302) 
      .setColorBackground(0xffCEC6C6) 
        .setColorForeground(0xffAA8A16) 
          .setPosition(240, box1Ypos+10) //+off1)
            .setSize(45, 20)
              .setFont(font12)
                .setFocus(false)
                    .setText("2.00");                   
                         
calA = cp5Txt.addTextfield("calA")  // status and com port text area
    .setPosition(30, 2*box1Ypos+step)
      .setSize(40, 20)   //was 30
      .setFont(font12) //(font)
         //.setLineHeight(14)
            .setColor(0xff030302)
              .setColorBackground(0xffCEC6C6)
                .setColorForeground(0xffAA8A16)//#CEC6C6
                .setText(strPh[0])
   //               .setLock(true)
  //                  .setLabel("4.00")
                    ;
                    
 calB = cp5Txt.addTextfield("calB")  // status and com port text area
    .setPosition(30, 2*box1Ypos+25+step)
      .setSize(40, 20)   //was 30
      .setFont(font12) //(font)
         //.setLineHeight(14)
            .setColor(0xff030302)
              .setColorBackground(0xffCEC6C6)
                .setColorForeground(0xffAA8A16)//#CEC6C6
                .setText(strPh[1]);     
                
 calC = cp5Txt.addTextfield("calC")  // status and com port text area
    .setPosition(30, 2*box1Ypos+50+step)
      .setSize(40, 20)   //was 30
      .setFont(font12) //(font)
         //.setLineHeight(14)
            .setColor(0xff030302)
              .setColorBackground(0xffCEC6C6)
                .setColorForeground(0xffAA8A16)//#CEC6C6
                .setText(strPh[2]);
}
public void getParams(){
  
// println("in speed loop");
////////////////get speed value from text box
  stringMode = String.valueOf(modeState);
  
  stringSpeed = cp5Txt.get(Textfield.class, "speed").getText();
  /* Info from text fields transmitted to u-controller as strings
   * to convert to float, int, or char, use commands below:
   *  float fSpeed = float(stringSpeed);
   *  iSpeed = round(fSpeed);
   *  cSpeed = char(iSpeed);
  */
  println("in params loop, line 143");
///////// get volume from text field
if (modeState == 0){
   println("in ModeState 0 loop");
   stringVolume = cp5Txt.get(Textfield.class, "volume_(mL)").getText();
   println("got text");
   int p = PApplet.parseInt(1000*PApplet.parseFloat(stringVolume));
   stringVolume = str(p);
   println("volume: "+stringVolume);
//}
println("params, line 153");
}
else{
  stringVolume = cp5Txt.get(Textfield.class, "volume").getText();
  println("params, line 156");
}
///////// get injetions from text field
//  stringInjections = cp5Txt.get(Textfield.class, "injections").getText();
println("params, line 160");
  stringInjDelay = cp5Txt.get(Textfield.class, "Delay_(s)").getText();
  sTarget = cp5Txt.get(Textfield.class, "target").getText();
  fTarget = PApplet.parseFloat(sTarget);
  strDia = cp5Txt.get(Textfield.class, "diam").getText();
  int intDia = PApplet.parseInt(1000*PApplet.parseFloat(strDia));
  strDia = str(intDia);
}
// fileReadWrite tab, titraumatic sketch, Feb 9 2017


public void writeCal(){     // write calibration file
//String[] file = {"pH,reading","","",""};
for(int p = 1; p < 4; p++){
 file[p] = str(calPh[p-1])+','+str(calVal[p-1]);
}
 strDia = cp5Txt.get(Textfield.class, "diam").getText();
 file[4] = "diameter,"+strDia;
selectOutput("Select a file to save:","calFileSave");
}
 
 public void calFileSave(File selection){
    if (selection == null){
       println("file selection was null");
    } else {
       String fileName = selection.getAbsolutePath();
       println("user selected "+ fileName);
       saveStrings(fileName, file);
    }
 }


public void logData( String fileName, String newData, boolean appendData)  //char//void logData( String fileName, String newData, boolean appendData)
{
   saveStrings(fileName, file);
   BufferedWriter bw=null;
  try { //try to open the file
    FileWriter fw = new FileWriter(fileName, appendData);
    bw = new BufferedWriter(fw);
    bw.write(newData);// + System.getProperty("line.separator"));
  } 
  catch (IOException e) {
    println("it broke");
    e.printStackTrace();
  } 
  finally {
    if (bw != null) { //if file was opened try to close
      try {
        bw.close();
      } 
      catch (IOException e) {
        println("error closing writer");
      }
    }
  }
}
// read_serial tab 
// Titraumatic_3 GUI sketch 


 public void  read_serial() {

readData();  
//  stuff for txt files cut Feb 23, 2017
/*if(logFile == true){    // stuff for saving txt file        
  String xVal = String.valueOf(inVol)+",";
  String yVal = String.valueOf(pHRead)+"\n";
  xVal += yVal;
  logData(file1,xVal,true);
  println("datalogged");
}              // end of txt file stuff

*/

if( modeState == 2|| modeState == 3){  // calibrate pH and manual do not read volume
   calcVol();

        ////////// set up data file 
    if (xData[0] == 0 && yData[0] == 0) {
          xData[0] = inVol; 
          yData[0] = pHRead;
          
        delta0 = (fTarget-yData[0]);
           if(delta0 >= 0){
              addBase = true;      // starting point acidic
           }else{
           addBase = false;        // starting point basic
           }  
         }   // end of set up for data file
         
           
       else if (inVol !=0 && yRead !=0 && newData == true){         
      
         xData = append(xData, inVol);
         yData = append(yData, pHRead);
         println("xData: "+xData.length);
         newData = false;
       }
         
       // if(modeState == 2 && yData.length >= 2){  // if mode is pH adjust, determine whether time to pause
        if(yData.length >= 2){    
           delta1 = (fTarget-pHRead);
           if(delta1 >= 0){
              belowTarget = true;
           }else{
              belowTarget = false;
           }
           if(addBase != belowTarget){
        //    println("passed target pH");
            serialPort.write("P");
            paused = true;
            println("paused at line 51 in read serial tab");
           }
           
           if(abs(delta1) <= pHthreshold && paused == false){
            serialPort.write("P");     //  pause injections
            paused = true;
       println("paused at line 59 in read serial tab");
           }
           if(delta1 >= 1.5f*pHthreshold && paused == true && belowTarget == addBase){
            serialPort.write("R");     //  resume injections
            paused = false;
            println("resumed at line 64 in read serail tab");
       //    println("injections resumed");
           }
           if(abs(delta0)<=abs(delta1)){
       //       println("going the wrong way!");
           }
        } // end of pause routine
     }   

         p +=1;

  }
  
  public void readData(){
 //    boolean reading = true;
 //  println("in readData");
 //  serialPort.clear();
 //     if (serialPort.available () <= 0) {}    // changed from if loop t while loop, 2/14/17
  //    while (reading == true){
      if (serialPort.available() > 0) {                     
        sData3 = serialPort.readStringUntil(LINE_FEED);  // new JS11/22

//   println("p value = "+p);
  if(sData3 != null && p != 0) {           // different in WheeStat
  //   println("line 91: ");   //+sData3);
  newData = true;
        String[] tokens = sData3.split(",");

    tokens = trim(tokens);  

     try{
        xRead = Float.parseFloat(tokens[0]);  // added volume
        yRead = Float.parseFloat(tokens[1]);  // pH reading
//        pHRead = calSlope*yRead + calInt;

        
        if(modeState == 2 || modeState == 3){
           command = PApplet.parseInt(tokens[2]);             // use for determining end of injection delay
        }
    if (xRead == 99999)  { // signals end of run
          runState = false;    // stops program
          pumpRunState = false;
          runStateTxt = "Run";
          runPtxt = "Run Pump";
          println("end the madness");
          cData = 'a';   
          }
 
        pHRead = calSlope*yRead + calInt;
       println("yRead: "+yRead+", pH read: "+pHRead);
//      reading = false;
  }
     catch(Exception e){
     println("exception e in read");
   }
     }
    }

 // }  // end of while reading is true
  }
  
public void calcVol(){
if(boolDir == false && xRead != 99999) {      
//if(iDir == 0 && xRead != 99999) {
  fVol = -xRead/1000+fVol;      // if injecting, subtract xRead from current vol
  inVol = xRead/1000+inVol;      //initVol-fVol;
  xRead = 0;                     // clear xRead so values dont add unless new reading recorded
   }
if(boolDir == true && xRead != 99999) {
//  if(iDir == 1 && xRead != 99999) {
  fVol = xRead/1000+fVol;      // if withdrawing, add xRead from current vol
  inVol = -xRead/1000+inVol;
  xRead = 0;
   }
}

/*void pauseAndRead(){
 serialPort.write('P');      // pauses injections, data keep coming every 200 ms
 readData();
 paused = true;
}*/
  public void settings() {  size(850, 550); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "Titraumatic2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
