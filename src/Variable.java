/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SON
 */
public class Variable {

    String privacy = "default";
    String name;
    Type type;

    public Variable(String privacy, String name, Type type) {
        this.privacy = privacy;
        this.name = name;
        this.type = type;
    }

    public Variable(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        String result = type + " " + name;
        return result;
    }
}
