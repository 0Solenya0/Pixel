package client.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MessageController {

    @FXML
    private AnchorPane sendMessagePane;

    @FXML
    private JFXTextArea txtContent;

    @FXML
    private JFXButton btnSend, btnAddUser, btnUserList;

    @FXML
    private ScrollPane messageScrollPane;

    @FXML
    private VBox vboxMessage, vboxChat;

    @FXML
    private Label lblChatName;

    @FXML
    void addUser(ActionEvent event) {

    }

    @FXML
    void attachImage(ActionEvent event) {

    }

    @FXML
    void createGroup(ActionEvent event) {

    }

    @FXML
    void sendMessage(ActionEvent event) {

    }

    @FXML
    void showUserList(ActionEvent event) {

    }

}
