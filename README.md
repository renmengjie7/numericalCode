# 24数码的A*解决
## 环境
JDK8

IntelliJ IDEA
## 功能
1、通过A*算法求解二十四数码问题

2、实现可视化界面、能够动画显示解题过程

3、实现在状态不可达和目标状态初始状态相同时进行提示，增强健壮性
## 技术路线
### 状态不可达的判断
可以计算初始状态与目标状态两个有序数列的逆序值，如果两个数都是偶数或者奇数，则可以通过变换到达，否则这两个状态就不可达。
### A*
1.使用一个二维数组来记录可行的移动方式，即{{0,1},{0,-1},{1,0},{-1,0}}

2.PT表中节点的结构可以记录为（当前八数码状态数组，移动的步骤数组，down）

3.使用JAVA自带的ArrayList来创建PT表，基于ArrayList，有造了几个函数。isMin来判断是不是最小的，getMin来获得down最小的节点，delete来删除down大于up的节点（循环判断，在其中调用了remove函数）

![M6U4}P1`6 7$(F3I~KU~8)7](https://user-images.githubusercontent.com/70495062/126855797-3e10c253-7a8d-4771-8c55-770943eb7fbe.png)
![(ZLLQ0 $22NKZ${(UV5YUQ](https://user-images.githubusercontent.com/70495062/126855800-d6f315fd-9dfd-45eb-ba7f-e06a5e2e41b3.png)



## 实验结果
![image](https://user-images.githubusercontent.com/70495062/126855735-51d99eec-9760-4e76-9bb9-25153730ded8.png)
![image](https://user-images.githubusercontent.com/70495062/126855736-f44d9c97-5a8e-461c-bd77-bd08ee6d0889.png)
