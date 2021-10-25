package my.projects.deepclone;

import java.util.HashMap;
import java.util.Map;

public class DeepCopyContext
{
   private final Map<Object, Object> cache = new HashMap<>();

   @SuppressWarnings("unchecked")
   public <T> Map<T, T> getCache()
   {
      return (Map<T, T>) cache;
   }
}
