// package music_codec_lib;
// 
// import music_codec_lib.Exceptions.RDC_Exception;
// import java.math.RoundingMode;
// import java.text.DecimalFormat;
// import java.util.ArrayList;
// import java.util.Arrays;
// 
// /*
//  * RDC - Reciprocal Duration Code
//  */
// public class RDC{
// 
//     /*
//      * Private, helper function
//      */
// 
//     private static ArrayList<Integer> acceptedDuration = new ArrayList<>(Arrays.asList(1, 2, 4, 8, 16, 32, 64, 128));
//     private static ArrayList<Double> acceptedDurationDouble;
//     static{
//         acceptedDurationDouble = new ArrayList<>();
//         try{
//             for(int ad : acceptedDuration){
//                 RDC.acceptedDurationDouble.add(durationInSeconds(1,ad));
//             }
//         }catch(Exception e){
//             e.printStackTrace();
//             System.out.println("Initialization duration error");
//         }
//     }
// 
//     private static int gcd(int num, int den){
//         num = ( num > 0) ? num : -num;
//         den = ( den > 0) ? den : -den;
// 
//         return (den == 0) ? num : RDC.gcd(den, num%den);
//     }
// 
//     private static Integer[] reduce_fraction(int num, int den){
//         int gcd_num_dem = RDC.gcd(num, den);
//         
//         return new Integer[]{num/gcd_num_dem, den/gcd_num_dem};
//     }
// 
//     // RDC format
//     private static <T> double durationInSeconds(T num, double denom) throws RDC_Exception{
//         if(num instanceof Integer){
//             return durationInSeconds(Double.valueOf((int)num)/denom);
//         }
//         return durationInSeconds((double)num/denom);
//     }
// 
//     private static <T> double durationInSeconds(double num) throws RDC_Exception{
//         DecimalFormat rdcFormat = new DecimalFormat("##.###");
//         rdcFormat.setRoundingMode(RoundingMode.DOWN);
// 
//         try{
//             return Double.parseDouble(rdcFormat.format(num));
//         }catch(NumberFormatException nfe){
//             throw new RDC_Exception("Incorrect format - " + ((Object)num).toString());
//         }
//     }
// 
// 
//     private static <T> double secondsPerBeat(T bpm) throws RDC_Exception {
//         int tmp_bpm = checkBPM(bpm);
// 
//         if (-1 == checkBPM(tmp_bpm)){
//             throw new RDC_Exception("bpm not valid - " + tmp_bpm);
//         }
// 
//         return ((double)60)/tmp_bpm*4;
//     }
// 
// 
//     private static <T> boolean checkAcceptedDuration(T bpm, T denom){
//         int index = NoteMatcher.findMatcher(denom);
// 
//         if(-1 == index){
//             return false;
//         }
//         
//         if(EnumMatcher.DECIMAL_NEGATIVE.getValue() > index){
//             return !(-1 == (RDC.acceptedDuration).indexOf(Math.abs((int)denom)));
//         }
// 
// 
//         double check_denom = (double)denom;
//         
//         for(double ad : RDC.acceptedDurationDouble){
//             while((double)check_denom >= ad){
//                 check_denom -= ad;
//             }
//         }
//         
//         return check_denom == 0;
//     }
// 
// 
//     private static <T> Integer[] checkNum(T num){
//         int indexNum = NoteMatcher.findMatcher(num);
// 
//         if (-1 == indexNum){
//             return new Integer[]{-1, 0};
//         }
// 
//         if (EnumMatcher.INTS.getValue() == indexNum || EnumMatcher.INTS_NEGATIVE.getValue() == indexNum){
//             if (num instanceof String){
//                 try{
//                     return new Integer[]{1, Integer.parseInt((String)num)};
//                 }catch(NumberFormatException nfe){
//                     return new Integer[]{-1, 0};
//                 }
//             }
// 
//             return new Integer[]{1, (int)num};
//         }
// 
//         return new Integer[]{-1, 0};
//     }
// 
// 
//     private static <T> Double[] checkDen(T den){
//         Double indexDen_int = Double.valueOf(NoteMatcher.findMatcher(EnumMatcher.INTS.getValue(), den));
//         Double indexDen_int_neg = Double.valueOf(NoteMatcher.findMatcher(EnumMatcher.INTS_NEGATIVE.getValue(), den));
//         Double indexDen_dec = Double.valueOf(NoteMatcher.findMatcher(EnumMatcher.DECIMAL_NEGATIVE.getValue(), den));
//         Double not_f = Double.valueOf(EnumMatcher.NOT_FOUND.getValue());
//         Double zero = Double.valueOf(EnumMatcher.ZERO.getValue());
// 
//         if (-1 == indexDen_int.intValue() && -1 == indexDen_int_neg.intValue() && -1 == indexDen_dec.intValue()){
//             return new Double[]{not_f, zero};
//         }
// 
//         if (den instanceof String){
//             try{
//                 if(-1 != indexDen_int.intValue() || -1 != indexDen_int_neg.intValue()){
//                     Double ret = (-1 != indexDen_int.intValue()) ? indexDen_int : indexDen_int_neg;
//                     return new Double[]{ret, Double.parseDouble((String)den)};
//                 }
//                 return new Double[]{indexDen_dec, Double.parseDouble((String)den)};
//             }catch(NumberFormatException nfe){
//                 return new Double[]{not_f, zero};
//             }
//         }
// 
// 
//         if(-1 != indexDen_int.intValue() || -1 != indexDen_int_neg.intValue()){
//             Double ret = (-1 != indexDen_int.intValue()) ? indexDen_int : indexDen_int_neg;
//             return new Double[]{ret, Double.valueOf((int)den)};
//         }
//         return new Double[]{Double.valueOf(indexDen_dec), Double.valueOf((double)den)};
// 
//     }
// 
// 
//     /*
//      * Init
//      */
// 
//     /**
//      * Given a tempo(bpm) and denominator of the fraction, it returns the rappresentation
//      * of RDC (Reciproval Duration Code) as a double.
//      * The default value of numerator is set to 1.
//      * 
//      * @param bpm           (beat per minute) an int/String that indicates the tempo of the notes/pitches/melody
//      * @param denominator   an int/String that indicates the denominator of the fraction
//      * @return              tempo as a double
//      */
//     public static <T> Double init(T bpm, T denominator) throws RDC_Exception{
//         return RDC.init(bpm, 1, denominator);
//     }
// 
// 
//     /**
//      * @param bpm           (beat per minute) an int/String that indicates the tempo of the notes/pitches/melody
//      * @param numerator     an int/String that indicates the numerator of the fraction
//      * @param denominator   an int/String that indicates the denominator of the fraction
//      * @return              tempo as a double
//      */
//     public static <T> Double init(T bpm, T numerator, T denominator) throws RDC_Exception{
//         Integer[] num_check = checkNum(numerator);
//         if(-1 == num_check[0]){
//             throw new RDC_Exception("Numerator incorrect format. num. " + ((Object)numerator).toString());
//         }
// 
//         Double[] den_check = checkDen(denominator);
//         if(-1 == (den_check[0]).intValue()){
//             throw new RDC_Exception("Denominator incorrect format. den. " + ((Object)denominator).toString());
//         }
// 
//         int index_pulse = NoteMatcher.findMatcher(EnumMatcher.INTS.getValue(), bpm);
//         if(-1 == index_pulse){
//             throw new RDC_Exception("Pulse incorrect format. pulse. " + ((Object)bpm).toString());
//         }
// 
//         if (EnumMatcher.DECIMAL_NEGATIVE.getValue() == (den_check[0]).intValue()){
//             if(!checkAcceptedDuration(bpm, den_check[1])){
//                 throw new RDC_Exception("Incorrect value. " + ((Object)den_check[1]).toString());
//             }
//             return durationInSeconds(den_check[1]);
//         }
// 
//         return init_int(bpm, numerator, (den_check[1]).intValue());
// 
//     }
// 
// 
//     /**
//      * It helps the init method, in particular, init_int manages the RDC franction
//      * when the given denominator was an int.
//      * 
//      * @param bpm           (beat per minute) an int/String that indicates the tempo of the notes/pitches/melody
//      * @param numerator     an int/String that indicates the numerator of the fraction
//      * @param denominator   an int/String that indicates the denominator of the fraction
//      * @return              tempo as a double
//      */
//     private static <T> Double init_int(T bpm, T numerator, T denominator) throws RDC_Exception{
//         if(-1 == acceptedDuration.indexOf(Math.abs((int)denominator))){
//             throw new RDC_Exception("Denominator duration format not supported. den. " + ((Object)denominator).toString());
//         }
// 
//         Integer[] reduced_fraction = RDC.reduce_fraction((int)numerator, (int)denominator);
//         Double rdc_double = Double.valueOf(((double)reduced_fraction[0])/reduced_fraction[1]);
// 
//         return durationInSeconds(secondsPerBeat(bpm), reduced_fraction[1]);
//     }
// 
// 
// 
// 
// 
// 
//     /*
//      * Methods
//      */
// 
// 
//     /**
//      * It checks that the tempo(bpm) given was in the correct format to be
//      * used with the methods, else it returns -1.
//      * 
//      * @param bpm  (beat per minute) an int/String that indicates the tempo of the notes/pitches/melody
//      * @return     the int that represents the tempo of the notes/melody
//      */
//     // check BPM format
//     public static <T> int checkBPM(T bpm) throws RDC_Exception{
//         int index = NoteMatcher.findMatcher(bpm);
// 
//         if (EnumMatcher.NOT_FOUND.getValue() == index || EnumMatcher.INTS.getValue() != index){
//             throw new RDC_Exception("Incorrect format - " + ((Object)bpm).toString());
//         }
//         
//         int tmp_bpm;
// 
//         if(bpm instanceof String){
//             try{
//                 tmp_bpm = Integer.parseInt((String)bpm);
//             }catch(NumberFormatException nfe){
//                 throw new RDC_Exception("Incorrect format bpm - " + ((Object)bpm).toString());
//             }
//         }else{
//             tmp_bpm = (int)bpm;
//         }
// 
//         return (tmp_bpm > 0) ? tmp_bpm : -1;
//     }
// 
// 
// 
//     /*
//      * Getter
//      */
//      
// 
// 
//     /*
//      * toString
//      */
// 
//     public static <T> String toString(T bpm, T rdc) throws RDC_Exception{
//         return "RDC: " + RDC.init(bpm, rdc);
//     }
// 
// 
// 
// 
// /*
// 
// 
// 
// 
// 
// 
//     public static <T> double durationInSeconds(T denom) throws RDC_Exception{
//         return RDC(denom);
//     }
// 
//     private static <T> double durationInSeconds(int num, double denom) throws RDC_Exception{
//         DecimalFormat rdcFormat = new DecimalFormat("##.###");
//         rdcFormat.setRoundingMode(RoundingMode.DOWN);
// 
//         try{
//             return Double.parseDouble(rdcFormat.format((double)num/denom));
//         }catch(NumberFormatException nfe){
//             throw new RDC_Exception("Incorrect format - " + ((Object)denom).toString());
//         }
//     }
// */    
// }
