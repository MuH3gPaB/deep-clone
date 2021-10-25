package my.projects.deepclone.strategies.impl;

import my.projects.deepclone.DeepCopier;
import my.projects.deepclone.DeepCopyContext;
import my.projects.deepclone.strategies.TypeCopyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ArrayTypeCopyStrategy implements TypeCopyStrategy
{
   private static final Logger LOG = LoggerFactory.getLogger(ArrayTypeCopyStrategy.class);

   @Override
   public boolean isApplicable(Class<?> clazz)
   {
      return clazz.isArray();
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> T copy(T source, DeepCopyContext context)
   {
      Object[] sourceArray = (Object[]) source;
      Object[] targetArray = Arrays.copyOf(sourceArray, sourceArray.length);

      context.getCache().put(source, targetArray);

      for (int i = 0; i < sourceArray.length; i++)
      {
         LOG.debug("Recursive call of deepcopy for array element '{}'.", sourceArray[i]);
         targetArray[i] = DeepCopier.deepCopyWithContext(sourceArray[i], context);
      }
      return (T) targetArray;
   }
}
