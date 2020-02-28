import java.awt.Color;
//import java.util.ArrayList;

public class Player extends Particle {
	private int jumps = 1;
	private boolean toggleUp = false;

	public Player(Vector position, Vector size, Vector velocity, float mass, float magnetic_str, float mag_radius,
			boolean movable, Color color) {
		super(position, size, velocity, mass, magnetic_str, mag_radius, movable, color);
		// TODO Auto-generated constructor stub
	}
	//Some Experimental set (no get) Methods
	public void setToggleUp(boolean tU) {
		toggleUp = tU;
	}
	public void setJumps(int j) {
		jumps = j;
	}
	
	public void upKeyToggle() {
		//Change Here
		if(toggleUp) {
			toggleUp = false;
			if(touchUpDown == true) {
				jumps = 1;
				fvelocity.addThis(new Vector(0, -16));//-16
			}else if(jumps > 0){
				jumps--;
				fvelocity.setY(-10);
			}
		}
	}
	
	public void upKey() {
		//System.out.println("w");//Debugging
		if(touchUpDown == true) {
			jumps = 1;
			fvelocity.addThis(new Vector(0, -16));//-16
		}else if(jumps > 0){
			jumps--;
			fvelocity.setY(-10);
		}
	}
	public void downKey() {
		fvelocity.addThis(new Vector(0, 2));
	}
	public void rightKey(float speed) {
		if(touchUpDown) {//On Ground
			fvelocity.addThis(new Vector(speed, 0));
		}else if(touchLeft) {//In Air Touching Left Structure
			fvelocity.setComponents(16, -12);//Adjust Wall Jump//(20,-8)
		}else {//else if(!touchRight)? Could be next to Right Wall, but won't matter. In Air
			fvelocity.addThis(new Vector(speed/1.5f, 0));
		}
	}
	public void leftKey(float speed) {
		if(touchUpDown) {//On Ground
			fvelocity.subtractThis(new Vector(speed, 0));
		}else if(touchRight) {//In Air Touching Left Structure
			fvelocity.setComponents(-16, -12);//Adjust Wall Jump//(20,-8)
		}else {//else if(!touchRight)? Could be next to Left Wall, but won't matter. In Air
			fvelocity.subtractThis(new Vector(speed/1.5f, 0));
		}
	}
	public void upLeftKey() {
		DoRetrograde();
	}
	public void upRightKey() {
		DoPrograde();
	}
	
	public void gravityII() {
		if(touchUpDown)
			return;//Feels Cheap, but fixes predictPosition
		if(!toggleUp || touchLeft || touchRight)
			force.addThis(new Vector(0,mass*1.5f));
		else
			force.addThis(new Vector(0,mass*4f));
	}
	/*
	public void doRectangleCollisionQSBulletPush(ArrayList<Projectile> b) {//Quarter Step
		
		for( Particle p : b) {
			if(p == this)
				continue;
			
			Vector bounds = p.size.add(size).multiply(0.5f);
			for(int step = 1; step < 5; step++) {
			
				Vector dist = p.position.subtract(position.add(velocity.multiply(0.25f*step))).abs();
				
				if(dist.x <= bounds.x && dist.y <= bounds.y) {
					//.abs() is screwing me on this. Don't know whether to add or subtract velocity
					//Maybe just remake gap without dist
					Vector gap = p.position.subtract(position).abs().subtract(bounds);
					
					step = 5;//End Quarter Step Checking
					
					if(gap.x > gap.y) {//Left/Right
						if(position.x < p.position.x) {
							fposition.addThis(new Vector(gap.x, 0));//Moves this Particle out of object
							if(fvelocity.x > 0)
								fvelocity.setX(0);
						}else{
							fposition.subtractThis(new Vector(gap.x, 0));
							if(fvelocity.x < 0)
								fvelocity.setX(0);
						}
					}
					if(gap.x < gap.y) {//Up/Down
						if(position.y < p.position.y) {
							fposition.addThis(new Vector(0, gap.y));
							jumps = 1;//Changed Here; Cheap Solution
							if(fvelocity.y > 0)
								fvelocity.setY(0);
						}else {
							fposition.subtractThis(new Vector(0, gap.y));
							if(fvelocity.y < 0)
								fvelocity.setY(0);
						}
					}
				}
			}
		}
	}
	*/
}