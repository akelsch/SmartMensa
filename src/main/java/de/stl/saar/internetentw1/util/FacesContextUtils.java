package de.stl.saar.internetentw1.util;

import lombok.experimental.UtilityClass;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import java.util.*;

/**
 * Werkzeugklasse mit Methoden zum Arbeiten mit {@link FacesContext}.
 */
@UtilityClass
public final class FacesContextUtils {

    /**
     * Fügt dem aktuellen JSF-Kontext eine globale Nachricht mit dem Level
     * "Error" hinzu.
     *
     * @param message Der Inhalt der Nachricht
     */
    public static void addGlobalErrorMessage(String message) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", message));
    }

    /**
     * Fügt dem aktuellen JSF-Kontext eine globale Nachricht mit dem Level
     * "Info" hinzu.
     *
     * @param message Der Inhalt der Nachricht
     */
    public static void addGlobalInfoMessage(String message) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", message));
    }

    /**
     * Fügt dem Flash ein Objekt an der Stelle {@code key} hinzu.
     * <p>
     * Flash ist eine Map Implementierung und erlaubt es Objekte zwischen zwei
     * {@code ViewScoped} Beans nach einem Redirect auszutauschen.
     *
     * @param key   Der Schlüssel des zu speichernden Objekts
     * @param value Der Wert des zu speichernden Objekts
     * @param <T>   Der Typ des zu speichernden Objekts
     */
    public static <T> void putFlashObject(String key, T value) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Flash flash = facesContext.getExternalContext().getFlash();
        flash.put(key, value);
    }

    /**
     * Liefert ein Objekt aus dem Flash an der Stelle {@code key}.
     *
     * @param key   Der Schlüssel des gesuchten Objekts
     * @param clazz Der Typ des gesuchten Objekts als {@link Class} zum casten
     * @param <T>   Der Typ des gesuchten Objekts
     * @return Das gesuchte Objekt in einem {@link Optional} verpackt oder ein
     * leeres {@link Optional}, falls das Objekt nicht existiert oder vom
     * falschen Typ ist
     */
    public static <T> Optional<T> getFlashObject(String key, Class<T> clazz) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Flash flash = facesContext.getExternalContext().getFlash();

        // Notwendig, da es sonst zu einer NullPointerException kommt
        if (flash.containsKey(key)) {
            return Optional.of(flash.get(key))
                    .filter(clazz::isInstance)
                    .map(clazz::cast);
        }

        return Optional.empty();
    }

    public static <T> List<T> getFlashObjects(String key, Class<T> clazz) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Flash flash = facesContext.getExternalContext().getFlash();
        List<T> list = new ArrayList<>();

        if (flash.containsKey(key)) {
            Object o = flash.get(key);
            if (o instanceof Collection<?>) {
                Collection<?> c = (Collection<?>) o;
                c.stream()
                        .filter(clazz::isInstance)
                        .map(clazz::cast)
                        .forEach(list::add);
            }
        }

        return list;
    }

    /**
     * Liefert {@code default-locale} aus {@code faces-config.xml}.
     *
     * @return Die Standardsprache als {@link Locale}
     */
    public static Locale getDefaultLocale() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getDefaultLocale();
    }

    /**
     * Setzt die JSF View Sprache auf die angegebene {@link Locale}.
     *
     * @param locale Die neue Sprache
     */
    public static void setLocale(Locale locale) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getViewRoot().setLocale(locale);
    }

    /**
     * Liefert den Wert für das Property mit dem Schlüssel {@code key} aus dem
     * Resource Bundle {@code msg}.
     *
     * @param key Der Schlüssel für den gesuchten Wert
     * @return Der Wert des gesuchten Properties
     */
    public static String getMessage(String key) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ResourceBundle resourceBundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");

        return resourceBundle.getString(key);
    }
}
