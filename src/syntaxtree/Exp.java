package syntaxtree;

import java.util.Vector;
import syntaxtree.visitor.*;

public abstract class Exp {
    public abstract void accept(Visitor v);
    public abstract Type accept(TypeVisitor v);
}




