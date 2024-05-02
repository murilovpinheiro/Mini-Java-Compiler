package syntaxtree.visitor;

import syntaxtree.*;
import symbol.*;
import utils.ErrorMsg;
import utils.Pair;
import utils.PrintUtil;

import java.util.*;

public class DepthFirstVisitor implements Visitor {

    public ClassTable mainClass;
    public Hashtable<Symbol, ClassTable> classList = new Hashtable<Symbol, ClassTable>();
    private ErrorMsg error = new ErrorMsg();


    public void printHierarchy() {
        System.out.println("[Main] " + mainClass.getNome());

        Iterator<Symbol> iterator = classList.keySet().iterator();
        while (iterator.hasNext()) {
            ClassTable currentClass = classList.get(iterator.next());
            System.out.println("\n[Classe] " + currentClass.getNome());
            //attrs
            for (Field currentAtr : currentClass.getAtributos()) {
                System.out.println("  L_[Atributo] " + currentAtr.getTipo() + " " + currentAtr.getNome());
            }
            for (MethodTable currentMtd : currentClass.getMetodos()) {
                System.out.println("  L_[Método] " + currentMtd.getTipo() + " " + currentMtd.getNome());
                for (Field param : currentMtd.getParametros()) {
                    System.out.println("      L_[Parâmetro] " + param.getTipo() + " " + param.getNome());
                }
                for (Field local : currentMtd.getVlocais()) {
                    System.out.println("      L_[Var Local] " + local.getTipo() + " " + local.getNome());
                }
            }
        }

    }

    // MainClass m;
    // ClassDeclList cl;
    public void visit(Program n) {
        n.m.accept(this);
        for ( int i = 0; i < n.cl.size(); i++ ) {
            n.cl.elementAt(i).accept(this);
        }

        printHierarchy();
    }

    // Identifier i1,i2;
    // Statement s;
    public void visit(MainClass n) {
        n.i1.accept(this);
        n.i2.accept(this); // id do string[]
        n.s.accept(this);

        mainClass = new ClassTable(n.i1.toString(), null);
        mainClass.mainArgs.add(n.i2.s);
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
        ClassTable currentClass = new ClassTable(n.i.toString(), null);
        MethodTable tempMethod;

        n.i.accept(this);

        //adiciona as variaveis da classe
        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.elementAt(i).accept(this);
            VarDecl currentVariable = n.vl.elementAt(i);
            if (!currentClass.addAtb(currentVariable.i.toString(), currentVariable.t.toString())) {
                error.complain("Erro ao adicionar o atributo " + PrintUtil.typeId(currentVariable.i.toString(), currentVariable.t.toString()) +
                        " na classe " + currentClass.getNome());
            }
        }

        //adiciona os metodos
        for ( int i = 0; i < n.ml.size(); i++ ) {
            MethodDecl currentMethod = n.ml.elementAt(i);
            currentMethod.accept(this);

            tempMethod = new MethodTable(Pair.of(Symbol.symbol(currentMethod.i.toString()), currentMethod.t.toString()));
            for (int j = 0; j < currentMethod.fl.size(); j++) {
                Formal currentFormal = currentMethod.fl.elementAt(j);
                tempMethod.addParam(currentFormal.i.toString(), currentFormal.t.toString());
            }
            for (int j = 0; j < currentMethod.vl.size(); j++) {
                VarDecl currentVariable = currentMethod.vl.elementAt(j);
                tempMethod.addLocal(currentVariable.i.toString(), currentVariable.t.toString());
            }
            if (!currentClass.addMtd(tempMethod)) {
                error.complain("Erro ao adicionar o metodo " + PrintUtil.typeId(currentMethod.i.toString(), currentMethod.t.toString()) +
                        " na classe " + currentClass.getNome());
            }
        }
        classList.put(Symbol.symbol(n.i.toString()), currentClass);
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {

        ClassTable currentClass = new ClassTable(n.i.toString(), null);
        ClassTable extendedClass = classList.get(Symbol.symbol(n.j.toString()));
        if (extendedClass != null) {

            currentClass = new ClassTable(n.i.toString(), extendedClass);

            //adicionar variaveis e metodos da classe herdada
            List<Field> extendedAtrs = extendedClass.getAtributos();
            for (int i = 0; i < extendedAtrs.size(); i++ ) {
                if (!currentClass.addAtb(extendedAtrs.get(i))) {
                    error.complain("Erro ao adicionar atributo" + PrintUtil.typeId(extendedAtrs.get(i).getNome(), extendedAtrs.get(i).getTipo())
                            + "vindo da classe " + extendedClass.getNome() + ": classe atual " + currentClass.getNome() + " possui o mesmo atributo" );

                }
            }
            List<MethodTable> extendedMtds = extendedClass.getMetodos();
            for (int i = 0; i < extendedMtds.size(); i++ ) {
                if (!currentClass.addMtd(extendedMtds.get(i))) {
                    error.complain("Erro ao adicionar metodo " + PrintUtil.typeId(extendedMtds.get(i).getNome(), extendedMtds.get(i).getTipo())
                            + "vindo da classe " + extendedClass.getNome() + ": classe atual " + currentClass.getNome() + " possui o mesmo metodo " );

                }
            }

        } else {
            error.complain("Erro: classe herdada " + n.j.s + " por " + currentClass.getNome() + " é inválida/não existe");

        }

        MethodTable tempMethod;

        n.i.accept(this);
        n.j.accept(this);

        //adiciona as variaveis da classe
        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.elementAt(i).accept(this);
            VarDecl currentVariable = n.vl.elementAt(i);
            if (!currentClass.addAtb(currentVariable.i.toString(), currentVariable.t.toString())) {
                error.complain("Erro ao adicionar o atributo " + PrintUtil.typeId(currentVariable.i.toString(), currentVariable.t.toString()) +
                        " na classe " + currentClass.getNome());

            }
        }

        //adiciona os metodos
        for ( int i = 0; i < n.ml.size(); i++ ) {
            MethodDecl currentMethod = n.ml.elementAt(i);
            currentMethod.accept(this);

            tempMethod = new MethodTable(Pair.of(Symbol.symbol(currentMethod.i.toString()), currentMethod.t.toString()));
            for (int j = 0; j < currentMethod.fl.size(); j++) {
                Formal currentFormal = currentMethod.fl.elementAt(j);
                tempMethod.addParam(currentFormal.i.toString(), currentFormal.t.toString());
            }
            for (int j = 0; j < currentMethod.vl.size(); j++) {
                VarDecl currentVariable = currentMethod.vl.elementAt(j);
                tempMethod.addLocal(currentVariable.i.toString(), currentVariable.t.toString());
            }
            if (!currentClass.addMtd(tempMethod)) {
                error.complain("Erro ao adicionar o metodo " + PrintUtil.typeId(currentMethod.i.toString(), currentMethod.t.toString()) +
                        " na classe " + currentClass.getNome());
            }
        }
        classList.put(Symbol.symbol(n.i.toString()), currentClass);
    }

    // Type t;
    // Identifier i;
    public void visit(VarDecl n) {
        n.t.accept(this);
        n.i.accept(this);
    }

    // Type t;
    // Identifier i;
    // FormalList fl;
    // VarDeclList vl;
    // StatementList sl;
    // Exp e;
    public void visit(MethodDecl n) {
        n.t.accept(this);
        n.i.accept(this);
        for ( int i = 0; i < n.fl.size(); i++ ) {
            n.fl.elementAt(i).accept(this);
        }
        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.elementAt(i).accept(this);
        }
        for ( int i = 0; i < n.sl.size(); i++ ) {
            n.sl.elementAt(i).accept(this);
        }
        n.e.accept(this);
    }

    // Type t;
    // Identifier i;
    public void visit(Formal n) {
        n.t.accept(this);
        n.i.accept(this);
    }

    public void visit(IntArrayType n) {
    }

    public void visit(BooleanType n) {
    }

    public void visit(IntegerType n) {
    }

    // String s;
    public void visit(IdentifierType n) {
    }

    // StatementList sl;
    public void visit(Block n) {
        for ( int i = 0; i < n.sl.size(); i++ ) {
            n.sl.elementAt(i).accept(this);
        }
    }

    // Exp e;
    // Statement s1,s2;
    public void visit(If n) {
        n.e.accept(this);
        n.s1.accept(this);
        n.s2.accept(this);
    }

    // Exp e;
    // Statement s;
    public void visit(While n) {
        n.e.accept(this);
        n.s.accept(this);
    }

    // Exp e;
    public void visit(Print n) {
        n.e.accept(this);
    }

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {
        n.i.accept(this);
        n.e.accept(this);
    }

    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {
        n.i.accept(this);
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(And n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(LessThan n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Plus n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Minus n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Times n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(ArrayLookup n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e;
    public void visit(ArrayLength n) {
        n.e.accept(this);
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit(Call n) {
        n.e.accept(this);
        n.i.accept(this);
        for ( int i = 0; i < n.el.size(); i++ ) {
            n.el.elementAt(i).accept(this);
        }
    }

    // int i;
    public void visit(IntegerLiteral n) {
    }

    public void visit(True n) {
    }

    public void visit(False n) {
    }

    // String s;
    public void visit(IdentifierExp n) {
    }

    public void visit(This n) {
    }

    // Exp e;
    public void visit(NewArray n) {
        n.e.accept(this);
    }

    // Identifier i;
    public void visit(NewObject n) {
    }

    // Exp e;
    public void visit(Not n) {
        n.e.accept(this);
    }

    // String s;
    public void visit(Identifier n) {
    }
}