/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.repositorios;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import modelo.entidades.Cita;
import modelo.entidades.HorarioConsulta;
import modelo.entidades.Medico;
import modelo.entidades.Paciente;

/**
 *
 * @author jalt2
 */
public class RepositorioAgenda implements IRepositorioAgenda{
    
    private final List<Paciente> pacientes;
    private final List<Medico> medicos;
    private final List<Cita> citas = new ArrayList<>();

    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final int duracionCitaMinutos;
    
    public RepositorioAgenda(List<Paciente> pacientes,List<Medico> medicos,LocalDate fechaInicio,LocalDate fechaFin,int duracionCitaMinutos) {
        this.pacientes = new ArrayList<>(pacientes);
        this.medicos = new ArrayList<>(medicos);
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.duracionCitaMinutos = duracionCitaMinutos;
    }
    
    @Override
    public Paciente buscarPorNss(String nss) {
        for (Paciente paciente : pacientes) {
            if (paciente.getNss().equals(nss)) {
                return paciente;
            }
        }

        return null;
    }

    @Override
    public List<Medico> listarMedicos() {
        return medicos;
    }

    @Override
    public List<LocalDate> listarFechasDisponibles(Medico medico) {
        List<LocalDate> fechas = new ArrayList<>();

        for (LocalDate fecha = fechaInicio;
                !fecha.isAfter(fechaFin);
                fecha = fecha.plusDays(1)) {

            if (!listarHorasDisponibles(medico, fecha).isEmpty()) {
                fechas.add(fecha);
            }
        }

        return fechas;
    }

    @Override
    public boolean guardarSiDisponible(Cita cita) {
        List<LocalTime> horasDisponibles = listarHorasDisponibles(
                cita.getMedico(),
                cita.getFecha()
        );

        // Solo se guarda un espacio ofertado y libre.
        if (!horasDisponibles.contains(cita.getHora())) {
            return false;
        }

        citas.add(cita);
        return true;
    }

    @Override
    public List<LocalTime> listarHorasDisponibles(Medico medico, LocalDate fecha) {
        // La oferta pertenece al rango configurado de la agenda.
        if (fecha.isBefore(fechaInicio) || fecha.isAfter(fechaFin)) {
            return Collections.emptyList();
        }

        TreeSet<LocalTime> horas = new TreeSet<>();

        for (HorarioConsulta horario : medico.getHorarios()) {
            if (horario.getDiaSemana() == fecha.getDayOfWeek()) {

                LocalDateTime turno = fecha.atTime(
                        horario.getHoraInicio()
                );

                LocalDateTime fin = fecha.atTime(
                        horario.getHoraFin()
                );

                // La cita completa debe caber en la franja.
                while (!turno.plusMinutes(duracionCitaMinutos).isAfter(fin)) {
                    horas.add(turno.toLocalTime());
                    turno = turno.plusMinutes(duracionCitaMinutos);
                }
            }
        }

        horas.removeAll(listarHorasOcupadas(medico, fecha));

        return Collections.unmodifiableList(
                new ArrayList<>(horas)
        );
    }

    @Override
    public List<LocalTime> listarHorasOcupadas(Medico medico, LocalDate fecha) {
        TreeSet<LocalTime> horas = new TreeSet<>();

        for (Cita cita : citas) {
            boolean mismoMedico = cita.getMedico().getId().equals(medico.getId());
            boolean mismaFecha = cita.getFecha().equals(fecha);

            if (mismoMedico && mismaFecha) {
                horas.add(cita.getHora());
            }
        }

        return Collections.unmodifiableList(
                new ArrayList<>(horas)
        );
    }
    
}
