package cats.pixelthing.client.comp;

import cats.pixelthing.client.Client;
import cats.pixelthing.core.Constants;
import cats.pixelthing.core.data.impl.ChangeColor;
import cats.pixelthing.core.data.impl.Message;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ChatAreaComponent extends JPanel implements KeyListener{

    private final JList<String> messageList;
    private final DefaultListModel<String> messageModel;

    private final JLabel nameLabel;
    private final JTextField inputBox;

    public ChatAreaComponent(){
        super(new BorderLayout());
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        messageModel = new DefaultListModel<>();

        messageList = new JList<>(messageModel);
        messageList.setEnabled(false);

        nameLabel = new JLabel("Unknown", JLabel.CENTER);
        nameLabel.setBorder(new EmptyBorder(0, 5, 0, 5));
        nameLabel.addMouseListener(
                new MouseAdapter(){
                    public void mousePressed(final MouseEvent e){
                        final Color color = JColorChooser.showDialog(null, "Choose new color", nameLabel.getForeground());
                        if(color != null && !color.equals(nameLabel.getForeground()))
                            Client.send(new ChangeColor(ChangeColor.USER, Client.user.uid, color.getRGB()));
                    }
                }
        );

        inputBox = new JTextField();
        inputBox.setFocusable(true);
        inputBox.addKeyListener(this);

        final JPanel south = new JPanel(new BorderLayout());
        south.add(nameLabel, BorderLayout.WEST);
        south.add(inputBox, BorderLayout.CENTER);

        add(new JScrollPane(messageList), BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }

    public void keyPressed(final KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_ENTER:
                final String text = inputBox.getText().trim();
                if(text.isEmpty())
                    return;
                if(text.startsWith(Constants.COMMAND_START_SEQUENCE))
                    Client.send(new Message(Message.COMMAND, text));
                else
                    Client.send(new Message(Message.PUBLIC_CHAT, text));
                inputBox.setText("");
                break;
            case CanvasComponent.LEFT:
            case CanvasComponent.DOWN:
            case CanvasComponent.RIGHT:
            case CanvasComponent.UP:
                Client.canvasComponent.process(e);
                break;
        }
    }

    public void keyReleased(final KeyEvent e){
        switch(e.getKeyCode()){
            case CanvasComponent.LEFT:
            case CanvasComponent.DOWN:
            case CanvasComponent.RIGHT:
            case CanvasComponent.UP:
                Client.canvasComponent.process(e);
                break;
        }
    }

    public void keyTyped(final KeyEvent e){}

    public void pushMessage(final Message m){
        messageModel.addElement(m.msg());
        SwingUtilities.invokeLater(() -> messageList.ensureIndexIsVisible(messageModel.getSize() - 1));
        messageList.repaint();
    }

    public void initUserLabel(){
        nameLabel.setText(Client.user.name);
        nameLabel.setForeground(Client.user.color);
        nameLabel.revalidate();
        nameLabel.repaint();
    }

}
