method IsIntegerTest(){

  var res1:=IsInteger("python");
  expect res1==false;

  var res2:=IsInteger("1");
  expect res2==true;

  var res3:=IsInteger("12345");
  expect res3==true;

}