/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fieldsAndForms;

import complex.DEC_Complex;
import complex.DEC_Iterator;
import complex.DEC_Object;
import exceptions.DEC_Exception;
import java.util.HashMap;
import processing.core.PVector;

/**
 *
 * @author laptop
 */
public class ContinuousVectorField{
 
 public ContinuousVectorField(){
 } 
 public Double P(float x, float y, float z){
  return new Double(x-y);
 }
 public Double Q(float x, float y, float z){
  return new Double(x*z);
 }
 public Double R(float x,float y, float z){
  return new Double(y*y-x*z);
 }
 public Double[] fieldFunction(float x, float y, float z){
  return new Double[]{P(x,y,z),Q(x,y,z), R(x,y,z)};
 }
 public double fieldMagnitude(float x, float y, float z){
  Double[] value = fieldFunction(x,y,z);
  return Math.sqrt(value[0]*value[0]+value[1]*value[1]+value[2]*value[2]);
 }
 public double[] fieldDirection(float x, float y, float z){
  double mag = fieldMagnitude(x,y,z);
  if(mag == 0){
   return new double[]{0,0,0};
  }else{
   return new double[]{P(x,y,z)/mag,Q(x,y,z)/mag,R(x,y,z)/mag};
  }
 }
}
