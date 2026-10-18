/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.IRegistroCitaController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import modelo.RegistroCitaModelo;
import modelo.entidades.Cita;
import modelo.entidades.HorarioConsulta;
import modelo.entidades.Medico;
import modelo.entidades.Paciente;

/**
 *
 * @author jalt2
 */
public class VistaRegistroCita implements PropertyChangeListener{
    
    private final RegistroCitaModelo modelo;
    private final IRegistroCitaController acciones;
    private final Scanner entrada;
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final Locale idioma = new Locale("es", "MX");
    
    public VistaRegistroCita(RegistroCitaModelo modelo, IRegistroCitaController acciones, Scanner entrada) {
        this.modelo = modelo;
        this.acciones = acciones;
        this.entrada = entrada;
    }
    
    public void ejecutar(){
        System.out.println("=== REGISTRAR CITA MEDICA ===");
        System.out.println("NSS de prueba: 00123456789 / 00987654321");
        boolean continuar;

        do {
            System.out.print("\nIngrese el NSS: ");
            acciones.buscarPaciente(entrada.nextLine());

            List<Medico> medicos = modelo.getMedicosDisponibles();
            int indiceMedico = leerIndice("Seleccione un medico: ");
            acciones.seleccionarMedico(medicos.get(indiceMedico));

            List<LocalDate> fechas = modelo.getFechasDisponibles();
            int indiceFecha = leerIndice("Seleccione una fecha: ");
            acciones.seleccionarFecha(fechas.get(indiceFecha));

            List<LocalTime> horas = modelo.getHorasDisponibles();
            int indiceHora = leerIndice("Seleccione una hora: ");
            acciones.seleccionarHora(horas.get(indiceHora));

            System.out.print("Presione Enter para apartar la cita: ");
            entrada.nextLine();
            acciones.apartarCita();

            System.out.print("\nRegistrar otra cita? (s/n): ");
            continuar = entrada.nextLine().equalsIgnoreCase("s");
            if (continuar) {
                acciones.reiniciar();
            }
        } while (continuar);

        System.out.println("Fin de la ejecucion.");
    }
    
    private int leerIndice(String mensaje) {
        System.out.print(mensaje);
        return Integer.parseInt(entrada.nextLine()) - 1;
    }
    
    
    @Override
    public void propertyChange(PropertyChangeEvent evento) {
        // Observer solo representa datos; no pide entrada ni ejecuta acciones.
        switch (evento.getPropertyName()) {
            case "pacienteActual":
                mostrarPacienteYMedicos();
                break;
            case "medicoActual":
                mostrarDetalleMedico();
                break;
            case "fechaSeleccionada":
                mostrarHoras();
                break;
            case "horaSeleccionada":
                System.out.println("Horario elegido: "
                        + modelo.getFechaSeleccionada().format(formatoFecha)
                        + " a las " + modelo.getHoraSeleccionada());
                break;
            case "citaRegistrada":
                mostrarDatosCita();
                break;
            default:
                break;
        }
    }
    
    private void mostrarPacienteYMedicos() {
        Paciente paciente = modelo.getPacienteActual();
        System.out.println("\nPaciente: " + paciente.getNombreCompleto());
        System.out.println("NSS: " + paciente.getNss());
        System.out.println("\nMEDICOS DISPONIBLES");

        List<Medico> medicos = modelo.getMedicosDisponibles();
        for (int i = 0; i < medicos.size(); i++) {
            Medico medico = medicos.get(i);
            System.out.println((i + 1) + ". " + medico.getNombreCompleto()
                    + " - " + medico.getEspecialidad());
        }
    }
    
    private void mostrarDetalleMedico() {
        Medico medico = modelo.getMedicoActual();
        System.out.println("\nDATOS DEL MEDICO");
        System.out.println("Nombre: " + medico.getNombreCompleto());
        System.out.println("Especialidad: " + medico.getEspecialidad());
        System.out.println("Consultorio: " + medico.getConsultorio());
        System.out.println("Horarios de consulta:");

        for (HorarioConsulta horario : medico.getHorarios()) {
            String dia = horario.getDiaSemana().getDisplayName(
                    TextStyle.FULL, idioma);
            System.out.println("  " + dia + ": " + horario.getHoraInicio()
                    + " a " + horario.getHoraFin());
        }

        System.out.println("\nFECHAS DISPONIBLES");
        List<LocalDate> fechas = modelo.getFechasDisponibles();
        for (int i = 0; i < fechas.size(); i++) {
            System.out.println((i + 1) + ". "
                    + fechas.get(i).format(formatoFecha));
        }
    }
    
    private void mostrarHoras() {
        System.out.println("\nHORAS DISPONIBLES");
        List<LocalTime> horas = modelo.getHorasDisponibles();
        for (int i = 0; i < horas.size(); i++) {
            System.out.println((i + 1) + ". " + horas.get(i));
        }
    }

    private void mostrarDatosCita() {
        Cita cita = modelo.getCitaRegistrada();
        System.out.println("\n=== CITA REGISTRADA ===");
        System.out.println("Paciente: " + cita.getPaciente().getNombreCompleto());
        System.out.println("NSS: " + cita.getPaciente().getNss());
        System.out.println("Medico: " + cita.getMedico().getNombreCompleto());
        System.out.println("Consultorio: " + cita.getMedico().getConsultorio());
        System.out.println("Fecha: " + cita.getFecha().format(formatoFecha));
        System.out.println("Hora: " + cita.getHora());
    }
}
