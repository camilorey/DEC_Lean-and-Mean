/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex;

import java.util.ArrayList;

/**
 *
 * @author Camilo Rey Torres - Leonardo Florez MISyC-2016. Pontificia Universidad Javeriana. Bogotá Colombia. 
 */
public class DEC_Iterator {

 protected ArrayList content;
 int index;
 public DEC_Iterator() {
  content = new ArrayList();
  index = 0;
 }
 public DEC_Iterator(DEC_Object object){
  content = new ArrayList();
  content.add(object);
  index = 0;
 }
 public int size(){
  return content.size();
 }
 public void setList(ArrayList list){
  this.content = list;
 }
 public ArrayList getList(){
  return this.content;
 }
 public void add(DEC_Object object) {
  content.add(object);
 }
 public DEC_Object next() {
  DEC_Object o = (DEC_Object) content.get(index);
  index++;
  return o;
 }
 public boolean hasNext() {
  return index < content.size();
 }
 public void remove() {
  content.remove(index);
 }

 public void set(DEC_Object object) {
  content.set(index,object);
 }

 public DEC_Object previous() {
  return (DEC_Object) content.get(index-1);
 }

 public boolean hasPrevious() {
  return index>0;
 }
}
