package com.example.lab3pc2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class HelloController {
    public Button loninButton;
    public TextField email;
    public PasswordField password;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        String uemail = email.getText();
        String upassword = password.getText();
        String jdbcUrl = "jdbc:mysql://localhost:3306/Cirkul";
        String dbUser = "root";
        String dbPassword = "";
        try (Connection connection = getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "SELECT * FROM login WHERE email='" + uemail + "' AND password ='" + upassword + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                try {
                    Parent secondScene = FXMLLoader.load(getClass().getResource("a-crud.fxml"));
                    Stage secondStage = new Stage();
                    secondStage.setTitle("ACRUD SCENE");
                    secondStage.setScene(new Scene(secondScene));
                    Stage firstSceneStage = (Stage) loninButton.getScene().getWindow();
                    firstSceneStage.close();
                    secondStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                welcomeText.setText("Invalid email and password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
