import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {

    //for bridge count
    int time = 0;
    static final int NIL = -1;
    int graphSize;
    int nbrBridge;

    ArrayList<ArrayList<Integer>> adjencyList;


    ArrayList<String[]> allDesigners;
    ArrayList<String[]> allFollowers;
    ArrayList<String[]> allShots;

    ArrayList<Integer> alreadyDone;

    // for component count
    Map<Integer, Boolean> visited;
    int numberOfComponent;
    Map<Integer, HashSet <Integer>> adjencyMap;

    boolean communNeighbor;


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
        adjencyList.get(follower).add(followed);
        //System.out.println(follower+" a suivi "+followed);

    }

    public void showAdjencyList(){
        for (int i = 0; i < adjencyList.size(); i++) {

            if(!adjencyList.get(i).isEmpty()){
                System.out.println("\nAdjacency list of vertex " + i);
                System.out.print("head");
                for (int j = 0; j < adjencyList.get(i).size(); j++) {

                    System.out.print(" -> "+ adjencyList.get(i).get(j));
                }
                System.out.println();
            }
        }
    }

    /**
     * A recursive function that finds bridges using DFS traversal
     *
     * @param u         : The vertex to be visited next
     * @param visited   : keeps tract of visited vertices
     * @param disc      : Stores discovery times of visited vertices
     * @param low
     * @param parent    : Stores parent vertices in DFS tree
     */
    void bridgeUtil(int u, Map visited, int disc[], int low[], int parent[])
    {

        // Mark the current node as visited
        visited.put(u, true);

        // Initialize discovery time and low value
        disc[u] = low[u] = ++time;

        // Go through all vertices adjacent to this
        Iterator<Integer> i = adjencyMap.get(u).iterator();
        while (i.hasNext())
        {
            int v = i.next();  // v is current adjacent of u
            boolean isVisited = (Boolean) visited.get(v);

            // If v is not visited yet, then make it a child
            // of u in DFS tree and recur for it.
            // If v is not visited yet, then recur for it
            if (!isVisited) {


                parent[v] = u;
                bridgeUtil(v, visited, disc, low, parent);

                // Check if the subtree rooted with v has a
                // connection to one of the ancestors of u
                low[u]  = Math.min(low[u], low[v]);

                // If the lowest vertex reachable from subtree
                // under v is below u in DFS tree, then u-v is
                // a bridge
                if (low[v] > disc[u])
                    nbrBridge++;
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
        //boolean visited[] = new boolean[graphSize];
        int disc[] = new int[allDesigners.size()];
        int low[] = new int[allDesigners.size()];
        int parent[] = new int[allDesigners.size()];


        // Initialize parent and visited, and ap(articulation point)
        // arrays
        for (int i = 0; i < graphSize; i++)
        {
            parent[i] = NIL;
            visited.put(i, false);
        }

        // Call the recursive helper function to find Bridges
        // in DFS tree rooted with vertex 'i'
        for(Integer node : adjencyMap.keySet()){
            if(visited.get(node) == false) {
                bridgeUtil(node, visited, disc, low, parent);
            }
        }
        System.out.println("Number of bridge : "+nbrBridge);
    }

    /////////////////////////////////////////////////////////////////
    ///////////////COMPTER LE NBR DE SOUS GRAPHES////////////////////
    /////////////////////////////////////////////////////////////////
    /**
     *  Walk through the graph and mark the node as visited
     * @param nodeId : id of a node
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
                if(!visited.get(neighbor)){
                    graphWalker(neighbor);
                }
            }
        }
    }

    /**
     * Launch a DFS for every node of the graph
     */
    public void componentCounter() {

        //every node is checked if it has been visited
        for(Integer node : visited.keySet()){

            if(!visited.get(node)){
                //if the node is not visited, it and all its connected node are visited
                graphWalker(node);
                numberOfComponent++;
            }

        }
        System.out.println("Number of components : "+numberOfComponent);
    }
    /////////////////////////////////////////////////////////////////
    /////////////////COMPTER LE NBR DE LOCAL BRIDGES/////////////////
    /////////////////////////////////////////////////////////////////


    public void localBridgeCounter(){
        int nbrLocalBridge = 0;
        //For every node
        for(Integer node1: adjencyMap.keySet()){
            HashSet<Integer> allNeighbors1 = adjencyMap.get(node1);
            if(!allNeighbors1.isEmpty()){
                //we must check if all of its neighbors
                for(int neighbor : allNeighbors1){
                    //share a same neighbor node
                    HashSet<Integer> neighborOfNeighbor = adjencyMap.get(neighbor);
                    communNeighbor = false;
                    for(int element : neighborOfNeighbor){
                        if(!allNeighbors1.contains(element)){
                            communNeighbor = true;
                        }
                        if(communNeighbor){
                            nbrLocalBridge++;
                        }
                    }
                }
            }
        }
        System.out.println("Number of local bridges : "+nbrLocalBridge);
    }

    // Trop lent
    /*
    public void localBCounter(){
        int nbrLocalBridge = 0;
        for(int node1 : adjencyMap.keySet()){
            for(int node2 : adjencyMap.keySet()){
                if(node1!=node2 || !alreadyDone.contains(node2)) {
                    if (common_neighbor(node1, node2)) {
                        nbrLocalBridge++;
                    };
                }
            }
            alreadyDone.add(node1);
        }
        System.out.println("Nombre de local bridges : "+nbrLocalBridge);
    }

    public boolean common_neighbor(int u, int v){
        HashSet<Integer> uNeighbors = adjencyMap.get(u);
        HashSet<Integer> vNeighbors = adjencyMap.get(v);
        communNeighbor = false;
        for(int element : uNeighbors){
            if(vNeighbors.contains(element)){
                communNeighbor=true;
            }
        }
        return communNeighbor;
    }*/

    /**
     * Build an adjency list used to manipulate the graph
     * @param isDirected : if false then add a link from B->A additionnaly to the A->B
     */
    public void buildQ1(boolean isDirected){


        for (String[] allFollower : allFollowers) {

            int follower = Integer.parseInt(allFollower[0]);
            int followed = Integer.parseInt(allFollower[1]);

            adjencyMap.putIfAbsent(followed, new HashSet<>());
            adjencyMap.putIfAbsent(follower, new HashSet<>());

            visited.put(followed, false);
            visited.put(follower, false);

            this.communNeighbor = false;
            this.alreadyDone = new ArrayList<>();

            adjencyMap.get(follower).add(followed);

            if (!isDirected) {
                adjencyMap.get(followed).add(follower);

            }
        }
        this.graphSize = adjencyMap.size();

    }

    /**
     * Constructor of a graph
     */
    public  Graph(){

        this.allShots = extract("shots.csv");
        this.allDesigners = extract("designers.csv");
        this.allFollowers = extract("followers.csv");

        this.visited = new HashMap<>();
        this.numberOfComponent = 0;

        this.adjencyList = new ArrayList<>();
        this.adjencyMap = new HashMap<>();
        this.nbrBridge = 0;
    }

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.buildQ1(false);

        graph.componentCounter();
        graph.localBridgeCounter();

        //trop long
        //graph.localBCounter();

        graph.bridge();
        //System.out.println(graph.common_neighbor(0,256));
    }
}
