// RunToVolume tab
// used when run mode is "to volume"

void runToVol(){
                 // need to deal with nAddns.
  int jogs = steps/600;  
  addVol = 600*Area/320/pitch;    // determine volume added or withdrawn
                                        // 42 uL for 15 mm dia syringe
        Serial.print("jogs = ");
        Serial.println(jogs);                                
 setValve(valveMode); 
   for (int k = 0; k< jogs; k++) {
      rotate(600);
 status2();
     if (k == jogs-1){  // && q == nAddns-1) {       // last rotation
 //       Serial.println(",1"); // 1 command indicates end of volume
        runState = false;     // should exit loop here
        Serial.println("99999,0,0");     // for data acuqisition
        }
      }  // end of k itterative loop
}


 void setValve(int p){
      valve1.attach(valve);
      valve1.write(valvePos[p]);
      delay(500);
      valve1.detach();
 
  
 }

