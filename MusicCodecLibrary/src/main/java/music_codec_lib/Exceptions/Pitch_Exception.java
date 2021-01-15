package music_codec_lib.Exceptions;

public class Pitch_Exception extends Exception{
    public Pitch_Exception(String msg){
        super("Pitch - " + msg);
    }
}
