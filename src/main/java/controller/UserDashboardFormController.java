package controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import db.InMemoryDB;
import db.User;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import util.Navigation;
import util.Routes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UserDashboardFormController {
    public AnchorPane pneDashboard;
    public ImageView imgQR;
    public Label lblQuota;
    public Label lblName;
    public Label lblNIC;
    public Label lblAddress;
    public Button btnDownload;
    public Button btnPrint;
    public Button btnLogOut;
    public HBox card;

    public void initialize() {
        Platform.runLater(pneDashboard::requestFocus);
    }

    public void setData(String nic) throws WriterException {
        User user = InMemoryDB.findUser(nic);
        lblName.setText(user.getFirstName() + " " + user.getLastName());
        lblAddress.setText(user.getAddress());
        lblNIC.setText(user.getNic());
        lblQuota.setText(user.getQuota() + " L (Weekly)");

        String plainSecret = user.getNic() + "-" + user.getFirstName();

        BitMatrix qrCode = new QRCodeWriter().encode(plainSecret, BarcodeFormat.QR_CODE, 150, 150);
        WritableImage image = new WritableImage(150, 150);
        PixelWriter pixelWriter = image.getPixelWriter();

        for (int y = 0; y < qrCode.getHeight(); y++) {
            for (int x = 0; x < qrCode.getWidth(); x++) {
                pixelWriter.setColor(x, y, qrCode.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }

        imgQR.setImage(image);
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.WELCOME);
    }

    public void btnPrintOnAction(ActionEvent actionEvent) {
        if (Printer.getDefaultPrinter() == null){
            new Alert(Alert.AlertType.ERROR, "No default printer has been selected").showAndWait();
            return;
        }
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null){
            printerJob.showPageSetupDialog(btnLogOut.getScene().getWindow());
            boolean success = printerJob.printPage(card);
            if (success){
                printerJob.endJob();
            }else{
                new Alert(Alert.AlertType.ERROR, "Failed to print, try again").show();
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Failed to initialize a new printer job").show();
        }
    }

    public void btnDownloadOnAction(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save the QR Code");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName("qr-code.png");
        fileChooser.getExtensionFilters().
                add(new FileChooser.ExtensionFilter("PNG Files (*.png)", "*.png"));
        File saveLocation = fileChooser.showSaveDialog(btnLogOut.getScene().getWindow());
        BufferedImage bufferedQRImage = SwingFXUtils.fromFXImage(imgQR.getImage(), null);
        boolean saved = ImageIO.write(bufferedQRImage, "png", saveLocation);
        if (saved){
            new Alert(Alert.AlertType.INFORMATION, "Downloaded").show();
        }else{
            new Alert(Alert.AlertType.ERROR, "Failed to download").show();
        }
    }
}
