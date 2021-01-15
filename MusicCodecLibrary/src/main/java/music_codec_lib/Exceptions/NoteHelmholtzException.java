package music_codec_lib.Exceptions;

public class NoteHelmholtzException extends Exception{
    public NoteHelmholtzException(String msg){
        super("Helmholtz - " + msg);
    }
}
