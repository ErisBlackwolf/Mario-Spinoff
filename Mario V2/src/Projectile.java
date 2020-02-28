import java.awt.Color;

public class Projectile extends Particle {

	public Projectile(Vector position, Vector size, Vector velocity, float mass, float magnetic_str, float mag_radius,
			boolean movable, Color color) {
		super(position, size, velocity, mass, magnetic_str, mag_radius, movable, color);
		// TODO Auto-generated constructor stub
	}
	
	
	public void bulletFiredV2(Particle shooter, Particle target) {//Used by Bullet Particle
		Vector direction = target.position.subtract(shooter.position).normalize();
		fposition = direction.multiply(mag_radius).add(shooter.position);
		fvelocity = direction.multiply(30f);
		
		if(direction.abs().getX() > direction.abs().getY())
			size.setComponents(15,5);
		else
			size.setComponents(5,15);
		
	}
	public void bulletFiredV3(Particle shooter, boolean right) {
		if(right) {//Shooter Aiming Right
			//fposition = new Vector(mag_radius,0).add(shooter.position);
			//fvelocity = new Vector(15,0);
			fposition.setComponents(mag_radius, 0).addThis(shooter.position);
			fvelocity.setComponents(15,0).addThis(shooter.velocity.multiply(1/4f));//Remove shooter.velocity part?
		}else {//Shooter Aiming Left
			//fposition = new Vector(-mag_radius,0).subtract(shooter.position);
			//fvelocity = new Vector(-15,0);
			fposition.setComponents(-mag_radius, 0).addThis(shooter.position);
			fvelocity.setComponents(-15,0).addThis(shooter.velocity.multiply(1/4f));
		}
	}
	/*
	public void projectileLobbed(Particle thrower, boolean right) {
		if(right) {//Throwing Right
			fposition.setComponents(thrower.getX() + mag_radius, thrower.getY());//Make better start pos
			fvelocity.setComponents(1, 1);//Finish
		}else{
		
		}
	}
	*/
	
	public void pushTargetQS(Player[] objects) {
		for( Player p : objects) {//no need for if p == this continue b/c Player != Projectile
			Vector bounds = p.size.add(size).multiply(0.5f);
			
			for(int step = 1; step < 5; step++) {
				Vector dist = p.position.subtract(position.add(velocity.multiply(0.25f*step))).abs();
				
				if(dist.x <= bounds.x && dist.y <= bounds.y) {
					//Vector gap = position.subtract(p.position.add(velocity)).abs().subtract(bounds);//Works Why?
					
					//Vector gap = p.position.subtract(position.add(velocity.multiply(0.25f*step))).abs().subtract(bounds);
					Vector gap = position.subtract(p.position).abs().subtract(bounds);
					
					step = 5;
					if(gap.x > gap.y) {//Left/Right
						p.fvelocity.setX(velocity.x);
						
						if(position.x < p.position.x) {//this is left of p.that
							p.fposition.addThis(new Vector(gap.x,0));
							
						}else {
							p.fposition.subtractThis(new Vector(gap.x,0));//Working on This
						}
						
					}
					
					/*
					if(gap.x < gap.y) {//Up/Down//Replace with else
						p.setJumps(1);//Important
					}
					*/
				}
			}
		}
	}
	public boolean isOffScreen(float ofsx) {//Use for Jake's Idea. Also update with ofsy
		if(position.x < 0 - ofsx || position.x > 1200 - ofsx || position.y < 0 || position.y > 900)
			return true;
		return false;
	}
}
