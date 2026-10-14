import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class SistemaEnvios {

    private final ArrayList<Envio> envios;
    private final ArrayList<Vehiculo> vehiculos;
    private final ArrayList<Despacho> despachos;

    private final Map<String, Double> registros;
    private final Map<String, Double> reintentos;

    public SistemaEnvios() {

        envios = new ArrayList<>();
        vehiculos = new ArrayList<>();
        despachos = new ArrayList<>();

        registros = new TreeMap<>();
        reintentos = new TreeMap<>();

        cargarIniciales();
    }

    private void cargarIniciales() {

        // Sobres
        envios.add(new Sobre(
            "S01", "Ana", "Luis", 0.2, Destino.URBANO
        ));

        envios.add(new Sobre(
            "S02", "Eva", "Mario", 0.5, Destino.DEPARTAMENTAL
        ));

        // Paquetes
        envios.add(new Paquete(
            "P01", "Ana", "Pedro", 3.2,
            Destino.URBANO, 40, 30, 20
        ));

        envios.add(new Paquete(
            "P02", "Luz", "Sofia", 2.1,
            Destino.DEPARTAMENTAL, 20, 20, 20
        ));

        // Paquetes fragiles
        envios.add(new PaqueteFragil(
            "F01", "Jose", "Iris", 3.2,
            Destino.URBANO, 40, 30, 20, 1200
        ));

        envios.add(new PaqueteFragil(
            "F02", "Sara", "Rosa", 1,
            Destino.DEPARTAMENTAL, 20, 20, 20, 500
        ));

        // Cargas refrigeradas
        envios.add(new CargaRefrigerada(
            "R01", "Farmacia", "Clinica", 5,
            Destino.URBANO, -25
        ));

        envios.add(new CargaRefrigerada(
            "R02", "Tienda", "Cliente", 8,
            Destino.URBANO, 8
        ));

        // Vehiculos
        vehiculos.add(new Vehiculo("GT-101", 30, false));
        vehiculos.add(new Vehiculo("GT-202", 100, true));
        vehiculos.add(new Vehiculo("GT-303", 50, false));
    }

    public String cotizar(Envio envio) {

        if (envio == null) {
            throw new IllegalArgumentException(
                "Envio obligatorio"
            );
        }

        return String.format(
            "%s | costo Q%.2f | %d dia(s) habiles",
            envio.getCategoria(),
            envio.calcularCosto(),
            envio.diasEntrega()
        );
    }

    public void registrarEnvio(Envio envio) {

        if (envio == null) {
            throw new IllegalArgumentException(
                "Envio obligatorio"
            );
        }

        for (Envio actual : envios) {
            if (actual.getCodigo().equalsIgnoreCase(envio.getCodigo())) {
                throw new IllegalArgumentException(
                    "Codigo de envio repetido"
                );
            }
        }

        double costo = envio.calcularCosto();

        if (!Double.isFinite(costo) || costo <= 0) {
            throw new IllegalArgumentException(
                "Costo invalido"
            );
        }

        envios.add(envio);

        acumularIngreso(
            registros,
            envio.getCategoria(),
            costo
        );
    }

    public void registrarVehiculo(Vehiculo vehiculo) {

        if (vehiculo == null) {
            throw new IllegalArgumentException(
                "Vehiculo obligatorio"
            );
        }

        for (Vehiculo actual : vehiculos) {
            if (actual.getPlaca().equalsIgnoreCase(vehiculo.getPlaca())) {
                throw new IllegalArgumentException(
                    "Placa repetida"
                );
            }
        }

        vehiculos.add(vehiculo);
    }

    public void crearDespacho(String codigo, String placa) {

        codigo = Envio.validarTexto(codigo, "Codigo despacho");

        for (Despacho despacho : despachos) {
            if (despacho.getCodigo().equalsIgnoreCase(codigo)) {
                throw new IllegalArgumentException(
                    "Codigo de despacho repetido"
                );
            }
        }

        Vehiculo vehiculo = buscarVehiculo(placa);

        if (tieneDespachoAbierto(vehiculo)) {
            throw new IllegalStateException(
                "Vehiculo ya tiene un despacho abierto"
            );
        }

        despachos.add(new Despacho(codigo, vehiculo));
    }

    public void asignar(String codigoDespacho, String codigoEnvio) {

        Despacho despacho = buscarDespacho(codigoDespacho);
        Envio envio = buscarEnvio(codigoEnvio);

        if (tieneAsignacionActiva(envio)) {
            throw new IllegalStateException(
                "Envio ya asignado a un despacho abierto"
            );
        }

        despacho.asignar(envio);
    }

    public void retirar(String codigoDespacho, String codigoEnvio) {
        buscarDespacho(codigoDespacho).retirar(codigoEnvio);
    }

    public void salirDespacho(String codigo) {

        Despacho despacho = buscarDespacho(codigo);

        despacho.validarSalida();

        List<Envio> carga = despacho.getEnvios();

        despacho.salir();

        for (Envio envio : carga) {
            if (envio.getIntentos() > 1) {
                acumularIngreso(
                    reintentos,
                    envio.getCategoria(),
                    15
                );
            }
        }
    }

    public void registrarResultado(
            String codigoDespacho,
            String codigoEnvio,
            boolean exito) {

        Despacho despacho = buscarDespacho(codigoDespacho);

        despacho.registrarResultado(codigoEnvio, exito);
    }

    private Envio buscarEnvio(String codigo) {

        for (Envio envio : envios) {
            if (envio.getCodigo().equalsIgnoreCase(codigo)) {
                return envio;
            }
        }

        throw new IllegalArgumentException(
            "Envio no encontrado: " + codigo
        );
    }

    private Vehiculo buscarVehiculo(String placa) {

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getPlaca().equalsIgnoreCase(placa)) {
                return vehiculo;
            }
        }

        throw new IllegalArgumentException(
            "Vehiculo no encontrado: " + placa
        );
    }

    private Despacho buscarDespacho(String codigo) {

        for (Despacho despacho : despachos) {
            if (despacho.getCodigo().equalsIgnoreCase(codigo)) {
                return despacho;
            }
        }

        throw new IllegalArgumentException(
            "Despacho no encontrado: " + codigo
        );
    }

    private boolean tieneDespachoAbierto(Vehiculo vehiculo) {

        for (Despacho despacho : despachos) {

            if (despacho.getVehiculo() == vehiculo
                    && despacho.getEstado() != EstadoDespacho.CERRADO) {
                return true;
            }
        }

        return false;
    }

    private boolean tieneAsignacionActiva(Envio envio) {

        for (Despacho despacho : despachos) {

            if (despacho.getEstado() != EstadoDespacho.CERRADO
                    && despacho.contieneEnvio(envio.getCodigo())) {
                return true;
            }
        }

        return false;
    }

    public String consultarEnvios() {

        StringBuilder sb = new StringBuilder("ENVIOS\n");

        for (Envio envio : envios) {
            sb.append(envio.descripcion()).append('\n');
        }

        return sb.toString();
    }

    public String consultarVehiculos() {

        StringBuilder sb = new StringBuilder("VEHICULOS\n");

        for (Vehiculo vehiculo : vehiculos) {
            sb.append(vehiculo).append('\n');
        }

        return sb.toString();
    }

    public String reporteCantidades() {

        Map<String, Map<EstadoEnvio, Integer>> conteos =
            new TreeMap<>();

        for (Envio envio : envios) {

            conteos.computeIfAbsent(
                envio.getCategoria(),
                categoria -> new EnumMap<>(EstadoEnvio.class)
            ).merge(envio.getEstado(), 1, Integer::sum);
        }

        StringBuilder sb = new StringBuilder(
            "CANTIDADES POR CATEGORIA Y ESTADO\n"
        );

        for (Map.Entry<String, Map<EstadoEnvio, Integer>> entrada
                : conteos.entrySet()) {

            int total = 0;

            for (int cantidad : entrada.getValue().values()) {
                total += cantidad;
            }

            sb.append(entrada.getKey())
              .append(": total ")
              .append(total);

            for (EstadoEnvio estado : EstadoEnvio.values()) {

                sb.append(" | ")
                  .append(estado)
                  .append(": ")
                  .append(entrada.getValue().getOrDefault(estado, 0));
            }

            sb.append('\n');
        }

        return sb.toString();
    }

    public String reporteIngresos() {

        double totalRegistro = 0;
        double totalReintentos = 0;

        for (double monto : registros.values()) {
            totalRegistro += monto;
        }

        for (double monto : reintentos.values()) {
            totalReintentos += monto;
        }

        StringBuilder sb = new StringBuilder();

        sb.append(String.format(
            "INGRESOS\nRegistros: Q%.2f\n"
            + "Reintentos: Q%.2f\nTotal: Q%.2f\n",
            totalRegistro,
            totalReintentos,
            totalRegistro + totalReintentos
        ));

        Set<String> categorias = new TreeSet<>();

        for (Envio envio : envios) {
            categorias.add(envio.getCategoria());
        }

        for (String categoria : categorias) {

            double ingresoRegistro =
                registros.getOrDefault(categoria, 0.0);

            double ingresoReintento =
                reintentos.getOrDefault(categoria, 0.0);

            sb.append(String.format(
                "%s: registros Q%.2f | reintentos Q%.2f"
                + " | total Q%.2f\n",
                categoria,
                ingresoRegistro,
                ingresoReintento,
                ingresoRegistro + ingresoReintento
            ));
        }

        return sb.toString();
    }

    public String reporteDespachos() {

        StringBuilder sb = new StringBuilder(
            "DESPACHOS ABIERTOS\n"
        );

        for (Despacho despacho : despachos) {

            if (despacho.getEstado() != EstadoDespacho.CERRADO) {

                sb.append(String.format(
                    "%s | %s | %s | carga %.2f kg"
                    + " | disponible %.2f kg\n",
                    despacho.getCodigo(),
                    despacho.getVehiculo().getPlaca(),
                    despacho.getEstado(),
                    despacho.pesoCargado(),
                    despacho.capacidadDisponible()
                ));
            }
        }

        return sb.toString();
    }

    private void acumularIngreso(
            Map<String, Double> mapa,
            String categoria,
            double monto) {

        mapa.merge(categoria, monto, Double::sum);
    }
}
