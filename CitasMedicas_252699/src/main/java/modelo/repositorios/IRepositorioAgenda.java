/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package modelo.repositorios;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import modelo.entidades.Cita;
import modelo.entidades.Medico;
import modelo.entidades.Paciente;

/**
 *
 * @author jalt2
 */
public interface IRepositorioAgenda {
    Paciente buscarPorNss(String nss);
    List<Medico> listarMedicos();
    List<LocalDate>listarFechasDisponibles(Medico medico);
    List<LocalTime>listarHorasDisponibles(Medico medico,LocalDate fecha);
    List<LocalTime>listarHorasOcupadas(Medico medico,LocalDate fecha);
    boolean guardarSiDisponible(Cita cita);
}
