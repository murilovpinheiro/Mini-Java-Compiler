package mips;
import temp.Temp;

public class InReg extends frame.Access {
    Temp temp;
    InReg(Temp t) {
        temp = t;
    }

    public tree.Exp exp(tree.Exp fp) {
        return new tree.TEMP(temp);
    }

    public String toString() {
        return temp.toString();
    }
}
