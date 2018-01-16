void drawContent(){
 drawComplex();
}
void drawComplex(){
 if(showSubComplex[2]){
  fill(200);
  noStroke();
  myViewer.plotComplex(myComplex,myContainer,2,'p');
 }
 if(showSubComplex[5]){
  fill(200);
  myViewer.plotComplex(myComplex,myContainer,2,'d');
 }
 if(showSubComplex[1]){
  stroke(0);
  strokeWeight(1);
  myViewer.plotComplex(myComplex,myContainer,1,'p');
  if(showBoundary){
   DEC_Iterator edgeIterator = myComplex.createIterator(1,'p');
   try{
    while(edgeIterator.hasNext()){
     DEC_PrimalObject edge = (DEC_PrimalObject) edgeIterator.next();
     if(edge.isBorder()){
      SimplexViewer edgeViewer = new SimplexViewer(this,edge);
      edgeViewer.getGeometry(myContainer);
      edgeViewer.setScale(myViewer.getModelWHD());
      strokeWeight(5);
      stroke(255,255,255);
      edgeViewer.plotPrimalEdge();
     }
    }
   }catch(DEC_Exception ex){
    println("something went wrong trying to plot boundary edges");
    ex.printStackTrace();
   }
  }
 }
 if(showSubComplex[4]){
  stroke(0);
  strokeWeight(1);
  myViewer.plotComplex(myComplex,myContainer,1,'d');
 }
 if(showSubComplex[0]){
  fill(255,255,255);
  myViewer.plotComplex(myComplex,myContainer,0,'p');
 }
 if(showSubComplex[3]){
  fill(150,255,255);
  myViewer.plotComplex(myComplex,myContainer,0,'d');
 }
}
