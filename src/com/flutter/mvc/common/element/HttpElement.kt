package com.flutter.mvc.common.element

import com.flutter.mvc.common.utils.StringUtils
import java.lang.StringBuilder

class RequestElement {
    var name = ""
    var type = ""
    var isRequest = ""
    var isSelect = true
    var remark = ""
    var state = 1;
    var tag = ""


    override fun toString(): String {
        val sb = StringBuilder("-----\n");
        sb.appendLine("name:${name}")
        sb.appendLine("type:${type}")
        sb.appendLine("isRequest:${isRequest}")
        sb.appendLine("tag:${tag}")
        return sb.toString()
    }
}

class HttpElement {
    var annotation = ""
    var method = ""
    var url = "";
    var name = ""
    var request = ArrayList<RequestElement>();
    var requestMust = ArrayList<RequestElement>();
    var requestDefault = ArrayList<RequestElement>();
    var requestNull = ArrayList<RequestElement>();
    var response = ""


    fun addResponse(list: MutableList<String>) {
        val sb = StringBuilder()
        for (s in list) {
            sb.appendLine(s)
        }
        response = sb.toString()
    }

    fun trim() {
        requestMust.clear()
        requestDefault.clear()
        requestNull.clear()
        for (requestElement in request) {
            if (requestElement.state == 1) {
                requestMust.add(requestElement)
            } else if (requestElement.state == 2) {
                if (requestElement.remark.isNotEmpty()) {
                    requestDefault.add(requestElement)
                } else {
                    requestNull.add(requestElement)
                }
            }
        }
    }


    fun addRequest(list: MutableList<String>) {
        request.clear()
        var requestElement = RequestElement()
        for (s in list) {
            if (s.trim().isEmpty()) {
                if (requestElement.name.isEmpty()) {
                    continue
                }
            }
            if (requestElement.name.isEmpty()) {
                requestElement.name = s;
            } else if (requestElement.type.isEmpty()) {
                requestElement.type = s;
            } else if (requestElement.isRequest.isEmpty()) {
                if (s.trim().isEmpty()) {
                    requestElement.isRequest = "false"
                } else {
                    requestElement.isRequest = s

                }
            } else if (requestElement.tag.isEmpty()) {
                if (s.trim().isEmpty()) {

                    requestElement.tag = "暂无"
                } else {

                    requestElement.tag = s
                }
            } else {
                request.add(requestElement)
                requestElement = RequestElement()
            }

        }

        val newList = request.dropWhile { it.name == "参数名称" }
        request.clear();
        request.addAll(newList)
//        println(newList)
        if (requestElement.name.isNotEmpty()) {
            request.add(requestElement)
        }
//        for (requestElement in request) {
//
//            println(requestElement);
//        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.appendLine("注释：" + annotation)
        sb.appendLine("类型：" + method)
        sb.appendLine("url：" + url)
        sb.appendLine("request：" + request)
        sb.appendLine("response：" + request)
        return sb.toString()

    }

    companion object {
        val value = "获取验证码 [/code/v1/op] [post]\n" +
                "\n" +
                "\n" +
                "@Header\n" +
                "\n" +
                "@Request (application/x-www-form-urlencoded)\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "参数名称\n" +
                "参数类型\n" +
                "是否必选\n" +
                "说明\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "type\n" +
                "string\n" +
                "required\n" +
                "值固定：reg\n" +
                "\n" +
                "\n" +
                "phone_name\n" +
                "string\n" +
                "required\n" +
                "手机号码\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "@Response (application/json)\n" +
                "\n" +
                "{\n" +
                "    code : 0\n" +
                "    msg : \"\"\n" +
                "    time : 1416883384 \n" +
                "}"

        fun httpToElement(source: String): HttpElement {
            val element = HttpElement()
            val spec = source.split("\n")

            val newSpec = ArrayList<String>()
            var isRemove = false
            for (s in spec) {
                if (s.trim().isNotEmpty()) {
                    isRemove = true;
                    newSpec.add(s.trim())
                } else if (isRemove) {
                    newSpec.add(s.trim())
                }
//                println(s)
            }
            var requestIndex = -1;
            var requestEndIndex = -1;
            var responseIndex = newSpec.size - 1;
            for (i in 0 until newSpec.size) {
                val value = newSpec[i]
                if (value.startsWith("@")) {

                    if (value.toLowerCase().startsWith("@request")) {
                        requestIndex = i
                    } else if (value.toLowerCase().startsWith("@response")) {
                        responseIndex = i
                        if (requestEndIndex == -1 && requestIndex != -1) {
                            requestEndIndex = i
                        }
                    } else {
                        if (requestEndIndex == -1 && requestIndex != -1) {
                            requestEndIndex = i
                        }
                    }
                }
            }
            val requestList = newSpec.subList(requestIndex + 1, requestEndIndex)
            val responseList = newSpec.subList(responseIndex + 1, newSpec.size)
            element.addRequest(requestList)
            element.addResponse(responseList)
            urlString(element, newSpec.first())
            return element
        }


        @JvmStatic
        fun main(args: Array<String>) {
            val element = httpToElement(value)

            println(element.request)
        }

        private fun urlTo_(url: String): String {
            val sb = StringBuilder()
            for (i in 0..url.length - 1) {
                val char = url[i]
                if (char == '/' || char == '-') {
                    if (sb.toString().isNotEmpty())
                        sb.append("_")
                } else {
                    sb.append(char)
                }
            }
            return sb.toString()
        }

        fun elementToCode(element: HttpElement, beanName: String): String {
            val name = element.name
            println("name:${name}")
            val nameFinish = StringUtils.initialUpper(StringUtils._toLowName(name))
            val sb = StringBuilder();
            if (element.annotation.isNotEmpty())
                sb.appendLine("    //${element.annotation}")
            sb.appendLine("    Future<${beanName}> http${nameFinish}(")
            //整理
            element.trim()
            //方法必须按参数
            for (requestElement in element.requestMust) {
                if(element.requestMust.last()==requestElement&&
                        (element.requestDefault.size > 0 || element.requestNull.size > 0)){

                    sb.appendLine("        String ${StringUtils.initialLower(StringUtils._toLowName(requestElement.name))}, {")
                }else {
                    sb.appendLine("        String ${StringUtils.initialLower(StringUtils._toLowName(requestElement.name))},")
                }
            }
            //方法可选参数
            if (element.requestDefault.size > 0 || element.requestNull.size > 0) {
                var i = 0;
                for (requestElement in element.requestDefault) {
                    if (i == 0) {
                        sb.appendLine("        String ${StringUtils.initialLower(StringUtils._toLowName(requestElement.name))}=\"${requestElement.remark}\",")
                    } else {
                        sb.appendLine("        String ${StringUtils.initialLower(StringUtils._toLowName(requestElement.name))}=\"${requestElement.remark}\",")
                    }
                    i++
                }
                for (requestElement in element.requestNull) {
                    sb.appendLine("        String ${StringUtils.initialLower(StringUtils._toLowName(requestElement.name))}=\"${requestElement.remark}\",")
                }

                sb.appendLine("    }) async {")
            } else {
                sb.appendLine("    ) async {")
            }
            sb.appendLine("        final url=\"${element.url}\";")
            sb.appendLine("        final para=<String,String>{")
            //设置map必选参数
            for (requestElement in element.requestMust) {
                sb.appendLine("            \"${requestElement.name}\":${StringUtils.initialLower(StringUtils._toLowName(requestElement.name))},")
            }
            //设置map可选参数
            for (requestElement in element.requestDefault) {
                sb.appendLine("            \"${requestElement.name}\":${StringUtils.initialLower(StringUtils._toLowName(requestElement.name))},")
            }
            sb.appendLine("        };")

            //设置map可空参数
            if (element.requestNull.size > 0) {
                for (requestElement in element.requestNull) {
                    sb.appendLine("       if(${StringUtils.initialLower(StringUtils._toLowName(requestElement.name))}.isNotEmpty){")
                    sb.appendLine("           para[\"${requestElement.name}\"]=${StringUtils.initialLower(StringUtils._toLowName(requestElement.name))};")
                    sb.appendLine("       }")

                }
            }
            sb.appendLine("       return ${beanName}(await http${StringUtils.initialUpper(element.method)}(url,para));")

            sb.appendLine("    }")
            return sb.toString()

        }

        private fun urlString(element: HttpElement, url: String) {
            val split = url.split("[")
            element.annotation = split.first()
            element.url = StringUtils.removeEnd(split[1].trim(), "]")
            element.name = urlTo_(element.url)
            if (split.size > 2) {
                element.method = StringUtils.removeEnd(split[2].trim(), "]")
            }
        }
    }
}