/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	try {
    		
    		int year = boxAnno.getValue();
    		this.model.creaGrafo(year);
    		txtResult.setText("Grafo creato!\nNumero vertici: " + model.getNumeroVertici());
    		txtResult.appendText("\nNumero archi: " + model.getNumeroArchi() + "\n");
    		boxRegista.getItems().clear();
    		boxRegista.getItems().addAll(this.model.getDirettori());
    		
    	} catch (NullPointerException e) {
    		txtResult.appendText("Selezionare un anno prima di proseguire.\n");
    		return;
    	}

    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	try {
    		Director director = boxRegista.getValue();
    		List<Adiacenza> result = new ArrayList<>(this.model.registiAdiacenti(director));
    		txtResult.appendText("Registi adiacenti a: " + director.toString() + ":\n");
    		for(Adiacenza a : result)
    			txtResult.appendText(a.toString() + "\n");
    	} catch (NullPointerException e) {
    		txtResult.appendText("Creare grafo e selezionare regista prima di proseguire.\n");
    	}

    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	try {
    		Director director = boxRegista.getValue();
    		int soglia = Integer.parseInt(txtAttoriCondivisi.getText());
    		List<Director> result = new ArrayList<>(this.model.cercaRegistiAffini(director, soglia));
    		txtResult.appendText("Trovata sequenza di lunghezza " + result.size() + " e peso " + this.model.getSommaMigliore() + " :\n");
    		for(Director d : result){
    			txtResult.appendText(d.toString() + "\n");
    		}
    	} catch (NullPointerException e) {
    		txtResult.appendText("Creare grafo e selezionare regista prima di proseguire.\n");
    	} catch (NumberFormatException e) {
    		txtResult.appendText("Inserire un numero intero come soglia prima di proseguire");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	boxAnno.getItems().add(2004);
    	boxAnno.getItems().add(2005);
    	boxAnno.getItems().add(2006);
    	
    }
    
}
