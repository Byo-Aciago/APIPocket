package com.example.apipocket.helper;

public class WeatherHelper {

    public static class WeatherInfo {
        private final String description;
        private final String emoji;

        public WeatherInfo(String description, String emoji) {
            this.description = description;
            this.emoji = emoji;
        }

        public String getDescription() {
            return description;
        }

        public String getEmoji() {
            return emoji;
        }
    }

    public static WeatherInfo getWeatherInfo(int code, boolean isDay) {
        switch (code) {
            case 0:
                return isDay ? new WeatherInfo("Cielo despejado", "☀️") : new WeatherInfo("Noche despejada", "🌙");
            case 1:
            case 2:
                return isDay ? new WeatherInfo("Parcialmente nublado", "⛅") : new WeatherInfo("Parcialmente nublado", "☁️");
            case 3:
                return new WeatherInfo("Nublado", "☁️");
            case 45:
            case 48:
                return new WeatherInfo("Niebla", "🌫️");
            case 51:
            case 53:
            case 55:
                return new WeatherInfo("Llovizna", "🌦️");
            case 61:
            case 63:
            case 65:
                return new WeatherInfo("Lluvia", "🌧️");
            case 71:
            case 73:
            case 75:
                return new WeatherInfo("Nieve", "🌨️");
            case 80:
            case 81:
            case 82:
                return new WeatherInfo("Chubascos de lluvia", "🌧️");
            case 95:
            case 96:
            case 99:
                return new WeatherInfo("Tormenta eléctrica", "⛈️");
            default:
                return new WeatherInfo("Código " + code, "🌤️");
        }
    }
}
