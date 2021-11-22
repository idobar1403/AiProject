import java.util.ArrayList;

public class bayesBall {
    public static String bayesBallAns(net bnet, String query) {
        //split the query in order to get the relevant noeds
        String[] split1 = query.split("\\|");
        String[] depen = split1[0].split("-");
        netNode start = bnet.getByString(depen[0]);
        netNode end = bnet.getByString(depen[1]);
        ArrayList<netNode> q = new ArrayList<netNode>();
        if (split1.length > 1) {
            String[] split2 = split1[1].split(",");
            for (int i = 0; i < split2.length; i++) {
                String temp = split2[i];
                String[] split3 = temp.split("=");
                if (bnet.getByString(split3[0]) != null) {
                    q.add(bnet.getByString(split3[0]));
                }
            }
        }
            ArrayList<netNode> was = new ArrayList<netNode>();
            boolean ans = bayesball(start,start, end, q, true, was);
            if (ans == false) {
                return "yes";
            } else {
                return "no";
            }

    }

    public static boolean bayesball(netNode src,netNode start, netNode end, ArrayList<netNode> q, boolean given, ArrayList<netNode> was) {
        if(start.getName().equals(end.getName())){
            return true;
        }
        if (q.isEmpty()) {
            if(given) {
                for (int i = 0; i < start.getParents().size(); i++) {
                    was.add(start);
                    if (!was.contains(start.getParents().get(i))||src!=start.getParents().get(i)) {
                        if (bayesball(src,start.getParents().get(i), end, q, true, was)) {
                            return true;
                        }
                    }
                }
            }
            for (int i = 0; i < start.getChilds().size(); i++) {
                was.add(start);
                if (!was.contains(start.getChilds().get(i))||src!=start.getChilds().get(i)) {
                    if (bayesball(src,start.getChilds().get(i), end, q, false, was)) {
                        return true;
                    }
                }
            }
            return false;
        }
        else{
            if(q.contains(start)){
                if(given){
                    return false;
                }
                else{
                    for(int i=0;i<start.getParents().size();i++) {
                        was.add(start);
                        if(!was.contains(start.getParents().get(i))|| src!=start.getParents().get(i)) {
                            if (bayesball(src,start.getParents().get(i), end, q, true, was)) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
            }
            else {
                if(given){
                    for (int i = 0; i < start.getParents().size(); i++) {
                        was.add(start);
                        if(!was.contains(start.getParents().get(i))||src!=start.getParents().get(i)) {
                            if (bayesball(src,start.getParents().get(i), end, q, true, was)) {
                                return true;
                            }
                        }
                    }
                }
                for(int i=0;i<start.getChilds().size();i++){
                    was.add(start);
                    if(!was.contains(start.getChilds().get(i))){
                        if(bayesball(src,start.getChilds().get(i),end,q,false,was)){
                            return true;
                        }
                    }
                }
                return false;
            }
        }
    }
}

