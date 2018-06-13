package PA2;




import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import PA2.City;
import PA2.Metropolis;
import PA2.Player;
import PA2.Town;


public class GameApplication extends Application {
	// Note: Please play the game in full-screen.
	// Resolution, Tiles and Offset for Rendering Unit ID on bottom-right.
	
	
	private static final int RESOLUTION_GAMEPLAY_WIDTH = 600;
	private static final int RESOLUTION_GAMEPLAY_HEIGHT = 600;
	
	
	
	// Scene and Stage
	private static final int SCENE_NUM = 2;
	private static final int SCENE_WELCOME = 0;
	private static final int SCENE_STARTGAME = 1;
	private static final String[] SCENE_TITLES = {"Welcome", "Wars of the cities"};
	private Scene[] scenes = new Scene[SCENE_NUM];
	private Stage stage;
	
	// Part 1: paneWelcome
	private Label lbMenuTitle;
	private Button btNewGame, btQuit;

	// Part 2: paneGameStart
	
	//Buttons at the botton 
	private Button btReStartGame;	
	private Button btExit;
	
	
	//Log window
	private ListView<String> listViewMessage;
	private ObservableList<String> listViewMessageItems = FXCollections.observableArrayList();
	private Label lbLog;
	
	private ListView<String> listViewRandomEvent;
	private ObservableList<String> listViewRandomEventItems = FXCollections.observableArrayList();
	private Label lbRandomEvent;
	
	//my generals
	private ListView<String> listViewGeneral;
	private ObservableList<String> listViewGeneralItems = FXCollections.observableArrayList();
	private String listViewSelectedGeneral = null;
	private Label lbMyGeneral;
	private Label lbMyGeneralTitle;
	
	//my cities
	private ListView<String> listViewCity;
	private ObservableList<String> listViewCityItems = FXCollections.observableArrayList();
	private String listViewSelectedCity= null;
	private Label lbMyCity;
	private Label lbMyCityTitle;
	
	//my neighbor
	private ListView<String> listViewNei;
	private ObservableList<String> listViewNeiItems = FXCollections.observableArrayList();
	private String listViewSelectedNei= null;
	private Label lbMyNei;
	private Label lbMyNeiTitle;
	
	//my gold
	private Label lbMyGold;
	//Command Button
	private Button btImproveCrop;
	private Button btCollectTax;
	private Button btRecruit;
	private Button btUpgrade;
	private Button btSendTroop;
	private Button exit;
	private TextField tfRecruit;
	private TextField tfSendTroop;
	
	//Map
	private Canvas canvasGameStart = new Canvas(RESOLUTION_GAMEPLAY_WIDTH, RESOLUTION_GAMEPLAY_HEIGHT);

	// Part 3: paneGameOver
	private Label lbGameOver;
	private Button btExitToMenu, btGameOverQuitGame;
	
	//Thead for player
	private Thread playerThread;
	
	private GameEngine game = new GameEngine();
	private Player humanPlayer;
	private ArrayList<Player> computerPlayers = new ArrayList<>();
	
	protected static ArrayList<String> printResult = new ArrayList<>();//To store the message that will be printed in the log window
	protected static ArrayList<String> RandomEventResult = new ArrayList<>(); //To store the message that will be printed in the random event window
	
	private boolean gameOver = false;
	
	
	private Pane paneWelcome() 
	{
		/**
		 *  To do: 
		 *  1. This function is to create the welcome scene
		 *  2. There are two buttons and a label in this scene
		 *  Hint:
		 *  1. To set the background of the pane, you can use welcomePane.getStyleClass().add("pane-welcome")
		 *  2. To set the style of the button/label, you can use 
		 *  	lbMenuTitle.getStyleClass().add("menu-title");
		 *  	btNewGame.getStyleClass().add("large-button"); 
		 *  	where lbMenuTitle is a Label and btNewGame is a button.
		 *  
		 */
		StackPane welcomePane = new StackPane();
		VBox container = new VBox(20);
		
		lbMenuTitle = new Label("Wars of the Cities");
		btNewGame = new Button("New Game");
		btQuit = new Button("Quit");
		container.getChildren().addAll(lbMenuTitle,btNewGame,btQuit);
		container.setPadding(new Insets(10, 10, 10, 10));
		container.setAlignment(Pos.CENTER);
		
		welcomePane.getChildren().add(container);
		
		
		
		welcomePane.getStyleClass().add("pane-welcome");
		lbMenuTitle.getStyleClass().add("menu-title");
		btNewGame.getStyleClass().add("large-button");
		btQuit.getStyleClass().add("large-button");
		
		return welcomePane;
	}

	private Pane paneStartGame() 
	{
		/**
		 * To do: 
		 * 1. This function is to create the game start scene.
		 * 2. There are two parts in this scene:
		 * 2.1 the canvas where you can draw the map 
		 * 2.2 the control part that consists of four listview, some buttons and two TextField
		 * 3. Please think that how to put the button/listview/label in the right place
		 * Hint: To set the background of the control pane, you can use pane.getStyleClass().add("pane-game-start");
		 */
		BorderPane pane = new BorderPane();
		
		VBox containerCanvas = new VBox(20);
		containerCanvas.getChildren().addAll(canvasGameStart);
		containerCanvas.setAlignment(Pos.CENTER);
		
		//ContainerLeft
		//neighbour
		lbMyNei = new Label("Neighbour Cities");
		lbMyNeiTitle = new Label("Name Population Army Crop Wall");
		listViewNei = new ListView<String>();
		listViewNei.setPrefSize(200,200);
		listViewNei.setItems(listViewNeiItems);
		
		VBox paneNei = new VBox();
		paneNei.getChildren().addAll(lbMyNeiTitle,listViewNei);
		
		VBox containerNei = new VBox(20);
		containerNei.getChildren().addAll(lbMyNei, paneNei);
		containerNei.setAlignment(Pos.BOTTOM_CENTER);
		containerNei.setPadding(new Insets(10, 10, 10, 10));
		containerNei.setAlignment(Pos.CENTER);
		
		//City
		listViewCity = new ListView<String>();
		listViewCity.setPrefSize(200, 200);
		listViewCity.setItems(listViewCityItems);
		lbMyCity = new Label("My cities");
		lbMyCityTitle = new Label("Name Population Army Crop Wall");
		
		VBox paneCity = new VBox();
		paneCity.getChildren().addAll(lbMyCityTitle,listViewCity);
		
		VBox containerCity = new VBox(20);
		containerCity.getChildren().addAll(lbMyCity, paneCity);
		containerCity.setAlignment(Pos.BOTTOM_CENTER);
		containerCity.setPadding(new Insets(10, 10, 10, 10));
		containerCity.setAlignment(Pos.CENTER);
		
		//General
		listViewGeneral = new ListView<String>();
		listViewGeneral.setPrefSize(200,200);
		listViewGeneral.setItems(listViewGeneralItems);
		lbMyGeneral = new Label("My Generals");
		lbMyGeneralTitle = new Label("Name Combat Leadship Wisdom Status");
		
		VBox paneGeneral = new VBox();
		paneGeneral.getChildren().addAll(lbMyGeneralTitle,listViewGeneral);
		
		VBox containerGeneral = new VBox(20);
		containerGeneral.getChildren().addAll(lbMyGeneral, paneGeneral);
		containerGeneral.setAlignment(Pos.BOTTOM_CENTER);
		containerGeneral.setPadding(new Insets(10, 10, 10, 10));
		containerGeneral.setAlignment(Pos.CENTER);
		
		VBox containerLeft = new VBox(20);
		containerLeft.getChildren().addAll(containerNei,containerCity, containerGeneral);
		containerLeft.setAlignment(Pos.BOTTOM_CENTER);
		containerLeft.setPadding(new Insets(10, 10, 10, 10));
		containerLeft.setAlignment(Pos.CENTER);
		
		//ContainerRight
		//my Gold
		lbMyGold = new Label("My Gold: 0");
		//Commands
		listViewMessage = new ListView<String>();
		listViewMessage.setPrefSize(200, 200);
		listViewMessage.setItems(listViewMessageItems);
		lbLog = new Label("Commands Window");
		
		VBox paneMyLog = new VBox();
		paneMyLog.getChildren().add(listViewMessage);
		
		VBox containerMessage = new VBox(20);
		containerMessage.getChildren().addAll(lbLog, paneMyLog);
		containerMessage.setAlignment(Pos.BOTTOM_CENTER);
		containerMessage.setPadding(new Insets(10, 10, 10, 10));
		containerMessage.setAlignment(Pos.CENTER);
		
		//random event
		listViewRandomEvent = new ListView<String>();
	   	listViewRandomEvent.setPrefSize(200, 200);
		listViewRandomEvent.setItems(listViewRandomEventItems);
		lbRandomEvent = new Label("Random Events");
		
		VBox paneMyRand = new VBox();
		paneMyRand.getChildren().add(listViewRandomEvent);
		
		VBox containerRand = new VBox(20);
		containerRand.getChildren().addAll(lbRandomEvent, paneMyRand);
		containerRand.setAlignment(Pos.BOTTOM_CENTER);
		containerRand.setPadding(new Insets(10, 10, 10, 10));
		containerRand.setAlignment(Pos.CENTER);
		
		//Recruit
		tfRecruit = new TextField();
		btRecruit = new Button("Recruit Army");
		HBox paneRecruit= new HBox(10);
		paneRecruit.getChildren().add(tfRecruit);
		paneRecruit.getChildren().add(btRecruit);
		paneRecruit.setAlignment(Pos.BASELINE_RIGHT);
		
		//send troops
		tfSendTroop = new TextField();
		btSendTroop = new Button("Send Troops");
		HBox paneTroops= new HBox(10);
		paneTroops.getChildren().add(tfSendTroop);
		paneTroops.getChildren().add(btSendTroop);
		paneTroops.setAlignment(Pos.BASELINE_RIGHT);
		
		//restart and exit
		btReStartGame = new Button("Restart Game");
		btExit = new Button("Exit");
		HBox paneReEx = new HBox(10);
		paneReEx.getChildren().add(btReStartGame);
		paneReEx.getChildren().add(btExit);
		paneReEx.setAlignment(Pos.BASELINE_RIGHT);
		
		//Button
		btCollectTax = new Button("Collect Tax");
		btUpgrade = new Button("Upgrade Towns");
		btImproveCrop = new Button("Improve Crop yield");
		VBox containerButton = new VBox(0);
		containerButton.getChildren().addAll(btCollectTax,btUpgrade,btImproveCrop,paneRecruit,paneTroops,paneReEx);
		containerButton.setAlignment(Pos.BOTTOM_RIGHT);
		containerButton.setPadding(new Insets(10, 10, 10, 10));
		
		VBox containerRight = new VBox(20);
		containerRight.getChildren().add(lbMyGold);
		containerRight.setAlignment(Pos.CENTER_RIGHT);
		containerRight.getChildren().addAll(containerRand,containerMessage,containerButton);
		containerLeft.setAlignment(Pos.BOTTOM_CENTER);
		containerLeft.setPadding(new Insets(10, 10, 10, 10));
		containerLeft.setAlignment(Pos.CENTER);
		
		//right of borderpane
		HBox controlpane = new HBox();
		controlpane.getChildren().addAll(containerLeft,containerRight);
		controlpane.setAlignment(Pos.CENTER);
		
		lbMyCity.getStyleClass().add("menu-title");		
		lbMyGeneral.getStyleClass().add("menu-title");	
		lbMyNei.getStyleClass().add("menu-title");	
		lbLog.getStyleClass().add("menu-title");
		lbMyGold.getStyleClass().add("menu-title");
		lbRandomEvent.getStyleClass().add("menu-title");
		lbMyCityTitle.getStyleClass().add("game-title");
		lbMyGeneralTitle.getStyleClass().add("game-title");	
		lbMyNeiTitle.getStyleClass().add("game-title");			
		btCollectTax.getStyleClass().add("menu-button");
		btUpgrade.getStyleClass().add("menu-button");
		btRecruit.getStyleClass().add("menu-button");
		btImproveCrop.getStyleClass().add("menu-button");
		btSendTroop.getStyleClass().add("menu-button");
		btReStartGame.getStyleClass().add("menu-button");
		btExit.getStyleClass().add("menu-button");
		listViewCity.getStyleClass().add("game-control");
		listViewMessage.getStyleClass().add("game-control");
		listViewRandomEvent.getStyleClass().add("game-control");
		listViewGeneral.getStyleClass().add("game-control");
		listViewNei.getStyleClass().add("game-control");

		
		//final
		pane.setRight(controlpane);
		pane.setCenter(containerCanvas);
		pane.getStyleClass().add("pane-game-start");
		
		return pane;
	}
	
	
	
	
	
	
	private void handleExitGame()
	{
		/**
		 * Todo: 
		 * 1. The handler for the Button exit and quit
		 * 2. When the button exit or quit is clicked, this function will be called.
		 * 3. When the function is called, there will be a dialog box showing 
		 * 		"Do you want to exit this game?"
		 * 4. If the user choose yes, then you can call Platform.exit();
		 * 
		 * Hint: user Alert: 
		 * 		Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to exit this game?", ButtonType.YES, ButtonType.NO);
		 * 
		 */
		Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to exit this game?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if(alert.getResult() == ButtonType.YES){
			Platform.exit();
		}	
		
		
	}
	
	//Draw the line in the map
	private void drawPath(int x1,int y1,int x2,int y2)
	{
				
		canvasGameStart.getGraphicsContext2D().strokeLine(x1,y1,x2,y2);
		canvasGameStart.getGraphicsContext2D().setStroke(Color.GREEN);
		canvasGameStart.getGraphicsContext2D().setLineWidth(5);
		canvasGameStart.getGraphicsContext2D().stroke();
		
		
	}
	private void clearLayer(Canvas layer)
	{
		//clear the content of the canvas
		layer.getGraphicsContext2D().clearRect(0, 0, layer.getWidth(), layer.getHeight());
	}
	//load the map and draw the towns in the map
	private void loadTownMap()
	{
		/**
		* TODO: 
		* 1. Draw the town/city/Metropolis in the canvas according to their coordinate
		* 2. Draw the flags next to the town/city/Metropolis's owners. 
		* 	For the 1st player, use the "flag_0.png", for the 2nd player, use the "flag_1.png"
		* 3. Draw the line between the connected town/city/Metropolis according to the data in the "MapData"
		* 	You can use the drawPath(int x1,int y1,int x2,int y2) to draw the line between two points (x1,y1) and (x2,y2).
		*
		*
		*/
		clearLayer(canvasGameStart);
		
		for(int i = 0;i < game.players.size();i++)
		{
			Player player = game.players.get(i);
			for(Town town: player.getTownList())
			{
				int longitute = town.getLongitude();
				int latitude = town.getLatitude();
				String townName = town.getName(); 
				Image image_flag = new Image("file:flag_"+i+".png");
				
				canvasGameStart.getGraphicsContext2D().drawImage(image_flag,latitude-40,longitute+40);
				
				if(town instanceof Metropolis)
				{
					Image image = new Image("file:metropolis.png");
					
					canvasGameStart.getGraphicsContext2D().drawImage(image,latitude,longitute);					
					canvasGameStart.getGraphicsContext2D().fillText(townName, latitude-20, longitute+20);
					
					
					
				}
				else if(town instanceof City)
				{
					Image image = new Image("file:city.png");
					
					canvasGameStart.getGraphicsContext2D().drawImage(image,latitude,longitute);
					canvasGameStart.getGraphicsContext2D().fillText(townName, latitude-20, longitute+20);
					
				}
				else if(town instanceof Town)
				{
					Image image = new Image("file:town.png");
					
					canvasGameStart.getGraphicsContext2D().drawImage(image,latitude,longitute);
					canvasGameStart.getGraphicsContext2D().fillText(townName, latitude-20, longitute+20);
					
				}
				ArrayList<Town> neiTowns = game.gameMap.getAdjacentTownList(town);
				for(Town neiTown:neiTowns)
				{
					drawPath(town.getLatitude() + 48,town.getLongitude() + 48,neiTown.getLatitude() + 48,neiTown.getLongitude() +48);
					
				}
			}
		}
				
		
	}
	
	private void handleNewGame() 
	{
		/**Todo: the handler for the button "New Game"
		 * 		 when the button "New Game" is clicked, this function will be called
		 * 
		 * Hints:
		 * 
		 * 1. load the map information from the file "MapData.txt":
		 * 		game.gameMap.loadGameMap("MapData.txt");
		 * 2. load the player information from the file "PlayersData.txt":
		 * 		game.loadPlayersData("PlayersData.txt");
		 * 3. call the loadTownMap() to plot the Map;
		 * 4. initialize the humanPlayer and the computerPlayers: 
		 * 		the first user in the "PlayersData.txt" is the humanPlayer;
		 * 5. change the scene: putSceneOnStage(SCENE_STARTGAME);
		 * 6. call the startTurn to start the Game: startTurn()
		 * 
		 *
		 */
		try{
		game.gameMap.loadGameMap("MapData.txt");
		game.loadPlayersData("PlayersData.txt");
		loadTownMap();
		humanPlayer = game.players.get(0);
		for(Player p: game.players){
			computerPlayers.add(p);
		}
		updateListViewCityItems();
		updateListViewGeneralItems();
		updateListViewNeiItems();
		printResult.add("-------------Game Begins!-------------");
		printResult.add("-----------------Red-----------------");
		updateListViewMessageItems();
		updateMyGold();
		putSceneOnStage(SCENE_STARTGAME);
		startTurn();
		}catch(IOException e){}
		
		
		
	}
	
	private void updateListViewRandomEvent()
	{
		//Update the listview of random event
		//Since the listview is updated outside the UI thread, Platform.runLater() is used here.
		
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	listViewRandomEventItems.clear();
            	for(int i = 0; i < RandomEventResult.size(); i++)
            	{
            		listViewRandomEventItems.add(RandomEventResult.get(i));
            	}
					
            	
            	
              
            }
          }); 
	}
	
	
	private void updateListViewMessageItems()
	{
		//Update the listview of command messages
		listViewMessageItems.clear();
		for(int i = 0;i < printResult.size();i++)
		{
			listViewMessageItems.add(printResult.get(i));
		}
			
		
		
	}
	private void updateListViewNeiItems()
	{
		//Update the listview of Neighbor City
		listViewNeiItems.clear();
		
		Town town = null;
		
		if(listViewSelectedCity!=null)
		{
			String selectedCityName = listViewSelectedCity.split(" ")[0];
			town = getTown(selectedCityName);
			
		
			ArrayList<Town> neiTowns = game.gameMap.getAdjacentTownList(town);
			for(Town neiTown:neiTowns)
			{
				int wall = 0;
				if(neiTown instanceof Metropolis)
				{
					wall = ((Metropolis) neiTown).walls;
				}
				listViewNeiItems.add(neiTown.name + "        " + neiTown.population + "         " + neiTown.armySize + "       " + town.cropYield + "         " + wall);
			}
		}
			
		
		
		
	}
	private void updateListViewCityItems()
	{
		/**
		 *  TODO: 
		 *  To update the listview of the information of the human player's cities.
		 *  1. It is similar to the function updateListViewNeiItems()
		 *  2. Use listViewCityItems.add() to add the information of cities
		 */
		listViewCityItems.clear();
		
		
		ArrayList<Town> towns = humanPlayer.getTownList();
		for(Town t: towns){
			int wall = 0;
			if(t instanceof Metropolis){
				wall = (((Metropolis)t).walls);
			}
			listViewCityItems.add(t.name + "        " + t.population + "         " + t.armySize + "       " + t.cropYield + "         " + wall);
		}
		
		
	}
	
	private void updateListViewGeneralItems() {
		/**
		 *  TODO: 
		 *  To update the listview of the information of the human player's generals.
		 *  1. It is similar to the function updateListViewNeiItems()
		 *  2. Use listViewGeneralItems.add() to add the information of generals.
		 */
		
		listViewGeneralItems.clear();
		
		
		ArrayList<General> general = humanPlayer.getGeneralList();
		for(General g: general){
			
			if(g.isReady() == true){
				listViewGeneralItems.add(g.getName() + "        " + g.getCombatPoint() + "         " + g.getLeadershipPoint()+ "       " + g.getWisdomPoint() + "         "+"R");
			}else{
				listViewGeneralItems.add(g.getName() + "        " + g.getCombatPoint() + "         " + g.getLeadershipPoint()+ "       " + g.getWisdomPoint() + "         "+"D");
			}
		}
		
	}

	//update the listview in another thread
	private void updateListView()
	{
		//Since the listview should be updated outside the UI thread, the Platform.runLater() is used here.
		//It will update the listview of the human player's generals, cities and neighbor cities.
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	
            		updateListViewCityItems();
					updateListViewGeneralItems();
					updateListViewNeiItems();
					updateMyGold();
					
            	
            	
              
            }
          }); 
	}
	
	//update the listview in another thread
	private void updateMessage()
	{
		//Since the listview should be updated outside the UI thread, the Platform.runLater() is used here.
		//It will update the listview of the command information
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	
            		updateListViewMessageItems();
					
            	
            	
              
            }
          }); 
	}
	
	//update the gold of the human player
	private void updateMyGold()
	{
		int gold = humanPlayer.getGold();
		lbMyGold.setText("My Gold: " + gold);
	}
	
	private void initWelcomeSceneHandler() 
	{
		/**
		 * TODO:
		 * To initialize the handler for the two buttons in the welcome scene
		 * 1. handleNewGame() for button btNewGame
		 * 2. handleExitGame() for button btQuit
		 */
		btNewGame.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				handleNewGame();
				
			}});
		btQuit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				handleExitGame();
				
			}});
		
	}
	
	
	private void initListView()
	{
		/**
		 * Initialize the listeners for all listview
		 */
		
		listViewGeneral.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() 
		{
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) 
			{
				if (new_val != null) 
				{
					//when a row in the listview is clicked, the listViewSelectedGeneral will be set as the content of that row
					listViewSelectedGeneral = new_val;
					
				}
			}
		});
		
		
		listViewCity.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() 
		{
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) 
			{
				if (new_val != null) 
				{
					listViewSelectedCity = new_val;
					updateListViewNeiItems();
					
				}
			}
		});
		
		
		listViewNei.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() 
		{
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) 
			{
				if (new_val != null) 
				{
					listViewSelectedNei = new_val;
					
				}
			}
		});
	}
	
	private void initStartGameSceneHandler()
	{
		/**
		 *  Todo: 
		 *  To initialize the handle for all listView and button in the start game pane.
		 *  1. call the initListView(); to initialize the listview
		 *  2. call setOnAction for each button
		 */
		initListView();
		
		btImproveCrop.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				handleImproveCrop();
				
			}});
		btCollectTax.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				handleCollectTax();
				
			}});
		btUpgrade.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				handleUpgrade();
				
			}});
		btRecruit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				handleRecruit();
				
			}});
		btSendTroop.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				handleSendTroop();
				
			}});
		btReStartGame.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				handleReStartGame();
				
			}});
		btExit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				handleExitGame();
				
			}});

	}
	
	private void showGameOver()
	{
		/**
		 * Todo: 
		 * TO Show Game Over using Dialog box. 
		 * When the game is over, this function will be called.
		 * Hint: 
		 * 1. Use another thread to handle it. You may use Platform.runLater in this function.
		 * 2. Show "YOU LOSE!" if the human player loses the game, otherwise "YOU WIN!"
		 * 3. You can use 
		 * 		Alert alert = new Alert(AlertType.CONFIRMATION, result, ButtonType.YES, ButtonType.NO);
		 * 		where result is the String to be presented
		 */
		Platform.runLater(new Runnable() {
            @Override
            public void run(){
            	if(humanPlayer.getTownList() == null){
            		String result = "YOU LOSE!\n Do you want to exit the game?";
            		Alert alert = new Alert(AlertType.CONFIRMATION,result, ButtonType.YES,ButtonType.NO);
            		//alert.setContentText("YOULOSE!Do you want to exit the game?");
            		alert.showAndWait();

            		if(alert.getResult() == ButtonType.YES){
            			Platform.exit();
            		}	
            		
            	}else{
            		String result = "YOU WIN! \n Do you want to exit the game?";
            		Alert alert = new Alert(AlertType.CONFIRMATION,result, ButtonType.YES,ButtonType.NO);
            		//alert.setContentText("Do you want to exit the game?");
            		alert.showAndWait();

            		if(alert.getResult() == ButtonType.YES){
            			Platform.exit();
            		}	
            		
            	}
            	
            }
		});
		
		
	}
	
	private void handleReStartGame() {
		/**
		 * Todo: 
		 * The handle for the button of restart game.
		 * When the button "Restart Game" is clicked, this function will be called.
		 * Hints:
		 * 1. Clear all the players
		 * 2. call game.gameMap.loadGameMap("MapData.txt");
		 	       game.loadPlayersData("PlayersData.txt");
				   loadTownMap();
		   3. Update all the listviewew
		   4. call the startTurn()
		 */
		
		try{
		gameOver = false;
		game.players.clear();
		computerPlayers.clear();
		game.gameMap.loadGameMap("MapData.txt");
		game.loadPlayersData("PlayersData.txt");
		
		humanPlayer = game.players.get(0);
		for(Player p: game.players){
			computerPlayers.add(p);
		}
		loadTownMap();
		updateListViewCityItems();
		updateListViewGeneralItems();
		updateListViewNeiItems();
		updateMyGold();
		printResult.add("Restart Game!");
		printResult.add("-----------------Red-----------------");
		updateListViewMessageItems();
		startTurn();
		}catch(IOException e){}
		
		
	}

	
	
	private void startTurn() {
		/**
		 * TODO: 
		 * 1. This function is to start the thread playerThread.
		 * 2. the function processPlayerTurns() will be run in the playerThread.
		 */
		playerThread = new Thread(new Runnable(){
			@Override
			public void run(){
				while(!gameOver)
				{
				processPlayerTurns();
				}
			}
		});
			
		playerThread.start();
		
		
			
		}	
		
		

	public void processPlayerTurns()
	{
		/**
		 * TODO: 
		 * 1. This function is for processing the commands of computer players.
		 * 2. All the computer players have to wait until all the generals of human player are done.
		 * 3. When the game over or when the human player chooses to restart the game, the function will be terminated.
		 * 4. After all the computer players finish their commands, all the generals of human players will be ready.
		 * 5. Each computer player in each round will randomly select a general, a town/city/metropolis and a command.
		 * 6. The same as the PA1, there are five command"
		 * (1)collect tax;(2)upgrade town;(3)improve crop yield;(4)recruit;(5)send troops 
		 * 7. All the output message will be added in the arraylist printResult and print in the log window.
		 * 8. When the game is over, there will be a dialog box to print the result and ask whether to exit the game.
		 * 	  To achieve this goal, you can call showGameOver();
		 * 9. When the button "Restart" is clicked, this function will be over. The variable gameOver can be used here.
		 * 10. If you want to change the UI outside the UI-thread, you should use Platform.runLater(), an example is provides in the updateMessage().
		 */
			if(btReStartGame.isPressed()){ //terminate if restart is press and gameOver is true to stop the thread run
				gameOver = true;
				computerPlayers.clear();
				return;
			}	
	
			if(game.isGameOver()){
				humanPlayer.readyAllGenerals();
				gameOver = true;
				computerPlayers.clear();
				showGameOver();
				return;
			}else{
				
			if(!humanPlayer.hasReadyGenerals()){		
			printResult.add("-------------Blu-------------");
			Player comp = computerPlayers.get(1); //since we have just two players		
			//randomselect	
			Random randomgenerator = new Random();
			while(comp.hasReadyGenerals()){
				try{
				int randomnum1 = randomgenerator.nextInt(comp.getGeneralList().size());
				if(!comp.getGeneralList().get(randomnum1).isReady()){
					continue;
				}
				General selectgeneral = comp.getGeneralList().get(randomnum1);
				int randomnum2 = randomgenerator.nextInt(comp.getTownList().size());
				Town selecttown = comp.getTownList().get(randomnum2);
				int randomcommand = randomgenerator.nextInt(5);
				
				switch(randomcommand){
				case 0:
					selecttown.collectTax(comp, selectgeneral);
					break;
				case 1:
					comp.upgradeTown(selecttown, selectgeneral);
					loadTownMap();
					break;
				case 2:
					selecttown.improveCropYield(selectgeneral);
					break;
				case 3:
					int randombudget = randomgenerator.nextInt(99);
					selecttown.recruitArmy(comp, selectgeneral, randombudget);
					break;
				case 4:
					try{
					int randomtroops = randomgenerator.nextInt(99);
					int randomnum4 = randomgenerator.nextInt(game.gameMap.getAdjacentTownList(selecttown).size());
					Town randomtarget = game.gameMap.getAdjacentTownList(selecttown).get(randomnum4);
					
					if (comp.getTownList().contains(randomtarget)) {
						 ((City)selecttown).transferArmy(randomtarget, selectgeneral, randomtroops);
						
					} else {
						if (((City)selecttown).attackTown(randomtarget, selectgeneral, randomtroops)) {
							if (randomtarget.getArmySize()<=0) {
								game.gameMap.getTownOwner(randomtarget).surrenderTown(randomtarget, comp);
								
							}
							selectgeneral.endTurn();
							
						}			
			
					}
					loadTownMap(); //reload the map as processsendtroopscommand() did not reload
					break;
					}catch(ClassCastException e){
						System.out.println("ERROR: invalid town type");
						printResult.add("ERROR: invalid town type");
						updateMessage();
						break;
					}
			}

			
			Platform.runLater(new Runnable() { //updatelistview
		            @Override
		            public void run() {
		            updateListView();
		            updateMessage();
		            }
		          }); 		
				}catch(IllegalArgumentException e){}
			}
			humanPlayer.readyAllGenerals();
			
			
			if(!comp.hasReadyGenerals()){
				for(Player p : computerPlayers){
					for(Town t:p.getTownList()){
						t.processTurn(Math.random());
					}			
					}
				updateListViewRandomEvent();
			}
			
			printResult.add("-------------Red-------------");
			comp.readyAllGenerals();
				}		
	}
			}
				
			
	
	
	//return the general object given the general's name
	private General getGeneral(String generalName)
	{
		for(General general:humanPlayer.getGeneralList())
		{
			if(general.getName().equals(generalName))
			{
				return general;
			}
		}
		return null;
		
	}
	
	//return the town object given the town's name
	private Town getTown(String townName)
	{
		for(Town town:humanPlayer.getTownList())
		{
			if(townName.equals(town.name))
			{
				return town;
			}
		}
		return null;
	}
	
	//return the general selected by the user
	private General selectGeneral()
	{
		General selectedGeneral = null;
		if(listViewSelectedGeneral!=null)
		{
			String generalName = listViewSelectedGeneral.split(" ")[0];
			selectedGeneral = getGeneral(generalName);
			//check whether generalName is ready
			if(!selectedGeneral.isReady())
			{
				listViewSelectedGeneral = null;	
				String result = "You have to select a ready general";
				printResult.add(result);
				updateListViewMessageItems();
				return null;
			}
			
		}
		return selectedGeneral;
	}
	
	// return the town selected by the user 
	private Town selectTown()
	{
		Town selectedTown = null;
		if(listViewSelectedCity!=null)
		{
			String cityName = listViewSelectedCity.split(" ")[0];
			selectedTown = getTown(cityName);
			
			
		}
		return selectedTown;
		
		
	}
	
	//return the neighbor town selected by the user
	private Town selectTargetTown(Town selectedTown)
	{
		Town targetTown = null;
		if(listViewSelectedNei != null && selectedTown != null)
		{
			String cityName = listViewSelectedNei.split(" ")[0];
			System.out.println(cityName);
			System.out.println(selectedTown.name);
			ArrayList<Town> neiTowns = game.gameMap.getAdjacentTownList(selectedTown);
			for(Town town:neiTowns)
			{
				System.out.println(town.name);
				if(cityName.equals(town.name))
				{
					targetTown = town;
					break;
				}
			}
			listViewSelectedNei = null;
		}
		
		return targetTown;
	}

	
	private void handleSendTroop() {
		/**
		 * 
		 * When the button send troop is clicked, this function will be executed.
		 *  
		 * 
		 */
		
		General selectedGeneral = null;
		Town selectedTown = null;
		Town targetTown = null;
		//get troopSize
		String tfText = tfSendTroop.getText();
		int troopSize;
		try
		{
			troopSize = Integer.parseInt(tfText);
		}catch(NumberFormatException e)
		{
			System.out.println("ERROR: invalid input");
			String result = "ERROR: invalid input";
			printResult.add(result);
			updateListViewMessageItems();
			
	    	return;
		}
		if (troopSize < 0) {
			System.out.println("ERROR: invalid input");
			String result = "ERROR: invalid input";
			printResult.add(result);
			updateListViewMessageItems();
	    	return ;	    	
		}
		
		
		//get general
		selectedGeneral = selectGeneral();
		if(selectedGeneral == null)
		{
			System.out.println("general");
		}
		//get Town
		selectedTown = selectTown();
		if(selectedTown == null)
		{
			System.out.println("town");
		}
		//get TargetTown
		targetTown = selectTargetTown(selectedTown);
		
		if(selectedGeneral== null || selectedTown == null || targetTown == null)
		{
			
			String result = "ERROR: You have to select a general, a town and a target town";
			printResult.add(result);
			updateListViewMessageItems();
			//print error info
			return;
		}
		
		if (!(selectedTown instanceof City || selectedTown instanceof Metropolis)) 
		{
			System.out.println("ERROR: Invalid town type");
			String result = "ERROR: Invalid town type";
			printResult.add(result);
			updateListViewMessageItems();
			return;
		}
		if (humanPlayer.getTownList().contains(targetTown)) 
		{
			((City)selectedTown).transferArmy(targetTown, selectedGeneral, troopSize);
			updateListViewMessageItems();
			
		} 
		else 
		{
			
			if (((City)selectedTown).attackTown(targetTown, selectedGeneral, troopSize)) {
				updateListViewMessageItems();
				if (targetTown.getArmySize()<=0) {
					game.gameMap.getTownOwner(targetTown).surrenderTown(targetTown, humanPlayer);
					updateListViewMessageItems();
					
				}
				
				selectedGeneral.endTurn();//modified by GY	
			}
		}
		updateListViewCityItems();
		updateListViewGeneralItems();
		updateListViewNeiItems();
		updateMyGold();
		loadTownMap();
		
	}

	private void handleUpgrade() {
		
		/**
		 * Todo: 
		 * When the button upgrade is clicked, this function will be called.
		 * 1. call selectGeneral() to obtain the selected general
		 * 2. call selectTown() to obtain the selected town
		 * 3. call upgradeTown(selectedTown, selectedGeneral) to upgrade
		 * 4. if upgrade successfully, call loadTownMap() to redraw the map
		 * 5. update all the listview and myGold
		 */
		General selectedGeneral = selectGeneral();
		Town selectedTown = selectTown();
		if(selectedTown == null || selectedGeneral == null){
			System.out.println("ERROR: You have to select a general and a town");
			String result = "ERROR: You have to select a general and a town";
			printResult.add(result);
			updateListViewMessageItems();
			return;
		}
		if(humanPlayer.upgradeTown(selectedTown, selectedGeneral)){
			updateListViewCityItems();
			updateListViewGeneralItems();
			updateListViewNeiItems();
			updateMyGold();
			updateListViewMessageItems();
			loadTownMap();
		}else if(selectedTown instanceof Metropolis){
			String result = "ERROR: already at max tier";
			printResult.add(result);
			updateListViewMessageItems();
			return;
		}
		
		
		
	}

	private void handleRecruit() {
		/**
		 * Todo: 
		 * When the button recruit is clicked, this function will be executed.
		 * 1. Obtain the input budget from the TextField using tfRecruit.getText();
		 * 2. call selectGeneral() and selectTown() to obtain the selected general and town
		 * 3. call selectedTown.recruitArmy(humanPlayer, selectedGeneral, budget) to execute the command
		 * 4. Update all the listview and myGold
		 */
		int budget;
		try{
			budget = Integer.parseInt(tfRecruit.getText());
		}catch(NumberFormatException e)
		{
			System.out.println("ERROR: invalid input");
			String result = "ERROR: invalid input";
			printResult.add(result);
			updateListViewMessageItems();
			
	    	return;
		}
		Town selectedTown = selectTown();
		if(budget <= 0){
			String result = "ERROR: invalid input";
			printResult.add(result);
			updateListViewMessageItems();
			return;
		}
		if(humanPlayer.getGold() <=0 || (humanPlayer.getGold() < budget)){
			String result = "ERROR: not enough Gold";
			printResult.add(result);
			updateListViewMessageItems();
			return;
		}
		General selectedGeneral = selectGeneral();
		if(selectedTown == null || selectedGeneral == null){
			String result = "You have to select a general and a town";
			printResult.add(result);
			updateListViewMessageItems();
			return;
		}
		selectedTown.recruitArmy(humanPlayer, selectedGeneral, budget);
		updateListViewCityItems();
		updateListViewGeneralItems();
		updateListViewNeiItems();
		updateMyGold();
		
		
	}

	private void handleCollectTax() {
		/**
		 * Todo: 
		 * When the button collect tax is clicked, this function is called.
		 * 1. selectGeneral() for obtaining the selected general
		 * 2. selectTown() for obtaining the selected town
		 * 3. call selectedTown.collectTax(humanPlayer, selectedGeneral) to execute the command
		 * 4. update all the listview and myGold:
		 * 		updateListViewCityItems();
		 * 		updateListViewGeneralItems();
		 * 		updateListViewMessageItems();
		 * 		updateMyGold();
		 * 		
		 */
		General selectedGeneral = selectGeneral();
		Town selectedTown = selectTown();
		if(selectedTown == null || selectedGeneral == null){
			String result = "You have to select a general and a town";
			printResult.add(result);
			updateListViewMessageItems();
			return;
		}
		selectedTown.collectTax(humanPlayer,selectedGeneral);
		updateListViewCityItems();
		updateListViewGeneralItems();
		updateListViewMessageItems();
		updateMyGold();
		
		
		
	}
	
	
	private void handleImproveCrop() {
		/**
		 * Todo: 
		 * When the button improve crop is clicked, this function will be called.
		 * Hints:
		 * 1. selectGeneral() for obtaining the selected general
		 * 2. selectTown() for obtaining the selected town
		 * 3. call selectedTown.improveCropYield(selectedGeneral) to execute the command
		 * 4. update all the listview and myGold:
		 * 		updateListViewCityItems();
		 * 		updateListViewGeneralItems();
		 * 		updateListViewMessageItems();
		 * 		updateMyGold();
		 * 		
		 *
		 */
		Town selectedTown = selectTown();
		General selectedGeneral = selectGeneral();
		if(selectedTown == null || selectedGeneral == null){
			String result = "You have to select a general and a town";
			printResult.add(result);
			updateListViewMessageItems();
			return;
		}
		selectedTown.improveCropYield(selectedGeneral);
		updateListViewCityItems();
		updateListViewGeneralItems();
		updateListViewNeiItems();
		updateMyGold();
		updateListViewMessageItems();
	}

	private void initEventHandlers() 
	{
		
		initWelcomeSceneHandler();
		initStartGameSceneHandler();
		
	}

	
	private void initScenes() 
	{
		scenes[SCENE_WELCOME] = new Scene(paneWelcome(), 1300,750);
		scenes[SCENE_STARTGAME] = new Scene(paneStartGame(), 1300,750);
		
		for (int i = 0; i < SCENE_NUM; i++)
		{
			scenes[i].getStylesheets().add("menu_and_css/styles.css"); // share stylesheet for all scenes
		}
	}

	private void putSceneOnStage(int sceneID) 
	{
		// ensure the sceneID is valid
		if (sceneID < 0 || sceneID >= SCENE_NUM)
		{
			return;
		}

		stage.hide();
		stage.setTitle(SCENE_TITLES[sceneID]);
		stage.setScene(scenes[sceneID]);
		stage.show();
	}
	
	

	@Override
	public void start(Stage primaryStage) throws Exception {
			
		initScenes();
		initEventHandlers();
		stage = primaryStage;
		putSceneOnStage(SCENE_WELCOME);
		
	}
	public static void main(String[] args)
	{
		launch(args);
	}
	
	
}
