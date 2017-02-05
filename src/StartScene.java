
import java.awt.Label;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author SON
 */
public class StartScene extends Scene {

    private final List<ClassInformation> classInfo = new ArrayList<>();
    private final List<String> javaFiles = new ArrayList<>();
    private int currentIndex = -1;
    private ProgressIndicator progressIndicator;
    private AtomicBoolean loading = new AtomicBoolean();
    private MainScene mainScene;
    private Stage primaryStage;

    public static StartScene createStartScene(Stage primaryStage) {
        Group root = new Group();
        StartScene result = new StartScene(root, primaryStage);
        return result;
    }

    public StartScene(Group root, Stage primaryStage) {
        super(root, 450, 450);
        ImageView bg = createBackGound("bg.jpg", this);
        progressIndicator = createProgressIndicator(this);
        progressIndicator.setVisible(true);
        this.primaryStage = primaryStage;
        root.getChildren().addAll(bg, dragUI());
        setupDragNDrop(this);
        progressIndicator = createProgressIndicator(this);
        root.getChildren().addAll(progressIndicator);
    }

    protected static ImageView createBackGound(String imgUrl, Scene scene) {
        ImageView backGround = new ImageView(imgUrl);
        backGround.fitHeightProperty().bind(scene.heightProperty());
        backGround.fitWidthProperty().bind(scene.widthProperty());
        backGround.setVisible(true);
        return backGround;
    }

    private BorderPane dragUI() {
        Rectangle rec = createBorder(0, 0, 250, 250);
        Group re = new Group(rec);
        MenuItem tile = new MenuItem("DRAG HERE", "black", "Times New Roman");
        MenuItem tile1 = new MenuItem("+", "black", "Times New Roman", 100);
        BorderPane label = new BorderPane();
        VBox tt = new VBox(tile1, tile);
        tt.setAlignment(Pos.CENTER);
        label.setCenter(tt);
        Group gr = new Group(re, label);
        BorderPane result = new BorderPane();
        result.prefHeightProperty().bind(this.heightProperty());
        result.prefWidthProperty().bind(this.widthProperty());
        label.prefHeightProperty().setValue(rec.getHeight());
        label.prefWidthProperty().setValue(rec.getWidth());
        result.setCenter(gr);
        return result;
    }

    static Rectangle createBorder(double x, double y, double width, double height) {
        Rectangle border = new Rectangle(x, y, width, height);
        border.setFill(Color.TRANSPARENT);
        border.setStrokeType(StrokeType.CENTERED);
        border.setStrokeLineCap(StrokeLineCap.BUTT);
        border.getStrokeDashArray().addAll(10d);
        border.setStrokeWidth(10);
        border.setStroke(Color.web("black", .4));
        return border;
    }

    private void setupDragNDrop(Scene scene) {
        scene.setOnDragOver((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles() || (db.hasUrl() && isValidJavaFile(db.getUrl()))) {
                event.acceptTransferModes(TransferMode.LINK);
            } else {
                event.consume();
            }
        });
        scene.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                db.getFiles().stream().forEach(file -> {
                    addJavaFiles(file);
                });
            } else {

            }
            if (currentIndex > -1) {
                loadFiles();
                mainScene = MainScene.createMainScene(classInfo);
                primaryStage.setScene(mainScene);
            }
            event.setDropCompleted(true);
            event.consume();
        });
    }

    private ProgressIndicator createProgressIndicator(Scene scene) {
        ProgressIndicator progress = new ProgressIndicator(0);
        progress.setVisible(false);
        progress.layoutXProperty()
                .bind(scene.widthProperty()
                        .subtract(progress.widthProperty())
                        .divide(2));
        progress.layoutYProperty()
                .bind(scene.heightProperty()
                        .subtract(progress.heightProperty())
                        .divide(2));
        return progress;
    }

    private boolean isValidJavaFile(String url) {
        List<String> imgTypes = Arrays.asList(".java");
        return imgTypes.stream().anyMatch(t -> url.endsWith(t));
    }

    private void addJavaFiles(final File files) {
        for (final File fileEntry : files.listFiles()) {
            if (fileEntry.isDirectory()) {
                addJavaFiles(fileEntry);
            } else if (isValidJavaFile(fileEntry.getPath())) {
                currentIndex += 1;
                javaFiles.add(currentIndex, fileEntry.getPath());
            }
        }
    }

    private Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                javaFiles.forEach(file -> {
                    Parser parser = new Parser(file);
                    ClassInformation[] cl = parser.getClassInfo();
                    int num = parser.getNumOfClass();
                    for (int i = 0; i < num; i++) {
                        classInfo.add(cl[i]);
                    }
                });
                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    loading.set(false);
                });
                return true;
            }
        };
    }

    private void loadFiles() {
        if (!loading.getAndSet(true)) {
            Task loadFile = createWorker();
            progressIndicator.setVisible(true);
            progressIndicator.progressProperty().unbind();
            progressIndicator.progressProperty()
                    .bind(loadFile.progressProperty());
            new Thread(loadFile).start();
        }
    }

}
