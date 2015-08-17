package ca.claytonrogers.Client.GUIScene;

/**
 * This generic class represent a change from one scene to another. It also optionally contains any
 * information the first scene wanted to give the next scene.
 * <p>
 * Created by clayton on 2015-08-16.
 */
public class SceneChange<T> {

    public class NullPayloadType {}

    private final Scene.SceneType nextScene;
    private final T payload;

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
