void createGradientForm() throws DEC_Exception{
 DEC_Iterator edgeIterator = myComplex.createIterator(1,'p');
 HashMap<DEC_Object,Double> functionValues = discreteZeroForm.getValues();
 gradientForm = new DiscreteDifferentialForm(1,'p');
 while(edgeIterator.hasNext()){
  DEC_PrimalObject edge = (DEC_PrimalObject) edgeIterator.next();
  ArrayList<DEC_Object> edgeBounds = edge.boundary();
  int i0 = myComplex.objectIndexSearch(new DEC_PrimalObject(edgeBounds.get(0)));
  int i1 = myComplex.objectIndexSearch(new DEC_PrimalObject(edgeBounds.get(1)));
  double value = 0;
  if(i0 != -1 && i1 != -1){
   DEC_PrimalObject v0 = myComplex.getPrimalObject(0,i0);
   DEC_PrimalObject v1 = myComplex.getPrimalObject(0,i1);
   double f0 = functionValues.get(v0);
   double f1 = functionValues.get(v1);
   value = edge.getOrientation()*(f1-f0);
  }
  gradientForm.assignScalar(edge,value);
 }
}
void createLaplacianForm() throws DEC_Exception{
 
}
void createGradientVectorField() throws DEC_Exception{
 DEC_Iterator dualVertIterator = myComplex.createIterator(0,'d');
 HashMap<DEC_Object,Double> functionValues = discreteZeroForm.getValues();
 discreteGradient = new VectorAssignment(0,'d');
 while(dualVertIterator.hasNext()){
  DEC_DualObject dualVert = (DEC_DualObject) dualVertIterator.next();
  DEC_PrimalObject primalFace = myComplex.dual(dualVert);
  PVector faceCenter = primalFace.getVectorContent("CENTER");
  PVector faceNormal = primalFace.getVectorContent("NORMAL_0");
  PVector resultingGradient = new PVector();
  //construct star(partial(dualVert))
  ArrayList<DEC_Object> primalFaceBounds = primalFace.boundary();
  ArrayList<DEC_PrimalObject> primalEdges = new ArrayList<DEC_PrimalObject>();
  ArrayList<PVector> edgeCenters = new ArrayList<PVector>();
  for(int i=0;i<primalFaceBounds.size();i++){
   int edgeIndex = myComplex.objectIndexSearch(new DEC_PrimalObject(primalFaceBounds.get(i)));
   if(edgeIndex!=-1){
    DEC_PrimalObject primalEdge = myComplex.getPrimalObject(1,edgeIndex); 
    primalEdges.add(primalEdge);
    edgeCenters.add(primalEdge.getVectorContent("CENTER"));
   }
  }
  //construct inner terms in sharp sum
  for(int i=0;i<primalEdges.size();i++){
   double gradientFormValue = gradientForm.getValues().get(primalEdges.get(i));
   int org = primalEdges.get(i).getVertices().getIndex(0);
   int dest = primalEdges.get(i).getVertices().getIndex(1);
   ArrayList<PVector> edgeVertices = primalEdges.get(i).getGeometry(myContainer);
   ArrayList<PVector> supportVolume0 = new ArrayList<PVector>();
   ArrayList<PVector> supportVolume1 = new ArrayList<PVector>();
   supportVolume0.add(faceCenter);supportVolume1.add(faceCenter);
   supportVolume0.add(edgeCenters.get(i));supportVolume1.add(edgeCenters.get(i));
   supportVolume0.add(edgeVertices.get(0));supportVolume1.add(edgeVertices.get(1));
   for(int j=0;j<primalEdges.size();j++){
    if(j!=i){
     if(primalEdges.get(j).getVertices().containsIndex(org)){
      supportVolume0.add(primalEdges.get(j).getVectorContent("CENTER"));
     }
     if(primalEdges.get(j).getVertices().containsIndex(dest)){
      supportVolume1.add(primalEdges.get(j).getVectorContent("CENTER"));
     }
    }
   }
   DEC_DualObject dualFace0 = myComplex.getDualObject(2,primalEdges.get(i).getVertices().getIndex(0));
   DEC_DualObject dualFace1 = myComplex.getDualObject(2,primalEdges.get(i).getVertices().getIndex(1));
   float dualFaceVol_0 = dualFace0.volume(myContainer);
   float dualFaceVol_1 = dualFace1.volume(myContainer);
   float volume_0 = GeometricUtils.surfaceArea(supportVolume0);
   float volume_1 = GeometricUtils.surfaceArea(supportVolume1);
   PVector edgeAsVector = PVector.sub(edgeVertices.get(1),edgeVertices.get(0));
   edgeAsVector.mult(primalEdges.get(i).getOrientation());
   PVector rotatedEdge = rodriguesRotation(edgeAsVector, faceNormal, HALF_PI);
   rotatedEdge.mult((float) gradientFormValue*(volume_0/dualFaceVol_0+volume_1/dualFaceVol_1));
   resultingGradient.add(rotatedEdge);
  }
  Double[] gradient = new Double[]{(double) resultingGradient.x,
                                   (double) resultingGradient.y,
                                   (double) resultingGradient.z};
  discreteGradient.assignVector(dualVert,gradient);
 }
}
