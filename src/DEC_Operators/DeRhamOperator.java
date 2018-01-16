/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DEC_Operators;

import complex.DEC_Complex;
import complex.DEC_Iterator;
import complex.DEC_Object;
import complex.DEC_PrimalObject;
import containers.DEC_GeometricContainer;
import exceptions.DEC_Exception;
import fieldsAndForms.ContinuousDifferentialForm;
import fieldsAndForms.ContinuousVectorField;
import fieldsAndForms.DiscreteDifferentialForm;
import java.util.ArrayList;
import processing.core.PVector;
import utils.CalculationUtils;

/**
 *
 * @author Camilo Rey Torres - Leonardo Florez MISyC-2016. Pontificia Universidad Javeriana. Bogotá Colombia. 
 */
public class DeRhamOperator {
 DEC_Complex complex;
 public DeRhamOperator(){
  this.complex = null;
 } 
 public DeRhamOperator(DEC_Complex complex){
  this.complex = complex;
 }
 public double integrate1Form(ContinuousDifferentialForm dForm, DEC_Object edge, DEC_GeometricContainer container) throws DEC_Exception{
  ArrayList<PVector> verts = container.getGeometricContent(edge);
  PVector edgeDirection = PVector.sub(verts.get(1),verts.get(0));
  double[] gaussianPoints = CalculationUtils.rescaleGaussianPoints(0, 1, 4);
  double[] gaussianWeights = CalculationUtils.gaussianWeights_Order4;
  double result = 0;
  for(int i=0;i<gaussianPoints.length;i++){
   PVector p = CalculationUtils.convexCombination((float) gaussianPoints[i], verts.get(0), verts.get(1));
   result += gaussianWeights[i]*dForm.value(edgeDirection, p)*0.5d;
  }
  return result; 
 }
 public double integrate2Form(ContinuousDifferentialForm dForm, DEC_Object face, DEC_GeometricContainer container) throws DEC_Exception{
  ArrayList<PVector> verts = container.getObjectVectorContent(face);
  return 0;
 }
 public DiscreteDifferentialForm apply(ContinuousDifferentialForm dForm,DEC_GeometricContainer container) throws DEC_Exception{
  DiscreteDifferentialForm result = new DiscreteDifferentialForm(dForm.getDimension(), dForm.getType());
  DEC_Iterator iterator = complex.createIterator(dForm.getDimension(), dForm.getType());
  while(iterator.hasNext()){
   if(dForm.getType()=='p'){
    if(dForm.getDimension()==0){
     DEC_PrimalObject vertex = (DEC_PrimalObject) iterator.next();
     PVector center = vertex.getVectorContent("CENTER");
     result.assignScalar(vertex, dForm.value(center));
    }else if(dForm.getDimension()==1){
     DEC_PrimalObject edge = (DEC_PrimalObject) iterator.next();
     double formValue = 0;
     formValue = integrate1Form(dForm, edge, container);
     result.assignScalar(edge, formValue);
    }else if(dForm.getDimension()==2){
     DEC_PrimalObject face = (DEC_PrimalObject) iterator.next();
     double formValue = 0;
     formValue = integrate2Form(dForm, face, container);
     result.assignScalar(face, formValue);
    }
   }
  }
  return result;
 }
}
