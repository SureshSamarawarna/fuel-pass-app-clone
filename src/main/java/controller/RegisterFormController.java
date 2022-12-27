package controller;

import db.InMemoryDB;
import db.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import util.Navigation;
import util.Routes;

import java.io.IOException;
import java.io.InputStream;

public class RegisterFormController {
    public AnchorPane pneRegisterForm;
    public TextField txtNIC;
    public Label lblNicStatus;
    public TextField txtFirstName;
    public TextField txtLastName;
    public TextField txtAddress;
    public Button btnRegister;

    public static boolean isValidNIC(String input) {
        if (input.length() != 10) return false;
        if (!(input.endsWith("v") || input.endsWith("V"))) return false;
        return input.substring(0, 9).matches("\\d+");
    }

    private void setDisableCmp(boolean disable) {
        txtFirstName.setDisable(disable);
        txtLastName.setDisable(disable);
        txtAddress.setDisable(disable);
        btnRegister.setDisable(disable);
    }

    public void initialize() {
        Platform.runLater(txtNIC::requestFocus);
        txtNIC.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue,
                                String oldText, String currentText) {

                setDisableCmp(true);
                if (currentText.isBlank()) {
                    lblNicStatus.setText("Please enter valid NIC Number to proceed");
                    lblNicStatus.setTextFill(Color.BLACK);
                } else {
                    if (isValidNIC(currentText)) {
                        lblNicStatus.setText("Valid NIC ✅");
                        lblNicStatus.setTextFill(Color.GREEN);
                        setDisableCmp(false);
                    } else {
                        lblNicStatus.setText("Invalid NIC ❌");
                        lblNicStatus.setTextFill(Color.RED);
                    }
                }
            }
        });
    }

    public void lblLoginOnMouseClicked(MouseEvent mouseEvent) throws IOException {
        Navigation.navigate(Routes.LOGIN);
    }

    public void btnRegisterOnAction(ActionEvent actionEvent) throws IOException {
        String firstName = txtFirstName.getText();

        if (firstName.isBlank()) {
            new Alert(Alert.AlertType.ERROR, "First name can't be empty").showAndWait();
            txtFirstName.requestFocus();
            return;
        } else if (!isName(firstName)) {
            new Alert(Alert.AlertType.ERROR, "First name is invalid, please enter a valid first name").showAndWait();
            txtFirstName.requestFocus();
            return;
        } else if (!isName(txtLastName.getText())) {
            new Alert(Alert.AlertType.ERROR, "Last name is invalid, please enter a valid last name").showAndWait();
            txtLastName.requestFocus();
            return;
        } else if (txtAddress.getText().trim().length() < 3) {
            new Alert(Alert.AlertType.ERROR, "Address is invalid, please enter a valid address").showAndWait();
            txtAddress.requestFocus();
            return;
        }

        boolean result = InMemoryDB.registerUser(new User(txtNIC.getText(),
                firstName, txtLastName.getText(),
                txtAddress.getText(), 16));

        if (result) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Registration is successful, you will be redirected to the login screen");
            InputStream resourceAsStream = this.getClass().getResourceAsStream("/image/tick.png");
            Image image = new Image(resourceAsStream);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(48);
            imageView.setFitHeight(48);
            alert.setGraphic(imageView);

            alert.setHeaderText("Registered");
            alert.setTitle("Congratulations");

            alert.getDialogPane().setMinWidth(500);
            alert.getDialogPane().setMinHeight(170);

            alert.showAndWait();
            lblLoginOnMouseClicked(null);
        } else {
            new Alert(Alert.AlertType.ERROR, "NIC is already registered, please double check your NIC").showAndWait();
            txtNIC.requestFocus();
        }
    }

    public boolean isName(String input) {
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            if (!Character.isLetter(aChar) && aChar != ' ') return false;
        }
        return true;
    }

}
