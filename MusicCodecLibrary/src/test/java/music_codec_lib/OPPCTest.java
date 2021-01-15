package music_codec_lib;

import music_codec_lib.Exceptions.CNC_Exception;
import music_codec_lib.Exceptions.CPC_Exception;
import music_codec_lib.Exceptions.OPPC_Exception;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OPPCTest {

    @Test
    void init() throws OPPC_Exception {
        // - number in int
        // - number in String format
        // - String note
        assertNotNull(OPPC.init(1));
        assertNotNull(OPPC.init("1"));
        assertNotNull(OPPC.init("1.01"));

        // One parameter
        assertEquals(1.01, OPPC.init("1.01"));
        assertEquals(8.00, OPPC.init("8"));
        assertEquals(4.00, OPPC.init(4));
        assertEquals(4.10, OPPC.init(4.10));
        assertEquals(0.05, OPPC.init("Mi#"));
        assertEquals(3.11, OPPC.init("Dob4"));

        // Two parameters
        assertEquals(0.05, OPPC.init("Mi#", 0));
        assertEquals(3.11, OPPC.init("Dob", 4));
        assertEquals(3.00, OPPC.init("Si#", 2));



        int error1 = -1;
        String expectedMessage1 = "OPPC - Under octave 0 - " + error1;
        Exception exception1 = assertThrows(OPPC_Exception.class, () -> {
            OPPC.init(error1);
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));



        String error2 = "Doxxx#";
        String expectedMessage2 = "OPPC - Incorrect format - " + error2;
        Exception exception2 = assertThrows(OPPC_Exception.class, () -> {
            OPPC.init(error2);
        });
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage2.contains(expectedMessage2));



        String error3 = "Dob";
        int error3_oct = 0;
        String expectedMessage3 = "OPPC - Pitch invalidates octave - -1";
        Exception exception3 = assertThrows(OPPC_Exception.class, () -> {
            OPPC.init(error3, error3_oct);
        });
        String actualMessage3 = exception3.getMessage();
        assertTrue(actualMessage3.contains(expectedMessage3));

    }

    @Test
    void getOctave() throws OPPC_Exception {
        assertEquals(7, OPPC.getOctave(7));
        assertEquals(4, OPPC.getOctave(4.10));
        assertEquals(0, OPPC.getOctave("Mi#"));
    }

    @Test
    void getPC() throws OPPC_Exception {
        assertEquals(0, OPPC.getPC(7));
        assertEquals(10, OPPC.getPC(4.10));
        assertEquals(5, OPPC.getPC("Mi#"));
    }

    @Test
    void convOPPCtoCNC() throws OPPC_Exception, CNC_Exception {
        assertEquals(85, OPPC.convOPPCtoCNC("Dox12"));
        assertEquals(868, OPPC.convOPPCtoCNC(124));
        assertEquals(23, OPPC.convOPPCtoCNC("Bxx#2"));
        assertEquals(27, OPPC.convOPPCtoCNC("Dob4"));

        assertEquals(13, OPPC.convOPPCtoCNC(1.11));
        assertEquals(46, OPPC.convOPPCtoCNC(6.07));
    }

    @Test
    void convOPPCtoCPC() throws OPPC_Exception, CPC_Exception {
        assertEquals(146, OPPC.convOPPCtoCPC("Dox12"));
        assertEquals(1488, OPPC.convOPPCtoCPC(124));
        assertEquals(40, OPPC.convOPPCtoCPC("Bxx#2"));
        assertEquals(47, OPPC.convOPPCtoCPC("Dob4"));

        assertEquals(23, OPPC.convOPPCtoCPC(1.11));
        assertEquals(79, OPPC.convOPPCtoCPC(6.07));
    }

    @Test
    void convOPPCtoNC() throws OPPC_Exception{
        assertEquals(1, OPPC.convOPPCtoNC("Dox12"));
        assertEquals(0, OPPC.convOPPCtoNC(124));
        assertEquals(2, OPPC.convOPPCtoNC("Bxx#2"));
        assertEquals(6, OPPC.convOPPCtoNC("Dob4"));

        assertEquals(6, OPPC.convOPPCtoNC(1.11));
        assertEquals(4, OPPC.convOPPCtoNC(6.07));
    }

    @Test
    void convOPPCtoPC() throws OPPC_Exception {
        assertEquals(2, OPPC.convOPPCtoPC("Dox12"));
        assertEquals(0, OPPC.convOPPCtoPC(124));
        assertEquals(3, OPPC.convOPPCtoPC("Bxx2"));
        assertEquals(11, OPPC.convOPPCtoPC("Dob4"));

        assertEquals(10, OPPC.convOPPCtoPC(1.10));
        assertEquals(7, OPPC.convOPPCtoPC(6.07));
    }

    @Test
    void testToString() throws OPPC_Exception {
        assertEquals("OPPC: 1.02", OPPC.toString(1.02));
        assertEquals("OPPC: 1.02", OPPC.toString("Re1"));
    }
}