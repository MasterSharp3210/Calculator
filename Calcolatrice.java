import java.awt.*; 
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class Calcolatrice extends JFrame implements ActionListener {

    JTextField display;
    String operatore = "";
    double num1 = 0;

    public Calcolatrice() {
        setTitle("Calcolatrice");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 50));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(5, 4, 5, 5));

        String[] tasti = {
            "√", "x²", "÷", "X",
            "7", "8", "9", "-",
            "4", "5", "6", "+",
            "1", "2", "3", ".",
            "C", "0", "#", "=" 
        };

        for (String t : tasti) {
            JButton b = new JButton(t);
            b.setFont(new Font("Arial", Font.BOLD, 18));
            b.addActionListener(this);
            panel.add(b);
        }

        add(panel, BorderLayout.CENTER);

        // --- KEY BINDINGS PROFESSIONALI ---
        setupKeyBindings();

        setVisible(true);
    }

    private void setupKeyBindings() {
        JComponent comp = this.getRootPane();
        InputMap im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = comp.getActionMap();

        // Numeri 0-9 (tastiera normale e tastierino)
        for (int i = 0; i <= 9; i++) {
            final String num = String.valueOf(i);

            im.put(KeyStroke.getKeyStroke(num), num);
            im.put(KeyStroke.getKeyStroke("NUMPAD" + i), num);

            am.put(num, new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    display.setText(display.getText() + num);
                }
            });
        }

        // Punto
        im.put(KeyStroke.getKeyStroke('.'), "dot");
        im.put(KeyStroke.getKeyStroke("DECIMAL"), "dot");
        am.put("dot", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!display.getText().contains(".")) {
                    display.setText(display.getText() + ".");
                }
            }
        });

        // Operatori
        bindOperator("+");
        bindOperator("-");
        bindOperator("*");
        bindOperator("/");

        // Invio = risultato
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "equals");
        am.put("equals", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                calcolaRisultato();
            }
        });
    }

    private void bindOperator(String op) {
        JComponent comp = this.getRootPane();
        InputMap im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = comp.getActionMap();

        im.put(KeyStroke.getKeyStroke(op), op);

        am.put(op, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!display.getText().isEmpty()) {
                    num1 = Double.parseDouble(display.getText());

                    if (op.equals("*")) operatore = "X";
                    else if (op.equals("/")) operatore = "÷";
                    else operatore = op;

                    display.setText("");
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (comando.matches("[0-9]")) {
            display.setText(display.getText() + comando);
        } else if (comando.matches("[+\\-*/X÷]")) {
            if (!display.getText().isEmpty()) {
                num1 = Double.parseDouble(display.getText());
                operatore = comando;
                display.setText("");
            }
        } else if (comando.equals("=")) {
            calcolaRisultato();
        } else if (comando.equals("C")) {
            display.setText("");
            num1 = 0;
            operatore = "";
        } 
        else if (comando.equals("√")) {
            if (!display.getText().isEmpty()) {
                double valore = Double.parseDouble(display.getText());
                display.setText(String.valueOf(Math.sqrt(valore)));
            }
        } else if (comando.equals("x²")) {
            if (!display.getText().isEmpty()) {
                double valore = Double.parseDouble(display.getText());
                display.setText(String.valueOf(Math.pow(valore, 2)));
            }
        } else if (comando.equals(".")) {
            if (!display.getText().contains(".")) {
                display.setText(display.getText() + ".");
            }
        } 
        else if (comando.equals("#")) {
            apriAppunti();
        }
    }

    private void calcolaRisultato() {
        if (!operatore.isEmpty() && !display.getText().isEmpty()) {
            double num2 = Double.parseDouble(display.getText());
            double risultato = 0;

            switch (operatore) {
                case "+": risultato = num1 + num2; break;
                case "-": risultato = num1 - num2; break;
                case "X": risultato = num1 * num2; break;
                case "÷": risultato = num1 / num2; break;
            }

            display.setText(String.valueOf(risultato));
            operatore = "";
        }
    }

    private void apriAppunti() {
        JFrame finestraAppunti = new JFrame("Appunti");
        finestraAppunti.setSize(300, 400);

        JTextArea areaTesto = new JTextArea();
        areaTesto.setLineWrap(true);
        areaTesto.setFont(new Font("Arial", Font.PLAIN, 16));
        areaTesto.setWrapStyleWord(true);

        File file = new File("appunti.txt");

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                areaTesto.read(reader, null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        finestraAppunti.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        finestraAppunti.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    areaTesto.write(writer);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        finestraAppunti.add(new JScrollPane(areaTesto));
        finestraAppunti.setVisible(true);
    }

    public static void main(String[] args) {
        new Calcolatrice();
    }
}
