package mips;

public class InFrame extends frame.Access {
    int offset;
    InFrame(int o) {
        offset = o;
    }

    public tree.Exp exp(tree.Exp fp) {
        return new tree.MEM
                (new tree.BINOP(tree.BINOP.PLUS, fp, new tree.CONST(offset)));
    }

    public String toString() {
        Integer offset = Integer.valueOf(this.offset);
        return offset.toString();
    }
}
