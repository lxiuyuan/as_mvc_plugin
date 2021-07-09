package com.flutter.mvc.common.element


open class Element {
    var startLine: Int = 0
    var endLine: Int = 0
    var name: String = ""
    var text=""
    var annotation=""


}

class ClassElement : Element() {
    var fields=ArrayList<FieldElement>()
    var methods=ArrayList<MethodElement>()
    var getters=ArrayList<FieldElement>()
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
    var body=""
}

//class MethodBodyElement{
//    String
//}