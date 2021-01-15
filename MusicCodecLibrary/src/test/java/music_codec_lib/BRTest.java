package music_codec_lib;

import music_codec_lib.Exceptions.BR_Exception;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BRTest {

    @Test
    void init() throws BR_Exception {
        // One parameter
        assertEquals(52, BR.init("Mi#"));
        assertEquals(30, BR.init("Dox#"));
        assertEquals(6, BR.init("Si#"));
        assertEquals(16, BR.init("16"));


        // Two parameter
        assertEquals(50, BR.init("Mi#", 0));
        assertEquals(2, BR.init("Si#", 2));
        assertEquals(0, BR.init(0, 0));



        int error1 = -1;
        String expectedMessage1 = "BR - Incorrect format - " + error1;
        Exception exception1 = assertThrows(BR_Exception.class, () -> {
            BR.init(error1);
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));


        String error2 = "Doxxx#";
        String expectedMessage2 = "BR - Incorrect format - " + error2;
        Exception exception2 = assertThrows(BR_Exception.class, () -> {
            BR.init(error2);
        });
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage2.contains(expectedMessage2));


        String pc = "Dob";
        String nc = "G";
        String expectedMessage3 = "Incorrect format: pc = " + pc + ", nc = " + nc;
        Exception exception3 = assertThrows(BR_Exception.class, () -> {
            BR.init(pc, nc);
        });
        String actualMessage3 = exception3.getMessage();
        assertTrue(actualMessage3.contains(expectedMessage3));


        int error4 = 117;
        String expectedMessage4 = "BR - Incorrect format - " + error4;
        Exception exception4 = assertThrows(BR_Exception.class, () -> {
            BR.init(error4);
        });
        String actualMessage4 = exception4.getMessage();
        assertTrue(actualMessage4.contains(expectedMessage4));


        int error5 = 98;
        String expectedMessage5 = "BR - Incorrect format - " + error5;
        Exception exception5 = assertThrows(BR_Exception.class, () -> {
            BR.init(error5);
        });
        String actualMessage5 = exception5.getMessage();
        assertTrue(actualMessage5.contains(expectedMessage5));
    }

    @Test
    void interval() throws BR_Exception {
        assertEquals(75, BR.interval("Mi#", 0));
        assertEquals(3, BR.interval("Si#", 2));
        assertEquals(0, BR.interval(0, 0));
        assertEquals(105, BR.interval(110, 95));
    }

    @Test
    void intervalInversion() throws BR_Exception {
        // One parameter
        assertEquals(52, BR.intervalInversion(75));
        assertEquals(4, BR.intervalInversion(3));
        assertEquals(0, BR.intervalInversion(0));
        assertEquals(22, BR.intervalInversion(105));

        // Two parameters
        assertEquals(52, BR.intervalInversion("Mi#", 0));
        assertEquals(4, BR.intervalInversion("Si#", 2));
        assertEquals(0, BR.intervalInversion(0, 0));
        assertEquals(22, BR.intervalInversion(110, 95));
    }

    @Test
    void transpose() throws BR_Exception {
        // Two parameters
        assertEquals(64, BR.transpose("Mi#", 12));
        assertEquals(116, BR.transpose("Si#", 110));
        assertEquals(30, BR.transpose(0, 30));
        assertEquals(85, BR.transpose(110, 95));
        assertEquals(100, BR.transpose(110, -10));


        // Three parameters
        assertEquals(50, BR.transpose("Mi#", 0, 12));
        assertEquals(94, BR.transpose("Si#", 21, 5));
        assertEquals(32, BR.transpose(0, 3, 2));
        assertEquals(101, BR.transpose(110, 95,1));



        int br_pitch = 110, trans = -11;
        String expectedMessage1 = "BR - Incorrect NC format: br = " + br_pitch + ", transposition = " + trans;
        Exception exception1 = assertThrows(BR_Exception.class, () -> {
            BR.transpose(br_pitch, trans);
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));
    }

    @Test
    void getBinomPitch() throws BR_Exception {
        assertEquals("Re", BR.getBinomPitch("Re"));
        assertEquals("Solxx", BR.getBinomPitch(114));
        assertEquals("Sibbbb", BR.getBinomPitch("76"));
    }

    @Test
    void getBinomInterval() throws BR_Exception {
        assertEquals("5d1", BR.getBinomInterval(70));
        assertEquals("6A5", BR.getBinomInterval(14));
    }

    @Test
    void getNC() throws BR_Exception {
        assertEquals(1, BR.getNC("Re"));
        assertEquals(4, BR.getNC(114));
        assertEquals(6, BR.getNC("76"));
        assertEquals(0, BR.getNC(70));
        assertEquals(5, BR.getNC(15));
    }

    @Test
    void getPC() throws BR_Exception {
        assertEquals(2, BR.getPC("Re"));
        assertEquals(11, BR.getPC(114));
        assertEquals(7, BR.getPC("76"));
        assertEquals(10, BR.getPC(100));
        assertEquals(1, BR.getPC(15));
    }

    @Test
    void convBRtoBinomial() throws BR_Exception {
        assertArrayEquals(new Integer[]{2,1}, BR.convBRtoBinomial(21));
        assertArrayEquals(new Integer[]{11,5}, BR.convBRtoBinomial(115));
    }

    @Test
    void testToString() throws BR_Exception {
        assertEquals("BR: 95", BR.toString(95));
    }
}