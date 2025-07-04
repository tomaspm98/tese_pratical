method IndexWiseAdditionTest(){

  var s1:seq<seq<int>> :=[[1, 3], [4, 5], [2, 9], [1, 10]];
  var s2:seq<seq<int>> :=[[6, 7], [3, 9], [1, 1], [7, 3]];
  var res1:=IndexWiseAddition(s1,s2);
  print(res1);print("\n");
              //expected [[7, 10], [7, 14], [3, 10], [8, 13]]

  var s3:seq<seq<int>> :=[[2, 4], [5, 6], [3, 10], [2, 11]];
  var s4:seq<seq<int>> :=[[7, 8], [4, 10], [2, 2], [8, 4]];
  var res2:=IndexWiseAddition(s3,s4);
  print(res2);print("\n");
              //expected [[9, 12], [9, 16], [5, 12], [10, 15]]

  var s5:seq<seq<int>> :=[[3, 5], [6, 7], [4, 11], [3, 12]];
  var s6:seq<seq<int>> :=[[8, 9], [5, 11], [3, 3], [9, 5]];
  var res3:=IndexWiseAddition(s5,s6);
  print(res3);print("\n");
              //expected [[11, 14], [11, 18], [7, 14], [12, 17]]

}