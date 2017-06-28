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
  import org.gicentre.utils.stat.*;     // needed for charts
  import controlP5.*;                   // needed for text boxes
  import processing.serial.*;
  import java.io.*;                     // needed for BufferedWriter
  import java.util.Arrays;            // need for BufferedWriter?
  import java.util.*;

  import java.lang.Math.*;             // for raising a number to a power
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
 float calSlope = -0.00354574;     // reported slope = -250 mV / pH unit, 
                       //    m = -3300 mV full scale / (4095 digits full scale * 250 mV / pH unit)
 float calInt =  14.86;                // Reported value of 1.75 volts at pH 7
                       //  b = 3500 mV at zero * 4095 digits full scale / 
 float avPh;
 float avVal;
 float sumSq;
 float[] calVal = {0,1,2}; 
 float[] calPh = {0,1,2};
 String[] strPh= {"0","1","2"};
 float pHthreshold = 0.01;
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
int bkgd = #0289A2;
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
  void setup() {
    size(850, 550);
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
    calPh[j-1] = float(tokens[0]);
     calVal[j-1] = float(tokens[1]);
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
  
  void draw() {
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
            text("Digital Reading: "+ int(yRead), 100, 2*box1Ypos+75);
            textSize (14);
            for(int m = 0; m<3; m++){
 
             text(int(calVal[m]),85, 2*box1Ypos+130+m*25);
            }
            text ("pH    Cal Value", 45,y2+85);
 ////// draw calibration chart ////////////
 setChartLims();
        fill(#EADFC9);               // background color
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
       fill(#EADFC9);               // background color
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
  
 