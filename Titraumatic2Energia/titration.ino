// titration tab
//  Titraumatic_09_15_15
//  multiple possible scenarios:
//              1.  Simple injections based on time
//              2.  Injected volumes based on change in pH
//              3.  Delay injections based on pH stability.
//  03/10/14  transmit injection number to GUI
//   GUI will calculate volume added from known values
/* 
From GUI code:
 float calSlope = -0.0032234;     // reported slope = -250 mV / pH unit, 
                       //    m = -3300 mV full scale / (4095 digits full scale * 250 mV / pH unit)
 float calInt =  14;                // Reported value of 1.75 volts at pH 7
                       //  b = 3500 mV at zero * 4095 digits full scale / 
 pHRead = calSlope*yRead + calInt;
 
 for pH stability of .05 pH units: .05/.0032234 = 16
 for pH min = 2:  (14-2)/.00322 = 3726
 for pH max = 12:  (14-12)/.00322 = 621
 for slope = 1 pH unit / 2 mL:  1 pH unit = 310, 2 mL = 357* 2 * 20 = 14280
     min slope = .0266
*/

void titration() {
  int rots =357;
  int jogs = steps/rots;
  addVol = rots*Area/320/pitch;
  
  int threshold = 16;     // threshold for pH stability within .05 pH units
  float lastSlope = 0;  // most recent slope 
  float maxSlope = 0;   // maximum slope 
  int currentSteps = 0;  // steps taken since initiation
  int stepsSinceMax = 0;  // number of steps since maxSlope
  int stepSize;      // number of times through rots cycles per step
  int lastValue;
  float minEndVal = 621;    // pH = 12, used for turning off titration
  float maxEndVal = 3726;    // pH =2, used for turning off titration
  float minSlopeVal = .0266;   // pH slope for turning off titration
 
 stepSize = jogs;  // initial step size equals value transmitted by GUI
    //// initial read and print to GUI  ///////////
  readP();
  int lastVal= sensorValue;
  Serial.print("0,");
  Serial.print(sensorValue);
  Serial.println(",0");
  
  /////////// main titration loop ////////////
  while(runState == true) {
    for (int k = 0; k<stepSize; k++){  // injection loop
      rotate(rots);
      }  // end of k loop
    delay(adDelay);  // 2 second delay after injection
    readState = true;
    while (readState == true){
      readP();
      int delta = abs(sensorValue - lastVal);
      lastVal = sensorValue;
      if (delta<=threshold){
        readState = false;
        }  // end of delta loop
      status2();       // read for stop or pause signal from GUI
      delay(500);
      } // end of readState loop
    Serial.print(stepSize*addVol);
    Serial.print(",");
    Serial.print(sensorValue);
    Serial.println(",0"); 
    delta = abs(lastValue-sensorValue);
    lastValue = sensorValue;
    lastSlope = delta/stepSize;    // need new delta variable, calculate from prior sent reading
    if (lastSlope > maxSlope) {
      maxSlope = lastSlope;
      stepsSinceMax = 0;
    }
    currentSteps += 1;
    stepsSinceMax += 1;
    ///// determine next injection size /////////////
    ////// determine whether to end titration ///////
 /*   if(sensorValue >= maxEndVal || sensorValue <= minEndVal){
     if (lastSlope <= minSlopeVal) {
       runState = false;
      Serial.println("99999,0,4"); 
     }
    }*/
 
  }  // end of runState loop
//  Serial.println("end of titration loop");
//  Serial.print("rerun status: ");
//  Serial.println(rerun);
}

 



 



