public class CargaRefrigerada extends Envio {
    private double temperaturaRequerida;

    public CargaRefrigerada(String codigo, String remitente, String destinatario, double pesoReal, boolean esUrbano,
                            double temperaturaRequerida) {
        super(codigo, remitente, destinatario, pesoReal, esUrbano);
        if (!esUrbano) {
            throw new IllegalArgumentException("Solo se aceptan cargas refrigeradas con destino urbano.");
        }
        if (temperaturaRequerida < -25.0 || temperaturaRequerida > 8.0) {
            throw new IllegalArgumentException("La temperatura requerida debe estar entre -25 °C y 8 °C.");
        }
        this.temperaturaRequerida = temperaturaRequerida;
    }

    @Override
    public double calcularCosto() {
        double tarifaBase = isEsUrbano() ? 25.0 : 40.0;
        return tarifaBase + (12.0 * getPesoReal()) + 60.0;
    }

    @Override
    public int calcularTiempo() {
        return 1;
    }

    @Override
    public int getLimiteIntentos() {
        return 1;
    }

    public double getTemperaturaRequerida() {
        return temperaturaRequerida;
    }

    public void setTemperaturaRequerida(double temperaturaRequerida) {
        if (temperaturaRequerida < -25.0 || temperaturaRequerida > 8.0) {
            throw new IllegalArgumentException("La temperatura requerida debe estar entre -25 °C y 8 °C.");
        }
        this.temperaturaRequerida = temperaturaRequerida;
    }
}