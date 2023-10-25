package baseball;

import baseball.controller.GameController;
import baseball.controller.NumberBaseballGameController;
import baseball.model.Game;
import baseball.model.NumberBaseballGame;
import baseball.view.GameView;
import baseball.view.NumberBaseballGameView;
import java.util.function.Supplier;

public class Application {
    private final GameController gameController;

    public Application(Class<? extends Game> gameClass, Class<? extends GameView> viewClass,
                       Class<? extends GameController> controllerClass, String messagePath) {
        Supplier<Game> gameSupplier = () -> {
            try {
                return gameClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("게임 인스턴스 생성 오류", e);
            }
        };

        try {
            GameView view = viewClass.getDeclaredConstructor(String.class).newInstance(messagePath);
            gameController = controllerClass.getDeclaredConstructor(Supplier.class, GameView.class)
                    .newInstance(gameSupplier, view);
        } catch (Exception e) {
            throw new RuntimeException("인스턴스 생성 오류", e);
        }
    }

    public static void main(String[] args) {
        Application.run(NumberBaseballGame.class, NumberBaseballGameView.class,
                NumberBaseballGameController.class, "src/main/resources/messages/messages.properties");
    }

    public static void run(Class<? extends Game> gameClass, Class<? extends GameView> viewClass,
                           Class<? extends GameController> controllerClass, String messagePath) {
        (new Application(gameClass, viewClass, controllerClass, messagePath)).run();
    }

    public void run() {
        gameController.play();
    }
}
