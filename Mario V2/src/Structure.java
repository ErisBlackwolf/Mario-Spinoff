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
				Vector gap = dist.abs().subtract(bounds);
				if(gap.x > gap.y) {//Left/Right
					fvelocity.addThis(new Vector(p.velocity.getX()/2,0));
				}
				if(gap.x < gap.y) {//Up/Down
					fvelocity.addThis(new Vector(0,p.velocity.getY()/2));
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
