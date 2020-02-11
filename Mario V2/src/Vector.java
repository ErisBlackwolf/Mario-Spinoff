//Mario V2
public class Vector {
	
	private double x,y;
	
	public Vector( double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getComponent(int i) {
		if( i == 0)
			return x;
		else if( i == 1)
			return y;
		else
			return Math.sqrt(x*x + y*y);
	}
	
	public double getResultant() {
		return Math.sqrt(x*x + y*y);
	}
	
	public void setComponent(int i, double component) {
		if( i == 0)
			x = component;
		else
			y = component;
	}
	
	public void setComponents(double nx, double ny) {//nx for 'new x' ny for 'new y'
		x = nx;
		y = ny;
	}
	
	public double getDifference(Vector loc1, int i) {//to be used with location0, location0 other Particle
		if( i == 0)
			return loc1.x - x;
		else if( i == 1)
			return loc1.y - y;
		else
			return Math.sqrt((loc1.x - x)*(loc1.x - x) + (loc1.y - y)*(loc1.y - y));
	}
	
	public void multiplyVectorBySingleNumber(int component, double multiplier) {
		if(component == 0)
			x = x*multiplier;
		else if(component == 1)
			y = y*multiplier;
		else {
			x = x*multiplier;
			y = y*multiplier;
		}
	}
	
	public void copyVector(Vector v) {
		x = v.x;
		y = v.y;
	}
	
	public void addVectors(Vector v1) {//Only changes the Vector that calls this method
		x = x + v1.x;
		y = y + v1.y;
	}
	
	public void subtractVectors(Vector v1) {//Only changes the Vector that calls this method
		x = x - v1.x;
		y = y - v1.y;
	}
	
	public void combineTwoVectors(Vector v1, Vector v2) {
		x = v1.x + v2.x;
		y = v1.y + v2.y;
	}
	
	public void addToVector( double x, double y) {
		this.x = this.x + x;
		this.y = this.y + y;
	}
	
}
