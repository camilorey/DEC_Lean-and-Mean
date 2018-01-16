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
 * @author Camilo Rey Torres - Leonardo Florez MISyC-2016. Pontificia Universidad Javeriana. Bogotá Colombia. 
 */
public class ScalarAssignment {
 protected int dimension;
 protected char type;
 protected HashMap<DEC_Object,Double> values;
 protected HashMap<PVector,Float> valueLookUpTable;
 
 public ScalarAssignment(){
  this.dimension = 0;
  this.type = 'p';
  values = new HashMap<DEC_Object,Double>();
  valueLookUpTable = new HashMap<PVector,Float>();
 }

 public ScalarAssignment(int dimension,char type){
  this.dimension = dimension;
  this.type = type;
  values = new HashMap<DEC_Object,Double>();
  valueLookUpTable = new HashMap<PVector,Float>();
 }
 public HashMap<PVector, Float> getValueLookUpTable() {
  return valueLookUpTable;
 }
 public double getValue(DEC_Object object){
  if(values.containsKey(object)){
   return values.get(object).doubleValue();
  }else{
   System.out.println("value not contained for object: "+object);
   return 0;
  }
 }
 public void setValueLookUpTable(HashMap<PVector, Float> valueLookUpTable) {
  this.valueLookUpTable = valueLookUpTable;
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
 
 public void setValues(HashMap<DEC_Object, Double> values) {
  this.values = values;
 }
 
 public HashMap<DEC_Object, Double> getValues() {
  return values;
 }
 public void assignScalar(DEC_Object object, double value) throws DEC_Exception{
  if(object.dimension()!= dimension){
   throw new DEC_Exception("scalar assignment impossible: dimension mismatch");
  }else if(type == 'p' && (object instanceof DEC_DualObject)){
   throw new DEC_Exception("scalar assignment impossible: assignment is primal and object is dual");
  }else if(type == 'd' && (object instanceof DEC_PrimalObject)){
   throw new DEC_Exception("scalar assignment impossible: assignment is dual and object is primal");
  }else{
   values.put(object, new Double(value));
  }
 }
}
