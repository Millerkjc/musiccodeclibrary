package music_codec_lib;

import music_codec_lib.Exceptions.CBR_Exception;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CBRTest {

    @Test
    void init() throws CBR_Exception {
        // One parameter

        assertEquals(4052, CBR.init("Mi#4"));
        assertEquals(2110, CBR.init("Dob3"));
        assertEquals(2006, CBR.init("Si#1"));
        assertEquals(16, CBR.init("16"));


        // Two parameter
        assertEquals(52, CBR.init("Mi#", 0));
        assertEquals(3006, CBR.init("Si#", 2));
        assertEquals(0, CBR.init(0, 0));


        int error1 = -1;
        String expectedMessage1 = "CBR - Format not supported - " + error1;
        Exception exception1 = assertThrows(CBR_Exception.class, () -> {
            CBR.init(error1);
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));


        String error2 = "Doxxx#";
        String expectedMessage2 = "CBR - Incorrect format - " + error2;
        Exception exception2 = assertThrows(CBR_Exception.class, () -> {
            CBR.init(error2);
        });
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage2.contains(expectedMessage2));


        String error3 = "Dob";
        String expectedMessage3 = "CBR - Error in pitch or octave - " + error3 + "0";
        Exception exception3 = assertThrows(CBR_Exception.class, () -> {
            CBR.init(error3);
        });
        String actualMessage3 = exception3.getMessage();
        assertTrue(actualMessage3.contains(expectedMessage3));

    }

    @Test
    void interval() throws CBR_Exception {
        assertEquals(-4042, CBR.interval("Mi#4", 10));
        assertEquals(-108, CBR.interval("Dob3", 2002));
        assertEquals(1105, CBR.interval("Si#1", 3111));
        assertEquals(54, CBR.interval("16", 70));
    }

    @Test
    void transpose() throws CBR_Exception {
        assertEquals(4042, CBR.transpose("Mi#4", -10));
        assertEquals(4112, CBR.transpose("Dob3", 2002));
        assertEquals(5110, CBR.transpose("Si#1", 3111));
        assertEquals(3101, CBR.transpose("Dob1", 3111));



        int cbr = 16, trans = -70;
        String expectedMessage1 = "CBR - Incorrect format - " + cbr + ", " + trans;
        Exception exception1 = assertThrows(CBR_Exception.class, () -> {
            CBR.transpose(cbr, trans);
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));
    }

    @Test
    void getBinomPitch() throws CBR_Exception {
        assertEquals("Re", CBR.getBinomPitch("Re"));
        assertEquals("Solxx", CBR.getBinomPitch(114));
        assertEquals("Sibbbb", CBR.getBinomPitch("76"));

        assertEquals("Solxx", CBR.getBinomPitch(3114));
        assertEquals("Sib", CBR.getBinomPitch("Sib123"));
    }

    @Test
    void getBinomInterval() throws CBR_Exception {
        assertEquals("5d1", CBR.getBinomInterval("70"));
        assertEquals("6A5", CBR.getBinomInterval(14));

        assertEquals("P1", CBR.getBinomInterval(3000));
        assertEquals("m7", CBR.getBinomInterval("Sib123"));
    }

    @Test
    void getOctave() throws CBR_Exception {
        assertEquals(3, CBR.getOctave(3000));
        assertEquals(123, CBR.getOctave("Sib123"));
        assertEquals(0, CBR.getOctave("Do"));
    }

    @Test
    void getBR() throws CBR_Exception {
        assertEquals(0, CBR.getBR(3000));
        assertEquals(106, CBR.getBR("Sib123"));
        assertEquals(10, CBR.getBR("Do#"));
    }

    @Test
    void getNC() throws CBR_Exception {
        assertEquals(2, CBR.getNC(3012));
        assertEquals(6, CBR.getNC("Sib123"));
        assertEquals(0, CBR.getNC("Do#"));
    }

    @Test
    void getPC() throws CBR_Exception {
        assertEquals(1, CBR.getPC(3012));
        assertEquals(10, CBR.getPC("Sib123"));
        assertEquals(1, CBR.getPC("Do#"));
    }

    @Test
    void testToString() throws CBR_Exception {
        assertEquals("CBR: 95", CBR.toString(95));
    }
}