package chat;

import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;

public class Handling_The_ServerData {
	public Socket clientSocket;
	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getRegister() {
		return Register;
	}

	public void setRegister(String register) {
		Register = register;
	}

	public OutputStream getOutputStream() {
		return OutputStream;
	}

	public void setOutputStream(OutputStream outputStream) {
		OutputStream = outputStream;
	}

	public HashSet<String> getTitle_set() {
		return Title_set;
	}

	public void setTitle_set(HashSet<String> title_set) {
		Title_set = title_set;
	}

	public HashSet<String> getBroadcast() {
		return broadcast;
	}

	public void setBroadcast(HashSet<String> broadcast) {
		this.broadcast = broadcast;
	}

	public DataBase_Confirmation getDatabase() {
		return database;
	}

	public void setDatabase(DataBase_Confirmation database) {
		this.database = database;
	}

	public Write_in_DataBase getWrite() {
		return write;
	}

	public void setWrite(Write_in_DataBase write) {
		this.write = write;
	}

	public Server server;
	public String login;
	public String Register;
	public OutputStream OutputStream;
	public HashSet<String> Title_set;
	public HashSet<String> broadcast;
	public DataBase_Confirmation database;
	public Write_in_DataBase write;

	public Handling_The_ServerData(String login, String register, HashSet<String> title_set, HashSet<String> broadcast,
			DataBase_Confirmation database, Write_in_DataBase write) {
		this.login = login;
		Register = register;
		Title_set = title_set;
		this.broadcast = broadcast;
		this.database = database;
		this.write = write;
	}
}