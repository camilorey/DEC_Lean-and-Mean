/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author laptop
 */
public class SparseIndex {
 protected int position;
 protected float value;
 
 public SparseIndex(){
  this.position = -1;
  this.value = 0;
 }
 public SparseIndex(int position){
  this.position = position;
  this.value = 0;
 }
 public SparseIndex(int position, float value){
  this.position = position;
  this.value = value;
 }
 public SparseIndex(SparseIndex index){
  this.position = index.getPosition();
  this.value = index.getValue();
 }
 public int getPosition() {
  return position;
 }
 public void setPosition(int position) {
  this.position = position;
 }
 public float getValue(){
  return value;
 } 
 public void setValue(float value){
  this.value = value;
 }
 public boolean isEqual(SparseIndex index){
  return getPosition()==index.getPosition() && getValue()==index.getValue();
 }
 public int compareTo(SparseIndex index){
  if(getPosition()<index.getPosition()){
   return -1;
  }else if(getPosition()==index.getPosition()){
   return 0;
  }else{
   return 1;
  }
 }
 @Override
 public String toString(){
  return "["+ position +":"+value+"]";
 }
}
