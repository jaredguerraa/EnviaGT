public class PaqueteFragil extends Paquete {

    private final double valorDeclarado;

    public PaqueteFragil(
            String codigo,
            String remitente,
            String destinatario,
            double pesoReal,
            Destino destino,
            double largo,
            double ancho,
            double alto,
            double valorDeclarado) {

        super(
            codigo,
            remitente,
            destinatario,
            pesoReal,
            destino,
            largo,
            ancho,
            alto
        );

        validarPositivo(valorDeclarado, "Valor declarado");

        this.valorDeclarado = valorDeclarado;
    }

    @Override
    public double calcularCosto() {
        return super.calcularCosto()
                + 15
                + 0.03 * valorDeclarado;
    }

    @Override
    public int diasEntrega() {
        return super.diasEntrega() + 1;
    }

    @Override
    public int maxIntentos() {
        return 2;
    }

    @Override
    public String getCategoria() {
        return "PaqueteFragil";
    }

    @Override
    public String descripcion() {
        return super.descripcion()
                + String.format(
                    " | valor declarado Q%.2f",
                    valorDeclarado
                );
    }
}
