package at.jku.sea.cloud.javaobjectstorage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

class ReflectionUtils {
  public static Collection<Field> getAllFields(final Class<?> c) {
    return getAllFieldsIntern(c).values();
  }

  private static Map<String, Field> getAllFieldsIntern(final Class<?> c) {
    final Map<String, Field> fields = new Hashtable<String, Field>();
    if (c.getSuperclass() != null) {
      fields.putAll(getAllFieldsIntern(c.getSuperclass()));
    }
    for (final Field field : c.getDeclaredFields()) {
      final int modifiers = field.getModifiers();
      if (!(Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers))) {
        fields.put(field.getName(), field);
      }
    }
    return fields;
  }
}
