package com.flutter.mvc.ui;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import common.widget.VFlowLayout;
import kotlin.Unit;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import static com.intellij.openapi.vcs.VcsNotifier.NOTIFICATION_GROUP_ID;

public class NewCreateView {
    private JPanel panel;
    private JCheckBox httpCheckBox;
    private JEditorPane editorPane1;
    private JEditorPane editorPane2;
    private JButton okButton;
    private JCheckBox widgetCheckBox;

    public NewCreateView(String place, @NotNull Function4<? super String, ? super String, ? super Boolean,?super Boolean, Unit> listener) {
        panel.setLayout(new VFlowLayout(true, false));
        editorPane1.setText(place);
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame();
        frame.setBounds(400, 180, 300, 180);
        frame.setDefaultCloseOperation(JFrame.NORMAL);
        initListener(frame, listener);
        frame.setContentPane(panel);
        frame.setVisible(true);
    }


    private void initListener(JFrame frame, @NotNull Function4<? super String, ? super String, ? super Boolean,?super Boolean, Unit> listener) {


        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text1=editorPane1.getText();
                String text2=editorPane2.getText();
                if(text1.isEmpty()){
                    JOptionPane.showMessageDialog(frame,"名称不能为空");
//                    Notifications.Bus.notify(
//                            new Notification(NOTIFICATION_GROUP_ID.getDisplayId(), "出问题了", "名称不能为空", NotificationType.ERROR)
//                    );
                    return;
                }
                frame.dispose();
                listener.invoke(text1,text2,httpCheckBox.isSelected(),widgetCheckBox.isSelected());
            }
        });


        long timeLine = System.currentTimeMillis();
        frame.addWindowFocusListener(new WindowFocusListener() {


            @Override
            public void windowGainedFocus(WindowEvent windowEvent) {

            }

            @Override
            public void windowLostFocus(WindowEvent windowEvent) {
                if (System.currentTimeMillis() - timeLine > 500) {
                    frame.dispose();
                }
            }
        });

    }
}
