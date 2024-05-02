package syntaxtree;
import syntaxtree.visitor.*;

public abstract class Type {
    public abstract void accept(Visitor v);
    public abstract Type accept(TypeVisitor v);
}