package syntaxtree;
import IRtree.ExpEncode;
import IRtree.IRVisitor;
import syntaxtree.visitor.*;

public class Call extends Expression {
    public Expression e;
    public Identifier i;
    public ExpressionList el;

    public Call(Expression ae, Identifier ai, ExpressionList ael) {
        e=ae; i=ai; el=ael;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }

    @Override
    public ExpEncode accept(IRVisitor irVisitor) {
        return irVisitor.visit(this);
    }
}