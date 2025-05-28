method CubeElementsTest(){
  var a1:= new int[] [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  var res1:=CubeElements(a1);
  var exp1:= new int[][1, 8, 27, 64, 125, 216, 343, 512, 729, 1000];
  var out1:=assertArrayEquals(exp1,res1);
  expect out1==true;

  var a2:= new int[] [10,20,30];
  var res2:=CubeElements(a2);
  var exp2:= new int[][1000, 8000, 27000];
  var out2:=assertArrayEquals(exp2,res2);
  expect out1==true;

  var a3:= new int[] [12,15];
  var res3:=CubeElements(a3);
  var exp3:= new int[][1728, 3375];
  var out3:=assertArrayEquals(exp3,res3);
  expect out1==true;


}

