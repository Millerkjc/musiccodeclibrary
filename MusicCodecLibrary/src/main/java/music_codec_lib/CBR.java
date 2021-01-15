package music_codec_lib;

import music_codec_lib.Exceptions.NoteHelpConverter_Exception;
import music_codec_lib.Exceptions.BR_Exception;
import music_codec_lib.Exceptions.CBR_Exception;

/*
 * CBR - Continuous Binomial Representation
 */
public class CBR{


    /*
     * Init
     */

    /**
     * Init function takes a pitch and an octave as parameters and
     * returns the int representation of the CPC.
     * If the pitch inserted is not correct, the function throws an exception
     * 
     * @param pitch     could be an int or a string. (Es. 1, "1" or "Re")
     * @param octave    could be an int or a String (Es. 1 or "1")
     * @return          the CBR representation
     * @throws          CBR_Exception
     *
     * @see NoteMatcher#checkOctave(T pitch)
     * @see PC#init(T pitch)
     */
    public static <T> int init(T pitch, T octave) throws CBR_Exception{
        int index = NoteMatcher.findMatcher(pitch);


        if(EnumMatcher.NOT_FOUND.getValue() == index){
            throw new CBR_Exception("Invalid format - PC = " + ((Object)pitch).toString());
        }

        if (EnumMatcher.NUMBERS_EXPANSION.getValue() <= index){
            throw new CBR_Exception("Format not supported - " + ((Object)pitch).toString());
        }

        if (!NoteMatcher.checkOctave(octave)){
            throw new CBR_Exception("Invalid format: octave = " + octave);
        }


        int br_int;
        int cbr_octave;

        if(octave instanceof String){
            try{
                cbr_octave = Integer.parseInt((String)octave);
            }catch(Exception e){
                throw new CBR_Exception("Octave incorrect format - " + ((Object)octave).toString());
            }
        }else{
            cbr_octave = (int)octave;
        }


        if(EnumMatcher.INTS.getValue() == index){
            try{
                br_int = BR.init(pitch);
            }catch(BR_Exception pce){
                throw new CBR_Exception("BR invalid format - " + ((Object)pitch).toString());
            }
        }else{
            try{

                int tmp_pitch = NoteHelpConverter.fromPitchesToInt((String)pitch + cbr_octave);
                br_int = NoteHelpConverter.toSemitones(tmp_pitch);

                if(-1 == br_int){
                    throw new Exception();
                }

                // update octave
                cbr_octave = (int)tmp_pitch/NoteMatcher.semitones_num;
                br_int = BR.init(pitch);

            }catch(Exception nhc){
                throw new CBR_Exception("Error in pitch or octave - " + ((Object)((String)pitch + cbr_octave)).toString());
            }
        }

        return cbr_octave*1000 + br_int;
    }



    /**
     * Init function takes the pitch as parameter and
     * returns CBR.
     * If the pitch inserted is not correct, the function throws an exception
     * 
     * @param pitch     the pitch value
     * @throws          CBR_Exception
     *
     * @see NoteMatcher#findMatcher(int index, T pitch)
     * @see NoteMatcher#checkOctave(T octave)
     * @see BR#init(T pitch)
     */
    public static <T> int init(T pitch) throws CBR_Exception{
        int index = NoteMatcher.findMatcher(pitch);


        if (EnumMatcher.NUMBERS_EXPANSION.getValue() <= index){
            throw new CBR_Exception("Format not supported - " + ((Object)pitch).toString());
        }


        int cbr_int;

        if(EnumMatcher.INTS.getValue() == index){

            if(pitch instanceof String){
                try{
                    cbr_int = Integer.parseInt((String)pitch);
                }catch(Exception e){
                    throw new CBR_Exception("Incorrect format - " + ((Object)pitch).toString());
                }
            }else{
                cbr_int = (int)pitch;
            }

            return CBR.init(cbr_int%1000, (int)cbr_int/1000);
        }


        String note_matched;
        String alteration_matched;
        int octave_matched;
        
        try{
            note_matched = NoteHelpConverter.getNoteMatched(pitch);
            alteration_matched = NoteHelpConverter.getAlterationMatched(pitch);
            octave_matched = NoteHelpConverter.getOctaveMatched(pitch);
        }catch(NoteHelpConverter_Exception nhce){
            throw new CBR_Exception("Incorrect format - " + ((Object)pitch).toString());
        }

        return CBR.init(note_matched + alteration_matched, octave_matched);
    }



    /*
     * Methods
     */


    /**
     * This function, given two CBR, CBRI (CBR Interval).
     * 
     * @param cbr0   the first CBR
     * @param cbr1   the second CBR
     * @return       CBRI
     * @throws       CBR_Exception
     *
     * @see CBR#init(T binomial)
     */
    public static <T> int interval(T cbr0, T cbr1) throws CBR_Exception{
        return CBR.init(cbr1) - CBR.init(cbr0);
    }


    // /**
    //  * This function transposes CBR forward or backward by octave, PC or
    //  * NC transposition
    //  * 
    //  * @param  cbr          is the CBR value
    //  * @param trans_oct     is the value by which you want to transpose the octave
    //  * @param trans_pc      is the value by which you want to transpose PC
    //  * @param trans_nc      is the value by which you want to transpose NC
    //  * @return              the new CBR value
    //  * 
    //  * @see CBR#init(T pitch)
    //  * @see BR#transpose(T br, T transposition)
    //  */
    // public static <T> int transpose(T cbr, int trans_oct, int trans_pc, int trans_nc) throws CBR_Exception{
    //     int cbr_int = CBR.init(cbr);
    //     int oct = (cbr_int/1000 + trans_oct);
    //     int br = cbr_int - ((cbr_int/1000)*1000);

    //     try{
    //         return CBR.init(oct*1000 + BR.transpose(br, trans_pc, trans_nc));
    //     }catch(BR_Exception bre){
    //         throw new CBR_Exception("Incorrect format - " + ((Object)cbr).toString());
    //     }
    // }


    /**
     * This function transposes the CBR forward or backward by transposition
     * 
     * @param cbr               is the cbr value
     * @param transposition     is the value by whitch you want to transpose
     *                          the pitch
     * @return                  the new CBR value
     * @throws                  CBR_Exception
     * 
     * @see CBR#init(T pitch)
     */
    public static <T> int transpose(T cbr, T transposition) throws CBR_Exception{
        int cbr_int = CBR.init(cbr);

        int index_trans = NoteMatcher.findMatcher(EnumMatcher.INTS_NEGATIVE.getValue(), transposition);
        int transp_int;

        if(EnumMatcher.NOT_FOUND.getValue() == index_trans){
            throw new CBR_Exception("Incorrect transposition format - " + ((Object)transposition).toString());
        }

        if (transposition instanceof String){
            try{
                transp_int = Integer.parseInt((String)transposition);
            }catch(NumberFormatException nfe){
                throw new CBR_Exception("Incorrect transposition format - " + ((Object)transposition).toString());
            }
        }else{
            transp_int = (int)transposition;
        }


        if(0 > transp_int && Math.abs(transp_int) > cbr_int){
            throw new CBR_Exception("Incorrect format - " + ((Object)cbr).toString() + ", " + transposition);
        }

        int br = cbr_int - (((int)cbr_int/1000)*1000);

        int oct = (int)cbr_int/1000 + (transp_int/1000);
        int br_int;

        try{
            br_int = BR.transpose(br, transp_int - ((transp_int/1000)*1000));
        }catch(BR_Exception bre){
            throw new CBR_Exception("Incorrect BR format - " + ((Object)cbr).toString() + ", " + transp_int);
        }

        return CBR.init(oct*1000 + br_int);
    }


    /*
     * Getter
     */


    /**
     * This function returns the pitch value as a String.
     * 
     * @param pitch     the pitch value
     * @return          a String that represents the value of the pitch
     * @throws          CBR_Exception
     *
     * @see BR#getBinomPitch(T pitch)
     */
    public static <T> String getBinomPitch(T pitch) throws CBR_Exception{
        try{
            return BR.getBinomPitch(CBR.getBR(pitch));
        }catch(BR_Exception bre){
            throw new CBR_Exception("Incorrect format - " + ((Object)pitch).toString());
        }
    }


    /**
     * This function returns the Binomial interval
     * 
     * @param pitch     the pitch value
     * @return          a String that represents the name of the interval
     * @throws          CBR_Exception
     *
     * @see CBR#getBR(T pitch)
     * @see BR#getBinomInterval(T pitch)
     */
    public static <T> String getBinomInterval(T pitch) throws CBR_Exception{
        try{
            return BR.getBinomInterval(CBR.getBR(pitch));
        }catch(BR_Exception bre){
            throw new CBR_Exception("Incorrect format - " + ((Object)pitch).toString());
        }
    }


    /**
     * This function returns the octave value from CBR
     * 
     * @param pitch     is the CBR value
     * @return          the octave value
     * @throws          CBR_Exception
     *
     * @see CBR#init(T pitch)
     */
    public static <T> int getOctave(T pitch) throws CBR_Exception{
        int cbr = CBR.init(pitch);
        return cbr/1000;
    }


    /**
     * This function returns the BR value from CBR
     * 
     * @param pitch     is the CBR value
     * @return          the BR value
     * @throws          CBR_Exception
     *
     * @see CBR#init(T pitch)
     */
    public static <T> int getBR(T pitch) throws CBR_Exception{
        int cbr = CBR.init(pitch);
        return cbr - ((cbr/1000)*1000);
    }


    /**
     * This function returns the NC value from CBR
     * 
     * @param pitch     is the CBR value
     * @return          the NC value
     * @throws          CBR_Exception
     *
     * @see CBR#getBR(T pitch)
     * @see BR#getNC(T pitch)
     */
    public static <T> int getNC(T pitch) throws CBR_Exception{
        int br = CBR.getBR(pitch);

        try{
            return BR.getNC(br);
        }catch(BR_Exception bre){
            throw new CBR_Exception("Incorrect format - " + ((Object)pitch).toString());
        }
    }


    /**
     * This function returns the PC value from CBR
     * 
     * @param pitch     is the CBR value
     * @return          the PC value
     * @throws          CBR_Exception
     *
     * @see CBR#getBR(T pitch)
     * @see BR#getPC(T pitch)
     */
    public static <T> int getPC(T pitch) throws CBR_Exception{
        int br = CBR.getBR(pitch);

        try{
            return BR.getPC(br);
        }catch(BR_Exception bre){
            throw new CBR_Exception("Incorrect format - " + ((Object)pitch).toString());
        }
    }


    /**
     * toString
     *
     * @throws  CBR_Exception
     *
     * @see CBR#init(T pitch)
     */
    public static <T> String toString(T pitch) throws CBR_Exception{
        return "CBR: " + CBR.init(pitch);
    }
}
