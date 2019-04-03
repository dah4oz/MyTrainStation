package com.example.mytrainstation.parser

import android.util.Log
import com.example.mytrainstation.model.StationData
import com.example.mytrainstation.model.Train
import com.example.mytrainstation.model.TrainMovement
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream

class TrainParser {

    private var text: String? = null

    @Deprecated("This method is deprecated!")
    fun parseTrainMovement(inputStream: InputStream): List<TrainMovement> {

        val trainMovementData = ArrayList<TrainMovement>()
        var trainMovement: TrainMovement? = null

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)
            var eventType = parser.eventType

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name
                when(eventType) {
                    XmlPullParser.START_TAG -> if(tagName.equals("objTrainMovements", ignoreCase = true)) {
                        trainMovement = TrainMovement()
                    }

                    XmlPullParser.TEXT -> text = parser.text

                    XmlPullParser.END_TAG -> when {
                        tagName.equals("objTrainMovements", ignoreCase = true) -> trainMovement?.let { trainMovementData.add(it) }
                        tagName.equals("TrainCode", ignoreCase = true) -> trainMovement!!.trainCode = text
                        tagName.equals("TrainDate", ignoreCase = true) -> trainMovement!!.trainDate = text
                        tagName.equals("LocationCode", ignoreCase = true) -> trainMovement!!.locationCode = text
                        tagName.equals("LocationFullName", ignoreCase = true) -> trainMovement!!.locationFullName = text
                        tagName.equals("LocationOrder", ignoreCase = true) -> trainMovement!!.locationOrder = text
                        tagName.equals("LocationType", ignoreCase = true) -> trainMovement!!.locationType = text
                        tagName.equals("TrainOrigin", ignoreCase = true) -> trainMovement!!.trainOrigin = text
                        tagName.equals("TrainDestination", ignoreCase = true) -> trainMovement!!.trainDestination = text
                        tagName.equals("ScheduledArrival", ignoreCase = true) -> trainMovement!!.schArrival = text
                        tagName.equals("ScheduledDeparture", ignoreCase = true) -> trainMovement!!.schDeparture = text
                        tagName.equals("ExpectedArrival", ignoreCase = true) -> trainMovement!!.expArrival = text
                        tagName.equals("ExpectedDeparture", ignoreCase = true) -> trainMovement!!.expDeparture = text
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

        return trainMovementData
    }

    fun serializeTrainXmlInput(inputStream: InputStream): List<Train> {
        val trainList = ArrayList<Train>()
        var train: Train? = null
        var tempValue: String? = null

        SimpleXmlParser.parse(inputStream, object: ParseHandler {
            override fun onStartTag(tag: String?) {
                if(tag.equals("objTrainPositions", ignoreCase = true)) {
                    train = Train()
                }
            }

            override fun onEndTag(tag: String?) {
                when {
                    tag.equals("objTrainPositions", ignoreCase = true) -> train?.let { trainList.add(it) }
                    tag.equals("TrainLatitude", ignoreCase = true) -> train!!.trainLatitude = tempValue
                    tag.equals("TrainLongitude", ignoreCase = true) -> train!!.trainLongitude = tempValue
                    tag.equals("TrainCode", ignoreCase = true) -> train!!.trainCode = tempValue
                    tag.equals("TrainDate", ignoreCase = true) -> train!!.trainDate = tempValue
                    tag.equals("PublicMessage", ignoreCase = true) -> train!!.publicMessage = tempValue
                    tag.equals("Direction", ignoreCase = true) -> train!!.direction = tempValue
                }
            }

            override fun onText(text: String?) {
                tempValue = text
            }

        })

        return trainList
    }

    fun parseTrainMovement2(inputStream: InputStream): List<TrainMovement> {
        val trainMovementData = ArrayList<TrainMovement>()
        var trainMovement: TrainMovement? = null
        var tempValue: String? = null

        SimpleXmlParser.parse(inputStream, object: ParseHandler {
            override fun onStartTag(tag: String?) {
                if(tag.equals("objTrainMovements", ignoreCase = true)) {
                    trainMovement = TrainMovement()
                }
            }

            override fun onEndTag(tag: String?) {
                when {
                    tag.equals("objTrainMovements", ignoreCase = true) -> trainMovement?.let { trainMovementData.add(it) }
                    tag.equals("TrainCode", ignoreCase = true) -> trainMovement!!.trainCode = tempValue
                    tag.equals("TrainDate", ignoreCase = true) -> trainMovement!!.trainDate = tempValue
                    tag.equals("LocationCode", ignoreCase = true) -> trainMovement!!.locationCode = tempValue
                    tag.equals("LocationFullName", ignoreCase = true) -> trainMovement!!.locationFullName = tempValue
                    tag.equals("LocationOrder", ignoreCase = true) -> trainMovement!!.locationOrder = tempValue
                    tag.equals("LocationType", ignoreCase = true) -> trainMovement!!.locationType = tempValue
                    tag.equals("TrainOrigin", ignoreCase = true) -> trainMovement!!.trainOrigin = tempValue
                    tag.equals("TrainDestination", ignoreCase = true) -> trainMovement!!.trainDestination = tempValue
                    tag.equals("ScheduledArrival", ignoreCase = true) -> trainMovement!!.schArrival = tempValue
                    tag.equals("ScheduledDeparture", ignoreCase = true) -> trainMovement!!.schDeparture = tempValue
                    tag.equals("ExpectedArrival", ignoreCase = true) -> trainMovement!!.expArrival = tempValue
                    tag.equals("ExpectedDeparture", ignoreCase = true) -> trainMovement!!.expDeparture = tempValue
                }
            }

            override fun onText(text: String?) {
                tempValue = text
            }

        })

        Log.d("MainActivity", "Invoked parseTrainMovement2()!!")

        return trainMovementData
    }
}