package p2p;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import java.rmi.registry.Registry;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PeerMain {
	
	static String stubname = null;
	private final static int ITERATIONS = 4;

	public static void main(String args[]) throws Throwable {

		int fail = 0;
		int search_times = 0;
		Integer a, b;
	////////////////////////////////////////////////////////////////////////////	
		
		System.out.print("Enter a stub name: ");
		Scanner sc = new Scanner(System.in);
		stubname = sc.nextLine();
		setStubname(stubname);
		
		///////////////////////////////////////////////////////////////////
		
		//Starting the peer

		server_bind(stubname, getReg());
		Peer_Interface peerObject = (Peer_Interface) getReg().lookup(stubname);
		
////////////////////////////////////////////////////////////////////////////////////////////////
		
		System.out.print("Enter original node name: ");
		Scanner sc1 = new Scanner(System.in);
		String originalnode = sc.nextLine();
		peerObject.addoriginalnode(originalnode);
		peerObject.addoriginalnode(originalnode + "_Replicated");
		peerObject.setNodeName(originalnode);
		
/////////////////////////////////////////////////////////////////////////////////////////////////
		//Saving peers Info
		Write(stubname, originalnode);
		
//////////////////////////////////////////////////////////////////////////////////////////////////
		
		int choice;
		String stubname = get_stubname_from_file("1");

		boolean bExist = true;
		while (bExist) {
			System.out.println("\n ************ Main Menu ***************");
			System.out.println("1. Storing keys and values:");
			System.out.println("2. Add Connected Nodes:");
			System.out.println("3. Remove one of the nodes: ");
			System.out.println("4. Search : ");
			System.out.println("5.  Printing current hashmap: ");
			System.out.println("6. calculate average responce time of search : ");
			System.out.println("7. calculate average responce time of search with different way : ");
			System.out.println("8. Exit loop: ");
			System.out.println("9. printing duplicated node : ");
			System.out.println("10. Failure Rate : ");
			System.out.println("11. Average response time for your current search operations : ");
			System.out.println("**************************************");
			System.out.println("Select your choice");
			choice = Integer.parseInt(sc.nextLine());
			switch (choice) {
			case 1:

				if (peerObject.Store_Key_Value() == false) {

				

					for (int i = 0; i < peerObject.getList_ConnectedNodes().size(); i++) {
						// String stub1=;
						System.out.print((peerObject.getList_ConnectedNodes().get(i)));
					}
					for (int i = 0; i < peerObject.getList_ConnectedNodes().size(); i++) {

						AddInAnotherNode(get_stubname_from_file(peerObject.getList_ConnectedNodes().get(i)));
					}

				}

				else {
					peerObject.Store_Key_Value();
					System.out.println("Replication technique to prevent failure...");

					

					getStubObject(get_stubname_from_file(originalnode + "_Replicated")).Store_Key_Value();
					

				}

				break;
				
			case 2:

				System.out.print("Enter connected node name: ");

				String Connectednodes = sc.nextLine();
				PeerServant ps=new PeerServant();
				ps.add_ConnectedNodes(Connectednodes);
		   //	ps.add_ConnectedNodes(originalnode + "_Replicated");

				
				break;

			case 3:
				System.out.print("Enter node to be removed: ");
				
				String Removednodes = sc.nextLine();

				peerObject.Remove_ConnectedNodes(Removednodes);
				//getStubObject(originalnode + "_Replicated").Remove_ConnectedNodes(Removednodes);
		
				break;

			case 4:
				
				  long lStartTime = Instant.now().toEpochMilli();

		      
				Scanner scan1 = new Scanner(System.in);

				search_times++;
				Writer wr1 = new FileWriter("searchdatabase.txt", true);
				wr1.write(1 + "");
				wr1.write("\n");
				wr1.close();
				System.out.print("Enter the key");
				int key = Integer.parseInt(scan1.nextLine());
				if (peerObject.search(key) == 0) {
					
					fail++;

					Writer wr = new FileWriter("failuredatabase.txt", true);
					wr.write(1 + "");
					wr.write("\n");
					wr.close();

					System.out.print("\n");
					System.out.print(" node :" + originalnode + " failed on searching key :" + key);
					System.out.print("\n");
					System.out.print("fail counter");
					System.out.print(fail);
					for (int i = 0; i < peerObject.getList_ConnectedNodes().size(); i++) {
						// String stub1=;
						System.out.print((peerObject.getList_ConnectedNodes().get(i)));
					}
					
					for (int i = 0; i < peerObject.getList_ConnectedNodes().size(); i++) {

						Search(get_stubname_from_file(peerObject.getList_ConnectedNodes().get(i)), key);
						System.out.print(peerObject.getList_ConnectedNodes().get(i));

					}

				}
				else {
					peerObject.search(key);
					System.out.print("\n");
					System.out.print(peerObject.getNodeName());
				}
					 
				
				
				long lEndTime = Instant.now().toEpochMilli();

		        long output = lEndTime - lStartTime;

		       
				Writer wr = new FileWriter("responsetime.txt", true);
				wr.write(output+ "");
				wr.write("\n");
				wr.close();

				break;
			case 5:
				peerObject.Print_Key_Value_Map();
				break;

			case 8:

				bExist = false;
				
				break;
			case 6:
				Scanner scan11 = new Scanner(System.in);

				
				
				long start = System.currentTimeMillis();
				for (int i = 0; i < ITERATIONS; ++i) {
					
					peerObject.search(5);
				}
				long elapsed = (System.currentTimeMillis() - start) / 1000000;
				long average = elapsed / ITERATIONS;
				System.out.println("\n");
				System.out.println("averach search time ");
				System.out.println("\n");
				System.out.println(average + " millis second ");
				break;
			case 7:
				long startTime1 = System.nanoTime();
				for (int i = 0; i < ITERATIONS; ++i) {
					// remoteMath.search(key_response_time);
					peerObject.search(1);
				}
				long endTime1 = System.nanoTime();

				long duration1 = (endTime1 - startTime1) ;
				System.out.println("\n");
				System.out.println(duration1 + " nano second ");
				break;

			case 9:
				Replication(originalnode);
				peerObject.Backup(originalnode + "_Replicated");    
				System.out.println(peerObject.getKeyValue_Map());
				System.out.println(" Duplicated node ");

				break;

			case 10:

				float noOfLines = 0;
				int sum = 0;
				try (BufferedReader reader = new BufferedReader(new FileReader("failuredatabase.txt"))) {
					while (reader.readLine() != null) {
						noOfLines++;
					}

				}

				float noOfLines1 = 0;

				try (BufferedReader reader = new BufferedReader(new FileReader("searchdatabase.txt"))) {
					while (reader.readLine() != null) {
						noOfLines1++;
					}
					System.out.println("Average fail rate ....  ");

					System.out.println(noOfLines / (noOfLines1 + noOfLines));
				}

				break;
			case 11:
				 CalculateAverage();
				break;

			}
		}

	}

	public static void server_bind(String stubname, Registry registry1)
			throws RemoteException, AlreadyBoundException, AccessException {
		Peer_Interface remoteMath = new PeerServant();
		Registry registry2 = LocateRegistry.getRegistry();
		registry2.bind(stubname, remoteMath);
	}

	public static void clientt_lookup(String stubname, Registry registry)
			throws RemoteException, NotBoundException, AccessException {
		Peer_Interface remoteMath = (Peer_Interface) registry.lookup(stubname);

	}

	public static void Search(String stubname, int key) throws Throwable {
		Peer_Interface remoteMath = (Peer_Interface) getReg().lookup(stubname);
		remoteMath.search(key);
	}

	public static void AddInAnotherNode(String stubname) throws Throwable {
		Peer_Interface remoteMath = (Peer_Interface) getReg().lookup(stubname);
		remoteMath.Store_Key_Value();
	}

	public static void Write(String Stubname, String Node) throws IOException {

		FileWriter fw = new FileWriter("hashmap.txt", true);
	
		fw.write(stubname);
		fw.write("\n");
		fw.write(Node);
		fw.write("\n");

		fw.close();
	}

	public static String get_stubname_from_file(String Node) throws IOException {
		File f = new File("hashmap.txt");
		FileReader r = new FileReader(f);
		Scanner read = new Scanner(f);
		String s1 = null;
		String x, y, s2 = null;
		while (read.hasNext()) {

			s1 = read.nextLine();
			s2 = read.nextLine();
			if (s2.equals(Node)) {
				break;
			}
		}
		y = s1;
		x = s2;
		if (x.equals(Node)) {

			System.out.print("stubname---------" + y);

		}
		return y;
	}

	public static Peer_Interface getStubObject(String stubname) throws Throwable {

		Peer_Interface remoteMath = (Peer_Interface) getReg().lookup(stubname);
		return remoteMath;
	}

	public static void Replication(String originalnode) throws AlreadyBoundException, NotBoundException, IOException {

		String NewStubName = stubname + "_Replicated ";
		String Neworiginalnode = originalnode + "_Replicated";
		server_bind(NewStubName, getReg());

		

	}
	
	public static String getStubname() {
		return stubname;
	}

	public static void setStubname(String stubname) {
		PeerMain.stubname = stubname;
	}

	public static Registry getReg() throws RemoteException {
		Registry registry = LocateRegistry.getRegistry("localhost");
		return registry;
	}
	
	
	public static void  CalculateAverage() throws IOException {
		float noOfLines2 = 0;
		
		try (BufferedReader reader = new BufferedReader(new FileReader("responsetime.txt"))) {
			while (reader.readLine() != null) {
				noOfLines2++;
			}

		}
		
		Scanner s = null;
		
		
		        double sum = 0;
		
		        try {
	
		             
		
		             s = new Scanner(
		
		                    new BufferedReader(new FileReader("responsetime.txt")));
		
		            
		
		            
		
		            while (s.hasNext()) {
		
		                
		
		               if (s.hasNextDouble()) {
		
		                        sum += s.nextDouble();
		                        double avg=sum/noOfLines2;
		
		                    } else {
		
		                        s.next();
		
		                    }  
		
		            }
		
		      } finally {
		
		            s.close();
		
		       }
		
		        
		
		        System.out.println(sum);
		
		    }

	}

