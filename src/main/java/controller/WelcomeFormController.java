package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import util.Navigation;
import util.Routes;

import java.io.IOException;

public class WelcomeFormController {
    public Button btnRegister;
    public Button btnLogin;
    public AnchorPane pneWelcome;

    public void btnRegisterOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.REGISTRATION);
    }

    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.LOGIN);
    }
}
