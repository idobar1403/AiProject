import java.util.ArrayList;

public class bayesball {
    public String bayesBallAns(net bnet,String query){
        String [] split1 = query.split("\\|");
        String [] depen = split1[0].split("-");
        netNode start = bnet.getByString(depen[0]);
        netNode end = bnet.getByString(depen[1]);
        if (end.inParents(start)){
            return "no";
        }
        if(split1.length>1){
            //bayesball()
            String [] split2=split1[1].split(",");
            ArrayList<netNode> q = new ArrayList<netNode>();
            for(int i=0;i<split2.length;i++){
                String temp=split2[i];
                String [] split3 = temp.split("=");
                if(bnet.getByString(split3[0])!=null){
                    q.add(bnet.getByString(split3[0]));
                }
            }
            ArrayList<netNode> was = new ArrayList<netNode>();
            boolean ans = bayesball(start, end, q, false, was);
            if(ans==false){
                return "no";
            }
            else{
                return "yes";
            }


        }
       return "";
    }
    public boolean bayesball(netNode start, netNode end, ArrayList<netNode> q, boolean given, ArrayList<netNode> was){
        return false;
    }
}
