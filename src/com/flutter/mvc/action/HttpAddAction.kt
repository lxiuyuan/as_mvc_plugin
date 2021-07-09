package com.flutter.mvc.action

import com.flutter.mvc.common.DartFileType
import com.flutter.mvc.common.element.DJsonParse
import com.flutter.mvc.common.element.HttpElement
import com.flutter.mvc.common.utils.StringUtils
import com.flutter.mvc.ui.HttpCreateView
import com.flutter.mvc.ui.RequestView
import com.google.gson.JsonParser
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory

class HttpAddAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project!!
        val editor = event.getData(PlatformDataKeys.EDITOR) ?: return
        val psiFile = event.getData(PlatformDataKeys.PSI_FILE)!!
        val document = editor.document
        val offset = editor.caretModel.offset

        val parentDir = psiFile.parent!!
        HttpCreateView { a, b, c ->
            val httpElement=HttpElement.httpToElement(b)

            RequestView(httpElement) {


                var name = a.trim();
                if (c) {
                    name = "base"
                }else if(name.isEmpty()){
                    name=httpElement.name.trim()
                }
                name=StringUtils.removeEnd(name,"_bean")
                val str = HttpElement.elementToCode(httpElement,StringUtils._toLowName(name+"_bean"))
                var jsonCode=""
                if(httpElement.response.isNotEmpty()&&!c){
                    val jsonElement = JsonParser.parseString(httpElement.response)
                    jsonCode=DJsonParse().toCode(name,jsonElement);
                }
                WriteCommandAction.runWriteCommandAction(project) {
                    document.insertString(offset, str)
                    val beanDir = createAndGetBean(parentDir)
                    if(httpElement.response.isNotEmpty()&&!c){
                        createJsonCode(project,beanDir,name,jsonCode)
                        document.insertString(0, "import 'bean/${name}_bean.dart';\n")
                    }
                }


            }.isVisible=true

            return@HttpCreateView Unit
        }.isVisible = true


//        WriteCommandAction.runWriteCommandAction(project) {
//            document.insertString(offset, "w");
//        }
    }


    private fun createJsonCode(project:Project,beanDir:PsiDirectory,name:String,code:String){
        val psiFile=PsiFileFactory.getInstance(project).createFileFromText("${name}_bean.dart",DartFileType.INSTANCE,code)
        beanDir.add(psiFile)

    }


    private fun createAndGetBean(parentDir: PsiDirectory): PsiDirectory {
        val beanDir = parentDir.findSubdirectory("bean")
        return if (beanDir == null) {
            parentDir.createSubdirectory("bean");

        } else {
            beanDir
        }
    }


}