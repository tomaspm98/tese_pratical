method ContainsSequenceTest(){
  var s1: seq<seq<int>> := [[2,4,3,5,7],[3,8]];
  var s2: seq<int> := [3,7];
  var res1:=ContainsSequence(s1,s2);
  expect res1==false;

  var s3: seq<seq<int>> := [[2,4,3,5,7],[4,3]];
  var s4: seq<int> := [4,3];
  var res2:=ContainsSequence(s3,s4);
  expect res2==true;

  var s5: seq<seq<int>> := [[2,4,3,5,7],[1,0]];
  var s6: seq<int> := [1,6];
  var res3:=ContainsSequence(s5,s6);
  expect res3==false;

}