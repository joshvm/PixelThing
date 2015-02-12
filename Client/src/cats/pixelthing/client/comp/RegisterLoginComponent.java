package cats.pixelthing.client.comp;

import cats.pixelthing.client.Client;
import cats.pixelthing.client.util.LabelBox;
import cats.pixelthing.core.data.impl.Login;
import cats.pixelthing.core.data.impl.Register;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class RegisterLoginComponent extends JPanel implements ActionListener{

    private final LabelBox nameBox;
    private final LabelBox passBox;

    private final JButton loginButton;
    private final JButton registerButton;

    public RegisterLoginComponent(){
        super(new BorderLayout());

        nameBox = new LabelBox("Name", false);

        passBox = new LabelBox("Password", true);

        final JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        boxPanel.add(nameBox);
        boxPanel.add(passBox);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);

        final JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        add(boxPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(final ActionEvent e){
        final Object source = e.getSource();
        if(source.equals(loginButton) || source.equals(registerButton)){
            final String name = nameBox.boxValue();
            final String pass = passBox.boxValue();
            if(name.isEmpty() || pass.isEmpty()){
                JOptionPane.showMessageDialog(null, "Fill in all fields");
                if(name.isEmpty())
                    nameBox.box.selectAll();
                else if(pass.isEmpty())
                    passBox.box.selectAll();
                return;
            }
            if(name.equalsIgnoreCase("server")){
                JOptionPane.showMessageDialog(null, "This name is reserved");
                return;
            }
            Client.send(source.equals(loginButton) ? new Login(name, pass) : new Register(name, pass));
        }
    }
}
