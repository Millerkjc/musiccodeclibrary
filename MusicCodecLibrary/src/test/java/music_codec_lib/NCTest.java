package music_codec_lib;

import music_codec_lib.Exceptions.NC_Exception;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NCTest {

    @Test
    void init() throws NC_Exception {
        // - number in int
        // - number in String format
        // - String note
        assertNotNull(NC.init(1));
        assertNotNull(NC.init("1"));
        assertNotNull(NC.init("Do"));

        // Semitone and all note with other alteration
        assertEquals(3, NC.init("Mi#") );
        assertEquals(6, NC.init("Dob1") );
        assertEquals(1, NC.init("Dox#"));
        assertEquals(4, NC.init(4));
        assertEquals(6, NC.init("6"));

        // take only the pitch and not the octave information
        assertEquals(1, NC.init("Dox#2"));



        int error1 = -1;
        String expectedMessage1 = "NC - Format not supported - " + error1;
        Exception exception1 = assertThrows(NC_Exception.class, () -> {
            NC.init(error1);
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));



        String error2 = "Doxxx#";
        String expectedMessage2 = "NC - Incorrect format - " + error2;
        Exception exception2 = assertThrows(NC_Exception.class, () -> {
            NC.init(error2);
        });
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage2.contains(expectedMessage2));


        String error3 = "Dob";
        String expectedMessage3 = "NC - Incorrect format - " + error3;
        Exception exception3 = assertThrows(NC_Exception.class, () -> {
            NC.init(error3);
        });
        String actualMessage3 = exception3.getMessage();
        assertTrue(actualMessage3.contains(expectedMessage3));
    }

    @Test
    void interval() throws NC_Exception {
        assertEquals(1, NC.interval("Si", "Do"));
        assertEquals(6, NC.interval(0, "6"));



        String pitch = "11";
        String expectedMessage = "NC - Incorrect format - " + pitch;
        Exception exception = assertThrows(NC_Exception.class, () -> {
            NC.interval(0, pitch);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void intervalInversion() throws NC_Exception {
        // One parameter: NCI
        assertEquals(1, NC.intervalInversion("6"));
        assertEquals(0, NC.intervalInversion(0));
        assertEquals(2, NC.intervalInversion("La"));

        // Two parameters: NC_a and NC_b
        assertEquals(6, NC.intervalInversion("Si", "Do"));
        assertEquals(4, NC.intervalInversion("Fab", "La"));



        String pitch = "r#3";
        String expectedMessage = "NC - Incorrect format - " + pitch;

        // One parameter
        Exception exception = assertThrows(NC_Exception.class, () -> {
            NC.intervalInversion(pitch);
        });
        String actualMessage1 = exception.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage));

        // Two parameters
        Exception exception2 = assertThrows(NC_Exception.class, () -> {
            NC.intervalInversion(0, pitch);
        });
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage2.contains(expectedMessage));
    }

    @Test
    void IC() throws NC_Exception {
        assertEquals(3, NC.IC(2,"6"));
        assertEquals(2, NC.IC(1,6));
        assertEquals(1, NC.IC("Si","La"));
    }

    @Test
    void transpose() throws NC_Exception {
        assertEquals(2, NC.transpose("Do", 2));
        assertEquals(0, NC.transpose(1,20));


        String nc0 = "Do";
        int nc1 = -4;
        String expectedMessage1 = "NC - Format not supported - " + nc1;
        Exception exception1 = assertThrows(NC_Exception.class, () -> {
            NC.transpose(nc0, nc1);
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));
    }

    @Test
    void getCIName() throws NC_Exception {
        assertEquals("Sesta", NC.getCIName(1,"6"));
        assertEquals("Quarta", NC.getCIName(6,2));
        assertEquals("Unisono | Ottava", NC.getCIName("Do",0));
    }

    @Test
    void getCIinverseName() throws NC_Exception {
        assertEquals("Terza", NC.getCIinverseName(1,"6"));
        assertEquals("Quinta", NC.getCIinverseName(6,2));
        assertEquals("Unisono | Ottava", NC.getCIinverseName("Do",0));
    }

    @Test
    void getNC() throws NC_Exception {
        assertEquals(6, NC.getNC(6));
        assertEquals(5, NC.getNC("Sib12"));


        int pitch = 24;
        String expectedMessage = "NC - Incorrect format - " + pitch;
        Exception exception = assertThrows(NC_Exception.class, () -> {
            NC.getNC(pitch);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void convNCtoPC() throws NC_Exception {
        assertEquals(2, NC.convNCtoPC(1));
        assertEquals(9, NC.convNCtoPC("bb8"));
        assertEquals(0, NC.convNCtoPC("Laxx12"));
    }

    @Test
    void testToString() throws NC_Exception {
        assertEquals("NC: 2", NC.toString(2));
    }
}