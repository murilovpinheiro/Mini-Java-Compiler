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

//Intermediate Visitor Class
public class IRVisitor implements IRtree.Visitor {

    public ClassTable mainClass;
    public Hashtable<Symbol, ClassTable> classList;
    public ArrayList <Frag> fragments;
    Stack<Frame> frames;
    Frame currentFrame;
    MethodTable currentMethod;
    ClassTable currentClass;
    ClassTable callStackClass;
    MethodTable callStackMethod;

    public IRVisitor(TypeDepthVisitor visitor, Frame currentFrame) {
        mainClass = visitor.mainClass;
        classList = visitor.classList;

        this.currentFrame = currentFrame;
        frames = new Stack<Frame>();
        frames.push(currentFrame);
        fragments = new ArrayList<Frag>();
    }

    public IRVisitor(SymbolVisitor visitor, Frame currentFrame) {
        mainClass = visitor.mainClass;
        classList = visitor.classList;

        this.currentFrame = currentFrame;
        frames = new Stack<Frame>();
        frames.push(currentFrame);
        fragments = new ArrayList<Frag>();
    }

    public ExpEncode getAddress(Symbol variable) {

        Field variableEnd;
        if((variableEnd = currentMethod.getInParams(variable.toString())) != null);
        else if((variableEnd = currentMethod.getInLocals(variable.toString()))!= null);
        else if ((variableEnd = mainClass.getInAtb(variable.toString())) != null);
        else variableEnd= currentClass.getInAtb(variable.toString());

        return new ExpEncode(variableEnd.access.exp(new TEMP(currentFrame.FP())));
    }

    @Override
    public ExpEncode visit(Program prog) {

        prog.m.accept(this);

        for (int i = 0; i < prog.cl.size(); i++) {
            this.currentClass = this.classList.get(Symbol.symbol(prog.cl.elementAt(i).toString()));
            prog.cl.elementAt(i).accept(this);
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
    public ExpEncode visit(MainClass main) {

        this.currentClass = mainClass;

        ArrayList<Boolean> j = new ArrayList<Boolean>();
        j.add(false);

        currentFrame = currentFrame.newFrame(Symbol.symbol("main"),j);
        frames.push(currentFrame);

        Stm body = new EXPR(main.s.accept(this).getExp());
        ArrayList<Stm> lista = new ArrayList<Stm>();
        lista.add(body);

        currentFrame.procEntryExit1(lista);
        fragments.add(new Frag(body,currentFrame));
        frames.pop();

        return null;
    }

    @Override
    public ExpEncode visit(NormalClass normal) {

        this.currentClass = classList.get(Symbol.symbol(normal.i.toString()));
        for (int i = 0; i < normal.vl.size(); i++) {
            normal.vl.elementAt(i).accept(this);
        }

        for (int i = 0; i < normal.ml.size(); i++) {
            normal.ml.elementAt(i).accept(this);
        }

        return null;
    }

    @Override
    public ExpEncode visit(SubClass sub) {

        this.currentClass = classList.get(Symbol.symbol(sub.i.toString()));
        for (int i = 0; i < sub.vl.size(); i++) {
            sub.vl.elementAt(i).accept(this);
        }

        for (int i = 0; i < sub.ml.size(); i++) {
            sub.ml.elementAt(i).accept(this);
        }

        return null;
    }


    @Override
    public ExpEncode visit(MethodDeclaration method) {

        Stm body = new EXPR(new CONST(0));

        ArrayList<Boolean> j = new ArrayList<Boolean>();

        currentMethod = currentClass.getInMethods(method.i.toString());

        for (int i = 0; i <= method.fl.size(); i++) {
            j.add(false);
        }

        currentFrame = currentFrame.newFrame(Symbol.symbol(currentClass.toString()+"$"+ currentMethod.toString()), (java.util.List<Boolean>)j);
        frames.push(currentFrame);

        for (int i = 0; i < method.fl.size(); i++) {
            method.fl.elementAt(i).accept(this);
        }

        for (int i = 0; i < method.vl.size(); i++) {
            method.vl.elementAt(i).accept(this);
        }

        for (int i = 0; i < method.sl.size(); i++) {
            body = new SEQ(body,new EXPR(method.sl.elementAt(i).accept(this).getExp()));
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
    public ExpEncode visit(VarDeclaration var) {

        Field varEnd;

        if(currentMethod != null) {
            varEnd= currentMethod.getInParams(var.i.toString());

            if(varEnd != null) {
                varEnd.access = currentFrame.allocLocal(false);
                return null;
            }

            varEnd = currentMethod.getInLocals(var.i.toString());

            if(varEnd!= null) {
                varEnd.access = currentFrame.allocLocal(false);
                return null;
            }
        }

        varEnd = currentClass.getInAtb(var.i.toString());

        if(varEnd!= null) varEnd.access = currentFrame.allocLocal(false);

        return new ExpEncode(varEnd.access.exp((new TEMP(currentFrame.FP()))));
    }

    @Override
    public ExpEncode visit(Formal formal) {

        Field varEnd = currentMethod.getInParams(formal.i.toString());
        varEnd.access = currentFrame.allocLocal(false);

        return null;
    }

    @Override
    public ExpEncode visit(IntArrayType iat) {
        return null;
    }

    @Override
    public ExpEncode visit(BooleanType bt) {
        return null;
    }

    @Override
    public ExpEncode visit(IntegerType it) {
        return null;
    }

    @Override
    public ExpEncode visit(IdentifierType idt) {
        return null;
    }

    @Override
    public ExpEncode visit(Block block) {
        tree.Exp stm = new CONST(0);
        for (int i = 0; i < block.sl.size(); i++) {
            stm = new ESEQ(new SEQ(new EXPR(stm),new EXPR(block.sl.elementAt(i).accept(this).getExp())),new CONST(0));
        }

        return new ExpEncode(stm);
    }

    @Override
    public ExpEncode visit(While w) {

        Label test = new Label();
        Label body = new Label();
        Label end = new Label();

        ExpEncode cond = w.e.accept(this);
        ExpEncode stm = w.s.accept(this);

        return new ExpEncode(new ESEQ(new SEQ(new SEQ(new LABEL(test),
                new SEQ(new CJUMP(CJUMP.GT,cond.getExp(),new CONST(0), body, end),
                        new SEQ(new LABEL(body), new SEQ(new EXPR(stm.getExp()),new JUMP(test))))),
                new LABEL(end)), new CONST(0)));
    }

    @Override
    public ExpEncode visit(If i) {

        Label ifF = new Label();
        Label elseE = new Label();
        Label end = new Label();

        tree.Exp cond = i.e.accept(this).getExp();

        ExpEncode label1 = i.s1.accept(this);
        ExpEncode label2 = i.s2.accept(this);

        tree.Exp Cx = new ESEQ(new SEQ(new CJUMP(CJUMP.GT,cond,new CONST(0),ifF,elseE),
                new SEQ(new SEQ(new LABEL(ifF),new SEQ(new EXPR(label1.getExp()), new JUMP(end))),
                        new SEQ(new LABEL(elseE),new SEQ(new EXPR(label2.getExp()),new LABEL(end))))),
                new CONST(0));

        return new ExpEncode(Cx);
    }

    @Override
    public ExpEncode visit(syntaxtree.Print sP) {
        ExpEncode exp = sP.e.accept(this);
        tree.ExpList parameters= new tree.ExpList(exp.getExp(),null);

        return new ExpEncode( currentFrame.externalCall("print", Conversor.ExpListToList(parameters)));
    }

    @Override
    public ExpEncode visit(Assign a) {
        ExpEncode i = a.i.accept(this);
        ExpEncode e = a.e.accept(this);

        return new ExpEncode(new ESEQ(new MOVE( i.getExp(), e.getExp() ), new CONST(0)));
    }

    @Override
    public ExpEncode visit(ArrayAssign aa) {
        ExpEncode i = aa.i.accept(this);
        ExpEncode e1 = aa.e1.accept(this);
        ExpEncode e2 = aa.e2.accept(this);

        return new ExpEncode( new ESEQ(new MOVE(new MEM(new BINOP(BINOP.PLUS, i.getExp(), new BINOP(BINOP.MUL, e1.getExp(), new CONST(currentFrame.wordSize())))), e2.getExp()), new CONST(0)) );
    }

    @Override
    public ExpEncode visit(LessThan lt) {
        ExpEncode e1 = lt.e1.accept(this);
        ExpEncode e2 = lt.e2.accept(this);

        return new ExpEncode(new BINOP(BINOP.MINUS, e2.getExp(), e1.getExp()));
    }

    @Override
    public ExpEncode visit(And a) {
        ExpEncode e1 = a.e1.accept(this);
        ExpEncode e2 = a.e2.accept(this);

        return new ExpEncode(new BINOP(BINOP.AND, e1.getExp(), e2.getExp()));
    }

    @Override
    public ExpEncode visit(Times t) {
        ExpEncode e1 = t.e1.accept(this);
        ExpEncode e2 = t.e2.accept(this);

        return new ExpEncode(new BINOP(BINOP.MUL, e1.getExp(), e2.getExp()));
    }

    @Override
    public ExpEncode visit(ArrayLookup al) {
        ExpEncode e1 = al.e1.accept(this);
        ExpEncode e2 = al.e2.accept(this);

        return new ExpEncode(new MEM(new BINOP(BINOP.PLUS, e1.getExp(), new BINOP(BINOP.MUL, new BINOP(BINOP.PLUS, new CONST(1), e2.getExp()), new CONST(currentFrame.wordSize())))));

    }

    @Override
    public ExpEncode visit(Plus p) {
        ExpEncode e1 = p.e1.accept(this);
        ExpEncode e2 = p.e2.accept(this);

        return new ExpEncode(new BINOP(BINOP.PLUS, e1.getExp(), e2.getExp()));
    }

    @Override
    public ExpEncode visit(Minus m) {
        ExpEncode e1 = m.e1.accept(this);
        ExpEncode e2 = m.e2.accept(this);

        return new ExpEncode(new BINOP(BINOP.MINUS, e1.getExp(), e2.getExp()));
    }


    @Override
    public ExpEncode visit(ArrayLength al) {
        return new ExpEncode(new MEM(getAddress(Symbol.symbol(( (IdentifierExp) al.e).s)).getExp()));
    }

    @Override
    public ExpEncode visit(Call c) {
        ClassTable j = null;

        tree.ExpList list = null;
        for (int i = c.el.size()-1; i >= 0 ; i--) {
            list = new tree.ExpList(c.el.elementAt(i).accept(this).getExp(), list);
        }

        list = new tree.ExpList(c.e.accept(this).getExp(),list);

        if (c.e instanceof This) {
            j = currentClass;
        }

        if (c.e instanceof NewObject) {
            j = classList.get(Symbol.symbol(c.e.toString()));
        }

        if (c.e instanceof IdentifierExp) {
            Field var;

            var = currentMethod.getInParams(c.e.toString());
            if (var == null) var = currentMethod.getInLocals(c.e.toString());

            if (var == null) {
                j = currentClass;
            }
            else {
                j = classList.get(Symbol.symbol(var.getNome()));
            }

        }

        if(c.e instanceof Call) {
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
            System.out.println(c.e);
            System.out.println(c.i);
            System.out.println(c.e.getClass());
        }

        callStackClass = j;
        callStackMethod = j.getInMethods(c.i.toString());
        return new ExpEncode(new CALL(new NAME(new Label(j.getNome()+"$"+c.i.toString())),list));
    }

    @Override
    public ExpEncode visit(Integer i) {
        return new ExpEncode(new CONST(i.i));
    }

    @Override
    public ExpEncode visit(True t) {
        return new ExpEncode(new CONST(1));
    }

    @Override
    public ExpEncode visit(False f) {
        return new ExpEncode(new CONST(0));
    }

    @Override
    public ExpEncode visit(IdentifierExp ie) {
        return getAddress(Symbol.symbol(ie.s));
    }

    @Override
    public ExpEncode visit(This t) {
        return new ExpEncode(new MEM(new TEMP(currentFrame.FP())));
    }

    @Override
    public ExpEncode visit(NewArray na) {

        ExpEncode size = na.e.accept(this);

        tree.Exp allocation = new BINOP(BINOP.MUL, new BINOP(BINOP.PLUS, size.getExp(), new CONST(1)) ,new CONST(currentFrame.wordSize()));

        tree.ExpList parameters = new tree.ExpList(allocation,null);

        List<tree.Exp> convertedList = Conversor.ExpListToList(parameters);

        tree.Exp returnValue = currentFrame.externalCall("initArray", convertedList);

        return new ExpEncode(returnValue);
    }

    @Override
    public ExpEncode visit(NewObject no) {
        ClassTable j = classList.get(Symbol.symbol(no.i.toString()));
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
    public ExpEncode visit(Identifier i) {
        return getAddress(Symbol.symbol(i.s));
    }
}