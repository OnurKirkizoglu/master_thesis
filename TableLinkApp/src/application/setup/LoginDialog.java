package application.setup;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.User;
import at.jku.sea.cloud.exceptions.CredentialsException;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

/**
 * <p>A login dialog, which prompts the user to insert valid login information. <br>
 * In case no valid information is given, the dialog remains opened until explicitly cancelled or correct credentials are given.
 * </p>
 */
public class LoginDialog extends Dialog<User> {

    private static final String TITLE = "Login Dialog";
    private static final String HEADER_TEXT = "Please enter a valid username and password to login";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";

    private final Cloud cloud;
    private User user;

    public LoginDialog(Cloud cloud) {
        this.cloud = cloud;
        createContent();
    }

    private void createContent() {
        this.setTitle(TITLE);
        this.setHeaderText(HEADER_TEXT);

        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(20, 150,10,10));

        // username
        Label userLabel = new Label(USERNAME + ":");
        TextField username = new TextField();
        username.setPromptText(USERNAME);
        // pw
        Label pwLabel = new Label(PASSWORD + ":");
        PasswordField password = new PasswordField();
        password.setPromptText(PASSWORD);

        pane.add(userLabel, 0,0);
        pane.add(username, 1,0);
        pane.add(pwLabel, 0, 1);
        pane.add(password, 1,1);

        // disable login button on startup
        Node button = this.getDialogPane().lookupButton(loginButtonType);
        button.setDisable(true);
        // do not close the dialog in case incorrect input is given!
        button.addEventFilter(ActionEvent.ACTION, (event) -> {
            try {
                user = cloud.getUserByCredentials(username.getText(), password.getText());
            } catch (CredentialsException e) {
                event.consume();
            }
        });

        // only allow login try if user field is not empty!
        username.textProperty().addListener((observable, oldValue, newValue) -> button.setDisable(newValue.trim().isEmpty()));

        this.getDialogPane().setContent(pane);
        this.setResultConverter(buttonType -> {
            if(buttonType == loginButtonType && user != null) {
                    return user;
            }
            return null;
        });
    }

}
