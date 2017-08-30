package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InsertPackageController {
	
	@FXML
	private TextField packageName;
	
	@FXML
	private Button insertButton;
	
	private MainApp mainApp;
	
	@FXML
	private void initialize()
	{
		
	}
	
	@FXML
	private void handleButtonSave()
	{
		this.mainApp.setPackageName(new String(packageName.getText()));
		Stage stage = (Stage) insertButton.getScene().getWindow();
		stage.close();
	}
	
	public void setMainApp(MainApp mainApp)
	{
		this.mainApp = mainApp;
	}

}
