#-*- coding: iso-8859-2 -*-

from ctypes import *

import sys
import re
from collections import defaultdict



__author__="krzysztof"
__date__ ="$May 3, 2010 10:08:25 PM$"

__filein__="Miecz.iso"

CLPLIB = CDLL("/usr/local/clp/lib/libclp.so")		# Ladowanie biblioteki CLP
CLPLIB.clp_ver.restype = c_char_p	# Ustawienie typu zwracanego przez funkcje clp_ver
Array50 = c_int * 50			# Typ pomocniczy dla buforow (tablic) int

def plp_init():
	"""Inicjalizuje bibiloteke CLP"""
	CLPLIB.clp_init(0)

def plp_ver():
	"""Zwraca napis z numerem wersji CLP"""
	return CLPLIB.clp_ver()

def plp_rec(word):
	"""Zwraca liste numerow ID dla danego slowa"""
	ids = Array50()
	num = c_int(0)
	CLPLIB.clp_rec(word, ids, byref(num))
	return ids[0:num.value]

def plp_label(id):
	"""Zwraca etykiete dla danego ID"""
	label = create_string_buffer(10)
	CLPLIB.clp_label(c_int(id), label)
	return label.value

def plp_bform(id):
	"""Zwraca etykiete dla danego ID"""
	bform = create_string_buffer(80)
	CLPLIB.clp_bform(c_int(id), bform)
	return bform.value

def plp_forms(id):
	"""Zwraca liste form dla danego wyrazu"""
	formy = create_string_buffer(2048)
	CLPLIB.clp_forms(c_int(id), formy)
	return formy.value.split(':')[0:-1]

def plp_vec(id, word):
	"""Zwraca wector odmiany"""
	out = Array50()
	num = c_int(0)
	CLPLIB.clp_vec(c_int(id), word, out, byref(num))
	return out[0:num.value]

def plp_pos(id):
    return CLPLIB.clp_pos(c_int(id))

plp_init()

wyglad_zewnetrzny = [];

inp = open("wyglad",'r');

for line in inp:
    print plp_rec(line[0:len(line)-1]);
    wyglad_zewnetrzny.append(plp_rec(line[0:len(line)-1])[0]);

inp.close();

usposobienie = [];

inp = open("usposobienie",'r');

for line in inp:
    print plp_rec(line[0:len(line)-1]);
    usposobienie.append(plp_rec(line[0:len(line)-1])[0]);

inp.close();


print "zażółć gęślą jaźń"
bohaterowie = {};

class Dane:
    czasownik = ''
    przymiotnik = ''
    linia = ''

    def __init__(self, czas, przy, lin):
        self.czasownik = czas;
        self.przymiotnik = przy;
        self.linia = lin;
        
    def tostring(self):
        return self.czasownik + " " + self.przymiotnik + " " + self.linia;


def znajdz_bohaterow():

    input = open(__filein__,'r');

    ilosc_wszystkich_slow = 0

    for line in input:
        str = line; #input.read();
        #print str;
        p = re.compile(r'[-,.!\?:;"()\']*\s+[-,.!\?:;"()\']*|^[-,.!\?:;"()\']+|[-,.!\?:;"()\']*[,.!\?:;"()\']+[-,.!\?:;"()\']*');
        words = p.split(str);

        for word in words:
            if word != "":
                if not plp_rec(word) and word[0:1].isupper():
                    try:
                        bohaterowie[word] = [];
                    except KeyError:
                        bohaterowie[word] = [];
    input.close();


def znajdz_cechy():

    print "not implemented yet"

    input = open(__filein__,'r');



    bohater = '';
    czasownik = '';
    cecha = '';
    ilosc_wszystkich_slow = 0

    for line in input:
        str = line; #input.read();
        #print str;
        p = re.compile(r'[-,.!\?:;"()\']*\s+[-,.!\?:;"()\']*|^[-,.!\?:;"()\']+|[-,.!\?:;"()\']*[,.!\?:;"()\']+[-,.!\?:;"()\']*');
        words = p.split(str);
    #  print "------------words:";
        for word in words:
       # print word;
            if word != "":
                if not plp_rec(word) and word[0:1].isupper():
                    #mamy bohatera
                    bohater = word;
                    czasownik = '';
                    cecha = '';
                elif plp_rec(word) and ( plp_label(plp_rec(word)[0])[0:1] == "B"  ) and czasownik == '':
                    
                    #czasownik = plp_forms(plp_rec(word)[0])[0] ;
                    czasownik = word
                elif plp_rec(word) and czasownik != '' and bohater != '' and ( plp_label(plp_rec(word)[0])[0:1] == "C" or plp_label(plp_rec(word)[0])[0:1] == "F" or  plp_label(plp_rec(word)[0])[0:1] == "A"  ):
                    cecha = word;
                    #print "cecha:"
                    #print word;
                    dane = Dane(czasownik, cecha,line);

                    #print dane.tostring();
                    bohaterowie[bohater].append(dane);
                    czasownik = '';
                    bohater = '';

    input.close();



def interpretuj():

    for bohater in bohaterowie:
        if len( bohaterowie[bohater] ) >= 2:
            print "------------------------------------"
            print bohater;
            print "+Wyglad zewnetrzny:";
            for dane in bohaterowie[bohater]:
                if plp_rec(dane.czasownik)[0] in wyglad_zewnetrzny:
                    print dane.tostring();

            print "+usposobienie:";
            for dane in bohaterowie[bohater]:
                if plp_rec(dane.czasownik)[0] in usposobienie:
                    print dane.tostring();


            print "------------------------------------"
            for b in  bohaterowie[bohater]:
                print b.tostring();

            print "------------------------------------"



znajdz_bohaterow();
znajdz_cechy();
interpretuj();


#for boh in bohaterowie:
#   print boh;
#    print " : ";
#    print bohaterowie[boh];


