package gui.controller;

import gui.editorstyles.BreakpointFactory;
import gui.editorstyles.DebuggerArrowFactory;
import gui.editorstyles.SelectedLineArrowFactory;
import gui.editorstyles.TokensHighlighting;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.reactfx.Subscription;
import simulator.Simulator;
import simulator.memory.Memory;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

public class MainWindow {

    public static Simulator simulator = Simulator.getInstance();

    @FXML
    public Button translateButton;

    @FXML
    public Button stepOverButton;

    @FXML
    public Button burnHexButton;

    @FXML
    public MenuItem interruptMenuItem;

    @FXML
    public MenuItem timer0MenuItem;

    @FXML
    public MenuItem timer1MenuItem;

    @FXML
    public TextField messageTextField;

    private Map<String, Stage> shownPortWindows;

    private Map<String, Stage> shownTimerWindows;

    private Map<String, Updatable> loadedControllers;

    @FXML
    private RegistersController registersController;

    @FXML
    private MemoryController memoryController;

    private InterruptController interruptController;

    @FXML
    public ScrollPane codeScrollPane;

    @FXML
    public MenuItem port0MenuItem;
    @FXML
    public MenuItem port1MenuItem;
    @FXML
    public MenuItem port2MenuItem;
    @FXML
    public MenuItem port3MenuItem;

    private Stage primaryStage;

    private Stage interruptWindow;

    public MainWindow() {
        shownPortWindows = new HashMap<>();
        shownTimerWindows = new HashMap<>();
        loadedControllers = new HashMap<>();
    }

    @FXML
    public void initialize() {
        registersController.setMainWindow(this);
        memoryController.setMainWindow(this);

        interruptMenuItem.setOnAction(event -> showInterruptWindow());

        port0MenuItem.setOnAction(event -> showPortWindow("0", simulator.getInternalData().P0));
        port1MenuItem.setOnAction(event -> showPortWindow("1", simulator.getInternalData().P1));
        port2MenuItem.setOnAction(event -> showPortWindow("2", simulator.getInternalData().P2));
        port3MenuItem.setOnAction(event -> showPortWindow("3", simulator.getInternalData().P3));

        timer0MenuItem.setOnAction(event -> showTimerWindow("0"));
        timer1MenuItem.setOnAction(event -> showTimerWindow("1"));


        CodeArea editor = new CodeArea();
        editor.setId("editor");
        editor.getStylesheets().add(getClass().getResource("editor.css").toExternalForm());

        ObservableList<Integer> breakpointLineNumbers = FXCollections.observableArrayList();
        SimpleObjectProperty<Integer> debuggedLine = new SimpleObjectProperty<>(0);

        IntFunction<Node> breakpointFactory = new BreakpointFactory(breakpointLineNumbers);
        IntFunction<Node> selectedLineArrowFactory = new SelectedLineArrowFactory(editor.currentParagraphProperty());
        IntFunction<Node> debuggerArrowFactory = new DebuggerArrowFactory(debuggedLine);
        IntFunction<Node> lineNumberFactory = LineNumberFactory.get(editor);
        IntFunction<Node> graphicFactory = line -> {
            HBox hbox = new HBox(
                    breakpointFactory.apply(line),
                    selectedLineArrowFactory.apply(line),
                    debuggerArrowFactory.apply(line),
                    lineNumberFactory.apply(line));
            hbox.getStyleClass().add("gutter");
            hbox.setOnMouseClicked(event -> {
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                    int currentParagraph = editor.getCurrentParagraph();
                    if (breakpointLineNumbers.contains(currentParagraph)) {
                        breakpointLineNumbers.remove((Integer) currentParagraph);
                    } else {
                        breakpointLineNumbers.add(currentParagraph);
                    }
                }
            });

            return hbox;
        };
        editor.setParagraphGraphicFactory(graphicFactory);

        // TODO: recompute syntax highlighting for a line instead of a file

        // recompute the syntax highlighting 20 ms after user stops editing area
        Subscription cleanupWhenNoLongerNeedIt = editor

                // plain changes = ignore style changes that are emitted when syntax highlighting is reapplied
                // multi plain changes = save computation by not rerunning the code multiple times
                //   when making multiple changes (e.g. renaming a method at multiple parts in file)
                .multiPlainChanges()

                // do not emit an event until 20 ms have passed since the last emission of previous stream
                .successionEnds(Duration.ofMillis(20))

                // run the following code block when previous stream emits an event
                .subscribe(ignore -> editor.setStyleSpans(0, TokensHighlighting.computeHighlighting(editor.getText())));

        // when no longer need syntax highlighting and wish to clean up memory leaks
        // run: `cleanupWhenNoLongerNeedIt.unsubscribe();`

        codeScrollPane.setContent(new VirtualizedScrollPane<>(editor));


        translateButton.setOnAction(event -> {
            messageTextField.clear();

            try {
                simulator.translate(editor.getText(), new BaseErrorListener() {
                    @Override
                    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                        messageTextField.setText("line " + line + ":" + charPositionInLine + " " + msg);
                        throw new ParseCancellationException();
                    }
                });
            } catch (ParseCancellationException e) {
                e.printStackTrace();
            }

            updateUserInterface();
        });

        burnHexButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open HEX File");
            File file = fileChooser.showOpenDialog(primaryStage);

            try {
                simulator.burnIntel8HexFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            }

            memoryController.update();
        });

        stepOverButton.setOnAction(event -> {
            if (debuggedLine.getValue() < editor.getLength()) {
                debuggedLine.set(debuggedLine.getValue() + 1);
            }
            simulator.step();
            updateUserInterface();
        });
    }

    private void showPortWindow(String portNumber, Memory.BitAddressableCell port) {
        if (shownPortWindows.containsKey(portNumber)) {
            shownPortWindows.get(portNumber).requestFocus();
            return;
        }

        try {
            Stage stage = new Stage();
            stage.setTitle("Parallel Port " + portNumber);
            stage.initStyle(StageStyle.UTILITY);
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.setOnCloseRequest(event -> shownPortWindows.remove(portNumber));

            FXMLLoader loader = new FXMLLoader(getClass().getResource("port.fxml"));
            Parent root = loader.load();
            PortController controller = loader.getController();
            controller.setPortNumber(portNumber);
            controller.setPort(port);

            Scene scene = new Scene(root);
            scene.getStylesheets().add("styles.css");
            stage.setScene(scene);
            stage.show();

            shownPortWindows.put(portNumber, stage);
            loadedControllers.put("port" + portNumber, controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showTimerWindow(String timerNumber/*, Memory.BitAddressableCell port*/) {
        if (shownTimerWindows.containsKey(timerNumber)) {
            shownTimerWindows.get(timerNumber).requestFocus();
            return;
        }

        try {
            Stage stage = new Stage();
            stage.setTitle("Timer/Counter " + timerNumber);
            stage.initStyle(StageStyle.UTILITY);
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.setOnCloseRequest(event -> shownTimerWindows.remove(timerNumber));

            FXMLLoader loader = new FXMLLoader(getClass().getResource("timer.fxml"));
            Parent root = loader.load();
            TimerController controller = loader.getController();
            controller.setTimerNumber(timerNumber);
            controller.setMainWindow(this);
            controller.initializeController();

            Scene scene = new Scene(root);
            scene.getStylesheets().add("styles.css");
            stage.setScene(scene);
            stage.show();

            shownTimerWindows.put(timerNumber, stage);
            loadedControllers.put("timer" + timerNumber, controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showInterruptWindow() {
        if (interruptWindow != null) {
            interruptWindow.requestFocus();
            return;
        }

        try {
            Stage stage = new Stage();
            stage.setTitle("Interrupt System");
            stage.initStyle(StageStyle.UTILITY);
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.setOnCloseRequest(event -> interruptWindow = null);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("interrupt.fxml"));
            Parent root = loader.load();
            interruptController = loader.getController();
            interruptController.setMainWindow(this);

            Scene scene = new Scene(root);
            scene.getStylesheets().add("styles.css");
            stage.setScene(scene);
            stage.show();

            interruptWindow = stage;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUserInterface() {
        if (interruptController != null) {
            interruptController.update();
        }
        registersController.update();
        memoryController.update();

        for (Updatable controller : loadedControllers.values()) {
            controller.update();
        }
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
