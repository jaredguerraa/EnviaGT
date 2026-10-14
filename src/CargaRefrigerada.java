public class CargaRefrigerada extends Envio {

    private final double temperatura;

    public CargaRefrigerada(
            String codigo,
            String remitente,
            String destinatario,
            double pesoReal,
            Destino destino,
            double temperatura) {

        super(codigo, remitente, destinatario, pesoReal, destino);

        if (destino != Destino.URBANO) {
            throw new IllegalArgumentException(
                "Carga refrigerada solo acepta destino urbano"
            );
        }

        if (!Double.isFinite(temperatura)
                || temperatura < -25
                || temperatura > 8) {

            throw new IllegalArgumentException(
                "La temperatura debe estar entre -25 y 8 grados"
            );
        }

        this.temperatura = temperatura;
    }

    @Override
    public double calcularCosto() {
        return 25 + 12 * getPesoReal() + 60;
    }

    @Override
    public int diasEntrega() {
        return 1;
    }

    @Override
    public int maxIntentos() {
        return 1;
    }

    @Override
    public String getCategoria() {
        return "CargaRefrigerada";
    }

    @Override
    public boolean esCompatible(Vehiculo vehiculo) {
        return vehiculo != null && vehiculo.isRefrigerado();
    }

    @Override
    public String descripcion() {
        return super.descripcion()
                + String.format(
                    " | temperatura %.2f C",
                    temperatura
                );
    }
}
