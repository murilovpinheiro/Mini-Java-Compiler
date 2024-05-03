package syntaxtree;

import java.util.Vector;


public class MethodList {
    private Vector list;

    public MethodList() {
        list = new Vector();
    }

    public void addElement(MethodDeclaration n) {
        list.addElement(n);
    }

    public MethodDeclaration elementAt(int i)  {
        return (MethodDeclaration)list.elementAt(i);
    }

    public int size() {
        return list.size();
    }
}