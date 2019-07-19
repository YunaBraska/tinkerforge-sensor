package berlin.yuna.tinkerforgesensor.logic;

import com.apple.eawt.Application;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import static java.util.Objects.requireNonNull;

public class HumanInput extends JFrame implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    public static final int LABEL_HEIGHT = 24;
    final JLabel keyCode = new JLabel();
    final JLabel keyTextCode = new JLabel();
    final JLabel keyChar = new JLabel();
    final JLabel keyType = new JLabel();
    final JLabel keyMods = new JLabel();
    final JLabel keyAction = new JLabel();
    final JLabel keyLocation = new JLabel();

    final JLabel mouseType = new JLabel();
    final JLabel mouseX = new JLabel();
    final JLabel mouseY = new JLabel();
    final JLabel mouseMods = new JLabel();
    final JLabel mouseClickCount = new JLabel();
    final JLabel mouseButton = new JLabel();

    public static void main(final String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(javax.swing.plaf.nimbus.NimbusLookAndFeel.class.getTypeName());
        new HumanInput();
    }

    public HumanInput() {
        setIcon();
        setTitle(getClass().getSimpleName());
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final Container content = getContentPane();
        final Box keyPanel = Box.createVerticalBox();
        keyPanel.setBorder(BorderFactory.createTitledBorder("Keyboard"));
        keyPanel.add(getHPanel("KeyCode", keyCode));
        keyPanel.add(getHPanel("KeyTextCode", keyTextCode));
        keyPanel.add(getHPanel("KeyChar", keyChar));
        keyPanel.add(getHPanel("KeyType", keyType));
        keyPanel.add(getHPanel("KeyMods", keyMods));
        keyPanel.add(getHPanel("KeyAction", keyAction));
        keyPanel.add(getHPanel("KeyLocation", keyLocation));

        final Box mousePanel = Box.createVerticalBox();
        mousePanel.setBorder(BorderFactory.createTitledBorder("Mouse"));
        mousePanel.add(getHPanel("MouseType", mouseType));
        mousePanel.add(getHPanel("MouseX", mouseX));
        mousePanel.add(getHPanel("MouseY", mouseY));
        mousePanel.add(getHPanel("MouseMods", mouseMods));
        mousePanel.add(getHPanel("MouseClickCount", mouseClickCount));
        mousePanel.add(getHPanel("MouseButton", mouseButton));
        mousePanel.add(getHPanel("", new JLabel()));

        final Box hPanel = Box.createHorizontalBox();
        hPanel.setBorder(BorderFactory.createEtchedBorder());
        final int length = keyPanel.getComponents().length;
        setMinimumSize(new Dimension(650, LABEL_HEIGHT * (length + 1)));
        hPanel.add(keyPanel);
        hPanel.add(mousePanel);
        content.add(hPanel);
        pack();
        setVisible(true);
    }

    private void setIcon() {
        final Image image = new ImageIcon(requireNonNull(getClass().getClassLoader().getResource("humanInput.png"))).getImage();
        Application.getApplication().setDockIconImage(image);
        setIconImage(image);
    }

    private Box getHPanel(final String key, final JLabel value) {
        final Box panel = Box.createHorizontalBox();
        final JLabel label = new JLabel(key);
        final Dimension size = new Dimension(150, LABEL_HEIGHT);
        label.setSize(size);
        label.setMaximumSize(size);
        label.setMinimumSize(size);
        value.setSize(size);
        value.setMaximumSize(size);
        value.setMinimumSize(size);
        value.setBackground(Color.WHITE);
        value.setText(" ");
        panel.add(label);
        panel.add(value);
        value.setEnabled(false);
        return panel;
    }

    public void keyPressed(final KeyEvent event) {
        onKeyEvent("Key Pressed", event);
    }

    public void keyReleased(final KeyEvent event) {
        onKeyEvent("Key Released", event);
    }

    public void keyTyped(final KeyEvent event) {
        onKeyEvent("Key Typed", event);
    }

    @Override
    public void mouseClicked(final MouseEvent event) {
        onMouseEvent("Mouse Clicked", event);
    }

    @Override
    public void mousePressed(final MouseEvent event) {
        onMouseEvent("Mouse Pressed", event);
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        onMouseEvent("Mouse Released", event);
    }

    @Override
    public void mouseEntered(final MouseEvent event) {
        onMouseEvent("Mouse Entered", event);
    }

    @Override
    public void mouseExited(final MouseEvent event) {
        onMouseEvent("Mouse Exited", event);
    }

    @Override
    public void mouseDragged(final MouseEvent event) {
        onMouseEvent("Mouse Drag", event);
    }

    @Override
    public void mouseMoved(final MouseEvent event) {
        onMouseEvent("Mouse Move", event);
    }

    @Override
    public void mouseWheelMoved(final MouseWheelEvent event) {
        onMouseEvent("Mouse Wheel", event);
    }

    private void onMouseEvent(final String type, final MouseEvent event) {
        mouseType.setText(type);
        mouseX.setText(String.valueOf(event.getX()));
        mouseY.setText(String.valueOf(event.getY()));
        mouseMods.setText(String.valueOf(event.getModifiers()));
        mouseClickCount.setText(String.valueOf(event.getClickCount()));
        mouseButton.setText(String.valueOf(event.getButton()));
    }

    private void onKeyEvent(final String type, final KeyEvent event) {
        final int code = event.getKeyCode();
        keyCode.setText(String.valueOf(code));
        keyTextCode.setText(KeyEvent.getKeyText(code));
        keyChar.setText(String.valueOf(event.getKeyChar()));
        keyType.setText(type);
        keyMods.setText(KeyEvent.getModifiersExText(event.getModifiersEx()));
        keyAction.setText(String.valueOf(event.isActionKey()));
        keyLocation.setText(keyboardLocation(event.getKeyLocation()));
    }

    private String keyboardLocation(final int keyLocation) {
        switch (keyLocation) {
            case KeyEvent.KEY_LOCATION_RIGHT:
                return "Right";
            case KeyEvent.KEY_LOCATION_LEFT:
                return "Left";
            case KeyEvent.KEY_LOCATION_NUMPAD:
                return "NumPad";
            case KeyEvent.KEY_LOCATION_STANDARD:
                return "Standard";
            case KeyEvent.KEY_LOCATION_UNKNOWN:
            default:
                return "Unknown [" + keyLocation + "]";
        }
    }
}