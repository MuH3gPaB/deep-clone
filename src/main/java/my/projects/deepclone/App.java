package my.projects.deepclone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class App
{
   private static final Logger LOG = LoggerFactory.getLogger(App.class);

   public static void main(String[] args)
   {
      Man sourceMan = new Man("Mister First", 28, Arrays.asList("Knuth", "Schildt ", "Fowler"));
      Man copyMan = DeepCopier.deepCopy(sourceMan);

      LOG.info("   SOURCE");
      LOG.info("      {}", sourceMan);
      LOG.info("      name: {}", sourceMan.getName());
      LOG.info("      age: {}", sourceMan.getAge());
      LOG.info("      books: {}", sourceMan.getFavoriteBooks());
      LOG.info("");

      LOG.info("   COPY");
      LOG.info("      {}", copyMan);
      LOG.info("      name: {}", copyMan.getName());
      LOG.info("      age: {}", copyMan.getAge());
      LOG.info("      books: {}", copyMan.getFavoriteBooks());
   }
}
