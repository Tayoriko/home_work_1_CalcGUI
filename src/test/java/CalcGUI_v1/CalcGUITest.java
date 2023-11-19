package CalcGUI_v1;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

class CalcGUITest {
    @Test
    public void gui() {
        CalcGUI test = new CalcGUI();
        test.CalcGUI();
        int i = 0;
        for (;;) {

            String text = java.awt.event.KeyEvent.getKeyText(i);
            if (!text.contains("Unknown keyCode: ")) {
                System.out.println("" + i + " -- " + text);
            }
        }
    }

    @Test
    public void getKeyEvent()
    {
        for(int i = 0; i < 1000000; ++i) {
            String text = java.awt.event.KeyEvent.getKeyText(i);
            if(!text.contains("Unknown keyCode: ")) {
                System.out.println("" + i + " -- " + text);
            }
        }
    }

    @Test
    public void getKeyEnter()
    {
        InputMap inputMap = new InputMap();
        String mapKey = "";
        JFrame frame;
        JButton keyEnter = new JButton("ENTER");
        keyEnter.setBounds(25, 25, 100, 25);
        frame = new JFrame("Вау, кулькулятор?!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(150, 150);
        frame.setLayout(null);
        frame.add(keyEnter);
        frame.setVisible(true);
        //KeyEvent.VK_NUMPAD8
        String function = "VK_8";
        mapKey = "VK_" + function;
        inputMap = keyEnter.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_8, InputEvent.SHIFT_MASK), mapKey);
        keyEnter.getActionMap().put(mapKey, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("OMG");
            }
        });
        for (;;){

        }
    }
}


