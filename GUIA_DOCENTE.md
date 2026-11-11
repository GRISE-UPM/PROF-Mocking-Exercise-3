# Guía docente: revisión de Mocking y actualización del ejemplo

Preparada el 11 de noviembre de 2026 para una clase de 90 minutos de JUnit y Mockito, con demostraciones desarrolladas por la docente. Presentación revisada: **Mocking_2026.pptx**, 28 diapositivas. La numeración de esta guía incluye la portada.

Proyecto base: [GRISE-UPM/PROF-Mocking-Exercise-1](https://github.com/GRISE-UPM/PROF-Mocking-Exercise-1), rama `master`, commit `d861d495bf8678236763d7025044f9f419feccec`, de 11 de febrero de 2022. La revisión corresponde al contenido público consultado para esta preparación. El ZIP contiene una copia actualizada; no se ha modificado el repositorio remoto.

## 1. Recomendación para impartir la clase

Conviene intercalar las diapositivas y el código. Después de explicar el SUT, sus colaboradores y los dobles, abrir el proyecto. A partir de ahí, presentar una operación de Mockito, mostrarla en una prueba del servicio y ejecutar esa prueba. No reservar todo el ejemplo para después de las 28 diapositivas: se acumularían muchas variantes de sintaxis antes de ver para qué sirven.

El ejemplo elegido es adecuado: el servicio es pequeño, tiene reglas comprensibles y permite enseñar respuestas configuradas, llamadas verificadas, ausencia de llamadas y excepciones. Es **Java con Maven**, sin Spring Boot. Esto permite concentrar la demostración en el aislamiento de colaboradores, manteniendo las versiones de Java y de pruebas del ejemplo de Fibonacci.

No es necesario añadir Spring Boot, un servidor, un endpoint, una base de datos ni un envío real de correo. `SubscriptionService` es el objeto real bajo prueba; `Client` es el colaborador simulado. `Message` representa el dato entregado. La colección de suscriptores y los mensajes del test pueden ser reales.

### Distribución de los 90 minutos

| Minutos | Qué presentar | Qué mostrar o desarrollar |
|---|---|---|
| 0–18 | Repaso de JUnit con la presentación corregida | Fibonacci: un resultado correcto, una excepción y, si procede, un test parametrizado. |
| 18–30 | Mocking, diapositivas 2–10 seleccionadas | SUT, colaborador, aislamiento y tipos de dobles. Usar el servicio de suscripciones como ejemplo verbal. |
| 30–34 | Introducción a `when`, `thenReturn` y `verify`: 11 y 17 corregidas | Abrir `Client`, `Message` y `SubscriptionService`; localizar el `if` de `hasEmail()`. |
| 34–55 | Demostración principal; recurrir a 13, 22 y 25 cuando sean necesarias | Cliente con correo, sin correo y dado de baja. Ejecutar cada caso al terminarlo. |
| 55–65 | Excepciones, diapositiva 12 corregida | `thenThrow` y `assertThrows`; después `doThrow` en un método `void`. |
| 65–73 | Captura de argumentos, diapositiva 27 corregida | Capturar un `Message` y comprobar su contenido. Respuestas consecutivas de la 14 si queda tiempo. |
| 73–80 | `mock` y `spy`, diapositiva 18 corregida | Un ejemplo breve de `ArrayList` y `doReturn`. |
| 80–85 | Valores por defecto, strict stubbing y límites: 15 y 19 corregidas | Explicar por qué los stubs se preparan en el test que los necesita y qué comprueba `verify`. |
| 85–90 | Cierre y preguntas | Qué se ha probado, qué queda fuera de estas pruebas y cómo ejecutar el proyecto descargado. |

Si falta tiempo, omitir respuestas consecutivas, `InOrder` y el segundo ejemplo de spy. Mantener los tres casos de envío, la diferencia entre configurar y verificar, y al menos una excepción.

## 2. Revisión de las 28 diapositivas

### Diapositivas 1–10: fundamentos

| Diapositiva | Valoración | Cambio recomendado |
|---|---|---|
| **1. Portada** | Ajuste menor. | Sustituir «Junit» por «JUnit» o retirar esa línea si este archivo corresponde exclusivamente al bloque de Mockito. Título sugerido: «Mocking with Mockito». |
| **2. Types of tests** | La pirámide sirve como orientación. | Presentarla como una estrategia habitual de equilibrio entre alcance, coste y rapidez. No como una proporción obligatoria ni una clasificación universal. |
| **3. Types of tests** | Corregir varias afirmaciones absolutas. | E2E puede recorrer una interfaz pública, incluida una API; no exige siempre una GUI. Una prueba unitaria no tiene que simular todos sus colaboradores. En integración son reales las colaboraciones que se pretende evaluar; pueden existir dobles fuera de ese alcance. Eliminar «Formerly: test stubs»: un stub es un tipo de doble. |
| **4. Unit test** | Conservar el esquema del driver y el SUT. | Presentar Arrange–Act–Assert. Corregir las notas: una excepción inesperada, como un `NullPointerException`, puede revelar un defecto del código productivo o del propio test; no significa automáticamente un problema de infraestructura o falta de aislamiento. No usar «Failure ≠ Error» para deducir el origen del defecto. |
| **5. Driver, SUT & Collaborators** | El dibujo es útil; su conclusión es demasiado categórica. | Sustituir «This is integration testing» por «The test scope depends on which real collaborations we intend to verify». Tener una colección o un objeto de valor real no convierte automáticamente una prueba en integración. Quitar también la coletilla «you can complain…». |
| **6. Basic idea** | Corregir el mecanismo de sustitución. | No todos los dobles «extend a base class»: pueden implementar una interfaz y los frameworks actuales también utilizan instrumentación. Enfatizar que ofrecen el contrato que el SUT necesita y sustituyen al colaborador en la prueba. |
| **7. Advantages of test doubles** | Corregir una ventaja. | Cambiar «Gain access to hidden information» por «Observe interactions with collaborators». Un mock no permite acceder automáticamente a los atributos privados del SUT. Ajustar las notas que hablan de «ver dentro» del objeto. |
| **8. Types of test doubles** | Rehacer la definición de spy y matizar categorías. | Dummy: objeto que se pasa pero no se utiliza en ese escenario. Fake: implementación funcional simplificada. Stub: respuestas configuradas. Test spy: registra llamadas. Mock: permite contrastar las interacciones con expectativas. No presentar `null` como recomendación general. Distinguir el concepto de test spy de la API concreta `Mockito.spy()`. |
| **9. Mocks** | Simplificar. | Escribir jMock, Mockito y PowerMock correctamente. Retirar «the most popular» sin una fuente y centrar la clase en Mockito. La explicación Java/C++ es secundaria: Java permite generar o instrumentar dobles en ejecución; gMock utiliza normalmente declaraciones explícitas. No convertirlo en una regla universal de dificultad. |
| **10. Types of Unit Tests** | La distinción es útil, pero no son categorías excluyentes. | Título sugerido: «What do we verify?». Un mismo test puede comprobar un resultado y una interacción. Un doble configurado con `thenReturn` puede utilizarse después con `verify`. No asociar cada familia de dobles a un único tipo de test de forma rígida. |

**Texto propuesto para la 3**, en el idioma de las diapositivas:

- **Unit tests:** verify a small unit of behavior. Selected collaborators can be replaced with test doubles to control the scenario.
- **Integration tests:** verify selected real collaborations between components.
- **End-to-end tests:** verify a complete flow through the system's public interfaces, within defined boundaries.

**Texto propuesto para la 6:**

- A test double substitutes a collaborator during a test.
- It provides the contract required by the SUT.
- Its behavior is controlled to create the scenario under test.

**Frase oral para conectar con el ejemplo:** «En Fibonacci probábamos una lógica que no necesitaba colaboradores externos. Ahora el servicio toma decisiones usando un cliente. Queremos controlar lo que responde ese cliente y comprobar si el servicio le solicita la entrega del mensaje».

### Diapositivas 11–19: API, mocks y spies

| Diapositiva | Valoración | Cambio recomendado |
|---|---|---|
| **11. Return a value** | Hay un import de JUnit 4. | Cambiar `org.junit.Assert.*` por `org.junit.jupiter.api.Assertions.*`. Retirar el comentario rojo «PLEASE NOTICE…», que sigue presente en esta versión. Renombrar el bloque como «Stubbing: predefined return values». Una llamada directa al mock demuestra su configuración; no prueba lógica de la aplicación. |
| **12. Throw an exception** | La anotación no es válida en Jupiter. | Sustituir `@Test(expected = …)` por `@Test` y `assertThrows`. Conservar `thenThrow`: configura el doble; `assertThrows` comprueba el resultado al ejecutar el SUT. Retirar también el comentario rojo. Utilizar una excepción no comprobada si la firma del método no declara excepciones comprobadas. |
| **13. Arguments** | La idea es correcta; el nombre de los matchers no. | `any`, `anyString`, `anyInt`, etc. son matchers de Mockito. Si un argumento de una llamada usa un matcher, todos deben usarlo: por ejemplo, `eq(...)` para un valor fijo junto a `any(...)`. Retirar el comentario rojo. |
| **14. Several return values** | La secuencia y la repetición del último valor son correctas. | Reducir código y utilizar `when(cliente.hasEmail()).thenReturn(true, false, true)`. Aclarar que las respuestas avanzan cuando se invoca ese método del doble. El método productivo del servicio sigue siendo real. |
| **15. Nice vs. Strict** | Rehacer completa. | Mezcla respuestas por defecto, strict stubbing y la diferencia mock/spy. Mockito puede devolver `false`, `0`, `null` o valores vacíos para ciertos tipos no configurados; `String` normalmente devuelve `null`, no `""`. `MockitoExtension` usa `STRICT_STUBS` por defecto en la versión elegida. Eliminar que los ejemplos previos son spies o que se puede sustituir indistintamente `mock()` por `spy()`. |
| **16. Types of Unit Tests** | Repite la 10. | Eliminarla o usarla únicamente como transición breve. Aplicar los mismos matices que en la 10. |
| **17. Interaction-based testing** | La sintaxis principal es correcta. | Mantener como resumen. Cambiar el comentario de `verifyNoMoreInteractions` por «no unverified interactions remain». No recomendarlo al final de todos los tests; puede hacerlos frágiles ante cambios irrelevantes. |
| **18. Spies vs. Mock** | La diferencia inicial es válida; la motivación necesita corrección. | Un `mock` usa respuestas configuradas o por defecto. Un `spy` ejecuta métodos reales salvo los configurados. `verify` funciona con ambos. No motivar su uso por acceder a estructuras internas o probar métodos auxiliares privados. Añadir la precaución de `doReturn` con spies. |
| **19. Why Mockito doesn't mock private methods?** | Eliminar afirmaciones antiguas y cambiar enfoque. | No recomendar cambiar un método privado a visibilidad de paquete solo para simularlo. Probar el contrato público; si hay una responsabilidad independiente, valorar extraer un colaborador. Mockito moderno puede simular muchos métodos finales y estáticos: ya no es correcto decir que siempre hace falta PowerMock. Dejar este tema como criterio de diseño o material opcional. |

**Sustitución breve de la 11**, reutilizando el contexto de la clase:

```java
when(cliente.hasEmail()).thenReturn(true); // Preparar la respuesta.
servicio.addSubscriber(cliente);

servicio.sendMessage(mensaje);             // Ejecutar el SUT real.

verify(cliente).receiveMessage(mensaje);   // Comprobar un efecto.
```

Si se prefiere conservar una llamada directa al doble en la diapositiva, rotularla «Mockito API demonstration». Explicar que comprobar `assertTrue(cliente.hasEmail())` solo confirma la respuesta configurada y que la prueba del servicio aparece al ejecutar `servicio.sendMessage(...)`.

**Sustitución de la 12:**

```java
when(cliente.hasEmail())
    .thenThrow(new IllegalStateException("No se puede consultar el correo"));
servicio.addSubscriber(cliente);

assertThrows(IllegalStateException.class,
    () -> servicio.sendMessage(mensaje));
```

Guion: «`thenThrow` prepara el escenario: el cliente va a fallar. `assertThrows` comprueba cómo se comporta el servicio al encontrarse con ese fallo. Uno configura el colaborador y el otro expresa el resultado esperado del test».

**Sustitución propuesta de la 15: “Default answers and strict stubbing”**

- Unstubbed mock methods return default values.
- `MockitoExtension` enables strict stubbing by default.
- Strict stubbing helps detect unused stubs and certain argument mismatches.
- Use `verify(...)` to check the interactions required by the behavior under test.

En notas: «No configurar un método no significa que cualquier llamada deba hacer fallar la prueba. La política de respuestas por defecto y las comprobaciones de strict stubbing son asuntos distintos. Tampoco equivalen a usar un spy».

**Sustitución propuesta de la 18:**

```java
List<String> lista = spy(new ArrayList<String>());
lista.add("Dato real");                         // Método real.
assertEquals("Dato real", lista.get(0));

doReturn("Dato configurado").when(lista).get(0);
assertEquals("Dato configurado", lista.get(0));
verify(lista).add("Dato real");
```

Notas: «Con un spy, `when(lista.get(0))` puede ejecutar el `get(0)` real durante la preparación. Si la lista está vacía, falla antes de ejecutar el test que pretendíamos preparar. `doReturn(...).when(lista).get(0)` evita esa llamada real al configurar la respuesta».

### Diapositivas 20–28: verificaciones y cierre

| Diapositiva | Valoración | Cambio recomendado |
|---|---|---|
| **20. Called once** | La verificación es correcta. | Eliminar la implementación completa de `MyList`, repetida en siete diapositivas. Usar `Client` o una interfaz de colección directamente. `verify(cliente).receiveMessage(mensaje)` comprueba una llamada con ese argumento. |
| **21. Number of times** | Correcta. | Integrarla con la 20: `times(1)` es el valor por defecto. Si se quiere mostrar un conteo distinto, utilizar dos publicaciones y `times(2)`. |
| **22. No method called** | Método antiguo. | Sustituir `verifyZeroInteractions(mockedList)` por `verifyNoInteractions(mockedList)`. Ejemplo útil: cliente dado de baja, al que ya no se consulta ni se entrega el mensaje. |
| **23. No unexpected interactions** | Matizar qué comprueba. | «No unverified interactions remain». Primero se verifican las interacciones pertinentes. No usarlo automáticamente: puede acoplar el test a detalles no exigidos por el contrato. |
| **24. InOrder** | Sintaxis válida. | Dejar opcional. Comprobar orden solo cuando forme parte del comportamiento requerido. El orden del bucle de suscriptores no necesita convertirse en requisito de negocio para enseñar la API. |
| **25. Never** | Correcta. | `never()` equivale a `times(0)`. Aplicación natural: cliente sin correo. Se permite consultar `hasEmail()`, pero no se permite `receiveMessage(...)`. |
| **26. At least / at most** | Sintaxis válida. | Sustituir «uncertain number» por «allowed range». Un rango debe proceder del contrato; no se usa para aceptar un comportamiento que no entendemos. Dejar como consulta. |
| **27. ArgumentCaptor** | Corregir errores de compilación y API. | Para `ArgumentCaptor<String>`, usar `String.class`, no `List.class`; el método es `getAllValues()`, no `getValues()`; usar comillas rectas, eliminar el paréntesis extra y escribir esperado antes que obtenido en `assertEquals`. Mejor adaptar el ejemplo a `Message`. |
| **28. Cierre** | Ajuste de plantilla. | Retirar «Título de Proyecto». Cierre sugerido: «Control dependencies, execute the real SUT, verify relevant behavior». |

**Corrección mínima de la 27**, si se mantiene el ejemplo de lista:

```java
List<String> lista = mock();
lista.add("one");

ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
verify(lista).add(captor.capture());

assertEquals("one", captor.getValue());
// captor.getAllValues() devuelve todos los valores capturados.
```

Este ejemplo demuestra la API. En la clase conviene enseñar la variante del proyecto que ejecuta primero el servicio real. `mock()` con inferencia de tipo está disponible en la versión elegida; si se quiere una forma más explícita, utilizar una interfaz propia como `Client.class`.

### Diseño visual y carga de contenido

Las diapositivas 11–14 y 20–27 tienen mucho código pequeño. Retirar imports repetidos de los fragmentos y explicar una única vez los imports de Jupiter y Mockito. Reemplazar los marcadores `<class to mock>` por nombres reales del proyecto. Eliminar la implementación repetida de `MyList` libera espacio para aumentar la fuente.

Se pueden conservar las variantes completas como material de consulta. En directo bastan una introducción, un resumen de stubbing, un resumen de `verify`, las excepciones, mock/spy y el captor. Revisar también las **notas del presentador**: las de 3–8 contienen afirmaciones que no deberían sobrevivir a la corrección visual de las diapositivas.

## 3. Qué hay realmente en el repositorio base

El POM original usa Java 11, JUnit **4.13.1** y `mockito-all` **1.10.19**. La clase `SubscriptionServiceTest` está vacía. No hay una gran batería de pruebas antigua que migrar: el trabajo principal consiste en configurar el entorno y escribir la demostración.

| Elemento | Contenido | Decisión |
|---|---|---|
| `Client` | `hasEmail()` y `receiveMessage(Message)` | Conservar. Usar como doble de Mockito. |
| `Message` | Interfaz sin métodos | Conservar. Crear un dato real de test que la implemente. |
| `SubscriptionService` | Alta, baja y envío a clientes con correo | Conservar la lógica. Hacer privada y final la colección interna. |
| Excepciones | Nulo, duplicado y baja inexistente | Conservar sus contratos. |
| `SubscriptionServiceTest` | Clase vacía | Completar con Jupiter y Mockito. |
| POM | Dependencias de JUnit 4 y Mockito 1 | Sustituir. |

## 4. Actualización paso a paso

### Paso 1. Trabajar sobre una copia y abrir la raíz correcta

El ZIP preparado ya contiene la actualización. Para reproducirla sobre el proyecto original, descargarlo o clonarlo y crear una rama de trabajo. Abrir en VS Code la carpeta que contiene `pom.xml`, no únicamente `src`.

No hace falta convertir el proyecto a Spring Boot. No hay que levantar una aplicación antes de ejecutar sus pruebas.

### Paso 2. Elegir versiones coherentes con Fibonacci

| Componente | Original | Copia preparada |
|---|---|---|
| Java | 11 | **21** |
| JUnit | 4.13.1 | **JUnit Jupiter 6.0.3** |
| Mockito | `mockito-all` 1.10.19 | **mockito-core 5.20.0** |
| Integración Mockito/Jupiter | Ausente | **mockito-junit-jupiter 5.20.0** |
| Maven Compiler Plugin | Sin fijar | **3.14.1** |
| Maven Surefire Plugin | Sin fijar | **3.5.5** |
| Maven Dependency Plugin | Ausente | **3.8.1**, para preparar el agente |
| Maven Wrapper | Ausente | Incluido; usa **Maven 3.9.15** |

El proyecto de Fibonacci consultado usa Java 21 y Spring Boot 4.0.5; su gestión de dependencias fija Jupiter 6.0.3 y Mockito 5.20.0. Se usan aquí esas mismas versiones para mantener la coherencia entre ejemplos. **Mockito no exige pasar de JUnit 5 a JUnit 6**: la elección responde al otro proyecto. Los ejemplos de Jupiter con `org.junit.jupiter.api` mantienen la misma sintaxis que se venía preparando. En las diapositivas, «JUnit Jupiter» evita confundir el nombre de la API con una versión concreta.

### Paso 3. Sustituir el POM

Eliminar las dependencias `junit:junit` y `org.mockito:mockito-all`. Incorporar `junit-jupiter`, `mockito-core` y `mockito-junit-jupiter`, todas con alcance `test`. Usar el POM completo de la copia preparada, reproducido en el anexo A.

No añadir `mockito-inline`: en Mockito 5 el mecanismo inline ya es el predeterminado. No añadir el runner de JUnit 4 ni Vintage para estas pruebas nuevas.

El POM también configura el agente de Mockito al arrancar las JVM de prueba. Esto sigue la configuración documentada para Java 21 y posteriores. La copia adicional del JAR en `target/test-agent` da a VS Code una ruta estable; Surefire utiliza la dependencia resuelta por Maven. Es configuración de preparación de la clase, no contenido que los estudiantes deban memorizar.

### Paso 4. Incorporar Maven Wrapper

Copiar del proyecto preparado `mvnw`, `mvnw.cmd` y la carpeta `.mvn/wrapper`. Permiten ejecutar una versión fijada de Maven sin instalación independiente. Se necesita un **JDK 21 completo**, no solo un JRE.

Comprobar en terminal:

```powershell
java -version
.\mvnw.cmd -version
```

En Linux/macOS:

```bash
java -version
./mvnw -version
```

Ambos deben mostrar que usan Java 21. En un equipo Unix donde se hayan perdido los permisos del ejecutable, ejecutar `chmod +x mvnw` una vez.

### Paso 5. Encapsular la colección del servicio

En `src/main/java/es/grise/upm/profundizacion/mocking_ejercicio_1/SubscriptionService.java`, sustituir el comentario que afirma que la colección no puede ser privada por:

```java
private final Collection<Client> subscribers = new ArrayList<>();
```

El campo no necesita estar expuesto para probar las altas o bajas. Un cliente añadido recibe mensajes; un cliente eliminado deja de recibirlos. Se comprueba ese comportamiento desde la API pública. `final` impide sustituir la referencia, pero permite añadir o eliminar elementos de la colección.

Este ajuste es una mejora de encapsulación, no un requisito para que Mockito funcione. `Client`, `Message`, las excepciones y las firmas de los métodos pueden permanecer iguales.

### Paso 6. Crear la estructura de la clase de prueba

Archivo `src/test/java/es/grise/upm/profundizacion/mocking_ejercicio_1/SubscriptionServiceTest.java`:

```java
package es.grise.upm.profundizacion.mocking_ejercicio_1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {
    @Mock
    private Client cliente;

    private SubscriptionService servicio;
    private Message mensaje;

    @BeforeEach
    void preparar() {
        servicio = new SubscriptionService();
        mensaje = new Aviso("Cambio de aula");
    }

    @Test
    void enviaMensajeAClienteConCorreo() throws Exception {
        when(cliente.hasEmail()).thenReturn(true);
        servicio.addSubscriber(cliente);

        servicio.sendMessage(mensaje);

        verify(cliente).receiveMessage(mensaje);
    }

    private record Aviso(String texto) implements Message {}
}
```

La versión del ZIP amplía esta clase con más escenarios. `@Mock` crea el cliente simulado; `MockitoExtension` inicializa esos campos. `new SubscriptionService()` crea el objeto **real** que se va a evaluar. No se usa `@InjectMocks` porque aquí no aporta nada y no suscribe clientes automáticamente. `addSubscriber(cliente)` forma parte de la preparación de cada escenario.

`Aviso` es un dato real de la prueba. No es necesario simular todos los objetos. Los métodos de prueba y la clase no necesitan ser `public`; pueden tener visibilidad de paquete. Los campos sí pueden seguir siendo privados.

### Paso 7. Añadir los escenarios en un orden docente

| Orden | Método incluido en el ZIP | Idea principal |
|---|---|---|
| 1 | `rechazaClienteNulo` | Enlace con `assertThrows` de JUnit. |
| 2 | `enviaMensajeAClienteConCorreo` | `thenReturn(true)`, ejecutar el servicio y verificar entrega. |
| 3 | `noEnviaAClienteSinCorreo` | `thenReturn(false)` y `never`. |
| 4 | `noContactaAClienteDadoDeBaja` | Diferencia entre no entregar y no llamar a ningún método del cliente. |
| 5 | `propagaErrorAlConsultarCorreo` | `thenThrow` y `assertThrows` en el mismo test. |
| 6 | `propagaErrorAlEntregarMensaje` | `doThrow` para un método `void`. |
| 7 | `capturaContenidoMensajeEnviado` | Captura y comprobación del argumento. |

Los demás tests del servicio cubren duplicados, bajas inválidas, varios clientes, dos publicaciones, respuestas consecutivas y orden. Son material de apoyo, no hace falta desarrollarlos todos en directo. Los dos ejemplos de spies están separados en `MockitoSpyTest` porque explican la herramienta, no una necesidad del negocio de suscripciones.

### Paso 8. Mantener la preparación específica en cada test

En `@BeforeEach` crear el servicio y los datos comunes. Configurar `when(cliente.hasEmail())...` únicamente en los tests que ejecutan ese método. Por ejemplo, el test de baja no configura `hasEmail`, porque precisamente espera que el servicio ya no consulte al cliente.

Esto facilita la lectura y evita stubs innecesarios que `STRICT_STUBS` puede detectar. Cada test prepara su estado: no depende de que antes se haya ejecutado el test de alta.

### Paso 9. Ejecutar primero con Maven

En PowerShell, desde la carpeta del POM:

```powershell
.\mvnw.cmd test
```

En Linux/macOS:

```bash
./mvnw test
```

La primera ejecución requiere red para descargar Maven y las dependencias. Debe terminar con `BUILD SUCCESS`; la copia completa contiene **16 tests: 14 del servicio y 2 de spies**. Para ejecutar solo una parte en PowerShell:

```powershell
.\mvnw.cmd "-Dtest=SubscriptionServiceTest" test
.\mvnw.cmd "-Dtest=SubscriptionServiceTest#enviaMensajeAClienteConCorreo" test
.\mvnw.cmd "-Dtest=MockitoSpyTest" test
```

Ejecutar una de esas líneas según lo que se quiera mostrar. En Unix sustituir el nombre del wrapper por `./mvnw`.

### Paso 10. Preparar VS Code

1. Tener habilitado Test Runner for Java, como en el ejemplo de Fibonacci; Extension Pack for Java reúne las extensiones habituales.
2. Abrir la carpeta que contiene el POM y aceptar la importación del proyecto Maven si se solicita.
3. Esperar a que termine la importación.
4. Ejecutar una vez el comando de Maven anterior. Además de comprobar el proyecto, crea `target/test-agent/mockito-agent.jar`.
5. Conservar la configuración incluida en `.vscode/settings.json`:

```json
{
  "java.test.config": {
    "name": "Mockito-Java-21",
    "vmArgs": [
      "-javaagent:${workspaceFolder}/target/test-agent/mockito-agent.jar"
    ]
  },
  "java.test.defaultConfig": "Mockito-Java-21"
}
```

6. Abrir la clase de prueba y pulsar el triángulo junto a la clase o al método. También se puede usar el panel Testing.

Si se elimina `target` o se ejecuta `clean`, repetir Maven antes de usar el runner de VS Code. Si no aparecen los tests, comprobar que Java terminó de importar el proyecto y que el archivo está guardado. Como alternativa fiable para la clase, usar el comando de Maven de un único método.

No hay que ejecutar una aplicación Spring Boot ni añadir Actuator para lanzar estos tests.

## 5. Guion de las demostraciones

### A. Cliente con correo

Abrir primero el código productivo y leer solo el bucle y el `if`. Después construir el test de envío del paso 6.

«El servicio no envía el mensaje a todos indiscriminadamente: pregunta si el cliente tiene correo. Para probar esa rama configuro la respuesta del cliente. Suscribo al cliente, ejecuto el servicio real y compruebo que el servicio le pide recibir el mensaje. `verify` sin indicar un número significa una vez, con esos argumentos».

Ejecutar el test y señalar que no se ha enviado un correo real. Lo observado es la solicitud de entrega al colaborador. Opcionalmente, comentar temporalmente la llamada productiva a `receiveMessage`, volver a ejecutar y leer el fallo de Mockito. Restaurar inmediatamente el código y comprobar que vuelve a pasar.

### B. Cliente sin correo

```java
when(cliente.hasEmail()).thenReturn(false);
servicio.addSubscriber(cliente);

servicio.sendMessage(mensaje);

verify(cliente, never()).receiveMessage(any(Message.class));
```

«Ahora el cliente sí está suscrito, pero no tiene correo. El servicio puede consultarlo, pero no debe solicitar la entrega. `never` expresa esa ausencia concreta. `any(Message.class)` acepta cualquier argumento no nulo de ese tipo; los matchers tipados no incluyen `null`. Si se quiere prohibir también una llamada con `null`, puede usarse `any()`».

El ejemplo usa un mensaje real no nulo. El propósito de esta pequeña nota es evitar enseñar que todos los matchers aceptan automáticamente `null`, no añadir una digresión a la clase.

### C. Cliente dado de baja

```java
servicio.addSubscriber(cliente);
servicio.removeSubscriber(cliente);

servicio.sendMessage(mensaje);

verifyNoInteractions(cliente);
```

«Aquí la expectativa es más fuerte: el servicio ya no debe contactar con ese cliente, ni siquiera consultar su correo. No configuro una respuesta que no debería utilizarse. La baja se comprueba por su efecto público; no necesito leer la colección privada».

Las llamadas a `addSubscriber` y `removeSubscriber` son al servicio real. Guardar el mock en una colección no implica una llamada a un método de negocio de `Client`.

### D. Fallo al consultar el correo

Mostrar `propagaErrorAlConsultarCorreo`. Separar visualmente preparación, ejecución y comprobación. El método `Client.hasEmail()` no declara excepciones comprobadas: se usa `IllegalStateException`.

«No estamos pidiendo a JUnit que configure el fallo. Mockito prepara la respuesta excepcional. JUnit comprueba que el servicio propaga esa excepción. El diseño actual no la captura; este test describe ese comportamiento. Si el requisito fuera continuar con los demás clientes, tendríamos que cambiar tanto el servicio como sus pruebas».

### E. Fallo en un método void

```java
when(cliente.hasEmail()).thenReturn(true);
servicio.addSubscriber(cliente);
doThrow(new IllegalStateException("Fallo de entrega"))
    .when(cliente).receiveMessage(any(Message.class));

assertThrows(IllegalStateException.class,
    () -> servicio.sendMessage(mensaje));
```

«`receiveMessage` devuelve `void`, por lo que no cabe dentro de `when(...)` de la misma forma que `hasEmail`. Para configurar el fallo usamos `doThrow`. La comprobación del servicio sigue siendo `assertThrows`».

### F. Captor

```java
ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
verify(cliente).receiveMessage(captor.capture());
Aviso enviado = assertInstanceOf(Aviso.class, captor.getValue());
assertEquals("Cambio de aula", enviado.texto());
```

Ejecutar antes el servicio como en el test completo. «Ahora recogemos el argumento utilizado en una llamada real al doble y podemos comprobar sus propiedades».

En este servicio el mensaje se reenvía sin transformarlo; verificar el mensaje esperado ya es suficiente para muchos tests. El captor es una demostración de API. Resulta especialmente útil cuando el SUT construye o transforma el objeto que entrega y queremos inspeccionar varios campos de ese resultado. No presentarlo como obligatorio.

### G. Spy

Abrir `MockitoSpyTest` y ejecutar un método. Mantenerlo separado del servicio real para evitar que parezca necesario convertir el SUT en spy.

«Con el mock controlábamos las respuestas sin ejecutar una implementación real de `Client`. Aquí partimos de una lista real. Añadir y leer ejecuta el comportamiento real; configuramos solo una lectura. Podemos verificar llamadas tanto en mocks como en spies. Con un spy hay que tener cuidado al configurar porque una llamada real puede ejecutarse en ese momento».

## 6. Qué preparar antes y qué hacer en directo

**Antes de clase:** JDK, extensiones, POM, wrapper, agente, importación Maven y primera descarga. Ejecutar el proyecto completo. Preparar una copia limpia o una rama para desarrollar los tests y conservar la solución completa como respaldo. No dedicar el tiempo de explicación a actualizar plugins.

**En directo:** leer interfaces y servicio; identificar el SUT y el colaborador; construir los tests esenciales en el orden propuesto; ejecutar cada uno; leer al menos un fallo provocado y restaurado. No hace falta escribir de nuevo las excepciones o clases productivas.

**Material para los estudiantes:** presentación corregida, proyecto completo, instrucciones de ejecución y pruebas adicionales. Todos los tests entregados deben estar en verde y funcionar de manera independiente.

## 7. Comprobaciones realizadas

Se han compilado las fuentes con Java 21 y se han ejecutado las pruebas tanto con JUnit Platform como mediante Maven Surefire y el agente de Mockito: **16 tests, 0 fallos, 0 errores y 0 omitidos; BUILD SUCCESS**. También se ha verificado que Maven prepara el JAR del agente en la ruta que utiliza VS Code.

La ejecución Maven de verificación fue sin conexión, con dependencias descargadas previamente. Como el JDK de este entorno no incluye `ct.sym`, en esa ejecución se usaron las opciones `source=21` y `target=21` en lugar de `release=21`; el POM entregado conserva `release=21`, adecuado para un JDK completo. No se ha ejecutado el runner gráfico de VS Code en un equipo Windows; su configuración sigue la opción `vmArgs` documentada por Test Runner for Java.

## 8. Fuentes primarias y referencias para mantener el material

- [Proyecto base de suscripciones](https://github.com/GRISE-UPM/PROF-Mocking-Exercise-1).
- [Proyecto Fibonacci de la clase](https://github.com/GRISE-UPM/PROF-fibonacci-api).
- [JUnit 6.0.3 User Guide](https://docs.junit.org/6.0.3/user-guide/): Jupiter, extensiones y ejecución de pruebas.
- [API de Mockito 5.20.0](https://javadoc.io/doc/org.mockito/mockito-core/5.20.0/): stubbing, verificación, spies, captores y configuración del agente para Java 21.
- [Fuentes publicadas de Mockito 5.20.0](https://repo.maven.apache.org/maven2/org/mockito/mockito-core/5.20.0/mockito-core-5.20.0-sources.jar): documentación de `Mockito` consultada para el agente y el comportamiento de la API.
- [Fuentes de Mockito/Jupiter 5.20.0](https://repo.maven.apache.org/maven2/org/mockito/mockito-junit-jupiter/5.20.0/mockito-junit-jupiter-5.20.0-sources.jar): `MockitoExtension` y strictness predeterminada.
- [Microsoft: Run with Configuration](https://github.com/microsoft/vscode-java-test/wiki/Run-with-Configuration): configuración del runner de Java en VS Code.
- [Martin Fowler: Test Double](https://martinfowler.com/bliki/TestDouble.html): terminología de dobles. Las definiciones son una ayuda conceptual; las herramientas pueden combinar funciones.
- [Google: gMock Cookbook](https://google.github.io/googletest/gmock_cook_book.html): técnicas de mocking en C++, incluidos casos sin métodos virtuales mediante diseño e inyección en compilación.

## Anexo A. POM completo de la copia preparada

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>es.grise.upm.profundizacion</groupId>
    <artifactId>PROF-Mocking-Exercise-1</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>Mocking - Servicio de suscripciones</name>
    <description>Ejemplo docente con JUnit Jupiter y Mockito.</description>
    <url>https://github.com/GRISE-UPM/PROF-Mocking-Exercise-1</url>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.release>21</maven.compiler.release>
        <junit.version>6.0.3</junit.version>
        <mockito.version>5.20.0</mockito.version>
        <argLine></argLine>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.junit</groupId>
                <artifactId>junit-bom</artifactId>
                <version>${junit.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>${mockito.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-junit-jupiter</artifactId>
            <version>${mockito.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.14.1</version>
            </plugin>
            <!-- Proporciona la ruta del JAR para el agente de Mockito. -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <version>3.8.1</version>
                <executions>
                    <execution>
                        <id>rutas-de-dependencias</id>
                        <phase>initialize</phase>
                        <goals>
                            <goal>properties</goal>
                        </goals>
                    </execution>
                    <!-- Ruta estable para ejecutar desde Test Runner for Java. -->
                    <execution>
                        <id>agente-para-vscode</id>
                        <phase>process-test-classes</phase>
                        <goals>
                            <goal>copy</goal>
                        </goals>
                        <configuration>
                            <artifactItems>
                                <artifactItem>
                                    <groupId>org.mockito</groupId>
                                    <artifactId>mockito-core</artifactId>
                                    <version>${mockito.version}</version>
                                    <outputDirectory>${project.build.directory}/test-agent</outputDirectory>
                                    <destFileName>mockito-agent.jar</destFileName>
                                </artifactItem>
                            </artifactItems>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.5.5</version>
                <configuration>
                    <argLine>@{argLine} -javaagent:"${org.mockito:mockito-core:jar}"</argLine>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```
