package music_codec_lib.Exceptions;

public class PC_Exception extends Exception{
    public PC_Exception(String msg){
        super("PC - " + msg);
    }
}
