package com.example.lab3pc2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public TableView<User> userTable;
    public TableColumn<User, Integer> id;
    public TableColumn<User, String> name;
    public TableColumn<User, String> email;
    public TableColumn<User, String> password;
    public TextField uid;
    public TextField uname;
    public TextField uemail;
    public TextField upassword;
    public Button viewbutton;
    @FXML
    private Label welcomeText;

    ObservableList<User> list = FXCollections.observableArrayList();

    @FXML
    protected void onHelloButtonClick() {
        fethdata();
    }

    private void fethdata() {
        list.clear();
        String jdbcUrl = "jdbc:mysql://localhost:3306/Cirkul";
        String dbUser = "root";
        String dbPassword = "";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "SELECT * FROM admin";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                userTable.getItems().add(new User(id, name, email, password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        password.setCellValueFactory(new PropertyValueFactory<>("password"));
        userTable.setItems(list);
    }

    public void InsertData(ActionEvent actionEvent) {
        String name = uname.getText();
        String email = uemail.getText();
        String password = upassword.getText();
        String jdbcUrl = "jdbc:mysql://localhost:3306/Cirkul";
        String dbUser = "root";
        String dbPassword = "";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "INSERT INTO admin(name, email, password) VALUES ('" + name + "','" + email + "','" + password + "')";
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void UpdateData(ActionEvent actionEvent) {
        String id = uid.getText();
        String name = uname.getText();
        String email = uemail.getText();
        String password = upassword.getText();
        String jdbcUrl = "jdbc:mysql://localhost:3306/Cirkul";
        String dbUser = "root";
        String dbPassword = "";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "UPDATE admin SET name='" + name + "',email='" + email + "',password='" + password + "' WHERE id='" + id + "' ";
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void DeleteData(ActionEvent actionEvent) {
        String id = uid.getText();
        String jdbcUrl = "jdbc:mysql://localhost:3306/Cirkul";
        String dbUser = "root";
        String dbPassword = "";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "DELETE FROM admin WHERE id='" + id + "'";
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Back(ActionEvent actionEvent) {
        try {
            Parent secondScene = FXMLLoader.load(getClass().getResource("a-crud.fxml"));
            Stage secondStage = new Stage();
            secondStage.setTitle("dash Scene");
            secondStage.setScene(new Scene(secondScene));
            Stage firstSceneStage = (Stage) viewbutton.getScene().getWindow();
            firstSceneStage.close();
            secondStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

