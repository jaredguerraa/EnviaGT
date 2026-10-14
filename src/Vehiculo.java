public class Vehiculo {

    private final String placa;
    private final double capacidadMaxima;
    private final boolean refrigerado;

    public Vehiculo(
            String placa,
            double capacidadMaxima,
            boolean refrigerado) {

        this.placa = Envio.validarTexto(placa, "Placa");

        Envio.validarPositivo(capacidadMaxima, "Capacidad");

        this.capacidadMaxima = capacidadMaxima;
        this.refrigerado = refrigerado;
    }

    public String getPlaca() {
        return placa;
    }

    public double getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public boolean isRefrigerado() {
        return refrigerado;
    }

    @Override
    public String toString() {
        return String.format(
            "%s | %.2f kg | refrigerado: %s",
            placa,
            capacidadMaxima,
            refrigerado ? "Si" : "No"
        );
    }
}
