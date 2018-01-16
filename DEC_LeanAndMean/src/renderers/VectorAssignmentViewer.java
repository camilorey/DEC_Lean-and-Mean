/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderers;

import complex.DEC_Complex;
import complex.DEC_Iterator;
import complex.DEC_Object;
import containers.DEC_GeometricContainer;
import exceptions.DEC_Exception;
import fieldsAndForms.ScalarAssignment;
import fieldsAndForms.VectorAssignment;
import java.util.HashMap;
import processing.core.PApplet;
import processing.core.PVector;

/**
 *
 * @author laptop
 */
public class VectorAssignmentViewer {
 protected PApplet parent;
 protected VectorAssignment assignment;
 protected HashMap<PVector,Float> magnitudeLookUpTable;
 protected HashMap<PVector,PVector> arrowLookUpTable;
 protected int dimension;
 protected char type;
 protected float[] modelScale;
 protected float arrowLength;
 protected boolean fixedLength;
 protected int strokeWeight;
 protected int color0;
 protected int color1;
 public VectorAssignmentViewer(){
  this.parent = null;
  this.assignment = null;
  this.magnitudeLookUpTable = null;
 
 }
 public VectorAssignmentViewer(PApplet parent){
  this.parent = parent;
  this.assignment = null;
  this.magnitudeLookUpTable = null;
  this.arrowLookUpTable = null;
 }
 public VectorAssignmentViewer(PApplet parent, VectorAssignment field){
  this.parent = parent;
  this.assignment = field;
  this.dimension = field.getDimension();
  this.type = field.getType();
  this.fixedLength = true;
  this.strokeWeight = 1;
  this.magnitudeLookUpTable = new HashMap<PVector,Float>();
  this.arrowLookUpTable = new HashMap<PVector,PVector>();
 }
 public VectorAssignmentViewer(PApplet parent, VectorAssignment field, boolean fixedLength){
  this.parent = parent;
  this.assignment = field;
  this.dimension = field.getDimension();
  this.type = field.getType();
  this.fixedLength = fixedLength;
  this.strokeWeight = 1;
  this.magnitudeLookUpTable = new HashMap<PVector,Float>();
  this.arrowLookUpTable = new HashMap<PVector,PVector>();
 }
 public void setPlotType(int dimension, char type){
  this.dimension = dimension;
  this.type = type;
 }
 public void setScale(float[] scale){
  this.modelScale = scale;
 }
 public void setColors(int color0, int color1){
  this.color0 = color0;
  this.color1 = color1;
 }
 public void setStrokeWeight(int strokeWeight){
  this.strokeWeight = strokeWeight;
 }
 public void setArrowLength(int arrowLength){
  this.arrowLength = arrowLength;
 }
 public int assignObjectColor(PVector position){
  float u = magnitudeLookUpTable.get(position).floatValue();
  return parent.lerpColor(color0, color1, u);
 }
 public void createLookUpTable(DEC_Complex complex) throws DEC_Exception{
  magnitudeLookUpTable = new HashMap<PVector,Float>();
  arrowLookUpTable = new HashMap<PVector,PVector>();
  DEC_Iterator iterator = complex.createIterator(dimension,type);
  double minValue = 1000000;
  double maxValue = -1000000; 
  while(iterator.hasNext()){
   DEC_Object object = iterator.next();
   double fMagnitude = assignment.getMagnitudes().get(object);
   minValue = Math.min(fMagnitude, minValue);
   maxValue = Math.max(fMagnitude,maxValue);
  }
  DEC_Iterator lookUpIterator = complex.createIterator(dimension,type);
  while(lookUpIterator.hasNext()){
   DEC_Object object = lookUpIterator.next();
   double fMagnitude = assignment.getMagnitudes().get(object);
   PVector fDirection = assignment.getDirections().get(object);
   fDirection.mult((float) fMagnitude);
   Double normalizedValue = new Double(1);
   if(maxValue!=minValue){
    normalizedValue = normalizedValue = new Double((fMagnitude-minValue)/(maxValue-minValue));
   }
   magnitudeLookUpTable.put(object.getVectorContent("CENTER"), normalizedValue.floatValue());
   arrowLookUpTable.put(object.getVectorContent("CENTER"), fDirection);
  }
 }
 public void plotArrow(PVector arrowStart, PVector arrow){
  PVector c = new PVector(arrowStart.x*modelScale[0],
                          arrowStart.y*modelScale[1],
                          arrowStart.z*modelScale[2]);
  parent.strokeWeight(strokeWeight);
  parent.line(c.x,c.y,c.z,c.x+arrow.x,c.y+arrow.y,c.z+arrow.z);
 }
 public void plot(DEC_Complex complex, DEC_GeometricContainer container) throws DEC_Exception{
  DEC_Iterator iterator = complex.createIterator(dimension, type);
  while(iterator.hasNext()){
   DEC_Object object = iterator.next();
   PVector center = object.getVectorContent("CENTER");
   PVector arrow = arrowLookUpTable.get(center);
   int arrowColor = assignObjectColor(center);
   parent.stroke(arrowColor);
   if(fixedLength){
    arrow.normalize();
    arrow.mult(arrowLength);
   }else{
    arrow.limit(arrowLength);
   }
   plotArrow(center,arrow);
  }
 }
}
