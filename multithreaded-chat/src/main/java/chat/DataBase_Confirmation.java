package chat;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class DataBase_Confirmation {

	public int verify(String UserName) throws FileNotFoundException {

		FileReader Freader = new FileReader("Users.txt");
		Scanner read = new Scanner("Users.txt");

		

			String line1;
			String line2;
			boolean Exists=false;
			while (read.hasNext()) {
				line1 = read.nextLine();
				line2 = read.nextLine();
				
				if (line1.equals(UserName)  ) {
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

		
}}
