/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SON
 */
public class Type {

    String nameType;
    boolean isArray = false;

    public Type() {

    }

    @Override
    public String toString() {
        String result = nameType;
        if (isArray == true) {
            result += "[]";
        }
        return result;
    }
}
