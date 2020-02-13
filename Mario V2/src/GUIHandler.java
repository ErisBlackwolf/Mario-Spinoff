import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
//Mario V2
@SuppressWarnings("serial")
public class GUIHandler extends JPanel implements KeyListener {

	//private classes added here
	private Particle [] p;//Player Particles
	private Particle [] t;//Target Particles
	private Particle [] b;//Bullet Particles
	private Particle [] f;//Floor Particles
	
	//Key Memory
	private boolean w = false,s = false,a = false,d = false, q = false,e = false;
	private boolean i = false,k = false,j = false,l = false,u = false,o = false;
	
	private boolean toggleW = true, toggleI = true;
	
	//Offset Variable and Bullet Rotation
	private double OffsetX = 0;
	private int bbNum = 0, rbNum = 3;//Which bullet is next
	
	public GUIHandler() {//Constructor
		t = new Particle[3];
		//Target
		t[0] = new Particle(300,750, 20,20, 0,0, 20,100,15,false,false,false, Color.CYAN);
		t[1] = new Particle(300,750, 20,20, 0,0, 20,100,15,false,false,false, Color.PINK);
		t[2] = new Particle(600,300, 300,300, 0,0, 1,10,15,false,false,false, Color.DARK_GRAY);
		p = new Particle[2];//x,y,   w,h,  vx,vy, mass,magnetic_str,mag_radius,movable,gravity,magnetic, color;
		//Player
		p[0] = new Particle(300,370, 20,20, 0,0, 20,100,20,true,true,true, Color.BLUE);
		p[1] = new Particle(900,370, 20,20, 0,0, 20,100,20,true,true,true, Color.RED);
		
		b = new Particle[6];
		//Bullets				y value for offscreen
		b[0] = new Particle(200,100, 15,5, 0,0, 20,1000,40,true,false,true, Color.WHITE);
		b[1] = new Particle(300,100, 15,5, 0,0, 20,1000,40,true,false,true, Color.WHITE);//Blue
		b[2] = new Particle(400,100, 15,5, 0,0, 20,1000,40,true,false,true, Color.WHITE);
		//				
		b[3] = new Particle(500,100, 15,5, 0,0, 20,1000,40,true,false,true, Color.WHITE);
		b[4] = new Particle(600,100, 15,5, 0,0, 20,1000,40,true,false,true, Color.WHITE);//Red
		b[5] = new Particle(700,100, 15,5, 0,0, 20,1000,40,true,false,true, Color.WHITE);
		//Floor/Wall
		f = new Particle[10];
		//Floor					,770 => Floor	Screen 0
		f[0] = new Particle(300,770, 400,20, 0,0, 20,100,20,false,false,false, Color.GRAY);//Floor-1
		f[1] = new Particle(900,770, 400,20, 0,0, 20,100,20,false,false,false, Color.GRAY);//Floor-2
		f[2] = new Particle(300,400, 120,30, 0,0, 20,100,20,false,false,false, Color.GRAY);//Platform
		f[3] = new Particle(900,400, 120,30, 0,0, 20,100,20,false,false,false, Color.GRAY);//Platform
		f[4] = new Particle(600,650, 20,120, 0,0, 20,100,20,false,false,false, Color.GRAY);//Mid Wall
		f[5] = new Particle(600,530, 200,30, 0,0, 20,100,20,false,false,false, Color.GRAY);//Floating
		f[6] = new Particle(200,650, 120,20, 0,0, 20,100,20,false,false,false, Color.GRAY);//Floating
		f[7] = new Particle(1000,650, 120,20, 0,0, 20,100,20,false,false,false, Color.GRAY);//Floating
		f[8] = new Particle(600,300, 30,30, 0,0, 20,10,150,false,false,true, Color.YELLOW);//Mid Mag Ball
		// Screen 1			1200+700
		f[9] = new Particle(1900,770, 1000,20, 0,0, 20,100,20,false,false,false, Color.GRAY);
	}

	public void paint(Graphics g) {
		
		OffsetX = OffsetX + p[0].cameraFocus(OffsetX);
		
		//Player Methods
		for( Particle o: p) {
			//o.testMagnet_Two(b, 1);
			//o.testMagnet_Two(f, 0);//URGENT MUST BE FIXED, BULLET MAGNETISM OVERWRITTEN BY FLOOR MAGNETISM
			o.testingGravity();//There is a patched in solution, but please improve it. Examine int loop
			o.testFriction();
			o.doRectangleCollision(f);
		}
		//Bullet Methods
		for( Particle o: b) {
			//o.testMagnetV3(p, 1);//Testing
			o.doRectangleCollision(f);
		}
				
		t[0].predictionPosition(p[0], 6);//4
		t[1].predictionPosition(p[1], 6);//
		
		//Blue's Buttons
		if(w) {
			if(toggleW)
				p[0].upKey();
			toggleW = false;
		}
		else {
			toggleW = true;
		}
		
		if(s)
			p[0].downKey();
		if(a)
			p[0].leftKey(4);
		if(d)
			p[0].rightKey(4);
		
		//Red's Buttons
		if(i) {
			if(toggleI)
				p[1].upKey();
			toggleI = false;
		}
		else {
			toggleI = true;
		}
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
			g.fillOval( (int)((OffsetX) + o.getX() - o.getW() / 2), (int) (o.getY() - o.getH() / 2), (int) (o.getW()), (int) o.getH());
		}
		for( Particle o: p) {
			o.updatePreparing();
			
			g.setColor(o.getColor());
			//g.fillRect(, y, width, height);
			g.fillRect( (int)((OffsetX) + o.getX() - o.getW() / 2), (int) (o.getY() - o.getH() / 2), (int) (o.getW()), (int) o.getH());
		}
		for( Particle o: b) {
			o.updatePreparing();
			
			g.setColor(o.getColor());
			//g.fillRect(, y, width, height);
			g.fillRect( (int)((OffsetX) + o.getX() - o.getW() / 2), (int) (o.getY() - o.getH() / 2), (int) (o.getW()), (int) o.getH());
		}
		for( Particle o: f) {
			o.updatePreparing();
			
			g.setColor(o.getColor());
			//g.fillRect(, y, width, height);
			g.fillRect( (int)((OffsetX) + o.getX() - o.getW() / 2), (int) (o.getY() - o.getH() / 2), (int) (o.getW()), (int) o.getH());
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
		
		if (e.getKeyCode() == KeyEvent.VK_G) {
			
			p[0].dataKey();
			
			for( Particle o: p) {
				o.positionResetKey();
			}
			for( Particle o: b) {
				o.positionResetKey();
			}
		}
		//Bullet Controls
		//Soley For Testing. Maybe Recode Key Stuff Soon?
		if (e.getKeyCode() == KeyEvent.VK_C) {//Blue Bullet
			bbNum = bbNum % 3;
				if(!this.e)//! Not put in for gaming reasons
					b[bbNum].bulletFiredV3(p[0],q);
				else
					b[bbNum].bulletFiredV2(p[0],t[1]);//Uses Pink Predition as Target
			bbNum++;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_N) {//Red Bullet
			rbNum = (rbNum-3) % 3 + 3;
				if(!u)//! Not put in for gaming reasons
					b[rbNum].bulletFiredV3(p[1],o);
				else
					b[rbNum].bulletFiredV2(p[1],t[0]);//Uses Cyan Prediction as Target
			rbNum++;
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
