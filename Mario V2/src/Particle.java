import java.awt.Color;

public class Particle {
	
	public static final int G = 10;
	
	// variables
	protected Vector position, size, velocity, fposition, fvelocity, force = new Vector(0,0);
	protected float mass, magnetic_str, mag_radius;//Examine to see if setting to private has value
	protected boolean movable, touchUpDown = false, touchLeft = false, touchRight = false;
	
	protected Color color;
	
	public Particle(Vector position, Vector size, Vector velocity, float mass, float magnetic_str, float mag_radius, boolean movable, Color color) {
		this.movable = movable;
		
		this.position = position;
		this.fposition = position.clone();
		
		this.velocity = velocity;
		this.fvelocity = velocity.clone();
		
		
		this.size = size;
		
		this.mass = mass;
		this.magnetic_str = magnetic_str;
		this.mag_radius = mag_radius;
		
		this.color = color;
	}
	
	public boolean isColliding(Particle p) {
		Vector dist = p.position.subtract(position).abs();
		Vector bounds = p.size.add(size).multiply(0.5f);
		
		if(dist.x <= bounds.x && dist.y <= bounds.y)
			return true;
		return false;
	}
	
	public void doRectangleCollision(Particle[] objects) {
		int numUDCollisions = 0;//for the jump = false if in air => I solved the problem
		int numLCollisions = 0;
		int numRCollisions = 0;
		
		for( Particle p : objects) {
			if(p == this)
				continue;
			Vector dist = p.position.subtract(position).abs();
			Vector bounds = p.size.add(size).multiply(0.5f);
			if(dist.x <= bounds.x && dist.y <= bounds.y) {
				Vector gap = dist.abs().subtract(bounds);//dis.abs() is not nessecary?
				touchUpDown = true;
				touchLeft = true;
				touchRight = true;
				
				if(gap.x > gap.y) {//Left/Right
					
					if(position.x < p.position.x) {
						fposition.addThis(new Vector(gap.x, 0));//Moves this Particle out of object
						if(fvelocity.x > 0)
							fvelocity.setX(0);
						numRCollisions++;
					}else{
						fposition.subtractThis(new Vector(gap.x, 0));
						if(fvelocity.x < 0)
							fvelocity.setX(0);
						numLCollisions++;
					}
				}
				if(gap.x < gap.y) {//Up/Down
					
					numUDCollisions++;
					if(position.y < p.position.y) {
						fposition.addThis(new Vector(0, gap.y));
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
		
		if(numLCollisions == 0) 
			touchLeft = false;
		
		if(numRCollisions == 0) 
			touchRight = false;
		
		if(numUDCollisions == 0) 
			touchUpDown = false;
	}
	
	public void doRectangleCollisionQS(Particle[] objects) {//Quarter Step
		int numUDCollisions = 0;//for the jump = false if in air => I solved the problem
		int numLCollisions = 0;
		int numRCollisions = 0;
		
		for( Particle p : objects) {
			if(p == this)
				continue;
			
			Vector bounds = p.size.add(size).multiply(0.5f);
			for(int step = 1; step < 5; step++) {
			
				Vector dist = p.position.subtract(position.add(velocity.multiply(0.25f*step))).abs();
				
				if(dist.x <= bounds.x && dist.y <= bounds.y) {
					//.abs() is screwing me on this. Don't know whether to add or subtract velocity
					//Maybe just remake gap without dist
					Vector gap = p.position.subtract(position).abs().subtract(bounds);
					touchUpDown = true;
					touchLeft = true;
					touchRight = true;
					step = 5;//End Quarter Step Checking
					
					if(gap.x > gap.y) {//Left/Right
						
						if(position.x < p.position.x) {
							fposition.addThis(new Vector(gap.x, 0));//Moves this Particle out of object
							if(fvelocity.x > 0)
								fvelocity.setX(0);
							numRCollisions++;
						}else{
							fposition.subtractThis(new Vector(gap.x, 0));
							if(fvelocity.x < 0)
								fvelocity.setX(0);
							numLCollisions++;
						}
					}
					if(gap.x < gap.y) {//Up/Down
						
						numUDCollisions++;
						if(position.y < p.position.y) {
							fposition.addThis(new Vector(0, gap.y));
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
		
		if(numLCollisions == 0) 
			touchLeft = false;
		
		if(numRCollisions == 0) 
			touchRight = false;
		
		if(numUDCollisions == 0) 
			touchUpDown = false;
	}
	
	public float cameraFocus(float ofsx) {
		if(position.getX() < 0 - ofsx) 
			return 1200;//1200
		if(position.getX() > 1200 - ofsx) 
			return -1200;//-1200
		return 0;
	}
	
	public void updatePreparing() {
		fvelocity.addThis(force.multiply(1/mass));
		force.multiplyThis(0);//Reset for next round. Just Add to Force, don't set its components
		if(movable) 
			fposition.addThis(fvelocity);
		position = fposition.clone();
		velocity = fvelocity.clone();
	}
	
	public void predictPosition(Particle object, float m) {//Ask Jake about .clone();. He says Yes
		fposition = object.position.clone().add(object.velocity.multiply(m));
		//Next line takes force into account
		//fposition = object.fposition.clone().add(object.fvelocity.add(object.force.multiply(1/object.mass)));
	}
	
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
	
	public Color getColor() {
		return color;
	}

	public void setColor( Color color) {//Don't really need to set Color I think
		this.color = color;
	}

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
	
	public float getM() {
		return mass;
	}

	public void setM( float mass) {
		this.mass = mass;
	}
	
	public float getMS() {
		return magnetic_str;
	}

	public void setMS( float magnetic_str) {
		this.magnetic_str = magnetic_str;
	}
	public float getMR() {
		return mag_radius;
	}

	public void setMR( float mag_radius) {
		this.mag_radius = mag_radius;
	}

	public void DoPrograde() {
		if(velocity.getMagnitude() == 0)//Prevents from dividing by 0
			return;
		fvelocity.addThis(velocity.normalizeThis().multiply(5));
	}
	public void DoRetrograde() {
		if(velocity.getMagnitude() < 5)//Prevents from dividing by 0
			fvelocity.multiplyThis(0);
		fvelocity.subtractThis(velocity.normalizeThis().multiply(5));
	}
	
	public void testMagnet_Two(Particle[] objects, boolean repel) {
		for( Particle p : objects) {
			if( p == this || mag_radius == 0)//Add in ||!p.magnetic or something
				continue;
			//Magnetism with a set Radius. (r^2 - loc0^2)^0.5 method
			Vector disp = p.position.subtract(position);
			if(disp.getMagnitude() >= mag_radius)//Meant to not divide by 0
				continue;
			
			float forceSize = (float) Math.pow(mag_radius*mag_radius - disp.getMagnitude()*disp.getMagnitude(), 0.5);
			Vector mForce = disp.normalize().multiply(forceSize * magnetic_str / 50);
			
			if(repel)
				p.force.addThis(mForce);//Repels
			else
				p.force.subtractThis(mForce);//Attracts
			
			//System.out.println(mForce.getMagnitude());
			//System.out.println(forceSize);
		}
	}
	
	public void testFriction() {//Fix
		if(velocity.x != 0 && touchUpDown)//If on Ground
			fvelocity.setX(fvelocity.getX() * 0.8f);
		if(Math.abs(velocity.x) > 12 && !touchUpDown)//If in Air
			fvelocity.setX(fvelocity.getX() * 0.9f);
		
		//if(Math.abs(velocity.x) < 0.01)//If vx is super low but not 0
		//	fvelocity.x = 0;//setX(0);
		
		if(touchLeft || touchRight)//Wall Slide
			fvelocity.setY(fvelocity.getY() / 2);
	}
	
	public void gravityI() {//II is for one version number
		force.addThis(new Vector(0,mass));
	}
}