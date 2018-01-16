void createCurvatureAssignment() throws DEC_Exception{
 DEC_Iterator vertexIterator = myComplex.createIterator(0,'p');
 while(vertexIterator.hasNext()){
  DEC_PrimalObject vert = (DEC_PrimalObject) vertexIterator.next();
  PVector vertexNormal = vert.getVectorContent("NORMAL_0");
  PVector vertexCenter = myContainer.getGeometricContent(vert).get(0);
  double angleDefect = 0;
  DEC_Iterator vertNeighborhood = myComplex.objectNeighborhood(vert);
  ArrayList vertsAsList = vertNeighborhood.getList();
  for(int i=0;i<vertsAsList.size();i++){
   DEC_PrimalObject v0 = (DEC_PrimalObject) vertsAsList.get(i);
   DEC_PrimalObject v1 = (DEC_PrimalObject) vertsAsList.get((i+1)%(vertsAsList.size()));
   PVector c0 = myContainer.getGeometricContent(v0).get(0);
   PVector c1 = myContainer.getGeometricContent(v1).get(0);
   float angle = PVector.angleBetween(PVector.sub(c1,vertexCenter),PVector.sub(c0,vertexCenter));
   angleDefect += angle;
  }
  double meanCurvature = 2*Math.PI-angleDefect;
  curvatureAssignment.assignScalar(vert, meanCurvature);
 }
}

