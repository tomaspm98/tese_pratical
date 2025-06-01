method IsEvenAtIndexEvenTest(){

  var s1: seq<int> := [3,2,1];
  var res1:=IsEvenAtIndexEven(s1);
  expect res1==false;

  var s2: seq<int> := [1,2,3];
  var res2:=IsEvenAtIndexEven(s2);
  expect res2==false;

  var s3: seq<int> := [2,1,4];
  var res3:=IsEvenAtIndexEven(s3);
  expect res3==true;

}