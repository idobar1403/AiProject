import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class bayesBallTest {

    @Test
    void bayesBallAns() {
        net alarmNet ;
        alarmNet = xmlRead.makeNet("data/nets/alarm_net.xml");
        assertNotNull(alarmNet);
        assertEquals("yes",bayesBall.bayesBallAns(alarmNet,"B-E|"));
        assertEquals("no",bayesBall.bayesBallAns(alarmNet,"B-A|M=T"));
        assertEquals("no",bayesBall.bayesBallAns(alarmNet,"B-E|A=T"));
        assertEquals("no",bayesBall.bayesBallAns(alarmNet,"B-E|J=T"));
        assertEquals("no",bayesBall.bayesBallAns(alarmNet,"M-J|"));
        assertEquals("yes",bayesBall.bayesBallAns(alarmNet,"M-J|A=F"));
        assertEquals("no",bayesBall.bayesBallAns(alarmNet,"J-B|"));
        assertEquals("yes",bayesBall.bayesBallAns(alarmNet,"J-E|A=T"));
        net bigNet;
        bigNet = xmlRead.makeNet("data/nets/big_net.xml");
        assertNotNull(bigNet);
        assertEquals("yes",bayesBall.bayesBallAns(bigNet,"B0-C2|A2=T,A3=T"));
        assertEquals("no",bayesBall.bayesBallAns(bigNet,"A1-D1|C3=T,B2=F,B3=F"));
    }
}