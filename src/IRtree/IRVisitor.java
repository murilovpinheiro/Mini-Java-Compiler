package IRtree;

import symbol.*;
import frame.*;
import syntaxtree.Integer;
import tree.*;

import java.util.*;

import syntaxtree.*;
import syntaxtree.visitor.SymbolVisitor;
import syntaxtree.visitor.TypeDepthVisitor;
import temp.*;
import utils.Conversor;

// Intermediate Visitor Class
public class IRVisitor implements IRtree.Visitor {

    Stack<Frame> frames;
    Frame currentFrame;
    public ClassTable mainClass;
    public Hashtable<Symbol, ClassTable> classList;
    MethodTable currentMethod;
    ClassTable currentClass;
    public ArrayList <Frag> fragments;
    ClassTable callStackClass;
    MethodTable callStackMethod;


    public IRVisitor(TypeDepthVisitor v, Frame currentFrame) {
        mainClass = v.mainClass;
        classList = v.classList;

        this.currentFrame = currentFrame;
        frames = new Stack<Frame>();
        frames.push(currentFrame);
        fragments = new ArrayList<Frag>();
    }

    public IRVisitor(SymbolVisitor v, Frame currentFrame) {
        mainClass = v.mainClass;
        classList = v.classList;

        this.currentFrame = currentFrame;
        frames = new Stack<Frame>();
        frames.push(currentFrame);
        fragments = new ArrayList<Frag>();
    }

    public ExpEncode getAddress(Symbol var) {

        Field varEnd;
        if((varEnd = currentMethod.getInParams(var.toString())) != null);
        else if((varEnd = currentMethod.getInLocals(var.toString()))!= null);
        else if ((varEnd = mainClass.getInAtb(var.toString())) != null);
        else varEnd= currentClass.getInAtb(var.toString());

        return new ExpEncode(varEnd.access.exp(new TEMP(currentFrame.FP())));
    }

    @Override
    public ExpEncode visit(Program n) {

        n.m.accept(this);

        for (int i = 0; i < n.cl.size(); i++) {
            this.currentClass = this.classList.get(Symbol.symbol(n.cl.elementAt(i).toString()));
            n.cl.elementAt(i).accept(this);
        }

        tree.Print h = new tree.Print(System.out);

        // DEBUG
        Stm temp;
        for (int i = 0; i < fragments.size(); i++) {
            temp = fragments.get(i).body;
            h.prStm(temp);
        }

        return null;
    }

    @Override
    public ExpEncode visit(MainClass n) {

        this.currentClass = mainClass;

        ArrayList<Boolean> j = new ArrayList<Boolean>();
        j.add(false);

        currentFrame = currentFrame.newFrame(Symbol.symbol("main"),j);
        frames.push(currentFrame);

        Stm body = new EXPR(n.s.accept(this).getExp());
        ArrayList<Stm> lista = new ArrayList<Stm>();
        lista.add(body);

        currentFrame.procEntryExit1(lista);
        fragments.add(new Frag(body,currentFrame));
        frames.pop();

        return null;
    }

    @Override
    public ExpEncode visit(NormalClass n) {

        this.currentClass = classList.get(Symbol.symbol(n.i.toString()));
        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.elementAt(i).accept(this);
        }

        for (int i = 0; i < n.ml.size(); i++) {
            n.ml.elementAt(i).accept(this);
        }

        return null;
    }

    @Override
    public ExpEncode visit(SubClass n) {

        this.currentClass = classList.get(Symbol.symbol(n.i.toString()));
        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.elementAt(i).accept(this);
        }

        for (int i = 0; i < n.ml.size(); i++) {
            n.ml.elementAt(i).accept(this);
        }

        return null;
    }

    @Override
    public ExpEncode visit(VarDeclaration n) {

        Field varEnd;

        if(currentMethod != null) {
            varEnd= currentMethod.getInParams(n.i.toString());

            if(varEnd != null) {
                varEnd.access = currentFrame.allocLocal(false);
                return null;
            }

            varEnd = currentMethod.getInLocals(n.i.toString());

            if(varEnd!= null) {
                varEnd.access = currentFrame.allocLocal(false);
                return null;
            }
        }

        varEnd = currentClass.getInAtb(n.i.toString());

        if(varEnd!= null) varEnd.access = currentFrame.allocLocal(false);

        return new ExpEncode(varEnd.access.exp((new TEMP(currentFrame.FP()))));
    }

    @Override
    public ExpEncode visit(MethodDeclaration n) {

        Stm body = new EXPR(new CONST(0));

        ArrayList<Boolean> j = new ArrayList<Boolean>();

        currentMethod = currentClass.getInMethods(n.i.toString());

        for (int i = 0; i <= n.fl.size(); i++) {
            j.add(false);
        }

        currentFrame = currentFrame.newFrame(Symbol.symbol(currentClass.toString()+"$"+ currentMethod.toString()), (java.util.List<Boolean>)j);
        frames.push(currentFrame);

        for (int i = 0; i < n.fl.size(); i++) {
            n.fl.elementAt(i).accept(this);
        }

        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.elementAt(i).accept(this);
        }

        for (int i = 0; i < n.sl.size(); i++) {
            body = new SEQ(body,new EXPR(n.sl.elementAt(i).accept(this).getExp()));
        }

        ArrayList<Stm> l = new ArrayList<Stm>();
        l.add(body);
        currentFrame.procEntryExit1(l);
        fragments.add(new Frag(body,currentFrame));
        currentMethod = null;
        frames.pop();

        return null;
    }

    @Override
    public ExpEncode visit(Formal n) {

        Field varEnd = currentMethod.getInParams(n.i.toString());
        varEnd.access = currentFrame.allocLocal(false);

        return null;
    }

    @Override
    public ExpEncode visit(IntArrayType n) {
        return null;
    }

    @Override
    public ExpEncode visit(BooleanType n) {
        return null;
    }

    @Override
    public ExpEncode visit(IntegerType n) {
        return null;
    }

    @Override
    public ExpEncode visit(IdentifierType n) {
        return null;
    }

    @Override
    public ExpEncode visit(Block n) {
        tree.Exp stm = new CONST(0);
        for (int i = 0; i < n.sl.size(); i++) {
            stm = new ESEQ(new SEQ(new EXPR(stm),new EXPR(n.sl.elementAt(i).accept(this).getExp())),new CONST(0));
        }

        return new ExpEncode(stm);
    }

    @Override
    public ExpEncode visit(If n) {

        Label ifF = new Label();
        Label elseE = new Label();
        Label end = new Label();

        tree.Exp cond = n.e.accept(this).getExp();

        ExpEncode label1 = n.s1.accept(this);
        ExpEncode label2 = n.s2.accept(this);

        tree.Exp Cx = new ESEQ(new SEQ(new CJUMP(CJUMP.GT,cond,new CONST(0),ifF,elseE),
                new SEQ(new SEQ(new LABEL(ifF),new SEQ(new EXPR(label1.getExp()), new JUMP(end))),
                        new SEQ(new LABEL(elseE),new SEQ(new EXPR(label2.getExp()),new LABEL(end))))),
                new CONST(0));

        return new ExpEncode(Cx);
    }

    @Override
    public ExpEncode visit(While n) {

        Label test = new Label();
        Label body = new Label();
        Label end = new Label();

        ExpEncode cond = n.e.accept(this);
        ExpEncode stm = n.s.accept(this);

        return new ExpEncode(new ESEQ(new SEQ(new SEQ(new LABEL(test),
                new SEQ(new CJUMP(CJUMP.GT,cond.getExp(),new CONST(0), body, end),
                        new SEQ(new LABEL(body), new SEQ(new EXPR(stm.getExp()),new JUMP(test))))),
                new LABEL(end)), new CONST(0)));
    }

    @Override
    public ExpEncode visit(syntaxtree.Print n) {
        ExpEncode exp = n.e.accept(this);
        tree.ExpList parameters= new tree.ExpList(exp.getExp(),null);

        return new ExpEncode( currentFrame.externalCall("print", Conversor.ExpListToList(parameters)));
    }

    @Override
    public ExpEncode visit(Assign n) {
        ExpEncode i = n.i.accept(this);
        ExpEncode e = n.e.accept(this);

        return new ExpEncode(new ESEQ(new MOVE( i.getExp(), e.getExp() ), new CONST(0)));
    }

    @Override
    public ExpEncode visit(ArrayAssign n) {
        ExpEncode i = n.i.accept(this);
        ExpEncode e1 = n.e1.accept(this);
        ExpEncode e2 = n.e2.accept(this);

        return new ExpEncode( new ESEQ(new MOVE(new MEM(new BINOP(BINOP.PLUS, i.getExp(), new BINOP(BINOP.MUL, e1.getExp(), new CONST(currentFrame.wordSize())))), e2.getExp()), new CONST(0)) );
    }

    @Override
    public ExpEncode visit(And n) {
        ExpEncode e1 = n.e1.accept(this);
        ExpEncode e2 = n.e2.accept(this);

        return new ExpEncode(new BINOP(BINOP.AND, e1.getExp(), e2.getExp()));
    }

    @Override
    public ExpEncode visit(LessThan n) {
        ExpEncode e1 = n.e1.accept(this);
        ExpEncode e2 = n.e2.accept(this);

        return new ExpEncode(new BINOP(BINOP.MINUS, e2.getExp(), e1.getExp()));
    }

    @Override
    public ExpEncode visit(Plus n) {
        ExpEncode e1 = n.e1.accept(this);
        ExpEncode e2 = n.e2.accept(this);

        return new ExpEncode(new BINOP(BINOP.PLUS, e1.getExp(), e2.getExp()));
    }

    @Override
    public ExpEncode visit(Minus n) {
        ExpEncode e1 = n.e1.accept(this);
        ExpEncode e2 = n.e2.accept(this);

        return new ExpEncode(new BINOP(BINOP.MINUS, e1.getExp(), e2.getExp()));
    }

    @Override
    public ExpEncode visit(Times n) {
        ExpEncode e1 = n.e1.accept(this);
        ExpEncode e2 = n.e2.accept(this);

        return new ExpEncode(new BINOP(BINOP.MUL, e1.getExp(), e2.getExp()));
    }

    @Override
    public ExpEncode visit(ArrayLookup n) {
        ExpEncode e1 = n.e1.accept(this);
        ExpEncode e2 = n.e2.accept(this);

        return new ExpEncode(new MEM(new BINOP(BINOP.PLUS, e1.getExp(), new BINOP(BINOP.MUL, new BINOP(BINOP.PLUS, new CONST(1), e2.getExp()), new CONST(currentFrame.wordSize())))));

    }

    @Override
    public ExpEncode visit(ArrayLength n) {
        return new ExpEncode(new MEM(getAddress(Symbol.symbol(( (IdentifierExp) n.e).s)).getExp()));
    }

    @Override
    public ExpEncode visit(Call n) {
        ClassTable j = null;

        tree.ExpList list = null;
        for (int i = n.el.size()-1; i >= 0 ; i--) {
            list = new tree.ExpList(n.el.elementAt(i).accept(this).getExp(), list);
        }

        list = new tree.ExpList(n.e.accept(this).getExp(),list);

        if (n.e instanceof This) {
            j = currentClass;
        }

        if (n.e instanceof NewObject) {
            j = classList.get(Symbol.symbol(n.e.toString()));
        }

        if (n.e instanceof IdentifierExp) {
            Field var;

            var = currentMethod.getInParams(n.e.toString());
            if (var == null) var = currentMethod.getInLocals(n.e.toString());

            if (var == null) {
                j = currentClass;
            }
            else {
                j = classList.get(Symbol.symbol(var.getNome()));
            }

        }

        if(n.e instanceof Call) {
            var returnType = callStackMethod.getTipo();

            Iterator<Symbol> iterator = classList.keySet().iterator();
            while (iterator.hasNext()) {
                ClassTable iteratorClass = classList.get(iterator.next());
                if (iteratorClass.getNome().equals(returnType)) {
                    j = iteratorClass;
                    break;
                }
            }
        }

        if(j == null)
        {
            if(currentClass != null)
                System.out.println(currentClass.getNome());
            if(currentMethod != null)
                System.out.println(currentMethod.getNome());
            System.out.println(n.e);
            System.out.println(n.i);
            System.out.println(n.e.getClass());
        }

        callStackClass = j;
        callStackMethod = j.getInMethods(n.i.toString());
        return new ExpEncode(new CALL(new NAME(new Label(j.getNome()+"$"+n.i.toString())),list));
    }

    @Override
    public ExpEncode visit(Integer n) {
        return new ExpEncode(new CONST(n.i));
    }

    @Override
    public ExpEncode visit(True n) {
        return new ExpEncode(new CONST(1));
    }

    @Override
    public ExpEncode visit(False n) {
        return new ExpEncode(new CONST(0));
    }

    @Override
    public ExpEncode visit(IdentifierExp n) {
        return getAddress(Symbol.symbol(n.s));
    }

    @Override
    public ExpEncode visit(This n) {
        return new ExpEncode(new MEM(new TEMP(currentFrame.FP())));
    }

    @Override
    public ExpEncode visit(NewArray n) {

        ExpEncode size = n.e.accept(this);

        tree.Exp allocation = new BINOP(BINOP.MUL, new BINOP(BINOP.PLUS, size.getExp(), new CONST(1)) ,new CONST(currentFrame.wordSize()));

        tree.ExpList parameters = new tree.ExpList(allocation,null);

        List<tree.Exp> convertedList = Conversor.ExpListToList(parameters);

        tree.Exp returnValue = currentFrame.externalCall("initArray", convertedList);

        return new ExpEncode(returnValue);
    }

    @Override
    public ExpEncode visit(NewObject n) {
        ClassTable j = classList.get(Symbol.symbol(n.i.toString()));
        int size = j.getAtributos().size();

        tree.ExpList parameters = new tree.ExpList(new BINOP(BINOP.MUL,new CONST(1+size) , new CONST(currentFrame.wordSize())), null);
        List<tree.Exp> list = utils.Conversor.ExpListToList(parameters);
        return new ExpEncode(currentFrame.externalCall("malloc", list));
    }

    @Override
    public ExpEncode visit(Not n) {
        ExpEncode e = n.e.accept(this);

        return new ExpEncode(new BINOP(BINOP.MINUS,new CONST(1), e.getExp()));
    }

    @Override
    public ExpEncode visit(Identifier n) {
        return getAddress(Symbol.symbol(n.s));
    }
}
