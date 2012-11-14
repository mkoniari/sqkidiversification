#!/usr/bin/perl 

open (IN,$ARGV[0]) or die "Can not open query file";
print  "<parameters>\n";

while(my $l=<IN>){
if ($l=~/(\d+\w):(.*)/){
print  "<query>\n";
print  "<number>$1</number>\n";
print  "<text>$2</text>\n";
print  "</query>\n";
}

}
print  "</parameters>\n";
close IN;

