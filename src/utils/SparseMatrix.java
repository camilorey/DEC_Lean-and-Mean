/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import exceptions.DEC_Exception;
import java.util.ArrayList;

/**
 *
 * @author Camilo Rey Torres - Leonardo Florez MISyC-2016. Pontificia Universidad Javeriana. Bogotá Colombia. 
 */
public class SparseMatrix {
 /**
  * number of rows for Sparse Matrix
  */
 protected int numRows;
 /**
  * number of columns for Sparse Matrix
  */
 protected int numCols;
 /**
  * ArrayList containing Matrix content
  */
 protected ArrayList<SparseVector> rows;
 /**
  * Sparse Matrix default constructor initial size is 0 x 0
  */
 public SparseMatrix(){
  this.numRows = 0;
  this.numCols = 0;
  this.rows = new ArrayList<SparseVector>();
 }
 /**
  * Sparse Matrix constructor for square matrices of size size x size
  * @param size size of square matrix to construct
  */
 public SparseMatrix(int size){
  this.numRows = size;
  this.numCols = size;
  this.rows = new ArrayList<SparseVector>();
  for(int i=0;i<numRows;i++){
   rows.add(new SparseVector(numCols));
  }
 }
 /**
  * Sparse Matrix constructor fixing number of rows and number of columns (starts filled with zeros)
  * @param numRows number of rows of this
  * @param numCols number of columns of this
  */
 public SparseMatrix(int numRows, int numCols){
  this.numRows = numRows;
  this.numCols = numCols;
  this.rows = new ArrayList<SparseVector>();
  for(int i=0;i<numRows;i++){
   rows.add(new SparseVector(numCols));
  }
 }
 /**
  * get number of rows in sparse matrix
  * @return number of rows in Sparse Matrix
  */
 public int numRows(){
  return this.numRows;
 }
 /**
  * get numbber of columns in sparse matrix
  * @return number of defined columns
  */
 public int numCols(){
  return this.numCols;
 }
 /**
  * set individual value at position ij from this or exception if position ij is undefined
  * @param value value to insert
  * @param i number of row to insert 
  * @param j number of column to insert
  * @throws DEC_Exception if position ij is undefined
  */
 public void set(float value, int i, int j) throws DEC_Exception{
  if(value !=0){
   if(i<0 || i>= numRows || j<0 || j>= numCols){
    throw new DEC_Exception(" Sparse Matrix out of bounds: ("+i+","+j+") is not reachable");
   }else{
    rows.get(i).set(j, value);
   }
  }
 }
 /**
  * set i-th row of sparse matrix using vector or exception if row is undefined
  * @param i number of row to replace
  * @param row new row content
  * @throws DEC_Exception 
  */
 public void setRow(int i, SparseVector row) throws DEC_Exception{
  if(i<0 || i>=numRows()){
   throw new DEC_Exception(" Sparse Matrix out of bounds: "+i+" is not reachable");
  }else{
   rows.set(i, row);
  }
 }
 /**
  * set the j-th column of this using sparse vector or exception if column is undefined
  * @param j number of column to replace
  * @param col column content 
  * @throws DEC_Exception 
  */
 public void setColumn(int j, SparseVector col) throws DEC_Exception{
  if(j<0 || j>= numCols){
   throw new DEC_Exception(" Sparse Matrix out of bounds: "+j+" is not reachable");
  }else{
   for(int i=0;i<numRows;i++){
    rows.get(i).set(j, col.get(j));
   }
  }
 }
 /**
  * get Sparse Matrix i-th column as Sparse Vector
  * @param i number of row
  * @return i-th row of this as Sparse Vector or Exception if row not found
  * @throws DEC_Exception 
  */
 public SparseVector getRow(int i) throws DEC_Exception{
  if(i<0 || i>=numRows()){
   throw new DEC_Exception(" Sparse Matrix out of bounds: "+i+" is not reachable");
  }else{
   return rows.get(i);
  }
 }
 /**
  * get Sparse Matrix position 
  * @param i row number for sparse matrix
  * @param j column number for sparse matrix
  * @return value stored in position ij or Exception if position is undefined
  * @throws DEC_Exception 
  */
 public float get(int i, int j) throws DEC_Exception{
  if(i<0 || j<0 || i>= numRows || j>= numCols){
   throw new DEC_Exception(" Sparse Matrix out of bounds: ("+i+","+j+") is not reachable");
  }
  return rows.get(i).get(j);
 }
 /**
  * get Sparse Matrix j-th column vector as Sparse Vector 
  * @param j column number
  * @return SparseVector composed of the j-th column of this or Exception if column not found
  * @throws DEC_Exception 
  */
 public SparseVector getColumn(int j) throws DEC_Exception{
  if(j<0 || j>= numCols){
   throw new DEC_Exception(" Sparse Matrix out of bounds: "+j+" is not reachable");
  }
  SparseVector col = new SparseVector(numRows);
  for(int i=0;i<numRows;i++){
   float value = rows.get(i).get(j);
   if(value != 0){
    col.set(i, value);
   }
  }
  return col;
 }
 /**
  * Sparse matrix addition between this and given matrix
  * @param matrix matrix to add to this
  * @return this + matrix or exception if type mismatch
  * @throws DEC_Exception 
  */
 public SparseMatrix add(SparseMatrix matrix) throws DEC_Exception{
  if(numRows()!=matrix.numRows() || numCols()!=matrix.numCols()){
   throw new DEC_Exception("SparseMatrix dimension mismatch");
  }else{
   SparseMatrix result = new SparseMatrix(numRows,numCols);
   for(int i=0;i<numRows;i++){
    SparseVector rowResult = rows.get(i).add(matrix.getRow(i));
    result.setRow(i, rowResult);
   }
   return result;
  }
 }
 /**
  * Sparse matrix subtraction between this and given matrix
  * @param matrix matrix to subtract from this
  * @return this - matrix or exception if type mismatch
  * @throws DEC_Exception 
  */
 public SparseMatrix sub(SparseMatrix matrix) throws DEC_Exception{
  if(numRows()!=matrix.numRows() || numCols()!=matrix.numCols()){
   throw new DEC_Exception("SparseMatrix dimension mismatch");
  }else{
   SparseMatrix result = new SparseMatrix(numRows,numCols);
   for(int i=0;i<numRows;i++){
    SparseVector rowResult = rows.get(i).sub(matrix.getRow(i));
    result.setRow(i, rowResult);
   }
   return result;
  }
 }
 /**
  * scalar Sparse Matrix product
  * @param a scalar to multiply matrix
  * @return matrix aA 
  * @throws DEC_Exception if this is undefined
  */
 public SparseMatrix mult(float a) throws DEC_Exception{
  SparseMatrix result = new SparseMatrix(numRows(),numCols());
  for(int i=0;i<numRows;i++){
   result.setRow(i, getRow(i).mult(a));
  }
  return result;
 }
 /**
  * Matrix-Matrix prouct for this with other sparse matrix
  * @param matrix other matrix to multiply
  * @return matrix product between this and matrix 
  * @throws DEC_Exception if type mismatch (rows vs. columns)
  */
 public SparseMatrix prod(SparseMatrix matrix) throws DEC_Exception{
  if(numCols() != matrix.numRows()){
   throw new DEC_Exception("SparseMatrix dimension mismatch");
  }else{
    SparseMatrix result = new SparseMatrix(numRows(),matrix.numCols());
    for(int i=0;i<numRows();i++){
     for(int j=0;j<matrix.numCols();j++){
      SparseVector row = getRow(i);
      SparseVector col = matrix.getColumn(j);
      float prod = row.prod(col);
      if(prod!=0){
       result.set(prod, i, j);
      }
     }
    }
    return result;
  }
 }
 /**
  * Matrix-vector product for this
  * @param vector sparse vector to multiply
  * @return vector resulting from product Ax 
  * @throws DEC_Exception if type mismatch
  */
 public SparseVector prod(SparseVector vector) throws DEC_Exception{
  if(numCols()!= vector.size()){
   throw new DEC_Exception("SparseMatrix dimension mismatch");
  }else{
   SparseVector result = new SparseVector(numRows());
   for(int i=0;i<numRows();i++){
    SparseVector row = getRow(i);
    float prod = row.prod(vector);
    if(prod!=0){
     result.set(i, prod);
    }
   }
   return result;
  }
 }
 /**
  * Matrix transpose for Sparse Matrix
  * @return matrix transpose of this or exception if matrix is undefined
  * @throws DEC_Exception 
  */
 public SparseMatrix transpose()throws DEC_Exception{
  SparseMatrix result = new SparseMatrix(numCols(),numRows());
  for(int j=0;j<numCols();j++){
   SparseVector vecTranspose = getColumn(j);
   result.setRow(j, vecTranspose);
  }
  return result;
 }
 /**
  * Turns Sparse Matrix into float matrix including zeros. 
  * @return matrix representation of Sparse Matrix in 
  */
 public float[][] toArray(){
  float[][] asArray = new float[numRows][numCols];
  for(int i=0;i<numRows;i++){
   asArray[i] = rows.get(i).toArray();
  }
  return asArray;
 }
 /**
  * Returns Matrix as String (for file saving) including zeros. Matrix rows are separated by lines. Components are separated by spaces.
  * @return String representation of SparseMatrix
  */
 public String toString(){
  String asString = "";
  for(int i=0;i<numRows;i++){
   asString += rows.get(i).toString();
   if(i<numRows-1){
    asString += '\n';
   }
  }
  return asString;
 } 
}
