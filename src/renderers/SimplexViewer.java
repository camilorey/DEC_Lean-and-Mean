/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderers;

import complex.DEC_DualObject;
import complex.DEC_Object;
import complex.DEC_PrimalObject;
import containers.DEC_GeometricContainer;
import exceptions.DEC_Exception;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 *
 * @author Camilo Rey Torres - Leonardo Florez MISyC-2016. Pontificia Universidad Javeriana. Bogotá Colombia. 
 */
public class SimplexViewer {
 protected PApplet parent;
 protected DEC_Object simplex;
 protected ArrayList<PVector> simplexVertices;
 protected float[] modelScale;
 public SimplexViewer(){
  this.parent = null;
  this.simplex = null;
 }
 public SimplexViewer(PApplet parent){
  this.parent = parent;
  this.simplex = null;
 }
 public SimplexViewer(PApplet parent, DEC_Object simplex){
  this.parent = parent;
  this.simplex = simplex;
 }
 public void setSimplex(DEC_Object simplex){
  this.simplex = simplex;
 }

 public ArrayList<PVector> getSimplexVertices() {
  return simplexVertices;
 }

 public void setSimplexVertices(ArrayList<PVector> simplexVertices) {
  this.simplexVertices = simplexVertices;
 }
 
 public void getGeometry(DEC_GeometricContainer container) throws DEC_Exception{
  this.simplexVertices = container.getGeometricContent(simplex);
 }
 public void setScale(float[] scale){
  this.modelScale = scale;
 }
 public void plotVertex(float vertexSize){
  parent.noStroke();
  parent.pushMatrix();
   parent.translate(simplexVertices.get(0).x*modelScale[0],
                    simplexVertices.get(0).y*modelScale[1],
                    simplexVertices.get(0).z*modelScale[2]);
   parent.box(vertexSize);
  parent.popMatrix();
 }
 public void plotPrimalEdge(){
   PVector p0 = new PVector(simplexVertices.get(0).x*modelScale[0],
                            simplexVertices.get(0).y*modelScale[1],
                            simplexVertices.get(0).z*modelScale[2]);
   PVector p1 = new PVector(simplexVertices.get(1).x*modelScale[0],
                            simplexVertices.get(1).y*modelScale[1],
                            simplexVertices.get(1).z*modelScale[2]);
   parent.line(p0.x,p0.y,p0.z, p1.x,p1.y,p1.z);
 }
 public void plotDualEdge() throws DEC_Exception{
   PVector p0 = new PVector(simplexVertices.get(0).x*modelScale[0],
                            simplexVertices.get(0).y*modelScale[1],
                            simplexVertices.get(0).z*modelScale[2]);
   PVector p1 = new PVector(simplexVertices.get(1).x*modelScale[0],
                            simplexVertices.get(1).y*modelScale[1],
                            simplexVertices.get(1).z*modelScale[2]);
   PVector c = new PVector(simplex.getVectorContent("CENTER").x*modelScale[0],
                           simplex.getVectorContent("CENTER").y*modelScale[1],
                           simplex.getVectorContent("CENTER").z*modelScale[2]);
   if(simplex.isBorder()){
    parent.line(c.x,c.y,c.z,p0.x,p0.y,p0.z);
   }else{
    parent.line(c.x,c.y,c.z,p0.x,p0.y,p0.z);
    parent.line(c.x,c.y,c.z,p1.x,p1.y,p1.z);
   }
 }
 public void plotPrimalFace(){
  PVector p0 = new PVector(simplexVertices.get(0).x*modelScale[0],
                            simplexVertices.get(0).y*modelScale[1],
                            simplexVertices.get(0).z*modelScale[2]);
  PVector p1 = new PVector(simplexVertices.get(1).x*modelScale[0],
                            simplexVertices.get(1).y*modelScale[1],
                            simplexVertices.get(1).z*modelScale[2]);
  PVector p2 = new PVector(simplexVertices.get(2).x*modelScale[0],
                            simplexVertices.get(2).y*modelScale[1],
                            simplexVertices.get(2).z*modelScale[2]); 
  parent.noStroke();
  parent.beginShape();
   parent.vertex(p0.x,p0.y,p0.z);
   parent.vertex(p1.x,p1.y,p1.z);
   parent.vertex(p2.x,p2.y,p2.z);
  parent.endShape(PApplet.CLOSE);
 }
 public void plotPrimalFace(boolean withNormals) throws DEC_Exception{
  if(!withNormals){
   plotPrimalFace();
  }else{
   PVector p0 = new PVector(simplexVertices.get(0).x*modelScale[0],
                            simplexVertices.get(0).y*modelScale[1],
                            simplexVertices.get(0).z*modelScale[2]);
   PVector p1 = new PVector(simplexVertices.get(1).x*modelScale[0],
                            simplexVertices.get(1).y*modelScale[1],
                            simplexVertices.get(1).z*modelScale[2]);
   PVector p2 = new PVector(simplexVertices.get(2).x*modelScale[0],
                            simplexVertices.get(2).y*modelScale[1],
                            simplexVertices.get(2).z*modelScale[2]);
   PVector n0 = simplex.getVectorContent("NORMAL_1");
   PVector n1 = simplex.getVectorContent("NORMAL_2");
   PVector n2 = simplex.getVectorContent("NORMAL_3");
   parent.noStroke();
   parent.beginShape();
   parent.normal(n0.x,n0.y,n0.z);parent.vertex(p0.x,p0.y,p0.z);
   parent.normal(n1.x,n1.y,n1.z);parent.vertex(p1.x,p1.y,p1.z);
   parent.normal(n2.x,n2.y,n2.z);parent.vertex(p2.x,p2.y,p2.z);
  parent.endShape(PApplet.CLOSE);
  }
 }
 public void plotPrimalFace(PImage texture) throws DEC_Exception{
  PVector p0 = new PVector(simplexVertices.get(0).x*modelScale[0],
                            simplexVertices.get(0).y*modelScale[1],
                            simplexVertices.get(0).z*modelScale[2]);
   PVector p1 = new PVector(simplexVertices.get(1).x*modelScale[0],
                            simplexVertices.get(1).y*modelScale[1],
                            simplexVertices.get(1).z*modelScale[2]);
   PVector p2 = new PVector(simplexVertices.get(1).x*modelScale[0],
                            simplexVertices.get(1).y*modelScale[1],
                            simplexVertices.get(1).z*modelScale[2]);
   PVector t0 = simplex.getVectorContent("UV_0");
   PVector t1 = simplex.getVectorContent("UV_1");
   PVector t2 = simplex.getVectorContent("UV_2");
   parent.noStroke();
   parent.beginShape();
   parent.texture(texture);
   parent.vertex(p0.x,p0.y,p0.z,t0.x,t0.y);
   parent.vertex(p1.x,p1.y,p1.z,t1.x,t1.y);
   parent.vertex(p2.x,p2.y,p2.z,t2.x,t2.y);
  parent.endShape(PApplet.CLOSE);
 }
 
 public void plotDualFace() throws DEC_Exception{
  ArrayList<PVector> realVerts = ((DEC_DualObject) simplex).getExtraGeometricContent();
  PVector c = new PVector(simplex.getVectorContent("CENTER").x*modelScale[0],
                          simplex.getVectorContent("CENTER").y*modelScale[1],
                          simplex.getVectorContent("CENTER").z*modelScale[2]);
  if(realVerts != null){
   for(int i=1;i<realVerts.size();i++){
    PVector p1 = new PVector(realVerts.get(i).x*modelScale[0],
                             realVerts.get(i).y*modelScale[1],
                             realVerts.get(i).z*modelScale[2]);
    PVector p2 = null;
    if(i<realVerts.size()-1){
     p2 = new PVector(realVerts.get(i+1).x*modelScale[0],
                      realVerts.get(i+1).y*modelScale[1],
                      realVerts.get(i+1).z*modelScale[2]);
    }else{
     p2 = new PVector(realVerts.get(1).x*modelScale[0],
                      realVerts.get(1).y*modelScale[1],
                      realVerts.get(1).z*modelScale[2]);
    }
    parent.strokeWeight(4);
    parent.line(p1.x,p1.y,p1.z,p2.x,p2.y,p2.z);
    parent.strokeWeight(1);
    parent.line(c.x,c.y,c.z,p1.x,p1.y,p1.z);
    parent.line(c.x,c.y,c.z,p2.x,p2.y,p2.z);
   }
  }
 }
}
