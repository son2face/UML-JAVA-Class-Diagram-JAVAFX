/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 *
 * @author SON
 */
public class MenuItem extends HBox {

    private String color;
    private TriCircle c1 = new TriCircle(), c2 = new TriCircle();
    private Label label;
    private Runnable script;
    private String font;
    private double size = 25;

    public MenuItem(String name, String color, String font) {
        super(15);
        this.color = color;
        this.font = font;
        setAlignment(Pos.CENTER);
        label = new Label(name);
        label.setEffect(new GaussianBlur(2));
        getChildren().addAll(c1, label, c2);
        setActive(false);
        setOnActivate(() -> System.out.println(name + " activated"));
    }

    public MenuItem(String name, String color, String font, double size) {
        super(15);
        this.color = color;
        this.font = font;
        this.size = size;
        setAlignment(Pos.CENTER);
        label = new Label(name);
        label.setEffect(new GaussianBlur(2));
        getChildren().addAll(c1, label, c2);
        setActive(false);
        setOnActivate(() -> System.out.println(name + " activated"));
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }

    public void setActive(boolean b) {
        c1.setVisible(b);
        c2.setVisible(b);
        if (b) {
            label.setTextFill(Color.web(color, .8));
            label.setFont(Font.font(font, FontWeight.BOLD, FontPosture.ITALIC, size));
        } else {
            label.setTextFill(Color.web(color, .5));
            label.setFont(Font.font(font, FontWeight.BOLD, size));
        }
    }

    public void setOnActivate(Runnable r) {
        script = r;
    }

    public void activate() {
        if (script != null) {
            script.run();
        }
    }
}
