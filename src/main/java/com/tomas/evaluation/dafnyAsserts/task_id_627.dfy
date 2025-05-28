method SmallestMissingNumberTest(){
  var a1:seq<int>:= [0,1,2,3];
  var out1:=SmallestMissingNumber(a1);
  expect out1==4;

  var a2:seq<int>:= [0,1,2,6,9];
  var out2:=SmallestMissingNumber(a2);
  expect out2==3;

  var a3:seq<int>:= [2,3,5,8,9];
  var out3:=SmallestMissingNumber(a3);
  expect out3==0;

}

