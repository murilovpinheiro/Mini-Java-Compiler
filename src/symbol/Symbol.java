package symbol;

public class Symbol {

    private String nome;
    private static java.util.Dictionary dict = new java.util.Hashtable();

    private Symbol(String n) {
        this.nome = n;
    }


    public static Symbol symbol(String n){
        String u = n.intern();
        Symbol s = (Symbol) dict.get(u);
        if (s == null) {
            s = new Symbol(u);
            dict.put(u, s);
        }
        return s;
    };

    @Override
    public String toString() {
        return nome;
    }
}
