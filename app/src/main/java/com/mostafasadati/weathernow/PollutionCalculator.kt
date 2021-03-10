package com.mostafasadati.weathernow

class PollutionCalculator {
    companion object {
       private const val p0 = 50.04
       private const val p1 = 50.008
       private const val p2 = 50
       private const val p3 = 49.97
       private const val p4 = 100
       private const val p5 = 100


        fun calculateAqi(p: Double): Int {
            var aqi = 0.0

            when {
                p > 0 && p <= 12 -> aqi = (p * 4.17)

                p > 12 && p <= 35.5 -> aqi = p0 + ((p - 12) * 2.128)

                p > 35.5 && p <= 55.5 -> aqi = p0 + p1 + ((p - 35.5) * 2.5)

                p > 55.5 && p <= 150.5 -> aqi = p0 + p1 + p2 + ((p - 55.5) * 0.526)

                p > 150.5 && p <= 250.5 -> aqi = p0 + p1 + p2 + p3 + (p - 150.5)

                p > 250.5 && p <= 350.5 -> aqi = p0 + p1 + p2 + p3 + p4 + (p - 250.5)

                p > 350.5 -> aqi = p0 + p1 + p2 + p3 + p4 + p5 + ((p - 350.5) * 0.67)
            }

            return aqi.toInt()

        }
    }
}