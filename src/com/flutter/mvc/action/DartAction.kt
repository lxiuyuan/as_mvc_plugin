package com.flutter.mvc.action

import com.flutter.mvc.common.element.FileElement
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
class DartAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        println("actionPerformed")
        val psiFile = event.getData(CommonDataKeys.PSI_FILE)
        val fileElement = FileElement()
        fileElement.nnnn(psiFile!!.text)
//        println(psiFile!!.text)
    }
}