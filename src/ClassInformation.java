/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SON
 */
public class ClassInformation {

    private String className;
    private Variable[] variable;
    private Method[] method;
    private String[] implement;
    private int methodNumber = 0;
    private int variableNumber = 0;
    private int implementsNumber = 0;
    private String extend = "";

    private ClassInformation() {
        this.method = new Method[1000];
        this.variable = new Variable[1000];
        this.implement = new String[1000];
    }

    private ClassInformation(String className, String extend, String[] implement, int variableNumber, int methodNumber, int implementsNumber, Variable[] variable, Method[] method) {
        this.extend = extend;
        this.className = className;
        this.variable = variable;
        this.method = method;
        this.methodNumber = methodNumber;
        this.variableNumber = variableNumber;
        this.implement = implement;
        this.implementsNumber = implementsNumber;
    }

    public static ClassInformation CreateClass(String className, String extend, String[] implement, int variableNumber, int methodNumber, int implementsNumber, Variable[] variable, Method[] method) {
        return new ClassInformation(className, extend, implement, variableNumber, methodNumber, implementsNumber, variable, method);
    }

    public static ClassInformation CreateClass() {
        return new ClassInformation();
    }

    public void addVariable(String variableName, Type variableType) {
        this.variable[variableNumber] = new Variable(variableName, variableType);
        variableNumber++;
    }

    public void addMethod(String methodName, Type methodType, Type[] parameter, boolean isArray) {
        this.method[methodNumber] = new Method(methodName, methodType, parameter);
        methodNumber++;
    }

    public void addVariable(String privacy, String variableName, Type variableType) {
        this.variable[variableNumber] = new Variable(privacy, variableName, variableType);
        variableNumber++;
    }

    public void addMethod(String privacy, String methodName, Type methodType, Type[] parameter, int numOfParameter) {
        this.method[methodNumber] = new Method(privacy, methodName, methodType, parameter, numOfParameter);
        methodNumber++;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void addImplements(String implement) {
        this.implement[implementsNumber] = implement;
        implementsNumber++;
    }

    public String getClassName() {
        return className;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String[] getImplement() {
        return implement;
    }

    public void setImplement(String[] implement) {
        this.implement = implement;
    }

    @Override
    public String toString() {
        String result = "ClassInformation " + className + "{methodNumber=" + methodNumber + ", variableNumber=" + variableNumber + ", implementsNumber=" + implementsNumber + ", extend=" + extend + "\n variable :\n";
        for (int i = 0; i < variableNumber; i++) {
            result += variable[i].toString();
        }
        result += "\n method :\n";
        for (int i = 0; i < methodNumber; i++) {
            result += method[i].toString();
        }
        result += "\n implements = ";
        for (int i = 0; i < implementsNumber; i++) {
            result += implement[i].toString();
        }
        result += '}';
        return result;
    }

    public Variable[] getVariable() {
        return variable;
    }

    public void setVariable(Variable[] variable) {
        this.variable = variable;
    }

    public Method[] getMethod() {
        return method;
    }

    public void setMethod(Method[] method) {
        this.method = method;
    }

    public int getMethodNumber() {
        return methodNumber;
    }

    public void setMethodNumber(int methodNumber) {
        this.methodNumber = methodNumber;
    }

    public int getVariableNumber() {
        return variableNumber;
    }

    public void setVariableNumber(int variableNumber) {
        this.variableNumber = variableNumber;
    }

}
