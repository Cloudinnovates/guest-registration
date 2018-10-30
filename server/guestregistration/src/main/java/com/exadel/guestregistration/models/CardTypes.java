package com.exadel.guestregistration.models;

import java.util.ArrayList;
import java.util.List;

public class CardTypes {
    public static final String GUEST = "Guest";
    private static final String FULL_TIME_EMPLOYEE = "Full-time Employee";
    private static final String VISITOR = "Visitor";
    private static final String TEMPORARY_EMPLOYEE = "Temporary Employee";

    public static List<String> getAllTypes(){
        ArrayList<String> types = new ArrayList<>();
        types.add(GUEST);
        types.add(FULL_TIME_EMPLOYEE);
        types.add(VISITOR);
        types.add(TEMPORARY_EMPLOYEE);
        return types;
    }

    public static boolean isTypeCorrect(String type){
        List<String> types = getAllTypes();
        for (String type1 : types) {
            if (type.equals(type1)) {
                return true;
            }
        }
        return false;
    }
}
