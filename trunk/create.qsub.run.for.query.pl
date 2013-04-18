#! /usr/bin/perl

sub trim($);
open (TP,"topic.trec.stemmed") or die "cannot open trec topic file \n";
while (my $l=<TP>){
        my $ll=trim($l);
        if ($ll=~/(\d+):(.*)/){
                my $q=$2;
                my $topic=$1;
                if ($topic eq $ARGV[0]){
                	$query=$q;	
		}
        }
}

close TP;

print "#PBS -N Code-Compile\n";
print "#PBS -l nodes=1:ppn=2,walltime=10:00:00\n";
#print "#PBS -M sadegh.kharazmi@gmail.com\n";
print "#PBS -m abe\n";
print "cd \$PBS_WORKDIR\n";
print "time java -Xms100M -Xmx4000M -Djava.library.path=/scratch/sadegh/Toolkits/indri-5.3/swig/obj/java/ -classpath /scratch/sadegh/source/sqkidiversification:/scratch/sadegh/source/sqkidiversification/src:/scratch/sadegh/Toolkits/indri-5.3/swig/obj/java/ com.sqki.net.Main $ARGV[0] /scratch/sadegh/source/sqkidiversification/baseline/tesTclueweb $ARGV[1] $ARGV[2] \"$query\" $ARGV[3] > /scratch/sadegh/source/sqkidiversification/temp-result/$ARGV[0]-mmr-$ARGV[1]-W$ARGV[3] \n";


sub trim($)
{
	my $string = shift;
	$string =~ s/^\s+//;
	$string =~ s/\s+$//;
	return $string;
}
