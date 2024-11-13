
# AEMET Temperature Service 🌡

Este proyecto permite crear un datalake utilizando datos de la API de la Agencia Estatal de Meteorología de España (AEMET). Almacena las temperaturas máximas y mínimas en una base de datos y las expone mediante un servicio web para facilitar su consulta. El proyecto está compuesto por tres servicios principales: `datamart-provider`, `feeder`, y `temperature-service`.

## Descripción

El sistema está diseñado para realizar las siguientes tareas:

1. **Extracción de datos**: El servicio `feeder` se conecta a la API de AEMET para obtener datos meteorológicos de temperatura.
2. **Almacenamiento en base de datos**: `datamart-provider` se encarga de almacenar las temperaturas máximas y mínimas en una base de datos para su posterior consulta.
3. **Exposición de datos**: `temperature-service` ofrece un servicio web para acceder a los datos almacenados.

## Estructura del Proyecto

- **`datamart-provider`**: Servicio que gestiona la base de datos, almacenando los datos de temperatura proporcionados por `feeder`.
- **`feeder`**: Servicio responsable de la conexión con la API de AEMET y la obtención de datos meteorológicos.
- **`temperature-service`**: Servicio web que expone los datos almacenados en la base de datos para su consulta.

## Requisitos

- **Java** 11 o superior
- **Maven** para la gestión de dependencias
- Dependencias adicionales definidas en cada `pom.xml` de los servicios.

## Instalación

1. Clona este repositorio:
   ```bash
   git clone https://github.com/tuusuario/AEMET-Temperature-Service.git
   ```
2. Navega al directorio de cada servicio (`datamart-provider`, `feeder`, `temperature-service`) y compílalos con Maven:
   ```bash
   cd AEMET-Temperature-Service/Code/[nombre_del_servicio]
   mvn clean install
   ```

## Uso
1. **Ejecuta el servicio `datamart-provider`**:
   Este servicio gestiona el almacenamiento en la base de datos. Ejecuta:
   ```bash
   mvn exec:java
   ```
2. **Ejecuta el servicio `feeder`**:
   Este servicio se encarga de obtener datos de la API de AEMET. Ejecuta:
   ```bash
   mvn exec:java
   ```
3. **Ejecuta el servicio `temperature-service`**:
   Este servicio expone los datos vía web. Ejecuta:
   ```bash
   mvn exec:java
   ```

Cada servicio debe estar activo para que el sistema funcione correctamente.
