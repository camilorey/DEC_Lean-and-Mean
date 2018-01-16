class ScalarField extends ScalarFunction{
 public ScalarField(){
  super();
 }
 public double function(float x, float y, float z){
  return Math.pow(x,2)+Math.pow(z,2)-y;
 }
}
class continousLaplacian extends ScalarFunction{
 public continousLaplacian(){
  super();
 }
 public double function(float x, float y, float z){
  return 4;
 }
}
