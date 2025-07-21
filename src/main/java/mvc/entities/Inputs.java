package mvc.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Clase DTO genérica para encapsular respuestas con campos dinámicos y opcionales.
 * Permite almacenar cualquier tipo de dato bajo una clave String y recuperarlo
 * como un Optional para un manejo seguro de la nulidad.
 */
public class Inputs {

    private final Map<String, Object> fields;

    /**
     * Constructor privado para forzar el uso del Builder.
     */
    private Inputs(Builder builder) {
        this.fields = new HashMap<>(builder.fields);
    }

    /**
     * Obtiene el valor de un campo específico como un Optional.
     * Esto permite manejar de forma segura si el campo no existe.
     *
     * @param key La clave (nombre) del campo a recuperar.
     * @return Un Optional que contiene el valor del campo si existe, o un Optional.empty() si no.
     */
    public Optional<Object> getField(String key) {
        return Optional.ofNullable(fields.get(key));
    }

    /**
     * Obtiene el valor de un campo específico y lo intenta castear al tipo esperado.
     * Útil cuando se sabe qué tipo se espera, pero aún se maneja la opcionalidad y la seguridad del tipo.
     * Esta versión realiza un casteo directo. Si el tipo no es compatible o la conversión falla
     * (excepto para valores nulos), se lanzará una excepción.
     *
     * @param key   La clave (nombre) del campo a recuperar.
     * @param clazz El tipo de clase al que se espera castear el valor.
     * @param <T>   El tipo genérico esperado.
     * @return Un Optional que contiene el valor casteado si existe y es del tipo correcto.
     * Devuelve Optional.empty() solo si el valor almacenado es null.
     * @throws ClassCastException Si el valor almacenado no es compatible con el tipo esperado
     * y no es null, ni una cadena parseable a LocalDateTime.
     * @throws DateTimeParseException Si se intenta parsear una cadena a LocalDateTime y el formato es incorrecto.
     */
    public <T> Optional<T> getFieldAs(String key, Class<T> clazz) {
        Object value = fields.get(key);

        // Si el valor es nulo, devuelve Optional.empty() como única excepción.
        if (value == null) {
            System.err.println("DEBUG: getFieldAs('" + key + "', " + clazz.getName() + ") - Valor almacenado es NULL. Retornando Optional.empty().");
            return Optional.empty();
        }

        // Obtener el tipo real del valor almacenado para depuración
        Class<?> actualValueClass = value.getClass();
        System.err.println("DEBUG: getFieldAs('" + key + "', " + clazz.getName() + ") - Tipo de valor almacenado: " + actualValueClass.getName());

        // Manejo especial para LocalDateTime (si el valor es una cadena)
        // Se intenta parsear y si falla, se lanza DateTimeParseException.
        if (clazz == LocalDateTime.class && value instanceof String) {
            try {
                System.err.println("DEBUG: getFieldAs('" + key + "', " + clazz.getName() + ") - Intentando parsear String a LocalDateTime: '" + value + "'");
                return Optional.of(clazz.cast(LocalDateTime.parse((String) value)));
            } catch (DateTimeParseException e) {
                System.err.println("ERROR: getFieldAs('" + key + "', " + clazz.getName() + ") - DateTimeParseException al parsear String a LocalDateTime: " + e.getMessage());
                // Relanzar la excepción para cumplir con el requisito de que falle.
                throw e;
            } catch (Exception e) {
                System.err.println("ERROR: getFieldAs('" + key + "', " + clazz.getName() + ") - Error inesperado al parsear String a LocalDateTime: " + e.getMessage());
                // Relanzar como una excepción de tiempo de ejecución si no es DateTimeParseException.
                throw new RuntimeException("Error inesperado al convertir String a LocalDateTime para la clave '" + key + "'", e);
            }
        }

        // Intento de casteo directo. Si el valor no es una instancia del tipo esperado,
        // clazz.cast() lanzará una ClassCastException.
        try {
            System.err.println("DEBUG: getFieldAs('" + key + "', " + clazz.getName() + ") - Intentando casteo directo.");
            return Optional.of(clazz.cast(value));
        } catch (ClassCastException e) {
            System.err.println("ERROR: getFieldAs('" + key + "', " + clazz.getName() + ") - ClassCastException: No se puede castear de " + actualValueClass.getName() + " a " + clazz.getName() + ". Mensaje: " + e.getMessage());
            // Relanzar la excepción para cumplir con el requisito de que falle.
            throw e;
        }
    }

    /**
     * Devuelve todas las claves de los campos almacenados.
     *
     * @return Un Set de Strings con todas las claves de los campos.
     */
    public Set<String> getFieldNames() {
        return fields.keySet();
    }

    /**
     * Devuelve una vista inmutable de todos los campos.
     *
     * @return Un Map que contiene todos los campos y sus valores.
     */
    public Map<String, Object> getAllFields() {
        return new HashMap<>(fields); // Devuelve una copia para evitar modificaciones externas
    }

    @Override
    public String toString() {
        return "GenericResponseDTO{" +
                "fields=" + fields +
                '}';
    }

    /**
     * Patrón Builder para construir instancias de GenericResponseDTO de forma fluida.
     */
    public static class Builder {
        private final Map<String, Object> fields = new HashMap<>();

        /**
         * Añade un campo a la respuesta.
         *
         * @param key   La clave (nombre) del campo.
         * @param value El valor del campo. Puede ser null, y se almacenará como tal.
         * @return El propio Builder para encadenar llamadas.
         */
        public Builder put(String key, Object value) {
            if (key == null || key.trim().isEmpty()) {
                throw new IllegalArgumentException("Field key cannot be null or empty.");
            }
            this.fields.put(key, value);
            return this;
        }

        /**
         * Construye y devuelve una nueva instancia de GenericResponseDTO.
         *
         * @return Una nueva instancia de GenericResponseDTO.
         */
        public Inputs build() {
            return new Inputs(this);
        }
    }
}
