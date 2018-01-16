/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fieldsAndForms;

import complex.DEC_Complex;
import complex.DEC_Iterator;
import complex.DEC_Object;
import exceptions.DEC_Exception;
import java.util.HashMap;
import utils.SparseVector;

/**
 *
 * @author laptop
 */
public class DiscreteDifferentialForm extends ScalarAssignment{

 public DiscreteDifferentialForm() {
  super();
 }
 public DiscreteDifferentialForm(int dimension, char type) {
  super(dimension, type);
 }
 public void fromSparseVector(DEC_Complex complex, int dimension, char type, SparseVector vector) throws DEC_Exception{
  values = new HashMap<DEC_Object,Double>();
  this.dimension = dimension;
  this.type = type;
  DEC_Iterator iterator = complex.createIterator(dimension, type);
  while(iterator.hasNext()){
   DEC_Object object = iterator.next();
   values.put(object, new Double(vector.get(object.getIndex())));
  }
 }
 public SparseVector toSparseVector() throws DEC_Exception{
  SparseVector asVector = new SparseVector(values.size());
  for(DEC_Object o: values.keySet()){
   asVector.set(o.getIndex(), values.get(o).floatValue());
  }
  return asVector;
 }
 
}
