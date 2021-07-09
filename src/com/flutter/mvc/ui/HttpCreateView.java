package com.flutter.mvc.ui;

import kotlin.UInt;
import kotlin.jvm.functions.Function3;

import javax.swing.*;
import java.awt.event.*;

public class HttpCreateView extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea textArea1;
    private JTextField textField1;
    private JCheckBox checkBox;

    public HttpCreateView(Function3<? super String, ? super String, ? super Boolean, ? super UInt> listener) {
        setContentPane(contentPane);
        setModal(true);
        setBounds(400, 100, 450, 600);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK(listener);
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

    private void onOK(Function3<? super String, ? super String, ? super Boolean, ? super UInt> listener) {
        dispose();
        if (!textArea1.getText().isEmpty())
            listener.invoke(textField1.getText(), textArea1.getText(), checkBox.isSelected());

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        HttpCreateView dialog = new HttpCreateView(new Function3<String, String, Boolean, UInt>() {
            @Override
            public UInt invoke(String s, String s2, Boolean aBoolean) {
                return null;
            }
        });
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
