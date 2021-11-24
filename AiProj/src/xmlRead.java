import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class xmlRead {
    public static net makeNet(String fileName){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //create empty net
        net bNet = new net();
        // create ArrayLists that will contain the data from the xml file
        ArrayList<String> vars= new ArrayList<String>();
        ArrayList<String> outcomes= new ArrayList<String>();
        ArrayList<String> givens= new ArrayList<String>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(fileName);
            doc.getDocumentElement().normalize();
            NodeList varlist= doc.getElementsByTagName("VARIABLE");
            NodeList varlist2= doc.getElementsByTagName("DEFINITION");
            ArrayList <String> table=new ArrayList<String>();
            // run on every variable and create netNode by his data
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
                        //creat new netNode and add it to the net by his place, and if there are duplicates then remove them
                        bNet.add(new netNode(vars.get(0),givens,outcomes,bNet));
                        if(bNet.netNodes.get(bNet.netNodes.size()-1).getName()==null){
                            bNet.netNodes.remove(bNet.netNodes.size()-1);
                        }
                        netNode netnode= bNet.getByString(vars.get(0));
                        bNet.netNodes.remove(netnode);
                        bNet.netNodes.add(i,netnode);
                        givens.clear();
                        vars.clear();
                        outcomes.clear();
                }
                }
            }
            //after adding all the nodes to the net add for every netNode his childs
            bNet.addChild();
            //for every netNode build his cpt with the values from the correct table
            for(int i=0;i<bNet.netNodes.size();i++){
                bNet.netNodes.get(i).build(table.get(i));
            }
            return bNet;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return bNet;
    }
    }
