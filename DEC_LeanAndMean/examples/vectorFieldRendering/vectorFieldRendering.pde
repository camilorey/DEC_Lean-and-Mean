//import OBJ Loader library
import saito.objloader.*;
//import lean-and-mean library
import utils.*;
import renderers.*;
import readers.*;
import fieldsAndForms.*;
import exceptions.*;
import complex.*;
import containers.*;
import DEC_Operators.*;
//-----------------------------------------------------------------
//----------------MOTION CONTROL-----------------------------------
//-----------------------------------------------------------------
float rotX=0;
float rotY=0;
float xTranslation = 0;
float yTranslation = 0;
float zTranslation = 0;
//-----------------------------------------------------------------
//---------------MODEL CONTROL-------------------------------------
//-----------------------------------------------------------------
char[] complexOptions = new char[]{'1','2','3','4','5','6'};
boolean[] showSubComplex = new boolean[]{true,false,false,false,false,false};
//-----------------------------------------------------------------
//--------------COMPLEX PARAMETERS---------------------------------
//-----------------------------------------------------------------
String modelName = "head.obj";
OBJMeshReader myReader;
DEC_GeometricContainer myContainer;
DEC_Complex myComplex;
ComplexViewer myViewer;
//-----------------------------------------------------------------
//---------------VECTOR FIELD PARAMETERS---------------------------
//-----------------------------------------------------------------
SolenoidVectorField solenoid;
VectorAssignment vectorAssignment;
VectorAssignmentViewer vectorFieldViewer;

void setup(){
 size(800,800,P3D);
 colorMode(HSB);
 textureMode(NORMAL);
 loadComplex(modelName, "BARYCENTRIC");
 solenoid = new SolenoidVectorField();
 vectorAssignment = new VectorAssignment(0, 'd');
 try{
  DEC_Iterator dualVertIterator = myComplex.createIterator(0,'d'); 
  while(dualVertIterator.hasNext()){
   DEC_DualObject dualVert = (DEC_DualObject) dualVertIterator.next();
   PVector center = dualVert.getVectorContent("CENTER");
   Double[] fieldValue = solenoid.fieldFunction(center.x,center.y,center.z);
   vectorAssignment.assignVector(dualVert,fieldValue);
  }
  vectorFieldViewer = new VectorAssignmentViewer(this,vectorAssignment,true);
  vectorFieldViewer.setScale(myViewer.getModelWHD());
  vectorFieldViewer.setColors(color(150,255,255),color(220,255,255));
  vectorFieldViewer.setArrowLength(15);
  vectorFieldViewer.setStrokeWeight(1);
  vectorFieldViewer.createLookUpTable(myComplex);
 }catch(DEC_Exception ex){
  println("something went wrong trying to create a vector field");
  ex.printStackTrace();
 }
}
void mouseDragged(){
 rotX += (mouseX-pmouseX)*0.01f;
 rotY -= (mouseY-pmouseY)*0.01f;
}
void mouseWheel(MouseEvent e){
  zTranslation += 10*e.getCount();
}
void keyPressed(){
 for(int i=0;i<complexOptions.length;i++){
  if(key == complexOptions[i]){
   if(!showSubComplex[i]){
    showSubComplex[i] = true;
   }else{
    showSubComplex[i] = false;
   }
  }
 }
 if(key == 'w' || key == 'W'){
  yTranslation -=10;
 }
 if(key == 's' || key == 'S'){
  yTranslation +=10;
 }
 if(key == 'a' || key == 'A'){
  xTranslation -=10;
 }
 if(key == 'd' || key == 'D'){
  xTranslation +=10;
 }
 if(key == '8'){
  loadComplex(modelName, "BARYCENTRIC");
 }
 if(key == '9'){
  loadComplex(modelName, "INCENTRIC");
 }
 if(key == '0'){
  loadComplex(modelName, "CIRCUMCENTRIC");
 }
 if(key =='z' || key == 'Z'){
  saveFrame("complexRenderingTest.png");
 }
 
}
void draw(){
 background(255);
 lights();
 translate(width/2,height/2,zTranslation);
 rotateX(rotY);
 rotateY(rotX);
 pushMatrix();
  translate(0,yTranslation,0);
  pushMatrix();
   translate(xTranslation,0,0);
   drawContent();
   try{
     vectorFieldViewer.plot(myComplex,myContainer);
   }catch(DEC_Exception ex){
    println("something went wrong trying to plot vector field");
    ex.printStackTrace();
   }
  popMatrix();
 popMatrix();
}



