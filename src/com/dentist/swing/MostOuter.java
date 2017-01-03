package com.dentist.swing;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MostOuter extends JFrame {

    private JPanel contentPane;
    private JTextArea textArea1;
    private JTextField customTextField;
    private JButton sendButton;
    private JPanel buttonPanel;
    private JScrollPane scrollPane;
    private JPanel customPanel;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton editButton;

    private EditButtons dialog = null;

    private Socket socket = null;

    private static String DENTIST_EVENT = "dentist event";
    private static String BUTTON_EVENT = "button event";

    public MostOuter(Socket socket) {
        super("Dentist App");

        this.socket = socket;

        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editButtons();
            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendDentistEvent();
            }
        });
        customTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                if (e.getKeyChar() == '\n') {
                    sendDentistEvent();
                }
            }
        });

        socket.on(DENTIST_EVENT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String textFieldText = (String) args[0];
                String message = textFieldText + '\n';
                String text = textArea1.getText() + message;
                textArea1.setText(text);
                pack();
            }
        });

        socket.on(BUTTON_EVENT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONArray buttons = (JSONArray) objects[0];
                for (int i = 0; i < buttons.length(); i++) {
                    JSONObject button = null;
                    try {
                        button = buttons.getJSONObject(i);
                    } catch (JSONException e) {
                        System.err.format("JSONException: %s", e.getMessage());
                    }

                    int index = -1;
                    boolean enabled = false;
                    String text = null;
                    String message = null;
                    try {
                        index = button.getInt("index");
                        enabled = button.getBoolean("enabled");
                        text = button.getString("text");
                        message = button.getString("message");
                    } catch (JSONException e) {
                        System.err.format("JSONException: %s", e.getMessage());
                    }

                    JButton b = (JButton) buttonPanel.getComponent(index);
                    b.setEnabled(enabled);
                    b.setText(text);
                    b.putClientProperty("message", message);
                    ActionListener[] listeners = b.getActionListeners();
                    if (listeners.length > 0) {
                        b.removeActionListener(listeners[0]);
                    }
                    b.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String message = (String) b.getClientProperty("message");
                            socket.emit(DENTIST_EVENT, message);
                        }
                    });
                }
                pack();
            }
        });

        socket.connect();
    }

    private void sendDentistEvent() {
        String textFieldText = customTextField.getText();

        if (textFieldText != null && !textFieldText.isEmpty()) {
            socket.emit(DENTIST_EVENT, textFieldText);
            customTextField.setText("");
        }
    }

    private void sendButtonEvent(boolean[] checkboxes, String[] text, String[] messages)
    {
        if (checkboxes.length == text.length && text.length == messages.length) {
            JSONArray moo = new JSONArray();
            for (int i = 0; i < buttonPanel.getComponentCount(); i++) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("index", i);
                    obj.put("enabled", checkboxes[i]);
                    obj.put("text", text[i]);
                    obj.put("message", messages[i]);
                } catch (JSONException e) {
                    System.err.println("JSONException: " + e.getMessage());
                }
                moo.put(obj);
            }
            socket.emit(BUTTON_EVENT, moo);
        } else {
            System.err.format("Checkbox length: %d, Text length: %d, Messages length %d", checkboxes.length, text.length, messages.length);
        }
    }

    private void editButtons() {
        dialog = new EditButtons();
        dialog.setStuff(buttonPanel.getComponents());
        dialog.pack();
        dialog.setVisible(true);

        if (dialog.getDecision()) {
            boolean[] c = dialog.getCheckboxes();
            String[] t = dialog.getText();
            String[] m = dialog.getMessages();

            sendButtonEvent(c, t, m);
        }
    }
}
