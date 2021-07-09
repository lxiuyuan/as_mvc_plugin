package com.flutter.mvc.common.utils

import com.flutter.mvc.common.element.ClassElement

class ElementKuoUtils {
    companion object{
        //获取对称{};
        fun getEndElement(text:String,startLine:Int):Int{
            val length=text.length
            val startIndexs=ArrayList<Int>()
            val endIndexs=ArrayList<Int>()
            var end=-1
            //获取结束位置
            for ( i in startLine until length){
                if(end>0) {
                    val char=text[i]
                    if(char==' '){
                        continue
                    }
                    if(char=='\n'){
                        return i
                    }else{
                        return end
                    }
                }
                if(text[i]=='{')startIndexs.add(i)
                if(text[i]=='}'){
                    endIndexs.add(i)
                    if(endIndexs.size==startIndexs.size){
                        end=i
                    }
                }
            }

            return end;
        }
        //是否是方法
        fun checkIsFun(text: String): Boolean {
            val start = text.indexOf('{')
            if(start==-1){
                return false
            }
            val leftCode = text.subSequence(0, start)
            if (leftCode.trimEnd().endsWith(')')) {
                return true
            }
            return false
        }

        //是否是class
        fun checkIsClass(text:String):Boolean{
            if(!text.contains("class ")){
                return false
            }
            val index=text.indexOf("class ")
            if(index==0){
                return true
            }
            val left=text[index-1]
            return (left=='\n' || left==' ')
        }

        //是否是扩展函数
        fun checkIsExtension(text: String): Boolean {
            return (text.contains("extension ") && text.contains(" on "))
        }
        //是否是get方法
        fun checkIsGet(code: String): Boolean {
            val startIndex=code.indexOf('{')
            val text=code.subSequence(0,startIndex)

            if(!text.contains("get ")){
                return false
            }
            val index=text.indexOf("get ")
            if(index==0){
                return true
            }
            val left=text[index-1]
            return (left=='\n' || left==' ')
        }
        //是否是set方法
        fun checkIsSet(text: String): Boolean {
            val startIndex=text.indexOf('{')
            val text=text.subSequence(0,startIndex)
            if(!text.contains("set ")){
                return false
            }
            val index=text.indexOf("set ")
            if(index==0){
                return true
            }
            val left=text[index-1]
            return (left=='\n' || left==' ')
        }

        //获取方法名字
        fun getFunName(text:String):String{
            val startIndex=text.indexOf('(')
            for(i in startIndex downTo 0){
                val char=text[i]
                if(char==' ') {
                    return text.substring(i,startIndex).trim()
                }

            }
            return ""

        }
        //获取方法体
        fun getFunBody(text:String):String{
            val startIndex=text.indexOf(')')+1
            return text.substring(startIndex)
        }

        //获取类名
        fun getClassName(text: String): String {
            val startIndex=text.indexOf('{')
            for(i in startIndex downTo 0){
                val char=text[i]
                if(char==' ') {
                    return text.substring(i,startIndex).trim()
                }

            }
            return ""
        }

        fun getClassBodyPos(text:String,classElement: ClassElement):List<Int>{
            val pos=ArrayList<Int>()
            val codeF = text.subSequence(classElement.startLine, classElement.endLine).toString()
            var startLine=classElement.startLine+classElement.annotation.length
            var endLine=classElement.endLine
            val code = text.substring(startLine, endLine)
            val startIndex=code.indexOf('{')
            startLine+=startIndex
            val body = ElementKuoUtils.getClassBody(code).trim()
            if(body.startsWith("{")){
                startLine++
            }
            if(body.endsWith("}")){
                endLine--
            }
            pos.add(startLine)
            pos.add(endLine)
            return pos
        }

        //获取方法体
        fun getClassBody(text:String):String{
            val startIndex=text.indexOf('{')
            return text.substring(startIndex)
        }

        fun getGetterNameTypeValue(text: String): List<String> {
            val str=ArrayList<String>();
            val index=text.indexOf('{')
            val left=text.subSequence(0,index).trim();
            val words=left.split(" ")
            //添加name
            str.add(words.last())
            val type=words[words.lastIndex-1]
            //添加类型
            if(type=="get"){
                str.add("dynamic")
            }else{
                str.add(type)
            }
            //添加value
            str.add(text.substring(index,text.length))
            return str

        }


    }
}