method MaxDifferenceTest(){
  var a1:= new int[] [2,1,5,3];
  var out1:=MaxDifference(a1);
  expect out1==4;

  var a2:= new int[] [9,3,2,5,1];
  var out2:=MaxDifference(a2);
  expect out2==8;

  var a3:= new int[] [3,2,1];
  var out3:=MaxDifference(a3);
  expect out3==2;

}