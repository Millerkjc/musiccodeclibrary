# Music Codec Library

Music Codec Library is a Java library for dealing with different pitch codecs.\
All the codec classes below could work indipendently and without any instantiation, because all their public methods are static.\
The `Pitch` class is a wrapper of all those codecs and it can be instantiated and used to link them.

## Classes

#### Pitch codecs supported
**Without octave information**
* [PC](MusicCodecLibrary/src/main/java/music_codec_lib/PC.java)
* [NC](MusicCodecLibrary/src/main/java/music_codec_lib/NC.java)
* [Binomial](MusicCodecLibrary/src/main/java/music_codec_lib/Binomial.java)
* [BR](MusicCodecLibrary/src/main/java/music_codec_lib/BR.java)

**With octave information**
* [OPPC](MusicCodecLibrary/src/main/java/music_codec_lib/OPPC.java)
* [CPC](MusicCodecLibrary/src/main/java/music_codec_lib/CPC.java)
* [CNC](MusicCodecLibrary/src/main/java/music_codec_lib/CNC.java)
* [CBR](MusicCodecLibrary/src/main/java/music_codec_lib/CBR.java)

#### Wrappers
* [Pitch](MusicCodecLibrary/src/main/java/music_codec_lib/Pitch.java) - wrapper for the pitch codecs

#### Other classes
* [EnumMatcher](MusicCodecLibrary/src/main/java/music_codec_lib/EnumMatcher.java) - Class that contains all the enum values
* [NoteMatcher](MusicCodecLibrary/src/main/java/music_codec_lib/NoteMatcher.java) - Class that contains the regex used to match the pitches
* [NoteHelpConverter](MusicCodecLibrary/src/main/java/music_codec_lib/NoteHelpConverter.java) - Class that contains some common pitch operations

#### Pitch exceptions
* [PC\_Exception](MusicCodecLibrary/src/main/java/music_codec_lib/Exceptions/PC_Exception.java)
* [CPC\_Exception](MusicCodecLibrary/src/main/java/music_codec_lib/Exceptions/CPC_Exception.java)
* [NC\_Exception](MusicCodecLibrary/src/main/java/music_codec_lib/Exceptions/NC_Exception.java)
* [CNC\_Exception](MusicCodecLibrary/src/main/java/music_codec_lib/Exceptions/CNC_Exception.java)
* [OPPC\_Exception](MusicCodecLibrary/src/main/java/music_codec_lib/Exceptions/OPPC_Exception.java)
* [Binomial\_Exception](MusicCodecLibrary/src/main/java/music_codec_lib/Exceptions/Binomial_Exception.java)
* [BR\_Exception](MusicCodecLibrary/src/main/java/music_codec_lib/Exceptions/BR_Exception.java)
* [CBR\_Exception](MusicCodecLibrary/src/main/java/music_codec_lib/Exceptions/CBR_Exception.java)
* [Pitch\_Exception](MusicCodecLibrary/src/main/java/music_codec_lib/PExceptions/itch_Exception.java)
* [NoteMatcher\_Exception](MusicCodecLibrary/src/main/java/music_codec_lib/Exceptions/NoteMatcher_Exception.java)
* [NoteHelpConverter\_Exception](MusicCodecLibrary/src/main/java/music_codec_lib/Exceptions/NoteHelpConverter_Exception.java)

## Tests
All JUnit tests could be found under the following directory: [Tests](MusicCodecLibrary/src/test/java/music_codec_lib/)\
JUnit version: 5.6.3

## How it works

All methods of the codec classes are static so you can use them without creating objects.
All project classes try to follow a template for the methods; this is to ensure a consistent structure is maintained when expanding the project.
All the methods can accept different numbers of arguments.\
The class template is as follows:

```
init              -> used to initialize the current codec pitch
interval          -> used to return the interval
intervalInversion -> used to return the interval inversion
IC                -> used to return the CI
transpose         -> used to transpose the pitch
getCIName         -> used to return the CI name
getCIinverseName  -> used to return the CI inverse name
getBinomPitch     -> used to return the pitch from the Binomial codec
getBinomInterval  -> used to return the Binomial interval name
getOctave         -> used to return the octave of the pitch
getNC             -> used to return the NC pitch codec
getPC             -> used to return the PC pitch codec
getBR             -> used to return the BR pitch codec
toString          -> Override toString
```

An example of how to use the static methods:

```java
import music_codec_lib.*;

public class libTest{
    public static void main(String[] args) throws Exception{
        System.out.println(PC.init(3));
    }
}
```


For different scenarios, the `Pitch` class is a wrapper of all codec classes; this class could be instantiated.
For most scenarios the Pitch class static methods are enough, however for more complex operations is it also possible to instantiate the class to access some more advanced functionalities.\
Two of the major features are listed below:

```
convertTo         -> used to convert the current pitch in another notation
addOctave         -> used to insert the ocatave information to the pitch and to convert it to the new codec
```

The image below shows the complete list of conversions and codec changes through the `addOctave` function.

### Conversions
![alt text](/imgs_and_docs/codec_conversions.png "Pitch conversions")


An example of how to use the `Pitch` class:

```java
import music_codec_lib.*;

public class libTest{
    public static void main(String[] args) throws Exception{
        Pitch pc = new Pitch("pc", 3);
        System.out.println(pc.toString());
    }
}
```

### Logical Pitch structure
![alt text](/imgs_and_docs/Pitch_class.png "Logical Pitch structure")

## Future works
#### Other codecs
* [Helmholtz](MusicCodecLibrary/src/main/java/music_codec_lib/FutureWorks/NoteHelmholtz.java)
#### Pitch duration codecs
* [RDC](MusicCodecLibrary/src/main/java/music_codec_lib/FutureWorks/RDC.java)

#### Wrappers
* [PitchDuration](MusicCodecLibrary/src/main/java/music_codec_lib/FutureWorks/PitchDuration.java) - extension of `Pitch`, it also manages pitch duration information

### Logical PitchDuration structure
![alt text](/imgs_and_docs/PitchDuration_class.png "Logical PitchDuration structures")
