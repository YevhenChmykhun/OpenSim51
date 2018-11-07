package gui.editorstyles;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.shape.Polygon;
import org.reactfx.value.Val;

import java.util.function.IntFunction;

public class DebuggerArrowFactory implements IntFunction<Node> {
    private final SimpleObjectProperty<Integer> selectedLine;

    public DebuggerArrowFactory(SimpleObjectProperty<Integer> selectedLine) {
        this.selectedLine = selectedLine;
    }

    @Override
    public Node apply(int lineNumber) {
        Polygon triangle = new Polygon(0.0, 0.0, 10.0, 5.0, 0.0, 10.0);
        triangle.getStyleClass().add("debuggerArrow");

        ObservableValue<Boolean> visible = Val.map(selectedLine, sl -> sl == lineNumber);

        triangle.visibleProperty().bind(
                Val.flatMap(triangle.sceneProperty(), scene -> scene != null ? visible : Val.constant(false)));

        return triangle;
    }
}