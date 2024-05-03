package syntaxtree.visitor;

import symbol.*;
import syntaxtree.*;
import syntaxtree.Integer;
import utils.ErrorMsg;
import utils.Pair;

import java.util.Hashtable;
import java.util.Iterator;

public class TypeDepthVisitor implements TypeVisitor {

    private ErrorMsg error = new ErrorMsg();
    public ClassTable mainClass;
    public Hashtable<Symbol, ClassTable> classList;

    public ClassTable currentClass;
    public MethodTable currentMethod;

    public TypeDepthVisitor(SymbolVisitor symbolVisitor) {
        mainClass = symbolVisitor.mainClass;
        classList = symbolVisitor.classList;
    }

    public Type visit(Program program) {
        program.m.accept(this);
        for (int i = 0; i < program.cl.size(); i++) {
            program.cl.elementAt(i).accept(this);
        }
        return null;
    }

    public Type visit(MainClass mainClass) {
        currentClass = this.mainClass;

        mainClass.i1.accept(this);
        mainClass.i2.accept(this);
        mainClass.s.accept(this);
        return null;
    }

    public Type visit(NormalClass normalClass) {
        currentClass = classList.get(Symbol.symbol(normalClass.i.s));

        normalClass.i.accept(this);

        for (int i = 0; i < normalClass.vl.size(); i++) {
            normalClass.vl.elementAt(i).accept(this);
        }
        for (int i = 0; i < normalClass.ml.size(); i++) {
            currentMethod = currentClass.getMetodos().get(i);
            normalClass.ml.elementAt(i).accept(this);
            currentMethod = null;
        }
        currentClass = null;
        return null;
    }

    public Type visit(SubClass subClass) {
        currentClass = classList.get(Symbol.symbol(subClass.i.s));

        subClass.i.accept(this);
        subClass.j.accept(this);
        for (int i = 0; i < subClass.vl.size(); i++) {
            subClass.vl.elementAt(i).accept(this);
        }
        for (int i = 0; i < subClass.ml.size(); i++) {
            for (MethodTable mtd : currentClass.getMetodos()) {
                if (subClass.ml.elementAt(i).toString().equals(mtd.getNome())) {
                    currentMethod = mtd; currentMethod = currentClass.getMetodos().get(i);

                    subClass.ml.elementAt(i).accept(this);
                    currentMethod = null;
                }
            }

        }
        currentClass = null;
        return null;
    }

    public Type visit(VarDeclaration varDeclaration) {
        varDeclaration.t.accept(this);
        varDeclaration.i.accept(this);
        return null;
    }

    public Type visit(MethodDeclaration methodDeclaration) {

        methodDeclaration.t.accept(this);
        methodDeclaration.i.accept(this);
        for (int i = 0; i < methodDeclaration.fl.size(); i++) {
            methodDeclaration.fl.elementAt(i).accept(this);
        }
        for (int i = 0; i < methodDeclaration.vl.size(); i++) {
            methodDeclaration.vl.elementAt(i).accept(this);
        }
        for (int i = 0; i < methodDeclaration.sl.size(); i++) {
            methodDeclaration.sl.elementAt(i).accept(this);
        }
        methodDeclaration.e.accept(this);
        return null;
    }

    public Type visit(Formal formal) {
        formal.t.accept(this);
        formal.i.accept(this);
        return null;
    }

    public Type visit(Block block) {
        for (int i = 0; i < block.sl.size(); i++) {
            block.sl.elementAt(i).accept(this);
        }
        return null;
    }

    public Type visit(If ifStatement) {
        if (!(ifStatement.e.accept(this) instanceof BooleanType)) {
            error.complain("A condição dentro do 'If' deve ser do tipo 'booleano'.");
        }
        ifStatement.s1.accept(this);
        ifStatement.s2.accept(this);
        return null;
    }

    public Type visit(While whileStatement) {
        if (!(whileStatement.e.accept(this) instanceof BooleanType)) {
            error.complain("A condição dentro do 'While' deve ser do tipo 'booleano'.");
        }
        whileStatement.s.accept(this);
        return null;
    }

    public Type visit(Print print) {
        Type type = print.e.accept(this);
        if (!error.anyErrors)
            if (!(type instanceof IntegerType || type instanceof BooleanType || type instanceof IdentifierType)) {
                error.complain("O Print recebeu um valor de tipo inválido: '"+ type.toString() +"'.");
            }
        return null;
    }

    public Type visit(Assign assign) {
        Type idType  = assign.i.accept(this);
        Type expType = assign.e.accept(this);
        String idName = assign.i.s;

        try {
            if (!(expType.toString().equals(idType.toString()))) {
                error.complain("O tipo da variável não corresponde ao tipo atribuído: variável '"+ idName
                        +"' do tipo '"+ idType.toString() + "' e expressão '"+expType.toString()+"'.");
            }

        } catch (Exception e) {
            System.out.println("Identificador: " + idName);
        }

        return null;
    }

    public Type visit(ArrayAssign arrayAssign) {
        Type idType = arrayAssign.i.accept(this);
        String idName = arrayAssign.i.s;

        if (!idType.toString().equals("int[]")) {
            error.complain("O identificador '"+ idType.toString() +"' deve ser do tipo 'int[]'.");
        }

        if(!(arrayAssign.e1.accept(this) instanceof IntegerType)) {
            error.complain("O índice do array deve ser do tipo 'int'.");
        } else if(!(arrayAssign.e2.accept(this) instanceof IntegerType)) {
            error.complain("O valor a inserir no array deve ser do tipo 'int'.");
        }

        return null;
    }

    public Type visit(And and) {
        if(!(and.e1.accept(this) instanceof BooleanType)) {
            error.complain("O lado esquerdo da operação '&&' deve ser do tipo 'booleano'.");
        } else if(!(and.e2.accept(this) instanceof BooleanType)) {
            error.complain("O lado direito da operação '&&' deve ser do tipo 'booleano'.");
        }
        return new BooleanType();
    }

    public Type visit(LessThan lessThan) {
        if(!(lessThan.e1.accept(this) instanceof IntegerType)) {
            error.complain("O lado esquerdo da operação '<' deve ser do tipo 'int'.");
        } else if(!(lessThan.e2.accept(this) instanceof IntegerType)) {
            error.complain("O lado direito da operação '<' deve ser do tipo 'int'.");
        }
        return new BooleanType();
    }

    public Type visit(Plus plus) {
        if(!(plus.e1.accept(this) instanceof IntegerType)) {
            error.complain("O lado esquerdo da operação '+' deve ser do tipo 'int'.");
        } else if(!(plus.e2.accept(this) instanceof IntegerType)) {
            error.complain("O lado direito da operação '+' deve ser do tipo 'int'.");
        }
        return new IntegerType();
    }

    public Type visit(Minus minus) {
        if(!(minus.e1.accept(this) instanceof IntegerType)) {
            error.complain("O lado esquerdo da operação '-' deve ser do tipo 'int'.");
        } else if(!(minus.e2.accept(this) instanceof IntegerType)) {
            error.complain("O lado direito da operação '-' deve ser do tipo 'int'.");
        }
        return new IntegerType();
    }

    public Type visit(Times times) {
        if(!(times.e1.accept(this) instanceof IntegerType)) {
            error.complain("O lado esquerdo da operação '*' deve ser do tipo 'int'.");
        } else if(!(times.e2.accept(this) instanceof IntegerType)) {
            error.complain("O lado direito da operação '*' deve ser do tipo 'int'.");
        }
        return new IntegerType();
    }

    public Type visit(ArrayLookup arrayLookup) {

        Type tipo1 = arrayLookup.e1.accept(this);
        Type tipo2 = arrayLookup.e2.accept(this);

        if(!(tipo1 instanceof IntArrayType)) {
            error.complain("Identificador inválido (ArrayLookup): " + tipo1.toString() +  ".");
        }

        if(!(tipo2 instanceof IntegerType)) {
            error.complain("A posição do array deve ser do tipo 'int'.");
        }

        return new IntegerType();
    }

    public Type visit(ArrayLength arrayLength) {

        Type tipo = arrayLength.e.accept(this);

        if(!(tipo instanceof IntArrayType)) {
            error.complain("Identificador inválido (ArrayLength): " + tipo.toString() + ".");
        }
        return new IntegerType();
    }

    public Type visit(Call call) {
        if (!(call.e.accept(this) instanceof IdentifierType)) {
            error.complain("A expressão não é um identificador");
        }

        Type objIdType = call.e.accept(this);
        ClassTable objClassTable = classList.get(Symbol.symbol(objIdType.toString()));

        if (objClassTable == null) {
            error.complain("A classe de '" + objIdType.toString() + "' não foi declarada.");
            return null;
        }

        MethodTable objMethodTable = null;
        for (MethodTable m : objClassTable.getMetodos()) {
            if (m.getNome().equals(call.i.s)) {
                objMethodTable = m;
                break;
            }
        }
        if (objMethodTable == null) {
            error.complain("O método '" + call.i.s + "' não existe na classe '" + objClassTable.getNome() + ".");
            return null;
        }

        if (call.el.size() != objMethodTable.getParametros().size()) {
            error.complain("Quantidade incorreta de parâmetros; foram dados " + call.el.size() +
                    ", esperava-se " +  objMethodTable.getParametros().size() + ".");
        }


        for ( int i = 0; i < call.el.size(); i++ ) {
            Type paramType = call.el.elementAt(i).accept(this);
            String tipoPAtual = "";
            if (i < objMethodTable.getParametros().size()) {
                tipoPAtual = objMethodTable.getParametros().get(i).getTipo();
            }
            if (!tipoPAtual.equals(paramType.toString()) && tipoPAtual != "") {
                error.complain("O parâmetro número '" + i + "' não é do tipo '" + tipoPAtual + "'." );

            }
        }

        if (objMethodTable.getTipo().equals("int")) {
            return new IntegerType();
        } else if (objMethodTable.getTipo().equals("int[]")) {
            return new IntArrayType();
        } else if (objMethodTable.getTipo().equals("boolean")) {
            return new BooleanType();
        } else {
            return new IdentifierType(objMethodTable.getTipo());
        }

    }

    public Type visit(Integer integer) {
        return new IntegerType();
    }

    public Type visit(True trueValue) {
        return new BooleanType();
    }

    public Type visit(False falseValue) {
        return new BooleanType();
    }

    public Type visit(IdentifierExp identifierExp) {

        Identifier iaux = new Identifier(identifierExp.s);
        return iaux.accept(this);
    }

    public Type visit(This thisValue) {
        return new IdentifierType(currentClass.getNome());
    }

    public Type visit(NewArray newArray) {
        if (!(newArray.e.accept(this) instanceof IntegerType)) {
            error.complain("A expressão dentro do novo array deve ser do tipo 'int'.");
        }
        return new IntArrayType();
    }

    public Type visit(NewObject newObject) {
        Type objType = newObject.i.accept(this);

        boolean classExists = false;
        Iterator<Symbol> iterator = classList.keySet().iterator();
        while (iterator.hasNext()) {
            ClassTable iteratorClass = classList.get(iterator.next());
            if (iteratorClass.getNome().equals(objType.toString())) {
                classExists = true;
                break;
            }
        }

        if (!classExists) {
            error.complain("Não foi possível criar o objeto pois a classe '" + objType.toString() + "' não foi declarada.");
        }

        if (!(objType instanceof IdentifierType)) {
            error.complain("O identificador '" + objType.toString() + "' do objeto é inválido.");
        }

        return new IdentifierType(objType.toString());
    }

    public Type visit(Not not) {
        if (!(not.e.accept(this) instanceof BooleanType)) {
            error.complain("A expressão após o 'Not' deve ser do tipo 'booleano'.");
        }
        return new BooleanType();
    }

    public Type visit(IntArrayType intArrayType) {
        return new IntArrayType();
    }

    public Type visit(BooleanType booleanType) {
        return new BooleanType();
    }

    public Type visit(IntegerType integerType) {
        return new IntegerType();
    }

    public Type visit(IdentifierType identifierType) {
        return identifierType;
    }

    public Type visit(Identifier identifier) {

        String id = identifier.s;

        if (mainClass.mainArgs.get(0).equals(identifier.s)) {
            return null;
        }

        Field field = null;


        if (currentMethod != null && currentMethod.containsInParams(id)) {
            for (int i = 0; i < currentMethod.getParametros().size(); ++i) {
                if (id.equals(currentMethod.getParametros().get(i).getNome())) {
                    field = currentMethod.getParametros().get(i);
                    break;
                }
            }
        } else if (currentMethod != null && currentMethod.containsInLocals(id)) {
            for (int i = 0; i < currentMethod.getVlocais().size(); ++i) {
                if (id.equals(currentMethod.getVlocais().get(i).getNome())) {
                    field = currentMethod.getVlocais().get(i);
                    break;
                }
            }
        } else {
            boolean achouAtributo = false;
            boolean achouClasse = false;

            if (currentClass != null) {
                for (int i = 0; i < currentClass.getAtributos().size(); ++i) {
                    if (id.equals(currentClass.getAtributos().get(i).getNome())) {
                        field = currentClass.getAtributos().get(i);
                        achouAtributo = true;
                        break;
                    }
                }
                if (!achouAtributo) {
                    for (int i = 0; i < currentClass.getMetodos().size(); ++i) {
                        if (id.equals(currentClass.getMetodos().get(i).getNome())) {
                            field = currentClass.getMetodos().get(i);
                            achouAtributo = true;
                            break;
                        }
                    }
                }
            }


            if (!achouAtributo) {

                if (mainClass.getNome().equals(id)) {

                    field = new Field(Pair.of(Symbol.symbol(id), id));
                    achouClasse = true;

                } else {
                    Iterator<Symbol> classIt = classList.keySet().iterator();
                    while (classIt.hasNext()) {
                        ClassTable iteratorClass = classList.get(classIt.next());
                        if (iteratorClass.getNome().equals(id)) {
                            field = new Field(Pair.of(Symbol.symbol(id), id));
                            achouClasse = true;
                            break;
                        }
                    }
                }

                if (!achouClasse) {
                    error.complain("O identificador '" + identifier.s + "' não foi declarado no escopo.");
                    return new IdentifierType(identifier.s);
                }


            }
        }


        return field.getTipo().equals("int[]") ? new IntArrayType() :
                field.getTipo().equals("int") ? new IntegerType() :
                        field.getTipo().equals("boolean") ? new BooleanType() :
                                new IdentifierType(field.getTipo());

    }
}
