method SecondSmallestTest(){
  
  var a1:= new int[] [1, 2, -8, -2, 0, -2];
  var out1:=SecondSmallest(a1);
  expect out1 ==-2;

  var a2:= new int[] [2,2,1];
  var out2:=SecondSmallest(a2);
  expect out2==2;

  var a3:= new int[] [-2,-3,-1];
  var out3:=SecondSmallest(a3);
  expect out3==-2;


}

