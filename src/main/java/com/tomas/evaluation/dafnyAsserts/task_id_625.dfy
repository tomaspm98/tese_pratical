method SwapFirstAndLastTest(){

  var a1:= new int[] [1,2,3];
  SwapFirstAndLast(a1);
  var exp1:= new int[][3,2,1];
  var out1:=assertArrayEquals(exp1,a1);
  expect out1==true;

  var a2:= new int[] [1,2,3,4,4];
  SwapFirstAndLast(a2);
  var exp2:= new int[][4,2,3,4,1];
  var out2:=assertArrayEquals(exp2,a2);
  expect out2==true;

  var a3:= new int[] [4,5,6];
  SwapFirstAndLast(a3);
  var exp3:= new int[] [6,5,4];
  var out3:=assertArrayEquals(exp3,a3);
  expect out3==true;


}

