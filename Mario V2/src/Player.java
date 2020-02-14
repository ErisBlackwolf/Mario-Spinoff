import java.awt.Color;

public class Player extends Particle {
	private int jumps = 1;
	private boolean leftWJ = false, rightWJ = false, touchUpDown = false;

	public Player(Vector position, Vector size, Vector velocity, float mass, float magnetic_str, float mag_radius,
			boolean movable, Color color) {
		super(position, size, velocity, mass, magnetic_str, mag_radius, movable, color);
		// TODO Auto-generated constructor stub
	}
	
	public void upKey() {
		//System.out.println("w");//Debugging
		if(jumps > 0) {
			if(touchUpDown == true) 
				fvelocity.add(new Vector(0, -16));
			else {
				jumps--;
				fvelocity.add(new Vector(0, -10));//Double Jumps are weaker
			}
			rightWJ = false;
			leftWJ = false;
		}
	}
	public void downKey() {
		fvelocity.add(new Vector(0, 2));
	}
	public void rightKey(float speed) {
		if(leftWJ == true) {
			fvelocity.setComponents(20, -8);//Adjust Wall Jump
			leftWJ = false;
		}else {
			fvelocity.add(new Vector(speed, 0));
		}
	}
	public void leftKey(float speed) {
		if(rightWJ == true) {
			fvelocity.setComponents(-20, -8);//Adjust Wall Jump
			rightWJ = false;
		}else {
			fvelocity.subtract(new Vector(speed, 0));
		}
	}
	public void upLeftKey() {
		DoRetrograde();
	}
	public void upRightKey() {
		DoPrograde();
	}
	/*
	public void positionResetKey() {
		fvelocity.setComponents(ovx, ovy);
		fposition.setComponents(ox, oy);
	}
	public void dataKey() {
		System.out.println(color.toString());
		System.out.print(" x == " +x+ ", y == " +y);
		System.out.print(" vx == " +vx+ ", vy == " +vy);
		System.out.println();
	}
	*/
	public float getX() {
		return position.x;
	}
	public float getY() {
		return position.y;
	}
	public float getW() {
		return size.x;
	}
	public float getH() {
		return size.y;
	}
	
	public void testFriction() {
		
		if(velocity.x != 0 && touchUpDown)//If on Ground
			fvelocity.subtract(velocity.setY(0).multiply(0.25f));//ensure this works
		else if(Math.abs(velocity.getX()) > 12)//If in Air
			fvelocity.subtract(velocity.setY(0).multiply(0.25f));//ensure this works
		if(Math.abs(velocity.getX()) < 0.01)//If vx is super low but not 0
			fvelocity.setX(0);
		
		if(Math.abs(velocity.getY()) > 20)
			fvelocity.multiply(0.75f);//So I don't clip through floor
	}
	public void doRectangleCollisionP2(Particle[] objects) {
		int numUDCollisions = 0;//for the jump = false if in air => I solved the problem
		int numLRCollisions = 0;
		
		for( Particle p : objects) {
			if(p == this)
				continue;
			
			Vector dist = p.position.subtract(position);
			Vector bounds = p.size.add(size).multiply(0.5f);
			
			if(dist.x <= bounds.x && dist.y <= bounds.y) {
				Vector gap = dist.abs().subtract(bounds);
				
				if(gap.x < gap.y) {
					fvelocity.setX(0);
					numLRCollisions++;
					if(position.x < p.position.x)
						fposition.add(new Vector(gap.x - size.x / 2, 0));
					else
						fposition.add(new Vector(gap.x + size.x / 2, 0));
				}else {
					fvelocity.setY(0);
					numUDCollisions++;
					if(position.y < p.position.y)
						fposition.add(new Vector(0, gap.y - size.y / 2));
					else
						fposition.add(new Vector(0, gap.y + size.y / 2));
				}
			}
		}
		if(numLRCollisions == 0) {
			leftWJ = false;
			rightWJ = false;
			//touchLeftRight = false;
		}
		if(numUDCollisions == 0) {
			touchUpDown = false;
		}
	}
}