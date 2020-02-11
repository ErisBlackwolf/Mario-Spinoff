import java.awt.Color;
//Mario V2
public class Particle {
	
	public static final int G = 10;
	
	private int loop = 0;
	
	private double x,y,w,h,vx,vy,mass,magnetic_str,mag_radius;//Double Variables that are used in constructors
	private boolean movable, gravity, magnetic;
							//Wall Jumps => left means wall is on left
	private boolean jump = false, doubleJump = false, leftWJ = false, rightWJ = false;//Mario Centric Boolean Values
	private double ox,oy,ovx,ovy;//Double Variables used to store original passed values
	
	private Color color;
	
	//Velocity Vectors					//Position Vectors		//Area Vector
	private Vector velocity0, velocity1, location0, location1, area0;
	
	//Force Vectors (Field)
	private Vector forceMagnet = new Vector(0,0), forceGravity = new Vector(0,0);
	private Vector forceFieldNet = new Vector(0,0);//Implement this and forceNormal
	
	//Net Force Vector
	private Vector forceNet = new Vector(0,0);
	
	//Placeholder Vector
	private Vector vector0 = new Vector(0,0);//Use so you don't do .setComponents(0,0) for other Vectors
	
	public Particle(double x, double y, double w, double h, double vx, double vy, double mass, double magnetic_str, double mag_radius, boolean movable, boolean gravity, boolean magnetic, Color color) {
		this.movable = movable;
		this.gravity = gravity;
		this.magnetic = magnetic;
		//Original Values
		ox = x;
		oy = y;
		ovx = vx;
		ovy = vy;
		//Area Vector (x = width, y = height)
		area0 = new Vector(w,h);
		
		this.mass = mass;
		this.magnetic_str = magnetic_str;
		this.mag_radius = mag_radius;
		this.color = color;
		
		//Velocity Vectors
		velocity0 = new Vector(vx,vy);
		velocity1 = new Vector(vx,vy);
		//Position (Location) Vectors
		location0 = new Vector(x,y);
		location1 = new Vector(x,y);
	}
	
	public boolean willCollide(Particle object, double step) {
		
		if(velocity0.getComponent(2) == 0)
			return false;
		
		vector0.setComponent(0, velocity0.getComponent(0));//Do not do vector0 = veloctiy0;
		vector0.setComponent(1, velocity0.getComponent(1));//It starts to think they are the same
		
		vector0.multiplyVectorBySingleNumber(2, step);// Half-Step. Quarter-Step is too much for Now
		vector0.addVectors(location0);//Predicts where Object will be in "Half a Frame"
				
		double xGap = Math.abs(vector0.getDifference(object.location0, 0)) - (w/2 + object.w/2);
		double yGap = Math.abs(vector0.getDifference(object.location0, 1)) - (h/2 + object.h/2);
		
		if( xGap <= 0 && yGap <= 0) //Particles are next to or inside each other
			return true;
		
		return false;
	}
	
	public boolean isColliding(Particle object) {
		
		double xGap = Math.abs(location0.getDifference(object.location0, 0)) - (w/2 + object.w/2);
		double yGap = Math.abs(location0.getDifference(object.location0, 1)) - (h/2 + object.h/2);
		if( xGap <= 0 && yGap <= 0) //Particles are next to or inside each other
			return true;
		
		return false;
	}
	public void doRectangleCollision(Particle[] objects) {
		int numCollisions = 0;//for the jump = false if in air => I solved the problem
		for( Particle p : objects) {
			if( p == this)// Method Purpose is to do Rectangle Collision Stuff
				continue;
									   // || willCollide(p, 1/2)
			if(isColliding(p)) {
				numCollisions++;
				
				double x1 = p.x - (p.w / 2), x2 = p.x + (p.w / 2);//Side X Pos of Colliding Object
				double y1 = p.y - (p.h / 2), y2 = p.y + (p.h / 2);//Side Y Pos
				
				double xGap = Math.abs(location0.getDifference(p.location0, 0)) - (w/2 + p.w/2);
				double yGap = Math.abs(location0.getDifference(p.location0, 1)) - (h/2 + p.h/2);
			
				if( xGap > yGap) {//Left/Right
					if( x < p.x) {
						location1.setComponent(0, x1 - w/2);
						rightWJ = true;
					}
					if(x > p.x) {
						location1.setComponent(0, x2 + w/2);
						leftWJ = true;
					}
					velocity1.setComponent(0, 0);
				}
				if( xGap < yGap) {//Up/Down
					if(y < p.y) {
						location1.setComponent(1, y1 - h/2);
						jump = true;
						doubleJump = true;
					}
					if(y > p.y) {
						location1.setComponent(1, y2 + h/2);
					}
					velocity1.setComponent(1, 0);
				}
			}
		}
		if(numCollisions == 0) {
			jump = false;
			leftWJ = false;
			rightWJ = false;
		}
	}
	
	public void bulletFired(Particle shooter, Particle target) {//Used by Bullet Particle
		if(shooter.x < target.x) {//Target is Right of Shooter
			location1.setComponents(shooter.x + mag_radius, shooter.y);//after + mag_radius
			velocity1.setComponents(15,0);//Bullets fired while next to wall appears at wall TOP
		}
		else {//Target is Left (Not Right) of Shooter
			location1.setComponents(shooter.x - mag_radius, shooter.y);
			velocity1.setComponents(-15,0);
		}
	}
	public void bulletFiredV2(Particle shooter, Particle target) {//Used by Bullet Particle
		double xDisp = shooter.location0.getDifference(target.location0, 0);
		double yDisp = shooter.location0.getDifference(target.location0, 1);
		double tDist = shooter.location0.getDifference(target.location0, 2);
		
		location1.setComponents(shooter.x + mag_radius*xDisp/tDist, shooter.y + mag_radius*yDisp/tDist);
		velocity1.setComponents(40*xDisp/tDist, 40*yDisp/tDist);
		
		if(Math.abs(xDisp) > Math.abs(yDisp))
			area0.setComponents(15,5);
		else
			area0.setComponents(5,15);
		
	}
	public void bulletFiredV3(Particle shooter, boolean direction) {
		if(direction) {//Shooter Aiming Right
			location1.setComponents(shooter.x + mag_radius, shooter.y);//after + mag_radius
			velocity1.setComponents(15,0);//Bullets fired while next to wall appears at wall TOP
		}
		else {//Shooter Aiming Left
			location1.setComponents(shooter.x - mag_radius, shooter.y);
			velocity1.setComponents(-15,0);
		}
	}
	
	public double cameraFocus(double ofsx) {
		if( x < 0 - ofsx) 
			return 1200;//1200
		if( x > 1200 - ofsx) 
			return -1200;//-1200
		return 0;
	}
	
	public void setNetForces() {//Will be Expanded upon when Normal Force is Added
		forceFieldNet.combineTwoVectors(forceGravity, forceMagnet);
		/*
		System.out.println("Gravity Force == " +forceGravity.getComponent(2));
		System.out.println("Net Field Force == " +forceFieldNet.getComponent(2));
		*/
		forceNet = forceFieldNet;
	}
	
	public void updatePreparing() {
		w = area0.getComponent(0);//This code is probably inefficient
		h = area0.getComponent(1);// w and h won't change that often.
		
		setNetForces();//May seem redundent but see setNetForces for more information
		forceNet.multiplyVectorBySingleNumber(2, 1.0/mass);
		velocity1.addVectors(forceNet);
		
		if(movable) {
			location1.addVectors(velocity0);
		}
		
		location0 = location1;
		x = location0.getComponent(0);
		y = location0.getComponent(1);
		velocity0 = velocity1;
		vx = velocity0.getComponent(0);//Will keep this for code simplification
		vy = velocity0.getComponent(1);//Like, vy = velocity0.getComponent(1); Its just easier
	}//					velocity0.getComponent(1); sometimes is better though
	
	
	public void positionResetKey() {
		velocity1.setComponents(ovx, ovy);
		location1.setComponents(ox, oy);
	}
	public void dataKey() {
		System.out.println(color.toString());
		System.out.print(" x == " +x+ ", y == " +y);
		System.out.print(" vx == " +vx+ ", vy == " +vy);
		System.out.println();
	}
	
	
	public void upKey() {
		if(jump) {
			velocity1.addToVector( 0, -16);
			jump = false;
			rightWJ = false;
			leftWJ = false;
		}
		else if(doubleJump) {//Relies on Toggle boolean
			doubleJump = false;
			velocity1.setComponent(1, -10);
		}
	}
	public void downKey() {
		velocity1.addToVector( 0, 2);//Examine this Code
	}
	public void rightKey(double speed) {
		if(jump == false && leftWJ == true) {
			velocity1.setComponents(20, -8);//Adjust Wall Jump
			leftWJ = false;
		}
		else
			velocity1.addToVector(speed, 0);
	}
	public void leftKey(double speed) {
		if(jump == false && rightWJ == true) {
			velocity1.setComponents(-20, -8);//Adjust Wall Jump
			rightWJ = false;
		}
		else
			velocity1.addToVector( -speed, 0);
	}
	public void upLeftKey() {
		DoRetrograde();
	}
	public void upRightKey() {
		DoPrograde();
	}
	
	public void testFriction() {
		if(velocity0.getComponent(0) != 0 && jump == true)//If on Ground
			velocity1.multiplyVectorBySingleNumber(0, 0.75);
		else if(Math.abs(velocity0.getComponent(0)) > 12)//If in Air
			velocity1.multiplyVectorBySingleNumber(0, 0.75);
		if(Math.abs(velocity0.getComponent(0)) < 0.01)//If vx is super low but not 0
			velocity1.setComponent(0, 0);
		
		if(Math.abs(velocity0.getComponent(1)) > 20)
			velocity1.multiplyVectorBySingleNumber(1, 0.75);//So I don't clip through floor
	}
	
	public void predictionPosition(Particle object, double m) {
		location1.setComponents(object.location1.getComponent(0) + object.velocity1.getComponent(0) * m,
				object.location1.getComponent(1) + object.velocity1.getComponent(1) * m);
	}
	
	public double solveResultantVector(double vx, double vy) {
		return Math.sqrt(vx*vx + vy*vy);
	}

	public Color getColor() {
		return color;
	}

	public void setColor( Color color) {//Don't really need to set Color I think
		this.color = color;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX( double x) {
		this.x = x;
	}

	public void setY( double y) {
		this.y = y;
	}

	public double getW() {
		return w;
	}

	public double getH() {
		return h;
	}

	public void setW( double dw) {
		this.w = dw;
	}

	public void setH( double dh) {
		this.h = dh;
	}

	public double getVX() {
		return vx;
	}

	public void setVX( double vx) {
		this.vx = vx;
	}
	
	public double getVY() {
		return vy;
	}

	public void setVY( double vy) {
		this.vy = vy;
	}

	public double getM() {
		return mass;
	}

	public void setM( double mass) {
		this.mass = mass;
	}
	
	public double getMS() {
		return magnetic_str;
	}

	public void setMS( double magnetic_str) {
		this.magnetic_str = magnetic_str;
	}
	public double getMR() {
		return mag_radius;
	}

	public void setMR( double mag_radius) {
		this.mag_radius = mag_radius;
	}

	public void DoPrograde() {
		double f = velocity0.getComponent(2);//Creates the final vector
		if( f == 0) {//Prevents from dividing by 0
			return;
		}
		velocity1.addToVector(2*vx/f, 2*vy/f);
	}
	public void DoRetrograde() {
		double f = velocity0.getComponent(2);//Creates the final vector
		if(f > 2){
			velocity1.addToVector(2*vx/f, 2*vy/f);
		}
		else {
			velocity1.multiplyVectorBySingleNumber(2, 0);
		}
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
	
	public void testMagnet_Two(Particle[] objects, int reverse) {
		vector0.setComponents(0, 0);//int loop used in method
		for( Particle p : objects) {
			if( p == this || !p.magnetic)
				continue;
			//Magnetism with a set Radius. (r^2 - loc0^2)^0.5 method
			double TotalD = location0.getDifference(p.location0, 2);
			if(TotalD >= p.mag_radius)//Meant to not divide by 0
				continue;
			
			double xDist = location0.getDifference(p.location0, 0);//Difference of vectors
			double yDist = location0.getDifference(p.location0, 1);
			
			double power_level = Math.pow(p.mag_radius*p.mag_radius - TotalD*TotalD, 0.5);
			//This TotalForceSize equation doesn't feel right.  Using TotalD in multiple places
			//double TotalForceSize = (power_level * p.getMS() * 25) / (TotalD*TotalD);//Old Equation
			double TotalForceSize = (power_level * p.getMS() / 50);
			
			double XForce = (xDist/TotalD) * TotalForceSize;
			double YForce = (yDist/TotalD) * TotalForceSize;
			
		
			if( reverse == 1) {//repel
				vector0.addToVector(XForce * -1, YForce * -1);
			}
			else {//attract
				vector0.addToVector(XForce, YForce);
			}
		}
		if(loop == 0) {
			forceMagnet.copyVector(vector0);
			loop++;//For Help in the GUI
		}
		else {
			forceMagnet.addVectors(vector0);
			loop = loop%1;//For Help in the GUI
		}
		//if(vector0.getComponent(2) > 0.01)//Debugging
			//System.out.println(vector0.getComponent(2));
	}
	
	public void testGravity(Particle[] objects) {
		vector0.setComponents(0, 0);
		for( Particle p : objects) {
			if( p == this || !p.gravity)
				continue;
			
			double TotalD = location0.getDifference(p.location0, 2);
			double xDist = location0.getDifference(p.location0, 0);
			double yDist = location0.getDifference(p.location0, 1);
			
			double TotalForceSize = (p.getM() * mass * G)/(TotalD*TotalD);
			if( TotalForceSize > 800)//So it doesn't yeet you out
				TotalForceSize = 800;
			
			double XForce = (xDist/TotalD) * TotalForceSize;
			double YForce = (yDist/TotalD) * TotalForceSize;
			
			vector0.addToVector(XForce, YForce);
		}
		forceGravity = vector0;
	}
	public void testingGravity() {
		if(jump)
			forceGravity.setComponents(0, mass);
		else
			forceGravity.addToVector(0, 1);
	}
	
	public void seekingParticle(Particle target) {
		boolean isLeft = target.x < x;
		boolean isAbove = target.y < y;
		
		if(isLeft) {
			velocity1.addToVector(-1, 0);
		}
		else {
			velocity1.addToVector(1, 0);
		}
		if(isAbove) {
			velocity1.addToVector(0, -1);
		}
		else {
			velocity1.addToVector(0, 1);
		}	
	}
}