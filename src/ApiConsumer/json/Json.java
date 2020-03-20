package ApiConsumer.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Json {

    private static final Set<Class> WRAPPER_TYPES = new HashSet<>(Arrays.asList(
            Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            Void.class)
    );

    public static String from(Object obj) throws JsonFormatingException {

        String json = "";
        boolean isArray = obj.getClass().isArray();

        if (isArray) {
            for (int i = 0; i < Array.getLength(obj); i++) {
                String format;

                if (Array.get(obj, i) == null)
                    continue;
                else if (isPrimitive(Array.get(obj, i)))
                    format = String.format("{ \"%d\" : %s }", i, Array.get(obj, i));
                else if (Array.get(obj, i) instanceof CharSequence)
                    format = String.format("{ \"%d\" : \"%s\" }", i, Array.get(obj, i));
                else
                    format = from(Array.get(obj, i));

                json += !json.isEmpty() ? "," + format : format;
            }
        } else
            try {
                for (Field declaredField : obj.getClass().getDeclaredFields()) {
                    String s = jsonField(obj, declaredField);

                    json += json.length() == 0 ? s : " , " + s;
                }
            } catch (Exception e) {
                throw new JsonFormatingException(e);
            }
        if (isArray)
            json = "[" + json + "]";
        else
            json = "{" + json + "}";

        return json.replaceAll("\\s+", " ");
    }

    private static String jsonField(Object object, Field field) throws JsonFormatingException {
        String str;

        if (!field.isAccessible())
            field.setAccessible(true);

        Object obj;
        try {
            obj = field.get(object);
        } catch (IllegalAccessException e) {
            throw new JsonFormatingException(e);
        }

        if (obj == null)
            str = String.format(" \"%s\" : %s ", field.getName(), obj);
        else if (isPrimitive(obj))
            str = String.format(" \"%s\" : %s ", field.getName(), obj);
        else if (obj instanceof CharSequence)
            str = String.format(" \"%s\" : \"%s\" ", field.getName(), obj);
        else
            str = String.format(" \"%s\" : %s ", field.getName(), from(obj));

        return str;

    }

    private static boolean isPrimitive(Object clazz) {
        return WRAPPER_TYPES.contains(clazz.getClass());
    }


}
