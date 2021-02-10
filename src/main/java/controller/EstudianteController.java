/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import gestion.EstudianteGestion;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import model.Estudiante;

/**
 *
 * @author User
 */
@Named(value = "estudianteController")
@SessionScoped
public class EstudianteController extends Estudiante implements Serializable {

    /**
     * Creates a new instance of EstudianteController
     */
    public EstudianteController() {
    }

    //Metodo encargado de traer todos los estudiantes
    public List<Estudiante> getEstudiantes() {
        return EstudianteGestion.getEstudiantes();
    }

    //Metodo encargado de mandar el parametro para la busqueda de un estudiante y 
    //mostrarlo en edita.xhtml
    public String editaEstudiante(int id, String idEstudiante) {
        Estudiante elEstudiante = EstudianteGestion.getEstudiante(id, idEstudiante);
        if (elEstudiante != null) {
            this.setId(elEstudiante.getId());
            this.setIdEstudiante(elEstudiante.getIdEstudiante());
            this.setNombre(elEstudiante.getNombre());
            this.setApellido1(elEstudiante.getApellido1());
            this.setApellido2(elEstudiante.getApellido2());
            this.setFechaNaci(elEstudiante.getFechaNaci());
            this.setFechaIngr(elEstudiante.getFechaIngr());
            this.setGenero(elEstudiante.getGenero());
            return "edita.xhtml";
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Posiblemente el registro no exista");
            FacesContext.getCurrentInstance().addMessage("editaEstudianteForm:identificacion", msg);
            return "list.xhtml";
        }

    }

}
