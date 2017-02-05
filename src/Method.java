/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SON
 */
public class Method {

    String privacy = "default";
    String name;
    Type type;
    Type[] parameter;
    int numOfParameter = 0;

    public Method(String privacy, String name, Type type) {
        this.privacy = privacy;
        this.name = name;
        this.type = type;
    }

    public Method(String privacy, String name, Type type, Type[] parameter, int numOfParameter) {
        this.name = name;
        this.privacy = privacy;
        this.type = type;
        this.parameter = parameter;
        this.numOfParameter = numOfParameter;
    }

    public Method(String name, Type type, Type[] parameter) {
        this.name = name;
        this.type = type;
        this.parameter = parameter;
        numOfParameter = this.parameter.length;
    }

    public Method(String name, Type type, boolean isArray) {
        this.name = name;
        this.type = type;
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

    public Type[] getParameter() {
        return parameter;
    }

    public void setParameter(Type[] parameter) {
        this.parameter = parameter;
        numOfParameter = this.parameter.length;
    }

    public void addParameter(Type parameter) {
        this.parameter[numOfParameter] = parameter;
        numOfParameter++;
    }

    public String getPrivacy() {
        return privacy;
    }

    @Override
    public String toString() {
        String result = type.nameType;
        result += " " + name + "(";
        for (int i = 0; i < numOfParameter - 1; i++) {
            result += parameter[i] + ", ";
        }
        if (numOfParameter > 0) {
            result += parameter[numOfParameter - 1];
        }
        result += ")";
        return result;
    }

}
