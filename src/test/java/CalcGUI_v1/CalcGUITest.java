package CalcGUI_v1;

import org.junit.jupiter.api.Test;

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
}


