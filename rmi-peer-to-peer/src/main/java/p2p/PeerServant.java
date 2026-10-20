package p2p;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;




public class PeerServant extends UnicastRemoteObject implements Peer_Interface {
	
	protected PeerServant() throws RemoteException {
		super();
	
	}
	/////////////////////////////////////////////////////////////////////////////////////
	
	private static final long serialVersionUID = 1L;
	HashMap<Integer, String> KeyValue_hashMap = new HashMap<Integer, String>();
	private String NodeName;   
	static Graph_Representation bstObject=new Graph_Representation ();
	static ArrayList<String> connected_List=new ArrayList<String>();
	static ArrayList<String> Backup_List=new ArrayList<String>();
	static int max_capacity=3;
	
	
	public HashMap<Integer, String> getKeyValue_Map() {
		return KeyValue_hashMap;
	}

	public void setKeyValue_Map(HashMap<Integer, String> keyValueMap) {
		this.KeyValue_hashMap = keyValueMap;
	}
	
	public static int getMax_capacity() {
		return max_capacity;
	}

	public static void setMax_capacity(int max_capacity) {
		PeerServant.max_capacity = max_capacity;
	}
	
	
//////////////////////////////////////////////////////////////////////////////////////
	
	public void addoriginalnode(String node) throws RemoteException {
		ArrayList<String> list=new ArrayList<String>();
		  list.add(node);
		  for (int i = 0; i < list.size(); i++) {
	            System.out.print(list.get(i) + " ");}
		  System.out.print("\n");

		  
	}
	
	 
	
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
		public boolean Store_Key_Value() throws Throwable {
			
			if(KeyValue_hashMap.size()>max_capacity){
				System.out.print("Reached max size");
				System.out.print("you can not add more nodes in this peer");
				return false;
			}
			else {
			boolean loopAgain = true;
			Scanner scan = new Scanner(System.in);
			
			while(loopAgain) {
				
				System.out.print("Enter key :");
				Integer KeyNumber = Integer.parseInt(scan.nextLine());

			
				System.out.print("Enter value :");
				String value = scan.nextLine();

				

				String oldVal = KeyValue_hashMap.put(KeyNumber, value);

				if (oldVal!=null) {
					System.out.println("key Number : " + KeyNumber + " is "
							+ oldVal + " and has been overwritten by " + value);
				}

			
				System.out.print("Enter another Key and value (y/n)?");
				String answer = scan.nextLine();
				BufferedWriter bf = null;
				 if (answer.equals("n") || answer.equals("N")) {
					break;
				}
				 else {
			
	            try {
					bf = new BufferedWriter(new FileWriter("hashmapNew.txt",true));
					for (Map.Entry<Integer, String> entry : KeyValue_hashMap.entrySet()) {
						bf.write(entry.getKey() + ":" + entry.getValue());	
						bf.newLine();
					}
					bf.flush();
				} catch (Exception e) {
					
				}
	            finally{
	           
	                try{
	                   
	                    bf.close();
	                }catch(Exception e){}
	            }
	         	read_hashmap() 	;
	            

				if (answer.equals("y") || answer.equals("Y")) {
				if(KeyValue_hashMap.size()>max_capacity) {
					System.out.print("Reached max size");
					System.out.print("you can not add more nodes in this peer");
					break;
				}
				else {
					continue;
				}} else if (answer.equals("n") || answer.equals("N")){
					break;
				}
				 }
		} 
			   
			System.out.print(KeyValue_hashMap);
			setKeyValue_Map(KeyValue_hashMap) ;
			
			}
			return false;
			
			}
				
		
		
	


public void mantain_Replication() {
	HashMap<Integer, String> new_hash_map = new HashMap<Integer, String>(); 
    new_hash_map.putAll(KeyValue_hashMap);
    System.out.print(new_hash_map);
}



public void Print_Key_Value_Map() {
	System.out.print(getKeyValue_Map());
}

public void read_hashmap() throws IOException{
    String filePath = "hashmapNew.txt";
    HashMap<String, String> map = new HashMap<String, String>();

    String line;
    BufferedReader reader = new BufferedReader(new FileReader(filePath));
    while ((line = reader.readLine()) != null)
    {
        String[] parts = line.split(":", 2);
        if (parts.length >= 2)
        {
            String key = parts[0];
            String value = parts[1];
            map.put(key, value);
     
        } else {
            System.out.println("ignoring line: " + line);
        }
    }

    for (String key : map.keySet())
    {
        System.out.println(key + ":" + map.get(key));
    }
    reader.close();
}

public int search(int key ) {
	
		
		if(KeyValue_hashMap.containsKey(key)) {
		String value = KeyValue_hashMap.get(key); 
		System.out.print(" value of this key is  found ");
		System.out.print(value);
		
		return 1;
		}
		else
			
			System.out.print("not found");
	
			return 0;
		


}

public void add_ConnectedNodes(String nodes) throws RemoteException, Throwable {
	
	  connected_List.add(nodes);
	  //setList_ConnectedNodes(connected_List);
	  for (int i = 0; i < connected_List.size(); i++) { 
		  System.out.print(connected_List.get(i) + " ");
	  }
	  
}

public void Remove_ConnectedNodes(String node2) throws RemoteException, Throwable {
	connected_List.remove(node2);
	
}



@Override
public ArrayList<String> getList_ConnectedNodes() throws RemoteException, Throwable {
	
	return connected_List;
}

@Override
public void setList_ConnectedNodes(ArrayList<String> list2) throws RemoteException, Throwable {
	this.connected_List = list2;
	
}

@Override
public int AddNode_inGraph(int key,String value)throws RemoteException, Throwable {
if( bstObject.Size_Of_Graph(bstObject.root)<4) {
	bstObject.AddConnections(key, value);
	return 1;}
else {
	return 0;
}
}

@Override
public void RemoveNode_inGraph(int key) throws RemoteException, Throwable {
	bstObject.Remove_From_Graph(key);
	
}

@Override
public void search_inGraph(int key) throws RemoteException, Throwable {
	bstObject.Find_And_Search(key);
	
}

@Override
public void printing_inGraph() throws RemoteException, Throwable {
	bstObject.Printing_InOrder(bstObject.root);
	
}

@Override
public void getSize_inGraph() throws RemoteException, Throwable {
	bstObject.Size_Of_Graph(bstObject.root);
	
}

@Override
public void Backup(String nodes) throws RemoteException, Throwable {
	  Backup_List.add(nodes);
	  //setList_ConnectedNodes(connected_List);
	  for (int i = 0; i < Backup_List.size(); i++) { 
		  System.out.print(Backup_List.get(i) + " ");
	  }
	
}

@Override
public void setNodeName(String nodeName) throws RemoteException, Throwable {
	this.NodeName=nodeName;
	
}

@Override
public String getNodeName() throws RemoteException, Throwable {
	
	return NodeName;
}




}