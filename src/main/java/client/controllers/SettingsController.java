package client.controllers;

import client.store.Index;
import client.views.ViewManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import server.db.models.fields.AccessLevel;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

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
    private JFXComboBox<AccessLevel> comboPhone, comboBirthday, comboEmail, comboLastSeen, comboAccount;

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
        Index.reset(); // TO DO remove other saved stores
        ViewManager.showView("LOGIN");
    }

    @FXML
    void saveProfileInfo(ActionEvent event) {

    }

    @FXML
    void savePrivacyInfo() {

    }

    @FXML
    void uploadPhoto(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Index.getInstance().updateProfileData();
        comboEmail.setValue(Index.getInstance().getMailPrivacy());
        comboPhone.setValue(Index.getInstance().getPhonePrivacy());
        comboLastSeen.setValue(Index.getInstance().getLastSeenPrivacy());
        comboBirthday.setValue(Index.getInstance().getBirthdayPrivacy());
        comboAccount.setValue(Index.getInstance().getVisibilityPrivacy());
        lblUsername.setText(Index.getInstance().getUsername());
        txtName.setText(Index.getInstance().getName());
        txtSurname.setText(Index.getInstance().getSurname());
        txtBio.setText(Index.getInstance().getBio());
        txtPhone.setText(Index.getInstance().getPhoneNumber());
    }
}
