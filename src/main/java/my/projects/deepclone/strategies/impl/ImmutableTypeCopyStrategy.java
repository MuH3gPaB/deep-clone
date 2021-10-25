package my.projects.deepclone.strategies.impl;

import my.projects.deepclone.DeepCopyContext;
import my.projects.deepclone.strategies.TypeCopyStrategy;

public class ImmutableTypeCopyStrategy implements TypeCopyStrategy
{
   @Override
   public boolean isApplicable(Class<?> clazz)
   {
      return String.class.equals(clazz)
               || Number.class.isAssignableFrom(clazz)
               || clazz.isEnum();
   }

   @Override
   public <T> T copy(T source, DeepCopyContext context)
   {
      return source;
   }
}
