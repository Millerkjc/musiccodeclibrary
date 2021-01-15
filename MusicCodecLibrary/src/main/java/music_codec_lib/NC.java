package music_codec_lib;

import music_codec_lib.Exceptions.CPC_Exception;
import music_codec_lib.Exceptions.NC_Exception;
import music_codec_lib.Exceptions.NoteHelpConverter_Exception;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * Name Class (NC) -> Represent only note name [0..6]
 */
public class NC{
    
    /*
     * Private, helper function
     */

    private static ArrayList<String> NCI_intervalName =
                new ArrayList<String>(
                    Arrays.asList(
                        "Unisono | Ottava",
                        "Seconda",
                        "Terza",
                        "Quarta",
                        "Quinta",
                        "Sesta",
                        "Settima"
                    )
                );
    

    /**
     * This private function checks the correctness of the given pitch
     * 
     * @param pitch     is the int that represent the pitch
     * @return          true if the pitch is betweene 0 and 6
     *                  false otherwise
     */
    private static boolean checkNC(int pitch){
        return !(EnumMatcher.ZERO.getValue() <= pitch &&  pitch <NoteMatcher.notes_num);
    }


    /**
     * This private function returns the String with the current int pitch
     * and the NCI name associated with that value.
     * If the get fails the function throws an exception.
     * 
     * @param nci   is the int that represent the pitch
     * @return      the String with the current pitch and the NCI
     *              name associated with that value
     * @throws      NC_Exception
     */
    private static String getNCIName(int nci) throws NC_Exception{
        try{
            return NCI_intervalName.get(nci);
        }catch(Exception nce){
            throw new NC_Exception("nci must be between [0..6] value");
        }
    }
    
    
    /*
     * Init
     */

    /**
     * Init function that takes a pitch as parameter and returns the int
     * that represents it.
     * If the pitch insertered is not correct, the function throws an exception
     * 
     * @param pitch     could be an int or a string. (Es. 1, "1" or "Re")
     * @return          the int representation of the pitch
     * @throws          NC_Exception
     *
     * @see NoteMatcher#findMatcher(T pitch)
     * @see NoteHelpConverter#toSemitones(T pitch)
     */
    public static <T> int init(T pitch) throws NC_Exception{
        int index = NoteMatcher.findMatcher(pitch);

        if (-1 == index){
            throw new NC_Exception("Incorrect format - " + ((Object)pitch).toString());
        }

        if (EnumMatcher.NUMBERS_EXPANSION.getValue() <= index){
            throw new NC_Exception("Format not supported - " + ((Object)pitch).toString());
        }


        if(pitch instanceof String){
            if (EnumMatcher.INTS.getValue() == index){

                int pitch_tmp = -1;
                
                try{
                    pitch_tmp = Integer.parseInt((String)pitch);
                    if (NC.checkNC(pitch_tmp)){
                        throw new Exception();
                    }
                }catch(Exception e){
                    throw new NC_Exception("Incorrect format - " + ((Object)pitch).toString());
                }
                
                return pitch_tmp;
            }

            try{
                return NoteHelpConverter.fromSemitonesToNote(pitch);
            }catch(NoteHelpConverter_Exception nhce){
                throw new NC_Exception("Incorrect format - " + ((Object)pitch).toString());
            }
            
        }

        if(NC.checkNC((int)pitch)){
            throw new NC_Exception("Incorrect format - " + ((Object)pitch).toString());
        }

        return (int)pitch;
    }


    /*
     * Methods
     */

    /**
     * This function, given two NC, returns the NCI (NC interval).
     * 
     * @param nc_a      the first NC
     * @param nc_b      the second NC
     * @return          the NCI as an int; this value will be
     *                  between 0 and 6.
     * @throws          NC_Exception
     *
     * @see NC#init(T pitch)
     */
    public static <T> int interval(T nc_a, T nc_b) throws NC_Exception{
        int nc_a_int = NC.init(nc_a);
        int nc_b_int = NC.init(nc_b);
        
        int NCI_ab = nc_b_int - nc_a_int;
        
        NCI_ab = (0 > NCI_ab) ? (NCI_ab + NoteMatcher.notes_num) : NCI_ab;

        return NCI_ab%EnumMatcher.NC_MODULE.getValue();
    }


    /**
     * This function, given two NC, returns the NCI' (NC interval inverse).
     * 
     * @param nc_a      the first NC
     * @param nc_b      the second NC
     * @return          the NCI' as an int; this value will be
     *                  between 0 and 6.
     * @throws          NC_Exception
     *
     * @see NC#interval(T nc_a, T nc_b)
     */
    public static <T> int intervalInversion(T nc_a, T nc_b) throws NC_Exception{
        return (NoteMatcher.notes_num - NC.interval(nc_a, nc_b))%NoteMatcher.notes_num;
    }


    /**
     * This function, given a PCI, returns his NCI' (NC interval inverse).
     * 
     * @param nci       NC interval
     * @return          the NCI' as an int; this value will be
     *                  between 0 and 6.
     * @throws          NC_Exception
     *
     * @see NC#intervalInversion(T nc_a, T nc_b)
     */
    public static <T> int intervalInversion(T nci) throws NC_Exception{
        return NC.intervalInversion(0, nci);
    }


    /**
     * This function, given two PC, returns the IC (Interval Class).
     * 
     * @param nc_a      the first NC
     * @param nc_b      the second NC
     * @return          the IC as an int; this value will be
     *                  between 0 and 6.
     * @throws          NC_Exception
     *
     * @see NC#interval(T nc_a, T nc_b)
     * @see NC#intervalInversion(T nci)
     */
    public static <T> int IC(T nc_a, T nc_b) throws NC_Exception{
        int nc_a_int = NC.init(nc_a);
        int nc_b_int = NC.init(nc_b);

        int nci = NC.interval(nc_a, nc_b);
        int nci_inv = NC.intervalInversion(nci);
        return (nci <= nci_inv) ? nci : nci_inv;
    }


    /**
     * This function transposes the NC forward or backward by transposition
     * 
     * @param nc                is the value of the name class
     * @param transposition     is the value by whitch you want to transpose
     *                          the pitch
     * @return                  the new name class value, as integer
     * @throws                  NC_Exception
     *
     * @see NC#init(T pitch)
     */
    public static <T> int transpose(T nc, T transposition) throws NC_Exception{
        int nc_i = NC.init(nc);
        int index_trans = NoteMatcher.findMatcher(EnumMatcher.INTS_NEGATIVE.getValue(), transposition);
        int transp_int;

        if(EnumMatcher.NOT_FOUND.getValue() == index_trans){
            throw new NC_Exception("Incorrect transposition format - " + ((Object)transposition).toString());
        }

        if (transposition instanceof String){
            try{
                transp_int = Integer.parseInt((String)transposition);
            }catch(NumberFormatException nfe){
                throw new NC_Exception("Incorrect transposition format - " + ((Object)transposition).toString());
            }
        }else{
            transp_int = (int)transposition;
        }

        return NC.init((nc_i + transp_int)%NoteMatcher.notes_num);
    }


    /*
     * Getter
     */

    /**
     * This function returns the name of the NCI interval
     * 
     * @param nc_a      the first NC
     * @param nc_b      the second NC
     * @return          a String with the name of the interval
     * @throws          NC_Exception
     *
     * @see NC#interval(T nc_a, T nc_b)
     * @see NC#getNCIName(int nci) 
     */
    public static <T> String getCIName(T nc_a, T nc_b) throws NC_Exception{
       return NC.getNCIName(NC.interval(nc_a, nc_b));
    }


    /**
     * This function returns the name of the NCI' interval
     * 
     * @param nc_a      the first NC
     * @param nc_b      the second NC
     * @return          a String with the name of the interval
     * @throws          NC_Exception
     *
     * @see NC#intervalInversion(T nc_a, T nc_b)
     * @see NC#getNCIName(int nci)
     */
    public static <T> String getCIinverseName(T nc_a, T nc_b) throws NC_Exception{
       return NC.getNCIName(NC.intervalInversion(nc_a, nc_b));
    }


    /**
     * This function returns the NC value
     * 
     * @param nc    is the name class value
     * @return      the NC value
     * @throws      NC_Exception
     * 
     * @see NC#init(T pitch)
     */
    public static <T> int getNC(T nc) throws NC_Exception{
        return NC.init(nc);
    }


    /*
     * Conversions
     */

    /**
     * This function convert NC to PC
     * 
     * @param nc    is the name class value
     * @return      the PC value
     * @throws      NC_Exception
     * 
     * @see NC#init(T pitch)
     * @see NoteHelpConverter#fromSemitonesToNote(T pitch)
     */
    public static <T> int convNCtoPC(T nc) throws NC_Exception{
        try{
            return NoteHelpConverter.fromNoteToSemitone(NC.init(nc));
        }catch(NoteHelpConverter_Exception nhce){
            throw new NC_Exception("Incorrect format - " + ((Object)nc).toString());
        }
    }


    /**
     * toString
     *
     * @throws NC_Exception
     *
     * @see NC#init(T pitch)
     */
    public static <T> String toString(T nc) throws NC_Exception{
        return "NC: " + NC.init(nc);
    }

}
