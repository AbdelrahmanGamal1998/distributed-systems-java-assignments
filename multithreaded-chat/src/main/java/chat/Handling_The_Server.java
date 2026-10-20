package chat;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

public class Handling_The_Server extends Thread {

	
	Handling_The_ServerData data = new Handling_The_ServerData(null, null, new HashSet<String>(), new HashSet<String>(), new DataBase_Confirmation(),
			new Write_in_DataBase());
	public Handling_The_Server(Server server,Socket clientSocket) {
		this.data.server=server;
				
		this.data.clientSocket= clientSocket;
	}
 
	
	@Override	
	public void run() {
		try {
			handleClientSocket();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// Whatever ended the connection, stop sending to this client.
			data.server.Remove_worker_from_Server(this);
		}
	
    }
	
	private  void handleClientSocket() throws IOException, InterruptedException {
		String Command_Line;
		BufferedReader reader = ClientSocketData();
		while ((Command_Line=reader.readLine())!=null) {
			String [] ArrayCommand=StringUtils.split(Command_Line);
			if (ArrayCommand !=null&&ArrayCommand.length>0) {
				String cmd_Command=ArrayCommand[0];
			//System.out.println();
			
		/*	String msg=" Register or Login with existing user"+cmd+"\n";
			OutputStream.write(msg.getBytes());
			*/
			if("Exit".equalsIgnoreCase(cmd_Command)||"logoff".equalsIgnoreCase(cmd_Command)) {
				Disconnect_Handler();
				break;
			}
			
			//////////////////////////////////////////
			
		
			else if("login".equalsIgnoreCase(cmd_Command)) {
				Login_User_Handler(data.OutputStream,ArrayCommand);
				
				
			}
			else if("Reg".equalsIgnoreCase(cmd_Command)) {
				Register_User_Handler(data.OutputStream,ArrayCommand);
			}
			else if("msg".equalsIgnoreCase(cmd_Command)) {
				String [] Message_Array=StringUtils.split(Command_Line,null,3); 
				Messages_Details_Handler( Message_Array);
				}
			else if("join".equalsIgnoreCase(cmd_Command)) {
				//String [] tokensMsg=StringUtils.split(line,null,3); 
				Joining_Handler( ArrayCommand);
				}
		
			
			else if("leave".equalsIgnoreCase(cmd_Command)) {
				//String [] tokensMsg=StringUtils.split(line,null,3); 
				Disconnect_From_Server_Handler( ArrayCommand);
				}
			else if("Request".equalsIgnoreCase(cmd_Command)) {
				//String [] tokensMsg=StringUtils.split(line,null,3); 
				Messages_Details_Handler( ArrayCommand);
			}
			
			else {
				
				String UnknownComman=" unknown command   "+cmd_Command+"\n";
				data.OutputStream.write(UnknownComman.getBytes());
			}
		
		}
		}
		data.clientSocket.close();
	}


	private BufferedReader ClientSocketData() throws IOException {
		this.data.OutputStream =data.clientSocket.getOutputStream();
		InputStream InputStream =data.clientSocket.getInputStream();
		BufferedReader reader =new BufferedReader(new InputStreamReader(InputStream));
		String Command_Line;
		return reader;
	}
	
	






private void Disconnect_From_Server_Handler(String[] CommandArray) {
	if (CommandArray.length>1) {
		String title =CommandArray[1];
		data.Title_set.remove(title);
	}
}


public boolean Same_Title(String Title) {
	return data.Title_set.contains(Title);
	
}
	
	private void Joining_Handler(String[] CommandArray) {
		if (CommandArray.length>1) {
			String title =CommandArray[1];
			data.Title_set.add(title);
		
		}
	}
	

		



	//format "msg" "login"  msg....
	//format "msg" "#topic" msg....
	private void Messages_Details_Handler(String[] CommandLine) throws IOException {
		String sendTo=CommandLine[1];
		String body=CommandLine[2];
	
		boolean Same_title=sendTo.charAt(0)=='#';
		
		List<Handling_The_Server>ServerHandler_worker=data.server.getWorkerList();
		for(Handling_The_Server worker:ServerHandler_worker) {

			if( sendTo.equalsIgnoreCase("all")) {
				if(body.equalsIgnoreCase("shit")||body.equalsIgnoreCase("nigga")||body.equalsIgnoreCase("hell")||body.equalsIgnoreCase("bullshit")||body.equalsIgnoreCase("fuck")) {
					
					
					BlockedWords(worker);
					break;
					
			    
			   // String outmsg="Warnning to User "+ login +" \n";	
			   	
				}
				else {
				String The_output_Message="msg "+sendTo+":"+ data.login +" "+body +"\n";
				worker.Sending_Message(The_output_Message);
			}
				}
		   else if(Same_title) {
				if(worker.Same_Title(sendTo)) {
					String The_output_Message="msg "+sendTo+":"+ data.login +" "+body +"\n";
					worker.Sending_Message(The_output_Message);
				}
				
			}
			
		   else if(body.equalsIgnoreCase("request")) {
			   if(sendTo.equals(worker.Login_User())) {
				   String outmsg="msg from :"+data.login+" wants to request you "+"\n";
					worker.Sending_Message(outmsg);
			  
		   }
		   }
		   else if(body.equalsIgnoreCase("Accept")) {
			   if(sendTo.equals(worker.Login_User())) {
				   String The_output_Message="user:"+data.login+" Accepted your Request "+"\n";
					worker.Sending_Message(The_output_Message);
			 
		   }}
		   else if(body.equalsIgnoreCase("Reject")) {
			   if(sendTo.equals(worker.Login_User())) {
				   String The_output_Message="user:"+data.login+" rejected your Request "+"\n";
					worker.Sending_Message(The_output_Message);
		            //handlinglogoff();
		   }
		   }
			else  {
			if(sendTo.equals(worker.Login_User())) {
				String The_output_Message="msg "+data.login+" "+body +"\n";
				worker.Sending_Message(The_output_Message);
			
			
			}
		}
	}
	}


	private void BlockedWords(Handling_The_Server worker) throws IOException {
		String Message=" Warning :"+"1"+ data.login ;
		data.OutputStream.write(Message.getBytes());
		worker.Sending_Message(data.login+ " : "+ "************");
		Disconnect_Handler();
	}


	private void Disconnect_Handler() throws IOException {
		data.server.Remove_worker_from_Server(this);
		List<Handling_The_Server>Server_Handling_Worker_List=data.server.getWorkerList();
		//send other online users current users status
		String Message="offline "+data.login+"\n";
		for(Handling_The_Server worker:Server_Handling_Worker_List) {
			if(!java.util.Objects.equals(data.login, worker.Login_User())) {
			worker.Sending_Message(Message);
			}
		}
		System.out.print(data.login+" is offline "+"\n");
		data.clientSocket.close();
	}


	public String Login_User() {
		return data.login;
	}
	public String Register_User() {
		return data.Register;
	}


	
	
	private void Register_User_Handler(OutputStream OutputStream, String[] Command_Line) throws IOException {
	if(Command_Line.length==3) {
		String UserName=Command_Line[1];
		String password=Command_Line[2];
		//Write_in_DataBase write=new Write_in_DataBase();

		FileWriter Writer = new FileWriter("Users.txt", true);
		File f = new File("Users.txt");
		FileReader r = new FileReader(f);
		Scanner read = new Scanner(f);
		String s1 = null ;
		String x,y,s2 = null;
		while(read.hasNext()) {
			   
		             s1=read.nextLine();
		            s2 = read.nextLine();
		            if(s1.equals(UserName) ){
		                break;
		            }
		}
		y=s1;
		x = s2;
		  if (y.equals(UserName) ) {
			  String msg=" username already taken \n";
				OutputStream.write(msg.getBytes());
			  System.out.println("username already taken");
		      
		  }
		 
		    else   {
		    	String msg=" ok Registered ";
				OutputStream.write(msg.getBytes());
		    	Writer.write(UserName);
				Writer.write("\n");
				Writer.write(password);
				Writer.write("\n");
			    Writer.close();
			    System.out.println(" ok Registered");
		 }   
		
		
	}
					
			
					//write.Write(UserName, password);
	
	}

		
		
	
		
		
	
	
		
	
	
	/*private void handlelogin(OutputStream OutputStream, String[] tokens) throws IOException {
		if(tokens.length==3) {
			String login=tokens[1];
			String password=tokens[2];
			
			if(login.equals("guest")&&password.equals("guest")||login.equals("jim")&&password.equals("jim")||login.equals("fatma")&&password.equals("fatma")||login.equals("a")&&password.equals("a")) {
				
				
				
				
				
				
				
				
				String msg=" ok login\n";
				OutputStream.write(msg.getBytes());
				this.login=login;
			
				System.out.print('\n');
				
				System.out.print("User logged in Successfully : "+login+'\n');
				
				
				List<ServerWorker>workerList=server.getWorkerList();
				//send current user all other online logings
				for(ServerWorker worker:workerList) {
					if(!login.equals(worker.getlogin())) {
						if(worker.getlogin()!=null) {
					String msg2="Online "+worker.getlogin()+"\n";
					send(msg2);
						}
					
					}	
				}
				
				//send other online users current users status
				String onlinemsg="Online "+login+"\n";
				for(ServerWorker worker:workerList) {
					if(!login.equals(worker.getlogin())) {
					worker.send(onlinemsg);
					}
				}
				
			}
			else {
				String msg=" error login\n";
				OutputStream.write(msg.getBytes());
			}
		}}

*/private void Login_User_Handler(OutputStream OutputStream, String[] Command_Line) throws IOException {
	if(Command_Line.length==3) {
	String login=Command_Line[1];
	String password=Command_Line[2];
	
	File f = new File("Users.txt");
	FileReader r = new FileReader(f);
	Scanner read = new Scanner(f);
	String s1 = null ;
	String x,y,s2 = null;
	while(read.hasNext()) {
		   
	             s1=read.nextLine();
	             s2 = read.nextLine();
	            if(s1.equalsIgnoreCase(login)&&s2.equals(password) ){
	                break;
	            }
	            else if(!s1.equalsIgnoreCase(login)&&s2.equals(password) ) {
	            	break;
	            }
	            else if(s1.equalsIgnoreCase(login)&& !s2.equals(password) ) {
	            	break;
	            }
	}
	y=s1;
	x = s2;
	  if (y.equals(login)&&x.equals(password) ) {
		  String msg=" ok login\n";
			OutputStream.write(msg.getBytes());
			this.data.login=login;
		
			System.out.print('\n');
			
			System.out.print("User logged in Successfully : "+login+'\n');
			
			
			List<Handling_The_Server>Server_Handler_Worker_List=data.server.getWorkerList();
			//send current user all other online logings
			for(Handling_The_Server worker:Server_Handler_Worker_List) {
				if(!login.equals(worker.Login_User())) {
					if(worker.Login_User()!=null) {
				String msg2="Online "+worker.Login_User()+"\n";
				Sending_Message(msg2);
					}
				
				}	
			}
			
			//send other online users current users status
			String onlinemsg="Online "+login+"\n";
			for(Handling_The_Server worker:Server_Handler_Worker_List) {
				if(!login.equals(worker.Login_User())) {
				worker.Sending_Message(onlinemsg);
				}
			}
			
		}
	
	  else if(y.equals(login) && !x.equals(password)) {
		//   System.out.print(x);
		  // System.out.print(password);
		   //System.out.print(!x.equals(password));
		   String msg=" password Incorrect\n";
			OutputStream.write(msg.getBytes());
			
			System.out.println(" password Incorrect \n");
	  }
	 else if(!y.equals(login)&&x.equals(password)) {
		  String msg=" username Incorrect\n";
			OutputStream.write(msg.getBytes());
	
			System.out.println(" UserName Incorrect \n");
	  }
	else {
		  String msg=" UserName AlreadyUsed\n";
			OutputStream.write(msg.getBytes());
			
			System.out.println(" Error in login  \n");
	  }
	      
	  }
	  
	  
}  
	  
	private void Sending_Message(String Message) throws IOException {
		if(data.login !=null) {
			try {
				data.OutputStream.write(Message.getBytes());
			} catch (IOException clientWentAway) {
				// The receiver disconnected; it is removed when its own connection ends. Do not fail the sender.
			}
		}
	}
}