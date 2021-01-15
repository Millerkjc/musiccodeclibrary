package music_codec_lib;

public enum EnumMatcher{
    SEMITONES_PITCH(0),
    SEMITONES_PITCH_ANGLO(1),
    INTS(2),
    HELMHOLTZ_LOW(3),
    HELMHOLTZ_UP(4),
    ALL_PITCHES_AND_NOTE(5),
    INTS_NEGATIVE(6),
    DECIMAL_NEGATIVE(7),
    
    ZERO(0),
    SEMITONES(2),
    HELMHOLTZ_ALL(5),
    
    NUMBERS_EXPANSION(6),
    NC_MODULE(7),
    PC_MODULE(12),
    NOT_FOUND(-1),

    END(8);



    private int type_of_dict;
    
    private EnumMatcher(int type_of_dict){
        this.type_of_dict = type_of_dict;
    }
    
    public int getValue(){
        return type_of_dict;
    }
}
