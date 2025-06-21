package interfaces;


public interface Command<T> {

    void execute(T execute);

    default void execute(){
        execute(null);
    }
}
