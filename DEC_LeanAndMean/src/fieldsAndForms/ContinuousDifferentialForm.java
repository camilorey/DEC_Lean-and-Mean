/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fieldsAndForms;

import complex.DEC_Complex;
import complex.DEC_DualObject;
import complex.DEC_Iterator;
import complex.DEC_Object;
import complex.DEC_PrimalObject;
import containers.DEC_GeometricContainer;
import exceptions.DEC_Exception;
import java.util.ArrayList;
import java.util.HashMap;
import processing.core.PVector;
import utils.CalculationUtils;

/**
 *
 * @author laptop
 */
public class ContinuousDifferentialForm extends DiscreteDifferentialForm{
 ScalarFieldMatrix coefficientFunctions;
 public ContinuousDifferentialForm() {
  super();
  dimension = 0;
  coefficientFunctions = null;
 }

 public ContinuousDifferentialForm(int dimension, char type) {
  super(dimension, type);
  if(dimension == 0){
   coefficientFunctions = new ScalarFieldMatrix(1, 1);
  }else if(dimension == 1){
   coefficientFunctions = new ScalarFieldMatrix(3, 1);
  }else if(dimension == 2){
   coefficientFunctions = new ScalarFieldMatrix(3,3);
  }else{
   coefficientFunctions = null;
  }
 }
 public ContinuousDifferentialForm(ScalarFieldMatrix coefficientFunctions, char type){
  int dim = -1;
  if(coefficientFunctions.size()==1){
   dim = 0;
  }else if(coefficientFunctions.size()==3){
   dim = 1;
  }else if(coefficientFunctions.size()==9){
   dim = 2;
  }
  if(dim!=-1){
   this.dimension = dim;
   this.type = type;
   values = new HashMap<DEC_Object,Double>();
   valueLookUpTable = new HashMap<PVector,Float>();
   this.coefficientFunctions = coefficientFunctions;
  }
 }
 public Double value(ContinuousVectorField[] vectorFields,PVector p){
  return value(vectorFields,p.x,p.y,p.z);
 }
 public double value(PVector tangentVector, PVector p){
  if(dimension != 1){
   return 0.0d;
  }else{
   Double[] coefficients = coefficientFunctions.value(p.x,p.y,p.z);
   double result = coefficients[0].doubleValue()*tangentVector.x+
                   coefficients[1].doubleValue()*tangentVector.y+
                   coefficients[2].doubleValue()*tangentVector.z;
   return result;
  }
 }
 public Double value(PVector p){
  return value(p.x,p.y,p.z);
 }
 public Double value(float x, float y, float z){
  if(dimension!=0){
   return new Double(-1);
  }else{
   return coefficientFunctions.value(x,y,z)[0];
  }
 }
 public Double value(ContinuousVectorField[] vectorFields, float x, float y, float z){
  Double result = new Double(0);
  if(dimension == 0){
   result = coefficientFunctions.value(x,y,z)[0];
  }else if(dimension == 1){
   Double[] fieldValue = vectorFields[0].fieldFunction(x,y,z);
   Double[] coefficients = coefficientFunctions.value(x, y, z);
   result = CalculationUtils.dotProduct(fieldValue, coefficients);
  }else if(dimension == 2){
   Double[] fieldValue1 = vectorFields[0].fieldFunction(x,y,z);
   Double[] fieldValue2 = vectorFields[1].fieldFunction(x,y,z);
   Double[] coefficients = coefficientFunctions.value(x, y, z);
   result = CalculationUtils.scalarInnerProduct(coefficients, fieldValue1, fieldValue2, 3, 3);
  }
  return result;
 }
 public void calculateForm(ContinuousVectorField[] vectorFields, DEC_Complex complex) throws DEC_Exception{
  DEC_Iterator iterator = complex.createIterator(dimension, type);
  while(iterator.hasNext()){
   if(type == 'p'){
    DEC_PrimalObject pObject = (DEC_PrimalObject) iterator.next();
    PVector center = pObject.getVectorContent("CENTER");
    double value = value(vectorFields,center.x,center.y,center.z);
    assignScalar(pObject, value);
   }else if(type == 'd'){
    DEC_DualObject dObject = (DEC_DualObject) iterator.next();
    PVector center = dObject.getVectorContent("CENTER");
    double value = value(vectorFields,center.x,center.y,center.z);
    assignScalar(dObject, value);
   }
  }
 }
}
