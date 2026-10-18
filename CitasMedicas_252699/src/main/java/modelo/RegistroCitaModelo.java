/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import modelo.entidades.Cita;
import modelo.entidades.Medico;
import modelo.entidades.Paciente;
import modelo.repositorios.IRepositorioAgenda;

/**
 *
 * @author jalt2
 */
public class RegistroCitaModelo {
        
    //RepositorioAgenda
    private final IRepositorioAgenda repositorio;
    
    //Observadores
    private final List<PropertyChangeListener> observadores = new ArrayList<>();

    //NotificadorCambios

    //Atributos
    private EstadoRegistro estado = EstadoRegistro.INICIO;
    private Paciente pacienteActual;
    private Medico medicoActual;
    private LocalDate fechaSeleccionada;
    private LocalTime horaSeleccionada;
    private Cita citaRegistrada;
    
    
    
    // Indice privado de horas libres: medico -> fecha -> horas ofertadas libres.
    private Map<Long, Map<LocalDate, List<LocalTime>>> agendaDisponible =
            Collections.emptyMap();
    
    
    public RegistroCitaModelo(IRepositorioAgenda repositorio) {
        this.repositorio = repositorio;
    }
    
    //Listas
    private List<Medico> medicosDisponibles = Collections.emptyList();
    private List<LocalDate> fechasDisponibles = Collections.emptyList();
    private List<LocalTime> horasDisponibles = Collections.emptyList();
    
    //Implementar
    
    public void identificarPaciente(String nss){
        Paciente anterior = pacienteActual;
        pacienteActual = repositorio.buscarPorNss(nss);
        medicoActual = null;
        fechaSeleccionada = null;
        horaSeleccionada = null;
        citaRegistrada = null;

        actualizarDisponibilidad();
        estado = EstadoRegistro.PACIENTE_IDENTIFICADO;
        notificarCambios("pacienteActual", anterior, pacienteActual);
    }
    
    public void seleccionarMedico(Medico medico){
        Medico anterior = medicoActual;
        medicoActual = medico;
        fechaSeleccionada = null;
        horaSeleccionada = null;

        fechasDisponibles = calcularFechasDisponibles(medico);
        horasDisponibles = Collections.emptyList();
        estado = EstadoRegistro.MEDICO_SELECCIONADO;
        notificarCambios("medicoActual", anterior, medicoActual);
    }
    
    public void seleccionarFecha(LocalDate fecha){
        LocalDate anterior = fechaSeleccionada;
        fechaSeleccionada = fecha;
        horaSeleccionada = null;

        horasDisponibles = calcularHorasDisponibles(medicoActual, fecha);
        estado = EstadoRegistro.MEDICO_SELECCIONADO;
        notificarCambios("fechaSeleccionada", anterior, fechaSeleccionada);
    }
    
    public void seleccionarHora(LocalTime hora){
        LocalTime anterior = horaSeleccionada;
        horaSeleccionada = hora;
        estado = EstadoRegistro.HORARIO_SELECCIONADO;
        notificarCambios("horaSeleccionada", anterior, horaSeleccionada);
    }
    
    public void registrarCita(){
        Cita cita = new Cita(pacienteActual, medicoActual,
                fechaSeleccionada, horaSeleccionada);

        // Se conserva esta comprobacion para no confirmar una cita no guardada.
        if (!repositorio.guardarSiDisponible(cita)) {
            throw new IllegalStateException("La cita no fue registrada.");
        }

        citaRegistrada = cita;
        actualizarDisponibilidad();
        estado = EstadoRegistro.CITA_REGISTRADA;
        notificarCambios("citaRegistrada", null, citaRegistrada);
    }
    
    public void reiniciar(){
        EstadoRegistro anterior = estado;
        pacienteActual = null;
        medicoActual = null;
        fechaSeleccionada = null;
        horaSeleccionada = null;
        citaRegistrada = null;
        medicosDisponibles = Collections.emptyList();
        fechasDisponibles = Collections.emptyList();
        horasDisponibles = Collections.emptyList();
        estado = EstadoRegistro.INICIO;
        notificarCambios("estado", anterior, estado);
    }
    
    
    // La aplicacion registra cada observador una sola vez.
    public void agregarObservador(PropertyChangeListener oyente) {
        observadores.add(oyente);
    }

    public void quitarObservador(PropertyChangeListener oyente) {
        observadores.remove(oyente);
    }
    
    private void notificarCambios(String propiedad, Object anterior, Object nuevo) {
        PropertyChangeEvent evento = new PropertyChangeEvent(this, propiedad, anterior, nuevo);
        for (PropertyChangeListener oyente : new ArrayList<>(observadores)) {
            oyente.propertyChange(evento);
        }
    }
    
    /** Actualiza los datos necesarios para mostrar y registrar una cita disponible. */
    private void actualizarDisponibilidad() {
        List<Medico> disponibles = new ArrayList<>();
        for (Medico medico : repositorio.listarMedicos()) {
            if (!calcularFechasDisponibles(medico).isEmpty()) {
                disponibles.add(medico);
            }
        }
        medicosDisponibles = listaSoloLectura(disponibles);

        // Estos casos forman parte del flujo: antes de seleccionar medico o
        // fecha, los combos correspondientes todavia no tienen opciones.
        fechasDisponibles = medicoActual == null ? Collections.emptyList()
                : calcularFechasDisponibles(medicoActual);
        horasDisponibles = medicoActual == null || fechaSeleccionada == null
                ? Collections.emptyList()
                : calcularHorasDisponibles(medicoActual, fechaSeleccionada);
    }
    
    private List<LocalDate> calcularFechasDisponibles(Medico medico) {
        List<LocalDate> fechas = new ArrayList<>();
        for (LocalDate fecha : new TreeSet<>(repositorio.listarFechasDisponibles(medico))) {
            if (!calcularHorasDisponibles(medico, fecha).isEmpty()) {
                fechas.add(fecha);
            }
        }
        return listaSoloLectura(fechas);
    }
    
    private List<LocalTime> calcularHorasDisponibles(Medico medico, LocalDate fecha) {
        List<LocalTime> ocupadas = repositorio.listarHorasOcupadas(medico, fecha);
        List<LocalTime> horas = new ArrayList<>();
        for (LocalTime hora : new TreeSet<>(repositorio.listarHorasDisponibles(medico, fecha))) {
            if (medico.atiendeHorario(fecha, hora) && !ocupadas.contains(hora)) {
                horas.add(hora);
            }
        }
        return listaSoloLectura(horas);
    }
    
    private static <T> List<T> listaSoloLectura(List<T> datos) {
        return Collections.unmodifiableList(new ArrayList<>(datos));
    }
    
    
    //Getters and Setters

    public EstadoRegistro getEstado() {
        return estado;
    }

    public void setEstado(EstadoRegistro estado) {
        this.estado = estado;
    }

    public Paciente getPacienteActual() {
        return pacienteActual;
    }

    public void setPacienteActual(Paciente pacienteActual) {
        this.pacienteActual = pacienteActual;
    }

    public Medico getMedicoActual() {
        return medicoActual;
    }

    public void setMedicoActual(Medico medicoActual) {
        this.medicoActual = medicoActual;
    }

    public LocalDate getFechaSeleccionada() {
        return fechaSeleccionada;
    }

    public void setFechaSeleccionada(LocalDate fechaSeleccionada) {
        this.fechaSeleccionada = fechaSeleccionada;
    }

    public LocalTime getHoraSeleccionada() {
        return horaSeleccionada;
    }

    public void setHoraSeleccionada(LocalTime horaSeleccionada) {
        this.horaSeleccionada = horaSeleccionada;
    }

    public Cita getCitaRegistrada() {
        return citaRegistrada;
    }

    public void setCitaRegistrada(Cita citaRegistrada) {
        this.citaRegistrada = citaRegistrada;
    }

    public List<Medico> getMedicosDisponibles() {
        return repositorio.listarMedicos();
    }

    public List<LocalDate> getFechasDisponibles() {
        return repositorio.listarFechasDisponibles(medicoActual);
    }

    public List<LocalTime> getHorasDisponibles() {
        return repositorio.listarHorasDisponibles(medicoActual, fechaSeleccionada);
    }
    
    
}
