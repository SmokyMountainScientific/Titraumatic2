/*****************************  read rotory switch on elecrical box *******************************/
void readFillSwitch(){       

  switchVal[0] = digitalRead(switchPin[0]);  // reading the state of the fill switch 
  switchVal[1] = digitalRead(switchPin[1]);
   if (switchVal[0] == HIGH) {   // || switchVal[1] == HIGH){
    digitalWrite(enablePin,LOW);   // power turned on to motor
    runState = true; 
    setValve(1);           // defined on run to volume tab    
    digitalWrite(dirPin,HIGH);
     
  while (switchVal[0] == HIGH) {
       swRotate(400);
       switchVal[0] = digitalRead(switchPin[0]);
 
     }
   }
   if(switchVal[1] == HIGH){
        digitalWrite(enablePin,LOW);   // power turned on to motor
    runState = true; 
    setValve(1);           // defined on run to volume tab    
    digitalWrite(dirPin,LOW);
     while (switchVal[1] == HIGH) {
     swRotate(400);
     switchVal[1] = digitalRead(switchPin[1]);
     }
   }
  if (switchVal[0] == LOW && switchVal[1] == LOW) {    // changed from || to &&
    digitalWrite(enablePin,HIGH);   // stepper power turned off 
    runState = false;
    setValve(0);
    

  
  }
//}
}
/******************************* end of read rotory switch ************************/
 void ISR()
  {
    flag=HIGH;
 //   Serial.print("ISR flag tripped, runState: ");
   // Serial.println(runState);
  }

  void swRotate(int j){
 for (int k = 0; k<j; k++){
      digitalWrite(stepPin, HIGH); 
    delayMicroseconds(100); 
    digitalWrite(stepPin, LOW); 
    delayMicroseconds(100);
  }
  }

