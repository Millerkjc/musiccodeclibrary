package music_codec_lib;

import music_codec_lib.Exceptions.NoteHelpConverter_Exception;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;

public class NoteHelpConverter{

    /*
    private static final String[] alteration = {"#", "b", "x"};
    public static final String[] alteration_symbols()
    {
        return alteration.clone();
    }
    */


    /**
     * HashMap that maps semitones to notes
     */
    public static HashMap<Integer, Integer> semitones_note;
    static{
        semitones_note = new HashMap<>();
        semitones_note.put( 0, 0); // Do
        semitones_note.put( 1, 0); // Do#
        semitones_note.put( 2, 1); // Re
        semitones_note.put( 3, 1); // Re#
        semitones_note.put( 4, 2); // Mi
        semitones_note.put( 5, 3); // Fa
        semitones_note.put( 6, 3); // Fa#
        semitones_note.put( 7, 4); // Sol
        semitones_note.put( 8, 4); // Sol#
        semitones_note.put( 9, 5); // La
        semitones_note.put(10, 5); // La#
        semitones_note.put(11, 6); // Si
    }


    /**
     * Semitone alterations and their values
     */
    public static HashMap<String, Integer> alteration_value;
    static{
        alteration_value = new HashMap<>();
        alteration_value.put("#", 1);
        alteration_value.put("b", -1);
        alteration_value.put("x", 2);
    }


    /*
     * 
     * RETRIEVE PITCHES/NOTES INFORMATION THROUGH REGEX
     * 
     */


    /**
     * format correctly the groups -> see fromMatchToGroups(...)
     * 
     * @param matcher       the object contains the results of the regex on the pitch
     * @param group_index   specify the group you want to take in return
     * @param default_value specify the group value in case any error occurs
     * @return              the group requested
     *
     * @see NoteHelpConverter#fromMatchToGroups(int, Object)
     */
    private static String getGroup(Matcher matcher, int group_index, String default_value){
        try{
            if (null == matcher.group(group_index)){
                throw new Exception();
            }
            return matcher.group(group_index);
        }catch(Exception e){
            return default_value;
        }
    }

    
    /**
     * This function applies the regex at the "rgx" index and
     * extracts the NOTE - ALTERATION - OCTAVE groups from the pitch.
     * 
     * return groups -> NOTE - ALTERACTION - OCTAVE
     * 
     * @param rgx     is the index of the regex we want to test
     * @param pitch   is the pitch on which the regex is applied
     * @return        an ArrayList with 3 entry: NOTE, ALTERATION AND OCTAVE
     * @throws        NoteHelpConverter_Exception
     *
     * @see NoteMatcher#createMatcher(int rgx, T pitch)
     * @see NoteMatcher#getGroup(Matcher matcher, int group_index, String default_value)
     */
    public static <T> ArrayList<String> fromMatchToGroups(int rgx, T pitch) throws NoteHelpConverter_Exception{
        Matcher matcher = NoteMatcher.createMatcher(rgx, pitch);
        ArrayList<String> groups = new ArrayList<>();
        ArrayList<String> default_value =
                    new ArrayList<>(
                            Arrays.asList(
                                    "not_note", // note
                                            "", // alteraction
                                           "0" // octave -> from -1 to 0 !!!!!
                            )
                        );
        
        
        if(!matcher.find()){
            throw new NoteHelpConverter_Exception("Matcher error");
        }


        // return the number as string
        if(EnumMatcher.INTS.getValue() == rgx ||
           EnumMatcher.INTS_NEGATIVE.getValue() == rgx ||
           EnumMatcher.DECIMAL_NEGATIVE.getValue() == rgx){

            return new ArrayList<>(Arrays.asList(String.valueOf(pitch)));
        }

        for(int i=1; i<4; i++){
            groups.add(NoteHelpConverter.getGroup(matcher, i, default_value.get(i-1)));
        }
        
        return groups;
    }


    /**
     * This function returns the input note matched
     * 
     * @param pitch     is the pitch that the user insert
     * @return          the note extracted from the pitch, if no error
     *                  occurs, "not_note" otherwise
     * @throws          NoteHelpConverter_Exception
     *
     * @see NoteHelpConverter#fromMatchToGroups(int rgx, T pitch)
     */
    public static <T> String getNoteMatched(T pitch) throws NoteHelpConverter_Exception{
        int index = NoteMatcher.findMatcher(pitch);

        if(EnumMatcher.NOT_FOUND.getValue() == index){
            throw new NoteHelpConverter_Exception("Error retrieving note");
        }

        try{
            return NoteHelpConverter.fromMatchToGroups(index, pitch).get(0);
        }catch(Exception e){
            throw new NoteHelpConverter_Exception("Error retrieving note");
        }
    }


    /**
     * This function returns the input alteration matched
     * 
     * @param pitch     is the pitch that the user insert
     * @return          the alteration extracted from the pitch, if no error
     *                  occurs, "" otherwise
     * @throws          NoteHelpConverter_Exception
     *
     * @see NoteHelpConverter#fromMatchToGroups(int rgx, T pitch)
     */
    public static <T> String getAlterationMatched(T pitch) throws NoteHelpConverter_Exception{
        int index = NoteMatcher.findMatcher(pitch);

        if(EnumMatcher.NOT_FOUND.getValue() == index){
            throw new NoteHelpConverter_Exception("Error retrieving alteration");
        }

        // no alteration
        if(EnumMatcher.INTS.getValue() == index ||
           EnumMatcher.INTS_NEGATIVE.getValue() == index ||
           EnumMatcher.DECIMAL_NEGATIVE.getValue() == index){

            return "";
        }

        try{
            return NoteHelpConverter.fromMatchToGroups(EnumMatcher.ALL_PITCHES_AND_NOTE.getValue(), pitch).get(1);
        }catch(Exception e){
            throw new NoteHelpConverter_Exception("Error retrieving alteration");
        }
    }


    /**
     * This function returns the input octave matched
     * 
     * @param pitch     is the pitch that the user insert
     * @return          the octave extracted from the pitch, if no error
     *                  occurs, 0 otherwise
     * @throws          NoteHelpConverter_Exception
     *
     * @see NoteHelpConverter#fromMatchToGroups(int rgx, T pitch)
     */
    public static <T> int getOctaveMatched(T pitch) throws NoteHelpConverter_Exception{
        int index = NoteMatcher.findMatcher(pitch);

        if(EnumMatcher.NOT_FOUND.getValue() == index){
            throw new NoteHelpConverter_Exception("Error retrieving octave");
        }

        // No generic method to extract octave from int
        if(EnumMatcher.INTS.getValue() == index ||
           EnumMatcher.INTS_NEGATIVE.getValue() == index ||
           EnumMatcher.DECIMAL_NEGATIVE.getValue() == index){

            throw new NoteHelpConverter_Exception("Error retrieving octave");
        }

        try{
            return Integer.parseInt(NoteHelpConverter.fromMatchToGroups(EnumMatcher.ALL_PITCHES_AND_NOTE.getValue(), pitch).get(2));
        }catch(Exception e){
            throw new NoteHelpConverter_Exception("Error retrieving octave");
        }
    }



    /**
     * This function returns a pitch, with correct characters case (es. note='dO#2' -> 'Do#2').
     * If "converter" argument is set to 1, it changes the notes dict
     * from standard to anglo and vice versa
     * 
     * @param pitch         is the pitch inserted by the user. It could be a String or an Integer
     * @param converter     could be 0 or 1. If set at 1, change the dict of the note
     *                      Anglo -> Latin or Latin -> Anglo
     * @throws              NoteHelpConverter_Exception
     *
     * @return              the String pitch value
     */
    private static <T> String getNoteFromDict(T pitch, int converter) throws NoteHelpConverter_Exception{

        if (pitch instanceof Integer){
            if(NoteMatcher.semitones_num < (int)pitch){
                throw new NoteHelpConverter_Exception("Error retrieving pitch");
            }

            // From semitones to note
            //return NoteMatcher.notesAndPitches.get(EnumMatcher.SEMITONES_PITCH.getValue()).get(NoteHelpConverter.fromNoteToSemitone(pitch));
            return NoteMatcher.notesAndPitches.get(EnumMatcher.SEMITONES_PITCH.getValue()).get(NoteHelpConverter.toSemitones(pitch));
        }

        String note_match; //= NoteHelpConverter.getNoteMatched(note);
        String alteration_match; // = NoteHelpConverter.getAlterationMatched(note);
        int octave_match; // = NoteHelpConverter.getOctaveMatched(note);

        try{

            note_match = NoteHelpConverter.getNoteMatched(pitch);
            alteration_match = NoteHelpConverter.getAlterationMatched(pitch);
            octave_match = NoteHelpConverter.getOctaveMatched(pitch);

        }catch(NoteHelpConverter_Exception nhce){
            throw new NoteHelpConverter_Exception("Error converting note - getNoteFromDict");
        }
        
        int index = NoteMatcher.findMatcher(note_match);
        String note_converted = NoteMatcher.notesAndPitches.get((index + converter)%2).get(NoteMatcher.notePitchIndex(index, note_match));

        if (!((String)pitch).contains(String.valueOf(octave_match))){
            return note_converted + alteration_match;
        }
        
        return note_converted + alteration_match + octave_match;
    }


    /**
     * This function converts Standard latin pitch to anglo and vice versa
     * 
     * @param pitch     is the pitch inserted by the user. It could be a String or an Integer
     * @return          the pitch wit the correct case and in the different
     *                  dictionary notation
     * @throws          NoteHelpConverter_Exception
     *
     * @see NoteHelpConverter#getNoteFromDict(T pitch, int converter)
     */ 
    public static <T> String standardAngloConverter(T pitch) throws NoteHelpConverter_Exception{
        return NoteHelpConverter.getNoteFromDict(pitch, 1);
    }


    /**
     * This function returns the pitch in the correct characters case
     * 
     * @param note     is the pitch inserted by the user. It could be a String or an Integer
     * @return          the pitch wit the correct case
     * @throws          NoteHelpConverter_Exception
     *
     * @see NoteHelpConverter#getNoteFromDict(T pitch, int converter)
     */ 
    public static <T> String getNoteFromDict(T note) throws NoteHelpConverter_Exception{
        return NoteHelpConverter.getNoteFromDict(note, 0);
    }



    /*
     * 
     * PITCH MANIPULATION
     * 
     */

    /**
     * This function converts a Pitches/Notes into an integer.
     * This procedure works also with alterations.
     * 
     * @param pitch     that the user wants to convert
     * @return          the integer pitch value
     * @throws          NoteHelpConverter_Exception
     */
    public static <T> int fromPitchesToInt(T pitch) throws NoteHelpConverter_Exception{
        int index = NoteMatcher.findMatcher(pitch);

        if(-1 == index){
            throw new NoteHelpConverter_Exception("Invalid format " + ((Object)pitch).toString());
        }


        if (pitch instanceof String){

            if (EnumMatcher.INTS.getValue() == index){                
                try{
                    return Integer.parseInt((String)pitch);
                }catch(NumberFormatException nfe){
                    throw new NoteHelpConverter_Exception("Invalid format " + (String)pitch);
                }
            }
            
            // set groups and the index of the pitch regex match
            ArrayList<String> groups = NoteHelpConverter.fromMatchToGroups(EnumMatcher.ALL_PITCHES_AND_NOTE.getValue(), pitch);
            int note_index = NoteMatcher.findMatcher(EnumMatcher.ZERO.getValue(), EnumMatcher.SEMITONES.getValue(), groups.get(0));
            int pc_int=0, alteration_int=0, octave_int = 0;

            
            // Get Pitch Class value
            pc_int = NoteMatcher.notePitchIndex(note_index, groups.get(0));


            // Get Alteration semitones value
            if(0 != groups.get(1).length()){
                try{
                    for(String alteration : groups.get(1).split("")){
                        alteration_int += alteration_value.get(alteration);
                    }
                }catch(Exception e){
                    throw new NoteHelpConverter_Exception("Invalid format alteration " + ((Object)pitch).toString());
                }
            }
            

            // Get octave value
            try{
                octave_int = Integer.parseInt(groups.get(2));
                if (0 > octave_int){
                    throw new Exception();
                }
                
            }catch(Exception e){
                throw new NoteHelpConverter_Exception("Invalid format octave " + ((Object)pitch).toString());
            }


            // Result
            int partial_result = pc_int + alteration_int;


            // fix octave and semitones
            if (partial_result >= NoteMatcher.semitones_num){
                partial_result%=NoteMatcher.semitones_num;
                octave_int+=1;
            }

            if (partial_result < EnumMatcher.ZERO.getValue()){
                partial_result= (partial_result + NoteMatcher.semitones_num);
                octave_int-=1;
            }


            // if the octave is less than 0
            if(EnumMatcher.ZERO.getValue() > octave_int){
                return -1;
            }


            return partial_result + (octave_int*12);
        }


        return (int)pitch;
    }



    /**
     * This function converts notes, pitches or ints to semitone notation => [0..11]
     * 
     * @param pitch     that the user wants to convert
     * @throws          NoteHelpConverter_Exception
     *
     * @see NoteHelpConverter#fromPitchesToInt(T pitch)
     */
    public static <T> int toSemitones(T pitch) throws NoteHelpConverter_Exception{
        try{
            return (NoteHelpConverter.fromPitchesToInt(pitch))%NoteMatcher.semitones_num;
        }catch(NumberFormatException nfe){
            throw new NoteHelpConverter_Exception("Invalid format " + ((Object)pitch).toString());
        }
    }


    /**
     * This function converts notes, pitches or ints in semitone format [0..11]
     * to note notation => [0..6]
     * 
     * @param pitch     that the user wants to convert
     * @throws          NoteHelpConverter_Exception
     *
     * @see NoteHelpConverter#fromPitchesToInt(T pitch)
     */
    public static <T> int fromSemitonesToNote(T pitch) throws NoteHelpConverter_Exception{
        try{
            return NoteHelpConverter.semitones_note.get(NoteHelpConverter.toSemitones(pitch));
        }catch(Exception nfe){
            throw new NoteHelpConverter_Exception("Invalid format " + ((Object)pitch).toString());
        }
    }


    /**
     * This function converts notes, pitches or ints in note format [0..6]
     * to semitone notation => [0,2,4,5,7,9,11]
     * 
     * @param pitch     that the user wants to convert
     * @throws          NoteHelpConverter_Exception
     *
     * @see NoteHelpConverter#fromPitchesToInt(T pitch)
     */
    public static <T> int fromNoteToSemitone(T pitch) throws NoteHelpConverter_Exception{
        
        ArrayList<Integer> noteToSemitone = new ArrayList<Integer>(Arrays.asList(0,2,4,5,7,9,11));
        int index = NoteMatcher.findMatcher(pitch);
        
        if(EnumMatcher.NOT_FOUND.getValue() == index || 
           EnumMatcher.INTS.getValue() < index){
            throw new NoteHelpConverter_Exception("Invalid format " + ((Object)pitch).toString());
        }


        int note_index;

        if(EnumMatcher.INTS.getValue() == index){
            int note_tmp;
            
            if(pitch instanceof String){
                try{
                    note_index = Integer.parseInt((String)pitch);
                }catch(NumberFormatException nfe){
                    throw new NoteHelpConverter_Exception("Invalid format " + ((Object)pitch).toString());
                }
            }else{
                note_index = (int)pitch;
            }

            if(NoteMatcher.notes_num <= note_index){
                throw new NoteHelpConverter_Exception("Invalid format, note range [0..6] - " + ((Object)pitch).toString());
            }

        }else{

            String note_alteration = NoteHelpConverter.getAlterationMatched(pitch);
            if(!note_alteration.equals("")){
                throw new NoteHelpConverter_Exception("Invalid format, with alteration - " + ((Object)pitch).toString());
            }

            String note_matched = NoteHelpConverter.getNoteMatched(pitch);
            note_index = NoteHelpConverter.semitones_note.get(NoteMatcher.notePitchIndex(index%2, note_matched));

        }

        try{
            return noteToSemitone.get(note_index);
        }catch(NumberFormatException nfe){
            throw new NoteHelpConverter_Exception("Invalid format " + ((Object)pitch).toString());
        }
    }
}
