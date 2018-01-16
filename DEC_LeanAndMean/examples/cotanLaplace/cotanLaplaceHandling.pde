void createLaplacianForm() throws DEC_Exception{
 DEC_Iterator vertexIterator = myComplex.createIterator(0,'p');
 discreteLaplacian = new DiscreteDifferentialForm(0,'p');
 HashMap<DEC_Object,Double> functionValues = discreteZeroForm.getValues();
 while(vertexIterator.hasNext()){
  double discreteLaplacianValue = 0;
  DEC_PrimalObject vert = (DEC_PrimalObject) vertexIterator.next();
  double functionValue = functionValues.get(vert);
  int vertIndex = vert.getIndex();
  discreteLaplacian.assignScalar(vert,discreteLaplacianValue);
  DEC_Iterator vertNeighborhood = myComplex.objectNeighborhood(vert);
  while(vertNeighborhood.hasNext()){
   DEC_PrimalObject neighbor = (DEC_PrimalObject) vertNeighborhood.next();
   int neighIndex = neighbor.getIndex();
   double neighFunctionValue = functionValues.get(neighbor);
   discreteLaplacianValue += cotanLaplace.get(vertIndex, neighIndex)*(neighFunctionValue-functionValue);
  }
  discreteLaplacian.assignScalar(vert,discreteLaplacianValue);
 }
 
}
void createCotanLaplaceMatrix() throws DEC_Exception{
 int numVerts = myComplex.numPrimalVertices();
 cotanLaplace = new SparseMatrix(numVerts,numVerts);
 DEC_Iterator vertexIterator = myComplex.createIterator(0,'p');
 while(vertexIterator.hasNext()){
  DEC_PrimalObject vert = (DEC_PrimalObject) vertexIterator.next();
  int vertIndex = vert.getIndex();
  PVector vertexNormal = vert.getVectorContent("NORMAL_0");
  PVector vertexCenter = myContainer.getGeometricContent(vert).get(0);
  DEC_Iterator vertNeighborhood = myComplex.objectNeighborhood(vert);
  while(vertNeighborhood.hasNext()){
   DEC_PrimalObject neighbor = (DEC_PrimalObject) vertNeighborhood.next();
   int neighIndex = neighbor.getIndex();
   PVector neighCenter = neighbor.getVectorContent("CENTER");
   DEC_PrimalObject tempEdge = new DEC_PrimalObject(new IndexSet(vertIndex,neighIndex));
   int edgeIndex = myComplex.objectIndexSearch(tempEdge);
   DEC_PrimalObject edge = myComplex.getPrimalObject(1,edgeIndex);
   DEC_DualObject dualEdge = myComplex.dual(edge);
   int f0Index = dualEdge.getVertices().getIndex(0);
   int f1Index = dualEdge.getVertices().getIndex(1);
   DEC_PrimalObject f0 = myComplex.getPrimalObject(2,f0Index);
   DEC_PrimalObject f1 = myComplex.getPrimalObject(2,f1Index);
   ArrayList<PVector> f0Verts = myContainer.getGeometricContent(f0);
   ArrayList<PVector> f1Verts = myContainer.getGeometricContent(f1);
   int index0 = indexOfOtherVector(vertexCenter,neighCenter,f0Verts);
   int index1 = indexOfOtherVector(vertexCenter,neighCenter,f0Verts);
   if(index0 != -1 && index1!=-1){
    PVector pivot0 = f0Verts.get(index0);
    PVector pivot1 = f1Verts.get(index1); 
    PVector c00 = PVector.sub(vertexCenter,pivot0);
    PVector c01 = PVector.sub(neighCenter,pivot0);
    PVector c10 = PVector.sub(vertexCenter,pivot1);
    PVector c11 = PVector.sub(neighCenter,pivot1);
    float angle0 = PVector.angleBetween(c00,c01);
    float angle1 = PVector.angleBetween(c10,c11);
    float tan0 = tan(angle0);
    float tan1 = tan(angle1);
    if(tan0!=0 && tan1!=0){
     cotanLaplace.set(1/tan(angle0)+1/tan(angle1),vertIndex,neighIndex);
    }else if(tan0==0 && tan1!=0){
     println("angle0 failed"); 
     cotanLaplace.set(1/tan(angle1),vertIndex,neighIndex);
    }else if(tan0!=0 && tan1==0){
     println("angle1 failed"); 
     cotanLaplace.set(1/tan(angle0),vertIndex,neighIndex);
    }else{
     println("both angles failed");
    }
   }
  }
 }
}
int indexOfOtherVector(PVector v0, PVector v1, ArrayList<PVector> list){
 int index = -1;
 for(int i=0;i<list.size();i++){
  if((list.get(i).x != v0.x && list.get(i).y != v0.y && list.get(i).z != v0.z) 
   &&(list.get(i).x != v1.x && list.get(i).y != v1.y && list.get(i).z != v1.z)){
   index = i;
  }
 }
 return index;
}
ArrayList<PVector> removeVertexFromList(PVector v, ArrayList<PVector> list){
 int index = -1;
 for(int i=0;i<list.size();i++){
  if(list.get(i).x == v.x && list.get(i).y == v.y && list.get(i).z == v.z){
   index = i;
  }
 }
 if(index!=-1){
  list.remove(index);
 }
 return list;
}
