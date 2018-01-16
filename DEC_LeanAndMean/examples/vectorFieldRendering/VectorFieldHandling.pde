public class SolenoidVectorField extends ContinuousVectorField{
 public SolenoidVectorField(){
  super();
 }
 public Double P(float x, float y, float z){
  return new Double(y);
 }
 public Double Q(float x, float y, float z){
  return new Double(-x);
 }
 public Double R(float x,float y, float z){
  return new Double(0);
 }
}
