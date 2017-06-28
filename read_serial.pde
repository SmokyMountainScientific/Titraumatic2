// read_serial tab 
// Titraumatic_3 GUI sketch 


 void  read_serial() {

readData();  
//  stuff for txt files cut Feb 23, 2017
/*if(logFile == true){    // stuff for saving txt file        
  String xVal = String.valueOf(inVol)+",";
  String yVal = String.valueOf(pHRead)+"\n";
  xVal += yVal;
  logData(file1,xVal,true);
  println("datalogged");
}              // end of txt file stuff

*/

if( modeState == 2|| modeState == 3){  // calibrate pH and manual do not read volume
   calcVol();

        ////////// set up data file 
    if (xData[0] == 0 && yData[0] == 0) {
          xData[0] = inVol; 
          yData[0] = pHRead;
          
        delta0 = (fTarget-yData[0]);
           if(delta0 >= 0){
              addBase = true;      // starting point acidic
           }else{
           addBase = false;        // starting point basic
           }  
         }   // end of set up for data file
         
           
       else if (inVol !=0 && yRead !=0 && newData == true){         
      
         xData = append(xData, inVol);
         yData = append(yData, pHRead);
         println("xData: "+xData.length);
         newData = false;
       }
         
       // if(modeState == 2 && yData.length >= 2){  // if mode is pH adjust, determine whether time to pause
        if(yData.length >= 2){    
           delta1 = (fTarget-pHRead);
           if(delta1 >= 0){
              belowTarget = true;
           }else{
              belowTarget = false;
           }
           if(addBase != belowTarget){
        //    println("passed target pH");
            serialPort.write("P");
            paused = true;
            println("paused at line 51 in read serial tab");
           }
           
           if(abs(delta1) <= pHthreshold && paused == false){
            serialPort.write("P");     //  pause injections
            paused = true;
       println("paused at line 59 in read serial tab");
           }
           if(delta1 >= 1.5*pHthreshold && paused == true && belowTarget == addBase){
            serialPort.write("R");     //  resume injections
            paused = false;
            println("resumed at line 64 in read serail tab");
       //    println("injections resumed");
           }
           if(abs(delta0)<=abs(delta1)){
       //       println("going the wrong way!");
           }
        } // end of pause routine
     }   

         p +=1;

  }
  
  void readData(){
 //    boolean reading = true;
 //  println("in readData");
 //  serialPort.clear();
 //     if (serialPort.available () <= 0) {}    // changed from if loop t while loop, 2/14/17
  //    while (reading == true){
      if (serialPort.available() > 0) {                     
        sData3 = serialPort.readStringUntil(LINE_FEED);  // new JS11/22

//   println("p value = "+p);
  if(sData3 != null && p != 0) {           // different in WheeStat
  //   println("line 91: ");   //+sData3);
  newData = true;
        String[] tokens = sData3.split(",");

    tokens = trim(tokens);  

     try{
        xRead = Float.parseFloat(tokens[0]);  // added volume
        yRead = Float.parseFloat(tokens[1]);  // pH reading
//        pHRead = calSlope*yRead + calInt;

        
        if(modeState == 2 || modeState == 3){
           command = int(tokens[2]);             // use for determining end of injection delay
        }
    if (xRead == 99999)  { // signals end of run
          runState = false;    // stops program
          pumpRunState = false;
          runStateTxt = "Run";
          runPtxt = "Run Pump";
          println("end the madness");
          cData = 'a';   
          }
 
        pHRead = calSlope*yRead + calInt;
       println("yRead: "+yRead+", pH read: "+pHRead);
//      reading = false;
  }
     catch(Exception e){
     println("exception e in read");
   }
     }
    }

 // }  // end of while reading is true
  }
  
void calcVol(){
if(boolDir == false && xRead != 99999) {      
//if(iDir == 0 && xRead != 99999) {
  fVol = -xRead/1000+fVol;      // if injecting, subtract xRead from current vol
  inVol = xRead/1000+inVol;      //initVol-fVol;
  xRead = 0;                     // clear xRead so values dont add unless new reading recorded
   }
if(boolDir == true && xRead != 99999) {
//  if(iDir == 1 && xRead != 99999) {
  fVol = xRead/1000+fVol;      // if withdrawing, add xRead from current vol
  inVol = -xRead/1000+inVol;
  xRead = 0;
   }
}

/*void pauseAndRead(){
 serialPort.write('P');      // pauses injections, data keep coming every 200 ms
 readData();
 paused = true;
}*/