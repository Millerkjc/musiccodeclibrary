package music_codec_lib;

import music_codec_lib.Exceptions.Binomial_Exception;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BinomialTest {

    private Integer[] returnValue(int pc, int nc){
        return new Integer[]{pc, nc};
    }

    @Test
    void init() throws Binomial_Exception {
        // One parameter
        assertArrayEquals(returnValue(5, 2), Binomial.init("Mi#"));
        assertArrayEquals(returnValue(11, 0), Binomial.init("Dob"));
        assertArrayEquals(returnValue(3, 0), Binomial.init("Dox#"));
        assertArrayEquals(returnValue(0, 6), Binomial.init("Si#"));
        assertArrayEquals(returnValue(4, 0), Binomial.init(returnValue(4, 0)));
        assertArrayEquals(returnValue(0, 0), Binomial.init(returnValue(0, 0)));



        // Two parameter
        assertArrayEquals(returnValue(4, 0), Binomial.init("Mi", "Do"));
        assertArrayEquals(returnValue(0, 0), Binomial.init("Si#", "Bx"));


        // With negative arguments
        assertArrayEquals(returnValue(-1, -4), Binomial.init(-1, -4));
        assertArrayEquals(returnValue(-5, -2), Binomial.init("-Mi#"));


        int error1 = 4;
        String expectedMessage1 = "Binomial - Incorrect format - " + error1;
        Exception exception1 = assertThrows(Binomial_Exception.class, () -> {
            Binomial.init(error1);
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));


        String error2 = "Dob1";
        String expectedMessage2 = "Binomial - Incorrect String format - " + error2;
        Exception exception2 = assertThrows(Binomial_Exception.class, () -> {
            Binomial.init(error2);
        });
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage2.contains(expectedMessage2));


        String error3 = "Dob";
        String expectedMessage3 = "Binomial - NC incorrect format - " + error3;
        Exception exception3 = assertThrows(Binomial_Exception.class, () -> {
            Binomial.init(4, error3);
        });
        String actualMessage3 = exception3.getMessage();
        assertTrue(actualMessage3.contains(expectedMessage3));
    }

    @Test
    void interval() throws Binomial_Exception {
        assertArrayEquals(returnValue(6, 5), Binomial.interval(returnValue(5, 2), returnValue(11, 0)));
        assertArrayEquals(returnValue(6, 2), Binomial.interval(returnValue(11, 0), returnValue(5, 2)));


        int pc0 = 11, pc1 = -1;
        int nc0 = 0, nc1 = -1;
        String expectedMessage4 = "Binomial - Interval calc error, pc0 = " + pc0 + ", nc0 = " + nc0 + "; pc1 = " + pc1 + ", nc1 = " + nc1;
        Exception exception4 = assertThrows(Binomial_Exception.class, () -> {
            Binomial.interval(returnValue(pc0, nc0), returnValue(pc1, nc1));
        });
        String actualMessage4 = exception4.getMessage();
        assertTrue(actualMessage4.contains(expectedMessage4));
    }

    @Test
    void intervalInversion() throws Binomial_Exception {
        // One arguments
        assertArrayEquals(returnValue(11, 5), Binomial.intervalInversion(returnValue(1, 2)));
        assertArrayEquals(returnValue(3, 2), Binomial.intervalInversion(returnValue(9, 5)));

        // Two arguments
        assertArrayEquals(returnValue(2, 2), Binomial.intervalInversion(returnValue(1, 2), returnValue(11, 0)));
        assertArrayEquals(returnValue(6, 5), Binomial.intervalInversion(returnValue(11, 0), returnValue(5, 2)));
    }


    @Test
    void transpose() throws Binomial_Exception {
        // Two parameters
        assertArrayEquals(returnValue(0, 2), Binomial.transpose(returnValue(1, 2), returnValue(11, 0)));
        assertArrayEquals(returnValue(6, 1), Binomial.transpose(returnValue(5, 5), returnValue(1, -4)));

        // Three parameters
        assertArrayEquals(returnValue(0, 2), Binomial.transpose(returnValue(1, 2), 11, 0));
        assertArrayEquals(returnValue(6, 1), Binomial.transpose(returnValue(5, 5), 1, -4));



        int pc0 = 5, pc1 = -1;
        int nc0 = 2, nc1 = -4;
        String expectedMessage1 = "Binomial - PC or NC incorrect format, binom = pc: " + pc0 + ", nc: " + nc0 + ", transposition = pc: " + pc1 + ", nc: " + nc1;
        Exception exception1 = assertThrows(Binomial_Exception.class, () -> {
            Binomial.transpose(returnValue(pc0, nc0), returnValue(pc1, nc1));
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));


        Exception exception2 = assertThrows(Binomial_Exception.class, () -> {
            Binomial.transpose(returnValue(pc0, nc0), pc1, nc1);
        });
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage2.contains(expectedMessage1));
    }

    @Test
    void getBinomPitch() throws Binomial_Exception {
        // One arguments
        assertEquals("Re", Binomial.getBinomPitch("Re"));
        assertEquals("Re", Binomial.getBinomPitch(returnValue(2, 1)));
        assertEquals("Lax", Binomial.getBinomPitch(returnValue(11, 5)));

        // Two arguments
        assertEquals("Dobbbbb", Binomial.getBinomPitch(7, 0));
        assertEquals("Solxxx", Binomial.getBinomPitch(1, 4));


        int pc = 5, nc = -1;
        String expectedMessage1 = "Binomial - Incorrect pitch - <" + pc + ", " + nc + ">";
        Exception exception1 = assertThrows(Binomial_Exception.class, () -> {
            Binomial.getBinomPitch(returnValue(pc, nc));
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));


        Exception exception2 = assertThrows(Binomial_Exception.class, () -> {
            Binomial.getBinomPitch(pc, nc);
        });
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage2.contains(expectedMessage1));
    }

    @Test
    void getBinomInterval() throws Binomial_Exception {
        assertEquals("M2", Binomial.getBinomInterval("Re"));
        assertEquals("M2", Binomial.getBinomInterval(returnValue(2, 1)));
        assertEquals("2A6", Binomial.getBinomInterval(returnValue(11, 5)));

        // Two arguments
        assertEquals("5d1", Binomial.getBinomInterval(7, 0));
        assertEquals("6A5", Binomial.getBinomInterval(1, 4));


        int pc = 5, nc = -1;
        String expectedMessage1 = "Binomial - Incorrect interval - <" + pc + ", " + nc + ">";
        Exception exception1 = assertThrows(Binomial_Exception.class, () -> {
            Binomial.getBinomInterval(returnValue(pc, nc));
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));


        Exception exception2 = assertThrows(Binomial_Exception.class, () -> {
            Binomial.getBinomInterval(pc, nc);
        });
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage2.contains(expectedMessage1));
    }

    @Test
    void getNC() throws Binomial_Exception {
        assertEquals(1, Binomial.getNC(returnValue(2, 1)));
        assertEquals(5, Binomial.getNC(returnValue(11, 5)));
    }

    @Test
    void getPC() throws Binomial_Exception {
        assertEquals(2, Binomial.getPC(returnValue(2, 1)));
        assertEquals(11, Binomial.getPC(returnValue(11, 5)));
    }

    @Test
    void convBinomialtoBR() throws Binomial_Exception {
        assertEquals(21, Binomial.convBinomialtoBR(returnValue(2, 1)));
        assertEquals(115, Binomial.convBinomialtoBR(returnValue(11, 5)));
    }

    @Test
    void testToString() throws Binomial_Exception {
        assertEquals("Binomial - pc: 11, nc: 5", Binomial.toString(returnValue(11, 5)));
    }
}