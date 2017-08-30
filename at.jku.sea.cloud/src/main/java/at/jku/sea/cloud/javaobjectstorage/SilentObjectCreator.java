package at.jku.sea.cloud.javaobjectstorage;

import java.lang.reflect.Constructor;

import sun.reflect.ReflectionFactory;

class SilentObjectCreator {
  public static <T> T create(final Class<T> clazz) {
    return create(clazz, Object.class);
  }

  public static <T> T create(final Class<T> clazz, final Class<? super T> parent) {
    try {
      final ReflectionFactory rf = ReflectionFactory.getReflectionFactory();
      final Constructor<? super T> objDef = parent.getDeclaredConstructor();
      final Constructor<?> intConstr = rf.newConstructorForSerialization(clazz, objDef);
      return clazz.cast(intConstr.newInstance());
    } catch (final RuntimeException e) {
      throw e;
    } catch (final Exception e) {
      throw new IllegalStateException("Cannot create object", e); //$NON-NLS-1$
    }
  }
}
