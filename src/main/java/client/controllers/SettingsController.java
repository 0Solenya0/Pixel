package client.controllers;

import client.request.SocketHandler;
import client.store.MyProfile;
import client.views.InfoDialog;
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
import shared.exception.ValidationException;
import shared.models.User;
import shared.models.fields.AccessLevel;
import shared.request.Packet;
import shared.request.StatusCode;
import shared.util.Config;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    private final Config config = Config.getLanguageConfig();

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
    private Label lblSaveErr, lblChangePassErr;

    @FXML
    void changePass(ActionEvent event) {
        if (!txtNewPass.getText().equals(txtNewPassRepeat.getText())) {
            lblChangePassErr.setText(config.getProperty("PASSWORD_REPEAT_ERROR"));
            return;
        }
        Packet packet = new Packet("reset-pass");
        packet.put("old-pass", txtOldPass.getText());
        packet.put("new-pass", txtNewPass.getText());
        Packet response = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        if (response.status == StatusCode.FORBIDDEN)
            lblChangePassErr.setText(response.get("error"));
        else if (response.getStatus() == StatusCode.BAD_REQUEST)
            lblChangePassErr.setText(response.getObject("error", ValidationException.class).getAllErrors().get(0));
        else
            InfoDialog.showSuccess(config.getProperty("PASSWORD_SUCCESS_DIALOG"));
    }

    @FXML
    void deleteAcc(ActionEvent event) {
        // TO DO
    }

    @FXML
    void disableAcc(ActionEvent event) {
        // TO DO
    }

    @FXML
    void logout(ActionEvent event) {
        MyProfile.reset(); // TO DO remove other saved stores
        ViewManager.showView("LOGIN");
    }

    @FXML
    void saveProfileInfo(ActionEvent event) {
        Packet packet = new Packet("update-profile");
        packet.put("name", txtName.getText());
        packet.put("surname", txtSurname.getText());
        packet.put("phone-number", txtPhone.getText());
        packet.putObject("phone-access", comboPhone.getValue());
        packet.putObject("birthday", dateBirthday.getValue());
        packet.putObject("birthday-access", comboBirthday.getValue());
        packet.put("bio", txtBio.getText());
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        if (res.status == StatusCode.OK) {
            MyProfile.getInstance().updateUserProfile();
            InfoDialog.showSuccess(config.getProperty("PROFILE_CHANGE_SUCCESS_DIALOG"));
        }
        else {
            // TO DO some error happened
        }
    }

    @FXML
    void savePrivacyInfo() {
        Packet packet = new Packet("update-profile");
        packet.putObject("mail-access", comboEmail.getValue());
        packet.putObject("last-seen-access", comboLastSeen.getValue());
        packet.putObject("visibility", comboAccount.getValue());
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        if (res.status == StatusCode.OK)
            MyProfile.getInstance().updateUserProfile();
        else {
            // TO DO some error happened
        }
    }

    @FXML
    void uploadPhoto(ActionEvent event) {
        // TO DO
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboAccount.getItems().addAll(AccessLevel.values());
        comboBirthday.getItems().addAll(AccessLevel.values());
        comboPhone.getItems().addAll(AccessLevel.values());
        comboEmail.getItems().addAll(AccessLevel.values());
        comboLastSeen.getItems().addAll(AccessLevel.values());

        MyProfile.getInstance().updateUserProfile();
        User user = MyProfile.getInstance().getUser();
        comboEmail.setValue(user.getMail().getAccessLevel());
        comboPhone.setValue(user.getPhone().getAccessLevel());
        comboLastSeen.setValue(user.getLastSeen().getAccessLevel());
        comboBirthday.setValue(user.getBirthdate().getAccessLevel());
        comboAccount.setValue(user.getVisibility());
        lblUsername.setText(user.getUsername());
        txtName.setText(user.getName());
        txtSurname.setText(user.getSurname());
        txtBio.setText(user.getBio());
        txtPhone.setText(user.getPhone().get());
    }
}
