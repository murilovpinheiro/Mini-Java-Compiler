package symbol;

import java.util.ArrayList;
import java.util.Hashtable;

public class Table {

//    public static Hashtable<Symbol, String> inner_table = new Hashtable<Symbol, String>();

    //symbol, type as string
    public static Hashtable<Symbol, String> inner_table = new Hashtable<Symbol, String>();

    //Retorna o próprio objeto, pra concatenar function calls
    //table.put(a, b).put(c, d).put(e, f)...
    public static void put(Symbol key, String value){
        inner_table.put(key, value);
    }
    public static String get(Symbol key){
        return inner_table.get(key);
    }
    public static java.util.Enumeration keys(){
        return inner_table.keys();
    }
}


