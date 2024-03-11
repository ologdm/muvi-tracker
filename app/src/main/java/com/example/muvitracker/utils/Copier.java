package com.example.muvitracker.utils;

// funzione per copiare un Object

public class Copier {
        public static <T> T copy(T original) {
            try {
                // Usa la riflessione per ottenere una nuova istanza dell'oggetto
                // e copia i valori dei campi dall'oggetto originale al nuovo oggetto
                Class<?> clazz = original.getClass();
                T copy = (T) clazz.newInstance();
                for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(copy, field.get(original));
                }
                return copy;
            } catch (InstantiationException | IllegalAccessException e) {
                // Gestione dell'eccezione se la classe non può essere istanziata o se non si hanno i permessi per accedere ai campi
                e.printStackTrace();
                return null;
            }
        }
}
