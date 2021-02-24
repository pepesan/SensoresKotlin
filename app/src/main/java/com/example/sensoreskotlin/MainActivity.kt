package com.example.sensoreskotlin

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal


class MainActivity : AppCompatActivity(), SensorEventListener {
    private var mSensorManager: SensorManager? = null
    private var sensorBrujula: Sensor? = null
    private var sensorAcelerometro: Sensor? = null
    private var sensorGiroscopio: Sensor? = null
    private var textView: TextView? = null
    private var xaxis: TextView? = null
    private var yaxis: TextView? = null
    private var zaxis: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView) as TextView
        xaxis = findViewById(R.id.xaxis) as TextView
        yaxis = findViewById(R.id.yaxis) as TextView
        zaxis = findViewById(R.id.zaxis) as TextView
        prepareSensorManager()
        //preparar el sensor de la brujula
        //preparar el sensor de la brujula
        prepareMagnetic()
        //preparar el sensor del acelerómetro
        //preparar el sensor del acelerómetro
        prepareAccelerometer()
        //preparar el sensor del giroscopio
        //preparar el sensor del giroscopio
        prepareGyroscopy()
    }

    private fun prepareSensorManager() {
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val deviceSensors = mSensorManager?.getSensorList(Sensor.TYPE_ALL)
    }

    private fun prepareGyroscopy() {
        sensorGiroscopio = mSensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

    private fun prepareAccelerometer() {
        sensorAcelerometro = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private fun prepareMagnetic() {
        if (mSensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            /*
            // Success! There's a magnetometer.
            Log.d("app","Hay en magnetómetro");
            List<Sensor> gravSensors = mSensorManager.getSensorList(Sensor.TYPE_GRAVITY);
            for(int i=0; i<gravSensors.size(); i++) {
                sensorBrujula =gravSensors.get(i);
                Log.d("app","Sensor:"+ sensorBrujula);

            }
            */
            sensorBrujula = mSensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        } else {
            // Failure! No magnetometer.
            Log.d("app", "No hay en magnetómetro")
        }
    }
    override fun onResume() {
        super.onResume()
        //registrar sensores
        mSensorManager!!.registerListener(this, sensorBrujula, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager!!.registerListener(this, sensorAcelerometro, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager!!.registerListener(this, sensorGiroscopio, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        //desregistrar sensores
        mSensorManager!!.unregisterListener(this)
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        //Log.d("app","onSensorChanged");
        //Log.d("app","sensor:"+sensorEvent.sensor.getType());
        if (sensorEvent.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            //Log.d("app","sensor:"+sensorEvent.sensor.getType());
            val valor = sensorEvent.values[0]
            //Log.d("app","valor:"+valor);
            val result: BigDecimal
            result = round(valor, 2)
            textView!!.text = "" + result
            // Do something with this sensor value.
        }
        if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val valorx = sensorEvent.values[0]
            val valory = sensorEvent.values[1]
            val valorz = sensorEvent.values[2]
            var result: BigDecimal
            result = round(valorx, 2)
            textView!!.text = "" + result
            xaxis!!.text = "" + result
            result = round(valory, 2)
            yaxis!!.text = "" + result
            result = round(valorz, 2)
            zaxis!!.text = "" + result
        }
    }

    fun round(d: Float, decimalPlace: Int): BigDecimal {
        var bd = BigDecimal(java.lang.Float.toString(d))
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP)
        return bd
    }

    override fun onAccuracyChanged(sensor: Sensor?, i: Int) {}
}