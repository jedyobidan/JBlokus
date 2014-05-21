package jedyobidan.blokus;

import javax.swing.JFrame;

import jedyobidan.blokus.local.GameStage;
import jedyobidan.ui.nanim.Display;
import jedyobidan.ui.nanim.ThreadFPSRunner;

public class ClientLaunch extends JFrame{
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 580, HEIGHT = 420;
	public static Display display;
	public ClientLaunch(){
		super("JBlokus");
		display = new Display(WIDTH, HEIGHT, new ThreadFPSRunner(60));
		TitleScreen title = new TitleScreen(display);
		display.addStage("TITLE", title);
		this.add(display);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		display.start();
		
	}
	
	
	public void dispose(){
		super.dispose();
		display.stop();
	}
	public static void main(String[] args){
		new ClientLaunch().setVisible(true);
	}
}
