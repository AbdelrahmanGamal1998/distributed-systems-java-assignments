package p2p;

import java.rmi.Remote;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;


public interface Peer_Interface extends Remote {

	//Your main node
	void addoriginalnode(String node) throws RemoteException;
	
	//Printing key and value of your main node
	void Print_Key_Value_Map()throws RemoteException, Throwable;
	
	//getter for the hashmap
	 HashMap<Integer, String> getKeyValue_Map() throws RemoteException, Throwable;
	 
	//setter for the hashmap
	void setKeyValue_Map(HashMap<Integer, String> KeyValueMap) throws RemoteException, Throwable;
	
	//searching / finding
     int search(int key )throws RemoteException, Throwable;
     
     //your connected nodes
     void add_ConnectedNodes(String node1) throws RemoteException, Throwable;
     
     void Backup(String Backup) throws RemoteException, Throwable;
   //Adding key and value
   	 boolean Store_Key_Value() throws RemoteException, Throwable;
     
     //Removing one of the nodes
     void Remove_ConnectedNodes(String node2)  throws RemoteException, Throwable;
     
     //getter for the connected nodes
	ArrayList<String> getList_ConnectedNodes() throws RemoteException, Throwable;
	
	//setters for the connected nodes
	void setList_ConnectedNodes(ArrayList<String> list2) throws RemoteException, Throwable;
	
	//Maintaing Replication
	void mantain_Replication() throws RemoteException, Throwable;
	
	//Adding node in graph
	int AddNode_inGraph(int key, String value) throws RemoteException, Throwable;
	
	//Removing node from graph
	void RemoveNode_inGraph(int key)throws RemoteException, Throwable;

	//Searching inside graph
	void search_inGraph(int key)throws RemoteException, Throwable;
	
	//Printing your graph
	void printing_inGraph() throws RemoteException, Throwable;
	
	 
	 //decides the size of the graph to check max
	void getSize_inGraph() throws RemoteException, Throwable;

	//void Backup(String backup) throws RemoteException, Throwable;
	//void setNodeName() throws RemoteException, Throwable;
	
	String getNodeName() throws RemoteException, Throwable;

	void setNodeName(String nodeName) throws RemoteException, Throwable;
}
