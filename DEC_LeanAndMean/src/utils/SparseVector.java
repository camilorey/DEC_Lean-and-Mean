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
public class SparseVector {
 protected int size;
 protected ArrayList<SparseIndex> indices;
 
 public SparseVector(){
  this.size = 0;
  indices = new ArrayList<SparseIndex>();
 }
 public SparseVector(int size){
  this.size = size;
  indices = new ArrayList<SparseIndex>();
 }
 public int size(){
  return size;
 }
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
 public float get(int pos){
  int index = getSparseIndexPosition(pos);
  if(index == -1){
   return 0;
  }else{
   return indices.get(index).getValue();
  }
 }
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
 public int getSparseIndexPosition(int pos){
  return binaryIndexSearch(pos,0,indices.size()-1);
 }
 public void sortIndices(){
  mergeSort(0,indices.size()-1);
 }
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
 public void mergeSort(int low, int high){
  if(low<high){
   int middle = low + (high-low)/2;
   mergeSort(low,middle);
   mergeSort(middle+1,high);
   merge(low,middle,high);
  }
 }
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
 public float[] toArray(){
  float[] asArray = new float[size];
  for(int i=0;i<indices.size();i++){
   asArray[indices.get(i).getPosition()] = indices.get(i).getValue();
  }
  return asArray;
 }
 public void printIndexList(){
  String indexList ="";
  for(int i=0;i<indices.size();i++){
   indexList+= indices.get(i).toString();
   indexList+= " ";
  }
  System.out.println(indexList);
 }
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
