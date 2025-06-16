method IsSmallerTest(){

  var s1: seq<int> := [1, 2, 3];
  var s2: seq<int> := [2, 3, 4];
  var res1:=IsSmaller(s1,s2);
  expect res1==false;

  var s3: seq<int> := [4, 5, 6];
  var s4: seq<int> := [3, 4, 5];
  var res2:=IsSmaller(s3,s4);
  expect res2==true;

  var s5: seq<int> := [11, 12, 13];
  var s6: seq<int> := [10, 11, 12];
  var res3:=IsSmaller(s5,s6);
  expect res3==true;

}