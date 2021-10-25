package my.projects.deepclone.strategies.impl;

import my.projects.deepclone.DeepCopier;
import my.projects.deepclone.DeepCopyContext;
import my.projects.deepclone.exception.DeepCopyException;
import my.projects.deepclone.strategies.TypeCopyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionTypeCopyStrategy implements TypeCopyStrategy
{
   private static final Logger LOG = LoggerFactory.getLogger(CollectionTypeCopyStrategy.class);

   @Override
   public boolean isApplicable(Class<?> clazz)
   {
      return Collection.class.isAssignableFrom(clazz);
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> T copy(T source, DeepCopyContext context)
   {
      Class<Collection<Object>> sourceClass = (Class<Collection<Object>>) source.getClass();
      Collection<Object> sourceCollection = (Collection<Object>) source;
      Collection<Object> targetCollection = new ArrayList<>();

      for (Object obj : sourceCollection)
      {
         LOG.debug("Recursive call of deepcopy for collection element '{}'", obj);
         targetCollection.add(DeepCopier.deepCopyWithContext(obj, context));
      }

      Constructor<Collection<Object>>[] declaredConstructors =
               (Constructor<Collection<Object>>[]) sourceClass.getDeclaredConstructors();

      for (Constructor<Collection<Object>> constructor : declaredConstructors)
      {
         constructor.setAccessible(true);
         List<Class<?>> parameterTypes = List.of(constructor.getParameterTypes());
         if (parameterTypes.size() == 1)
         {
            if (parameterTypes.contains(Collection.class))
            {
               return createInstance(constructor, targetCollection);
            }
            else if (parameterTypes.contains(Object[].class))
            {
               // Such construction is supposed to handle collections created by Arrays.asList method
               return createInstance(constructor, targetCollection.toArray());
            }
         }
         else if (parameterTypes.size() == 0)
         {
            Collection<Object> result = createInstance(constructor, null);
            result.addAll(targetCollection);
            return (T) result;
         }
      }

      return (T) targetCollection;
   }

   @SuppressWarnings("unchecked")
   private <T> T createInstance(Constructor<Collection<Object>> constructor, Object argument)
   {
      try
      {
         if (argument != null)
         {
            return (T) constructor.newInstance(argument);
         }
         else
         {
            return (T) constructor.newInstance();
         }
      }
      catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
      {
         throw new DeepCopyException(e);
      }
   }
}
