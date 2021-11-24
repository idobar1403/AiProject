import java.util.ArrayList;
import java.util.HashMap;

public class net {
    public ArrayList<netNode> netNodes;
    /**
     * build net with no netNodes
     */
    public net(){
        this.netNodes = new ArrayList<netNode>();
    }
    /**
     * build net from given netNodes
     * @param netNodes
     */
    public net(ArrayList<netNode> netNodes) {
        this.netNodes = new ArrayList<netNode>();
        for (int i=0;i<netNodes.size();i++){
            this.netNodes.add(netNodes.get(i));
        }
        for (int i=0;i<this.netNodes.size();i++){
            addChild();
        }
    }

    /**
     * add netNode to the net
     * @param node
     */
    public void add(netNode node) {
        if (!exist(node)) {
            this.netNodes.add(node);
        }
    }

    /**
     * ask if the netNode is already exist in the net
     * @param node
     * @return boolean answer
     */
    public boolean exist(netNode node) {
        for (int i = 0; i < this.netNodes.size(); i++) {
            if (this.netNodes.get(i).getName() == node.getName()) {
                return true;
            }
        }
        return false;
    }
    /**
     * ask if the netNode of the name is ancestor of the given netNode by recursion
     * @param e
     * @param name
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
     * @param n
     * @return netNode
     */
    public netNode getByString(String n) {
        for (int i = 0; i < this.netNodes.size(); i++) {
            if(this.netNodes.get(i).getName()!=null) {
                if (this.netNodes.get(i).getName().equals(n)) {
                    return this.netNodes.get(i);
                }
            }
        }
        return null;
    }
    /**
     * update for every netNode his childes by iterate over his parents
     */
    public void addChild() {
        for (int i = 0; i < this.netNodes.size(); i++) {
            for (int j = 0; j < this.netNodes.get(i).parents.size(); j++) {
                boolean found = false;
                for (int h = 0; h < getByString(this.netNodes.get(i).parents.get(j).getName()).getChilds().size(); h++) {
                    // if the child of the parents is already in the child ArrayList of his parents
                    if (this.netNodes.get(i).name.equals(getByString(this.netNodes.get(i).parents.get(j).getName()).getChilds().get(h).getName())) {
                        found = true;
                    }
                }
                if (!found) {
                    //if the child is not in his parents child ArrayList
                    getByString(this.netNodes.get(i).parents.get(j).getName()).getChilds().add(this.netNodes.get(i));
                }
            }
        }
    }
}