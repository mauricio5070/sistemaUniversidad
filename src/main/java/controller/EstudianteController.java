/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import gestion.EstudianteGestion;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import model.Estudiante;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private boolean noImprimir = true;

    public boolean isNoImprimir() {
        return noImprimir;
    }

    public void setNoImprimir(boolean noImprimir) {
        this.noImprimir = noImprimir;
    }

    //Metodo encargado de mandar el parametro para la busqueda de un estudiante y 
    //mostrarlo para el reporte
    public String buscarEstudiante(String idEstudiante) {
        Estudiante elEstudiante = EstudianteGestion.getEstudianteCertifica(idEstudiante);
        if (elEstudiante != null) {
            this.setId(elEstudiante.getId());
            this.setIdEstudiante(elEstudiante.getIdEstudiante());
            this.setNombre(elEstudiante.getNombre());
            this.setApellido1(elEstudiante.getApellido1());
            this.setApellido2(elEstudiante.getApellido2());
            this.setFechaNaci(elEstudiante.getFechaNaci());
            this.setFechaIngr(elEstudiante.getFechaIngr());
            this.setGenero(elEstudiante.getGenero());
            noImprimir = false;
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Posiblemente el registro no exista");
            FacesContext.getCurrentInstance().addMessage("certificacionEstudianteForm:identificacion", msg);
            noImprimir = true;
        }
        return "certificacion.xhtml";
    }

    public String insertEstudiante() {
        if (EstudianteGestion.insertEstudiante(this)) {
            return "list.xhtml";
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Ocurrio un error al insertar un nuevo estudiante");
            FacesContext.getCurrentInstance().addMessage("editaEstudianteForm:identificacion", msg);
            return "edita.xhtml";

        }
    }

    public String updateEstudiante() {
        if (EstudianteGestion.updateEstudiante(this)) {
            return "list.xhtml";
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Ocurrio un error al modificar el estudiante");
            FacesContext.getCurrentInstance().addMessage("editaEstudianteForm:identificacion", msg);
            return "edita.xhtml";

        }
    }

    public String deleteEstudiante() {
        if (EstudianteGestion.deleteEstudiante(this)) {
            return "list.xhtml";
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Ocurrio un error al eliminar el estudiante");
            FacesContext.getCurrentInstance().addMessage("editaEstudianteForm:identificacion", msg);
            return "edita.xhtml";

        }
    }

    public void respaldo() {
        ZipOutputStream out = null;
        try {
            String json = EstudianteGestion.generarJson();
            File f = new File(FacesContext.getCurrentInstance().
                    getExternalContext().getRealPath("/respaldo") + "estudiantes.zip");
            out = new ZipOutputStream(new FileOutputStream(f));
            ZipEntry e = new ZipEntry("estudiantes.json");
            out.putNextEntry(e);
            byte[] data = json.getBytes();
            out.write(data, 0, data.length);
            out.closeEntry();
            out.close();
            File zipPath = new File(FacesContext.getCurrentInstance().
                    getExternalContext().getRealPath("/respaldo") + "estudiantes.zip");
            byte[] zip = Files.readAllBytes(zipPath.toPath());

            HttpServletResponse respuesta = (HttpServletResponse) FacesContext.getCurrentInstance()
                    .getExternalContext().getResponse();
            respuesta.setContentType("application/pdf");
            respuesta.addHeader("Content-disposition", "attachment; filename=estudiantes.zip");
            ServletOutputStream flujo = respuesta.getOutputStream();
            flujo.write(zip);
            flujo.flush();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EstudianteController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EstudianteController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();

            } catch (IOException ex) {
                Logger.getLogger(EstudianteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
