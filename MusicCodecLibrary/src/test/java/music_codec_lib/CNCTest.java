package music_codec_lib;

import music_codec_lib.Exceptions.CNC_Exception;
import music_codec_lib.Exceptions.CPC_Exception;
import music_codec_lib.Exceptions.OPPC_Exception;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CNCTest {

    @Test
    void init() throws CNC_Exception {
        // - number in int
        // - number in String format
        // - String note
        assertNotNull(CNC.init(1));
        assertNotNull(CNC.init("1"));
        assertNotNull(CNC.init("Do"));

        // One parameter
        assertEquals(3, CNC.init("Mi#"));
        assertEquals(6, CNC.init("Dob1"));
        assertEquals(1, CNC.init("Dox#"));
        assertEquals(7, CNC.init("Si#"));
        assertEquals(4, CNC.init(4));
        assertEquals(16, CNC.init("16"));



        // Two parameter
        assertEquals(3, CNC.init("Mi#", 0));
        assertEquals(27, CNC.init("Dob", 4));
        assertEquals(21, CNC.init("Si#", 2));


        int error1 = -1;
        String expectedMessage1 = "CNC - Format not supported - " + error1;
        Exception exception1 = assertThrows(CNC_Exception.class, () -> {
            CNC.init(error1);
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));


        String error2 = "Doxxx#";
        String expectedMessage2 = "CNC - Incorrect format - " + error2;
        Exception exception2 = assertThrows(CNC_Exception.class, () -> {
            CNC.init(error2);
        });
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage2.contains(expectedMessage2));


        String error3 = "Dob";
        String expectedMessage3 = "CNC - Error in pitch or octave - " + error3;
        Exception exception3 = assertThrows(CNC_Exception.class, () -> {
            CNC.init(error3);
        });
        String actualMessage3 = exception3.getMessage();
        assertTrue(actualMessage3.contains(expectedMessage3));

    }

    @Test
    void interval() throws CNC_Exception {
        assertEquals(-6, CNC.interval("Si", "Do"));
        assertEquals(6, CNC.interval(0, "6"));
        assertEquals(-114, CNC.interval(123, 9));
        assertEquals(11, CNC.interval("B#1", "Abb3"));
    }

    @Test
    void transpose() throws CNC_Exception {
        assertEquals(2, CNC.transpose("Do", 2));
        assertEquals(21, CNC.transpose(1,20));
        assertEquals(13, CNC.transpose("G#3",-12));
        assertEquals(8, CNC.transpose("Dob1", 2));
        assertEquals(16, CNC.transpose("B#1", 2));


        String error = "-4";
        String expectedMessage = "CNC - Format not supported - " + error;
        Exception exception = assertThrows(CNC_Exception.class, () -> {
            CNC.transpose("Do",-4);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getOctave() throws CNC_Exception {
        assertEquals(12, CNC.getOctave("Do12"));
        assertEquals(0, CNC.getOctave(1));
        assertEquals(3, CNC.getOctave("G#3"));
    }

    @Test
    void getNC() throws CNC_Exception {
        assertEquals(0, CNC.getNC("Do12"));
        assertEquals(5, CNC.getNC(124));
        assertEquals(4, CNC.getNC("G#3"));
    }

    @Test
    void convCNCtoCPC() throws CNC_Exception, CPC_Exception {
        assertEquals(144, CNC.convCNCtoCPC("Do12"));
        assertEquals(213, CNC.convCNCtoCPC(124));
        assertEquals(24, CNC.convCNCtoCPC("B#1"));
        assertEquals(11, CNC.convCNCtoCPC("Dob1"));
        assertEquals(47, CNC.convCNCtoCPC("Ax3"));
    }

    @Test
    void convCNCtoOPPC() throws CNC_Exception, OPPC_Exception {
        assertEquals(12.00, CNC.convCNCtoOPPC("Do12"));
        assertEquals(17.09, CNC.convCNCtoOPPC(124));
        assertEquals(2.00, CNC.convCNCtoOPPC("Bx1"));
        assertEquals(0.11, CNC.convCNCtoOPPC("Dob1"));
        assertEquals(3.09, CNC.convCNCtoOPPC("A3"));
    }

    @Test
    void convCNCtoNC() throws CNC_Exception {
        assertEquals(0, CNC.convCNCtoNC("Do12"));
        assertEquals(5, CNC.convCNCtoNC(124));
        assertEquals(4, CNC.convCNCtoNC("G#3"));
        assertEquals(0, CNC.convCNCtoNC("B#2"));
        assertEquals(6, CNC.convCNCtoNC("Dob4"));
    }

    @Test
    void convCNCtoPC() throws CNC_Exception {
        assertEquals(0, CNC.convCNCtoPC("Do12"));
        assertEquals(9, CNC.convCNCtoPC(124));
        assertEquals(7, CNC.convCNCtoPC("G#3"));
    }


    @Test
    void testToString() throws CNC_Exception {
        assertEquals("CNC: 2", CNC.toString(2));
    }
}