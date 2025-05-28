method FirstEvenOddDifferenceTest(){
  var a1:= new int[] [1,3,5,7,4,1,6,8];
  var out1:=FirstEvenOddDifference(a1);
  expect out1==3;
  
  var a2:= new int[] [1,2,3,4,5,6,7,8,9,10];
  var out2:=FirstEvenOddDifference(a2);
  expect out2==1;

  var a3:= new int[] [1,5,7,9,10];
  var out3:=FirstEvenOddDifference(a3);
  expect out3==9;

}

