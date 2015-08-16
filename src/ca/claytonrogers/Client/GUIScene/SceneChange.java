package ca.claytonrogers.Client.GUIScene;

/**
 * Created by clayton on 2015-08-16.
 */
public class SceneChange<T> {

    public class NullPayloadType {}
    public NullPayloadType nullPayload;

    private Scene.SceneType nextScene;
    private T payload;

    public SceneChange (Scene.SceneType nextScene, T payload) {
        this.nextScene = nextScene;
        this.payload = payload;
    }

    public Scene.SceneType getNextScene() {
        return nextScene;
    }

    public T getPayload() {
        return payload;
    }
}
