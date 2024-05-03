package syntaxtree;

import java.util.Vector;

public class ExpressionList {
    private Vector list;

    public ExpressionList() {
        list = new Vector();
    }

    public void addElement(Expression n) {
        list.addElement(n);
    }

    public Expression elementAt(int i)  {
        return (Expression)list.elementAt(i);
    }

    public int size() {
        return list.size();
    }
}