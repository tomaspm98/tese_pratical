method GetFirstElementsTest(){
  var s1:seq<seq<int>> :=[[1, 2], [3, 4, 5], [6, 7, 8, 9]];
  var res1:=GetFirstElements(s1);
  print(res1);print("\n");
              //expected [1, 3, 6]

  var s2:seq<seq<int>> :=[[1,2,3],[4, 5]];
  var res2:=GetFirstElements(s2);
  print(res2);print("\n");
              //expected [1,4]

  var s3:seq<seq<int>> :=[[9,8,1],[1,2]];
  var res3:=GetFirstElements(s3);
  print(res3);print("\n");
              //expected [9,1]


}