package com.flutter.mvc.ui;

import com.flutter.mvc.common.element.RequestElement;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RequestItem {
    private JPanel panel1;
    public JPanel getPanel1(){
        return panel1;
    }
    private JRadioButton rb0;
    private JRadioButton rb1;
    private JRadioButton rb2;
    private JLabel label;
    private JTextField textField1;
    RequestElement requestElement;

    public  RequestItem(final RequestElement requestElement){
        this.requestElement=requestElement;
        label.setText("  "+requestElement.getName());
        ButtonGroup buttonGroup=new ButtonGroup();
        rb1.setSelected(true);
        buttonGroup.add(rb0);
        buttonGroup.add(rb1);
        buttonGroup.add(rb2);
        initListener(requestElement);
//        checkBox1.addChangeList
    }

    void initListener(final RequestElement requestElement){
        rb0.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {

                requestElement.setState(0);

            }
        });
        rb1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {


                requestElement.setState(1);

            }
        });
        rb2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {

                requestElement.setState(2);
                textField1.setEnabled(rb2.isSelected());

            }
        });
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                requestElement.setRemark(textField1.getText());
            }
        });

    }

    public void finish(){
        if(rb2.isSelected()){

            requestElement.setRemark(textField1.getText());
        }
    }

    private void createUIComponents() {
    }
}
