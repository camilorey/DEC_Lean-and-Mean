/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex;

import containers.DEC_GeometricContainer;
import exceptions.DEC_Exception;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import utils.IndexSet;
import java.util.ArrayList;
import processing.core.PVector;
import utils.GeometricUtils;

/**
 *
 * @author laptop
 */
public class DEC_DualObject extends DEC_Object {

 protected int dimension;
 protected ArrayList<PVector> extraGeometricContent;
 int numExtraNormals;

 public DEC_DualObject() {
  super();
 }

 public DEC_DualObject(int index) {
  super(index);
 }

 public DEC_DualObject(IndexSet vertices) throws DEC_Exception {
  super(vertices);
  numExtraNormals = vertices.size();
 }

 public DEC_DualObject(IndexSet vertices, int index) throws DEC_Exception {
  super(vertices, index);
  numExtraNormals = vertices.size();
 }

 public DEC_DualObject(IndexSet vertices, int index, int orientation) throws DEC_Exception {
  super(vertices, index, orientation);
  numExtraNormals = vertices.size();
 }

 public DEC_DualObject(DEC_Object object) throws DEC_Exception {
  super(object.getVertices(), object.getIndex(), object.getOrientation());
  numExtraNormals = vertices.size();
  scalarContent = object.scalarContent;
  vectorContent = object.vectorContent;
  if (object instanceof DEC_DualObject) {
   extraGeometricContent = ((DEC_DualObject) object).extraGeometricContent;
  }
  scalarContent_2 = object.scalarContent_2;
  vectorContent_2 = object.vectorContent_2;
 }

 public DEC_DualObject(char dimension) {
  super();
  this.dimension = assignDimension(dimension);
 }

 public DEC_DualObject(int index, char dimension) {
  super(index);
  this.dimension = assignDimension(dimension);
 }

 public DEC_DualObject(IndexSet vertices, char dimension) throws DEC_Exception {
  super(vertices);
  numExtraNormals = vertices.size();
  this.dimension = assignDimension(dimension);
 }

 public DEC_DualObject(IndexSet vertices, int index, char dimension) throws DEC_Exception {
  super(vertices, index);
  numExtraNormals = vertices.size();
  this.dimension = assignDimension(dimension);
 }

 public DEC_DualObject(IndexSet vertices, int index, int orientation, char dimension) throws DEC_Exception {
  super(vertices, index, orientation);
  numExtraNormals = vertices.size();
  this.dimension = assignDimension(dimension);
 }

 public DEC_DualObject(DEC_Object object, char dimension) throws DEC_Exception {
  super(object.getVertices(), object.getIndex(), object.getOrientation(), object.scalarContent, object.vectorContent);
  numExtraNormals = object.getVertices().size();
  this.dimension = assignDimension(dimension);
  this.isBorder = object.isBorder();
  scalarContent = object.scalarContent;
  vectorContent = object.vectorContent;
  if (object instanceof DEC_DualObject) {
   extraGeometricContent = ((DEC_DualObject) object).extraGeometricContent;
  }
  scalarContent_2 = object.scalarContent_2;
  vectorContent_2 = object.vectorContent_2;
 }

 @Override
 public int dimension() {
  return this.dimension;
 }

 public int assignDimension(char d) {
  int dim = 0;
  switch (d) {
   case 'v':
    dim = 0;
    break;
   case 'e':
    dim = 1;
    break;
   case 'f':
    dim = 2;
    break;
   default:
    dim = 3;
    break;
  }
  return dim;
 }

 @Override
 public float volume(DEC_GeometricContainer container) throws DEC_Exception {
  if(dimension == 0){
   return 1;
  }
  else if (dimension == 1) {
   PVector center = getVectorContent("CENTER");
   ArrayList<PVector> verts = getGeometry(container);
   if(isBorder()){
    return center.dist(verts.get(0));
   }else{
    return center.dist(verts.get(0))+center.dist(verts.get(1));
   }
  } else if (dimension == 2) {
   return GeometricUtils.surfaceArea(extraGeometricContent);
  } else if (dimension == 3) {
   return GeometricUtils.cellVolume(extraGeometricContent);
  } else {
   return 0;
  }
 }

 public ArrayList<PVector> getGeometry(DEC_GeometricContainer geometricContainer) throws DEC_Exception {
  return geometricContainer.getGeometricContent(this);
 }

 @Override
 public ArrayList<DEC_Object> boundary() throws DEC_Exception {
  ArrayList<DEC_Object> bounds = new ArrayList<DEC_Object>();
  if (dimension() == 0) {
   return bounds;
  } else if (dimension() == 1) {
   if (getOrientation() > 0) {
    bounds.add(new DEC_Object(new IndexSet(vertices.getIndex(0)), -1, 1));
    bounds.add(new DEC_Object(new IndexSet(vertices.getIndex(1)), -1, -1));
   } else {
    bounds.add(new DEC_Object(new IndexSet(vertices.getIndex(1)), -1, 1));
    bounds.add(new DEC_Object(new IndexSet(vertices.getIndex(0)), -1, -1));
   }
  } else if (dimension() == 2) {
   for (int i = 0; i < vertices.size() - 1; i++) {
    int i0 = vertices.getIndex(i);
    int i1 = vertices.getIndex(i + 1);
    IndexSet boundVerts = new IndexSet(new int[]{i0, i1});
    bounds.add(new DEC_Object(boundVerts, -1, 1));
   }
   int i0 = vertices.getIndex(0);
   int i1 = vertices.getIndex(vertices.size() - 1);
   bounds.add(new DEC_Object(new IndexSet(new int[]{i0, i1}), -1, -1));
  }
  return bounds;
 }

 public ArrayList<PVector> getExtraGeometricContent() {
  return extraGeometricContent;
 }
 public void createExtraGeometricContent(ArrayList<PVector> fCenters, ArrayList<PVector> eCenters) throws DEC_Exception{
  ArrayList<PVector> realVertices = new ArrayList<PVector>();
  for(int i=0;i<fCenters.size();i++){
   realVertices.add(fCenters.get(i));
  }
  for(int i=0;i<eCenters.size();i++){
   realVertices.add(eCenters.get(i));
  }
  this.extraGeometricContent = GeometricUtils.sortPoints(realVertices, getVectorContent("NORMAL_0"), getVectorContent("CENTER"));
  //this.extraGeometricContent = realVertices;
 }
 public void setExtraGeometricContent(ArrayList<PVector> extraGeometricContent) {
  this.extraGeometricContent = extraGeometricContent;
 }

 public int getNumExtraNormals() {
  return numExtraNormals;
 }

 public void setNumExtraNormals(int numExtraNormals) {
  this.numExtraNormals = numExtraNormals;
 }
 
}
