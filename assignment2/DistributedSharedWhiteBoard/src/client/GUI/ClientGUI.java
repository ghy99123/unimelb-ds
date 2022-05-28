package client.GUI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.ClientController;

import java.awt.*;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class ClientGUI extends JFrame{
	
	// only the manager can use the pro version
	private boolean isPro;
	private JFrame frame;
//	private ClientGUI clientGUI;
	private ClientController clientController;
	private DrawArea da;
	private DrawListener dl;
	
	private JTextArea userListArea;
	private JTextArea chatArea;
	private JTextField sendField;
	String tool;
	
	public ClientGUI(boolean isPro, ClientController clientController) {
		this.isPro = isPro;
		this.clientController = clientController;
	}
	
	public JTextArea getUserListArea() {
		return userListArea;
	}

	public JTextArea getChatArea() {
		return chatArea;
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public DrawArea getDa() {
		return da;
	}
	
	public ClientController getClientController() {
		return clientController;
	}
	


	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setTitle("whiteboard - " + clientController.getUserName());
		frame.setBounds(0, 0, 730, 465);
//		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		// create white board area
		JPanel drawPanel = new JPanel();
		
		da = new DrawArea();
		
		drawPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		drawPanel.setBackground(Color.GRAY);
		da.setBackground(Color.WHITE);
		da.setPreferredSize(new Dimension(500, 410));
		drawPanel.add(da);
		
		frame.getContentPane().add(drawPanel, BorderLayout.CENTER);
		
		initTools();
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		
		JMenu operationMenu = new JMenu("Operation");
		menuBar.add(operationMenu);
		
		
		if (isPro) {
			
			JMenuItem menuItemClear= new JMenuItem("Clear");
			menuItemClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clientController.onClearClick();
				}
			});
			operationMenu.add(menuItemClear);
			
			
			JMenuItem menuItemKick= new JMenuItem("Kick out");
			menuItemKick.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clientController.onKickOutClick();
				}
			});
			operationMenu.add(menuItemKick);
		}
		
		
		
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(null);
//		rightPanel.setBackground(Color.RED);
		rightPanel.setPreferredSize(new Dimension(180, 200));
		
		frame.getContentPane().add(rightPanel, BorderLayout.EAST);
		
		
		// user list display area
		JLabel lblUsers = new JLabel("User List");
		lblUsers.setFont(new Font("Monaco", Font.PLAIN, 12));
		lblUsers.setBounds(5, 0, 66, 25);
		rightPanel.add(lblUsers);
		
		userListArea = new JTextArea();
		userListArea.setEditable(false);
		userListArea.setLineWrap(true);
		userListArea.setBounds(5, 25, 170, 80);
		JScrollPane userListScroll = new JScrollPane(userListArea);
		userListScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		userListScroll.setBounds(5, 25, 170, 80);
		rightPanel.add(userListScroll);
		
		
		// chat window display area
		JLabel lblChatWindow = new JLabel("Chat Window");
		lblChatWindow.setFont(new Font("Monaco", Font.PLAIN, 12));
		lblChatWindow.setBounds(5, 110, 100, 25);
		rightPanel.add(lblChatWindow);
		
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		chatArea.setBounds(5, 135, 170, 185);
		JScrollPane chatScroll = new JScrollPane(chatArea);
		chatScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		chatScroll.setBounds(5, 135, 170, 185);
		rightPanel.add(chatScroll);
		
		
		// chat sending area
		JLabel lblChatInput = new JLabel("Chat with others:");
		lblChatInput.setFont(new Font("Monaco", Font.PLAIN, 12));
		lblChatInput.setBounds(5, 320, 170, 25);
		rightPanel.add(lblChatInput);
		
		sendField = new JTextField();
		sendField.setBounds(0, 345, 180, 25);
		rightPanel.add(sendField);
		
		JButton btnSend = new JButton("Send");
		btnSend.setForeground(Color.RED);
		btnSend.setBounds(3, 375, 80, 40);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sentence = sendField.getText();
				clientController.onSendClick(sentence);
			}
		});
		rightPanel.add(btnSend);
		

		JButton btnClear = new JButton("Clear");
		btnClear.setForeground(Color.darkGray);
		btnClear.setBounds(97, 375, 80, 40);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendField.setText(null);
			}
		});
		rightPanel.add(btnClear);
		
		
		frame.setVisible(true);
		

		dl = new DrawListener(da, this, drawPanel);
		
		drawPanel.addMouseListener(dl);
		
		drawPanel.addMouseMotionListener(dl);
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
                int res = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to close this whiteboard?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (res == JOptionPane.YES_OPTION){
                    System.out.println("close");
                    clientController.onCloseClick(windowEvent);
                }
            }
		});
		
	}
	
	private void initTools() {
		JPanel toolPanel = new JPanel();
		toolPanel.setLayout(new BorderLayout());
		toolPanel.setPreferredSize(new Dimension(40, 300));
		
		JPanel toolLayoutPanel = new JPanel();
		toolLayoutPanel.setLayout(new GridLayout(5, 1, 1, 1));
		
		String[] toolArr = {"/icon/circle.png", "/icon/rectangle.png", "/icon/triangle.png", "/icon/line.png", "/icon/text.png"};
		
		// shape buttons
		for (int i = 0; i < toolArr.length; i++) {
			ImageIcon image = new ImageIcon(getClass().getResource(toolArr[i]));
			JButton button = new JButton(image);
			button.setPreferredSize(new Dimension(40, 40));
			
			button.setActionCommand(toolArr[i].substring(6, toolArr[i].lastIndexOf(".")));
			
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tool = e.getActionCommand();
					
					dl.setShape(tool);
				}
				
			});		
			toolLayoutPanel.add(button);
		}
		toolPanel.add(toolLayoutPanel, BorderLayout.NORTH);
		
		// create color pickers
		JPanel colorLayoutPanel = new JPanel();
		colorLayoutPanel.setLayout(new GridLayout(8, 2, 2, 2));
		
		Color[] colorArr = {Color.black, Color.BLUE, Color.cyan, Color.red, Color.green, Color.darkGray,
				Color.ORANGE, Color.pink, Color.magenta, Color.yellow, new Color(166, 21, 48), 
				new Color(46, 114, 191), new Color(232, 171, 102), new Color(99, 2, 227),
				new Color(125, 161, 74), new Color(134, 46, 140), 
				};
		
		for (int i = 0; i < colorArr.length; i++) {
			JButton button = new JButton();
			
			final Integer innerI = i;
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dl.setColor(colorArr[innerI]);
				}
			});
			button.setPreferredSize(new Dimension(20, 20));
			button.setBorderPainted(false);
			button.setOpaque(true);
//			button.setForeground(colorArr[i]);
			button.setBackground(colorArr[i]);
			colorLayoutPanel.add(button);
		}
		
		toolPanel.add(colorLayoutPanel,  BorderLayout.SOUTH);
		
		frame.getContentPane().add(toolPanel, BorderLayout.WEST);
	}
}
