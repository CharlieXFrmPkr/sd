package com.example.lab3pc2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    private TextField Email;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginButton;

    @FXML
    private Label welcomeText;

    @FXML
    private TableView<Employee> userTable;

    @FXML
    private TableColumn<Employee, Integer> idColumn;

    @FXML
    private TableColumn<Employee, String> nameColumn;

    @FXML
    private TableColumn<Employee, String> emailColumn;

    @FXML
    private TableColumn<Employee, String> salaryColumn;

    @FXML
    private TextField uname;

    @FXML
    private TextField uemail;

    @FXML
    private TextField usalary;

    private ObservableList<Employee> employees = FXCollections.observableArrayList();

    private final String jdbcUrl = "jdbc:mysql://localhost:3306/Cirkul";
    private final String dbUser = "root";
    private final String dbPassword = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        fetchEmployees();
    }

    private void initializeTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        userTable.setItems(employees);
    }

    private void fetchEmployees() {
        employees.clear();
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "SELECT * FROM Employee";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String salary = resultSet.getString("salary");
                employees.add(new Employee(id, name, email, salary));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String enteredEmail = Email.getText();
        String enteredPassword = password.getText();
        // Replace with your authentication logic
        if (isValidUser(enteredEmail, enteredPassword)) {
            welcomeText.setText("Welcome, " + enteredEmail + "!");
        } else {
            welcomeText.setText("Invalid email or password.");
        }
    }

    private boolean isValidUser(String email, String password) {
        // Example of hardcoded validation, replace with your own logic
        return email.equals("admin@example.com") && password.equals("admin");
    }

    @FXML
    private void insertEmployee(ActionEvent event) {
        String name = uname.getText();
        String email = uemail.getText();
        String salary = usalary.getText();
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "INSERT INTO Employee (name, email, salary) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, salary);
            preparedStatement.executeUpdate();
            fetchEmployees();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateEmployee(ActionEvent event) {
        Employee selectedEmployee = userTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            int id = selectedEmployee.getId();
            String name = uname.getText();
            String email = uemail.getText();
            String salary = usalary.getText();
            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                String query = "UPDATE Employee SET name = ?, email = ?, salary = ? WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, salary);
                preparedStatement.setInt(4, id);
                preparedStatement.executeUpdate();
                fetchEmployees();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Employee Selected", "Please select an employee to update.");
        }
    }

    @FXML
    private void deleteEmployee(ActionEvent event) {
        Employee selectedEmployee = userTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            int id = selectedEmployee.getId();
            try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                String query = "DELETE FROM Employee WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
                fetchEmployees();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Employee Selected", "Please select an employee to delete.");
        }
    }

    @FXML
    private void viewEmployee(ActionEvent event) {
        Employee selectedEmployee = userTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            uname.setText(selectedEmployee.getName());
            uemail.setText(selectedEmployee.getEmail());
            usalary.setText(selectedEmployee.getSalary());
        } else {
            showAlert(Alert.AlertType.WARNING, "No Employee Selected", "Please select an employee to view.");
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        // Implement navigation to previous screen or action
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
