package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

/**
 * This class provides start and close
 * javafx application.
 *
 * @version   1.0 26 Nov 2020
 * @author    Oleksandr Lynnyk
 */
public class Main extends Application {

    private static Controller controller;

    public static void main(String[] args){
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main_window.fxml"));
            Parent root = fxmlLoader.load();
            controller = (Controller) fxmlLoader.getController();
            Scene scene = new Scene(root,640,480);
            primaryStage.setScene(scene);
            primaryStage.setTitle("BroChat");
            primaryStage.show();

        } catch (IOException e){
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void stop(){
        try {
            controller.close();
            super.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}