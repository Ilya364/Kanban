package kanban.manager.exception;

public class SendResponseException extends RuntimeException{
    public SendResponseException(String message) {
        super(message);
    }
}
