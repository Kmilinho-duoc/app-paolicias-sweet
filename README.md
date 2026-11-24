# app-paolicias-sweet

Este proyecto trata del desarrollo de una aplicación móvil para la gestión, cotización y reserva de eventos de banquetería para la empresa "Paolicias.Sweet". Este proyecto fue realizado en Android Studio.

# Funcionalidades por rol

- Rol cliente: catálogo de productos, detalle producto, carrito de compras (para acumular varios productos), reserva (fecha, hora, observaciones), comprobante (cámara para deposito en banco / galería para captura de transferencia), mis reservas (con estado actual), usuario (cerrar sesión).
- Rol administrador: agenda de reservas de clientes, validar pagos (cambio de estado), administración de productos y usuarios en la nube, notificiaciones.

## Endpoints utilizados

| Microservicio | Puerto | Endpoints |

| Usuarios | 8083 | `GET/POST /api/usuarios` · `GET/PUT/DELETE /api/usuarios/{id}` · `GET /api/usuarios/email/{email}` |
| Productos | 8081 | `GET/POST /api/productos` · `GET/PUT/DELETE /api/productos/{id}` · `GET/POST /api/productos/{id}/imagen` |
| Reservas | 8082 | `GET/POST /api/reservas` · `GET/DELETE /api/reservas/{id}` · `PATCH /api/reservas/{id}/estado` · `GET/POST /api/reservas/{id}/comprobante` |

# Estructura

El proyecto utiliza la estructura MVVM (Model-View-ViewMode) y cuenta con la utilización de una base de datos local SQLite (utilizando exclusivamente para el registro e inicio de sesión del usuario, ya sea cliente o administrador), y un total de 3 microservicios que componen el backend de la aplicación, mantenidos en la nube desde AWS (en una instancia de EC2), con bases de datos únicas en PostgreSQL.

# Uso de recursos nativos

Los recursos nativos utilizados para este proyecto son:

- Cámara: Para tomar una foto del deposito al banco del 50% del evento a reservar.
- Vibración: Para indicar la exitosa realización de la reserva de evento.
- Galería: Para subir una captura de pantalla de la transferencia del 50%.
- Notificaciones: Para avisar sobre estado de la reserva al cliente.

## Información adicional - importante

- Este proyecto fue inicialmente realizado externamente entre los integrantes de este repositorio, por lo que los primeros commits sirven para reafirmar el desarrollo cooperativo de este proyecto. Este texto está presente para evitar futuras dudas en relación a este aspecto.
