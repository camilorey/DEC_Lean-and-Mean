/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import fieldsAndForms.ScalarFunction;
import processing.core.PVector;

/**
 *
 * @author laptop
 */
public class CalculationUtils {
 public static double[] gaussianPoints_Order2 = new double[]{-0.5773502691896257d,
                                                             0.5773502691896257};
 public static double[] gaussianWeights_Order2 = new double[]{1.0d,
                                                              1.0d};
 		
 public static double[] gaussianPoints_Order3 = new double[]{-0.7745966692414834d,
                                                              0.0d, 
                                                              0.7745966692414834d};
 public static double[] gaussianWeights_Order3 = new double[]{0.5555555555555556d,
                                                              0.8888888888888888d,
                                                              0.5555555555555556d};

 public static double[] gaussianPoints_Order4 = new double[]{-0.8611363115940526d,
                                                             -0.3399810435848563d, 
                                                              0.3399810435848563d,
                                                              0.8611363115940526};
 public static double[] gaussianWeights_Order4 = new double[]{0.3478548451374538d,
                                                              0.6521451548625461d,
                                                              0.6521451548625461d,
                                                              0.3478548451374538d};
 public static double[][] gaussianPointMatrix = new double[][]{{1.0d/3.0d,1.0d/3.0d},
                                                               {1.0d/5.0d,3.0d/5.0d},
                                                               {1.0d/5.0d,1.0d/5.0d},
                                                               {3.0d/5.0d,1.0d/5.0d}};
 public static double[] gaussianDoubleIntegralWeights = new double[]{-27.0d/48.0d,
                                                                      25.0d/48.0d,
                                                                      25.0d/48.0d,
                                                                      25.0d/48.0d};
 public static double[] rescaleGaussianPoints(float a, float b, int gaussianOrder){
  if(gaussianOrder == 2){
   double[] rescaled = new double[gaussianPoints_Order2.length];
   for(int i=0;i<rescaled.length;i++){
    rescaled[i] = gaussianPoints_Order2[i]*(b-a)/2.0f+(b+a)/2.0f;
   }
   return rescaled;
  }else if(gaussianOrder == 3){
   double[] rescaled = new double[gaussianPoints_Order3.length];
   for(int i=0;i<rescaled.length;i++){
    rescaled[i] = gaussianPoints_Order3[i]*(b-a)/2.0f+(b+a)/2.0f;
   }
   return rescaled;
  }else if(gaussianOrder==4){
   double[] rescaled = new double[gaussianPoints_Order4.length];
   for(int i=0;i<rescaled.length;i++){
    rescaled[i] = gaussianPoints_Order4[i]*(b-a)/2.0f+(b+a)/2.0f;
   }
   return rescaled;
  }else{
   return null;
  }
 }
 public static PVector convexCombination(float t, PVector v1, PVector v2){
  PVector result = new PVector();
  result.add(PVector.mult(v1, t));
  result.add(PVector.mult(v2,1-t));
  return result;
 }
 public static float lineIntegral(ScalarFunction f,PVector v1, PVector v2){
  float result = 0;
  float dist = v1.dist(v2);
  
  return result;
 }
 public static PVector barycentricPoint(float alfa, float beta, PVector v0, PVector v1, PVector v2){
  PVector point = new PVector();
  point.add(PVector.mult(v2, alfa));
  point.add(PVector.mult(v1,beta));
  point.add(PVector.mult(v0,1-(alfa+beta)));
  return point;
 }
 public static double barycentricJacobian(PVector v0, PVector v1, PVector v2){
  PVector d1 = PVector.sub(v1,v0);
  PVector d2 = PVector.sub(v2,v0);
  float area = d1.cross(d2).mag();
  return 1.0d*area;
 }
 public static Double gaussianSum(Double[] values, int gaussianOrder){
  Double result = new Double(0);
  if(gaussianOrder == 2){
   for(int i=0;i<gaussianWeights_Order2.length;i++){
    result += gaussianWeights_Order2[i]*values[i];
   }
  }else if(gaussianOrder == 3){
   for(int i=0;i<gaussianWeights_Order2.length;i++){
    result += gaussianWeights_Order2[i]*values[i];
   }
  }
  return result;
 }
 public static Double dotProduct(Double[] u, Double[] v){
  Double result = new Double(0);
  for(int i=0;i<u.length;i++){
   result += u[i]*v[i];
  }
  return result;
 }
 public static Double[] matrixVectorProduct(Double[] A, Double[] b, int numRows, int numCols){
  Double[] result = new Double[numRows];
  for(int i=0;i<numRows;i++){
   Double rowResult = new Double(0);
   for(int j=0;j<numCols;j++){
    int index = i+j*numRows;
    rowResult += b[i]*A[index];
   }
   result[i] = rowResult;
  }
  return result;
 }
 public static Double scalarInnerProduct(Double[] A, Double[] b1, Double[] b2, int numRows, int numCols){
  Double[] firstProduct = matrixVectorProduct(A,b2,numRows,numCols);
  return dotProduct(b1,firstProduct);
 }
}
