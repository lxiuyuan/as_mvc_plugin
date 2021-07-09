package com.flutter.mvc.action

import com.flutter.mvc.common.DartFileType
import com.flutter.mvc.common.element.DJsonParse
import com.flutter.mvc.common.utils.StringUtils
import com.flutter.mvc.ui.JsonToDartView
import com.intellij.configurationStore.NOTIFICATION_GROUP_ID
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import org.apache.commons.lang.StringUtils.removeEnd

class JsonToDartAction : AnAction() {

    override fun actionPerformed(p0: AnActionEvent) {
        JsonToDartView { a, b ->
            val name= StringUtils.removeEnd(a,"_bean");
            val  code = DJsonParse().toCode(name, b)

            createFile(p0,a,code)
            return@JsonToDartView Unit


        }.isVisible = true;
    }

    fun createFile(event: AnActionEvent, name:String, code:String){

        val dataContext = event.dataContext
        val module = LangDataKeys.MODULE.getData(dataContext) ?: return
        val navigatable = LangDataKeys.NAVIGATABLE.getData(dataContext) ?: return
        val directory = when (navigatable) {
            is PsiDirectory -> navigatable
            is PsiFile -> navigatable.containingDirectory
            else -> {
                val root = ModuleRootManager.getInstance(module)
                root.sourceRoots.asSequence().mapNotNull {
                    PsiManager.getInstance(event.project!!).findDirectory(it)
                }.firstOrNull()

            }
        } ?: return
        val psiFileFactory = PsiFileFactory.getInstance(event.project)
        val file = psiFileFactory.createFileFromText("${name}_bean.dart", DartFileType.INSTANCE, code)
        ApplicationManager.getApplication().runWriteAction {
            directory.add(file)
            Notifications.Bus.notify(
                    Notification(NOTIFICATION_GROUP_ID, "FlutterMVC", "创建模型完成", NotificationType.INFORMATION)
            )
        }
    }
}