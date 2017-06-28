// fileReadWrite tab, titraumatic sketch, Feb 9 2017


public void writeCal(){     // write calibration file
//String[] file = {"pH,reading","","",""};
for(int p = 1; p < 4; p++){
 file[p] = str(calPh[p-1])+','+str(calVal[p-1]);
}
 strDia = cp5Txt.get(Textfield.class, "diam").getText();
 file[4] = "diameter,"+strDia;
selectOutput("Select a file to save:","calFileSave");
}
 
 void calFileSave(File selection){
    if (selection == null){
       println("file selection was null");
    } else {
       String fileName = selection.getAbsolutePath();
       println("user selected "+ fileName);
       saveStrings(fileName, file);
    }
 }


void logData( String fileName, String newData, boolean appendData)  //char//void logData( String fileName, String newData, boolean appendData)
{
   saveStrings(fileName, file);
   BufferedWriter bw=null;
  try { //try to open the file
    FileWriter fw = new FileWriter(fileName, appendData);
    bw = new BufferedWriter(fw);
    bw.write(newData);// + System.getProperty("line.separator"));
  } 
  catch (IOException e) {
    println("it broke");
    e.printStackTrace();
  } 
  finally {
    if (bw != null) { //if file was opened try to close
      try {
        bw.close();
      } 
      catch (IOException e) {
        println("error closing writer");
      }
    }
  }
}