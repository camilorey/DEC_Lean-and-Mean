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
ScalarAssignment valenceAssignment;
ScalarAssignmentViewer valenceViewer;
int numEvenValence = 0;
int numOddValence = 0;
float meanValence = 0;
boolean showValence = true;

void setup(){
 size(800,800,P3D);
 colorMode(HSB);
 textureMode(NORMAL);
 loadComplex(modelName, "BARYCENTRIC");
 println("Euler Characteristic of the surface");
 int V = myComplex.numPrimalVertices();
 int E = myComplex.numPrimalEdges();
 int F = myComplex.numPrimalFaces();
 println("V-E+F="+(V-E+F));
 try{
  valenceAssignment = new ScalarAssignment(0,'p');
  createValenceAssignment();
  println("even valence: "+numEvenValence + " percentage: "+((float) numEvenValence / (float) myComplex.numPrimalVertices()));
  println("odd valence: "+numOddValence + " percentage: "+((float) numOddValence / (float) myComplex.numPrimalVertices()));
  println("mean valence: "+meanValence);
  valenceViewer = new ScalarAssignmentViewer(this,valenceAssignment,false);
  valenceViewer.setScale(myViewer.getModelWHD());
  valenceViewer.createLookUpTable(myComplex);
  valenceViewer.setColors(color(120,255,255),color(255,255,255));
  valenceViewer.setVertexSize(2);
 }catch(DEC_Exception ex){
  println("something went wrong trying to calculate the valence of vertices");
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
  saveFrame("valenceTest.png");
 }
 if(key == 'b' || key == 'B'){
  if(!showBoundary){
   showBoundary = true;
  }else{
   showBoundary = false;
  }
 }
 if(key == 'v' || key == 'V'){
  if(!showValence){
   showValence = true;
  }else{
   showValence = false;
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
   if(showValence){
    try{
     valenceViewer.plotAssignment(myComplex,myContainer);
    }catch(DEC_Exception ex){
     println("something went wrong trying to plot discrete laplacian");
     ex.printStackTrace();
    }
   }
  popMatrix();
 popMatrix();
}



