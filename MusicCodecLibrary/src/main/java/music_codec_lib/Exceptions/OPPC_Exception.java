package music_codec_lib.Exceptions;

public class OPPC_Exception extends Exception{
    public OPPC_Exception(String msg){
        super("OPPC - " + msg);
    }
}
