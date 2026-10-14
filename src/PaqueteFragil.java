public class PaqueteFragil extends Paquete {
    private double valorDeclarado;

    public PaqueteFragil(String codigo, String remitente, String destinatario, double pesoReal, boolean esUrbano,
                         double largo, double ancho, double alto, double valorDeclarado) {
        super(codigo, remitente, destinatario, pesoReal, esUrbano, largo, ancho, alto);
        if (valorDeclarado <= 0) {
            throw new IllegalArgumentException("El valor declarado debe ser mayor a Q0.00.");
        }
        this.valorDeclarado = valorDeclarado;
    }

    @Override
    public double calcularCosto() {
        return super.calcularCosto() + 15.0 + (0.03 * valorDeclarado);
    }

    @Override
    public int calcularTiempo() {
        return super.calcularTiempo() + 1;
    }

    @Override
    public int getLimiteIntentos() {
        return 2;
    }

    public double getValorDeclarado() {
        return valorDeclarado;
    }

    public void setValorDeclarado(double valorDeclarado) {
        if (valorDeclarado <= 0) {
            throw new IllegalArgumentException("El valor declarado debe ser mayor a Q0.00.");
        }
        this.valorDeclarado = valorDeclarado;
    }
}