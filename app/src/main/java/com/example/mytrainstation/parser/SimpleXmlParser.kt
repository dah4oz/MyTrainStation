package com.example.mytrainstation.parser

import com.example.mytrainstation.model.TrainMovement
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream

class SimpleXmlParser {

    companion object {
        fun parse(inputStream: InputStream, handler: ParseHandler) {
            try {
                val factory = XmlPullParserFactory.newInstance()
                factory.isNamespaceAware = true
                val parser = factory.newPullParser()
                parser.setInput(inputStream, null)
                var eventType = parser.eventType

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    val tagName = parser.name
                    when(eventType) {
                        XmlPullParser.START_TAG -> handler.onStartTag(tagName)

                        XmlPullParser.TEXT -> handler.onText(parser.text)

                        XmlPullParser.END_TAG -> handler.onEndTag(tagName)
                    }

                    eventType = parser.next()
                }
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}