
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parser {

    int stack = 0;
    int statusFind = 0;
    StreamTokenizer st = null;
    private ClassInformation[] classInfo = new ClassInformation[200];
    private int numOfClass = 0;

    Parser(String fileUrl) {
        try {
            FileReader rd = new FileReader(fileUrl);
            st = new StreamTokenizer(rd);
            boolean isInMethod = false;
            st.parseNumbers();
            st.wordChars('_', '_');
            st.eolIsSignificant(true);
            st.slashSlashComments(true);
            st.slashStarComments(true);
            int token = st.nextToken();
            while (token != StreamTokenizer.TT_EOF) {
                findClass();
                token = find();
            }
            rd.close();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Parser x = new Parser("LinkedList.java");
    }

    private int findClass() {
        try {
            int token = st.nextToken();
            while (token != StreamTokenizer.TT_EOF) {
                if ((token == StreamTokenizer.TT_WORD) && (st.sval.equals("class"))) {
                    classInfo[numOfClass] = ClassInformation.CreateClass();
                    st.nextToken();
                    classInfo[numOfClass].setClassName(st.sval);
                    while (token != StreamTokenizer.TT_EOF) {
                        switch (token) {
                            case StreamTokenizer.TT_WORD:
                                String word = st.sval;
                                if (word.equals("extends")) {
                                    st.nextToken();
                                    classInfo[numOfClass].setExtend(st.sval);
                                }
                                if (word.equals("implements")) {
                                    token = st.nextToken();
                                    while (token != StreamTokenizer.TT_EOF && token != '{') {
                                        if (token == StreamTokenizer.TT_WORD) {
                                            classInfo[numOfClass].addImplements(st.sval);
                                        }
                                        token = st.nextToken();
                                    }
                                    return 1;
                                }
                                break;
                            case '{':
                                return 1;
                        }
                        token = st.nextToken();
                    }
                    return 1;
                }
                token = st.nextToken();
            }
        } catch (IOException ex) {
            Logger.getLogger(UML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return StreamTokenizer.TT_EOF;
    }

    private int find() {
        int token = 0;
        try {
            String temp[] = new String[350];
            int num = 0;
            token = st.nextToken();
            while ((char) st.ttype == ';') {
                token = st.nextToken();
            }
            boolean isMethod = false;
            while (token != StreamTokenizer.TT_EOF) {
                switch (token) {
                    case StreamTokenizer.TT_WORD:
                        temp[num] = st.sval;
                        num++;
                        break;
                    case '(':
                        isMethod = true;
                        temp[num] = "(";
                        num++;
                        break;
                    case ')':
                        temp[num] = ")";
                        num++;
                        break;
                    case ']':
                        temp[num] = "]";
                        num++;
                        break;
                    case '[':
                        temp[num] = "[";
                        num++;
                        break;
                    case ',':
                        temp[num] = ",";
                        num++;
                        break;
                    case '@':
                        temp[num] = "@";
                        num++;
                        break;
                    case '<':
                        escapeGeneric();
                        break;
                    case '{':
                        escapeInsideMethod();
                        break;
                    case '}':
                        numOfClass++;
                        return 1;
                    case '=':
                        while (token != StreamTokenizer.TT_EOF && token != ';') {
                            token = st.nextToken();
                        }
                    case ';':
                        if (num != 0) {
                            if (isMethod == true) {
                                getMethod(temp, num);
                            } else {
                                getVariable(temp, num);
                            }
                        }
                        find();
                        return 1;
                }
                if ((char) st.ttype == '}') {
                    break;
                }
                token = st.nextToken();
            }
            if (num != 0) {
                if (isMethod == true) {
                    getMethod(temp, num);
                } else {
                    getVariable(temp, num);
                }
                find();
                return 1;
            }
        } catch (IOException ex) {
            Logger.getLogger(UML.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (token == StreamTokenizer.TT_EOF) {
            return StreamTokenizer.TT_EOF;
        }
        return 1;
    }

    private int escapeInsideMethod() {
        try {
            int stack = 1;
            int token = st.nextToken();
            while (token != StreamTokenizer.TT_EOF) {
                switch (token) {
                    case '{':
                        stack++;
                        break;
                    case '}':
                        stack--;
                        break;
                }
                if (stack == 0) {
                    return 1;
                }
                token = st.nextToken();
            }
        } catch (IOException ex) {
            Logger.getLogger(UML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return StreamTokenizer.TT_EOF;
    }

    private void getMethod(String token[], int number) {
        if (number > 0) {
            String privacy = "default";
            String methodName = null;
            Type[] parameter = null;
            int paraNum = 0;
            Type methodType = new Type();
            int current = 0;
            if (token[current].equals("@") && token[current + 1].equals("Override")) {
                current += 2;
            }
            if (token[current].equals("public") || token[current].equals("private") || token[current].equals("protected")) {
                privacy = token[current];
                current++;
            }
            if (token[current].equals("static")) {
                current++;
            }
            if (token[current].equals("abstract")) {
                current++;
            }
            if (token[current].equals("final")) {
                current++;
            }
            if (token[current + 1].equals("(")) {
                methodType.nameType = "constructor";
            } else {
                methodType.nameType = token[current];
                current++;
                if (token[current].equals("[")) {
                    methodType.isArray = true;
                    current++;
                }
                if (token[current].equals("]")) {
                    current++;
                }
            }
            methodName = token[current];
            current++;
            if (token[current].equals("[")) {
                methodType.isArray = true;
                current++;
            }
            if (token[current].equals("]")) {
                current++;
            }
            current++;
            if (current < number - 1) {
                parameter = new Type[100];
                while (current < number - 1) {
                    parameter[paraNum] = new Type();
                    parameter[paraNum].nameType = token[current];
                    current++;  // name
                    if (current < number - 1) {
                        if (token[current].equals("[")) {
                            parameter[paraNum].isArray = true;
                            current++;
                        }
                    }
                    if (current < number - 1) {
                        if (token[current].equals("]")) {
                            current++;
                        }
                    }
                    current++;
                    if (current < number - 1) {
                        if (token[current].equals("[")) {
                            parameter[paraNum].isArray = true;
                            current++;
                        }
                    }
                    if (current < number - 1) {
                        if (token[current].equals("]")) {
                            current++;
                        }
                    }
                    current++;
                    paraNum++;
                }
            }
            classInfo[numOfClass].addMethod(privacy, methodName, methodType, parameter, paraNum);
        }
    }

    private void getVariable(String[] token, int number) {
        if (number > 0) {
            String privacy = "default";
            String variableName = null;
            Type variableType = new Type();
            int current = 0;
            if (token[current].equals("public") || token[current].equals("private") || token[current].equals("protected")) {
                privacy = token[current];
                current++;
            }
            if (token[current].equals("static")) {
                current++;
            }
            if (token[current].equals("final")) {
                current++;
            }
            variableType.nameType = token[current];
            current++;
            if (current < number) {
                if (token[current].equals("[")) {
                    variableType.isArray = true;
                    current++;
                }
            }
            if (current < number) {
                if (token[current].equals("]")) {
                    current++;
                }
            }
            variableName = token[current];
            current++;
            if (current < number) {
                if (token[current].equals("[")) {
                    variableType.isArray = true;
                    current++;
                }
            }
            if (current < number) {
                if (token[current].equals("]")) {
                    current++;
                }
            }
            classInfo[numOfClass].addVariable(privacy, variableName, variableType);
            while (current < number && token[current].equals(",")) {
                current++;
                if (current < number) {
                    String name = token[current];
                    current++;
                    Type t = new Type();
                    t.nameType = variableType.nameType;
                    if (current < number) {
                        if (token[current].equals("[")) {
                            t.isArray = true;
                            current++;
                        }
                    }
                    if (current < number) {
                        if (token[current].equals("]")) {
                            current++;
                        }
                    }
                    classInfo[numOfClass].addVariable(privacy, name, t);
                }
            }

        }
    }

    private int escapeGeneric() {
        int token = 0;
        try {
            token = st.nextToken();
            while (token != '>' && token != StreamTokenizer.TT_EOF) {
                token = st.nextToken();
            }
        } catch (IOException ex) {
            Logger.getLogger(UML.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (token == StreamTokenizer.TT_EOF) {
            return StreamTokenizer.TT_EOF;
        }
        return 1;
    }

    public ClassInformation[] getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ClassInformation[] classInfo) {
        this.classInfo = classInfo;
    }

    public int getNumOfClass() {
        return numOfClass;
    }

}
