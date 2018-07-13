package personasapp;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import personasapp.components.people.PersonaController;
import personasapp.datasource.PersonasDataSource;
import personasapp.model.Persona;

import java.io.IOException;

public class MainController {

    //declarar controles
    @FXML
    Button agregarBtn;
    //faltarán la tabla y columnas
    @FXML
    TableView<Persona> personasTable;
    @FXML
    TableColumn<Persona, String> nombreCol, apellidosCol, sexoCol, fechaNacimientoCol;


    //en cada controlador vamos a poner
    //el método  initialize()

    //initialize() se ejecuta cuando
    //la interfaz termina de cargarse
    public void initialize(){

        //asociar las columnas a los atributos
        nombreCol.setCellValueFactory(
                new PropertyValueFactory<Persona, String>("nombre")
        );

        apellidosCol.setCellValueFactory(
                new PropertyValueFactory<Persona, String>("apellidos")
        );

        fechaNacimientoCol.setCellValueFactory(
                new PropertyValueFactory<Persona, String>("fechaNacimiento")
        );

        sexoCol.setCellValueFactory(
                new PropertyValueFactory<Persona, String>("sexo")
        );

        //Cargar la lista de personas en la tabla
        CargarDatos();


        //evento clic al botón agregarBtn
        this.agregarBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //System.out.println("Hola desde el botón");

                //abrir la otra personaForm
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(
                            PersonaController.class.getResource(
                                    "personaForm.fxml"
                            )
                    );
                    Parent root = (Parent) fxmlLoader.load();
                    Stage stage =new Stage();
                    stage.setScene(new Scene(root));
                    stage.showAndWait();

                    //Recargar Datos
                    CargarDatos();


                }catch (IOException ex){
                    System.out.println("No se pudo cargar el form");
                }
            }
        });

        //evento doble click sobre la tabla
        this.personasTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //identificar el doble click
                if(event.getClickCount() == 2){
                    //abrir la ventana
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(
                                PersonaController.class.getResource(
                                        "personaForm.fxml"
                                )
                        );
                        Parent root = (Parent) fxmlLoader.load();
                        Stage stage =new Stage();
                        stage.setScene(new Scene(root));

                        //pasar el objeto seleccionado
                        Persona selecionado = personasTable.getSelectionModel().getSelectedItem();
                        // recuperar el controlador de Persona
                        PersonaController controller = fxmlLoader.getController();
                        controller.setPersona(selecionado);

                        stage.showAndWait();

                        //actualizar el registro en la tabla
                        int index = personasTable.getItems().indexOf(selecionado);
                        personasTable.getItems().set(index, selecionado);


                    }catch (IOException ex){
                        System.out.println("No se pudo cargar el form");
                    }
                }
            }
        });
    }

    private void CargarDatos() {
        //mandar a llamar la ListaPersonas
        personasTable.setItems(FXCollections.observableList(
                PersonasDataSource.ListaPersonas()
        ));
    }


}