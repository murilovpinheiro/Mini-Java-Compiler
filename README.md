# Team 02 Compilers

### Team Members:
    Murilo Pinheiro

    Vitor Manoel

    Gustavo Sousa


Project using Javacc to create a compiler to Mini Java Language;

Compilation order:

cd src 

javacc ../Parser.jj

javac -d ../classes ./Parser.java

cd ../classes

java Parser ../test/factorial.txt
