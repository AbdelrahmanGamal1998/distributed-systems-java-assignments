package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Login_GUI_Frame extends JFrame {
	Login_GUI_FrameData data = new Login_GUI_FrameData(new JTextField(), new JTextField(), new JButton("login"),
			new JButton("Register"));
	 int red;
	   int green;
	   int blue;
	public Login_GUI_Frame() {
		super("login");

		this.data.client = new Client("localhost", 8818);
		data.client.Connecting_Client();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel Panel = new JPanel();
		Panel.setLayout(new BoxLayout(Panel, BoxLayout.Y_AXIS));
		Panel.add(data.UserName);
		Panel.add(data.passwordfield);
		Panel.add(data.login_Button);
		Panel.add(data.Register_Button);
		  red = (int)(Math.random()*256);
	      green = (int)(Math.random()*256);
	      blue = (int)(Math.random()*256);
	      Panel.setBackground(new Color(red,green,blue));
		data.login_Button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				
		
				dologin();
			}

		});
		getContentPane().add(Panel, BorderLayout.CENTER);
		pack();
		setVisible(true);
		
		data.Register_Button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				doRegister();
			}

			

		});
		getContentPane().add(Panel, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}

	
	
	private void doRegister() {
		String login = data.UserName.getText();
		String password = data.passwordfield.getText();
		try {
			if (data.client.Login_User(login, password)==1) {

				ContactList_Gui userlistpane = new ContactList_Gui(data.client);
				JFrame frame = new JFrame("Users List");
				frame.setSize(400, 600);
				frame.getContentPane().add(userlistpane, BorderLayout.CENTER);
				frame.setVisible(true);
				setVisible(false);
			} else {
				JOptionPane.showMessageDialog(this, "Please try again");
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}
	
	private void dologin() {
		String login = data.UserName.getText();
		String password = data.passwordfield.getText();
		try {
			if (data.client.Login_User(login, password)==1) {

				ContactList_Gui userlistpane = new ContactList_Gui(data.client);
				JFrame frame = new JFrame("Users List");
				frame.setSize(400, 600);
				frame.getContentPane().add(userlistpane, BorderLayout.CENTER);
				frame.setVisible(true);
				setVisible(false);
			} else if(data.client.Login_User(login, password)==2) {
				JOptionPane.showMessageDialog(this, "password Incorrect");
			}
			else if(data.client.Login_User(login, password)==3) {
				JOptionPane.showMessageDialog(this, "UserName Incorrect");
			}
			else if(data.client.Login_User(login, password)==3) {
				JOptionPane.showMessageDialog(this, "UserName AlreadyUsed");
			}
			
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	
	
	

	public static void main(String[] args) {
		
	  
		Login_GUI_Frame Loginwin = new Login_GUI_Frame();
	
		Loginwin.setVisible(true);
	}

}