package com.flutter.mvc.common

import com.intellij.core.JavaPsiBundle
import com.intellij.icons.AllIcons
import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import com.intellij.lang.jvm.JvmLanguage
import com.intellij.openapi.fileTypes.LanguageFileType
import org.jetbrains.annotations.NonNls
import javax.swing.Icon


class DartFileType private constructor() : LanguageFileType(Language.ANY) {
    override fun getName(): String {
        return "DART"
    }

    override fun getDescription(): String {

        return "dart"
    }

    override fun getDefaultExtension(): String {
        return "dart"
    }

    override fun getIcon(): Icon? {
        return AllIcons.FileTypes.Java
    }

    override fun isJVMDebuggingSupported(): Boolean {
        return true
    }

    companion object {
        @NonNls
        val DEFAULT_EXTENSION = "dart"

        @NonNls
        val DOT_DEFAULT_EXTENSION = ".dart"
        val INSTANCE = DartFileType()
    }
}

class DartLanguage private constructor() : Language("Dart", *arrayOf("text/x-dart-source", "text/dart", "application/x-dart", "text/x-dart")) {
    override fun getDisplayName(): String {
        return "dart"
    }

    override fun isCaseSensitive(): Boolean {
        Language.ANY
        return true
    }

    companion object {

        val INSTANCE: DartLanguage = DartLanguage()
    }
}
