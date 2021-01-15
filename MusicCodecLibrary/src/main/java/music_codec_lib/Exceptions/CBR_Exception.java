package music_codec_lib.Exceptions;

public class CBR_Exception extends Exception{
    public CBR_Exception(String msg){
        super("CBR - " + msg);
    }
}
