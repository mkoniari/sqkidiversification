
for i in {1..150}
do
cat $1 | grep "^$i " | head -100

done
