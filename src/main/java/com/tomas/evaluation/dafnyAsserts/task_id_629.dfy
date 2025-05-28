method FindEvenNumbersTest(){
  var a1:= new int[] [1,2,3,4,5];
  var res1:=FindEvenNumbers(a1);
  expect res1==[2,4];

  var a2:= new int[] [4,5,6,7,8,0,1];
  var res2:=FindEvenNumbers(a2);
  expect res2==[4,6,8,0];

  var a3:= new int[] [8,12,15,19];
  var res3:=FindEvenNumbers(a3);
  expect res3==[8,12];

}


