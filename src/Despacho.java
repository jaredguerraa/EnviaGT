import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Despacho {

    private final String codigo;
    private final Vehiculo vehiculo;
    private final ArrayList<Envio> envios;
    private final Map<String, Boolean> resultados;

    private EstadoDespacho estado;

    public Despacho(String codigo, Vehiculo vehiculo) {

        this.codigo = Envio.validarTexto(
            codigo,
            "Codigo de despacho"
        );

        if (vehiculo == null) {
            throw new IllegalArgumentException(
                "Vehiculo obligatorio"
            );
        }

        this.vehiculo = vehiculo;
        this.envios = new ArrayList<>();
        this.resultados = new HashMap<>();
        this.estado = EstadoDespacho.PREPARACION;
    }

    public String getCodigo() {
        return codigo;
    }

    public EstadoDespacho getEstado() {
        return estado;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public double pesoCargado() {
        double peso = 0;

        for (Envio envio : envios) {
            peso += envio.getPesoReal();
        }

        return peso;
    }

    public double capacidadDisponible() {
        return vehiculo.getCapacidadMaxima() - pesoCargado();
    }

    List<Envio> getEnvios() {
        return Collections.unmodifiableList(
            new ArrayList<>(envios)
        );
    }

    boolean contieneEnvio(String codigoEnvio) {

        for (Envio envio : envios) {
            if (envio.getCodigo().equalsIgnoreCase(codigoEnvio)) {
                return true;
            }
        }

        return false;
    }

    boolean tieneResultado(String codigoEnvio) {
        return resultados.containsKey(
            codigoEnvio.toUpperCase(Locale.ROOT)
        );
    }

    void asignar(Envio envio) {

        if (estado != EstadoDespacho.PREPARACION) {
            throw new IllegalStateException(
                "Despacho no esta en preparacion"
            );
        }

        if (envio == null
                || envio.getEstado() != EstadoEnvio.BODEGA
                || envio.getIntentos() >= envio.maxIntentos()
                || contieneEnvio(envio.getCodigo())) {

            throw new IllegalStateException(
                "Envio no disponible"
            );
        }

        if (!envio.esCompatible(vehiculo)) {
            throw new IllegalStateException(
                "Vehiculo incompatible con el envio"
            );
        }

        if (envio.getPesoReal() > capacidadDisponible() + 1e-9) {
            throw new IllegalStateException(
                "Capacidad insuficiente"
            );
        }

        envio.asignar();
        envios.add(envio);
    }

    void retirar(String codigoEnvio) {

        if (estado != EstadoDespacho.PREPARACION) {
            throw new IllegalStateException(
                "Solo se retira en preparacion"
            );
        }

        for (Envio envio : envios) {
            if (envio.getCodigo().equalsIgnoreCase(codigoEnvio)) {
                envio.retirar();
                envios.remove(envio);
                return;
            }
        }

        throw new IllegalArgumentException(
            "Envio no pertenece al despacho"
        );
    }

    void validarSalida() {

        if (estado != EstadoDespacho.PREPARACION
                || envios.isEmpty()) {

            throw new IllegalStateException(
                "Despacho no puede salir vacio o fuera de preparacion"
            );
        }

        if (pesoCargado() > vehiculo.getCapacidadMaxima() + 1e-9) {
            throw new IllegalStateException(
                "Capacidad excedida"
            );
        }

        for (Envio envio : envios) {

            if (envio.getEstado() != EstadoEnvio.ASIGNADO
                    || envio.getIntentos() >= envio.maxIntentos()
                    || !envio.esCompatible(vehiculo)) {

                throw new IllegalStateException(
                    "Envio invalido para salida: "
                    + envio.getCodigo()
                );
            }
        }
    }

    void salir() {

        validarSalida();

        for (Envio envio : envios) {
            envio.iniciarIntento();
        }

        estado = EstadoDespacho.EN_RUTA;
    }

    void registrarResultado(String codigoEnvio, boolean exito) {

        if (estado != EstadoDespacho.EN_RUTA) {
            throw new IllegalStateException(
                "Despacho no esta en ruta"
            );
        }

        Envio elegido = null;

        for (Envio envio : envios) {
            if (envio.getCodigo().equalsIgnoreCase(codigoEnvio)) {
                elegido = envio;
                break;
            }
        }

        if (elegido == null) {
            throw new IllegalArgumentException(
                "Envio no pertenece al despacho"
            );
        }

        if (tieneResultado(elegido.getCodigo())) {
            throw new IllegalStateException(
                "Resultado ya registrado"
            );
        }

        elegido.registrarResultado(exito);

        resultados.put(
            elegido.getCodigo().toUpperCase(Locale.ROOT),
            exito
        );

        if (todosConResultado()) {
            estado = EstadoDespacho.CERRADO;
        }
    }

    private boolean todosConResultado() {
        return resultados.size() == envios.size();
    }
}
