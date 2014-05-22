package jedyobidan.blokus.setup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

import jedyobidan.blokus.core.GameModel;
import jedyobidan.blokus.core.GameObserver;
import jedyobidan.blokus.core.Move;
import jedyobidan.blokus.core.Player;
import jedyobidan.blokus.network.BlokusServer;
import jedyobidan.blokus.network.PlayerData;
import jedyobidan.borrowed.SmartScroller;

public class ServerSetup extends JFrame implements GameObserver{
	private PlayerComponent[] players;
	private JButton start;
	private JTextArea moveLog;
	private JScrollPane scroll;
	public ServerSetup(final BlokusServer s){
		super("JBlokus Server");
		players = new PlayerComponent[4];
		
		Box box = Box.createHorizontalBox();
			box.add(Box.createHorizontalStrut(2));
			try {
				box.add(new JLabel("IP: " + InetAddress.getLocalHost().getHostAddress() + ":" + s.getPort()));
			} catch (UnknownHostException e2) {
				e2.printStackTrace();
			}
			box.add(Box.createHorizontalGlue());
			box.add(new JLabel("AI Level: " + s.getAILevel()));
			box.add(Box.createHorizontalStrut(2));
		this.add(box, BorderLayout.NORTH);
		
		JComponent playerC = new JPanel(new GridLayout(2,2));
		for(int i= 0; i < 4; i++){
			this.players[i] = new PlayerComponent();
			this.players[i].setPlayer(s.getPlayerData()[i]);
			playerC.add(this.players[i]);
		}
		add(playerC);
		
		
			
		scroll = new JScrollPane(moveLog = new JTextArea("Welcome to the server!", 5, 15));
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		moveLog.setEditable(false);
		moveLog.setFont(Font.decode("Consolas-12"));
		moveLog.setMargin(new Insets(1,1,1,1));
		new SmartScroller(scroll);
		
		start = new JButton("Start Game");
		start.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				s.startGame();
			}
		});
		start.setFocusPainted(false);
		JPanel bot = new JPanel(new BorderLayout());
			bot.add(scroll);
//			bot.add(start, BorderLayout.SOUTH);
		add(bot, BorderLayout.SOUTH);
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosed(WindowEvent e) {
				try {
					s.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		this.pack();
		this.setLocation(20, 20);
	}
	
	public void setPlayer(PlayerData p){
		players[p.playerNum].setPlayer(p);
		repaint();
	}
	
	public void setReady(int pnum, boolean ready){
		players[pnum].ready(ready);
	}
	
	public void enable(){
		start.setText("Start Game");
		start.setEnabled(true);
	}
	
	public void disable(){
		start.setEnabled(false);
		start.setText("Playing...");
	}
	
	private class PlayerComponent extends JLabel{
		private PlayerData player;
		public PlayerComponent(){
			this.setOpaque(true);
			this.setHorizontalAlignment(CENTER);
			this.setPreferredSize(new Dimension(150,60));
		}
		public void setPlayer(PlayerData p){
			player = p;
			this.setText(p.name+ " : " + p.clientID);
			Color c = Player.getColor(p.playerNum);
			this.setBorder(BorderFactory.createLineBorder(c, 2));
			float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
			this.setBackground(Color.getHSBColor(hsb[0], hsb[1]/4, hsb[2]));
		}
		public void onTurn(boolean turn){
			this.setBorder(BorderFactory.createLineBorder(Player.getColor(player.playerNum), turn?4:2));
		}
		
		public void ready(boolean ready){
			if(ready){
				this.setFont(getFont().deriveFont(Font.ITALIC + Font.BOLD));
			} else {
				this.setFont(getFont().deriveFont(Font.BOLD));
			}
		}
	}

	@Override
	public void turnStart(Player p) {
		for(int i = 0; i < 4; i++){
			players[i].onTurn(i == p.playerID);
		}
		repaint();
	}

	@Override
	public void moveMade(final Move m) {
		appendMessage(m.toString());
	}

	@Override
	public void noMoves(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameEnd(GameModel game) {
		enable();
		appendMessage(game.getWinner().getName() + " is the winner!");
	}

	@Override
	public void gameStart() {
		appendMessage("--Game Start--");
		
	}
	
	public void appendMessage(final String message){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				moveLog.append("\n" + message);
				repaint();
			}
		});
	}
}
