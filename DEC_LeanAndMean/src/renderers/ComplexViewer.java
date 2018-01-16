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
import fieldsAndForms.DiscreteDifferentialForm;
import fieldsAndForms.ScalarFunction;
import fieldsAndForms.VectorAssignment;
import fieldsAndForms.ContinuousVectorField;
import java.util.ArrayList;
import java.util.HashMap;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import saito.objloader.BoundingBox;
import utils.GeometricUtils;

/**
 *
 * @author Camilo Rey
 */
public class ComplexViewer {
 protected PApplet parent;
 protected float[] modelWHD;
 protected int[] fillColor;
 protected int[] strokeColor;
 protected int strokeWeight;
 protected float primalVertexSize;
 protected float dualVertexSize;
 
 public ComplexViewer(){
  this.parent = null;
  this.modelWHD = null;
  this.fillColor = null;
  this.strokeColor = null;
  this.strokeWeight = 1;
  this.primalVertexSize = 5;
  this.dualVertexSize = 3;
 }
 public ComplexViewer(PApplet parent){
  this.parent = parent;
  this.modelWHD = new float[]{parent.width/2,parent.height/2,parent.height/2};
  this.fillColor = new int[]{150,150,150,0};
  this.strokeColor = new int[]{0,0,0};
  this.strokeWeight = 1;
  this.primalVertexSize = 5;
  this.dualVertexSize = 3;
 }
 public void fill(int r, int g, int b){
  this.fillColor = new int[]{r,g,b,0};
  parent.fill(r,g,b);
 }
 public void stroke(int r, int g, int b){
  this.strokeColor = new int[]{r,g,b};
  parent.stroke(r,g,b);
 }
 public void strokeWeight(int strokeWeight){
  strokeWeight = strokeWeight;
  parent.strokeWeight(strokeWeight);
 }
 public void setModelSize(float w, float h, float d){
  this.modelWHD = new float[]{w/2,h/2,d/2};
 }
 public void setModelScale(float scale, BoundingBox bbox){
  this.modelWHD = new float[]{bbox.getWHD().x*scale,bbox.getWHD().y*scale,bbox.getWHD().z*scale};
 }
 public float[] getModelWHD(){
  return modelWHD;
 }
 public void setPrimalVertexSize(float primalVertexSize) {
  this.primalVertexSize = primalVertexSize;
 }
 public void setDualVertexSize(float dualVertexSize) {
  this.dualVertexSize = dualVertexSize;
 }
 public float getPrimalVertexSize() {
  return primalVertexSize;
 }
 public float getDualVertexSize() {
  return dualVertexSize;
 }
 public PVector scalePVector(PVector p){
  return new PVector(p.x*modelWHD[0],p.y*modelWHD[1],p.z*modelWHD[2]);
 }
 public void plotBoundingBox(){
  parent.noFill();
  parent.stroke(0);
  parent.box(2*modelWHD[0],2*modelWHD[1],2*modelWHD[2]);
 }
 public void plotComplex(DEC_Complex complex, DEC_GeometricContainer container, int dimension, char type){
  DEC_Iterator iter = complex.createIterator(dimension, type);
  while(iter.hasNext()){
   try{
    if(type == 'p'){
     DEC_PrimalObject op = (DEC_PrimalObject) iter.next();
     SimplexViewer viewer = new SimplexViewer(parent, op);
     viewer.setScale(modelWHD);
     viewer.getGeometry(container);
     if(dimension == 0){
      viewer.plotVertex(primalVertexSize);
     }else if(dimension == 1){
      viewer.plotPrimalEdge();
     }else if(dimension==2){
      viewer.plotPrimalFace(true);
     }
    }else{
     DEC_DualObject od = (DEC_DualObject) iter.next();
     SimplexViewer viewer = new SimplexViewer(parent, od);
     viewer.setScale(modelWHD);
     if(dimension == 0){
      viewer.getGeometry(container);
      viewer.plotVertex(dualVertexSize);
     }else if(dimension == 1){
      viewer.getGeometry(container);
      viewer.plotDualEdge();
     }else if(dimension == 2){
      viewer.plotDualFace();
     }
    }
   }catch(DEC_Exception ex){
     System.out.println("something went wrong plotting complex");
     ex.printStackTrace();
   }
  }
 }
 public void plotComplex(DEC_Complex complex,DEC_GeometricContainer container,int dimension, char type, PImage textureImage){
  DEC_Iterator iter = complex.createIterator(dimension, type);
  while(iter.hasNext()){
   try{
    if(type == 'p'){
     DEC_PrimalObject op = (DEC_PrimalObject) iter.next();
     SimplexViewer viewer = new SimplexViewer(parent, op);
     viewer.setScale(modelWHD);
     viewer.getGeometry(container);
     if(dimension == 0){
      viewer.plotVertex(primalVertexSize);
     }else if(dimension == 1){
      viewer.plotPrimalEdge();
     }else if(dimension==2){
      viewer.plotPrimalFace(textureImage);
     }
    }else{
     DEC_DualObject od = (DEC_DualObject) iter.next();
     SimplexViewer viewer = new SimplexViewer(parent, od);
     viewer.setScale(modelWHD);
     if(dimension == 0){
      viewer.getGeometry(container);
      viewer.plotVertex(dualVertexSize);
     }else if(dimension == 1){
      viewer.getGeometry(container);
      viewer.plotDualEdge();
     }else if(dimension == 2){
      viewer.plotDualFace();
     }
    }
   }catch(DEC_Exception ex){
     System.out.println("something went wrong plotting complex with texture");
     ex.printStackTrace();
   }
  }
 }
 
}
