package music_codec_lib;

import music_codec_lib.Exceptions.NoteMatcher_Exception;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import music_codec_lib.EnumMatcher;

class NoteMatcherTest {

    @Test
    void notePitchIndexWithList() {
        ArrayList latin_semitones = new ArrayList<String>(Arrays.asList("Do", "Do#", "Re", "Re#", "Mi", "Fa", "Fa#", "Sol", "Sol#", "La", "La#", "Si"));
        ArrayList anglo_semitones = new ArrayList<String>(Arrays.asList("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"));

        // latin semitones
        assertEquals(0, NoteMatcher.notePitchIndex(latin_semitones, "Do"));
        assertEquals(2, NoteMatcher.notePitchIndex(latin_semitones, "re"));
        assertEquals(4, NoteMatcher.notePitchIndex(latin_semitones, "mI"));
        assertEquals(5, NoteMatcher.notePitchIndex(latin_semitones, "FA"));
        assertEquals(-1, NoteMatcher.notePitchIndex(latin_semitones, "C"));

        // anglo semitones
        assertEquals(0, NoteMatcher.notePitchIndex(anglo_semitones, "C"));
        assertEquals(8, NoteMatcher.notePitchIndex(anglo_semitones, "g#"));
        assertEquals(11, NoteMatcher.notePitchIndex(anglo_semitones, "b"));
        assertEquals(-1, NoteMatcher.notePitchIndex(anglo_semitones, "Sol"));
    }

    @Test
    void NotePitchIndexWithIndex() {
        int latin_semitones_index = 0;
        int anglo_semitones_index = 1;
        int latin_notes_index = 2;
        int anglo_notes_index = 3;

        assertEquals(0, NoteMatcher.notePitchIndex(latin_semitones_index, "Do"));
        assertEquals(-1, NoteMatcher.notePitchIndex(latin_semitones_index, "G#"));

        assertEquals(6, NoteMatcher.notePitchIndex(anglo_semitones_index, "F#"));
        assertEquals(-1, NoteMatcher.notePitchIndex(anglo_semitones_index, "La"));

        assertEquals(6, NoteMatcher.notePitchIndex(latin_notes_index, "Si"));
        assertEquals(-1, NoteMatcher.notePitchIndex(latin_notes_index, "b"));

        assertEquals(5, NoteMatcher.notePitchIndex(anglo_notes_index, "a"));
        assertEquals(-1, NoteMatcher.notePitchIndex(anglo_notes_index, "Do"));

    }

    @Test
    void findMatcher() {
        // one parameter: findMatcher(T pitch)
        assertEquals(EnumMatcher.INTS.getValue(), NoteMatcher.findMatcher(1));
        assertEquals(EnumMatcher.SEMITONES_PITCH.getValue(), NoteMatcher.findMatcher("Faxxx"));
        assertEquals(EnumMatcher.INTS_NEGATIVE.getValue(), NoteMatcher.findMatcher(-9));
        assertEquals(EnumMatcher.NOT_FOUND.getValue(), NoteMatcher.findMatcher("lalaland"));

        // two parameters: findMatcher(int index, T pitch)
        assertEquals(EnumMatcher.INTS.getValue(), NoteMatcher.findMatcher(EnumMatcher.INTS.getValue(), 1));
        assertEquals(EnumMatcher.SEMITONES_PITCH.getValue(), NoteMatcher.findMatcher(EnumMatcher.SEMITONES_PITCH.getValue(),"Faxxx"));
        assertEquals(EnumMatcher.INTS_NEGATIVE.getValue(), NoteMatcher.findMatcher(EnumMatcher.INTS_NEGATIVE.getValue(),-9));
        assertEquals(EnumMatcher.NOT_FOUND.getValue(), NoteMatcher.findMatcher(EnumMatcher.ALL_PITCHES_AND_NOTE.getValue(),"lalaland"));

        // three parameters: findMatcher(int start, int end, T pitch)
        assertEquals(EnumMatcher.INTS.getValue(), NoteMatcher.findMatcher(EnumMatcher.ZERO.getValue(), EnumMatcher.NUMBERS_EXPANSION.getValue(), 1));
        assertEquals(EnumMatcher.SEMITONES_PITCH.getValue(), NoteMatcher.findMatcher(EnumMatcher.ZERO.getValue(),EnumMatcher.END.getValue() + 1, "Faxxx"));
        assertEquals(EnumMatcher.INTS_NEGATIVE.getValue(), NoteMatcher.findMatcher(EnumMatcher.NUMBERS_EXPANSION.getValue(), EnumMatcher.DECIMAL_NEGATIVE.getValue() + 1,-9));
        assertEquals(EnumMatcher.NOT_FOUND.getValue(), NoteMatcher.findMatcher(EnumMatcher.ZERO.getValue(), EnumMatcher.END.getValue(),"lalaland"));
    }

    @Test
    void checkOctave() {
        assertEquals(true, NoteMatcher.checkOctave(9));
        assertEquals(true, NoteMatcher.checkOctave("9"));
        assertEquals(false, NoteMatcher.checkOctave(-1));
        assertEquals(false, NoteMatcher.checkOctave("-1"));
    }
}