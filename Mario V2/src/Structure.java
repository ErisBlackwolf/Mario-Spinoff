import java.awt.Color;

public class Structure extends Particle {
	
	
	
	public Structure(Vector position, Vector size, Vector velocity, float mass, float magnetic_str, float mag_radius,
			boolean movable, Color color) {
		super(position, size, velocity, mass, magnetic_str, mag_radius, movable, color);
		// TODO Auto-generated constructor stub
	}
	
	
	public void pushing(Particle[] objects) {
		for( Particle p : objects) {
			if(p == this)
				continue;
			
			Vector dist = p.position.subtract(position).abs();
			Vector bounds = p.size.add(size).multiply(0.5f);
			
			if(dist.x <= bounds.x && dist.y <= bounds.y && p.mass > mass) {
				Vector gap = dist.subtract(bounds);
				
				
				if(gap.x > gap.y) {//Left/Right
					if(p.position.x < position.x) {// && p.velocity.x > 0)//Pushed to Right +vx,0
						force.addThis(new Vector(10,0));//Arbitray Values used
						//fvelocity.addThis(new Vector(p.velocity.getX()/2,0));
					}
					if(p.position.x > position.x) {// && p.velocity.x < 0)//Pushed to Left -vx,0
						force.subtractThis(new Vector(10,0));
						//fvelocity.subtractThis(new Vector(5,0));
					}
					//if(p.velocity.x == 0)//Thing is pushing Player
					//	fvelocity.subtractThis(new Vector(fvelocity.getX()/2,0));
				}
				if(gap.x < gap.y) {//Up/Down
					if(p.fposition.y < position.y)
						force.addThis(new Vector(0,10));
					else
						force.subtractThis(new Vector(0,10));
					//fvelocity.addThis(new Vector(0,p.velocity.getY()/2));
				}
			}
		}
	}
	
	public boolean doRectTest(Particle object) {//Unused
		Vector dist = object.position.subtract(position).abs();
		Vector bounds = object.size.add(size).multiply(0.5f);
		if(dist.x <= bounds.x && dist.y <= bounds.y)	
			return true;
		return false;
	}
}
