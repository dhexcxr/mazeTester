// ver 1.5
// with working random size mazes

// create a maze in a Graph data structure, iterate through it to find
// the path to the exit, and display the results
//
// TODO ensure we find the most efficient route by comparing multiple paths to one exit

package mazeTester;

import static java.lang.System.out;
import java.util.ArrayList;
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
//
class Maze {

	List<GraphNode> mazeNodes = new ArrayList<>();
	List<Integer> exits = new ArrayList<>();

	int mazeWidth = 5;
	int mazeHeight = 4;

	public Maze() {

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
		
		Random random = new Random();
		random.setSeed(seed);
		
		// set random maze size, between 4-10 cells per side
		final int MAX_SIZE_OF_MAZE_SIDE = 10;
		final int MIN_SIZE_OF_MAZE_SIDE = 4;
		mazeWidth = random.nextInt(MAX_SIZE_OF_MAZE_SIDE - MIN_SIZE_OF_MAZE_SIDE + 1) + MIN_SIZE_OF_MAZE_SIDE;
		mazeHeight = random.nextInt(MAX_SIZE_OF_MAZE_SIDE - MIN_SIZE_OF_MAZE_SIDE + 1) + MIN_SIZE_OF_MAZE_SIDE;

		List<Integer> validEntryAndExit = getValidEntriesAndExitCells();

		// construct maze out of GraphNodes
		for(int i = 0; i < mazeWidth * mazeHeight; i++) {
			mazeNodes.add(new GraphNode(i + 1));
		}
		
		// choose random entries and exits
		final int MAX_NUMBER_OF_EXITS = 3;
		int numOfExits = random.nextInt(MAX_NUMBER_OF_EXITS);
		
		int entry = random.nextInt(validEntryAndExit.size());
		mazeNodes.get(validEntryAndExit.get(entry)).isEntry = true;

		for(int i = 0; i <= numOfExits; i++) {
			int exit = random.nextInt(validEntryAndExit.size());
			while(mazeNodes.get(validEntryAndExit.get(exit)).isEntry)
				exit = random.nextInt(validEntryAndExit.size());
			GraphNode exitNode = mazeNodes.get(validEntryAndExit.get(exit)); 
			exitNode.isExit = true;
			// add exits to list of exits
			exits.add(exitNode.id);
		}
		
		// randomly connect a number of nodes to create the maze
		for(int i = 0; i < mazeWidth * mazeHeight; i++) {
			final byte CONNECT_UP = 0b0001;
			final byte CONNECT_DOWN = 0b0010;
			final byte CONNECT_LEFT = 0b0100;
			final byte CONNECT_RIGHT = 0b1000;
			final int CONNECTION_VARIATIONS = 16;

			int nodeToConnect = random.nextInt(mazeNodes.size())+1;
			int childrenMask = random.nextInt(CONNECTION_VARIATIONS);		// random 0-15

			boolean isTop = nodeToConnect >= 1 && nodeToConnect <= mazeWidth;
			boolean isBottom = nodeToConnect >= mazeWidth * mazeHeight - mazeWidth + 1 && nodeToConnect <= mazeWidth * mazeHeight;
			boolean isLeft = nodeToConnect % mazeWidth - 1 == 0;
			boolean isRight = 	nodeToConnect % mazeWidth == 0;

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
				connect(mazeNodes, nodeToConnect, nodeToConnect - mazeWidth);
			if((childrenMask & CONNECT_DOWN) == CONNECT_DOWN)
				connect(mazeNodes, nodeToConnect, nodeToConnect + mazeWidth);
			if((childrenMask & CONNECT_LEFT) == CONNECT_LEFT)
				connect(mazeNodes, nodeToConnect, nodeToConnect - 1);
			if((childrenMask & CONNECT_RIGHT) == CONNECT_RIGHT)
				connect(mazeNodes, nodeToConnect, nodeToConnect + 1);
		}
	}
	
	private List<Integer>getValidEntriesAndExitCells() {
		// find the cells that line the outside of the maze
		List<Integer> validEntryAndExit = new ArrayList<>();
		for(int i = 0; i < mazeWidth; i++ )
			validEntryAndExit.add(i);
		for(int i = mazeWidth, rEdge = 0; i <= mazeWidth * mazeHeight - mazeWidth;) {
			validEntryAndExit.add(i);
			if(rEdge == 0) {
				i += mazeWidth - 1;
				rEdge = 1;
			}			
			else if(rEdge == 1) {
				i += 1;
				rEdge = 0;
			}
		}
		for(int i = mazeWidth * mazeHeight - mazeWidth + 1; i < mazeWidth * mazeHeight; i++ )
			validEntryAndExit.add(i);
		
		return validEntryAndExit;
	}

	public void print() {

		final int BEGGINNING_OF_FIRST_LINE = 0;
		final int END_OF_FIRST_LINE = mazeWidth - 1;
		final int BEGGINNING_OF_LAST_LINE = this.mazeNodes.size() - mazeWidth;
		final int END_OF_LAST_LINE = this.mazeNodes.size() - 1;
		final String LEFT_JUSTIFY ="     ";

		out.println("   E is maze entry.\n   X are maze exits.");

		// print cell index above maze
		out.print("Cell  ");
		for(int i = 1; i <= mazeWidth; i++)
			if(i < 10)
				out.print(i + "  ");
			else if(i >=10 && i < 100)
				out.print(i + " ");
		out.println();

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
			if(node.id % mazeWidth - 1 == 0)
				if(node.id < 10)
					out.print(node.id + "  ");
				else if(node.id >= 10 && node.id < 100)
					out.print(node.id + " ");
				else if(node.id >= 100)
					out.print(node.id);

			boolean isLeftInnerCell = node.id % mazeWidth - 1 == 0 && node.id != 1 && node.id != mazeWidth * mazeHeight - mazeWidth + 1;
			
			// print entry or exit indicator for left edge of maze			
			if(node.isEntry && isLeftInnerCell)
				out.print(" E");
			else if(node.isExit && isLeftInnerCell)
				out.print(" X");
			else if(node.id == 1 || isLeftInnerCell || node.id == mazeWidth * mazeHeight - mazeWidth + 1)
				out.print("  ");

			boolean isLastRow = node.id >= mazeWidth * mazeHeight - mazeWidth + 1 && node.id <= mazeWidth * mazeHeight;
//			boolean isEntryOrExit = node.isEntry || node.isExit;
			boolean hasDownChild = false;
			boolean hasLeftChild = false;
			
			// set which walls to draw
			for(GraphNode child : node.children) {
				if(child.id == node.id + mazeWidth || isLastRow  && (node.isEntry || node.isExit))
					hasDownChild = true;
				if(child.id == node.id - 1 || isLeftInnerCell && (node.isEntry || node.isExit))
					hasLeftChild = true;
			}

			// print the cell with it's walls
			if(hasDownChild && hasLeftChild)
				out.print("   ");
			else if(!hasDownChild && hasLeftChild)
				out.print("___");
			else if(hasDownChild && !hasLeftChild)
				out.print("|  ");
			else if(!hasDownChild && !hasLeftChild)
				out.print("|__");

			// print entry or exit indicator for right edge of maze
			if(node.isEntry && (node.id != mazeWidth && node.id % mazeWidth == 0 && node.id != mazeWidth * mazeHeight))
				out.println(" E ");
			else if(node.isExit && (node.id != mazeWidth && node.id % mazeWidth == 0 && node.id != mazeWidth * mazeHeight))
				out.println(" X ");
			else if(node.id % mazeWidth == 0)
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
//		Maze randomMaze = new Maze(1636591473033l);
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
