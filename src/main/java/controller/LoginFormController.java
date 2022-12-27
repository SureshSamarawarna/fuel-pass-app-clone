package controller;

import com.google.zxing.WriterException;
import db.InMemoryDB;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import util.Navigation;
import util.Routes;

import java.io.IOException;
import java.io.InputStream;

public class LoginFormController {
    public TextField txtNIC;
    public Button btnLogin;

    public void initialize() {
        Platform.runLater(txtNIC::requestFocus);
    }

    public void lblRegisterOnMouseClicked(MouseEvent mouseEvent) throws IOException {
        Navigation.navigate(Routes.REGISTRATION);
    }

    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException, WriterException {
        if (!RegisterFormController.isValidNIC(txtNIC.getText()) ||
                InMemoryDB.findUser(txtNIC.getText()) == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid NIC to continue");

            InputStream resourceAsStream = this.getClass().getResourceAsStream("/image/lock.png");
            Image image = new Image(resourceAsStream);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(48);
            imageView.setFitHeight(48);
            alert.setGraphic(imageView);

            alert.setHeaderText("Invalid NIC");
            alert.setTitle("Access Denied");

            alert.showAndWait();
            txtNIC.requestFocus();
            txtNIC.selectAll();
        } else {
            UserDashboardFormController ctrl =
                    (UserDashboardFormController) Navigation.navigate(Routes.DASHBOARD);
            ctrl.setData(txtNIC.getText());
        }
    }
}
