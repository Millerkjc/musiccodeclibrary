package music_codec_lib;

import music_codec_lib.Exceptions.Binomial_Exception;
import music_codec_lib.Exceptions.Pitch_Exception;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PitchTest {

    private Integer[] returnValue(int pc, int nc){
        return new Integer[]{pc, nc};
    }


    @Test
    void constructor() throws Pitch_Exception {
        assertNotNull(new Pitch("nc", 1));
        assertNotNull(new Pitch("cnc", "Mi#", 0));
        assertNotNull(new Pitch("pc", 7));
        assertNotNull(new Pitch("cpc", "Dob1"));
        assertNotNull(new Pitch("oppc", "1.01"));
        assertNotNull(new Pitch("binom", new Integer[]{11,3}));
        assertNotNull(new Pitch("br", 94));
        assertNotNull(new Pitch("cbr", 1115));


        String expectedMessage1 = "Pitch - Error executing init in codec ('br'); Reason: BR - Incorrect format - 394";
        Exception exception1 = assertThrows(Pitch_Exception.class, () -> {
            new Pitch("br", 394);
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));
    }



    @Test
    void interval() throws Pitch_Exception {
        // PC
        Pitch pc = new Pitch("pc", "Si");
        assertEquals(1, pc.interval("Do"));


        // NC
        Pitch nc = new Pitch("nc", "Si");
        assertEquals(1, nc.interval("Do"));


        // OPPC
        Pitch oppc = new Pitch("oppc", "2.11");
        String expectedMessage_oppc = "Pitch - The current codec ('oppc') doesn't support interval functionality";
        Exception exception_oppc = assertThrows(Pitch_Exception.class, () -> {
            oppc.interval("1.02");
        });
        String actualMessage_oppc = exception_oppc.getMessage();
        assertTrue(actualMessage_oppc.contains(expectedMessage_oppc));


        // Binomial
        Pitch binom = new Pitch("binom", returnValue(11, 0));
        assertArrayEquals(returnValue(6, 2), binom.interval(returnValue(5, 2)));


        // BR
        Pitch br = new Pitch("br", 110);
        assertEquals(105, br.interval(95));


        // CNC
        Pitch cnc = new Pitch("cnc", "Si");
        assertEquals(-6, cnc.interval("C"));


        // CPC
        Pitch cpc = new Pitch("cpc", 123);
        assertEquals(-114, cpc.interval("9"));


        // CBR
        Pitch cbr = new Pitch("cbr", "Mi#4");
        assertEquals(-4042, cbr.interval(10));
    }

    @Test
    void intervalInversion() throws Pitch_Exception {
        // PC
        Pitch pc = new Pitch("pc", "7");
        assertEquals(6, pc.intervalInversion(1));
        assertEquals(5, (int)pc.intervalInversion());


        // NC
        Pitch nc = new Pitch("nc", "3");
        assertEquals(2, nc.intervalInversion(1));
        assertEquals(4, (int)nc.intervalInversion());


        // OPPC
        Pitch oppc = new Pitch("oppc", "2.11");
        String expectedMessage_oppc = "Pitch - The current codec ('oppc') doesn't support intervalInversion functionality";
        Exception exception_oppc = assertThrows(Pitch_Exception.class, () -> {
            oppc.intervalInversion("1.02");
        });
        String actualMessage_oppc = exception_oppc.getMessage();
        assertTrue(actualMessage_oppc.contains(expectedMessage_oppc));


        // Binomial
        Pitch binom = new Pitch("binom", returnValue(9, 5));
        assertArrayEquals(returnValue(3, 2), binom.intervalInversion());
        binom = new Pitch("binom", returnValue(11, 0));
        assertArrayEquals(returnValue(6, 5), binom.intervalInversion(returnValue(5, 2)));


        // BR
        Pitch br = new Pitch("br", 105);
        assertEquals(22, (int)br.intervalInversion());
        br = new Pitch("br", "Mi#");
        assertEquals(52, (int)br.intervalInversion(0));


        // CNC
        Pitch cnc = new Pitch("cnc", "La");
        String expectedMessage_cnc = "Pitch - The current codec ('cnc') doesn't support intervalInversion functionality";
        Exception exception_cnc = assertThrows(Pitch_Exception.class, () -> {
            cnc.intervalInversion("Si");
        });
        String actualMessage_cnc = exception_cnc.getMessage();
        assertTrue(actualMessage_cnc.contains(expectedMessage_cnc));


        // CPC
        Pitch cpc = new Pitch("cpc", "La");
        String expectedMessage_cpc = "Pitch - The current codec ('cpc') doesn't support intervalInversion functionality";
        Exception exception_cpc = assertThrows(Pitch_Exception.class, () -> {
            cpc.intervalInversion("Si");
        });
        String actualMessage_cpc = exception_cpc.getMessage();
        assertTrue(actualMessage_cpc.contains(expectedMessage_cpc));


        // CBR
        Pitch cbr = new Pitch("cbr", "La");
        String expectedMessage_cbr = "Pitch - The current codec ('cbr') doesn't support intervalInversion functionality";
        Exception exception_cbr = assertThrows(Pitch_Exception.class, () -> {
            cbr.intervalInversion("Si");
        });
        String actualMessage_cbr = exception_cbr.getMessage();
        assertTrue(actualMessage_cbr.contains(expectedMessage_cbr));
    }

    @Test
    void IC() throws Pitch_Exception {
        // PC
        Pitch pc = new Pitch("pc", 2);
        assertEquals(4, pc.IC("6"));


        // NC
        Pitch nc = new Pitch("nc", 2);
        assertEquals(3, nc.IC("6"));


        // OPPC
        Pitch oppc = new Pitch("oppc", "2.11");
        String expectedMessage_oppc = "Pitch - The current codec ('oppc') doesn't support IC functionality";
        Exception exception_oppc = assertThrows(Pitch_Exception.class, () -> {
            oppc.IC("1.02");
        });
        String actualMessage_oppc = exception_oppc.getMessage();
        assertTrue(actualMessage_oppc.contains(expectedMessage_oppc));


        // Binomial
        Pitch binom = new Pitch("binom", returnValue(1,2));
        String expectedMessage_binom = "Pitch - The current codec ('binom') doesn't support IC functionality";
        Exception exception_binom = assertThrows(Pitch_Exception.class, () -> {
            binom.IC(returnValue(2,2));
        });
        String actualMessage_binom = exception_binom.getMessage();
        assertTrue(actualMessage_binom.contains(expectedMessage_binom));


        // BR
        Pitch br = new Pitch("br", "La");
        String expectedMessage_br = "Pitch - The current codec ('br') doesn't support IC functionality";
        Exception exception_br = assertThrows(Pitch_Exception.class, () -> {
            br.IC("Si");
        });
        String actualMessage_br = exception_br.getMessage();
        assertTrue(actualMessage_br.contains(expectedMessage_br));


        // CNC
        Pitch cnc = new Pitch("cnc", "La");
        String expectedMessage_cnc = "Pitch - The current codec ('cnc') doesn't support IC functionality";
        Exception exception_cnc = assertThrows(Pitch_Exception.class, () -> {
            cnc.IC("Si");
        });
        String actualMessage_cnc = exception_cnc.getMessage();
        assertTrue(actualMessage_cnc.contains(expectedMessage_cnc));


        // CPC
        Pitch cpc = new Pitch("cpc", "La");
        String expectedMessage_cpc = "Pitch - The current codec ('cpc') doesn't support IC functionality";
        Exception exception_cpc = assertThrows(Pitch_Exception.class, () -> {
            cpc.IC("Si");
        });
        String actualMessage_cpc = exception_cpc.getMessage();
        assertTrue(actualMessage_cpc.contains(expectedMessage_cpc));


        // CBR
        Pitch cbr = new Pitch("cbr", "La");
        String expectedMessage_cbr = "Pitch - The current codec ('cbr') doesn't support IC functionality";
        Exception exception_cbr = assertThrows(Pitch_Exception.class, () -> {
            cbr.IC("Si");
        });
        String actualMessage_cbr = exception_cbr.getMessage();
        assertTrue(actualMessage_cbr.contains(expectedMessage_cbr));
    }

    @Test
    void transpose() throws Pitch_Exception {
        // PC
        Pitch pc = new Pitch("pc", "Do");
        assertEquals(2, pc.transpose(2));


        // NC
        Pitch nc = new Pitch("nc", "Do");
        assertEquals(2, nc.transpose(2));


        // OPPC
        Pitch oppc = new Pitch("oppc", "2.11");
        String expectedMessage_oppc = "Pitch - The current codec ('oppc') doesn't support transpose functionality";
        Exception exception_oppc = assertThrows(Pitch_Exception.class, () -> {
            oppc.transpose("1.02");
        });
        String actualMessage_oppc = exception_oppc.getMessage();
        assertTrue(actualMessage_oppc.contains(expectedMessage_oppc));


        // Binomial
        Pitch binom = new Pitch("binom", returnValue(5, 5));
        assertArrayEquals(returnValue(6, 1), binom.transpose(returnValue(1, -4)));


        // BR
        Pitch br = new Pitch("br", "Si#");
        assertEquals(116, (int)br.transpose(110));


        // CNC
        Pitch cnc = new Pitch("cnc", "G#3");
        assertEquals(13, cnc.transpose(-12));


        // CPC
        Pitch cpc = new Pitch("cpc", "B#2");
        assertEquals(50, cpc.transpose(14));


        // CBR
        Pitch cbr = new Pitch("cbr", "Dob3");
        assertEquals(4112, cbr.transpose(2002));
    }



    @Test
    void getCIName() throws Pitch_Exception {
        // PC
        Pitch pc = new Pitch("pc", 2);
        assertEquals("Quarta giusta", pc.getCIName("7"));


        // NC
        Pitch nc = new Pitch("nc", 2);
        assertEquals("Quarta", nc.getCIName("5"));


        // OPPC
        Pitch oppc = new Pitch("oppc", "2.11");
        String expectedMessage_oppc = "Pitch - The current codec ('oppc') doesn't support getCIName functionality";
        Exception exception_oppc = assertThrows(Pitch_Exception.class, () -> {
            oppc.getCIName("1.02");
        });
        String actualMessage_oppc = exception_oppc.getMessage();
        assertTrue(actualMessage_oppc.contains(expectedMessage_oppc));


        // Binomial
        Pitch binom = new Pitch("binom", returnValue(1,2));
        String expectedMessage_binom = "Pitch - The current codec ('binom') doesn't support getCIName functionality";
        Exception exception_binom = assertThrows(Pitch_Exception.class, () -> {
            binom.getCIName(returnValue(1,2));
        });
        String actualMessage_binom = exception_binom.getMessage();
        assertTrue(actualMessage_binom.contains(expectedMessage_binom));


        // BR
        Pitch br = new Pitch("br", "La");
        String expectedMessage_br = "Pitch - The current codec ('br') doesn't support getCIName functionality";
        Exception exception_br = assertThrows(Pitch_Exception.class, () -> {
            br.getCIName("Si");
        });
        String actualMessage_br = exception_br.getMessage();
        assertTrue(actualMessage_br.contains(expectedMessage_br));


        // CNC
        Pitch cnc = new Pitch("cnc", "La");
        String expectedMessage_cnc = "Pitch - The current codec ('cnc') doesn't support getCIName functionality";
        Exception exception_cnc = assertThrows(Pitch_Exception.class, () -> {
            cnc.getCIName("Si");
        });
        String actualMessage_cnc = exception_cnc.getMessage();
        assertTrue(actualMessage_cnc.contains(expectedMessage_cnc));


        // CPC
        Pitch cpc = new Pitch("cpc", "La");
        String expectedMessage_cpc = "Pitch - The current codec ('cpc') doesn't support getCIName functionality";
        Exception exception_cpc = assertThrows(Pitch_Exception.class, () -> {
            cpc.getCIName("Si");
        });
        String actualMessage_cpc = exception_cpc.getMessage();
        assertTrue(actualMessage_cpc.contains(expectedMessage_cpc));


        // CBR
        Pitch cbr = new Pitch("cbr", "La");
        String expectedMessage_cbr = "Pitch - The current codec ('cbr') doesn't support getCIName functionality";
        Exception exception_cbr = assertThrows(Pitch_Exception.class, () -> {
            cbr.getCIName("Si");
        });
        String actualMessage_cbr = exception_cbr.getMessage();
        assertTrue(actualMessage_cbr.contains(expectedMessage_cbr));
    }
    
    @Test
    void getCIinverseName() throws Pitch_Exception {
        // PC
        Pitch pc = new Pitch("pc", 2);
        assertEquals("Quinta giusta", pc.getCIinverseName("7"));


        // NC
        Pitch nc = new Pitch("nc", 2);
        assertEquals("Quinta", nc.getCIinverseName("5"));


        // OPPC
        Pitch oppc = new Pitch("oppc", "2.11");
        String expectedMessage_oppc = "Pitch - The current codec ('oppc') doesn't support getCIinverseName functionality";
        Exception exception_oppc = assertThrows(Pitch_Exception.class, () -> {
            oppc.getCIinverseName("1.02");
        });
        String actualMessage_oppc = exception_oppc.getMessage();
        assertTrue(actualMessage_oppc.contains(expectedMessage_oppc));


        // Binomial
        Pitch binom = new Pitch("binom", returnValue(1,2));
        String expectedMessage_binom = "Pitch - The current codec ('binom') doesn't support getCIinverseName functionality";
        Exception exception_binom = assertThrows(Pitch_Exception.class, () -> {
            binom.getCIinverseName(returnValue(1,2));
        });
        String actualMessage_binom = exception_binom.getMessage();
        assertTrue(actualMessage_binom.contains(expectedMessage_binom));


        // BR
        Pitch br = new Pitch("br", "La");
        String expectedMessage_br = "Pitch - The current codec ('br') doesn't support getCIinverseName functionality";
        Exception exception_br = assertThrows(Pitch_Exception.class, () -> {
            br.getCIinverseName("Si");
        });
        String actualMessage_br = exception_br.getMessage();
        assertTrue(actualMessage_br.contains(expectedMessage_br));


        // CNC
        Pitch cnc = new Pitch("cnc", "La");
        String expectedMessage_cnc = "Pitch - The current codec ('cnc') doesn't support getCIinverseName functionality";
        Exception exception_cnc = assertThrows(Pitch_Exception.class, () -> {
            cnc.getCIinverseName("Si");
        });
        String actualMessage_cnc = exception_cnc.getMessage();
        assertTrue(actualMessage_cnc.contains(expectedMessage_cnc));


        // CPC
        Pitch cpc = new Pitch("cpc", "La");
        String expectedMessage_cpc = "Pitch - The current codec ('cpc') doesn't support getCIinverseName functionality";
        Exception exception_cpc = assertThrows(Pitch_Exception.class, () -> {
            cpc.getCIinverseName("Si");
        });
        String actualMessage_cpc = exception_cpc.getMessage();
        assertTrue(actualMessage_cpc.contains(expectedMessage_cpc));


        // CBR
        Pitch cbr = new Pitch("cbr", "La");
        String expectedMessage_cbr = "Pitch - The current codec ('cbr') doesn't support getCIinverseName functionality";
        Exception exception_cbr = assertThrows(Pitch_Exception.class, () -> {
            cbr.getCIinverseName("Si");
        });
        String actualMessage_cbr = exception_cbr.getMessage();
        assertTrue(actualMessage_cbr.contains(expectedMessage_cbr));
    }

    @Test
    void getBinomPitch() throws Pitch_Exception {
        // PC
        Pitch pc = new Pitch("pc", 2);
        String expectedMessage_pc = "Pitch - The current codec ('pc') doesn't support getBinomPitch functionality";
        Exception exception_pc = assertThrows(Pitch_Exception.class, () -> {
            pc.getBinomPitch();
        });
        String actualMessage_pc = exception_pc.getMessage();
        assertTrue(actualMessage_pc.contains(expectedMessage_pc));


        // NC
        Pitch nc = new Pitch("nc", 2);
        String expectedMessage_nc = "Pitch - The current codec ('nc') doesn't support getBinomPitch functionality";
        Exception exception_nc = assertThrows(Pitch_Exception.class, () -> {
            nc.getBinomPitch();
        });
        String actualMessage_nc = exception_nc.getMessage();
        assertTrue(actualMessage_nc.contains(expectedMessage_nc));


        // OPPC
        Pitch oppc = new Pitch("oppc", "2.11");
        String expectedMessage_oppc = "Pitch - The current codec ('oppc') doesn't support getBinomPitch functionality";
        Exception exception_oppc = assertThrows(Pitch_Exception.class, () -> {
            oppc.getBinomPitch();
        });
        String actualMessage_oppc = exception_oppc.getMessage();
        assertTrue(actualMessage_oppc.contains(expectedMessage_oppc));

        // Binomial
        Pitch binom = new Pitch("binom", returnValue(11, 5));
        assertEquals("Lax", binom.getBinomPitch());


        // BR
        Pitch br = new Pitch("br", "114");
        assertEquals("Solxx", br.getBinomPitch());


        // CNC
        Pitch cnc = new Pitch("cnc", "La");
        String expectedMessage_cnc = "Pitch - The current codec ('cnc') doesn't support getBinomPitch functionality";
        Exception exception_cnc = assertThrows(Pitch_Exception.class, () -> {
            cnc.getBinomPitch();
        });
        String actualMessage_cnc = exception_cnc.getMessage();
        assertTrue(actualMessage_cnc.contains(expectedMessage_cnc));


        // CPC
        Pitch cpc = new Pitch("cpc", "La");
        String expectedMessage_cpc = "Pitch - The current codec ('cpc') doesn't support getBinomPitch functionality";
        Exception exception_cpc = assertThrows(Pitch_Exception.class, () -> {
            cpc.getBinomPitch();
        });
        String actualMessage_cpc = exception_cpc.getMessage();
        assertTrue(actualMessage_cpc.contains(expectedMessage_cpc));


        // CBR
        Pitch cbr = new Pitch("cbr", "Sib123");
        assertEquals("Sib", cbr.getBinomPitch());
    }

    @Test
    void getBinomInterval() throws Pitch_Exception {
        // PC
        Pitch pc = new Pitch("pc", 2);
        String expectedMessage_pc = "Pitch - The current codec ('pc') doesn't support getBinomInterval functionality";
        Exception exception_pc = assertThrows(Pitch_Exception.class, () -> {
            pc.getBinomInterval();
        });
        String actualMessage_pc = exception_pc.getMessage();
        assertTrue(actualMessage_pc.contains(expectedMessage_pc));


        // NC
        Pitch nc = new Pitch("nc", 2);
        String expectedMessage_nc = "Pitch - The current codec ('nc') doesn't support getBinomInterval functionality";
        Exception exception_nc = assertThrows(Pitch_Exception.class, () -> {
            nc.getBinomInterval();
        });
        String actualMessage_nc = exception_nc.getMessage();
        assertTrue(actualMessage_nc.contains(expectedMessage_nc));


        // OPPC
        Pitch oppc = new Pitch("oppc", "2.11");
        String expectedMessage_oppc = "Pitch - The current codec ('oppc') doesn't support getBinomInterval functionality";
        Exception exception_oppc = assertThrows(Pitch_Exception.class, () -> {
            oppc.getBinomInterval();
        });
        String actualMessage_oppc = exception_oppc.getMessage();
        assertTrue(actualMessage_oppc.contains(expectedMessage_oppc));


        // Binomial
        Pitch binom = new Pitch("binom", "Dobbbbb");
        assertEquals("5d1", binom.getBinomInterval());


        // BR
        Pitch br = new Pitch("br", 70);
        assertEquals("5d1", br.getBinomInterval());


        // CNC
        Pitch cnc = new Pitch("cnc", "La");
        String expectedMessage_cnc = "Pitch - The current codec ('cnc') doesn't support getBinomInterval functionality";
        Exception exception_cnc = assertThrows(Pitch_Exception.class, () -> {
            cnc.getBinomInterval();
        });
        String actualMessage_cnc = exception_cnc.getMessage();
        assertTrue(actualMessage_cnc.contains(expectedMessage_cnc));


        // CPC
        Pitch cpc = new Pitch("cpc", "La");
        String expectedMessage_cpc = "Pitch - The current codec ('cpc') doesn't support getBinomInterval functionality";
        Exception exception_cpc = assertThrows(Pitch_Exception.class, () -> {
            cpc.getBinomInterval();
        });
        String actualMessage_cpc = exception_cpc.getMessage();
        assertTrue(actualMessage_cpc.contains(expectedMessage_cpc));


        // CBR
        Pitch cbr = new Pitch("cbr", "Sib123");
        assertEquals("m7", cbr.getBinomInterval());
    }

    @Test
    void getOctave() throws Pitch_Exception {
        // PC
        Pitch pc = new Pitch("pc", 2);
        String expectedMessage_pc = "Pitch - The current codec ('pc') doesn't support getOctave functionality";
        Exception exception_pc = assertThrows(Pitch_Exception.class, () -> {
            pc.getOctave();
        });
        String actualMessage_pc = exception_pc.getMessage();
        assertTrue(actualMessage_pc.contains(expectedMessage_pc));


        // NC
        Pitch nc = new Pitch("nc", 2);
        String expectedMessage_nc = "Pitch - The current codec ('nc') doesn't support getOctave functionality";
        Exception exception_nc = assertThrows(Pitch_Exception.class, () -> {
            nc.getOctave();
        });
        String actualMessage_nc = exception_nc.getMessage();
        assertTrue(actualMessage_nc.contains(expectedMessage_nc));


        // OPPC
        Pitch oppc = new Pitch("oppc", "3.11");
        assertEquals(3, oppc.getOctave());


        // Binomial
        Pitch binom = new Pitch("binom", returnValue(1,2));
        String expectedMessage_binom = "Pitch - The current codec ('binom') doesn't support getOctave functionality";
        Exception exception_binom = assertThrows(Pitch_Exception.class, () -> {
            binom.getOctave();
        });
        String actualMessage_binom = exception_binom.getMessage();
        assertTrue(actualMessage_binom.contains(expectedMessage_binom));


        // BR
        Pitch br = new Pitch("br", "La");
        String expectedMessage_br = "Pitch - The current codec ('br') doesn't support getOctave functionality";
        Exception exception_br = assertThrows(Pitch_Exception.class, () -> {
            br.getOctave();
        });
        String actualMessage_br = exception_br.getMessage();
        assertTrue(actualMessage_br.contains(expectedMessage_br));


        // CNC
        Pitch cnc = new Pitch("cnc", "Do12");
        assertEquals(12, cnc.getOctave());


        // CPC
        Pitch cpc = new Pitch("cpc", "B#2");
        assertEquals(3, cpc.getOctave());


        // CBR
        Pitch cbr = new Pitch("cbr", "Sib123");
        assertEquals(123, cbr.getOctave());
    }

    @Test
    void getNC() throws Pitch_Exception {
        // PC
        Pitch pc = new Pitch("pc", 2);
        String expectedMessage1 = "Pitch - The current codec ('pc') doesn't support getNC functionality";
        Exception exception1 = assertThrows(Pitch_Exception.class, () -> {
            pc.getNC();
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));


        // NC
        Pitch nc = new Pitch("nc", 2);
        assertEquals(2, nc.getNC());


        // OPPC
        Pitch oppc = new Pitch("oppc", "2.11");
        String expectedMessage_oppc = "Pitch - The current codec ('oppc') doesn't support getNC functionality";
        Exception exception_oppc = assertThrows(Pitch_Exception.class, () -> {
            oppc.getNC();
        });
        String actualMessage_oppc = exception_oppc.getMessage();
        assertTrue(actualMessage_oppc.contains(expectedMessage_oppc));


        // Binomial
        Pitch binom = new Pitch("binom",returnValue(2,1));
        assertEquals(1, binom.getNC());


        // BR
        Pitch br = new Pitch("br", 70);
        assertEquals(0, br.getNC());


        // CNC
        Pitch cnc = new Pitch("cnc", 124);
        assertEquals(5, cnc.getNC());


        // CPC
        Pitch cpc = new Pitch("cpc", "La");
        String expectedMessage_cpc = "Pitch - The current codec ('cpc') doesn't support getNC functionality";
        Exception exception_cpc = assertThrows(Pitch_Exception.class, () -> {
            cpc.getNC();
        });
        String actualMessage_cpc = exception_cpc.getMessage();
        assertTrue(actualMessage_cpc.contains(expectedMessage_cpc));


        // CBR
        Pitch cbr = new Pitch("cbr", "3012");
        assertEquals(2, cbr.getNC());
    }

    @Test
    void getPC() throws Pitch_Exception {
        // PC
        Pitch pc = new Pitch("pc", 11);
        assertEquals(11, pc.getPC());


        // NC
        Pitch nc = new Pitch("nc", 2);
        String expectedMessage_nc = "Pitch - The current codec ('nc') doesn't support getPC functionality";
        Exception exception_nc = assertThrows(Pitch_Exception.class, () -> {
            nc.getPC();
        });
        String actualMessage_nc = exception_nc.getMessage();
        assertTrue(actualMessage_nc.contains(expectedMessage_nc));


        // OPPC
        Pitch oppc = new Pitch("oppc", "2.06");
        assertEquals(6, oppc.getPC());


        // Binomial
        Pitch binom = new Pitch("binom",returnValue(2,1));
        assertEquals(2, binom.getPC());


        // BR
        Pitch br = new Pitch("br", 70);
        assertEquals(7, br.getPC());


        // CNC
        Pitch cnc = new Pitch("cnc", "La");
        String expectedMessage_cnc = "Pitch - The current codec ('cnc') doesn't support getPC functionality";
        Exception exception_cnc = assertThrows(Pitch_Exception.class, () -> {
            cnc.getPC();
        });
        String actualMessage_cnc = exception_cnc.getMessage();
        assertTrue(actualMessage_cnc.contains(expectedMessage_cnc));


        // CPC
        Pitch cpc = new Pitch("cpc", "G#3");
        assertEquals(8, cpc.getPC());


        // CBR
        Pitch cbr = new Pitch("cbr", "3012");
        assertEquals(1, cbr.getPC());
    }

    @Test
    void getBR() throws Pitch_Exception {
        // PC
        Pitch pc = new Pitch("pc", 2);
        String expectedMessage1 = "Pitch - The current codec ('pc') doesn't support getBR functionality";
        Exception exception1 = assertThrows(Pitch_Exception.class, () -> {
            pc.getBR();
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));


        // NC
        Pitch nc = new Pitch("nc", 2);
        String expectedMessage_nc = "Pitch - The current codec ('nc') doesn't support getBR functionality";
        Exception exception_nc = assertThrows(Pitch_Exception.class, () -> {
            nc.getBR();
        });
        String actualMessage_nc = exception_nc.getMessage();
        assertTrue(actualMessage_nc.contains(expectedMessage_nc));


        // OPPC
        Pitch oppc = new Pitch("oppc", "2.11");
        String expectedMessage_oppc = "Pitch - The current codec ('oppc') doesn't support getBR functionality";
        Exception exception_oppc = assertThrows(Pitch_Exception.class, () -> {
            oppc.getBR();
        });
        String actualMessage_oppc = exception_oppc.getMessage();
        assertTrue(actualMessage_oppc.contains(expectedMessage_oppc));


        // Binomial
        Pitch binom = new Pitch("binom", returnValue(1,2));
        String expectedMessage_binom = "Pitch - The current codec ('binom') doesn't support getBR functionality";
        Exception exception_binom = assertThrows(Pitch_Exception.class, () -> {
            binom.getBR();
        });
        String actualMessage_binom = exception_binom.getMessage();
        assertTrue(actualMessage_binom.contains(expectedMessage_binom));


        // BR
        Pitch br = new Pitch("br", "La");
        String expectedMessage_br = "Pitch - The current codec ('br') doesn't support getBR functionality";
        Exception exception_br = assertThrows(Pitch_Exception.class, () -> {
            br.getBR();
        });
        String actualMessage_br = exception_br.getMessage();
        assertTrue(actualMessage_br.contains(expectedMessage_br));


        // CNC
        Pitch cnc = new Pitch("cnc", "La");
        String expectedMessage_cnc = "Pitch - The current codec ('cnc') doesn't support getBR functionality";
        Exception exception_cnc = assertThrows(Pitch_Exception.class, () -> {
            cnc.getBR();
        });
        String actualMessage_cnc = exception_cnc.getMessage();
        assertTrue(actualMessage_cnc.contains(expectedMessage_cnc));


        // CPC
        Pitch cpc = new Pitch("cpc", "La");
        String expectedMessage_cpc = "Pitch - The current codec ('cpc') doesn't support getBR functionality";
        Exception exception_cpc = assertThrows(Pitch_Exception.class, () -> {
            cpc.getBR();
        });
        String actualMessage_cpc = exception_cpc.getMessage();
        assertTrue(actualMessage_cpc.contains(expectedMessage_cpc));


        // CBR
        Pitch cbr = new Pitch("cbr", "Sib123");
        assertEquals(106, cbr.getBR());
    }

    @Test
    void convertTo() throws Pitch_Exception, Binomial_Exception {
        // PC
        Pitch pc = new Pitch("pc", 1);
        pc.convertTo("nc");
        assertEquals(0, (int)pc.getPitch());

        // NC
        Pitch nc = new Pitch("nc", 1);
        nc.convertTo("pc");
        assertEquals(2, (int)nc.getPitch());


        // OPPC
        Pitch oppc = new Pitch("oppc", "2.06");
        oppc.convertTo("cnc");
        assertEquals(17, (int)oppc.getPitch());
        oppc = new Pitch("oppc", "2.06");
        oppc.convertTo("cpc");
        assertEquals(30, (int)oppc.getPitch());
        oppc = new Pitch("oppc", "2.06");
        oppc.convertTo("nc");
        assertEquals(3, (int)oppc.getPitch());
        oppc = new Pitch("oppc", "2.06");
        oppc.convertTo("pc");
        assertEquals(6, (int)oppc.getPitch());


        // Binomial
        Pitch binom = new Pitch("binom",returnValue(2,1));
        binom.convertTo("br");
        assertEquals(21, (int)binom.getPitch());


        // BR
        Pitch br = new Pitch("br", 21);
        br.convertTo("binom");
        assertEquals("Binomial - pc: 2, nc: 1", Binomial.toString(br.getPitch()));


        // CNC
        Pitch cnc = new Pitch("cnc","Do12");
        cnc.convertTo("cpc");
        assertEquals(144, (int)cnc.getPitch());
        cnc = new Pitch("cnc","Bx1");
        cnc.convertTo("oppc");
        assertEquals(2.0, (double)cnc.getPitch());
        cnc = new Pitch("cnc","G#3");
        cnc.convertTo("nc");
        assertEquals(4, (int)cnc.getPitch());
        cnc = new Pitch("cnc","124");
        cnc.convertTo("pc");
        assertEquals(9, (int)cnc.getPitch());


        // CPC
        Pitch cpc = new Pitch("cpc", "Do12");
        cpc.convertTo("cnc");
        assertEquals(84, (int)cpc.getPitch());
        cpc = new Pitch("cpc", "Bx1");
        cpc.convertTo("oppc");
        assertEquals(2.01, (double)cpc.getPitch());
        cpc = new Pitch("cpc", "Dob4");
        cpc.convertTo("nc");
        assertEquals(6, (int)cpc.getPitch());
        cpc = new Pitch("cpc", "B#2");
        cpc.convertTo("pc");
        assertEquals(0, (int)cpc.getPitch());


        // CBR
        Pitch cbr = new Pitch("cbr", "La");
        String expectedMessage_cbr = "Pitch - Error executing convCBRtoBR in codec ('cbr')";
        Exception exception_cbr = assertThrows(Pitch_Exception.class, () -> {
            cbr.convertTo("br");
        });
        String actualMessage_cbr = exception_cbr.getMessage();
        assertTrue(actualMessage_cbr.contains(expectedMessage_cbr));
    }

    @Test
    void addOctave() throws Pitch_Exception {
        // PC
        Pitch pc = new Pitch("pc", 2);
        pc.addOctave(1);
        assertEquals("cpc", pc.getCodec());
        assertEquals(14, (int)pc.getPitch());


        // NC
        Pitch nc = new Pitch("nc", 2);
        nc.addOctave(1);
        assertEquals("cnc", nc.getCodec());
        assertEquals(9, (int)nc.getPitch());


        // OPPC
        Pitch oppc = new Pitch("oppc", "2.11");
        String expectedMessage_oppc = "Pitch - The current codec ('oppc') already has octave information";
        Exception exception_oppc = assertThrows(Pitch_Exception.class, () -> {
            oppc.addOctave(1);
        });
        String actualMessage_oppc = exception_oppc.getMessage();
        assertTrue(actualMessage_oppc.contains(expectedMessage_oppc));


        // Binomial
        Pitch binom = new Pitch("binom",returnValue(2,1));
        binom.addOctave(3);
        assertEquals("cbr", binom.getCodec());
        assertEquals(3021, (int)binom.getPitch());


        // BR
        Pitch br = new Pitch("br", 21);
        br.addOctave(12);
        assertEquals("cbr", br.getCodec());
        assertEquals(12021, (int)br.getPitch());


        // CNC
        Pitch cnc = new Pitch("cnc", "Do#5");
        String expectedMessage_cnc = "Pitch - The current codec ('cnc') already has octave information";
        Exception exception_cnc = assertThrows(Pitch_Exception.class, () -> {
            cnc.addOctave(1);
        });
        String actualMessage_cnc = exception_cnc.getMessage();
        assertTrue(actualMessage_cnc.contains(expectedMessage_cnc));


        // CPC
        Pitch cpc = new Pitch("cpc", "Do#5");
        String expectedMessage_cpc = "Pitch - The current codec ('cpc') already has octave information";
        Exception exception_cpc = assertThrows(Pitch_Exception.class, () -> {
            cpc.addOctave(1);
        });
        String actualMessage_cpc = exception_cpc.getMessage();
        assertTrue(actualMessage_cpc.contains(expectedMessage_cpc));


        // CBR
        Pitch cbr = new Pitch("cbr", "La");
        String expectedMessage_cbr = "Pitch - The current codec ('cbr') already has octave information";
        Exception exception_cbr = assertThrows(Pitch_Exception.class, () -> {
            cbr.addOctave(1);
        });
        String actualMessage_cbr = exception_cbr.getMessage();
        assertTrue(actualMessage_cbr.contains(expectedMessage_cbr));
    }

    @Test
    void testToString() throws Pitch_Exception {
        // PC
        Pitch pc = new Pitch("pc", 2);
        assertEquals("PC: 2", pc.toString());


        // NC
        Pitch nc = new Pitch("nc", 2);
        assertEquals("NC: 2", nc.toString());


        // OPPC
        Pitch oppc = new Pitch("oppc", 2);
        assertEquals("OPPC: 2.0", oppc.toString());


        // Binomial
        Pitch binom = new Pitch("binom",returnValue(11,5));
        assertEquals("Binomial - pc: 11, nc: 5", binom.toString());


        // BR
        Pitch br = new Pitch("br", 21);
        assertEquals("BR: 21", br.toString());


        // CNC
        Pitch cnc = new Pitch("cnc", 124);
        assertEquals("CNC: 124", cnc.toString());


        // CNC
        Pitch cpc = new Pitch("cpc", 11);
        assertEquals("CPC: 11", cpc.toString());


        // CBR
        Pitch cbr = new Pitch("cbr", 95);
        assertEquals("CBR: 95", cbr.toString());
    }
}