package chat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Messages_GUI_frame extends JPanel implements MessageTakers{
	public final Client client;
	public final String User;
	public DefaultListModel<String> ListModel=new  DefaultListModel<String> ();
	public JList<String> messagelist = new JList<String>(ListModel);
	public JTextField inputField=new JTextField();


	public Messages_GUI_frame(final Client client, final String login) {
		this.client=client;
		this.User=login;
		setLayout(new BorderLayout());
		add(new JScrollPane(messagelist),BorderLayout.CENTER);
		add(inputField,BorderLayout.SOUTH);
		client.AddMessageListener(this);
		
        inputField.addActionListener(new ActionListener() {
			

			public void actionPerformed(ActionEvent e) {
				
				try {
					ActionListener(client, login);
                     
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}

			private void ActionListener(final Client client, final String login) throws IOException {
				String text= inputField.getText();
				 client.Messages_Reader(login,  text);
				 inputField.setText("");
				 ListModel.addElement("You : " +text);
			}

		
			
		});
	}

	public void OnlineMessage_(String FromMessage, String MessageBody) {
	String Line=FromMessage+" "+MessageBody;
	ListModel.addElement(Line);
		
	}
}
