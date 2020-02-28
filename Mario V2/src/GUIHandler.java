import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JPanel;
//Mario V2
@SuppressWarnings("serial")
public class GUIHandler extends JPanel implements KeyListener {

	//private classes added here
	private Player [] p;//Player
	private Particle [] t;//Target Particles
	
	//Bullet Projectiles (Changeable Array)
	private ArrayList<Projectile> b = new ArrayList <>();
	
	private Structure [] f;//Floor Structure
	
	//Key Memory
	private boolean w = false,s = false,a = false,d = false, q = false,e = false;
	private boolean i = false,k = false,j = false,l = false,u = false,o = false;
	
	//private boolean toggleW = true, toggleI = true;//Moved these things to Player Class
	
	//Offset Variable
	private float offSetX = 0;
	
	public GUIHandler() {//Constructor
		t = new Particle[4];
		//Target Particles
		t[0] = new Particle(new Vector(300,580), new Vector(20,20), new Vector(0,0), 20f,20f,20f,false,Color.CYAN);
		t[1] = new Particle(new Vector(900,580), new Vector(20,20), new Vector(0,0), 20f,20f,20f,false,Color.PINK);
		t[2] = new Particle(new Vector(600,300), new Vector(400,400), new Vector(0,0), 20f,20f,20f,false,Color.LIGHT_GRAY);
		t[3] = new Particle(new Vector(300,600), new Vector(200,200), new Vector(0,0), 20f,20f,20f,false,Color.LIGHT_GRAY);
		//Bullets were here
		p = new Player[2];
		//Player		Vector position,		Vector size,	Vector velocity,  mass,mag_str,mad_radius,movable,Clolor;
		p[0] = new Player(new Vector(1400,760), new Vector(20,20), new Vector(0,0), 20f,20f,20f,true,Color.BLUE);
		p[1] = new Player(new Vector(1800,760), new Vector(20,20), new Vector(0,0), 20f,20f,20f,true,Color.RED);
		//Floor Structure
		f = new Structure[9];
		f[0] = new Structure(new Vector(850,750), new Vector(20,20), new Vector(0,0), 10f,0f,0f,true,Color.GRAY);//Pushable
		f[1] = new Structure(new Vector(100,770), new Vector(100,20), new Vector(0,0), 20f,0f,0f,false,Color.DARK_GRAY);
		f[2] = new Structure(new Vector(1100,770), new Vector(100,20), new Vector(0,0), 20f,0f,0f,false,Color.DARK_GRAY);
		f[3] = new Structure(new Vector(850,640), new Vector(200,20), new Vector(0,0), 20f,0f,0f,false,Color.DARK_GRAY);
		f[4] = new Structure(new Vector(600,640), new Vector(20,160), new Vector(0,0), 20f,0f,0f,false,Color.DARK_GRAY);
		f[5] = new Structure(new Vector(600,770), new Vector(600,20), new Vector(0,0), 20f,0f,0f,false,Color.DARK_GRAY);
		f[6] = new Structure(new Vector(600,300), new Vector(30,30), new Vector(0,0), 20f,20f,200f,false,Color.DARK_GRAY);
		f[7] = new Structure(new Vector(300,600), new Vector(30,30), new Vector(0,0), 20f,50f,100f,false,Color.DARK_GRAY);
		//Screen (1,0)
		f[8] = new Structure(new Vector(1800,770), new Vector(1200,20), new Vector(0,0), 20f,0f,0f,false,Color.DARK_GRAY);
	}

	public void paint(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		//Setting Methods
		offSetX = offSetX + p[1].cameraFocus(offSetX);
		if(b.size() > 10)
			b.remove(0);//Remove the "Bottom" one when the "11th" Bullet is Added
		
		f[0].pushing(p);
		f[0].gravityI();
		f[0].doRectangleCollision(f);
		
		f[6].testMagnet_Two(p, false);
		f[7].testMagnet_Two(p, false);
		
		for(int i = 0; i < 2; i++) {//Put this down below? In Painting for loop
			t[i].predictPosition(p[i], 4);
		}
		
		//To Do. Being hit by Bullets. Fix Magnet? Do something else?
		
		
		//Blue's Buttons
		if(w) 
			p[0].upKeyToggle();
		else 
			p[0].setToggleUp(true);//W Key ends here
		if(s)
			p[0].downKey();//Will Likley Never use, make it into an ability?
		if(a)
			p[0].leftKey(4);
		if(d)
			p[0].rightKey(4);
		
		
		//Red's Buttons
		if(i) 
			p[1].upKeyToggle();
		else 
			p[1].setToggleUp(true);//I Key ends here
		if(k)
			p[1].downKey();
		if(j)
			p[1].leftKey(4);
		if(l)
			p[1].rightKey(4);
		
		// Particle Color and Movement Retrieval
		for( Particle o: t) {
			o.updatePreparing();
			
			g.setColor(o.getColor());
			//g.fillRect(, y, width, height);
			g.fillOval( (int)((offSetX) + o.getX() - o.getW() / 2), (int) (o.getY() - o.getH() / 2), (int) (o.getW()), (int) o.getH());
		}
		
		for(int i = b.size() - 1; i > 0; i--){//.add/append? .remove .get
			b.get(i).pushTargetQS(p);//Like a Reverse Rectangle Collision
			//Continue to fix pushTargetQS. Gotta Fiddle around with gap.x > gap.y
			b.get(i).doRectangleCollisionQS(f);
			
			b.get(i).updatePreparing();//Issue, First Bullet summoned doesn't come, usually
			
			g.setColor(b.get(i).getColor());//Do I want Oval Bullets?
			//Maybe make a simple, single return method
			g.fillRect( (int)((offSetX) + b.get(i).getX() - b.get(i).getW() / 2), (int) (b.get(i).getY() - b.get(i).getH() / 2), (int) (b.get(i).getW()), (int) b.get(i).getH());
			
			if(b.get(i).isOffScreen(offSetX)) {//Do Last to avoid problems?
				b.remove(i);
			}
		}
		
		for( Player o: p) {
			o.gravityII();//Fixed
			//o.doRectangleCollisionQSBulletPush(b);
			o.doRectangleCollisionQS(f);//QS for Quarter Step.
			o.testFriction();
			
			o.updatePreparing();
			
			g.setColor(o.getColor());
			//g.fillRect(, y, width, height);
			g.fillRect( (int)((offSetX) + o.getX() - o.getW() / 2), (int) (o.getY() - o.getH() / 2), (int) (o.getW()), (int) o.getH());
		}
		for( Structure o: f) {
			
			o.updatePreparing();
			
			g.setColor(o.getColor());
			//g.fillRect(, y, width, height);
			g.fillRect( (int)((offSetX) + o.getX() - o.getW() / 2), (int) (o.getY() - o.getH() / 2), (int) (o.getW()), (int) o.getH());
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		//Blue Controls
		if (e.getKeyCode() == KeyEvent.VK_W) {
			w = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			s = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {
			d = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_A) {
			a = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_Q) {
			q = !q;//Toggle Button
		}
		if (e.getKeyCode() == KeyEvent.VK_E) {
			this.e = !this.e;//e needs this.e//Toggle Button
		}
		if (e.getKeyCode() == KeyEvent.VK_C) {//Bullet Testing
			b.add(new Projectile(new Vector(10,20), new Vector(15,5), new Vector(0,0), 5f,1000f,40f,true,Color.WHITE));
			b.get(b.size()-1).bulletFiredV2(p[0], t[1]);
			
		}
		
		//Red Controls
		if (e.getKeyCode() == KeyEvent.VK_I) {
			i = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_K) {
			k = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_L) {
			l = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_J) {
			j = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_U) {
			u = !u;
		}
		if (e.getKeyCode() == KeyEvent.VK_O) {
			o = !o;//Toggle Button
		}
		if (e.getKeyCode() == KeyEvent.VK_N) {//Bullet Testing
			b.add(new Projectile(new Vector(20,20), new Vector(15,5), new Vector(0,0), 5f,1000f,40f,true,Color.WHITE));
			b.get(b.size()-1).bulletFiredV3(p[1], false);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		//Blue Controls
		if (e.getKeyCode() == KeyEvent.VK_W) {
			w = false;
			
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			s = false;
			
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {
			d = false;
			
		}
		if (e.getKeyCode() == KeyEvent.VK_A) {
			a = false;
		}
		/*//Experimenting
		if (e.getKeyCode() == KeyEvent.VK_Q) {
			q = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_E) {
			this.e = false;//e needs this.e
		}
		*/
		//Red Controls
		
		if (e.getKeyCode() == KeyEvent.VK_I) {
			i = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_K) {
			k = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_L) {
			l = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_J) {
			j = false;
		}
		/* Experimenting
		if (e.getKeyCode() == KeyEvent.VK_U) {//Retrograde
			u = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_O) {//Prograde
			o = false;
		}
		*/
		
	}
}
