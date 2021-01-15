package music_codec_lib;

import music_codec_lib.Exceptions.CNC_Exception;
import music_codec_lib.Exceptions.CPC_Exception;
import music_codec_lib.Exceptions.OPPC_Exception;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CPCTest {

    @Test
    void init() throws CPC_Exception {
        // - number in int
        // - number in String format
        // - String note
        assertNotNull(CPC.init(1));
        assertNotNull(CPC.init("1"));
        assertNotNull(CPC.init("Do"));

        // One parameter
        assertEquals(5, CPC.init("Mi#"));
        assertEquals(11, CPC.init("Dob1"));
        assertEquals(3, CPC.init("Dox#"));
        assertEquals(4, CPC.init(4));
        assertEquals(16, CPC.init("16"));
        assertEquals(12, CPC.init("Si#"));

        // Two parameter
        assertEquals(5, CPC.init("Mi#", 0));
        assertEquals(47, CPC.init("Dob", 4));
        assertEquals(36, CPC.init("Si#", 2));



        int error1 = -1;
        String expectedMessage1 = "CPC - Format not supported - " + error1;
        Exception exception1 = assertThrows(CPC_Exception.class, () -> {
            CPC.init(error1);
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));


        String error2 = "Doxxx#";
        String expectedMessage2 = "CPC - Incorrect format - " + error2;
        Exception exception2 = assertThrows(CPC_Exception.class, () -> {
            CPC.init(error2);
        });
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage2.contains(expectedMessage2));


        String error3 = "Dob";
        String expectedMessage3 = "CPC - Under octave 0 - " + error3;
        Exception exception3 = assertThrows(CPC_Exception.class, () -> {
            CPC.init(error3);
        });
        String actualMessage3 = exception3.getMessage();
        assertTrue(actualMessage3.contains(expectedMessage3));
    }

    @Test
    void interval() throws CPC_Exception {
        assertEquals(-11, CPC.interval("Si", "Do"));
        assertEquals(6, CPC.interval(0, "6"));
        assertEquals(-114, CPC.interval(123, 9));
        assertEquals(19, CPC.interval("B#1", "Abb3"));
    }

    @Test
    void transpose() throws CPC_Exception {
        assertEquals(2, CPC.transpose("Do", 2));
        assertEquals(21, CPC.transpose(1,20));
        assertEquals(32, CPC.transpose("G#3",-12));
        assertEquals(50, CPC.transpose("B#2",14));
        assertEquals(37, CPC.transpose("Dob2",14));

        String error = "-4";
        String expectedMessage = "CPC - Format not supported - " + error;
        Exception exception = assertThrows(CPC_Exception.class, () -> {
            CPC.transpose("Do",-4);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getOctave() throws CPC_Exception {
        assertEquals(12, CPC.getOctave("Do12"));
        assertEquals(0, CPC.getOctave(1));
        assertEquals(3, CPC.getOctave("G#3"));
    }

    @Test
    void getPC() throws CPC_Exception {
        assertEquals(0, CPC.getPC("Do12"));
        assertEquals(4, CPC.getPC(124));
        assertEquals(8, CPC.getPC("G#3"));
    }

    @Test
    void convCPCtoCNC() throws CPC_Exception, CNC_Exception {
        assertEquals(84, CPC.convCPCtoCNC("Do12"));
        assertEquals(72, CPC.convCPCtoCNC(124));
        assertEquals(14, CPC.convCPCtoCNC("B#1"));
        assertEquals(6, CPC.convCPCtoCNC("Dob1"));
        assertEquals(27, CPC.convCPCtoCNC("Ax3"));
    }

    @Test
    void convCPCtoOPPC() throws OPPC_Exception, CPC_Exception {
        assertEquals(12.00, CPC.convCPCtoOPPC("Do12"));
        assertEquals(10.04, CPC.convCPCtoOPPC(124));
        assertEquals(2.01, CPC.convCPCtoOPPC("Bx1"));
        assertEquals(0.11, CPC.convCPCtoOPPC("Dob1"));
        assertEquals(3.09, CPC.convCPCtoOPPC("A3"));
    }

    @Test
    void convCPCtoNC() throws CPC_Exception {
        assertEquals(1, CPC.convCPCtoNC("Dox12"));
        assertEquals(2, CPC.convCPCtoNC(124));
        assertEquals(0, CPC.convCPCtoNC("B#2"));
        assertEquals(6, CPC.convCPCtoNC("Dob4"));
    }

    @Test
    void convCPCtoPC() throws CPC_Exception {
        assertEquals(2, CPC.convCPCtoPC("Dox12"));
        assertEquals(4, CPC.convCPCtoPC(124));
        assertEquals(0, CPC.convCPCtoPC("B#2"));
        assertEquals(11, CPC.convCPCtoPC("Dob4"));
    }

    @Test
    void testToString() throws CPC_Exception {
        assertEquals("CPC: 11", CPC.toString(11));
    }
}