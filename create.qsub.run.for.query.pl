#! /usr/bin/perl
print "#PBS -N Code-Compile\n";
print "#PBS -l nodes=1:ppn=2,walltime=10:00:00\n";
#print "#PBS -M sadegh.kharazmi@gmail.com\n";
print "#PBS -m abe\n";
print "cd \$PBS_WORKDIR\n";
print "time java -Xms100M -Xmx4000M -Djava.library.path=/scratch/sadegh/Toolkits/indri-5.3/swig/obj/java/ -classpath /scratch/sadegh/source/sqkidiversification:/scratch/sadegh/source/sqkidiversification/src:/scratch/sadegh/Toolkits/indri-5.3/swig/obj/java/ com.sqki.net.Main $i /scratch/sadegh/source/sqkidiversification/baseline/tesTclueweb 0.5 > /scratch/sadegh/source/sqkidiversification/temp-result/$ARGV[0] \n";


