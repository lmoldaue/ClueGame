package clueGUI;

import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import clueBoard.BadConfigFormatException;
import clueBoard.Board;
import clueBoard.BoardCell;
import clueGame.ClueGame;

public class BoardGUI extends JInternalFrame {
	int length, width, lengthOffset, widthOffset;
	private DetectiveNotesDialog dialog;
	private ClueGame cg;
	
	public BoardGUI(Board b, ClueGame cg) throws Exception {
		this.cg = cg;
		add(b, BorderLayout.CENTER);
		length = b.getNumColumns()*BoardCell.getSidelength();
		width = b.getNumRows()*BoardCell.getSidelength();
		setSize(width, length);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());
		
		setVisible(true);
	}
	
	private JMenu createFileMenu() {
		JMenu menu = new JMenu("File");
		menu.add(createMenuItem());
		menu.add(createFileExitItem());
		return menu;
	}
	
	private JMenuItem createMenuItem() {
		JMenuItem item = new JMenuItem("Detective Notes");
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				dialog = new DetectiveNotesDialog(cg);
				dialog.setVisible(true);
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}

	
	private JMenuItem createFileExitItem() {
	  JMenuItem item = new JMenuItem("Exit");
	  class MenuItemListener implements ActionListener {
	    public void actionPerformed(ActionEvent e)
	    {
	       System.exit(0);
	    }
	  }
	  item.addActionListener(new MenuItemListener());
	  return item;
	}
}
