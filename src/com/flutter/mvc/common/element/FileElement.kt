package com.flutter.mvc.common.element

import com.flutter.mvc.common.utils.ClassUtils
import com.flutter.mvc.common.utils.ElementFenUtils
import com.flutter.mvc.common.utils.ElementKuoUtils


class FileElement {
    var imports = ArrayList<String>()
    var fields = ArrayList<FieldElement>()
    var methods = ArrayList<MethodElement>()
    var classs = ArrayList<ClassElement>()
    var getters = ArrayList<FieldElement>()
    var setters = ArrayList<MethodElement>()


    private fun addClassElement(text: String, classElement: ClassElement) {

        val pos = ElementKuoUtils.getClassBodyPos(text, classElement)
        var i = pos[0]
        val length = pos[1]
        val body = text.subSequence(i, length)

//        println("body--${body}")
        var startLine = i
        while (i < length) {
            val char = text[i]
            if (char == ';') {
                val endLine = ElementFenUtils.getEndLine(text, i)
                addFenElement(text, startLine, endLine, classElement)
                startLine = endLine
                i = endLine + 1
            } else if (char == '{') {
                val endLine = ElementKuoUtils.getEndElement(text, startLine)
                addKuoElement(text, startLine, endLine, classElement)
                startLine = endLine
                i = endLine + 1
            }
            i++
        }

    }

    private fun addFenElement(text: String, startLine: Int, endLine: Int, classElement: ClassElement? = null) {

        val codeF = text.subSequence(startLine, endLine).toString()
        val annotation = ElementFenUtils.getAnnotation(codeF)
        val code = codeF.substring(annotation.length, codeF.length)

        if (code.startsWith("import ")) {
            imports.add(code)
            println("import")
            //添加setter
        } else if (ElementFenUtils.checkIsSet(code)) {
            val method = MethodElement()
            method.startLine = startLine
            method.endLine = endLine
            method.text = codeF
            method.annotation = annotation
            method.name = ElementFenUtils.getFunName(code)
            method.body = ElementFenUtils.getFunBody(code)
            if (classElement == null)
                setters.add(method)
            else
                classElement.setters.add(method)
            println("setter-${classElement!=null}:${method.name}")
            //添加方法
        } else if (ElementFenUtils.checkIsFun(code)) {
            val method = MethodElement()
            method.startLine = startLine
            method.endLine = endLine
            method.text = codeF
            method.annotation = annotation
            method.name = ElementFenUtils.getFunName(code)
            method.body = ElementFenUtils.getFunBody(code)
            if (classElement == null)
                methods.add(method)
            else classElement.methods.add(method)
            println("fun-${classElement!=null}:${method.name}")
            //添加getter
        } else if (ElementFenUtils.checkIsGet(code)) {
            val field = FieldElement()
            field.startLine = startLine
            field.endLine = endLine
            field.text = codeF
            field.annotation = annotation
            val nameTypeValue = ElementFenUtils.getGetterNameTypeValue(code)
            field.name = nameTypeValue[0]
            field.type = nameTypeValue[1]
            field.value = nameTypeValue[2]
            if (classElement == null)
                getters.add(field)
            else classElement.getters.add(field)

            println("gettre-${classElement!=null}:${field.name}")
        } else if (ElementFenUtils.checkIsExtension(code)) {
            println("Extension")
        } else {
            val field = FieldElement()
            field.startLine = startLine
            field.endLine = endLine + 1
            field.text = codeF
            field.annotation = annotation
            val nameTypeValue = ElementFenUtils.getFieldNameTypeValue(code)
            field.name = nameTypeValue[0]
            field.type = nameTypeValue[1]
            field.value = nameTypeValue[2]
            if (classElement == null)
                fields.add(field)
            else
                classElement.fields.add(field)
            println("field-${classElement != null}:${field.name}")
        }

    }

    private fun addKuoElement(text: String, startLine: Int, endLine: Int, classElement: ClassElement? = null) {
        val codeF = text.subSequence(startLine, endLine).toString()
        val annotation = ElementFenUtils.getAnnotation(codeF)
        val code = codeF.substring(annotation.length, codeF.length)
        if (ElementKuoUtils.checkIsSet(code)) {
            val method = MethodElement()
            method.startLine = startLine
            method.endLine = endLine
            method.text = codeF
            method.annotation = annotation
            method.name = ElementKuoUtils.getFunName(code)
            method.body = ElementKuoUtils.getFunBody(code)

            if (classElement == null)
                setters.add(method)
            else
                classElement.setters.add(method)

            println("setter-${classElement != null}:${method.name}")
        } else if (ElementKuoUtils.checkIsFun(code)) {
            val method = MethodElement()
            method.startLine = startLine
            method.endLine = endLine
            method.text = codeF
            method.annotation = annotation
            method.name = ElementKuoUtils.getFunName(code)
            method.body = ElementKuoUtils.getFunBody(code)

            if (classElement == null)
                methods.add(method)
            else
                classElement.methods.add(method)

            println("fun-${classElement != null}:${method.name}")
        } else if (ElementKuoUtils.checkIsGet(code)) {
            val field = FieldElement()
            field.startLine = startLine
            field.endLine = endLine
            field.text = codeF
            field.annotation = annotation
            val nameTypeValue = ElementKuoUtils.getGetterNameTypeValue(code)
            field.name = nameTypeValue[0]
            field.type = nameTypeValue[1]
            field.value = nameTypeValue[2]
            if (classElement == null)
                getters.add(field)
            else
                classElement.getters.add(field)


            println("getter-${classElement != null}:${field.name}")

        } else if (ElementKuoUtils.checkIsExtension(code)) {
            println("extension")
        } else if (ElementKuoUtils.checkIsClass(code)) {
            val clazz = ClassElement()
            clazz.annotation = annotation
            clazz.startLine = startLine
            clazz.endLine = endLine
            clazz.text = codeF
            clazz.name = ElementKuoUtils.getClassName(code)

            println("class:${clazz.name}")
            addClassElement(text, clazz)

            if (classElement == null)
                classs.add(clazz)
            else
                println("class不允许class子类")

        }
    }

    fun nnnn(text: String) {
        var i = 0
        val length = text.length
        var startLine = 0
        while (i < length) {
            val char = text[i]
            if (char == ';') {
                val endLine = ElementFenUtils.getEndLine(text, i)
                addFenElement(text, startLine, endLine)
                startLine = endLine
                i = endLine
            } else if (char == '{') {
                val endLine = ElementKuoUtils.getEndElement(text, startLine)
                addKuoElement(text, startLine, endLine)
                startLine = endLine
                i = endLine
            }
            i++
        }

    }

}
