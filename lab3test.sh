#! /bin/bash
javac Main.java
for file in ./test/lab3/*.pg
do
    if test -f $file
    then
        echo $file
        java Main $file
        java -jar spp.jar < ${file%.*}_my.spg
        java -jar pgi.jar < ${file%.*}_my.spg > ${file%.*}_my.txt
        java -jar pgi.jar < ${file%.*}.spg > ${file%.*}.txt
        echo "Difference:"
        diff ${file%.*}.txt ${file%.*}_my.txt
    fi
done
rm ./test/lab3/*.txt
