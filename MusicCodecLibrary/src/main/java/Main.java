import music_codec_lib.*;
import music_codec_lib.Exceptions.*;

public class Main{
    public static void main(String[] args){
        
        Pitch p;
        
        try{
            p = new Pitch("pc", 11);
            System.out.println(p.transpose(11));

        }catch(Pitch_Exception pe){
            System.out.println("err");
            System.exit(1);
        }
        

    }
}
