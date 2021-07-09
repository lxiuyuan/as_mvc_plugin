package com.flutter.mvc.action

import com.flutter.mvc.common.DartFileType
import com.flutter.mvc.common.manager.RefreshListManager
import com.flutter.mvc.common.utils.StringUtils
import com.flutter.mvc.ui.NewCreateView
import com.flutter.mvc.ui.OverrideView
import com.intellij.configurationStore.NOTIFICATION_GROUP_ID
import com.intellij.ide.actions.OpenFileAction
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.file.PsiDirectoryFactory
import java.awt.MouseInfo
import java.awt.Window


class NewMenuAction : AnAction("Flutter Mvc") {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.getData(PlatformDataKeys.PROJECT) ?: return


        val dataContext = event.dataContext

        val module = LangDataKeys.MODULE.getData(dataContext) ?: return

        val navigatable = LangDataKeys.NAVIGATABLE.getData(dataContext) ?: return

        val directory = when (navigatable) {
            is PsiDirectory -> navigatable
            is PsiFile -> navigatable.containingDirectory
            else -> {
                val root = ModuleRootManager.getInstance(module)
                root.sourceRoots.asSequence().mapNotNull {
                    PsiManager.getInstance(project).findDirectory(it)
                }.firstOrNull()

            }
        } ?: return

        val directoryFactory = PsiDirectoryFactory.getInstance(project)

        val packageName = directoryFactory.getQualifiedName(directory, true)

        val psiFileFactory = PsiFileFactory.getInstance(project)


        //计算位置大小
        var x = 360
        var y = 300
        arrayOf(event.inputEvent)
        try {
            val position = MouseInfo.getPointerInfo().location
            x = position.x - 100
            y = position.y - 32
        } catch (e: Exception) {
            try {
                if (Window.WIDTH == 0 || Window.HEIGHT == 0) {
                    return;
                }
                x = Window.WIDTH / 2
                y = Window.HEIGHT / 2
            } catch (e: Exception) {
            }
        }

        val isOverride = checkHasChild(directory, "view.dart") || checkHasChild(directory, "controller.dart") || checkHasChild(directory, "http.dart")

        NewCreateView(getNameForPackage(packageName)) { name: String, description: String, isHttp,isWidget ->
            if (isOverride) {
                OverrideView(x, y) {
                    createFile(project, psiFileFactory, directory, name, description, isHttp,isWidget, isOverride)
                }
            } else {
                createFile(project, psiFileFactory, directory, name, description,isHttp,isWidget, isOverride)
            }
        }


    }


    private fun checkHasChild(directory: PsiDirectory, name: String): Boolean {

        return directory.findFile(name) != null
    }

    private fun getNameForPackage(pkg: String): String {


        val map = System.getenv()

        for (key in map.keys) {
            println("${key}:${map[key]}")
        }


        if (!pkg.contains("/") && !pkg.contains(".")) {
            return "";
        }
        if (pkg.contains("/")) {
            val names = pkg.split("/")
            return names[names.size - 1];
        } else {

            val names = pkg.split(".")
            return names[names.size - 1];
        }
    }

    private fun getUser(): String {
        val env = System.getenv()
        if (env.containsKey("USER")) {
            return env["USER"]!!
        }
        if (env.containsKey("USERNAME")) {
            return env["USERNAME"]!!;
        }
        return "";
    }

    private fun createFile(project: Project?, psiFileFactory: PsiFileFactory, directory: PsiDirectory, name: String, description: String, isHttp: Boolean,isWidget: Boolean, isOverride: Boolean) {
        val mvcBean=com.flutter.mvc.common.Config.getMvcBean(project!!.basePath!!);
        val user = getUser();
        var zname = StringUtils._toLowName(name)
        if (zname.endsWith("Page")) {
            zname = zname.substring(0, zname.length - "Page".length)
        }
        fun getViewCode(): String {
            val sb = StringBuilder("import 'controller.dart';\n")
            sb.appendLine("import 'package:flutter/material.dart';")
            if (isWidget)
                sb.appendLine("import 'widget.dart';")
            sb.appendLine("${mvcBean.importView}\n")
            sb.appendLine("///description:${description}")
            sb.appendLine("class ${zname}Page extends ${mvcBean.extendsView}<${zname}Controller>{")
            sb.appendLine("   ")
            sb.appendLine("   @override")
            sb.appendLine("   Widget build(BuildContext context){")
            sb.appendLine("       return Container();")
            sb.appendLine("   }")
            sb.appendLine("}")

            return sb.toString()
        }

        fun getControllerCode(): String {
            val sb = StringBuilder("import 'view.dart';\n")

            if (isHttp)
            sb.appendLine("import 'http.dart';")
            sb.appendLine("${mvcBean.importController}\n")
            sb.appendLine("///Description:${description}")
            sb.appendLine("///Author:${user}")
            sb.appendLine("class ${zname}Controller extends ${mvcBean.extendsController} {")
            sb.appendLine("   ")
            sb.appendLine("   ${zname}Controller():super(${zname}Page());")
            if (isHttp)
                sb.appendLine("   var _http=${zname}Http();")
            sb.appendLine("   ")
            sb.appendLine("   @override")
            sb.appendLine("   void initState(){")
            sb.appendLine("       super.initState();")
            sb.appendLine("       ")
            sb.appendLine("   }")
            sb.appendLine("   ")
            sb.appendLine("}")
            return sb.toString()
        }

        fun getHttpCode(): String {

            val sb = StringBuilder("${mvcBean.importHttp}\n")
            sb.appendLine("///description:${description}")
            sb.appendLine("class ${zname}Http extends ${mvcBean.extendsHttp} {")
            sb.appendLine("   ")
//            sb.appendLine("   Future<void> httpList() async{")
//            sb.appendLine("       var url='';")
//            sb.appendLine("       var para={};")
//            sb.appendLine("       var result=await httpPost(url,para);")
//            sb.appendLine("   }")
//            sb.appendLine("   ")
            sb.appendLine("}")
            return sb.toString()
        }

        fun getWidgetCode(): String {


            val sb = StringBuilder("import 'controller.dart';\n")
            sb.appendLine("///description:${description}")
            return sb.toString()
        }

        ApplicationManager.getApplication().runWriteAction{
            //是否文件已经存在
            if (isOverride) {
                removeMvcFile(directory,isHttp,isWidget);
            }


            val file = psiFileFactory.createFileFromText("view.dart", DartFileType.INSTANCE, getViewCode())

            val element=directory.add(file)
            val fileController = psiFileFactory.createFileFromText("controller.dart", DartFileType.INSTANCE, getControllerCode())
            directory.add(fileController)

            if (isHttp) {
                val fileHttp = psiFileFactory.createFileFromText("http.dart",DartFileType.INSTANCE, getHttpCode())
                directory.add(fileHttp)
            }
            if (isWidget) {
                val widgetFile = psiFileFactory.createFileFromText("widget.dart",DartFileType.INSTANCE, getWidgetCode())
                directory.add(widgetFile)
            }
//            OpenFileAction.openFile(file.virtualFile,project)
            OpenFileAction.openFile(element.containingFile.virtualFile,project)
            Notifications.Bus.notify(
                    Notification(NOTIFICATION_GROUP_ID, "FlutterMVC", "创建完成", NotificationType.INFORMATION)
            )
            RefreshListManager.manager?.refresh()


        }


    }


    fun removeMvcFile(directory: PsiDirectory, isHttp: Boolean, isWidget: Boolean) {
        try {
            directory.findFile("view.dart")?.delete()

            if(isHttp)
            directory.findFile("http.dart")?.delete()
            if(isWidget)
            directory.findFile("widget.dart")?.delete()

            directory.findFile("controller.dart")?.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}