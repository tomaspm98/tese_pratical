method ElementWiseModuloTest(){
  var a1:= new int[] [10, 4, 5, 6];
  var a2:= new int[] [5, 6, 7, 5];
  var res1:=ElementWiseModulo(a1,a2);
  var exp1:= new int[] [0, 4, 5, 1];
  var out1:=assertArrayEquals(res1,exp1);
  expect out1==true;

  var a3:= new int[] [11, 5, 6, 7];
  var a4:= new int[] [6, 7, 8, 6];
  var res2:=ElementWiseModulo(a3,a4);
  var exp2:= new int[] [5, 5, 6, 1];
  var out2:=assertArrayEquals(res2,exp2);
  expect out2==true;

  var a5:= new int[] [12, 6, 7, 8];
  var a6:= new int[] [7, 8, 9, 7];
  var res3:=ElementWiseModulo(a5,a6);
  var exp3:= new int[]  [5, 6, 7, 1];
  var out3:=assertArrayEquals(res3,exp3);
  expect out3==true;

}


