void calibration(){
  while(runState == true){         // continue reading until stop button pushed
  for (int j = 0; j<16; j++) {
  sensorValue = analogRead(analogInPin);   //added for measurement
  delay(50);
  Serial.print("0,");
  Serial.println(sensorValue);
  //Serial.println(",");  
  }
  status2();
  delay(250);
  }
//  Serial.println("0,99999");
//  runState = false;
}
