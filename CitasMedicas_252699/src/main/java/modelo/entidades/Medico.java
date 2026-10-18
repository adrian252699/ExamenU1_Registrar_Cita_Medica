/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.entidades;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jalt2
 */
public class Medico {
    private Long id;
    private String nombreCompleto;
    private String especialidad;
    private String consultorio;
    private List<HorarioConsulta> horarios = new ArrayList<>();

    public Medico() {
    }

    public Medico(Long id, String nombreCompleto,String especialidad, String consultorio) {

        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.especialidad = especialidad;
        this.consultorio = consultorio;
    }

    public Medico(Long id, String nombreCompleto, String especialidad, String consultorio, List<HorarioConsulta> horarios) {
        this(id, nombreCompleto, especialidad, consultorio);
        this.horarios = new ArrayList<>(horarios);
    }
    
    //Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(String consultorio) {
        this.consultorio = consultorio;
    }

    public List<HorarioConsulta> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioConsulta> horarios) {
        this.horarios = horarios;
    }
    
    
    public boolean atiendeHorario(LocalDate fecha, LocalTime hora) {
        for (HorarioConsulta horario : horarios) {
            if (horario.existeHorario(fecha, hora)) {
                return true;
            }
        }

        return false;
    }
    
}
