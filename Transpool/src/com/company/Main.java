package com.company;

import Model.Map;
import Model.Road;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Main {

    public static void main(String[] args) {
        try {
            Map.init(5, 5, new HashSet<>(), new HashSet<>());
        } catch (OperationNotSupportedException e) {
            e.printStackTrace();
        }

    }
}
