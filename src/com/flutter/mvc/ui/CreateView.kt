package com.flutter.mvc.ui

import net.sourceforge.pinyin4j.PinyinHelper
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.WindowEvent
import java.awt.event.WindowFocusListener
import javax.swing.JEditorPane
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel

class CreateView {
    private lateinit var contentPane: JPanel
    private  var txtName: JEditorPane? = null
    private  lateinit var txtDescription: JEditorPane
    private var text: String
    private var listener: (String,String)->Unit

    companion object{
        val WIDTH=250;
        val HEIGHT=105;
    }


    constructor(x:Int,y:Int,text: String,listener: (String,String)->Unit) {

        this.listener = listener
        this.text = text
        createContent()
        createTextLabel()
        createTextField()
        createDescription()


        txtName!!.text = text
        txtName!!.requestFocus()
        txtName!!.isFocusable = true
        txtName!!.selectAll()


        JFrame.setDefaultLookAndFeelDecorated(true);
        val frame= JFrame();
        frame.setDefaultCloseOperation(JFrame.NORMAL);



        frame.setLocation(x,y)
        frame.setSize(WIDTH, HEIGHT)
        frame.contentPane=contentPane
        initListener(frame)
//        frame.pack()
        frame.isVisible=true

    }

    private fun initListener(frame: JFrame) {
        txtName!!.addKeyListener(object : KeyListener {
            override fun keyTyped(keyEvent: KeyEvent) {}
            override fun keyPressed(keyEvent: KeyEvent) {
                if (keyEvent.keyCode == KeyEvent.VK_ENTER) {
                    listener!!.invoke(txtName!!.text.trim(),txtDescription.text.trim())
//                    dispose()
                    frame.dispose()
//                    frame.isVisible=false
                }
            }

            override fun keyReleased(keyEvent: KeyEvent) {}
        })
        txtDescription!!.addKeyListener(object : KeyListener {
            override fun keyTyped(keyEvent: KeyEvent) {}
            override fun keyPressed(keyEvent: KeyEvent) {
                if (keyEvent.keyCode == KeyEvent.VK_ENTER) {
                    listener!!.invoke(txtName!!.text.trim(),txtDescription.text.trim())
//                    dispose()
                    frame.dispose()
//                    frame.isVisible=false
                }
            }

            override fun keyReleased(keyEvent: KeyEvent) {}
        })

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


    private fun createContent(){
        contentPane= JPanel(BorderLayout(1,6))
    }

    private fun createTextField(){
        txtName= JEditorPane()
        txtName!!.background= Color.WHITE
        txtName!!.setSize(WIDTH,40)
        txtName?.margin= Insets(3,7,0,0)
        txtName?.maximumSize= Dimension(WIDTH,20)
        contentPane.add(txtName!!, BorderLayout.CENTER)
    }

    private fun createDescription(){
        val descriptionPanel=JPanel(BorderLayout(0,3))
        contentPane.add(descriptionPanel,BorderLayout.SOUTH)
        txtDescription= JEditorPane()
        txtDescription.background= Color.WHITE
        txtDescription.setSize(WIDTH,40)
        txtDescription.margin= Insets(0,7,2,0)
        txtDescription.maximumSize= Dimension(WIDTH,20)
        descriptionPanel.add(txtDescription,BorderLayout.CENTER)
        val bottomPanel=JPanel(BorderLayout())
        val bottomJLabel=JLabel("——")
        bottomPanel.add(bottomJLabel,BorderLayout.CENTER)
        bottomJLabel.horizontalAlignment=JLabel.CENTER
        descriptionPanel.add(bottomPanel,BorderLayout.SOUTH)
        descriptionPanel.add(JLabel("  描述："),BorderLayout.NORTH)


    }

    private fun createTextLabel(){
        val label= JLabel("请输入昵称")
        label.setSize(WIDTH,30)
        label.horizontalAlignment= JLabel.CENTER
        label.verticalAlignment= JLabel.CENTER
        contentPane.add(label, BorderLayout.NORTH)
    }


}