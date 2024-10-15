
# Prueba Técnica - Aplicación de Gestión de Pedidos

## Descripción
Esta aplicación está diseñada para importar una lista de pedidos online desde una API REST pública, almacenarlos en una base de datos y generar un resumen de los pedidos por distintos tipos. Además, permite exportar los pedidos ordenados por `Order ID` en formato CSV, tal como se detalla en el enunciado de la prueba.

## Stack Tecnológico
- **Java y Spring Boot**: Para el desarrollo de la aplicación backend.
- **PostgreSQL**: Base de datos relacional para almacenar los pedidos importados.
- **Docker**: Contenerización de los servicios de la aplicación.
- **Grafana**: Visualización de métricas y monitoreo.
- **Prometheus**: Recolección de métricas para el monitoreo de la aplicación.

## Configuración del Proyecto

### Variables de Entorno

Antes de levantar la aplicación, asegúrate de crear un archivo `.env` en la raíz del proyecto con las variables de entorno adjuntas en el correo de la entrega:

```bash
DB_USERNAME=user
DB_URL=url
DB_PASSWORD=pass
TOKEN_SECRET_KEY=secret
```

### Requisitos

Para levantar la aplicación necesitas tener **Docker** y **Docker Compose** instalados. Puedes instalar ambos en tu sistema siguiendo estas instrucciones:

- [Instrucciones para instalar Docker](https://docs.docker.com/get-docker/)
- [Instrucciones para instalar Docker Compose](https://docs.docker.com/compose/install/)

### Levantar la Aplicación

Una vez configurado el archivo `.env` con las variables de entorno y Docker instalado, puedes desplegar la aplicación completa ejecutando el siguiente comando:

```bash
docker-compose up
```

Esto iniciará automáticamente todos los servicios necesarios, incluidos:

- **Base de datos PostgreSQL**: Base de datos para almacenar los pedidos.
- **Backend Spring Boot**: La imagen del backend se encuentra en un repositorio público de Docker Hub: `dannyelyankee/es-publico-app:v4`.
- **Prometheus**: Servicio de monitoreo para la recolección de métricas.
- **Grafana**: Servicio para visualizar métricas y generar dashboards.

### Uso del Token JWT

Para acceder a los endpoints protegidos de la aplicación, se utiliza un token JWT que ya está pre-generado y tiene una duración de **1 mes**. Este token ha sido incluido en la colección de **Postman** adjunta al correo de la entrega.

#### Importar la colección de Postman

1. Abre Postman.
2. Haz clic en "Importar".
3. Selecciona el archivo `collection.json` adjunto a este proyecto.
4. Una vez importado, la colección de Postman ya tendrá configurado el token JWT en todas las peticiones.

### Endpoints Disponibles

#### 1. Importar pedidos y generar resumen

- **Ruta**: `POST /import/orders`
- **Descripción**: Importa los pedidos desde la API REST pública, los almacena en la base de datos y genera un resumen de los pedidos.
- **Autenticación**: Requiere JWT.

#### 2. Obtener el resumen de pedidos

- **Ruta**: `GET /orders/summary`
- **Descripción**: Devuelve un resumen del número de pedidos importados, agrupados por varios campos.
- **Autenticación**: Requiere JWT.

#### 3. Exportar pedidos a CSV

- **Ruta**: `GET /orders/export`
- **Descripción**: Exporta los pedidos a un archivo CSV, ordenado por `Order ID`. Para descargarlo desde postman a la derecha de "Send" hay un desplegable, seleccionar "Send and Download".
- **Autenticación**: Requiere JWT.


### Limpieza

Para detener y eliminar los contenedores de Docker, ejecuta:

```bash
docker-compose down
```

Esto detendrá todos los servicios y eliminará los contenedores, pero mantendrá los volúmenes de datos de la base de datos y Grafana.
