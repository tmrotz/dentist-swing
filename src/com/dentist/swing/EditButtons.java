package com.dentist.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class EditButtons extends JDialog {

    private boolean decision = false;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField messageField1;
    private JTextField messageField2;
    private JTextField messageField3;
    private JTextField messageField4;
    private JTextField messageField5;
    private JTextField messageField6;
    private JPanel namePanel;
    private JPanel messagePanel;
    private JPanel checkboxPanel;
    private JCheckBox asdfCheckBox;
    private JCheckBox checkBox2;
    private JCheckBox checkBox3;
    private JCheckBox checkBox4;
    private JCheckBox checkBox5;
    private JCheckBox checkBox6;

    public EditButtons() {
        setTitle("Edit Buttons");
        setModal(true);

        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        decision = true;
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public void setStuff(Component[] components) {
        Component[] names = namePanel.getComponents();
        Component[] messages = messagePanel.getComponents();
        Component[] checkboxes = checkboxPanel.getComponents();

        for (int i = 0; i < components.length; i++) {
            JButton b = (JButton) components[i];
            JCheckBox checkBox = (JCheckBox) checkboxes[i];
            JTextField name = (JTextField) names[i];
            JTextField message = (JTextField) messages[i];

            checkBox.setSelected(b.isEnabled());
            name.setText(b.getText());
            message.setText((String) b.getClientProperty("message"));
        }
    }

    public boolean[] getCheckboxes() {
        Component[] components = checkboxPanel.getComponents();
        JCheckBox[] fields = Arrays.copyOf(components, components.length - 1, JCheckBox[].class);
        boolean[] checkboxes = new boolean[fields.length];
        for (int i = 0; i < fields.length; i++) {
            checkboxes[i] = fields[i].isSelected();
        }
        return checkboxes;
    }

    public String[] getText() {
        return getStrings(namePanel.getComponents());
    }

    public String[] getMessages() {
        return getStrings(messagePanel.getComponents());
    }

    private String[] getStrings(Component[] components) {
        JTextField[] fields = Arrays.copyOf(components, components.length - 1, JTextField[].class);
        String[] strings = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            strings[i] = fields[i].getText();
        }
        return strings;
    }

    public boolean getDecision() {
        return decision;
    }
}
