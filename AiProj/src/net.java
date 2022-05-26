import java.util.ArrayList;

public class net {
    public ArrayList<netNode> netNodes;
    /**
     * build net with no netNodes
     */
    public net(){
        this.netNodes = new ArrayList<>();
    }
    /**
     * build net from given netNodes
     * @param netNodes the net
     */
    public net(ArrayList<netNode> netNodes) {
        this.netNodes = new ArrayList<>();
        this.netNodes.addAll(netNodes);
        for (int i=0;i<this.netNodes.size();i++){
            updateChilds();
        }
    }

    /**
     * add netNode to the net
     * @param node the added node
     */
    public void add(netNode node) {
        if (!exist(node)) {
            this.netNodes.add(node);
        }
    }

    /**
     * ask if the netNode is already exist in the net
     * @param node the node we want to check if exists
     * @return boolean answer
     */
    public boolean exist(netNode node) {
        for (netNode netNode : this.netNodes) {
            if (netNode.getName().equals(node.getName())) {
                return true;
            }
        }
        return false;
    }
    /**
     * ask if the netNode of the name is ancestor of the given netNode by recursion
     * @param e the starting node
     * @param name the node we look for
     * @return boolean answer
     */
    public boolean inParents(netNode e, String name){
        if(e.getName().equals(name)){
            return true;
        }
        for(int i=0;i<e.getParents().size();i++){
            if(inParents(e.getParents().get(i),name)){
                return true;
            }
        }
        return false;
    }
    /**
     * return the netNode with the same name as the given string
     * @param n - the name of the node
     * @return netNode
     */
    public netNode getByString(String n) {
        for (netNode netNode : this.netNodes) {
            if (netNode.getName() != null) {
                if (netNode.getName().equals(n)) {
                    return netNode;
                }
            }
        }
        return null;
    }
    /**
     * update for every netNode his childes by iterate over his parents
     */
    public void updateChilds() {
        for (netNode netNode : this.netNodes) {
            for (int j = 0; j < netNode.parents.size(); j++) {
                boolean found = false;
                for (int h = 0; h < getByString(netNode.parents.get(j).getName()).getChilds().size(); h++) {
                    // if the child of the parents is already in the child ArrayList of his parents
                    if (netNode.name.equals(getByString(netNode.parents.get(j).getName()).getChilds().get(h).getName())) {
                        found = true;
                    }
                }
                if (!found) {
                    //if the child is not in his parents child ArrayList
                    getByString(netNode.parents.get(j).getName()).getChilds().add(netNode);
                }
            }
        }
    }
}