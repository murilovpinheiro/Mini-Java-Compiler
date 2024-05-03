package syntaxtree;

import java.util.Vector;


public class VarList {
    private Vector list;

    public VarList() {
        list = new Vector();
    }

    public void addElement(VarDeclaration n) {
        list.addElement(n);
    }

    public VarDeclaration elementAt(int i)  {
        return (VarDeclaration)list.elementAt(i);
    }

    public int size() {
        return list.size();
    }
}