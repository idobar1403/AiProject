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
        // check if there are hiden variables
        for (int i = 0; i < givens.length; i += 2) {
            evidence.add(givens[i]);
            values.add(givens[i + 1]);
        }
        if(bayesSplit.length>1) {
            String [] hiden=bayesSplit[1].split("-");
            // add the hidens to arraytList
            for(int i=0;i<hiden.length;i++){
                hidens.add(hiden[i]);
            }
            // add the evidence to arrayList and the values to another

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
            for(int i=0;i< hidens.size();i++) {
                if (rNet.getByString(hidens.get(i)) != null) {
                    ghidens.add(hidens.get(i));
                }
            }
        }
            if(rNet.getByString(query[0]).getFactor().get(0).size()-1==evidence.size()+1) {
                for (int i = 0; i < rNet.getByString(query[0]).getFactor().size(); i++) {
                    boolean found = true;
                    if (rNet.getByString(query[0]).getFactor().get(i).get(query[0]).equals(query[1])) {
                        for (int j = 0; j < evidence.size(); j++) {
                            if (rNet.getByString(query[0]).getFactor().get(i).get(evidence.get(j)) != null) {
                                if (!rNet.getByString(query[0]).getFactor().get(i).get(evidence.get(j)).equals(values.get(j))) {
                                    found = false;
                                }
                            } else {
                                found=false;
                            }
                        }
                    }
                    else{
                        found=false;
                    }
                    if (found) {
                        return (String) rNet.getByString(query[0]).getFactor().get(i).get("P");
                    }
                }
            }

        ArrayList<ArrayList<HashMap<String,String>>> factors= new ArrayList<ArrayList<HashMap<String,String>>>();
        ArrayList<ArrayList<HashMap>> sorted = new ArrayList<ArrayList<HashMap>>();
        ArrayList<ArrayList<HashMap<String,String>>> keep = new ArrayList<ArrayList<HashMap<String,String>>>();
        ArrayList<String> names=new ArrayList<String>();
        ArrayList<String> names1=new ArrayList<String>();
        for(int i =0;i< evidence.size();i++) {
            if (rNet.netNodes.contains(bNet.getByString(evidence.get(i)))) {
                for (int j = 0; j < rNet.netNodes.size(); j++) {
                    if (rNet.netNodes.get(j).getFactor().get(0).get(evidence.get(i))!=null) {
                        for (int h = 0; h < rNet.netNodes.get(j).getFactor().size(); h++) {
                            //get the correct values of the
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
        ArrayList<ArrayList<HashMap<String,String>>> joined = new ArrayList<ArrayList<HashMap<String,String>>>();
        ArrayList<netNode> getCpt = new ArrayList<netNode>();
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

            for (int i = 0; i < names.size(); i++) {
                getCpt.add(relevent.get(i));
            }
            //run on all of the hiden and join them
            for (int i = 0; i < relevent.size(); i++) {
                if (relevent.size() > 1) {
                    joined.add(join(relevent.get(i), relevent.get(i + 1), rNet));
                    for (int j = 0; j < relevent.get(i).getFactor().size(); j++) {
                        HashMap temp = (HashMap) relevent.get(i).getFactor().get(j).clone();
                        String p = temp.get("P").toString();
                        double p1 = Double.parseDouble(p);
                        temp.remove("P");
                        joined.get(i).indexOf(temp);
                        for (int z = 0; z < relevent.get(i + 1).getFactor().size(); z++) {
                            HashMap temp1 = (HashMap) relevent.get(i + 1).getFactor().get(z).clone();
                            String p3 = temp1.get("P").toString();
                            double p2 = Double.parseDouble(p3);
                            temp1.remove("P");
                            for (int m = 0; m < joined.get(joined.size()-1).size(); m++) {
                                boolean n = true;
                                for (Object key : temp.keySet()) {
                                    if (joined.get(joined.size()-1).get(m).get(key) != null) {
                                        if (!joined.get(joined.size()-1).get(m).get(key).equals(temp.get(key))) {
                                            n = false;
                                        }
                                    }
                                    else{
                                        n=false;
                                    }
                                }
                                for (Object key : temp1.keySet()) {
                                    if (joined.get(joined.size()-1).get(m).get(key) != null) {
                                        if (!joined.get(joined.size()-1).get(m).get(key).equals(temp1.get(key))) {
                                            n = false;
                                        }
                                    }
                                    else {
                                        n=false;
                                    }
                                }
                                if (n == true) {
                                    mul++;
                                    p2=p2*p1;
                                    String stringAnswer=""+p2;
                                    joined.get(joined.size()-1).get(m).put("P", stringAnswer);
                                }
                            }
                        }
                    }
                    relevent.remove(i);
                    relevent.remove(i);
                    i--;
                }
            }
            if (relevent.size() == 1) {
                joined.add(0, relevent.get(0).getFactor());
            }
            for (int i = 0; i < joined.size(); i++) {
                getCpt.get(i).setFactor(joined.get(i));
            }
            for (int i = joined.size(); i < getCpt.size(); i++) {
                getCpt.remove(i);
            }
            for (int i = 0; i < getCpt.size(); i++) {
                if (joined.size() > 1) {
                    joined.add(join(getCpt.get(i), getCpt.get(i + 1), rNet));
                    for (int j = 0; j < getCpt.get(i).getFactor().size(); j++) {
                        HashMap temp = (HashMap) getCpt.get(i).getFactor().get(j).clone();
                        String p = temp.get("P").toString();
                        double p1 = Double.parseDouble(p);
                        temp.remove("P");
                        joined.get(i).indexOf(temp);
                        for (int z = 0; z < getCpt.get(i + 1).getFactor().size(); z++) {
                            HashMap temp1 = (HashMap) getCpt.get(i + 1).getFactor().get(z).clone();
                            String p3 = temp1.get("P").toString();
                            double p2 = Double.parseDouble(p3);
                            temp1.remove("P");
                            for (int m = 0; m < joined.get(joined.size() - 1).size(); m++) {
                                boolean n = true;
                                for (Object key : temp.keySet()) {
                                    if (!joined.get(joined.size() - 1).get(m).get(key).equals(temp.get(key))) {
                                        n = false;
                                    }
                                }
                                for (Object key : temp1.keySet()) {
                                    if (!joined.get(joined.size() - 1).get(m).get(key).equals(temp1.get(key))) {
                                        n = false;
                                    }
                                }
                                if (n == true) {
                                    mul++;
                                    p2=p2*p1;
                                    String stringAnswer=""+p2;
                                    joined.get(joined.size()-1).get(m).put("P", stringAnswer);
                                }
                            }
                        }
                    }
                    joined.remove(i);
                    joined.remove(i);
                    getCpt.get(i).setFactor(joined.get(joined.size() - 1));
                    getCpt.remove(i + 1);
                    i--;
                }
            }
            for (int i = 0; i < joined.get(0).size(); i++) {

                HashMap temp = (HashMap) joined.get(0).get(i).clone();
                String p = temp.get("P").toString();
                double p1 = Double.parseDouble(p);
                temp.remove("P");
                double sum = p1;
                for (int j = i + 1; j < joined.get(0).size(); j++) {
                    HashMap temp1 = (HashMap) joined.get(0).get(j).clone();
                    String p3 = temp1.get("P").toString();
                    double p2 = Double.parseDouble(p3);
                    temp1.remove("P");
                    boolean n = true;

                    for (Object key : temp.keySet()) {
                        if (!(temp.get(key).equals(temp1.get(key))) && (!key.toString().equals(ghidens.get(h)))) {
                            n = false;
                        }
                    }
                    if(n==true){
                        adding++;
                        sum+=p2;
                        joined.get(0).remove(j);
                    }
                }
                joined.get(0).get(i).remove(ghidens.get(h));
                String stringSum=""+sum;
                joined.get(0).get(i).replace("P",stringSum);
            }
            factors.add(joined.get(0));
            keep.clear();
            names.clear();
        }

            if(!names1.contains(query[0])){
                getCpt.add(rNet.getByString(query[0]));
                joined.add(rNet.getByString(query[0]).getFactor());
            }
        for (int i = 0; i < getCpt.size(); i++) {
            if (joined.size() > 1) {
                joined.add(join(getCpt.get(i), getCpt.get(i + 1), rNet));
                for (int j = 0; j < getCpt.get(i).getFactor().size(); j++) {
                    HashMap temp = (HashMap) getCpt.get(i).getFactor().get(j).clone();
                    String p = temp.get("P").toString();
                    double p1 = Double.parseDouble(p);
                    temp.remove("P");
                    joined.get(i).indexOf(temp);
                    for (int z = 0; z < getCpt.get(i + 1).getFactor().size(); z++) {
                        HashMap temp1 = (HashMap) getCpt.get(i + 1).getFactor().get(z).clone();
                        String p3 = temp1.get("P").toString();
                        double p2 = Double.parseDouble(p3);
                        temp1.remove("P");
                        for (int m = 0; m < joined.get(joined.size() - 1).size(); m++) {
                            boolean n = true;
                            for (Object key : temp.keySet()) {
                                if (!joined.get(joined.size() - 1).get(m).get(key).equals(temp.get(key))) {
                                    n = false;
                                }
                            }
                            for (Object key : temp1.keySet()) {
                                if (!joined.get(joined.size() - 1).get(m).get(key).equals(temp1.get(key))) {
                                    n = false;
                                }
                            }
                            if (n == true) {
                                mul++;
                                p2=p2*p1;
                                String stringAnswer=""+p2;
                                joined.get(joined.size() - 1).get(m).put("P", stringAnswer);
                            }
                        }
                    }
                }
                joined.remove(i);
                joined.remove(i);
                getCpt.get(i).setFactor(joined.get(joined.size() - 1));
                getCpt.remove(i + 1);
                i--;
            }
        }


        double answer=0;
        double sum=0;
        for(int i=0;i< joined.get(0).size();i++){
            HashMap temp = (HashMap) joined.get(0).get(i).clone();
            String p = temp.get("P").toString();
            double p1 = Double.parseDouble(p);
            temp.remove("P");
            sum+=p1;
            adding++;
            boolean n=true;
            for (Object key : temp.keySet()) {
                if(key.toString().equals(query[0])) {
                    if ((!(temp.get(key).equals(query[1])))) {
                        n = false;
                    }
                }
                else if(evidence.contains(key.toString())) {
                    for (int j = 0; j < evidence.size(); j++) {
                        if (key.toString().equals(evidence.get(j))) {
                            if ((!(temp.get(key).equals(values.get(j))))) {
                                n = false;
                            }
                        }
                    }
                }
            }
            if(n==true){
                answer=p1;
                adding--;
            }

        }
        answer=answer/sum;
        answer= (double)Math.round(answer* 100000d) /100000d;
        String realAnswer=""+answer+","+adding+","+mul;
        return realAnswer;
    }

    public static ArrayList<String> sort(ArrayList<ArrayList<HashMap<String,String>>> temp,net bNet,ArrayList<String>names){
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
    public static ArrayList<HashMap<String,String>> join(netNode a, netNode b,net rNet){
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
        ArrayList<HashMap<String,String>> joind=new ArrayList<HashMap<String,String>>();
        joind=buildCpt(p);
//       for(int i=0;i<a.getParents().size();i++){
//           p.add(a.getParents().get(i));
//       }
//
//        //joind= buildCpt(a.getOutcomes(),p);
        return joind;
    }
    public static ArrayList<HashMap<String, String>> buildCpt(ArrayList<netNode> p) {
        int size = 1;
        ArrayList<HashMap<String,String>> factor = new ArrayList<HashMap<String,String>>();
        for (int i = 0; i < p.size(); i++) {
            size = size * p.get(i).getOutcomes().size();
        }
        for (int i = 0; i < size; i++) {
            factor.add(i, new LinkedHashMap<String,String>());
        }
        for(int i=0;i<p.get(0).outcomes.size();i++){
            for (int j = i; j < size; j += p.get(0).outcomes.size()) {
                factor.get(j).put(p.get(0).getName(), p.get(0).outcomes.get(i));
            }
        }
        int time=size;
        for (int i = 1; i < p.size(); i++) {
            time=time/p.get(i).getOutcomes().size();
            for (int h = 0; h < p.get(i).getOutcomes().size(); h++) {
                for (int j = (h * time); j < size; j += time*(p.get(i).getOutcomes().size()-1)) {
                    for (int m = j; m < j+time; m++) {
                        if (m < size) {
                            factor.get(m).put(p.get(i).getName(), p.get(i).getOutcomes().get(h));
                        }
                    }
                    j+=time;
                }
            }
        }
        return factor;
    }
}

