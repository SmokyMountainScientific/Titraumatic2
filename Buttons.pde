// Buttons tab
// Titraumatic_3 GUI

 void setupButtons() {
    PImage[] run_imgs = {loadImage("run_button1.png"),loadImage("run_button2.png"),loadImage("run_button3.png")};
    PImage[] stop_imgs = {loadImage("stop_button1.png"),loadImage("stop_button2.png"),loadImage("stop_button3.png")};
    PImage[] togs = {loadImage("tog_button1.png"),loadImage("tog_button2.png"),loadImage("tog_button3.png")};
 cp5 = new ControlP5(this);  
 
    runButton = cp5.addButton("runButton")
     .setPosition(450,35)
     .setSize(30,30)
    .setImages(run_imgs)
   ;
   
  runPump = cp5.addButton("runPump")
     .setPosition(20,box1Ypos+50)
     .setSize(30,30)
    .setImages(run_imgs)
   ;
/*     runValve = cp5.addButton("runValve")
 //    .setValue(10)
     .setPosition(20,y2+off1)
     .setSize(30,30)
    .setImages(run_imgs)
   ;  */
   
    dirButton = cp5.addButton("dirButton")     //  dirrection button
 //    .setValue(1)
     .setPosition(70,box1Ypos+6)  //off2)  // y position was 50
     .setSize(30,20)
        .setImages(togs)   //stop_imgs)
    ;
    
   valDir = cp5.addButton("valDir")     //  valve dirrection button
 //    .setValue(1)
     .setPosition(70,box1Ypos+26)  // y position was 50
     .setSize(30,20)
        .setImages(togs)   //stop_imgs)
    ;
 
     savRun = cp5.addButton("saveRun")     //  was saveButton
          .setPosition(400, 10)
            .setSize(80, 20)
                .setLabel("Save Run")
    //              .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)  //.setLabel("Start_Run")
                    ;
                    
  loadCal  = cp5.addButton("loadCal")                          // program is at bottom of this tab
      .setColorBackground(#FFFEFC)//#FFFEFC 
        .setColorCaptionLabel(#030302) //#030302
          .setColorForeground(#AA8A16)
          .setColorActive(#06CB49)
            .setPosition(100, 2*box1Ypos+25)
              .setSize(50, 20)
     //           .setTriggerEvent(Bang.RELEASE)
                  .setLabel("Load File") //
      //              .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)  //.setLabel("Start_Run")
                      ;
    readCal =   cp5.addButton("writeCal")                          // program is at bottom of this tab
       .setColorBackground(#FFFEFC)//#FFFEFC 
        .setColorCaptionLabel(#030302) //#030302
          .setColorForeground(#AA8A16)
          .setColorActive(#06CB49)
            .setPosition(160, 2*box1Ypos+25)
              .setSize(50, 20)
        //        .setTriggerEvent(Bang.RELEASE)
                  .setLabel("Write File") //
      //              .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)  //.setLabel("Start_Run")
                      ;
   read =   cp5.addButton("read")                          // read pH for calibration
       .setColorBackground(#FFFEFC)//#FFFEFC 
        .setColorCaptionLabel(#030302) //#030302
          .setColorForeground(#AA8A16)
          .setColorActive(#06CB49)
            .setPosition(40, 2*box1Ypos+60)
              .setSize(50, 20)
        //        .setTriggerEvent(Bang.RELEASE)
                  .setLabel("Read") //
      //              .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER)  //.setLabel("Start_Run")
                      ;
   
   
  int step = 115;                    
   setA = cp5.addButton("set_A")                          // program is at bottom of this tab
       .setColorBackground(#FFFEFC)//#FFFEFC 
        .setColorCaptionLabel(#030302) //#030302
          .setColorForeground(#AA8A16)
          .setColorActive(#06CB49)
            .setPosition(140, 2*box1Ypos+step)
              .setSize(50, 20)
        //        .setTriggerEvent(Bang.RELEASE)
                  .setLabel("Set A") ;
                  
     setB = cp5.addButton("set_B")                          // program is at bottom of this tab
       .setColorBackground(#FFFEFC)//#FFFEFC 
        .setColorCaptionLabel(#030302) //#030302
          .setColorForeground(#AA8A16)
          .setColorActive(#06CB49)
            .setPosition(140, 2*box1Ypos+25+step)
              .setSize(50, 20)
        //        .setTriggerEvent(Bang.RELEASE)
                  .setLabel("Set B"); 
                  
     setC = cp5.addButton("set_C")                          // program is at bottom of this tab
       .setColorBackground(#FFFEFC)//#FFFEFC 
        .setColorCaptionLabel(#030302) //#030302
          .setColorForeground(#AA8A16)
          .setColorActive(#06CB49)
            .setPosition(140, 2*box1Ypos+50+step)
              .setSize(50, 20)
        //        .setTriggerEvent(Bang.RELEASE)
                  .setLabel("Set C") ;
                  
      reCalc = cp5.addButton("reCalc")                          // program is at bottom of this tab
       .setColorBackground(#FFFEFC)//#FFFEFC 
        .setColorCaptionLabel(#030302) //#030302
          .setColorForeground(#AA8A16)
          .setColorActive(#06CB49)
            .setPosition(220, 2*box1Ypos+25)
              .setSize(50, 20)
        //        .setTriggerEvent(Bang.RELEASE)
                  .setLabel("Recalib.") ;            
                  
     readPh = cp5.addButton("readPh")                          // program is at bottom of this tab
       .setColorBackground(#FFFEFC)//#FFFEFC 
        .setColorCaptionLabel(#030302) //#030302
          .setColorForeground(#AA8A16)
          .setColorActive(#06CB49)
            .setPosition(50, 65)
              .setSize(50, 20)
              .setLabel("Read pH") ;

    pause = cp5.addButton("pause")                          // program is at bottom of this tab
       .setColorBackground(#FFFEFC)//#FFFEFC 
        .setColorCaptionLabel(#030302) //#030302
          .setColorForeground(#AA8A16)
          .setColorActive(#06CB49)
            .setPosition(500, 35)
              .setSize(30, 30);
        //        .setTriggerEvent(Bang.RELEASE)
   //               .setLabel("Run pH") ;  
               
 }


public void dirButton() {
 boolDir =! boolDir;
 if(boolDir == false) {
  println ("direction = inject");
  dirTxt = "Direction: Inject";
 }
else {
//  transInt[0] = 0;
 println ("direction = withdraw");
  dirTxt = "Direction: Withdraw";
} 
} 




void valDir(){
   valDirB =! valDirB;
   if (valDirB == true){
      valDirTxt = "To Sample";
      valveMode= "0";

   } else {
      valDirTxt = "To Reservoir";
      valveMode= "1";
   }
       println("valveMode = "+valveMode);  
}


public void saveRun() {
  selectInput("Select a file to process:", "fileSelected");
  // set up file1
  file1[0] = ",,Titraumatic titration data";
  file1[1] = ",,Inject vol,"+stringVolume ;
  file1[2] = ",,Final delay,"+stringInjDelay;
  file1[3] = ",,Syr diam,"+strDia;
  int p = xData.length;
   println("data length: "+p);
  for (int j = 0; j<p; j++){
   String jData = str(xData[j])+","+str(yData[j]);
   file1 = append(file1,jData);
//   println("jData: "+jData);
  }
  int q = file1.length;
   println("file length: "+q);
  
}

void fileSelected(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } 
  else {
    file2 = selection.getAbsolutePath();
    println("User selected " + file2);
   // myTextarea.setText(file2);
      ///////////////////////////////////////
  //  String file2 = "C:/Users/Ben/Documents/Voltammetry Stuff/log/data.txt";
  try{
    saveStrings(file2,file1);
  //saveStream(file2,file1);
      }
      catch(Exception e){}              
            
          }}
          
 public void loadCal() {                                       // load the calibration file
 selectInput("Select a calibration file:", "fileToLoad");
}

void fileToLoad(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
  } 
  else {
    String[] file2str = loadStrings(selection.getAbsolutePath());
    for (int j = 0; j<3; j++){
       String[] tokens = split(file2str[j+1],',');
       strPh[j] = tokens[0];
       calPh[j] = float(tokens[0]);
       calVal[j] = float(tokens[1]);       
    }  
    String[] tokens2 = split(file2str[4],',');
    strDia = tokens2[1];
  }
  setupTxtFields();
  reCalc();
}
    
void reCalc(){
   avPh = (calPh[0]+calPh[1]+calPh[2])/3;
   avVal = (calVal[0]+calVal[1]+calVal[2])/3;
//   println("average pH: "+avPh+", average value: "+avVal);
   
   // calculate sum of squares using existing slope and average values
   calcSumSq();
   float minSumSq = sumSq;
   // increment to min
   boolean upEnd = false;
   while(upEnd == false){
    calSlope += .000001;
    calcSumSq();
    if(sumSq > minSumSq){
       upEnd = true;
    }else{
     minSumSq = sumSq;  
    }
   }
   boolean downEnd = false;
     minSumSq = sumSq;
   while(downEnd == false){
    calSlope -= .000001;
    calcSumSq();
    if(sumSq > minSumSq){
       downEnd = true;
    }else{
     minSumSq = sumSq;  
    }
   }
   float[] delta = {0,0,0};
   for (int d = 0; d<3; d++){
      delta[d] = (calPh[d]-calInt)/calSlope-calVal[d];
   }
   println("Calibration complete");
   println("calSlope: "+calSlope+", calInt: "+calInt);
   println("delta values: A, "+delta[0]+ ", B; "+ delta[1] +", C; "+delta[2]);
   
}

void calcSumSq(){
      calInt = avPh-(calSlope * avVal); // calculate intercept 
      sumSq = 0;
   for (int n = 0; n<3; n++){
    sumSq += sq((calPh[n]-calInt)/calSlope-calVal[n]);  
   }
   println("slope: "+calSlope+" intercept: "+calInt+" sum of squares: "+sumSq);
}



void set_A(){
   calVal[0] = yRead;
   println("calibration Value 0: "+calVal[0]);
   String s = cp5Txt.get(Textfield.class, "calA").getText();
   calPh[0] = float(s);
}

void set_B(){
   calVal[1] = yRead;
   String s = cp5Txt.get(Textfield.class, "calB").getText();
   calPh[1] = float(s);
}

void set_C(){
   calVal[2] = yRead;
  String s = cp5Txt.get(Textfield.class, "calC").getText();
   calPh[2] = float(s);
}

void readPh(){
      println("Read pH from pH adjust screen");
   serialPort.write('&');
   runState = true;
   delay(10);

   serialPort.write("1,0,0,0,0,0,0");
   delay(250);
    p=1;

 //   println("value: "+value);
   String trash = serialPort.readStringUntil(LINE_FEED);                // need to get stuff out of buffer
   println("made it through trash");
   readVal();

   runState=false;
   serialPort.write('&');
   println("new pH value read");
   println("value: "+yRead);
  
}

void read(){    // button initiates pH read for calibration screen
   println("Read pH in process");

  contRead =! contRead;
  runState = contRead;
  println("contRead: "+contRead);

   serialPort.write('&');
  println(" go signal");
   delay(10);
   if(runState == true){
   serialPort.write("1,0,0,0,0,0,0");
   delay(200);
   String trash = serialPort.readStringUntil(LINE_FEED);                // need to get stuff out of buffer
   println("made it through trash");
   p=1;
   readVal();
  
   }
 //  runState=! runState;
}

   void readVal(){
   float value = 0;
   for(int j = 0; j<10; j++){
     yRead = 0;
     while(yRead == 0){
     readData();
   }
   println("line 357");
      value += yRead;
//      println("value read for j = "+j);
   }
     yRead = value/10;
     println("line 361 buttons tab");
   }
   