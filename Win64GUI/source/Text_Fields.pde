void setupTxtFields() { 
 cp5Txt = new ControlP5(this);  
int step = 115;
//int box1Off = 25;
  speed = cp5Txt.addTextfield("speed")  // time based txt field
//  cp5Txt.addTextfield("speed")  // time based txt field
    .setColor(#030302) 
      .setColorBackground(#CEC6C6) 
        .setColorForeground(#AA8A16) 
          .setPosition(240, box1Ypos+off1) //box1Off)
            .setSize(45, 20)
   //           .setFont(font)// 1/3/17
                .setFocus(false)
                    .setText("300");
  /*                    controlP5.Label ril = speed.captionLabel(); 
                        ril.setFont(font);
                          ril.toUpperCase(false);
                            ril.setText("Speed (uL/s)");     */
 int textY1 = 35;
 
 volume = cp5Txt.addTextfield("volume")  // time based txt field
//  cp5Txt.addTextfield("speed")  // time based txt field
    .setColor(#030302) 
      .setColorBackground(#CEC6C6) 
        .setColorForeground(#AA8A16) 
          .setPosition(320,textY1)
            .setSize(45, 20)
   //           .setFont(font)  // 1/3/17
                .setFocus(false)
                    .setText("200");
   /*                   controlP5.Label vol = volume.captionLabel(); 
                        vol.setFont(font);
                          vol.toUpperCase(false);
                            vol.setText("Vol. (uL)");     */

/*  injections = cp5Txt.addTextfield("injections")  // time based txt field
//  cp5Txt.addTextfield("speed")  // time based txt field
    .setColor(#030302) 
      .setColorBackground(#CEC6C6) 
        .setColorForeground(#AA8A16) 
          .setPosition(20, 150)
            .setSize(45, 20)
   //           .setFont(font)  // 1/3/17
                .setFocus(false)
                    .setText("1");
    */ 
                            
injDelay = cp5Txt.addTextfield("Delay_(s)")  // time based txt field
//  cp5Txt.addTextfield("speed")  // time based txt field
    .setColor(#030302) 
      .setColorBackground(#CEC6C6) 
        .setColorForeground(#AA8A16) 
          .setPosition(380, textY1)

            .setSize(45, 20)
              .setFont(font12)
                .setFocus(false)
                    .setText("2.0");
    /*                  controlP5.Label inDl = injDelay.captionLabel(); 
                        inDl.setFont(font);
                          inDl.toUpperCase(false);
                            inDl.setText("Delay (s)");     */
                            
 target = cp5Txt.addTextfield("target")  // status and com port text area
    .setPosition(320, textY1+25)
      .setSize(40, 20)   //was 30
      .setFont(font12) //(font)
         //.setLineHeight(14)
            .setColor(#030302)
              .setColorBackground(#CEC6C6)
                .setColorForeground(#AA8A16)//#CEC6C6
                .setText("14.00");
                    
diam = cp5Txt.addTextfield("diam")  
    .setColor(#030302) 
      .setColorBackground(#CEC6C6) 
        .setColorForeground(#AA8A16) 
          .setPosition(160, box1Ypos+off1)  //box1Off)
            .setSize(45, 20)
              .setFont(font12)
                .setFocus(false)
                    .setText(strDia);
  
                            

                    
setVol  = cp5Txt.addTextfield("volume_(mL)")  
    .setColor(#030302) 
      .setColorBackground(#CEC6C6) 
        .setColorForeground(#AA8A16) 
          .setPosition(240, box1Ypos+10) //+off1)
            .setSize(45, 20)
              .setFont(font12)
                .setFocus(false)
                    .setText("2.00");                   
                         
calA = cp5Txt.addTextfield("calA")  // status and com port text area
    .setPosition(30, 2*box1Ypos+step)
      .setSize(40, 20)   //was 30
      .setFont(font12) //(font)
         //.setLineHeight(14)
            .setColor(#030302)
              .setColorBackground(#CEC6C6)
                .setColorForeground(#AA8A16)//#CEC6C6
                .setText(strPh[0])
   //               .setLock(true)
  //                  .setLabel("4.00")
                    ;
                    
 calB = cp5Txt.addTextfield("calB")  // status and com port text area
    .setPosition(30, 2*box1Ypos+25+step)
      .setSize(40, 20)   //was 30
      .setFont(font12) //(font)
         //.setLineHeight(14)
            .setColor(#030302)
              .setColorBackground(#CEC6C6)
                .setColorForeground(#AA8A16)//#CEC6C6
                .setText(strPh[1]);     
                
 calC = cp5Txt.addTextfield("calC")  // status and com port text area
    .setPosition(30, 2*box1Ypos+50+step)
      .setSize(40, 20)   //was 30
      .setFont(font12) //(font)
         //.setLineHeight(14)
            .setColor(#030302)
              .setColorBackground(#CEC6C6)
                .setColorForeground(#AA8A16)//#CEC6C6
                .setText(strPh[2]);
}
public void getParams(){
  
// println("in speed loop");
////////////////get speed value from text box
  stringMode = String.valueOf(modeState);
  
  stringSpeed = cp5Txt.get(Textfield.class, "speed").getText();
  /* Info from text fields transmitted to u-controller as strings
   * to convert to float, int, or char, use commands below:
   *  float fSpeed = float(stringSpeed);
   *  iSpeed = round(fSpeed);
   *  cSpeed = char(iSpeed);
  */
  println("in params loop, line 143");
///////// get volume from text field
if (modeState == 0){
   println("in ModeState 0 loop");
   stringVolume = cp5Txt.get(Textfield.class, "volume_(mL)").getText();
   println("got text");
   int p = int(1000*float(stringVolume));
   stringVolume = str(p);
   println("volume: "+stringVolume);
//}
println("params, line 153");
}
else{
  stringVolume = cp5Txt.get(Textfield.class, "volume").getText();
  println("params, line 156");
}
///////// get injetions from text field
//  stringInjections = cp5Txt.get(Textfield.class, "injections").getText();
println("params, line 160");
  stringInjDelay = cp5Txt.get(Textfield.class, "Delay_(s)").getText();
  sTarget = cp5Txt.get(Textfield.class, "target").getText();
  fTarget = float(sTarget);
  strDia = cp5Txt.get(Textfield.class, "diam").getText();
  int intDia = int(1000*float(strDia));
  strDia = str(intDia);
}