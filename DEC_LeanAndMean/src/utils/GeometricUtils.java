/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import processing.core.PApplet;
import static processing.core.PApplet.atan2;
import static processing.core.PConstants.HALF_PI;
import static processing.core.PConstants.PI;
import static processing.core.PConstants.TWO_PI;
import processing.core.PVector;

/**
 *
 * @author Camilo Rey
 */
public class GeometricUtils {
 public static PVector midPoint(PVector p1, PVector p2){
  return centroid(new PVector[]{p1,p2});
 }
 public static PVector centroid(PVector[] p){
  PVector c = new PVector();
  for(int i=0;i<p.length;i++){
   c.add(p[i]);
  }
  c.div((float)p.length);
  return c;
 }
 public static PVector barycenter(PVector[] p){
  PVector c = new PVector();
  c.add(p[0]);
  c.add(p[1]);
  c.add(p[2]);
  c.div(3.0f);
  return c;
 }
 public static PVector incenter(PVector[] p){
  float L0 = PVector.sub(p[1],p[2]).mag();
  float L1 = PVector.sub(p[0],p[2]).mag();
  float L2 = PVector.sub(p[0],p[1]).mag();
  PVector c = new PVector();
  c.add(PVector.mult(p[0],L0/(L0+L1+L2)));
  c.add(PVector.mult(p[1],L1/(L0+L1+L2)));
  c.add(PVector.mult(p[2],L2/(L0+L1+L2)));
  return c;
 }
 public static PVector circumcenter(PVector[] p){
  PVector p1 = p[0];
  PVector p2=  p[1];
  PVector p3 = p[2];
  float D = 2*PVector.sub(p1,p2).cross(PVector.sub(p2,p3)).magSq();
  float A = PVector.sub(p2,p3).magSq()*PVector.sub(p1,p2).dot(PVector.sub(p1,p3));
  float B = PVector.sub(p1,p3).magSq()*PVector.sub(p2,p1).dot(PVector.sub(p2,p3));
  float C = PVector.sub(p1,p2).magSq()*PVector.sub(p3,p1).dot(PVector.sub(p3,p2));
  //PVector T1 = PVector.sub(PVector.mult(b,a.magSq()),PVector.mult(a,b.magSq()));
  //PVector T2 = a.cross(b);
  if(D == 0){
   System.out.println("D=0 !!");
   return incenter(p);
  }else{
   PVector center = new PVector();
   center.add(PVector.mult(p1,A/D));
   center.add(PVector.mult(p2,B/D));
   center.add(PVector.mult(p3,C/D));
   //c.add(PVector.div(T1.cross(T2),2*T2.magSq()));
   return center;
  }
 }
 public static PVector centroid(ArrayList<PVector> p){
  PVector c = new PVector();
  for(int i=0;i<p.size();i++){
   c.add(p.get(i));
  }
  c.div((float)p.size());
  return c;
 }
 public static float volume(ArrayList<PVector> v){
  if(v.size() == 1){
   return 1;
  }else if(v.size() == 2){
   return v.get(0).dist(v.get(1));
  }else if(v.size() == 3){
   PVector w1 = PVector.sub(v.get(1), v.get(0));
   PVector w2 = PVector.sub(v.get(2),v.get(0));
   return w1.cross(w2).mag()/2.0f;
  }else if(v.size() == 4){
   PVector w1 = PVector.sub(v.get(1), v.get(0));
   PVector w2 = PVector.sub(v.get(2),v.get(0));
   PVector w3 = PVector.sub(v.get(3),v.get(0));
   return determinant3x3(new float[][]{{w1.x,w2.x,w3.x},
                                       {w1.y,w2.y,w3.y},
                                       {w1.z,w2.z,w3.z}});
  }else{
   return 0;
  }
 }
 public static ArrayList<PVector> sortPoints(ArrayList<PVector> p, PVector normal, PVector center){
  ArrayList<PVector> tempPoints = new ArrayList<PVector>();
  int firstIndex = -1;
  float minY = 100000;
  for(int i=0;i<p.size();i++){
   tempPoints.add(p.get(i));
   if(minY>p.get(i).y){
    minY = p.get(i).y;
    firstIndex = i;
   }
  }
  ArrayList<PVector> sortedPoints = new ArrayList<PVector>();
  PVector temp = tempPoints.get(firstIndex);
  PVector first = tempPoints.get(0);
  tempPoints.set(firstIndex, first);
  tempPoints.set(0,temp);
  PVector firstVertex = tempPoints.remove(0);
  sortedPoints.add(firstVertex);
  //System.out.println("before sorting: "+angleString);
  while(!tempPoints.isEmpty()){
   double minAngle = 10000.0d;
   int minAngleIndex = -1;
   for(int i=0;i<tempPoints.size();i++){
    double angle = angleBetweenVectors(center,normal,firstVertex,tempPoints.get(i));
    if(angle <minAngle){
     minAngle = angle;
     minAngleIndex = i;
    }
   }
   if(minAngleIndex != -1){
    sortedPoints.add(tempPoints.remove(minAngleIndex));
   }
  }
  //System.out.println("after sorting: "+angleString);
  //return p;
  return sortedPoints;
 }
 public static double angleBetweenVectors(PVector pivot, PVector normal, PVector A, PVector B){
  PVector p = PVector.sub(A,pivot);
  PVector q = PVector.sub(B,pivot);
  float dot = p.dot(q);
  float area = p.cross(q).mag();
  int sign = normal.dot(p.cross(q))>=0 ? 1: -1;
  if(area == 0 && dot == 0){
   return 0;
  }else if(area == 0 && dot !=0){
   if(dot>0){
    return 0;
   }else{
    return Math.PI;
   }
  }else if(area!= 0 && dot == 0){
   if(area>=0){
    return 0.5d*Math.PI;
   }else{
    return 1.5d*Math.PI;
   }
  }else{
   float cos = dot/ (p.mag()*q.mag());
   float sin = area / (p.mag()*q.mag());
   if(Math.abs(cos)>1 || Math.abs(sin)>1){
    if(cos>1){
     cos = 1;
    }else{
     cos = -1;
    }
    if(sin>1){
     sin = 1;
    }else{
     sin = -1;
    }
   }
   double cosAngle = Math.acos(cos);
   double sinAngle = Math.asin(sin);
   if(sign>0){ //first and second quadrant
    if(cos>=0){
     //first quadrant: sinAngle = cosAngle
     //println("first quadrant: cos: "+degrees(cosAngle)+", sin: "+degrees(sinAngle)+ " real angle: "+degrees(sinAngle));
     return cosAngle;
    }else{
     //second quadrant: cosAngle = PI-sinAngle
     //println("second quadrant: cos: "+degrees(cosAngle)+", sin: "+degrees(sinAngle)+ " real angle: "+degrees(cosAngle));
     return cosAngle;
    }
   }else{//third and fourth quadrants
    if(cos<=0){ 
     //third quadrant
     //println("third quadrant: cos: "+degrees(cosAngle)+", sin: "+degrees(sinAngle)+ " real angle: "+degrees(PI+sinAngle));
     return Math.PI+sinAngle;
    }else{
     //fourth quadrant
     //println("fourth quadrant: cos: "+degrees(cosAngle)+", sin: "+degrees(sinAngle)+ " real angle: "+degrees(TWO_PI-sinAngle));
     return 2*Math.PI-sinAngle;
    }
   }
  }
 }
 public static float areaBetweenVectors(PVector pivot,PVector normal, PVector A, PVector B){
  PVector p = PVector.sub(A, pivot);
  PVector q = PVector.sub(B, pivot);
  PVector cross = p.cross(q);
  float area = cross.mag()/2.0f;
  return signBetweenVectors(cross,normal)*area;
 }
 public static int signBetweenVectors(PVector A, PVector B){
  return A.dot(B)>=0? 1: -1;
 }
 public static float surfaceArea(ArrayList<PVector> v){
  float totalArea = 0;
  PVector c = v.get(0);
  for(int i=1;i<v.size();i++){
   PVector v1 = v.get(i);
   PVector v2 = null;
   if(i<v.size()-1){
    v2 = v.get(i+1);
   }else{
    v2 = v.get(1);
   }
   PVector d1 = PVector.sub(c,v1);
   PVector d2 = PVector.sub(c,v2);
   totalArea += d1.cross(d2).mag()/2.0f;
  }
  return totalArea;
 }
 public static float cellVolume(ArrayList<PVector> v){
  return 1;
 }
 public static float determinant2x2(float[][] A){
  return A[0][0]*A[1][1]-A[0][1]*A[1][0];
 }
 public static float determinant3x3(float[][] A){
  return A[0][0]*determinant2x2(new float[][]{{A[1][1],A[2][1]},
                                              {A[1][2],A[2][2]}})
        -A[1][0]*determinant2x2(new float[][]{{A[0][1],A[2][1]},
                                              {A[0][2],A[2][2]}})
        +A[2][0]*determinant2x2(new float[][]{{A[0][1],A[1][1]},
                                              {A[0][2],A[1][2]}});  
 }
}
