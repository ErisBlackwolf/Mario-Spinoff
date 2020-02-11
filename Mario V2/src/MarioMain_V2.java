import java.awt.Color;

import javax.swing.JFrame;
//Mario V2
public class MarioMain_V2 {
	
	private static final long TICK_RATE = 50000000;//Original
	//private static final long TICK_RATE = 60000000;//50000000;//
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame w = new JFrame("World");
		GUIHandler gui = new GUIHandler();
		w.add(gui);
		w.addKeyListener(gui);
				
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		w.pack();
		w.setSize(1200, 800);//1200, 780 is shown
		w.setBackground(Color.BLACK);
		w.setVisible(true);
		while (true) {
			long elapsed = System.nanoTime();
			w.repaint();
			
			try {
				long sleep = (long) ((TICK_RATE - System.nanoTime() + elapsed) / 1e6);
				if(sleep > 0)
					Thread.sleep(sleep);
			}catch(InterruptedException e) {}
		}	
	}
}
