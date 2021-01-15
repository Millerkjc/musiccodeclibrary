package music_codec_lib;

import music_codec_lib.Exceptions.NoteHelpConverter_Exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoteMatcher{


    /**
     * Dict with all list of base semitones and notes in latin and aglo
     */
    public static Map<Integer, List<String>> notesAndPitches;
    static{
        notesAndPitches = new HashMap<>();

        notesAndPitches.put(0, new ArrayList<String>(Arrays.asList("Do", "Do#", "Re", "Re#", "Mi", "Fa", "Fa#", "Sol", "Sol#", "La", "La#", "Si")));
        notesAndPitches.put(1,               new ArrayList<String>(Arrays.asList("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")));
        notesAndPitches.put(2,                                     new ArrayList<String>(Arrays.asList("Do", "Re", "Mi", "Fa", "Sol", "La", "Si")));
        notesAndPitches.put(3,                                             new ArrayList<String>(Arrays.asList("C", "D", "E", "F", "G", "A", "B")));


    }


    static int semitones_num = NoteMatcher.notesAndPitches.get(0).size();
    static int notes_num = NoteMatcher.notesAndPitches.get(2).size();


    /*
     * creation of the note list for the regex.
     * es.
     * notes_anglo_str = "C|D|E|F|G|A|B"
     */
    static String notes_str = String.join("|", NoteMatcher.notesAndPitches.get(2));
    static String notes_anglo_str = String.join("|", NoteMatcher.notesAndPitches.get(3));
    static String all_notes_str = notes_str + "|" + notes_anglo_str;


    /**
     * HashMap with all regex per notation.
     * All key value are defined in EnumMatcher class.
     * 
     * @see EnumMatcher
     */
    public static Map<Integer, String> noteAndPitchesChecker;
    static{
        noteAndPitchesChecker = new HashMap<>();
      
        /*
         * Semitones and Anglo semitones
         */
        noteAndPitchesChecker.put(      EnumMatcher.SEMITONES_PITCH.getValue(),       "^((?i)" + notes_str + ")(?![ac-wyzAC-WYZ])(#{0,6}(?![xb])|b{0,5}(?![#x])|x{0,3}(?![b#])|x{0,2}#{0,1})?(\\d+)?$");
        noteAndPitchesChecker.put(EnumMatcher.SEMITONES_PITCH_ANGLO.getValue(), "^((?i)" + notes_anglo_str + ")(?![ac-wyzAC-WYZ])(#{0,6}(?![xb])|b{0,5}(?![#x])|x{0,3}(?![b#])|x{0,2}#{0,1})?(\\d+)?$");

        /*
         * Number format ---> [0-127] ==> ALL INTS
         */
        noteAndPitchesChecker.put(EnumMatcher.INTS.getValue(), "^\\d+$");

        /* 
         * Helmholtz notation
         */
        noteAndPitchesChecker.put(EnumMatcher.HELMHOLTZ_LOW.getValue(), "^(" + all_notes_str.toLowerCase() + ")('{0,5}(?!,)'{0,1})$");
        noteAndPitchesChecker.put(EnumMatcher.HELMHOLTZ_UP.getValue(), "^(" + all_notes_str.toUpperCase() + ")(,{0,1}(?!'),{0,1})$");

        /*
         * All semitones
         */
        noteAndPitchesChecker.put(EnumMatcher.ALL_PITCHES_AND_NOTE.getValue(),"^((?i)" + notes_str + "|" + notes_anglo_str + ")(?![ac-wyzAC-WYZ])(#{0,6}(?![xb])|b{0,5}(?![#x])|x{0,3}(?![b#])|x{0,2}#{0,1})?(\\d+)?$");


        /*
         * All decimal/int and also negative // NUMBERS_EXPANSION
         */
        noteAndPitchesChecker.put(EnumMatcher.INTS_NEGATIVE.getValue(),"^-?\\d+$");
        noteAndPitchesChecker.put(EnumMatcher.DECIMAL_NEGATIVE.getValue(),"^-?[0-9]\\d*(\\.\\d+)?$");

    }


    /**
     * Get note index from tuple (list_of_notes, note)
     * list_of_notes is a list of notes/semitones, while note is
     * the specific note that we enter to know his position in that list
     * 
     * @param npi   that contains the specific alphabet note (anglo/latin, etc)
     * @param note  is the note/semi
     */
    public static int notePitchIndex(List<String> npi, String note){
        int i=0;
        
        while(npi.size() > i && !npi.get(i).equalsIgnoreCase(note)){
            i+=1;
        }
        
        return i<npi.size() ? i : -1;
    }


    /**
     * Is similar to the function above notePitchIndex(List<>, String),
     * but take an integer instead of a list as argument.
     * The int is the index of the list with the specific notes, taken
     * form notesAndPitches dict.
     * 
     * @param index_dict    index of the dict that contains the specific note
     * @param note          is the note/semitone
     * @throws              NoteHelpConverter_Exception
     *
     * @see NoteMatcher#notesAndPitches
     * @see NoteMatcher#notePitchIndex(List, String)
     */
    public static int notePitchIndex(int index_dict, String note){
        return notePitchIndex(NoteMatcher.notesAndPitches.get(index_dict), note);
    }


    /**
     * Returns the check with Matcher got from specific tuple (regex, pitch)
     * 
     * @param rgx       is the index of the String regex to use in the Pattern compilation
     * @param pitch     is the pitch on which the regex is applied
     * @return          the Matcher create with the regex and the pitch
     */
    static <T> Matcher createMatcher(int rgx, T pitch){
        Pattern pattern = Pattern.compile(noteAndPitchesChecker.get(rgx));
        return pattern.matcher(((Object)pitch).toString());
    }


    /**
     * Checks if the specific tuple (regex, pitch) hits a match
     * 
     * @param rgx       is the index of the String regex to use
     * @param pitch     is the pitch on which the regex is applied
     * @return          the index of the regex, if a match was hit, -1 otherwise
     */
    private static <T> int checkPitch(int rgx, T pitch){
        return createMatcher(rgx, pitch).find() ? rgx : -1;
    }


    /**
     * Search for a pitch regex hit, if exists.
     * 
     * @param start     is the index of the regex from which to start the search
     * @param end       is the index of the regex from which to end the search
     * @param pitch     is the pitch on which the regex is applied
     * @return          the index of the regex, if a match was hit, -1 otherwise
     */
    public static <T> int findMatcher(int start, int end, T pitch){
        int index = -1;

        while(start < end && -1 == index){
            index = checkPitch(start, pitch);
            start+=1;
        }

        return index;
    }


    /**
     * Check if the specific regex hits a match with the given pitch.
     * 
     * @param index     is the index of the regex we want to test
     * @param pitch     is the pitch on which the regex is applied
     * @return          the index of the regex, if a match was hit, -1 otherwise
     * 
     * @see NoteMatcher#findMatcher(int start, int end, T pitch)
     */
    public static <T> int findMatcher(int index, T pitch){
        return findMatcher(index, index+1, pitch);
    }


    /**
     * Search for a pitch regex hit, if exists.
     * It starts from ZERO until the last one.
     * 
     * @param pitch     is the pitch on which the regex is applied
     * @return          the index of the regex, if a match was hit, -1 otherwise
     * 
     * @see NoteMatcher#findMatcher(int start, int end, T pitch)
     */
    public static <T> int findMatcher(T pitch){
        return findMatcher(EnumMatcher.ZERO.getValue(), noteAndPitchesChecker.size(), pitch);
    }


    /**
     * This function check if the octave is given in the correct format
     * 
     * @param octave
     * @return          a boolean: true if it is correct, false otherwise
     * 
     * @see NoteMatcher#findMatcher(T pitch)
     */
    public static <T> boolean checkOctave(T octave){
        int index = NoteMatcher.findMatcher(EnumMatcher.INTS.getValue(), octave);
        if(-1 == index){
            return false;
        }

        int octave_check;

        if(octave instanceof String){
            octave_check = Integer.parseInt((String)octave);
        }else{
            octave_check = (int)octave;
        }

        return (EnumMatcher.ZERO.getValue() <= octave_check);
    }
}
