import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

class varEliminationTest {

    @Test
    void sort() {
    net alarmNet;
    alarmNet = xmlRead.makeNet("data/nets/alarm_net.xml");
    assertNotNull(alarmNet);
    ArrayList<ArrayList<HashMap<String,String>>> joinedFactors = new ArrayList<>();
    ArrayList<String> namesOfFactors= new ArrayList<>();
    joinedFactors.add(alarmNet.getByString("A").getFactor());
    joinedFactors.add(alarmNet.getByString("B").getFactor());
    assertTrue(joinedFactors.get(0).size()>joinedFactors.get(1).size());
    namesOfFactors.add("A");
    namesOfFactors.add("B");
    assertEquals("A",namesOfFactors.get(0));
    assertEquals("B",namesOfFactors.get(1));
    varElimination.sort(joinedFactors,alarmNet,namesOfFactors);
    assertEquals("B",namesOfFactors.get(0));
    assertEquals("A",namesOfFactors.get(1));
    assertTrue(joinedFactors.get(0).size()<joinedFactors.get(1).size());
    namesOfFactors.clear();
    joinedFactors.clear();

    // now sorting two factors with the same size in order to check sort by asci
    joinedFactors.add(alarmNet.getByString("E").getFactor());
    joinedFactors.add(alarmNet.getByString("B").getFactor());
    assertEquals(joinedFactors.get(0).size(), joinedFactors.get(1).size());
    namesOfFactors.add("E");
    namesOfFactors.add("B");
    assertEquals("E",namesOfFactors.get(0));
    assertEquals("B",namesOfFactors.get(1));
    varElimination.sort(joinedFactors,alarmNet,namesOfFactors);
    assertEquals("B",namesOfFactors.get(0));
    assertEquals("E",namesOfFactors.get(1));
    assertEquals(joinedFactors.get(0).size(), joinedFactors.get(1).size());
    namesOfFactors.clear();
    joinedFactors.clear();

    // another two factors with the same size to sort by asci
    joinedFactors.add(alarmNet.getByString("M").getFactor());
    joinedFactors.add(alarmNet.getByString("J").getFactor());
    assertEquals(joinedFactors.get(0).size(), joinedFactors.get(1).size());
    namesOfFactors.add("M");
    namesOfFactors.add("J");
    assertEquals("M",namesOfFactors.get(0));
    assertEquals("J",namesOfFactors.get(1));
    varElimination.sort(joinedFactors,alarmNet,namesOfFactors);
    assertEquals("J",namesOfFactors.get(0));
    assertEquals("M",namesOfFactors.get(1));
    assertEquals(joinedFactors.get(0).size(), joinedFactors.get(1).size());
    namesOfFactors.clear();
    joinedFactors.clear();

    // sorting three factors
    joinedFactors.add(alarmNet.getByString("A").getFactor());
    joinedFactors.add(alarmNet.getByString("B").getFactor());
    joinedFactors.add(alarmNet.getByString("J").getFactor());
    assertTrue(joinedFactors.get(0).size()>joinedFactors.get(1).size());
    assertTrue(joinedFactors.get(1).size()<joinedFactors.get(2).size());
    assertTrue(joinedFactors.get(0).size()>joinedFactors.get(2).size());
    namesOfFactors.add("A");
    namesOfFactors.add("B");
    namesOfFactors.add("J");
    assertEquals("A",namesOfFactors.get(0));
    assertEquals("B",namesOfFactors.get(1));
    assertEquals("J",namesOfFactors.get(2));
    varElimination.sort(joinedFactors,alarmNet,namesOfFactors);
    assertEquals("B",namesOfFactors.get(0));
    assertEquals("J",namesOfFactors.get(1));
    assertEquals("A",namesOfFactors.get(2));
    assertTrue(joinedFactors.get(0).size()<joinedFactors.get(1).size());
    assertTrue(joinedFactors.get(1).size()<joinedFactors.get(2).size());
    assertTrue(joinedFactors.get(0).size()<joinedFactors.get(2).size());
    namesOfFactors.clear();
    joinedFactors.clear();
    }
    @Test
    void join() {
    net alarmNet;
    alarmNet = xmlRead.makeNet("data/nets/alarm_net.xml");
    assertNotNull(alarmNet);
    // insert the factor for node M
    alarmNet.getByString("M").factor.clear();
    alarmNet.getByString("M").factor.add(new LinkedHashMap<>());
    alarmNet.getByString("M").factor.get(0).put("M","T");
    alarmNet.getByString("M").factor.get(0).put("A","T");
    alarmNet.getByString("M").factor.get(0).put("P","0.7");
    alarmNet.getByString("M").factor.add(new LinkedHashMap<>());
    alarmNet.getByString("M").factor.get(1).put("M","T");
    alarmNet.getByString("M").factor.get(1).put("A","F");
    alarmNet.getByString("M").factor.get(1).put("P","0.01");

    // insert the factor for node J
    alarmNet.getByString("J").factor.clear();
    alarmNet.getByString("J").factor.add(new LinkedHashMap<>());
    alarmNet.getByString("J").factor.get(0).put("J","T");
    alarmNet.getByString("J").factor.get(0).put("A","T");
    alarmNet.getByString("J").factor.get(0).put("P","0.9");
    alarmNet.getByString("J").factor.add(new LinkedHashMap<>());
    alarmNet.getByString("J").factor.get(1).put("J","T");
    alarmNet.getByString("J").factor.get(1).put("A","F");
    alarmNet.getByString("J").factor.get(1).put("P","0.05");

    // check the output
    ArrayList<HashMap<String,String>> factor = varElimination.join(alarmNet.getByString("J"),
            alarmNet.getByString("M"),alarmNet);
    assertEquals(8,factor.size());
    assertEquals("T",factor.get(0).get("M"));
    assertEquals("T",factor.get(0).get("J"));
    assertEquals("T",factor.get(0).get("A"));
    assertEquals("F",factor.get(1).get("M"));
    assertEquals("T",factor.get(1).get("J"));
    assertEquals("T",factor.get(1).get("A"));
    assertEquals("T",factor.get(2).get("M"));
    assertEquals("T",factor.get(2).get("J"));
    assertEquals("F",factor.get(2).get("A"));
    assertEquals("F",factor.get(3).get("M"));
    assertEquals("T",factor.get(3).get("J"));
    assertEquals("F",factor.get(3).get("A"));
    assertEquals("T",factor.get(4).get("M"));
    assertEquals("F",factor.get(4).get("J"));
    assertEquals("T",factor.get(4).get("A"));
    assertEquals("F",factor.get(5).get("M"));
    assertEquals("F",factor.get(5).get("J"));
    assertEquals("T",factor.get(5).get("A"));
    assertEquals("T",factor.get(6).get("M"));
    assertEquals("F",factor.get(6).get("J"));
    assertEquals("F",factor.get(6).get("A"));
    assertEquals("F",factor.get(7).get("M"));
    assertEquals("F",factor.get(7).get("J"));
    assertEquals("F",factor.get(7).get("A"));
    }
}