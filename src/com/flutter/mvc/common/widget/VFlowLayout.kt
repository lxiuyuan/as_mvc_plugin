package common.widget

import java.awt.Container
import java.awt.Dimension
import java.awt.FlowLayout


/**
 * VerticalFlowLayout is similar to FlowLayout except it lays out components
 * vertically. Extends FlowLayout because it mimics much of the behavior of the
 * FlowLayout class, except vertically. An additional feature is that you can
 * specify a fill to edge flag, which causes the VerticalFlowLayout manager to
 * resize all components to expand to the column width Warning: This causes
 * problems when the main panel has less space that it needs and it seems to
 * prohibit multi-column output. Additionally there is a vertical fill flag,
 * which fills the last component to the remaining height of the container.
 */
class VFlowLayout @JvmOverloads constructor(align: Int = TOP, hgap: Int = 5, vgap: Int = 5, hfill: Boolean = true, vfill: Boolean = false) : FlowLayout() {

    /**
     * Returns true if the layout horizontally fills.
     *
     * @return true if horizontally fills.
     */
    /**
     * Set to true to enable horizontally fill.
     *
     * @param hfill
     * true to fill horizontally.
     */
    var horizontalFill: Boolean
    /**
     * Returns true if the layout vertically fills.
     *
     * @return true if vertically fills the layout using the specified.
     */
    /**
     * Set true to fill vertically.
     *
     * @param vfill
     * true to fill vertically.
     */
    var verticalFill: Boolean

    /**
     * Construct a new VerticalFlowLayout with a middle alignment.
     *
     * @param hfill
     * the fill to edge flag
     * @param vfill
     * the vertical fill in pixels.
     */
    constructor(hfill: Boolean, vfill: Boolean) : this(TOP, 5, 5, hfill, vfill) {}

    /**
     * Construct a new VerticalFlowLayout.
     *
     * @param align
     * the alignment value
     * @param hfill
     * the horizontalfill in pixels.
     * @param vfill
     * the vertical fill in pixels.
     */
    constructor(align: Int, hfill: Boolean, vfill: Boolean) : this(align, 5, 5, hfill, vfill) {}

    /**
     * Returns the preferred dimensions given the components in the target
     * container.
     *
     * @param target
     * the component to lay out
     */
    override fun preferredLayoutSize(target: Container): Dimension {
        val tarsiz = Dimension(0, 0)
        for (i in 0 until target.componentCount) {
            val m = target.getComponent(i)
            if (m.isVisible) {
                val d = m.preferredSize
                tarsiz.width = Math.max(tarsiz.width, d.width)
                if (i > 0) {
                    tarsiz.height += hgap
                }
                tarsiz.height += d.height
            }
        }
        val insets = target.insets
        tarsiz.width += insets.left + insets.right + hgap * 2
        tarsiz.height += insets.top + insets.bottom + vgap * 2
        return tarsiz
    }

    /**
     * Returns the minimum size needed to layout the target container.
     *
     * @param target
     * the component to lay out.
     * @return the minimum layout dimension.
     */
    override fun minimumLayoutSize(target: Container): Dimension {
        val tarsiz = Dimension(0, 0)
        for (i in 0 until target.componentCount) {
            val m = target.getComponent(i)
            if (m.isVisible) {
                val d = m.minimumSize
                tarsiz.width = Math.max(tarsiz.width, d.width)
                if (i > 0) {
                    tarsiz.height += vgap
                }
                tarsiz.height += d.height
            }
        }
        val insets = target.insets
        tarsiz.width += insets.left + insets.right + hgap * 2
        tarsiz.height += insets.top + insets.bottom + vgap * 2
        return tarsiz
    }

    /**
     * places the components defined by first to last within the target
     * container using the bounds box defined.
     *
     * @param target
     * the container.
     * @param x
     * the x coordinate of the area.
     * @param y
     * the y coordinate of the area.
     * @param width
     * the width of the area.
     * @param height
     * the height of the area.
     * @param first
     * the first component of the container to place.
     * @param last
     * the last component of the container to place.
     */
    private fun placethem(target: Container, x: Int, y: Int, width: Int, height: Int, first: Int, last: Int) {
        var y = y
        val align = alignment
        if (align == MIDDLE) {
            y += height / 2
        }
        if (align == BOTTOM) {
            y += height
        }
        for (i in first until last) {
            val m = target.getComponent(i)
            val md = m.size
            if (m.isVisible) {
                val px = x + (width - md.width) / 2
                m.setLocation(px, y)
                y += vgap + md.height
            }
        }
    }

    /**
     * Lays out the container.
     *
     * @param target
     * the container to lay out.
     */
    override fun layoutContainer(target: Container) {
        val insets = target.insets
        val maxheight = target.size.height - (insets.top + insets.bottom + vgap * 2)
        val maxwidth = target.size.width - (insets.left + insets.right + hgap * 2)
        val numcomp = target.componentCount
        var x = insets.left + hgap
        var y = 0
        var colw = 0
        var start = 0
        for (i in 0 until numcomp) {
            val m = target.getComponent(i)
            if (m.isVisible) {
                val d = m.preferredSize
                // fit last component to remaining height
                if (verticalFill && i == numcomp - 1) {
                    d.height = Math.max(maxheight - y, m.preferredSize.height)
                }
                // fit component size to container width
                if (horizontalFill) {
                    m.setSize(maxwidth, d.height)
                    d.width = maxwidth
                } else {
                    m.setSize(d.width, d.height)
                }
                if (y + d.height > maxheight) {
                    placethem(target, x, insets.top + vgap, colw, maxheight - y, start, i)
                    y = d.height
                    x += hgap + colw
                    colw = d.width
                    start = i
                } else {
                    if (y > 0) {
                        y += vgap
                    }
                    y += d.height
                    colw = Math.max(colw, d.width)
                }
            }
        }
        placethem(target, x, insets.top + vgap, colw, maxheight - y, start, numcomp)
    }

    companion object {
        /**
         *
         */
        private const val serialVersionUID = 1L
        /**
         * Specify alignment top.
         */
        const val TOP = 0
        /**
         * Specify a middle alignment.
         */
        const val MIDDLE = 1
        /**
         * Specify the alignment to be bottom.
         */
        const val BOTTOM = 2

    }
    /**
     * Construct a new VerticalFlowLayout.
     *
     * @param align
     * the alignment value
     * @param hgap
     * the horizontal gap variable
     * @param vgap
     * the vertical gap variable
     * @param hfill
     * the fill to edge flag
     * @param vfill
     * true if the panel should vertically fill.
     */
    /**
     * Construct a new VerticalFlowLayout with a middle alignment, and the fill
     * to edge flag set.
     */
    /**
     * Construct a new VerticalFlowLayout with a middle alignment.
     *
     * @param align
     * the alignment value
     */
    init {
        alignment = align
        this.hgap = hgap
        this.vgap = vgap
        horizontalFill = hfill
        verticalFill = vfill
    }
}