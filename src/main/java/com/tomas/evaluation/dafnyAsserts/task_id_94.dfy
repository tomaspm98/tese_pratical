method MinSecondValueFirstTest(){

  var s1:array<seq<int>> := new seq<int>[] [[1, 3], [5, 7], [9, 11], [13, 15]];
  var res1:=MinSecondValueFirst(s1);
  expect res1==1;

  var s2:array<seq<int>> := new seq<int>[][[1, 4], [5, 1], [9, 11], [13, 15]];
  var res2:=MinSecondValueFirst(s2);
  expect res2==5;

  var s3:array<seq<int>> := new seq<int>[][[1, 4], [5, 1], [9, -1], [13, 15]];
  var res3:=MinSecondValueFirst(s3);
  expect res3==9;

}