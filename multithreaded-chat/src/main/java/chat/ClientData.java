package chat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientData {
	public String Name_of_Server;
	public String getName_of_Server() {
		return Name_of_Server;
	}

	public void setName_of_Server(String name_of_Server) {
		Name_of_Server = name_of_Server;
	}

	public int getPort_Number() {
		return Port_Number;
	}

	public void setPort_Number(int port_Number) {
		Port_Number = port_Number;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public OutputStream getServerOut() {
		return serverOut;
	}

	public void setServerOut(OutputStream serverOut) {
		this.serverOut = serverOut;
	}

	public InputStream getServerIn() {
		return serverIn;
	}

	public void setServerIn(InputStream serverIn) {
		this.serverIn = serverIn;
	}

	public BufferedReader getBufferedIn() {
		return bufferedIn;
	}

	public void setBufferedIn(BufferedReader bufferedIn) {
		this.bufferedIn = bufferedIn;
	}

	public ArrayList<UserActivity> getUserslisteners() {
		return userslisteners;
	}

	public void setUserslisteners(ArrayList<UserActivity> userslisteners) {
		this.userslisteners = userslisteners;
	}

	public ArrayList<MessageTakers> getMessagelisteners() {
		return Messagelisteners;
	}

	public void setMessagelisteners(ArrayList<MessageTakers> messagelisteners) {
		Messagelisteners = messagelisteners;
	}

	public int Port_Number;
	public Socket socket;
	public OutputStream serverOut;
	public InputStream serverIn;
	public BufferedReader bufferedIn;
	public ArrayList<UserActivity> userslisteners;
	public ArrayList<MessageTakers> Messagelisteners;

	public ClientData(ArrayList<UserActivity> userslisteners, ArrayList<MessageTakers> messagelisteners) {
		this.userslisteners = userslisteners;
		Messagelisteners = messagelisteners;
	}
}