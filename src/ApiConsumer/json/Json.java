package ApiConsumer.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Json {
    private static final Set<Class> WRAPPER_TYPES = new HashSet(Arrays.asList(Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class));

    public Json() {
    }

    public static String from(Object obj) throws JsonFormatingException {
        String json = "";
        boolean isArray = obj.getClass().isArray();
        boolean isList = obj instanceof List;
        if (isArray)
            for (int i = 0; i < Array.getLength(obj); ++i) {
                Object inst = Array.get(obj, i);
                if (inst != null) {
                    String format;
                    if (isPrimitive(inst))
                        format = String.format("{ \"%d\" : %s }", i, inst);
                    else if (inst instanceof CharSequence)
                        format = String.format("{ \"%d\" : \"%s\" }", i, inst);
                    else
                        format = from(inst);

                    json = json + (!json.isEmpty() ? "," + format : format);
                }
            }
        else if (isList) {
            List list = (List) obj;
            for (int i = 0; i < list.size(); ++i) {
                Object o = list.get(i);
                if (o != null) {
                    String format;
                    if (isPrimitive(o))
                        format = String.format("{ \"%d\" : %s }", i, o);
                    else if (o instanceof CharSequence)
                        format = String.format("{ \"%d\" : \"%s\" }", i, o);
                    else
                        format = from(o);

                    json = json + (!json.isEmpty() ? "," + format : format);
                }
            }
        } else {
            try {
                Field[] fields = obj.getClass().getDeclaredFields();

                for (int i = 0; i < fields.length; ++i) {
                    Field declaredField = fields[i];
                    if (declaredField.isSynthetic())
                        continue;
                    String fieldString = jsonField(obj, declaredField);
                    json = json + (json.length() == 0 ? fieldString : " , " + fieldString);
                }
            } catch (Exception e) {
                throw new JsonFormatingException(e);
            }
        }

        if (isArray || isList) {
            json = "[" + json + "]";
        } else {
            json = "{" + json + "}";
        }

        return json.replaceAll("\\s+", " ");
    }

    private static String jsonField(Object object, Field field) throws Exception {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        Object obj = field.get(object);


        if (obj == null)
            return String.format(" \"%s\" : %s ", field.getName(), obj);
        else if (isPrimitive(obj))
            return String.format(" \"%s\" : %s ", field.getName(), obj);
        else if (obj instanceof CharSequence)
            return String.format(" \"%s\" : \"%s\" ", field.getName(), obj);
        else
            return String.format(" \"%s\" : %s ", field.getName(), from(obj));

    }

    private static boolean isPrimitive(Object clazz) {
        return WRAPPER_TYPES.contains(clazz.getClass());
    }
}
