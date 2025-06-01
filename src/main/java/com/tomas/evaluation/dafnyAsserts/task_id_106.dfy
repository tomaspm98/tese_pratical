method AppendArrayToSeqTest(){

  var s1: seq<int> := [9, 10];
  var a1:= new int[] [5, 6, 7];
  var res1:=AppendArrayToSeq(s1,a1);
  expect res1==[9, 10, 5, 6, 7];

  var s2: seq<int> := [10, 11];
  var a2:= new int[] [6, 7, 8];
  var res2:=AppendArrayToSeq(s2,a2);
  expect res2==[10, 11, 6, 7, 8];

  var s3: seq<int> := [11, 12];
  var a3:= new int[] [7, 8, 9];
  var res3:=AppendArrayToSeq(s3,a3);
  expect res3==[11, 12, 7, 8, 9];

}