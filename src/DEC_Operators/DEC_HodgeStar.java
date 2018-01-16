/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DEC_Operators;

import complex.DEC_Complex;
import complex.DEC_DualObject;
import complex.DEC_Iterator;
import complex.DEC_PrimalObject;
import containers.DEC_GeometricContainer;
import exceptions.DEC_Exception;
import fieldsAndForms.DiscreteDifferentialForm;
import utils.SparseMatrix;
import utils.SparseVector;

/**
 *
 * @author Camilo Rey Torres - Leonardo Florez MISyC-2016. Pontificia Universidad Javeriana. Bogotá Colombia. 
 */
public class DEC_HodgeStar extends DEC_MatrixOperator{
 
 public DEC_HodgeStar(){
  super();
 }

 public void calculateOperator(DEC_Complex complex, int dimension, char type, DEC_GeometricContainer container) throws DEC_Exception {
  int N = -1;
  switch (dimension) {
   case 0:
    if(type == 'p'){
     N = complex.numPrimalVertices();
    }else{
     N = complex.numDualVertices();
    }break;
   case 1:
    if(type == 'p'){
     N = complex.numPrimalEdges();
    }else{
     N = complex.numDualEdges();
    }break;
   case 2:
    if(type == 'p'){
     N = complex.numPrimalFaces();
    }else{
     N = complex.numDualFaces();
    }break;
   default:
    break;
  }
  operatorMatrix = new SparseMatrix(N,N); 
  DEC_Iterator iterator = complex.createIterator(dimension, type);
  while(iterator.hasNext()){
   if(type == 'p'){
    DEC_PrimalObject simplex = (DEC_PrimalObject) iterator.next();
    DEC_DualObject dualSimplex = complex.dual(simplex);
    float primalVolume = simplex.volume(container);
    float dualVolume = simplex.volume(container);
    float hodgeContent = dualVolume / primalVolume;
    int index = simplex.getIndex();
    operatorMatrix.set(hodgeContent, index,index);
   }else{
    DEC_DualObject simplex = (DEC_DualObject) iterator.next();
    DEC_PrimalObject dualSimplex = complex.dual(simplex);
    float primalVolume = simplex.volume(container);
    float dualVolume = simplex.volume(container);
    float hodgeContent = dualVolume / primalVolume;
    int index = simplex.getIndex();
    operatorMatrix.set(hodgeContent, index,index);
   }
  }
 }
 public DiscreteDifferentialForm apply(DEC_Complex complex,DiscreteDifferentialForm dF) throws DEC_Exception{
  SparseVector result = operatorMatrix.prod(dF.toSparseVector());
  DiscreteDifferentialForm resultForm = new DiscreteDifferentialForm(dF.getDimension(), dF.getType());
  resultForm.fromSparseVector(complex, dF.getDimension(), dF.getType(), result);
  return resultForm;
 }
}
