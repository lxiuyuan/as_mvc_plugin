package com.flutter.mvc.ui;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

import javax.swing.*;
import java.awt.event.*;

public class JsonToDartView extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextArea textPanel2;

    public JsonToDartView(Function2<?super  String ,?super JsonElement,?super Unit> listener) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setBounds(400, 100, 450, 600);
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

    private void onOK(Function2<? super String, ? super JsonElement, ? super Unit> listener) {
        String json=textPanel2.getText();
        try {
            JsonElement jsonElement = JsonParser.parseString(json);
            listener.invoke(textField1.getText(),jsonElement);
            dispose();
        }catch (Exception e){

        }
    }

    private void onCancel() {
        dispose();
    }
}
