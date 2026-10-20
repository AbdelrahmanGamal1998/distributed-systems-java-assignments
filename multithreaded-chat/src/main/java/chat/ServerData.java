package chat;

public class ServerData {
	public int serverport;
	public int getServerport() {
		return serverport;
	}

	public void setServerport(int serverport) {
		this.serverport = serverport;
	}

	public java.util.List<Handling_The_Server> getServer_Handler_Worker_List() {
		return Server_Handler_Worker_List;
	}

	public void setServer_Handler_Worker_List(java.util.List<Handling_The_Server> server_Handler_Worker_List) {
		Server_Handler_Worker_List = server_Handler_Worker_List;
	}

	public java.util.List<Handling_The_Server> Server_Handler_Worker_List;

	public ServerData(java.util.List<Handling_The_Server> server_Handler_Worker_List) {
		Server_Handler_Worker_List = server_Handler_Worker_List;
	}
}