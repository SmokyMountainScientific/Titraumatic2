// Rotations tab
//////// Nov 12, 2014:  
//  Injection speed in uL / s, all units are in mm and seconds except pitch (which is in threads/cm).
//             usDelay = 10^6*Area*10/(2*pitch*speed*3200)   -- change for 1/16th microstepping
//     for 15 mm diameter plunger, Area = 176 sq mm.
//     pitch currently set to 7.874 threads/cm
//     for 176 uL/s speed, requires 1 mm/s travel, 1/0.7874 rotations at 3200 usteps /  rotation
//     this gives 4064 steps/s, or 246 us/step.  Each microstep takes 2*usDelay:  usDelay = 123


void rotate(int msteps){
  msteps = abs(msteps);
    if (woi == 0) {              // withdraw or inject
      digitalWrite(dirPin,LOW);   // output to direction pin
      }
    else {
      digitalWrite(dirPin,HIGH);
      }
if(runState == true){
  for(int i=0; i < msteps; i++) {      // step through msteps
    digitalWrite(stepPin, HIGH); 
    delayMicroseconds(usDelay); 
    digitalWrite(stepPin, LOW); 
    delayMicroseconds(usDelay); 
  }   
}
  status();
}

