// reset_status tab

void status(){
   int endQ = digitalRead(endStop);          // forward limit switch
   int endR = digitalRead(emptyStop);  
   if (endQ == HIGH) {     //|| endR == HIGH){
     woi = 0;               // toggle direction to reverse
     rotate(300);
     endItAll();
  //  runState = false;
    //   HWREG(NVIC_APINT) = NVIC_APINT_VECTKEY | NVIC_APINT_SYSRESETREQ; 
    //Serial.println("99999,0,2");       // end signal added 10/22/14
  }
   if (endR == HIGH) {     //|| endR == HIGH){
     woi = 1;               // toggle direction to reverse
     rotate(300);
     endItAll();
  }
}

void status2(){
  if(Serial.available()>0) {
    int stuff = Serial.read();

    if(stuff == 80){
      pause = true;
     pauseAndRead();
 //    Serial.println("exit status2, line 28");
  //   readState = false;   // try 3/8/17
 
    }
    if(stuff == 38){
      endItAll();
    }

  }

}

void endItAll()  {
    Serial.println("99999,0,2"); 
  runState = false;
}

void pauseAndRead() {
  while(pause == true){
  readP();
  Serial.print("0,");
  Serial.print(sensorValue);
  Serial.println(",0");  // command indicates mid-run
  delay(250);
    while(Serial.available()>0) {   // changed from if to while 3/8/17
      int j = Serial.read();
      if (j== 82){          // ascii value for R
       pause = false;
      }
      if (j== 38){          // ascii value for &
        endItAll();
        pause = false;
      }
     if(j == 'N'){   // look for new inj and delay parameters
  //    Serial.println("in new, line 61");
     while (Serial.available()<=0){}
     adDelay=1000*Serial.parseInt();

     while (Serial.available()<=0){}
     dispVol=Serial.parseInt();
     injCyc = dispVol*pitch/Area/10;     //dispVol is in uL, area in sq mm, pitch = cycles / cm
     steps = 3200*injCyc;             // volume
     runState = false;   // end titration without ending experiment
     rerun = true;      // go back and get run experiment with new variables
     pause = false;     // exit this loop*
   //  Serial.flush();   
  //   Serial.print("add delay: ");
  //   Serial.print(adDelay);  
   //  Serial.print(", volume: ");
   //  Serial.println(dispVol);  // try 3/8/17
 //    Serial.println("exiting pause loop");
    }

    }

       
  }  

}
  
