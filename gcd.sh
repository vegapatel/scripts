#!/bin/bash
echo "Enter List of all Numbers with a space in between to find GCF or HCF)"
read list
numArray=(${list})
len="${#numArray[@]}-1"

firstNum=${numArray[i]}
secondNum=${numArray[i+1]}
for ((i=0;i<=${len};i++));
do	
while [ "$secondNum" -ne "0" ];
do
	rem=`expr $firstNum % $secondNum`
	firstNum="$secondNum"
	secondNum="$rem"
done
firstNum=$firstNum
secondNum=${numArray[i+1]}
done
echo $firstNum
