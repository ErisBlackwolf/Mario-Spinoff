import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
//Mario V2
@SuppressWarnings("serial")
public class GUIHandler extends JPanel implements KeyListener {

	//private classes added here
	private Player [] p;//Player Particles
	//private Particle [] t;//Target Particles
	//private Particle [] b;//Bullet Particles
	//private Particle [] f;//Floor Particles
	
	//Key Memory
	private boolean w = false,s = false,a = false,d = false, q = false,e = false;
	private boolean i = false,k = false,j = false,l = false,u = false,o = false;
	
	private boolean toggleW = true, toggleI = true;
	
	//Offset Variable and Bullet Rotation
	private float OffsetX = 0;
	//private int bbNum = 0, rbNum = 3;//Which bullet is next
	
	public GUIHandler() {//Constructor
		p = new Player[2];
		//Player		Vector position,		Vector size,	Vector velocity,  mass,mag_str,mad_radius,movable,Clolor;
		p[0] = new Player(new Vector(300,370), new Vector(20,20), new Vector(0,5), 20f,20f,20f,true,Color.BLUE);
		p[1] = new Player(new Vector(900,370), new Vector(20,20), new Vector(0,0), 20f,20f,20f,true,Color.RED);
				
	}

	public void paint(Graphics g) {
		
		OffsetX = OffsetX + p[0].cameraFocus(OffsetX);
		/*
		//Player Methods
		for( Player o: p) {
			
			o.testFriction();
			//o.doRectangleCollisionP2(p);
		}
		
		//Bullet Methods
		for( Particle o: b) {
			//o.testMagnetV3(p, 1);//Testing
			o.doRectangleCollision(f);
		}
		*/	
		
		//Blue's Buttons
		if(w) {
			if(toggleW)
				p[0].upKey();
			toggleW = false;
		}else {
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
		}else {
			toggleI = true;
		}
		if(k)
			p[1].downKey();
		if(j)
			p[1].leftKey(4);
		if(l)
			p[1].rightKey(4);
		
		
		// Particle Color and Movement Retrieval
		for( Player o: p) {
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
		/*
		if (e.getKeyCode() == KeyEvent.VK_G) {
			
			p[0].dataKey();
			
			for( Particle o: p) {
				o.positionResetKey();
			}
			for( Particle o: b) {
				o.positionResetKey();
			}
		}
		*/
		/*
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
		*/
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
