// RunToSignal tab
// used with "to signal" mode
void runToSignal(){   // working on 3/18/14
   Serial.println("in run to signal routine");
      addVol = 600*Area/320/pitch;    // 42 uL for 15 mm syringe dia
     while(runState == true) {       //fill syringe until stop signal
         rotate(600); 
        ////// calculate uL added ///////

        readP();                                 // read sensor value
        Serial.print(addVol);
        Serial.print(",");
        Serial.print(sensorValue);
        Serial.print(",");
        Serial.println("0");  // command indicates mid-run  
        status2();
  //   HWREG(NVIC_APINT) = NVIC_APINT_VECTKEY | NVIC_APINT_SYSRESETREQ;   
     }
  }
