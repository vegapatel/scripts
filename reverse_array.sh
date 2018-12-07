#!/bin/bash

array=(1 4 6 4 3 2 1 10 29 3 -1 -2 32 -3 45 -56 929 20021)
min=0
max=$((${#array[@]}-1))

while [[ $min -lt $max ]]
do
	temp="${array[$min]}"
	array[$min]="${array[$max]}"
	array[$max]="${temp}"
	((min++,max--))
done
echo ${array[@]}

#sort an array
for ((i=0; i<="$((${#array[@]}-2))"; ++i))
 do
   for ((j="$((i+1))"; j<="$((${#array[@]}-1))"; ++j))
        do
	    if [[ ${array[i]} -eq ${array[j]} ]]
            then
		    unset array[$j]
            elif [[ ${array[i]} -gt ${array[j]} ]]
            then
                tmp=${array[i]}
                array[i]=${array[j]}
                array[j]=$tmp         
	    fi

        done
    done

echo ${array[@]}

######sort desending
for ((i=0; i<="$((${#array[@]}-2))"; ++i))
 do
   for ((j="$((i+1))"; j<="$((${#array[@]}-1))"; ++j))
        do
	    if [[ ${array[i]} -eq ${array[j]} ]]
            then
		   unset array[$j]
            elif [[ ${array[i]} -lt ${array[j]} ]]
            then
                tmp=${array[i]}
                array[i]=${array[j]}
                array[j]=$tmp         
            fi

        done
    done

echo ${array[@]}

#############palindrome

num=554544335

rem=0
reverse=""
temp=$num

while [ $num -gt 0 ]
do
    rem=$(( $num % 10 ))

    num=$(( $num / 10 ))

    reverse=$( echo ${reverse}${s} )
done

if [ "${temp}" == "${reverse}" ];
then
    echo "Number is palindrome"
else
    echo "Number is NOT palindrome"
fi

#####least value
min_num="${array[$min]}"
for num in ${array[@]}; do
        ((num < min_num)) && min_num=${num}
done
echo $min_num

#####find highest value

max_num="${array[$min]}"
for num in ${array[@]}; do
	((num > max_num)) && max_num=${num}
done
echo $max_num
