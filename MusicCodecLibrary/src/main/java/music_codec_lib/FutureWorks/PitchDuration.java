// package music_codec_lib;
// 
// import music_codec_lib.Exceptions.Pitch_Exception;
// import music_codec_lib.Exceptions.PitchDuration_Exception;
// import java.util.ArrayList;
// import java.util.HashMap;
// 
// /**
//  * Pitch with Duration information
//  * @see Pitch
//  */
// public class PitchDuration extends Pitch{
//     private Object bpm;
//     private Object duration;
//     
//     /*
//      * Private, helper function
//      */
//     
//     private static HashMap<String, String> codecDurationClass;
//     static{
//         codecDurationClass = new HashMap<>();
//         codecDurationClass.put("rdc", "RDC");
//     }
//     private ArrayList<String> allDurationClassCodec = new ArrayList<>(codecDurationClass.keySet());
// 
// 
//     /**
//      * This function use the Pitch.execMethod(..) to execute the method and
//      * intercept the errors to rewrite it in a better format
//      * 
//      * @see Pitch.execMethod(...)
//      */
//     @SuppressWarnings("unchecked")
//     private <T> Object execPitchDurationMethodSafety(String classname, String class_method, T...args) throws PitchDuration_Exception, Pitch_Exception{
//         try{
//             return super.execMethod(codecDurationClass.get(classname), class_method, args);
//         }catch(Exception e){
//             throw new PitchDuration_Exception("The current codec ('" + classname + "') doesn't support " + class_method + " functionality");
//         }
//     }
// 
// 
//     /**
//      * Check the tempo given, using the RDC class method.
//      * 
//      * @param bpm   tempo of the specific pitch
//      * @return      the bpm only if it is in the correct form
//      */
//     private <T> int initBpm(T bpm) throws PitchDuration_Exception, Pitch_Exception{
//         @SuppressWarnings("unchecked")
//         int bpm_tmp = (int)execPitchDurationMethodSafety("rdc", "checkBPM", bpm);
//         if(-1 == bpm_tmp){
//             throw new PitchDuration_Exception("Incorrect format bpm - " + ((Object)bpm).toString());
//         }
// 
//         return bpm_tmp;
//     }
// 
//     
//     /**
//      * Class constructor
//      * 
//      * @param bpm       tempo of the specific pitch
//      * @param duration  duration of the specific pitch
//      * @param codec     codec of the pitch
//      * @param args      arguments for the various codec constructor
//      * 
//      * @see Pitch
//      */
//     @SuppressWarnings("unchecked")
//     public <T> PitchDuration(T bpm, T duration, String codec, T...args) throws Exception{
//         super(codec, args);
//         this.bpm = initBpm(bpm);
//         this.duration = execPitchDurationMethodSafety("rdc", "init", bpm, duration);
//     }
// 
// 
// 
//     /*
//      * Setter
//      */
//     public <T> void setBpm(T bpm) throws PitchDuration_Exception, Pitch_Exception{
//         this.bpm = initBpm(bpm);
//     }
// 
//     public <T> void setDuration(T duration) throws PitchDuration_Exception, Pitch_Exception{
//         this.duration = execPitchDurationMethodSafety("rdc", "init", this.bpm, duration);
//     }
// 
//     // TOSO
//     /*
//     public <T> void changeBpmAndDuration(T bpm) throws PitchDuration_Exception, Pitch_Exception{
//         this.bpm = initBpm(bpm);
//     }
//     */
// 
// 
// 
//     /*
//      * Getter
//      */
// 
//     public int getBpm(){
//         return (int)this.bpm;
//     }
// 
//     public double getDuration(){
//         return (double)this.duration;
//     }
// 
// 
//     /*
//      * toString
//      */
// 
//     public String toString(){
//         try{
//             String pitch_str = super.toString() + "\n";
//             return pitch_str + (String)execPitchDurationMethodSafety("rdc", "toString", this.bpm, this.duration);
//         }catch(Exception e){
//             e.printStackTrace();
//             return "Error in toString ";
//         }
//     }
// 
// 
// }
