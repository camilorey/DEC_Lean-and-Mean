/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import exceptions.DEC_Exception;
import static java.lang.Math.pow;
import java.util.ArrayList;

/**
 *
 * @author Camilo Rey Torres - Leonardo Florez MISyC-2016. Pontificia
 * Universidad Javeriana. Bogotá Colombia.
 */
public class IndexSet {

 protected ArrayList<Integer> indices;

 public IndexSet() {
  this.indices = new ArrayList<Integer>();
 }

 public IndexSet(int index) {
  this.indices = new ArrayList<Integer>();
  indices.add(new Integer(index));
 }

 public IndexSet(int i0, int i1) {
  this.indices = new ArrayList<Integer>();
  indices.add(new Integer(i0));
  indices.add(new Integer(i1));
 }

 public IndexSet(int i0, int i1, int i2) {
  this.indices = new ArrayList<Integer>();
  indices.add(new Integer(i0));
  indices.add(new Integer(i1));
  indices.add(new Integer(i2));
 }

 public IndexSet(int i0, int i1, int i2, int i3) {
  this.indices = new ArrayList<Integer>();
  indices.add(new Integer(i0));
  indices.add(new Integer(i1));
  indices.add(new Integer(i2));
  indices.add(new Integer(i3));
 }

 public IndexSet(int[] inds) {
  this.indices = new ArrayList<Integer>();
  for (int i = 0; i < inds.length; i++) {
   indices.add(new Integer(inds[i]));
  }
 }

 public IndexSet(ArrayList<Integer> inds) {
  this.indices = inds;
 }

 public int size() {
  return indices.size();
 }

 public int getIndex(int pos) throws DEC_Exception {
  if (pos < 0 || pos >= indices.size()) {
   throw new DEC_Exception(" index: " + pos + " is out of bounds.");
  } else {
   return indices.get(pos).intValue();
  }
 }

 public void addIndex(int value) throws DEC_Exception {
  if (indexPosition(value) != -1) {
   throw new DEC_Exception(" value: " + value + " is already in set.");
  } else {
   indices.add(new Integer(value));
  }
 }

 public void setIndex(int pos, int value) throws DEC_Exception {
  if (pos < 0) {
   throw new DEC_Exception(" index: " + pos + " is less than zero.");
  } else if (pos >= indices.size()) {
   throw new DEC_Exception(" index: " + pos + " is unreachable.");
  } else {
   indices.set(pos, new Integer(value));
  }
 }

 public void removeIndex(int pos) throws DEC_Exception {
  if (pos < 0) {
   throw new DEC_Exception(" index: " + pos + " is less than zero.");
  } else if (pos > indices.size()) {
   throw new DEC_Exception(" index: " + pos + " is unreachable.");
  } else {
   indices.remove(pos);
  }
 }

 public IndexSet removeFromIndices(int pos) throws DEC_Exception {
  if (pos < 0 || pos >= indices.size()) {
   throw new DEC_Exception(" index: " + pos + " is out of bounds.");
  } else {
   IndexSet removed = new IndexSet();
   for (int i = 0; i < indices.size(); i++) {
    if (i != pos) {
     removed.addIndex(indices.get(i));
    }
   }
   return removed;
  }
 }

 public int indexPosition(int value) {
  int index = -1;
  for (int i = 0; i < indices.size(); i++) {
   if (indices.get(i).intValue() == value) {
    index = i;
   }
  }
  return index;
 }

 public boolean containsIndex(int value) {
  return indexPosition(value) != -1;
 }

 public boolean contains(IndexSet set) throws DEC_Exception {
  boolean contains = true;
  for (int i = 0; i < set.size(); i++) {
   contains = contains && containsIndex(set.getIndex(i));
  }
  return contains;
 }

 public boolean isContainedIn(IndexSet set) throws DEC_Exception {
  return set.contains(this);
 }
/**
 * IndexSet comparison test (to see if indices match perfectly)
 * @param set other IndexSet to compare with
 * @return true if all indices in this are present in other IndexSet and sizes match. False, otherwise.
 * @throws DEC_Exception 
 */
 public boolean isEqual(IndexSet set) throws DEC_Exception {
  if (size() != set.size()) {
   return false;
  } else if (size() == 1) {
   return indices.get(0).intValue() == set.getIndex(0);
  } else {
   return contains(set) && isContainedIn(set);
  }
 }
/**
 * sort indices in IndexSet storing the sign of the permutation needed to sort
 * @return sign of permutation required to sort (in ascending order) numbers in IndexSet
 * @throws DEC_Exception
 */
 public int sortVertices() throws DEC_Exception {
  int numTranspositions = 0;
  for (int i = 0; i < size(); i++) {
   for (int j = 1; j < size() - i; j++) {
    int posJMinus = getIndex(j - 1);
    int posJ = getIndex(j);
    if (posJMinus > posJ) {
     setIndex(j - 1, posJ);
     setIndex(j, posJMinus);
     numTranspositions++;
    }
   }
  }
  return (int) pow(-1, numTranspositions);
 }
/**
 * set union between this and other set (not symmetric) 
 * @param set other IndexSet to create union from
 * @return set union between this and other set removing repeated indices.
 * @throws DEC_Exception if this or other IndexSet is undefined
 */
 public IndexSet union(IndexSet set) throws DEC_Exception {
  ArrayList<Integer> members = new ArrayList<Integer>();
  for (int i = 0; i < indices.size(); i++) {
   members.add(indices.get(i));
  }
  for (int i = 0; i < set.size(); i++) {
   if (!containsIndex(set.getIndex(i))) {
    members.add(set.getIndex(i));
   }
  }
  int[] values = new int[members.size()];
  for (int i = 0; i < values.length; i++) {
   values[i] = members.get(i).intValue();
  }
  return new IndexSet(values);
 }
/**
 * set intersection between this and set (non symmetric) 
 * @param set other set to intersect this
 * @return set intersection between this and set
 * @throws DEC_Exception if this or set is undefined
 */
 public IndexSet intersection(IndexSet set) throws DEC_Exception {
  ArrayList<Integer> members = new ArrayList<Integer>();
  for (int i = 0; i < indices.size(); i++) {
   if (set.containsIndex(indices.get(i).intValue())) {
    members.add(indices.get(i));
   }
  }
  int[] values = new int[members.size()];
  for (int i = 0; i < values.length; i++) {
   values[i] = members.get(i).intValue();
  }
  return new IndexSet(values);
 }
/**
 * Set Difference beetween this and given index set 
 * @param set other IndexSet to construct difference
 * @return set difference between this and set. 
 * @throws DEC_Exception if this or set is undefined
 */
 public IndexSet difference(IndexSet set) throws DEC_Exception {
  ArrayList<Integer> members = new ArrayList<Integer>();
  for (int i = 0; i < indices.size(); i++) {
   if (!set.containsIndex(indices.get(i).intValue())) {
    members.add(indices.get(i));
   }
  }
  int[] values = new int[members.size()];
  for (int i = 0; i < values.length; i++) {
   values[i] = members.get(i).intValue();
  }
  return new IndexSet(values);
 }
/**
 * symmetric difference between two index sets (A ~ B = (A\B)U (B\A) 
 * @param set other Index Set to construct symmetric difference (may be empty)
 * @return Index Set containing symmetric difference of this and set. 
 * @throws DEC_Exception if this or set is undefined
 */
 public IndexSet symmetricDifference(IndexSet set) throws DEC_Exception {
  return difference(set).union(set.difference(this));
 }

 /**
  * toString implementation in IndexSet. Indices are enclosed by brackets, separated by commas [i_0,i_1,...,i_n]
  * @return string representation of index set
  */
 @Override
 public String toString() {
  String content = "";
  for (int i = 0; i < indices.size(); i++) {
   content += indices.get(i).intValue();
   if (i < indices.size() - 1) {
    content += ",";
   }
  }
  return "[" + content + "]";

 }
}
