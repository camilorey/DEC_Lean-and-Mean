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
public class SparseVector {
 /**
  * size of Sparse Vector (initially filled with zeros). 
  */
 protected int size;
 /**
  * positions filled by Sparse Vector. 
  */
 protected ArrayList<SparseIndex> indices;
 
 /**
  * default constructor. Creates zero sized Sparse Vector
  */
 public SparseVector(){
  this.size = 0;
  indices = new ArrayList<SparseIndex>();
 }
 /**
  * Constructor with fixed size
  * @param size size of sparse vector to construct
  */
 public SparseVector(int size){
  this.size = size;
  indices = new ArrayList<SparseIndex>();
 }
 /**
  * size of Sparse Vector
  * @return size of Sparse Vector
  */
 public int size(){
  return size;
 }
 /**
  * set value in position pos in sparse vector. Throws exception if position is undefined (more than size of Sparse Vector). 
  * @param pos position to set 
  * @param value value to set in position
  * @throws DEC_Exception 
  */
 public void set(int pos, float value) throws DEC_Exception{
  if(value != 0){
   if(pos<0 || pos >= size){
    throw new DEC_Exception(" Sparse Vector out of bounds: "+size+" is not reachable");
   }else{
    int posIndex = getSparseIndexPosition(pos);
    if(posIndex == -1){
     indices.add(new SparseIndex(pos,value));
     sortIndices();
    }else{
     indices.get(posIndex).setValue(value);
    }
   }
  }
 }
 /**
  * get position from sparse vector 
  * @param pos position to search for
  * @return value in position pos
  */
 public float get(int pos){
  int index = getSparseIndexPosition(pos);
  if(index == -1){
   return 0;
  }else{
   return indices.get(index).getValue();
  }
 }
 /**
  * Sparse vector scalar product
  * @param a scalar to multiply
  * @return Sparse Vector mutiplied by scalar a (same size), throws exception if Sparse Vector is undefined. 
  * @throws DEC_Exception 
  */
 public SparseVector mult(float a) throws DEC_Exception{
  SparseVector result = new SparseVector(size());
  if(a == 0){
   return result;
  }else{
   for(int i=0;i<indices.size();i++){
    result.set(indices.get(i).getPosition(), indices.get(i).getValue()*a);
   }
   return result;
  }
 }
 /**
  * Sparse vector addition 
  * @param vec other vector to add
  * @return Sparse Vector adition between this and vec throws exception if Sparse Vectors differ in size. 
  * @throws DEC_Exception 
  */
 public SparseVector add(SparseVector vec) throws DEC_Exception{
  if(size()!= vec.size()){
   throw new DEC_Exception("vector size mismatch with: "+vec.size());
  }else{
   SparseVector result = new SparseVector(size());
   for(int i=0;i<size();i++){
    float val1 = get(i);
    float val2 = vec.get(i);
    if(val1 != 0 && val2 !=0){
     float res = val1+val2; 
     if(res!=0){
      result.set(i, res);
     }
    }else if(val1 !=0 && val2 == 0){
     result.set(i,val1);
    }else if(val1 == 0 && val2 !=0){
     result.set(i,val2);
    }
   }
   return result;
  }
 }
 /**
  * Sparse vector subtraction 
  * @param vec other vector to subtract
  * @return Sparse Vector subtraction between this and vec throws exception if Sparse Vectors differ in size. 
  * @throws DEC_Exception 
  */
 public SparseVector sub(SparseVector vec) throws DEC_Exception{
  if(size()!= vec.size()){
   throw new DEC_Exception("vector size mismatch with: "+vec.size());
  }else{
   SparseVector result = new SparseVector(size());
   for(int i=0;i<size();i++){
    float val1 = get(i);
    float val2 = vec.get(i);
    if(val1 != 0 && val2 !=0){
     float res = val1-val2; 
     if(res!=0){
      result.set(i, res);
     }
    }else if(val1 !=0 && val2 == 0){
     result.set(i,val1);
    }else if(val1 == 0 && val2 !=0){
     result.set(i,-val2);
    }
   }
   return result;
  }
 }
 /**
  * dot product between sparse vectors
  * @param vec other vector to do dot product with (throws exception if Sparse Vectors are different size). 
  * @return dot product between two vectors (scalar). 
  * @throws DEC_Exception 
  */
 public float prod(SparseVector vec) throws DEC_Exception{
  if(size()!= vec.size()){
   throw new DEC_Exception("vector size mismatch with: "+vec.size());
  }else{
   float result = 0;
   for(int i=0;i<size();i++){
    result += get(i)*vec.get(i);
   }
   return result;
  }
 }
 /**
  * method to get index content
  * @param pos position to get in sparse vector
  * @return value stored in position pos
  */
 public int getSparseIndexPosition(int pos){
  return binaryIndexSearch(pos,0,indices.size()-1);
 }
 /**
  * merge sort to sort indices in Sparse Vector (from low to high)
  */
 public void sortIndices(){
  mergeSort(0,indices.size()-1);
 }
 /**
  * Binary search in Sparse Vector
  * @param pos position to search for
  * @param low initial position in list
  * @param high final position to search for
  * @return index sought for (-1 if not in range)
  */
 public int binaryIndexSearch(int pos, int low, int high){
  if(low>high){
   return -1;
  }else if(low==high){
   return indices.get(low).getPosition() == pos? low: -1;
  }else{
    int mid = (low+high)/2;
    int index1 = binaryIndexSearch(pos,low,mid);
    if(index1 == -1){
     int index2 = binaryIndexSearch(pos,mid+1,high);
     return index2;
    }else{
     return index1;
    }
  }
 }
 /**
  * merge sort in Sparse Vector (recursive)
  * @param low initial position to merge sort
  * @param high final position to merge sort
  */
 public void mergeSort(int low, int high){
  if(low<high){
   int middle = low + (high-low)/2;
   mergeSort(low,middle);
   mergeSort(middle+1,high);
   merge(low,middle,high);
  }
 }
 /**
  * Helper method in merge sort 
  * @param low initial position in list to merge
  * @param mid middle position of the list to merge
  * @param high high position to merge
  */
 public void merge(int low, int mid,int high){
  ArrayList<SparseIndex> helper = new ArrayList<SparseIndex>();
  //create a copy of the indices array
  for(int i=0;i<indices.size();i++){
   helper.add(new SparseIndex(indices.get(i)));
  }
  int i = low;
  int j = mid+1;
  int k = low;
  //start merging
  while(i<=mid && j<= high){
   if(helper.get(i).compareTo(helper.get(j))<0){
    indices.set(k, helper.get(i));
    i++;
   }else{
     indices.set(k, helper.get(j));
     j++;
   }
   k++;
  }
  //copy the rest of the elements on the left side
  while(i<=mid){
   indices.set(k,helper.get(i));
   i++;
   k++;
  }
  while(j<=high){
   indices.set(k,helper.get(j));
   j++;
   k++;
  }
 }
 /**
  * method to turn Sparse Vector into float array including zeros.
  * @return Sparse Vector to float array
  */
 public float[] toArray(){
  float[] asArray = new float[size];
  for(int i=0;i<indices.size();i++){
   asArray[indices.get(i).getPosition()] = indices.get(i).getValue();
  }
  return asArray;
 }
 /**
  * Method to print all present indices in the Sparse Index (non-zero)
  */
 public void printIndexList(){
  String indexList ="";
  for(int i=0;i<indices.size();i++){
   indexList+= indices.get(i).toString();
   indexList+= " ";
  }
  System.out.println(indexList);
 }
 /**
  * Method to transform a Sparse Vector into a String. Vector components are separated by spaces
  * @return string with components separated by spaces (zeros included)
  */
 @Override
 public String toString(){
  float[] asArray = toArray();
  String asString = "";
  for(int i=0;i<asArray.length;i++){
   asString += asArray[i];
   if(i<asArray.length-1){
    asString += " ";
   }
  }
  return asString;
 }
}
