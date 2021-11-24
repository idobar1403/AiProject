import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class netNode {
    public String name;
    public net bNet;
    public ArrayList<netNode> childs;
    public ArrayList<netNode> parents;
    public ArrayList<String> outcomes;
    public ArrayList<HashMap<String,String>> factor;

    public netNode(String name, ArrayList<String> parents, ArrayList<String> outcomes,net bNet) {
        //check if the net does not contain the current node
        if(bNet.getByString(name)==null) {
            this.bNet = bNet;
            this.name = name;
            this.childs = new ArrayList<netNode>();
            this.parents = new ArrayList<netNode>();
            if (parents.size() > 0) {
                for (int i = 0; i < parents.size(); i++) {
                    if (this.bNet.getByString(parents.get(i)) != null) {
                        this.parents.add(this.bNet.getByString(parents.get(i)));
                    } else {
                        //if the node is not in the net then create it
                        this.parents.add(new netNode(parents.get(i)));
                        bNet.add(this.parents.get(i));
                    }
                }
            }
            this.outcomes = new ArrayList<String>();
            for (int i = 0; i < outcomes.size(); i++) {
                this.outcomes.add(outcomes.get(i));
            }
            this.factor = new ArrayList<HashMap<String,String>>();
        }
        else{
            // if there is already a node by this name then initialized it by the given values
            bNet.getByString(name).toNetNode(name,parents, outcomes,bNet);
        }
    }
    /**
     * this function build netNode that already exist
     * @param name
     * @param parents
     * @param outcomes
     * @param bNet
     */
    public void toNetNode(String name, ArrayList<String> parents, ArrayList<String> outcomes,net bNet){
        this.bNet = bNet;
        this.name = name;
        this.childs = new ArrayList<netNode>();
        this.parents = new ArrayList<netNode>();
        if (parents.size() > 0) {
            for (int i = 0; i < parents.size(); i++) {
                if (this.bNet.getByString(parents.get(i)) != null) {
                    this.parents.add(this.bNet.getByString(parents.get(i)));
                } else {
                    this.parents.add(new netNode(parents.get(i)));
                    bNet.add(this.parents.get(i));
                }
            }
        }
        this.outcomes = new ArrayList<String>();
        for (int i = 0; i < outcomes.size(); i++) {
            this.outcomes.add(outcomes.get(i));
        }
        this.factor = new ArrayList<HashMap<String,String>>();
    }
    /**
     * build netNode only by name
     * @param name
     */
    public netNode(String name) {
        this.name = name;
        this.childs = new ArrayList<netNode>();
        this.parents = new ArrayList<netNode>();
    }
    /**
     * get the netNode name
     * @return String name
     */
    public String getName() {
        return this.name;
    }
    /**
     * get the netNode childs
     * @return ArrayList<netNode>
     */
    public ArrayList<netNode> getChilds() {
        return this.childs;
    }
    /**
     * get the netNode parents
     * @return ArrayList<netNode>
     */
    public ArrayList<netNode> getParents() {
        return this.parents;
    }
    /**
     * get the netNode outcomes
     * @return ArrayList<String>
     */
    public ArrayList<String> getOutcomes() {
        return this.outcomes;
    }
    /**
     * get the netNode factor
     * @return ArrayList<HashMap<String,String>>
     */
    public ArrayList<HashMap<String,String>> getFactor(){
        return this.factor;
    }
    /**
     * check if the given netNode is ancestor of the current netNode
     * @param node
     * @return boolean answer
     */
    public boolean inParents(netNode node) {
        for (int i = 0; i < this.parents.size(); i++) {
            if (this.parents.get(i).getName() == node.name) {
                return true;
            }
        }
        return false;
    }
    /**
     * replace the current factor with the given factor
     * @param f
     */
    public void setFactor(ArrayList<HashMap<String,String>> f){
      this.factor=f;
    }
    /**
     * this function get the cpt and fill the values in the correct place
     * @param table
     */
    public void build(String table){
        this.buildCpt();
        String [] tableList=table.split(" ");
        for(int i=0;i<this.factor.size();i++){
            this.factor.get(i).put("P",tableList[i] );
        }
    }
    /**
     * computes the ascii value of the netNode cpt
     * @return int value
     */
    public int getAsciSize(){
        int ans=0;
        ans=ans+this.name.charAt(0);
        for(int i=0;i<this.parents.size();i++){
            ans=ans+this.parents.get(i).getName().charAt(0);
        }
        return ans;
    }
    /**
     * this function build the cpt given the parents and the current netNode outcomes
     */
    public void buildCpt() {
        //compute the size of the table
        int size = this.outcomes.size();
        for (int i = 0; i < this.parents.size(); i++) {
            size = size * this.parents.get(i).getOutcomes().size();
        }
        //creat LinkedHashMap for every row of the cpt
        for (int i = 0; i < size; i++) {
            this.factor.add(i, new LinkedHashMap<String,String>());
        }
        //fill the  current netNode values in the correct palces in the hashmaps
        for (int i = 0; i < this.outcomes.size(); i++) {
            for (int j = i; j < size; j += this.outcomes.size()) {
                this.factor.get(j).put(this.name, this.outcomes.get(i));
            }
        }int time=size;
        // every netNode occurrence will appear every time/his outcome size
        for (int i = 0; i < this.parents.size(); i++) {
            time=time/this.parents.get(i).getOutcomes().size();
            for (int h = 0; h < this.parents.get(i).getOutcomes().size(); h++) {
                for (int j = (h * time); j < size; j += time*(this.parents.get(i).getOutcomes().size()-1)) {
                    for (int m = j; m < j+time; m++) {
                        if (m < size) {
                            this.factor.get(m).put(this.parents.get(i).getName(), this.parents.get(i).getOutcomes().get(h));
                        }
                    }
                    j+=time;
                }
            }
        }
    }
}

