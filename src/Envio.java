public abstract class Envio {

    private final String codigo;
    private final String remitente;
    private final String destinatario;
    private final double pesoReal;
    private final Destino destino;

    private EstadoEnvio estado;
    private int intentos;

    protected Envio(
            String codigo,
            String remitente,
            String destinatario,
            double pesoReal,
            Destino destino) {

        this.codigo = validarTexto(codigo, "Codigo");
        this.remitente = validarTexto(remitente, "Remitente");
        this.destinatario = validarTexto(destinatario, "Destinatario");

        validarPositivo(pesoReal, "Peso real");

        if (destino == null) {
            throw new IllegalArgumentException("Destino obligatorio");
        }

        this.pesoReal = pesoReal;
        this.destino = destino;
        this.estado = EstadoEnvio.BODEGA;
        this.intentos = 0;
    }

    protected static String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                campo + " no puede estar vacio"
            );
        }

        return valor.trim();
    }

    protected static void validarPositivo(double valor, String campo) {
        if (!Double.isFinite(valor) || valor <= 0) {
            throw new IllegalArgumentException(
                campo + " debe ser positivo y finito"
            );
        }
    }

    public abstract double calcularCosto();

    public abstract int diasEntrega();

    public abstract int maxIntentos();

    public abstract String getCategoria();

    public boolean esCompatible(Vehiculo vehiculo) {
        return vehiculo != null;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getRemitente() {
        return remitente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public double getPesoReal() {
        return pesoReal;
    }

    public Destino getDestino() {
        return destino;
    }

    public EstadoEnvio getEstado() {
        return estado;
    }

    public int getIntentos() {
        return intentos;
    }

    void asignar() {
        if (estado != EstadoEnvio.BODEGA) {
            throw new IllegalStateException(
                "El envio no esta en bodega"
            );
        }

        if (intentos >= maxIntentos()) {
            throw new IllegalStateException(
                "El envio agoto sus intentos"
            );
        }

        estado = EstadoEnvio.ASIGNADO;
    }

    void retirar() {
        if (estado != EstadoEnvio.ASIGNADO) {
            throw new IllegalStateException(
                "El envio no esta asignado"
            );
        }

        estado = EstadoEnvio.BODEGA;
    }

    void iniciarIntento() {
        if (estado != EstadoEnvio.ASIGNADO) {
            throw new IllegalStateException(
                "El envio no esta asignado"
            );
        }

        if (intentos >= maxIntentos()) {
            throw new IllegalStateException(
                "El envio agoto sus intentos"
            );
        }

        intentos++;
        estado = EstadoEnvio.EN_RUTA;
    }

    void registrarResultado(boolean exito) {
        if (estado != EstadoEnvio.EN_RUTA) {
            throw new IllegalStateException(
                "El envio no esta en ruta"
            );
        }

        if (exito) {
            estado = EstadoEnvio.ENTREGADO;
        } else if (intentos >= maxIntentos()) {
            estado = EstadoEnvio.DEVUELTO;
        } else {
            estado = EstadoEnvio.BODEGA;
        }
    }

    public String descripcion() {
        return String.format(
            "%s | %s | %s -> %s | %.2f kg | %s | %s | intentos %d/%d",
            codigo,
            getCategoria(),
            remitente,
            destinatario,
            pesoReal,
            destino,
            estado,
            intentos,
            maxIntentos()
        );
    }
}
