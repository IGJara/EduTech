package com.edutech.soporte_servicio.model;

public enum EstadoTicket {
    ABIERTO,      // El ticket ha sido creado y está pendiente de revisión
    EN_PROGRESO,  // Un agente está trabajando en el ticket
    RESUELTO,     // El problema ha sido solucionado
    CERRADO,      // El ticket ha sido cerrado (puede ser después de resuelto y confirmado por el usuario)
    PENDIENTE     // El ticket está esperando información del usuario o de un tercero
}
