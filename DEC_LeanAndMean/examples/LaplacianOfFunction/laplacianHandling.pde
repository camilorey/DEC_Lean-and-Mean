void createLaplacianContext(){
 try{
  myZeroForm.calculateForm(null,myComplex);
  continuousLaplacianForm.calculateForm(null,myComplex);
  discreteZeroForm = deRham.apply(myZeroForm,myContainer);  
   //set viewing parameters on discrete laplacian
  createLaplacianForm();
  laplacianFormViewer = new ScalarAssignmentViewer(this,discreteLaplacian,true);
  laplacianFormViewer.setScale(myViewer.getModelWHD());
  laplacianFormViewer.createLookUpTable(myComplex);
  laplacianFormViewer.setColors(color(120,255,255),color(255,255,255));
  laplacianFormViewer.setVertexSize(5);
 }catch(DEC_Exception ex){
  println("something went wrong trying to create the Laplacian");
  ex.printStackTrace();
 }
}
void createLaplacianForm() throws DEC_Exception{
 DEC_Iterator vertexIterator = myComplex.createIterator(0,'p');
 discreteLaplacian = new DiscreteDifferentialForm(0,'p');
 HashMap<DEC_Object,Double> functionValues = discreteZeroForm.getValues();
 while(vertexIterator.hasNext()){
  DEC_PrimalObject vert = (DEC_PrimalObject) vertexIterator.next();
  int vertIndex = vert.getVertices().getIndex(0);
  double functionValue = functionValues.get(vert);
  DEC_DualObject dualFace = myComplex.dual(vert);
  ArrayList<DEC_PrimalObject> contEdges = myComplex.primalEdgesContaining(vert);
  double discreteLaplacianValue = 0;
  for(int i=0;i<contEdges.size();i++){
   ArrayList<DEC_Object> edgeBoundary = contEdges.get(i).boundary();
   DEC_DualObject dualEdge = myComplex.dual(contEdges.get(i));
   float primalEdgeVol = contEdges.get(i).volume(myContainer);
   float dualEdgeVol = dualEdge.volume(myContainer);
   int otherVertIndex = -1;
   if(edgeBoundary.get(0).getVertices().getIndex(0) != vertIndex){
    otherVertIndex = edgeBoundary.get(0).getVertices().getIndex(0);
   }else{
    otherVertIndex = edgeBoundary.get(1).getVertices().getIndex(0);
   }
   DEC_PrimalObject otherVert = myComplex.getPrimalObject(0,otherVertIndex);
   double otherVertFunctionValue = functionValues.get(otherVert);
   discreteLaplacianValue += contEdges.get(i).getOrientation()*
                             (dualEdgeVol/primalEdgeVol)*(otherVertFunctionValue-functionValue);
  }
  float dualFaceVolume = dualFace.volume(myContainer);
  discreteLaplacianValue /= dualFaceVolume;
  discreteLaplacian.assignScalar(vert,discreteLaplacianValue);
 }
}
