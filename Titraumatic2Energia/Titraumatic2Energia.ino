//////////////////////////////////////////////////////////////////
// Titraumatic Automatic Titrator 
//    pin setup for Feb 2017 eagle autotitrator file
//    with 2nd board from OSH Park
// First tab, Titraumatic_01262017
// in public energia sketches folder
// old file name: Titraumatic_11_28_14
// Dissables stepper motor when not in use
// servo currently run off of fill switch 
// Status command looks for % transmitted by GUI, board reset
// reads voltage from pH electrode on pin 1.6, 
//  voltage from electrode is output to serial monitor
// stepper motor controll using easy driver
// initial code copied from Arduino ©2011 bildr  
// Released under the MIT License - Please reuse change and share
// 
// switch inputs vcc to stop pins at syringe limits
// speed is given in uL/s - 
// Slower Speed == Stronger movement
// begun by Angela Edwards & J Summers - 8/27/13
//
// Data written to GUI in the form; uL added, digital measurement from electrode, commmand
//      command   idea
//        0       middle of run
//        1       end of injection to volume
//        2       last reading in titration delay, indicates need to plot this data point
// command indicates where you are in the experiment

/////////////////////////////////////////////////////////////////

// Inputs;
//  syringe diameter              dia           mm
//  syringe volume                vTotal        mL
//  direction (fill or dispense)  InOut  
//  number of additions           nAddns
//  time between additions        adDelay       sec
//                                delayMin      min
//  volume per addition           dispVol       uL
//  screw pitch   (revolutions per cm of plunger displacement)
//                                pitch
//  3 modes;  fill, continuous run, or titration
///////////////////////////////////////////////////////////////////

#include <Servo.h>
#define enablePin PB_7  // new 3/26/15
#define dirPin PB_2    // was PE_0  determines whether sample injected or removed
#define stepPin PE_0   // was PF_0   outputs every time the stepper moves 1/16th step
                              //  200 steps per revolution
#define endStop PA_7    // was PA_7
#define emptyStop PA_6    // was PA_6


//#define fillPin PE_5   // was PB_5  
//#define dumpPin PA_5   // was PB_0
//#define MS1Pin PF_0      // microstepping pin
//#define valveAdj A9      // analog read for adjusting valve position
#define valve PB_0    // Servo control pin for moving valve
//  stuff for fill/empty switches
int switchPin[] = {PE_5,PA_5};
int switchVal[] = {LOW,LOW};  // read values for switch pins
volatile int flag = 0;
boolean run = false;
int uSecs = 100;          // microseconds delay for switch activated pump motion 

Servo valve1;               // valve controlled by Servo
const int analogInPin = A10;  // Pin 6:  Analog input pin where the pH electrode connects
int sensorValue = 0;        // value read from the electrode
int threshold = 100;        // threshold for allowing progression to next step in titration
int delta;                  // difference between sequential sensor readings
int valveMode;              // used in manual mode, 0 = to reservoir, 1 = to sample

unsigned long stepCount = 0;         // number of steps the motor has taken  
unsigned int rotations;

uint16_t params[8];           // array to hold parameters in, changed from 7, Feb 2017
int dia = 15;                      // 5 mm dia
unsigned int sVol;            //one mL syringe
int fillmode =0;   // run, fill, or titrate
boolean runState=false;      //0 is off, 1 is on
int nAddns;          // number of additions 
int adDelay;         //  seconds between each addition in seconds
float addVol;        // volume of 600 addition during rotations step (~42 uL)
float Area;
int woi;             // withdraw or infuse; 0 = infuse, 1 = withdraw
unsigned long steps;
float injCyc;       // cycles to inject
int dispVol;        // uL dispensed per addition
int spd = 300;    
float fspd;         //injection speed,

float usDelay;        // microseconds between pulses, frequency = 1/(2*usDelay)
float pitch = 7.874; // 20 thread per inch, in threads/cm plunger displacement
int param =0;      // paramater used in readParams routine in setup
int addedVol;               // volume of titrant added to reaction
int currentVol;             // volume of titrant currently in syringe
int valvePos[] = {115,25};
//int valvePos1 = 25;
//boolean valveBool = true;               // current valve position value
//int targetPh;   // is this used?

  boolean pause = false;
//  boolean resume =false;
  boolean rsStatus = false;
int tollerance = 10;  // 0.03 pH units
int numCalAv = 0;
int calInterval = 0;
boolean rerun = false;
boolean readState = true;

void setup() { 
  pinMode(RED_LED, OUTPUT);
  pinMode(dirPin, OUTPUT); 
  pinMode(stepPin, OUTPUT);
  pinMode(endStop, INPUT_PULLDOWN);
  pinMode(emptyStop, INPUT_PULLDOWN); 
  pinMode(switchPin[0], INPUT_PULLDOWN);
  pinMode(switchPin[1], INPUT_PULLDOWN);
  attachInterrupt(switchPin[0], ISR, RISING);
  attachInterrupt(switchPin[1], ISR, RISING); 
  pinMode(enablePin, OUTPUT);
  pinMode(valve, OUTPUT);
  
 // Area = dia*dia*3.14159/4;  // diameter in mm, area in square mm
  usDelay = 1562*Area/pitch/spd;  
  digitalWrite(enablePin, LOW);  // added 3-26-15
  valve1.attach(valve);
  valve1.write(valvePos[0]);
  delay(500);
  valve1.detach();
  
  Serial.begin(9600);             //  begin serial comm. at 9600 baud
  //Serial.begin(19200);
} 

void loop(){ 
//  readFillSwitch();

setupRun();
if(runState == true){
  setValve(valveMode);
 if(fillmode==0) {  // manual
 //   runToSignal();
 digitalWrite(enablePin,LOW);   // power turned on to motor 
   runToVol();
   setValve(0);
 //    Serial.println("through run");
    }
    
 if (fillmode==1) {  // calibration
        calibration();
   }
   
 if (fillmode==2) {  // run to pH
  digitalWrite(enablePin,LOW);   // power turned on to motor 
//   pHadjust();
   titration();                   // titration and pH adjust use same routines.
   while(rerun == true){
//    Serial.println("rerun true");
    runState=true;
    rerun = false;
    titration();
   }
    }
    
/*if (fillmode==3) {  // titration
  digitalWrite(enablePin,LOW);   // power turned on to motor 
   titration();
    }    */

  }  // end of 'if runstate == true' loop
}
