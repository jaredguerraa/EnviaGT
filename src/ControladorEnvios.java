public class ControladorEnvios {

    private final SistemaEnvios sistema;
    private final VistaEnvios vista;

    public ControladorEnvios() {
        sistema = new SistemaEnvios();
        vista = new VistaEnvios();
    }

    public void iniciar() {

        while (true) {

            try {
                int opcion = vista.mostrarMenu();

                switch (opcion) {

                    case 0:
                        vista.mostrarMensaje("Hasta luego.");
                        return;

                    case 1:
                        vista.mostrarMensaje(
                            sistema.cotizar(leerEnvio())
                        );
                        break;

                    case 2: {
                        Envio envio = leerEnvio();

                        sistema.registrarEnvio(envio);

                        vista.mostrarMensaje(
                            "Registrado. " + sistema.cotizar(envio)
                        );
                        break;
                    }

                    case 3: {
                        String placa = vista.pedirTexto("Placa: ");

                        double capacidad = vista.pedirDouble(
                            "Capacidad kg: "
                        );

                        int frio = vista.pedirInt(
                            "Refrigerado? 1=si, 0=no: "
                        );

                        if (frio != 0 && frio != 1) {
                            throw new IllegalArgumentException(
                                "Seleccione 0 o 1"
                            );
                        }

                        Vehiculo vehiculo = new Vehiculo(
                            placa,
                            capacidad,
                            frio == 1
                        );

                        sistema.registrarVehiculo(vehiculo);

                        vista.mostrarMensaje("Vehiculo registrado.");
                        break;
                    }

                    case 4: {
                        String codigo = vista.pedirTexto(
                            "Codigo despacho: "
                        );

                        String placa = vista.pedirTexto("Placa: ");

                        sistema.crearDespacho(codigo, placa);

                        vista.mostrarMensaje("Despacho creado.");
                        break;
                    }

                    case 5: {
                        String despacho = vista.pedirTexto(
                            "Despacho: "
                        );

                        String envio = vista.pedirTexto("Envio: ");

                        sistema.asignar(despacho, envio);

                        vista.mostrarMensaje("Envio asignado.");
                        break;
                    }

                    case 6: {
                        String despacho = vista.pedirTexto(
                            "Despacho: "
                        );

                        String envio = vista.pedirTexto("Envio: ");

                        sistema.retirar(despacho, envio);

                        vista.mostrarMensaje("Envio retirado.");
                        break;
                    }

                    case 7: {
                        String despacho = vista.pedirTexto(
                            "Despacho: "
                        );

                        sistema.salirDespacho(despacho);

                        vista.mostrarMensaje("Despacho en ruta.");
                        break;
                    }

                    case 8: {
                        String despacho = vista.pedirTexto(
                            "Despacho: "
                        );

                        String envio = vista.pedirTexto("Envio: ");

                        int resultado = vista.pedirInt(
                            "1=entregado, 0=fallido: "
                        );

                        if (resultado != 0 && resultado != 1) {
                            throw new IllegalArgumentException(
                                "Seleccione 0 o 1"
                            );
                        }

                        sistema.registrarResultado(
                            despacho,
                            envio,
                            resultado == 1
                        );

                        vista.mostrarMensaje("Resultado guardado.");
                        break;
                    }

                    case 9:
                        vista.mostrarMensaje(
                            sistema.consultarEnvios()
                        );
                        break;

                    case 10:
                        vista.mostrarMensaje(
                            sistema.consultarVehiculos()
                        );
                        break;

                    case 11:
                        vista.mostrarMensaje(
                            sistema.reporteCantidades()
                        );
                        break;

                    case 12:
                        vista.mostrarMensaje(
                            sistema.reporteIngresos()
                        );
                        break;

                    case 13:
                        vista.mostrarMensaje(
                            sistema.reporteDespachos()
                        );
                        break;

                    default:
                        vista.mostrarMensaje("Opcion no valida.");
                        break;
                }

            } catch (IllegalArgumentException | IllegalStateException ex) {
                vista.mostrarMensaje(
                    "Error: " + ex.getMessage()
                );
            }
        }
    }

    private Envio leerEnvio() {

        vista.mostrarMensaje("1. Sobre");
        vista.mostrarMensaje("2. Paquete");
        vista.mostrarMensaje("3. Paquete fragil");
        vista.mostrarMensaje("4. Carga refrigerada");

        int tipo = vista.pedirInt("Categoria: ");

        if (tipo < 1 || tipo > 4) {
            throw new IllegalArgumentException(
                "Categoria no valida"
            );
        }

        String codigo = vista.pedirTexto("Codigo: ");

        String remitente = vista.pedirTexto("Remitente: ");

        String destinatario = vista.pedirTexto("Destinatario: ");

        double peso = vista.pedirDouble("Peso real kg: ");

        int opcionDestino = vista.pedirInt(
            "Destino 1=urbano, 2=departamental: "
        );

        if (opcionDestino != 1 && opcionDestino != 2) {
            throw new IllegalArgumentException(
                "Destino no valido"
            );
        }

        Destino destino;

        if (opcionDestino == 1) {
            destino = Destino.URBANO;
        } else {
            destino = Destino.DEPARTAMENTAL;
        }

        if (tipo == 1) {
            return new Sobre(
                codigo,
                remitente,
                destinatario,
                peso,
                destino
            );
        }

        if (tipo == 4) {

            double temperatura = vista.pedirDouble(
                "Temperatura C: "
            );

            return new CargaRefrigerada(
                codigo,
                remitente,
                destinatario,
                peso,
                destino,
                temperatura
            );
        }

        double largo = vista.pedirDouble("Largo cm: ");
        double ancho = vista.pedirDouble("Ancho cm: ");
        double alto = vista.pedirDouble("Alto cm: ");

        if (tipo == 2) {
            return new Paquete(
                codigo,
                remitente,
                destinatario,
                peso,
                destino,
                largo,
                ancho,
                alto
            );
        }

        double valorDeclarado = vista.pedirDouble(
            "Valor declarado Q: "
        );

        return new PaqueteFragil(
            codigo,
            remitente,
            destinatario,
            peso,
            destino,
            largo,
            ancho,
            alto,
            valorDeclarado
        );
    }
}
