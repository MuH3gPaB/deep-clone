package my.projects.deepclone.strategies.impl;

import my.projects.deepclone.DeepCopier;
import my.projects.deepclone.DeepCopyContext;
import my.projects.deepclone.exception.DeepCopyException;
import my.projects.deepclone.strategies.TypeCopyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.STATIC;
import static java.lang.reflect.Modifier.TRANSIENT;
import static java.util.Arrays.stream;

public class ObjectTypeCopyStrategy implements TypeCopyStrategy
{
   private static final Logger LOG = LoggerFactory.getLogger(ObjectTypeCopyStrategy.class);

   @Override
   public boolean isApplicable(Class<?> clazz)
   {
      return true;
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> T copy(T source, DeepCopyContext context)
   {
      try
      {
         Class<?> sourceClass = source.getClass();

         Constructor<?> constructor = ReflectionFactory.getReflectionFactory()
                  .newConstructorForSerialization(sourceClass,
                           Object.class.getDeclaredConstructor());

         Object target = constructor.newInstance();
         context.getCache().put(source, target);

         List<Field> filteredFields = stream(sourceClass.getDeclaredFields())
                  .filter(field -> (field.getModifiers() & (STATIC | TRANSIENT)) == 0)
                  .collect(Collectors.toList());

         for (Field field : filteredFields)
         {
            field.setAccessible(true);
            Object sourceValue = field.get(source);
            LOG.debug("Recursive call of deepcopy for object attribute '{}' having value '{}'.",
                     field.getName(), sourceValue);
            Object targetValue = DeepCopier.deepCopyWithContext(sourceValue, context);
            field.set(target, targetValue);
         }

         return (T) target;
      }
      catch (NoSuchMethodException | InstantiationException |
               IllegalAccessException | InvocationTargetException e)
      {
         throw new DeepCopyException(e);
      }
   }
}
