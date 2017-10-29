public class Transition {
    public Transition(String currentName, String eventName, String destName) {
        this.destName = destName;
        this.currentName = currentName;
        this.eventName = eventName;
    }

    String destName;
    String currentName;
    String eventName;

    @Override
    public String toString() {
        return "Transition{" +
                "destName='" + destName + '\'' +
                ", currentName='" + currentName + '\'' +
                ", eventName='" + eventName + '\'' +
                '}';
    }
}
