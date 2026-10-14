public class Sobre extends Envio {

    public Sobre(
            String codigo,
            String remitente,
            String destinatario,
            double pesoReal,
            Destino destino) {

        super(codigo, remitente, destinatario, pesoReal, destino);

        if (pesoReal > 0.5) {
            throw new IllegalArgumentException(
                "El sobre no puede superar 0.5 kg"
            );
        }
    }

    @Override
    public double calcularCosto() {
        return getDestino() == Destino.URBANO ? 20 : 35;
    }

    @Override
    public int diasEntrega() {
        return getDestino() == Destino.URBANO ? 1 : 2;
    }

    @Override
    public int maxIntentos() {
        return 3;
    }

    @Override
    public String getCategoria() {
        return "Sobre";
    }
}
