package com.example.mytrainstation.parser

import com.example.mytrainstation.model.Station
import com.example.mytrainstation.model.StationData
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream

class StationParser {

    private val stations = ArrayList<Station>()
    private var station: Station? = null
    private var text: String? = null

    @Deprecated("Please use serializeStationXMLInput()")
    fun parseStation(inputStream: InputStream): List<Station> {
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)
            var eventType = parser.eventType

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name
                when(eventType) {
                    XmlPullParser.START_TAG -> if(tagName.equals("objStation", ignoreCase = true)) {
                        station = Station()
                    }

                    XmlPullParser.TEXT -> text = parser.text
                    XmlPullParser.END_TAG -> when {
                        tagName.equals("objStation", ignoreCase = true) -> station?.let { stations.add(it) }
                        tagName.equals("StationDesc", ignoreCase = true) -> station!!.name = text
                        tagName.equals("StationLatitude", ignoreCase = true) -> station!!.latitude = text
                        tagName.equals("StationLongitude", ignoreCase = true) -> station!!.longitude = text
                        tagName.equals("StationCode", ignoreCase = true) -> station!!.code = text
                        tagName.equals("StationId", ignoreCase = true) -> station!!.id = Integer.parseInt(text)
                    }
                    else -> {}
                }

                eventType = parser.next()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return stations
    }

    fun serializeStationXMLInput(inputStream: InputStream): List<Station> {
        val stations = ArrayList<Station>()
        var station: Station? = null
        var tempValue: String? = null

        SimpleXmlParser.parse(inputStream, object: ParseHandler {
            override fun onStartTag(tag: String?) {
                if(tag.equals("objStation", ignoreCase = true)) {
                    station = Station()
                }
            }

            override fun onEndTag(tag: String?) {
                when {
                    tag.equals("objStation", ignoreCase = true) -> station?.let { stations.add(it) }
                    tag.equals("StationDesc", ignoreCase = true) -> station!!.name = tempValue
                    tag.equals("StationLatitude", ignoreCase = true) -> station!!.latitude = tempValue
                    tag.equals("StationLongitude", ignoreCase = true) -> station!!.longitude = tempValue
                    tag.equals("StationCode", ignoreCase = true) -> station!!.code = tempValue
                    tag.equals("StationId", ignoreCase = true) -> station!!.id = Integer.parseInt(tempValue)
                }
            }

            override fun onText(text: String?) {
                tempValue = text
            }

        })

        return stations
    }

    @Deprecated("Deprecated! Please use serializeStationDataXmlInput()")
     fun parseStationData(inputStream: InputStream): List<StationData> {
        val stationDataList = ArrayList<StationData>()
        var stationData: StationData? = null

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)
            var eventType = parser.eventType

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name
                when(eventType) {
                    XmlPullParser.START_TAG -> if(tagName.equals("objStationData", ignoreCase = true)) {
                        stationData = StationData()
                    }

                    XmlPullParser.TEXT -> text = parser.text

                    XmlPullParser.END_TAG -> when {
                        tagName.equals("objStationData", ignoreCase = true) -> stationData?.let { stationDataList.add(it) }
                        tagName.equals("Traincode", ignoreCase = true) -> stationData!!.trainCode = text
                        tagName.equals("Querytime", ignoreCase = true) -> stationData!!.queryTime = text
                        tagName.equals("Traindate", ignoreCase = true) -> stationData!!.trainDate = text
                        tagName.equals("Origin", ignoreCase = true) -> stationData!!.origin = text
                        tagName.equals("Destination", ignoreCase = true) -> stationData!!.destination = text
                        tagName.equals("Origintime", ignoreCase = true) -> stationData!!.originTime = text
                        tagName.equals("Destinationtime", ignoreCase = true) -> stationData!!.destinationTime = text
                        tagName.equals("Lastlocation", ignoreCase = true) -> stationData!!.lastLocation = text
                        tagName.equals("Duein", ignoreCase = true) -> stationData!!.dueIn = text
                        tagName.equals("Late", ignoreCase = true) -> stationData!!.late = text
                        tagName.equals("Exparrival", ignoreCase = true) -> stationData!!.expectedArrival = text
                        tagName.equals("Expdepart", ignoreCase = true) -> stationData!!.expectedDepart = text
                        tagName.equals("Scharrival", ignoreCase = true) -> stationData!!.schArr = text
                        tagName.equals("Schdepart", ignoreCase = true) -> stationData!!.schDepart = text
                        tagName.equals("Direction", ignoreCase = true) -> stationData!!.direction = text
                    }
                    else -> {}
                }

                eventType = parser.next()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return stationDataList
    }

    fun serializeStationDataXmlInput(inputStream: InputStream): List<StationData> {
        val stationDataList = ArrayList<StationData>()
        var stationData: StationData? = null
        var tempValue: String? = null

        SimpleXmlParser.parse(inputStream, object: ParseHandler {
            override fun onStartTag(tag: String?) {
                if(tag.equals("objStationData", ignoreCase = true)) {
                    stationData = StationData()
                }
            }

            override fun onEndTag(tag: String?) {
                when {
                    tag.equals("objStationData", ignoreCase = true) -> stationData?.let { stationDataList.add(it) }
                    tag.equals("Traincode", ignoreCase = true) -> stationData!!.trainCode = tempValue
                    tag.equals("Querytime", ignoreCase = true) -> stationData!!.queryTime = tempValue
                    tag.equals("Traindate", ignoreCase = true) -> stationData!!.trainDate = tempValue
                    tag.equals("Origin", ignoreCase = true) -> stationData!!.origin = tempValue
                    tag.equals("Destination", ignoreCase = true) -> stationData!!.destination = tempValue
                    tag.equals("Origintime", ignoreCase = true) -> stationData!!.originTime = tempValue
                    tag.equals("Destinationtime", ignoreCase = true) -> stationData!!.destinationTime = tempValue
                    tag.equals("Lastlocation", ignoreCase = true) -> stationData!!.lastLocation = tempValue
                    tag.equals("Duein", ignoreCase = true) -> stationData!!.dueIn = tempValue
                    tag.equals("Late", ignoreCase = true) -> stationData!!.late = tempValue
                    tag.equals("Exparrival", ignoreCase = true) -> stationData!!.expectedArrival = tempValue
                    tag.equals("Expdepart", ignoreCase = true) -> stationData!!.expectedDepart = tempValue
                    tag.equals("Scharrival", ignoreCase = true) -> stationData!!.schArr = tempValue
                    tag.equals("Schdepart", ignoreCase = true) -> stationData!!.schDepart = tempValue
                    tag.equals("Direction", ignoreCase = true) -> stationData!!.direction = tempValue
                }
            }

            override fun onText(text: String?) {
                tempValue = text
            }

        })

        return stationDataList
    }

}