package symbol;

import utils.ErrorMsg;
import utils.Pair;
import utils.PrintUtil;

import java.util.ArrayList;

public class ClassTable extends Table {

    private String nome; // declarar nome da variavel como simbolo ou string?
    private Symbol snome;

    // lista de simbolo -> string
    // simbolo sendo o id do atributo e string o retorno dele (representacao recomendada pelo livro)
    private ArrayList<Field> atributos;

    // lista de simbolo -> string
    // simbolo sendo o id do metodo e string o retorno dele
    private ArrayList<MethodTable> metodos;

    private ErrorMsg error;

    public ArrayList<String> mainArgs;

    public ClassTable(String nome, ClassTable pai) {
        this.nome = nome;
        snome = Symbol.symbol(nome);
        atributos = new ArrayList<Field>();
        metodos = new ArrayList<MethodTable>();
        error = new ErrorMsg();
        mainArgs = new ArrayList<String>();

        // checar se a classe atual eh extendida
//        if (pai != null) {
//            ArrayList<Field> atbPai = pai.getAtributos();
//            ArrayList<MethodTable> metPai = pai.getMetodos();
//
//            for (int i = 0; i < atbPai.size(); ++i) {
//                String idAtual = atbPai.get(i).getPair().first.toString();
//                String tAtual = atbPai.get(i).getPair().second;
//                // adicionando e checando atributo
//                if (!addAtb(idAtual, tAtual)) {
//                    error.complain("Erro ao adicionar atributo" + PrintUtil.typeId(idAtual, tAtual) + "vindo da classe " + pai.getNome() + ": classe atual " + getNome() + " possui o mesmo atributo" );
//                }
//            }
//
//            for (int i = 0; i < metPai.size(); ++i) {
//                String idAtual = metPai.get(i).getPair().first.toString();
//                String tAtual = metPai.get(i).getPair().second;
//                // adicionando e checando metodo
//                if (!addMtd(idAtual, tAtual)) {
//                    error.complain("Erro ao adicionar metodo" + PrintUtil.typeId(idAtual, tAtual) + "vindo da classe " + pai.getNome() + ": classe atual " + getNome() + " possui o mesmo atributo");
//                }
//            }
//        }

        Table.put(snome, nome);
    }

    public boolean addAtb(String id, String t) {
        for (int i = 0; i < atributos.size(); ++i) {
            if (atributos.get(i).getPair().first.toString().equals(id)) {
                error.complain("Classe " + getNome() + " ja tem o atributo " + PrintUtil.typeId(id, t) + ".");
                return false;
            }
        }

        atributos.add(new Field(Pair.of(Symbol.symbol(id), t)));
        return true;
    }

    public boolean addAtb(Field atr) {
        for (Field atr2 : getAtributos()) {
            if (atr2.getNome().equals(atr.getNome())) {
                error.complain("Classe " + getNome() + " ja tem o atributo " + PrintUtil.typeId(atr2.getNome(), atr2.getTipo()) + ".");
                return false;
            }
        }

        atributos.add(atr);
        return true;
    }

    public boolean addMtd(String id, String t) {
        for (int i = 0; i < metodos.size(); ++i) {
            if (metodos.get(i).getPair().first.toString().equals(id)) {
                error.complain("Classe " + getNome() + " ja tem o metodo " + PrintUtil.typeId(id, t) + ".");
                return false;
            }
        }

        metodos.add(new MethodTable(Pair.of(Symbol.symbol(id), t)));
        return true;
    }

    public boolean addMtd(MethodTable mtd) {
        for (MethodTable mtd2 : getMetodos()) {
            if (mtd2.getNome().equals(mtd.getNome())) {
                error.complain("Classe " + getNome() + " ja tem o metodo " + PrintUtil.typeId(mtd2.getNome(), mtd2.getTipo()) + ".");
                return false;
            }
        }

        metodos.add(mtd);
        return true;
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<Field> getAtributos() {
        return atributos;
    }

    public ArrayList<MethodTable> getMetodos() {
        return metodos;
    }

    public boolean containsInMethods(String id) {
        for (MethodTable mt : getMetodos()) {
            if (mt.getNome().equals(id)) {
                return true;
            }
        }
        return false;
    }
}