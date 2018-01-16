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
public class ScalarFunction{
 
 public ScalarFunction(){
 
 }
 public double function(float x, float y, float z){
  return Math.sin(Math.PI*x)*Math.cos(2*Math.PI*y)*Math.cos(3*Math.PI*z);
 }
 public double function(PVector v){
  return function(v.x,v.y,v.z);
 }
}
