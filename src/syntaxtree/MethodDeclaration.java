package syntaxtree;
import IRtree.ExpEncode;
import IRtree.IRVisitor;
import syntaxtree.visitor.*;

public class MethodDeclaration {
    public Type t;
    public Identifier i;
    public FormalList fl;
    public VarList vl;
    public StatementList sl;
    public Expression e;

    public MethodDeclaration(Type at, Identifier ai, FormalList afl, VarList avl,
                             StatementList asl, Expression ae) {
        t=at; i=ai; fl=afl; vl=avl; sl=asl; e=ae;
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