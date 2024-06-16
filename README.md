# JNOTEPAD
Welcome! This is simple notepad in Java.\
First, what you should know, commands should be written like this - ">command<".\
Second - help command is ">help<".\
I hope, you will understand.

HOW TO OPEN PROGRAMM
--------------------
1) If you have Linux
    1. chmod +x jnotepad.jar
    2. ./jnotepad.jar [args]
2) Works on any System
    * java -jar jnotepad.jar [args]
    * java -classpath bin src.Main [args]

HOW TO BUILD PROGRAMM IF YOU MODEFIED IT
----------------------------------------
1) javac -classpath bin src.Main
2) Check file manifest.mf ( see  _HOW FILE manifest.mf SHOULD TO LOOKS_ ) \
3) jar -cmf manifest.mf jnotepad.jar -C bin .

HOW FILE manifest.mf SHOULD TO LOOKS
----------------------------------------
1) main-class: src.Main
2) class-path: bin