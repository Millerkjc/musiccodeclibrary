package music_codec_lib;

import music_codec_lib.Exceptions.PC_Exception;
import music_codec_lib.Exceptions.NC_Exception;
import music_codec_lib.Exceptions.Binomial_Exception;
import music_codec_lib.Exceptions.NoteHelpConverter_Exception;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * Binomial
 */
public class Binomial{

    /*
     * Private, helper function
     */

    private static ArrayList<String> intervals = new ArrayList<String>(
        Arrays.asList(
            "P1", "A1", "2A1", "3A1", "4A1", "5A1", "6A1", "5d1", "4d1", "3d1", "2d1", "d1",
            "d2", "m2", "M2", "A2", "2A2", "3A2", "4A2", "5A2", "5d2", "4d2", "3d2", "2d2",
            "3d3", "2d3", "d3", "m3", "M3", "A3", "2A3", "3A3", "4A3", "5A3", "5d3", "4d3",
            "5d4", "4d4", "3d4", "2d4", "d4", "P4", "A4", "2A4", "3A4", "4A4", "5A4", "6A4",
            "5A5", "6A5", "5d5", "4d5", "3d5", "2d5", "d5", "P5", "A5", "2A5", "3A5", "4A5",
            "3A6", "4A6", "5A6", "5d6", "4d6", "3d6", "2d6", "d6", "m6", "M6", "A6", "2A6",
            "A7", "2A7", "3A7", "4A7", "5A7", "5d7", "4d7", "3d7", "2d7", "d7", "m7", "M7"
        ));

    private static ArrayList<String> allBinomSemitones = new ArrayList<String>(
        Arrays.asList(
            "Do", "Do#", "Dox", "Dox#", "Doxx", "Doxx#", "Doxxx", "Dobbbbb", "Dobbbb", "Dobbb", "Dobb", "Dob",
            "Rebb", "Reb", "Re", "Re#", "Rex", "Rex#", "Rexx", "Rexx#", "Rexxx", "Rebbbbb", "Rebbbb", "Rebbb",
            "Mibbbb", "Mibbb", "Mibb", "Mib", "Mi", "Mi#", "Mix", "Mix#", "Mixx", "Mixx#", "Mixxx", "Mibbbbb",
            "Fabbbbb", "Fabbbb", "Fabbb", "Fabb", "Fab", "Fa", "Fa#", "Fax", "Fax#", "Faxx", "Faxx#", "Faxxx",
            "Solxx#", "Solxxx", "Solbbbbb", "Solbbbb", "Solbbb", "Solbb", "Solb", "Sol", "Sol#", "Solx", "Solx#", "Solxx",
            "Lax#", "Laxx", "Laxx#", "Laxxx", "Labbbbb", "Labbbb", "Labbb", "Labb", "Lab", "La", "La#", "Lax",
            "Si#", "Six", "Six#", "Sixx", "Sixx#", "Sixxx", "Sibbbbb", "Sibbbb", "Sibbb", "Sibb", "Sib", "Si"
        ));


    /**
     * Taken an int[], this function convert it to Integer[]
     * If all arguments are correct, it returns an array with the two
     * value in integer format, otherwise it throws an error
     * 
     * @param array is the int[]
     * @return      Integer[] with the same values of the int[]
     * @throws      Binomial_Exception
     */
    private static <T> Integer[] fromIntToIntegerArray(T array) throws Binomial_Exception{
       Integer[] IntegerArray;

        if(array instanceof int[]){
             IntegerArray = Arrays.stream((int[])array).boxed().toArray( Integer[]::new );
        }else{
            IntegerArray = (Integer[])array;
        }

        if(2 != IntegerArray.length){
            throw new Binomial_Exception("Incorrect format, array's length must be 2");
        }

        return IntegerArray;
    }


    /**
     * Taken the String argument, this function convert it to the correct
     * Binom notation.
     * If all arguments are correct, it returns an array with the two
     * value in integer format, otherwise it throws an error
     * 
     * @param pc    is the PC value
     * @param nc    is the NC value
     * @return      an Integer array structured as follow:
     *              binomialRepresentation[0] is NC value
     *              binomialRepresentation[1] is PC value
     * @throws      Binomial_Exception
     */
    private static Integer[] fromStringToBinom(String stringToBinom) throws Binomial_Exception{
        int index_to_binom = Binomial.indexFromBinomSemitones(stringToBinom);
        int index_to_interval = Binomial.intervals.indexOf(stringToBinom);

        int index_match = (-1 != index_to_binom) ? index_to_binom : index_to_interval;


        try{
            if (-1 == index_match){
                throw new Exception();
            }
            int pc = index_match%12;
            int nc = (int)index_match/12;

            return new Integer[]{pc, nc};
        }catch(Exception e){
            throw new Binomial_Exception("Incorrect String format - " + ((Object)stringToBinom).toString());
        }
    }


    /**
     * Given a String semitone value, this function returns
     * the index of that specific pitch
     * If all arguments are correct, it returns an array with the two
     * value in integer format, otherwise it throws an error
     * 
     * @param semitone      the String pitch value
     * @return              the index of the pitch from the
     *                      allBinomSemitones array
     * @throws              Binomial_Exception
     *
     * @see NoteMatcher#findMatcher(int index, T pitch)
     * @see NoteHelpConverter#standardAngloConverter(T pitch)
     * @see Binomial#allBinomSemitones
     */
    private static <T> int indexFromBinomSemitones(String semitone){
        int index = NoteMatcher.findMatcher(EnumMatcher.ALL_PITCHES_AND_NOTE.getValue(), semitone);
        
        if(-1 == index){
            return -1;
        }
        
        String std_note = semitone;
        index = NoteMatcher.findMatcher(EnumMatcher.SEMITONES_PITCH.getValue(), semitone);
        
        if(-1 == index){
            try{
                std_note = NoteHelpConverter.standardAngloConverter(semitone);
            }catch(NoteHelpConverter_Exception e){
                return -1;
            }
        }

        return Binomial.allBinomSemitones.indexOf(std_note);
    }


    /**
     * Given a String argument, this function check the correctness of
     * a negative Binomial value.
     * If all arguments are correct, it returns an array with the two
     * value in integer format, otherwise it throws an error
     * 
     * @param binomial      the binomial value
     * @return              an Integer array structured as follow:
     *                      binomialRepresentation[0] is NC value (negative)
     *                      binomialRepresentation[1] is PC value (negative)
     * @throws              Binomial_Exception
     */
    private static <T> Integer[] negativeBinomString(T binomial) throws Binomial_Exception{
        String neg_str = (String)binomial;
        neg_str = (neg_str).substring(1);
        Integer[] binom_codec = Binomial.fromStringToBinom(neg_str);
        
        return new Integer[]{(-1)*binom_codec[0], (-1)*binom_codec[1]};
    }


    /**
     * Given the arguments, this function check the correctness and initialize
     * the PC and NC values.
     * If all arguments are correct, it returns an array with the two
     * value in integer format, otherwise it throws an error
     * 
     * @param pc    is the PC value
     * @param nc    is the NC value
     * @return      an Integer array structured as follow:
     *              binomialRepresentation[0] is NC value
     *              binomialRepresentation[1] is PC value
     * @throws      Binomial_Exception
     */
    private static <T> Integer[] initializePCandNCBinom(T pc, T nc) throws Binomial_Exception{
        Integer[] binomialRepresentation = new Integer[2];
        int nc_int, pc_int;

        try{
            pc_int = PC.init(pc);
        }catch(PC_Exception pce){
            throw new Binomial_Exception("PC incorrect format - " + ((Object)pc).toString());
        }

        try{
            nc_int = NC.init(nc);
        }catch(NC_Exception nce){
            throw new Binomial_Exception("NC incorrect format - " + ((Object)nc).toString());
        }

        binomialRepresentation[0] = pc_int;
        binomialRepresentation[1] = nc_int;

        return binomialRepresentation;
    }


    /**
     * This function is used to manage negative pitch.
     * It checks that the absolute values is correct and return his original
     * sign.
     * Es. (NC: -Re = -1; PC: -Sib = -10)
     * 
     * @param pitch     represents the PC/NC value
     * @param type      represents the variable type
     * @param nc_or_pc  is the string that specify if the input is NC or PC
     * @return          a String array structured as follow:
     *                  ret[0] is the absolute value of pc/nc
     *                  ret[1] is "0"(the input is positive) or "1"(the input is negative)
     * @throws          Binomial_Exception
     */
    private static <T> String[] manageNegativeNCPC(T pitch, int type, String nc_or_pc) throws Binomial_Exception{
        String[] ret = new String[2];
        ret[1] = "0";

        // INTS_NEGATIVE
        if(-1 != type){
            if(0 > (int)pitch){
                ret[1] = "1";
                ret[0] = String.valueOf((-1)*(int)pitch);
            }else{
                ret[0] = String.valueOf((int)pitch);
            }
        }else{
            if(pitch instanceof String){
                ret[0] = (String)pitch;
                
                if((ret[0]).equals("")){
                    throw new Binomial_Exception("Incorrect format, String " + nc_or_pc + " - " + ((Object)pitch).toString());
                }
                if((ret[0]).substring(0,1).equals("-")){
                    ret[0] = (ret[0]).substring(1);
                    ret[1] = "1";
                }
            }
        }

        return ret;
    }


    /**
     * Init function takes negative values of PC and NC and, after
     * checks, it returns the Integer[].
     * If the pitch inserterted is not correct, the function throws an exception
     * 
     * @param pc            the PC value
     * @param nc            the NC value
     * @param pc_index      the index of the regex that match PC
     * @param nc_index      the index of the regex that match NC
     * @return              the Binomial representation
     * @throws              Binomial_Exception
     *
     * @see Binomial#manageNegativeNCPC(T pitch, int type, String nc_or_pc)
     * @see Binomial#initializePCandNCBinom(T pc, T nc)
     */
    private static <T> Integer[] negativeBinomArray(T pc, T nc, int pc_index, int nc_index) throws Binomial_Exception{
        Integer[] binomialRepresentation = new Integer[2];
        String[] pc_str, nc_str;

        pc_str = Binomial.manageNegativeNCPC(pc, pc_index, "PC");
        nc_str = Binomial.manageNegativeNCPC(nc, nc_index, "NC");

        binomialRepresentation = Binomial.initializePCandNCBinom(pc_str[0], nc_str[0]);
        
        if(pc_str[1].equals("1")){
            binomialRepresentation[0] = (-1)*binomialRepresentation[0];
        }
        if(nc_str[1].equals("1")){
            binomialRepresentation[1] = (-1)*binomialRepresentation[1];
        }

        return binomialRepresentation;
    }




    /*
     * Init
     */


    /**
     * Init function takes the Binomial pitch as parameter and
     * returns the Integer[] that represents it.
     * If the pitch inserterted is not correct, the function throws an exception
     * 
     * @param binomial      could be an Integer[] or a string
     * @return              the Binomial representation
     * @throws              Binomial_Exception
     *
     * @see NoteMatcher#findMatcher(int index, T pitch)
     * @see Binomial#negativeBinomArray(T pc, T nc, int pc_index, int nc_index)
     * @see Binomial#initializePCandNCBinom(T pc, T nc)
     */
    public static <T> Integer[] init(T pc, T nc) throws Binomial_Exception{
        int pc_index = NoteMatcher.findMatcher(EnumMatcher.INTS_NEGATIVE.getValue(), pc);
        int nc_index = NoteMatcher.findMatcher(EnumMatcher.INTS_NEGATIVE.getValue(), nc);

        if(-1 != pc_index || -1 != nc_index){
            return Binomial.negativeBinomArray(pc, nc, pc_index, nc_index);
        }

        return Binomial.initializePCandNCBinom(pc, nc);
    }


    /**
     * Init function takes the Binomial pitch as parameter and
     * returns the Integer[] that represents it.
     * If the pitch inserterted is not correct, the function throws an exception
     * 
     * @param binomial      could be an Integer[] or a string
     * @return              the Binomial representation
     * @throws              Binomial_Exception
     *
     * @see Binomial#negativeBinomString(T binomial)
     * @see Binomial.fromStringToBinom(String stringToBinom)
     * @see Binomial.fromIntToIntegerArray(T array)
     */
    public static <T> Integer[] init(T binomial) throws Binomial_Exception{
        boolean str_bin = binomial instanceof String;
        boolean array_bin = (binomial instanceof Integer[] || binomial instanceof int[]);

        if(!(str_bin || array_bin)){
            throw new Binomial_Exception("Incorrect format - " + ((Object)binomial).toString());
        }

        // Interval or Semitone string
        if (str_bin){

            String bin_str = (String)binomial;

            if((bin_str).equals("")){
                throw new Binomial_Exception("Incorrect format, String = " + ((Object)binomial).toString());
            }

            if((bin_str).substring(0,1).equals("-")){
                return Binomial.negativeBinomString(bin_str);
            }

            return Binomial.fromStringToBinom(bin_str);

        }

        Integer[] binomInteger = Binomial.fromIntToIntegerArray(binomial);

        return Binomial.init(binomInteger[0], binomInteger[1]);
    }


    /*
     * Methods
     */


    /**
     * This function, given two Binomial, returns PCI and NCI.
     * Binomial
     *      |=> NCI(NC interval).
     *      |=> PCI(PC interval).
     * 
     * @param binom0    the first Binomial
     * @param binom1    the second Binomial
     * @return          PCI and NCI as an Integer[]
     * @throws          Binomial_Exception
     *
     * @see Binomial#init(T binomial)
     * @see PC#init(T pitch)
     * @see NC#init(T pitch)
     */
    public static <T> Integer[] interval(T binom0, T binom1) throws Binomial_Exception{
        Integer[] b0 = Binomial.init(binom0);
        Integer[] b1 = Binomial.init(binom1);

        try{
            return new Integer[]{PC.interval(b0[0], b1[0]), NC.interval(b0[1], b1[1])};
        }catch(Exception e){
            throw new Binomial_Exception("Interval calc error, pc0 = " + b0[0] + ", nc0 = " + b0[1] + "; pc1 = " + b1[0] + ", nc1 = " + b1[1]);
        }
    }


    /**
     * This function, given a Binomial, returns his Binomial'.
     * => NCI to NCI' (NC interval inverse).
     * => PCI to PCI' (PC interval inverse).
     *
     * @param binom0    the first Binomial
     * @param binom1    the second Binomial
     * @return          the Binomial' as an Integer[]
     * @throws          Binomial_Exception
     * 
     * @see Binomial#interval(T binom0, T binom1)
     */
    public static <T> Integer[] intervalInversion(T binom0, T binom1) throws Binomial_Exception{
        return Binomial.interval(binom1, binom0);
    }


    /**
     * This function, given a Binomial, returns his Binomial'.
     * => NCI to NCI' (NC interval inverse).
     * => PCI to PCI' (PC interval inverse).
     * 
     * @param binom     Binomial value interval ([NCI, PCI])
     * @return          the Binomial' as an Integer[]
     * @throws          Binomial_Exception
     *
     * @see Binomial#init(T pc, T nc)
     * @see Binomial#intervalInversion(T binom1, T binom0)
     */
    public static <T> Integer[] intervalInversion(T binom) throws Binomial_Exception{
        return Binomial.intervalInversion(Binomial.init(0, 0), binom);
    }


    /**
     * This function transposes the NC and PC of the Binomial notation
     * forward or backward by transposition
     * 
     * @param binom             is the Binomial value
     * @param transposition     is the value by whitch you want to transpose
     *                          the pitch
     * @return                  the new Binomial value, as Integer[]
     * @throws          Binomial_Exception
     *
     * @see PC#transpose(T pitch, int transposition)
     * @see NC#transpose(T pitch, int transposition)
     */
    public static <T> Integer[] transpose(T binom, T transposition) throws Binomial_Exception{
        Integer[] bin = Binomial.init(binom);
        Integer[] trans = Binomial.init(transposition);
        int pc_transp, nc_transp;

        try{

            pc_transp = PC.transpose(bin[0], trans[0]);
            nc_transp = NC.transpose(bin[1], trans[1]);

        }catch(PC_Exception | NC_Exception pce){
            throw new Binomial_Exception("PC or NC incorrect format, binom = " + "pc: " + bin[0] + ", nc: " + bin[1] + ", transposition = " + "pc: " + trans[0] + ", nc: " + trans[1]);
        }

        return new Integer[]{pc_transp, nc_transp};
    }


    /**
     * This function transposes the NC and PC of the Binomial notation
     * forward or backward by transposition
     * 
     * @param binom         is the Binomial value
     * @param transpose_pc  is the value by whitch you want to
     *                      transpose PC
     * @param transpose_nc  is the value by whitch you want to
     *                      transpose NC
     * @return              the new Binomial value, as Integer[]
     * @throws              Binomial_Exception
     * 
     * @see Binomial#transpose(T binom, T transposition)
     */
    public static <T> Integer[] transpose(T binom, T transpose_pc, T transpose_nc) throws Binomial_Exception{
        int index_trans_pc = NoteMatcher.findMatcher(EnumMatcher.INTS_NEGATIVE.getValue(), transpose_pc);
        int index_trans_nc = NoteMatcher.findMatcher(EnumMatcher.INTS_NEGATIVE.getValue(), transpose_nc);
        int transp_pc_int, transp_nc_int;

        if(EnumMatcher.NOT_FOUND.getValue() == index_trans_pc){
            throw new Binomial_Exception("Incorrect PC transposition format - " + ((Object)transpose_pc).toString());
        }

        if(EnumMatcher.NOT_FOUND.getValue() == index_trans_nc){
            throw new Binomial_Exception("Incorrect NC transposition format - " + ((Object)transpose_nc).toString());
        }


        if (transpose_pc instanceof String){
            try{
                transp_pc_int = Integer.parseInt((String)transpose_pc);
            }catch(NumberFormatException nfe){
                throw new Binomial_Exception("Incorrect PC transposition format - " + ((Object)transpose_pc).toString());
            }
        }else{
            transp_pc_int = (int)transpose_pc;
        }


        if (transpose_nc instanceof String){
            try{
                transp_nc_int = Integer.parseInt((String)transpose_nc);
            }catch(NumberFormatException nfe){
                throw new Binomial_Exception("Incorrect NC transposition format - " + ((Object)transpose_nc).toString());
            }
        }else{
            transp_nc_int = (int)transpose_nc;
        }

        return Binomial.transpose(binom, new Integer[]{transp_pc_int, transp_nc_int});
    }


    /*
     * Getter
     */

    /**
     * This function, given a PC and NC value, returns the pitch value
     * as a String.
     * 
     * @param pc        the PC value
     * @param nc        the NC value
     * @return          a String that represents the value of the pitch
     * @throws          Binomial_Exception
     *
     * @see Binomial#init(T binom)
     * @see NoteMatcher#semitones_num
     * @see Binomial#allBinomSemitones
     */
    public static <T> String getBinomPitch(T pc, T nc) throws Binomial_Exception{
        Integer[] bin = Binomial.init(pc, nc);
        try{
            return Binomial.allBinomSemitones.get((NoteMatcher.semitones_num*(bin[1]))+bin[0]);
        }catch(Exception e){
            throw new Binomial_Exception("Incorrect pitch - <" + bin[0] + ", " + bin[1] + ">");
        }
    }

    
     /**
     * This function, given a binomial value, returns the pitch value
     * as a String.
     * 
     * @param binom     the binomial value
     * @return          a String that represents the value of the pitch
     * @throws          Binomial_Exception
     *
     * @see Binomial#init(T binom)
     * @see Binomial#getBinomPitch(T pc, T nc)
     */
    public static <T> String getBinomPitch(T binom) throws Binomial_Exception{
        Integer[] bin = Binomial.init(binom);
        return Binomial.getBinomPitch(bin[0], bin[1]);
    }


    /**
     * This function, given PC and NC, returns the Binomial interval.
     * 
     * @param pc    the PC value
     * @param nc    the NC value
     * @return      a String that represents the name of the interval
     * @throws      Binomial_Exception
     *
     * @see Binomial#init(T pc, T nc)
     * @see NoteMatcher#semitones_num
     * @see Binomial#intervals
     */
    public static <T> String getBinomInterval(T pc, T nc) throws Binomial_Exception{
        Integer[] bin = Binomial.init(pc, nc);
        try{
            return Binomial.intervals.get((NoteMatcher.semitones_num*(bin[1]))+bin[0]);
        }catch(Exception e){
            throw new Binomial_Exception("Incorrect interval - <" + bin[0] + ", " + bin[1] + ">");
        }
    }


    /**
     * This function, given a binomial value, returns the Binomial interval.
     * 
     * @param binom     the Binomial value
     * @return          a String that represents the name of the interval
     * @throws          Binomial_Exception
     *
     * @see Binomial#init(T pc, T nc)
     * @see Binomial#getBinomInterval(T pc, T nc)
     */
    public static <T> String getBinomInterval(T binom) throws Binomial_Exception{
        Integer[] bin = Binomial.init(binom);
        return Binomial.getBinomInterval(bin[0], bin[1]);
    }


    /**
     * This function returns the NC value
     * 
     * @param binom     is the Binomial pitch value
     * @return          the NC value
     * @throws          Binomial_Exception
     *
     * @see Binomial#init(T binomial)
     */
    public static <T> int getNC(T binom) throws Binomial_Exception{
        Integer[] bin = Binomial.init(binom);
        return bin[1];        
    }


    /**
     * This function returns the PC value
     * 
     * @param binom     is the Binomial pitch value
     * @return          the PC value
     * @throws          Binomial_Exception
     *
     * @see Binomial#init(T binomial)
     */
    public static <T> int getPC(T binom) throws Binomial_Exception{
        Integer[] bin = Binomial.init(binom);
        return bin[0];
    }


    /*
     * Conversions
     */

    /**
     * This function convert Binomial to BR
     * 
     * @param pitch     is the Binomial pitch value
     * @return          the BR value
     * @throws          Binomial_Exception
     *
     * @see Binomial#init(T binomial)
     */
    public static <T> int convBinomialtoBR(T pitch) throws Binomial_Exception{
        Integer[] bin = Binomial.init(pitch);
        return (bin[0]*10)+bin[1];
    }


    /**
     * toString
     *
     * @param binom     is the Binomial pitch value
     * @throws          Binomial_Exception
     *
     * @see Binomial#init(T binomial)
     */
    public static <T> String toString(T binom) throws Binomial_Exception{
        Integer[] bin = Binomial.init(binom);
        return "Binomial - pc: " + bin[0] + ", nc: " + bin[1];
    }
}
