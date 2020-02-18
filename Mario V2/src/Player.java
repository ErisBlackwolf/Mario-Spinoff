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
				fvelocity.addThis(new Vector(0, -25));//-16
			else {
				jumps--;
				fvelocity.setY(-15);//Adjust Double Jump Mechanics//-10
				//fvelocity.addThis(new Vector(0, -10));//Double Jumps are weaker
			}
			rightWJ = false;
			leftWJ = false;
		}
	}
	public void downKey() {
		fvelocity.addThis(new Vector(0, 2));
	}
	public void rightKey(float speed) {
		if(leftWJ == true) {
			fvelocity.setComponents(25, -20);//Adjust Wall Jump//(20,-8)
			leftWJ = false;
		}else {
			fvelocity.addThis(new Vector(speed, 0));
		}
	}
	public void leftKey(float speed) {
		if(rightWJ == true) {
			fvelocity.setComponents(-25, -20);//Adjust Wall Jump
			rightWJ = false;
		}else {
			fvelocity.subtractThis(new Vector(speed, 0));
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
	
	public void testFriction() {
		if(velocity.x != 0 && touchUpDown)//If on Ground
			fvelocity.subtractThis(velocity.setY(0).multiply(0.25f));//ensure this works
		else if(Math.abs(velocity.getX()) > 12)//If in Air
			fvelocity.subtractThis(velocity.setY(0).multiply(0.25f));//ensure this works
		if(Math.abs(velocity.getX()) < 0.01)//If vx is super low but not 0
			fvelocity.setX(0);
		/*
		if(Math.abs(velocity.getY()) > 20 && !touchUpDown)//Is !touchUpDown needed?
			fvelocity.multiply(0.75f);//So I don't clip through floor
		*/
	}
	
	public void gravityI() {//I is for one Dimensional (Roman Numerals)
		if(touchUpDown) {//Will allow Ceiling Clinging; Maybe Add in return if affected by Magnetism
			force.addThis(new Vector(0,mass));
			return;
		}
		if(velocity.getY() < 0)//Player Jumping Up
			force.addThis(new Vector(0,mass*5));
		
		if(velocity.getY() >= 0 && velocity.getY() < 15) {// >= is needed
			//This math is fucking sketchy but it kinda works
			force.addThis(new Vector(0, (float) Math.pow(15 - velocity.getY(), 0.65)*mass));//Will also reduce speed
		}
	}//(10 - velocity.getY())*mass
	
	public boolean rectTest(Particle[] objects) {
		for( Particle p : objects) {
			if(p == this)
				continue;
			
			Vector dist = p.position.subtract(position).abs();
			Vector bounds = p.size.add(size).multiply(0.5f);
			
			if(dist.x <= bounds.x && dist.y <= bounds.y)
				return true;
		}
		return false;
	}
	
	
	public void doRectangleCollisionP2(Particle[] objects) {
		int numUDCollisions = 0;//for the jump = false if in air => I solved the problem
		int numLRCollisions = 0;
		
		for( Particle p : objects) {
			if(p == this)
				continue;
			Vector dist = p.position.subtract(position).abs();
			Vector bounds = p.size.add(size).multiply(0.5f);
			if(dist.x <= bounds.x && dist.y <= bounds.y) {
				Vector gap = dist.abs().subtract(bounds);
				touchUpDown = true;
				
				if(gap.x > gap.y) {//Left/Right
					fvelocity.setX(0);
					numLRCollisions++;
					if(position.x < p.position.x) {
						fposition.addThis(new Vector(gap.x, 0));//Works
						rightWJ = true;
					}else{
						fposition.subtractThis(new Vector(gap.x, 0));
						leftWJ = true;
					}
				}
				if(gap.x < gap.y) {//Up/Down
					fvelocity.setY(0);
					numUDCollisions++;
					if(position.y < p.position.y) {
						fposition.addThis(new Vector(0, gap.y));//Works
						jumps = 1;
					}else
						fposition.subtractThis(new Vector(0, gap.y));
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