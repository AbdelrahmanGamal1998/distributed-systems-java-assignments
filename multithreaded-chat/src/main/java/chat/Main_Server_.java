package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main_Server_ {
public static void main (String[] args) throws IOException {
	    
		int port = Get_PortNumber();
		Server server=new Server(port);
        server.start();
		
}

private static int Get_PortNumber() {
	int port =8818;
	return port;
}}
