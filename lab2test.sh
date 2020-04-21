#! /bin/bash
javac Main.java
for file in ./test/lab2/*.java
do
    if test -f $file
    then
        echo $file
        java Main $file
        java -jar spp.jar < ${file%.*}_my.pg
    fi
done
rm ./test/lab2/*.txt

