public class Sobre extends Envio {

    public Sobre(String codigo, String remitente, String destinatario, double pesoReal, boolean esUrbano) {
        super(codigo, remitente, destinatario, pesoReal, esUrbano);
        if (pesoReal > 0.5) {
            throw new IllegalArgumentException("El peso de un sobre no puede superar 0.5 kg.");
        }
    }

    @Override
    public double calcularCosto() {
        return isEsUrbano() ? 20.0 : 35.0;
    }

    @Override
    public int calcularTiempo() {
        return isEsUrbano() ? 1 : 2;
    }

    @Override
    public int getLimiteIntentos() {
        return 3;
    }
}