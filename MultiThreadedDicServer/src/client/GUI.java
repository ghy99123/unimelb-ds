/*
 * Author: Hanyi Gao <hanyig1@student.unimelb.edu.au>
 * Student ID: 1236476
 */

package client;

//import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;


public class GUI {

	JFrame frame;
	private JTextField textField;
	private JTextArea textArea;
	private JLabel lblMessage;
	private ClientController clientController;
	
	/**
	 * Create the application.
	 */
	public GUI(ClientController clientController) {
		this.clientController = clientController;
		initialize();
	}
	
	public JTextArea getTextArea() {
		return this.textArea;
	}

	public JLabel getLblMessage() {
		return this.lblMessage;
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{16, 339, 72, 0};
		gridBagLayout.rowHeights = new int[]{24, 16, 26, 20, 34, 0, 26, 16, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JLabel lblWord = new JLabel("Please enter a word:");
		GridBagConstraints gbc_lblWord = new GridBagConstraints();
		gbc_lblWord.anchor = GridBagConstraints.SOUTH;
		gbc_lblWord.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblWord.insets = new Insets(0, 0, 5, 5);
		gbc_lblWord.gridx = 1;
		gbc_lblWord.gridy = 1;
		frame.getContentPane().add(lblWord, gbc_lblWord);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText(null);
				textField.setText(null);
				lblMessage.setText(null);
			}
		});
		btnClear.setForeground(new Color(0, 100, 0));
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.insets = new Insets(0, 0, 5, 0);
		gbc_btnClear.gridx = 2;
		gbc_btnClear.gridy = 1;
		frame.getContentPane().add(btnClear, gbc_btnClear);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.anchor = GridBagConstraints.NORTH;
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 2;
		frame.getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setForeground(Color.RED);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = textField.getText();
				String meaning = textArea.getText();
				clientController.add(word, meaning);
			}
		});
		
		JButton btnQuery = new JButton("Query");
		btnQuery.setForeground(Color.RED);
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = textField.getText();
				clientController.query(word);
			}
		});
		
		JLabel lblMeaning = new JLabel("meaning(s):");
		GridBagConstraints gbc_lblMeaning = new GridBagConstraints();
		gbc_lblMeaning.anchor = GridBagConstraints.WEST;
		gbc_lblMeaning.insets = new Insets(0, 0, 5, 5);
		gbc_lblMeaning.gridx = 1;
		gbc_lblMeaning.gridy = 3;
		frame.getContentPane().add(lblMeaning, gbc_lblMeaning);
		GridBagConstraints gbc_btnQuery = new GridBagConstraints();
		gbc_btnQuery.anchor = GridBagConstraints.NORTH;
		gbc_btnQuery.insets = new Insets(0, 0, 5, 0);
		gbc_btnQuery.gridx = 2;
		gbc_btnQuery.gridy = 3;
		frame.getContentPane().add(btnQuery, gbc_btnQuery);
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.anchor = GridBagConstraints.NORTH;
		gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
		gbc_btnAdd.gridx = 2;
		gbc_btnAdd.gridy = 4;
		frame.getContentPane().add(btnAdd, gbc_btnAdd);
		
		textArea = new JTextArea();
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.insets = new Insets(0, 0, 5, 5);
		gbc_textArea.gridheight = 3;
		gbc_textArea.gridx = 1;
		gbc_textArea.gridy = 4;
		frame.getContentPane().add(textArea, gbc_textArea);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = textField.getText();
				clientController.remove(word);
			}
		});
		btnRemove.setForeground(Color.RED);
		GridBagConstraints gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.anchor = GridBagConstraints.NORTH;
		gbc_btnRemove.insets = new Insets(0, 0, 5, 0);
		gbc_btnRemove.gridx = 2;
		gbc_btnRemove.gridy = 5;
		frame.getContentPane().add(btnRemove, gbc_btnRemove);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = textField.getText();
				String meaning = textArea.getText();
				clientController.update(word, meaning);
			}
		});
		btnUpdate.setForeground(Color.RED);
		GridBagConstraints gbc_btnUpdate = new GridBagConstraints();
		gbc_btnUpdate.insets = new Insets(0, 0, 5, 0);
		gbc_btnUpdate.anchor = GridBagConstraints.NORTH;
		gbc_btnUpdate.gridx = 2;
		gbc_btnUpdate.gridy = 6;
		frame.getContentPane().add(btnUpdate, gbc_btnUpdate);
		
		lblMessage = new JLabel("");
		GridBagConstraints gbc_lblMessage = new GridBagConstraints();
		gbc_lblMessage.anchor = GridBagConstraints.WEST;
		gbc_lblMessage.insets = new Insets(0, 0, 0, 5);
		gbc_lblMessage.gridx = 1;
		gbc_lblMessage.gridy = 7;
		frame.getContentPane().add(lblMessage, gbc_lblMessage);
	}
}
