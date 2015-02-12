package cats.pixelthing.client.util;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LabelBox extends JPanel {

    public final JLabel label;
    public final JTextField box;

    public LabelBox(final String labelText, final String boxText, final boolean mask){
        super(new GridLayout(1, 2));
        label = new JLabel(labelText, JLabel.CENTER);

        box = mask ? new JPasswordField(boxText, 20) : new JTextField(boxText, 20);
        box.setHorizontalAlignment(JLabel.CENTER);

        add(label);
        add(box);
    }

    public LabelBox(final String labelText, final boolean mask){
        this(labelText, "", mask);
    }

    public String boxValue(){
        return box.getText().trim();
    }
}
