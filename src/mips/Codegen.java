package mips;
import frame.*;
import temp.*;
import tree.*;
import utils.Conversor;

import java.util.List;


public class Codegen {

    Frame frame;
    private assem.InstrList ilist = null, last = null;

    public Codegen(Frame f) {
        frame = f;
    }

    private void emit(assem.Instr inst) {
        if (last != null)
            last = last.tail = new assem.InstrList(inst, null);
        else
            last = ilist = new assem.InstrList(inst, null);
    }

    void munchStm(Stm s) {
        if (s instanceof SEQ) {
            munchStm(((SEQ) s).left);
            munchStm(((SEQ) s).right);
        }
        if (s instanceof MOVE)
            munchMove(((MOVE) s).dst, ((MOVE) s).src);
        if (s instanceof LABEL)
            emit(new assem.LABEL( (((LABEL) s).label) + ":\n", ((LABEL) s).label));
        if (s instanceof JUMP)
            munchJump((JUMP) s);
        if (s instanceof CJUMP)
            munchCJump((CJUMP) s);
        if (s instanceof EXPR) {
            if( ((EXPR)s).exp instanceof CALL)
            {
                Temp r = munchExp(((CALL) (((EXPR) s).exp)).func);
                TempList l = munchArgs(0, ((CALL) (((EXPR) s).exp)).args);
                emit(new assem.OPER("CALL " + ((NAME)((CALL) (((EXPR) s).exp)).func).label + "\n",
                        null, new TempList(r, l)));
            }
        }
    }

    Temp munchExp(Exp s) {
        if (s instanceof MEM)
            return munchMem((MEM) s);
        if (s instanceof CONST) {
            Temp r = new Temp();
            emit(new assem.OPER("ADDI "+r +" <- r0+" + ((CONST) s).value
                    + "\n", new TempList(r, null), null));
            return r;
        }
        if (s instanceof BINOP)
            return munchBinop((BINOP) s);
        if (s instanceof TEMP)
            return ((TEMP) s).temp;
        if( s instanceof NAME)
            return new Temp();
        return null;
    }

    private TempList munchArgs(int i, ExpList args) {
        ExpList temps = args;
        TempList retorno = null;
        while (temps != null) {
            Temp x = munchExp(temps.head);
            retorno = new TempList(x,retorno);
            temps = temps.tail;
        }
        return retorno;
    }

    private void munchCJump(CJUMP s) {
        Temp r = munchExp(new BINOP(BINOP.MINUS, s.left, s.right));
        if (s.relop == CJUMP.EQ) {
            emit(new assem.OPER("BRANCHEQ if " + r + " = 0 goto " + s.iftrue+"\n",
                    null, new TempList(r, null), new LabelList(
                    s.iftrue, null)));
        } else if (s.relop == CJUMP.GE) {
            emit(new assem.OPER("BRANCHGE if " + r + " >= 0 goto " + s.iftrue+"\n",
                    null, new TempList(r, null), new LabelList(
                    s.iftrue, null)));
        } else if (s.relop == CJUMP.LT) {
            emit(new assem.OPER("BRANCHLT if " + r + " < 0 goto " + s.iftrue+"\n",
                    null, new TempList(r, null), new LabelList(
                    s.iftrue, null)));
        } else if (s.relop == CJUMP.NE) {
            emit(new assem.OPER("BRANCHNE if " + r + " != 0 goto " + s.iftrue+"\n",
                    null, new TempList(r, null), new LabelList(
                    s.iftrue, null)));
        }
        else if (s.relop == CJUMP.GT) {
            emit(new assem.OPER("BRANCHGT if " + r + " > 0 goto " + s.iftrue+"\n",
                    null, new TempList(r, null), new LabelList(
                    s.iftrue,null)));
        }
        else
            emit(new assem.OPER("goto " + s.iffalse.toString()+"\n", null, null,
                    new LabelList(s.iffalse, null)));
    }

    private void munchJump(JUMP s) {
        emit(new assem.OPER("goto " + ((NAME) s.exp).label.toString() + "\n",
                null, null, new LabelList(((NAME) s.exp).label, null)));
    }

    void munchMove(MEM dst, Exp src) {
        if (dst.exp instanceof BINOP && ((BINOP) dst.exp).binop == BINOP.PLUS
                && ((BINOP) dst.exp).right instanceof CONST) {
            TempList destino = new TempList(
                    munchExp(((BINOP) dst.exp).left), null);

            TempList fonte = new TempList(munchExp(src), null);

            emit(new assem.OPER("STORE M[" + frame.FP() + " + " + destino.head
                    + " + " + ((CONST) ((BINOP) dst.exp).right).value
                    + " ] <- " + fonte.head+"\n", destino, fonte));
        }
        else if (dst.exp instanceof BINOP
                && ((BINOP) dst.exp).binop == BINOP.PLUS
                && ((BINOP) dst.exp).left instanceof CONST) {

            TempList destino = new TempList(
                    munchExp(((BINOP) dst.exp).right), null);

            TempList fonte = new TempList(munchExp(src), null);

            emit(new assem.OPER("STORE M[" + frame.FP() + " + " + destino.head
                    + " + " + ((CONST) ((BINOP) dst.exp).left).value + "] <- "
                    + fonte.head +"\n", destino, fonte));
        }

        else if (src instanceof MEM) {
            TempList destino = new TempList(munchExp(dst.exp), null);
            TempList fonte = new TempList(munchExp(src), null);

            emit(new assem.OPER("MOVEM M[" + frame.FP() + " + " + destino.head
                    + "] <- M[" + fonte.head + "]"+"\n", destino, fonte));
        }

        else if (dst.exp instanceof MEM && ((MEM) dst.exp).exp instanceof CONST) {

            TempList destino = new TempList(munchExp(dst.exp), null);
            TempList fonte = new TempList(munchExp(src), null);

            emit(new assem.OPER("STORE M[" + frame.FP() + " + "
                    + ((CONST) ((MEM) dst.exp).exp).value + "] <- "
                    + fonte.head+"\n", destino, fonte, null));
        }

        else {
            TempList destino = new TempList(munchExp(dst.exp), null);
            TempList fonte = new TempList(munchExp(src), null);
            emit(new assem.OPER("STORE M[" + frame.FP() + " + " + destino.head
                    + " + 0] <- " + fonte.head+"\n", destino, fonte));
        }

    }

    void munchMove(TEMP dst, Exp src) {
        Temp t = munchExp(src);
        emit(new assem.MOVE("MOVEA " + dst.temp+ " <- " + t+"\n", dst.temp, t));
    }

    void munchMove(Exp dst, Exp src) {

        // MOVE(d, e)
        if (dst instanceof MEM)
            munchMove((MEM) dst, src);
        if (dst instanceof TEMP && src instanceof CALL) {
            Temp r = munchExp(((CALL) src).func);
            TempList l = munchArgs(0, ((CALL) src).args);
            emit(new assem.OPER("CALL " + ((NAME)((CALL) src).func).label + "\n",
                    new TempList(r,null), l));
        } else if (dst instanceof TEMP)
            munchMove((TEMP) dst, src);
    }

    private Temp munchBinop(BINOP s) {
        // munchExp(BINOP(PLUS,e1,CONST (i)))
        if (s.binop == BINOP.PLUS && s.right instanceof CONST) {
            Temp r = new Temp();
            TempList fonte = new TempList(munchExp(s.left), null);
            emit(new assem.OPER("ADDI "+ r + " <- " + fonte.head + " + "
                    + ((CONST) s.right).value + "\n",
                    new TempList(r, null), fonte));
            return r;

        }

        // munchExp(BINOP(PLUS,CONST (i),e1))
        if (s.binop == BINOP.PLUS && s.left instanceof CONST) {
            Temp r = new Temp();
            TempList fonte = new TempList(munchExp(s.right), null);
            emit(new assem.OPER("ADDI "+ r + " <- "  + fonte.head + " + "
                    + ((CONST) s.left).value + "\n",
                    new TempList(r, null), fonte));
            return r;

        }

        // munchExp(BINOP(PLUS,e1,e2))
        if (s.binop == BINOP.PLUS) {
            Temp r = new Temp();
            TempList fonte = new TempList(munchExp(s.left),
                    new TempList(munchExp(s.right), null));
            emit(new assem.OPER("ADD "+ r + " <- " + fonte.head + "+"
                    + fonte.tail.head + "\n", new TempList(r, null), fonte));
            return r;
        }

        // munchExp(BINOP(MUL,e1,e2))
        if (s.binop == BINOP.MUL) {
            Temp r = new Temp();
            TempList fonte = new TempList(munchExp(s.left),
                    new TempList(munchExp(s.right), null));
            emit(new assem.OPER("MUL "+ r + " <- " + fonte.head + "*"
                    + fonte.tail.head + "\n", new TempList(r, null), fonte));
            return r;
        }

        // munchExp(BINOP(DIV,e1,e2))
        if (s.binop == BINOP.DIV) {
            Temp r = new Temp();
            TempList fonte = new TempList(munchExp(s.left),
                    new TempList(munchExp(s.right), null));
            emit(new assem.OPER("DIV "+ r + " <- " + fonte.head + "/"
                    + fonte.tail.head + "\n", new TempList(r, null), fonte));
            return r;
        }

        // munchExp(BINOP(SUB,e1,CONST(i)))
        if (s.binop == BINOP.MINUS && s.right instanceof CONST) {
            Temp r = new Temp();
            TempList fonte = new TempList(munchExp(s.left),null);
            emit(new assem.OPER("SUBI "+ r + " <- " + fonte.head + " - "
                    + ((CONST) s.right).value + "\n",
                    new TempList(r, null), fonte));
            return r;

        }

        // munchExp(BINOP(SUB,e1,e2))
        if (s.binop == BINOP.MINUS) {
            Temp r = new Temp();
            TempList fonte = new TempList(munchExp(s.left),
                    new TempList(munchExp(s.right), null));
            emit(new assem.OPER("SUB "+r +" <- " + fonte.head + "-"
                    + fonte.tail.head + "\n", new TempList(r, null), fonte));
            return r;
        }

        return null;
    }

    private Temp munchMem(MEM s) {

        // munchExp(MEM(BINOP(PLUS,e1,CONST(i))))
        if (s.exp instanceof BINOP
                && (((BINOP) s.exp).binop == BINOP.PLUS)
                && ((BINOP) s.exp).right instanceof CONST) {
            Temp r = new Temp();
            emit(new assem.OPER(
                    "LOAD "+ r +" <- M[‘s0+"
                            + ((CONST) (((BINOP) s.exp).right)).value
                            + "]\n",
                    new TempList(r, null),
                    new TempList(munchExp(((BINOP) s.exp).left), null)));
            return r;

        }

        // munchExp(MEM(BINOP(PLUS,CONST(i),e1)))
        if (s.exp instanceof BINOP
                && (((BINOP) s.exp).binop == BINOP.PLUS)
                && ((BINOP) s.exp).left instanceof CONST) {
            Temp r = new Temp();
            emit(new assem.OPER("LOAD "+r + " <- M[‘s0+"
                    + ((CONST) (((BINOP) s.exp).left)).value + "]\n",
                    new TempList(r, null), new TempList(
                    munchExp(((BINOP) s.exp).right), null)));
            return r;

        }

        // munchExp(MEM(CONST (i)))
        if (s.exp instanceof CONST) {
            Temp r = new Temp();
            emit(new assem.OPER("LOAD "+r+ "<- M[r0+" + ((CONST) s.exp).value
                    + "]\n", new TempList(r, null), null));
            return r;
        }

        // munchExp(MEM(e1))
        Temp r = new Temp();
        emit(new assem.OPER("LOAD "+ r +" <- M[‘s0+0]\n",
                new TempList(r, null), new TempList(munchExp(s.exp),
                null)));
        return r;

    }

    public assem.InstrList codegen(Stm s) {
        assem.InstrList l;
        munchStm(s);
        l = ilist;
        ilist = last = null;
        return l;
    }

    public assem.InstrList codegen(List<tree.Stm> stms) {
        assem.InstrList first=null, last=null;
        for (int i = 0; i < stms.size(); ++i) {
            assem.InstrList inst = codegen(stms.get(i));
            if (last == null) {
                first = last = inst;
            } else {
                while (last.tail != null) {
                    last = last.tail;
                }
                last = last.tail = inst;
            }
        }

        return first;
    }
}

