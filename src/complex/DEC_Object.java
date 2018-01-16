/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex;

//
import containers.DEC_GeometricContainer;
import utils.IndexSet;
import exceptions.DEC_Exception;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import java.util.ArrayList;
import java.util.HashMap;
import processing.core.PVector;

/**
 *
 * @author Camilo Rey Torres - Leonardo Florez MISyC-2016. Pontificia Universidad Javeriana. Bogotá Colombia. 
 */
public class DEC_Object {

 protected IndexSet vertices;
 protected int index;
 protected int orientation;
 protected HashMap<String, Float> scalarContent_2;
 protected HashMap<String, PVector> vectorContent_2;
 protected ArrayList<Float> scalarContent;
 protected ArrayList<PVector> vectorContent;
 
 protected boolean isBorder;

 public DEC_Object() {
  this.vertices = null;
  this.index = -1;
  this.orientation = 1;
  this.scalarContent = new ArrayList<Float>();
  this.vectorContent = new ArrayList<PVector>();
  this.scalarContent_2 = new HashMap<String, Float>();
  this.vectorContent_2 = new HashMap<String, PVector>();
  isBorder = false;
 }

 public DEC_Object(int index) {
  this.vertices = null;
  this.index = index;
  this.orientation = 1;
  this.scalarContent = new ArrayList<Float>();
  this.vectorContent = new ArrayList<PVector>();
  this.scalarContent_2 = new HashMap<String, Float>();
  this.vectorContent_2 = new HashMap<String, PVector>();
  isBorder = false;
 }

 public DEC_Object(IndexSet vertices) throws DEC_Exception {
  this.vertices = vertices;
  this.index = -1;
  this.orientation = 1;
  this.scalarContent = new ArrayList<Float>();
  this.vectorContent = new ArrayList<PVector>();
  this.scalarContent_2 = new HashMap<String, Float>();
  this.vectorContent_2 = new HashMap<String, PVector>();
  isBorder = false;
 }

 public DEC_Object(IndexSet vertices, int index) throws DEC_Exception {
  this.vertices = vertices;
  this.index = index;
  this.orientation = 1;
  this.scalarContent = new ArrayList<Float>();
  this.vectorContent = new ArrayList<PVector>();
  this.scalarContent_2 = new HashMap<String, Float>();
  this.vectorContent_2 = new HashMap<String, PVector>();
  isBorder = false;
 }

 public DEC_Object(IndexSet vertices, int index, int orientation) throws DEC_Exception {
  this.vertices = vertices;
  this.index = index;
  this.orientation = orientation;
  this.scalarContent = new ArrayList<Float>();
  this.vectorContent = new ArrayList<PVector>();
  this.scalarContent_2 = new HashMap<String, Float>();
  this.vectorContent_2 = new HashMap<String, PVector>();
  isBorder = false;
 }

 public DEC_Object(IndexSet vertices, int index, int orientation, ArrayList<Float> scalarContent, ArrayList<PVector> vectorContent) throws DEC_Exception {
  this.vertices = vertices;
  this.index = index;
  this.orientation = orientation;
  this.scalarContent = scalarContent;
  this.vectorContent = vectorContent;
  this.scalarContent_2 = new HashMap<String, Float>();
  this.vectorContent_2 = new HashMap<String, PVector>();
  isBorder = false;
 }
 public DEC_Object(IndexSet vertices, int index, int orientation, HashMap<String, Float> scalarContent2, HashMap<String, PVector> vectorContent2) throws DEC_Exception {
  this.vertices = vertices;
  this.index = index;
  this.orientation = orientation;
  this.scalarContent = new ArrayList<Float>();
  this.vectorContent = new ArrayList<PVector>();
  this.scalarContent_2 = scalarContent2;
  this.vectorContent_2 = vectorContent2;
  isBorder = false;
 }
 public void markAsBorder() {
  isBorder = true;
 }

 public boolean isBorder() {
  return isBorder;
 }

 public int getIndex() {
  return this.index;
 }

 public void setIndex(int index) {
  this.index = index;
 }

 public int getOrientation() {
  return this.orientation;
 }

 public void setOrientation(int orientation) {
  this.orientation = orientation;
 }

 public ArrayList<DEC_Object> boundary() throws DEC_Exception {
  ArrayList<DEC_Object> bounds = new ArrayList<DEC_Object>();
  return bounds;
 }

 public IndexSet getVertices() {
  return this.vertices;
 }
 public float volume(DEC_GeometricContainer container) throws DEC_Exception {
  return 0;
 }

 public boolean contains(DEC_Object object) throws DEC_Exception {
  if (this instanceof DEC_PrimalObject && object instanceof DEC_DualObject) {
   throw new DEC_Exception("type mismatch, this is DEC_PrimalObject and object is DEC_DualObject");
  } else if (this instanceof DEC_DualObject && object instanceof DEC_PrimalObject) {
   throw new DEC_Exception("type mismatch, this is DEC_DualObject and object is DEC_PrimalObject");
  } else {
   return vertices.contains(object.getVertices());
  }
 }

 public boolean isContainedIn(DEC_Object object) throws DEC_Exception {
  if (this instanceof DEC_PrimalObject && object instanceof DEC_DualObject) {
   throw new DEC_Exception("type mismatch, this is DEC_PrimalObject and object is DEC_DualObject");
  } else if (this instanceof DEC_DualObject && object instanceof DEC_PrimalObject) {
   throw new DEC_Exception("type mismatch, this is DEC_DualObject and object is DEC_PrimalObject");
  } else {
   return object.contains(this);
  }

 }

 public boolean isEqual(DEC_Object object) throws DEC_Exception {
  if (this instanceof DEC_PrimalObject && object instanceof DEC_DualObject) {
   throw new DEC_Exception("type mismatch, this is DEC_PrimalObject and object is DEC_DualObject");
  } else if (this instanceof DEC_DualObject && object instanceof DEC_PrimalObject) {
   throw new DEC_Exception("type mismatch, this is DEC_DualObject and object is DEC_PrimalObject");
  } else {
   return vertices.isEqual(object.getVertices());
  }
 }

 public boolean cobordant(DEC_Object object) throws DEC_Exception {
  if (this instanceof DEC_PrimalObject && object instanceof DEC_DualObject) {
   throw new DEC_Exception("type mismatch, this is DEC_PrimalObject and object is DEC_DualObject");
  } else if (this instanceof DEC_DualObject && object instanceof DEC_PrimalObject) {
   throw new DEC_Exception("type mismatch, this is DEC_DualObject and object is DEC_PrimalObject");
  } else {
   ArrayList<DEC_Object> otherBounds = object.boundary();
   boolean isCobordant = false;
   for (int i = 0; i < otherBounds.size(); i++) {
    isCobordant = isCobordant || contains(otherBounds.get(i));
   }
   return isCobordant;
  }

 }
 public void addToScalarContent(ArrayList<Float> scalars){
  if(scalarContent == null){
   scalarContent = scalars;
  }else{
   for(int i=0;i<scalars.size();i++){
    scalarContent.add(scalars.get(i));
   }
  }
 }
 public void addToVectorContent(ArrayList<PVector> vectors){
  if(vectorContent == null){
   vectorContent = vectors;
  }else{
   for(int i=0;i<vectors.size();i++){
    vectorContent.add(vectors.get(i));
   }
  }
 }
 public void addToScalarContent(HashMap<String,Float> scalars){
  if(scalarContent == null){
   scalarContent_2 = scalars;
  }else{
   for(String varName: scalars.keySet()){
     scalarContent_2.put(varName,scalars.get(varName));
   }
  }
 }
 public void addToVectorContent(HashMap<String,PVector> vectors){
  if(vectorContent == null){
   vectorContent_2 = vectors;
  }else{
   for(String varName: vectors.keySet()){
     vectorContent_2.put(varName,vectors.get(varName));
   }
  }
 }
 
 public void addToScalarContent(String varName, float value){
  if(!scalarContent_2.containsKey(varName)){
   scalarContent_2.put(varName, value);
  }
 }
 public void addToScalarContent(float scalar) {
  scalarContent.add(new Float(scalar));
 }

 public void addToVectorContent(PVector vector) {
  vectorContent.add(vector);
 }
 public void addToVectorContent(String vecName, PVector vector) {
  if(!vectorContent_2.containsKey(vecName)){
   vectorContent_2.put(vecName,vector);
  }
 }
 public int vectorContentSize() {
  return vectorContent.size();
 }

 public int scalarContentSize() {
  return scalarContent.size();
 }

 public float getScalarContent(int position) throws DEC_Exception {
  if (position < 0 || position > scalarContent.size()) {
   throw new DEC_Exception(" index: " + position + " is out of bounds in scalar content.");
  } else {
   return scalarContent.get(position).floatValue();
  }
 }

 public PVector getVectorContent(int position) throws DEC_Exception {
  if (position < 0 || position >= vectorContent.size()) {
   throw new DEC_Exception(" index: " + position + " is out of bounds in vector content.");
  } else {
   return vectorContent.get(position);
  }
 }
 public float getScalarContent(String name) throws DEC_Exception {
  if (!scalarContent_2.containsKey(name)) {
   throw new DEC_Exception(" parameter: " + name + " is not in scalar content.");
  } else {
   return scalarContent_2.get(name).floatValue();
  }
 }

 public PVector getVectorContent(String name) throws DEC_Exception {
  if (!vectorContent_2.containsKey(name)) {
   throw new DEC_Exception(" parameter: " + name + " is not in vector content.");
  } else {
   return vectorContent_2.get(name);
  }
 }
 public void deleteFromScalarContent(int position) throws DEC_Exception {
  if (position < 0 || position > scalarContent.size()) {
   throw new DEC_Exception(" index: " + position + " is out of bounds in scalar content.");
  } else {
   scalarContent.remove(position);
  }
 }

 public void deleteFromVectorContent(int position) throws DEC_Exception {
  if (position < 0 || position > scalarContent.size()) {
   throw new DEC_Exception(" index: " + position + " is out of bounds in vector content.");
  } else {
   vectorContent.remove(position);
  }
 }
 public void deleteFromScalarContent(String name) throws DEC_Exception {
  if (!scalarContent_2.containsKey(name)) {
   throw new DEC_Exception(" parameter: " + name + " is not in scalar content.");
  } else {
   scalarContent_2.remove(name);
  }
 }

 public void deleteFromVectorContent(String name) throws DEC_Exception {
  if (!vectorContent_2.containsKey(name)) {
   throw new DEC_Exception(" parameter: " + name + " is not in vector content.");
  } else {
   vectorContent_2.remove(name);
  }
 }

 public ArrayList<PVector> getGeometry(DEC_GeometricContainer geometricContainer) throws DEC_Exception {
  return new ArrayList<PVector>();
 }
 
 public int dimension() {
  return vertices.size() - 1;
 }

 @Override
 public String toString() {
  return "[" + index + "]:" + vertices.toString() + "(" + orientation + ")";
 }
}
