# 🔄 Mappers con MapStruct

## 📋 Descripción

Este proyecto utiliza [MapStruct](https://mapstruct.org/) como biblioteca para la conversión entre objetos de dominio (entidades) y DTOs (Data Transfer Objects). MapStruct es un generador de código que simplifica la implementación de mapeos entre tipos Java beans, reduciendo significativamente el código repetitivo necesario para estas conversiones.

## 🌟 Características principales

- **Generación automática de código**: MapStruct genera automáticamente el código de implementación para las interfaces de mapeo definidas.
- **Mapeo basado en convenciones**: Mapea automáticamente propiedades con el mismo nombre.
- **Mapeo explícito**: Permite definir mapeos específicos para propiedades con nombres diferentes.
- **Mapeo de tipos complejos**: Soporta mapeo de colecciones, mapas y otros tipos complejos.
- **Integración con Spring**: Utiliza el modelo de componentes de Spring para la inyección de dependencias.

## 🛠️ Configuración

MapStruct está configurado en el proyecto con la versión 1.5.5.Final. La dependencia está definida en el archivo `build.gradle.kts`:

```kotlin
// Definición de la versión
const val MAPSTRUCT = "1.5.5.Final"

// Dependencia de implementación
implementation("org.mapstruct:mapstruct:${Versions.MAPSTRUCT}")

// Procesador de anotaciones
annotationProcessor("org.mapstruct:mapstruct-processor:${Versions.MAPSTRUCT}")
testAnnotationProcessor("org.mapstruct:mapstruct-processor:${Versions.MAPSTRUCT}")
```

## 📝 Uso en el proyecto

En este proyecto, MapStruct se utiliza para mapear entre entidades JPA y DTOs de respuesta. Los principales mappers son:

### UserMapper

Convierte entre la entidad `User` y los DTOs `UserRequestDTO` y `UserResponseDTO`:

```java
@Mapper(componentModel = "spring", uses = {PhoneMapper.class})
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "createdAt", target = "created")
    @Mapping(source = "updatedAt", target = "modified")
    @Mapping(source = "lastLogin", target = "last_login")
    @Mapping(source = "userToken", target = "token")
    @Mapping(target = "isactive", expression = "java(user.isActive())")
    UserResponseDTO userToUserResponseDTO(User user);

    @Mapping(source = "name", target = "fullName")
    @Mapping(source = "email", target = "userEmail")
    @Mapping(source = "password", target = "userPassword")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "userToken", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "phones", ignore = true)
    User userRequestDTOToUser(UserRequestDTO userRequestDTO);
}
```

### PhoneMapper

Convierte entre la entidad `Phone` y los DTOs `PhoneRequestDTO` y `PhoneResponseDTO`:

```java
@Mapper(componentModel = "spring")
public interface PhoneMapper {

    PhoneMapper INSTANCE = Mappers.getMapper(PhoneMapper.class);

    PhoneResponseDTO phoneToPhoneResponseDTO(Phone phone);

    java.util.List<PhoneResponseDTO> phonesToPhoneResponseDTOs(java.util.List<Phone> phones);

    @Mapping(source = "number", target = "phoneNumber")
    @Mapping(source = "citycode", target = "cityCode")
    @Mapping(source = "contrycode", target = "countryCode")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Phone phoneRequestDTOToPhone(PhoneRequestDTO phoneRequestDTO);

    java.util.List<Phone> phoneRequestDTOsToPhones(java.util.List<PhoneRequestDTO> phoneRequestDTOs);
}
```

## 🔍 Anotaciones principales

- **@Mapper**: Define una interfaz como un mapper de MapStruct. El atributo `componentModel = "spring"` indica que la implementación generada será un bean de Spring.
- **@Mapping**: Especifica cómo mapear propiedades individuales cuando los nombres son diferentes o se requiere una conversión especial.
- **uses**: Permite utilizar otros mappers para mapear tipos complejos o anidados.

## 💡 Ventajas de usar MapStruct

1. **Rendimiento**: MapStruct genera código Java puro, lo que resulta en un mejor rendimiento en comparación con soluciones basadas en reflexión.
2. **Seguridad de tipos**: Los errores de mapeo se detectan en tiempo de compilación.
3. **Facilidad de mantenimiento**: Reduce significativamente la cantidad de código repetitivo.
4. **Integración con frameworks**: Se integra bien con Spring y otros frameworks populares.

## 📚 Recursos adicionales

- [Documentación oficial de MapStruct](https://mapstruct.org/documentation/stable/reference/html/)
- [Guía de inicio rápido](https://mapstruct.org/documentation/stable/reference/html/#_getting_started)
- [Ejemplos de MapStruct](https://github.com/mapstruct/mapstruct-examples)
