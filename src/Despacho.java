import java.util.ArrayList;
import java.util.List;

public class Despacho {
    private Vehiculo vehiculo;
    private List<Envio> envios;
    private String estado;
    private double pesoActual;

    public Despacho(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El despacho requiere un vehículo válido.");
        }
        this.vehiculo = vehiculo;
        this.envios = new ArrayList<>();
        this.estado = "En preparación";
        this.pesoActual = 0.0;
        this.vehiculo.setEnUso(true);
    }

    public boolean agregarEnvio(Envio e) {
        if (e == null || !"En preparación".equals(this.estado)) {
            return false;
        }
        if (!"En bodega".equals(e.getEstado()) || this.envios.contains(e)) {
            return false;
        }
        if (this.pesoActual + e.getPesoReal() > this.vehiculo.getCapacidadMax()) {
            return false;
        }
        if (e.getLimiteIntentos() == 1 && !this.vehiculo.isRefrigerado()) {
            return false;
        }
        this.envios.add(e);
        this.pesoActual += e.getPesoReal();
        return true;
    }

    public boolean retirarEnvio(Envio e) {
        if (e == null || !"En preparación".equals(this.estado)) {
            return false;
        }
        if (this.envios.remove(e)) {
            this.pesoActual -= e.getPesoReal();
            if (this.pesoActual < 0) {
                this.pesoActual = 0.0;
            }
            e.setEstado("En bodega");
            return true;
        }
        return false;
    }

    public void iniciarRuta() {
        if (!"En preparación".equals(this.estado)) {
            throw new IllegalStateException("El despacho debe estar 'En preparación' para salir a ruta.");
        }
        if (this.envios.isEmpty()) {
            throw new IllegalStateException("No puede salir un despacho vacío.");
        }
        this.estado = "En ruta";
        for (Envio e : this.envios) {
            e.setEstado("En ruta");
        }
    }

    public void cerrar() {
        if (!"En ruta".equals(this.estado)) {
            throw new IllegalStateException("Solo se puede cerrar un despacho que esté 'En ruta'.");
        }
        for (Envio e : this.envios) {
            if ("En ruta".equals(e.getEstado())) {
                throw new IllegalStateException("Todos los envíos deben tener resultado registrado antes de cerrar el despacho.");
            }
        }
        this.estado = "Cerrado";
        this.vehiculo.setEnUso(false);
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public List<Envio> getEnvios() {
        return envios;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getPesoActual() {
        return pesoActual;
    }

    public void setPesoActual(double pesoActual) {
        this.pesoActual = pesoActual;
    }
}