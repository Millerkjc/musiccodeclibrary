package music_codec_lib.Exceptions;

public class RDC_Exception extends Exception{
    public RDC_Exception(String msg){
        super("RDC - " + msg);
    }
}
