import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Calculator extends JFrame implements ActionListener {

    JTextField display;
    String operator = "";
    double num1 = 0;

    public Calculator() {
        setTitle("Calculator");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 50));
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

        setVisible(true);
    }

    private void setupKeyBindings() {
        JComponent comp = this.getRootPane();
        InputMap im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = comp.getActionMap();


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

        im.put(KeyStroke.getKeyStroke('.'), "dot");
        im.put(KeyStroke.getKeyStroke("DECIMAL"), "dot");
        am.put("dot", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!display.getText().contains(".")) {
                    display.setText(display.getText() + ".");
                }
            }
        });

        bindOperator("+");
        bindOperator("-");
        bindOperator("*");
        bindOperator("/");
        bindOperator("%");

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "equals");
        am.put("equals", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                calculate();
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

                    if (op.equals("*")) operator = "X";
                    else if (op.equals("/")) operator = "÷";
                    else operator = op;

                    display.setText("");
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.matches("[0-9]")) {
            display.setText(display.getText() + command);
        } else if (command.matches("[+\\-*/X÷%]")) {
            if (!display.getText().isEmpty()) {
                num1 = Double.parseDouble(display.getText());
                operator = command;
                display.setText("");
            }
        } else if (command.equals("=")) {
            calculate();
        } else if (command.equals("C")) {
            display.setText("");
            num1 = 0;
            operator = "";
        } 
        else if (command.equals("√")) {
            if (!display.getText().isEmpty()) {
                double value = Double.parseDouble(display.getText());
                display.setText(String.valueOf(Math.sqrt(value)));
            }
        } else if (command.equals("x²")) {
            if (!display.getText().isEmpty()) {
                double value = Double.parseDouble(display.getText());
                display.setText(String.valueOf(Math.pow(value, 2)));
            }
        } else if (command.equals(".")) {
            if (!display.getText().contains(".")) {
                display.setText(display.getText() + ".");
            }
        } 
    }

    private void calculate() {
        if (!operator.isEmpty() && !display.getText().isEmpty()) {
            double num2 = Double.parseDouble(display.getText());
            double result = 0;

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
                    result = num1 / num2;
                    break;
                case "%":
                    result = (num1 * num2) / 100;
                    break;
            }

            display.setText(String.valueOf(result));
            operator = "";
        }
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
