package music_codec_lib;

import music_codec_lib.Exceptions.PC_Exception;
import music_codec_lib.Exceptions.NC_Exception;
import music_codec_lib.Exceptions.BR_Exception;
import music_codec_lib.Exceptions.Binomial_Exception;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * BR - Binomial single int Representation
 */
public class BR{

    /*
     * Private, helper function
     */

    /**
     * This function checks BR value
     * 
     * @param br    the BR value
     * @return      true if the BR representation is correct,
     *              false otherwise
     */
    private static boolean checkBRrange(int br){
        return (EnumMatcher.ZERO.getValue() > br || 7 < br%10 || 116 < br);
    }

    /**
     * This function returns an ArrayList with BR and Binom value.
     * The ArrayList is struct as follow:
     * [0] -> BR
     * [1] -> PC
     * [2] -> NC
     * 
     * @param pitch     the pitch value
     * @throws          BR_Exception
     * 
     * @see NoteMatcher#findMatcher(int index, T pitch)
     * @see Binomial#init(T pc, T nc)
     * @see Binomial#init(T pitch)
     */
    private static <T> ArrayList<Integer> getBRandBinom(T pitch) throws BR_Exception{
        int index = NoteMatcher.findMatcher(EnumMatcher.INTS.getValue(), pitch);

        ArrayList<Integer> br_pc_nc = new ArrayList<>();
        Integer[] bin;

        if(-1 != index){
            if (pitch instanceof String){
                try{
                    br_pc_nc.add(Integer.parseInt((String)pitch));
                }catch(NumberFormatException nfe){
                    throw new BR_Exception("Incorrect format - " + ((Object)pitch).toString());
                }
            }else{
                br_pc_nc.add((int)pitch);
            }

            if(checkBRrange(br_pc_nc.get(0))){
                throw new BR_Exception("Incorrect format - " + ((Object)pitch).toString());
            }

            try{
                int pc_temp = br_pc_nc.get(0)/10;
                int nc_temp = br_pc_nc.get(0)%10;
                bin = Binomial.init(pc_temp, nc_temp);
            }catch(Exception e){
                throw new BR_Exception("Incorrect format - " + ((Object)pitch).toString());
            }

            br_pc_nc.add(bin[0]);
            br_pc_nc.add(bin[1]);

            // returns BR, PC, NC
            return br_pc_nc;
        }


        try{
            bin = Binomial.init(pitch);
            return new ArrayList<Integer>(Arrays.asList((bin[0]*10) + bin[1], bin[0], bin[1]));
        }catch(Binomial_Exception e){
            throw new BR_Exception("Incorrect format - " + ((Object)pitch).toString());
        }
    }



    /*
     * Init
     */

    /**
     * Init function takes the BR pitch as parameter and
     * returns the int that represents it.
     * If the pitch inserted is not correct, the function throws an exception
     * 
     * @param pitch     the pitch value
     * @throws          BR_Exception
     *
     * @see BR#getBRandBinom(T pitch)
     */
    public static <T> int init(T pitch) throws BR_Exception{
        int br = BR.getBRandBinom(pitch).get(0);
        return br;
    }


    /**
     * Init function takes the BR pitch as parameter and
     * returns the int that represents it.
     * If the pitch inserted is not correct, the function throws an exception
     * 
     * @param pc     the PC value
     * @param nc     the NC value
     * @throws       BR_Exception
     * 
     * @see Binomial#init(T pc, T nc)
     * @see BR#getBRandBinom(T pitch)
     */
    public static <T> int init(T pc, T nc) throws BR_Exception{
        try{
            return BR.getBRandBinom(Binomial.init(pc, nc)).get(0);
        }catch(Binomial_Exception bn){
            throw new BR_Exception("Incorrect format: pc = " + ((Object)pc).toString() + ", nc = " + ((Object)nc).toString());
        }
    }
    
    

    /*
     * Method
     */

    /**
     * This function, given two BR, BRI (BR Interval).
     * 
     * @param br0   the first BR
     * @param br1   the second BR
     * @return      BRI
     * @throws      BR_Exception
     *
     * @see BR#init(T binomial)
     * @see PC#init(T pitch)
     * @see NC#init(T pitch)
     */
    public static <T> int interval(T br0, T br1) throws BR_Exception{
        int br_0 = BR.init(br0);
        int br_1 = BR.init(br1);

        int pc, nc;

        try{
             pc = PC.interval((int)br_0/10, (int)br_1/10);
             nc = NC.interval(br_0%10, br_1%10);
        }catch(PC_Exception | NC_Exception pc_nc_e){
            throw new BR_Exception("Incorrect format: br0 = " + ((Object)br0).toString() + ", br1 = " + ((Object)br0).toString());
        }

        return pc*10 + nc;
    }

    /**
     * This function, given two BR, returns his BRI'.
     *
     * @param br0   the first BR
     * @param br1   the second BR
     * @return      the Binomial' as an Integer[]
     * @throws      BR_Exception
     *
     * @see BR#interval(T br0, T br1)
     */
    public static <T> int intervalInversion(T br0, T br1) throws BR_Exception{
        return BR.interval(br1, br0);
    }

    /**
     * This function, given a BR, returns his BRI'.
     * 
     * @param br    BR value interval
     * @return      the Binomial' as an Integer[]
     * @throws      BR_Exception
     *
     * @see BR#interval(T br0, T br1)
     */
    public static <T> int intervalInversion(T br) throws BR_Exception{
        return BR.intervalInversion(0, br);
    }


    /**
     * This function transposes BR forward or backward by transposition
     * 
     * @param  br               is the BR value
     * @param transposition     is the value by whitch you want to transpose
     *                          the pitch
     * @return                  the new BR value
     * @throws                  BR_Exception
     *
     * @see BR#init(T pitch)
     */
    public static <T> int transpose(T br, T transposition) throws BR_Exception{
        int br_int = BR.init(br);
        int index_trans = NoteMatcher.findMatcher(EnumMatcher.INTS_NEGATIVE.getValue(), transposition);
        int transp_int;

        if(EnumMatcher.NOT_FOUND.getValue() == index_trans){
            throw new BR_Exception("Incorrect transposition format - " + ((Object)transposition).toString());
        }

        if (transposition instanceof String){
            try{
                transp_int = Integer.parseInt((String)transposition);
            }catch(NumberFormatException nfe){
                throw new BR_Exception("Incorrect transposition format - " + ((Object)transposition).toString());
            }
        }else{
            transp_int = (int)transposition;
        }

        int pc, nc;

        try{
            pc = PC.transpose((int)br_int/10, (int)transp_int/10);
        }catch(PC_Exception pce){
            throw new BR_Exception("Incorrect PC format: br = " + br_int + ", transposition = " + transp_int);
        }

        try{
            nc = NC.transpose(br_int%10, transp_int%10);
        }catch(NC_Exception nce){
            throw new BR_Exception("Incorrect NC format: br = " + br_int + ", transposition = " + transp_int);
        }

        return BR.init(pc*10 + nc);
    }


    /**
     * This function transposes the NC and PC notation
     * forward or backward by transpose_pc and transpose_nc
     * 
     * @param  br               is the BR value
     * @param transpose_pc      is the value by whitch you want to transpose PC
     * @param transpose_nc      is the value by whitch you want to transpose NC
     * @return                  the new BR value
     * @throws                  BR_Exception
     *
     * @see BR#init(T pc, T nc)
     * @see BR#transpose(T br, T transposition)
     */
    public static <T> int transpose(T br, T transpose_pc, T transpose_nc) throws BR_Exception{
        int index_trans_pc = NoteMatcher.findMatcher(EnumMatcher.INTS_NEGATIVE.getValue(), transpose_pc);
        int index_trans_nc = NoteMatcher.findMatcher(EnumMatcher.INTS_NEGATIVE.getValue(), transpose_nc);
        int transp_pc_int, transp_nc_int;

        if(EnumMatcher.NOT_FOUND.getValue() == index_trans_pc){
            throw new BR_Exception("Incorrect PC transposition format - " + ((Object)transpose_pc).toString());
        }

        if(EnumMatcher.NOT_FOUND.getValue() == index_trans_nc){
            throw new BR_Exception("Incorrect NC transposition format - " + ((Object)transpose_nc).toString());
        }


        if (transpose_pc instanceof String){
            try{
                transp_pc_int = Integer.parseInt((String)transpose_pc);
            }catch(NumberFormatException nfe){
                throw new BR_Exception("Incorrect PC transposition format - " + ((Object)transpose_pc).toString());
            }
        }else{
            transp_pc_int = (int)transpose_pc;
        }


        if (transpose_nc instanceof String){
            try{
                transp_nc_int = Integer.parseInt((String)transpose_nc);
            }catch(NumberFormatException nfe){
                throw new BR_Exception("Incorrect NC transposition format - " + ((Object)transpose_nc).toString());
            }
        }else{
            transp_nc_int = (int)transpose_nc;
        }

        return BR.transpose(br, (transp_pc_int%NoteMatcher.semitones_num)*10 + (transp_nc_int%NoteMatcher.notes_num));
    }


    /*
     * Getter
     */

    /**
     * This function returns the pitch value as a String.
     * 
     * @param pitch     the pitch value
     * @return          a String that represents the value of the pitch
     * @throws          BR_Exception
     *
     * @see BR#getBRandBinom(T pitch)
     * @see Binomial#getBinomPitch(T pc, T nc)
     */
    public static <T> String getBinomPitch(T pitch) throws BR_Exception{
        ArrayList<Integer> pc_nc = new ArrayList<>(BR.getBRandBinom(pitch).subList(1,3));

        try{
            return Binomial.getBinomPitch(pc_nc.get(0), pc_nc.get(1));
        }catch(Binomial_Exception be){
            throw new BR_Exception("Incorrect format: " + ((Object)pitch).toString());
        }
    }


    /**
     * This function returns the Binomial interval
     * 
     * @param pitch     the pitch value
     * @return          a String that represents the name of the interval
     * @throws          BR_Exception
     *
     * @see BR#getBRandBinom(T pitch)
     * @see Binomial#getBinomPitch(T pc, T nc)
     */
    public static <T> String getBinomInterval(T pitch) throws BR_Exception{
        ArrayList<Integer> pc_nc = new ArrayList<>(BR.getBRandBinom(pitch).subList(1,3));

        try{
            return Binomial.getBinomInterval(pc_nc.get(0), pc_nc.get(1));
        }catch(Binomial_Exception be){
            throw new BR_Exception("Incorrect format: " + ((Object)pitch).toString());
        }
    }


    /**
     * This function returns the NC value
     * 
     * @param pitch     is the pitch value
     * @return          the NC value
     * @throws          BR_Exception
     * 
     * @see BR#init(T pitch)
     */
    public static <T> int getNC(T pitch) throws BR_Exception{
        return (BR.init(pitch))%10;
    }


    /**
     * This function returns the PC value
     * 
     * @param pitch     is the pitch value
     * @return          the PC value
     * @throws          BR_Exception
     *
     * @see BR#init(T pitch)
     */
    public static <T> int getPC(T pitch) throws BR_Exception{
        return (BR.init(pitch))/10;
    }


    /*
     * Conversions
     */

    /**
     * This function convert BR to Binomial
     * 
     * @param pitch     is the BR pitch value
     * @return          the Binomial value
     * @throws          BR_Exception
     *
     * @see BR#getPC(T pitch)
     * @see BR#getNC(T pitch)
     */
    public static <T> Integer[] convBRtoBinomial(T pitch) throws BR_Exception{
        return new Integer[]{BR.getPC(pitch), BR.getNC(pitch)};
    }


    /**
     * toString
     *
     * @throws BR_Exception
     *
     * @see BR#init(T pitch)
     */
    public static <T> String toString(T pitch) throws BR_Exception{
        return "BR: " + BR.init(pitch);
    }
}
