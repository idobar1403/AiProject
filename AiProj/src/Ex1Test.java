import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Ex1Test {

    private net bNet;

    @Test
    void readFile() {
        bNet = xmlRead.makeNet("data/nets/alarm_net.xml");
        assertNotNull(bNet);

        // check that the nodes exists
        assertNotNull(bNet.getByString("A"));
        assertNotNull(bNet.getByString("E"));
        assertNotNull(bNet.getByString("B"));
        assertNotNull(bNet.getByString("J"));
        assertNotNull(bNet.getByString("M"));

        // check correct insertion
        assertTrue(bNet.getByString("A").parents.contains(bNet.getByString("E")));
        assertTrue(bNet.getByString("A").parents.contains(bNet.getByString("B")));
        assertTrue(bNet.getByString("E").childs.contains(bNet.getByString("A")));
        assertTrue(bNet.getByString("B").childs.contains(bNet.getByString("A")));
        assertFalse(bNet.getByString("B").parents.contains(bNet.getByString("A")));

        // check children and parents size
        assertEquals(2, bNet.getByString("A").childs.size());
        assertEquals(2, bNet.getByString("A").outcomes.size());

        // check the factor values
        assertEquals("0.002", bNet.getByString("E").factor.get(0).get("P"));
        assertEquals("0.998", bNet.getByString("E").factor.get(1).get("P"));
        assertEquals("0.001", bNet.getByString("B").factor.get(0).get("P"));
        assertEquals("0.999", bNet.getByString("B").factor.get(1).get("P"));
        assertEquals("0.95", bNet.getByString("A").factor.get(0).get("P"));
        assertEquals("T", bNet.getByString("A").factor.get(0).get("A"));
        assertEquals("T", bNet.getByString("A").factor.get(0).get("E"));
        assertEquals("T", bNet.getByString("A").factor.get(0).get("B"));

        // check factors size
        assertEquals(8, bNet.getByString("A").factor.size());
        assertEquals(4, bNet.getByString("M").factor.size());
        assertEquals(4, bNet.getByString("J").factor.size());
    }
}