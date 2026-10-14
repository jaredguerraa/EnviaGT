# Lab 3 - Herencia: EnvíaGT

## Compilar y ejecutar

Abra una terminal dentro de la carpeta `Lab 3 POO`.

```sh
mkdir -p bin
javac -encoding UTF-8 -d bin src/*.java
java -cp bin Main
```

Requiere JDK 8 o superior. No utiliza dependencias externas.

## Datos iniciales (generados con ayuda de IA)

| Código | Categoría | Remitente | Destinatario | Peso real | Destino | Características |
|---|---|---|---|---:|---|---|
| S01 | Sobre | Ana | Luis | 0.20 kg | Urbano | Documentos |
| S02 | Sobre | Eva | Mario | 0.50 kg | Departamental | Peso máximo permitido |
| P01 | Paquete | Ana | Pedro | 3.20 kg | Urbano | 40 × 30 × 20 cm |
| P02 | Paquete | Luz | Sofía | 2.10 kg | Departamental | 20 × 20 × 20 cm |
| F01 | Paquete frágil | José | Iris | 3.20 kg | Urbano | 40 × 30 × 20 cm, valor declarado Q1,200.00 |
| F02 | Paquete frágil | Sara | Rosa | 1.00 kg | Departamental | 20 × 20 × 20 cm, valor declarado Q500.00 |
| R01 | Carga refrigerada | Farmacia | Clínica | 5.00 kg | Urbano | Temperatura −25 °C |
| R02 | Carga refrigerada | Tienda | Cliente | 8.00 kg | Urbano | Temperatura 8 °C |

| Placa | Capacidad máxima | Refrigeración |
|---|---:|---|
| GT-101 | 30.00 kg | No |
| GT-202 | 100.00 kg | Sí |
| GT-303 | 50.00 kg | No |

Todos los envíos iniciales comienzan en estado `BODEGA`, con cero intentos de entrega. Los vehículos están disponibles y no existen despachos activos.

Los datos precargados no generan ingresos. Los reportes de ingresos por registro y reintentos comienzan en Q0.00.

Para comprobar el cálculo del peso volumétrico se pueden utilizar P01 y F01. Ambos tienen un peso real de 3.20 kg y dimensiones de 40 × 30 × 20 cm, lo que produce un peso volumétrico de 4.80 kg y un peso facturable de 5 kg.

El paquete P01 tiene un costo de Q65.00. El paquete frágil F01 cuesta Q116.00, incluyendo Q15.00 de embalaje y el 3 % de seguro sobre su valor declarado.

Para comprobar las restricciones de refrigeración se pueden utilizar R01 y R02 con el vehículo GT-202. Las cargas refrigeradas únicamente aceptan destinos urbanos y vehículos refrigerados.

Cada intento de entrega posterior al primero genera un recargo de Q15.00, registrado cuando el despacho sale. Los sobres y paquetes permiten tres intentos, los paquetes frágiles dos y las cargas refrigeradas uno.

## Organización y diseño

- **Modelo:** `Envio` es una clase abstracta que contiene los atributos y comportamientos comunes. `Sobre`, `Paquete` y `CargaRefrigerada` heredan de `Envio`, `PaqueteFragil` hereda de `Paquete`. `Vehiculo` representa la flotilla y `Despacho` administra las asignaciones y entregas.
- **Enumeraciones:** `Destino`, `EstadoEnvio` y `EstadoDespacho` definen los destinos y estados permitidos.
- **Sistema:** `SistemaEnvios` administra las colecciones de envíos, vehículos y despachos, valida registros únicos y contabiliza los ingresos.
- **Controlador:** `ControladorEnvios` coordina las operaciones del menú y comunica la vista con el sistema.
- **Vista:** `VistaEnvios` solicita los datos al usuario, maneja entradas incorrectas y muestra resultados.
- **Clase principal:** `Main` contiene el método `main` e inicia el controlador.

## Funcionalidades

El programa permite cotizar y registrar envíos, registrar vehículos, crear despachos, asignar y retirar envíos, registrar salidas y resultados de entrega, consultar información y generar reportes.

Las cotizaciones no registran envíos ni generan ingresos. Las operaciones inválidas se rechazan sin modificar el estado previo del sistema.