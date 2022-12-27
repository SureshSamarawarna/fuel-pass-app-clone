package controller;

import db.InMemoryDB;
import db.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Optional;

public class ControlCenterFormController {

    public AnchorPane pneControlCenter;
    public TableView<User> tblUsers;
    public TextField txtSearch;
    public Button btnUpdateQuota;
    public Button btnRemoveUsers;
    public Spinner<Integer> txtQuota;
    public TableColumn colAddress;

    public void initialize(){
        Platform.runLater(pneControlCenter::requestFocus);
        txtQuota.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,20,16));

        pneControlCenter.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number prev, Number current) {
                if (prev.doubleValue() == 0.0) return;
                double diff = current.doubleValue() - prev.doubleValue();
                double prefWidth = colAddress.getWidth() + diff;
                if (prefWidth < 199) prefWidth = 199;
                colAddress.setPrefWidth(prefWidth);
            }
        });

        tblUsers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("nic"));
        tblUsers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("quota"));
        tblUsers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tblUsers.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tblUsers.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("address"));

//        ObservableList<User> olUsers = FXCollections.observableArrayList(InMemoryDB.getUserDatabase());
        tblUsers.setItems(FXCollections.observableArrayList(InMemoryDB.getUserDatabase()));
        tblUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        ObservableList<User> olUsers = tblUsers.getItems();
//        for (User user : InMemoryDB.getUserDatabase()) {
//            olUsers.add(user);
//        }
//        String output = "Te" + "" + "st" + ""; // Test

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String prev, String current) {
                if (prev.equals(current)) return;
//                ArrayList<User> foundUsers = InMemoryDB.findUsers(current);
//                ObservableList<User> olFoundUsers = FXCollections.observableArrayList(foundUsers);
                tblUsers.setItems(FXCollections.observableArrayList(InMemoryDB.findUsers(current)));
            }
        });

        btnRemoveUsers.setDisable(true);
        txtQuota.setDisable(true);
        btnUpdateQuota.setDisable(true);
        tblUsers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observableValue, User prev, User current) {
                btnRemoveUsers.setDisable(tblUsers.getSelectionModel().getSelectedItems().isEmpty());
                txtQuota.setDisable(btnRemoveUsers.isDisable());
                btnUpdateQuota.setDisable(btnRemoveUsers.isDisable());
            }
        });
    }

    public void btnUpdateQuotaOnAction(ActionEvent actionEvent) {
        ObservableList<User> selectedUsers = tblUsers.getSelectionModel().getSelectedItems();
        for (User selectedUser : selectedUsers) {
            selectedUser.setQuota(txtQuota.getValue());
        }
        tblUsers.refresh();
    }

    public void btnRemoveUsersOnAction(ActionEvent actionEvent) {
        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete these users?",
                ButtonType.YES, ButtonType.NO).showAndWait();
        if (buttonType.get() == ButtonType.NO) return;

        ObservableList<User> selectedUsers = tblUsers.getSelectionModel().getSelectedItems();
        for (User selectedUser : selectedUsers) {
            InMemoryDB.removeUser(selectedUser.getNic());
        }
        tblUsers.getItems().removeAll(selectedUsers);
        tblUsers.getSelectionModel().clearSelection();
    }
}
