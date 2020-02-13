
public class Vector {
	protected float x, y;
	
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector setX(float x) {
		this.x = x;
		return this;
	}
	
	public Vector setY(float y) {
		this.y = y;
		return this;
	}
	
	public Vector setComponents(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getMagnitude() {
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public Vector normalize() {
		return this.multiply( 1 / this.getMagnitude() );
	}
	
	public Vector negate() {
		return this.multiply(-1);
	}
	
	public Vector abs() {
		return new Vector(Math.abs(x), Math.abs(y));
	}
	
	public Vector add(Vector v) {
		return new Vector(x + v.x, y + v.y);
	}
	
	public Vector subtract(Vector v) {
		return new Vector(x - v.x, y - v.y);
	}
	
	public Vector multiply(float d) {
		return new Vector(x * d, y * d);
	}
	
	public Vector rotate(float a) {
		return new Vector( (float) (x * Math.cos(a) - y * Math.sin(a)), (float) (x * Math.sin(a) + y * Math.cos(a)) );
	}
	
	public float dotProduct(Vector v) {
		return x * v.x + y * v.y;
	}
	
	public float angle() {
		if(x < 0)
			return (float) (Math.atan( y / x ) + Math.PI);
		if(x > 0 && y > 0)
			return (float) Math.atan( y / x );
		else
			return (float) (Math.atan( y / x ) + 2 * Math.PI);
	}
	
	public Vector normalizeThis() {
		return this.multiplyThis( 1 / this.getMagnitude() );
	}
	
	public Vector negateThis() {
		return this.multiplyThis(-1);
	}
	
	public Vector addThis(Vector v) {
		x += v.x;
		y += v.y;
		return this;
	}
	
	public Vector subtractThis(Vector v) {
		x -= v.x;
		y -= v.y;
		return this;
	}
	
	public Vector multiplyThis(float d) {
		x *= d;
		y *= d;
		return this;
	}
	
	public Vector rotateThis(float a) {
		float px = (float) (x * Math.cos(a) - y * Math.sin(a));
		y = (float) (x * Math.sin(a) + y * Math.cos(a));
		x = px;
		return this;
	}
	
	@Override
	public Vector clone() {
		return new Vector(x, y);
	}
	
	@Override
	public String toString() {
		return String.format("<%d, %d>", x, y);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		
		if(o instanceof Vector) {
			Vector v = (Vector) o;
			return v.x == x && v.y == y;
		}
		
		return false;
	}
}
