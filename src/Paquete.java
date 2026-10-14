public class Paquete extends Envio {
    private double largo;
    private double ancho;
    private double alto;

    public Paquete(String codigo, String remitente, String destinatario, double pesoReal, boolean esUrbano,
                   double largo, double ancho, double alto) {
        super(codigo, remitente, destinatario, pesoReal, esUrbano);
        if (largo <= 0 || ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException("Las dimensiones (largo, ancho, alto) deben ser mayores a 0 cm.");
        }
        this.largo = largo;
        this.ancho = ancho;
        this.alto = alto;
    }

    public double calcularPesoVolumetrico() {
        return (largo * ancho * alto) / 5000.0;
    }

    @Override
    public double calcularCosto() {
        double tarifaBase = isEsUrbano() ? 25.0 : 40.0;
        double pesoFacturable = Math.ceil(Math.max(getPesoReal(), calcularPesoVolumetrico()));
        return tarifaBase + (pesoFacturable * 8.0);
    }

    @Override
    public int calcularTiempo() {
        return isEsUrbano() ? 2 : 4;
    }

    @Override
    public int getLimiteIntentos() {
        return 3;
    }

    public double getLargo() {
        return largo;
    }

    public void setLargo(double largo) {
        if (largo <= 0) {
            throw new IllegalArgumentException("El largo debe ser mayor a 0 cm.");
        }
        this.largo = largo;
    }

    public double getAncho() {
        return ancho;
    }

    public void setAncho(double ancho) {
        if (ancho <= 0) {
            throw new IllegalArgumentException("El ancho debe ser mayor a 0 cm.");
        }
        this.ancho = ancho;
    }

    public double getAlto() {
        return alto;
    }

    public void setAlto(double alto) {
        if (alto <= 0) {
            throw new IllegalArgumentException("El alto debe ser mayor a 0 cm.");
        }
        this.alto = alto;
    }
}