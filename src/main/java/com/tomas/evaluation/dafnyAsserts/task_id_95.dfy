
method SmallestListLengthTest(){

  var s1:seq<seq<int>> :=[[1],[1,2]];
  var res1:=SmallestListLength(s1);
  expect res1==1;

  var s2:seq<seq<int>> :=[[1,2],[1,2,3],[1,2,3,4]];
  var res2:=SmallestListLength(s2);
  expect res2==2;

  var s3:seq<seq<int>> :=[[3,3,3],[4,4,4,4]];
  var res3:=SmallestListLength(s3);
  expect res3==3;


}