package utils;

import symbol.ClassTable;
import symbol.Field;
import symbol.MethodTable;
import symbol.Symbol;

import java.util.Hashtable;
import java.util.Iterator;

public class PrintUtil {

    public static String typeId(String id, String type) {
        return "{ " + type + " " + id + " }";
    }

    public static void printHierarchy(ClassTable mainClass, Hashtable<Symbol, ClassTable> classList) {
        System.out.println("[Main] " + mainClass.getNome());

        Iterator<Symbol> iterator = classList.keySet().iterator();
        while (iterator.hasNext()) {
            ClassTable currentClass = classList.get(iterator.next());
            System.out.println("[Classe] " + currentClass.getNome());
            for (Field currentAtr : currentClass.getAtributos()) {
                System.out.println("  L_[Atributo] " + currentAtr.getTipo() + " " + currentAtr.getNome());
            }
            for (MethodTable currentMtd : currentClass.getMetodos()) {
                System.out.println("  L_[Método] " + currentMtd.getTipo() + " " + currentMtd.getNome());
                for (Field param : currentMtd.getParametros()) {
                    System.out.println("      L_[Parâmetro] " + param.getTipo() + " " + param.getNome());
                }
                for (Field local : currentMtd.getVlocais()) {
                    System.out.println("      L_[Var Local] " + local.getTipo() + " " + local.getNome());
                }
            }
        }

    }

}
