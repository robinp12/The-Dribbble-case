import com.sun.javafx.collections.MappingChange;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {

    int time = 0;
    static final int NIL = -1;

    ArrayList<ArrayList<Integer>> adjacencyList;

    ArrayList<String[]> allDesigners;
    ArrayList<String[]> allFollowers;
    ArrayList<String[]> allShots;

    // for component count
    Map<Integer, Boolean> visited;
    int numberOfComponent;
    Map<Integer, HashSet <Integer>> adjencyMap;


    public ArrayList<String[]>  extract(String name){
        String COMMA_DELIMITER = ",";
        ArrayList<String[]> allDesigners = new ArrayList<>();
        try {

            BufferedReader br = new BufferedReader(new FileReader("..\\Dribbble_data\\"+name));
            String line;
            while ((line = br.readLine() )!=null){
                //System.out.println(line);
                String[] values = line.split(",");
                allDesigners.add(values);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        allDesigners.remove(0);

        for( int i = 0; i < allDesigners.size(); i++){
            String infos= "";

            for(int j =0 ; j<allDesigners.get(i).length; j++){

                String info = allDesigners.get(i)[j];
                infos += info +" , ";
            }
        };
    return allDesigners;
    }

    public void addEdge( int follower, int followed){
        adjacencyList.get(follower).add(followed);
        //System.out.println(follower+" a suivi "+followed);

    }



    public void showAdjencyList(){
        for (int i = 0; i < adjacencyList.size(); i++) {

            if(!adjacencyList.get(i).isEmpty()){
                System.out.println("\nAdjacency list of vertex " + i);
                System.out.print("head");
                for (int j = 0; j < adjacencyList.get(i).size(); j++) {

                    System.out.print(" -> "+adjacencyList.get(i).get(j));
                }
                System.out.println();
            }
        }
    }

    // A recursive function that finds and prints bridges
    // using DFS traversal
    // u --> The vertex to be visited next
    // visited[] --> keeps tract of visited vertices
    // disc[] --> Stores discovery times of visited vertices
    // parent[] --> Stores parent vertices in DFS tree
    void bridgeUtil(int u, boolean visited[], int disc[], int low[], int parent[])
    {

        // Mark the current node as visited
        visited[u] = true;

        // Initialize discovery time and low value
        disc[u] = low[u] = ++time;

        // Go through all vertices aadjacent to this
        Iterator<Integer> i = adjacencyList.get(u).iterator();
        while (i.hasNext())
        {
            int v = i.next();  // v is current adjacent of u

            // If v is not visited yet, then make it a child
            // of u in DFS tree and recur for it.
            // If v is not visited yet, then recur for it
            if (!visited[v])
            {
                parent[v] = u;
                bridgeUtil(v, visited, disc, low, parent);

                // Check if the subtree rooted with v has a
                // connection to one of the ancestors of u
                low[u]  = Math.min(low[u], low[v]);

                // If the lowest vertex reachable from subtree
                // under v is below u in DFS tree, then u-v is
                // a bridge
                if (low[v] > disc[u])
                    System.out.println(u+"-->"+v);
            }

            // Update low value of u for parent function calls.
            else if (v != parent[u])
                low[u]  = Math.min(low[u], disc[v]);
        }
    }

    // DFS based function to find all bridges. It uses recursive
    // function bridgeUtil()
    void bridge()
    {
        // Mark all the vertices as not visited
        boolean visited[] = new boolean[adjacencyList.size()];
        int disc[] = new int[adjacencyList.size()];
        int low[] = new int[adjacencyList.size()];
        int parent[] = new int[adjacencyList.size()];


        // Initialize parent and visited, and ap(articulation point)
        // arrays
        for (int i = 0; i < adjacencyList.size(); i++)
        {
            parent[i] = NIL;
            visited[i] = false;
        }

        // Call the recursive helper function to find Bridges
        // in DFS tree rooted with vertex 'i'
        for (int i = 0; i < adjacencyList.size(); i++)
            if (visited[i] == false)
                bridgeUtil(i, visited, disc, low, parent);
    }

    public void nodeWithMoreThanXConnection( int size){
        for (int i = 0; i < adjacencyList.size(); i++) {

            if(adjacencyList.get(i).size()>size){
                System.out.println("\nAdjacency list of vertex " + i);
                System.out.print("head");
                for (int j = 0; j < adjacencyList.get(i).size(); j++) {

                    System.out.print(" -> "+adjacencyList.get(i).get(j));
                }
                System.out.println();
            }
        }
    }

    /////////////////////////////////////////////////////////////////
    ///////////////COMPTER LE NBR DE SOUS GRAPHES////////////////////
    /////////////////////////////////////////////////////////////////
    /**
     *  walk through the graph and mark the node as visited
     * @param nodeId id of a node
     *
     *  Check a node and all of its neighbors (and theirs neighbors, ... recursively)
     */
    public void graphWalker(int nodeId) {

        //mark node as visited
        visited.put(nodeId, true);
        //for all neighbor : check if they are visited
        if(adjencyMap.get(nodeId)!=null){
            for(int neighbor: adjencyMap.get(nodeId) ){
                //if yes, then check their own neighbor
                if(visited.get(neighbor)== false){
                    graphWalker(neighbor);
                }
            }
        }
    }

    public void componentCounter() {

        //every node is checked if it has been visited
        for(Integer node : visited.keySet()){

            if(visited.get(node) == false){
                //if the node is not visited, it and all its connected node are visited
                graphWalker(node);
                numberOfComponent++;
            }

        }
        System.out.println("Number of components : "+numberOfComponent);
    }
    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
/*
    /**
     * Build an adjency list used to manipulate the graph
     * @param isDirected : if false then add a link from B->A additionnaly to the A->B
     *
    public void build(boolean isDirected){

        for(int i=0; i<allDesigners.size(); i++){
            adjacencyList.add(new ArrayList<Integer>());
        }

        for(int i=0; i<allFollowers.size(); i++){
            int follower = Integer.parseInt(String.valueOf(allFollowers.get(i)[0]));
            int followed= Integer.parseInt(allFollowers.get(i)[1]);
            addEdge(follower, followed);
            if(!isDirected){
                addEdge(followed,follower);
            }
        }

    }*/
    /**
     * Build an adjency list used to manipulate the graph
     * @param isDirected : if false then add a link from B->A additionnaly to the A->B
     */
    public void buildQ1(boolean isDirected){
        for(int i=0; i<allFollowers.size();i++){

            int follower = Integer.valueOf(allFollowers.get(i)[0]);
            int followed = Integer.valueOf(allFollowers.get(i)[1]);

            adjencyMap.putIfAbsent(followed, new HashSet<Integer>());
            adjencyMap.putIfAbsent(follower, new HashSet<Integer>());

            visited.put(followed, false);
            visited.put(follower, false);

            adjencyMap.get(follower).add(followed);
            if(!isDirected){
                adjencyMap.get(followed).add(follower);
            }
        }


    }

    public  Graph(){

        this.adjacencyList = new ArrayList<ArrayList<Integer>>();

        this.allShots = extract("shots.csv");
        this.allDesigners = extract("designers.csv");
        this.allFollowers = extract("followers.csv");
        this.visited = new HashMap<>();
        this.numberOfComponent = 0;

        this.adjencyMap = new HashMap<Integer, HashSet<Integer>>();
    }

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.buildQ1(false);
        //graph.bridge();
        //graph.nodeWithMoreThanXConnection(30);
        graph.componentCounter();
    }
}
