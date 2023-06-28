/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Model;
import it.polito.tdp.yelp.model.User;
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

    @FXML // fx:id="btnUtenteSimile"
    private Button btnUtenteSimile; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbAnno"
    private ComboBox<Integer> cmbAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="cmbUtente"
    private ComboBox<User> cmbUtente; // Value injected by FXMLLoader

    @FXML // fx:id="txtX1"
    private TextField txtX1; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	if(txtN.getText() == "") {
    		txtResult.setText("Inserire N");
    		return;
    	}else {
    		try {
    			int n = Integer.parseInt(txtN.getText());
    			if(cmbAnno.getValue() == null) {
    				txtResult.setText("Scegliere un anno");
    				return;
    			}else {
    				model.CreaGrafo(n, cmbAnno.getValue());
    				cmbUtente.getItems().addAll(model.getVertexSet());
    			}
    	}catch(NumberFormatException e) {
    		txtResult.setText("N inserito in modo scorretto");
    	}
    	}
    }

    @FXML
    void doUtenteSimile(ActionEvent event) {
    	if(cmbUtente.getValue() == null) {
    		txtResult.setText("Selezionare un utente prima");
    		return;
    	}else {
    		List<User> user = model.getSimilarita(cmbUtente.getValue());
    		String s = "";
    		for(User u: user) {
    			s += "\n" + u.getName();
    		}
    		txtResult.setText(s);
    	}
    }
    
    @FXML
    void doSimula(ActionEvent event) {
    	if(txtX1.getText() == "" || txtX2.getText() == "") {
    		txtResult.appendText("Inserire x1 e x2 correttamente");
    	}
    	try {
			int n = Integer.parseInt(txtX1.getText());
			int n2 = Integer.parseInt(txtX2.getText());
			String s = model.simulazione(n, n2);
			txtResult.appendText(s);
    	}catch(NumberFormatException e) {
    		txtResult.setText("X1 o X2 inseriti in modo scorretto");
    	}
    }
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUtenteSimile != null : "fx:id=\"btnUtenteSimile\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbUtente != null : "fx:id=\"cmbUtente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX1 != null : "fx:id=\"txtX1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbAnno.getItems().addAll(2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013);
    }
}
