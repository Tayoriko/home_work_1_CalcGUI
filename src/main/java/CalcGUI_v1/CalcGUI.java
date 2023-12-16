package CalcGUI_v1;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.swing.KeyStroke.getKeyStroke;

public class CalcGUI implements ActionListener {
    private final String PREFIX_VK = "VK_";
    private final String PREFIX_VK_NUMPAD = "VK_NUMPAD";
    private JFrame frame;
    private JTextField fieldInput;
    private JTextArea fieldHistory;
    final JButton[] keyNumbers = new JButton[10];
    final List<JButton> keyFunction = new ArrayList<>();
    private JButton keyAdd, keySub, keyMul, keyDiv, keyInv;
    private JButton keyDec, keyDel, keyEqu, keyClr;
    private JPanel panel;
    private Font myFont = new Font("Arial", Font.BOLD, 16);
    private double valueOne = 0;
    private double valueTwo = 0;
    private double result = 0;
    char function;
    private InputMap inputMap = new InputMap();
    private String mapKey = "";
    private String history = "";


    private void frameCreate() {
        frame = new JFrame("Вау, кулькулятор?!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 450);
        frame.setLayout(null);
    }

    private void initKeys() {
        keyAdd = new JButton("+");
        keySub = new JButton("-");
        keyMul = new JButton("*");
        keyDiv = new JButton("/");
        keyDec = new JButton(".");
        keyDel = new JButton("DEL");
        keyClr = new JButton("CLR");
        keyEqu = new JButton("=");
        keyInv = new JButton("INV");

        keyFunction.add(keyAdd);
        keyFunction.add(keySub);
        keyFunction.add(keyMul);
        keyFunction.add(keyDiv);
        keyFunction.add(keyDec);
        keyFunction.add(keyDel);
        keyFunction.add(keyClr);
        keyFunction.add(keyEqu);
        keyFunction.add(keyInv);

        for (JButton i : keyFunction) {
            i.addActionListener(this);
            i.setFont(myFont);
            i.setFocusable(false); //чтобы не работал выбор через TAB
        }

        for (int i = 0; i < 10; i++) {
            keyNumbers[i] = new JButton(String.valueOf(i));
            keyNumbers[i].addActionListener(this);
            keyNumbers[i].setFont(myFont);
            keyNumbers[i].setFocusable(false); //чтобы не работал выбор через TAB
        }

    }

    private void configPanel() {
        panel = new JPanel();
        panel.setBounds(25, 100, 275, 275);
        panel.setLayout(new GridLayout(4, 4, 10, 10));
        panel.setBackground(Color.DARK_GRAY);
        //row 1
        panel.add(keyNumbers[1]);
        panel.add(keyNumbers[2]);
        panel.add(keyNumbers[3]);
        panel.add(keyAdd);
        //row 2
        panel.add(keyNumbers[4]);
        panel.add(keyNumbers[5]);
        panel.add(keyNumbers[6]);
        panel.add(keySub);
        //row 3
        panel.add(keyNumbers[7]);
        panel.add(keyNumbers[8]);
        panel.add(keyNumbers[9]);
        panel.add(keyMul);
        //row 4
        panel.add(keyDec);
        panel.add(keyNumbers[0]);
        panel.add(keyEqu);
        panel.add(keyDiv);

    }

    private void configKeys() {
        fieldInput = new JTextField();
        fieldInput.setBounds(25, 25, 275, 25);
        fieldInput.setFont(myFont);
        fieldInput.setEditable(false);

        fieldHistory = new JTextArea();
        fieldHistory.setBounds(315, 25, 150, 350);
        fieldHistory.setFont(myFont);
        fieldHistory.setEditable(false);
        fieldHistory.setText("Operations log:");

        initKeys();

        keyDel.setBounds(25, 60, 75, 25);
        keyInv.setBounds(125, 60, 75, 25);
        keyClr.setBounds(225, 60, 75, 25);
    }

    private void frameComponents() {
        frame.add(fieldInput);
        frame.add(fieldHistory);
        frame.add(keyDel);
        frame.add(keyInv);
        frame.add(keyClr);
        frame.add(panel);
    }

    private void configList() {
        frameCreate();
        configKeys();
        configPanel();
        frameComponents();
    }

    public void CalcGUI() {
        configList();
        frame.setVisible(true);
        keyboardControl();
    }

    private void keyboardControl() {
        checkAction(keyAdd, KeyEvent.VK_EQUALS, "ADD", PREFIX_VK);
        checkAction(keySub, KeyEvent.VK_SUBTRACT, "SUBTRACT", PREFIX_VK);
        checkAction(keySub, KeyEvent.VK_MINUS, "MINUS", PREFIX_VK);
        checkAction(keyMul, KeyEvent.VK_8, "MULTIPLY", PREFIX_VK);
        checkAction(keyDiv, KeyEvent.VK_SLASH, "DIVIDE", PREFIX_VK);
        for (int i = 0; i < 10; i++) {
            checkNumbers(keyNumbers[i], String.valueOf(i), PREFIX_VK);
            checkNumbers(keyNumbers[i], String.valueOf(i), PREFIX_VK_NUMPAD);
        }
        checkAction(keyEqu, KeyEvent.VK_ENTER, "ENTER", PREFIX_VK);
        checkAction(keyClr, KeyEvent.VK_ESCAPE, "ESCAPE", PREFIX_VK);
        checkAction(keyDel, KeyEvent.VK_BACK_SPACE, "BACK_SPACE", PREFIX_VK);
        checkAction(keyDec, KeyEvent.VK_DECIMAL, "DECIMAL", PREFIX_VK);
    }

    private void checkNumbers(JButton key, String function, String prefix) {
        mapKey = prefix + function;
        inputMap = key.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(function), mapKey);
        key.getActionMap().put(mapKey, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                actionNumbers(e);
            }
        });
    }

    private void checkAction(JButton key, int event, String function, String prefix) {
        mapKey = prefix + function;
        inputMap = key.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        if (event != KeyEvent.VK_SLASH) {
            inputMap.put(getKeyStroke(event, InputEvent.SHIFT_MASK), mapKey);
        } else if (event == KeyEvent.VK_SLASH) {
            inputMap.put(getKeyStroke(function), "VK_SLASH");
        }
        inputMap.put(getKeyStroke(function), mapKey);
        key.getActionMap().put(mapKey, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                selectAction(function, e);
            }
        });
    }

    private void selectAction(String function, ActionEvent e) {
        if (function.equals("ADD") || function.equals("SUBTRACT") || function.equals("MINUS")
                || function.equals("MULTIPLY") || function.equals("DIVIDE")) {
            actionFunction(e);
            return;
        }
        if (function.equals("ENTER")) {
            actionEqual(e);
            return;
        }
        if (function.equals("ESCAPE") || function.equals("BACK_SPACE") || function.equals("DECIMAL")) {
            actionOptions(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actionNumbers(e);
        actionFunction(e);
        actionEqual(e);
        actionOptions(e);
    }

    private void actionNumbers(ActionEvent e) {
        //add numbers
        for (int i = 1; i < 10; i++) {
            if (fieldInput.getText().equals("0")) {
                fieldInput.setText("");
            }
            if (e.getSource() == keyNumbers[i]) {
                fieldInput.setText(fieldInput.getText().concat(String.valueOf(i)));
            }
        }

        //add zero
        if (e.getSource() == keyNumbers[0]) {
            if (!fieldInput.getText().equals("0")) {
                fieldInput.setText(fieldInput.getText().concat(String.valueOf(0)));
            }
        }

        //add decimal
        if (e.getSource() == keyDec) {
            String regex = "\\.";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(fieldInput.getText());
            if (!matcher.find()) {
                fieldInput.setText(fieldInput.getText().concat("."));
            }
        }
    }

    private void actionFunction(ActionEvent e) {
        if (!Objects.equals(fieldInput.getText(), "")) {
            if (e.getSource() == keyAdd) {
                valueOne = Double.parseDouble(fieldInput.getText());
                function = '+';
                fieldInput.setText("");
                return;
            }
            //functional keys - sub
            if (e.getSource() == keySub) {
                valueOne = Double.parseDouble(fieldInput.getText());
                function = '-';
                fieldInput.setText("");
                return;
            }
            //functional keys - mul
            if (e.getSource() == keyMul) {
                valueOne = Double.parseDouble(fieldInput.getText());
                function = '*';
                fieldInput.setText("");
                return;
            }
            //functional keys - div
            if (e.getSource() == keyDiv) {
                valueOne = Double.parseDouble(fieldInput.getText());
                function = '/';
                fieldInput.setText("");
            }
        }
    }

    private void actionEqual(ActionEvent e) {
        if (e.getSource() == keyEqu && valueOne != 0.0) {
            valueTwo = Double.parseDouble(fieldInput.getText());
            switch (function) {
                case '+':
                    result = valueOne + valueTwo;
                    break;
                case '-':
                    result = valueOne - valueTwo;
                    break;
                case '*':
                    result = valueOne * valueTwo;
                    break;
                case '/':
                    result = valueOne / valueTwo;
                    break;
            }
            fieldInput.setText(String.valueOf(result));
            history = fieldHistory.getText() + "\n"
                    + valueOne + " " + function + " " + valueTwo + " = " + result;
            fieldHistory.setText(history);
            int linesCnt = fieldHistory.getLineCount();
            int linesLimit = 15;
            if (linesCnt > linesLimit) {
                try {
                    fieldHistory.replaceRange("", fieldHistory.getLineStartOffset(1), fieldHistory.getLineStartOffset(2));
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
            }
            valueOne = result;
        }
    }

    private void actionOptions(ActionEvent e) {
        if (e.getSource() == keyClr) {
            fieldInput.setText("");
        }
        if (e.getSource() == keyDel) {
            String text = String.valueOf(fieldInput.getText());
            fieldInput.setText("");
            for (int i = 0; i < text.length() - 1; i++) {
                fieldInput.setText(fieldInput.getText() + text.charAt(i));
            }
        }
        if (e.getSource() == keyInv) {
            double tmp = Double.parseDouble(fieldInput.getText());
            fieldInput.setText(String.valueOf(-tmp));
        }
    }
}
