import java.awt.Color;

public class Player extends Particle {
	private int jumps;
	private boolean leftWJ = false, rightWJ = false;

	public Player(Vector position, Vector size, Vector velocity, float mass, float magnetic_str, float mag_radius,
			boolean movable, Color color) {
		super(position, size, velocity, mass, magnetic_str, mag_radius, movable, color);
		// TODO Auto-generated constructor stub
	}
	
	public void upKey() {
		if(jumps > 0) {
			fvelocity.addToVector( 0, -16);
			jumps--;
			rightWJ = false;
			leftWJ = false;
		}
	}
	public void downKey() {
		fvelocity.addToVector( 0, 2);//Examine this Code
	}
	public void rightKey(float speed) {
		if(jump == false && leftWJ == true) {
			fvelocity.setComponents(20, -8);//Adjust Wall Jump
			leftWJ = false;
		}
		else
			fvelocity.addToVector(speed, 0);
	}
	public void leftKey(float speed) {
		if(jump == false && rightWJ == true) {
			fvelocity.setComponents(-20, -8);//Adjust Wall Jump
			rightWJ = false;
		}
		else
			fvelocity.addToVector( -speed, 0);
	}
	public void upLeftKey() {
		DoRetrograde();
	}
	public void upRightKey() {
		DoPrograde();
	}
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
	
	public void testFriction() {
		
		if(velocity.x != 0 && jumps == 2)//If on Ground
			fvelocity.multiplyVectorBySingleNumber(0, 0.75);
		else if(Math.abs(velocity.getComponent(0)) > 12)//If in Air
			fvelocity.multiplyVectorBySingleNumber(0, 0.75);
		if(Math.abs(velocity.getComponent(0)) < 0.01)//If vx is super low but not 0
			fvelocity.setComponent(0, 0);
		
		if(Math.abs(velocity.getComponent(1)) > 20)
			fvelocity.multiply(0.75f);//So I don't clip through floor
	}
	private int score = 0;//for the score
	public void gameRules() {
		//Detect out of bounds
		if( x < -150 || x > 1350) {
			magnetic_str += 20;//Different Rewards for off the side?
			positionResetKey();
			score -= 1;
			System.out.print(color.toString() + " score == " +score);
			System.out.println();
		}
		if( y < -150 || y > 930) {//150 off camera allowed
			magnetic_str += 20;//Different Rewards for off the top/bottom? Nah
			positionResetKey();
			score -= 1;
			System.out.print(color.toString() + " score == " +score);
			System.out.println();		}
	}
}