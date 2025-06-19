import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class TemperatureConverter2D extends JFrame {
    private JSlider temperatureSlider;
    private JTextField temperatureInput;
    private JLabel celsiusLabel, fahrenheitLabel, kelvinLabel;
    private JComboBox<String> unitSelector;
    private JPanel colorIndicator;
    private DecimalFormat df = new DecimalFormat("0.00");

    public TemperatureConverter2D() {
        setTitle("ThermoConvert 2D Pro");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Main panel with padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Unit selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Select Unit:"), gbc);
        
        gbc.gridx = 1;
        unitSelector = new JComboBox<>(new String[]{"Celsius", "Fahrenheit", "Kelvin"});
        unitSelector.addActionListener(new UnitChangeListener());
        mainPanel.add(unitSelector, gbc);

        // Temperature input field
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Enter Temperature:"), gbc);
        
        gbc.gridx = 1;
        temperatureInput = new JTextField(10);
        temperatureInput.addActionListener(new InputListener());
        mainPanel.add(temperatureInput, gbc);

        // Temperature slider
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        temperatureSlider = new JSlider(JSlider.HORIZONTAL, -100, 200, 0);
        temperatureSlider.setMajorTickSpacing(50);
        temperatureSlider.setMinorTickSpacing(10);
        temperatureSlider.setPaintTicks(true);
        temperatureSlider.setPaintLabels(true);
        temperatureSlider.addChangeListener(new SliderChangeListener());
        mainPanel.add(temperatureSlider, gbc);

        // Color indicator
        gbc.gridy = 3;
        colorIndicator = new JPanel();
        colorIndicator.setPreferredSize(new Dimension(400, 60));
        colorIndicator.setBackground(getTemperatureColor(0));
        mainPanel.add(colorIndicator, gbc);

        // Temperature displays
        JPanel tempDisplayPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        celsiusLabel = new JLabel("0.00 °C", SwingConstants.CENTER);
        fahrenheitLabel = new JLabel("32.00 °F", SwingConstants.CENTER);
        kelvinLabel = new JLabel("273.15 K", SwingConstants.CENTER);
        
        Font labelFont = new Font("SansSerif", Font.BOLD, 18);
        celsiusLabel.setFont(labelFont);
        fahrenheitLabel.setFont(labelFont);
        kelvinLabel.setFont(labelFont);
        
        tempDisplayPanel.add(celsiusLabel);
        tempDisplayPanel.add(fahrenheitLabel);
        tempDisplayPanel.add(kelvinLabel);
        
        gbc.gridy = 4;
        mainPanel.add(tempDisplayPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    private class SliderChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (!temperatureSlider.getValueIsAdjusting()) {
                temperatureInput.setText(df.format(temperatureSlider.getValue()));
                updateTemperatures(temperatureSlider.getValue());
            }
        }
    }

    private class UnitChangeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateTemperatures(getCurrentTemperature());
        }
    }

    private class InputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                double temp = Double.parseDouble(temperatureInput.getText());
                temperatureSlider.setValue((int) Math.round(temp));
                updateTemperatures(temp);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(TemperatureConverter2D.this,
                    "Please enter a valid number",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
                temperatureInput.setText(df.format(temperatureSlider.getValue()));
            }
        }
    }

    private double getCurrentTemperature() {
        try {
            return Double.parseDouble(temperatureInput.getText());
        } catch (NumberFormatException e) {
            return temperatureSlider.getValue();
        }
    }

    private void updateTemperatures(double temp) {
        String unit = (String) unitSelector.getSelectedItem();
        
        double celsius, fahrenheit, kelvin;
        
        switch (unit) {
            case "Celsius":
                celsius = temp;
                fahrenheit = (celsius * 9/5) + 32;
                kelvin = celsius + 273.15;
                break;
            case "Fahrenheit":
                fahrenheit = temp;
                celsius = (fahrenheit - 32) * 5/9;
                kelvin = celsius + 273.15;
                break;
            case "Kelvin":
                kelvin = temp;
                celsius = kelvin - 273.15;
                fahrenheit = (celsius * 9/5) + 32;
                break;
            default:
                celsius = 0;
                fahrenheit = 32;
                kelvin = 273.15;
        }
        
        celsiusLabel.setText(df.format(celsius) + " °C");
        fahrenheitLabel.setText(df.format(fahrenheit) + " °F");
        kelvinLabel.setText(df.format(kelvin) + " K");
        
        colorIndicator.setBackground(getTemperatureColor(celsius));
    }

    private Color getTemperatureColor(double celsius) {
        float intensity = (float) Math.min(1, Math.max(0, (celsius + 20) / 120));
        return new Color(intensity, 0.3f, 1 - intensity);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new TemperatureConverter2D().setVisible(true);
        });
    }
}