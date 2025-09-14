# Weather API (Spring Boot) — Unit Testing / API de Clima (Spring Boot) — Pruebas Unitarias

> **EN:** Minimal Spring Boot service that fetches weather data from OpenWeather and exposes a simple endpoint. Includes **unit tests** for controller and service with JUnit 5, Mockito (inline), and MockMvc.  
> **ES:** Servicio mínimo en Spring Boot que consulta OpenWeather y expone un endpoint. Incluye **pruebas unitarias** para el controlador y el servicio con JUnit 5, Mockito (inline) y MockMvc.

---

## 🧩 Overview / Resumen

**Endpoint**: `GET /v1/api/weather-report?latitude={lat}&longitude={lon}`  
**Returns / Retorna**: JSON with `temperature` and `humidity`.

- **EN:** The project calls OpenWeather (`/data/2.5/weather`) using the API key you provide via environment variable. A global exception handler returns clear JSON errors (e.g., invalid API key).  
- **ES:** El proyecto llama a OpenWeather (`/data/2.5/weather`) usando la API key que pasas por variable de entorno. Un manejador global de excepciones devuelve errores JSON claros (p. ej., API key inválida).

---

## 🛠 Tech Stack / Tecnologías

- Java 17
- Spring Boot **3.0.3** (`spring-boot-starter-web`)
- Jackson (JSON)
- Testing: JUnit 5, Spring Boot Test, Mockito **inline** (para `MockedConstruction`)
- Build: Gradle (wrapper incluido)

---

## 📁 Project Structure / Estructura

```
src/
├─ main/
│  ├─ java/org/adaschool/Weather/
│  │  ├─ WeatherApplication.java
│  │  ├─ controller/WeatherReportController.java
│  │  ├─ data/
│  │  │  ├─ WeatherApiResponse.java
│  │  │  └─ WeatherReport.java
│  │  └─ config/GlobalExceptionHandler.java
│  └─ resources/application.properties
└─ test/
   └─ java/org/adaschool/Weather/
      ├─ controller/WeatherReportControllerTest.java
      └─ service/WeatherReportServiceTest.java
```

---

## ✅ Requirements / Requisitos

- **Java 17** (e.g., Amazon Corretto 17)
- Internet access / Acceso a Internet
- OpenWeather API key → https://openweathermap.org/api

---

## ⚙️ Configuration / Configuración

`src/main/resources/application.properties`

```properties
openweather.api.key=${OPENWEATHER_API_KEY:CHANGE_ME}
openweather.api.url=https://api.openweathermap.org/data/2.5/weather
openweather.api.units=metric
```

### Set environment variable / Definir variable de entorno

**Windows PowerShell**
```powershell
$env:OPENWEATHER_API_KEY = "YOUR_REAL_API_KEY"
```

**Windows CMD**
```cmd
set OPENWEATHER_API_KEY=YOUR_REAL_API_KEY
```

**macOS / Linux (bash/zsh)**
```bash
export OPENWEATHER_API_KEY="YOUR_REAL_API_KEY"
```

> **Tip EN:** In IntelliJ → *Run/Debug Configurations* → add `OPENWEATHER_API_KEY` under *Environment variables*.  
> **Tip ES:** En IntelliJ → *Run/Debug Configurations* → agrega `OPENWEATHER_API_KEY` en *Environment variables*.

---

## ▶️ Run / Ejecutar

**Gradle (terminal):**
```bash
./gradlew bootRun        # macOS/Linux
# o
gradlew.bat bootRun      # Windows
```

App runs at / La app corre en: `http://localhost:8080`

---

## 🔎 API

### GET `/v1/api/weather-report`

**Query params / Parámetros**:
- `latitude` (double)  
- `longitude` (double)

**Example / Ejemplo**:
```bash
curl "http://localhost:8080/v1/api/weather-report?latitude=4.711&longitude=-74.0721"
```

**Sample response / Respuesta ejemplo**:
```json
{
  "temperature": 18.7,
  "humidity": 75.0
}
```

**Errors (JSON) / Errores (JSON)** — handled by `GlobalExceptionHandler`:
```json
{
  "timestamp": "2025-09-14T22:45:10.123Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "OpenWeather error: {\"cod\":401,\"message\":\"Invalid API key\"}",
  "path": "/v1/api/weather-report"
}
```

---

## 🧪 Testing / Pruebas

Run all tests / Ejecutar todas las pruebas:
```bash
./gradlew clean test    # macOS/Linux
# o
gradlew.bat clean test  # Windows
```

**Included tests / Pruebas incluidas**:
- `WeatherReportControllerTest` → uses **MockMvc** & `@WebMvcTest` to assert HTTP 200 and JSON body.  
- `WeatherReportServiceTest` → uses **mockito-inline** to intercept `new RestTemplate()` (`MockedConstruction`) and verifies mapping/URL.

**Gradle test deps / Dependencias de test (build.gradle):**
```gradle
testImplementation "org.springframework.boot:spring-boot-starter-test"
testImplementation "org.mockito:mockito-inline:4.11.0"
// opcional:
testImplementation "org.mockito:mockito-junit-jupiter:4.11.0"
tasks.named("test") { useJUnitPlatform() }
```

---

## 🧰 Troubleshooting / Solución de problemas

- **500 Whitelabel** → Usually a null `main` from OpenWeather → check API key and coordinates.  
  **ES:** Suele ser `main` nulo desde OpenWeather → verifica tu API key y coordenadas.
- **401 Unauthorized** → Set `OPENWEATHER_API_KEY` properly (terminal or IntelliJ env vars).  
  **ES:** Define `OPENWEATHER_API_KEY` correctamente (terminal o variables de entorno en IntelliJ).
- **Kelvin values** → Ensure `openweather.api.units=metric` (°C).  
  **ES:** Revisa `openweather.api.units=metric` para °C.
- **Port already in use (8080)** → Change `server.port` in `application.properties`.  
  **ES:** Cambia `server.port` en `application.properties`.
- **IntelliJ shows red imports in tests** → Make sure tests are under `src/test/java` and Gradle is synced.  
  **ES:** Asegúrate de que los tests estén en `src/test/java` y sincroniza Gradle.

---

## 📄 License / Licencia

**EN:** This project is for educational purposes. You may reuse it under the MIT license.  
**ES:** Proyecto con fines educativos. Puedes reutilizarlo bajo licencia MIT.

```text
MIT License © 2025
```

---

## ✍️ Notes / Notas

- Controller: `WeatherReportController`  
- Service: `WeatherReportService` (reads props with `@Value`)  
- Data: `WeatherReport`, `WeatherApiResponse` (maps `main.temp` → `temperature`)  
- Global errors: `GlobalExceptionHandler`
