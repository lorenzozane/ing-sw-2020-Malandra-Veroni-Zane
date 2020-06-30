package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuiNetworkController {
    private GuiController guiController;
    private Scene networkScene;

    @FXML
    private void initialize() {
        guiController = GuiController.getInstance();
        //guiController.setNetworkController(this);
    }

    protected void setScene(Scene settingScene) {
        if (this.networkScene == null) {
            this.networkScene = settingScene;
        }
    }

            @FXML
    Button sendButton;

    @FXML
    TextField textIP;

    @FXML
    TextField textPORT;

    public void sendIpPort(ActionEvent event){
        if(!(textIP.getText().isEmpty() || textPORT.getText().isEmpty())) {
            if(checkIp(textIP.getText())) {
                try {
                     Gui.setClient(networkScene, new Client(textIP.getText(), Integer.parseInt(textPORT.getText())));
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error ");
                    alert.setHeaderText("Server not reachable");
                    alert.setContentText("Please try again with another IP or PORT");

                    alert.showAndWait();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error ");
                alert.setHeaderText("IP format not valid");
                alert.setContentText("Please try again with another IP or PORT");

                alert.showAndWait();
            }
        }


    }

    public boolean checkIp(String ip){
        Pattern pattern;
        Matcher matcher;
        String IPADDRESS_PATTERN
                = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        pattern = Pattern.compile(IPADDRESS_PATTERN);
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }

}
