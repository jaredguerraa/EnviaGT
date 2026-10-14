public class Paquete extends Envio {

    private final double largo;
    private final double ancho;
    private final double alto;

    public Paquete(
            String codigo,
            String remitente,
            String destinatario,
            double pesoReal,
            Destino destino,
            double largo,
            double ancho,
            double alto) {

        super(codigo, remitente, destinatario, pesoReal, destino);

        validarPositivo(largo, "Largo");
        validarPositivo(ancho, "Ancho");
        validarPositivo(alto, "Alto");

        if (!Double.isFinite(largo * ancho * alto)) {
            throw new IllegalArgumentException(
                "Las dimensiones son demasiado grandes"
            );
        }

        this.largo = largo;
        this.ancho = ancho;
        this.alto = alto;
    }

    protected double pesoFacturable() {
        double pesoVolumetrico = largo * ancho * alto / 5000.0;

        return Math.ceil(
            Math.max(getPesoReal(), pesoVolumetrico)
        );
    }

    @Override
    public double calcularCosto() {
        double tarifaBase = getDestino() == Destino.URBANO ? 25 : 40;

        return tarifaBase + 8 * pesoFacturable();
    }

    @Override
    public int diasEntrega() {
        return getDestino() == Destino.URBANO ? 2 : 4;
    }

    @Override
    public int maxIntentos() {
        return 3;
    }

    @Override
    public String getCategoria() {
        return "Paquete";
    }

    @Override
    public String descripcion() {
        return super.descripcion()
                + String.format(
                    " | dimensiones %.2f x %.2f x %.2f cm"
                    + " | peso facturable %.0f kg",
                    largo,
                    ancho,
                    alto,
                    pesoFacturable()
                );
    }
}
