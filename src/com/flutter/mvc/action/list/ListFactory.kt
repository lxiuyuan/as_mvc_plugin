package com.flutter.mvc.action.list

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

class ListFactory():ToolWindowFactory,DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        DumbService.getInstance(project).runWhenSmart {
            (ServiceManager.getService(project,ListControllerView::class.java)).initToolWindow(toolWindow)
        }
    }
}