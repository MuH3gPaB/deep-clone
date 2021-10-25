package my.projects.deepclone;

import my.projects.deepclone.exception.DeepCopyException;
import my.projects.deepclone.strategies.TypeCopyStrategy;
import my.projects.deepclone.strategies.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DeepCopier
{
   private static final Logger LOG = LoggerFactory.getLogger(DeepCopier.class);

   private static final List<TypeCopyStrategy> copyStrategies = Arrays.asList(
            new ImmutableTypeCopyStrategy(),
            new ArrayTypeCopyStrategy(),
            new CollectionTypeCopyStrategy(),
            new MapTypeCopyStrategy(),
            new ObjectTypeCopyStrategy()
   );

   public static <T> T deepCopy(T source)
   {
      DeepCopyContext context = new DeepCopyContext();
      return deepCopyWithContext(source, context);
   }

   public static <T> T deepCopyWithContext(T source, DeepCopyContext context)
   {
      if (source == null)
      {
         return null;
      }

      Map<T, T> cache = context.getCache();

      if (cache.containsKey(source))
      {
         LOG.debug("Copied value of '{}' is taken from cache.", source);
         return cache.get(source);
      }

      Class<?> clazz = source.getClass();
      for (TypeCopyStrategy strategy : copyStrategies)
      {
         if (strategy.isApplicable(clazz))
         {
            LOG.debug("Strategy '{}' used to copy object of type '{}'",
                     strategy.getClass().getSimpleName(), clazz);
            return strategy.copy(source, context);
         }
      }

      throw new DeepCopyException("No strategy found to copy " + source + " object");
   }

}
