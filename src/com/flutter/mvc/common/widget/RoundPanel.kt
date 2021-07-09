package com.flutter.mvc.common.widget

import com.intellij.ui.JBColor
import com.flutter.mvc.common.Config
import java.awt.Color
import java.awt.Graphics
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.geom.RoundRectangle2D
import javax.swing.JPanel


class RoundPanel() : JPanel() {
    override fun paint(g: Graphics) {
        val fieldX = 0
        val fieldY = 0
        val fieldWeight = size.width
        val fieldHeight = size.height
        val rect: RoundRectangle2D = RoundRectangle2D.Double(fieldX.toDouble(), fieldY.toDouble(), fieldWeight.toDouble(), fieldHeight.toDouble(), 8.0, 8.0)
        g.clip = rect
        super.paint(g)
    }

    override fun paintAll(g: Graphics?) {
        val fieldX = 0
        val fieldY = 0
        val fieldWeight = size.width
        val fieldHeight = size.height
        val rect: RoundRectangle2D = RoundRectangle2D.Double(fieldX.toDouble(), fieldY.toDouble(), fieldWeight.toDouble(), fieldHeight.toDouble(), 8.0, 8.0)
        g?.clip = rect
        super.paintAll(g)
    }

    override fun paintComponent(g: Graphics?) {
        val fieldX = 0
        val fieldY = 0
        val fieldWeight = size.width
        val fieldHeight = size.height
        val rect: RoundRectangle2D = RoundRectangle2D.Double(fieldX.toDouble(), fieldY.toDouble(), fieldWeight.toDouble(), fieldHeight.toDouble(), 8.0, 8.0)
        g?.clip = rect
        super.paintComponent(g)

    }





    override fun setBackground(bg: Color?) {
        super.setBackground(bg)
        repaint()
//        revalidate()
    }


    var listener:(()->Unit)?=null
    fun setOnClickListener(listener:()->Unit){
        this.listener=listener
    }

    companion object{
        val selectColor1: Color = JBColor.namedColor("Plugins.SectionHeader.selectColor", JBColor(0xf8f8f9, 0x484b4c))
    }
    init {

        isOpaque = true
        background = Config.background
        addMouseListener(object:MouseListener{
            override fun mouseReleased(e: MouseEvent?) {

            }

            override fun mouseEntered(e: MouseEvent?) {
            }

            override fun mouseClicked(e: MouseEvent?) {
                background= Config.background
            }

            override fun mouseExited(e: MouseEvent?) {
                if(background!= Config.background) {
                    background = Config.background
                }
            }

            override fun mousePressed(e: MouseEvent?) {
                background=selectColor1
                listener?.invoke()
            }

        })
    }
}