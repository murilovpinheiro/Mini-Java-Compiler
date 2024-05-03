package tree;
public class ExpList {
  public Exp head;
  public ExpList tail;
  public ExpList(Exp h, ExpList t) {head=h; tail=t;}

  public void add(Exp expIn) {
    if (tail == null) {
      tail = new ExpList(expIn, null);
    } else {
      tail.add(expIn);
    }
  }

}



