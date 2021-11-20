import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public class xmlRead {
    public static void main(String[] args) {
        net bNet = new net();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ArrayList<netNode> arrNode = new ArrayList<netNode>();
        ArrayList<String> vars= new ArrayList<String>();
        ArrayList<String> outcomes= new ArrayList<String>();
        ArrayList<String> givens= new ArrayList<String>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("/Users/idobar/Bsc/Year 2/AIAlgoProject/AiProject/AiProj/src/alarm_net.xml");
            doc.getDocumentElement().normalize();
            NodeList varlist= doc.getElementsByTagName("VARIABLE");
            NodeList varlist2= doc.getElementsByTagName("DEFINITION");
            ArrayList <String> table=new ArrayList<String>();
            for(int i=0; i<varlist.getLength();i++){
                varlist=doc.getElementsByTagName("VARIABLE");
                Node var=varlist.item(i);
                String names="";
                if(var.getNodeType()==Node.ELEMENT_NODE){
                    Element v = (Element) var;
                    names = v.getElementsByTagName("NAME").item(0).getTextContent();
                    vars.add(names);
                    for(int j=0;j< v.getElementsByTagName("OUTCOME").getLength();j++){
                        outcomes.add(v.getElementsByTagName("OUTCOME").item(j).getTextContent());
                    }
                    varlist2=doc.getElementsByTagName("DEFINITION");
                    Node var2=varlist2.item(i);
                    String names2="";
                    if(var2.getNodeType()==Node.ELEMENT_NODE){
                        Element v2 = (Element) var2;
                        names2 = v2.getElementsByTagName("FOR").item(0).getTextContent();
                        for(int j=0;j< v2.getElementsByTagName("GIVEN").getLength();j++){
                            givens.add(v2.getElementsByTagName("GIVEN").item(j).getTextContent());
                        }
                        for(int h=0;h<v2.getElementsByTagName("TABLE").getLength();h++){
                            table.add(v2.getElementsByTagName("TABLE").item(h).getTextContent());
                        }
                        bNet.add(new netNode(vars.get(0),givens,outcomes,bNet));
                        if(bNet.netNodes.get(bNet.netNodes.size()-1).getName()==null){
                            bNet.netNodes.remove(bNet.netNodes.size()-1);
                        }
                        netNode netnode= bNet.getByString(vars.get(0));
                        bNet.netNodes.remove(netnode);
                        bNet.netNodes.add(i,netnode);
                        System.out.println(vars);
                        System.out.println(outcomes);
                        System.out.println(givens);
                        givens.clear();
                        vars.clear();
                        outcomes.clear();

                }
                }
            }
            bNet.addChild();
            for(int i=0;i<bNet.netNodes.size();i++){
                bNet.netNodes.get(i).build(table.get(i));
            }
            //System.out.println(bayesBall.bayesBallAns(bNet,"C2-A3|B3=T,C1=T"));
           System.out.println(varElimination.variableElimination(bNet,"P(J=T|B=T) A-E-M"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
    }
