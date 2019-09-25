package sample;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Stack;

public class chatroom {

    private static BorderPane root = new BorderPane();
    private static StackPane toolbar = new StackPane();
    private static Rectangle toolbarrec = new Rectangle();

    public static BorderPane getLogInRoot(){


        toolbarrec.setHeight(20);
        toolbarrec.widthProperty().bind(root.widthProperty());
        toolbarrec.setFill(Color.rgb(204, 153, 255));
        toolbar.getChildren().add(toolbarrec);
        root.setTop(toolbar);
        root.setStyle("-fx-background-color: rgb(50, 50, 50);");
        return root;
    }

    public static Scene getNewScene(){
        root = getLogInRoot();
        return new Scene(root, 800, 600);
    }
}
