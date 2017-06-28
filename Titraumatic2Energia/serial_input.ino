   // Serial Input tab, Titraumatic01262017
   //   Modified with parseInt command--3/29/15
   //  modify code to get all parameters in one run from GUI
   //  single call; setupRun
   //  readParam code from StellarisWheeStat5_0


void setupRun(){
  int starti = 0;
  while (starti != '&') {    // GUI transmits & character before transmiting params, 
                            //  & character marks start of parameter input   
    if(flag == HIGH){
      flag = LOW;
      readFillSwitch();            // read position of hardware switch to fill / empty
    }
   // }
 
    if(Serial.available() > 0) {
        starti = Serial.read();
        }  // end of if Serial.available > 0 loop
    if(starti == '*') {           // Gui transmits * looking for com port
      Serial.println('&');         // LaunchPad responds to Gui with &
      starti = 0;          
      }
    digitalWrite(RED_LED,LOW);
  }  // end of waiting for & character
// run state toggled on line 82, at end of setupRun()

  ///////////mode////////////
 if (runState == false)  { 

  readParam();  
     fillmode=params[0];     // mode
     delay(20); 
     dia=params[1];
     float radius = dia/2000;    // dia was multiplied by 1000 in GUI 
       Area = radius*radius*3.14159;  // diameter in mm, area in square mm

    dispVol=params[2];
      injCyc = dispVol*pitch/Area/10;     //dispVol is in uL, area in sq mm, pitch = cycles / cm
      steps = 3200*injCyc;             // volume
      
    woi=params[3];                   // dir
   
    spd=params[4]; 
      fspd =10*spd;  
      usDelay = 781*Area/pitch/spd;      // speed was 1562*Area/pitch/spd
      
    valveMode=params[5];         // was nAddns, now valveMode
     adDelay=1000*params[6];       // delay
    
   ///////////Print header///////
   Serial.println('&');

 //  Serial.println(",,Mode, Diameter, Addn Vol, Direction, Speed, Valve mode, Step delay");


/********************* print values for header ********************/
/*  Serial.print(",,");
  Serial.print(fillmode);
  Serial.print(',');
  Serial.print(dia);
  Serial.print(',');
  Serial.print(dispVol);
  Serial.print(',');
  Serial.print(woi);
  Serial.print(',');
  Serial.print(spd); 
  Serial.print(',');
  Serial.print(valveMode);
  Serial.print(',');
  Serial.println(adDelay);
 //////// signal all params received ////////
 delay(10); 
   Serial.println('&');  */
//      analogWrite(RED_LED, LOW);
//      digitalWrite(enablePin,LOW);   // power turned on to motor 
}   // end of 'if runstate == false' loop
else {
      digitalWrite(enablePin,HIGH);  // stepper power turned off
      Serial.println("99999,0,2"); 
    }
    runState =!runState;      //  toggles value of runstate
 //   Serial.print("Run state = ");
   // Serial.println(runState);

}

  /******************************  end setupRun *******************************/

/****************************** read Paramaters ****************************/
void readParam() {             // old definition deleted
                         // new definition from PumpWithPurpleBoard_2
  //delayMicroseconds(300);  
  while (Serial.available()<= 0) {
  }
  if (Serial.available() > 0){  
    for (int k = 0; k<7; k++) {    // was 6
      params[k] =  Serial.parseInt(); 
    } 
  }
} 


/******************end of Read Parameters ********************/

