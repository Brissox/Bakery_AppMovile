# Bakery_AppMovile
 
----------------------------------
BakeryApp es una aplicación móvil desarrollada como proyecto del curso "Creación de Apps Móviles". La app permite a los usuarios explorar productos de panadería, hacer pedidos en línea, programar entregas o recogidas, y recibir notificaciones sobre promociones y novedades.
----------------------------------
## Funcionalidades

### Autenticación y Gestión de Usuarios
- **Login:** Inicio de sesión validado contra **Firebase Authentication**.
- **Registro:** Creación de cuenta que sincroniza Firebase Auth con la base de datos propia para guardar datos adicionales:
  - RUN, DV, Fecha de Nacimiento.
- **Recuperar Contraseña:** Envío de correos de recuperación mediante Firebase.
- **Perfil:** Visualización y edición de datos del usuario. Permite subir foto de perfil usando la **Cámara** o la **Galería** del dispositivo.

### Catálogo y Navegación
- **Home:** Pantalla de bienvenida con accesos rápidos.
- **Principal:** Listado de productos (Pasteles, Tortas, etc.) visualizados en una grilla (`LazyVerticalGrid`).  
  - Incluye filtrado por categorías mediante **Chips**.
- **Favoritos:** Permite marcar o desmarcar productos favoritos de forma local.

### Proceso de Compra (Carrito y Pago)
- **Carrito:** 
  - Agregar productos, modificar cantidades (+/-) y eliminar ítems.  
  - Cálculo automático del total.  
  - Persistencia local básica.
- **Pago (Checkout):**
  - Formulario para datos de despacho (Dirección, Contacto).  
  - Selección de método de pago.  
  - Genera la orden en el backend.

### Gestión de Pedidos
- **Historial:** Visualización de pedidos realizados por el usuario, mostrando:
  - Fecha
  - Total
  - Método de pago
  - Detalle de productos

### Utilidades Extra
- **Agenda / Recordatorios:** Gestión de recordatorios (CRUD) con selección de fecha.
- **Feriados:** Consulta a una API externa para listar los feriados de Chile.

----------------------------------

## Endpoints utilizados

La aplicación consume datos de dos fuentes principales, configuradas en:

- `Data/Remote/RetrofitInstance1.kt`
- `Data/Remote/RetrofitClient.kt`

### A. Microservicios Locales (Backend Propio)

La app apunta a `10.0.2.2` (localhost del emulador) en diferentes puertos.

#### Servicio de Usuarios (Puerto 8081)
- `POST /Usuarios/Reg` - Registrar un nuevo usuario.
- `GET /Usuarios/uid/{uidFb}` - Obtener datos del usuario por UID de Firebase.
- `PUT /Usuarios/{uidFb}/nombre` - Actualizar nombre del usuario.
- `PUT /Usuarios/{uid}/imagen` - Subir/Actualizar foto de perfil (Multipart).

#### Servicio de Productos (Puerto 8084)
- `GET /Productos` - Obtener la lista de productos disponibles.
- `GET /Productos` - (Duplicado en interfaz, posiblemente para obtener un solo producto).

#### Servicio de Pedidos (Puerto 8085)
- `POST /pedidos/crear` - Enviar una nueva orden de compra con su detalle.
- `GET /pedidos/uid/{uid}` - Listar el historial de pedidos de un usuario.

### B. API Externa (Servicios Públicos)

Configurado en `RetrofitClient` apuntando a `https://api.boostr.cl/`:

- `GET /holidays.json` - Obtiene la lista de feriados de Chile.

----------------------------------
## Pasos para ejecutar

### 3.1 Prerrequisitos
- **Android Studio Ladybug** o superior.
- **JDK 11** (requerido por `build.gradle.kts`).

### 3.2 Configuración del Backend
- La aplicación espera que los servicios backend estén corriendo en tu máquina local:
  - Puerto 8081 → Usuarios
  - Puerto 8084 → Productos
  - Puerto 8085 → Pedidos
- Levantar estos microservicios antes de abrir la app.

### 3.3 Configuración de Firebase
- Asegúrate de que el archivo `google-services.json` esté presente en la carpeta `app/`.

### 3.4 Ejecución en Android Studio
1. Abrir el proyecto y esperar a que termine la sincronización de **Gradle**.
2. Seleccionar el módulo `app` en la configuración de ejecución.
3. Elegir un **Emulador Android** (API 26+ recomendado).  
   > Nota: La IP `10.0.2.2` permite que el emulador vea el "localhost" de tu PC.  
   > Si usas un dispositivo físico, reemplaza esta IP por la IP local de tu computadora.

### 3.5 Flujo de prueba
1. Registrarse y crear una cuenta.
2. Iniciar sesión con la cuenta creada.
3. Navegar al **Home**, agregar productos al **Carrito**.
4. Proceder a **Ir a Pagar** para generar un pedido.

----------------------------------

## Tecnologías utilizadas
- **Frontend:** Kotlin, Jetpack Compose, MVVM, Retrofit, Firebase Authentication.
- **Backend:** Microservicios en Spring Boot con puertos independientes.
- **Base de datos:** Propia para usuarios, productos y pedidos.

----------------------------------

## Autores
- **Integrantes:**
- Bastian Brisso
- Carlos Camero
- Carlos Muñoz
  
- **Institución:** Duoc UC - Escuela de Informática y Telecomunicaciones  

----------------------------------

## 📄 Licencia
Este proyecto se distribuye bajo la **licencia MIT**, lo que permite su uso académico y educativo sin restricciones.  

----------------------------------

## Próximas mejoras
- Integración con servicios REST externos adicionales.  
- Animaciones y transiciones personalizadas en **Jetpack Compose**.
- Agregar nuevas funcionalidades con pedidos
- Integrar seguimiento de los envios de pedidos

----------------------------------
# Trello 

https://trello.com/b/ngAIMlWm/bakeryapp
----------------------------------
# BackEnd

https://github.com/Brissox/nuevo_back.git
    

