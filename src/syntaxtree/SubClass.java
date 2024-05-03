package syntaxtree;
import IRtree.ExpEncode;
import IRtree.IRVisitor;
import syntaxtree.visitor.*;

public class SubClass extends ClassDeclaration {
    public Identifier i;
    public Identifier j;
    public VarList vl;
    public MethodList ml;

    public SubClass(Identifier ai, Identifier aj,
                    VarList avl, MethodList aml) {
        i=ai; j=aj; vl=avl; ml=aml;
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