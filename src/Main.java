import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


//在这里显示界面和动画吧
public class Main extends Application {
    int size = 5;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //左侧的内容
        VBox vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER);

        vBox.getChildren().add(new Text("请输入初始状态"));
        MyPanel myPanel1 = new MyPanel(size);
        vBox.getChildren().add(myPanel1);
        vBox.getChildren().add(new Text("请输入目标状态"));
        MyPanel myPanel2 = new MyPanel(size);
        vBox.getChildren().add(myPanel2);
        Button button = new Button("开始");

        vBox.getChildren().add(button);
        vBox.setMaxWidth(250);

        HBox hbox = new HBox(5);   //the main layout
        hbox.getChildren().add(vBox);

        hbox.getChildren().add(new Line(350, 0, 350, 350));
        Scene scene = new Scene(hbox);            //create the scene

        primaryStage.setWidth(550);             //the stage size
        primaryStage.setHeight(370);
        primaryStage.setScene(scene);           //set the scene
        primaryStage.setTitle("数码问题");
        primaryStage.show();                    //show the stage

        VBox vBox1=new VBox();
        vBox1.setAlignment(Pos.CENTER);
        hbox.getChildren().add(vBox1);

        button.setOnMouseClicked(event -> {
            //do something
            //在这里需要拿到数据
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    MyPanelB myPanel3 = new MyPanelB(size,myPanel1.getArray());
                    hbox.getChildren().remove(vBox1);
                    vBox1.getChildren().clear();
                    vBox1.getChildren().add(myPanel3);
                    hbox.getChildren().add(vBox1);
                    A a = new A(size, myPanel1.getArray(), myPanel2.getArray(), myPanel3);
                    int temp=a.move();
                    if(temp==0){
                        System.out.println("初始状态已经是目标状态了，不需要移动！");
                        Alert alert=new Alert(Alert.AlertType.NONE,"初始状态已经是目标状态了，不需要移动！",new ButtonType[]{ButtonType.CLOSE});
                        alert.setTitle("提示");
                        alert.show();
                    }
                    else if(temp==1){
                        System.out.println("成功");
//                        Alert alert=new Alert(Alert.AlertType.NONE,"移动成功！",new ButtonType[]{ButtonType.CLOSE});
//                        alert.setTitle("提示");
//                        alert.show();
                    }
                    else if(temp==-1){
                        System.out.println("目标状态不可达");
                        Alert alert=new Alert(Alert.AlertType.NONE,"目标状态不可达",new ButtonType[]{ButtonType.CLOSE});
                        alert.setTitle("提示");
                        alert.show();
                    }
                }
            });
        });
    }
}


//网格布局
class MyPanel extends GridPane {

    TextField textFields[][];
    private int size;

    public MyPanel(int size) {
        this.size = size;
        this.setPadding(new Insets(3));
        textFields = new TextField[size][size];
        //设置网格布局
        //Setting the Grid alignment
        this.setAlignment(Pos.CENTER);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                textFields[i][j] = new TextField();
                textFields[i][j].setText((i * size + j) + "");
                textFields[i][j].setAlignment(Pos.CENTER);
                this.add(textFields[i][j], j, i);
            }
        }
    }

    public int[][] getArray() {
        int array[][] = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                array[i][j] = Integer.parseInt(textFields[i][j].getText());
            }
        }
        return array;
    }

    public void setArray(int array[][]) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                textFields[i][j].setText(array[i][j] + "");
            }
        }
    }
}


//网格布局
class MyPanelB extends AnchorPane {

    public Button buttons[][];
    public int size;
    public int eight[][];
    public int space[]={0,0};

    public MyPanelB(int size,int array[][]) {
        this.eight=array;
        this.size = size;
        this.setPadding(new Insets(3));
        buttons = new Button[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new Button(array[i][j] + "");
                buttons[i][j].setAlignment(Pos.CENTER);
                this.getChildren().add(buttons[i][j]);
                buttons[i][j].setLayoutX(j*50);
                buttons[i][j].setLayoutY(i*50);
                if(array[i][j]==0){
                    buttons[i][j].setVisible(false);
                    space[0]=i;
                    space[1]=j;
                }
            }
        }
    }

    public void show(Node temp) {
        SequentialTransition seq = new SequentialTransition();
        Button buttons1[][]=new Button[size][size];
        double layout[][][]=new double[size][size][2];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons1[i][j]=buttons[i][j];
                layout[i][j][0]=buttons[i][j].getLayoutX();
                layout[i][j][1]=buttons[i][j].getLayoutY();
            }
        }

        for (int i = 0; i < temp.step.length; i++) {
            //先进行移动
            System.out.println("空格：----------"+space[0]+"   "+space[1]);
            Button tempB=buttons[space[0] + temp.step[i][0]][space[1] + temp.step[i][1]];
            Button spaceB=buttons[space[0]][space[1]];

            TranslateTransition t2=new TranslateTransition(Duration.seconds(1),tempB);
            for (int i1 = 0; i1 < size; i1++) {
                for (int j1 = 0; j1 < size; j1++) {
                    if(buttons1[i1][j1].equals(tempB)){
                        t2.setToX(spaceB.getLayoutX()-layout[i1][j1][0]);
                        t2.setToY(spaceB.getLayoutY()-layout[i1][j1][1]);
                        break;
                    }
                }
            }

            System.out.println(spaceB.getLayoutX()+"   "+tempB.getLayoutX());
            System.out.println(spaceB.getLayoutY()+"   "+tempB.getLayoutY());
            seq.getChildren().add(t2);
            seq.getChildren().add(new PauseTransition(Duration.seconds(0.5)));

            buttons[space[0] + temp.step[i][0]][space[1] + temp.step[i][1]]=spaceB;
            double x1=spaceB.getLayoutX();
            double y1=spaceB.getScaleY();
            spaceB.setLayoutX(tempB.getLayoutX());
            spaceB.setLayoutY(tempB.getLayoutY());
            buttons[space[0]][space[1]]=tempB;
            tempB.setLayoutX(x1);
            tempB.setLayoutY(y1);

            //更新
            space[0] += temp.step[i][0];
            space[1] += temp.step[i][1];
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons1[i][j].setLayoutX(layout[i][j][0]);
                buttons1[i][j].setLayoutY(layout[i][j][1]);
            }
        }
        seq.play();
        System.out.println("成功啦");
    }

}