package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;
import java.util.Date;

public class manageVacationsController {
    public TableView<ListTableMV> MVTable;
    public TableColumn<ListTableMV,String> Name;
    public TableColumn<ListTableMV, String> StartDate;
    public TableColumn<ListTableMV,String> EndDate;
    public TableColumn<ListTableMV,String> Type;
    public TableColumn<ListTableMV,Integer> NumOfReqDays;
    public TableColumn<ListTableMV,Integer> NumOfRemDays;
    public TableColumn<ListTableMV,Integer> ID;
    public TextField search;
    float remDays=14;
    float reqDyas=14;

    public void initialize(){
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        StartDate.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
        EndDate.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
        Type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        NumOfReqDays.setCellValueFactory(new PropertyValueFactory<>("NumOfReqDays"));
        NumOfRemDays.setCellValueFactory(new PropertyValueFactory<>("NumOfRemDays"));
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        if(search.getText().isEmpty()){
            getVacations();
        }
    }
    public ObservableList<ListTableMV> getVacations(){

        ObservableList<ListTableMV> VacationItem= FXCollections.observableArrayList();
        Unit con=new Unit();
        try {
            Connection connection=con.mySQLConnect();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from vacation");
            PreparedStatement preparedStatement1=connection.prepareStatement("select employees.Fname" +
                    ",employees.Lname from employees INNER JOIN empvac ON empvac.EmpID=employees.ID");
            System.out.println("0");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSet resultSet1 =preparedStatement1.executeQuery();
            while(resultSet.next() && resultSet1.next()){
                ListTableMV vacation=new ListTableMV();
                vacation.Name.set(resultSet1.getString("Fname")+" "+resultSet1.getString("Lname"));
                vacation.StartDate.set(resultSet.getDate("StartDate").toString());
                vacation.EndDate.set(resultSet.getDate("EndDate").toString());
                vacation.NumOfReqDays.set(resultSet.getInt("ReqVacDay"));
                vacation.NumOfRemDays.set(resultSet.getInt("RemVacDay"));
                vacation.ID.set(resultSet.getInt("id"));
                vacation.Type.set(resultSet.getString("Emergency"));
                vacation.flag.set(resultSet.getInt("Flag"));
                VacationItem.add(vacation);
            }

            MVTable.setItems(VacationItem);
            connection.close();
        }catch (Exception ex){
            System.out.println(ex);
        }
        return VacationItem;
    }
    public void accept(){
        Unit con=new Unit();
        Connection connection=con.mySQLConnect();
        PreparedStatement preparedStatement = null;
        try {
            Integer idSelected = MVTable.getSelectionModel().getSelectedItem().getID();
            if(Name.equals(null)){
                con.showAlert("Done", "Manage vacations",
                        "Please select an employee from table!", Alert.AlertType.WARNING);
            }
            else{
                if(remDays<=14 && remDays>0){
                    preparedStatement = connection.prepareStatement("update vacation set Flag=1 where id="+idSelected);
                    preparedStatement.executeUpdate();
                    remDays=remDays-1;
                }
                if(remDays==0){

                }

                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void refuse(){
        Unit con=new Unit();
        Connection connection=con.mySQLConnect();
        PreparedStatement preparedStatement = null;
        try {
            Integer idSelected = MVTable.getSelectionModel().getSelectedItem().getID();
            if(Name.equals(null)){
                con.showAlert("Done", "Manage vacations",
                        "Please select an employee from table!", Alert.AlertType.WARNING);
            }
            else{
                preparedStatement = connection.prepareStatement("update vacation set Flag=0 where id="+idSelected);
                preparedStatement.executeUpdate();
                connection.close();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void search(){
        Unit con=new Unit();
        Connection connection=con.mySQLConnect();
        boolean flag=false;
        try {
            if(search.getText().isEmpty()){
                con.showAlert("Done", "Manage vacations",
                        "Please make sure to enter a name in search field!", Alert.AlertType.WARNING);
            }
            else{
                ObservableList<ListTableMV> VacationItem= FXCollections.observableArrayList();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from vacation");
                PreparedStatement preparedStatement1=connection.prepareStatement("select employees.Fname" +
                        ",employees.Lname from employees INNER JOIN empvac ON empvac.EmpID=employees.ID");

                for(int i=0;i<MVTable.getItems().size();i++){
                    System.out.println(Name.getCellObservableValue(i).getValue());
                    if(search.getText().equals(Name.getCellObservableValue(i).getValue())){
                        ResultSet resultSet = preparedStatement.executeQuery();
                        ResultSet resultSet1 =preparedStatement1.executeQuery();
                        while(resultSet.next() && resultSet1.next()){
                            ListTableMV vacation=new ListTableMV();
                            vacation.Name.set(resultSet1.getString("Fname")+" "+resultSet1.getString("Lname"));
                            vacation.StartDate.set(resultSet.getDate("StartDate").toString());
                            vacation.EndDate.set(resultSet.getDate("EndDate").toString());
                            vacation.NumOfReqDays.set(resultSet.getInt("ReqVacDay"));
                            vacation.NumOfRemDays.set(resultSet.getInt("RemVacDay"));
                            vacation.ID.set(resultSet.getInt("id"));
                            vacation.Type.set(resultSet.getString("Emergency"));
                            vacation.flag.set(resultSet.getInt("Flag"));
                            VacationItem.add(vacation);
                            if(search.getText().equals(resultSet1.getString("Fname")+" "+resultSet1.getString("Lname"))){
                                break;
                            }

                        }
                        MVTable.setItems(VacationItem);
                        connection.close();
                    }
                    else{
                        flag=true;
                    }
                }
                if(flag){
                    con.showAlert("Done", "Manage vacations",
                            "Not found!", Alert.AlertType.ERROR);
                }
            }

            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void close(){
        search.setText("");
        getVacations();
    }
    public void backHome(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("ManagerHomePage.fxml"));
        stage.setScene(root.getScene());
        stage.setResizable(false);
    }

}
