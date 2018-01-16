PVector rodriguesRotation(PVector vector, PVector axis, float angle){
 PVector u0 = PVector.mult(vector,cos(angle));
 PVector u1 = PVector.mult(axis.cross(vector),sin(angle));
 PVector u2 = PVector.mult(axis,axis.dot(vector)*(1-cos(angle)));
 PVector rotated = new PVector();
 rotated.add(u0);
 rotated.add(u1);
 rotated.add(u2);
 return rotated;
}
