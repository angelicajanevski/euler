import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class GlobalMinCut {

    static Random random;

    /**
     * One run of the contraction algorithm
     * Returns the min cut found
     *
     * @param   graph - the graph to find min cut of
     * @return  two ArrayLists of characters (placed together in another ArrayList)
     *          representing the partitions of the cut
     */
    public static ArrayList<ArrayList<Character>> global_min_cut(Graph graph) {
        ArrayList<ArrayList<Character>> cut = new ArrayList<ArrayList<Character>>();

        // For each node v, we will record
        // the list S(v) of nodes that have been contracted into v
        Map<Character, ArrayList<Character>> S = new HashMap<Character, ArrayList<Character>>();


        // TODO: Initialize S(v) = {v} for each v
        ArrayList<Character> Nodes = graph.getNodes();
        for (Character node : Nodes){
            ArrayList<Character> Sx = new ArrayList<>();
            Sx.add(node);

            S.put(node,Sx );
        }


        while (graph.getNbNodes() > 2) {

            // select an edge randomly
            Edge edge = graph.getEdge(random.nextInt(graph.getNbEdges()));

            // TODO: fill in the rest
            Character u = edge.node_1;
            Character v = edge.node_2;

            ArrayList <Character> Sv = S.get(v); //get S(v)
            Sv.addAll(S.get(u)); //add all nodes from S(u)
            S.put(v, Sv);  //update hash table

            graph.contractEdge(edge);
        }

        // TODO: assemble the output object
        for (Character node : graph.getNodes()){
            cut.add(S.get(node));
        }
        return cut;
    }


    /**
     * repeatedly calls global_min_cut until finds min cut or exceeds the max allowed number of iterations
     *
     * @param graph         the graph to find min cut of
     * @param r             a Random object, don't worry about this, we only use it for grading so we can use seeds
     * @param maxIterations some large number of iterations, you expect the algorithm to have found the min cut
     *                      before then (with high probability), used as a sanity check / to avoid infinite loop
     * @return              two lists of nodes representing the cut
     */
    public static ArrayList<ArrayList<Character>> global_min_cut_repeated(Graph graph, Random r, int maxIterations) {
        random = (r != null) ? r : new Random();

        ArrayList<ArrayList<Character>> cut = new ArrayList<ArrayList<Character>>();
        int count = 0;
        do  {
            
            cut = global_min_cut(graph);
            graph = new Graph(graph);


            ++ count;

            //if (get_cut_size(graph, cut) > min_cut_size)
            //    System.out.println("Cut has " + get_cut_size(graph, cut) + " edges but the min cut should have " +
            //      graph.getExpectedMinCutSize() + ", restarting");

        } while (get_cut_size(graph, cut) > graph.getExpectedMinCutSize() && count < maxIterations);

        if (count >= maxIterations) System.out.println("Forced stop after " + maxIterations + " iterations... did something go wrong?");
        return cut;

    }

    /**
     * @param graph  the original unchanged graph
     * @param cut    the partition of graph into two lists of nodes
     * @return       the number of edges between the partitions
     */
    public static int get_cut_size(Graph graph, ArrayList<ArrayList<Character>> cut) {
        ArrayList<Edge> edges = graph.getEdges();
        int cut_size = 0;
        for (int i = 0; i < edges.size(); ++i) {
            Edge edge = edges.get(i);
            if (cut.get(0).contains(edge.node_1) && cut.get(1).contains(edge.node_2) ||
                    cut.get(0).contains(edge.node_2) && cut.get(1).contains(edge.node_1)) {
                ++cut_size;
            }
        }
        return cut_size;
    }
}
