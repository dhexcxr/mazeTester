package mazeTester;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
//
public class MazeTester {

	public static void main(String[] args) {
		List<GraphNode> mazeNodes = new ArrayList<>();
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

		@SuppressWarnings("unused")
		GraphNode mazeEntry = null;
		for(GraphNode node : mazeNodes)
			if(node.isEntry)
				mazeEntry = node;


		//List<GraphNode> path = new ArrayList<>();
		List<List<GraphNode>> paths = solve(mazeNodes);

		// to solve multiple paths, traverse nodes and count # of exits (add +1 to exit count)
		// each time exit is found, save path and -1 from exit count

		// alternatively, traverse nodes and save path for any exit until all nodes are visited (this'll probably be less O(n)

		if(paths.size() != 0) {
			out.print("Exit found!\n");
			out.print("Path to exit: ");
			for(List<GraphNode> path : paths) {
				for(GraphNode pathNode : path)
					out.print(pathNode.id + " ");
				out.println();
			}
		}
		else
			out.print("Exit not found");
	}
//
//
	private static List<List<GraphNode>> solve(List<GraphNode> maze) {
		// returns ArrayList of nodes to exit of maze, returns null if no exit/path found
		List<List<GraphNode>> paths = new ArrayList<>();
		List<GraphNode> path = new ArrayList<>();
		int numOfExits = 0;

		GraphNode mazeEntry = null;
		for(GraphNode node : maze)
			if(node.isEntry)
				mazeEntry = node;

		for(GraphNode node : maze)
			if(node.isExit)
				numOfExits++;
		while(paths.size() < numOfExits) {
			if(findExit(mazeEntry, new ArrayList<>(), path)) {
				// if we found the exit, add maze entry point to path ArrayList, reverse the list, and return it
				path.add(mazeEntry);
				Collections.reverse(path);
				paths.add(new ArrayList<>(path));
				path = new ArrayList<>();
			}
		}
		return paths;
	}
//
	private static boolean findExit(GraphNode node, List<GraphNode> visitedGraphNodes, List<GraphNode> path) {

		out.print(node.id + " ");
		visitedGraphNodes.add(node);

		// is this node the maze exit?
		if(node.isExit) {
			return true;
		}

		// if it isn't, iterate over children nodes to see if they are exit
		for(GraphNode child : node.children)
			if(!visitedGraphNodes.contains(child))
				if(findExit(child, visitedGraphNodes, path)) {
					// if we found the exit, add it to path ArrayList and return true
					path.add(child);
					return true;
				}

		return false;
	}
//
//
	// NEW CODE	
	//	private static List<List<GraphNode>> solve(List<GraphNode> maze) {
	//		// returns ArrayList of nodes to exit of maze, returns null if no exit/path found
	//		List<List<GraphNode>> paths = new ArrayList<>();
	//		List<GraphNode> path = new ArrayList<>();
	//		List<GraphNode> visitedGraphNodes = new ArrayList<>();
	//		
	//		GraphNode mazeEntry = null;
	//		for(GraphNode node : maze)
	//			if(node.isEntry)
	//				mazeEntry = node;
	//		
	//		while(visitedGraphNodes.size() < maze.size())
	//			findExits(mazeEntry, visitedGraphNodes, path, paths);
	//			
	//		if(paths.size() != 0) {
	//			// if we found the exit, add maze entry point to path ArrayList, reverse the list, and return it
	//			path.add(mazeEntry);
	//			Collections.reverse(path);
	////			paths.add(path);
	//			return paths;
	//		}
	//		
	//		return null;
	//	}
	//	
	//	private static boolean findExits(GraphNode node, List<GraphNode> visitedGraphNodes, List<GraphNode> path, List<List<GraphNode>> paths) {
	//		
	//		out.print(node.id + " ");
	//		visitedGraphNodes.add(node);
	//		
	////		while(visitedGraphNodes.size() <= node)
	//		// is this node the maze exit?
	//		if(node.isExit) {
	//			paths.add(path);
	//			return true;
	//		}
	//		
	//		// if it isn't, iterate over children nodes to see if they are exit
	//		for(GraphNode child : node.children)
	//			if(!visitedGraphNodes.contains(child))
	//				if(findExits(child, visitedGraphNodes, path, paths)) {
	//					// if we found the exit, add it to path ArrayList and return true
	//					path.add(child);
	//					return true;
	//				}
	//					
	//		return false;
	//	}


	// ORIGINAL CODE	
	//	private static List<GraphNode> solve(GraphNode mazeEntry) {
	//		// returns ArrayList of nodes to exit of maze, returns null if no exit/path found
	//		List<GraphNode> path = new ArrayList<>();
	//		if(findExit(mazeEntry, new ArrayList<>(), path)) {
	//			// if we found the exit, add maze entry point to path ArrayList, reverse the list, and return it
	//			path.add(mazeEntry);
	//			Collections.reverse(path);
	//			return path;
	//		}
	//		return null;
	//	}
	//	
	//	private static boolean findExit(GraphNode node, List<GraphNode> visitedGraphNodes, List<GraphNode> path) {
	//		
	//		out.print(node.id + " ");
	//		visitedGraphNodes.add(node);
	//		
	//		// is this node the maze exit?
	//		if(node.isExit) {
	//			return true;
	//		}
	//		
	//		// if it isn't, iterate over children nodes to see if they are exit
	//		for(GraphNode child : node.children)
	//			if(!visitedGraphNodes.contains(child))
	//				if(findExit(child, visitedGraphNodes, path)) {
	//					// if we found the exit, add it to path ArrayList and return true
	//					path.add(child);
	//					return true;
	//				}
	//					
	//		return false;
	//	}
//
	private static void connect(List<GraphNode>nodes, int node1, int node2) {
		if(!nodes.get(node1 - 1).children.contains(nodes.get(node2 - 1))) {
			nodes.get(node1 - 1).children.add(nodes.get(node2 - 1));
			nodes.get(node2 - 1).children.add(nodes.get(node1 - 1));
		}
	}

}