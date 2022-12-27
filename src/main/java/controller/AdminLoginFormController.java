package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.print.Printer;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import util.Navigation;
import util.Routes;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class AdminLoginFormController {

    private static final String ADMIN_PASSWORD = "dep9@ADMIN";
    private static int attempts = 3;

    public PasswordField txtPassword;
    public AnchorPane pneAdminLoginForm;

    public void initialize() {
        Platform.runLater(txtPassword::requestFocus);
    }

    public void txtPasswordOnAction(ActionEvent actionEvent) throws IOException, URISyntaxException {
        if (!txtPassword.getText().equals(ADMIN_PASSWORD)) {

            if (attempts == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, You have reached maximum number of attempts");
                alert.getDialogPane().setMinWidth(400);
                alert.getDialogPane().setMinHeight(170);
                alert.showAndWait();
                Platform.exit();
                return;
            }

            URL resource = this.getClass().getResource("/audio/security-breach.mp3");
            Media media = new Media(resource.toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();

            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Admin Password. You have " + attempts + " more attempts to try again.");
            attempts--;

            InputStream resourceAsStream = this.getClass().getResourceAsStream("/image/lock.png");
            Image image = new Image(resourceAsStream);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(48);
            imageView.setFitHeight(48);
            alert.setGraphic(imageView);

            alert.setHeaderText("Invalid Login Credential");
            alert.setTitle("Access Denied");
            alert.getDialogPane().setMinWidth(450);
            alert.getDialogPane().setMinHeight(170);

            alert.showAndWait();
            mediaPlayer.dispose();
            txtPassword.requestFocus();
            txtPassword.selectAll();
            return;
        }

        Navigation.navigate(Routes.CONTROL_CENTER);
    }
}
