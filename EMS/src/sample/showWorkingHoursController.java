package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class showWorkingHoursController implements Initializable {
    @FXML
    private ImageView profilePhoto ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            Unit u = new Unit();
            Connection con = u.mySQLConnect();
            Statement s = con.createStatement();
            String searchStr = "select Photo from managers where ID = " + LogInCVController.userID;
            ResultSet check = s.executeQuery(searchStr);
            check.next() ;
            String pn = check.getString(1) ;
            Image MImage = new Image(getClass().getResourceAsStream("/employeesPhotos/"+pn));
            profilePhoto.setImage(MImage);

        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void backHome(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("ManagerHomePage.fxml"));
        stage.setScene(root.getScene());
        stage.setResizable(false);
    }
    public void showProfile(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("profile.fxml"));
        stage.setScene(root.getScene());
        stage.setResizable(false);
    }
}
