package client.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class SettingsController {

    @FXML
    private ImageView imgAvatar;

    @FXML
    private JFXButton btnUploadPhoto, btnChangePass;

    @FXML
    private TextField txtName, txtSurname, txtPhone;

    @FXML
    private Label lblUsername;

    @FXML
    private PasswordField txtOldPass, txtNewPass, txtNewPassRepeat;

    @FXML
    private DatePicker dateBirthday;

    @FXML
    private TextArea txtBio;

    @FXML
    private JFXButton btnSaveProfileInfo;

    @FXML
    private JFXComboBox<?> comboPhone, comboBirthday, comboEmail, comboLastSeen, comboAccount;

    @FXML
    private Label lblPhoneErr, lblSaveErr, lblChangePassErr;

    @FXML
    void changePass(ActionEvent event) {

    }

    @FXML
    void deleteAcc(ActionEvent event) {

    }

    @FXML
    void disableAcc(ActionEvent event) {

    }

    @FXML
    void logout(ActionEvent event) {

    }

    @FXML
    void saveProfileInfo(ActionEvent event) {

    }

    @FXML
    void updateAccountPrivacy(ActionEvent event) {

    }

    @FXML
    void updateBirthdayPrivacy(ActionEvent event) {

    }

    @FXML
    void updateEmailPrivacy(ActionEvent event) {

    }

    @FXML
    void updateLastSeenPrivacy(ActionEvent event) {

    }

    @FXML
    void updatePhonePrivacy(ActionEvent event) {

    }

    @FXML
    void uploadPhoto(ActionEvent event) {

    }

}
