import java.util.ArrayList;
import java.util.HashMap;

public class net {
    public ArrayList<netNode> netNodes;

    public net() {
        this.netNodes = new ArrayList<netNode>();
    }

    public void add(netNode node) {
        if (!exist(node)) {
            this.netNodes.add(node);
            addChild();
        }
    }

    public boolean exist(netNode node) {
        for (int i = 0; i < this.netNodes.size(); i++) {
            if (this.netNodes.get(i).getName() == node.getName()) {
                return true;
            }
        }
        return false;
    }
    public boolean inParents(netNode e, String name){
        if(e.getName().equals(name)){
            return true;
        }
        for(int i=0;i<e.getParents().size();i++){
            return inParents(e.getParents().get(i),name);
        }
        return false;
    }

    public netNode getByString(String n) {
        for (int i = 0; i < this.netNodes.size(); i++) {
            if (this.netNodes.get(i).getName().equals(n)) {
                return this.netNodes.get(i);
            }
        }
        return null;
    }

    public void addChild() {
        for (int i = 0; i < this.netNodes.size(); i++) {
            for (int j = 0; j < this.netNodes.get(i).parents.size(); j++) {
                boolean found = false;
                for (int h = 0; h < getByString(this.netNodes.get(i).parents.get(j).getName()).getChilds().size(); h++) {
                    if (this.netNodes.get(i).name.equals(getByString(this.netNodes.get(i).parents.get(j).getName()).getChilds().get(h).getName())) {
                        found = true;
                    }
                }
                if (!found) {
                    getByString(this.netNodes.get(i).parents.get(j).getName()).getChilds().add(this.netNodes.get(i));
                }

            }
        }
    }
}


