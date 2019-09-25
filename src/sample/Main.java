package sample;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.sql.*;

public class Main extends Application {

    private BorderPane root = new BorderPane();
    private Button closebtn = new Button();
    private VBox info = new VBox();
    private TextField usernametf = new TextField();
    private PasswordField passwordtf = new PasswordField();
    private HBox buttons = new HBox();
    private Button login = new Button();
    private Button signup = new Button();
    private double xOffset, yOffset;
    private final String USERNAME = "dbuser";
    private final String PASSWORD = "e4qAxBAOVPxKPHrhbFsR";
    private final String CONNECITON = "jdbc:mysql://localhost/logins";
    private database db;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("BePass");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("This is a prototype of a login and chat system. Do NOT put in any valuable information such as your normal password.");
        alert.showAndWait();

        closebtn.setText("X");
        closebtn.translateXProperty().bind(root.widthProperty().divide(2).subtract(10));
        closebtn.setTextFill(Color.WHITE);
        closebtn.translateXProperty().bind(root.widthProperty().subtract(25));
        closebtn.setScaleY(2);
        closebtn.setScaleX(2);
        closebtn.setStyle("-fx-background-color: rgb(50, 50, 50); -fx-text-fill: rgb(255, 255, 255);");
        closebtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.close();
            }
        });
        //toolbar.getChildren().add(closebtn);
        root.setTop(closebtn);

        //username/password field
        usernametf.setPromptText("Username");
        usernametf.setStyle("-fx-background-color: rgb(50, 50, 50); -fx-text-fill: rgb(255, 255, 255); -fx-border-color: rgb(204, 153, 255);");
        usernametf.setFont(Font.font("Agency FB", FontWeight.NORMAL, 15));
        passwordtf.setPromptText("Password");
        passwordtf.setStyle("-fx-background-color: rgb(50, 50, 50); -fx-text-fill: rgb(255, 255, 255); -fx-border-color: rgb(204, 153, 255);");
        passwordtf.setFont(Font.font("Agency FB", FontWeight.NORMAL, 15));

        login.setText("Login");
        login.setStyle("-fx-background-color: rgb(50, 50, 50); -fx-text-fill: rgb(255, 255, 255); -fx-border-color: rgb(204, 153, 255);");
        login.setFont(Font.font("Agency FB", FontWeight.NORMAL, 15));
        login.setMinWidth(100);

        signup.setText("Sign up");
        signup.setStyle("-fx-background-color: rgb(50, 50, 50); -fx-text-fill: rgb(255, 255, 255); -fx-border-color: rgb(204, 153, 255);");
        signup.setFont(Font.font("Agency FB", FontWeight.NORMAL, 15));
        signup.setMinWidth(100);
        buttons.getChildren().addAll(login, signup);
        buttons.setSpacing(45);
        buttons.setAlignment(Pos.BASELINE_CENTER);

        info.getChildren().addAll(usernametf, passwordtf, buttons);
        info.setPadding(new Insets(50, 50, 50, 50));
        info.setSpacing(50);
        root.setCenter(info);

        root.setStyle("-fx-background-color: rgb(50, 50, 50);");
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });
        primaryStage.setScene(new Scene(root, 500, 275));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), info);
        fade.setFromValue(0);
        fade.setToValue(1.0);
        fade.setCycleCount(1);
        fade.play();

        //Database

        try{
            Connection con = DriverManager.getConnection(CONNECITON, USERNAME, PASSWORD);
            Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet res = statement.executeQuery("SELECT * FROM registration");
            db = new database(con, statement, res);
            System.out.println("Connected");

        }
        catch(SQLException i){
            i.printStackTrace();
            alert.setContentText("A connection could not be made to the servers.");
            alert.showAndWait();
        }

        signup.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!usernametf.getText().equals("") && !passwordtf.getText().equals("")){ // check that both fields arent empty
                    try {
                        if(!db.checkUserExists(usernametf.getText())) { // check if user exists in database already
                            ResultSet res = db.getStatement().executeQuery("SELECT * FROM registration");
                            res.last(); // gets the amount of users in the database
                            db.getStatement().executeUpdate("INSERT INTO `registration` (`id`, `first`, `last`, `age`) VALUES ('" + (res.getRow()+1) + "', '" + usernametf.getText() + "', '" + passwordtf.getText() + "', '0')");
                            res.close();
                        }
                        else{
                            alert.setContentText("User already exists");
                            alert.showAndWait();
                        }
                    }
                    catch(SQLException i){
                        i.printStackTrace();
                    }
                }
            }
        });

        login.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //primaryStage.setScene(chatroom.getNewScene());
                if(!usernametf.getText().equals("") && !passwordtf.getText().equals("")){
                    try{
                        if(db.checkUserExists(usernametf.getText())){
                            Statement statement = db.getStatement();
                            ResultSet res = statement.executeQuery("SELECT last FROM registration WHERE first='" + usernametf.getText() + "'");
                            res.first();
                            if(res.getString("last").equals(passwordtf.getText())){
                                primaryStage.setScene(chatroom.getNewScene());
                            }
                            else{
                                alert.setContentText("Username/password do not match");
                                alert.showAndWait();
                            }
                            res.close();
                        }
                        else{
                            alert.setContentText("Username/password do not match");
                            alert.showAndWait();
                        }
                    }
                    catch(SQLException i){
                        i.printStackTrace();
                    }
                }
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
