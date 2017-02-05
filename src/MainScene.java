
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author SON
 */
public class MainScene extends Scene {

    private List<ClassInformation> classInfo;
    private double x = 1;
    private double y = 1;
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;

    public static MainScene createMainScene(List<ClassInformation> classInfo) {
        Group root = new Group();
        MainScene result = new MainScene(root, classInfo);
        return result;
    }

    public MainScene(Group root, List<ClassInformation> classInfo) {
        super(root, 900, 900);
        ImageView bg = createBackGound("bg.jpg", this);
        this.classInfo = classInfo;
        Group classes = new Group();
        Group lines = new Group();
        root.getChildren().addAll(bg, lines, classes);
        ClassInformation[] array = new ClassInformation[1];
        array = (ClassInformation[]) classInfo.toArray(array);
        VBox[] clArray = new VBox[array.length];
        for (int i = 0; i < array.length; i++) {
            clArray[i] = drawClass(array[i]);
            classes.getChildren().add(clArray[i]);
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i].getExtend().equals("") == false) {
                for (int j = 0; j < array.length; j++) {
                    if (array[j].getClassName().equals(array[i].getExtend())) {
                        lines.getChildren().add(createLineExtends(clArray[i], clArray[j]));
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < array.length; i++) {
            Variable[] variable = array[i].getVariable();
            for (int j = 0; j < array[i].getVariableNumber(); j++) {
                if (variable[j].getType().equals("") == false) {
                    for (int k = 0; k < array.length; k++) {
                        if (array[k].getClassName().equals(variable[j].getType().nameType) && k != i) {
                            lines.getChildren().add(createLineAsociate(clArray[i], clArray[k]));
                            break;
                        }
                    }
                }
            }
            Method[] method = array[i].getMethod();
            for (int j = 0; j < array[i].getMethodNumber(); j++) {
                if (method[j].getType().equals("") == false) {
                    for (int k = 0; k < array.length; k++) {
                        if (array[k].getClassName().equals(method[j].getType()) && k != i) {
                            lines.getChildren().add(createLineAsociate(clArray[i], clArray[k]));
                            break;
                        }
                    }
                }
            }
        }
    }

    protected static ImageView createBackGound(String imgUrl, Scene scene) {
        ImageView backGround = new ImageView(imgUrl);
        backGround.fitHeightProperty().bind(scene.heightProperty());
        backGround.fitWidthProperty().bind(scene.widthProperty());
        backGround.setVisible(true);
        return backGround;
    }

    public VBox drawClass(ClassInformation classInfo) {
        Text nameClass = makeText(classInfo.getClassName(), "black");
        nameClass.setFont(Font.font(30));
        nameClass.setTextAlignment(TextAlignment.CENTER);
        VBox name = new VBox(nameClass);
        name.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        VBox method = new VBox();
        Method[] me = classInfo.getMethod();
        int numMe = classInfo.getMethodNumber();
        for (int i = 0; i < numMe; i++) {
            method.getChildren().add(makeText(getNotationPrivacy(me[i].getPrivacy()) + me[i].toString(), getColorPrivacy(me[i].getPrivacy())));
        }
        method.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        VBox variable = new VBox();
        Variable[] va = classInfo.getVariable();
        int numVa = classInfo.getVariableNumber();
        for (int i = 0; i < numVa; i++) {
            variable.getChildren().add(makeText(getNotationPrivacy(va[i].getPrivacy()) + va[i].toString(), getColorPrivacy(va[i].getPrivacy())));
        }
        variable.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        VBox classUI = new VBox(name, variable, method);
        classUI.setOnMousePressed(circleOnMousePressedEventHandler);
        classUI.setOnMouseDragged(circleOnMouseDraggedEventHandler);
        Button extend = new Button("-");
        extend.setOnMouseClicked(event -> {
            if (extend.getText().equals("+")) {
                classUI.getChildren().add(variable);
                classUI.getChildren().add(method);
                extend.setText("-");
            } else {
                classUI.getChildren().remove(variable);
                classUI.getChildren().remove(method);
                extend.setText("+");
            }
        });
        name.getChildren().add(extend);
        return classUI;
    }

    private static Text makeText(String text, String color) {
        Text nameClass = new Text(text);
        nameClass.setFill(Color.web(color));
        return nameClass;
    }

    private static String getColorPrivacy(String privacy) {
        if (privacy.equals("protected")) {
            return "purple";
        }
        if (privacy.equals("private")) {
            return "red";
        }
        if (privacy.equals("public")) {
            return "green";
        }
        return "black";
    }

    private static String getNotationPrivacy(String privacy) {
        if (privacy.equals("protected")) {
            return "#";
        }
        if (privacy.equals("private")) {
            return "-";
        }
        if (privacy.equals("public")) {
            return "+";
        }
        return "";
    }
    EventHandler<MouseEvent> circleOnMousePressedEventHandler
            = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
            orgTranslateX = ((Node) (t.getSource())).getTranslateX();
            orgTranslateY = ((Node) (t.getSource())).getTranslateY();
        }
    };

    EventHandler<MouseEvent> circleOnMouseDraggedEventHandler
            = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;
            ((Node) (t.getSource())).setTranslateX(newTranslateX);
            ((Node) (t.getSource())).setTranslateY(newTranslateY);
        }
    };

    private Arrow createLineExtends(Node parent, Node children) {
        Arrow result = new Arrow();
        result.draw(children.layoutXProperty().add(children.translateXProperty()), children.layoutYProperty().add(children.translateYProperty()), parent.layoutXProperty().add(parent.translateXProperty()).add(parent.prefWidth(0)), parent.layoutYProperty().add(parent.translateYProperty()), 90, "white", -10, 10, -10);
        return result;
    }

    private Arrow createLineAsociate(Node parent, Node children) {
        Arrow result = new Arrow();
        result.draw(children.layoutXProperty().add(children.translateXProperty()).add(children.prefWidth(0)), children.layoutYProperty().add(children.translateYProperty()), parent.layoutXProperty().add(parent.translateXProperty()), parent.layoutYProperty().add(parent.translateYProperty()), -90, "black", 0, 30, 10);
        return result;
    }
}
