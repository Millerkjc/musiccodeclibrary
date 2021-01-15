package music_codec_lib;

import music_codec_lib.Exceptions.*;

import java.util.ArrayList;
import java.util.List;

/*
 * Continuous Name Class (CNC) -> NC + octave information
 */
public class CNC{

    /*
     * Init
     */

    /**
     * Init function takes a NC and an octave as parameters and
     * returns the int representation of the CNC.
     * If the pitch inserted is not correct, the function throws an exception
     * 
     * @param nc        could be an int or a string. (Es. 1, "1" or "Re")
     * @param octave    could be an int or a String (Es. 1 or "1")
     * @return          the int representation of the pitch
     * @throws          CNC_Exception
     *
     * @see NoteMatcher#checkOctave(T pitch)
     * @see NC#init(T pitch)
     */
    public static <T> int init(T nc, T octave) throws CNC_Exception{
        int index = NoteMatcher.findMatcher(nc);

        if (-1 == index){
            throw new CNC_Exception("NC incorrect format - " + ((Object)nc).toString());
        }

        if (EnumMatcher.NUMBERS_EXPANSION.getValue() <= index){
            throw new CNC_Exception("Format not supported - " + ((Object)nc).toString());
        }


        if (!NoteMatcher.checkOctave(octave)){
            throw new CNC_Exception("Invalid format - octave - " + octave );
        }

        int nc_int;
        int cnc_octave;


        if(octave instanceof String){
            try{
                cnc_octave = Integer.parseInt((String)octave);
            }catch(Exception e){
                throw new CNC_Exception("Octave incorrect format - " + ((Object)octave).toString());
            }
        }else{
            cnc_octave = (int)octave;
        }


        //if(0 == cnc_octave || EnumMatcher.INTS.getValue() == index){
        if(EnumMatcher.INTS.getValue() == index){
            try{
                nc_int = NC.init(nc);
            }catch(NC_Exception nce){
                throw new CNC_Exception("NC invalid format - " + ((Object)nc).toString());
            }
        }else{
            try{

                int tmp_pitch = NoteHelpConverter.fromPitchesToInt((String)nc + cnc_octave);
                nc_int = NoteHelpConverter.fromSemitonesToNote(tmp_pitch);

                // update octave
                cnc_octave = (int)tmp_pitch/NoteMatcher.semitones_num;

            }catch(NoteHelpConverter_Exception nhc){
                throw new CNC_Exception("Error in pitch or octave - " + ((Object)((String)nc + cnc_octave)).toString());
            }
        }

        return (cnc_octave * NoteMatcher.notes_num) + nc_int;
    }

 
     /**
     * Init function takes a CNC pitch as parameter and
     * returns the int representation of it.
     * If the pitch inserted is not correct, the function throws an exception
     * 
     * @param pitch     could be an int or a string. (Es. 1, "1" or "Re")
     * @return          the int representation of the pitch
     * @throws          CNC_Exception
      *
     * @see NoteMatcher#findMatcher(T pitch)
     * @see CNC#init(T pitch, T octave)
     * @see NoteHelpConverter#getOctaveMatched(T pitch)
     * @see NoteHelpConverter#getNoteMatched(T pitch)
     * @see NoteHelpConverter#fromPitchesToInt(T pitch)
     * @see NoteMatcher#notes_num
     */   
    public static <T> int init(T pitch) throws CNC_Exception{
        int index = NoteMatcher.findMatcher(pitch);
        
        if (-1 == index){
            throw new CNC_Exception("Incorrect format - " + ((Object)pitch).toString());
        }
        
        if (EnumMatcher.NUMBERS_EXPANSION.getValue() <= index){
            throw new CNC_Exception("Format not supported - " + ((Object)pitch).toString());
        }


        if(pitch instanceof String){

            if(EnumMatcher.INTS.getValue() == index){
                int cnc;

                try{
                    cnc = Integer.parseInt((String)pitch);
                }catch(NumberFormatException nfe){
                    throw new CNC_Exception("Incorrect format - " + ((Object)pitch).toString());
                }

                return CNC.init(cnc%NoteMatcher.notes_num, cnc/NoteMatcher.notes_num);
            }


            int octave;
            String alteration;
            String nc_pitch;

            try{

                nc_pitch = NoteHelpConverter.getNoteMatched(pitch);
                alteration = NoteHelpConverter.getAlterationMatched(pitch);
                octave = NoteHelpConverter.getOctaveMatched(pitch);

            }catch(NoteHelpConverter_Exception nhce){
                throw new CNC_Exception("Incorrect format - " + ((Object)pitch).toString());
            }


            return CNC.init(nc_pitch + alteration, octave);
        }

        return CNC.init(((int)pitch)%NoteMatcher.notes_num, ((int)pitch)/NoteMatcher.notes_num);
    }


    /*
     * Methods
     */


    /*
     * TODO: Manage input passed as list of CNC
     * FUTURE RELEASE
     */
    // public static <T> List<Integer> CNClist(List<T> pitchList) throws CNC_Exception{
    //    List<Integer> cncList = new ArrayList<>();
    //    
    //    for(T pitch : pitchList){
    //        if(pitch instanceof List){
    //            throw new CNC_Exception("Invalid format - " + pitch);
    //        }
    //        cncList.add(CNC.init(pitch));
    //    }
    //
    //    return cncList;
    //}



    /**
     * This function, given two CNC, returns the CNCI (CNC interval).
     * 
     * @param cnc_a     the first CNC
     * @param cnc_b     the second CNC
     * @return          the CNCI as an int
     * @throws          CNC_Exception
     *
     * @see CNC#init(T pitch)
     */
    public static <T> int interval(T cnc_a, T cnc_b) throws CNC_Exception{
        return CNC.init(cnc_b) - CNC.init(cnc_a);
    }


    /**
     * This function transposes the CNC forward or backward by transposition
     * 
     * @param cnc               is the CNC value
     * @param transposition     is the value by whitch you want to transpose
     *                          the pitch
     * @return                  the new name class value, as integer
     * @throws                  CNC_Exception
     *
     * @see CNC#init(T pitch)
     */
    public static <T> int transpose(T cnc, T transposition) throws CNC_Exception{
        int index_trans = NoteMatcher.findMatcher(EnumMatcher.INTS_NEGATIVE.getValue(), transposition);
        int transp_int;

        if(EnumMatcher.NOT_FOUND.getValue() == index_trans){
            throw new CNC_Exception("Incorrect transposition format - " + ((Object)transposition).toString());
        }

        if (transposition instanceof String){
            try{
                transp_int = Integer.parseInt((String)transposition);
            }catch(NumberFormatException nfe){
                throw new CNC_Exception("Incorrect transposition format - " + ((Object)transposition).toString());
            }
        }else{
            transp_int = (int)transposition;
        }

        return CNC.init(CNC.init(cnc) + transp_int);
    }


    /*
     * Getter
     */

    /**
     * This function returns the octave value from CNC
     * 
     * @param pitch     is the CNC value
     * @return          the octave value
     * @throws          CNC_Exception
     *
     * @see CNC#init(T pitch)
     * @see NoteMatcher#notes_num
     */
    public static <T> int getOctave(T pitch) throws CNC_Exception{
        return CNC.init(pitch)/NoteMatcher.notes_num;
    }


    /**
     * This function returns the NC value from CNC
     * 
     * @param pitch     is the CNC value
     * @return          the NC value
     * @throws          CNC_Exception
     *
     * @see CNC#init(T pitch)
     * @see NoteMatcher#notes_num
     */
    public static <T> int getNC(T pitch) throws CNC_Exception{
        return CNC.init(pitch)%NoteMatcher.notes_num;
    }

    
    /*
     * Conversion
     */

    /**
     * This function convert CNC to CPC
     * 
     * @param pitch     is the CNC value
     * @return          the CPC value
     * @throws          CNC_Exception
     *
     * @see CNC#getNC(T pitch)
     * @see CNC#getOctave(T pitch)
     * @see CPC#init(T pitch, T octave)
     * @see NoteHelpConverter#fromSemitonesToNote(T pitch)
     */
    public static <T> int convCNCtoCPC(T pitch) throws CNC_Exception, CPC_Exception{
        try{
            return CPC.init(NoteHelpConverter.fromNoteToSemitone(CNC.getNC(pitch)), CNC.getOctave(pitch));
        }catch(NoteHelpConverter_Exception nhce){
                throw new CNC_Exception("Incorrect format - " + ((Object)pitch).toString());
        }
    }


    /**
     * This function convert CNC to OPPC
     * 
     * @param pitch     is the CNC value
     * @return          the OPPC value
     * @throws          CNC_Exception
     *
     * @see CNC#getNC(T pitch)
     * @see CNC#getOctave(T pitch)
     * @see OPPC#init(T pitch, T octave)
     * @see NoteHelpConverter#fromNoteToSemitone(T pitch)
     */
    public static <T> double convCNCtoOPPC(T pitch) throws CNC_Exception, OPPC_Exception{
        try{
            return OPPC.init(NoteHelpConverter.fromNoteToSemitone(CNC.getNC(pitch)), CNC.getOctave(pitch));
        }catch(NoteHelpConverter_Exception nhce){
                throw new CNC_Exception("Incorrect format - " + ((Object)pitch).toString());
        }
    }


    /**
     * This function convert CNC to NC
     * 
     * @param pitch     is the CNC value
     * @return          the NC value
     * @throws          CNC_Exception
     *
     * @see CNC#getNC(T pitch)
     */
    public static <T> int convCNCtoNC(T pitch) throws CNC_Exception{
        return CNC.getNC(pitch);
    }


    /**
     * This function convert CNC to PC
     * 
     * @param pitch     is the CNC value
     * @return          the PC value
     * @throws          CNC_Exception
     *
     * @see CNC#getNC(T pitch)
     * @see NoteHelpConverter#fromSemitonesToNote(T pitch)
     */
    public static <T> int convCNCtoPC(T pitch) throws CNC_Exception{
        try{
            return NoteHelpConverter.fromNoteToSemitone(CNC.getNC(pitch));
        }catch(NoteHelpConverter_Exception nhce){
                throw new CNC_Exception("Incorrect format - " + ((Object)pitch).toString());
        }
    }


    /**
     * toString
     *
     * @throws CNC_Exception
     *
     * @see CNC#init(T pitch)
     */
    public static <T> String toString(T cnc) throws CNC_Exception{
        return "CNC: " + CNC.init(cnc);
    }
}
