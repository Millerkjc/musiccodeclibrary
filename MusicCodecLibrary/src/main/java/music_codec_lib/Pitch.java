package music_codec_lib;

import music_codec_lib.Exceptions.PC_Exception;
import music_codec_lib.Exceptions.Pitch_Exception;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.NoSuchMethodException;

public class Pitch{
    private Object pitch;
    private String codec;
    final String lib_name = "music_codec_lib";

    /**
     * Map the user input to the classname
     */
    private static HashMap<String, String> codecClass;
    static{
        codecClass = new HashMap<>();
        codecClass.put(   "nc",       "NC");
        codecClass.put(  "cnc",      "CNC");
        codecClass.put(   "pc",       "PC");
        codecClass.put(  "cpc",      "CPC");
        codecClass.put( "oppc",     "OPPC");
        codecClass.put("binom", "Binomial");
        codecClass.put(   "br",       "BR");
        codecClass.put(  "cbr",      "CBR");
    }
    private ArrayList<String> allClassCodec = new ArrayList<>(codecClass.keySet());


    /**
     * Specify which codec has the octave information
     */
    private static ArrayList<String> codecWithOctave;
    static{
        codecWithOctave = new ArrayList<>();
        codecWithOctave.add("cnc");
        codecWithOctave.add("cpc");
        codecWithOctave.add("oppc");
        codecWithOctave.add("cbr");
    }


    /**
     * It is used in addOctave method.
     * It maps the codec without octave information with the one
     * that you gain after add that information
     */
    private static HashMap<String, String> addOctaveHm;
    static{
        addOctaveHm = new HashMap<String, String>();
        addOctaveHm.put(   "nc", "cnc");
        addOctaveHm.put(   "pc", "cpc");
        addOctaveHm.put(   "br", "cbr");
        addOctaveHm.put("binom", "cbr");
    }


    /**
     * This function use the Java Reflection to search and execute the
     * requested method from a specific class.
     * If the class doesn't exist or implement the method, the function throws an error.
     * 
     * @param codec         is remapped on the class where the method will be executed
     * @param class_method  is the method that will be executed
     * @param args          are the variable arguments of the called method
     * @return              the result of the method. If any arror occurs, it throws an exception
     * @throws              Exception
     */
    @SuppressWarnings("unchecked")
    private <T> Object execMethod(String codec, String class_method, T...args) throws Exception{
        Class<?> c;

        try{
            c = Class.forName(lib_name + "." + codec);
        }catch(ClassNotFoundException cnfe){
            throw new ClassNotFoundException();
        }

        Method method;
        ArrayList<Class> params = new ArrayList<>();

        for(int i=0; i<args.length; i++){
            params.add(Object.class);
        }

        try {
            method = c.getMethod(class_method, params.toArray(new Class[0]));

        }catch(Exception e){
            throw new NoSuchMethodException();
        }

        try{
            return method.invoke(null, args);

        } catch (Exception e) {
            throw (Exception) e.getCause();
        }
    }

    /**
     * This function use the execMethod(..) to execute methods and
     * to intercept the errors to rewrite it in a better format
     *
     * @param codec         the current codec
     * @param class_method  the class that you want to execute
     * @param args          the methods arguments
     * @throws              Pitch_Exception
     *
     * @see #execMethod(String, String, Object[])
     */
    @SuppressWarnings("unchecked")
    private <T> Object execPitchMethodSafety(String codec, String class_method, T...args) throws Pitch_Exception{

        try{
            return execMethod(codecClass.get(codec), class_method, args);

        }catch(ClassNotFoundException e){
            throw new Pitch_Exception("Problem with the library: " + lib_name);

        }catch(NoSuchMethodException e){
            throw new Pitch_Exception("The current codec ('" + codec + "') doesn't support " + class_method + " functionality");

        }catch(Exception e){
            throw new Pitch_Exception("Error executing " + class_method + " in codec ('" + codec + "')" +
                                      "; Reason: " + e.getMessage());
        }

    }

    /**
     * This function use the execMethod(..) to execute conversion methods and
     * to intercept the errors to rewrite it in a better format
     *
     * @param from          the current codec
     * @param to            the codec you want in return
     * @param class_method  the class that you want to execute
     * @param args          the methods arguments
     * @throws              Pitch_Exception
     *
     * @see #execMethod(String, String, Object[])
     */
    @SuppressWarnings("unchecked")
    private <T> Object execConversionSafety(String from, String to, String class_method, T...args) throws Pitch_Exception{
        String convFrom = codecClass.get(from);
        String convTo = codecClass.get(to);

        try{
            return execMethod(convFrom, String.format(class_method, convFrom, convTo), args);

        }catch(ClassNotFoundException e){
            throw new Pitch_Exception("The current codec ('" + from + "') doesn't support " + String.format(class_method, convFrom, convTo) + " functionality");

        }catch(Exception e){
            throw new Pitch_Exception("Error executing " + String.format(class_method, convFrom, convTo) + " in codec ('" + codec + "')" +
                                      "; Reason: " + e.getMessage());
        }
    }






    /**
     * Class constructor
     *
     * @param codec     the pitch codec
     * @param args      the pitch value
     * @throws          Pitch_Exception
     *
     * @see #execPitchMethodSafety(String, String, Object[]) 
     */
    @SuppressWarnings("unchecked")
    public <T> Pitch(String codec, T...args) throws Pitch_Exception{
        String low_codec = codec.toLowerCase();

        if(!allClassCodec.contains(low_codec)){
            throw new Pitch_Exception("No codec found");
        }

        this.codec = low_codec;
        this.pitch = execPitchMethodSafety(this.codec, "init", args);
    }


    /*
     * Methods
     */

    /**
     * This function returns the interval from the current pitch
     * and the other inserted as argument, throws an error if the pitch
     * doesn't contain the information
     *
     * @param  other_pitch
     * @throws Pitch_Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T interval(T other_pitch) throws Pitch_Exception{
        Object ret = execPitchMethodSafety(this.codec, "interval", this.pitch, other_pitch);

        if (ret instanceof Integer[]){
            return (T)(Integer[])ret;
        }
        return (T)(Integer)ret;
    }


    /**
     * This function returns the interval inversion from the current
     * pitch, throws an error if the pitch doesn't contain the information
     *
     * @throws Pitch_Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T intervalInversion() throws Pitch_Exception{
        Object ret = execPitchMethodSafety(this.codec, "intervalInversion", this.pitch);

        if (ret instanceof Integer[]){
            return (T)(Integer[])ret;
        }
        return (T)(Integer)ret;
    }


    /**
     * This function returns the interval inversion from the current pitch
     * and the other inserted as argument, throws an error if the pitch
     * doesn't contain the information
     *
     * @param  other_pitch
     * @throws Pitch_Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T intervalInversion(T other_pitch) throws Pitch_Exception{
        Object ret = execPitchMethodSafety(this.codec, "intervalInversion", this.pitch, other_pitch);

        if (ret instanceof Integer[]){
            return (T)(Integer[])ret;
        }
        return (T)(Integer)ret;
    }


    /**
     * This function returns the IC from the current pitch
     * and the other inserted as argument, throws an error if the pitch
     * doesn't contain the information
     *
     * @param  other_pitch
     * @throws Pitch_Exception
     */
    public <T> int IC(T other_pitch) throws Pitch_Exception{
        return (int)execPitchMethodSafety(this.codec, "IC", this.pitch, other_pitch);
    }


    /**
     * This function returns the current pitch transpose by the
     * transposition inserted as argument
     *
     * @param  transposition
     * @throws Pitch_Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T transpose(T transposition) throws Pitch_Exception{
        Object ret = execPitchMethodSafety(this.codec, "transpose", this.pitch, transposition);

        if (ret instanceof Integer[]){
            return (T)(Integer[])ret;
        }
        return (T)(Integer)ret;
    }

    /*
    @SuppressWarnings("unchecked")
    public <T> T transpose(T transpose_pc, T transpose_nc) throws Pitch_Exception{
        Object ret = execPitchMethodSafety(this.codec, "transpose", this.pitch, transpose_pc, transpose_nc);

        if (ret instanceof Integer[]){
            return (T)(Integer[])ret;
        }
        return (T)(Integer)ret;
    }
    */



    /*
     * Getter
     */

    /**
     * Returns the current codec value
     *
     * @return the current codec
     */
    public String getCodec(){
        return this.codec;
    }


    /**
     * Returns the current pitch value
     *
     * @return the current pitch
     */
    @SuppressWarnings("unchecked")
    public <T> T getPitch(){
        return (T)this.pitch;
    }


    /**
     * This function returns the CI name from the current pitch
     * and the other inserted as argument, throws an error if the pitch
     * doesn't contain the information
     *
     * @param  other_pitch
     * @throws Pitch_Exception
     */
    public <T> String getCIName(T other_pitch) throws Pitch_Exception{
        return (String)execPitchMethodSafety(this.codec, "getCIName", this.pitch, other_pitch);
    }


    /**
     * This function returns the CI inverse name from the current pitch
     * and the other inserted as argument, throws an error if the pitch
     * doesn't contain the information
     *
     * @param  other_pitch
     * @throws Pitch_Exception
     */
    public <T> String getCIinverseName(T other_pitch) throws Pitch_Exception{
        return (String)execPitchMethodSafety(this.codec, "getCIinverseName", this.pitch, other_pitch);
    }


    /**
     * Returns the Binomial pitch, throws an error if the pitch
     * doesn't contain the information
     *
     * @return the current Binomial pitch
     * @throws Pitch_Exception
     */
    public String getBinomPitch() throws Pitch_Exception{
        return (String)execPitchMethodSafety(this.codec, "getBinomPitch", this.pitch);
    }


    /**
     * Returns the Binomial interval, throws an error if the pitch
     * doesn't contain the information
     *
     * @return the current Binom interval
     * @throws Pitch_Exception
     */
    public String getBinomInterval() throws Pitch_Exception{
        return (String)execPitchMethodSafety(this.codec, "getBinomInterval", this.pitch);
    }


    /**
     * Returns the current pitch octave, throws an error if the pitch
     * doesn't contain the information
     *
     * @return the current octave
     * @throws Pitch_Exception
     */
    public int getOctave() throws Pitch_Exception{
        return (int)execPitchMethodSafety(this.codec, "getOctave", this.pitch);
    }


    /**
     * Returns the current NC value, throws an error if the pitch
     * doesn't contain the information
     *
     * @return the NC value
     * @throws Pitch_Exception
     */
    public int getNC() throws Pitch_Exception{
        return (int)execPitchMethodSafety(this.codec, "getNC", this.pitch);
    }


    /**
     * Returns the current PC value, throws an error if the pitch
     * doesn't contain the information
     *
     * @return the PC value
     * @throws Pitch_Exception
     */
    public int getPC() throws Pitch_Exception{
        return (int)execPitchMethodSafety(this.codec, "getPC", this.pitch);
    }


    /**
     * Returns the current BR value, throws an error if the pitch
     * doesn't contain the information
     *
     * @return the BR value
     * @throws Pitch_Exception
     */
    public int getBR() throws Pitch_Exception{
        return (int)execPitchMethodSafety(this.codec, "getBR", this.pitch);
    }


    /*
     * Conversions
     */

    /**
     * It converts the current pitch to a different codec
     * (it could be a loss of information, es. octave information)
     * 
     * @see #execPitchMethodSafety(String, String, Object[])
     */
    public void convertTo(String codec) throws Pitch_Exception{
        String low_codec = codec.toLowerCase();

        if(!allClassCodec.contains(low_codec)){
            throw new Pitch_Exception("No codec found");
        }

        if (this.codec == low_codec){
            return;
        }

        this.pitch = execConversionSafety(this.codec, low_codec, "conv%1$sto%2$s", this.pitch);
        this.codec = low_codec;
    }



    /**
     * Adding the octave to the current pitch and convert it
     * to the codec with the octave information, throws an error if
     * the pitch can't convert to that codec
     *
     * @see #execPitchMethodSafety(String, String, Object[])
     */
    public void addOctave(int octave) throws Pitch_Exception{
        if(codecWithOctave.contains(this.codec)){
            throw new Pitch_Exception("The current codec ('" + this.codec + "') already has octave information");
        }

        if((this.codec).equals("binom")){
            convertTo("br");
        }
        this.codec = addOctaveHm.get(this.codec);
        this.pitch = execPitchMethodSafety(this.codec, "init", this.pitch, octave);
    }


    /*
    // Future works
    public void changeOctave(int octave) throws Pitch_Exception{
        if(!checkCodec(PitchFunctionMaps.codecWithOctave, this.codec)){
            throw new Pitch_Exception("This codec doesn't have octave information");
        }        
        // TODO
    }

    public <T> void changePitch(T pitch){
        // TODO
    }

    */


    /**
     * toString
     * 
     * @see #execPitchMethodSafety(String, String, Object[])
     */
    public String toString(){
        try{
            return (String)execPitchMethodSafety(this.codec, "toString", this.pitch);
        }catch(Exception e){
            e.printStackTrace();
            return "Error in toString";
        }
    }

}
