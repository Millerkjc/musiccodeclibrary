package music_codec_lib.Exceptions;

public class NC_Exception extends Exception{
    public NC_Exception(String msg){
        super("NC - " + msg);
    }
}
