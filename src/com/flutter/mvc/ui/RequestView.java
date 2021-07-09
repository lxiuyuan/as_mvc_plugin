package com.flutter.mvc.ui;

import com.flutter.mvc.common.element.HttpElement;
import com.flutter.mvc.common.element.RequestElement;
import common.widget.VFlowLayout;
import kotlin.UInt;
import kotlin.jvm.functions.Function0;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RequestView extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel jListPanel;

    public RequestView(HttpElement httpElement, final Function0<?super UInt> listener) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        initValue(httpElement);
        setBounds(400, 180, 500, 350);
        jListPanel.setLayout(new VFlowLayout());
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (RequestItem requestItem : requestItems) {
                    requestItem.finish();
                }
                listener.invoke();
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

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

    ArrayList<RequestItem> requestItems= new ArrayList<RequestItem>();

    private void initValue(HttpElement httpElement){
        for (RequestElement requestElement : httpElement.getRequest()) {
            RequestItem requestItem = new RequestItem(requestElement);
            requestItems.add(requestItem);
            jListPanel.add(requestItem.getPanel1());
        }
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
