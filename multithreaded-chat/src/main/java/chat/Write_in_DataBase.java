package chat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

public class Write_in_DataBase {

	
	public void Write(String username,String password) throws IOException {

		FileWriter Writer = new FileWriter("Accounts.txt", true);
		File f = new File("Accounts.txt");
		FileReader r = new FileReader(f);
		Scanner read = new Scanner(f);
		String s1 = null ;
		String x,y,s2 = null;
		while(read.hasNext()) {
			   
		             s1=read.nextLine();
		            s2 = read.nextLine();
		            if(s1.equals(username) ){
		                break;
		            }
		}
		y=s1;
		x = s2;
		  if (y.equals(username) ) {
			  String msg=" username already taken \n";
				//OutputStream.write(msg.getBytes());
			  System.out.println("username already taken");
		      
		  }
		 
		    else   {
		    	String msg=" ok Registered \n";
				//OutputStream.write(msg.getBytes());
		    	Writer.write(username);
				Writer.write("\n");
				Writer.write(password);
				Writer.write("\n");
			    Writer.close();
			    System.out.println(" ok Registered");
		 }   
		
		
	}




	
	public int Write2(String username,String password) throws FileNotFoundException {

		FileReader Freader = new FileReader("Accounts.txt");
		Scanner read = new Scanner("Accounts.txt");
		String line1;
		String line2;
		boolean Exists=false;
		while (read.hasNext()) {
			line1 = read.nextLine();
			line2 = read.nextLine();
			
			if (line1.equals(username)  ) {
				
				Exists=true;
			}
				break;
			}
		

		if (Exists) {

			return 1;

		}

		else {

			return 0;
		}

}


}