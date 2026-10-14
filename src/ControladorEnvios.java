import java.util.Map;

public class ControladorEnvios {
    private Envios modelo;
    private VistaConsola vista;

    public ControladorEnvios() {
        this.modelo = new Envios();
        this.vista = new VistaConsola();
    }

    public void iniciar() {
        if (modelo.getInventario().isEmpty() && modelo.getFlotilla().isEmpty()) {
            modelo.getInventario().add(new Sobre("S-01", "Bufete Central", "Notaría Gómez", 0.25, true));
            modelo.getInventario().add(new Sobre("S-02", "Banco Industrial", "Sucursal Xela", 0.45, false));
            modelo.getInventario().add(new Paquete("P-01", "Tienda Electrónica", "Carlos Ruiz", 3.2, true, 40, 30, 20));
            modelo.getInventario().add(new Paquete("P-02", "Distribuidora Sur", "Ferretería Cobán", 6.5, false, 25, 20, 20));
            modelo.getInventario().add(new PaqueteFragil("PF-01", "Galería Arte", "Museo Nacional", 3.2, true, 40, 30, 20, 1200.0));
            modelo.getInventario().add(new PaqueteFragil("PF-02", "Cristalería Real", "Hotel Atitlán", 2.1, false, 30, 20, 15, 500.0));
            modelo.getInventario().add(new CargaRefrigerada("CR-01", "Farmacéutica GT", "Hospital Roosevelt", 4.0, true, 4.0));
            modelo.getInventario().add(new CargaRefrigerada("CR-02", "Lácteos del Valle", "Supermercado Zona 10", 8.5, true, -18.0));

            modelo.getFlotilla().add(new Vehiculo("C-101AAA", 25.0, false));
            modelo.getFlotilla().add(new Vehiculo("C-202BBB", 15.0, true));
            modelo.getFlotilla().add(new Vehiculo("C-303CCC", 5.0, false));
        }

        int opcion;
        do {
            opcion = vista.mostrarMenuPrincipal();
            switch (opcion) {
                case 1:
                    procesarCotizacionYRegistro();
                    break;
                case 2:
                    procesarRegistroVehiculo();
                    break;
                case 3:
                    procesarGestionDespachos();
                    break;
                case 4:
                    procesarResultadosEntrega();
                    break;
                case 5:
                    generarReportesGerenciales();
                    break;
                case 0:
                    vista.mostrarMensaje("Cerrando el sistema EnvíaGT.");
                    break;
                default:
                    vista.mostrarMensaje("[Operación rechazada] Opción de menú no válida.");
            }
        } while (opcion != 0);
    }

    private void procesarCotizacionYRegistro() {
        try {
            vista.mostrarMensaje("\n[Paso 1] Seleccione la opción 1 para ingresar los datos del envío a cotizar:");
            Map<String, Object> datos = vista.solicitarDatosEnvio();
            if ((int) datos.get("modo") != 1) {
                vista.mostrarMensaje("[Operación rechazada] Debía seleccionar la opción 1 para ingresar datos de cotización.");
                return;
            }

            String codigo = (String) datos.get("codigo");
            if (modelo.buscarEnvio(codigo) != null) {
                vista.mostrarMensaje("[Operación rechazada] Ya existe un envío registrado con el código: " + codigo);
                return;
            }

            int categoria = (int) datos.get("categoria");
            String remitente = (String) datos.get("remitente");
            String destinatario = (String) datos.get("destinatario");
            double pesoReal = (double) datos.get("pesoReal");
            boolean esUrbano = (boolean) datos.get("esUrbano");

            Envio nuevoEnvio;
            if (categoria == 1) {
                nuevoEnvio = new Sobre(codigo, remitente, destinatario, pesoReal, esUrbano);
            } else if (categoria == 2) {
                nuevoEnvio = new Paquete(codigo, remitente, destinatario, pesoReal, esUrbano,
                        (double) datos.get("largo"), (double) datos.get("ancho"), (double) datos.get("alto"));
            } else if (categoria == 3) {
                nuevoEnvio = new PaqueteFragil(codigo, remitente, destinatario, pesoReal, esUrbano,
                        (double) datos.get("largo"), (double) datos.get("ancho"), (double) datos.get("alto"),
                        (double) datos.get("valorDeclarado"));
            } else {
                nuevoEnvio = new CargaRefrigerada(codigo, remitente, destinatario, pesoReal, esUrbano,
                        (double) datos.get("temperatura"));
            }

            vista.mostrarMensaje(String.format("\n>>> COTIZACIÓN: Costo Total: Q%.2f | Tiempo estimado: %d día(s) hábil(es) <<<",
                    nuevoEnvio.calcularCosto(), nuevoEnvio.calcularTiempo()));
            vista.mostrarMensaje("[Paso 2] Seleccione la opción 2 para confirmar o descartar el registro:");

            Map<String, Object> confirmacion = vista.solicitarDatosEnvio();
            if ((int) confirmacion.get("modo") == 2 && (boolean) confirmacion.get("aceptar")) {
                modelo.registrarEnvio(nuevoEnvio);
                vista.mostrarMensaje(String.format("[Éxito] Envío '%s' registrado en bodega. Monto cobrado: Q%.2f",
                        nuevoEnvio.getCodigo(), nuevoEnvio.calcularCosto()));
            } else {
                vista.mostrarMensaje("[Información] Cotización no registrada. Los ingresos no fueron modificados.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            vista.mostrarMensaje("[Operación rechazada] " + e.getMessage());
        }
    }

    private void procesarRegistroVehiculo() {
        try {
            vista.mostrarMensaje("\nSeleccione la opción 1 para registrar un nuevo vehículo:");
            Map<String, Object> datos = vista.solicitarDatosVehiculo();
            if ((int) datos.get("modo") != 1) {
                vista.mostrarMensaje("[Operación rechazada] Debía seleccionar la opción 1 para registrar un vehículo.");
                return;
            }

            String placa = (String) datos.get("placa");
            for (Vehiculo existente : modelo.getFlotilla()) {
                if (existente.getPlaca().equalsIgnoreCase(placa)) {
                    vista.mostrarMensaje("[Operación rechazada] Ya existe un vehículo con la placa: " + placa);
                    return;
                }
            }

            Vehiculo nuevo = new Vehiculo(placa, (double) datos.get("capacidadMax"), (boolean) datos.get("refrigerado"));
            modelo.getFlotilla().add(nuevo);
            vista.mostrarMensaje("[Éxito] Vehículo registrado con placa: " + nuevo.getPlaca());
        } catch (IllegalArgumentException | IllegalStateException e) {
            vista.mostrarMensaje("[Operación rechazada] " + e.getMessage());
        }
    }

    private void procesarGestionDespachos() {
        try {
            vista.mostrarMensaje("\nSeleccione opción 2 (Crear despacho), 3 (Asignar envío), 4 (Retirar envío) o 5 (Dar salida a ruta):");
            Map<String, Object> datosV = vista.solicitarDatosVehiculo();
            int modo = (int) datosV.get("modo");
            String placa = (String) datosV.get("placa");

            if (modo == 2) {
                Vehiculo encontrado = null;
                for (Vehiculo v : modelo.getFlotilla()) {
                    if (v.getPlaca().equalsIgnoreCase(placa)) {
                        encontrado = v;
                        break;
                    }
                }
                Despacho d = modelo.crearDespacho(encontrado);
                vista.mostrarMensaje("[Éxito] Despacho creado en preparación para el vehículo " + d.getVehiculo().getPlaca());
                return;
            }

            Despacho despachoActivo = null;
            for (Despacho d : modelo.getDespachos()) {
                if (!"Cerrado".equals(d.getEstado()) && d.getVehiculo().getPlaca().equalsIgnoreCase(placa)) {
                    despachoActivo = d;
                    break;
                }
            }

            if (despachoActivo == null) {
                vista.mostrarMensaje("[Operación rechazada] No existe un despacho activo para el vehículo con placa: " + placa);
                return;
            }

            if (modo == 3) {
                if (!"En preparación".equals(despachoActivo.getEstado())) {
                    vista.mostrarMensaje("[Operación rechazada] El despacho no está 'En preparación'.");
                    return;
                }
                vista.mostrarMensaje("Seleccione opción 3 para ingresar el código del envío a asignar:");
                Map<String, Object> datosE = vista.solicitarDatosEnvio();
                if ((int) datosE.get("modo") != 3) {
                    vista.mostrarMensaje("[Operación rechazada] Opción incorrecta.");
                    return;
                }
                Envio env = modelo.buscarEnvio((String) datosE.get("codigo"));
                if (env == null) {
                    vista.mostrarMensaje("[Operación rechazada] Envío no encontrado.");
                    return;
                }
                // Verificar que no esté asignado a otro despacho abierto
                for (Despacho otro : modelo.getDespachos()) {
                    if (!"Cerrado".equals(otro.getEstado()) && otro.getEnvios().contains(env)) {
                        vista.mostrarMensaje("[Operación rechazada] El envío ya está asignado a un despacho activo.");
                        return;
                    }
                }
                if (despachoActivo.agregarEnvio(env)) {
                    vista.mostrarMensaje(String.format("[Éxito] Envío '%s' asignado. Peso cargado: %.2f / %.2f kg.",
                            env.getCodigo(), despachoActivo.getPesoActual(), despachoActivo.getVehiculo().getCapacidadMax()));
                } else {
                    vista.mostrarMensaje("[Operación rechazada] El envío no cumple las condiciones (estado en bodega, capacidad o refrigeración).");
                }
            } else if (modo == 4) {
                vista.mostrarMensaje("Seleccione opción 3 para ingresar el código del envío a retirar:");
                Map<String, Object> datosE = vista.solicitarDatosEnvio();
                if ((int) datosE.get("modo") != 3) {
                    vista.mostrarMensaje("[Operación rechazada] Opción incorrecta.");
                    return;
                }
                Envio env = modelo.buscarEnvio((String) datosE.get("codigo"));
                if (despachoActivo.retirarEnvio(env)) {
                    vista.mostrarMensaje("[Éxito] Envío retirado del despacho y devuelto a bodega.");
                } else {
                    vista.mostrarMensaje("[Operación rechazada] No se pudo retirar el envío (verifique que el despacho esté en preparación y contenga el envío).");
                }
            } else if (modo == 5) {
                despachoActivo.iniciarRuta();
                // Al salir el despacho, cada envío con al menos 1 intento previo genera un cargo de Q15.00
                for (Envio e : despachoActivo.getEnvios()) {
                    if (e.getIntentos() >= 1) {
                        modelo.registrarCargoExtra();
                    }
                }
                vista.mostrarMensaje("[Éxito] El despacho del vehículo " + placa + " ha salido a ruta.");
            } else {
                vista.mostrarMensaje("[Operación rechazada] Opción no válida para gestión de despachos.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            vista.mostrarMensaje("[Operación rechazada] " + e.getMessage());
        }
    }

    private void procesarResultadosEntrega() {
        try {
            vista.mostrarMensaje("\nSeleccione la opción 5 e ingrese la placa del vehículo cuyo despacho regresa de ruta:");
            Map<String, Object> datosV = vista.solicitarDatosVehiculo();
            if ((int) datosV.get("modo") != 5) {
                vista.mostrarMensaje("[Operación rechazada] Debía seleccionar la opción 5.");
                return;
            }
            String placa = (String) datosV.get("placa");

            Despacho despachoEnRuta = null;
            for (Despacho d : modelo.getDespachos()) {
                if ("En ruta".equals(d.getEstado()) && d.getVehiculo().getPlaca().equalsIgnoreCase(placa)) {
                    despachoEnRuta = d;
                    break;
                }
            }

            if (despachoEnRuta == null) {
                vista.mostrarMensaje("[Operación rechazada] No se encontró ningún despacho 'En ruta' con la placa: " + placa);
                return;
            }

            for (Envio e : despachoEnRuta.getEnvios()) {
                if ("En ruta".equals(e.getEstado())) {
                    vista.mostrarMensaje("\nRegistrando resultado para envío '" + e.getCodigo() + "' (Destinatario: " + e.getDestinatario() + ").");
                    vista.mostrarMensaje("Seleccione opción 4 para indicar si fue Entregado o Entrega fallida:");
                    Map<String, Object> datosE = vista.solicitarDatosEnvio();
                    while ((int) datosE.get("modo") != 4) {
                        vista.mostrarMensaje("[Error] Debe elegir la opción 4 para registrar el resultado de la entrega:");
                        datosE = vista.solicitarDatosEnvio();
                    }
                    boolean exito = (boolean) datosE.get("exito");
                    e.registrarIntento(exito);
                    vista.mostrarMensaje(String.format(" -> Envío '%s' actualizado a estado: %s (Intentos: %d/%d)",
                            e.getCodigo(), e.getEstado(), e.getIntentos(), e.getLimiteIntentos()));
                }
            }

            despachoEnRuta.cerrar();
            vista.mostrarMensaje("[Éxito] Despacho cerrado correctamente. El vehículo " + placa + " ahora está libre.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            vista.mostrarMensaje("[Operación rechazada] " + e.getMessage());
        }
    }

    private void generarReportesGerenciales() {
        vista.mostrarReporte(modelo.obtenerReportes());
    }
}