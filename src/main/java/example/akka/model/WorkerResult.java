package example.akka.model;

public class WorkerResult {
    private final int id;

    private final boolean success;

    public WorkerResult(int id, boolean success) {
        this.id = id;
        this.success = success;
    }

    public int getId() {
        return id;
    }

    public boolean isSuccess() {
        return success;
    }

    public static WorkerResult of(int id, boolean success) {
        return new WorkerResult(id, success);
    }
}
