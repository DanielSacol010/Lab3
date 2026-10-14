import java.util.ArrayList;
import java.util.List;

public class Envios {
    private List<Envio> inventario;
    private List<Vehiculo> flotilla;
    private List<Despacho> despachos;
    private double ingresosRegistro;
    private double ingresosReintentos;

    public Envios() {
        this.inventario = new ArrayList<>();
        this.flotilla = new ArrayList<>();
        this.despachos = new ArrayList<>();
        this.ingresosRegistro = 0.0;
        this.ingresosReintentos = 0.0;
    }

    public Envio buscarEnvio(String c) {
        if (c == null) {
            return null;
        }
        for (Envio e : this.inventario) {
            if (e.getCodigo().equalsIgnoreCase(c.trim())) {
                return e;
            }
        }
        return null;
    }

    public void registrarEnvio(Envio e) {
        if (e == null) {
            throw new IllegalArgumentException("El envío no puede ser nulo.");
        }
        if (buscarEnvio(e.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un envío con el código: " + e.getCodigo());
        }
        this.inventario.add(e);
        this.ingresosRegistro += e.calcularCosto();
    }

    public Despacho crearDespacho(Vehiculo v) {
        if (v == null) {
            throw new IllegalArgumentException("El vehículo no existe.");
        }
        if (v.isEnUso()) {
            throw new IllegalStateException("El vehículo " + v.getPlaca() + " ya tiene un despacho sin cerrar.");
        }
        Despacho nuevo = new Despacho(v);
        this.despachos.add(nuevo);
        return nuevo;
    }

    public void registrarCargoExtra() {
        this.ingresosReintentos += 15.0;
    }

    public String obtenerReportes() {
        StringBuilder sb = new StringBuilder();
        String[] nombresCat = {"Sobre", "Paquete", "Paquete Frágil", "Carga Refrigerada"};
        String[] estados = {"En bodega", "En ruta", "Entregado", "Devuelto al remitente"};
        int[][] conteos = new int[4][4];
        double[] ingresosCat = new double[4];

        sb.append("\n============================================================\n");
        sb.append("        CONSULTA GENERAL Y REPORTES GERENCIALES             \n");
        sb.append("============================================================\n");

        sb.append("\n--- INVENTARIO DE ENVÍOS ---\n");
        for (int i = 0; i < inventario.size(); i++) {
            Envio e = inventario.get(i);
            int catIdx;
            if (e.getLimiteIntentos() == 1) {
                catIdx = 3; // Carga Refrigerada
            } else if (e.getLimiteIntentos() == 2) {
                catIdx = 2; // Paquete Frágil
            } else if ((e.isEsUrbano() && e.calcularTiempo() == 1) || (!e.isEsUrbano() && e.calcularTiempo() == 2)) {
                catIdx = 0; // Sobre
            } else {
                catIdx = 1; // Paquete
            }

            for (int s = 0; s < estados.length; s++) {
                if (estados[s].equals(e.getEstado())) {
                    conteos[catIdx][s]++;
                }
            }

            // Los primeros 8 envíos son los precargados al inicio sin ingreso de registro
            if (i >= 8) {
                ingresosCat[catIdx] += e.calcularCosto();
            }

            // Cada intento adicional que ya salió a ruta genera Q15.00 para su categoría
            int reintentosCobrados = "En ruta".equals(e.getEstado())
                    ? e.getIntentos()
                    : Math.max(0, e.getIntentos() - 1);
            ingresosCat[catIdx] += (reintentosCobrados * 15.0);

            sb.append(String.format(" [%s] %-17s | %s -> %s | Peso: %.2f kg | %s | Estado: %s | Intentos: %d/%d | Costo: Q%.2f | Entrega: %d día(s)\n",
                    e.getCodigo(), nombresCat[catIdx], e.getRemitente(), e.getDestinatario(),
                    e.getPesoReal(), (e.isEsUrbano() ? "Urbano" : "Departamental"),
                    e.getEstado(), e.getIntentos(), e.getLimiteIntentos(),
                    e.calcularCosto(), e.calcularTiempo()));
        }

        sb.append("\n--- FLOTILLA DE VEHÍCULOS ---\n");
        for (Vehiculo v : flotilla) {
            sb.append(String.format(" Placa: %-10s | Capacidad Máx: %6.2f kg | Refrigerado: %-2s | En uso: %s\n",
                    v.getPlaca(), v.getCapacidadMax(), (v.isRefrigerado() ? "Sí" : "No"), (v.isEnUso() ? "Sí" : "No")));
        }

        sb.append("\n1. CANTIDAD DE ENVÍOS POR CATEGORÍA Y ESTADO:\n");
        for (int c = 0; c < nombresCat.length; c++) {
            int totalCat = conteos[c][0] + conteos[c][1] + conteos[c][2] + conteos[c][3];
            sb.append(String.format(" * %s (Total: %d)\n", nombresCat[c], totalCat));
            for (int s = 0; s < estados.length; s++) {
                sb.append(String.format("   - %-22s: %d\n", estados[s], conteos[c][s]));
            }
        }

        sb.append("\n2. REPORTE DE INGRESOS:\n");
        sb.append(String.format(" - Ingresos por registro   : Q%.2f\n", ingresosRegistro));
        sb.append(String.format(" - Ingresos por reintentos : Q%.2f\n", ingresosReintentos));
        sb.append(String.format(" - INGRESO TOTAL           : Q%.2f\n", (ingresosRegistro + ingresosReintentos)));
        sb.append("   Ingresos desglosados por categoría:\n");
        for (int c = 0; c < nombresCat.length; c++) {
            sb.append(String.format("   * %-18s: Q%.2f\n", nombresCat[c], ingresosCat[c]));
        }

        sb.append("\n3. DESPACHOS NO CERRADOS:\n");
        boolean hayAbiertos = false;
        for (int i = 0; i < despachos.size(); i++) {
            Despacho d = despachos.get(i);
            if (!"Cerrado".equals(d.getEstado())) {
                hayAbiertos = true;
                double disp = d.getVehiculo().getCapacidadMax() - d.getPesoActual();
                sb.append(String.format(" - Despacho #%d | Estado: %s | Vehículo: %s | Peso cargado: %.2f kg | Capacidad disponible: %.2f kg\n",
                        (i + 1), d.getEstado(), d.getVehiculo().getPlaca(), d.getPesoActual(), disp));
            }
        }
        if (!hayAbiertos) {
            sb.append("   No existen despachos abiertos actualmente.\n");
        }
        sb.append("============================================================\n");
        return sb.toString();
    }

    public List<Envio> getInventario() {
        return inventario;
    }

    public List<Vehiculo> getFlotilla() {
        return flotilla;
    }

    public List<Despacho> getDespachos() {
        return despachos;
    }

    public double getIngresosRegistro() {
        return ingresosRegistro;
    }

    public double getIngresosReintentos() {
        return ingresosReintentos;
    }
}