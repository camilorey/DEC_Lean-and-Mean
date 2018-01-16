/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package containers;

//
import complex.DEC_DualObject;
import complex.DEC_Object;
import complex.DEC_PrimalObject;
import utils.IndexSet;
import exceptions.DEC_Exception;
import java.util.ArrayList;
import java.util.HashMap;
import processing.core.PVector;
import readers.OBJMeshReader;
import utils.GeometricUtils;

//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : Untitled
//  @ File Name : DEC_GeometricContainer.java
//  @ Date : 14/02/2016
//  @ Author :
//
//
public class DEC_GeometricContainer {

 protected ArrayList<PVector> primalVertices;
 protected ArrayList<PVector> dualVertices;
 protected HashMap<PVector, PVector> vertexNormals;
 protected HashMap<PVector, PVector> vertexTexels;
 protected HashMap<PVector, PVector> faceNormals;
 protected ArrayList<IndexSet> cellInformation;

 public DEC_GeometricContainer() {
  primalVertices = new ArrayList<PVector>();
  dualVertices = new ArrayList<PVector>();
  vertexNormals = new HashMap<PVector, PVector>();
  vertexTexels = null;
  faceNormals = new HashMap<PVector, PVector>();
  cellInformation = new ArrayList<IndexSet>();
 }

 public void setContent(OBJMeshReader reader) {
  primalVertices = reader.getModelVertices();
  dualVertices = reader.getModelDualVertices();
  faceNormals = reader.getModelFaceNormals();
  vertexNormals = reader.getVertexNormals();
  if (reader.withTexture()) {
   vertexTexels = reader.getVertexTexels();
  } else {
   vertexTexels = null;
  }
  cellInformation = reader.getModelFaceIndices();
 }

 public ArrayList<IndexSet> getModelFaceIndexInformation() {
  return cellInformation;
 }

 public ArrayList<PVector> getPrimalVerticesInformation() {
  return primalVertices;
 }
 public boolean hasTexture(){
  return vertexTexels != null;
 }
 public ArrayList<PVector> getObjectVectorContent(DEC_Object object) throws DEC_Exception {
  if (object instanceof DEC_PrimalObject) {
   return getPrimalObjectVectorContent((DEC_PrimalObject) object);
  } else if (object instanceof DEC_DualObject) {
   return getDualObjectVectorContent((DEC_DualObject) object);
  } else {
   return new ArrayList<PVector>();
  }
 }
 public HashMap<String,PVector> createObjectVectorContent(DEC_Object object) throws DEC_Exception {
  if (object instanceof DEC_PrimalObject) {
   return createPrimalObjectVectorContent((DEC_PrimalObject) object);
  } else if (object instanceof DEC_DualObject) {
   return createDualObjectVectorContent((DEC_DualObject) object);
  } else {
   return new HashMap<String,PVector>();
  }
 }
 public int calculateObjectOrientation(DEC_PrimalObject object) throws DEC_Exception {
  if (object.dimension() != 2) {
   return 1;
  } else {
   ArrayList<PVector> verts = getFromPrimalVertices(object.getVertices());
   PVector w0 = PVector.sub(verts.get(1), verts.get(0));
   PVector w1 = PVector.sub(verts.get(2), verts.get(0));
   PVector N = w0.cross(w1);
   PVector objectNormal = object.getVectorContent("NORMAL_0");
   return GeometricUtils.signBetweenVectors(N, objectNormal);
  }
 }

 public ArrayList<PVector> getDualObjectVectorContent(DEC_DualObject object) throws DEC_Exception {
  ArrayList<PVector> vectorInfo = new ArrayList<PVector>();
  if (object.dimension() == 0) {
   int dualIndex = object.getVertices().getIndex(0);
   PVector vertCenter = dualVertices.get(dualIndex);
   PVector vertNormal = faceNormals.get(vertCenter);
   vectorInfo.add(vertCenter);
   vectorInfo.add(vertNormal);
  }else if (object.dimension() == 1) {
    int i0 = object.getVertices().getIndex(0);
    int i1 = object.getVertices().getIndex(1);
    PVector v0 = dualVertices.get(i0);
    PVector v1 = dualVertices.get(i1);
    PVector n0 = faceNormals.get(v0);
    PVector n1 = faceNormals.get(v1);
    vectorInfo.add(n0);
    vectorInfo.add(n1);
  }else if (object.dimension() == 2) {
   PVector faceCenter = primalVertices.get(object.getIndex());
   PVector faceNormal = vertexNormals.get(faceCenter);
   vectorInfo.add(faceCenter);
   vectorInfo.add(faceNormal);
   for (int i = 0; i < object.getVertices().size(); i++) {
    PVector faceVert = dualVertices.get(object.getVertices().getIndex(i));
    PVector vertNormal = faceNormals.get(faceVert);
    vectorInfo.add(vertNormal);
   }
  }
  return vectorInfo;
 }
 public ArrayList<PVector> getPrimalObjectVectorContent(DEC_PrimalObject object) throws DEC_Exception {
  ArrayList<PVector> vectorInfo = new ArrayList<PVector>();
  if (object.dimension() == 0) {
   int vertIndex = object.getVertices().getIndex(0);
   PVector vertCenter = primalVertices.get(vertIndex);
   PVector vertNormal = vertexNormals.get(vertCenter);
   vectorInfo.add(vertCenter);
   vectorInfo.add(vertNormal);
   if(hasTexture()){
    PVector vertTexel = vertexTexels.get(vertCenter);
    vectorInfo.add(vertTexel);
   }
   return vectorInfo;
  } else if (object.dimension() == 1) {
   int i0 = object.getVertices().getIndex(0);
   int i1 = object.getVertices().getIndex(1);
   PVector v0 = primalVertices.get(i0);
   PVector v1 = primalVertices.get(i1);
   PVector edgeCenter = circumcenter(object);
   PVector n0 = vertexNormals.get(v0);
   PVector n1 = vertexNormals.get(v1);
   vectorInfo.add(edgeCenter);
   vectorInfo.add(n0);
   vectorInfo.add(n1);
   if(hasTexture()){
    PVector t0 = vertexTexels.get(v0);
    PVector t1 = vertexTexels.get(v1);
    vectorInfo.add(t0);
    vectorInfo.add(t1);
   }
   return vectorInfo;
  } else if (object.dimension() == 2) {
   PVector faceCenter = dualVertices.get(object.getIndex());
   PVector faceNormal = faceNormals.get(faceCenter);
   PVector[] faceNormals = new PVector[3];
   for (int i = 0; i < object.getVertices().size(); i++) {
    int faceVertIndex = object.getVertices().getIndex(i);
    PVector faceVert = primalVertices.get(faceVertIndex);
    faceNormals[i] = vertexNormals.get(faceVert);
   }
   vectorInfo.add(faceCenter); //vector content 0
   vectorInfo.add(faceNormal); //vector content 1
   //vector content 2, 3, 4
   for (int i = 0; i < faceNormals.length; i++) {
    vectorInfo.add(faceNormals[i]);
   }
   //vector content 5,6,7
   if(hasTexture()){
    for (int i = 0; i < object.getVertices().size(); i++) {
     int faceVertIndex = object.getVertices().getIndex(i);
     PVector faceVert = primalVertices.get(faceVertIndex);
     PVector faceTexel = vertexTexels.get(faceVert);
     vectorInfo.add(faceTexel);
    }
   }
   return vectorInfo;
  }
  return vectorInfo;
 }
 public HashMap<String,PVector> createDualObjectVectorContent(DEC_DualObject object) throws DEC_Exception {
  HashMap<String,PVector> vectorInfo = new HashMap<String,PVector>();
  if (object.dimension() == 0) {
   int dualIndex = object.getVertices().getIndex(0);
   PVector vertCenter = dualVertices.get(dualIndex);
   PVector vertNormal = faceNormals.get(vertCenter);
   vectorInfo.put("CENTER",vertCenter);
   vectorInfo.put("NORMAL_0",vertNormal);
  }else if (object.dimension() == 1) {
    int i0 = object.getVertices().getIndex(0);
    int i1 = object.getVertices().getIndex(1);
    PVector v0 = dualVertices.get(i0);
    PVector v1 = dualVertices.get(i1);
    PVector n0 = faceNormals.get(v0);
    PVector n1 = faceNormals.get(v1);
    vectorInfo.put("NORMAL_0",n0);
    vectorInfo.put("NORMAL_1",n1);
  }else if (object.dimension() == 2) {
   PVector faceCenter = primalVertices.get(object.getIndex());
   PVector faceNormal = vertexNormals.get(faceCenter);
   vectorInfo.put("CENTER",faceCenter);
   vectorInfo.put("NORMAL_0",faceNormal);
   for (int i = 0; i < object.getVertices().size(); i++) {
    PVector faceVert = dualVertices.get(object.getVertices().getIndex(i));
    PVector vertNormal = faceNormals.get(faceVert);
    vectorInfo.put("NORMAL_"+(i+1),vertNormal);
   }
  }
  return vectorInfo;
 }
 public HashMap<String,PVector> createPrimalObjectVectorContent(DEC_PrimalObject object) throws DEC_Exception {
  HashMap<String,PVector> vectorInfo = new HashMap<String,PVector>();
  if (object.dimension() == 0) {
   int vertIndex = object.getVertices().getIndex(0);
   PVector vertCenter = primalVertices.get(vertIndex);
   PVector vertNormal = vertexNormals.get(vertCenter);
   vectorInfo.put("CENTER",vertCenter);
   vectorInfo.put("NORMAL_0",vertNormal);
   if(hasTexture()){
    PVector vertTexel = vertexTexels.get(vertCenter);
    vectorInfo.put("UV_0",vertTexel);
   }
  }else if (object.dimension() == 1) {
    int i0 = object.getVertices().getIndex(0);
    int i1 = object.getVertices().getIndex(1);
    PVector v0 = primalVertices.get(i0);
    PVector v1 = primalVertices.get(i1);
    PVector edgeCenter = circumcenter(object);
    PVector n0 = vertexNormals.get(v0);
    PVector n1 = vertexNormals.get(v1);
    vectorInfo.put("CENTER",edgeCenter);
    vectorInfo.put("NORMAL_0",n0);
    vectorInfo.put("NORMAL_1",n1);
    if(hasTexture()){
     PVector t0 = vertexTexels.get(v0);
     PVector t1 = vertexTexels.get(v1);
     vectorInfo.put("UV_0",t0);
     vectorInfo.put("UV_1",t1);
    }
  } else if (object.dimension() == 2) {
    PVector faceCenter = dualVertices.get(object.getIndex());
    PVector faceNormal = faceNormals.get(faceCenter);
    PVector[] faceNormals = new PVector[3];
    for (int i = 0; i < object.getVertices().size(); i++) {
     int faceVertIndex = object.getVertices().getIndex(i);
     PVector faceVert = primalVertices.get(faceVertIndex);
     faceNormals[i] = vertexNormals.get(faceVert);
    }
    vectorInfo.put("CENTER",faceCenter); 
    vectorInfo.put("NORMAL_0",faceNormal);
    for (int i = 0; i < faceNormals.length; i++) {
     vectorInfo.put("NORMAL_"+(i+1),faceNormals[i]);
    }
    if(hasTexture()){
     for (int i = 0; i < object.getVertices().size(); i++) {
      int faceVertIndex = object.getVertices().getIndex(i);
      PVector faceVert = primalVertices.get(faceVertIndex);
      PVector faceTexel = vertexTexels.get(faceVert);
      vectorInfo.put("UV_"+i,faceTexel);
     }
    }
  }
  return vectorInfo;
 }
 public ArrayList<PVector> getGeometricContent(DEC_Object object) throws DEC_Exception {
  if (object instanceof DEC_PrimalObject) {
   return getFromPrimalVertices(object.getVertices());
  } else if (object instanceof DEC_DualObject) {
    return getFromDualVertices(object.getVertices());
  } else {
   throw new DEC_Exception("undefined geometric settings");
  }
 }

 public ArrayList<PVector> getFromPrimalVertices(IndexSet set) throws DEC_Exception {
  ArrayList<PVector> verts = new ArrayList<PVector>();
  for (int i = 0; i < set.size(); i++) {
   verts.add(primalVertices.get(set.getIndex(i)));
  }
  return verts;
 }
 public ArrayList<PVector> getFromDualVertices(IndexSet set) throws DEC_Exception {
  ArrayList<PVector> verts = new ArrayList<PVector>();
  for (int i = 0; i < set.size(); i++) {
   if (set.getIndex(i) < dualVertices.size()) {
    verts.add(dualVertices.get(set.getIndex(i)));
   }
  }
  return verts;
 }
 public float volume(DEC_Object object) throws DEC_Exception{
  if(object instanceof DEC_PrimalObject){
   return primalVolume(object);
  }else if(object instanceof DEC_DualObject){
   return dualVolume(object);
  }else{
   return 0;
  }
 }
 public float primalVolume(DEC_Object object) throws DEC_Exception{
  if(object.dimension()==0){
   return 1;
  }else{
   ArrayList<PVector> verts = getFromPrimalVertices(object.getVertices());
   if(object.dimension()==1){
    return verts.get(0).dist(verts.get(1));
   }else if(object.dimension()==2){
    return Math.abs(GeometricUtils.areaBetweenVectors(verts.get(0), object.getVectorContent(0), verts.get(1), verts.get(2)));
   }else{
    return 1;
   }
  }
 }
 public float dualVolume(DEC_Object object) throws DEC_Exception{
  if(object.dimension()==0){
   return 1;
  }else{
   if(object.dimension()==1){
    if(object.isBorder()){
     PVector v0 = object.getVectorContent(0);
     PVector v1 = dualVertices.get(object.getVertices().getIndex(0));
     return v0.dist(v1);
    }else{
     ArrayList<PVector> verts = getFromDualVertices(object.getVertices());
     PVector c = object.getVectorContent(0);
     return c.dist(verts.get(0))+c.dist(verts.get(1));
    }
   }else if(object.dimension()==2){
    ArrayList<PVector> faceVerts = getFromDualVertices(object.getVertices());
    if(((DEC_DualObject)object).getExtraGeometricContent()!=null){
     ArrayList<PVector> extraContent = ((DEC_DualObject)object).getExtraGeometricContent();
     for(int i=0;i<extraContent.size();i++){
      faceVerts.add(extraContent.get(i));
     }   
    }
    ArrayList<PVector> sortedVerts = GeometricUtils.sortPoints(faceVerts, object.getVectorContent(1), object.getVectorContent(0));
    float dualVol = 0;
    for(int i=0;i<sortedVerts.size();i++){
     PVector A  = sortedVerts.get(i);
     PVector B = sortedVerts.get((i+1)%sortedVerts.size());
     dualVol += Math.abs(GeometricUtils.areaBetweenVectors(object.getVectorContent(0), object.getVectorContent(1), A, B));
    }
    return dualVol;
   }else{
    return 1;
   }
  }
 }
 public PVector circumcenter(DEC_Object object) throws DEC_Exception {
  if (object instanceof DEC_PrimalObject) {
   return primalCircumcenter(object.getVertices());
  } else if (object instanceof DEC_DualObject) {
   return dualCircumcenter(object.getVertices());
  } else {
   return null;
  }
 }

 public PVector dualCircumcenter(IndexSet indices) throws DEC_Exception {
  PVector[] p = new PVector[indices.size()];
  for (int i = 0; i < indices.size(); i++) {
   p[i] = dualVertices.get(indices.getIndex(i));
  }
  return GeometricUtils.centroid(p);
 }
 
 public PVector primalCircumcenter(IndexSet indices) throws DEC_Exception {
  PVector[] p = new PVector[indices.size()];
  for (int i = 0; i < indices.size(); i++) {
   p[i] = primalVertices.get(indices.getIndex(i));
  }
  return GeometricUtils.centroid(p);
 }

 public void printContainerInfo() {
  System.out.println("geometric container info: ");
  System.out.println("number of primal vertices loaded: " + primalVertices.size());
  System.out.println("number of primal faces loaded: " + cellInformation.size());
  System.out.println("number of dual vertices loaded: " + dualVertices.size());
 }
}
