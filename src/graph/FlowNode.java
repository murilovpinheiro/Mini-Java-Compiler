package graph;

import temp.Temp;
import temp.TempList;
import temp.Label;

public class FlowNode extends Node{
    public TempList def;
    public TempList use;
    public Label label;
    public boolean isMove;

    public FlowNode(Graph g, Label label) {
        super(g);
        this.label = label;
    }

    public FlowNode(Graph g, boolean isMove) {
        super(g);
        this.isMove = isMove;
    }

    public FlowNode(Node n) {
        super(n.mygraph);
    }

    public FlowNode(Node n, Label label) {
        super(n.mygraph);
        this.label = label;
    }
}
