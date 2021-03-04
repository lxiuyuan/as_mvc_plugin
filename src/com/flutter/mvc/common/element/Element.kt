package com.flutter.mvc.common.element


open class Element {
    var startLine: Int = 0
    var endLine: Int = 0
    var name: String = ""
    var text=""


}

class FileElement{
    var imports=ArrayList<String>()
    var fields=ArrayList<FieldElement>()
    var methods=ArrayList<MethodElement>()
    var classElement=ArrayList<MethodElement>()
}

class ClassElement : Element() {
    var fields=ArrayList<FieldElement>()
    var methods=ArrayList<MethodElement>()
    var getters=ArrayList<MethodElement>()
    var setters=ArrayList<MethodElement>()
}

class FieldElement : Element() {
    var type=""
    var value=""
}
class GetElement:Element(){

}
class SetterElement:Element(){

}

class MethodElement : Element() {
    var line=0
}

//class MethodBodyElement{
//    String
//}