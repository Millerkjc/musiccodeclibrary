package music_codec_lib.Exceptions;

public class CNC_Exception extends Exception{
    public CNC_Exception(String msg){
        super("CNC - " + msg);
    }
}
