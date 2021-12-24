//ver 0.99
// with random mazes, but no ui

package mazeTester;

import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

//TODO make Maze class that creates random 20 cell mazes
//make solve method a member of maze class

class GraphNode {
	int id;
	List<GraphNode> children = new ArrayList<>();
	boolean isEntry = false;
	boolean isExit = false;

	public GraphNode(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return ((Integer) id).toString();
	}
}

class Maze {

	List<GraphNode> mazeNodes = new ArrayList<>();
	List<Integer> validEntryAndExit = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 9, 10, 14, 15, 16, 17, 18, 19));


	public Maze() {
		for(int i = 0; i < 20; i++) {
			mazeNodes.add(new GraphNode(i + 1));
		}

		mazeNodes.get(2).isEntry = true;
		mazeNodes.get(15).isExit = true;
		mazeNodes.get(9).isExit = true;

		connect(mazeNodes, 1, 2);
		connect(mazeNodes, 2, 3);
		connect(mazeNodes, 3, 4);
		connect(mazeNodes, 4, 5);
		connect(mazeNodes, 1, 6);
		connect(mazeNodes, 3, 8);
		connect(mazeNodes, 4, 9);
		connect(mazeNodes, 7, 8);
		connect(mazeNodes, 6, 11);
		connect(mazeNodes, 7, 12);
		connect(mazeNodes, 9, 14);
		connect(mazeNodes, 10, 15);
		connect(mazeNodes, 11, 12);
		connect(mazeNodes, 14, 15);
		connect(mazeNodes, 13, 18);
		connect(mazeNodes, 15, 20);
		connect(mazeNodes, 16, 17);
		connect(mazeNodes, 17, 18);
		connect(mazeNodes, 18, 19);
		connect(mazeNodes, 19, 20);
	}

	public Maze(long seed) {
		// construct a maze with random entries and exits

		final int MAX_NUMBER_OF_EXITS = 3;
		for(int i = 0; i < 20; i++) {
			mazeNodes.add(new GraphNode(i + 1));
		}

		Random random = new Random();
		random.setSeed(seed);

		int numOfExits = random.nextInt(MAX_NUMBER_OF_EXITS)+1;

		// set random entry
		int entry = random.nextInt(validEntryAndExit.size() - 1);
		mazeNodes.get(validEntryAndExit.get(entry)).isEntry = true;

		// set random exit
		for(int i = 0; i < numOfExits; i++) {
			int exit = random.nextInt(validEntryAndExit.size() - 1);
			while(mazeNodes.get(validEntryAndExit.get(exit)).isEntry)
				exit = random.nextInt(validEntryAndExit.size() - 1);
			mazeNodes.get(validEntryAndExit.get(exit)).isExit = true;
		}
		
		for(int i = 0; i < 20; i++) {
			final int CHILDREN_VARIATIONS = 16;
			int nodeToConnect = random.nextInt(mazeNodes.size())+1;		// random 1-20
			int childrenMask = random.nextInt(CHILDREN_VARIATIONS);		// 0-15
			boolean isTop = nodeToConnect >= 1 && nodeToConnect <=5;
			boolean isBottom = nodeToConnect >= 16 && nodeToConnect <=20;
			boolean isLeft = nodeToConnect == 1 || nodeToConnect == 6 || nodeToConnect == 11 || nodeToConnect == 16;
			boolean isRight = 	nodeToConnect == 5 || nodeToConnect == 10 || nodeToConnect == 15 || nodeToConnect == 20;
			
			if(isTop)
				childrenMask = childrenMask & 14;
			if(isBottom)
				childrenMask = childrenMask & 13;
			if(isLeft)
				childrenMask = childrenMask & 11;
			if(isRight)
				childrenMask = childrenMask & 7;
			
			switch (childrenMask) {
			case 0:
				break;
			case 1:
				connect(mazeNodes, nodeToConnect, nodeToConnect - 5);
				break;
			case 2:
				connect(mazeNodes, nodeToConnect, nodeToConnect + 5);
				break;
			case 3:
				connect(mazeNodes, nodeToConnect, nodeToConnect - 5);
				connect(mazeNodes, nodeToConnect, nodeToConnect + 5);
				break;
			case 4:
				connect(mazeNodes, nodeToConnect, nodeToConnect - 1);
				break;
			case 5:
				connect(mazeNodes, nodeToConnect, nodeToConnect - 5);
				connect(mazeNodes, nodeToConnect, nodeToConnect - 1);
				break;
			case 6:
				connect(mazeNodes, nodeToConnect, nodeToConnect + 5);
				connect(mazeNodes, nodeToConnect, nodeToConnect - 1);
				break;
			case 7:
				connect(mazeNodes, nodeToConnect, nodeToConnect - 5);
				connect(mazeNodes, nodeToConnect, nodeToConnect + 5);
				connect(mazeNodes, nodeToConnect, nodeToConnect - 1);
				break;
			case 8:
				connect(mazeNodes, nodeToConnect, nodeToConnect + 1);
				break;
			case 9:
				connect(mazeNodes, nodeToConnect, nodeToConnect - 5);
				connect(mazeNodes, nodeToConnect, nodeToConnect + 1);
				break;
			case 10:
				connect(mazeNodes, nodeToConnect, nodeToConnect + 5);
				connect(mazeNodes, nodeToConnect, nodeToConnect + 1);
				break;
			case 11:
				connect(mazeNodes, nodeToConnect, nodeToConnect - 5);
				connect(mazeNodes, nodeToConnect, nodeToConnect + 5);
				connect(mazeNodes, nodeToConnect, nodeToConnect + 1);
				break;
			case 12:
				connect(mazeNodes, nodeToConnect, nodeToConnect - 1);
				connect(mazeNodes, nodeToConnect, nodeToConnect + 1);
				break;
			case 13:
				connect(mazeNodes, nodeToConnect, nodeToConnect - 5);
				connect(mazeNodes, nodeToConnect, nodeToConnect - 1);
				connect(mazeNodes, nodeToConnect, nodeToConnect + 1);
				break;
			case 14:
				connect(mazeNodes, nodeToConnect, nodeToConnect + 5);
				connect(mazeNodes, nodeToConnect, nodeToConnect - 1);
				connect(mazeNodes, nodeToConnect, nodeToConnect + 1);
				break;
			case 15:
				connect(mazeNodes, nodeToConnect, nodeToConnect - 5);
				connect(mazeNodes, nodeToConnect, nodeToConnect + 5);
				connect(mazeNodes, nodeToConnect, nodeToConnect - 1);
				connect(mazeNodes, nodeToConnect, nodeToConnect + 1);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + childrenMask);
			}
		}

//		connect(mazeNodes, 1, 2);
//		connect(mazeNodes, 2, 3);
//		connect(mazeNodes, 3, 4);
//		connect(mazeNodes, 4, 5);
//		connect(mazeNodes, 1, 6);
//		connect(mazeNodes, 3, 8);
//		connect(mazeNodes, 4, 9);
//		connect(mazeNodes, 7, 8);
//		connect(mazeNodes, 6, 11);
//		connect(mazeNodes, 7, 12);
//		connect(mazeNodes, 9, 14);
//		connect(mazeNodes, 10, 15);
//		connect(mazeNodes, 11, 12);
//		connect(mazeNodes, 14, 15);
//		connect(mazeNodes, 13, 18);
//		connect(mazeNodes, 15, 20);
//		connect(mazeNodes, 16, 17);
//		connect(mazeNodes, 17, 18);
//		connect(mazeNodes, 18, 19);
//		connect(mazeNodes, 19, 20);

	}

	public void print() {
	
//ENTRY and EXIT INDICATOR
//		out.println("E is maze entry.\nX is maze exit.");
//		out.print(" 1  2  3  4  5\n");
//ENTRY and EXIT INDICATOR
		
		for(int i = 0; i < 5; i++) {
			//			boolean isFirstRow = (this.mazeNodes.get(i).id >= 1 || this.mazeNodes.get(i).id <= 5);
			boolean isEntryOrExit = this.mazeNodes.get(i).isEntry || this.mazeNodes.get(i).isExit;
			//			boolean isOuterEdge = (this.mazeNodes.get(i).id == 6 || this.mazeNodes.get(i).id == 10 ||
			//					this.mazeNodes.get(i).id == 11 || this.mazeNodes.get(i).id == 15); 

			if(isEntryOrExit)
				out.print("   ");
			else
				out.print("___");
		}
		out.println();

		for(GraphNode node : this.mazeNodes) {
			
//			if(node.id == 1 || node.id == 6)
//				out.print(node.id + "   ");
//			else if(node.id == 11 || node.id == 16)
//				out.print(node.id + "  ");

//			boolean isFirstOrLastRow = (node.id >= 1 || node.id <= 5) || (node.id >= 16 || node.id <= 20);
			boolean isLastRow = node.id >= 16 && node.id <= 20;
			boolean isEntryOrExit = node.isEntry || node.isExit;
			boolean isLeftOuterEdge = node.id == 6 || node.id == 11; 
//			boolean isRightOuterEdge = node.id == 10 || node.id == 15;

//			boolean upChild = true;
			boolean downChild = false;
			boolean leftChild = false;
//			boolean rightChild = true;

			//			
			//			if(isFirstOrLastRow && isEntryOrExit)
			//				out.print(" ");
			//			else
			//				out.print("_");

			//			if(node.id == 1) {
			for(GraphNode child : node.children) {
//				if(child.id == node.id + 1 || isRightOuterEdge && isEntryOrExit)
//					rightChild = true;
				if(child.id == node.id + 5 || (isLastRow  && isEntryOrExit))
					downChild = true;
				if(child.id == node.id - 1 || isLeftOuterEdge && isEntryOrExit)
					leftChild = true;
//				if(child.id == node.id - 5)
//					upChild = true;
				//				}
			}
//ENTRY and EXIT INDICATOR
//			if(node.isEntry)
//				out.print(" E ");
//			else if(node.isExit)
//				out.print(" X ");
//			else
//ENTRY and EXIT INDICATOR			
			
			if(downChild && leftChild)
				out.print("   ");
			else if(!downChild && leftChild)
				out.print("___");
			else if(downChild && !leftChild)
				out.print("|  ");
//			else if(downChild && leftChild)
//				out.print("  |");
			else if(!downChild && !leftChild)
				out.print("|__");
//			else if(!downChild && leftChild)
//				out.print("__|");
//			else if(!downChild && !leftChild)
//				out.print("|_|");
//			else if(downChild && !leftChild)
//				out.print("| |");
			
//			if(upChild && downChild && leftChild && rightChild)
//				out.print("   ");
//			else if(upChild && !downChild && leftChild && rightChild)
//				out.print("___");
//			else if(upChild && downChild && !leftChild && rightChild)
//				out.print("|  ");
//			else if(upChild && downChild && leftChild && !rightChild)
//				out.print("  |");
//			else if(upChild && !downChild && !leftChild && rightChild)
//				out.print("|__");
//			else if(upChild && !downChild && leftChild && !rightChild)
//				out.print("__|");
//			else if(upChild && !downChild && !leftChild && !rightChild)
//				out.print("|_|");
//			else if(upChild && downChild && !leftChild && !rightChild)
//				out.print("| |");


//			if(isOuterEdge && isEntryOrExit)
//				out.print(" ");
//			else if(isOuterEdge)
//				out.print("|");
			
//			if(node.id == 5 || node.id == 10 || node.id == 15 || node.id == 20) {
//				if(isEntryOrExit)
//					out.println();
//				else
//					out.println("|");
////				out.print(node.id + 1);
//			}
			
			if(node.id == 5 || node.id == 10 || node.id == 15 || node.id == 20) {
				if(isEntryOrExit && (node.id == 10 || node.id == 15))
					out.println();
				else
					out.println("|");
//				out.print(node.id + 1);
			}
				
		}
		out.println();
	}

	private void connect(List<GraphNode>nodes, int node1, int node2) {
		if(!nodes.get(node1 - 1).children.contains(nodes.get(node2 - 1))) {
			nodes.get(node1 - 1).children.add(nodes.get(node2 - 1));
			nodes.get(node2 - 1).children.add(nodes.get(node1 - 1));
		}
	}

	public void solve() {
		List<GraphNode> visiGraphNodes = new ArrayList<>();
		List<GraphNode> path = new ArrayList<>();
		List<List<GraphNode>> paths = new ArrayList<>();

		GraphNode mazeEntry = null;
		for(GraphNode node : this.mazeNodes)
			if(node.isEntry)
				mazeEntry = node;

		findExits(mazeEntry, visiGraphNodes, path, paths);
	}

	private void findExits(GraphNode node, List<GraphNode> visitedGraphNodes, List<GraphNode> path, List<List<GraphNode>> paths) {
		visitedGraphNodes.add(node);
		path.add(node);

		if(node.isExit) {
			out.print("Exit found! ");
			out.print("Path to exit: ");
			for(GraphNode pathNode : path)
				out.print(pathNode.id + " ");
			out.println();
		}

		for(GraphNode child : node.children)
			if(!visitedGraphNodes.contains(child))
				findExits(child, visitedGraphNodes, path, paths);

		path.remove(path.size() - 1);
	}
}

public class MazeTester {

	public static void main(String[] args) {

		Maze maze = new Maze(java.lang.System.currentTimeMillis());
//		Maze maze = new Maze();
		maze.print();
		maze.solve();
	}
}