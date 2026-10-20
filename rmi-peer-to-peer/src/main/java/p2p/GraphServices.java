package p2p;

public interface GraphServices {

	void AddConnections(int key, String value);

	int Size_Of_Graph(Node root);

	void Printing_InOrder(Node currentNode);

	boolean Remove_From_Graph(int key);

	void Find_And_Search(int key);

}