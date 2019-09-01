package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import constants.ResponseCode;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mainApp.App;
import request.LoginRequest;
import request.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Controller_Login {
    public JFXTextField email;
    public JFXPasswordField password;
    public Label status;

    @FXML
    JFXButton login,signup;


    public void onsignupclicked(ActionEvent actionEvent) {
    }

    public void onloginclicked(ActionEvent actionEvent) {
        String emailid = this.email.getText();
        String password = this.password.getText();
        if(password==null){
            status.setText("Password : Null");
        }else{
            LoginRequest loginRequest = new LoginRequest(emailid,password);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        App.sockerTracker = new Socket(App.serverIP,App.portNo);
                        App.oosTracker = new ObjectOutputStream(App.sockerTracker.getOutputStream());
                        App.oisTracker = new ObjectInputStream(App.sockerTracker.getInputStream());
                        App.oosTracker.writeObject(loginRequest);
                        App.oosTracker.flush();
                        Response response;
                        response = (Response)App.oisTracker.readObject();
                        if(response.getResponseCode().equals(ResponseCode.SUCCESS)){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Stage primaryStage = (Stage) signup.getScene().getWindow();
                                    Parent root = null;
                                    try {

                                        root = FXMLLoader.load(getClass().getResource("/dashboard.fxml"));
                                    }catch(IOException e){
                                        e.printStackTrace();
                                    }
                                    primaryStage.setScene(new Scene(root, 1303, 961));

                                }
                            });
                        }else{
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    status.setText("Error");
                                }
                            });
                        }

                    }catch (IOException | ClassNotFoundException e){

                    }
                }
            }).start();
        }

    }
}
