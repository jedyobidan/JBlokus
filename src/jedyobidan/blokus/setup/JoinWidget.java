package jedyobidan.blokus.setup;

import java.awt.Graphics2D;
import java.awt.Shape;

import jedyobidan.blokus.ClientLaunch;
import jedyobidan.ui.nanim.Actor;
import jedyobidan.ui.nanim.Command;
import jedyobidan.ui.nanim.Controller;
import jedyobidan.ui.nanim.actors.Button;
import jedyobidan.ui.nanim.actors.Textbox;

public abstract class JoinWidget extends Actor implements Command{
	private Textbox joinName;
	private Button button;
	public JoinWidget(){
		joinName = new Textbox(10, ClientLaunch.HEIGHT - 28, 120, 18);
		button = new Button(135, ClientLaunch.HEIGHT - 28, 50, 18, "Join", this);
	}
	public void processInput(Controller c){
		joinName.processInput(c);
		button.processInput(c);
	}
	@Override
	public void onStep() {
		joinName.onStep();
		button.onStep();
	}
	@Override
	public void render(Graphics2D g) {
		joinName.render(g);
		button.render(g);
	}
	@Override
	public Shape getHitbox() {
		return null;
	}
	
	public String getJoinName(){
		return joinName.getText();
	}
	
	public void clearName(){
		joinName.clearText();
	}
}
