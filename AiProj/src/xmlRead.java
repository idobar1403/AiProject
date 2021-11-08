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
        ArrayList<String> vars= new ArrayList<String>();
        ArrayList<String> outcomes= new ArrayList<String>();
        ArrayList<String> givens= new ArrayList<String>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("/Users/idobar/Bsc/Year 2/AIAlgo/src/alarm_net.xml");
            doc.getDocumentElement().normalize();
            NodeList varlist= doc.getElementsByTagName("VARIABLE");
            NodeList varlist2= doc.getElementsByTagName("DEFINITION");
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
                        bNet.add(new netNode(vars.get(0),givens,bNet));
                        System.out.println(vars);
                        System.out.println(outcomes);
                        System.out.println(givens);
                        givens.clear();
                        vars.clear();
                        outcomes.clear();
                        /**
                     NodeList outlist=v.getChildNodes();
                     Node n=outlist.item(1);
                     if(n.getNodeType()==Node.ELEMENT_NODE) {
                     Element name = (Element) n;
                     String content = name.getTextContent();
                     String [] c=content.split("\r");
                     System.out.println(c[0]);
                     content.split(System.lineSeparator());
                     // System.out.println(name.getTextCntent().stripIndent());//.split("\n"));
                     }

                     for(int j=0;j<outlist.getLength();j++){
                     Node n=outlist.item(1);
                     if(n.getNodeType()==Node.ELEMENT_NODE){
                     Element name = (Element) n;
                     System.out.println(name.getTextContent());
                     }
                     }

                    **/
                }
                }
            }
            //bNet.addChild();
            for(int i=0;i<bNet.netNodes.size();i++) {
                for (int j = 0; j < bNet.netNodes.get(i).getChilds().size(); j++) {
                    System.out.println(bNet.netNodes.get(i).getChilds().get(j).getName());
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
    }
