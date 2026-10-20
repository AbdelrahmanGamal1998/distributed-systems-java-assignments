package p2p;

public class Graph_Representation implements GraphServices {
	
	Node root;
	
	
	@Override
	public void AddConnections(int key, String value) {
		Node CreatedNode = new Node(key, value);
		
		if(root == null) { 
			root = CreatedNode;
			System.out.println("The new node inserted is : " + CreatedNode.key + " as the root."); 
		} else { 
			Node Node = root;
			Node Node_Parent;
			while(true) { 
				Node_Parent = Node;
				
				if (key < Node.key) {
					
					Node = Node.nodeLeft;
					if (Node == null) { 
						Node_Parent.nodeLeft = CreatedNode;
						System.out.println("Insert the new node : " + CreatedNode.key); 
						return;
					} 
				} else if (key > Node.key){ 
					Node = Node.nodeRight;
					if (Node == null) { 
						Node_Parent.nodeRight = CreatedNode;
						System.out.println("Insert the new node : " + CreatedNode.key); 
						return;
					}
				} else {
					System.out.println("Cannot insert duplicated node sorry..");
					return;
				}
			}
		}
	}
	
	@Override
	public int Size_Of_Graph(Node root){
		if(root==null){
			return 0;
		}
		return 1 + Size_Of_Graph(root.nodeLeft) + Size_Of_Graph(root.nodeRight);
	}
	
	
	
	@Override
	public void Printing_InOrder(Node currentNode) {
		if(currentNode != null) {
			Printing_InOrder(currentNode.nodeLeft);
			System.out.print(currentNode + " ");
			Printing_InOrder(currentNode.nodeRight);
		}
	}
	
		
		
	@Override
	public boolean Remove_From_Graph(int key) {
		if (root == null) {
			return false;
		}

		Node CurrentNode = root;
		Node Node_Parent = root;
		
		boolean CheckLeftOfTheNode_Child = true;
		
		
		while(CurrentNode.key != key) { 
			Node_Parent = CurrentNode;
			if(key < CurrentNode.key) { 
				CheckLeftOfTheNode_Child = true;
				CurrentNode = CurrentNode.nodeLeft;
			} else {
				CheckLeftOfTheNode_Child = false;
				CurrentNode = CurrentNode.nodeRight;
			}
			
			if(CurrentNode == null)
				return false;
		}
		
		if (CurrentNode.nodeLeft == null && CurrentNode.nodeRight == null) {
			if(CurrentNode == root) {
				root = null; 
			} 
			else if (CheckLeftOfTheNode_Child) { 
				Node_Parent.nodeLeft = null;
			} else { 
				Node_Parent.nodeRight = null;
			}
			
		}
		
		
		else if (CurrentNode.nodeRight == null) {
			if (CurrentNode == root) {
				root = CurrentNode.nodeLeft;
			} else if (CheckLeftOfTheNode_Child) {
				Node_Parent.nodeLeft = CurrentNode.nodeLeft;
			} else {
				Node_Parent.nodeRight = CurrentNode.nodeLeft;
			}
		}
		
		
		else if (CurrentNode.nodeLeft == null) {
			if (CurrentNode == root) {
				root = CurrentNode.nodeRight;
			} else if (CheckLeftOfTheNode_Child) {
				Node_Parent.nodeLeft = CurrentNode.nodeRight;
			} else {
				Node_Parent.nodeRight = CurrentNode.nodeRight;
			}
		}
		
		
		else {
			Node NodeReplacement = ReplaceRemoved(CurrentNode);
			
			if(CurrentNode == root) {
				root = NodeReplacement;
			} else if (CheckLeftOfTheNode_Child) {
				Node_Parent.nodeLeft = NodeReplacement;
			} else {
				Node_Parent.nodeRight = NodeReplacement;
			}
			
			NodeReplacement.nodeLeft = CurrentNode.nodeLeft;
		}
		System.out.println("The removed node is: " + key );
		return true;
	}
	
	
	private Node ReplaceRemoved(Node replacedNode) {
		
		Node replacementParent = replacedNode;
		Node replacement = replacedNode;
		
		Node CurrentNode = replacedNode.nodeRight;
		
		while(CurrentNode != null) {
			replacementParent = replacement;
			replacement = CurrentNode;
			CurrentNode = CurrentNode.nodeLeft;
		}
		
		if(replacement != replacedNode.nodeRight) {
			replacementParent.nodeLeft = replacement.nodeRight;
			replacement.nodeRight = replacedNode.nodeRight;
		}
		
		return replacement;
	}
	
	
	@Override
	public void Find_And_Search(int key) {
		Node CurrentNode = root;

		while (CurrentNode != null && CurrentNode.key != key) {
			CurrentNode = (key < CurrentNode.key) ? CurrentNode.nodeLeft : CurrentNode.nodeRight;
		}

		if (CurrentNode == null) {
			System.out.println("Not found.");
			return;
		}

		System.out.println("found!");
		System.out.println(CurrentNode.value);
	}

}
class Node {
	
	int key;
	String value;
	
	Node nodeLeft;
	Node nodeRight;
	
	Node(int key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String toString() {
		return "" + key;
	}
}