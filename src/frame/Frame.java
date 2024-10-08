package frame;
import java.util.List;

public abstract class Frame implements temp.TempMap {
    public temp.Label name;
    public List<Access> formals;
    public abstract Frame newFrame(symbol.Symbol name, List<Boolean> formals);
    public abstract Access allocLocal(boolean escape);
    public abstract temp.Temp FP();
    public abstract int wordSize();
    public abstract tree.Exp externalCall(String func, List<tree.Exp> args);
    public abstract temp.Temp RV();
    public abstract String string(temp.Label label, String value);
    public abstract temp.Label badPtr();
    public abstract temp.Label badSub();
    public abstract String tempMap(temp.Temp temp);
    public abstract List<assem.Instr> codegen(List<tree.Stm> stms);
    public abstract void procEntryExit1(List<tree.Stm> body);
    public abstract void procEntryExit2(List<assem.Instr> body);
    public abstract void procEntryExit3(List<assem.Instr> body);
    public abstract temp.Temp[] registers();
    public abstract void spill(List<assem.Instr> insns, temp.Temp[] spills);
    public abstract String programTail(); //append to end of target code
}
