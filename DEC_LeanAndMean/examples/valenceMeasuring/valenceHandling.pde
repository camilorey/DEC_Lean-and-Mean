void createValenceAssignment() throws DEC_Exception{
 DEC_Iterator vertexIterator = myComplex.createIterator(0,'p');
 while(vertexIterator.hasNext()){
  DEC_PrimalObject vert = (DEC_PrimalObject) vertexIterator.next();
  ArrayList<DEC_PrimalObject> contFaces = myComplex.primalFacesContaining(vert);
  int valenceNumber = contFaces.size();
  meanValence += (float) valenceNumber /(float) myComplex.numPrimalVertices(); 
  double valence = valenceNumber%2 == 0 ? 0: 1;
  if(valence == 0){
   numEvenValence++;
  }else{
   numOddValence++;
  }
  valenceAssignment.assignScalar(vert, valence);
 }
}
