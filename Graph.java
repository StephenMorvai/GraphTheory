import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Graph {
	LinkedList<Node> nodes;
	
	/**
	 * Creates an empty graph
	 */
	public Graph(){
		this.nodes = new LinkedList<Node>();
	}
	
	/**
	 * Creates a graph using established nodes
	 * @param g List of nodes to be added to new graph
	 */
	public Graph(LinkedList<Node> g){
		this.nodes = g;
	}
	
	/**
	 * 
	 * @param n the name of node
	 * @return the node which is has name n in specific graph
	 */
	public static Node findNode(String n, Graph g){
		Iterator<Node> iterate = g.nodes.iterator();
		while(iterate.hasNext()){
			Node i = iterate.next();
			if(i.getName().equals(n)){
				return i;
			}
		}
		return null;
	}
	
	public static Graph deepCopy(Graph old, Graph newGraph){
		Iterator<Node> iterate = old.nodes.iterator();
		while(iterate.hasNext()){
			Node i = iterate.next();
			if(findNode(i.getName(),newGraph) == null){
				newGraph.addNode(new Node(i.getName()));
			}
			Iterator<Node> it = i.getNeighbors().iterator();
			while(it.hasNext()){
				Node j = it.next();
				if(findNode(j.getName(),newGraph) == null){
					newGraph.addNode(new Node(j.getName()));
				}
				newGraph.addEdge(findNode(i.getName(),newGraph),findNode(j.getName(),newGraph));
				
			}
			
		}
		
		return newGraph;
	}
	/**
	 * Removes dead edges from graph (Edges pointing to nodes that are not part of graph)
	 */
	public void clean(){
		Iterator<Node> iterate = this.nodes.iterator();
		while(iterate.hasNext()){
			Node i = iterate.next();
			i.setVisited(false);
			Iterator<Node> it = i.getNeighbors().iterator();
			while(it.hasNext()){
				Node j = it.next();
				if(!this.nodes.contains(j)){
					it.remove();
				}
			}
			
		}
	}
	
	public int nColorable(){
		
		if(this.nodes.size()==1){
			return 1;
		}
		
		Iterator<Node> it = this.nodes.iterator();
		while(it.hasNext()){
			Node n = it.next();
			
			Graph copy = deepCopy(this,new Graph());
			
			Graph neigh = new Graph(findNode(n.getName(),copy).getNeighbors());
			neigh.clean();
			
			
			System.out.println(neigh.nodes + " Node: " + n );
			/*
			 * There is some form of duplication error. neigh.nodes contains 2 sets of every node it should contain
			 * But besides this is seems to work fine.
			 * 
			 * The bug can easily be worked because the the cycle finding algorithm simple finds twice the cycles it 
			 * should
			 * 
			 * In fact it does not seem to effect the result at all
			 */
			
			
			
			LinkedList<LinkedList<Node>> cycs = neigh.getCycles();
			Iterator<LinkedList<Node>> iterate = cycs.iterator();
			while(iterate.hasNext()){
				LinkedList<Node> q = iterate.next();
				if(q.size() % 2 == 1){
					//There is an odd cycle among the neighbors of a node
					return 4;
				}
			}
		}
		LinkedList<LinkedList<Node>> cycles = this.getCycles();
		Iterator<LinkedList<Node>> its = cycles.iterator();
		while(its.hasNext()){
			LinkedList<Node> q = its.next();
			if(q.size() % 2 == 1){
				//There is an odd cycle somewhere in the graph
				return 3;
			}
		}
		
		if(this.nodes.size() > 1){
			return 2;
		}
		
		return 1;
	}
	
	public LinkedList<LinkedList<Node>> getCycles(){
		LinkedList<LinkedList<Node>> list = new LinkedList<LinkedList<Node>>();
		while(true){
			//System.out.println(this.nodes);
			LinkedList<Node> i = this.findCycle(list);
			//this.clean();
			if(i.isEmpty()){
				break;
			}else{
				list.add(i);
			}
		}
		
		return list;
	}
	
	/**
	 * 
	 * @param master. List of already found cycles
	 * @return the next found cycle
	 */
	public LinkedList<Node> findCycle(LinkedList<LinkedList<Node>> master){
		
		LinkedList<Node> list = new LinkedList<Node>();
		//System.out.println(this.nodes);
		list.add(this.nodes.getFirst());
		
		return depthSearch(null,list,master);
	}
	
	public LinkedList<Node> depthSearch(Node previous,LinkedList<Node> list, LinkedList<LinkedList<Node>> master){
		Node current = list.getLast();
		current.setVisited(true);
		Iterator<Node> it = current.getNeighbors().iterator();
		while(it.hasNext()){
			Node next = it.next();
			
			if(next != previous){
				if(list.contains(next)){//found cycle
					LinkedList<Node> re = new LinkedList<Node>(list.subList(list.indexOf(next), list.size()));
					if(!master.contains(re)){
						return re;
					}
				}else{//does not contain
					LinkedList<Node> sr = new LinkedList<Node>(list);
					list.add(next);
					
					//System.out.println(next);
					
					
					
					LinkedList<Node> temp = depthSearch(current,list,master);
					if(temp.isEmpty()){
						list = sr;
						
					}else{
						return temp;
					}
				}
			}// if it is previous it moves on to the next one
			
			
		}
		
		//System.out.println("no cycles");
		return new LinkedList<Node>();
	}
	
	
	/*
	public LinkedList<Node> getCycle(Node n, LinkedList<Node> list, LinkedList<LinkedList<Node>> master){
		n.setVisited(true);
		if(n.getDistance() == 0){
			n.setDistance(1);
			list.add(n);
		}
		Iterator<Node> it = n.getNeighbors().iterator();
		while(it.hasNext()){
			Node q = it.next();
			if(q.getDistance() == 0){
				q.setDistance(n.getDistance() + 1);
				list.add(q);
			}
			System.out.println(n.getName() + ":" + n.getDistance() + " " + q.getName() + ":"+ q.getDistance());
			if(q.isVisited() && Math.abs(q.getDistance()-n.getDistance()) > 1 && list.contains(q) && list.contains(n)){//Cycle
				//break connection
				System.out.println(q.getName() + " end " + n.getName());
				//n.removeEdge(q);
				//q.removeEdge(n);
				List<Node> qwe;
				if(list.indexOf(q) > list.indexOf(n)){
					qwe = list.subList(list.indexOf(n),list.indexOf(q) + 1);
				}else{
					qwe = list.subList(list.indexOf(q),list.indexOf(n) + 1);
				}
				
				
				LinkedList<Node> re = new LinkedList<Node>(qwe);
				
				if(!master.contains(re)){
					return re;
				}
				//return n.getDistance() - q.getDistance() + 1;
			}else if(q.getDistance() == n.getDistance() + 1) {
				q.setVisited(true);
				System.out.println(q.getName() + " else if");
				LinkedList<Node> temp = getCycle(q,list,master);
				if(!list.isEmpty()){
					list.removeLast();
				}
				while(temp.isEmpty()){
					if(!list.isEmpty()){
						temp = getCycle(list.peekLast(),list,master);
						if(!list.isEmpty()){
							list.removeLast();
						}
					}else{
						break;
					}
				}
				return temp;
			}
		}
		System.out.println("No cycle");
		return new LinkedList<Node>();
	}*/
	
	public void addNode(Node a){
		if(!this.nodes.contains(a)){
			this.nodes.add(a);
		}
	}
	
	public void addEdge(Node a, Node b){
		addNode(a);
		addNode(b);
		a.addEdge(b);
		b.addEdge(a);
	}
	
	public static void main(String[] args){
		Graph g = new Graph();
		//Graph g = canada();
		
		
		Node a = new Node("a");
		Node b = new Node("b");
		Node c = new Node("c");
		Node d = new Node("d");
		
		//g.addNode(a);
		g.addEdge(a,b);
		g.addEdge(b,c);
		g.addEdge(c,a);
		g.addEdge(a,d);
		g.addEdge(b,d);
		g.addEdge(c,d);
		
		
		//System.out.println(g.getCycles());
		
		/*
		Graph temp = new Graph(a.getNeighbors());
		temp.clean();
		System.out.println(temp.getCycles());
		*/
		System.out.println(g.nColorable());
		
	}
	
	
	/**
	 * 
	 * @return A graph of canada 
	 */
	public static Graph canada(){
		Node yt = new Node("Yukon");
		Node nwt = new Node("North West Territories");
		Node nun = new Node("Nunavut");
		Node bc = new Node("British Columbia");
		Node ab = new Node("Alberta");
		Node sk = new Node("Saskachewan");
		Node mb = new Node("Manitoba");
		Node on = new Node("Ontario");
		Node qc = new Node("Quebec");
		Node nb = new Node("New Brunswick");
		Node ns = new Node("Nova Scotia");
		Node pei = new Node("Prince Edward Island");
		Node nl = new Node("Newfoundland and Labrador");
		
		Graph temp = new Graph();
		temp.addEdge(yt, nwt);
		temp.addEdge(nwt, nun);
		temp.addEdge(yt, bc);
		temp.addEdge(bc, ab);
		temp.addEdge(ab, nwt);
		temp.addEdge(ab, sk);
		temp.addEdge(sk, nwt);
		temp.addEdge(sk, mb);
		temp.addEdge(mb, nun);
		temp.addEdge(mb, on);
		temp.addEdge(on, qc);
		temp.addEdge(qc, nl);
		temp.addEdge(qc, nb);
		temp.addEdge(nb, pei);
		temp.addEdge(nb, ns);
		
		
		
		return temp;
	}
	
}
