import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class netNode {
    public String name;
    public net bNet;
    public ArrayList<netNode> childs;
    public ArrayList<netNode> parents;
    public ArrayList<String> outcomes;
    public ArrayList<HashMap> factor;

    public netNode(String name, ArrayList<String> parents, ArrayList<String> outcomes,String table ,net bNet) {
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
                }
            }
        }
        this.outcomes = new ArrayList<String>();
        for (int i = 0; i < outcomes.size(); i++) {
            this.outcomes.add(outcomes.get(i));
        }
        this.factor = new ArrayList<HashMap>();
        this.buildCpt();
        String [] tableList=table.split(" ");
        for(int i=0;i<this.factor.size();i++){
            this.factor.get(i).put("P",tableList[i] );
        }
    }

    public netNode(String name) {
        this.name = name;
        this.childs = new ArrayList<netNode>();
        this.parents = new ArrayList<netNode>();
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<netNode> getChilds() {
        return this.childs;
    }

    public ArrayList<netNode> getParents() {
        return this.parents;
    }

    public ArrayList<String> getOutcomes() {
        return this.outcomes;
    }

    public boolean inParents(netNode node) {
        for (int i = 0; i < this.parents.size(); i++) {
            if (this.parents.get(i).getName() == node.name) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<HashMap> getFactor(){
        return this.factor;
    }
    public int getAsciSize(){
        int ans=0;
        ans=ans+this.name.charAt(0);
        for(int i=0;i<this.parents.size();i++){
            ans=ans+this.parents.get(i).getName().charAt(0);
        }
        return ans;
    }

    public void buildCpt() {
        int size = this.outcomes.size();
        for (int i = 0; i < this.parents.size(); i++) {
            size = size * this.parents.get(i).getOutcomes().size();
        }
        for (int i = 0; i < size; i++) {
            this.factor.add(i, new LinkedHashMap());
        }
        for (int i = 0; i < this.outcomes.size(); i++) {
            for (int j = i; j < size; j += this.outcomes.size()) {
                this.factor.get(j).put(this.name, this.outcomes.get(i));
            }
        }
        for (int i = 0; i < this.parents.size(); i++) {
            for (int h = 0; h < this.parents.get(i).getOutcomes().size(); h++) {
                int times= (int)Math.pow(this.parents.get(i).getOutcomes().size(), (this.parents.size()-i ));
                for (int j = (h * times); j < size; j += times) {
                    for (int m = j; m < j+times; m++) {
                        this.factor.get(m).put(this.parents.get(i).getName(), this.parents.get(i).getOutcomes().get(h));
                    }
                    j+=times;
                }
            }
        }
    }
}

