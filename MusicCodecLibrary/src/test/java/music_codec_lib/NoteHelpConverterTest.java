package music_codec_lib;

import music_codec_lib.Exceptions.NoteHelpConverter_Exception;
import music_codec_lib.Exceptions.PC_Exception;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;

class NoteHelpConverterTest {

    @Test
    void fromMatchToGroups() throws NoteHelpConverter_Exception {
        assertEquals(new ArrayList<>(Arrays.asList("Do","#","0")), NoteHelpConverter.fromMatchToGroups(EnumMatcher.SEMITONES_PITCH.getValue(), "Do#"));
        assertEquals(new ArrayList<>(Arrays.asList("B","b","3")), NoteHelpConverter.fromMatchToGroups(EnumMatcher.SEMITONES_PITCH_ANGLO.getValue(),"Bb3"));
        assertEquals(new ArrayList<>(Arrays.asList("2")), NoteHelpConverter.fromMatchToGroups(EnumMatcher.INTS.getValue(), 2));
        assertEquals(new ArrayList<>(Arrays.asList("-15")), NoteHelpConverter.fromMatchToGroups(EnumMatcher.INTS_NEGATIVE.getValue(), -15));
        assertEquals(new ArrayList<>(Arrays.asList("123.4")), NoteHelpConverter.fromMatchToGroups(EnumMatcher.DECIMAL_NEGATIVE.getValue(), 123.4));

        String expectedMessage = "NoteHelperConverter - Matcher error";
        Exception exception = assertThrows(NoteHelpConverter_Exception.class, () -> {
            NoteHelpConverter.fromMatchToGroups(EnumMatcher.SEMITONES_PITCH.getValue(),"42#1");
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getNoteMatched() throws NoteHelpConverter_Exception {
        assertEquals("1", NoteHelpConverter.getNoteMatched("1"));
        assertEquals("-2", NoteHelpConverter.getNoteMatched(-2));
        assertEquals("Mi", NoteHelpConverter.getNoteMatched("Mi#5"));
        assertEquals("F", NoteHelpConverter.getNoteMatched("Fb3"));
        assertEquals("123.4", NoteHelpConverter.getNoteMatched(123.4));

        String expectedMessage = "NoteHelperConverter - Error retrieving note";
        Exception exception = assertThrows(NoteHelpConverter_Exception.class, () -> {
            NoteHelpConverter.getNoteMatched("42#1");
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getAlterationMatched() throws NoteHelpConverter_Exception {
        assertEquals("b", NoteHelpConverter.getAlterationMatched("Sib"));
        assertEquals("#", NoteHelpConverter.getAlterationMatched("Sol#3"));
        assertEquals("", NoteHelpConverter.getAlterationMatched("A"));
        assertEquals("", NoteHelpConverter.getAlterationMatched(1));

        String expectedMessage = "NoteHelperConverter - Error retrieving alteration";
        Exception exception = assertThrows(NoteHelpConverter_Exception.class, () -> {
            NoteHelpConverter.getAlterationMatched("42#1");
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getOctaveMatched() throws NoteHelpConverter_Exception {
        assertEquals(0, NoteHelpConverter.getOctaveMatched("Sib"));
        assertEquals(3, NoteHelpConverter.getOctaveMatched("Sol#3"));
        assertEquals(1, NoteHelpConverter.getOctaveMatched("B1"));

        String expectedMessage = "NoteHelperConverter - Error retrieving octave";
        Exception exception = assertThrows(NoteHelpConverter_Exception.class, () -> {
            NoteHelpConverter.getOctaveMatched(1);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void standardAngloConverter() throws NoteHelpConverter_Exception {
        assertEquals("Re", NoteHelpConverter.standardAngloConverter(2));
        assertEquals("C", NoteHelpConverter.standardAngloConverter("do"));
        assertEquals("Do", NoteHelpConverter.standardAngloConverter("C"));
        assertEquals("B#4", NoteHelpConverter.standardAngloConverter("Si#4"));


        String expectedMessage = "NoteHelperConverter - Error converting note - getNoteFromDict";
        Exception exception = assertThrows(NoteHelpConverter_Exception.class, () -> {
            NoteHelpConverter.standardAngloConverter("12p3");
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getNoteFromDict() throws NoteHelpConverter_Exception {
        assertEquals("Re#", NoteHelpConverter.getNoteFromDict(3));
        assertEquals("Do", NoteHelpConverter.getNoteFromDict("dO"));
        assertEquals("D", NoteHelpConverter.getNoteFromDict("d"));
        assertEquals("Si#4", NoteHelpConverter.getNoteFromDict("si#4"));


        String expectedMessage = "NoteHelperConverter - Error converting note - getNoteFromDict";
        Exception exception = assertThrows(NoteHelpConverter_Exception.class, () -> {
            NoteHelpConverter.getNoteFromDict("12p3");
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void fromPitchesToInt() throws NoteHelpConverter_Exception {
        assertEquals(12, NoteHelpConverter.fromPitchesToInt(12));
        assertEquals(5, NoteHelpConverter.fromPitchesToInt("Mi#"));
        assertEquals(37, NoteHelpConverter.fromPitchesToInt("Do#3"));
        assertEquals(37, NoteHelpConverter.fromPitchesToInt("Six2"));
        assertEquals(53, NoteHelpConverter.fromPitchesToInt("Solbb4"));
    }

    @Test
    void toSemitones() throws NoteHelpConverter_Exception {
        assertEquals(1, NoteHelpConverter.toSemitones(13));
        assertEquals(2, NoteHelpConverter.toSemitones("Re"));
        assertEquals(11, NoteHelpConverter.toSemitones("Lax7"));
    }

    @Test
    void fromSemitonesToNote() throws NoteHelpConverter_Exception {
        assertEquals(0, NoteHelpConverter.fromSemitonesToNote(13));
        assertEquals(1, NoteHelpConverter.fromSemitonesToNote("Re"));
        assertEquals(6, NoteHelpConverter.fromSemitonesToNote("Lax7"));
    }

    @Test
    void fromNoteToSemitone() throws NoteHelpConverter_Exception {
        assertEquals(11, NoteHelpConverter.fromNoteToSemitone(6));
        assertEquals(2, NoteHelpConverter.fromNoteToSemitone("Re"));


        String expectedMessage = "NoteHelperConverter - Invalid format, note range [0..6]";
        Exception exception = assertThrows(NoteHelpConverter_Exception.class, () -> {
            NoteHelpConverter.fromNoteToSemitone("7");
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }
}