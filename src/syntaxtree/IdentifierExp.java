package syntaxtree;
import IRtree.ExpEncode;
import IRtree.IRVisitor;
import syntaxtree.visitor.*;

public class IdentifierExp extends Expression {
    public String s;
    public IdentifierExp(String as) {
        s=as;
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