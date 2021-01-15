package music_codec_lib.Exceptions;

public class PitchDuration_Exception extends Exception{
    public PitchDuration_Exception(String msg){
        super("PitchDuration - " + msg);
    }
}
