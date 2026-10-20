package chat;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
	private ServerData data = new ServerData(new java.util.concurrent.CopyOnWriteArrayList<Handling_The_Server>());
	public Server(int serverport) {
		super();
		this.data.serverport = serverport;
	}
	
	public List<Handling_The_Server>getWorkerList(){
		return data.Server_Handler_Worker_List;
		
		
	}
	
	
	public void run() {
	try {
		    ServerSocket ServerSocket=new ServerSocket(data.serverport);
		    while(true) {
		    Accepting_Connection(ServerSocket);

		    }
		    
		    
	 }catch(IOException e) {
		 e.printStackTrace();
	 }

	}

	private void Accepting_Connection(ServerSocket ServerSocket) throws IOException {
		System.out.print("Ready to accept connection......");
		Socket clientSocket=ServerSocket.accept();
		  System.out.print("\n");
		System.out.print("Accepted connection from :"+clientSocket);
		Handling_The_Server worker=new Handling_The_Server(this,clientSocket);
		data.Server_Handler_Worker_List.add(worker);
		worker.start();
	}

	public void Remove_worker_from_Server(Handling_The_Server serverWorker) {
	data.Server_Handler_Worker_List.remove(serverWorker);
		
	}


}

