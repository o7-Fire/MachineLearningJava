a neural network that imitate a computational thing\
score: lower better accuracy: higher better
![](https://cdn.discordapp.com/attachments/809580979777568828/811182724114284594/unknown.png)
![](https://cdn.discordapp.com/attachments/713346278003572777/811093717008121926/unknown.png)
![](https://cdn.discordapp.com/attachments/801061985034305556/811188188676096000/unknown.png)

```mermaid
>eval


========================Evaluation Metrics========================
 # of classes:    2
 Accuracy:        0.7400
 Precision:       0.0000
 Recall:          0.0000
 F1 Score:        0.0000
Precision, recall & F1: reported for positive class (class 1 - "1") only

Warning: 1 class was never predicted by the model and was excluded from average precision
Classes excluded from average precision: [1]

=========================Confusion Matrix=========================
  0  1
-------
 37  0 | 0 = 0
 13  0 | 1 = 1

Confusion matrix format: Actual (rowClass) predicted as (columnClass) N times
==================================================================
>test
Temperature, Humidity, Prefer to use jacket (float)
[[            -38,             16,    0.710907042]]
Output:
[[      0.6892066,     0.31079337]]
Desired Output:
[[              1,              0]]
Should Use jacket: Yes
NeuralNet Answer: Yes
Score: 0.3722141981124878
>score
0.6492486409842968
>min
0.5786642402410507
>train
Accuracy Before: 0.66
Accuracy After: 0.66
>score
0.6476687848567962
>min
0.5786642402410507
>
```