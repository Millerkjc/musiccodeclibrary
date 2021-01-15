package music_codec_lib;

import music_codec_lib.Exceptions.NC_Exception;
import music_codec_lib.Exceptions.PC_Exception;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PCTest {

    @Test
    void init() throws PC_Exception {
        // - number in int
        // - number in String format
        // - String note
        assertNotNull(PC.init(1));
        assertNotNull(PC.init("1"));
        assertNotNull(PC.init("Do"));

        // Semitone and all note with other alteration
        assertEquals(3, PC.init("Mib") );
        assertEquals(3, PC.init("Re#") );
        assertEquals(3, PC.init("Dox#"));
        assertEquals(4, PC.init(4));
        assertEquals(11, PC.init("11"));

        assertEquals(0, PC.init("Si#"));

        // take only the pitch and not the octave information
        assertEquals(3, PC.init("Dox#2"));
        assertEquals(0, PC.init("Si#2"));
        assertEquals(11, PC.init("Cb3"));



        int error1 = -1;
        String expectedMessage1 = "PC - Format not supported - " + error1;
        Exception exception1 = assertThrows(PC_Exception.class, () -> {
            PC.init(error1);
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));



        String error2 = "Doxxx#";
        String expectedMessage2 = "PC - Incorrect format - " + error2;
        Exception exception2 = assertThrows(PC_Exception.class, () -> {
            PC.init(error2);
        });
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage2.contains(expectedMessage2));


        String error3 = "Dob";
        String expectedMessage3 = "PC - Incorrect format - " + error3;
        Exception exception3 = assertThrows(PC_Exception.class, () -> {
            PC.init(error3);
        });
        String actualMessage3 = exception3.getMessage();
        assertTrue(actualMessage3.contains(expectedMessage3));
    }

    @Test
    void interval() throws PC_Exception {
        assertEquals(1, PC.interval("Si", "Do"));
        assertEquals(11, PC.interval(0, "11"));



        String pitch = "r#3";
        String expectedMessage = "PC - Incorrect format - " + pitch;
        Exception exception = assertThrows(PC_Exception.class, () -> {
            PC.interval(0, pitch);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void intervalInversion() throws PC_Exception {
        // One parameter: PCI
        assertEquals(5, PC.intervalInversion("7"));
        assertEquals(6, PC.intervalInversion(6));
        assertEquals(3, PC.intervalInversion("La"));

        // Two parameters: PC_a and PC_b
        assertEquals(11, PC.intervalInversion("Si", "Do"));
        assertEquals(1, PC.intervalInversion(0, "11"));



        String pitch = "r#3";
        String expectedMessage = "PC - Incorrect format - " + pitch;

        // One parameter
        Exception exception = assertThrows(PC_Exception.class, () -> {
            PC.intervalInversion(pitch);
        });
        String actualMessage1 = exception.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage));

        // Two parameters
        Exception exception2 = assertThrows(PC_Exception.class, () -> {
            PC.intervalInversion(0, pitch);
        });
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage2.contains(expectedMessage));
    }


    @Test
    void IC() throws PC_Exception {
        assertEquals(5, PC.IC(2,"7"));
        assertEquals(5, PC.IC(11,6));
        assertEquals(3, PC.IC("Do","La"));
    }

    @Test
    void transpose() throws PC_Exception {
        assertEquals(2, PC.transpose("Do", 2));
        assertEquals(5, PC.transpose(11,6));
        //assertEquals(8, PC.transpose("Do",-4));

        String pc0 = "Do";
        int pc1 = -4;
        String expectedMessage1 = "PC - Format not supported - " + pc1;
        Exception exception1 = assertThrows(PC_Exception.class, () -> {
            PC.transpose(pc0, pc1);
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));
    }

    @Test
    void getCIName() throws PC_Exception {
        assertEquals("Quarta giusta", PC.getCIName(2,"7"));
        assertEquals("Sesta minore", PC.getCIName(11,7));
        assertEquals("Unisono giusto | Ottava giusta", PC.getCIName("Do",0));
    }

    @Test
    void getCIinverseName() throws PC_Exception {
        assertEquals("Quinta giusta", PC.getCIinverseName(2,"7"));
        assertEquals("Terza maggiore", PC.getCIinverseName(11,7));
        assertEquals("Unisono giusto | Ottava giusta", PC.getCIinverseName("Do",0));
    }

    @Test
    void getPC() throws PC_Exception {
        assertEquals(11, PC.getPC(11));
        assertEquals(10, PC.getPC("Sib12"));



        int pitch = 24;
        String expectedMessage = "PC - Incorrect format - " + pitch;
        Exception exception = assertThrows(PC_Exception.class, () -> {
            PC.getPC(pitch);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void convPCtoNC() throws PC_Exception {
        assertEquals(0, PC.convPCtoNC(1));
        assertEquals(5, PC.convPCtoNC("bb8"));
        assertEquals(1, PC.convPCtoNC("Laxxx12"));
    }

    @Test
    void testToString() throws PC_Exception {
        assertEquals("PC: 2", PC.toString(2));
    }
}