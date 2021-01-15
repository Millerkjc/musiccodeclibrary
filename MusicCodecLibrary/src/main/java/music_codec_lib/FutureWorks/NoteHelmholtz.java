// package music_codec;
// 
// import music_codec.Exceptions.NoteHelmholtzException;
// import music_codec.Exceptions.NoteHelpConverter_Exception;
// import music_codec.NoteMatcher;
// import music_codec.NoteHelpConverter;
// import music_codec.EnumMatcher;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;
// 
// 
// public class NoteHelmholtz{
// 
//     private String pitch;
//     private int hlmtz = 0; // To resolve misunderstanding with Anglo notation.
//     
//     /*
//      * Constructors
//      */
//     public NoteHelmholtz(String pitch) throws NoteHelmholtzException, NoteHelpConverter_Exception{
//         int index = NoteMatcher.findMatcher(pitch);
//         
//         if(-1 == index){
//             throw new NoteHelmholtzException("Invalid notation: " + pitch);
//         }
// 
//         this.pitch = pitch;
//         this.convToHelmholtz();
//     }
// 
//     public NoteHelmholtz(char pitch, int octave) throws NoteHelmholtzException, NoteHelpConverter_Exception{
//         this(Character.toString(pitch) + octave);
//     }
// 
//     public NoteHelmholtz(String pitch, int octave) throws NoteHelmholtzException, NoteHelpConverter_Exception{
//         this(pitch + octave);
//     }
// 
// 
// 
// 
//     /*
//      * 
//      * CONVERSIONS
//      * 
//      */ 
//      
//     /*
//      * Change notation to Helmholtz
//      */
//     public void convToHelmholtz() throws NoteHelpConverter_Exception{
//         if(-1 == NoteMatcher.findMatcher(EnumMatcher.HELMHOLTZ_LOW.getValue(), EnumMatcher.HELMHOLTZ_ALL.getValue(), this.pitch) && 0 == this.hlmtz){
//             String pitch = (getOctave()<3) ? NoteHelpConverter.getNoteMatched(this.pitch).toUpperCase() : NoteHelpConverter.getNoteMatched(this.pitch).toLowerCase();
//             
//             this.pitch = pitch + octaveHlmz();
//             this.hlmtz = 1;
//         }
//     }
// 
// 
//     /*
//      * Change notation to Standard
//      */
//     public void convToPitchOct() throws NoteHelpConverter_Exception{
//         if(-1 != NoteMatcher.findMatcher(EnumMatcher.SEMITONES_PITCH.getValue(), this.pitch)){
//             return;
//         }
//         
//         this.pitch = notationStandard();
//         this.hlmtz = 0;
//     }
// 
//     /*
//      * Change notation to Anglo
//      */
//     public void convToPitchOctAnglo() throws NoteHelpConverter_Exception{
//         if(-1 != NoteMatcher.findMatcher(EnumMatcher.SEMITONES_PITCH_ANGLO.getValue(), this.pitch) && 0 == this.hlmtz){
//             return;
//         }
// 
//         this.pitch = notationAngl();
//         this.hlmtz = 0;
//     }
// 
//     /*
//      * Change notation to specific dict ("conversion")
//      */
//     private String changeNotation(int conversion) throws NoteHelpConverter_Exception{
// 
//         int index = NoteMatcher.findMatcher(NoteHelpConverter.getNoteMatched(this.pitch));
// 
//         if(conversion == index){
//             return NoteHelpConverter.getNoteFromDict(this.pitch) + getOctave();
//         }
//         return NoteHelpConverter.standardAngloConverter(NoteHelpConverter.getNoteMatched(this.pitch)) + getOctave();
//     }
// 
//     /*
//      * Notation Standard: [Do, Re, .. Si] 
//      */
//     private String notationStandard() throws NoteHelpConverter_Exception{
//         this.hlmtz = 0;
//         return changeNotation(EnumMatcher.SEMITONES_PITCH.getValue());
//     }
// 
// 
//     /*
//      * Notation Anglo: [C, D, .. B] 
//      */
//     private String notationAngl() throws NoteHelpConverter_Exception{
//         if (1 == this.hlmtz){
//             notationStandard();
//         }
//         this.hlmtz = 0;
//         return changeNotation(EnumMatcher.SEMITONES_PITCH_ANGLO.getValue());
//     }
// 
// 
// 
// 
// 
// 
// 
// 
//     /*
//      * Returns the octave
//      */
//     public int getOctave() throws NoteHelpConverter_Exception{
//         if (-1 == NoteMatcher.findMatcher(EnumMatcher.HELMHOLTZ_LOW.getValue(), EnumMatcher.HELMHOLTZ_ALL.getValue(), this.pitch)){
//             return NoteHelpConverter.getOctaveMatched(this.pitch);
//         }
//         
//         long comma = (this.pitch).chars().filter(ch -> ch == ',').count();
//         int upper = ((this.pitch).charAt(0) == Character.toUpperCase((this.pitch).charAt(0))) ? 1 : 2;
//         long quote = (this.pitch).chars().filter(ch -> ch == '\'').count();
// 
//         return 2 - (int)comma + upper + (int)quote - 1;
//     }
// 
// 
//     /*
//      * Returns the octave in Helmholtz notation
//      */
//     public String octaveHlmz() throws NoteHelpConverter_Exception{
//         int index = NoteMatcher.findMatcher(EnumMatcher.HELMHOLTZ_LOW.getValue(), EnumMatcher.HELMHOLTZ_ALL.getValue(), this.pitch);
//         
//         
//         if(-1 != index){
//             return this.pitch.substring(NoteHelpConverter.getNoteMatched((this.pitch).toLowerCase()).length());
//         }
//         
//         
//         int octave = NoteHelpConverter.getOctaveMatched(this.pitch);
//         String octaveHlmz = "";
//         
//         if (octave<3){
//             for (int i=0; i<(2-octave); i++){
//                 octaveHlmz +=',';
//             }
//         }else{
//             for (int i=0; i<(octave-3); i++){
//                 octaveHlmz +='\'';
//             }
//         }
// 
//         return octaveHlmz;
//     }
//     
// 
//     public String toString(){
//         return this.pitch;
//     }
//     
// }
