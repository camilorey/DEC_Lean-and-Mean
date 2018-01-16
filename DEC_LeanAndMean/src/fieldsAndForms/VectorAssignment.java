/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fieldsAndForms;

import complex.DEC_DualObject;
import complex.DEC_Object;
import complex.DEC_PrimalObject;
import exceptions.DEC_Exception;
import java.util.HashMap;
import processing.core.PVector;

/**
 *
 * @author laptop
 */
public class VectorAssignment {
 protected int dimension;
 protected char type;
 protected HashMap<DEC_Object,PVector> directions;
 protected HashMap<DEC_Object,Double>  magnitudes;
 protected HashMap<PVector,Float> magnitudeLookUpTable;
 
 public VectorAssignment(){
  this.dimension = 0;
  this.type = 'p';
  directions = new HashMap<DEC_Object,PVector>();
  magnitudes = new HashMap<DEC_Object,Double>();
  magnitudeLookUpTable = new HashMap<PVector,Float>();
 }
 public VectorAssignment(int dimension, char type){
  this.dimension = dimension;
  this.type = type;
  directions = new HashMap<DEC_Object,PVector>();
  magnitudes = new HashMap<DEC_Object,Double>();
  magnitudeLookUpTable = new HashMap<PVector,Float>();
 }

 public int getDimension() {
  return dimension;
 }

 public void setDimension(int dimension) {
  this.dimension = dimension;
 }

 public char getType() {
  return type;
 }

 public void setType(char type) {
  this.type = type;
 }

 public HashMap<DEC_Object, PVector> getDirections() {
  return directions;
 }

 public void setDirections(HashMap<DEC_Object, PVector> fieldDirections) {
  this.directions = fieldDirections;
 }

 public HashMap<DEC_Object, Double> getMagnitudes() {
  return magnitudes;
 }

 public void setMagnitudes(HashMap<DEC_Object, Double> fieldMagnitudes) {
  this.magnitudes = fieldMagnitudes;
 }

 public HashMap<PVector, Float> getMagnitudeLookUpTable() {
  return magnitudeLookUpTable;
 }

 public void setMagnitudeLookUpTable(HashMap<PVector, Float> magnitudeHues) {
  this.magnitudeLookUpTable = magnitudeHues;
 }
 public void assignVector(DEC_Object object, Double[] vector) throws DEC_Exception{
  if(object.dimension()!= dimension){
   throw new DEC_Exception("vector assignment impossible: dimension mismatch");
  }else if(type == 'p' && (object instanceof DEC_DualObject)){
   throw new DEC_Exception("vector assignment impossible: assignment is primal and object is dual");
  }else if(type == 'd' && (object instanceof DEC_PrimalObject)){
   throw new DEC_Exception("vector assignment impossible: assignment is dual and object is primal");
  }else{
   Double mag = new Double(Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1]+vector[2]*vector[2]));
   magnitudes.put(object, new Double(mag));
   if(mag == 0){
    directions.put(object, new PVector());
   }else{
    float x = vector[0].floatValue()/mag.floatValue();
    float y = vector[1].floatValue()/mag.floatValue();
    float z = vector[2].floatValue()/mag.floatValue();
    directions.put(object, new PVector(x,y,z));
   }
  }
 }
}
