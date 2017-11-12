package RegularLanguages.Operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.omg.CORBA.Current;

import RegularLanguages.FiniteAutomata;
import RegularLanguages.FiniteAutomata.FABuilder.IncompleteAutomataException;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidBuilderException;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidStateException;
import RegularLanguages.FiniteAutomata.FABuilder.InvalidSymbolException;

/**
 * Class that represents the DiSimone method to
 * transform a regular expression into a 
 * finite automata by constructing the expression
 * threaded tree
 */
public class DiSimone {
	
	private Node root; // root of the tree
	private String regex; // regex that it represents
	private String postOrderRegex; // regex in postfix notation
	private int nTerminals = 0; // quantity of terminals 
	private FiniteAutomata automata; // the equivalent automata
	
	/**
	 * Public constructor
	 * @param the regex associated to the tree
	 * @throws InvalidStateException 
	 */
	public DiSimone(String regex) throws InvalidStateException {
		this.regex = regex;
		this.postOrderRegex = getPostOrder(this.regex);
		this.root = createTree(this.postOrderRegex.toCharArray()); // Create DiSimone Tree
		this.automata = createStatesComposition();
	}
	
	/**
	 * Method to return the DiSimone object
	 * @return the object representing a Di Simone tree
	 */
	public DiSimone getTree() {
		return this;
	}
	
	/**
	 * Method to get the root of the tree
	 * @return the root of the tree
	 */
	public Node getRoot() {
		return this.root;
	}
	
	/**
	 * Method to get the regex in postorder
	 * @return the regex in post order 
	 */
	public String getPostOrderRegex() {
		return this.postOrderRegex;
	}
	
	/**
	 * Method that returns the quantity of
	 * leaf nodes (terminal symbols)
	 * @return the amount of leaf nodes
	 */
	public int getNumberOfTerminals() {
		return this.nTerminals;
	}
	
	
	/**
	 * Method to return the finite automata that
	 * represents a regular expression
	 * @return
	 */
	public FiniteAutomata getFA() {
		return this.automata;
	}
	
	/**
	 * Transforms Regular Expression to
	 * Reverse Polish Notation
	 * @param r the regex
	 * @return the regex converted to reverse polish notation
	 */
	private String getPostOrder(String r) {
		String s = "";
		Stack<Character> stack = new Stack<Character>();
		char c;
		for (int i = 0; i < r.length(); i++) {
			c = r.charAt(i);
			// If the scanned character is an operand, add it to output.
			if (Character.isLetterOrDigit(c)) {
				s += c;
			} else if (c == '(') { // If the scanned character is an '(', push it to the stack.
				stack.push(c);
			} else if (c == ')') { //  If the scanned character is an ')', pop and output from the stack 
	            					// until an '(' is encountered.
				while (!stack.isEmpty() && stack.peek() != '(') {
                    s += stack.pop();
				}
                if (!stack.isEmpty() && stack.peek() != '(') {
                    return "Invalid Expression"; // invalid expression                
                } else { 
                    stack.pop();
                }
			} else { // an operator is encountered
				while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
                    s += stack.pop();
				}
                stack.push(c);
			}
		} 
		// pop all the operators from the stack
        while (!stack.isEmpty()) {
            s += stack.pop();
        }
		return s;
	}
	
	/**
	 * Get the priority of an operator
	 * @param c the operator
	 * @return the operator priority 
	 */
	public static int precedence(char c) {
		switch (c) {
		case '|':
			return 1;
		case '.':
			return 2;
		case '*':
		case '?':
		case '+':
			return 3;
		
		}
		return -1;
		
	}
	
	/**
	 * Create a DiSimone Tree
	 * @param postfix the regex in postfix order
	 * @return the root of the tree
	 */
	public Node createTree(char postfix[]) {
		this.nTerminals = 0;
        
		Stack<Node> stack = new Stack<>();

        int closurePrecedence = precedence('*');
        for (int i = 0; i < postfix.length; i++) {
            Character c = postfix[i];
            Node node = new Node(c);

            if (Character.isLetterOrDigit(c)) { // operand
            	node.nodeNumber = ++this.nTerminals; // sets leaf number
            }
            if (precedence(c) >= 0) { // operator
                if (precedence(c) == closurePrecedence) {
                    Node nodoDaEsquerda = stack.pop();
                    node.left = nodoDaEsquerda;
                } else {
                    Node nodoDaDireita = stack.pop();
                    node.right = nodoDaDireita;

                    Node nodoDaEsquerda = stack.pop();
                    node.left = nodoDaEsquerda;
                }

                stack.push(node);

            } else {
            	stack.push(node);
            }
        }
        return stack.pop(); // Tree root
	}
	
	public Queue<Node> inOrder(Node node, Queue<Node> q) {
		//keep the nodes in the path that are waiting to be visited
        Stack<Node> stack = new Stack<Node>();

        //first node to be visited will be the left one
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
         
        // traverse the tree
        while (stack.size() > 0) {
           
            // visit the top node
            node = stack.pop();
            q.add(node);
            if (node.right != null) {
                node = node.right;
                 
                // the next node to be visited is the leftmost
                while (node != null) {
                    stack.push(node);
                    node = node.left;
                }
            }
        }
		return q;
	}
		
	/**
	 * In order traversal in a threaded tree
	 * @param node of the tree
	 * @param q queue with the traversal
	 * @return the queue q
	 */
	public Queue<Node> inOrderThreaded(Node node, Queue<Node> q) {
		if (node == null) {
			return null;
		}
		Node current = leftMost(node); // the leftmost node
		while (current != null) {
			q.add(current);
			// If this node is a thread node, then go to in order successor
			if (current.isThreaded == true) {
				current = current.right;
			} else {// Else go to the leftmost child in right subtree
				current = leftMost(current.right);
			}
		}
		return q;
	}
	
	/**
	 * Find the leftmost node in a tree rooted with node
	 * @param node the root of the tree
	 * @return the leftmost node
	 */
	public static Node leftMost(Node node) {
		while (node != null && node.left != null) {
			node = node.left;
		}
		return node;
	}
	
	
	/**
	 * Converts the tree to a threaded tree
	 * @param node 
	 */
	public void makeThreadedTree(Node node) {

        // Create a queue to store inorder traversal
        Queue<Node> q = new LinkedList<Node>();
  
        // Store inorder traversal in queue
        populateQueue(node, q);
        
        // Link NULL right pointers to inorder successor
        threadedUtil(node, q);
        
	}
	
	
	/**
	 * Helper function to put the Nodes inorder traversal
	 * traversal into the queue
	 * @param node
	 * @param queue
	 */
	private void populateQueue(Node node, Queue<Node> queue) {
		if (node == null) {
			return;
		}
		if (node.left != null) {
			populateQueue(node.left, queue);
		}
		queue.add(node);
		if (node.right != null) {
			populateQueue(node.right, queue);
		}
	}
	
	/**
	 * Traverse tree and make it threaded
	 * @param node
	 */
	private void threadedUtil(Node node, Queue<Node> queue) {
		if (node == null) {
			return;
		}
		if (node.left != null) { 
			threadedUtil(node.left, queue);
		}
		queue.remove();
		if (node.right != null) {
			threadedUtil(node.right, queue);
		} else {
			node.right = queue.peek();
			node.isThreaded = true;
		}
	}
	
	/**
	 * Create all the states composition
	 * @return the finite automata given by the regular expression
	 * @throws InvalidStateException
	 */
	public FiniteAutomata createStatesComposition() throws InvalidStateException {
		// Create builder and set the initial state
		FiniteAutomata.FABuilder fa = new FiniteAutomata.FABuilder();
		FiniteAutomata.State q0 = fa.newState();
		fa.setInitial(q0);

		// Convert B-Tree to a Threaded Tree
		makeThreadedTree(root);
		
		// Store the composition for each state
		HashMap<Set<Node>, FiniteAutomata.State> statesComposition = new HashMap<Set<Node>, FiniteAutomata.State>();
		// Nodes that need to be traversed
		List<Set<Node>> pendentNodes = new ArrayList<Set<Node>>();

		// First step: go down from root
		Set<Node> downFromRoot = downPath(root);
		statesComposition.put(downFromRoot, q0); // composition for q0
		pendentNodes.add(downFromRoot); // analyze q0 composition
		for (Node nd : downFromRoot) {
			if (nd.data == '$') {
				fa.setFinal(q0); // if lambda is found, q0 is final
				break;
			}
		}
		
		// Second step: create the rest of the compositions
		for (int i = 0; i < pendentNodes.size(); i++) {
			// Get a composition from the pendent ones for the state qi
			Set<Node> qiComposition = pendentNodes.get(i);
			// Input state is the one that already has a composition ready
			FiniteAutomata.State in = statesComposition.get(qiComposition);
			// For a given terminal symbol, get the UNION of their compositions
			HashMap<Character, Set<Node>> unionSymbolsComposition = new HashMap<Character, Set<Node>>();
			
			// Get the union of state compositions for a given symbol
			unionSymbolsComposition = getStateQiComposition
					(unionSymbolsComposition, qiComposition);
			
			// Associate compositions to FA states
			for (Character nodeSymbol : unionSymbolsComposition.keySet()) {
				// get composition created for the symbol nodeSymbol
				Set<Node> symbolComposition = unionSymbolsComposition.get(nodeSymbol);
				// Output state corresponds to the composition for the current symbol
				FiniteAutomata.State out = statesComposition.get(symbolComposition);
				if (out == null) {
					out = fa.newState(); // out state (in -> out)
					statesComposition.put(symbolComposition, out); // add the composition for out state
					pendentNodes.add(symbolComposition); // add the new compositions to be analyzed
					// Check if it is a final state
					for(Node nd : symbolComposition) {
						if (nd.data == '$') { // lambda
							fa.setFinal(out);
							break;
						}
					}
				}
				
				try {
					fa.addTransition(in, nodeSymbol, out);
				} catch (InvalidSymbolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		try {
			FiniteAutomata automata = fa.build();
			return automata; // return the automata
		} catch (IncompleteAutomataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Return the union of compositions for a given symbol
	 * @param unionSymbolsComposition a mapping for a given symbol to a set of nodes it reaches
	 * @param qiComposition the composition of the state qi
	 * @return the union of compositions for every symbol in state qi
	 */
	public HashMap<Character, Set<Node>> getStateQiComposition
			(HashMap<Character, Set<Node>> unionSymbolsComposition, Set<Node> qiComposition) {
		for (Node nd : qiComposition) {
			if (nd.data != '$') { // If symbol is not a lambda
				Set<Node> upComposition = upPath(nd); // Nodes reached by going up routine
				// Composition for the current terminal
				Set<Node> symbolComposition = unionSymbolsComposition.get(nd.data);
				if (symbolComposition != null) {
					symbolComposition.addAll(upComposition);
				} else { // if null
					// insert all the nodes reached (up routine) by nd input symbol
					unionSymbolsComposition.put(nd.data, new HashSet<Node>(upComposition));
				}
			}
		}
		return unionSymbolsComposition;
	}
		
	/**
	 * Traverse the tree according to the down routines
	 * for each operator + | ? * .
	 * @param  node the node from which the traversal begins
	 * @return the set of nodes reached by the current node
	 */
	public Set<Node> downPath(Node node) {
		Set<Node> down = new HashSet<>(); // the nodes reached by the up routine
		Node lambda = new Node('$');
        switch (node.data) {
            case '*':
            case '?': // go up and down
                down.addAll(downPath(node.left));
                if (node.right != null) {
                	down.addAll(upPath(node.right));
                } else {
                    down.add(lambda);
                }
                break;
            case '|': // go both to right and left
                down.addAll(downPath(node.right));
            case '+': // only to left
            case '.':
            	down.addAll(downPath(node.left));
                break;
            default:
            	down.add(node);
            	break;
            }
        return down;
	}

	/**
	 * Traverse the tree according to the up routines
	 * for each operator + | ? * .
	 * @param node the node from which the traversal begins
	 * @return the set of nodes reached by the current node
	 */
	public Set<Node> upPath(Node node) {
		Set<Node> up = new HashSet<>(); // the nodes reached by the up routine
		Node lambda = new Node('$');
		switch (node.data) { // which operator
            case '*': // down and up
            case '+':
                up.addAll(downPath(node.left));
            case '?': // up
                if (node.right != null) { // if it is threaded
                    up.addAll(upPath(node.right));
                } else { // points to lamda
                    up.add(lambda);
                }
                break;
            case '.': // go to right
                up.addAll(downPath(node.right));
                break;
            case '|': 
                Node rightNode = node.right; // ignore right subtree
                while (rightNode.right.right != null) {
                	rightNode = rightNode.right;
                }
                if (rightNode.right != null) {
                    up.addAll(upPath(rightNode.right));
                } else {
                    up.add(lambda);
                }
                break;
            default:
                if (node.right != null) {
                    up.addAll(upPath(node.right));
                } else {
                    up.add(lambda);
                }
                break;
            }
		return up;
        }
		
	/**
	 * Class that represents a node in the three
	 */
	public class Node {
		public Node left; // left child
		public Node right; // right child
		public Node threaded;
		public char data; // node data
		boolean isThreaded; // If right pointer is a normal right pointer or a pointer to inorder successor.
		public int nodeNumber; // number of terminal leaf node
		
		/**
		 * Public constructor
		 * @param data the data of the node
		 */
		public Node(char data) {
		 this.data = data;
		 this.left = this.right = null;
		 this.nodeNumber = -1;
		}
		
		@Override
		public boolean equals(Object n) {
			if(n == null || !Node.class.isAssignableFrom(n.getClass())) {
				return false;
			}
			Node other = (Node)n;
			if (other.data == this.data
					&& other.nodeNumber == this.nodeNumber) {
				return true;
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return this.nodeNumber + this.data;
		}
	}

}
