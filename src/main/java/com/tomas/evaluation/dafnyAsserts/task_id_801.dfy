method CountEqualNumbersTest(){

  var res1:=CountEqualNumbers(1,1,1);
  expect res1==3;

  var res2:=CountEqualNumbers(-1,-2,-3);
  expect res2==1;

  var res3:=CountEqualNumbers(-1,-2,-3);
  expect res3==1;

}