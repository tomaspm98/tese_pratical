method PairwiseAdditionTest(){
  var s1:= new int[] [1, 5, 7, 8, 10];
  var e1: seq<int> := [6, 12, 15, 18];
  var res1:=PairwiseAddition(s1);
  print(res1);print("\n");



  var s2:= new int[] [2, 6, 8, 9, 11];
  var e2: seq<int> := [8, 14, 17, 20];
  var res2:=PairwiseAddition(s2);
  print(res1);print("\n");


  var s3:= new int[][3, 7, 9, 10, 12];
  var e3: seq<int> := [10, 16, 19, 22];
  var res3:=PairwiseAddition(s3);
  print(res1);print("\n");



}