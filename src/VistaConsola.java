import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class VistaConsola {
    private Scanner scanner;

    public VistaConsola() {
        this.scanner = new Scanner(System.in);
    }

    public int mostrarMenuPrincipal() {
        System.out.println("\n================ MENÚ PRINCIPAL - ENVÍAGT ================");
        System.out.println("1. Cotizar y registrar envío");
        System.out.println("2. Registrar vehículo");
        System.out.println("3. Gestionar despachos (crear, asignar, retirar, salir a ruta)");
        System.out.println("4. Registrar resultados de entrega");
        System.out.println("5. Consultar envíos, vehículos y reportes gerenciales");
        System.out.println("0. Salir");
        System.out.println("==========================================================");
        while (true) {
            System.out.print("Seleccione una opción: ");
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[Error] Entrada inválida. Ingrese un número entero.");
            }
        }
    }

    public Map<String, Object> solicitarDatosEnvio() {
        Map<String, Object> datos = new HashMap<>();
        int modo = 0;
        while (modo < 1 || modo > 4) {
            System.out.println("\n--- OPERACIÓN DE ENVÍO ---");
            System.out.println("1. Ingresar datos de nuevo envío para cotización");
            System.out.println("2. Responder confirmación de cotización (Aceptar / Rechazar)");
            System.out.println("3. Ingresar código de envío existente (para asignar o retirar de despacho)");
            System.out.println("4. Ingresar resultado de entrega del envío en ruta (Entregado / Fallido)");
            System.out.print("Seleccione opción (1-4): ");
            try {
                modo = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[Error] Ingrese un número del 1 al 4.");
            }
        }
        datos.put("modo", modo);

        if (modo == 1) {
            int categoria = 0;
            while (categoria < 1 || categoria > 4) {
                System.out.print("Categoría ([1] Sobre | [2] Paquete | [3] Paquete Frágil | [4] Carga Refrigerada): ");
                try {
                    categoria = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("[Error] Ingrese un número del 1 al 4.");
                }
            }
            datos.put("categoria", categoria);

            System.out.print("Código del envío: ");
            datos.put("codigo", scanner.nextLine().trim());

            System.out.print("Nombre del remitente: ");
            datos.put("remitente", scanner.nextLine().trim());

            System.out.print("Nombre del destinatario: ");
            datos.put("destinatario", scanner.nextLine().trim());

            while (true) {
                System.out.print("Peso real en kg: ");
                try {
                    datos.put("pesoReal", Double.parseDouble(scanner.nextLine().trim()));
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("[Error] Ingrese un valor numérico válido.");
                }
            }

            int dest = 0;
            while (dest != 1 && dest != 2) {
                System.out.print("Destino ([1] Urbano - Ciudad de Guatemala | [2] Departamental): ");
                try {
                    dest = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("[Error] Ingrese 1 o 2.");
                }
            }
            datos.put("esUrbano", dest == 1);

            if (categoria == 2 || categoria == 3) {
                String[] dims = {"largo", "ancho", "alto"};
                for (String d : dims) {
                    while (true) {
                        System.out.print("Ingrese " + d + " en cm: ");
                        try {
                            datos.put(d, Double.parseDouble(scanner.nextLine().trim()));
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("[Error] Ingrese un valor numérico válido.");
                        }
                    }
                }
            }

            if (categoria == 3) {
                while (true) {
                    System.out.print("Valor declarado en Quetzales (Q): ");
                    try {
                        datos.put("valorDeclarado", Double.parseDouble(scanner.nextLine().trim()));
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("[Error] Ingrese un monto numérico válido.");
                    }
                }
            }

            if (categoria == 4) {
                while (true) {
                    System.out.print("Temperatura requerida en °C (-25 a 8): ");
                    try {
                        datos.put("temperatura", Double.parseDouble(scanner.nextLine().trim()));
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("[Error] Ingrese una temperatura numérica válida.");
                    }
                }
            }
        } else if (modo == 2) {
            System.out.print("¿El cliente acepta la cotización para registrar y cobrar el envío? (S/N): ");
            String resp = scanner.nextLine().trim();
            datos.put("aceptar", resp.equalsIgnoreCase("S") || resp.equalsIgnoreCase("SI") || resp.equalsIgnoreCase("SÍ"));
        } else if (modo == 3) {
            System.out.print("Ingrese el código del envío: ");
            datos.put("codigo", scanner.nextLine().trim());
        } else {
            int res = 0;
            while (res != 1 && res != 2) {
                System.out.print("Resultado de entrega ([1] Entregado | [2] Entrega fallida): ");
                try {
                    res = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("[Error] Ingrese 1 o 2.");
                }
            }
            datos.put("exito", res == 1);
        }

        return datos;
    }

    public Map<String, Object> solicitarDatosVehiculo() {
        Map<String, Object> datos = new HashMap<>();
        int modo = 0;
        while (modo < 1 || modo > 5) {
            System.out.println("\n--- OPERACIÓN DE VEHÍCULO / DESPACHO ---");
            System.out.println("1. Registrar nuevo vehículo en la flotilla");
            System.out.println("2. Crear nuevo despacho con un vehículo disponible");
            System.out.println("3. Asignar envío a un despacho en preparación");
            System.out.println("4. Retirar envío de un despacho en preparación");
            System.out.println("5. Dar salida a ruta / Seleccionar despacho en ruta");
            System.out.print("Seleccione opción (1-5): ");
            try {
                modo = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[Error] Ingrese un número del 1 al 5.");
            }
        }
        datos.put("modo", modo);

        System.out.print("Ingrese la placa del vehículo: ");
        datos.put("placa", scanner.nextLine().trim());

        if (modo == 1) {
            while (true) {
                System.out.print("Capacidad máxima de carga en kg: ");
                try {
                    datos.put("capacidadMax", Double.parseDouble(scanner.nextLine().trim()));
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("[Error] Ingrese un valor numérico válido.");
                }
            }

            int ref = 0;
            while (ref != 1 && ref != 2) {
                System.out.print("¿Cuenta con refrigeración? ([1] Sí | [2] No): ");
                try {
                    ref = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("[Error] Ingrese 1 o 2.");
                }
            }
            datos.put("refrigerado", ref == 1);
        }

        return datos;
    }

    public void mostrarMensaje(String m) {
        System.out.println(m);
    }

    public void mostrarReporte(String r) {
        System.out.println(r);
    }
}