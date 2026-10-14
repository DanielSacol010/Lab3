public class Vehiculo {
    private String placa;
    private double capacidadMax;
    private boolean refrigerado;
    private boolean enUso;

    public Vehiculo(String placa, double capacidadMax, boolean refrigerado) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa del vehículo no puede estar vacía.");
        }
        if (capacidadMax <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser mayor a 0 kg.");
        }
        this.placa = placa.trim();
        this.capacidadMax = capacidadMax;
        this.refrigerado = refrigerado;
        this.enUso = false;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa no puede estar vacía.");
        }
        this.placa = placa.trim();
    }

    public double getCapacidadMax() {
        return capacidadMax;
    }

    public void setCapacidadMax(double capacidadMax) {
        if (capacidadMax <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser mayor a 0 kg.");
        }
        this.capacidadMax = capacidadMax;
    }

    public boolean isRefrigerado() {
        return refrigerado;
    }

    public void setRefrigerado(boolean refrigerado) {
        this.refrigerado = refrigerado;
    }

    public boolean isEnUso() {
        return enUso;
    }

    public void setEnUso(boolean enUso) {
        this.enUso = enUso;
    }
}