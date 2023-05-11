package com.ejemplos.b10.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.widget.ImageView
import java.io.ByteArrayOutputStream
import java.net.URL


class ImagenUtilidad {

    fun redimensionarImagenMaximo(mBitmap: Bitmap, newWidth: Float, newHeight: Float): Bitmap {
        val width = mBitmap.width
        val height = mBitmap.height
        val scaleWidth = newWidth / width
        val scaleHeight = newHeight / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false)
    }

    fun RutaImagenToBitmap(uri:String, context: Context):Bitmap
    {
        //uri="@drawable/nombreimagensinpng
        var imageResource=context.resources.getIdentifier(uri,null,context.packageName)
        return BitmapFactory.decodeResource(context.resources,  imageResource)
    }

    fun RecursoImagenToBitmap(recurso: Int, context: Context): Bitmap {
        return BitmapFactory.decodeResource(context.getResources(), recurso)
    }

    fun StringToBitmap(imagen: String?): Bitmap {
        val decodedString: ByteArray = Base64.decode(imagen, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    fun BitmapToString(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val byte_arr: ByteArray = stream.toByteArray()
        return Base64.encodeToString(byte_arr, Base64.DEFAULT)
    }

    fun BitmapToByteArray(bitmap: Bitmap?): ByteArray? {
        if (bitmap != null) {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
            return stream.toByteArray()
        }
        return null
    }

    fun ByteArrayToBitmap(biteArray:ByteArray):Bitmap
    {
        val bitmap = BitmapFactory.decodeByteArray(biteArray, 0, biteArray.size)
        return bitmap
    }

    fun BitmapToImageView(bitmap:Bitmap, context: Context):ImageView
    {
        var foto=ImageView(context)
        var drawable=BitmapDrawable(context.resources, bitmap)
        foto.setImageDrawable(drawable)
        return  foto
    }

    fun ImageViewToBitMap(imagen:ImageView):Bitmap
    {
        var drawable=imagen.drawable as BitmapDrawable
        return drawable.bitmap
    }

    fun getToBitmapBytes(imagenbyte: ByteArray?): Bitmap? {
        return if (imagenbyte != null) {
            BitmapFactory.decodeByteArray(imagenbyte, 0, imagenbyte.size)
        } else null
    }

    fun descargaImagenToBitmap(url:String):Bitmap{

        var url= URL( url);
        var inputStream = url.openStream();
        var imagen = BitmapFactory.decodeStream(inputStream);
        return  imagen
    }

    fun comprimirImagenAntesDescarga(url:String)
    {
        var inputStream=URL(url).openStream()
        var option=BitmapFactory.Options()
        option.inSampleSize=10//Cuando mas alto es el valor mas se comprimen (tambien baja la calidad)
        var bitmap=BitmapFactory.decodeStream(inputStream,null,option)
        bitmap?.compress(Bitmap.CompressFormat.PNG,70,ByteArrayOutputStream())
    }
}