/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SON
 */
public class RelationShip {

    private String[] fileUrl;
    private int numFileUrl;
    private ClassInformation[] classInfo;
    private int numClass = 0;

    public RelationShip() {
    }

    public RelationShip(String[] fileUrl, int numFileUrl, ClassInformation[] classInfo, int numClass) {
        this.fileUrl = fileUrl;
        this.numFileUrl = numFileUrl;
        this.classInfo = classInfo;
        this.numClass = numClass;
    }

    public RelationShip(String[] fileUrl) {
        this.fileUrl = fileUrl;
        this.numFileUrl = fileUrl.length;
    }

    public RelationShip(String[] fileUrl, int numFileUrl) {
        this.fileUrl = fileUrl;
        this.numFileUrl = numFileUrl;
    }

    public void process() {
        classInfo = new ClassInformation[1000];
        for (int i = 0; i < numFileUrl; i++) {
            Parser temp = new Parser(fileUrl[i]);
            ClassInformation[] tempClass = temp.getClassInfo();
            int tempNum = temp.getNumOfClass();
            for (int j = 0; j < tempNum; j++) {
                classInfo[numClass] = tempClass[j];
                numClass++;
            }
        }
    }

    public String toStringClass() {
        String result = "";
        for (int i = 0; i < numClass; i++) {
            result += classInfo[i].toString() + "\n";
        }
        return result;
    }

//    public static void main(String[] args) {
//        String[] fileUrl = {"Clazz.java", "Person.java", "PersonFactory.java", "Student.java", "Teacher.java"};
//        RelationShip test = new RelationShip(fileUrl);
//        test.process();
//        System.out.println(test.toStringClass());
//    }
}
