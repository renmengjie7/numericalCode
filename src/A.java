import java.util.ArrayList;
import java.util.List;

//A算法，启发式搜索【输入是局面，输出是每一步怎么走】
public class A {

    MyPanelB myPanel;
    int length=0;
    private int space[] = {0, 0};
    //步子
    private int stepKind[][]={
            {0,1},
            {0,-1},
            {1,0},
            {-1,0}
    };

    List<Node> PT = new ArrayList<>();
    int up=100,down=0;

    //记录初始状态
    private int[][] eight;
    //记录当前的状态
    private int[][] state;
    //目标状态
    private int[][] object;
    private int[] spaceInitail={0,0};

    //初始化
    public A(int length,int state[][],int object[][],MyPanelB myPanel){
        this.length=length;
        //记录初始状态
        eight = new int[length][length];
        //记录当前的状态
        this.state = new int[length][length];
        //目标状态
        this.object = new int[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                this.object[i][j] = object[i][j];
            }
        }
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                this.state[i][j] = state[i][j];
                this.eight[i][j] = state[i][j];
                if (this.state[i][j] == 0) {
                    //空格坐标的记录
                    this.space[0] = i;
                    this.space[1] = j;
                    this.spaceInitail[0]=i;
                    this.spaceInitail[1]=j;
                }
            }
        }
        this.myPanel=myPanel;
    }

    private int maxFail=1000;

    //若两个状态的逆序奇偶性 相同，则可相互到达，否则不可相互到达。（是否可达的判断）
    public boolean hasAnswer(){
        int temp1=ReserveCount.getReverseCount(eight);
        int temp2=ReserveCount.getReverseCount(object);
        if(temp1%2==0&&temp2%2==0){
            return true;
        }
        else if(temp1%2==1&&temp2%2==1){
            return true;
        }
        else {
            return false;
        }
    }


    //八数码进行移动【空格上下左右都能移动】
    public int move() {
        //判断是否有解
        if (!hasAnswer()){
            return -1;
        }

        if(diff(state)==0){
            return 0;
        }

        //深度
        int depth = 0;
        int cost;
        int step[][]={{0,0}};
        Node temp;
        int[][] stateNext=new int[length][length];

        while (true){
            //找到可移动的，进行移动【上下左右，宽度优先遍历】
            for(int i=0;i<4;i++){
                //判断是否合法
                if(0<=(space[0]+stepKind[i][0])&&(space[0]+stepKind[i][0])<length
                        &&0<=(space[1]+stepKind[i][1])&&(space[1]+stepKind[i][1])<length){
                    //如果是原来的样貌，可以删除了
                    step[step.length-1][0]=stepKind[i][0];
                    step[step.length-1][1]=stepKind[i][1];
                    for(int p=0;p<length;p++){
                        for(int q=0;q<length;q++){
                            stateNext[p][q]=state[p][q];
                        }
                    }
                    //移动
                    stateNext[space[0]][space[1]]=stateNext[space[0]+stepKind[i][0]][space[1]+stepKind[i][1]];
                    stateNext[space[0]+stepKind[i][0]][space[1]+stepKind[i][1]]=0;
                    //加入接点
                    cost=diff(stateNext)+depth+1;
                    //没有越界
                    if(cost<up){
                        PT.add(new Node(stateNext,cost,step));
                        //叶子结点
                        if(cost==depth+1){
                            delete();
                        }
                    }
                }
            }

            //如果已经全部都符合了
            //找到代价最小的节点
            temp=getMin();
            depth=temp.step.length;
            if(temp.cost>=maxFail+length){
                return -1;
            }
            //叶子结点
            if(depth==temp.cost){
                print(temp);
                return 1;
            }
            step=new int[depth+1][2];
            for(int i=0;i<depth;i++){
                step[i][0]=temp.step[i][0];
                step[i][1]=temp.step[i][1];
            }

            for(int i=0;i<length;i++){
                for(int j=0;j<length;j++){
                    state[i][j]=temp.state[i][j];
                    if(state[i][j]==0){
                        space[0]=i;
                        space[1]=j;
                    }
                }
            }
        }
    }



    public void delete(){
        //刚放入了叶子结点，进行剪枝
        Node temp=PT.get(PT.size()-1);
        for(int i=0;i<PT.size()-1;i++){
            if(PT.get(i).cost>=temp.cost){
                PT.remove(i);
            }
        }
    }

    //打印结果
    public void print(Node temp){
        System.out.println("最优的解决方案需要"+temp.step.length+"步\n移动如下：");
        //每一步
        myPanel.show(temp);
    }

    //评估函数（计算有多少个不同）
    public int diff(int[][] state){
        int diff = 0;
        //比较有多少位不同
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (state[i][j] != object[i][j]) {
                    diff++;
                }
            }
        }
        //空白处不同不计数
        if(state[spaceInitail[0]][spaceInitail[1]]!=object[spaceInitail[0]][spaceInitail[1]]){
            diff--;
        }
        return diff;
    }

    //获得当前最小代价节点
    public Node getMin(){
        Node node=null;
        int cost=-1;
        for(int i=0;i<PT.size();i++){
            if(cost==-1||cost>PT.get(i).cost){
                node=PT.get(i);
                cost=node.cost;
            }
        }
        PT.remove(node);
        return node;
    }


}



//PT表中的一个节点
class Node {

    //记录前面的【暂时先记着吧】
    int step[][];

    int state[][];
    int cost = 0;

    //新建一个节点
    public Node(int[][] state, int cost,int[][] step) {
        this.state = new int[state.length][state.length];
        this.step=new int[step.length][2];
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                this.state[i][j] = state[i][j];
            }
        }
        for(int i=0;i<step.length;i++){
            this.step[i][0]=step[i][0];
            this.step[i][1]=step[i][1];
        }
        this.cost = cost;
    }

}