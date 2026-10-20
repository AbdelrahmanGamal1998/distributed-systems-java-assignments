package chat;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class ContactList_GuiData {
	public Client client;
	public JList<String> ActiveUsers;
	public DefaultListModel<String> ActiveUsersListModel;

	public ContactList_GuiData() {
	}
}