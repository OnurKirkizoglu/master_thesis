package application;

import java.io.IOException;

import at.jku.sea.cloud.rest.client.RestCloud;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainApp extends Application{

	private Stage primaryStage;
	private AnchorPane rootLayout;
	private String packName;
	private static MainViewController controller;
	private static RestCloud cloud;
	private static MainViewModel model;
	
	
	@Override
	public void start(Stage primaryStage)
	{
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Design Space Explorer");
		initRootLayout();
	}
	
	private void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("MainView.fxml"));
			rootLayout = (AnchorPane) loader.load();
			
			
			initController(loader.getController());
			
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void initDesignSpaceConnection()
	{
		cloud = RestCloud.getInstance();
	}
	
	public static void initModel()
	{
		model = new MainViewModel();
	}
	
	private void initController(MainViewController ctrl) 
	{
		controller = ctrl;
		controller.setMainApp(this);
		controller.setCloud(cloud);
		controller.initMainView();
	}
	
	public static void main(String[] args)
	{
		long startTime = System.nanoTime();
		initDesignSpaceConnection();
		initModel();
		long endTime = System.nanoTime();
		long duration = (endTime - startTime); 
		
		launch(args);
	}

	public String getPackageName() {
		return packName;
	}

	public void setPackageName(String packName) {
		this.packName = packName;
	}
}
