#! /bin/bash
javac Main.java
for file in ./test/lab3/*.pg
do
    if test -f $file
    then
        echo $file
        java Main $file
        java -jar spp.jar < ${file%.*}_my.spg
    fi
done

