/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.IRegistroCitaController;
import controlador.RegistroCitaController;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import modelo.RegistroCitaModelo;
import modelo.entidades.HorarioConsulta;
import modelo.entidades.Medico;
import modelo.entidades.Paciente;
import modelo.repositorios.IRepositorioAgenda;
import modelo.repositorios.RepositorioAgenda;

/**
 *
 * @author jalt2
 */
public class EjecucionConsola {
    public static void main(String[] args) {
        List<Paciente> pacientes = Arrays.asList(
                new Paciente(1L,"00123456789", "Ana Lopez Garcia"),
                new Paciente(1L,"00987654321", "Carlos Perez Torres")
        );

        Medico luis = new Medico(1L, "Luis Hernandez Ruiz","Medicina general", "101");
        luis.setHorarios(Arrays.asList(
                new HorarioConsulta(1L, DayOfWeek.MONDAY,
                        LocalTime.of(9, 0), LocalTime.of(12, 0)),
                new HorarioConsulta(2L, DayOfWeek.WEDNESDAY,
                        LocalTime.of(9, 0), LocalTime.of(12, 0))
        ));

        Medico elena = new Medico(2L, "Elena Martinez Soto",
                "Cardiologia", "202");
        elena.setHorarios(Arrays.asList(
                new HorarioConsulta(3L, DayOfWeek.TUESDAY,
                        LocalTime.of(10, 0), LocalTime.of(13, 0)),
                new HorarioConsulta(4L, DayOfWeek.THURSDAY,
                        LocalTime.of(10, 0), LocalTime.of(13, 0))
        ));

        List<Medico> medicos = Arrays.asList(luis, elena);

        // Configuracion de prueba: dos semanas y turnos de treinta minutos.
        LocalDate inicio = LocalDate.now().with(
                TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));

        IRepositorioAgenda repositorio = new RepositorioAgenda(
                pacientes, medicos, inicio, inicio.plusDays(13), 30);

        RegistroCitaModelo modelo = new RegistroCitaModelo(repositorio);
        IRegistroCitaController controlador = new RegistroCitaController(modelo);

        try (Scanner entrada = new Scanner(System.in)) {
            VistaRegistroCita vista = new VistaRegistroCita(
                    modelo, controlador, entrada);

            modelo.agregarObservador(vista);
            vista.ejecutar();
            modelo.quitarObservador(vista);
        }
    }
}
