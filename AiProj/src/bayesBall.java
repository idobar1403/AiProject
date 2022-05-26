import java.util.ArrayList;

public class bayesBall {
    /**
     * this function create the variables in order to send them to the recursion function
     * @param bnet the net to make the bayesball on
     * @param query the query to check
     * @return String as the answer to the query
     */
    public static String bayesBallAns(net bnet, String query) {
        //split the query in order to get the given nodes and the query nodes
        String[] split1 = query.split("\\|");
        String[] depend = split1[0].split("-");
        netNode start = bnet.getByString(depend[0]);
        netNode end = bnet.getByString(depend[1]);
        ArrayList<netNode> givens = new ArrayList<>();
        if (split1.length > 1) {
            String[] split2 = split1[1].split(",");
            for (String temp : split2) {
                String[] split3 = temp.split("=");
                if (bnet.getByString(split3[0]) != null) {
                    givens.add(bnet.getByString(split3[0]));
                }
            }
        }
        //the visited ArrayList will save certain nodes in order to prevent infinity recursion
        ArrayList<netNode> visited = new ArrayList<>();
        boolean ans = bayesball(start,start, end, givens, true, visited);
        if (!ans) {
            return "yes";
        } else {
            return "no";
        }
    }
    /**
     * check if two nodes are independent or not
     * @param src the start of the recursion
     * @param start the start of the recursion that won't change
     * @param end the target of the recursion
     * @param givens the ArrayList of the
     * @param up if the direction is up or down
     * @param visited the ArrayList of the visited nodes
     * @return boolean answer if they independent
     */
    public static boolean bayesball(netNode src,netNode start, netNode end, ArrayList<netNode> givens,
                                    boolean up, ArrayList<netNode> visited){
        // check if the node arrived at the destination
        if(start.getName().equals(end.getName())){
            return true;
        }
        // if there are no givens enter the if
        if (givens.isEmpty()) {
            // check if the node came from child
            if(up) {
                // run over his parents
                for (int i = 0; i < start.getParents().size(); i++) {
                    visited.add(start);
                    // check if the node already visited in the parent or if the parent is not the src
                    if (!visited.contains(start.getParents().get(i))||src!=start.getParents().get(i)) {
                        if (bayesball(src,start.getParents().get(i), end, givens, true, visited)) {
                            return true;
                        }
                    }
                }
            }
            // run over his children
            for (int i = 0; i < start.getChilds().size(); i++) {
                visited.add(start);
                // check if the node already visited in the child or if the child is not the src
                if (!visited.contains(start.getChilds().get(i))||src!=start.getChilds().get(i)) {
                    if (bayesball(src,start.getChilds().get(i), end, givens, false, visited)) {
                        return true;
                    }
                }
            }
            return false;
        }
        else{
            // if the current node is given
            if(givens.contains(start)){
                // can't move so return false
                if(up){
                    return false;
                }
                else{
                    // run over his parents
                    for(int i=0;i<start.getParents().size();i++) {
                        visited.add(start);
                        // check if the node already visited in the parent or if the parent is not the src
                        if(!visited.contains(start.getParents().get(i))|| src!=start.getParents().get(i)) {
                            if (bayesball(src,start.getParents().get(i), end, givens, true, visited)) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
            }
            else {
                // check if came from child
                if(up){
                    // run over the parents
                    for (int i = 0; i < start.getParents().size(); i++) {
                        visited.add(start);
                        // check if the node already visited in the parent or if the parent is not the src
                        if(!visited.contains(start.getParents().get(i))||src!=start.getParents().get(i)) {
                            if (bayesball(src,start.getParents().get(i), end, givens, true, visited)) {
                                return true;
                            }
                        }
                    }
                }
                // run over the children
                for(int i=0;i<start.getChilds().size();i++){
                    visited.add(start);
                   // check if the node already visited in the child
                    if(!visited.contains(start.getChilds().get(i))){
                        if(bayesball(src,start.getChilds().get(i),end,givens,false,visited)){
                            return true;
                        }
                    }
                }
                return false;
            }
        }
    }
}