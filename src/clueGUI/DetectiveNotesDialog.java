package clueGUI;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.Card;
import clueGame.ClueGame;


public class DetectiveNotesDialog extends JDialog{
	private JTextField name;
	private SomePanel peeps, roomies, weaps;
	private JPanel peepQuestion, roomQuestion, weapQuestion;
	private ClueGame game;
	
	public DetectiveNotesDialog(ClueGame game) {
		this.game = game;
		
		setTitle("I am the world's greatest detective. Here are my Detective Notes");
		setSize(600,700);
		setLayout(new GridLayout(4,1));
		
		peeps = new SomePanel("People", game.peopleList);
		add(peeps);
		
		peepQuestion = ComboBoxPanel("Murderer Guess", game.peopleList);
		add(peepQuestion);
		
		roomies = new SomePanel("Rooms", game.roomList);
		add(roomies);
		
		roomQuestion = ComboBoxPanel("Room Guess", game.roomList);
		add(roomQuestion);
		
		weaps = new SomePanel("Weapons", game.weaponList);
		add(weaps);
		
		weapQuestion = ComboBoxPanel("Murder Weapon Guess", game.weaponList);
		add(weapQuestion);
		
		JPanel buttonPanel = new JPanel();
		JButton buttin = new JButton("BOOP");
		buttin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});	
		buttonPanel.add(buttin);
		buttonPanel.setBorder(new TitledBorder(new EtchedBorder(), "Button"));
		add(buttonPanel);
		
	}
	
	private class SomePanel extends JPanel {
		
		public SomePanel(String panelName, ArrayList<Card> list) {
			setLayout(new GridLayout(0,2));
			for(Card card : list) {
				JCheckBox aBox = new JCheckBox(card.getName());
				add(aBox);
			}
			
			setBorder(new TitledBorder(new EtchedBorder(), panelName));
		}
	}
	
	private JPanel ComboBoxPanel(String panelName, ArrayList<Card> list){
		JPanel panel = new JPanel();
		panel.add(SomeComboBox(list));
		panel.setBorder(new TitledBorder(new EtchedBorder(), panelName));
		return panel;
	}
	
	private JComboBox<String> SomeComboBox(ArrayList<Card> list) {
		JComboBox<String> combo = new JComboBox<String>();
		for(Card card : list) {
			combo.addItem(card.getName());
		}
		return combo;	
	}
}
