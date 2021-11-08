import java.util.ArrayList;

public class netNode {
    public String name;
    public net bNet;
    public ArrayList<netNode> childs;
    public ArrayList<netNode> parents;

    public netNode(String name, ArrayList<String> parents,net bNet) {
        this.bNet=bNet;
        this.name = name;
        this.childs = new ArrayList<netNode>();
        this.parents = new ArrayList<netNode>();
        if (parents.size() > 0) {
            for (int i = 0; i < parents.size(); i++) {
                if (this.bNet.getByString(parents.get(i))!=null) {
                    this.parents.add(this.bNet.getByString(parents.get(i)));
                } else {
                    this.parents.add(new netNode(parents.get(i)));
                }
            }
        }
    }
    public netNode(String name){
        this.name = name;
        this.childs=new ArrayList<netNode>();
        this.parents=new ArrayList<netNode>();
    }
    public String getName(){
        return this.name;
    }
    public ArrayList<netNode> getChilds(){
        return this.childs;
    }
    public ArrayList<netNode> getParents(){
        return this.parents;
    }
    public boolean inParents(netNode node){
        for(int i=0; i<this.parents.size();i++){
            if(this.parents.get(i).getName()==node.name){
                return true;
            }
        }
        return false;
    }
}

