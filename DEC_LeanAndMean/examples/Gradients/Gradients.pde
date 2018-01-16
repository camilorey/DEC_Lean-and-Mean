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
String modelName = "hand.obj";
OBJMeshReader myReader;
DEC_GeometricContainer myContainer;
DEC_Complex myComplex;
ComplexViewer myViewer;
//-----------------------------------------------------------------
//---------------SCALAR FIELD PARAMETERS---------------------------
//-----------------------------------------------------------------
ContinuousDifferentialForm myZeroForm;
ScalarFieldMatrix zeroFormCoefficients;
ScalarAssignmentViewer zeroFormViewer;
ScalarAssignmentViewer continuousLaplacianViewer;
ScalarAssignmentViewer laplacianFormViewer;
ScalarAssignmentViewer oneFormViewer;
boolean showZeroForm = true;
//-----------------------------------------------------------------
//--------------DIFFERENTIAL OPERATOR PARAMETERS-------------------
//-----------------------------------------------------------------
DeRhamOperator deRham;
DiscreteDifferentialForm discreteZeroForm;
VectorAssignment discreteGradient;
VectorAssignmentViewer discreteGradientViewer;
DiscreteDifferentialForm gradientForm;
DiscreteDifferentialForm discreteLaplacian;
boolean showGradientForm = true;
boolean showGradientField = true;
void setup(){
 size(800,800,P3D);
 colorMode(HSB);
 textureMode(NORMAL);
 loadComplex(modelName, "BARYCENTRIC");
 zeroFormCoefficients = new ScalarFieldMatrix(1,1);
 zeroFormCoefficients.setComponents(new ScalarFunction[]{new ScalarField()});
 myZeroForm = new ContinuousDifferentialForm(zeroFormCoefficients,'p');
 deRham = new DeRhamOperator(myComplex);
 try{
  myZeroForm.calculateForm(null,myComplex);
  discreteZeroForm = deRham.apply(myZeroForm,myContainer); 
  createGradientForm();
  createGradientVectorField();
  //set viewing parameters for zero form
  zeroFormViewer = new ScalarAssignmentViewer(this,discreteZeroForm,true);
  zeroFormViewer.setScale(myViewer.getModelWHD());
  zeroFormViewer.createLookUpTable(myComplex);
  zeroFormViewer.setColors(color(120,255,255),color(255,255,255));
  zeroFormViewer.setVertexSize(5);
  //set viewing parameters for gradient form
  oneFormViewer = new ScalarAssignmentViewer(this,gradientForm,false);
  oneFormViewer.setScale(myViewer.getModelWHD());
  oneFormViewer.createLookUpTable(myComplex);
  oneFormViewer.setColors(color(120,255,255),color(255,255,255));
  //set viewing parameters for gradient vector field
  discreteGradientViewer = new VectorAssignmentViewer(this,discreteGradient,true);
  discreteGradientViewer.setScale(myViewer.getModelWHD());
  discreteGradientViewer.setColors(color(120,255,255),color(255,255,255));
  discreteGradientViewer.setArrowLength(15);
  discreteGradientViewer.setStrokeWeight(1);
  discreteGradientViewer.createLookUpTable(myComplex);
 }catch(DEC_Exception ex){
  println("something went wrong calculating 0-form");
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
 if(key == 'q' || key == 'Q'){
  if(!showZeroForm){
   showZeroForm = true;
  }else{
   showZeroForm = false;
  }
 }
 if(key == 'g' || key == 'G'){
  if(!showGradientForm){
   showGradientForm = true;
  }else{
   showGradientForm = false;
  }
 }
 if(key == 'v' || key == 'V'){
  if(!showGradientField){
   showGradientField = true;
  }else{
   showGradientField = false;
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
   drawComplex();
   if(showZeroForm){
    try{
     zeroFormViewer.plotInterpolatedSurface(myComplex,myContainer);
    }catch(DEC_Exception ex){
     println("something went wrong trying to plot 0-form");
     ex.printStackTrace();
    }
   }
  if(showGradientForm){
   try{
    oneFormViewer.plotAssignment(myComplex,myContainer);
   }catch(DEC_Exception ex){
    println("something went wrong trying to plot gradient form");
    ex.printStackTrace();
   }
  }
  if(showGradientField){
   try{
     discreteGradientViewer.plot(myComplex,myContainer);
   }catch(DEC_Exception ex){
    println("something went wrong trying to plot vector field");
    ex.printStackTrace();
   }
  }
  popMatrix();
 popMatrix();
}



