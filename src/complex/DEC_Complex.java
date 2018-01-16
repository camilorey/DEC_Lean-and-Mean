/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complex;

//

import containers.DEC_GeometricContainer;
import exceptions.DEC_Exception;
import utils.IndexSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import processing.core.PVector;
import readers.OBJMeshReader;
import utils.GeometricUtils;

/**
 *
 * @author Camilo Rey Torres - Leonardo Florez MISyC-2016. Pontificia Universidad Javeriana. Bogotá Colombia. 
 */
public class DEC_Complex {

 protected ArrayList<DEC_PrimalObject> primalVertices;
 protected ArrayList<DEC_PrimalObject> primalEdges;
 protected ArrayList<DEC_PrimalObject> primalFaces;
 protected ArrayList<DEC_PrimalObject> primalTets;
 protected ArrayList<DEC_DualObject> dualVertices;
 protected ArrayList<DEC_DualObject> dualEdges;
 protected ArrayList<DEC_DualObject> dualFaces;
 protected ArrayList<DEC_DualObject> dualCells;
 public DEC_Complex() {
  primalVertices = new ArrayList<DEC_PrimalObject>();
  primalEdges = new ArrayList<DEC_PrimalObject>();
  primalFaces = new ArrayList<DEC_PrimalObject>();
  primalTets = new ArrayList<DEC_PrimalObject>();
  dualVertices = new ArrayList<DEC_DualObject>();
  dualEdges = new ArrayList<DEC_DualObject>();
  dualFaces = new ArrayList<DEC_DualObject>();
  dualCells = new ArrayList<DEC_DualObject>();
 }
 
 public void addObject(DEC_Object object) throws DEC_Exception{
   if(object.dimension()==0){
    if(object instanceof DEC_PrimalObject){
     primalVertices.add(new DEC_PrimalObject(object));
    }else{
     dualVertices.add(new DEC_DualObject(object,'v'));
    }
   }else if(object.dimension()==1){
    if(object instanceof DEC_PrimalObject){
     primalEdges.add(new DEC_PrimalObject(object));
    }else{
     dualEdges.add(new DEC_DualObject(object,'e'));
    }
   }else if(object.dimension()==2){
    if(object instanceof DEC_PrimalObject){
     primalFaces.add(new DEC_PrimalObject(object));
    }else{
     dualFaces.add(new DEC_DualObject(object,'f'));
    }
   }else{
    if(object instanceof DEC_PrimalObject){
     primalTets.add(new DEC_PrimalObject(object));
    }else{
     dualCells.add(new DEC_DualObject(object,'c'));
    }
   }
  
 }
 public void setComplex(DEC_GeometricContainer container,OBJMeshReader mesh)throws DEC_Exception{
  setPrimalComplex(container);
  setDualComplex2(container);
 }
 public void setDualComplex2(DEC_GeometricContainer container) throws DEC_Exception{
  //create dual vertices from primal faces
  DEC_Iterator faceIterator = createIterator(2,'p');
  while(faceIterator.hasNext()){
   DEC_PrimalObject face = (DEC_PrimalObject) faceIterator.next();
   DEC_DualObject dualVert = new DEC_DualObject(new IndexSet(face.getIndex()),face.getIndex(),'v');
   HashMap<String,PVector> vecContent = container.createObjectVectorContent(dualVert);
   dualVert.addToVectorContent(vecContent);
   addObject(dualVert);
  }
  System.out.println("Dual vertices created: "+dualVertices.size());
  DEC_Iterator edgeIterator = createIterator(1,'p');
  while(edgeIterator.hasNext()){
   DEC_PrimalObject edge = (DEC_PrimalObject) edgeIterator.next();
   ArrayList<DEC_PrimalObject> ring = primalRing(edge);
   int i0=-1;
   int i1=-1;
   int dualEdgeOrientation = edge.getOrientation();
   boolean dualBorder = false;
   DEC_DualObject dualEdge = null;
   if(ring.size()==2){
    i0 = ring.get(1).getIndex();
    i1 = ring.get(1).getIndex();
    dualEdgeOrientation *= ring.get(1).getOrientation();
    edge.markAsBorder();
    dualBorder = true;
    dualEdge = new DEC_DualObject(new IndexSet(new int[]{i0,i1}), edge.getIndex(),'e');
    dualEdge.addToVectorContent("CENTER", edge.getVectorContent("CENTER"));
   }else{
    i0 = ring.get(1).getIndex();
    i1 = ring.get(2).getIndex();
    dualEdgeOrientation *= ring.get(1).getOrientation();
    dualBorder = false;
    dualEdge = new DEC_DualObject(new IndexSet(new int[]{i0,i1}), edge.getIndex(),'e');
    HashMap<String,PVector> vecContent = container.createObjectVectorContent(dualEdge);
    dualEdge.addToVectorContent(vecContent);
    dualEdge.addToVectorContent("CENTER", edge.getVectorContent("CENTER"));
   }
   dualEdge.setOrientation(dualEdgeOrientation);
   if(dualBorder){
    dualEdge.markAsBorder();
   }
   addObject(dualEdge);
  }
  System.out.println("dual edges created: "+dualEdges.size());
  //create dual faces from primal vertices
  DEC_Iterator vertexIterator = createIterator(0,'p');
  while(vertexIterator.hasNext()){
   DEC_PrimalObject vertex = (DEC_PrimalObject) vertexIterator.next();
   ArrayList<DEC_PrimalObject> ring = primalRing(vertex);
   ArrayList<Integer> dualFaceIndices = new ArrayList<Integer>();
   ArrayList<PVector> dualFaceGeometricVerts = new ArrayList<PVector>();
   ArrayList<PVector> dualFaceExtraNormals = new ArrayList<PVector>();
   boolean isBorder = false;
   for(int i=0;i<ring.size();i++){
    if(ring.get(i).dimension()==2){
     dualFaceIndices.add(ring.get(i).getIndex());
    }
    isBorder = isBorder || ring.get(i).isBorder();
    dualFaceGeometricVerts.add(ring.get(i).getVectorContent("CENTER"));
   }
   DEC_DualObject dualFace = new DEC_DualObject(new IndexSet(dualFaceIndices),vertex.getIndex(),'f');
   dualFace.setExtraGeometricContent(dualFaceGeometricVerts);
   dualFace.setOrientation(vertex.getOrientation());
   dualFace.addToVectorContent("CENTER", vertex.getVectorContent("CENTER"));
   dualFace.addToVectorContent("NORMAL_0",vertex.getVectorContent("NORMAL_0"));
   if(isBorder){
    vertex.markAsBorder();
    dualFace.markAsBorder();
   }
   addObject(dualFace);
  }
  System.out.println("dual faces created: "+dualFaces.size());
 }
 public void setPrimalComplex(DEC_GeometricContainer container) throws DEC_Exception{
  ArrayList<IndexSet> primalFaceIndices = container.getModelFaceIndexInformation();
  ArrayList<PVector> primalVertexInformation = container.getPrimalVerticesInformation();
  //create primal vertices
  for(int i=0;i<primalVertexInformation.size();i++){
   IndexSet vertSet = new IndexSet(i);
   DEC_PrimalObject vertex = new DEC_PrimalObject(new IndexSet(i),i);
   HashMap<String,PVector> vectContent = container.createObjectVectorContent(vertex);
   vertex.addToVectorContent(vectContent);
   addObject(vertex);
  }
  System.out.println("primal vertices created: "+primalVertices.size());
  //create primal faces
  int edgeIndexInComplex = 0;
  for(int i=0;i<primalFaceIndices.size();i++){
   IndexSet faceIndices = primalFaceIndices.get(i);
   DEC_PrimalObject face = new DEC_PrimalObject(faceIndices,i);
   HashMap<String,PVector> vectContent = container.createObjectVectorContent(face);
   face.addToVectorContent(vectContent);
   int decOrientation = faceIndices.sortVertices();
   int surfaceOrientation = container.calculateObjectOrientation(face);
   face.setOrientation(surfaceOrientation*decOrientation);
   addObject(face);
  }
  System.out.println("primal faces created: "+primalFaces.size());
  //create primal edges from face boundary
  for(int i=0;i<primalFaces.size();i++){
   DEC_PrimalObject face = primalFaces.get(i);
   ArrayList<DEC_Object> boundEdges = face.boundary();
   for(int j=0;j<boundEdges.size();j++){
    DEC_PrimalObject edge = new DEC_PrimalObject(boundEdges.get(j));
    if(objectIndexSearch(edge)==-1){
     HashMap<String,PVector> vectContent = container.createObjectVectorContent(edge);
     edge.addToVectorContent(vectContent);
     edge.setIndex(edgeIndexInComplex);
     addObject(edge);
     edgeIndexInComplex++;
    }
   }
  }
  System.out.println("primal edges created: "+primalEdges.size());
 }
 public DEC_Iterator objectNeighborhood(DEC_Object object) throws DEC_Exception{
  ArrayList neighborhoodContent = null;
  DEC_Iterator neighIter = new DEC_Iterator();
  if(object instanceof DEC_PrimalObject){
   neighborhoodContent = primalObjectNeighborhood(object.dimension(),object.getIndex());
   neighIter.setList(neighborhoodContent);
  }else if(object instanceof DEC_DualObject){
   neighborhoodContent = dualObjectNeighborhood(object.dimension(),object.getIndex());
   neighIter.setList(neighborhoodContent);
  }
  return neighIter;
 }
 public ArrayList<DEC_DualObject> dualObjectNeighborhood(DEC_DualObject object) throws DEC_Exception{
  ArrayList<DEC_DualObject> neighborhood = new ArrayList<DEC_DualObject>();
  if(object.dimension()== 0){
   DEC_PrimalObject dualFace = dual(object);
   ArrayList<DEC_PrimalObject> faceNeighbors = primalObjectNeighborhood(dualFace);
   for(int i=0;i<faceNeighbors.size();i++){
    neighborhood.add(dual(faceNeighbors.get(i)));
   }
  }else if(object.dimension()==1){
   int i0 = object.getVertices().getIndex(0);
   int i1 = object.getVertices().getIndex(1);
   DEC_PrimalObject dualEdge = dual(object);
   DEC_PrimalObject f0 = getPrimalObject(2,i0);
   DEC_PrimalObject f1 = getPrimalObject(2,i1);
   ArrayList<DEC_Object> boundsF0 = f0.boundary();
   ArrayList<DEC_Object> boundsF1 = f1.boundary();
   for(int i=0;i<boundsF0.size();i++){
    int boundIndex = primalObjectIndexSearch(new DEC_PrimalObject(boundsF0.get(i)));
    if(boundIndex != -1 && boundIndex!= object.getIndex()){
     neighborhood.add(getDualObject(1,boundIndex));
    }
   }
   for(int i=0;i<boundsF1.size();i++){
    int boundIndex = primalObjectIndexSearch(new DEC_PrimalObject(boundsF1.get(i)));
    if(boundIndex != -1 && boundIndex!= object.getIndex()){
     neighborhood.add(getDualObject(1,boundIndex));
    }
   }
  }else if(object.dimension()==2){
   DEC_PrimalObject dualVertex = dual(object);
   ArrayList<DEC_PrimalObject> vertexNeighbors = primalObjectNeighborhood(dualVertex);
   for(int i=0;i<vertexNeighbors.size();i++){
    neighborhood.add(dual(vertexNeighbors.get(i)));
   }
  }
  return neighborhood;
 }
 public ArrayList<DEC_DualObject> dualObjectNeighborhood(int dimension, int index) throws DEC_Exception{
  return dualObjectNeighborhood(getDualObject(dimension, index));
 }
 public ArrayList<DEC_PrimalObject> primalObjectNeighborhood(int dimension, int index) throws DEC_Exception{
  return primalObjectNeighborhood(getPrimalObject(dimension,index));
 }
 public ArrayList<DEC_PrimalObject> primalObjectNeighborhood(DEC_PrimalObject object) throws DEC_Exception{
  ArrayList<DEC_PrimalObject> neighborhood = new ArrayList<DEC_PrimalObject>();
  if(object.dimension()==0){
   DEC_DualObject dualFace = dual(object);
   ArrayList<DEC_Object> dualFaceEdges = dualFace.boundary();
   for(int i=0;i<dualFaceEdges.size();i++){
    int dualEdgeIndex = dualObjectIndexSearch(new DEC_DualObject(dualFaceEdges.get(i),'e'));
    if(dualEdgeIndex != -1){
     DEC_PrimalObject edge = getPrimalObject(1,dualEdgeIndex);
     int i0 = edge.getVertices().getIndex(0);
     int i1 = edge.getVertices().getIndex(1);
     if(i0 == object.getIndex()){
      neighborhood.add(getPrimalObject(0,i1));
     }else{
      neighborhood.add(getPrimalObject(0,i0));
     }
    }
   }
  }else if(object.dimension()==1){
    DEC_DualObject f0 = getDualObject(2, object.getVertices().getIndex(0));
    DEC_DualObject f1 = getDualObject(2, object.getVertices().getIndex(1));
    ArrayList<DEC_Object> boundsF0 = f0.boundary();
    ArrayList<DEC_Object> boundsF1 = f1.boundary();
    int indexOfEdge = -1;
    for(int i=0;i<boundsF0.size();i++){
     indexOfEdge = dualObjectIndexSearch(new DEC_DualObject(boundsF0.get(i),'e'));
     if(indexOfEdge != -1 && indexOfEdge != object.getIndex()){
      neighborhood.add(getPrimalObject(1,indexOfEdge));
     }
    }
    for(int i=0;i<boundsF1.size();i++){
     indexOfEdge = dualObjectIndexSearch(new DEC_DualObject(boundsF1.get(i),'e'));
     if(indexOfEdge != -1 && indexOfEdge != object.getIndex()){
      neighborhood.add(getPrimalObject(1,indexOfEdge));
     }
    }
   }else if(object.dimension()== 2){
     ArrayList<DEC_Object> faceBounds = object.boundary();
     for(int i=0;i<faceBounds.size();i++){
      int indexOfEdge = primalObjectIndexSearch(new DEC_PrimalObject(faceBounds.get(i)));
      if(indexOfEdge != -1){
       DEC_DualObject edgeDual = getDualObject(1,indexOfEdge);
       int dualIndex0 = edgeDual.getVertices().getIndex(0);
       int dualIndex1 = edgeDual.getVertices().getIndex(1);
       if(dualIndex0 == object.getIndex()){
        neighborhood.add(getPrimalObject(2,dualIndex1));
       }else{
        neighborhood.add(getPrimalObject(2,dualIndex0));
       } 
      }
    }
   }
   return neighborhood;
 }
 public DEC_PrimalObject getPrimalObject(int dimension, int position) throws DEC_Exception{
  if(dimension == 0){
   if(position<0 || position > primalVertices.size()){
    throw new DEC_Exception(" index: "+position+" is out of bounds.");
   }else{
    return primalVertices.get(position);
   }
  }else if(dimension == 1){
   if(position<0 || position > primalEdges.size()){
    throw new DEC_Exception(" index: "+position+" is out of bounds.");
   }else{
    return primalEdges.get(position);
   }
  }else if(dimension == 2){
   if(position<0 || position > primalFaces.size()){
    throw new DEC_Exception(" index: "+position+" is out of bounds.");
   }else{
    return primalFaces.get(position);
   }
  }else{
   if(position<0 || position > primalTets.size()){
    throw new DEC_Exception(" index: "+position+" is out of bounds.");
   }else{
    return primalTets.get(position);
   }
  }
 }
 public DEC_DualObject getDualObject(int dimension, int position) throws DEC_Exception{
  if(dimension == 0){
   if(position<0 || position > dualVertices.size()){
    throw new DEC_Exception(" index: "+position+" is out of bounds.");
   }else{
    return dualVertices.get(position);
   }
  }else if(dimension == 1){
   if(position<0 || position > dualEdges.size()){
    throw new DEC_Exception(" index: "+position+" is out of bounds.");
   }else{
    return dualEdges.get(position);
   }
  }else if(dimension == 2){
   if(position<0 || position > dualFaces.size()){
    throw new DEC_Exception(" index: "+position+" is out of bounds.");
   }else{
    return dualFaces.get(position);
   }
  }else{
   if(position<0 || position > dualCells.size()){
    throw new DEC_Exception(" index: "+position+" is out of bounds.");
   }else{
    return dualCells.get(position);
   }
  }
 }
 public int objectIndexSearch(DEC_Object object) throws DEC_Exception{
  if(object instanceof DEC_PrimalObject){
   return primalObjectIndexSearch(new DEC_PrimalObject(object));
  }else if(object instanceof DEC_DualObject){
   return dualObjectIndexSearch(new DEC_DualObject(object));
  }else{
   return -1;
  }
 }
 public int primalObjectIndexSearch(DEC_PrimalObject object)throws DEC_Exception{
  if(object.dimension()==0){
   return primalBinarySearchObjectIndex(object,primalVertices,0,primalVertices.size()-1);
  }else if(object.dimension()==1){
   return primalBinarySearchObjectIndex(object,primalEdges,0,primalEdges.size()-1);
  }else if(object.dimension()==2){
   return primalBinarySearchObjectIndex(object,primalFaces,0,primalFaces.size()-1);
  }else if(object.dimension()==3){
   return primalBinarySearchObjectIndex(object,primalTets,0,primalTets.size()-1);
  }else{
   return -1;
  }
 }
 public int dualObjectIndexSearch(DEC_DualObject object) throws DEC_Exception{
  if(object.dimension()==0){
   return dualBinarySearchObjectIndex(object, dualVertices, 0, dualVertices.size()-1);
  }else if(object.dimension()==1){
   return dualBinarySearchObjectIndex(object, dualEdges, 0, dualEdges.size()-1);
  }else if(object.dimension()==2){
   return dualBinarySearchObjectIndex(object, dualFaces, 0, dualFaces.size()-1);
  }else if(object.dimension()==3){
   return dualBinarySearchObjectIndex(object, dualCells, 0, dualCells.size()-1);
  }else{
   return -1;
  }
 }
 public int primalBinarySearchObjectIndex(DEC_PrimalObject object, ArrayList<DEC_PrimalObject> list, int i0, int i1)throws DEC_Exception{
  if(i0>i1){
   return -1;
  }else{
   if(i0==i1){
    if(list.get(i0).isEqual(object)){
     return i0;
    }else{
     return -1;
    }
   }else{
    int midPoint = (i0+i1)/2;
    int index1 = primalBinarySearchObjectIndex(object,list,i0,midPoint);
    int index2 = primalBinarySearchObjectIndex(object, list, midPoint+1, i1);
    if(index1==-1){
     return index2;
    }else{
     return index1;
    }
   }
  }
 }
 public int dualBinarySearchObjectIndex(DEC_DualObject object, ArrayList<DEC_DualObject> list, int i0, int i1) throws DEC_Exception{
  if(i0>i1){
   return -1;
  }else{
   if(i0==i1){
    if(list.get(i0).isEqual(object)){
     return i0;
    }else{
     return -1;
    }
   }else{
    int midPoint = (i0+i1)/2;
    int index1 = dualBinarySearchObjectIndex(object,list,i0,midPoint);
    if(index1==-1){
     return dualBinarySearchObjectIndex(object, list, midPoint+1, i1);
    }else{
     return index1;
    }
   }
  }
 }
 public void setObject(int position, DEC_Object object) throws DEC_Exception{
  if(position < 0){
   throw new DEC_Exception(position +"is out of bounds");
  }else{
   if(object instanceof DEC_PrimalObject){
    if(object.dimension() == 0){
     if(position > primalVertices.size()){
      throw new DEC_Exception(position +"is out of bounds");
     }else{
      primalVertices.set(position, new DEC_PrimalObject(object));
     }
    }else if(object.dimension() == 1){
     if(position > primalEdges.size()){
      throw new DEC_Exception(position +"is out of bounds");
     }else{
      primalEdges.set(position, new DEC_PrimalObject(object));
     }
    }else if(object.dimension() == 2){
     if(position > primalFaces.size()){
      throw new DEC_Exception(position +"is out of bounds");
     }else{
      primalFaces.set(position, new DEC_PrimalObject(object));
     }
    }else if(object.dimension() == 3){
     if(position > primalTets.size()){
      throw new DEC_Exception(position +"is out of bounds");
     }else{
      primalTets.set(position, new DEC_PrimalObject(object));
     }
    }
   }else if(object instanceof DEC_DualObject){
    if(object.dimension() == 0){
     if(position > dualVertices.size()){
      throw new DEC_Exception(position +"is out of bounds");
     }else{
      dualVertices.set(position,new DEC_DualObject(object,'v'));
     }
    }else if(object.dimension() == 1){
     if(position > dualEdges.size()){
      throw new DEC_Exception(position +"is out of bounds");
     }else{
      dualEdges.set(position,new DEC_DualObject(object,'e'));
     }
    }else if(object.dimension() == 2){
     if(position > dualFaces.size()){
      throw new DEC_Exception(position +"is out of bounds");
     }else{
      dualFaces.set(position,new DEC_DualObject(object,'f'));
     }
    }else if(object.dimension() == 3){
     if(position > dualCells.size()){
      throw new DEC_Exception(position +"is out of bounds");
     }else{
      dualCells.set(position, new DEC_DualObject(object,'c'));
     }
    }
   }else{
    throw new DEC_Exception("undefined DEC_Object used");
   } 
  }
 }
 public void deleteObject(int dimension, int position, char type) throws DEC_Exception{
  if(position < 0){
   throw new DEC_Exception(position +"is out of bounds");
  }else{
   if(type == 'p'){
    if(dimension == 0){
     if(position > primalVertices.size()){
      throw new DEC_Exception(position +"is out of bounds");
     }else{
      primalVertices.remove(position);
     }
    }else if(dimension == 1){
     if(position > primalEdges.size()){
      throw new DEC_Exception(position +"is out of bounds");
     }else{
      primalEdges.remove(position);
     }
    }else if(dimension == 2){
     if(position > primalFaces.size()){
      throw new DEC_Exception(position +"is out of bounds");
     }else{
      primalFaces.remove(position);
     }
    }else if(dimension == 3){
     if(position > primalTets.size()){
      throw new DEC_Exception(position +"is out of bounds");
     }else{
      primalTets.remove(position);
     }
    }
   }else if(type == 'd'){
    if(dimension == 0){
     if(position > dualVertices.size()){
      throw new DEC_Exception(position +"is out of bounds");
     }else{
      dualVertices.remove(position);
     }
    }else if(dimension == 1){
     if(position > dualEdges.size()){
      throw new DEC_Exception(position +"is out of bounds");
     }else{
      dualEdges.remove(position);
     }
    }else if(dimension == 2){
     if(position > dualFaces.size()){
      throw new DEC_Exception(position +"is out of bounds");
     }else{
      dualFaces.remove(position);
     }
    }else if(dimension == 3){
     if(position > dualCells.size()){
      throw new DEC_Exception(position +"is out of bounds");
     }else{
      dualCells.remove(position);
     }
    }
   }else{
    throw new DEC_Exception("undefined DEC_Object type used");
   } 
  }
 }
 public ArrayList<DEC_PrimalObject> primalRing(DEC_PrimalObject object) throws DEC_Exception{
  ArrayList<DEC_PrimalObject> objects = new ArrayList<DEC_PrimalObject>();
  objects.add(object);
  if(object.dimension() == 2){
   return objects;
  }else if(object.dimension()== 1){
   ArrayList<DEC_PrimalObject> contFaces = primalFacesContaining(object);
   for(int i=0;i<contFaces.size();i++){
    objects.add(contFaces.get(i));
   }
   return objects;
  }else if(object.dimension()==0){
   ArrayList<DEC_PrimalObject> contElements = new ArrayList<DEC_PrimalObject>();
   ArrayList<PVector> contCenters = new ArrayList<PVector>();
   HashMap<PVector,DEC_PrimalObject> centersToSimplices = new HashMap<PVector,DEC_PrimalObject>();
   ArrayList<DEC_PrimalObject> contFaces = primalFacesContaining(object);
   ArrayList<DEC_PrimalObject> contEdges = primalEdgesContaining(object);
   for(int i=0;i<contFaces.size();i++){
    contElements.add(contFaces.get(i));
    centersToSimplices.put(contFaces.get(i).getVectorContent("CENTER"), contFaces.get(i));
    contCenters.add(contFaces.get(i).getVectorContent("CENTER"));
   }
   for(int i=0;i<contEdges.size();i++){
    contElements.add(contEdges.get(i));
    centersToSimplices.put(contEdges.get(i).getVectorContent("CENTER"), contEdges.get(i));
    contCenters.add(contEdges.get(i).getVectorContent("CENTER"));
   }
   PVector vertCenter = object.getVectorContent("CENTER");
   PVector vertNormal = object.getVectorContent("NORMAL_0");
   ArrayList<PVector> sorted = GeometricUtils.sortPoints(contCenters, vertNormal, vertCenter);
   for(int i=0;i<sorted.size();i++){
    objects.add(centersToSimplices.get(sorted.get(i)));
   }
   return objects;
  }else{
   return objects;
  }
 }
 public ArrayList<DEC_PrimalObject> ringOfPrimalFacesForPrimalVertex(DEC_PrimalObject vertex) throws DEC_Exception{
  ArrayList<DEC_PrimalObject> faces = primalFacesContaining(vertex);
  HashMap<PVector,DEC_PrimalObject> centersToFaces = new HashMap<PVector,DEC_PrimalObject>();
  ArrayList<PVector> faceCenters = new ArrayList<PVector>();
  for(int i=0;i< faces.size();i++){
   PVector center = faces.get(i).getVectorContent("CENTER");
   faceCenters.add(center);
   centersToFaces.put(center,faces.get(i));
  }
  PVector vertexCenter = vertex.getVectorContent("CENTER");
  PVector vertexNormal = vertex.getVectorContent("NORMAL_0");
  ArrayList<PVector> sortedFaceCenters = GeometricUtils.sortPoints(faceCenters, vertexNormal, vertexCenter);
  ArrayList<DEC_PrimalObject> sortedRing = new ArrayList<DEC_PrimalObject>();
  for(int i=0;i<sortedFaceCenters.size();i++){
   sortedRing.add(centersToFaces.get(sortedFaceCenters.get(i)));
  }
  return sortedRing;
 }
 public ArrayList<DEC_PrimalObject> primalFacesContaining(DEC_PrimalObject object) throws DEC_Exception{
  if(object.dimension()>=0 && object.dimension()<2){
   return primalContainingElementsBinarySearch(object, primalFaces, 0, primalFaces.size()-1);
  }else{
   return new ArrayList<DEC_PrimalObject>();
  }
 }
 public ArrayList<DEC_PrimalObject> primalEdgesContaining(DEC_PrimalObject object) throws DEC_Exception{
  if(object.dimension()==0){
   return primalContainingElementsBinarySearch(object, primalEdges, 0, primalEdges.size()-1);
  }else{
   return new ArrayList<DEC_PrimalObject>();
  }
 }
 public ArrayList<DEC_PrimalObject> primalContainingElementsBinarySearch(DEC_PrimalObject object, ArrayList<DEC_PrimalObject> containingList, int i0, int i1) throws DEC_Exception{
  ArrayList<DEC_PrimalObject> containing = new ArrayList<DEC_PrimalObject>();
  if(i0>i1){
   return containing;
  }else if(i0 == i1){
   if(containingList.get(i0).contains(object)){
    containing.add(containingList.get(i0));
   }
   return containing;
  }else{
   int midPoint = (i0+i1)/2;
   ArrayList<DEC_PrimalObject> containing1 = primalContainingElementsBinarySearch(object,containingList,i0,midPoint);
   ArrayList<DEC_PrimalObject> containing2 = primalContainingElementsBinarySearch(object,containingList,midPoint+1,i1);
   if(!containing1.isEmpty()){
    for(int i=0;i<containing1.size();i++){
     containing.add(containing1.get(i));
    }
   }
   if(!containing2.isEmpty()){
    for(int i=0;i<containing2.size();i++){
     containing.add(containing2.get(i));
    }
   }
   return containing;
  }
 }
 public int numPrimalVertices(){
  return primalVertices.size();
 }
 public int numPrimalEdges(){
  return primalEdges.size();
 }
 public int numPrimalFaces(){
  return primalFaces.size();
 }
 public int numPrimalTets(){
  return primalTets.size();
 }
 public int numDualVertices(){
  return dualVertices.size();
 }
 public int numDualEdges(){
  return dualEdges.size();
 }
 public int numDualFaces(){
  return dualFaces.size();
 }
 public int numDualCells(){
  return dualCells.size();
 }
 public ArrayList<DEC_PrimalObject> cobordingElements(DEC_PrimalObject object) throws DEC_Exception{
  ArrayList<DEC_PrimalObject> cobording = new ArrayList<DEC_PrimalObject>();
  DEC_Iterator iter = createIterator(object.dimension(), 'p');
  while(iter.hasNext()){
   DEC_PrimalObject op = (DEC_PrimalObject) iter.next();
   if(op.cobordant(object)){
    cobording.add(op);
   }
  }
  return cobording;
 }
 public ArrayList<DEC_DualObject> cobordingElements(DEC_DualObject object) throws DEC_Exception{
  ArrayList<DEC_DualObject> cobording = new ArrayList<DEC_DualObject>();
  DEC_Iterator iter = createIterator(object.dimension(), 'd');
  while(iter.hasNext()){
   DEC_DualObject od = (DEC_DualObject) iter.next();
   if(od.cobordant(object)){
    cobording.add(od);
   }
  }
  return cobording;
 }
 public DEC_DualObject dual(DEC_PrimalObject object){
  if(object.dimension() == 0){
   if(dualCells.isEmpty()){
    return dualFaces.get(object.getIndex());
   }else{
    return dualCells.get(object.getIndex());
   }
  }else if(object.dimension() == 1){
   return dualEdges.get(object.getIndex());
  }else if(object.dimension() == 2){
   if(dualCells.isEmpty()){
    return dualVertices.get(object.getIndex());
   }else{
    return dualFaces.get(object.getIndex());
   }
  }else{
   return dualVertices.get(object.getIndex());
  }
 }
 public DEC_PrimalObject dual(DEC_DualObject object){
  if(object.dimension() == 0){
   if(primalTets.isEmpty()){
    return primalFaces.get(object.getIndex());
   }else{
    return primalTets.get(object.getIndex());
   }
  }else if(object.dimension() == 1){
   return primalEdges.get(object.getIndex());
  }else if(object.dimension() == 2){
   if(primalTets.isEmpty()){
    return primalVertices.get(object.getIndex());
   }else{
    return primalFaces.get(object.getIndex());
   }
  }else{
   return primalVertices.get(object.getIndex());
  }
 }
 public DEC_Iterator createIterator(DEC_PrimalObject object) throws DEC_Exception{
  ArrayList<DEC_PrimalObject> sortedElements = new ArrayList<DEC_PrimalObject>();
  sortedElements = recursiveFrontAdd(object, new ArrayList<DEC_PrimalObject>());
  DEC_Iterator objectIterator = new DEC_Iterator();
  objectIterator.setList(sortedElements);
  System.out.println(sortedElements.size());
  return objectIterator;
 }
 public ArrayList<DEC_PrimalObject> recursiveFrontAdd(DEC_PrimalObject object, ArrayList<DEC_PrimalObject> front) throws DEC_Exception{
  int indexOfObject = primalBinarySearchObjectIndex(object, front, 0, front.size()-1);
  if(indexOfObject == -1){
   front.add(object);
   ArrayList<DEC_PrimalObject> neighborhood = primalObjectNeighborhood(object);
   for(int i=0;i<neighborhood.size();i++){
    DEC_PrimalObject neigh = neighborhood.get(i);
    int indexOfNeigh = primalBinarySearchObjectIndex(neigh, front, 0, front.size()-1);
    if(indexOfNeigh == -1){
     front.add(neigh);
    }
   }
   
  }
  return front;
 }
 public DEC_Iterator createIterator(int dimension, char type) {
  DEC_Iterator iterator = new DEC_Iterator();
  switch(dimension){
   case 0:
    if(type == 'p'){
     iterator.setList(primalVertices);
    }else{
     iterator.setList(dualVertices);
    }
    break;
   case 1:
    if(type == 'p'){
     iterator.setList(primalEdges);
    }else{
     iterator.setList(dualEdges);
    }
    break;
   case 2:
    if(type == 'p'){
     iterator.setList(primalFaces);
    }else{
     iterator.setList(dualFaces);
    }
    break; 
   case 3:
    if(type == 'p'){
     iterator.setList(primalTets);
    }else{
     iterator.setList(dualCells);
    }
    break; 
  }
  return iterator;
 }
 public void printComplexInformation(){
  System.out.println("----------------------------------------------------------");
  System.out.println("-----------------DEC Complex Information-----------------");
  System.out.println("----------------------------------------------------------");
  System.out.println("num Tets: "+primalTets.size());
  System.out.println("num primal faces: "+primalFaces.size());
  System.out.println("num primal edges: "+primalEdges.size());
  System.out.println("num primal verts: "+primalVertices.size());
  System.out.println("----------------------------------------------------------");
  System.out.println("num cells: "+ dualCells.size());
  System.out.println("num dual faces: "+dualFaces.size());
  System.out.println("num dual edges: "+dualEdges.size());
  System.out.println("num dual vertices: "+dualVertices.size());
  System.out.println("----------------------------------------------------------");
  System.out.println("----------------------------------------------------------");
 }
}
