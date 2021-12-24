// ver 1.1
// with working random mazes, with some code cleanup

// create a maze in a Graph data structure, iterate through it to find
// the path to the exit, and display the results
//
// TODO ensure we find the most efficient route by comparing multiple paths to one exit
//	add functionality to generate maze of arbitrary size

package mazeTester;

import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
	// make some method variables here to define maze width and height
	// for to make arbitrary sized mazes, like REAL big ones :-D
	int mazeWidth = 0;
	int mazeHeight = 0;

	public Maze() {
		mazeWidth = 5;
		mazeHeight = 4;
		
		// construct a maze with some defaults
		for(int i = 0; i < mazeWidth * mazeHeight; i++) {
			mazeNodes.add(new GraphNode(i + 1));
		}

		mazeNodes.get(2).isEntry = true;
		mazeNodes.get(15).isExit = true;
		mazeNodes.get(10).isExit = true;

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
		
		// defaults for testing
		mazeWidth = 5;
		mazeHeight = 4;
		
		// construct a maze with random entries and exits
		final int MAX_NUMBER_OF_EXITS = 3;
		for(int i = 0; i < 20; i++) {
			mazeNodes.add(new GraphNode(i + 1));
		}

		Random random = new Random();
		random.setSeed(seed);

		int numOfExits = random.nextInt(MAX_NUMBER_OF_EXITS);

		// set random entry
		int entry = random.nextInt(validEntryAndExit.size() - 1);
		mazeNodes.get(validEntryAndExit.get(entry)).isEntry = true;

		// set random exits
		for(int i = 0; i <= numOfExits; i++) {
			int exit = random.nextInt(validEntryAndExit.size() - 1);
			while(mazeNodes.get(validEntryAndExit.get(exit)).isEntry)
				exit = random.nextInt(validEntryAndExit.size() - 1);
			mazeNodes.get(validEntryAndExit.get(exit)).isExit = true;
		}

		// randomly connect 20 nodes to create the maze
		for(int i = 0; i < 20; i++) {
			final byte CONNECT_UP = 0b0001;
			final byte CONNECT_DOWN = 0b0010;
			final byte CONNECT_LEFT = 0b0100;
			final byte CONNECT_RIGHT = 0b1000;
			final int CONNECTION_VARIATIONS = 16;

			int nodeToConnect = random.nextInt(mazeNodes.size())+1;		// random 1-20
			int childrenMask = random.nextInt(CONNECTION_VARIATIONS);		// random 0-15

			boolean isTop = nodeToConnect >= 1 && nodeToConnect <=5;
			boolean isBottom = nodeToConnect >= 16 && nodeToConnect <=20;
			boolean isLeft = nodeToConnect == 1 || nodeToConnect == 6 || nodeToConnect == 11 || nodeToConnect == 16;
			boolean isRight = 	nodeToConnect == 5 || nodeToConnect == 10 || nodeToConnect == 15 || nodeToConnect == 20;

			// if nodeToConnect is in outer edge of maze
			// don't try to connect to a cell that is out of bounds
			if(isTop)
				childrenMask = childrenMask & ~CONNECT_UP;
			if(isBottom)
				childrenMask = childrenMask & ~CONNECT_DOWN;
			if(isLeft)
				childrenMask = childrenMask & ~CONNECT_LEFT;
			if(isRight)
				childrenMask = childrenMask & ~CONNECT_RIGHT;

			// test childMask settings and run connect functions
			if((childrenMask & CONNECT_UP) == CONNECT_UP)
				connect(mazeNodes, nodeToConnect, nodeToConnect - 5);
			if((childrenMask & CONNECT_DOWN) == CONNECT_DOWN)
				connect(mazeNodes, nodeToConnect, nodeToConnect + 5);
			if((childrenMask & CONNECT_LEFT) == CONNECT_LEFT)
				connect(mazeNodes, nodeToConnect, nodeToConnect - 1);
			if((childrenMask & CONNECT_RIGHT) == CONNECT_RIGHT)
				connect(mazeNodes, nodeToConnect, nodeToConnect + 1);
		}
	}

	public void print() {

		final int BEGGINNING_OF_FIRST_LINE = 0;
		final int END_OF_FIRST_LINE = 4;
		final int BEGGINNING_OF_LAST_LINE = this.mazeNodes.size()-5;
		final int END_OF_LAST_LINE = this.mazeNodes.size()-1;
		final String LEFT_JUSTIFY ="    ";

		out.println("   E is maze entry.\n   X are maze exits.");

		out.println("Cell 1  2  3  4  5");

		// correct spacing of maze from left edge
		out.print(LEFT_JUSTIFY);

		// print entry or exit indicator for first line
		for(int i = BEGGINNING_OF_FIRST_LINE; i <= END_OF_FIRST_LINE; i++) {
			if(this.mazeNodes.get(i).isEntry)
				out.print(" E ");
			else if(this.mazeNodes.get(i).isExit)
				out.print(" X ");
			else
				out.print("___");
		}
		out.println();

		// print walls for the maze
		for(GraphNode node : this.mazeNodes) {

			// print cell numbers down left side of maze
			if(node.id == 1 || node.id == 6)
				out.print(node.id + " ");
			else if(node.id == 11 || node.id == 16)
				out.print(node.id);

			boolean isLastRow = node.id >= 16 && node.id <= 20;
			boolean isEntryOrExit = node.isEntry || node.isExit;
			boolean isLeftOuterEdge = node.id == 6 || node.id == 11;

			boolean downChild = false;
			boolean leftChild = false;

			// print entry or exit indicator for left edge of maze			
			if(node.isEntry && isLeftOuterEdge)
				out.print(" E");
			else if(node.isExit && isLeftOuterEdge)
				out.print(" X");
			else if(node.id == 1 || isLeftOuterEdge || node.id == 16)
				out.print("  ");

			// set which walls to draw
			for(GraphNode child : node.children) {
				if(child.id == node.id + 5 || isLastRow  && isEntryOrExit)
					downChild = true;
				if(child.id == node.id - 1 || isLeftOuterEdge && isEntryOrExit)
					leftChild = true;
			}

			// print the cell with it's walls
			if(downChild && leftChild)
				out.print("   ");
			else if(!downChild && leftChild)
				out.print("___");
			else if(downChild && !leftChild)
				out.print("|  ");
			else if(!downChild && !leftChild)
				out.print("|__");

			// print entry or exit indicator for right edge of maze
			if(node.isEntry && (node.id == 10 || node.id == 15))
				out.println(" E ");
			else if(node.isExit && (node.id == 10 || node.id == 15))
				out.println(" X ");
			else if(node.id == 5 || node.id == 10 || node.id == 15 || node.id == 20)
				out.println("|");
		}

		// correct spacing from left edge
		out.print(LEFT_JUSTIFY);
		
		// print entry and exits for last line
		for(int i = BEGGINNING_OF_LAST_LINE; i <= END_OF_LAST_LINE; i++) {
			if(this.mazeNodes.get(i).isEntry)
				out.print(" E ");
			else if(this.mazeNodes.get(i).isExit)
				out.print(" X ");
			else
				out.print("   ");
		}
		out.println();
	}

	private void connect(List<GraphNode>nodes, int node1, int node2) {
		// connect nodes specified if they are not already connected
		if(!nodes.get(node1 - 1).children.contains(nodes.get(node2 - 1))) {
			nodes.get(node1 - 1).children.add(nodes.get(node2 - 1));
			nodes.get(node2 - 1).children.add(nodes.get(node1 - 1));
		}
	}

	public List<List<GraphNode>> solve() {
		// prep for findExits
		List<GraphNode> visitedGraphNodes = new ArrayList<>();
		List<GraphNode> pathToExit = new ArrayList<>();
		List<List<GraphNode>> allPaths = new ArrayList<>();

		GraphNode mazeEntry = null;
		for(GraphNode node : this.mazeNodes)
			if(node.isEntry)
				mazeEntry = node;

		findExits(mazeEntry, visitedGraphNodes, pathToExit, allPaths);
		return allPaths;
	}

	private void findExits(GraphNode node, List<GraphNode> visitedGraphNodes, List<GraphNode> pathToExit, List<List<GraphNode>> allPaths) {
		// TODO add ArrayList<ArrayList<GraphNode> pathsToThisExit here, track when an exit is found
		// and devise a way to find all paths to it, then compare and only save shortest path
		
		// recurse through maze starting at node
		visitedGraphNodes.add(node);
		pathToExit.add(node);

		if(node.isExit) {
			allPaths.add(new ArrayList<>(pathToExit));
		}

		for(GraphNode child : node.children)
			if(!visitedGraphNodes.contains(child))
				findExits(child, visitedGraphNodes, pathToExit, allPaths);

		// if we arrive here we did not find the exit, remove this node from pathToExit
		pathToExit.remove(pathToExit.size() - 1);
	}
}

public class MazeTester {

	public static void main(String[] args) {

		out.println("Maze from textbook:");
		Maze mazeFromBook = new Maze();
		mazeFromBook.print();
		printResults(mazeFromBook.solve());		

		out.println("Randomized maze:");
		Maze randomMaze = new Maze(java.lang.System.currentTimeMillis());
		randomMaze.print();
		printResults(randomMaze.solve());
	}

	public static void printResults(List<List<GraphNode>> results) {
		// print paths from results
		if(results.size() > 0) {
			for(List<GraphNode> path : results) {
				out.print("Exit found! ");
				out.print("Path to exit: ");
				for(GraphNode pathNode : path)
					out.print(pathNode.id + " ");
				out.println();
			}
		}
		else
			out.println("No path to exit...");
		out.println();
	}
}
