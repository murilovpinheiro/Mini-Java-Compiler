package syntaxtree;
import IRtree.ExpEncode;
import IRtree.IRVisitor;
import syntaxtree.visitor.*;

public abstract class ClassDeclaration {
    public abstract void accept(Visitor v);
    public abstract Type accept(TypeVisitor v);
    public abstract ExpEncode accept(IRVisitor irVisitor);
}
