package syntaxtree;
import IRtree.ExpEncode;
import IRtree.IRVisitor;
import syntaxtree.visitor.*;

public class If extends Statement {
    public Expression e;
    public Statement s1,s2;

    public If(Expression ae, Statement as1, Statement as2) {
        e=ae; s1=as1; s2=as2;
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
