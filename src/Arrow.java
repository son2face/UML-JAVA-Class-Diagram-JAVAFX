
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;

public class Arrow extends Group {

    Polyline headArrow = new Polyline(5, 0, 0, 10, 10, 10, 5, 0);
    private final Line line = new Line();
//    DoubleBinding x1;
//    DoubleBinding y1;
//    DoubleBinding x2;
//    DoubleBinding y2;

    public Arrow() {
        getChildren().addAll(line, headArrow);
    }

    public void draw(DoubleBinding x1, DoubleBinding y1, DoubleBinding x2, DoubleBinding y2, double radius, String color, double translateX, double translateY, double translateXLine) {
//        result.setStrokeLineCap(StrokeLineCap.BUTT);
//        result.setStrokeWidth(3);
//        headArrow.setRadius(radius);
        headArrow.setFill(Color.web(color));
//        headArrow.centerXProperty().bind(x1);
//        headArrow.centerYProperty().bind(y1);
        headArrow.setRotate(radius);
        headArrow.layoutXProperty().bind(x1.add(translateX));
        headArrow.layoutYProperty().bind(y1.add(translateY - 5));
        line.setFill(Color.web(color));
        line.startXProperty().bind(x1.add(translateXLine));
        line.startYProperty().bind(y1.add(translateY));
        line.endXProperty().bind(x2);
        line.endYProperty().bind(y2.add(translateY));
    }
}
