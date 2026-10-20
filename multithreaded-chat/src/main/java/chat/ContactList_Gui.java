package chat;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ContactList_Gui extends JPanel implements UserActivity {
	

	
	private ContactList_GuiData data = new ContactList_GuiData();

	public ContactList_Gui(final Client client) {
		this.data.client=client;
		this.data.client.AddUserStatusListener(this);
		data.ActiveUsersListModel=new DefaultListModel<String>();
		
		data.ActiveUsers=new JList<String>(data.ActiveUsersListModel);
		setLayout(new BorderLayout());
		add(new JScrollPane(data.ActiveUsers),BorderLayout.CENTER);
		
		data.ActiveUsers.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(arg0.getClickCount()>1) {
					MousePressedData(client);
				}
				
			}

			private void MousePressedData(final Client client) {
				String login= data.ActiveUsers.getSelectedValue();
				Messages_GUI_frame messagepane=new Messages_GUI_frame(client,login);
				JFrame frame=new JFrame("Message "+login);
frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setSize(500, 500);
				frame.getContentPane().add(messagepane,BorderLayout.CENTER);
				frame.setVisible(true);
			}

	
		});
		}
	
public static void main(String[]args) {
		
		Client client= new Client("localhost",8818);
		Intilization_ContactList(client);
		
		if(client.Connecting_Client()) {
			
			try {
				client.Login_User("fatma","fatma");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
}

private static void Intilization_ContactList(Client client) {
	ContactList_Gui userlistpane=new ContactList_Gui(client);
	JFrame frame=new JFrame("Users List");
	frame.setSize(400, 600);
	frame.getContentPane().add(userlistpane,BorderLayout.CENTER);
	frame.setVisible(true);
}

public void Check_Online_User(String user) {
	data.ActiveUsersListModel.addElement(user);
	
}

public void Check_Offline_User(String user) {
	data.ActiveUsersListModel.removeElement(user);
	
}

}