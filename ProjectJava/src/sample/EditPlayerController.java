package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditPlayerController {
    private String dateFormat="yyyy-MM-dd";

        @FXML
        private TextField PeselTXT;
        @FXML
        private TextField ImieTXT;
        @FXML
        private TextField NazwiskoTXT;
        @FXML
        private TextField DataTXT;
        @FXML
        private TextField poziomTXT;
        @FXML
        private CheckBox kTXT;
        @FXML
        private CheckBox mTXT;
        @FXML
        private Text errorMSG;



        //Dane zawodnika
        private static String pesel= ZawodnicyController.getPesel();
        private static String imie;
        private static String nazwisko;
        private static String data;
        private static String plec;
        private static String poziom;

        @FXML
        public void initialize() throws SQLException {
            loadData();
        }

        public void loadData() throws SQLException {
            Statement stmt = ConnectionDB.con.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM Zawodnik WHERE Pesel="+"'"+pesel+"'");
            while(result.next()){
                imie= result.getString("Imię");
                nazwisko=result.getString("Nazwisko");
                data=result.getString("Data_urodzenia");
                poziom=result.getString("Poziom");
                plec=result.getString("Płeć");
            }
            PeselTXT.setText(pesel);
            ImieTXT.setText(imie);
            NazwiskoTXT.setText(nazwisko);
            DataTXT.setText(data);
            poziomTXT.setText(poziom);
            if(plec=="k"){
                kTXT.setSelected(true);
                mTXT.setSelected(false);
            }else {
                kTXT.setSelected(false);
                mTXT.setSelected(true);
            }
        }
    public void updatePlayer(ActionEvent event) throws IOException {
        //zapisanie danych
        pesel=PeselTXT.getText();
        imie=ImieTXT.getText();
        nazwisko=NazwiskoTXT.getText();
        data=DataTXT.getText();
        poziom=poziomTXT.getText();

        if(checkDaneOsobowe()){
            //zamkniecie okna
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

            //zmiana okna
            changeView("viewsFXML/Zawodnik/edytujAdres.fxml");
        }else{
            System.out.println("Bledne dane");
        }
    }
    public static void updateZawodnik() throws SQLException {
            String id=getId();
        String sql1 = "UPDATE Zawodnik SET Pesel=? WHERE Id=?";
        String sql2 = "UPDATE Zawodnik SET Imię=? WHERE Id=?";
        String sql3 = "UPDATE Zawodnik SET Nazwisko=? WHERE Id=?";
        String sql4 = "UPDATE Zawodnik SET Data_urodzenia=? WHERE Id=?";
        String sql5 = "UPDATE Zawodnik SET Poziom=? WHERE Id=?";
        String sql6 = "UPDATE Zawodnik SET Płeć=? WHERE Id=?";

        PreparedStatement stmt = ConnectionDB.con.prepareStatement(sql1);
        PreparedStatement stmt2 = ConnectionDB.con.prepareStatement(sql2);
        PreparedStatement stmt3 = ConnectionDB.con.prepareStatement(sql3);
        PreparedStatement stmt4 = ConnectionDB.con.prepareStatement(sql4);
        PreparedStatement stmt5 = ConnectionDB.con.prepareStatement(sql5);
        PreparedStatement stmt6 = ConnectionDB.con.prepareStatement(sql6);

        stmt.setString(1,pesel);
        stmt.setString(2,id);
        stmt2.setString(1,imie);
        stmt2.setString(2,id);
        stmt3.setString(1,nazwisko);
        stmt3.setString(2,id);
        stmt4.setString(1,data);
        stmt4.setString(2,id);
        stmt5.setString(1,poziom);
        stmt5.setString(2,id);
        stmt6.setString(1,plec);
        stmt6.setString(2,id);

        stmt.executeUpdate();
        stmt2.executeUpdate();
        stmt3.executeUpdate();
        stmt4.executeUpdate();
        stmt5.executeUpdate();
        stmt6.executeUpdate();



    }

   private static String getId() throws SQLException {
        String id = null;
        ResultSet result = ConnectionDB.con.createStatement().executeQuery("SELECT Id FROM Zawodnik WHERE Pesel=" + "'" + pesel + "'");
        while (result.next()) {
            id = result.getString("Id");
        }
        return id;
    }

    public boolean checkDaneOsobowe() {
        if (kTXT.isSelected() && mTXT.isSelected() == false) {
            plec = "k";
        } else if (mTXT.isSelected() && kTXT.isSelected() == false) {
            plec = "m";
        } else {
            errorMSG.setText("Złe dane dotyczące płci!");
            return false;
        }
        if (pesel.length() == 11 && pesel.chars().allMatch(Character::isDigit)) {
            System.out.println("pesel ok");
            if (imie.length() > 0 && imie.chars().allMatch(Character::isLetter)) {
                System.out.println("imie ok");
                if (nazwisko.length() > 0 && nazwisko.chars().allMatch(Character::isLetter)) {
                    System.out.println("nazwisko ok");
                    if (isValid(data)) {
                        System.out.println("data ok");
                        if(poziom.equals("Senior") || poziom.equals("Młodzik") || poziom.equals("Junior")){
                            System.out.println("wszystko ok");
                            return true;
                        }else errorMSG.setText("Błedne dane poziom!");
                    } else errorMSG.setText("Niepoprawny format daty!");
                } else errorMSG.setText("Nazwisko nie może zawierać cyfr ");
            } else errorMSG.setText("Imię nie może zawierać cyfr ");
        } else errorMSG.setText("Niepoprawny pesel!");
        return false;
    }

    //czy podana data jest zgodna z formatem bazy danych

    public boolean isValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    //funkcja zmieniajaca okno

    @FXML
    public void changeView(String nazwa) throws IOException {
        System.out.println("zmieniam scene");
        FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource(nazwa));
        Parent root1=fxmlLoader.load();
        Stage stage=new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }


}
