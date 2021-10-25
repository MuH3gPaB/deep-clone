package my.projects.deepclone.exception;

/**
 * Exception represents error while DeepCopy process.
 */
public class DeepCopyException extends RuntimeException
{
   public DeepCopyException()
   {
   }

   public DeepCopyException(String message)
   {
      super(message);
   }

   public DeepCopyException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public DeepCopyException(Throwable cause)
   {
      super(cause);
   }

   public DeepCopyException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace)
   {
      super(message, cause, enableSuppression, writableStackTrace);
   }
}
