package chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

public class Client {

	
	private ClientData data = new ClientData(new ArrayList<UserActivity>(), new ArrayList<MessageTakers>());

	public Client(String Name_of_Server,int Port_Number) {
	
		this.data.Name_of_Server=Name_of_Server;
		this.data.Port_Number=Port_Number;
	}

	public static void main(String[]args) throws IOException {
		Client client=new Client("localhost",8818);
		client.AddUserStatusListener(new UserActivity() {

			public void Check_Online_User(String user) {
				System.out.println("Online : "+ user);
				
			}

			public void Check_Offline_User(String user) {
				System.out.println("Offline : "+ user);
				
			}
			
		}
		);
		client.AddMessageListener(new MessageTakers() {

			public void OnlineMessage_(String FromMessage, String MessageBody) {
				System.out.println("You got a message from "+ FromMessage+">>>>>"+MessageBody);
				
			}
			
		}
		);
		
		
		if(!client.Connecting_Client()) {
			System.out.println("Connection failed");
		}
		else {
			System.out.println("Connection successful ");
			
			if(client.Login_User("fatma","fatma")==1) {
				System.out.println("Login successful ");
				client.Messages_Reader("Jim","hello");
			}
			else {
				System.out.println("Login failed ");
			}
			//client.login("fatma","hh");
			/////client.logoff();
			
		}
	}
	public void Messages_Reader(String sendTo, String MessageBody) throws IOException {
		String Line="msg"+sendTo+" "+MessageBody+"\n";
		data.serverOut.write(Line.getBytes());
		
	}

	public void Disconnecting() throws IOException {
		
		  String Line="logoff\n ";
			
			data.serverOut.write(Line.getBytes());
	}

	public int Login_User(String username,String password) throws IOException {
		  String Line="login "+username+" "+password+"\n";
		
			data.serverOut.write(Line.getBytes());
			String Server_Responser=data.bufferedIn.readLine();
			System.out.println("Response line "+ Server_Responser);
			
			if(" ok login".equalsIgnoreCase(Server_Responser)) {
				Message_Reading();
				return 1;
			}
			else if(" password Incorrect".equalsIgnoreCase(Server_Responser)) {
				return 2;
			}
			else if(" UserName Incorrect".equalsIgnoreCase(Server_Responser)) {
				return 3;
			}
			else if(" UserName AlreadyUsed".equalsIgnoreCase(Server_Responser)) {
				return 4;
			}
			else {
				return 5;
			}
			
			}
			
			public boolean Register_User(String username,String password) throws IOException {
				  String Line="login "+username+" "+password+"\n";
				
					data.serverOut.write(Line.getBytes());
					String Server_Responser=data.bufferedIn.readLine();
					System.out.println("Response line "+ Server_Responser);
					
					if(" ok Registered ".equalsIgnoreCase(Server_Responser)) {
						Message_Reading();
						return true;
					}
					else {
						return false;
					}
					
			
			
		
		
	
		
		
	}
	
	

		
		
	
		
		
	


	public void Message_Reading() {
		Thread thread=new Thread() { 
			public void run() {
				Messages_Reading_loop();
			}
		
		
	};
	thread.start();
	}

	protected void Messages_Reading_loop() {
		try {
			String line;
			while((line=data.bufferedIn.readLine())!=null ) {
				String [] CommandLine=StringUtils.split(line);
				if (CommandLine !=null&&CommandLine.length>0) {
					String cmd=CommandLine[0];
					if("Online".equalsIgnoreCase(cmd)) {
						Message_Handler_Online_Client(CommandLine);
					}
					else if("Offline".equalsIgnoreCase(cmd)) {
						Message_Handler_Offline_Client(CommandLine);
					}
					else if("msg".equalsIgnoreCase(cmd)) {
						String [] Message_Array=StringUtils.split(line,null,3); 
						Messages_Handler_Client(Message_Array);
					}
				
			}
		}} catch (IOException e) {
			
			e.printStackTrace();
			try {
				data.socket.close();
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
		}
		
	}

	public void Messages_Handler_Client(String[] Message_Array) {
		String login=Message_Array[1];
		String MainMessage=Message_Array[2];
		for(MessageTakers listener :data.Messagelisteners) {
			listener.OnlineMessage_(login, MainMessage);
		}
		
		
	}

	public void Message_Handler_Offline_Client(String[] Message_Array) {
		String login=Message_Array[1];
		for(UserActivity listener :data.userslisteners ) {
			listener.Check_Offline_User(login);
		}
		
	}

	public void Message_Handler_Online_Client(String[] Message_Array) {
		String login=Message_Array[1];
		for(UserActivity listener :data.userslisteners ) {
			listener.Check_Online_User(login);
		}
		
	}

	public boolean Connecting_Client() {
		
		try {
			ClientData_Conecction();
			
			return true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private void ClientData_Conecction() throws UnknownHostException, IOException {
		this.data.socket=new Socket(data.Name_of_Server, data.Port_Number);
		System.out.println("Client port is :"+data.socket.getLocalPort());
		this.data.serverOut=data.socket.getOutputStream();
		this.data.serverIn=data.socket.getInputStream();
		this.data.bufferedIn=new BufferedReader(new InputStreamReader(data.serverIn));
	}
	
	public void AddUserStatusListener(UserActivity listener) {
		 data.userslisteners.add(listener);
		
	}
	public void RemoveUserStatusListener(UserActivity listener) {
		 data.userslisteners.remove(listener);
		
	}
	public void AddMessageListener(MessageTakers listener) {
		 data.Messagelisteners.add(listener);
		
	}
	public void RemoveMessageListener(MessageTakers listener) {
		 data.Messagelisteners.remove(listener);
		
	}
	
}
