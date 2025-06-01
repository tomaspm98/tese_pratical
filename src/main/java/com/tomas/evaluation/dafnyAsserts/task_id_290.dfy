method MaxLengthListTest(){

  var s1:seq<seq<int>> :=[[0], [1, 3], [5, 7], [9, 11], [13, 15, 17]];
  var e1: seq<int> := [13, 15, 17];
  var res1:=MaxLengthList(s1);
  print(res1);print("\n");
              //expected [13, 15, 17]

  var s2:seq<seq<int>> :=[[1], [5, 7], [10, 12, 14,15]];
  var e2: seq<int> := [10, 12, 14,15];
  var res2:=MaxLengthList(s2);
  print(res2);print("\n");
              //expected [10, 12, 14, 15]


  var s3:seq<seq<int>> :=[[5], [15,20,25]];
  var e3: seq<int> := [15,20,25];
  var res3:=MaxLengthList(s3);
  print(res3);print("\n");
              //expected [15, 20, 25]


}