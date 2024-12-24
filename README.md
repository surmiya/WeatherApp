### **Intro**

A weather app that demonstrates skills in **Kotlin**, **Jetpack Compose**, and **clean architecture**. The app allow users to search for a city, display its weather on the home screen, and persist the selected city across launches. 

---

### **Description**

1. **Home Screen**:
    - Displays weather for a single **saved city**, including:
        - City name.
        - Temperature.
        - Weather condition (with corresponding icon from the API).
        - Humidity (%).
        - UV index.
        - "Feels like" temperature.
    - If no city is saved, prompts the user to search.
    - **Search bar** for querying new cities.
2. **Search Behavior**:
    - Shows a **search result card** for the queried city.
    - Tapping the result updates the Home Screen with the city’s weather and persists the selection.
3. **Local Storage**:
    - Uses **SharedPreferences** to persist the selected city.
    - Reloads the city’s weather on app launch.

---

### **API Integration**

- Uses **WeatherAPI.com** to fetch weather data:
    - ⭐️ 🔗 API Documentation: [WeatherAPI Documentation](https://www.weatherapi.com/docs/). 🔗 ⭐️

---

### **Tech Stack**

1. Uses **Kotlin** and **Jetpack Compose** exclusively.
2. Follow **MVVM** along with **clean architecture** with modular, testable code.
3. Use **dependency injection** (Hilt) and interface-based abstractions.

---
