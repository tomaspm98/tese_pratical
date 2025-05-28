method IsOddAtIndexOddTest(){
  
  var a1:= new int[] [2,1,4,3,6,7,6,3];
  var out1:=IsOddAtIndexOdd(a1);
  expect out1==true;

  var a2:= new int[] [4,1,2];
  var out2:=IsOddAtIndexOdd(a2);
  expect out2==true;

  var a3:= new int[] [1,2,3];
  var out3:=IsOddAtIndexOdd(a3);
  expect out3==false;

}

