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
boolean showBoundary = true;
//-----------------------------------------------------------------
//--------------COMPLEX PARAMETERS---------------------------------
//-----------------------------------------------------------------
String modelName = "head.obj";
OBJMeshReader myReader;
DEC_GeometricContainer myContainer;
DEC_Complex myComplex;
ComplexViewer myViewer;
//-----------------------------------------------------------------
//-------------VALENCE PARAMETERS----------------------------------
//-----------------------------------------------------------------
ScalarAssignment curvatureAssignment;
ScalarAssignmentViewer curvatureViewer;
boolean showCurvature = true;

void setup(){
 size(800,800,P3D);
 colorMode(RGB);
 textureMode(NORMAL);
 loadComplex(modelName, "BARYCENTRIC");
 println("Euler Characteristic of the surface");
 int V = myComplex.numPrimalVertices();
 int E = myComplex.numPrimalEdges();
 int F = myComplex.numPrimalFaces();
 println("V-E+F="+(V-E+F));
 try{
  curvatureAssignment = new ScalarAssignment(0,'p');
  createCurvatureAssignment();
  curvatureViewer = new ScalarAssignmentViewer(this,curvatureAssignment,true);
  curvatureViewer.setScale(myViewer.getModelWHD());
  curvatureViewer.createLookUpTable(myComplex);
  curvatureViewer.setColors(color(255,0,0),color(0,0,255));
  curvatureViewer.setVertexSize(5);
 }catch(DEC_Exception ex){
  println("something went wrong trying to calculate the curvature of surface");
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
  saveFrame("curvatureTest.png");
 }
 if(key == 'b' || key == 'B'){
  if(!showBoundary){
   showBoundary = true;
  }else{
   showBoundary = false;
  }
 }
 if(key == 'c' || key == 'C'){
  if(!showCurvature){
   showCurvature = true;
  }else{
   showCurvature = false;
  }
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
   if(showCurvature){
    try{
     noStroke();
     curvatureViewer.plotInterpolatedSurface(myComplex,myContainer);
    }catch(DEC_Exception ex){
     println("something went wrong trying to plot discrete laplacian");
     ex.printStackTrace();
    }
   }
  popMatrix();
 popMatrix();
}



