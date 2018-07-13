package personasapp.components.people;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import personasapp.datasource.PersonasDataSource;
import personasapp.model.Persona;

import java.net.URL;
import java.util.ResourceBundle;

public class PersonaController implements Initializable {
    //mapear los controles de la vista
    @FXML
    TextField nombreText, apellidosText;
    @FXML
    DatePicker nacimientoDtp;
    @FXML
    RadioButton hombreRadio, mujerRadio;
    @FXML
    Button guardarBtn, cancelarBtn;

    //la vista estará asociada a un objeto Persona
    private Persona persona = new Persona();

    public void setPersona(Persona persona) {
        this.persona = persona;
        // mostrar los datos en los controles
        MostrarDatos();
    }

    private void MostrarDatos() {
        nombreText.setText(persona.getNombre());
        apellidosText.setText(persona.getApellidos());
        nacimientoDtp.setValue(persona.getFechaNacimiento());
        hombreRadio.setSelected(persona.getSexo().equals("H"));
        mujerRadio.setSelected(persona.getSexo().equals("M"));
    }

    private void GuardarDatos() {
        //Tomar los datos de los controles
        //ponerlos en persona
        //mandar a agregar a ListaPersonas
        persona.setNombre(nombreText.getText());
        persona.setApellidos(apellidosText.getText());
        if(hombreRadio.isSelected()){
            persona.setSexo("H");
        }
        if(mujerRadio.isSelected()){
            persona.setSexo("M");
        }
        persona.setFechaNacimiento(nacimientoDtp.getValue());

        //mandar a agregar la persona a la lista
        PersonasDataSource.GuardarPersona(persona);
        //cerrar la ventana
        Stage window = (Stage) guardarBtn.getScene().getWindow();
        window.close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.guardarBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GuardarDatos();
            }
        });
    }
}
