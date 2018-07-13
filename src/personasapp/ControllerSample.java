package personasapp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import personasapp.components.people.PersonaControllerSample;

public class ControllerSample {
    @FXML
    Button agregarBtn;

    //método que se ejecuta al cargarse la interfaz y el controlador
    public void initialize(){
        this.agregarBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                //load PersonaForm

                try {

                    FXMLLoader fxmlLoader = new FXMLLoader(PersonaControllerSample.class.getResource("personaForm.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Agregar nuevo Cliente");
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(((Node) t.getSource()).getScene().getWindow()); //set window parent
                    stage.showAndWait();

                    //access controller for get Sucursal item on action result is Ok
                    PersonaControllerSample ejecutivoController = fxmlLoader.getController();
                    /*if (ejecutivoController.getActionResult()) { //if say Save
                        //add or update tableview
                        setEjecutivosTableViewData();

                    }*/


                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage() + " stack: " + ex.getCause());
                }
            }
        });
    }
}
