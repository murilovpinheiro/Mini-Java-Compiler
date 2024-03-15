# Team 02 Compilers

### Team Members:
    Murilo Pinheiro

    Vitor Manoel

    Gustavo Sousa


Project using Javacc to create a compiler to Mini Java Language;


Compilation order:

javacc -OUTPUT_DIRECTORY=JavaFiles Parser.jj


javac -d Classes JavaFiles/*.java


java -cp Classes:./Classes Parser ./Tests/print_example.txt


