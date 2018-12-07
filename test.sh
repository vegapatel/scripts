#!/bin/bash

str='hello sadaskldj isadlasdlk'
array=(`echo "${str}" | grep -o .`)
echo ${array[@]}

line='woe wweo sadd   asdasd asd sad '
#read -a arr <<< $line
arr=($line)
echo ${arr[@]}


#####palindrome
#!/bin/bash
read -p "Enter a string: " string
if [[ $(rev <<< "$string") == "$string" ]]; then
    echo Palindrome
fi

########fibonacci

N=6
a=0
b=1

echo "The Fibonacci series is : "

for (( i=0; i<N; i++ ))
do
    echo -n "$a "
    fn=$((a + b))
    a=$b
    b=$fn
done

cat file | grep -o -m 1 fhl
