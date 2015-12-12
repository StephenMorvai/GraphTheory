import java.util.LinkedList;

public class Node {
	private LinkedList<Node> neighbors;
	private int color;
	private String name;
	private boolean visited;
	private int distance;
	
	/**
	 * 
	 * Creates a new node with no neighbors
	 * Sets color to 0 which means it does not have a defined color
	 * 
	 */
	public Node(){
		this.neighbors = new LinkedList<Node>();
		this.color = 0; // 0 means no color
		this.name = "";
		this.visited = false;
		this.distance = 0;
	}
	public Node(String n){
		this.neighbors = new LinkedList<Node>();
		this.color = 0; // 0 means no color
		this.name = n;
		this.visited = false;
		this.distance = 0;
	}
	
	
	public LinkedList<Node> getNeighbors(){
		return this.neighbors;
	}
	public int getColor(){
		return this.color;
	}
	public String getName(){
		return this.name;
	}
	public boolean isVisited(){
		return this.visited;
	}
	public int getDistance(){
		return this.distance;
	}
	
	
	public void setDistance(int d){
		this.distance = d;
	}
	public void setVisited(boolean b){
		this.visited = b;
	}
	public void setName(String n){
		this.name = n;
	}
	
	public void setColor(int col){
		this.color = col;
	}
	
	public void addEdge(Node n){
		this.neighbors.add(n);
	}
	
	public void removeEdge(Node n){
		this.neighbors.remove(n);
	}
	
	public String toString(){
		return this.name;
	}
	
}
