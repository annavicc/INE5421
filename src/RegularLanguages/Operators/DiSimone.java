package RegularLanguages.Operators;

import java.util.ArrayList;

public class DiSimone {
	
	private Node root;
	
	public DiSimone() {
		root = null;
	}
	

	public void createTree(String regex) {
		ArrayList<Character> left = new ArrayList<>();
		ArrayList<Character> right = new ArrayList<>();
		char current, before;
		int priority = 10;
		
		for (int i = 0; i < regex.length(); i++) {
			current = regex.charAt(i);
			if (Character.isLetter(current)
					|| (current == '?')
					|| (current == '*')
					|| (current == ')')) {
				left.add(current);
			}
		}
	}
	
	
	public DiSimone getTree() {
		return this;
	}
	
	
	
	public class Node {
		public Node left;
		public Node right;
		public Node father;
		public char data;
		public int priority;
		
		public Node(char data) {
		 this.data = data;
		}
	}

}
