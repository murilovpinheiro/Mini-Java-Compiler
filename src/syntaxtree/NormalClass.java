package syntaxtree;
import IRtree.ExpEncode;
import IRtree.IRVisitor;
import syntaxtree.visitor.*;

public class NormalClass extends ClassDeclaration {
    public Identifier i;
    public VarList vl;
    public MethodList ml;

    public NormalClass(Identifier ai, VarList avl, MethodList aml) {
        i=ai; vl=avl; ml=aml;
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