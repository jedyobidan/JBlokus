package jedyobidan.blokus.setup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;

import jedyobidan.blokus.core.Player;
import jedyobidan.blokus.network.BlokusServer;
import jedyobidan.blokus.network.PlayerData;

public class ServerSetup extends JFrame{
	private PlayerComponent[] players;
	private JButton start;
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
		
		start = new JButton("Start Game");
		start.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				start.setEnabled(false);
				start.setText("Playing...");
				s.startGame();
			}
		});
		start.setFocusPainted(false);
		add(start, BorderLayout.SOUTH);
		
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
	
	public void enable(){
		start.setText("Start Game");
		start.setEnabled(true);
	}
	
	private class PlayerComponent extends JLabel{
		public PlayerComponent(){
			this.setOpaque(true);
			this.setHorizontalAlignment(CENTER);
			this.setPreferredSize(new Dimension(150,60));
		}
		public void setPlayer(PlayerData p){
			this.setText(p.name+ " : " + p.clientID);
			Color c = Player.getColor(p.playerNum);
			this.setBorder(BorderFactory.createLineBorder(c, 2));
			float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
			this.setBackground(Color.getHSBColor(hsb[0], hsb[1]/4, hsb[2]));
		}
	}
}
