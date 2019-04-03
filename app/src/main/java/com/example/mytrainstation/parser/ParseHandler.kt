package com.example.mytrainstation.parser

interface ParseHandler {

    fun onStartTag(tag: String?)

    fun onEndTag(tag: String?)

    fun onText(text: String?)
}