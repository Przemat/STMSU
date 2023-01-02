package com.example.stmsu

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory


class WebService : AsyncTask<Double, Void, String>() {
    private final val USER_AGENT = "Mozilla/5.0"
    var iv :ImageView? = null
    var btn: Button? = null
    var decodedByte :Bitmap? = null
    // HTTP POST request
    fun sendPost(left: Double?, up: Double?, right: Double?, down: Double?) {

        val url = "http://przemat.live/soap/"
        val body = "<SOAP-ENV:Envelope" +
                " xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xmlns:ns0=\"django.soap.example\"" +
                " xmlns:ns1=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<SOAP-ENV:Header/>" +
                "<ns1:Body>" +
                "<ns0:maps>" +
                "<ns0:left>" +
                left.toString() +
                "</ns0:left>" +
                "<ns0:up>" +
                up.toString() +
                "</ns0:up>" +
                "<ns0:right>" +
                right.toString() +
                "</ns0:right>" +
                "<ns0:down>" +
                down.toString() +
                "</ns0:down>" +
                "</ns0:maps>" +
                "</ns1:Body>" +
                "</SOAP-ENV:Envelope>"

        Log.e("XML: ",body )
        val obj : URL = URL(url);
        val con : HttpURLConnection =  obj.openConnection() as HttpURLConnection

        // Ustawienie nagłówków żądania
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

        // Wysłanie ciała żądania
        con.setDoOutput(true)
        var wr :DataOutputStream= DataOutputStream(con.getOutputStream())
        wr.write(body.toByteArray(Charsets.UTF_8))
        wr.flush()
        wr.close()

        val input = BufferedReader(InputStreamReader(con.inputStream))
        var inputLine: String?
        val response = StringBuffer()

        while (input.readLine().also { inputLine = it } != null) {
            response.append(inputLine)
        }
        input.close();
Log.e("map",response.toString())
        // Wypisanie odpowiedzi serwera
        val doc : Document? = convertStringToXMLDocument( response.toString() );
        val encodedImage = doc!!.firstChild.firstChild.firstChild.firstChild.firstChild.nodeValue

        val decodedString: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

    }

    private fun convertStringToXMLDocument(xmlString: String): Document? {
        //Parser that produces DOM object trees from XML content
        val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()

        //API to obtain DOM Document instance
        var builder: DocumentBuilder? = null
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder()

            //Parse the content to Document object
            return builder.parse(InputSource(StringReader(xmlString)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun doInBackground(vararg params: Double?): String? {
        sendPost(params[0],params[1],params[2],params[3])
        return "Done"
    }
    override fun onPostExecute(result: String?) {
        iv!!.setImageBitmap(decodedByte)
        btn!!.isEnabled = true
    }

    override fun onPreExecute() {
        btn!!.isEnabled = false
    }

    fun setImageView(imageView: ImageView) {
        iv = imageView
    }

    fun setButton(button: Button?) {
        btn = button
    }

}
