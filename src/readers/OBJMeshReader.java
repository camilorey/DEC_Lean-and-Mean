/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readers;

import utils.IndexSet;
import java.util.ArrayList;
import java.util.HashMap;
import processing.core.PApplet;
import processing.core.PVector;
import saito.objloader.BoundingBox;
import saito.objloader.Face;
import saito.objloader.OBJModel;
import saito.objloader.Segment;
import utils.GeometricUtils;

/**
 *
 * @author Camilo Rey Torres - Leonardo Florez MISyC-2016. Pontificia Universidad Javeriana. Bogotá Colombia. 
 */
public class OBJMeshReader {
 protected String fileName;
 protected OBJModel objModel;
 protected BoundingBox objBox;
 protected PApplet parent;
 protected float modelScale;
 protected boolean withTexture;
 protected ArrayList<PVector> modelVertices;
 protected ArrayList<PVector> modelDualVertices;
 protected ArrayList<IndexSet> faceIndices;
 protected HashMap<PVector,PVector> vertexNormals;
 protected HashMap<PVector,PVector> vertexTexels;
 protected HashMap<PVector,PVector> faceNormals;
 
 public OBJMeshReader(){
  this.fileName = "";
  this.parent = null;
  this.modelScale = 1.0f;
  this.objModel = null;
  modelVertices = new ArrayList<PVector>();
  modelDualVertices = new ArrayList<PVector>();
  vertexNormals = new HashMap<PVector,PVector>();
  vertexTexels = new HashMap<PVector,PVector>();
  faceNormals = new HashMap<PVector,PVector>();
  faceIndices = new ArrayList<IndexSet>();
 }
 public OBJMeshReader(String fileName){
  this.fileName = fileName;
  this.parent = null;
  this.modelScale = 1.0f;
  this.objModel = null;
  modelVertices = new ArrayList<PVector>();
  modelDualVertices = new ArrayList<PVector>();
  vertexNormals = new HashMap<PVector,PVector>();
  vertexTexels = new HashMap<PVector,PVector>();
  faceNormals = new HashMap<PVector,PVector>();
  faceIndices = new ArrayList<IndexSet>();
 }
 public OBJMeshReader(String fileName, PApplet parent){
  this.fileName = fileName;
  this.parent = parent;
  this.modelScale = 1.0f;
  this.objModel = null;
  modelVertices = new ArrayList<PVector>();
  modelDualVertices = new ArrayList<PVector>();
  vertexNormals = new HashMap<PVector,PVector>();
  vertexTexels = new HashMap<PVector,PVector>();
  faceNormals = new HashMap<PVector,PVector>();
  faceIndices = new ArrayList<IndexSet>();
 }
 public OBJMeshReader(String fileName, PApplet parent,float modelScale){
  this.fileName = fileName;
  this.parent = parent;
  this.modelScale = modelScale;
  this.objModel = null;
  modelVertices = new ArrayList<PVector>();
  modelDualVertices = new ArrayList<PVector>();
  vertexNormals = new HashMap<PVector,PVector>();
  vertexTexels = new HashMap<PVector,PVector>();
  faceNormals = new HashMap<PVector,PVector>();
  faceIndices = new ArrayList<IndexSet>();
 }
 public void loadModel(String centerType){
  objModel = new OBJModel(parent, fileName);
  objModel.translateToCenter();
  objBox = new BoundingBox(parent,objModel);
  setModelInformation(centerType);
  System.out.println("--------------OBJ Model info--------------------------");
  System.out.println("model vertices: "+modelVertices.size());
  System.out.println("model faces: "+modelDualVertices.size());
  System.out.println("vertex normals: "+vertexNormals.keySet().size());
  System.out.println("face normals: "+faceNormals.keySet().size());
  System.out.println("------------------------------------------------------");
 }
 public void loadModel(String centerType, boolean withTexture){
  objModel = new OBJModel(parent, fileName);
  objModel.translateToCenter();
  objBox = new BoundingBox(parent,objModel);
  this.withTexture = withTexture;
  setModelInformation(centerType);
  System.out.println("--------------OBJ Model info--------------------------");
  System.out.println("model vertices: "+modelVertices.size());
  System.out.println("model faces: "+modelDualVertices.size());
  System.out.println("vertex normals: "+vertexNormals.keySet().size());
  System.out.println("face normals: "+faceNormals.keySet().size());
  System.out.println("------------------------------------------------------");
 }
 public void setModelInformation(String centerType){
  modelVertices = new ArrayList<PVector>();
  modelDualVertices = new ArrayList<PVector>();
  faceNormals = new HashMap<PVector,PVector>();
  vertexNormals = new HashMap<PVector,PVector>();
  vertexTexels = new HashMap<PVector,PVector>();
  faceIndices = new ArrayList<IndexSet>();
  for(int i=0;i<objModel.getVertexCount();i++){
   modelVertices.add(normalize(objModel.getVertex(i)));
  }
  for(int i=0;i<objModel.getSegmentCount();i++){
   Segment seg = objModel.getSegment(i);
   Face[] f = seg.getFaces();
   for(int j=0;j<f.length;j++){
    PVector[] verts = f[j].getVertices();
    PVector[] normals = f[j].getNormals();
    int[] faceInds = f[j].getVertexIndices();
    PVector faceCenter = normalize(createCenter(centerType,verts));
    PVector faceNormal = f[j].getNormal();
    modelDualVertices.add(faceCenter);
    faceNormals.put(faceCenter, faceNormal);
    faceIndices.add(new IndexSet(faceInds));
    for(int k=0;k<verts.length;k++){
     vertexNormals.put(normalize(verts[k]), normals[k]);
    }
    if(withTexture){
     PVector[] texels = f[j].getUvs();
     for(int k=0;k<verts.length;k++){
     vertexTexels.put(normalize(verts[k]),texels[k]);
     }
    }
   }
  }
 }
 public boolean withTexture(){
  return this.withTexture;
 }
 public PVector createCenter(String centerType, PVector[] verts){
  PVector center = new PVector();
  if(centerType.equals("BARYCENTRIC")){
   center = GeometricUtils.barycenter(verts);
  }else if(centerType.equals("INCENTRIC")){
   center = GeometricUtils.incenter(verts);
  }else if(centerType.equals("CIRCUMCENTRIC")){
   center = GeometricUtils.circumcenter(verts);
  }else{
   center = GeometricUtils.centroid(verts);
  }
  return center;
 }
 public BoundingBox getModelBoundingBox(){
  return objBox;
 }
 public ArrayList<PVector> getModelVertices(){
  return modelVertices;
 }
 public ArrayList<PVector> getModelDualVertices(){
  return modelDualVertices;
 }
 public PVector getVertexNormal(PVector vert){
  return vertexNormals.get(vert);
 }
 public PVector getVertexTexel(PVector vert){
  return vertexTexels.get(vert);
 }
 public PVector getModelVertex(int numVertex){
  return modelVertices.get(numVertex);
 }
 public PVector getModelDualVertex(int numVertex){
  return modelDualVertices.get(numVertex);
 }
 public PVector getModelDualVertexNormal(PVector dualVert){
  return faceNormals.get(dualVert);
 }
 public IndexSet getFaceIndices(int numFace){
  return faceIndices.get(numFace);
 }
 public ArrayList<IndexSet> getModelFaceIndices(){
  return faceIndices;
 }
 public HashMap<PVector,PVector> getModelFaceNormals(){
  return faceNormals;
 }
 public HashMap<PVector,PVector> getVertexNormals(){
  return vertexNormals;
 }
 public HashMap<PVector,PVector> getVertexTexels(){
  return vertexTexels;
 }
 public PVector normalize(PVector v){
  float x = 2*v.x/objBox.getWHD().x;
  float y = 2*v.y/objBox.getWHD().y;
  float z = 2*v.z/objBox.getWHD().z;
  return new PVector(x,y,z);
 }
}
