
package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Locale;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaServiciosExtra extends JFrame {

    private static final long serialVersionUID = 1L;

    private JComboBox<String> cbReserva;
    private JComboBox<String> cbServicio;

    private JSpinner spCantidad;
    private JLabel lblPrecio;
    private JLabel lblTotal;

    private JTable tablaServicios;
    private DefaultTableModel modeloServicios;

    // Constructor de la ventana
    public VentanaServiciosExtra() {

        setTitle("Gestion Hotel - Servicios Extra");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        crearVentana();
        cargarDatosPrueba();
    }

    // Creamos la ventana
    private void crearVentana() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setContentPane(panelPrincipal);

        JPanel panelSuperior = new JPanel(new GridLayout(2, 1, 5, 5));

        JPanel panelReserva = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelReserva.setBorder(
                BorderFactory.createTitledBorder("Datos de la reserva"));

        cbReserva = new JComboBox<>();

        panelReserva.add(new JLabel("Reserva:"));
        panelReserva.add(cbReserva);

        panelSuperior.add(panelReserva);

        // Panel para añadir servicios
        JPanel panelAnadir = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelAnadir.setBorder(
                BorderFactory.createTitledBorder("Añadir servicio"));

        cbServicio = new JComboBox<>();

        cbServicio.addItem("Desayuno");
        cbServicio.addItem("Aparcamiento");
        cbServicio.addItem("Lavanderia");
        cbServicio.addItem("Minibar");
        cbServicio.addItem("Spa");

        spCantidad = new JSpinner(
                new SpinnerNumberModel(1, 1, 20, 1));

        lblPrecio = new JLabel("0,00 €");

        JButton btnAnadir = new JButton("Añadir servicio");

        panelAnadir.add(new JLabel("Servicio:"));
        panelAnadir.add(cbServicio);

        panelAnadir.add(new JLabel("Cantidad:"));
        panelAnadir.add(spCantidad);

        panelAnadir.add(new JLabel("Precio:"));
        panelAnadir.add(lblPrecio);

        panelAnadir.add(btnAnadir);

        panelSuperior.add(panelAnadir);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
 
        String[] columnas = {
                "Servicio", "Cantidad", "Precio unitario", "Subtotal"
        };

        modeloServicios = new DefaultTableModel(columnas, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaServicios = new JTable(modeloServicios);

        JScrollPane scroll = new JScrollPane(tablaServicios);

        scroll.setBorder(
                BorderFactory.createTitledBorder("Servicios consumidos"));

        panelPrincipal.add(scroll, BorderLayout.CENTER);

        // PARTE INFERIOR
        JPanel panelInferior = new JPanel(new BorderLayout());

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        lblTotal = new JLabel("Total servicios: 0,00 €");

        panelTotal.add(lblTotal);

        panelInferior.add(panelTotal, BorderLayout.NORTH);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnEliminar = new JButton("Eliminar servicio");
        JButton btnGuardar = new JButton("Guardar consumos");

        // Guardar lo programaremos mas adelante
        btnGuardar.setEnabled(false);

        panelBotones.add(btnEliminar);
        panelBotones.add(btnGuardar);

        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        // EVENTOS DE LOS BOTONES
        btnAnadir.addActionListener(e -> anadirServicio());

        btnEliminar.addActionListener(e -> eliminarServicio());

        // Cuando cambiamos de servicio, actualizamos su precio
        cbServicio.addActionListener(e -> actualizarPrecio());
    }

    // Introducimos datos de ejemplo
    private void cargarDatosPrueba() {

        cbReserva.addItem("Reserva 1001 - Juan Garcia - Habitacion 203");

        modeloServicios.addRow(new Object[]{
                "Aparcamiento", 1, 15.0, 15.0
        });

        modeloServicios.addRow(new Object[]{
                "Minibar", 2, 8.0, 16.0
        });

        actualizarPrecio();
        actualizarTotal();
    }


    private double obtenerPrecio(String servicio) {

        if (servicio.equals("Desayuno")) {
            return 12.50;

        } else if (servicio.equals("Aparcamiento")) {
            return 15.0;

        } else if (servicio.equals("Lavanderia")) {
            return 10.0;

        } else if (servicio.equals("Minibar")) {
            return 8.0;

        } else if (servicio.equals("Spa")) {
            return 25.0;
        }

        return 0;
    }

    // Mostramos el precio del servicio seleccionado
    private void actualizarPrecio() {

        String servicio = (String) cbServicio.getSelectedItem();

        if (servicio != null) {

            double precio = obtenerPrecio(servicio);

            lblPrecio.setText(formatoEuros(precio));
        }
    }

    // Añadimos un servicio a la tabla
    private void anadirServicio() {

        String servicio = (String) cbServicio.getSelectedItem();

        if (servicio == null) {
            return;
        }

        int cantidad = (Integer) spCantidad.getValue();

        double precio = obtenerPrecio(servicio);

        double subtotal = precio * cantidad;

        modeloServicios.addRow(new Object[]{
                servicio, cantidad, precio, subtotal
        });


        actualizarTotal();
        spCantidad.setValue(1);
    }

    // Eliminamos el servicio seleccionado
    private void eliminarServicio() {

        int fila = tablaServicios.getSelectedRow();

        // Comprobamos que se haya seleccionado una fila
        if (fila == -1) {

            JOptionPane.showMessageDialog(this,
                    "Selecciona un servicio para eliminar.");

            return;
        }

        modeloServicios.removeRow(fila);

        actualizarTotal();
    }

    // Sumamos los subtotales de todos los servicios
    private void actualizarTotal() {

        double total = 0;

        for (int i = 0; i < modeloServicios.getRowCount(); i++) {

            double subtotal = ((Number)
                    modeloServicios.getValueAt(i, 3)).doubleValue();

            total += subtotal;
        }

        lblTotal.setText(
                "Total servicios: " + formatoEuros(total));
    }

    // Mostramos los precios con dos decimales
    private String formatoEuros(double cantidad) {

        return String.format(
                Locale.forLanguageTag("es-ES"), "%.2f €", cantidad);
    }

    // MAIN 
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            VentanaServiciosExtra ventana = new VentanaServiciosExtra();

            ventana.setVisible(true);
        });
    }
}
