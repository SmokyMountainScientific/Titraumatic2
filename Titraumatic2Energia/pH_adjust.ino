//  pH_adjust tab

void pHadjust(){

  readP();
  Serial.print("0,");
  Serial.print(sensorValue);
  Serial.println(",0");
  while(runState == true){         // continue reading until stop button pushed

readP();      
      Serial.print(addVol);  // need to define addVol
      Serial.print(",");
      Serial.print(sensorValue);
      Serial.println(",0");
      

  // inject tirant

  delay(adDelay);
  status2();
  if(delta<=4*tollerance){
    delay(adDelay);
  }
 if(delta<=2*tollerance){
    delay(adDelay);
  }
  status2();
  }
  
}

