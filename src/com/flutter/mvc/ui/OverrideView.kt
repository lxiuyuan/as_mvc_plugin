package com.flutter.mvc.ui

import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.WindowEvent
import java.awt.event.WindowFocusListener
import javax.swing.*

class OverrideView {
    private lateinit var contentPane: JPanel
    private  var btnLeft: JButton? = null
    private  var btnRight: JButton? = null
    private var listener: ()->Unit

    companion object{
        val WIDTH=250;
        val HEIGHT=55;
    }


    constructor(x:Int,y:Int,listener: ()->Unit) {

        this.listener = listener
        createContent()
        createLabel()
        createRightButton()
        JFrame.setDefaultLookAndFeelDecorated(true);
        val frame= JFrame("提示")
        frame.setLocation(x,y)
        frame.setSize(WIDTH, HEIGHT)
        frame.contentPane=contentPane
        initListener(frame)
//        frame.pack()
        frame.isVisible=true
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    private fun initListener(frame: JFrame) {
        btnLeft?.addActionListener {
            frame.dispose()
        }

        btnRight?.addActionListener {
            frame.dispose()
            listener.invoke();
        }

        val timeLine=System.currentTimeMillis()

        frame.addWindowFocusListener(object: WindowFocusListener {

            override fun windowLostFocus(p0: WindowEvent?) {
                if(System.currentTimeMillis()-timeLine>500) {
                    frame.dispose()
                }
            }

            override fun windowGainedFocus(p0: WindowEvent?) {

            }

        })
    }


    fun createContent(){
        contentPane= JPanel()
        contentPane.setSize(WIDTH,HEIGHT)
    }

    fun createLabel(){
        val jLable=JLabel("发现mvc文件已存在，是否覆盖他们")
        jLable.setBounds(0,0,WIDTH, HEIGHT/2)
        contentPane.add(jLable)
        jLable.horizontalAlignment=JLabel.CENTER
    }

    fun createRightButton(){


        val jLable=JLabel("      ")
        contentPane.add(jLable)
        jLable.setBounds(0,HEIGHT/2,WIDTH/4, HEIGHT/2)
//
        btnLeft= JButton("取消")
        contentPane.add(btnLeft!!)
        btnLeft?.setBounds(WIDTH/4*2, HEIGHT/2, WIDTH/4-2, HEIGHT/2)
        btnRight= JButton("覆盖")
        contentPane.add(btnRight!!)
        btnRight?.setBounds(WIDTH/4*3,HEIGHT/2, WIDTH/4-2, HEIGHT/2)
    }

}