package jedyobidan.blokus;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import jedyobidan.blokus.ai.AIPlayer;
import jedyobidan.blokus.network.BlokusServer;

public class ServerInit extends JFrame{
	private JComboBox<String> aiLevel;
	private JSpinner port;
	
	public ServerInit(){
		super("JBlokus Server");
		JComponent panel = Box.createVerticalBox();
			JComponent portC = new JPanel();
				portC.add(new JLabel("Port:"));
				portC.add(port = new JSpinner(new SpinnerNumberModel(BlokusServer.DEFAULT_PORT, 2000, 9999, 1)));
			panel.add(portC);
			JComponent aiC = new JPanel();
				aiC.add(new JLabel("AI Level:"));
				aiC.add(aiLevel = new JComboBox<String>(AIPlayer.AI_LEVELS));
			panel.add(aiC);
		this.add(panel);
		
		port.setEditor(new JSpinner.NumberEditor(port, "#"));
		
		JButton start = new JButton("Start Server");
			start.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					startServer();
				}
			});
		this.add(start, BorderLayout.SOUTH);
		
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		this.setSize(220, this.getSize().height);
		this.setLocation(10,10);
	}
	
	public void setVisible(boolean b){
		super.setVisible(b);
		port.requestFocusInWindow();
	}
	
	private void startServer(){
		this.dispose();
		try {
			new BlokusServer((String)aiLevel.getSelectedItem(), (Integer) port.getValue());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		new ServerInit().setVisible(true);
	}
}
