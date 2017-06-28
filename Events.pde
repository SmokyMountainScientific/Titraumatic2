public void controlEvent(ControlEvent theEvent) {
//  print("coming from the Event controller:  ");
  //println(theEvent.getController().getName());
//////// new stuff //////////////////////
/*  if (theEvent.isGroup() ) {
    println("event from group: "+theEvent.getGroup().getValue()+" from "+theEvent.getGroup());
//    float Mod =theEvent.getGroup().getValue();
    modeState =(int)theEvent.getGroup().getValue();
   println("modeState "+modeState);

    
      if(theEvent.name().equals("mode")){
        if (modeState == 0) {
        Modetorun = "toSignal";
  //      Modesel = true;
        }
        if (modeState == 1) {
        Modetorun = "toVol";
  //      Modesel = true;
        }
              if (modeState == 2) {
        Modetorun = "Titrate";
  //      Modesel = true;
        }
              if (modeState == 3) {
        Modetorun = "pHAdjust";
   //     Modesel = true;
        }
        println(Modetorun);
      }
      
    } */

  if (theEvent.isController()) {
    println("event from controller : "+theEvent.getController().getValue()+" from "+theEvent.getController());
    //println("event from controller : "+theEvent.getController().getValue()+" from "+theEvent.getController()
    
    if(theEvent.isFrom(valveKnob)){
          
          if(serialPort!=null){
            println("<"+(int)theEvent.getController().getValue()+">");
            int knobpos = (int)theEvent.getController().getValue();
            if(valvewritepos != knobpos){
               valvewritepos = (int)theEvent.getController().getValue();
              if(valvewritepos == 1){
                serialPort.write("a");
              }
              if(valvewritepos == 2){
                serialPort.write("b");
              }
              if(valvewritepos == 3){
                serialPort.write("c");
              }
              println("new pos "+(int)theEvent.getController().getValue());
            }
          }
      }
  }
}