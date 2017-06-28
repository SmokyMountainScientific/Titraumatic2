void chartsSetup(){
/*   // use the following to align charts
 float minY = 0.85*calVal[2];
 float maxY = 1.15*calVal[0];
 float minX = calPh[0]-1;
 float maxX = calPh[2]+1;
 float[] xVals = {minX, minX, maxX, maxX};
 float[] yVals = {minY, maxY, minX, maxX}; */
              ////////////////////////////////gicentre charts///
  lineChart = new XYChart(this);
  lineChart.setData(new float[] {1, 2, 3}, new float[] {1, 2, 3});
  lineChart.showXAxis(true); 
  lineChart.showYAxis(true);
  lineChart.setXAxisLabel("injected volume (mL)");
  lineChart.setYAxisLabel("pH");
  /////////////////// limits on chart ////

  //lineChart.setMinY(0);   
  lineChart.setYFormat("##.##");  
  lineChart.setXFormat("##.##");       
  // Symbol colours
  lineChart.setPointColour(color(234, 28, 28));
  lineChart.setPointSize(5);
  lineChart.setLineWidth(2);
  
                ////////////////////////////////Calibration charts///
  calChart = new XYChart(this);
  calChart.setData(calPh,calVal);
//  calChart.setData(xVals,yVals);    // use to align charts
  calChart.showXAxis(true); 
  calChart.showYAxis(true);
  calChart.setXAxisLabel("pH");
  calChart.setYAxisLabel("Read value");

/* float minY = 0.85*calVal[2];
 float maxY = 1.15*calVal[0];
  calChart.setMinY(minY);
  calChart.setMaxY(maxY);
  calChart.setMinX(calPh[0]-1);
  calChart.setMaxX(calPh[2]+1);  */
  calChart.setYFormat("##.##");  
  calChart.setXFormat("##.##");       
  // Symbol colours
  calChart.setPointColour(color(234, 28, 28));
  calChart.setPointSize(5);
//  calChart.setLineWidth(2);

 trendChart = new XYChart(this);
  trendChart.showXAxis(false); 
  trendChart.showYAxis(false);
  trendChart.setLineWidth(2);
  trendChart.setPointSize(1);
  setChartLims();
}/////////////////////////////////////////////////end charts_gic_setup///////////////////////////////////////////////

void setChartLims(){
 float minY = 0.85*calVal[2];
 float maxY = 1.15*calVal[0];
  calChart.setMinY(minY);
  calChart.setMaxY(maxY);
  calChart.setMinX(calPh[0]-1);
  calChart.setMaxX(calPh[2]+1);
  trendChart.setMinY(minY);
  trendChart.setMaxY(maxY);
  trendChart.setMinX(calPh[0]-1);
  trendChart.setMaxX(calPh[2]+1);
 float[] trendX = {calPh[0],calPh[2]};
 float[] trendY = {(calPh[0]-calInt)/calSlope,(calPh[2]-calInt)/calSlope};
 trendChart.setData(trendX,trendY);


}