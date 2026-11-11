# Mocking: servicio de suscripciones

Ejemplo para una demostración docente con JUnit Jupiter y Mockito. Basado en [GRISE-UPM/PROF-Mocking-Exercise-1](https://github.com/GRISE-UPM/PROF-Mocking-Exercise-1), commit `d861d495bf8678236763d7025044f9f419feccec`.

## Entorno

- JDK 21.
- JUnit Jupiter 6.0.3 y Mockito 5.20.0, alineados con el ejemplo de Fibonacci utilizado en la clase.
- Maven Wrapper incluido: no hace falta instalar Maven por separado.
- Para trabajar en VS Code, Extension Pack for Java con Test Runner for Java habilitado.

Este proyecto es Java con Maven. Las pruebas construyen un servicio real y utilizan clientes simulados.

## Primera ejecución

Abrir esta carpeta, que contiene `pom.xml`, en el entorno de desarrollo.

En PowerShell, desde la raíz del proyecto:

```powershell
java -version
.\mvnw.cmd test
```

En Linux o macOS:

```bash
java -version
./mvnw test
```

La primera ejecución descarga las dependencias y prepara el agente de Mockito. El resultado esperado es **16 pruebas, sin fallos ni errores**.

## Ejecutar una parte

Solo las pruebas del servicio:

```powershell
.\mvnw.cmd "-Dtest=SubscriptionServiceTest" test
```

Solo la primera demostración de envío:

```powershell
.\mvnw.cmd "-Dtest=SubscriptionServiceTest#enviaMensajeAClienteConCorreo" test
```

Los dos ejemplos de spy:

```powershell
.\mvnw.cmd "-Dtest=MockitoSpyTest" test
```

En Linux o macOS, sustituir `.\mvnw.cmd` por `./mvnw`.

## Ejecutar desde VS Code

1. Ejecutar una vez el comando completo anterior.
2. Esperar a que VS Code termine de importar el proyecto Maven.
3. Abrir `SubscriptionServiceTest.java`.
4. Utilizar el triángulo de ejecución junto a la clase o a un método.

La configuración incluida en `.vscode/settings.json` usa el agente generado en `target/test-agent/mockito-agent.jar`. Si se elimina `target`, repetir `mvnw test` antes de ejecutar desde el explorador de pruebas.

## Qué representa cada elemento

| Elemento | Papel |
|---|---|
| `SubscriptionService` | SUT real |
| `Client` | Colaborador sustituido con Mockito |
| `Message` | Contrato del dato que se entrega |
| `Aviso` | Dato real de prueba, definido como record dentro de la clase de tests |
| `subscribers` | Colección real, privada, que mantiene el servicio |
| `MockitoExtension` | Inicializa `@Mock` y controla su ciclo de vida y strict stubbing |

Se comprueba que el servicio solicita la entrega a los clientes adecuados. Estas pruebas no comprueban la entrega de un correo real.

## Recorrido recomendado durante la clase

Mostrar, en este orden, los métodos siguientes de `SubscriptionServiceTest`:

1. `rechazaClienteNulo`: enlace con `assertThrows` de JUnit.
2. `enviaMensajeAClienteConCorreo`: `when`, `thenReturn` y `verify`.
3. `noEnviaAClienteSinCorreo`: `never` y matchers.
4. `noContactaAClienteDadoDeBaja`: `verifyNoInteractions`.
5. `propagaErrorAlConsultarCorreo`: `thenThrow` junto con `assertThrows`.
6. `propagaErrorAlEntregarMensaje`: excepción en un método void con `doThrow`.
7. `capturaContenidoMensajeEnviado`: `ArgumentCaptor`.

Mostrar después uno de los métodos de `MockitoSpyTest` si el tiempo permite explicar llamadas reales y `doReturn`.

Las pruebas restantes amplían los casos de negocio y ofrecen ejemplos de conteo, respuestas consecutivas y orden. Se pueden dejar disponibles para consulta.

Cada test prepara su propio estado; el orden de ejecución no es un requisito para que funcionen.

## Cambios respecto al proyecto base

- Sustitución de JUnit 4 y `mockito-all` por las dependencias actuales del POM.
- Configuración de Java 21 y de los plugins de Maven.
- Agente de Mockito configurado para Maven y VS Code.
- Incorporación del Maven Wrapper.
- Colección de suscriptores privada y final, sin cambiar el comportamiento público.
- Catorce pruebas del servicio y dos ejemplos de spies.
- Mensajes reales de prueba; no se simula todo el modelo de datos.

Las excepciones originales y las interfaces `Client` y `Message` mantienen su contrato.
