# TheFlasher
Coding Standards

Branches should be named feature/ or bugfix/ followed by the Trello story number and then your branch name in Kebab Case.  Everything should be lower case.
Examples: feature/tfa-001-start-application-builds or bugfix/tfa-002-fix-app-blowing-up

You must have a Pull Request reviewed, approved, and (tested?) prior to merging.

Taken from Wikipedia (https://en.wikipedia.org/wiki/Naming_convention_(programming)):
In Java, naming conventions for identifiers have been established and suggested by various Java communities such as Sun Microsystems,[17] Netscape,[18] AmbySoft,[19] etc. A sample of naming conventions set by Sun Microsystems are listed below, where a name in "CamelCase" is one composed of a number of words joined without spaces, with each word's initial letter in capitals — for example "CamelCase".

Identifier types
Classes	- Class names should be nouns in UpperCamelCase, with the first letter of every word capitalised. Use whole words — avoid acronyms and abbreviations (unless the abbreviation is much more widely used than the long form, such as URL or HTML)  
    class Raster {}  
    class ImageSprite {}  
    
Methods	- Methods should be verbs in lowerCamelCase or a multi-word name that begins with a verb in lowercase; that is, with the first letter lowercase and the first letters of subsequent words in uppercase.  
    run();  
    runFast();  
    getBackground();  
    
Variables -	Local variables, instance variables, and class variables are also written in lowerCamelCase. Variable names should not start with underscore (_) or dollar sign ($) characters, even though both are allowed. This is in contrast to other coding conventions that state that underscores should be used to prefix all instance variables.
Variable names should be short yet meaningful. The choice of a variable name should be mnemonic — that is, designed to indicate to the casual observer the intent of its use. One-character variable names should be avoided except for temporary "throwaway" variables. Common names for temporary variables are i, j, k, m, and n for integers; c, d, and e for characters.  
    int i;  
    char c;  
    float myWidth;  
   
Constants	Constants should be written in uppercase characters separated by underscores. Constant names may also contain digits if appropriate, but not as the first character.  
    static final int MAX_PARTICIPANTS = 10;  
