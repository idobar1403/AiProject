import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class varElimination {
    public static String variableElimination(net bNet, String q){
        ArrayList<String> evidence = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();
        ArrayList<String> hidens = new ArrayList<String>();
        ArrayList<String> ghidens=new ArrayList<String>();
        int mul=0;
        int adding=0;
        net rNet=new net();
        String [] bayesSplit=q.split(" ");
        String ans = bayesSplit[0].substring(2);
        String [] secSplit = ans.split("\\)");
        //q for the bayesball
        String bayesAns = secSplit[0];
        String [] firstSplit = bayesAns.split("\\|");
        String ask = firstSplit[0];
        String [] query=ask.split("=");
        String [] givens= firstSplit[1].split(",|=");
        rNet.netNodes.add(bNet.getByString(query[0]));
        if(bayesSplit.length>1) {
            String [] hiden=bayesSplit[1].split("-");
            for(int i=0;i<hiden.length;i++){
                hidens.add(hiden[i]);
            }
            for (int i = 0; i < givens.length; i += 2) {
                evidence.add(givens[i]);
                values.add(givens[i + 1]);
            }
            for(int i =0 ;i<bNet.netNodes.size();i++) {
                if (!bNet.netNodes.get(i).getName().equals(query[0])) {
                    boolean inParents = false;
                    if (bNet.inParents(bNet.getByString(query[0]), bNet.netNodes.get(i).getName())) {
                        inParents = true;
                    }
                    if (!inParents) {
                        for (int j = 0; j < evidence.size(); j++) {
                            if (bNet.inParents(bNet.getByString(evidence.get(j)), bNet.netNodes.get(i).getName())) {
                                inParents = true;
                                break;
                            }
                        }
                    }
                    if (inParents) {
                        if (evidence.contains(bNet.netNodes.get(i).getName())) {
                            String b=query[0]+"-"+bNet.netNodes.get(i).getName()+"|";
                            for (int j = 0; j < evidence.size(); j++) {
                                if (!evidence.get(j).equals(bNet.netNodes.get(i).getName())) {
                                    b=b+evidence.get(j)+"="+values.get(j)+",";
                                }
                            }
                            b=b.substring(0,b.length()-1);
                            if (bayesBall.bayesBallAns(bNet, b).equals("no")) {
                                rNet.netNodes.add(bNet.netNodes.get(i));
                            }
                        }
                        else {
                            String b = query[0] + "-" + bNet.netNodes.get(i).getName() + "|" + firstSplit[1];
                            if (bayesBall.bayesBallAns(bNet, b).equals("no")) {
                                rNet.netNodes.add(bNet.netNodes.get(i));
                            }
                        }
                    }
                }
            }
            for (int i=0;i<rNet.netNodes.size();i++){
                if(hidens.contains(rNet.netNodes.get(i).getName())){
                    ghidens.add(rNet.netNodes.get(i).getName());
                }
            }
            /**
            for(int i=0;i< hidens.size();i++){
                boolean inParents=false;
                if(bNet.inParents(bNet.getByString(query[0]), hidens.get(i))){
                    inParents=true;
                }
                if(!inParents){
                    for (int j=0;j<evidence.size();j++){
                        if(bNet.inParents(bNet.getByString(evidence.get(j)), hidens.get(i))){
                            inParents=true;
                            break;
                        }
                    }
                }
                else if(inParents){
                    String b= query[0]+"-"+hidens.get(i)+"|"+firstSplit[1];
                    if(bayesBall.bayesBallAns(bNet,b).equals("no")){
                        ghidens.add(hidens.get(i));
                    }
                }
            }
             **/
        }
        ArrayList<ArrayList<HashMap>> factors= new ArrayList<ArrayList<HashMap>>();
        ArrayList<ArrayList<HashMap>> sorted = new ArrayList<ArrayList<HashMap>>();
        ArrayList<ArrayList<HashMap>> keep = new ArrayList<ArrayList<HashMap>>();
        ArrayList<String> names=new ArrayList<String>();
        ArrayList<String> names1=new ArrayList<String>();
        for(int i =0;i< evidence.size();i++) {
            if (rNet.netNodes.contains(bNet.getByString(evidence.get(i)))) {
                for (int j = 0; j < rNet.netNodes.size(); j++) {
                    if (rNet.netNodes.get(j).getFactor().get(0).get(evidence.get(i))!=null) {
                        for (int h = 0; h < rNet.netNodes.get(j).getFactor().size(); h++) {
                            if(!rNet.netNodes.get(j).getFactor().get(h).get(evidence.get(i)).equals(values.get(i))){
                                rNet.getByString(evidence.get(i)).getOutcomes().remove(rNet.netNodes.get(j).getFactor().get(h).get(evidence.get(i))) ;
                                rNet.netNodes.get(j).getFactor().remove(h);
                                h--;
                            }
                        }
                    }
                }
            }
        }
        for(int h=0;h<ghidens.size();h++) {
            for (int i = 0; i < rNet.netNodes.size(); i++) {
                for (int j = 0; j < rNet.netNodes.get(i).getFactor().size(); j++) {
                    if (rNet.netNodes.get(i).getFactor().get(j).containsKey(ghidens.get(h))) {
                        if (!keep.contains(rNet.netNodes.get(i).getFactor()) && !factors.contains(rNet.netNodes.get(i).getFactor())) {
                            keep.add(rNet.netNodes.get(i).getFactor());
                            names.add(rNet.netNodes.get(i).getName());
                        }
                    }
                }
            }
            names = sort(keep, rNet, names);
            for (int j = 0; j < names.size(); j++) {
                names1.add(names.get(j));
                factors.add(keep.get(j));
            }
            ArrayList<netNode> relevent = new ArrayList<netNode>();
            for (int i = 0; i < names.size(); i++) {
                relevent.add(rNet.getByString(names.get(i)));
            }
            //run on all of the hiden and join them
            ArrayList<ArrayList<HashMap>> joined = new ArrayList<ArrayList<HashMap>>();
            for (int i = 0; i < relevent.size(); i++) {
                joined.add(join(relevent.get(i), relevent.get(i + 1), rNet));
                for (int j = 0; j < relevent.get(i).getFactor().size(); j++) {
                    HashMap temp = (HashMap) relevent.get(i).getFactor().get(j).clone();
                    String p=temp.get("P").toString();
                    System.out.println(p);
                    double p1 = Double.parseDouble(p);
                    temp.remove("P");
                    joined.get(i).indexOf(temp);
                    for (int z = 0; z < relevent.get(i+1).getFactor().size(); z++) {
                        HashMap temp1 = (HashMap) relevent.get(i + 1).getFactor().get(z).clone();
                        String p3=temp1.get("P").toString();
                        double p2 = Double.parseDouble(p3);
                        temp1.remove("P");
                        for (int m = 0; m < joined.get(i).size(); m++) {
                            boolean n = true;
                            for (Object key : temp.keySet()) {
                                if (!joined.get(i).get(m).get(key).equals(temp.get(key))) {
                                    n = false;
                                }
                            }
                            for (Object key : temp1.keySet()) {
                                if (!joined.get(i).get(m).get(key).equals(temp1.get(key))) {
                                    n = false;
                                }
                            }
                            if (n == true) {
                                mul++;
                                joined.get(i).get(m).put("P", p1 * p2);
                            }
                        }
                    }
                }
               relevent.remove(i);
               relevent.remove(i);
               i--;
            }
            keep.clear();
            names.clear();
        }
        return null;

    }
    public static ArrayList<String> sort(ArrayList<ArrayList<HashMap>> temp,net bNet,ArrayList<String>names){
        for(int i=0;i< temp.size();i++){
            for(int j=1;j< temp.size()-i;j++){
                if(temp.get(j-1).size()>temp.get(j).size()){
                    temp.add(j+1,temp.get(j-1));
                    temp.remove(j-1);
                    names.add(j+1,names.get(j-1));
                    names.remove(j-1);
                }
            }
        }
        for(int i=0;i<temp.size()-1;i++){
            for(int j=1;j<temp.size()-i;j++) {
                if (temp.get(j).size() == temp.get(j-1).size()) {
                    int sizeAsci1=bNet.getByString(names.get(j-1)).getAsciSize();
                    int sizeAsci2=bNet.getByString(names.get(j)).getAsciSize();
                    if(sizeAsci1>sizeAsci2){
                        temp.add(j+1,temp.get(j-1));
                        temp.remove(j-1);
                        names.add(j+1,names.get(j-1));
                        names.remove(j-1);
                    }
                }
            }
        }
        return names;
    }
    public static ArrayList<HashMap> join(netNode a, netNode b,net rNet){
       int size =a.getFactor().size();
       ArrayList<netNode> p=new ArrayList<netNode>();
       for(Object key:b.getFactor().get(0).keySet()){
           if(a.getFactor().get(0).get(key)==null){
               size=size*rNet.getByString((String) key).getOutcomes().size();
               p.add(rNet.getByString((String) key));
           }
       }
       for(Object key:a.getFactor().get(0).keySet()){
           if(!key.equals("P")) {
               p.add(rNet.getByString((String) key));
           }
       }
        ArrayList<HashMap> joind=new ArrayList<HashMap>();
        joind=buildCpt(p);
//       for(int i=0;i<a.getParents().size();i++){
//           p.add(a.getParents().get(i));
//       }
//
//        //joind= buildCpt(a.getOutcomes(),p);
        return joind;
    }
    public static ArrayList<HashMap> buildCpt(ArrayList<netNode> p) {
        int size = 1;
        ArrayList<HashMap> factor = new ArrayList<HashMap>();
        for (int i = 0; i < p.size(); i++) {
            size = size * p.get(i).getOutcomes().size();
        }
        for (int i = 0; i < size; i++) {
            factor.add(i, new LinkedHashMap());
        }
        for(int i=0;i<p.get(0).outcomes.size();i++){
            for (int j = i; j < size; j += p.get(0).outcomes.size()) {
                factor.get(j).put(p.get(0).getName(), p.get(0).outcomes.get(i));
            }
        }
        int t=1;
        for (int i = 1; i < p.size(); i++) {
            t = t * p.get(i).getOutcomes().size();
            for (int h = 0; h < p.get(i).getOutcomes().size(); h++) {
                if (p.get(i).outcomes.size() == 1) {
                    for (int j = 0; j < size; j++) {
                        factor.get(j).put(p.get(i).getName(), p.get(i).getOutcomes().get(h));
                    }
                    //t=t*p.get(i).outcomes.size();
                } else {
                    int times = (int) Math.pow(p.get(i).getOutcomes().size(), (-i - 1));
                    for (int j = (h * t); j < size; j += t) {
                        for (int m = j; m < j + t; m++) {
                            factor.get(m).put(p.get(i).getName(), p.get(i).getOutcomes().get(h));
                        }
                        j += t;

                    }
                }
            }
        }
            return factor;
    }
}
