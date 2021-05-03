import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Graph {

    ArrayList<ArrayList<Integer>> adjacencyList;

    ArrayList<String[]> allDesigners;
    ArrayList<String[]> allFollowers;
    ArrayList<String[]> allShots;


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
    public void build(){

        for(int i=0; i<allDesigners.size(); i++){
            adjacencyList.add(new ArrayList<Integer>());
        }

        for(int i=0; i<allFollowers.size(); i++){
            int follower = Integer.parseInt(String.valueOf(allFollowers.get(i)[0]));
            int followed= Integer.parseInt(allFollowers.get(i)[1]);
            addEdge(follower, followed);
        }

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

    public  Graph(){

        this.adjacencyList = new ArrayList<ArrayList<Integer>>();

        this.allShots = extract("shots.csv");
        this.allDesigners = extract("designers.csv");
        this.allFollowers = extract("followers.csv");
    }

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.build();
        graph.showAdjencyList();

    }
}
