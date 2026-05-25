import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.*;
public class Calculator extends JFrame {
    public int prioritate_operatori(char operator) {
        if (operator == '+' || operator == '-') return 1;
        if (operator == '*' || operator == '/') return 3;
        return 0;
    }

    public double Operatii(double a, double b, char operator) {
        return switch (operator) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> a / b;
            default -> 0;
        };
    }

    private double evaluateExpression(String expr) throws Exception {
        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (c == ' ') continue;

            // number
            if (Character.isDigit(c) || c == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < expr.length() &&
                        (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    sb.append(expr.charAt(i++));
                }
                i--;
                values.push(Double.parseDouble(sb.toString()));
            }

            // open paren
            else if (c == '(') {
                ops.push(c);
            }

            // close paren
            else if (c == ')') {
                while (!ops.isEmpty() && ops.peek() != '(') {
                    double b = values.pop();
                    double a = values.pop();
                    values.push(Operatii(a, b, ops.pop()));
                }
                ops.pop();
            }

            // operator
            else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!ops.isEmpty() &&
                        prioritate_operatori(ops.peek()) >= prioritate_operatori(c)) {
                    double b = values.pop();
                    double a = values.pop();
                    values.push(Operatii(a, b, ops.pop()));
                }
                ops.push(c);
            }
        }

        while (!ops.isEmpty()) {
            double b = values.pop();
            double a = values.pop();
            values.push(Operatii(a, b, ops.pop()));
        }

        return values.pop();
    }
    JButton[] digits = {
            new JButton(" 0 "),
            new JButton(" 1 "),
            new JButton(" 2 "),
            new JButton(" 3 "),
            new JButton(" 4 "),
            new JButton(" 5 "),
            new JButton(" 6 "),
            new JButton(" 7 "),
            new JButton(" 8 "),
            new JButton(" 9 ")
    };

    JButton []operators = {
            new JButton(" + "),
            new JButton(" - "),
            new JButton(" * "),
            new JButton(" / "),
            new JButton(" = "),
            new JButton(" C "),
            new JButton(" ( "),
            new JButton(" ) ")


    };

    String[] oper_values = {"+", "-", "*", "/", "=", "","(",")"};

    String value;
    char operator;

    JTextArea area = new JTextArea(3, 5);

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
       // calculator.pack();
        calculator.setSize(300, 240);
        calculator.setTitle(" Java-Calc, PP Lab1 ");
        calculator.setResizable(true);
        calculator.setVisible(true);
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Calculator() {
        add(new JScrollPane(area), BorderLayout.NORTH);
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new FlowLayout());

        for (JButton digit : digits)
            buttonpanel.add(digit);

        for (JButton operatori: operators)
            buttonpanel.add(operatori);

        add(buttonpanel, BorderLayout.CENTER);
        area.setForeground(Color.BLACK);
        area.setBackground(Color.WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);

        for (int i=0;i<digits.length;i++) {
            int finalI = i;
            digits[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    area.append(Integer.toString(finalI));
                }
            });
        }

        for (int i=0;i<operators.length;i++){
            int finalI = i;
            operators[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (finalI == 5)
                        area.setText("");
                    else
                    if (finalI == 4) {
                        try {
                            String expr = area.getText();
                            double result = evaluateExpression(expr);
                            area.append(" = " + result);
                        } catch (Exception e) {
                            area.setText(" !!!Probleme!!! ");
                        }
                    }
                    else {
                        area.append(oper_values[finalI]);
                        operator = oper_values[finalI].charAt(0);
                    }
                }
            });
        }
    }
}