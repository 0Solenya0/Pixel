package apps.auth.controller;

import apps.auth.State;
import db.dbSet.ImageDBSet;
import javafx.stage.FileChooser;
import model.User;
import model.field.AccessLevel;
import model.Notification;
import model.Relation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import util.Config;
import controller.Controller;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.validator.UserValidators;
import view.InfoDialog;
import view.ViewManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class SettingsController extends Controller implements Initializable {
    private static final Logger logger = LogManager.getLogger(SettingsController.class);
    private Config languageConfig = Config.getLanguageConfig();

    @FXML
    private Label lblName, lblSurname, lblPhone, lblEmail, lblUsername, lblBirthday, lblChangePassTitle,
            lblNewPass, lblOldPass, lblNewPassRepeat, lblProfileInfoTitle, lblBio,
            lblLastSeen, lblPhoneErr, lblSaveErr, lblChangePassErr, lblAccountPrivacy;

    @FXML
    private TextField txtName, txtSurname, txtPhone;

    @FXML
    private PasswordField txtOldPass, txtNewPass, txtNewPassRepeat;

    @FXML
    private DatePicker dateBirthday;

    @FXML
    private JFXComboBox<String> comboPhone, comboBirthday, comboEmail, comboLastSeen, comboAccount;

    @FXML
    private JFXButton btnUploadPhoto, btnChangePass, btnSaveProfileInfo, btnLogout, btnDeleteAcc, btnDisableAcc;

    @FXML
    private ImageView imgAvatar;

    @FXML
    private TextArea txtBio;

    @FXML
    void changePass(ActionEvent event) throws ConnectionException {
        lblChangePassErr.setVisible(false);
        Objects.requireNonNull(State.getUser());
        User user = State.getUser();
        if (!user.checkPassword(txtOldPass.getText())) {
            lblChangePassErr.setText("Old password is wrong");
            lblChangePassErr.setVisible(true);
            return;
        }
        if (!txtNewPass.getText().equals(txtNewPassRepeat.getText())) {
            lblChangePassErr.setText("Passwords doesn't match");
            lblChangePassErr.setVisible(true);
            return;
        }
        user.setPassword(txtNewPass.getText());
        try {
            context.users.save(user);
        }
        catch (ValidationException e) {
            logger.error("change password failed unexpected validation error");
        }
        InfoDialog.showSuccess("Password successfully changed!");
    }

    @FXML
    void deleteAcc(ActionEvent event) throws ConnectionException {
        User user = State.getUser();
        logout();
        assert user != null;
        context.users.delete(user);
        ArrayList<Relation> relations = new ArrayList<>();
        relations.addAll(context.relations.getAll(context.relations.getQueryBuilder().getByUser1(user).getQuery()));
        relations.addAll(context.relations.getAll(context.relations.getQueryBuilder().getByUser2(user).getQuery()));
        for (Relation rel : relations)
            context.relations.delete(rel);

        ArrayList<Notification> notifications = new ArrayList<>();
        notifications.addAll(context.notifications.getAll(context.notifications.getQueryBuilder().getByUser1(user).getQuery()));
        notifications.addAll(context.notifications.getAll(context.notifications.getQueryBuilder().getByUser2(user).getQuery()));
        for (Notification notification : notifications)
            context.notifications.delete(notification);
    }

    @FXML
    void disableAcc(ActionEvent event) throws ConnectionException {
        User user = State.getUser();
        assert user != null;
        user.setEnabled(!user.isEnabled());
        try {
            context.users.save(user);
        }
        catch (ValidationException e) {
            logger.error("disable account failed unexpected validation error");
        }
        updateSettings();
    }

    @FXML
    void logout() {
        State.logout();
        ViewManager.clearHistory();
        ViewManager.setScene(ViewManager.loginView);
    }

    @FXML
    void saveProfileInfo(ActionEvent event) throws ConnectionException {
        Objects.requireNonNull(State.getUser());
        lblPhoneErr.setVisible(false);
        lblSaveErr.setVisible(false);
        if (!txtPhone.getText().isEmpty() && !UserValidators.isValidPhone(txtPhone.getText())) {
            lblPhoneErr.setText("Phone number is not valid");
            lblPhoneErr.setVisible(true);
            return;
        }
        User user = State.getUser();
        user.setName(txtName.getText());
        user.setSurname(txtSurname.getText());
        user.setBio(txtBio.getText());
        if (dateBirthday.getValue() != null)
            user.getBirthdate().set(dateBirthday.getValue());
        user.getPhone().set(txtPhone.getText());
        try {
            context.users.save(user);
        }
        catch (ValidationException e) {
            lblSaveErr.setText(e.getAllErrors().get(0));
            return;
        }
        InfoDialog.showSuccess("Profile info saved");
    }

    @FXML
    void updateBirthdayPrivacy(ActionEvent event) throws ConnectionException {
        User user = State.getUser();
        user.getBirthdate().setAccessLevel(AccessLevel.valueOf(comboBirthday.getValue()));
        try {
            context.users.save(user);
        }
        catch (ValidationException e) {
            logger.error("change birthday privacy failed unexpected validation error");
        }
    }

    @FXML
    void updateEmailPrivacy(ActionEvent event) throws ConnectionException {
        User user = State.getUser();
        user.getMail().setAccessLevel(AccessLevel.valueOf(comboEmail.getValue()));
        try {
            context.users.save(user);
        }
        catch (ValidationException e) {
            logger.error("change email privacy failed unexpected validation error");
        }
    }

    @FXML
    void updateLastSeenPrivacy(ActionEvent event) throws ConnectionException {
        User user = State.getUser();
        user.getLastseen().setAccessLevel(AccessLevel.valueOf(comboLastSeen.getValue()));
        try {
            context.users.save(user);
        }
        catch (ValidationException e) {
            logger.error("change lastseen privacy failed unexpected validation error");
        }
    }

    @FXML
    void updatePhonePrivacy(ActionEvent event) throws ConnectionException {
        User user = State.getUser();
        user.getPhone().setAccessLevel(AccessLevel.valueOf(comboPhone.getValue()));
        try {
            context.users.save(user);
        }
        catch (ValidationException e) {
            logger.error("change phone privacy failed unexpected validation error");
        }
    }

    @FXML
    void updateAccountPrivacy(ActionEvent event) throws ConnectionException {
        User user = State.getUser();
        user.setVisibility(AccessLevel.valueOf(comboAccount.getValue()));
        try {
            context.users.save(user);
        }
        catch (ValidationException e) {
            logger.error("change phone privacy failed unexpected validation error");
        }
    }

    @FXML
    void uploadPhoto(ActionEvent event) throws ConnectionException {
        Objects.requireNonNull(State.getUser());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select your image");
        File file = fileChooser.showOpenDialog(ViewManager.getWindow());
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            ImageDBSet imageDBSet = new ImageDBSet();
            String id = imageDBSet.save(bufferedImage);
            User user = State.getUser();
            user.setPhoto(id);
            context.users.save(user);
        } catch (IOException e) {
            logger.error("Failed to read photo");
        } catch (ValidationException e) {
            logger.error("Validation failed after adding an image");
            logger.error(e.getLog());
        }
        updateSettings();
    }

    public void addPrivacyToCombo(JFXComboBox<String> combo, AccessLevel cur) {
        for (AccessLevel a: AccessLevel.values())
            combo.getItems().add(a.toString());
        combo.setValue(cur.toString());
    }

    public void updateSettings() throws ConnectionException {
        Objects.requireNonNull(State.getUser());
        txtName.setText(State.getUser().getName());
        txtPhone.setText(State.getUser().getPhone().get());
        txtBio.setText(State.getUser().getBio());
        txtSurname.setText(State.getUser().getSurname());
        if (!State.getUser().getBirthdate().get().equals(LocalDate.MIN))
            dateBirthday.setValue(State.getUser().getBirthdate().get());
        if (Objects.requireNonNull(State.getUser()).isEnabled())
            btnDisableAcc.setText(languageConfig.getProperty("DISABLE_ACCOUNT_BTN_TEXT"));
        else
            btnDisableAcc.setText(languageConfig.getProperty("ENABLE_ACCOUNT_BTN_TEXT"));
        ImageDBSet imageDBSet = new ImageDBSet();
        imgAvatar.setImage(imageDBSet.load(State.getUser().getPhoto()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            lblUsername.setText(languageConfig.getProperty("USERNAME")
                    + ": " + Objects.requireNonNull(State.getUser()).getUsername());
            lblEmail.setText(languageConfig.getProperty("EMAIL")
                    + ": " + State.getUser().getMail().get());

            lblLastSeen.setText(languageConfig.getProperty("LAST_SEEN_PRIVACY"));
            lblAccountPrivacy.setText(languageConfig.getProperty("ACCOUNT_PRIVACY"));
            lblProfileInfoTitle.setText(languageConfig.getProperty("PROFILE_INFO"));
            lblChangePassTitle.setText(languageConfig.getProperty("CHANGE_PASSWORD"));
            lblName.setText(languageConfig.getProperty("NAME"));
            lblSurname.setText(languageConfig.getProperty("SURNAME"));
            lblPhone.setText(languageConfig.getProperty("PHONE"));
            lblBirthday.setText(languageConfig.getProperty("BIRTHDAY"));
            lblBio.setText(languageConfig.getProperty("BIO"));
            lblOldPass.setText(languageConfig.getProperty("OLD_PASS"));
            lblNewPass.setText(languageConfig.getProperty("NEW_PASS"));
            lblNewPassRepeat.setText(languageConfig.getProperty("NEW_PASS_REPEAT"));
            btnSaveProfileInfo.setText(languageConfig.getProperty("SAVE_BTN_TEXT"));
            btnUploadPhoto.setText(languageConfig.getProperty("UPLOAD_PHOTO_BTN_TEXT"));
            btnChangePass.setText(languageConfig.getProperty("CHANGE_PASS_BTN_TEXT"));
            btnLogout.setText(languageConfig.getProperty("LOGOUT_BTN_TEXT"));
            btnDeleteAcc.setText(languageConfig.getProperty("DELETE_ACCOUNT_BTN_TEXT"));

            btnDeleteAcc.setTextFill(Paint.valueOf(languageConfig.getProperty("ERROR_COLOR")));
            btnDisableAcc.setTextFill(Paint.valueOf(languageConfig.getProperty("ERROR_COLOR")));
            lblPhoneErr.setTextFill(Paint.valueOf(languageConfig.getProperty("ERROR_COLOR")));
            lblSaveErr.setTextFill(Paint.valueOf(languageConfig.getProperty("ERROR_COLOR")));
            lblChangePassErr.setTextFill(Paint.valueOf(languageConfig.getProperty("ERROR_COLOR")));

            lblSaveErr.setVisible(false);
            lblPhoneErr.setVisible(false);
            lblChangePassErr.setVisible(false);

            addPrivacyToCombo(comboBirthday, State.getUser().getBirthdate().getAccessLevel());
            addPrivacyToCombo(comboEmail, State.getUser().getMail().getAccessLevel());
            addPrivacyToCombo(comboPhone, State.getUser().getPhone().getAccessLevel());
            addPrivacyToCombo(comboLastSeen, State.getUser().getLastseen().getAccessLevel());
            comboAccount.getItems().add("PUBLIC");
            comboAccount.getItems().add("PRIVATE");
            comboAccount.setValue(State.getUser().getVisibility().toString());

            updateSettings();
        }
        catch (ConnectionException e) {
            ViewManager.connectionFailed();
        }
    }
}
