import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class bayesBallTest {

    @Test
    void bayesBallAns() {
        net alarmNet ;
        alarmNet = xmlRead.makeNet("data/alarm_net.xml");
        assertNotNull(alarmNet);
        assertEquals("yes",bayesBall.bayesBallAns(alarmNet,"B-E|"));
        assertEquals("no",bayesBall.bayesBallAns(alarmNet,"B-A|M=T"));
        assertEquals("no",bayesBall.bayesBallAns(alarmNet,"B-E|A=T"));
        assertEquals("no",bayesBall.bayesBallAns(alarmNet,"B-E|J=T"));
        assertEquals("no",bayesBall.bayesBallAns(alarmNet,"M-J|"));
        assertEquals("yes",bayesBall.bayesBallAns(alarmNet,"M-J|A=F"));
        assertEquals("no",bayesBall.bayesBallAns(alarmNet,"J-B|"));
        assertEquals("yes",bayesBall.bayesBallAns(alarmNet,"J-E|A=T"));
    }
}