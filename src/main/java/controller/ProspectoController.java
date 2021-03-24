/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import gestion.ProspectoGestion;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import model.Prospecto;

/**
 *
 * @author User
 */
@Named(value = "prospectoController")
@SessionScoped
public class ProspectoController extends Prospecto implements Serializable {

    private String tiraJson;

    /**
     * Creates a new instance of ProspectoController
     */
    public ProspectoController() {
    }

    //Metodo Encargado de Insertar Prospecto.
    public String insertaProspecto() {
        if (ProspectoGestion.insertarProspecto(this)) {
            return "confirmacion.xhtml";
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "Posiblemente el Registro se encuentre duplicado.");
            FacesContext.getCurrentInstance().addMessage("registroProspectoForm:cedula", msg);
            return "registro.xhtml";
        }

    }

    //Metodo encargado de mandar el parametro para la busqueda de un prospecto
    public void recupera(String idProspecto) {
        Prospecto elProspecto = ProspectoGestion.getProspecto(idProspecto);
        if (elProspecto != null) {
            this.setIdProspecto(elProspecto.getIdProspecto());
            this.setNombre(elProspecto.getNombre());
            this.setApellido1(elProspecto.getApellido1());
            this.setApellido2(elProspecto.getApellido2());
            this.setFechaNacimiento(elProspecto.getFechaNacimiento());
            this.setFechaGraduacionColegio(elProspecto.getFechaGraduacionColegio());
            this.setFechaPosibleIngreso(elProspecto.getFechaPosibleIngreso());
            this.setCorreo(elProspecto.getCorreo());
            this.setCelular(elProspecto.getCelular());
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Posiblemente el registro no exista");
            FacesContext.getCurrentInstance().addMessage("prospectoJsonForm:cedula", msg);

        }
    }

    public void creaJson() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fecha1 = format.format(this.getFechaNacimiento());
        JsonObjectBuilder creadorJson = Json.createObjectBuilder();
        JsonObject objetoJson = creadorJson.add("idProspecto", this.getIdProspecto())
                .add("nombre", this.getNombre())
                .add("apellido1", this.getApellido1())
                .add("apellido2", this.getApellido2())
                .add("fechaNacimiento", fecha1)
                .add("correo", this.getCorreo())
                .add("celular", this.getCelular())
                .build();
        StringWriter tira = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(tira);
        jsonWriter.writeObject(objetoJson);
        setTiraJson(tira.toString());
    }

    public void crearObjetoProspecto() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            JsonReader lectorJson = Json.createReader(new StringReader(tiraJson));
            JsonObject objetoJson = lectorJson.readObject();
             this.setIdProspecto(objetoJson.getString("idProspecto"));
            this.setNombre(objetoJson.getString("nombre"));
            this.setApellido1(objetoJson.getString("apellido1"));
            this.setApellido2(objetoJson.getString("apellido2"));
            this.setFechaNacimiento(format.parse(objetoJson.getString("fechaNacimiento")));          
            this.setCorreo(objetoJson.getString("correo"));
            this.setCelular(objetoJson.getString("celular"));
        } catch (ParseException ex) {
            Logger.getLogger(ProspectoController.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }

    public String getTiraJson() {
        return tiraJson;
    }

    public void setTiraJson(String tiraJson) {
        this.tiraJson = tiraJson;
    }

}
