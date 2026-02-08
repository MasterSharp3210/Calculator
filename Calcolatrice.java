import java.awt.*;
import java.awt.event.*;
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
            "√", "x²", "/", "*",
            "7", "8", "9", "-",
            "4", "5", "6", "+",
            "1", "2", "3", "#",
            "C", "0", ".", "=" 
        };

        for (String t : tasti) {
            JButton b = new JButton(t);
            b.setFont(new Font("Arial", Font.BOLD, 18));
            b.addActionListener(this);
            panel.add(b);
        }

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (comando.matches("[0-9]")) {
            display.setText(display.getText() + comando);
        } else if (comando.matches("[+\\-*/]")) {
            if (!display.getText().isEmpty()) {
                num1 = Double.parseDouble(display.getText());
                operatore = comando;
                display.setText("");
            }
        } else if (comando.equals("=")) {
            if (!operatore.isEmpty() && !display.getText().isEmpty()) {
                double num2 = Double.parseDouble(display.getText());
                double risultato = 0;

                switch (operatore) {
                    case "+": risultato = num1 + num2; break;
                    case "-": risultato = num1 - num2; break;
                    case "*": risultato = num1 * num2; break;
                    case "/": risultato = num1 / num2; break;
                }

                display.setText(String.valueOf(risultato));
                operatore = ""; // reset operatore dopo =
            } else if (!display.getText().isEmpty()) {
                // Nessuna operazione: mostra lo stesso numero
                display.setText(display.getText());
            } else {
                // Nessun numero: mostra 0
                display.setText("0");
            }
        } else if (comando.equals("C")) {
            display.setText("");
            num1 = 0;
            operatore = "";
        } 
        // operazioni
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
        // Bottone Appunti
        else if (comando.equals("#")) {
            apriAppunti();
        }
    }

    // Appunti Temporanei
    private void apriAppunti() {
        JFrame finestraAppunti = new JFrame("Appunti");
        finestraAppunti.setSize(300, 400);
        JTextArea areaTesto = new JTextArea();
        areaTesto.setLineWrap(true);
        areaTesto.setFont(new Font("Arial", Font.PLAIN, 20));
        areaTesto.setWrapStyleWord(true);
        finestraAppunti.add(new JScrollPane(areaTesto));
        finestraAppunti.setVisible(true);
    }

    public static void main(String[] args) {
        new Calcolatrice();
    }
}
