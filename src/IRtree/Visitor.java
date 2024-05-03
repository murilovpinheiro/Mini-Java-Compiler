package IRtree;

import syntaxtree.*;
import syntaxtree.Integer;

public interface Visitor {
    public ExpEncode visit(Program n);
    public ExpEncode visit(MainClass n);
    public ExpEncode visit(NormalClass n);
    public ExpEncode visit(SubClass n);
    public ExpEncode visit(VarDeclaration n);
    public ExpEncode visit(MethodDeclaration n);
    public ExpEncode visit(Formal n);
    public ExpEncode visit(IntArrayType n);
    public ExpEncode visit(BooleanType n);
    public ExpEncode visit(IntegerType n);
    public ExpEncode visit(IdentifierType n);
    public ExpEncode visit(Block n);
    public ExpEncode visit(If n);
    public ExpEncode visit(While n);
    public ExpEncode visit(Print n);
    public ExpEncode visit(Assign n);
    public ExpEncode visit(ArrayAssign n);
    public ExpEncode visit(And n);
    public ExpEncode visit(LessThan n);
    public ExpEncode visit(Plus n);
    public ExpEncode visit(Minus n);
    public ExpEncode visit(Times n);
    public ExpEncode visit(ArrayLookup n);
    public ExpEncode visit(ArrayLength n);
    public ExpEncode visit(Call n);
    public ExpEncode visit(Integer n);
    public ExpEncode visit(True n);
    public ExpEncode visit(False n);
    public ExpEncode visit(IdentifierExp n);
    public ExpEncode visit(This n);
    public ExpEncode visit(NewArray n);
    public ExpEncode visit(NewObject n);
    public ExpEncode visit(Not n);
    public ExpEncode visit(Identifier n);
}
