/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import modelo.entidades.Cita;
import modelo.entidades.HorarioConsulta;
import modelo.entidades.Medico;
import modelo.entidades.Paciente;
import modelo.repositorios.IRepositorioAgenda;
import modelo.repositorios.RepositorioAgenda;

/**
 *
 * @author jalt2
 */
public class EjecucionConsolaPrueba {
//    public static void main(String[] args) {
//
//        // 1. Crear pacientes de prueba.
//        Paciente ana = new Paciente(
//                1L,
//                "00123456789",
//                "Ana Lopez Garcia"
//        );
//
//        Paciente carlos = new Paciente(
//                2L,
//                "00987654321",
//                "Carlos Pérez Torres"
//        );
//
//        List<Paciente> pacientes = Arrays.asList(ana, carlos);
//
//        // 2. Crear horarios de consulta.
//        HorarioConsulta horarioLuis = new HorarioConsulta(
//                1L,
//                DayOfWeek.MONDAY,
//                LocalTime.of(9, 0),
//                LocalTime.of(11, 0)
//        );
//
//        HorarioConsulta horarioElena = new HorarioConsulta(
//                2L,
//                DayOfWeek.TUESDAY,
//                LocalTime.of(10, 0),
//                LocalTime.of(12, 0)
//        );
//
//        // 3. Crear médicos y asignarles sus horarios.
//        Medico luis = new Medico(
//                1L,
//                "Luis Hernandez Ruiz",
//                "Medicina general",
//                "101"
//        );
//
//        luis.setHorarios(Arrays.asList(horarioLuis));
//
//        Medico elena = new Medico(
//                2L,
//                "Elena Martinez Soto",
//                "Cardiologia",
//                "202"
//        );
//
//        elena.setHorarios(Arrays.asList(horarioElena));
//
//        List<Medico> medicos = Arrays.asList(luis, elena);
//
//        // 4. Crear una sola instancia del almacenamiento.
//        // El 7 de enero de 2030 es lunes.
//        LocalDate fechaInicio = LocalDate.of(2030, 1, 7);
//        LocalDate fechaFin = LocalDate.of(2030, 1, 13);
//
//        IRepositorioAgenda repositorio = new RepositorioAgenda(
//                pacientes,
//                medicos,
//                fechaInicio,
//                fechaFin,
//                30 // Duración de prueba: 30 minutos por cita.
//        );
//
//        // 5. Buscar un paciente existente.
//        Paciente paciente = repositorio.buscarPorNss("00123456789");
//
//        System.out.println(
//                "Paciente: " + paciente.getNombreCompleto()
//        );
//
//        // 6. Consultar los médicos.
//        System.out.println("\nMedicos:");
//
//        for (Medico medico : repositorio.listarMedicos()) {
//            System.out.println(
//                    medico.getNombreCompleto()
//                    + " - " + medico.getEspecialidad()
//            );
//        }
//
//        // 7. Consultar fechas y horas disponibles de Luis.
//        LocalDate fechaCita = LocalDate.of(2030, 1, 7);
//
//        System.out.println(
//                "\nFechas de Luis: "
//                + repositorio.listarFechasDisponibles(luis)
//        );
//
//        System.out.println(
//                "Horas antes de reservar: "
//                + repositorio.listarHorasDisponibles(luis, fechaCita)
//        );
//
//        // 8. Crear y guardar una cita.
//        Cita cita = new Cita(
//                paciente,
//                luis,
//                fechaCita,
//                LocalTime.of(9, 30)
//        );
//
//        boolean guardada = repositorio.guardarSiDisponible(cita);
//
//        System.out.println("\nCita guardada: " + guardada);
//
//        // 9. Consultar la misma instancia después de guardar.
//        System.out.println(
//                "Horas ocupadas: "
//                + repositorio.listarHorasOcupadas(luis, fechaCita)
//        );
//
//        System.out.println(
//                "Horas despues de reservar: "
//                + repositorio.listarHorasDisponibles(luis, fechaCita)
//        );
//    }
}
