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
            this.childs = new ArrayList<>();
            this.parents = new ArrayList<>();
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
            this.outcomes = new ArrayList<>();
            this.outcomes.addAll(outcomes);
            this.factor = new ArrayList<>();
        }
        else{
            // if there is already a node by this name then initialized it by the given values
            bNet.getByString(name).toNetNode(name,parents, outcomes,bNet);
        }
    }
    /**
     * this function build netNode that already exist
     * @param name the name of the node.
     * @param parents the node parents.
     * @param outcomes the outcomes of the node.
     * @param bNet the net he belongs to.
     */
    public void toNetNode(String name, ArrayList<String> parents, ArrayList<String> outcomes,net bNet){
        this.bNet = bNet;
        this.name = name;
        this.childs = new ArrayList<>();
        this.parents = new ArrayList<>();
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
        this.outcomes = new ArrayList<>();
        this.outcomes.addAll(outcomes);
        this.factor = new ArrayList<>();
    }
    /**
     * build netNode only by name
     * @param name the new node name
     */
    public netNode(String name) {
        this.name = name;
        this.childs = new ArrayList<>();
        this.parents = new ArrayList<>();
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
     * replace the current factor with the given factor
     * @param f set the given factor as the current factor
     */
    public void setFactor(ArrayList<HashMap<String,String>> f){
      this.factor=f;
    }
    /**
     * this function get the cpt and fill the values in the correct place
     * @param table build factor from given string
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
        for (netNode parent : this.parents) {
            ans = ans + parent.getName().charAt(0);
        }
        return ans;
    }
    /**
     * this function build the cpt given the parents and the current netNode outcomes
     */
    public void buildCpt() {
        //compute the size of the table
        int size = this.outcomes.size();
        for (netNode parent : this.parents) {
            size = size * parent.getOutcomes().size();
        }
        //creat LinkedHashMap for every row of the cpt
        for (int i = 0; i < size; i++) {
            this.factor.add(i, new LinkedHashMap<>());
        }
        //fill the current netNode values in the correct places in the hashmaps
        for (int i = 0; i < this.outcomes.size(); i++) {
            for (int j = i; j < size; j += this.outcomes.size()) {
                this.factor.get(j).put(this.name, this.outcomes.get(i));
            }
        }int time=size;
        // every netNode occurrence will appear every time/his outcome size
        for (netNode parent : this.parents) {
            time = time / parent.getOutcomes().size();
            for (int h = 0; h < parent.getOutcomes().size(); h++) {
                // to complete one loop of every outcome
                for (int j = (h * time); j < size; j += time * (parent.getOutcomes().size() - 1)) {
                    for (int m = j; m < j + time; m++) {
                        if (m < size) {
                            this.factor.get(m).put(parent.getName(), parent.getOutcomes().get(h));
                        }
                    }
                    // move j + "times" forward because in the inner for we moved "times" number of times
                    j += time;
                }
            }
        }
    }
}

