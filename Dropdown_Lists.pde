 // dropdown lists depricated, change to scrollableList
 
 void dropdownSetup() {
   //////////////////////////////////////////Dropdownlist////////////////////////////////////////
 cp5list = new ControlP5(this); 
 List modes = Arrays.asList("Manual","Calibration", "Titration"); //"pH Adjust", "Titration");
//  mode = cp5list.addDropdownList("list-2", 250, 30, 80, 84);     //Nov. 1, 2013
  cp5list.addScrollableList("mode")
    .setPosition(250, 10)
    .setSize(80, 104)
    .setItemHeight(20)
    .setBarHeight(20)
    .addItems(modes)
    ;
    

 }
void mode(int n){
  print("from ScrollableList, n = ");
  println(n, cp5list.get(ScrollableList.class,"mode").getItem(n));
  modeState = n;
  setupScreen();
}


void keyPressed() {
   
 }
 
 void setupScreen(){
       if (modeState == 0) {    // Manual
     logFile = false;
     bkgd = #8B9593;
       surface.setSize(350, 210);
              volume.hide();
       speed.show();
   //    injections.hide();
       injDelay.hide();
       dirButton.show();
       runButton.hide();
       runPump.show();
       valDir.show();
       setVol.show();
   }
        else {
       dirButton.hide();
       runPump.hide(); 
  //     runValve.hide();
       valDir.hide();
      setVol.hide();
     }
  if (modeState == 1) {  // calibrate
          logFile = false;
          bkgd = #7A8995;
      surface.setSize(600, 420);
      println("modeState 1, dropdownlist, line 56");
         //injections.hide();
       injDelay.hide();
       volume.hide();
       speed.show();
    //   injections.hide();
  //     syrCal.show();
  //     setVol.show();
  //     injDelay.show();
  //   dirButton.hide();
            runButton.hide();
            calA.show();
            calB.show();
            calC.show();
            loadCal.show();
            readCal.show();
            diam.show();
            setA.show();
            setB.show();
            setC.show();
            read.show();
            reCalc.show();   
  }else {
       calA.hide();
       calB.hide();
       calC.hide();
            setA.hide();
            setB.hide();
            setC.hide();
       loadCal.hide();
       readCal.hide();
       diam.hide();
 //      syrCal.hide();
//       setVol.hide();
       reCalc.hide();
       read.hide();
     }
          if (modeState == 2 || modeState == 3) {  // pH adjust
          logFile = true;   // may want to change this in the GUI later
     bkgd = #529079;
      surface.setSize(650, 550);
//       frame.setSize(850, 550);
       target.show();
       speed.hide();
 //      injections.hide();
       readPh.show();
       volume.show();
       injDelay.show();
       runButton.show();  
       savRun.show();
            pause.show();
       
          }  else{
     savRun.hide();
     target.hide();
     pause.hide();
  }
  
 }