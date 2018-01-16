class ScalarField extends ScalarFunction{
 public ScalarField(){
  super();
 }
 public double function(float x, float y, float z){
  return Math.pow(x,2)+Math.pow(z,2)-y;
 }
}
class ContinousLaplacian extends ScalarFunction{
 public ContinousLaplacian(){
  super();
 }
 public double function(float x, float y, float z){
  return 4;
 }
}
