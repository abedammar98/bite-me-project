package gui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.URL;
import java.util.ResourceBundle;

import Server.DBControl;
import Server.EchoServer;
import Server.ServerUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.ClientInfo;

/**
 * Controller for the Server Port Frame in the application. This class manages
 * the server connection, disconnection, data import, and client connection
 * details, including the display of connected clients.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class ServerPortFrameController implements Initializable {

	/** TextArea for displaying server logs and messages. */
	@FXML
	private TextArea txtareaserver;

	/** TableView for displaying information about connected clients. */
	@FXML
	private TableView<ClientInfo> tblViewConnection;

	/** TableColumn for displaying the hostname of connected clients. */
	@FXML
	private TableColumn<ClientInfo, String> hostnameCol;

	/** TableColumn for displaying the IP address of connected clients. */
	@FXML
	private TableColumn<ClientInfo, String> ipCol;

	/** TableColumn for displaying the status of connected clients. */
	@FXML
	private TableColumn<ClientInfo, String> statusCol;

	/** ToggleButton for filtering clients by general clients. */
	@FXML
	private ToggleButton tglBtnClient;

	/** ToggleButton for filtering clients by customers. */
	@FXML
	private ToggleButton tglBtnCustomer;

	/** ToggleButton for filtering clients by suppliers. */
	@FXML
	private ToggleButton tglBtnSupplier;

	/** ToggleButton for filtering clients by managers. */
	@FXML
	private ToggleButton tglBtnManager;

	/** ToggleButton for filtering clients by CEOs. */
	@FXML
	private ToggleButton tglBtnCeo;

	/** Button for connecting to the server. */
	@FXML
	private Button btnconnect;

	/** Button for disconnecting from the server. */
	@FXML
	private Button btnDisconnect;

	/** Button for exiting the server application. */
	@FXML
	private Button btnExit;

	/** Text displaying the server port. */
	@FXML
	private Text porttxt;

	/** Text displaying the server IP address. */
	@FXML
	private Text iptxt;

	/** Button for importing data into the server. */
	@FXML
	private Button btnImport;

	/** TextField for entering the database name. */
	@FXML
	private TextField dbnametxtfield;

	/** TextField for entering the MySQL username. */
	@FXML
	private TextField mysqlusertxtfield;

	/** TextField for entering the MySQL password. */
	@FXML
	private TextField mysqlpasstxtfield;

	/** ObservableList for holding client information data. */
	private ObservableList<ClientInfo> clientInfoList = FXCollections.observableArrayList();

	/** ToggleGroup for managing the toggle buttons. */
	private ToggleGroup group1 = new ToggleGroup();

	/** Flag to track the import status. */
	private int flag = 0;

	/**
	 * Starts the ServerPortFrameController and sets up the user interface.
	 * 
	 * @param primaryStage The primary stage for this application.
	 * @throws Exception If there is an error during the initialization of the
	 *                   stage.
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerPort.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/ServerPort.css").toExternalForm());
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Handles the action of disconnecting from the server.
	 * 
	 * @param event The event triggered by the user's action.
	 */
	public void getDisconnectBtn(ActionEvent event) {
		btnImport.setDisable(true);
		ServerUI.sv.closeDB();
		btnDisconnect.setDisable(true);
		btnconnect.setDisable(false);
	}

	/**
	 * Handles the action of connecting to the server.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the connection process.
	 */
	public void getConnectBtn(ActionEvent event) throws Exception {
		ServerUI.sv.startDB(dbnametxtfield.getText(), mysqlusertxtfield.getText(), mysqlpasstxtfield.getText());
		ServerUI.sv.setController(this);
		if (flag == 0)
			btnImport.setDisable(false);
		btnconnect.setDisable(true);
		btnDisconnect.setDisable(false);
	}

	/**
	 * Handles the action of exiting the server application.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the exit process.
	 */
	public void getExitBtn(ActionEvent event) throws Exception {
		System.out.println("exiting server");
		System.exit(0);
	}

	/**
	 * Handles the action of importing data into the server's database.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the data import process.
	 */
	@FXML
	public void getImportBtn(ActionEvent event) throws Exception {
		btnImport.setDisable(true);
		boolean result = (boolean) DBControl.getInstance().importExternalDB();
		if (result) {
			showAlert("Data Import", "Data import completed successfully.");
			flag = 1;
		} else
			showAlert("Data Import", "Data import failed.");
	}

	/**
	 * Initializes the controller after its root element has been completely
	 * processed. Sets up the server configuration and UI components.
	 * 
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnImport.setDisable(true);
		btnDisconnect.setDisable(true);
		String ip;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		}
		porttxt.setText(Integer.toString(ServerUI.DEFAULT_PORT));
		iptxt.setText(ip);
		PrintStream printStream = new PrintStream(new TextAreaOutputStream(txtareaserver));
		System.setOut(printStream);
		mysqlpasstxtfield.setText("Aa123456");
		mysqlusertxtfield.setText("root");
		dbnametxtfield.setText("jdbc:mysql://localhost/biteme");

		initializeTableColumns();
		addToggleButtonsListeners();
	}

	/**
	 * Initializes the columns of the TableView for displaying client information.
	 */
	private void initializeTableColumns() {
		hostnameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		ipCol.setCellValueFactory(new PropertyValueFactory<>("address"));
		statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
		tblViewConnection.setItems(clientInfoList);
	}

	/**
	 * Adds listeners to the toggle buttons to update the TableView based on the
	 * selected filter.
	 */
	private void addToggleButtonsListeners() {
		tglBtnClient.setToggleGroup(group1);
		tglBtnCustomer.setToggleGroup(group1);
		tglBtnSupplier.setToggleGroup(group1);
		tglBtnManager.setToggleGroup(group1);
		tglBtnCeo.setToggleGroup(group1);

		group1.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
				oldValue.setSelected(true);
			} else {
				updateTableView();
			}
		});

		tglBtnClient.setOnAction(event -> updateTableView());
		tglBtnCustomer.setOnAction(event -> updateTableView());
		tglBtnSupplier.setOnAction(event -> updateTableView());
		tglBtnManager.setOnAction(event -> updateTableView());
		tglBtnCeo.setOnAction(event -> updateTableView());
	}

	/**
	 * Updates the TableView with the list of clients based on the selected toggle
	 * button.
	 */
	public void updateTableView() {
		ToggleButton selectedToggleButton = (ToggleButton) group1.getSelectedToggle();
		if (selectedToggleButton == tglBtnClient) {
			clientInfoList.setAll(EchoServer.ConnectedClientsList);
		} else if (selectedToggleButton == tglBtnCustomer) {
			clientInfoList.setAll(EchoServer.ConnectedCustomerList);
		} else if (selectedToggleButton == tglBtnSupplier) {
			clientInfoList.setAll(EchoServer.ConnectedSupplierList);
		} else if (selectedToggleButton == tglBtnManager) {
			clientInfoList.setAll(EchoServer.ConnectedManagerList);
		} else if (selectedToggleButton == tglBtnCeo) {
			clientInfoList.setAll(EchoServer.ConnectedCeoList);
		}
	}

	/**
	 * Custom OutputStream class to write to the TextArea. This class captures
	 * system output and redirects it to a TextArea.
	 */
	private static class TextAreaOutputStream extends OutputStream {
		/** The TextArea to which the output will be redirected. */
		private final TextArea textArea;

		/**
		 * Constructs a new TextAreaOutputStream.
		 * 
		 * @param textArea The TextArea to which the output will be redirected.
		 */
		public TextAreaOutputStream(TextArea textArea) {
			this.textArea = textArea;
		}

		/**
		 * Writes a single byte of data to the TextArea.
		 * 
		 * @param b The byte to be written.
		 * @throws IOException If an I/O error occurs.
		 */
		@Override
		public void write(int b) throws IOException {
			textArea.appendText(String.valueOf((char) b));
		}

		/**
		 * Writes an array of bytes to the TextArea.
		 * 
		 * @param b   The byte array to be written.
		 * @param off The start offset in the data.
		 * @param len The number of bytes to write.
		 * @throws IOException If an I/O error occurs.
		 */
		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			textArea.appendText(new String(b, off, len));
		}
	}

	/**
	 * Displays an alert with the specified title and message.
	 * 
	 * @param title   The title of the alert.
	 * @param message The message to be displayed in the alert.
	 */
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
