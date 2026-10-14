import java.util.Scanner;

public class VistaEnvios {

    private final Scanner sc;

    public VistaEnvios() {
        sc = new Scanner(System.in);
    }

    public int mostrarMenu() {

        mostrarMensaje("\n= ENVIA GT =");
        mostrarMensaje("1. Cotizar envio");
        mostrarMensaje("2. Registrar envio");
        mostrarMensaje("3. Registrar vehiculo");
        mostrarMensaje("4. Crear despacho");
        mostrarMensaje("5. Asignar envio");
        mostrarMensaje("6. Retirar envio");
        mostrarMensaje("7. Salir despacho");
        mostrarMensaje("8. Registrar resultado");
        mostrarMensaje("9. Consultar envios");
        mostrarMensaje("10. Consultar vehiculos");
        mostrarMensaje("11. Reporte cantidades");
        mostrarMensaje("12. Reporte ingresos");
        mostrarMensaje("13. Reporte despachos");
        mostrarMensaje("0. Salir");
        mostrarMensaje("=====");

        return pedirInt("Opcion: ");
    }

    public String pedirTexto(String mensaje) {

        System.out.print(mensaje);

        return sc.nextLine().trim();
    }

    public int pedirInt(String mensaje) {

        while (true) {

            try {
                return Integer.parseInt(pedirTexto(mensaje));

            } catch (NumberFormatException ex) {
                mostrarMensaje(
                    "Ingrese un numero entero valido."
                );
            }
        }
    }

    public double pedirDouble(String mensaje) {

        while (true) {

            try {
                String entrada = pedirTexto(mensaje);
                entrada = entrada.replace(',', '.');

                double numero = Double.parseDouble(entrada);

                if (!Double.isFinite(numero)) {
                    throw new NumberFormatException();
                }

                return numero;

            } catch (NumberFormatException ex) {
                mostrarMensaje(
                    "Ingrese un numero decimal finito valido."
                );
            }
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}
