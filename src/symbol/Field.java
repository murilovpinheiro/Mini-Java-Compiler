package symbol;

import utils.ErrorMsg;
import utils.Pair;

public class Field extends Table {

    private Pair<Symbol, String> formal;
    private String nome;
    private Symbol snome;
    private String tipo;
    public ErrorMsg error;

    public Field(Pair<Symbol, String> formal) {
        this.formal = formal;
        snome = formal.first;
        nome = snome.toString();
        tipo = formal.second;
        error = new ErrorMsg();

        Table.put(formal.first, formal.second);
    }

    public Pair<Symbol, String> getPair() {
        return formal;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }
}