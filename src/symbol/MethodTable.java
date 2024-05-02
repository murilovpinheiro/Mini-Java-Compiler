package symbol;

import utils.Pair;
import utils.PrintUtil;

import java.util.ArrayList;

public class MethodTable extends Field {

//    private ArrayList<Pair<Symbol, String>> parametros;
    private ArrayList<Field> parametros;
    private ArrayList<Field> vlocais;

    public MethodTable(Pair<Symbol, String> formal) {
        super(formal);
        parametros = new ArrayList<Field>();
        vlocais    = new ArrayList<Field>();

        Table.put(formal.first, formal.second);
    }

    public boolean addParam(String id, String t) {
        for (int i = 0; i < parametros.size(); ++i) {
            if (parametros.get(i).getNome().equals(id)) {
                error.complain("Erro ao adicionar parametro" + PrintUtil.typeId(id, t) + "no metodo" + PrintUtil.typeId(getNome(), getTipo()) + ".");
                return false;
            }
        }

        parametros.add(new Field(Pair.of(Symbol.symbol(id), t)));
        return true;
    }

    public boolean addLocal(String id, String t) {
        for (int i = 0; i < vlocais.size(); ++i) {
            if (vlocais.get(i).getNome().equals(id)) {
                error.complain("Erro ao adicionar variavel local" + PrintUtil.typeId(id, t) + "no metodo" + PrintUtil.typeId(getNome(), getTipo()) + ".");
                return false;
            }
        }

        vlocais.add(new Field(Pair.of(Symbol.symbol(id), t)));
        return true;
    }

    public ArrayList<Field> getParametros() {
        return parametros;
    }

    public ArrayList<Field> getVlocais() {
        return vlocais;
    }

    public boolean containsInParams(String id) {
        for (Field obj : getParametros()) {
            if (obj.getNome().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsInLocals(String id) {
        for (Field obj : getVlocais()) {
            if (obj.getNome().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
