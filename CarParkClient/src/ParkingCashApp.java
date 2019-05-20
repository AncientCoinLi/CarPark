import controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class ParkingCashApp extends Application
{
    @Override
    public void start(Stage primaryStage)
    {

        primaryStage.setTitle("Parking Cash");
        Group root = new Group();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        Canvas canvas = new Canvas(ParkingCashRender.WIDTH, ParkingCashRender.HEIGHT);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        //create a controller to handle relevant resources
        Controller controller = new Controller();
        
        ParkingCashRender render = new ParkingCashRender(scene, gc, controller);
        render.run();


        //set close request action
        primaryStage.setOnCloseRequest(e ->{
        	e.consume();
        	controller.close();
        	primaryStage.close();
        	System.exit(0);
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
