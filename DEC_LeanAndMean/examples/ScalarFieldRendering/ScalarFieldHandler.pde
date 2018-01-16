class sineScalarField extends ScalarFunction{
 public sineScalarField(){
  super();
 }
 public double function(float x, float y, float z){
  return Math.sin(Math.PI*x)*Math.sin(2*Math.PI*y)*Math.sin(3*PI*z);
 }
}
