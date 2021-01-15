package music_codec_lib;

import music_codec_lib.Exceptions.OPPC_Exception;
import music_codec_lib.Exceptions.CNC_Exception;
import music_codec_lib.Exceptions.CPC_Exception;
import music_codec_lib.Exceptions.NoteHelpConverter_Exception;

import java.math.RoundingMode;
import java.text.DecimalFormat;


public class OPPC{
    
    /*
     * Private, helper function
     */

    /**
     * This function checks OPPC value
     * 
     * @param oppc  the double representation of OPPC
     * @return      true if the OPPC representation is correct,
     *              false otherwise
     * 
     * @see NoteMatcher#semitones_num
     */
    private static boolean checkOPPC(double oppc){
        if (EnumMatcher.ZERO.getValue() > oppc){
            return false;
        }
        return (oppc - (int)oppc)*100 < NoteMatcher.semitones_num;
    }

    // OPPC formatter -> ##.##
    /**
     * This function format correctly the OPPC representation
     * return OPPC formatted as follow -> ##.##
     * 
     * @param oppc  is the OPPC value
     * @return      double representation formatted correctly
     */
    private static <T> double oppcFormatter(T oppc){
        DecimalFormat oppcFormat = new DecimalFormat("##.##");
        oppcFormat.setRoundingMode(RoundingMode.DOWN);

        return Double.parseDouble(oppcFormat.format(oppc));
    }


    /*
     * Init
     */

    /**
     * Init function that takes a pitch and an octave as parameters and
     * returns the int representation of the OPPC.
     * If the pitch insertered is not correct, the function throws an exception
     * 
     * @param pitch     could be an int or a string. (Es. 1, "1" or "Re")
     * @param octave    could be an int or a String (Es. 1 or "1")
     * @return          the int representation of the pitch
     * @throws          OPPC_Exception
     *
     * @see NoteMatcher#findMatcher(T pitch)
     * @see NoteHelpConverter#toSemitones(T pitch);
     */
    public static <T> double init(T pitch, T octave) throws OPPC_Exception{
        int index = NoteMatcher.findMatcher(pitch);

        if(EnumMatcher.NOT_FOUND.getValue() == index){
            throw new OPPC_Exception("Incorrect format - " + ((Object)pitch).toString());
        }


        if(EnumMatcher.DECIMAL_NEGATIVE.getValue() == index ||
           EnumMatcher.INTS_NEGATIVE.getValue() == index){
            throw new OPPC_Exception("Incorrect format - " + ((Object)pitch).toString());
        }


        int octave_oppc;

        if(!NoteMatcher.checkOctave(octave)){
            throw new OPPC_Exception("Invalid octave - " + ((Object)octave).toString());
        }


        if(octave instanceof String){
            try{
                octave_oppc = Integer.parseInt((String)octave);
            }catch(Exception e){
                throw new OPPC_Exception("Octave incorrect format - " + ((Object)octave).toString());
            }
        }else{
            octave_oppc = (int)octave;
        }


        int tmp_int_pitch;
        int tmp_oct;

        if(EnumMatcher.INTS.getValue() == index){

            if(pitch instanceof Integer){
                tmp_int_pitch = (int)pitch;
            }else{
                try{
                    tmp_int_pitch = Integer.parseInt((String)pitch);
                }catch(NumberFormatException nfe){
                    throw new OPPC_Exception("Invalid format - " + ((Object)octave).toString());
                }
            }

            tmp_oct = (int)tmp_int_pitch/NoteMatcher.semitones_num;
            tmp_int_pitch = tmp_int_pitch%NoteMatcher.semitones_num;

        }else{
            try{

                tmp_int_pitch = NoteHelpConverter.fromPitchesToInt(((Object)pitch).toString() + octave);
                tmp_oct = (int)tmp_int_pitch/NoteMatcher.semitones_num;
                tmp_oct += (0 > tmp_int_pitch) ? -1 : 0;
                tmp_int_pitch = tmp_int_pitch%NoteMatcher.semitones_num;

            }catch(NoteHelpConverter_Exception nhce){
                throw new OPPC_Exception("Incorrect pitch format - " + ((Object)pitch).toString());
            }

        }


        if(1>= Math.abs(octave_oppc - tmp_oct)){
            octave_oppc = tmp_oct;
        }


        if(!NoteMatcher.checkOctave(octave_oppc)){
            throw new OPPC_Exception("Pitch invalidates octave - " + ((Object)octave_oppc).toString());
        }

        return OPPC.oppcFormatter(octave_oppc + (double)tmp_int_pitch/100);
    }


     /**
     * Init function that takes a OPPC pitch as parameter and
     * returns the double representation of it.
     * If the pitch insertered is not correct, the function throws an exception
     * 
     * @param pitch     could be an int or a string. (Es. 1.01, "1.01")
     * @return          the int representation of the pitch
     * @throws          OPPC_Exception
     *
     * @see NoteMatcher#findMatcher(T pitch)
     * @see OPPC#oppcFormatter(T oppc)
     * @see OPPC#checkOPPC(double oppc)
     */
    public static <T> double init(T pitch) throws OPPC_Exception{
        int index = NoteMatcher.findMatcher(pitch);

        if(EnumMatcher.NOT_FOUND.getValue() == index){
            throw new OPPC_Exception("Incorrect format - " + ((Object)pitch).toString());
        }

        if(EnumMatcher.INTS.getValue() != index && EnumMatcher.DECIMAL_NEGATIVE.getValue() != index){
            //throw new OPPC_Exception("Incorrect format - " + ((Object)pitch).toString());
            
            int pitch_int;
            int pc, oct;
            
            try{
                pitch_int = NoteHelpConverter.fromPitchesToInt(pitch);
            }catch(NoteHelpConverter_Exception nhce){
                throw new OPPC_Exception("Incorrect format - " + ((Object)pitch).toString());
            }

            if(0 > pitch_int){
                throw new OPPC_Exception("Under octave 0 - " + ((Object)pitch).toString());
            }

            pc = pitch_int%NoteMatcher.semitones_num;
            oct = (int)pitch_int/NoteMatcher.semitones_num;
            
            return OPPC.oppcFormatter((double)oct + ((double)pc)/100);
            
        }

        // IF INT (like 8) -> returns 8.00
        if(EnumMatcher.INTS.getValue() == index){
            if (pitch instanceof String){
                try{
                    return OPPC.oppcFormatter((double)Integer.parseInt((String)pitch));
                }catch(NumberFormatException nfe){
                    throw new OPPC_Exception("Incorrect format - " + ((Object)pitch).toString());
                }
            }

            return OPPC.oppcFormatter((int)pitch);
        }

        // IF DOUBLE
        double tmp_double_pitch;

        if (pitch instanceof String){
            try{
                tmp_double_pitch = Double.parseDouble((String)pitch);
            }catch(NumberFormatException nfe){
                throw new OPPC_Exception("Incorrect format - " + ((Object)pitch).toString());
            }
        }else{
            tmp_double_pitch = (double)pitch;
        }

        if (!OPPC.checkOPPC(tmp_double_pitch)){
            throw new OPPC_Exception("Incorrect format - " + ((Object)pitch).toString());
        }

        return OPPC.oppcFormatter(tmp_double_pitch);
    }



    /*
     * Getter
     */

    /**
     * This function returns the octave value from OPPC
     * 
     * @param pitch     is the OPPC value
     * @return          the octave value
     * @throws          OPPC_Exception
     *
     * @see OPPC#init(T pitch)
     */
    public static <T> int getOctave(T pitch) throws OPPC_Exception{
        return (int)OPPC.init(pitch);
    }

    /**
     * This function returns the PC value from OPPC
     * 
     * @param pitch     is the OPPC value
     * @return          the PC value
     * @throws          OPPC_Exception
     *
     * @see OPPC#init(T pitch)
     */
    public static <T> int getPC(T pitch) throws OPPC_Exception{
        double oppc = OPPC.init(pitch);
        return (int)(Math.round((oppc - (int)oppc)*100));
    }


    /*
     * Conversion
     */

    /**
     * This function convert OPPC to CNC
     * 
     * @param pitch     is the OPPC value
     * @return          the CNC value
     * @throws          OPPC_Exception
     *
     * @see OPPC#getPC(T pitch)
     * @see OPPC#getOctave(T pitch)
     * @see NoteHelpConverter#fromSemitonesToNote(T pitch)
     * @see CNC#init(T pitch)
     */
    public static <T> int convOPPCtoCNC(T pitch) throws  OPPC_Exception, CNC_Exception{
        try{
            return CNC.init(NoteHelpConverter.fromSemitonesToNote(OPPC.getPC(pitch)), OPPC.getOctave(pitch));
        }catch(NoteHelpConverter_Exception nhce){
            throw new OPPC_Exception("Incorrect format - " + ((Object)pitch).toString());
        }
    }


    /**
     * This function convert OPPC to CPC
     * 
     * @param pitch     is the OPPC value
     * @return          the CPC value
     * @throws          OPPC_Exception
     *
     * @see OPPC#getPC(T pitch)
     * @see OPPC#getOctave(T pitch)
     * @see CPC#init(T pitch)
     */
    public static <T> int convOPPCtoCPC(T pitch) throws OPPC_Exception, CPC_Exception{
        return CPC.init(OPPC.getPC(pitch), OPPC.getOctave(pitch));
    }


    /**
     * This function convert OPPC to NC
     * 
     * @param pitch     is the OPPC value
     * @return          the NC value
     * @throws          OPPC_Exception
     *
     * @see OPPC#getPC(T pitch)
     * @see NoteHelpConverter#fromSemitonesToNote(T pitch)
     */
    public static <T> int convOPPCtoNC(T pitch) throws OPPC_Exception{
        try{
            return NoteHelpConverter.fromSemitonesToNote(OPPC.getPC(pitch));
        }catch(NoteHelpConverter_Exception nhce){
            throw new OPPC_Exception("Incorrect format - " + ((Object)pitch).toString());
        }
    }


    /**
     * This function convert OPPC to PC
     * 
     * @param pitch     is the OPPC value
     * @return          the PC value
     * @throws          OPPC_Exception
     *
     * @see OPPC#getPC(T pitch)
     */
    public static <T> int convOPPCtoPC(T pitch) throws OPPC_Exception{
        return OPPC.getPC(pitch);
    }


    /**
     * toString
     *
     * @throws          OPPC_Exception
     *
     * @see OPPC#init(T pitch)
     */
    public static <T> String toString(T oppc) throws OPPC_Exception{
        return "OPPC: " + OPPC.init(oppc);
    }
}
