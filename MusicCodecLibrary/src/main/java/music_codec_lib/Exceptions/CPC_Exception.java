package music_codec_lib.Exceptions;

public class CPC_Exception extends Exception{
    public CPC_Exception(String msg){
        super("CPC - " + msg);
    }
}
