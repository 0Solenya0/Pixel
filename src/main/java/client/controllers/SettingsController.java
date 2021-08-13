package client.controllers;

import client.request.SocketHandler;
import client.request.exception.ConnectionException;
import client.store.MessageStore;
import client.store.MyProfileStore;
import client.utils.ImageUtils;
import client.views.AutoUpdate;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    private final Config config = Config.getLanguageConfig();

    @FXML
    private ImageView imgAvatar;

    @FXML
    private JFXButton btnUploadPhoto, btnChangePass, btnDisableAcc;

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
        switch (response.getStatus()) {
            case OK:
                InfoDialog.showSuccess(config.getProperty("PASSWORD_SUCCESS_DIALOG"));
                break;
            case BAD_REQUEST:
                lblChangePassErr.setText(response.getObject("error", ValidationException.class).getAllErrors().get(0));
                break;
            case FORBIDDEN:
                lblChangePassErr.setText(response.get("error"));
                break;
            case BAD_GATEWAY:
                InfoDialog.showConnectionErrorSaveLater(new ConnectionException(ConnectionException.ErrorType.CONNECTION_ERROR));
                break;
        }
    }

    @FXML
    void deleteAcc() {
        Packet packet = new Packet("delete-account");
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        if (res.getStatus() == StatusCode.OK)
            logout();
    }

    @FXML
    void disableAcc(ActionEvent event) {
        User user = MyProfileStore.getInstance().getUser();
        if (user.getVisibility() != AccessLevel.PRIVATE) {
            user.setVisibility(AccessLevel.PRIVATE);
            try {
                MyProfileStore.getInstance().commitChanges();
                InfoDialog.showSuccess(config.getProperty("PROFILE_CHANGE_SUCCESS_DIALOG"));
            } catch (ConnectionException e) {
                InfoDialog.showConnectionErrorSaveLater(e);
            } catch (ValidationException ignored) {
            }
            btnDisableAcc.setText("Enable account");
        }
        else {
            user.setVisibility(AccessLevel.CONTACTS);
            try {
                MyProfileStore.getInstance().commitChanges();
                InfoDialog.showSuccess(config.getProperty("PROFILE_CHANGE_SUCCESS_DIALOG"));
            } catch (ConnectionException e) {
                InfoDialog.showConnectionErrorSaveLater(e);
            } catch (ValidationException ignored) {
            }
            btnDisableAcc.setText("Disable account");
        }
    }

    @FXML
    void logout() {
        MyProfileStore.reset();
        MessageStore.reset();
        AutoUpdate.getRunning().forEach(AutoUpdate::stop);
        ViewManager.showView("LOGIN");
    }

    @FXML
    void saveProfileInfo(ActionEvent event) {
        User user = MyProfileStore.getInstance().getUser();
        user.setName(txtName.getText());
        user.setSurname(txtSurname.getText());
        user.getPhone().set(txtPhone.getText());
        user.getPhone().setAccessLevel(comboPhone.getValue());
        user.getBirthdate().set(dateBirthday.getValue());
        user.getBirthdate().setAccessLevel(comboBirthday.getValue());
        user.setBio(txtBio.getText());
        try {
            MyProfileStore.getInstance().commitChanges();
            InfoDialog.showSuccess(config.getProperty("PROFILE_CHANGE_SUCCESS_DIALOG"));
        } catch (ConnectionException e) {
            InfoDialog.showConnectionErrorSaveLater(e);
        } catch (ValidationException e) {
            lblSaveErr.setText(e.getAllErrors().get(0));
        }
    }

    @FXML
    void savePrivacyInfo() {
        User user = MyProfileStore.getInstance().getUser();
        user.getMail().setAccessLevel(comboEmail.getValue());
        user.getLastSeen().setAccessLevel(comboLastSeen.getValue());
        user.setVisibility(comboAccount.getValue());
        try {
            MyProfileStore.getInstance().commitChanges();
            InfoDialog.showSuccess(config.getProperty("PROFILE_CHANGE_SUCCESS_DIALOG"));
        } catch (ConnectionException e) {
            InfoDialog.showConnectionErrorSaveLater(e);
        } catch (ValidationException ignored) { }
    }

    @FXML
    void uploadPhoto(ActionEvent event) {
        File photo = ViewManager.showFileDialog();
        try {
            MyProfileStore.getInstance().getUser().setPhoto(Files.readAllBytes(photo.toPath()));
            MyProfileStore.getInstance().commitChanges();
            updatePhoto();
        } catch (IOException e) {
            InfoDialog.showFailed(config.getProperty("IMAGE_LOAD_FAILED"));
        } catch (ValidationException ignored) {
        } catch (ConnectionException e) {
            InfoDialog.showConnectionErrorSaveLater(e);
        }
    }

    public void updatePhoto() {
        byte[] photo = MyProfileStore.getInstance().getUser().getPhoto();
        if (photo != null)
            imgAvatar.setImage(ImageUtils.load(photo));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboAccount.getItems().addAll(AccessLevel.values());
        comboBirthday.getItems().addAll(AccessLevel.values());
        comboPhone.getItems().addAll(AccessLevel.values());
        comboEmail.getItems().addAll(AccessLevel.values());
        comboLastSeen.getItems().addAll(AccessLevel.values());

        MyProfileStore.getInstance().updateUserProfile();
        User user = MyProfileStore.getInstance().getUser();
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
        if (user.getBirthdate().get() != null)
            dateBirthday.setValue(user.getBirthdate().get());

        btnDisableAcc.setText(user.getVisibility() == AccessLevel.PRIVATE ? "Enable account" : "Disable account");
        updatePhoto();
    }
}
