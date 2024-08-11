package IRtree;

import tree.*;
import frame.*;
public class Frag {
    public Stm body;
    public frame.Frame frame;
    public String nome;

    public Frag(Stm body, frame.Frame frame) {
        this.body = body;
        this.frame = frame;
    }

    public Frag(Stm body, frame.Frame frame, String nome) {
        this.body = body;
        this.frame = frame;
        this.nome = nome;
    }
}