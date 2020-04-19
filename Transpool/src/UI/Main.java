package UI;

import model.Map;

import javax.naming.OperationNotSupportedException;
import java.util.HashSet;

public class Main {

    public static void main(String[] args) {
//        try {
//            Map.init(5, 5, new HashSet<>(), new HashSet<>());
//        } catch (OperationNotSupportedException e) {
//            e.printStackTrace();
//        }
        TranspoolConsole.start();

    }
}
