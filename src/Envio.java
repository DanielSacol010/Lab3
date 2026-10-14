public abstract class Envio {
    private String codigo;
    private String remitente;
    private String destinatario;
    private double pesoReal;
    private boolean esUrbano;
    private String estado;
    private int intentos;

    public Envio(String codigo, String remitente, String destinatario, double pesoReal, boolean esUrbano) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del envío no puede estar vacío.");
        }
        if (remitente == null || remitente.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del remitente no puede estar vacío.");
        }
        if (destinatario == null || destinatario.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del destinatario no puede estar vacío.");
        }
        if (pesoReal <= 0) {
            throw new IllegalArgumentException("El peso real debe ser mayor a 0 kg.");
        }
        this.codigo = codigo.trim();
        this.remitente = remitente.trim();
        this.destinatario = destinatario.trim();
        this.pesoReal = pesoReal;
        this.esUrbano = esUrbano;
        this.estado = "En bodega";
        this.intentos = 0;
    }

    public abstract double calcularCosto();
    public abstract int calcularTiempo();
    public abstract int getLimiteIntentos();

    public void registrarIntento(boolean exito) {
        if (!"En ruta".equals(this.estado)) {
            throw new IllegalStateException("Solo se puede registrar el resultado de un envío que está 'En ruta'.");
        }
        this.intentos++;
        if (exito) {
            this.estado = "Entregado";
        } else {
            if (this.intentos >= getLimiteIntentos()) {
                this.estado = "Devuelto al remitente";
            } else {
                this.estado = "En bodega";
            }
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        this.codigo = codigo.trim();
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        if (remitente == null || remitente.trim().isEmpty()) {
            throw new IllegalArgumentException("El remitente no puede estar vacío.");
        }
        this.remitente = remitente.trim();
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        if (destinatario == null || destinatario.trim().isEmpty()) {
            throw new IllegalArgumentException("El destinatario no puede estar vacío.");
        }
        this.destinatario = destinatario.trim();
    }

    public double getPesoReal() {
        return pesoReal;
    }

    public void setPesoReal(double pesoReal) {
        if (pesoReal <= 0) {
            throw new IllegalArgumentException("El peso real debe ser mayor a 0 kg.");
        }
        this.pesoReal = pesoReal;
    }

    public boolean isEsUrbano() {
        return esUrbano;
    }

    public void setEsUrbano(boolean esUrbano) {
        this.esUrbano = esUrbano;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado no puede estar vacío.");
        }
        this.estado = estado.trim();
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        if (intentos < 0) {
            throw new IllegalArgumentException("El número de intentos no puede ser negativo.");
        }
        this.intentos = intentos;
    }
}