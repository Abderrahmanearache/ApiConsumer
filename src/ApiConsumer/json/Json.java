package ApiConsumer.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
 
 

public class Json {
    private static final Set<Class> WRAPPER_TYPES = new HashSet(Arrays.asList(Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class));

    public Json() {
    }

    public static String from(Object obj) throws Exception {
        String json = "";
        boolean isArray = obj.getClass().isArray();
        if (isArray) { 
            for (int i = 0; i < Array.getLength(obj); ++i) {
                if (Array.get(obj, i) != null) {
                    String format;
                    if (isPrimitive(Array.get(obj, i))) {
                        format = String.format("{ \"%d\" : %s }", i, Array.get(obj, i));
                    } else if (Array.get(obj, i) instanceof CharSequence) {
                        format = String.format("{ \"%d\" : \"%s\" }", i, Array.get(obj, i));
                    } else {
                        format = from(Array.get(obj, i));
                    }
                    json = json + (!json.isEmpty() ? "," + format : format);
                }
            }
        } else {
            try {
                Field[] var9 = obj.getClass().getDeclaredFields();
                int var10 = var9.length;

                for (int var5 = 0; var5 < var10; ++var5) {
                    Field declaredField = var9[var5];
                    if (declaredField.isSynthetic())
                        continue;
                    String fieldString = jsonField(obj, declaredField);
                    json = json + (json.length() == 0 ? fieldString : " , " + fieldString);
                }
            } catch (Exception var8) {
                throw new Exception(var8);
            }
        }

        if (isArray) {
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


        Object obj;
        try {
            obj = field.get(object);
        } catch (IllegalAccessException var5) {
            throw new Exception(var5);
        }

        String str = null;
        if (obj == null) {
            str = String.format(" \"%s\" : %s ", field.getName(), obj);
        } else if (isPrimitive(obj)) {
            str = String.format(" \"%s\" : %s ", field.getName(), obj);
        } else if (obj instanceof CharSequence) { 
            str = String.format(" \"%s\" : \"%s\" ", field.getName(), obj);

        } else {
            str = String.format(" \"%s\" : %s ", field.getName(), from(obj));
        }

        return str;
    }

    private static boolean isPrimitive(Object clazz) {
        return WRAPPER_TYPES.contains(clazz.getClass());
    }
}
