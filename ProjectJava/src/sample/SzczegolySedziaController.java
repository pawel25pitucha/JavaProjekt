package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SzczegolySedziaController {
    @FXML
    private Text imieTXT;
    @FXML
    private Text nazwiskoTXT;
    @FXML
    private Text peselTXT;
    @FXML
    private Text dataTXT;
    @FXML
    private Text plecTXT;

    private String pesel;

    public void initialize() throws SQLException {
    pesel=SedziowieController.getPesel();
    ResultSet rs = ConnectionDB.con.createStatement().executeQuery("SELECT * FROM Sędzia WHERE Pesel="+"'"+pesel+"'");
        while(rs.next()) {
        imieTXT.setText(rs.getString("Imię"));
        nazwiskoTXT.setText(rs.getString("Nazwisko"));
        peselTXT.setText(rs.getString("Pesel"));
        dataTXT.setText(rs.getString("Data_urodzenia"));
        plecTXT.setText(rs.getString("Nazwisko"));
        plecTXT.setText(rs.getString("Płeć"));
    }
}


    public void close(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
