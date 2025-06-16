method MinLengthSublistTest(){
  var s1:seq<seq<int>> :=[[1],[1,2],[1,2,3]];
  var res1:=MinLengthSublist(s1);
  print(res1);print("\n");
              //expected [1]

  var s2:seq<seq<int>> :=[[1,1],[1,1,1],[1,2,7,8]];
  var res2:=MinLengthSublist(s2);
  print(res2);print("\n");
              //expected [1,1]

  var s3:seq<seq<int>> :=[[1,2,3],[3,4],[11,12,14]];
  var res3:=MinLengthSublist(s3);
  print(res3);print("\n");
              //expected [3,4]
}