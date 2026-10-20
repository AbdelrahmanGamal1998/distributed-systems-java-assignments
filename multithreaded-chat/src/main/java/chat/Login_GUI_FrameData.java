package chat;

import javax.swing.JButton;
import javax.swing.JTextField;

public class Login_GUI_FrameData {
	public Client client;
	public JTextField UserName;
	public JTextField passwordfield;
	public JButton login_Button;
	public JButton Register_Button;

	public Login_GUI_FrameData(JTextField userName, JTextField passwordfield, JButton login_Button,
			JButton register_Button) {
		UserName = userName;
		this.passwordfield = passwordfield;
		this.login_Button = login_Button;
		Register_Button = register_Button;
	}
}