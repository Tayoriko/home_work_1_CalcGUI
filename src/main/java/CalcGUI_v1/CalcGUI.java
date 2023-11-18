package CalcGUI_v1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalcGUI implements ActionListener {
    JFrame frame;
    JTextField textField;
    final JButton[] keyNumbers = new JButton[10];
    final JButton[] keyFunction = new JButton[9];
    JButton keyAdd, keySub, keyMul, keyDiv, keyInv;
    JButton keyDec, keyDel, keyEqu, keyClr;
    JPanel panel;
    Font myFont = new Font("Arial", Font.BOLD, 16);
    private double valueOne = 0;
    private double valueTwo = 0;
    private double result = 0;
    char function;
    InputMap inputMap = new InputMap();
    String mapKey = "";
    String value = "";


    private void frameCreate() {
        frame = new JFrame("Вау, кулькулятор?!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 450);
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

        keyFunction[0] = keyAdd;
        keyFunction[1] = keySub;
        keyFunction[2] = keyMul;
        keyFunction[3] = keyDiv;
        keyFunction[4] = keyDec;
        keyFunction[5] = keyDel;
        keyFunction[6] = keyClr;
        keyFunction[7] = keyEqu;
        keyFunction[8] = keyInv;

        for (int i = 0; i < 9; i++) {
            keyFunction[i].addActionListener(this);
            keyFunction[i].setFont(myFont);
            keyFunction[i].setFocusable(false); //чтобы не работал выбор через TAB
        }

        for (int i = 0; i < 10; i++) {
            keyNumbers[i] = new JButton(String.valueOf(i));
            keyNumbers[i].addActionListener(this);
            keyNumbers[i].setFont(myFont);
            keyNumbers[i].setFocusable(false); //чтобы не работал выбор через TAB
        }

        keyNumbers[0].setMnemonic(KeyEvent.VK_0);
        String mapKey = "VK_0";
        InputMap inputMap = keyNumbers[0].getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke("0"), mapKey);

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
        textField = new JTextField();
        textField.setBounds(25, 25, 275, 25);
        textField.setFont(myFont);
        textField.setEditable(false);

        keyDel.setBounds(25, 60, 75, 25);
        keyInv.setBounds(125, 60, 75, 25);
        keyClr.setBounds(225, 60, 75, 25);
    }

    private void frameComponents() {
        frame.add(textField);
        frame.add(keyDel);
        frame.add(keyInv);
        frame.add(keyClr);
        frame.add(panel);
    }

    private void configList() {
        frameCreate();
        initKeys();
        configKeys();
        configPanel();
        frameComponents();
    }

    public void CalcGUI() {
        configList();
        frame.setVisible(true);
        keyboardControl();
    }

    private void keyboardControl()
    {
        checkFunction(keyAdd, KeyEvent.VK_EQUALS, "ADD");
        checkFunction(keySub, KeyEvent.VK_SEMICOLON, "SUBTRACT");
        checkFunction(keyMul, KeyEvent.VK_8, "MULTIPLY");
        checkFunction(keyDiv, KeyEvent.VK_SLASH, "DIVIDE");
        for (int i = 0; i < 10; i++) {
            int eventID = 48 + i;
            checkNumbers(keyNumbers[i], String.valueOf(i));
        }
        checkEquals(keyDiv, "ENTER");
    }

    private void checkFunction(JButton key, int event, String function){
        mapKey = "VK_" + function;
        inputMap = key.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(event, InputEvent.SHIFT_MASK), mapKey);
        key.getActionMap().put(mapKey, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                actionFunction(e);
            }
        });
        inputMap.put(KeyStroke.getKeyStroke(function), mapKey);
        key.getActionMap().put(mapKey, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                actionFunction(e);
            }
        });
    }

    private void checkNumbers(JButton key, String function){
        mapKey = "VK_" + function;
        inputMap = key.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(function), mapKey);
        key.getActionMap().put(mapKey, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                actionNumbers(e);
            }
        });
    }

    private void checkEquals(JButton key, String function){
        mapKey = "VK_" + function;
        inputMap = key.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), mapKey);
        ActionMap ap = keyEqu.getActionMap();
        ap.put(mapKey, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                actionEqual(e);
            }
        });
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
            if (textField.getText().equals("0")) {
                textField.setText("");
            }
            if (e.getSource() == keyNumbers[i]) {
                textField.setText(textField.getText().concat(String.valueOf(i)));
            }
        }

        //add zero
        if (e.getSource() == keyNumbers[0]) {
            if (!textField.getText().equals("0")) {
                textField.setText(textField.getText().concat(String.valueOf(0)));
            }
        }

        //add decimal
        if (e.getSource() == keyDec) {
            String regex = "\\.";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(textField.getText());
            if (!matcher.find()) {
                textField.setText(textField.getText().concat("."));
            }
        }
    }

    private void actionFunction(ActionEvent e) {
        if (!Objects.equals(textField.getText(), "")) {
            if (e.getSource() == keyAdd) {
                valueOne = Double.parseDouble(textField.getText());
                function = '+';
                textField.setText("");
                return;
            }
            //functional keys - sub
            if (e.getSource() == keySub) {
                valueOne = Double.parseDouble(textField.getText());
                function = '-';
                textField.setText("");
                return;
            }
            //functional keys - mul
            if (e.getSource() == keyMul) {
                valueOne = Double.parseDouble(textField.getText());
                function = '*';
                textField.setText("");
                return;
            }
            //functional keys - div
            if (e.getSource() == keyDiv) {
                valueOne = Double.parseDouble(textField.getText());
                function = '/';
                textField.setText("");
            }
        }
    }

    private void actionEqual(ActionEvent e) {
        if (e.getSource() == keyEqu) {
            valueTwo = Double.parseDouble(textField.getText());
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
            textField.setText(String.valueOf(result));
            valueOne = result;
        }
    }

    private void actionOptions(ActionEvent e) {
        if (e.getSource() == keyClr) {
            textField.setText("");
        }
        if (e.getSource() == keyDel) {
            String text = String.valueOf(textField.getText());
            textField.setText("");
            for (int i = 0; i < text.length() - 1; i++) {
                textField.setText(textField.getText() + text.charAt(i));
            }
        }
        if (e.getSource() == keyInv) {
            double tmp = Double.parseDouble(textField.getText());
            textField.setText(String.valueOf(-tmp));
        }
    }
}
