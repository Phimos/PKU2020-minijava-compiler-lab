#! /bin/bash
javac Main.java
for file in ./test/lab2/*.java
do
    if test -f $file
    then
        echo $file
        java Main $file
        java -jar pgi.jar < ${file%.*}.pg > ${file%.*}.txt
        java -jar pgi.jar < ${file%.*}_my.pg > ${file%.*}_my.txt
        echo "Difference in file ${file%.*}:"
        diff ${file%.*}.txt ${file%.*}_my.txt
    fi
done
rm ./test/lab2/*.txt

