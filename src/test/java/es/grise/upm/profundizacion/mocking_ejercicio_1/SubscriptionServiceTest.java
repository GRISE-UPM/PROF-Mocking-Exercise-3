package es.grise.upm.profundizacion.mocking_ejercicio_1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private Client cliente;

    @Mock
    private Client otroCliente;

    private SubscriptionService servicio;
    private Message mensaje;

    @BeforeEach
    void preparar() {
        servicio = new SubscriptionService();
        mensaje = new Aviso("Cambio de aula");
    }

    @Test
    @DisplayName("Rechaza un cliente nulo")
    void rechazaClienteNulo() {
        assertThrows(NullClientException.class,
                () -> servicio.addSubscriber(null));
    }

    @Test
    @DisplayName("Rechaza una suscripción duplicada")
    void rechazaClienteDuplicado() throws Exception {
        servicio.addSubscriber(cliente);

        assertThrows(ExistingClientException.class,
                () -> servicio.addSubscriber(cliente));
    }

    @Test
    @DisplayName("Rechaza dar de baja un cliente nulo")
    void rechazaBajaClienteNulo() {
        assertThrows(NullClientException.class,
                () -> servicio.removeSubscriber(null));
    }

    @Test
    @DisplayName("Rechaza dar de baja un cliente no suscrito")
    void rechazaBajaClienteNoSuscrito() {
        assertThrows(NonExistingClientException.class,
                () -> servicio.removeSubscriber(cliente));
    }

    @Test
    @DisplayName("Envía el mensaje a un cliente suscrito con correo")
    void enviaMensajeAClienteConCorreo() throws Exception {
        // Arrange: el servicio es real; el cliente es el doble.
        when(cliente.hasEmail()).thenReturn(true);
        servicio.addSubscriber(cliente);

        // Act: ejecutar el SUT.
        servicio.sendMessage(mensaje);

        // Assert: verificar un efecto relevante.
        verify(cliente).receiveMessage(mensaje);
    }

    @Test
    @DisplayName("No envía mensajes a un cliente sin correo")
    void noEnviaAClienteSinCorreo() throws Exception {
        when(cliente.hasEmail()).thenReturn(false);
        servicio.addSubscriber(cliente);

        servicio.sendMessage(mensaje);

        verify(cliente, never()).receiveMessage(any(Message.class));
    }

    @Test
    @DisplayName("Envía el mensaje a todos los clientes con correo")
    void enviaATodosLosClientesConCorreo() throws Exception {
        when(cliente.hasEmail()).thenReturn(true);
        when(otroCliente.hasEmail()).thenReturn(true);
        servicio.addSubscriber(cliente);
        servicio.addSubscriber(otroCliente);

        servicio.sendMessage(mensaje);

        verify(cliente).receiveMessage(mensaje);
        verify(otroCliente).receiveMessage(mensaje);
    }

    @Test
    @DisplayName("No contacta con un cliente que se ha dado de baja")
    void noContactaAClienteDadoDeBaja() throws Exception {
        // No se configura hasEmail: el cliente ya no debe consultarse.
        servicio.addSubscriber(cliente);
        servicio.removeSubscriber(cliente);

        servicio.sendMessage(mensaje);

        verifyNoInteractions(cliente);
    }

    @Test
    @DisplayName("Entrega una vez cada una de dos publicaciones")
    void enviaCadaPublicacionUnaVez() throws Exception {
        when(cliente.hasEmail()).thenReturn(true);
        servicio.addSubscriber(cliente);
        Message segundoMensaje = new Aviso("Nueva fecha de entrega");

        servicio.sendMessage(mensaje);
        servicio.sendMessage(segundoMensaje);

        verify(cliente).receiveMessage(mensaje);
        verify(cliente).receiveMessage(segundoMensaje);
        verify(cliente, times(2)).receiveMessage(any(Message.class));
    }

    @Test
    @DisplayName("Las respuestas consecutivas controlan los envíos")
    void respuestasConsecutivasControlanEnvios() throws Exception {
        when(cliente.hasEmail()).thenReturn(true, false, true);
        servicio.addSubscriber(cliente);
        Message segundo = new Aviso("Segundo aviso");
        Message tercero = new Aviso("Tercer aviso");
        Message cuarto = new Aviso("Cuarto aviso");

        servicio.sendMessage(mensaje);
        servicio.sendMessage(segundo);
        servicio.sendMessage(tercero);
        servicio.sendMessage(cuarto); // Se repite la última respuesta: true.

        verify(cliente).receiveMessage(mensaje);
        verify(cliente, never()).receiveMessage(segundo);
        verify(cliente).receiveMessage(tercero);
        verify(cliente).receiveMessage(cuarto);
    }

    @Test
    @DisplayName("Propaga un error al consultar si el cliente tiene correo")
    void propagaErrorAlConsultarCorreo() throws Exception {
        var fallo = new IllegalStateException("No se puede consultar el correo");
        when(cliente.hasEmail()).thenThrow(fallo);
        servicio.addSubscriber(cliente);

        IllegalStateException recibido = assertThrows(IllegalStateException.class,
                () -> servicio.sendMessage(mensaje));

        assertSame(fallo, recibido);
        verify(cliente, never()).receiveMessage(any(Message.class));
    }

    @Test
    @DisplayName("Propaga un error al entregar el mensaje")
    void propagaErrorAlEntregarMensaje() throws Exception {
        when(cliente.hasEmail()).thenReturn(true);
        servicio.addSubscriber(cliente);
        var fallo = new IllegalStateException("Fallo de entrega");
        doThrow(fallo).when(cliente).receiveMessage(any(Message.class));

        IllegalStateException recibido = assertThrows(IllegalStateException.class,
                () -> servicio.sendMessage(mensaje));

        assertSame(fallo, recibido);
    }

    @Test
    @DisplayName("Captura el contenido del mensaje enviado")
    void capturaContenidoMensajeEnviado() throws Exception {
        when(cliente.hasEmail()).thenReturn(true);
        servicio.addSubscriber(cliente);

        servicio.sendMessage(mensaje);

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(cliente).receiveMessage(captor.capture());
        Aviso enviado = assertInstanceOf(Aviso.class, captor.getValue());
        assertEquals("Cambio de aula", enviado.texto());
        assertSame(mensaje, enviado);
    }

    @Test
    @DisplayName("Demuestra la verificación del orden de dos publicaciones")
    void demuestraOrdenDeDosPublicaciones() throws Exception {
        when(cliente.hasEmail()).thenReturn(true);
        servicio.addSubscriber(cliente);
        Message segundoMensaje = new Aviso("Segundo aviso");

        servicio.sendMessage(mensaje);
        servicio.sendMessage(segundoMensaje);

        // Ejemplo opcional: usar InOrder cuando el orden sea relevante.
        InOrder orden = inOrder(cliente);
        orden.verify(cliente).receiveMessage(mensaje);
        orden.verify(cliente).receiveMessage(segundoMensaje);
    }

    // Dato real del test: no hace falta simular el contenido del mensaje.
    private record Aviso(String texto) implements Message {
    }
}
