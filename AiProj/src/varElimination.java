import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class varElimination {
    /**
     * this function computes the probability, the number of adding actions and the number of the multiply actions for the query.
     * @param bNet the net
     * @param q the query
     * @return the answer for the query
     */
    public static String variableElimination(net bNet, String q){
        net rNet=new net();
        int multActions=0, addActions=0;
        boolean isExist;
        ArrayList<String> evidence = new ArrayList<>();
        ArrayList<String> evidenceValues = new ArrayList<>();
        ArrayList<String> hiddens = new ArrayList<>();
        ArrayList<String> relevantHidden=new ArrayList<>();
        // get the evidence, hidden and the query node by splitting the query string
        String [] bayesSplit=q.split(" ");
        String completeQuery = bayesSplit[0].substring(2,bayesSplit[0].length()-1);
        //queries for the bayesball algorithm
        String [] firstSplit = completeQuery.split("\\|");
        String queryQuestion = firstSplit[0];
        String [] query=queryQuestion.split("=");
        if(firstSplit.length>1) {
            String[] givens = firstSplit[1].split("[,=]");
            for (int i = 0; i < givens.length; i += 2) {
                evidence.add(givens[i]);
                evidenceValues.add(givens[i + 1]);
            }
        }
        rNet.add(bNet.getByString(query[0]));
        // add the evidence to arrayList and the values to other one

        // check if there are hidden nodes
        if(bayesSplit.length>1) {
            String [] hiden=bayesSplit[1].split("-");
            // add the hidens to arraytList
            hiddens.addAll(Arrays.asList(hiden));
            // check for every hidden if he is relevant
            for(int i =0 ;i<bNet.netNodes.size();i++) {
                // run only on the hiddens
                if (hiddens.contains(bNet.netNodes.get(i).getName())) {
                    boolean inParents = bNet.inParents(bNet.getByString(query[0]), bNet.netNodes.get(i).getName());
                    // check if the hidden is in the query parents
                    if (!inParents) {
                        // if not in the parents of the query check if in the parents of the evidences
                        for (String s : evidence) {
                            if (bNet.inParents(bNet.getByString(s), bNet.netNodes.get(i).getName())) {
                                inParents = true;
                                break;
                            }
                        }
                    }
                    if (inParents) {
                        // if in one of the parents of the evidences or the query then check if is independent by bayesball
                        String b = query[0] + "-" + bNet.netNodes.get(i).getName() + "|" + firstSplit[1];
                        // send to bayesball
                        if (bayesBall.bayesBallAns(bNet, b).equals("no")) {
                            // add the hidden to the net
                            rNet.add(bNet.netNodes.get(i));
                        }
                    }
                }
            }
        }
        // add to the relevant net the evidences
        for (String s : evidence) {
            rNet.add(bNet.getByString(s));
        }
        // add the relevant hiddens to list
        for (String hidden : hiddens) {
            if (rNet.getByString(hidden) != null) {
                relevantHidden.add(hidden);
            }
        }
        // check if the query is already in one of the factors
        for (int j = 0; j < rNet.netNodes.size(); j++) {
            for (int i = 0; i < rNet.netNodes.get(j).getFactor().size(); i++) {
                boolean found = true;
                // first check if the query value is the same as the value of the query question
                if (rNet.netNodes.get(j).getFactor().get(i).containsKey(query[0])) {
                    if (rNet.netNodes.get(j).getFactor().get(i).get(query[0]).equals(query[1])) {
                        // check the same for the all the evidences
                        for (int k = 0; k < evidence.size(); k++) {
                            if (rNet.netNodes.get(j).getFactor().get(i).containsKey(evidence.get(k))) {
                                if (!rNet.netNodes.get(j).getFactor().get(i).get(evidence.get(k)).equals(evidenceValues.get(k))) {
                                    found = false;
                                }
                            } else {
                                found = false;
                            }
                        }
                    } else {
                        found = false;
                    }
                    if (found) {
                        if (rNet.netNodes.get(j).getFactor().size() - 1 == evidence.size() + 1) {
                            return rNet.netNodes.get(j).getFactor().get(i).get("P") + "," + "0" + "," + "0";
                        }
                    }
                }
            }
        }
        // remove all the factors that contains not relevant hiddens
        for (String hidden : hiddens) {
            if (!relevantHidden.contains(hidden)) {
                for (int i = 0; i < rNet.netNodes.size(); i++) {
                    for (int j = 0; j < rNet.netNodes.get(i).getFactor().size(); j++) {
                        if (rNet.netNodes.get(i).getFactor().get(j).containsKey(hidden)) {
                            rNet.netNodes.get(i).getFactor().clear();
                            break;
                        }
                    }
                }
            }
        }
        HashMap<?,?>  temp;
        HashMap<?,?>  anotherTemp;
        ArrayList<ArrayList<HashMap<String,String>>> keepFactors = new ArrayList<>();
        ArrayList<String> namesOfFactors= new ArrayList<>();
        // remove the rows with different evidence value of the given evidence
        for (int k = 0; k < evidence.size() ; k++) {
            for(int i =0;i< rNet.netNodes.size();i++) {
                if (rNet.netNodes.get(i).getFactor().size() > 0) {
                    if (rNet.netNodes.get(i).getFactor().get(0).containsKey(evidence.get(k))) {
                        for (int h = 0; h < rNet.netNodes.get(i).getFactor().size(); h++) {
                            //get the correct values of the evidence and remove the not correct values
                            String tempValue = rNet.netNodes.get(i).getFactor().get(h).get(evidence.get(k));
                            if (!tempValue.equals(evidenceValues.get(k))) {
                                if (rNet.getByString(evidence.get(k)) != null) {
                                    rNet.getByString(evidence.get(k)).getOutcomes().remove(tempValue);
                                }
                                rNet.netNodes.get(i).getFactor().remove(h);
                                h--;
                            }
                        }
                    }
                }
            }
        }
        ArrayList<ArrayList<HashMap<String,String>>> joinedFactors = new ArrayList<>();
        ArrayList<netNode> factorsOfHidden = new ArrayList<>();
        // run on every hidden that is relevant
        for (String s : relevantHidden) {
            for (int i = 0; i < rNet.netNodes.size(); i++) {
                for (int j = 0; j < rNet.netNodes.get(i).getFactor().size(); j++) {
                    if (rNet.netNodes.get(i).getFactor().get(j).containsKey(s)) {
                        // add the factors that contain the hidden
                        if (!keepFactors.contains(rNet.netNodes.get(i).getFactor())) {
                            keepFactors.add(rNet.netNodes.get(i).getFactor());
                            namesOfFactors.add(rNet.netNodes.get(i).getName());
                        }
                    }
                }
            }

            // sort the keepFactors ArrayList
            sort(keepFactors, rNet, namesOfFactors);
            ArrayList<netNode> relevantFactors = new ArrayList<>();
            // add the nodes that their factors contains the hidden
            for (String ofFactor : namesOfFactors) {
                relevantFactors.add(rNet.getByString(ofFactor));
                factorsOfHidden.add(rNet.getByString(ofFactor));
            }
            // add the factors to the joinedFactors ArrayList
            for (netNode factor : relevantFactors) {
                joinedFactors.add(factor.getFactor());
            }
            //run on all the factors of the hidens and join them
            for (int i = 0; i < relevantFactors.size(); i++) {
                if (relevantFactors.size() > 1) {
                    // send the joined factors to the joinedFactors ArrayList
                    joinedFactors.add(join(relevantFactors.get(i), relevantFactors.get(i + 1), rNet));
                    // run on the factors and match the lines in order to multiply the new joined lines
                    for (int j = 0; j < relevantFactors.get(i).getFactor().size(); j++) {
                        // get the first line of the first factor
                        temp = (HashMap<?,?> ) relevantFactors.get(i).getFactor().get(j).clone();
                        String p = temp.get("P").toString();
                        // get his probability
                        double p1 = Double.parseDouble(p);
                        temp.remove("P");
                        joinedFactors.get(i).indexOf(temp);
                        // run over the second factor
                        for (int z = 0; z < relevantFactors.get(i + 1).getFactor().size(); z++) {
                            // get the first line of the second factor
                            anotherTemp = (HashMap<?,?> ) relevantFactors.get(i + 1).getFactor().get(z).clone();
                            String p3 = anotherTemp.get("P").toString();
                            double p2 = Double.parseDouble(p3);
                            anotherTemp.remove("P");
                            for (int m = 0; m < joinedFactors.get(joinedFactors.size() - 1).size(); m++) {
                                // check if the value of the keys are the same as the joined
                                isExist = isSameAsJoined(joinedFactors, temp, m, true);
                                // check if the value of the keys are the same as the joined
                                isExist = isSameAsJoined(joinedFactors, anotherTemp, m, isExist);
                                // if founded the correct line then multiply the probabilities
                                if (isExist) {
                                    multActions++;
                                    p2 = p2 * p1;
                                    String stringAnswer = "" + p2;
                                    joinedFactors.get(joinedFactors.size() - 1).get(m).put("P", stringAnswer);
                                    break;
                                }
                            }
                        }
                    }
                    // remove the factors that made the joined factor
                    relevantFactors.get(i + 1).setFactor(joinedFactors.get(joinedFactors.size() - 1));
                    relevantFactors.remove(i);
                    joinedFactors.remove(i);
                    joinedFactors.remove(i);
                    namesOfFactors.remove(i);
                    i--;
                    // sort the factors
                    sort(joinedFactors, rNet, namesOfFactors);
                    relevantFactors.clear();
                    for (String namesOfFactor : namesOfFactors) {
                        relevantFactors.add(rNet.getByString(namesOfFactor));
                    }
                    for (int j = 0; j < joinedFactors.size(); j++) {
                        if (joinedFactors.get(j).size() == 1) {
                            joinedFactors.remove(j);
                            relevantFactors.remove(j);
                        }
                    }
                }
            }
            // this will eliminate the hidden variable by running on all of his possible outcomes
            // can be more than two outcomes for every hidden
            for (int i = 0; i < joinedFactors.get(0).size(); i++) {
                temp = (HashMap<?,?> ) joinedFactors.get(0).get(i).clone();
                String p = temp.get("P").toString();
                double p1 = Double.parseDouble(p);
                temp.remove("P");
                double sum = p1;
                for (int j = i + 1; j < joinedFactors.get(0).size(); j++) {
                    anotherTemp = (HashMap<?,?> ) joinedFactors.get(0).get(j).clone();
                    String p3 = anotherTemp.get("P").toString();
                    double p2 = Double.parseDouble(p3);
                    anotherTemp.remove("P");
                    isExist = true;
                    // check if the values in the factor are matched besides the hidden himself
                    for (Object key : temp.keySet()) {
                        if (!(temp.get(key).equals(anotherTemp.get(key))) && (!key.toString().equals(s))) {
                            isExist = false;
                        }
                    }
                    // found match row then we can add them together
                    if (isExist) {
                        addActions++;
                        sum += p2;
                        joinedFactors.get(0).remove(j);
                        j--;
                    }
                }
                // remove the hidden from the row that had been found matched
                joinedFactors.get(0).get(i).remove(s);
                String stringSum = "" + sum;
                joinedFactors.get(0).get(i).replace("P", stringSum);
            }
            // remove the hidden factor at the end of the elimination
            factorsOfHidden.remove(rNet.getByString(s));
            rNet.netNodes.remove(rNet.getByString(s));
            for (int i = 0; i < factorsOfHidden.size() - 1; i++) {
                factorsOfHidden.remove(i);
            }

            factorsOfHidden.get(0).setFactor(joinedFactors.get(0));
            // remove all the factors that as been used in the elimination
            // to make sure that the hidden will not appear anymore
            for (int i = 0; i < rNet.netNodes.size(); i++) {
                if (rNet.netNodes.get(i).getFactor().size() > 0) {
                    if (rNet.netNodes.get(i).getFactor().get(0).containsKey(s)) {
                        rNet.netNodes.get(i).getFactor().clear();
                    }
                }
            }
            joinedFactors.clear();
            factorsOfHidden.clear();
            keepFactors.clear();
            namesOfFactors.clear();
            // clear every factor that has only one row
            for (int i = 0; i < rNet.netNodes.size(); i++) {
                if (rNet.netNodes.get(i).getFactor().size() == 1) {
                    rNet.netNodes.get(i).getFactor().clear();
                }
            }
        }
        // add all the factors that contains the query
        for (int i = 0; i < rNet.netNodes.size(); i++) {
            for (int j = 0; j < rNet.netNodes.get(i).getFactor().size(); j++) {
                if (rNet.netNodes.get(i).getFactor().get(j).containsKey(query[0])&&!joinedFactors.contains(rNet.netNodes.get(i).getFactor())) {
                joinedFactors.add(rNet.netNodes.get(i).getFactor());
                factorsOfHidden.add(rNet.netNodes.get(i));
                namesOfFactors.add(rNet.netNodes.get(i).getName());
                break;
                }
            }
        }
        // the same action of join but now on the query
        for (int i = 0; i < factorsOfHidden.size(); i++) {
            if (joinedFactors.size() > 1) {
                joinedFactors.add(join(factorsOfHidden.get(i), factorsOfHidden.get(i + 1), rNet));
                // run over all the factors and fined two rows that matches for the multiply action
                for (int j = 0; j < factorsOfHidden.get(i).getFactor().size(); j++) {
                    temp = (HashMap<?,?>)factorsOfHidden.get(i).getFactor().get(j).clone();
                    String p = temp.get("P").toString();
                    double p1 = Double.parseDouble(p);
                    temp.remove("P");
                    joinedFactors.get(i).indexOf(temp);
                    // find the second row
                    for (int z = 0; z < factorsOfHidden.get(i + 1).getFactor().size(); z++) {
                        anotherTemp = (HashMap<?,?> ) factorsOfHidden.get(i + 1).getFactor().get(z).clone();
                        String p3 = anotherTemp.get("P").toString();
                        double p2 = Double.parseDouble(p3);
                        anotherTemp.remove("P");
                        for (int m = 0; m < joinedFactors.get(joinedFactors.size() - 1).size(); m++) {
                            // check if the value of the keys are the same as the joined
                            isExist = isSameAsJoined(joinedFactors, temp, m, true);
                            // check if the value of the keys are the same as the joined
                            isExist = isSameAsJoined(joinedFactors, anotherTemp, m, isExist);
                            // if found two matches rows then multiply their probabilities
                            if (isExist) {
                                multActions++;
                                p2=p2*p1;
                                String stringAnswer=""+p2;
                                joinedFactors.get(joinedFactors.size() - 1).get(m).put("P", stringAnswer);
                            }
                        }
                    }
                }
                factorsOfHidden.get(i+1).setFactor(joinedFactors.get(joinedFactors.size()-1));
                factorsOfHidden.remove(i);
                joinedFactors.remove(i);
                joinedFactors.remove(i);
                namesOfFactors.remove(i);
                i--;
                sort(joinedFactors,rNet,namesOfFactors);
                factorsOfHidden.clear();
                for (String namesOfFactor : namesOfFactors) {
                    factorsOfHidden.add(rNet.getByString(namesOfFactor));
                }
            }
        }
        double answer=0;
        double sum=0;
        // now I need to look for the row that matches the query and normalize it
        for(int i=0;i< joinedFactors.get(0).size();i++){
            temp = (HashMap<?, ?>) joinedFactors.get(0).get(i).clone();
            String p = temp.get("P").toString();
            double p1 = Double.parseDouble(p);
            temp.remove("P");
            sum+=p1;
            addActions++;
            isExist =true;
            // check if the key is the query, if so then check it value
            for (Object key : temp.keySet()) {
                if(key.toString().equals(query[0])) {
                    if ((!(temp.get(key).equals(query[1])))) {
                        isExist = false;
                    }
                }
                // check if the key is evidence, if so check it value
                else if(evidence.contains(key.toString())) {
                    for (int j = 0; j < evidence.size(); j++) {
                        if (key.toString().equals(evidence.get(j))) {
                            if ((!(temp.get(key).equals(evidenceValues.get(j))))) {
                                isExist = false;
                            }
                        }
                    }
                }
            }
            // found the query row so that will be the answer
            if(isExist){
                answer=p1;
                addActions--;
            }
        }
        // round the answer closer to 0
        answer=answer/sum;
        answer= (double)Math.round(answer* 100000d) /100000d;
        return (""+answer+","+addActions+","+multActions);
    }

    private static boolean isSameAsJoined(ArrayList<ArrayList<HashMap<String, String>>> joinedFactors,
                                          HashMap<?,?> temp, int m, boolean n) {
        for (Object key : temp.keySet()) {
            if (joinedFactors.get(joinedFactors.size() - 1).get(m).get((String)key) != null) {
                if (!joinedFactors.get(joinedFactors.size() - 1).get(m).get((String)key).equals(temp.get(key))) {
                    n = false;
                }
            } else {
                n = false;
            }
        }
        return n;
    }

    /**
     * this function will sort the ArrayList that contains ArrayList of HashMaps
     * @param temp the ArrayList<ArrayList<HashMap<String,String>>> that the function is sorting
     * @param bNet the net
     * @param names the names of every node in the ArrayList<ArrayList<HashMap<String,String>>>
     */
    public static void sort(ArrayList<ArrayList<HashMap<String,String>>> temp, net bNet,
                            ArrayList<String>names){
        // bubble sort with the sizes of the ArrayLists
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
                // if there are two ArrayLists with the same size then sort them by their asci size
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
    }
    /**
     * this function will get two nodes that has factors and it will merge their factors
     * @param a netNode
     * @param b netNode
     * @param rNet the ner of netNode
     * @return new merged factor
     */
    public static ArrayList<HashMap<String,String>> join(netNode a, netNode b,net rNet){
       ArrayList<netNode> p=new ArrayList<>();
       // add the netNodes that are only in b
       for(String key:b.getFactor().get(0).keySet()){
           if(a.getFactor().get(0).get(key)==null) {
               if (rNet.getByString(key) != null) {
                   p.add(rNet.getByString(key));
               }
           }
       }
       // add the remaining netNodes
       for(String key:a.getFactor().get(0).keySet()){
           if(!key.equals("P")) {
               p.add(rNet.getByString(key));
           }
       }
        // send the ArrayList of netNodes to the buildCpt function
        return buildFactor(p);
    }
    /**
     * build the factor given the netNodes that are in the factor
     * @param p the ArrayList of netNodes that in the factor
     * @return the new factor
     */
    public static ArrayList<HashMap<String, String>> buildFactor(ArrayList<netNode> p) {
        int size = 1;
        // compute the size of the new factor
        ArrayList<HashMap<String,String>> factor = new ArrayList<>();
        for (netNode netNode : p) {
            size = size * netNode.getOutcomes().size();
        }
        // create in every place new LinkedHashMap in order to keep the putting order
        for (int i = 0; i < size; i++) {
            factor.add(i, new LinkedHashMap<>());
        }
        // place the first netNode values before placing every other netNode values
        for(int i=0;i<p.get(0).outcomes.size();i++){
            for (int j = i; j < size; j += p.get(0).outcomes.size()) {
                factor.get(j).put(p.get(0).getName(), p.get(0).outcomes.get(i));
            }
        }
        // run over the netNodes and add the values of the netNodes outcomes at the correct place
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