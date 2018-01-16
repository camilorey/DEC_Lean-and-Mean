/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Camilo Rey Torres - Leonardo Florez MISyC-2016. Pontificia Universidad Javeriana. Bogotá Colombia. 
 */
public class SparseIndex {
 protected int position;
 protected float value;
 /**
  * Default constructor for Sparse Index. Position is set to -1 and value set to zero.
  */
 public SparseIndex(){
  this.position = -1;
  this.value = 0;
 }
 /**
  * Constructor defining position for Sparse Index, default value is zero. 
  * @param position position to assign to Sparse Index
  */
 public SparseIndex(int position){
  this.position = position;
  this.value = 0;
 }
 /**
  * Sparse Index Constructor defining both value and position
  * @param position position of Sparse Index in collection
  * @param value value contained under position
  */
 public SparseIndex(int position, float value){
  this.position = position;
  this.value = value;
 }
 /**
  * Copy constructor of Sparse Index
  * @param index other Sparse Index to copy from
  */
 public SparseIndex(SparseIndex index){
  this.position = index.getPosition();
  this.value = index.getValue();
 }
 /**
  * get position of index in larger collection
  * @return sparse index position
  */
 public int getPosition() {
  return position;
 }
 /**
  * set position of sparse index
  * @param position position to set sparse index in
  */
 public void setPosition(int position) {
  this.position = position;
 }
 /**
  * get value contained in sparse index
  * @return value contained under this index
  */
 public float getValue(){
  return value;
 } 
 /**
  * Sets the value contained in this sparse index
  * @param value value to set under this index
  */
 public void setValue(float value){
  this.value = value;
 }
 /**
  * Comparison method to know if SparseIndex is equal (in position and value) than other given SparseIndex
  * @param index other SparseIndex to compare
  * @return true if this and index match in position and value, false otherwise.
  */
 public boolean isEqual(SparseIndex index){
  return getPosition()==index.getPosition() && getValue()==index.getValue();
 }
 /**
  * Sparse Index Comparison method
  * @param index SparseIndex to compare
  * @return -1 if the position of index is less than this, 0 if they are equal, 1 if this has less index than other index
  */
 public int compareTo(SparseIndex index){
  if(getPosition()<index.getPosition()){
   return -1;
  }else if(getPosition()==index.getPosition()){
   return 0;
  }else{
   return 1;
  }
 }
 /**
  * 
  * @return 
  */
 @Override
 public String toString(){
  return "["+ position +":"+value+"]";
 }
}
