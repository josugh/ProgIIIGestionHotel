
package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Locale;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaFacturacion extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTable tablaReservas;
    private JTable tablaExtras;
    private JTable tablaResumen;

    private DefaultTableModel modeloReservas;
    private DefaultTableModel modeloExtras;
    private DefaultTableModel modeloResumen;

    // Componentes para el hilo
    private JProgressBar barraProgreso;
    private JButton btnCalcular;

    // Constructor
    public VentanaFacturacion() {
        setTitle("Gestion Hotel - Facturacion");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        crearVentana();
        cargarDatosPrueba();
        calcularFactura();
    }

    // Creamos la ventana con las tres tablas
    private void crearVentana() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setContentPane(panelPrincipal);

        JPanel panelTablas = new JPanel(new GridLayout(3, 1, 10, 10));

        // TABLA 1: Datos de la reserva
        String[] columnasReservas = {
                "Reserva", "Cliente", "Habitacion",
                "Noches", "Precio/noche"
        };

        modeloReservas = new DefaultTableModel(columnasReservas, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaReservas = new JTable(modeloReservas);

        JScrollPane scrollReservas = new JScrollPane(tablaReservas);

        scrollReservas.setBorder(
                BorderFactory.createTitledBorder("Datos de la reserva"));

        panelTablas.add(scrollReservas);

        // TABLA 2: Servicios extra
        String[] columnasExtras = {
                "Servicio", "Cantidad", "Precio unitario", "Subtotal"
        };

        modeloExtras = new DefaultTableModel(columnasExtras, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaExtras = new JTable(modeloExtras);

        JScrollPane scrollExtras = new JScrollPane(tablaExtras);

        scrollExtras.setBorder(
                BorderFactory.createTitledBorder("Servicios extra"));

        panelTablas.add(scrollExtras);

        // TABLA 3: Resumen de la factura
        String[] columnasResumen = {"Concepto", "Importe"};

        modeloResumen = new DefaultTableModel(columnasResumen, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                // Solo se puede modificar el descuento
                return fila == 2 && columna == 1;
            }
        };

        tablaResumen = new JTable(modeloResumen);

        JScrollPane scrollResumen = new JScrollPane(tablaResumen);

        scrollResumen.setBorder(
                BorderFactory.createTitledBorder("Resumen de la factura"));

        panelTablas.add(scrollResumen);

        panelPrincipal.add(panelTablas, BorderLayout.CENTER);

        // BOTONES
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnCalcular = new JButton("Calcular factura");
        JButton btnGuardar = new JButton("Guardar factura");
        JButton btnExportar = new JButton("Exportar TXT");

        // Estos botones los programaremos mas adelante
        btnGuardar.setEnabled(false);
        btnExportar.setEnabled(false);

        panelBotones.add(btnCalcular);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnExportar);

        // BARRA DE PROGRESO
        barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setStringPainted(true);
        barraProgreso.setVisible(false);

        // Panel inferior para la barra y los botones
        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));

        panelInferior.add(barraProgreso, BorderLayout.NORTH);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        // Evento del boton calcular
        btnCalcular.addActionListener(e -> calcularFactura());
    }

    // Cargamos datos de prueba
    private void cargarDatosPrueba() {

        modeloReservas.addRow(new Object[]{
                "1001", "Juan Garcia", "203", 3, 90.0
        });

        modeloExtras.addRow(new Object[]{
                "Desayuno", 2, 12.50, 25.00
        });

        modeloExtras.addRow(new Object[]{
                "Aparcamiento", 1, 15.00, 15.00
        });

        modeloResumen.addRow(new Object[]{"Habitacion", ""});
        modeloResumen.addRow(new Object[]{"Servicios extra", ""});
        modeloResumen.addRow(new Object[]{"Descuento (%)", "0"});
        modeloResumen.addRow(new Object[]{"TOTAL", ""});
    }

    // Calculamos el precio final de la factura
    private void calcularFactura() {

        // Guardamos el descuento si lo estamos editando
        if (tablaResumen.isEditing()) {
            if (!tablaResumen.getCellEditor().stopCellEditing()) {
                return;
            }
        }

        // Obtenemos las noches y el precio
        int noches = ((Number)
                modeloReservas.getValueAt(0, 3)).intValue();

        double precioNoche = ((Number)
                modeloReservas.getValueAt(0, 4)).doubleValue();

        double importeHabitacion = noches * precioNoche;

        // Sumamos los servicios extra
        double importeExtras = 0;

        for (int i = 0; i < modeloExtras.getRowCount(); i++) {

            double subtotal = ((Number)
                    modeloExtras.getValueAt(i, 3)).doubleValue();

            importeExtras += subtotal;
        }

        // Obtenemos el descuento de la tabla
        double descuento;

        try {
            String textoDescuento = modeloResumen
                    .getValueAt(2, 1).toString()
                    .replace(",", ".");

            descuento = Double.parseDouble(textoDescuento);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this,
                    "El descuento debe ser un numero.");
            return;
        }

        // Comprobamos que el descuento sea correcto
        if (!Double.isFinite(descuento)
                || descuento < 0 || descuento > 100) {

            JOptionPane.showMessageDialog(this,
                    "El descuento debe estar entre 0 y 100.");
            return;
        }

        // Calculamos el total
        double subtotalFactura = importeHabitacion + importeExtras;

        double total = subtotalFactura -
                (subtotalFactura * descuento / 100);

        // Mostramos la habitacion y los extras
        modeloResumen.setValueAt(
                formatoEuros(importeHabitacion), 0, 1);

        modeloResumen.setValueAt(
                formatoEuros(importeExtras), 1, 1);

        // Solo usamos el hilo si hay descuento
        if (descuento > 0) {

            modeloResumen.setValueAt("Calculando...", 3, 1);
            calcularConHilo(total);

        } else {

            barraProgreso.setVisible(false);
            modeloResumen.setValueAt(formatoEuros(total), 3, 1);
        }
    }

    // Hilo para simular el calculo del descuento
    private void calcularConHilo(double total) {

        // Preparamos la barra
        barraProgreso.setValue(0);
        barraProgreso.setString("Calculando descuento...");
        barraProgreso.setVisible(true);

        // Desactivamos el boton mientras trabaja el hilo
        btnCalcular.setEnabled(false);

        Thread hilo = new Thread(() -> {

            try {

                // Vamos aumentando la barra de 20 en 20
                for (int i = 20; i <= 100; i += 20) {

                    Thread.sleep(400);

                    int progreso = i;

                    SwingUtilities.invokeLater(() -> {
                        barraProgreso.setValue(progreso);
                    });
                }

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                SwingUtilities.invokeLater(() -> {
                    barraProgreso.setString("Calculo interrumpido");
                    modeloResumen.setValueAt("No calculado", 3, 1);
                    btnCalcular.setEnabled(true);
                });

                return;
            }

            // termina el hilo y se muestra el resultado
            SwingUtilities.invokeLater(() -> {

                modeloResumen.setValueAt(
                        formatoEuros(total), 3, 1);

                barraProgreso.setValue(100);
                barraProgreso.setString("Descuento aplicado");

                btnCalcular.setEnabled(true);
            });

        });

        hilo.start();
    }
    
    
    private String formatoEuros(double cantidad) {

        return String.format(
                Locale.forLanguageTag("es-ES"), "%.2f €", cantidad);
    }

    // MAIN 
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            VentanaFacturacion ventana = new VentanaFacturacion();

            ventana.setVisible(true);
        });
    }
}
