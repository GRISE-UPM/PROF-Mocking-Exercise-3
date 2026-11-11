package es.grise.upm.profundizacion.mocking_ejercicio_1;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// Ejemplos de sintaxis de spy; separados de las pruebas del servicio.
class MockitoSpyTest {

    @Test
    @DisplayName("El spy ejecuta métodos reales salvo los configurados")
    void ejecutaMetodosRealesSalvoLosConfigurados() {
        List<String> lista = spy(new ArrayList<String>());
        lista.add("Dato real");
        assertEquals("Dato real", lista.get(0));

        doReturn("Dato configurado").when(lista).get(0);

        assertEquals("Dato configurado", lista.get(0));
        verify(lista).add("Dato real");
    }

    @Test
    @DisplayName("doReturn evita ejecutar el método real durante la preparación")
    void doReturnEvitaEjecutarElMetodoRealAlPrepararElSpy() {
        List<String> lista = spy(new ArrayList<String>());

        // when(lista.get(0)).thenReturn("Dato configurado") lanzaría una
        // IndexOutOfBoundsException al preparar el spy, porque la lista está vacía.
        doReturn("Dato configurado").when(lista).get(0);

        assertEquals("Dato configurado", lista.get(0));
    }
}
