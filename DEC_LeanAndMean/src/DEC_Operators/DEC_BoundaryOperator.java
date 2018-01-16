/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DEC_Operators;

import complex.DEC_Complex;
import complex.DEC_DualObject;
import complex.DEC_Object;
import complex.DEC_PrimalObject;
import exceptions.DEC_Exception;
import java.util.ArrayList;
import utils.SparseMatrix;
import utils.SparseVector;

/**
 *
 * @author laptop
 */
public class DEC_BoundaryOperator extends DEC_MatrixOperator{
 
 public DEC_BoundaryOperator(){
  super();
 }
 @Override
 public void calculateOperator(DEC_Complex complex,int dimension, char type) throws DEC_Exception{
  if(dimension == 1 && type == 'p'){
   operatorMatrix = new SparseMatrix(complex.numPrimalVertices(),complex.numPrimalEdges());
   for(int i=0;i<complex.numPrimalEdges();i++){
    SparseVector boundAsVector = new SparseVector(complex.numPrimalVertices());
    DEC_PrimalObject edge = complex.getPrimalObject(1, i);
    ArrayList<DEC_Object> edgeBound = edge.boundary();
    for(int j=0;j<edgeBound.size();j++){
     DEC_PrimalObject vert = new DEC_PrimalObject(edgeBound.get(j));
     int index = complex.primalObjectIndexSearch(vert);
     if(index !=-1){
      boundAsVector.set(index, (float) vert.getOrientation());
     }
    }
    operatorMatrix.setColumn(i, boundAsVector);
   }
  }else if(dimension == 2 && type =='p'){
   operatorMatrix = new SparseMatrix(complex.numPrimalEdges(),complex.numPrimalFaces());
   for(int i=0;i<complex.numPrimalFaces();i++){
    SparseVector boundAsVector = new SparseVector(complex.numPrimalEdges());
    DEC_PrimalObject face = complex.getPrimalObject(2, i);
    ArrayList<DEC_Object> faceBound = face.boundary();
    for(int j=0;j<faceBound.size();j++){
     DEC_PrimalObject edge = new DEC_PrimalObject(faceBound.get(j));
     int index = complex.primalObjectIndexSearch(edge);
     if(index != -1){
      boundAsVector.set(index, (float) edge.getOrientation());
     }
    }
    operatorMatrix.setColumn(i, boundAsVector);
   }
  }else if(dimension == 1 && type == 'd'){
   operatorMatrix = new SparseMatrix(complex.numDualVertices(),complex.numDualEdges());
   for(int i=0;i<complex.numDualEdges();i++){
    SparseVector boundAsVector = new SparseVector(complex.numDualVertices());
    DEC_DualObject dualEdge = complex.getDualObject(1, i);
    ArrayList<DEC_Object> edgeBound = dualEdge.boundary();
    for(int j=0;j<edgeBound.size();j++){
     DEC_DualObject vert = new DEC_DualObject(edgeBound.get(j),'v');
     int index = complex.dualObjectIndexSearch(vert);
     if(index !=-1){
      boundAsVector.set(index, vert.getOrientation());
     }
    }
    operatorMatrix.setColumn(i, boundAsVector);
   }
  }else if(dimension == 2 && type == 'd'){
   operatorMatrix = new SparseMatrix(complex.numDualEdges(),complex.numDualFaces());
   for(int i=0;i<complex.numDualFaces();i++){
    SparseVector boundAsVector = new SparseVector(complex.numDualEdges());
    DEC_DualObject dualFace = complex.getDualObject(2, i);
    ArrayList<DEC_Object> faceBound = dualFace.boundary();
    for(int j=0;j<faceBound.size();j++){
     DEC_DualObject edge = new DEC_DualObject(faceBound.get(j),'e');
     int index = complex.dualObjectIndexSearch(edge);
     if(index !=-1){
      boundAsVector.set(index, edge.getOrientation());
     }
    }
    operatorMatrix.setColumn(i, boundAsVector);
   }
  }else{
   throw new DEC_Exception("undefined boundary requested");
  }
 }
}
