package my.projects.deepclone.strategies.impl;

import my.projects.deepclone.DeepCopier;
import my.projects.deepclone.DeepCopyContext;
import my.projects.deepclone.exception.DeepCopyException;
import my.projects.deepclone.strategies.TypeCopyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class MapTypeCopyStrategy implements TypeCopyStrategy
{
   private static final Logger LOG = LoggerFactory.getLogger(MapTypeCopyStrategy.class);

   @Override
   public boolean isApplicable(Class<?> clazz)
   {
      return Map.class.isAssignableFrom(clazz);
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> T copy(T source, DeepCopyContext context)
   {
      try
      {
         Map<Object, Object> sourceMap = (Map<Object, Object>) source;
         Constructor<? extends Map> constructor = sourceMap.getClass().getConstructor();
         Map<Object, Object> targetMap = constructor.newInstance();

         for (Map.Entry<Object, Object> entry : sourceMap.entrySet())
         {
            LOG.debug("Recursive call of deepcopy for map key '{}'.", entry.getKey());
            Object key = DeepCopier.deepCopyWithContext(entry.getKey(), context);

            LOG.debug("Recursive call of deepcopy for map value '{}'.", entry.getValue());
            Object value = DeepCopier.deepCopyWithContext(entry.getValue(), context);

            targetMap.put(key, value);
         }

         return (T) targetMap;
      }
      catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e)
      {
         throw new DeepCopyException(e);
      }
   }
}
