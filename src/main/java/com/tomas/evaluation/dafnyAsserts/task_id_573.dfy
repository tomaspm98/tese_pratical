method UniqueProductTest(){
  var a1:= new int[] [10, 20, 30, 40, 20, 50, 60, 40];
  var out1:=UniqueProduct(a1);
  expect out1==720000000;

  var a2:= new int[] [1, 2, 3, 1];
  var out2:=UniqueProduct(a2);
  expect out2==6;

  var a3:= new int[] [7, 8, 9, 0, 1, 1];
  var out3:=UniqueProduct(a3);
  expect out3==0;
  
}
