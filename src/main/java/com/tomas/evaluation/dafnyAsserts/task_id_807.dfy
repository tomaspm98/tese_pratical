method FindFirstOddTest(){

  var a1:= new int[] [1,3,5];
  var found1,out1:=FindFirstOdd(a1);
  expect a1[out1]==1;

  var a2:= new int[] [2,4,1,3];
  var found2,out2:=FindFirstOdd(a2);
  expect a2[out2]==1;

  var a3:= new int[] [8,9,1];
  var found3,out3:=FindFirstOdd(a3);
  expect a3[out3]==9;


}

