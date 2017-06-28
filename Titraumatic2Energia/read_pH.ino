void readP() {
    sensorValue = 0;
  for (int j = 0; j<16; j++) {
    sensorValue += analogRead(analogInPin);   //added for measurement  
  delay(10);  
  }
  sensorValue = sensorValue/16; 
  } 
