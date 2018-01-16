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
 * @author laptop
 */
public class SparseMatrix {
 protected int numRows;
 protected int numCols;
 protected ArrayList<SparseVector> rows;
 
 public SparseMatrix(){
  this.numRows = 0;
  this.numCols = 0;
  this.rows = new ArrayList<SparseVector>();
 }
 public SparseMatrix(int size){
  this.numRows = size;
  this.numCols = size;
  this.rows = new ArrayList<SparseVector>();
  for(int i=0;i<numRows;i++){
   rows.add(new SparseVector(numCols));
  }
 }
 public SparseMatrix(int numRows, int numCols){
  this.numRows = numRows;
  this.numCols = numCols;
  this.rows = new ArrayList<SparseVector>();
  for(int i=0;i<numRows;i++){
   rows.add(new SparseVector(numCols));
  }
 }
 public int numRows(){
  return this.numRows;
 }
 public int numCols(){
  return this.numCols;
 }
 public void set(float value, int i, int j) throws DEC_Exception{
  if(value !=0){
   if(i<0 || i>= numRows || j<0 || j>= numCols){
    throw new DEC_Exception(" Sparse Matrix out of bounds: ("+i+","+j+") is not reachable");
   }else{
    rows.get(i).set(j, value);
   }
  }
 }
 public void setRow(int i, SparseVector row) throws DEC_Exception{
  if(i<0 || i>=numRows()){
   throw new DEC_Exception(" Sparse Matrix out of bounds: "+i+" is not reachable");
  }else{
   rows.set(i, row);
  }
 }
 public void setColumn(int j, SparseVector col) throws DEC_Exception{
  if(j<0 || j>= numCols){
   throw new DEC_Exception(" Sparse Matrix out of bounds: "+j+" is not reachable");
  }else{
   for(int i=0;i<numRows;i++){
    rows.get(i).set(j, col.get(j));
   }
  }
 }
 public SparseVector getRow(int i) throws DEC_Exception{
  if(i<0 || i>=numRows()){
   throw new DEC_Exception(" Sparse Matrix out of bounds: "+i+" is not reachable");
  }else{
   return rows.get(i);
  }
 }
 public float get(int i, int j) throws DEC_Exception{
  if(i<0 || j<0 || i>= numRows || j>= numCols){
   throw new DEC_Exception(" Sparse Matrix out of bounds: ("+i+","+j+") is not reachable");
  }
  return rows.get(i).get(j);
 }
 
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
 public SparseMatrix mult(float a) throws DEC_Exception{
  SparseMatrix result = new SparseMatrix(numRows(),numCols());
  for(int i=0;i<numRows;i++){
   result.setRow(i, getRow(i).mult(a));
  }
  return result;
 }
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
 public SparseMatrix transpose()throws DEC_Exception{
  SparseMatrix result = new SparseMatrix(numCols(),numRows());
  for(int j=0;j<numCols();j++){
   SparseVector vecTranspose = getColumn(j);
   result.setRow(j, vecTranspose);
  }
  return result;
 }
 public float[][] toArray(){
  float[][] asArray = new float[numRows][numCols];
  for(int i=0;i<numRows;i++){
   asArray[i] = rows.get(i).toArray();
  }
  return asArray;
 }
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
