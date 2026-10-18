/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import modelo.RegistroCitaModelo;
import modelo.entidades.Medico;

/**
 *
 * @author jalt2
 */
public class RegistroCitaController implements IRegistroCitaController{
    
    private final RegistroCitaModelo registroCitaModelo;
    
    //Duda
    public RegistroCitaController(RegistroCitaModelo registroCitaModelo) {
        this.registroCitaModelo = Objects.requireNonNull(
                registroCitaModelo,
                "El modelo de registro de citas es obligatorio"
        );
    }
    
    
    
    @Override
    public void buscarPaciente(String nss) {
        registroCitaModelo.identificarPaciente(nss);
    }

    @Override
    public void seleccionarMedico(Medico medico) {
        registroCitaModelo.seleccionarMedico(medico);
    }

    @Override
    public void seleccionarFecha(LocalDate fecha) {
        registroCitaModelo.seleccionarFecha(fecha);
    }

    @Override
    public void seleccionarHora(LocalTime hora) {
        registroCitaModelo.seleccionarHora(hora);
    }

    @Override
    public void apartarCita() {
        registroCitaModelo.registrarCita();
    }

    @Override
    public void reiniciar() {
        registroCitaModelo.reiniciar();
    }
    
}
