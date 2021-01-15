package music_codec_lib;

import music_codec_lib.Exceptions.OPPC_Exception;
import music_codec_lib.Exceptions.PC_Exception;
import music_codec_lib.Exceptions.NoteHelpConverter_Exception;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * Pitch Class (PC) -> Represent only semitones name [0..11]
 */
public class PC{

    /*
     * Private, helper function
     */

    private static ArrayList<String> PCI_intervalName =
                new ArrayList<String>(
                    Arrays.asList(
                        "Unisono giusto | Ottava giusta",
                        "Seconda minore",
                        "Seconda maggiore",
                        "Terza minore",
                        "Terza maggiore",
                        "Quarta giusta",
                        "Quarta eccedente | Quinta diminuita",
                        "Quinta giusta",
                        "Sesta minore",
                        "Sesta maggiore",
                        "Settima minore",
                        "Settima maggiore"
                    )
                );


    /**
     * This private function checks the correctness of the given pitch
     * 
     * @param pitch     is the int that represent the pitch
     * @return          true if the pitch is betweene 0 and 11
     *                  false otherwise
     */
    private static boolean checkPC(int pitch){
        return !(EnumMatcher.ZERO.getValue() <= pitch &&  pitch <NoteMatcher.semitones_num);
    }


    /**
     * This private function returns the String with the current int pitch
     * and the PCI name associated with that value.
     * If the get fails the function throws an exception.
     * 
     * @param pci   is the int that represent the pitch
     * @return      the String with the current pitch and the PCI
     *              name associated with that value
     * @throws      PC_Exception
     */
    private static String getPCIName(int pci) throws PC_Exception{
        try{
            return PC.PCI_intervalName.get(pci);
        }catch(Exception pce){
            throw new PC_Exception("pci must be between [0..11] value");
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
     * @param pitch     could be an int or a string. (Es. 3, "3" or "Re")
     * @return          the int representation of the pitch
     * @throws          PC_Exception
     * 
     * @see NoteMatcher#findMatcher(T pitch)
     * @see NoteHelpConverter#toSemitones(T pitch)
     */
    public static <T> int init(T pitch) throws PC_Exception{
        int index = NoteMatcher.findMatcher(pitch);

        if (-1 == index){
            throw new PC_Exception("Incorrect format - " + ((Object)pitch).toString());
        }
        
        if (EnumMatcher.NUMBERS_EXPANSION.getValue() <= index){
            throw new PC_Exception("Format not supported - " + ((Object)pitch).toString());
        }


        if(pitch instanceof String){
            if (EnumMatcher.INTS.getValue() == index){
                
                int pitch_tmp = -1;
                
                try{
                    pitch_tmp = Integer.parseInt((String)pitch);
                    if (PC.checkPC(pitch_tmp)){
                        throw new Exception();
                    }
                }catch(Exception e){
                    throw new PC_Exception("Incorrect format - " + ((Object)pitch).toString());
                }

                return pitch_tmp;
            }

            try{

                int semitone_tmp = NoteHelpConverter.toSemitones(pitch);

                if(0 > semitone_tmp){
                    throw new Exception();
                }

                return semitone_tmp;

            }catch(Exception nhce){
                throw new PC_Exception("Incorrect format - " + ((Object)pitch).toString());
            }
        }

        if(PC.checkPC((int)pitch)){
            throw new PC_Exception("Incorrect format - " + ((Object)pitch).toString());
        }
        
        return (int)pitch;
    }


    /*
     * Methods
     */

    /**
     * This function, given two PC, returns the PCI (PC interval).
     * 
     * @param pc_a      the first PC
     * @param pc_b      the second PC
     * @return          the PCI as an int; this value will be
     *                  between 0 and 11.
     * @throws          PC_Exception
     *
     * @see PC#init(T pitch)
     */
    public static <T> int interval(T pc_a, T pc_b) throws PC_Exception{
        int pc_a_int = PC.init(pc_a);
        int pc_b_int = PC.init(pc_b);

        int PCI_ab = pc_b_int - pc_a_int;

        PCI_ab = (0 > PCI_ab) ? (PCI_ab + NoteMatcher.semitones_num) : PCI_ab;

        return PCI_ab%EnumMatcher.PC_MODULE.getValue();
    }


    /**
     * This function, given two PC, returns the PCI' (PC interval inverse).
     * 
     * @param pc_a      the first PC
     * @param pc_b      the second PC
     * @return          the PCI' as an int; this value will be
     *                  between 0 and 11.
     * @throws          PC_Exception
     *
     * @see PC#interval(T pc_a, T pc_b)
     */
    public static <T> int intervalInversion(T pc_a, T pc_b) throws PC_Exception{
        return (NoteMatcher.semitones_num - PC.interval(pc_a, pc_b))%NoteMatcher.semitones_num;
    }


    /**
     * This function, given a PCI, returns his PCI' (PC interval inverse).
     * 
     * @param pci       pc interval
     * @return          the PCI' as an int; this value will be
     *                  between 0 and 11.
     * @throws          PC_Exception
     *
     * @see PC#intervalInversion(T pc_a, T pc_b)
     */
    public static <T> int intervalInversion(T pci) throws PC_Exception{
        return PC.intervalInversion(0, pci);
    }


    /**
     * This function, given two PC, returns the IC (Interval Class).
     * 
     * @param pc_a      the first PC
     * @param pc_b      the second PC
     * @return          the IC as an int; this value will be
     *                  between 0 and 11.
     * @throws          PC_Exception
     *
     * @see PC#interval(T pc_a, T pc_b)
     * @see PC#intervalInversion(T pci)
     */
    public static <T> int IC(T pc_a, T pc_b) throws PC_Exception{
        int pc_a_int = PC.init(pc_a);
        int pc_b_int = PC.init(pc_b);

        int pci = PC.interval(pc_a, pc_b);
        int pci_inv = PC.intervalInversion(pci);
        return (pci <= pci_inv) ? pci : pci_inv;
    }


    /**
     * This function transposes the PC forward or backward by transposition
     * 
     * @param pc                is the pitch class value
     * @param transposition     is the value by whitch you want to transpose
     *                          the pitch
     * @return                  the new pitch class value, as integer
     * @throws                  PC_Exception
     *
     * @see PC#init(T pitch)
     * @see NoteMatcher#findMatcher(int index, T transposition)
     */
    public static <T> int transpose(T pc, T transposition) throws PC_Exception{
        int pc_i = PC.init(pc);
        int index_trans = NoteMatcher.findMatcher(EnumMatcher.INTS_NEGATIVE.getValue(), transposition);
        int transp_int;

        if(EnumMatcher.NOT_FOUND.getValue() == index_trans){
            throw new PC_Exception("Incorrect transposition format - " + ((Object)transposition).toString());
        }

        if (transposition instanceof String){
            try{
                transp_int = Integer.parseInt((String)transposition);
            }catch(NumberFormatException nfe){
                throw new PC_Exception("Incorrect transposition format - " + ((Object)transposition).toString());
            }
        }else{
            transp_int = (int)transposition;
        }

        return PC.init((pc_i + transp_int)%NoteMatcher.semitones_num);
    }


    /*
     * Getter
     */

    /**
     * This function returns the name of the PCI interval
     * 
     * @param pc_a      the first PC
     * @param pc_b      the second PC
     * @return          a String with the name of the interval
     * @throws          PC_Exception
     * 
     * @see PC#interval(T pc_A, T pc_b) 
     * @see PC#getPCIName(int pci) 
     */
    public static <T> String getCIName(T pc_a, T pc_b) throws PC_Exception{
       return PC.getPCIName(PC.interval(pc_a, pc_b));
    }


    /**
     * This function returns the name of the PCI' interval
     * 
     * @param pc_a      the first PC
     * @param pc_b      the second PC
     * @return          a String with the name of the interval
     * @throws          PC_Exception
     *
     * @see PC#intervalInversion(T pc_A, T pc_b)
     * @see PC#getPCIName(int pci)
     */
    public static <T> String getCIinverseName(T pc_a, T pc_b) throws PC_Exception{
       return PC.getPCIName(PC.intervalInversion(pc_a, pc_b));
    }

    /**
     * This function returns the PC value
     * 
     * @param pc    is the pitch class value
     * @return      the value of the pitch
     * @throws      PC_Exception
     * 
     * @see PC#init(T pitch)
     */
    public static <T> int getPC(T pc) throws PC_Exception{
        return PC.init(pc);
    }


    /*
     * Conversions
     */

    /**
     * This function convert PC to NC
     * 
     * @param pc    is the pitch class value
     * @return      the NC value
     * @throws      PC_Exception
     *
     * @see PC#init(T pitch)
     * @see NoteHelpConverter#fromSemitonesToNote(T pitch)
     */
    public static <T> int convPCtoNC(T pc) throws PC_Exception{
        try{
            return NoteHelpConverter.fromSemitonesToNote(PC.init(pc));
        }catch(NoteHelpConverter_Exception nhce){
            throw new PC_Exception("Incorrect format - " + ((Object)pc).toString());
        }
    }


    /**
     * toString
     *
     * @throws PC_Exception
     *
     * @see PC#init(T pitch)
     */
    public static <T> String toString(T pc) throws PC_Exception{
        return "PC: " + PC.init(pc);
    }
}
