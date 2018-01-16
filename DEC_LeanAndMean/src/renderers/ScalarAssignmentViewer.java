/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderers;

import complex.DEC_Complex;
import complex.DEC_DualObject;
import complex.DEC_Iterator;
import complex.DEC_Object;
import complex.DEC_PrimalObject;
import containers.DEC_GeometricContainer;
import exceptions.DEC_Exception;
import fieldsAndForms.ScalarAssignment;
import fieldsAndForms.ScalarFunction;
import java.util.ArrayList;
import java.util.HashMap;
import processing.core.PApplet;
import static processing.core.PApplet.println;
import processing.core.PVector;
import static processing.core.PApplet.println;

/**
 *
 * @author laptop
 */
public class ScalarAssignmentViewer {
 protected PApplet parent;
 protected ScalarAssignment assignment;
 protected int dimension;
 protected char type;
 protected HashMap<PVector,Float> lookUpTable;
 protected HashMap<PVector,Float> vertexLookUpTable;
 protected boolean withInterpolation; 
 protected boolean usingModelNormals;
 protected float[] modelScale;
 protected float vertexSize;
 protected int color0;
 protected int color1;
 public ScalarAssignmentViewer(){
  this.parent = null;
  this.assignment = null;
  this.lookUpTable = null;
  this.withInterpolation = false;
 }
 public ScalarAssignmentViewer(PApplet parent){
  this.parent = parent;
  this.assignment = null;
  this.lookUpTable = null;
  this.withInterpolation = false;
 }
 public ScalarAssignmentViewer(PApplet parent, ScalarAssignment field){
  this.parent = parent;
  this.assignment = field;
  this.dimension = field.getDimension();
  this.type = field.getType();
  this.lookUpTable = new HashMap<PVector,Float>();
  this.withInterpolation = false;
 }
 public ScalarAssignmentViewer(PApplet parent, ScalarAssignment field,boolean withInterpolation){
  this.parent = parent;
  this.assignment = field;
  this.dimension = field.getDimension();
  this.type = field.getType();
  this.lookUpTable = new HashMap<PVector,Float>();
  this.withInterpolation = withInterpolation;
 }
 public void setPlotType(int dimension, char type){
  this.dimension = dimension;
  this.type = type;
 }
 public void setScale(float[] scale){
  this.modelScale = scale;
 }
 public void setVertexSize(float vertexSize){
  this.vertexSize = vertexSize;
 }
 public void setColors(int color0, int color1){
  this.color0 = color0;
  this.color1 = color1;
 }
 public int assignObjectColor(PVector position){
  float u = lookUpTable.get(position).floatValue();
  return parent.lerpColor(color0, color1, u);
 }
 public int assignVertexColor(PVector vertex){
  float u = vertexLookUpTable.get(vertex).floatValue();
  return parent.lerpColor(color0,color1,u);
 }
 public void usingNormals(String normalType){
  if(normalType.equals("MODEL")){
   usingModelNormals = true;
  }else{
   usingModelNormals = false;
  }
 }
 public void createLookUpTable(DEC_Complex complex) throws DEC_Exception{
  createObjectLookUpTable(complex);
  if(withInterpolation){
   createVertexLookUpTable(complex);
  }
 }
 public void createObjectLookUpTable(DEC_Complex complex)throws DEC_Exception{
  lookUpTable = new HashMap<PVector,Float>();
  DEC_Iterator iterator = complex.createIterator(dimension,type);
  double minValue = 1000000;
  double maxValue = -1000000; 
  while(iterator.hasNext()){
   DEC_Object object = iterator.next();
   double fValue = assignment.getValue(object);
   minValue = Math.min(fValue, minValue);
   maxValue = Math.max(fValue,maxValue);
  }
  DEC_Iterator lookUpIterator = complex.createIterator(dimension,type);
  while(lookUpIterator.hasNext()){
   DEC_Object object = lookUpIterator.next();
   double fValue = assignment.getValue(object);
   Double normalizedValue = new Double(1);
   if(maxValue!=minValue){
    normalizedValue = normalizedValue = new Double((fValue-minValue)/(maxValue-minValue));
   }
   lookUpTable.put(object.getVectorContent("CENTER"), normalizedValue.floatValue());
  }
 }
 public void createVertexLookUpTable(DEC_Complex complex) throws DEC_Exception{
  vertexLookUpTable = new HashMap<PVector,Float>();
  DEC_Iterator vertIterator = complex.createIterator(0,'p');
  double minValue = 1000000;
  double maxValue = -1000000; 
  while(vertIterator.hasNext()){
   DEC_PrimalObject vertex = (DEC_PrimalObject) vertIterator.next();
   double fValue = assignment.getValue(vertex);
   minValue = Math.min(fValue, minValue);
   maxValue = Math.max(fValue,maxValue);
  }
  DEC_Iterator lookUpIterator = complex.createIterator(0,'p');
  while(lookUpIterator.hasNext()){
   DEC_PrimalObject vertex = (DEC_PrimalObject) lookUpIterator.next();
   double fValue = assignment.getValue(vertex);
   Double normalizedValue = new Double(1);
   if(maxValue!=minValue){
    normalizedValue = normalizedValue = new Double((fValue-minValue)/(maxValue-minValue));
   }
   vertexLookUpTable.put(vertex.getVectorContent("CENTER"), normalizedValue.floatValue());
  }
 }
 public void plotInterpolatedFace(SimplexViewer viewer){
  ArrayList<PVector> verts = viewer.getSimplexVertices();
  parent.beginShape();
  for(int i=0;i<verts.size();i++){
   int resultingColor = assignVertexColor(verts.get(i));
   PVector p = new PVector(verts.get(i).x*modelScale[0],
                           verts.get(i).y*modelScale[1],
                           verts.get(i).z*modelScale[2]);
   parent.fill(resultingColor);parent.vertex(p.x,p.y,p.z);
  }
  parent.endShape(PApplet.CLOSE);
 }
 public void plotInterpolatedSurface(DEC_Complex complex, DEC_GeometricContainer container) throws DEC_Exception{
  DEC_Iterator iterator = complex.createIterator(2,'p');
  while(iterator.hasNext()){
   DEC_PrimalObject face = (DEC_PrimalObject) iterator.next();
   SimplexViewer faceViewer = new SimplexViewer(parent,face);
   faceViewer.setScale(modelScale);
   faceViewer.getGeometry(container);
   plotInterpolatedFace(faceViewer);
  }
 }
 public void plotAssignment(DEC_Complex complex,DEC_GeometricContainer container) throws DEC_Exception{
   DEC_Iterator iterator = complex.createIterator(dimension, type);
   while(iterator.hasNext()){
    if(type == 'p'){
     DEC_PrimalObject content = (DEC_PrimalObject) iterator.next();
     PVector center = content.getVectorContent("CENTER");
     SimplexViewer viewer = new SimplexViewer(parent, content);
     viewer.setScale(modelScale);
     viewer.getGeometry(container);
     int resultingColor = assignObjectColor(center);
     if(dimension == 0){
      parent.noStroke();
      parent.fill(resultingColor);
      viewer.plotVertex(vertexSize);
     }else if(dimension==1){
      parent.stroke(resultingColor);
      viewer.plotPrimalEdge();
     }else if(dimension == 2){
      if(withInterpolation){
       plotInterpolatedFace(viewer);
      }else{
       parent.fill(resultingColor);
       viewer.plotPrimalFace(usingModelNormals);
      }
     }
    }else{
     DEC_DualObject content = (DEC_DualObject) iterator.next();
     PVector center = content.getVectorContent("CENTER");
     SimplexViewer viewer = new SimplexViewer(parent, content);
     viewer.setScale(modelScale);
     int resultingColor = assignObjectColor(center);
     if(dimension == 0){
      viewer.getGeometry(container);
      parent.noStroke();
      parent.fill(resultingColor);
      viewer.plotVertex(vertexSize);
     }else if(dimension == 1){
      viewer.getGeometry(container);
      parent.stroke(resultingColor);
      viewer.plotDualEdge();
     }else if(dimension == 2){
      parent.stroke(resultingColor);
      parent.fill(resultingColor);
      viewer.plotDualFace();
     }
    }
   }
 }
}
