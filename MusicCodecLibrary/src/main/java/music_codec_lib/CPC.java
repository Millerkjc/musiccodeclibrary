package music_codec_lib;

import music_codec_lib.Exceptions.CPC_Exception;
import music_codec_lib.Exceptions.CNC_Exception;
import music_codec_lib.Exceptions.OPPC_Exception;
import music_codec_lib.Exceptions.PC_Exception;
import music_codec_lib.Exceptions.NoteHelpConverter_Exception;
import java.util.ArrayList;
import java.util.List;

/*
 * Continuous Pitch Class (CPC) -> PC + octave information
 */
public class CPC{
    
    
    /*
     * Init
     */

    /**
     * Init function takes a PC and an octave as parameters and
     * returns the int representation of the CPC.
     * If the pitch insertered is not correct, the function throws an exception
     * 
     * @param pc        could be an int or a string. (Es. 1, "1" or "Re")
     * @param octave    could be an int or a String (Es. 1 or "1")
     * @return          the int representation of the pitch
     * @throws          CPC_Exception
     *
     * @see NoteMatcher#findMatcher(T pitch);
     * @see NoteMatcher#checkOctave(T pitch)
     * @see PC#init(T pitch)
     */
    public static <T> int init(T pc, T octave) throws CPC_Exception{
        int index = NoteMatcher.findMatcher(pc);

        if(EnumMatcher.NOT_FOUND.getValue() == index){
            throw new CPC_Exception("Invalid format - PC = " + ((Object)pc).toString());
        }

        if (EnumMatcher.NUMBERS_EXPANSION.getValue() <= index){
            throw new CPC_Exception("Format not supported - " + ((Object)pc).toString());
        }
        
        if (!NoteMatcher.checkOctave(octave)){
            throw new CPC_Exception("Invalid format - octave = " + octave );
        }

        int pc_int;
        int cpc_octave;

        if(octave instanceof String){
            try{
                cpc_octave = Integer.parseInt((String)octave);
            }catch(Exception e){
                throw new CPC_Exception("Octave incorrect format: " + ((Object)octave).toString());
            }
        }else{
            cpc_octave = (int)octave;
        }


        if(EnumMatcher.INTS.getValue() == index){
            try{
                pc_int = PC.init(pc);
            }catch(PC_Exception pce){
                throw new CPC_Exception("PC invalid format - " + ((Object)pc).toString());
            }
        }else{
            try{

                int tmp_pitch = NoteHelpConverter.fromPitchesToInt((String)pc + cpc_octave);
                pc_int = NoteHelpConverter.toSemitones(tmp_pitch);

                // update octave
                cpc_octave = (int)tmp_pitch/NoteMatcher.semitones_num;

            }catch(NoteHelpConverter_Exception nhc){
                throw new CPC_Exception("Error in pitch or octave - " + ((Object)((String)pc + cpc_octave)).toString());
            }
        }

        return (cpc_octave * NoteMatcher.semitones_num) + pc_int;
    }


     /**
     * Init function takes a CPC pitch as parameter and
     * returns the int representation of it.
     * If the pitch insertered is not correct, the function throws an exception
     * 
     * @param pitch     could be an int or a string. (Es. 1, "1" or "Re")
     * @return          the int representation of the pitch
     * @throws          CPC_Exception
     *
     * @see NoteMatcher#findMatcher(T pitch)
     * @see CPC#init(T pitch, T octave)
     * @see NoteHelpConverter#getOctaveMatched(T pitch)
     * @see NoteHelpConverter#getNoteMatched(T pitch)
     * @see NoteHelpConverter#fromPitchesToInt(T pitch)
     * @see NoteMatcher#semitones_num
     */
    public static <T> int init(T pitch) throws CPC_Exception{
        int index = NoteMatcher.findMatcher(pitch);

        if (-1 == index){
            throw new CPC_Exception("Incorrect format - " + ((Object)pitch).toString());
        }
        
        if (EnumMatcher.NUMBERS_EXPANSION.getValue() <= index){
            throw new CPC_Exception("Format not supported - " + ((Object)pitch).toString());
        }


        if (pitch instanceof String){
            int cpc;

            if(EnumMatcher.INTS.getValue() == index){
                try{
                    cpc = Integer.parseInt((String)pitch);
                }catch(NumberFormatException nfe){
                    throw new CPC_Exception("Incorrect format - " + ((Object)pitch).toString());
                }
                
                return init(cpc%NoteMatcher.semitones_num, cpc/NoteMatcher.semitones_num);
            }

            try{
                cpc = NoteHelpConverter.fromPitchesToInt(pitch);
            }catch(NoteHelpConverter_Exception nhce){
                throw new CPC_Exception("Incorrect format - " + ((Object)pitch).toString());
            }
            
            if(-1 == cpc){
                throw new CPC_Exception("Under octave 0 - " + ((Object)pitch).toString());
            }

            return CPC.init(cpc%NoteMatcher.semitones_num, cpc/NoteMatcher.semitones_num);

        }

        return CPC.init(((int)pitch)%NoteMatcher.semitones_num, ((int)pitch)/NoteMatcher.semitones_num);
    }


    /*
     * Methods
     */



    /*
     * TODO: Manage input passed as list of CPC
     * FUTURE RELEASE
     */
    //public static <T> List<Integer> CPClist(List<T> pitchList) throws CPC_Exception{
    //    List<Integer> cpcList = new ArrayList<>();
    //    
    //    for(T pitch : pitchList){
    //        if(pitch instanceof List){
    //            throw new CPC_Exception("Invalid format " + pitch);
    //        }
    //        cpcList.add(CPC.init(pitch));
    //    }
    //    
    //    return cpcList;
    //}



    /**
     * This function, given two cnc, returns the CPCI (CPC interval).
     * 
     * @param cpc_a     the first CPC
     * @param cpc_b     the second CPC
     * @return          the CPCI as an int
     * @throws          CPC_Exception
     *
     * @see CPC#init(T pitch)
     */
    public static <T> int interval(T cpc_a, T cpc_b) throws CPC_Exception{
        return CPC.init(cpc_b) - CPC.init(cpc_a);
    }


    /**
     * This function transposes the CPC forward or backward by transposition
     * 
     * @param cpc               is the cpc value
     * @param transposition     is the value by which you want to transpose
     *                          the pitch
     * @return                  the new name class value, as integer
     * 
     * @see CNC#init(T pitch)
     */
    public static <T> int transpose(T cpc, T transposition) throws CPC_Exception{
        int index_trans = NoteMatcher.findMatcher(EnumMatcher.INTS_NEGATIVE.getValue(), transposition);
        int transp_int;

        if(EnumMatcher.NOT_FOUND.getValue() == index_trans){
            throw new CPC_Exception("Incorrect transposition format - " + ((Object)transposition).toString());
        }

        if (transposition instanceof String){
            try{
                transp_int = Integer.parseInt((String)transposition);
            }catch(NumberFormatException nfe){
                throw new CPC_Exception("Incorrect transposition format - " + ((Object)transposition).toString());
            }
        }else{
            transp_int = (int)transposition;
        }

        return CPC.init(CPC.init(cpc) + transp_int);
    }



    /*
     * Getter
     */

    /**
     * This function returns the octave value from CPC
     * 
     * @param pitch     is the CPC value
     * @return          the octave value
     * @throws          CPC_Exception
     *
     * @see CPC#init(T pitch)
     * @see NoteMatcher#semitones_num
     */
    public static <T> int getOctave(T pitch) throws CPC_Exception{
        return (CPC.init(pitch))/NoteMatcher.semitones_num;
    }
 
 
     /**
     * This function returns the PC value from CPC
     * 
     * @param pitch     is the CPC value
     * @return          the PC value
     * @throws          CPC_Exception
     *
     * @see CPC#init(T pitch)
     * @see NoteMatcher#semitones_num
     */   
    public static <T> int getPC(T pitch) throws CPC_Exception{
        return (CPC.init(pitch))%NoteMatcher.semitones_num;
    }


    /*
     * Conversion
     */


    /**
     * This function convert CPC to CNC
     * 
     * @param pitch     is the CPC value
     * @return          the CNC value
     * @throws          CPC_Exception
     *
     * @see CPC#getPC(T pitch)
     * @see CPC#getOctave(T pitch)
     * @see CNC#init(T pitch, T octave)
     * @see NoteHelpConverter#fromSemitonesToNote(T pitch)
     */
    public static <T> int convCPCtoCNC(T pitch) throws CNC_Exception, CPC_Exception{
        try{
            return CNC.init(NoteHelpConverter.fromSemitonesToNote(CPC.getPC(pitch)), CPC.getOctave(pitch));
        }catch(NoteHelpConverter_Exception nhce){
            throw new CPC_Exception("Incorrect format - " + pitch);
        }
    }


    /**
     * This function convert CPC to OPPC
     * 
     * @param pitch     is the CPC value
     * @return          the OPPC value
     * @throws          CPC_Exception
     * 
     * @see CNC#getNC(T pitch)
     * @see CNC#getOctave(T pitch)
     * @see OPPC#init(T pitch, T octave)
     */
    public static <T> double convCPCtoOPPC(T pitch) throws CPC_Exception, OPPC_Exception{
        return OPPC.init(CPC.getPC(pitch), CPC.getOctave(pitch));
    }


    /**
     * This function convert CPC to NC
     * 
     * @param pitch     is the CPC value
     * @return          the NC value
     * @throws          CPC_Exception
     * 
     * @see CPC#getPC(T pitch)
     * @see NoteHelpConverter#fromSemitonesToNote(T pitch);
     */
    public static <T> int convCPCtoNC(T pitch) throws CPC_Exception{
        try{
            return NoteHelpConverter.fromSemitonesToNote(CPC.getPC(pitch));
        }catch(NoteHelpConverter_Exception nhce){
            throw new CPC_Exception("Incorrect format - " + pitch);
        }
    }


    /**
     * This function convert CPC to PC
     * 
     * @param pitch     is the CPC value
     * @return          the PC value
     * @throws          CPC_Exception
     *
     * @see CPC#getPC(T pitch)
     */
    public static <T> int convCPCtoPC(T pitch) throws CPC_Exception{
        return CPC.getPC(pitch);
    }


    /**
     * toString
     *
     * @throws          CPC_Exception
     *
     * @see CPC#init(T pitch)
     */
    public static <T> String toString(T cpc) throws CPC_Exception{
        return "CPC: " + CPC.init(cpc);
    }

}
