package utils;

import java.util.ArrayList;
import java.util.List;
import temp.TempList;
import tree.ExpList;

public class Conversor {

    public static List<tree.Exp> ExpListToList(tree.ExpList expList){

        ArrayList<tree.Exp> list = new ArrayList<tree.Exp>();

        ExpList temp = expList;
        while (temp != null) {
            list.add(temp.head);
            temp = temp.tail;
        }

        return list;
    }
    public static tree.ExpList ListToExpList(List<tree.Exp> list) {
        tree.ExpList expList = null;

        for (int i = list.size()-1; i >= 0; --i) {
            expList = new tree.ExpList(list.get(i), expList);
        }
        return expList;
    }

    public static List<temp.Temp> TempListToList(temp.TempList tempList) {
        ArrayList<temp.Temp> list = new ArrayList<temp.Temp>();

        TempList temp = tempList;
        while (temp != null) {
            list.add(temp.head);
            temp = temp.tail;
        }

        return list;
    }
    public static temp.TempList ListToTempList(List<temp.Temp> list) {
        temp.TempList tempList = null;

        for (int i = list.size()-1; i >= 0; --i) {
            tempList = new temp.TempList(list.get(i), tempList);
        }
        return tempList;
    }

    public static temp.Temp[] TempListToArray(TempList tempList) {
        temp.Temp array[] = new temp.Temp[Conversor.TempListToList(tempList).size()];
        TempList temp = tempList;

        for (int i = 0; i < array.length; i++) {
            array[i] = temp.head;
            temp = temp.tail;
        }

        return array;
    }

    public static temp.TempList ArrayToTempList(temp.Temp array[]) {
        temp.TempList tempList = null;

        for (int i = array.length-1; i >= 0; --i) {
            tempList = new temp.TempList(array[i], tempList);
        }

        return tempList;
    }

}