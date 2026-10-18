/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controlador;

import java.time.LocalDate;
import java.time.LocalTime;
import modelo.entidades.Medico;

/**
 *
 * @author jalt2
 */
public interface IRegistroCitaController {
    void buscarPaciente(String nss);
    void seleccionarMedico(Medico medico);
    void seleccionarFecha(LocalDate fecha);
    void seleccionarHora(LocalTime hora);
    void apartarCita();
    void reiniciar();
}
