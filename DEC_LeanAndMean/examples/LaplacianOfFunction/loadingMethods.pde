void loadComplex(String fileName, String centerType){
 myReader = new OBJMeshReader(fileName,this);
 myContainer = new DEC_GeometricContainer();
 myComplex = new DEC_Complex();
 myViewer = new ComplexViewer(this);
 myReader.loadModel(centerType,false); //load model without texture
 myViewer.setModelScale(50,myReader.getModelBoundingBox());
 //link reader to geometric container
 myContainer.setContent(myReader);
 myContainer.printContainerInfo();
 try{
   myComplex.setComplex(myContainer,myReader);
   myComplex.printComplexInformation();
 }catch(DEC_Exception ex){
  println("something went wrong trying to create complex");
  ex.printStackTrace();
 }
}
