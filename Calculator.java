import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

// Coded by 85cs/Itelcan3 (aka. MasterSharp3210)
// Signed by Itelcan3 :33

public class Calculator extends JFrame implements ActionListener {

    private JTextField display;

    private double num1 = 0;
    private String operator = "";

    private boolean newNumber = true;

    public Calculator() {

        setTitle("Calculator");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 40));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);

        add(display, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(5, 4, 5, 5));

        String[] buttons = {
            "%", "x²", "√", "÷",
            "7", "8", "9", "X",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "C", "0", ".", "="
        };

        for (String t : buttons) {
            JButton b = new JButton(t);
            b.setFont(new Font("Arial", Font.BOLD, 18));
            b.addActionListener(this);
            panel.add(b);
        }

        add(panel, BorderLayout.CENTER);

        setupKeyBindings();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void appendNumber(String number) {

        if (newNumber) {
            display.setText(number.equals(".") ? "0." : number);
            newNumber = false;
            return;
        }

        if (number.equals(".") && display.getText().contains(".")) {
            return;
        }

        display.setText(display.getText() + number);
    }

    private void setOperator(String op) {

        if (!operator.isEmpty() && !newNumber) {
            calculate();
        }

        if (!operator.isEmpty() && newNumber) {
            operator = op;
            return;
        }

        num1 = Double.parseDouble(display.getText());
        operator = op;
        newNumber = true;
    }

    private void calculate() {

        if (operator.isEmpty() || newNumber) {
            return;
        }

        double num2 = Double.parseDouble(display.getText());
        double result;

        switch (operator) {

            case "+":
                result = num1 + num2;
                break;

            case "-":
                result = num1 - num2;
                break;

            case "X":
                result = num1 * num2;
                break;

            case "÷":

                if (num2 == 0) {
                    display.setText("Errore");
                    operator = "";
                    num1 = 0;
                    newNumber = true;
                    return;
                }

                result = num1 / num2;
                break;

            case "%":
                result = (num1 * num2) / 100;
                break;

            default:
                return;
        }

        num1 = result;
        display.setText(formatResult(result));

        operator = "";
        newNumber = true;
    }

    private String formatResult(double result) {

        if (Double.isNaN(result) || Double.isInfinite(result)) {
            return "Errore";
        }

        if (result == (long) result) {
            return String.valueOf((long) result);
        }

        return String.valueOf(result);
    }

    private void clear() {

        display.setText("0");
        num1 = 0;
        operator = "";
        newNumber = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();

        if (command.matches("[0-9]")) {
            appendNumber(command);
        }

        else if (command.matches("[+\\-X÷%]")) {
            setOperator(command);
        }

        else if (command.equals("=")) {
            calculate();
        }

        else if (command.equals("C")) {
            clear();
        }

        else if (command.equals(".")) {
            appendNumber(".");
        }

        else if (command.equals("√")) {

            double value = Double.parseDouble(display.getText());

            if (value < 0) {
                display.setText("Errore");
                newNumber = true;
                return;
            }

            double result = Math.sqrt(value);

            display.setText(formatResult(result));
            num1 = result;
            newNumber = true;
        }

        else if (command.equals("x²")) {

            double value = Double.parseDouble(display.getText());

            double result = Math.pow(value, 2);

            display.setText(formatResult(result));
            num1 = result;
            newNumber = true;
        }
    }

    private void setupKeyBindings() {

        JComponent comp = getRootPane();

        InputMap im = comp.getInputMap(
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        ActionMap am = comp.getActionMap();

        for (int i = 0; i <= 9; i++) {

            String num = String.valueOf(i);

            bindKey(im, am, num, num);
            bindKey(im, am, "NUMPAD" + i, num);
        }

        bindKey(im, am, ".", ".");
        bindKey(im, am, "DECIMAL", ".");

        bindKey(im, am, "+", "+");
        bindKey(im, am, "-", "-");
        bindKey(im, am, "*", "X");
        bindKey(im, am, "/", "÷");
        bindKey(im, am, "%", "%");

        bindKey(im, am, "ENTER", "=");
        bindKey(im, am, "BACK_SPACE", "BACKSPACE");
        bindKey(im, am, "ESCAPE", "C");
    }

    private void bindKey(
        InputMap im,
        ActionMap am,
        String key,
        String command
    ) {

        im.put(KeyStroke.getKeyStroke(key), command);

        am.put(command, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (command.equals("BACKSPACE")) {

                    if (!newNumber && !display.getText().isEmpty()) {

                        String text = display.getText();

                        if (text.length() > 1) {
                            display.setText(
                                text.substring(0, text.length() - 1)
                            );
                        } else {
                            display.setText("0");
                            newNumber = true;
                        }
                    }

                } else {
                    Calculator.this.actionPerformed(
                        new ActionEvent(
                            this,
                            ActionEvent.ACTION_PERFORMED,
                            command
                        )
                    );
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Calculator());
    }
}
