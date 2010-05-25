#!/bin/bash


#PBS -N testowe_zadanie
#PBS -q l_short
#PBS -l walltime=00:01:00
  


echo 'hello world'
#javac Hello.java
java Hello
echo 'wypisany parametr 1'



