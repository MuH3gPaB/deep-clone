package my.projects.deepclone.strategies;

import my.projects.deepclone.DeepCopyContext;

/**
 * Interface represents strategy for processing objects of different types
 * by {@link my.projects.deepclone.DeepCopier}
 */
public interface TypeCopyStrategy
{
   /**
    * Check if current strategy is able to copy objects of given type
    *
    * @param clazz - type of copied object to be checked
    * @return - true if type could be copied, otherwise - false
    */
   boolean isApplicable(Class<?> clazz);

   /**
    * Perform copy of given object considering copy context
    * <p>
    * Given context may contain supporting data for copying process (eg. cached values)
    * <p>
    * If given source is 'null' returns 'null'.
    *
    * @param source  - source to be copied
    * @param context - copy process context
    * @param <T>     - type of copying object
    * @return copy of given object
    */
   <T> T copy(T source, DeepCopyContext context);
}
