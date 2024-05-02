package utils;

public class ErrorMsg {
    public boolean anyErrors;
    private String msg;

    public void complain(String msg) {
        anyErrors = true;
        System.out.println(msg);
    }
}