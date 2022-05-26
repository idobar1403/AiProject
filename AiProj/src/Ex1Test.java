import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Ex1Test {

    @Test
    void readFile() {
        net bNet;
        bNet = xmlRead.makeNet("data/alarm_net.xml");
        assertNotNull(bNet);
        assertNotNull(bNet.getByString("A"));
        assertNotNull(bNet.getByString("E"));
        assertNotNull(bNet.getByString("B"));
        assertNotNull(bNet.getByString("J"));
        assertNotNull(bNet.getByString("M"));
        assertTrue(bNet.getByString("A").parents.contains(bNet.getByString("E")));
        assertTrue(bNet.getByString("A").parents.contains(bNet.getByString("B")));
        assertTrue(bNet.getByString("E").childs.contains(bNet.getByString("A")));
        assertTrue(bNet.getByString("B").childs.contains(bNet.getByString("A")));
        assertFalse(bNet.getByString("B").parents.contains(bNet.getByString("A")));
        assertEquals(2, bNet.getByString("A").childs.size());
        assertEquals(2, bNet.getByString("A").outcomes.size());
        assertEquals("0.002",bNet.getByString("E").factor.get(0).get("P"));
        assertEquals("0.998",bNet.getByString("E").factor.get(1).get("P"));
        assertEquals("0.001",bNet.getByString("B").factor.get(0).get("P"));
        assertEquals("0.999",bNet.getByString("B").factor.get(1).get("P"));
        assertEquals("0.95", bNet.getByString("A").factor.get(0).get("P"));
        assertEquals("T", bNet.getByString("A").factor.get(0).get("A"));
        assertEquals("T", bNet.getByString("A").factor.get(0).get("E"));
        assertEquals("T", bNet.getByString("A").factor.get(0).get("B"));
    }
}