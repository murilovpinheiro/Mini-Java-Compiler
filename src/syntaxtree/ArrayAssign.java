package syntaxtree;

import IRtree.ExpEncode;
import IRtree.IRVisitor;
import syntaxtree.visitor.*;

public class ArrayAssign extends Statement {
    public Identifier i;
    public Expression e1,e2;

    public ArrayAssign(Identifier ai, Expression ae1, Expression ae2) {
        i=ai; e1=ae1; e2=ae2;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }

    public ExpEncode accept(IRVisitor irVisitor) {
        return irVisitor.visit(this);
    }

}
