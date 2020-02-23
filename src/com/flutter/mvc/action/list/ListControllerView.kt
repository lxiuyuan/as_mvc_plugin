package com.flutter.mvc.action.list

import com.flutter.mvc.ui.ListControllerViewz
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.treeStructure.Tree
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.tree.DefaultMutableTreeNode

class ListControllerView(
        private var project: Project?
):PersistentStateComponent<ListControllerViewState> {


    private var state=ListControllerViewState()


    fun initToolWindow(toolWindow: ToolWindow) {
        val contentFactory=ContentFactory.SERVICE.getInstance()
        val contentManager=toolWindow.contentManager
        val content=contentFactory.createContent(null,null,false)
        content.isCloseable=false

        val jPanel=ListControllerComponent(this)
        val jPanel1=JPanel()
        jPanel.add(ListControllerViewz(project!!))
        content.component=jPanel
        content.setPreferredFocusedComponent {
            return@setPreferredFocusedComponent jPanel1
        }
        contentManager.addContent(content)

        contentManager.setSelectedContent(content)

    }

    override fun getState(): ListControllerViewState?=state

    override fun loadState(state: ListControllerViewState) {
        this.state.copyFrom(state)
    }
}



class ListControllerComponent(
        private val view:ListControllerView
):SimpleToolWindowPanel(true,true){

    override fun getData(dataId: String): Any? {
        println1(dataId)
        return super.getData(dataId)
    }
    companion object{
        fun println1( message:String){
//            System.out.println("message:${message}")
        }
    }
}


