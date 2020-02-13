import java.awt.Color;

public class Particle {
	
	/**
	 * the gravitational constant
	 */
	public static final int G = 10;
	
	// variables
	protected Vector position, size, velocity, fposition, fvelocity, force;//now protected, was private
	protected float mass, magnetic_str, mag_radius;//Examine to see if setting to private has value
	protected boolean movable;
	
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
		//int numCollisions = 0;//for the jump = false if in air => I solved the problem
		//May need to move into Player class for int numUPDown, numLeftRight;
		for( Particle p : objects) {
			if(p == this)
				continue;
			
			Vector dist = p.position.subtract(position);
			Vector bounds = p.size.add(size).multiply(0.5f);
			
			if(dist.x <= bounds.x && dist.y <= bounds.y) {
				Vector gap = dist.abs().subtract(bounds);
				
				if(gap.x < gap.y) {
					fvelocity.setX(0);
					if(position.x < p.position.x)
						fposition.add(new Vector(gap.x - size.x / 2, 0));
					else
						fposition.add(new Vector(gap.x + size.x / 2, 0));
				}else {
					fvelocity.setY(0);
					if(position.y < p.position.y)
						fposition.add(new Vector(0, gap.y - size.y / 2));
					else
						fposition.add(new Vector(0, gap.y + size.y / 2));
				}
			}
		}
	}
	
	public void bulletFiredV2(Particle shooter, Particle target) {//Used by Bullet Particle
		Vector direction = target.position.subtractThis(shooter.position).normalize();
		fposition = direction.multiply(mag_radius);
		fvelocity = direction.multiply(40f);
		
		if(direction.abs().getX() > direction.abs().getY())
			size.setComponents(15,5);
		else
			size.setComponents(5,15);
		
	}
	public void bulletFiredV3(Particle shooter, boolean direction) {
		if(direction) {//Shooter Aiming Right
			fposition.setComponents(0, mag_radius).add(shooter.position);
			fvelocity.setComponents(15,0).add(shooter.velocity.multiply(1/4f));//Remove shooter.velocity part?
		}else {//Shooter Aiming Left
			fposition.setComponents(0, -mag_radius).subtract(shooter.position);
			fvelocity.setComponents(-15,0).subtract(shooter.velocity.multiply(1/4f));
		}
	}
	
	public float cameraFocus(float ofsx) {
		if(position.getX() < 0 - ofsx) 
			return 1200;//1200
		if(position.getX() > 1200 - ofsx) 
			return -1200;//-1200
		return 0;
	}
	
	public void updatePreparing() {
		fvelocity.addThis(force.multiply((1.0f/mass)));
		force.setComponents(0, 0);//Reset for next round.
		if(movable) 
			fposition.add(fvelocity);
		position = fposition;
		velocity = fvelocity;
	}
	
	public void predictionPosition(Particle object, float m) {
		fposition = object.position.clone();//Ask Jake about .clone();
		fposition.addThis(object.velocity.multiply(m));
	}

	public Color getColor() {
		return color;
	}

	public void setColor( Color color) {//Don't really need to set Color I think
		this.color = color;
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
		fvelocity.add(velocity.normalizeThis().multiply(5));
	}
	public void DoRetrograde() {
		if(velocity.getMagnitude() < 5)//Prevents from dividing by 0
			fvelocity.multiply(0);
		fvelocity.subtract(velocity.normalizeThis().multiply(5));
	}
	
	
	
	public void testMagnet_Two(Particle[] objects, int reverse) {
		for( Particle p : objects) {
			if( p == this)//Add in ||!p.magnetic or something
				continue;
			//Magnetism with a set Radius. (r^2 - loc0^2)^0.5 method
			Vector tDisp = p.position.subtract(position);
			if(tDisp.getMagnitude() >= mag_radius)//Meant to not divide by 0
				continue;
			//tDisp.getMagnitude() should be squared here
			float forceSize = (float) Math.pow(mag_radius*mag_radius - tDisp.getMagnitude()*tDisp.getMagnitude(), 0.5);
			tDisp.normalizeThis().multiply(forceSize * magnetic_str / 50);//name of vector is misleading
		
			if( reverse == 1)//repel
				p.force.subtract(tDisp);
			else //attract
				p.force.add(tDisp);
		}
	}
	
	public void testGravity(Particle[] objects) {
		for( Particle p : objects) {
			if( p == this)//Add in ||!p.gravity or something
				continue;
			Vector tDisp = p.position.subtract(position);
			float forceSize = (p.mass * mass * G)/(tDisp.getMagnitude()*tDisp.getMagnitude());
			p.force.addThis(tDisp.normalizeThis().multiply(-forceSize));//Make sure this attracts, not repels
		}
	}
}